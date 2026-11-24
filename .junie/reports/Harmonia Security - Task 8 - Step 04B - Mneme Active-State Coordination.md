<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Harmonia Security - Task 8 - Step 04B - Mneme Active-State Coordination

## Executive Summary & System Utility

To maximize platform throughput, prevent redundant distributed computation, and eliminate race conditions across the Harmonia Health Integration Environment (HIE), Task 08 Step 08.04B implements the smallest production capability in **Mneme** (Infinispan / Hot Rod) required to support distributed active-state coordination beneath the governed-write contract established in Step 08.04A.

By encapsulating native Hot Rod optimistic concurrency primitives (`replaceWithVersion`) into a dedicated, non-authoritative active-state coordinator without introducing resource caching complexity, application version arithmetic, or process-local fallbacks, this implementation delivers substantial systemic utility:
1. **Early Duplicate Rejection**: Distributed participants competing from the same observed active state are resolved at the in-memory tier before invoking expensive downstream workflow activities or database transactions, yielding at most one winner (and exactly one winner under healthy cluster conditions).
2. **Clinical Storage Isolation**: Coordination state resides exclusively in an isolated, non-persistent cache (`active-coordination-cache`), preventing transient coordination markers from polluting clinical databases.
3. **Misuse-Resistant Opacity**: Callers observe strictly opaque tokens (`ActiveStateToken`) with zero numeric accessors or sequence arithmetic, ensuring that distributed coordination invariants cannot be subverted by caller-side version assumptions.
4. **Transparent Distributed Fail-Fast Semantics**: The coordinator guarantees visible failure (`UNAVAILABLE`) upon cluster unreachability rather than masking network partitions through unsafe JVM-local locks or maps.

---

## Terminology Correction & Opacity Boundary

### Vocabulary Correction: `ActiveCoordinationToken` $\rightarrow$ `ActiveStateToken`
In Step 08.04A, the coordination token was initially named `ActiveCoordinationToken`. Step 08.04B completes a repository-wide rename to `ActiveStateToken` across all production Java code, unit tests, architecture tests, and design specifications.
- **Rationale**: The token strictly identifies *what was observed* (the opaque identity of the Mneme active state associated with a resource observation), rather than prescribing subsequent actions.

### Token Opacity & Internal Bridge Boundary
To guarantee that application callers (Pylai gateways, Energeia workflows, Iris presentation services) treat active state tokens as purely opaque values:
- `ActiveStateToken` has a **package-private** constructor and internal accessor (`long internalVersion()`).
- It exposes **zero** public constructors, no public static factory methods accepting raw/primitive values, no numeric/string unwrap getters (`longValue()`, `intValue()`), does not implement `Comparable`, and unconditionally masks its representation in `toString()` (`ActiveStateToken[opaque]`).
- `ActiveStateTokenBridge` resides in the same package (`net.fhirfactory.harmonia.model.governedwrite`) and provides infrastructure-only factory (`create(long)`) and extractor (`extractVersion(ActiveStateToken)`) methods.
- **ArchUnit Enforcement**: Architecture rules strictly forbid classes outside `net.fhirfactory.harmonia.hestia.mneme..` (and the `model.governedwrite` package itself) from accessing `ActiveStateTokenBridge`.

```
+-------------------------------------------------------------+
|                     CALLER WORKFLOWS                        |
|  (Pylai, Energeia, Iris - see ActiveStateToken as Opaque)   |
+------------------------------+------------------------------+
                               |
                               | ActiveStateToken (Opaque)
                               v
+-------------------------------------------------------------+
|                 ActiveStateTokenBridge                      |
| (create / extractVersion restricted strictly to Mneme)      |
+------------------------------+------------------------------+
                               |
                               | long entryVersion (Hot Rod CAS)
                               v
+-------------------------------------------------------------+
|                 HotRodActiveStateCoordinator                |
|             (hestia :: mneme-cluster / Hot Rod)             |
+-------------------------------------------------------------+
```

---

## Production Types & Module Structure

### 1. `calliope` (`net.fhirfactory.harmonia.model.governedwrite`)
- `ActiveStateToken.java`: Immutable, opaque value object representing observed cache entry state.
- `ActiveStateTokenBridge.java`: Infrastructure bridge for Mneme token construction and version extraction.
- `ActiveStateCoordinator.java`: Caller-facing coordination interface declaring `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
- `ActiveStateCoordinationResult.java`: Outcome enum representing CAS progression results (`CONSUMED`, `STALE`, `UNAVAILABLE`).
- `GovernedRead.java`, `GovernedWriter.java`, `WriteResult.java`, `ActiveStateConflict.java`: Updated to reference `ActiveStateToken`.

### 2. `hestia:mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.coordination`)
- `HotRodActiveStateCoordinator.java`: Production implementation of `ActiveStateCoordinator` using Hot Rod `RemoteCache` operations against `active-coordination-cache`.
- `ActiveCoordinationUnavailableException.java`: Runtime exception thrown by `observe` when the Infinispan cluster or Hot Rod transport is unavailable.
- `infinispan.xml`: Added non-persistent `active-coordination-cache` definition (`mode="SYNC"`, statistics enabled, zero `<persistence>` stores).

### 3. `paradeigma:paradeigma-test` (`net.fhirfactory.harmonia.paradeigma.test.arch`)
- `GovernedWriteContractArchitectureTest.java`: ArchUnit rules enforcing contract purity, bridge access restriction to Mneme, zero DELETE methods, and zero process-local fallbacks (`ConcurrentHashMap`, `AtomicReference`, `ReentrantLock`) in coordination classes.

---

## Coordination-State Storage & Infinispan Primitives

### Dedicated Non-Persistent Storage
Coordination state lives exclusively in `active-coordination-cache`:
- **Clustering Mode**: `REPL_SYNC` (synchronous replication across cluster nodes).
- **In-Memory Only**: Strictly **zero** `<persistence>` stores configured, preventing coordination markers from writing through or behind to Mnemosyne or PostgreSQL.
- **Boring Coordination Value**: Cache entries contain the static literal `"ACTIVE"`. No application-level UUIDs, counters, generation arithmetic, or timestamps are stored.

### Exact Infinispan Primitives Used
`HotRodActiveStateCoordinator` interacts with Infinispan Hot Rod using four specific primitives:
1. `RemoteCache.getWithMetadata(cacheKey)`: Retrieves `MetadataValue<String>` containing the cached marker and the 64-bit cluster version.
2. `MetadataValue.getVersion()`: Obtains the native Hot Rod entry version for token encapsulation.
3. `RemoteCache.putIfAbsent(cacheKey, "ACTIVE")`: Atomically initializes the coordination entry when observed for the first time.
4. `RemoteCache.replaceWithVersion(cacheKey, "ACTIVE", version)`: Executes atomic compare-and-swap (CAS). Returns `true` if the version matched and advanced (mapped to `CONSUMED`); returns `false` if the version was mismatched or superseded (mapped to `STALE`).

---

## Production Java API Example

```java
package net.fhirfactory.harmonia.example;

import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskCoordinationService {

    private static final Logger log = LoggerFactory.getLogger(TaskCoordinationService.class);
    private final ActiveStateCoordinator coordinator;

    public TaskCoordinationService(ActiveStateCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public boolean tryClaimTaskExecution(ResourceKey taskKey) {
        // 1. Observe the current active state token from Mneme
        ActiveStateToken observedToken = coordinator.observe(taskKey);

        // 2. Attempt atomic CAS consumption
        ActiveStateCoordinationResult result = coordinator.consume(taskKey, observedToken);

        return switch (result) {
            case CONSUMED -> {
                log.info("Successfully claimed active state execution for {}", taskKey);
                yield true; // Winner: proceed with task execution or durable persistence
            }
            case STALE -> {
                log.warn("Active state claim rejected as STALE for {} (lost race)", taskKey);
                yield false; // Loser: abort duplicate execution
            }
            case UNAVAILABLE -> {
                log.error("Coordination cluster UNAVAILABLE for {}; aborting safely", taskKey);
                yield false; // Fail-fast: no silent local degradation
            }
        };
    }
}
```

---

## Verification & Test Evidence

### Laboratory & Clustered Scenario Tests (`hestia:mneme-cluster`)
The multi-node test harness (`InfinispanLaboratoryServer`) instantiates 2 clustered Infinispan nodes (`REPL_SYNC`) and 3 distinct Hot Rod clients (Client-A, Client-B, Client-C). The clustered scenario test suite `InfinispanActiveStateCoordinatorScenarioTest` verifies:
- **Scenario 1 — Active-State Observation**: `observe(key)` lazily creates the `"ACTIVE"` marker in `active-coordination-cache` and returns an `ActiveStateToken` that is consistently observed across cluster nodes.
- **Scenario 2 — Single Participant Atomic Consumption**: `consume(key, token)` succeeds with `CONSUMED` and atomically advances the entry version, yielding a distinct next-generation token on subsequent observation.
- **Scenario 3 — Duplicate Token Reuse Rejection**: Once consumed, repeated consumption attempts using the same token yield `STALE`.
- **Scenario 4 — Stale Token Rejection Across Participants**: When Participant-A consumes an observed baseline token, Participant-B attempting consumption with the baseline token receives `STALE`.
- **Scenario 5 — Competing CAS Attempts (At-Most-One Winner)**: Verified that across competing CAS attempts sharing the same observed baseline token, exactly one participant receives `CONSUMED` and the remaining participants receive `STALE`.
- **Scenario 6 — Visible Failure on Unavailability (No Local Fallback)**: When the server node is stopped, `consume` returns `UNAVAILABLE` and `observe` throws `ActiveCoordinationUnavailableException` without falling back to JVM locks or in-memory maps.
- **Scenario 7 — Non-Authoritative Coordination Storage Isolation**: Active-state operations affect only `active-coordination-cache`; clinical caches (`person-cache`, `task-cache`, `practitioner-cache`) remain completely untouched.
- **Scenario 8 — Participant Failure Invariance**: When a participant consumes a token and crashes before downstream persistence, the token remains consumed in the cluster and cannot be re-consumed by other participants.
- **Scenario 9 — Opaque State Progression**: Active-state progression operates entirely via opaque Hot Rod entry metadata versions without application version counters or sequence arithmetic.

### Architecture Guardrails (`paradeigma:paradeigma-test`)
`GovernedWriteContractArchitectureTest` verifies:
- **Contract Purity**: Zero Infinispan, JPA/Hibernate, or HTTP framework dependencies in `net.fhirfactory.harmonia.model.governedwrite..`.
- **Zero DELETE Primitive**: No `delete*` or `remove*` methods declared on `GovernedWriter`.
- **Strict Allowlist for `ActiveStateTokenBridge`**: Enforces that only `net.fhirfactory.harmonia.hestia.mneme..` (and the `model.governedwrite` package itself) can access `ActiveStateTokenBridge`.
- **No Process-Local Fallback**: Classes in `net.fhirfactory.harmonia.hestia.mneme.coordination..` must not depend on `java.util.concurrent.ConcurrentHashMap`, `java.util.concurrent.atomic..`, or `java.util.concurrent.locks..`.

---

## Tests Executed vs. Not Executed

### Executed Suites

| Suite / Command | Scope | Result | Execution Time |
| :--- | :--- | :--- | :--- |
| `mvn test -pl calliope` | Calliope domain models, governed write contracts, token opacity invariants (`GovernedWriteContractTest`). | **PASS** (136 tests, 0 failures, 0 errors) | ~0.4s |
| `mvn test -pl hestia/mneme-cluster -am -Dtest=HotRodActiveStateCoordinatorTest,InfinispanActiveStateCoordinatorScenarioTest,InfinispanClusterConfigTest,MnemeConcurrencyAndVersionScenarioTest -Dsurefire.failIfNoSpecifiedTests=false` | Mneme coordination unit tests, clustered multi-node scenario tests, cluster configuration tests, laboratory concurrency tests. | **PASS** (25 tests, 0 failures, 0 errors) | ~9.5s |
| `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` | Repository-wide ArchUnit architecture test suite across all 35 reactor modules. | **PASS** (64 tests, 0 failures, 0 errors) | ~3.1s |

### Not Executed
- **Full Docker Multi-Container Integration Tests**: Deployment-level tests involving external PostgreSQL containers, WildFly runtime servers, and Synapse/Matrix containers were not executed.
  - *Reason*: Step 08.04B scope is strictly bounded to the Calliope contract layer and the Mneme clustered Hot Rod library. Full container deployments are tested during end-to-end milestone verification.

---

## Explicit Architectural Confirmations

1. **No Authoritative Persistence Implemented**: Authoritative persistence in PostgreSQL/JPA (`MnemosynePersistencePort`, conditional SQL updates) was not implemented and remains strictly scheduled for Step 08.04C.
2. **No Production Write Path Migrated**: Existing write paths in Pylai, Energeia, and Iris were not modified or migrated; production pipeline migration is deferred to Step 08.04D and subsequent tasks.
3. **No New Subsystem or Maven Module Introduced**: All code and configuration changes were integrated strictly within existing modules (`calliope`, `hestia/mneme-cluster`, `paradeigma/paradeigma-test`).

---

## Conclusion & Next Step Readiness

Step 08.04B successfully delivers a robust, lean, and misuse-resistant distributed active-state coordination capability in Mneme. The platform is now fully prepared for **Step 08.04C**, which will implement Mnemosyne authoritative conditional persistence and SQL adapters beneath the governed-write contract.
