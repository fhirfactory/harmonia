# Governed Write and Concurrency Contract `[FOUNDATIONAL CONTRACT & MNEME ACTIVE-STATE COORDINATION IMPLEMENTED (08.04A/08.04B)]`

This document is the authoritative engineering and architectural specification for **Harmonia's Governed Write and Concurrency Contract**. It formalises the Strong Hybrid persistence and concurrency architecture across the platform, establishing strict contracts between caller workflows (Pylai, Energeia Ponos/Erga/Praxis, Iris BEFE) and the storage subsystems: **Mneme** (distributed in-memory cache and active coordination grid) and **Mnemosyne** (authoritative relational JPA persistence).

> **Implementation Note (Task 08 Steps 08.04A & 08.04B)**: The foundational Java contract layer (`ActiveStateToken`, `ActiveStateTokenBridge`, `ActiveStateCoordinator`, `ActiveStateCoordinationResult`, `GovernedRead`, `GovernedWriter`, `WriteResult`) is implemented in `calliope` under `net.fhirfactory.harmonia.model.governedwrite`. Runtime distributed active-state coordination (`HotRodActiveStateCoordinator` backed by `active-coordination-cache` with fixed marker CAS) is implemented in `hestia/mneme-cluster`. Authoritative conditional persistence (Mnemosyne SQL adapters) and full `GovernedWriter` pipeline integration are scheduled for subsequent steps (08.04C / 08.04D).

---

## 1. Purpose

The purpose of this contract is to:
1. Establish a single, unambiguous write and concurrency architecture across all Harmonia subprojects.
2. Eliminate concurrency anomalies, lost updates, and stale reads in a distributed, multi-node healthcare environment.
3. Define the precise separation of responsibilities between in-memory distributed coordination (Mneme) and durable relational persistence (Mnemosyne).
4. Provide idiomatic, strongly typed Java contract interfaces and data models for read, create, update, and convergence operations.
5. Define a rigorous 4-domain version model and a mutually exclusive conflict taxonomy.
6. Provide an actionable contract test plan and catalog bypass vulnerabilities for subsequent implementation phases.

---

## 2. Architectural Context

Harmonia operates as a healthcare-grade integration and interoperability platform where data integrity, longitudinal clinical accuracy, and high-throughput low-latency processing are paramount. The persistence and concurrency model is governed by three foundational Architecture Decision Records:

```
+-----------------------------------------------------------------------------------+
|                                HARMONIA ECOSYSTEM                                 |
|                                                                                   |
|   +-------------------+      +-------------------+      +---------------------+   |
|   |    Pylai Ingress  |      |   Energeia Ergon  |      |   Iris BEFE Gateway |   |
|   |  (REST / MLLP)    |      | (Workflow Engine) |      |   (Presentation)    |   |
|   +---------+---------+      +---------+---------+      +----------+----------+   |
|             \                          |                           /              |
|              \                         |                          /               |
|               v                        v                         v                |
|             +------------------------------------------------------+              |
|             |                GovernedWriter Facade                 |              |
|             |      (Active Coordination + Authoritative Commit)    |              |
|             +--------------------------+---------------------------+              |
|                                        |                                          |
|                    +-------------------+-------------------+                      |
|                    |                                       |                      |
|                    v                                       v                      |
|         +---------------------+                 +---------------------+           |
|         |    Mneme Grid       |                 |     Mnemosyne       |           |
|         |  (Infinispan 15)    |                 | (PostgreSQL 16 JPA) |           |
|         |                     |                 |                     |           |
|         | * Active Coord.     |                 | * Authoritative     |           |
|         | * Opaque CAS Tokens |                 |   Durable Commit    |           |
|         | * Read-Dominant     |                 | * Monotonic Version |           |
|         | * Non-Authoritative |                 | * Provenance & Audit|           |
|         +---------------------+                 +---------------------+           |
+-----------------------------------------------------------------------------------+
```

### 2.1 Strong Hybrid Model
Harmonia rejects both pure distributed database architectures (which suffer from high write latencies and complex cross-datacenter locking) and naive cache-aside patterns (which suffer from race conditions and stale overwrites). Instead, Harmonia implements a **Strong Hybrid Model**:
- **Active Distributed Coordination**: Distributed in-memory caching absorbs heavy read traffic (approximately 10:1 read-to-write ratio) and coordinates in-flight concurrency across cluster nodes using compare-and-swap (CAS) primitives.
- **Authoritative Durable State**: A relational transactional database acts as the single source of durable truth and guarantees ACID commit, monotonic version progression, and longitudinal history.

### 2.2 ADR-018: Mnemosyne Authoritative Boundary
Under [ADR-018](../architecture-decisions.md#adr-018-----mnemosyne-defines-the-authoritative-durable-state-boundary):
- Mnemosyne (PostgreSQL / HAPI FHIR JPA) defines the authoritative durable application-state boundary.
- An entry's presence in or replication across Mneme (Infinispan) does **not** constitute durable acceptance.
- A successful Mneme cache operation is non-authoritative; application state is durably accepted only upon successful commit to Mnemosyne or upon transfer across a durable Petasos message boundary.

### 2.3 ADR-019: Mneme Distributed Access and Coordination
Under [ADR-019](../architecture-decisions.md#adr-019--mneme-owns-distributed-resource-access-and-coordination):
- Mneme owns distributed resource access, low-latency availability, and concurrency coordination for active working sets.
- Mneme state is reconstructable from Mnemosyne upon cache miss, eviction, or node restart.
- Core tenet: *Mneme coordinates active distributed state; Mnemosyne commits authoritative durable state.*

### 2.4 ADR-020: Lifecycle State Transitions vs Physical Deletion
Under [ADR-020](../architecture-decisions.md#adr-020--governed-information-uses-lifecycle-state-rather-than-physical-deletion):
- Harmonia does not provide physical `DELETE` semantics for governed clinical or operational resources.
- Logical removal, deactivation, cancellation, or error-marking is a domain-significant lifecycle transition executed strictly as a concurrency-controlled authoritative `UPDATE`.
- Cache eviction in Mneme is an operational memory management action and must never be interpreted as resource deletion.

---

## 3. Core Architectural Invariants

The governed write and concurrency contract enforces five non-negotiable invariants:

| Invariant ID | Name | Normative Rule |
| :--- | :--- | :--- |
| **INV-01** | **Separation of Coordination vs Authority** | Mneme token consumption coordinates in-flight active state only. It confers **zero authority** to bypass Mnemosyne's authoritative conditional predecessor check. |
| **INV-02** | **Monotonic Authoritative Progression** | Mnemosyne is the sole authority for state versioning. Every write to an existing resource MUST atomically verify that the current database version matches `ExpectedAuthoritativeVersion` and advance it by exactly one. |
| **INV-03** | **Guarded Convergence** | Following an authoritative database commit, cache convergence into Mneme MUST execute via a CAS loop. A write MUST NEVER overwrite a newer version in the cache with a stale or older version. |
| **INV-04** | **Commit Visibility over Degraded Convergence** | If an authoritative commit to Mnemosyne succeeds but subsequent Mneme convergence fails (e.g., node network partition, CAS exhaustion), the operation MUST be reported as a **SUCCESS** (`COMMITTED_CONVERGENCE_DEGRADED`). It MUST NOT throw an exception or report failure, preventing callers from issuing spurious duplicate mutations. |
| **INV-05** | **Zero-Delete Lifecycle** | Governed write APIs expose only `create` and `update` primitives. Physical deletion is strictly prohibited at the contract and service layers. |

---

## 4. Governed Read Model

Any component intending to mutate a governed resource must first obtain a `GovernedRead<T>`. This encapsulates the resource payload, the active coordination token from the cache, and the authoritative persistence version.

### 4.1 Java Contract Record Shapes

```java
package net.fhirfactory.harmonia.model.governedwrite;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Unique identifier for a governed resource.
 */
public record ResourceKey(
    String resourceType,
    String id
) implements Serializable {
    public ResourceKey {
        Objects.requireNonNull(resourceType, "resourceType must not be null");
        Objects.requireNonNull(id, "id must not be null");
        if (resourceType.isBlank()) {
            throw new IllegalArgumentException("resourceType must not be blank");
        }
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
    }

    public static ResourceKey of(String resourceType, String id) {
        return new ResourceKey(resourceType, id);
    }

    public String toQualifiedPath() {
        return resourceType + "/" + id;
    }
}

/**
 * Immutable, opaque active-state token representing distributed cache entry state (Mneme).
 * Strictly represents a real observed token (no magic absence states).
 * Carries NO arithmetic meaning and exposes no numeric or string unwrap methods.
 * Construction and internal version extraction are restricted to infrastructure via ActiveStateTokenBridge.
 */
public final class ActiveStateToken implements Serializable {

    private final long version;

    ActiveStateToken(long version) {
        if (version < 0) {
            throw new IllegalArgumentException("Version must be non-negative");
        }
        this.version = version;
    }

    long internalVersion() {
        return this.version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveStateToken that = (ActiveStateToken) o;
        return this.version == that.version;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(version);
    }

    @Override
    public String toString() {
        return "ActiveStateToken[opaque]";
    }
}

/**
 * Internal infrastructure bridge enabling Mneme coordination layers to construct and inspect
 * ActiveStateToken instances without exposing numeric or internal accessors to general callers.
 */
public final class ActiveStateTokenBridge {
    private ActiveStateTokenBridge() {}

    public static ActiveStateToken create(long version) {
        return new ActiveStateToken(version);
    }

    public static long extractVersion(ActiveStateToken token) {
        Objects.requireNonNull(token, "token must not be null");
        return token.internalVersion();
    }
}

/**
 * Authoritative version identifying durable persistence state in Mnemosyne.
 */
public record AuthoritativeVersion(
    String value
) implements Serializable {
    public AuthoritativeVersion {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    public static AuthoritativeVersion of(String value) {
        return new AuthoritativeVersion(value);
    }

    public static AuthoritativeVersion of(long versionNumber) {
        return new AuthoritativeVersion(String.valueOf(versionNumber));
    }

    public ExpectedAuthoritativeVersion toExpected() {
        return ExpectedAuthoritativeVersion.of(this);
    }
}

/**
 * Expected authoritative predecessor version for conditional writes.
 * Represents either expected absence (for CREATE) or a specific prior authoritative version (for UPDATE).
 */
public final class ExpectedAuthoritativeVersion implements Serializable {

    private static final ExpectedAuthoritativeVersion NONE = new ExpectedAuthoritativeVersion(null);

    private final AuthoritativeVersion version;

    private ExpectedAuthoritativeVersion(AuthoritativeVersion version) {
        this.version = version;
    }

    public static ExpectedAuthoritativeVersion none() {
        return NONE;
    }

    public static ExpectedAuthoritativeVersion of(AuthoritativeVersion version) {
        Objects.requireNonNull(version, "version must not be null");
        return new ExpectedAuthoritativeVersion(version);
    }

    public static ExpectedAuthoritativeVersion of(String versionString) {
        Objects.requireNonNull(versionString, "versionString must not be null");
        return new ExpectedAuthoritativeVersion(AuthoritativeVersion.of(versionString));
    }

    public static ExpectedAuthoritativeVersion of(long versionNumber) {
        return new ExpectedAuthoritativeVersion(AuthoritativeVersion.of(versionNumber));
    }

    public boolean isNone() {
        return version == null;
    }

    public Optional<AuthoritativeVersion> version() {
        return Optional.ofNullable(version);
    }

    public Optional<String> value() {
        return version().map(AuthoritativeVersion::value);
    }
}

/**
 * Immutable envelope representing the result of a governed read, packaging the resource
 * along with both active coordination context (Mneme) and authoritative persistence predecessor context (Mnemosyne).
 */
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveStateToken activeToken,
    AuthoritativeVersion authoritativeVersion
) implements Serializable {
    public GovernedRead {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(activeToken, "activeToken must not be null");
        Objects.requireNonNull(authoritativeVersion, "authoritativeVersion must not be null");
    }

    public static <T> GovernedRead<T> of(
        ResourceKey key,
        T resource,
        ActiveStateToken activeToken,
        AuthoritativeVersion authoritativeVersion
    ) {
        return new GovernedRead<>(key, resource, activeToken, authoritativeVersion);
    }

    public ExpectedAuthoritativeVersion expectedAuthoritativeVersion() {
        return authoritativeVersion.toExpected();
    }
}
```

### 4.2 Read Semantics and Cold Reads
1. **Cache Hit (Warm Read)**:
   - The reader issues a Hot Rod `getWithMetadata(key)` to Mneme.
   - If the entry exists, `GovernedRead<T>` is constructed using the cached payload, the opaque entry version from `MetadataValue.getVersion()` wrapped as `ActiveStateToken` via `ActiveStateTokenBridge`, and the extracted persistence version as `ExpectedAuthoritativeVersion`.
2. **Cache Miss (Cold Read)**:
   - The reader queries Mnemosyne durable storage.
   - If found, Mnemosyne returns the persisted entity and its database `versionId`.
   - The reader seeds Mneme using a conditional `putIfAbsent` or loads metadata, obtaining a fresh `ActiveStateToken`.
   - If not found, `GovernedRead<T>` is not produced; a `ResourceNotFoundException` or empty optional is returned.

---

## 5. CREATE Contract

The `create` primitive establishes a new authoritative resource in Mnemosyne and initializes its representation in Mneme.

```java
package net.fhirfactory.harmonia.model.governedwrite;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

public interface GovernedWriter {

    /**
     * Initiates a governed CREATE operation for a new resource.
     *
     * @param key             target resource key
     * @param resource        the resource payload to create
     * @param securityContext Themis security and provenance context
     * @param <T>             resource payload type
     * @return result of the governed write (sealed WriteResult hierarchy)
     */
    <T> WriteResult<T> create(
        ResourceKey key, 
        T resource, 
        ThemisSecurityContext securityContext
    );
    
    /**
     * Initiates a governed UPDATE operation against a previously read resource.
     *
     * @param current         current governed read state containing active and authoritative predecessor tokens
     * @param proposed        proposed updated resource payload
     * @param securityContext Themis security and provenance context
     * @param <T>             resource payload type
     * @return result of the governed write (sealed WriteResult hierarchy)
     */
    <T> WriteResult<T> update(
        GovernedRead<T> current, 
        T proposed, 
        ThemisSecurityContext securityContext
    );
}
```

### 5.1 Strict Uniqueness & No Upsert
- **No Upsert Semantics**: CREATE asserts that no prior entity with the given `ResourceKey` exists in durable persistence.
- **Duplicate Rejection**: If an entity with the specified identifier already exists in Mnemosyne, CREATE fails and returns `WriteResult.AuthoritativeConflict` with failure reason `PreconditionFailureReason.RESOURCE_ALREADY_EXISTS` and `commitOutcome = NOT_COMMITTED`. It **MUST NOT** overwrite or update the existing entity.

### 5.2 End-to-End CREATE Sequence
```
Caller                GovernedWriter           Themis            Mneme (Cache)       Mnemosyne (DB)
  |                         |                    |                     |                   |
  |--- create(key, res) --->|                    |                     |                   |
  |                         |-- authorize(res) ->|                     |                   |
  |                         |<- PERMIT ----------|                     |                   |
  |                         |                                          |                   |
  |                         |-- putIfAbsent(key, coordination claim) ->|                   |
  |                         |<- OK (or cache unavailable) -------------|                   |
  |                         |                                                              |
  |                         |-- insertResource(key, res, version=1) ---------------------->|
  |                         |<- Committed (version=1) -------------------------------------|
  |                         |                                                              |
  |                         |-- converge(key, res, v=1) -------------->|                   |
  |                         |<- Converged -----------------------------|                   |
  |                         |                                                              |
  |<- WriteResult.Committed-|                                                              |
```

1. **Phase 1: Authorization**: Evaluate `ThemisSecurityContext` against default-deny policies for `CREATE` on the target resource type.
2. **Phase 2: Active Coordination Check**: Execute `putIfAbsent` on a Mneme coordination claim. If an active claim already exists, verify its authoritative status. The claim is not a readable resource representation and MUST NOT publish the proposed payload before the durable insert succeeds.
3. **Phase 3: Authoritative Insert**: Execute an atomic SQL `INSERT` into Mnemosyne with initial `version = 1`. If a unique constraint violation occurs, return `WriteResult.AuthoritativeConflict` (`RESOURCE_ALREADY_EXISTS`).
4. **Phase 4: Guarded Convergence**: Update Mneme with the committed version 1 representation via CAS or put.
5. **Phase 5: Return Result**: Return `WriteResult.Committed<T>` with `AuthoritativeCommitOutcome.COMMITTED`, `ConvergenceStatus.CONVERGED`, and committed version `1`.

---

## 6. UPDATE Contract

The `update` primitive modifies an existing resource, advancing its authoritative version under strict optimistic concurrency control.

### 6.1 Complete 5-Phase End-to-End Sequence

```
Caller           GovernedWriter         Mneme (Infinispan)         Themis          Mnemosyne (PostgreSQL)
  |                    |                        |                    |                       |
  |=== Phase 1: Governed Read ===============================================================|
  |-- read(key) ------>|                        |                    |                       |
  |                    |-- getWithMetadata() -->|                    |                       |
  |                    |<- entry + token =======|                    |                       |
  |<- GovernedRead ----|                        |                    |                       |
  |                    |                        |                    |                       |
  |=== Phase 2: Active Coordination =========================================================|
  |-- update(curr,prop)|                        |                    |                       |
  |                    |-- replaceWithVersion ->|                    |                       |
  |                    |   (key, coordination claim, token)          |                       |
  |                    |<- CAS Success / Fail --|                    |                       |
  |                    |   [If Fail: Return WriteResult.ActiveConflict]                      |
  |                    |                        |                    |                       |
  |=== Phase 3: Security & Business Validation ==============================================|
  |                    |-- authorize(UPDATE) ----------------------->|                       |
  |                    |<- PERMIT -----------------------------------|                       |
  |                    |                                                                     |
  |=== Phase 4: Authoritative Persistence Commit ============================================|
  |                    |-- conditionalUpdate(key, prop, expectedVer=N) --------------------->|
  |                    |   [Atomic: UPDATE ... SET ver=N+1 WHERE id=id AND ver=N]            |
  |                    |<- Success: CommittedVersion = N+1 ----------------------------------|
  |                    |   [If 0 rows: Return WriteResult.AuthoritativeConflict]             |
  |                    |                        |                                            |
  |=== Phase 5: Guarded Cache Convergence ===================================================|
  |                    |-- converge(key, prop, CommittedVer=N+1) --->|                       |
  |                    |   [CAS Loop with newer-version check]       |                       |
  |                    |<- Converged / Degraded ---------------------|                       |
  |                    |                                                                     |
  |<- WriteResult -----|                                                                     |
```

---

## 7. Active Coordination Contract

Mneme (Infinispan 15.0.3) provides distributed in-memory active-state concurrency coordination via Hot Rod client metadata and atomic compare-and-swap (CAS) primitives. Implemented in Task 08 Step 08.04B, this capability encapsulates native Hot Rod optimistic concurrency into a dedicated, non-authoritative active-state coordinator without introducing resource caching complexity, application version arithmetic, or process-local fallbacks.

### 7.1 ActiveStateCoordinator API & Outcome Taxonomy

The active coordination interface in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) provides caller-facing observation and CAS progression operations:

```java
package net.fhirfactory.harmonia.model.governedwrite;

/**
 * Distributed active-state coordinator managing optimistic in-memory token observation and CAS progression.
 */
public interface ActiveStateCoordinator {

    /**
     * Observes the current active state token for the specified resource key.
     *
     * @param key target resource key (must not be null)
     * @return current active state token representing observed cache state
     */
    ActiveStateToken observe(ResourceKey key);

    /**
     * Atomically consumes the observed active state token via Hot Rod CAS.
     *
     * @param key           target resource key (must not be null)
     * @param observedToken the active state token previously observed (must not be null)
     * @return coordination result indicating CAS outcome
     */
    ActiveStateCoordinationResult consume(ResourceKey key, ActiveStateToken observedToken);
}
```

```java
package net.fhirfactory.harmonia.model.governedwrite;

/**
 * Outcome of an atomic active-state token consumption attempt.
 */
public enum ActiveStateCoordinationResult {
    /** Token was successfully consumed and active state was advanced. */
    CONSUMED,
    /** Token was stale or already consumed by a competing participant. */
    STALE,
    /** Coordination cluster or transport is unreachable; no local fallback permitted. */
    UNAVAILABLE
}
```

### 7.2 Strict Token Opacity & Internal Bridge Boundary

- **Opaque Value Object**: `ActiveStateToken` encapsulates the opaque 64-bit version returned by Infinispan's `MetadataValue.getVersion()`.
- **Zero Raw Accessors**: The token exposes no public constructor, no public static factory from raw primitive/string values, no numeric getters (`longValue()`, `intValue()`), does not implement `Comparable`, masks its internal representation in `toString()` (`ActiveStateToken[opaque]`), and exposes zero arithmetic methods.
- **Internal Bridge Encapsulation**: Construction (`ActiveStateTokenBridge.create(long)`) and version unwrapping (`ActiveStateTokenBridge.extractVersion(ActiveStateToken)`) are restricted via ArchUnit rules exclusively to `net.fhirfactory.harmonia.hestia.mneme..` and the `model.governedwrite` package itself.

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

### 7.3 Dedicated Coordination Cache Isolation

- **Isolated Cache Name**: Active coordination state lives exclusively in `active-coordination-cache`.
- **Non-Persistent In-Memory Storage**: Defined in `infinispan.xml` as a `REPL_SYNC` cache with statistics enabled and strictly **zero** `<persistence>` stores to Mnemosyne. This guarantees that transient coordination markers never pollute clinical databases.
- **Boring Coordination State**: Coordination cache values use a fixed marker (`"ACTIVE"`). The coordinator never stores UUIDs, counters, timestamps, or application version arithmetic in the coordination cache.

### 7.4 Hot Rod CAS Mechanics & Implementation

The production coordinator `HotRodActiveStateCoordinator` in `hestia:mneme-cluster` executes native Hot Rod CAS operations:

1. **Observation (`observe`)**:
   - Executes `coordinationCache.getWithMetadata(key.toQualifiedPath())`.
   - If the entry is absent, initializes it via `coordinationCache.putIfAbsent(key.toQualifiedPath(), "ACTIVE")` and re-reads metadata.
   - Wraps `MetadataValue.getVersion()` into `ActiveStateToken` via `ActiveStateTokenBridge.create(version)`.
2. **Consumption (`consume`)**:
   - Unpacks the entry version via `ActiveStateTokenBridge.extractVersion(observedToken)`.
   - Executes `coordinationCache.replaceWithVersion(key.toQualifiedPath(), "ACTIVE", version)`.
   - If CAS returns `true` $\rightarrow$ returns `ActiveStateCoordinationResult.CONSUMED`.
   - If CAS returns `false` $\rightarrow$ returns `ActiveStateCoordinationResult.STALE`.
   - If Hot Rod transport or client exception occurs $\rightarrow$ returns `ActiveStateCoordinationResult.UNAVAILABLE`.

### 7.5 At-Most-One Winner Architectural Invariant

- **At-Most-One Winner**: For any given `ActiveStateToken`, at most one concurrent participant can successfully receive `CONSUMED`; all other competing participants receive `STALE`. Under healthy cluster conditions, exactly one winner is produced across competing participants.
- **Re-use Rejection**: A previously consumed `ActiveStateToken` cannot be consumed a second time (yields `STALE`).
- **Participant Failure Invariance**: If a participant consumes a token and crashes or fails before completing downstream work, the token remains consumed in the cluster and cannot be re-consumed by other participants using the prior token.

### 7.6 Non-Authoritative Boundary & No Process-Local Fallback

- **Token Consumption $\ne$ Authority**: Successfully executing `consume` on Mneme coordinates only the active in-flight cache state. It confers **zero authority** to bypass Mnemosyne's authoritative conditional predecessor check (`ExpectedAuthoritativeVersion`).
- **Visible Failure Without Local Fallback**: If Infinispan or Hot Rod is unreachable, `consume` returns `UNAVAILABLE` (and `observe` throws `ActiveCoordinationUnavailableException`). The implementation strictly prohibits silent degradation or fallback to JVM-local synchronization, CAS (`AtomicReference`, `AtomicLong`), or in-memory maps (`ConcurrentHashMap`).

### 7.7 Active Coordination Failure Modes

1. **CAS Rejection (`false` / `STALE`)**: Another concurrent thread or cluster node updated or consumed the active state entry between observation and consumption.
   - *Action*: Return `WriteResult.ActiveConflict<T>`. The caller may reread and retry.
2. **Entry Evicted/Missing**: The active coordination entry expired or was evicted.
   - *Action*: Observation lazily re-seeds the `"ACTIVE"` marker with a fresh version.
3. **Mneme Cluster Partition / Outage (`UNAVAILABLE`)**: Hot Rod communication fails with timeout or connection exception.
   - *Action*: If configured for strict active coordination, return `WriteResult.ActiveConflict<T>` or `WriteResult.NotCommitted<T>`. If configured for high-availability fallback, proceed directly to Mnemosyne conditional update, marking convergence as `ConvergenceStatus.DEGRADED`.

---

## 8. Authoritative Persistence Contract

Mnemosyne defines the durable persistence boundary, implemented via PostgreSQL 16 and HAPI FHIR JPA entities (`FhirResourceEntity`).

### 8.1 Conditional Update Specification (Internal Persistence Port)

```java
package net.fhirfactory.harmonia.hestia.governance.port;

import net.fhirfactory.harmonia.model.governedwrite.*;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

public interface MnemosynePersistencePort {

    <T> AuthoritativeVersion insertResource(
        ResourceKey key,
        T resource,
        ThemisSecurityContext securityContext
    );

    <T> AuthoritativeVersion conditionalUpdate(
        ResourceKey key,
        T resource,
        ExpectedAuthoritativeVersion expectedVersion,
        ThemisSecurityContext securityContext
    );
}
```

### 8.2 Atomic Database Precondition
The database execution must be strictly atomic. In SQL/JPQL terms:

```sql
UPDATE fhir_resource
SET 
    resource_json = :payloadJson,
    version_id = version_id + 1,
    last_updated = :commitTimestamp,
    updated_by = :principalName
WHERE 
    resource_id = :resourceId 
    AND resource_type = :resourceType 
    AND version_id = :expectedVersionNumber;
```

- **Row Count Evaluation**:
  - `updatedRows == 1`: The commit succeeded. The new authoritative version is `expectedVersionNumber + 1`.
  - `updatedRows == 0`: Precondition failed. The entity was either mutated by a concurrent transaction or absent. The transaction rolls back and returns `WriteResult.AuthoritativeConflict` (`EXPECTED_VERSION_MISMATCH`).

---

## 9. Conflict Model

Harmonia distinguishes mutually exclusive write conflict and error types through strongly typed record models rather than wide exception trees:

```
                                  WRITE ERROR TAXONOMY
                                            |
        +-----------------------------------+-----------------------------------+
        |                                   |                                   |
        v                                   v                                   v
+-----------------------+       +-------------------------+       +--------------------------+
|  ActiveStateConflict  |       | AuthoritativePreconditionConflict| OutcomeUnknown          |
+-----------------------+       +-------------------------+       +--------------------------+
| * Mneme Hot Rod CAS   |       | * Mnemosyne PostgreSQL  |       | * Ambiguous network      |
|   mismatch or race.   |       |   precondition failure. |       |   timeout during commit. |
| * Durable DB state    |       | * Duplicate CREATE key. |       | * DB state unconfirmed.  |
|   NOT modified.       |       | * Expected version fail.|       | * Caller must reconcile. |
| * Fast retry possible.|       | * Re-read required.     |       | * Do NOT auto-retry.     |
+-----------------------+       +-------------------------+       +--------------------------+
```

### 9.1 Conflict Classification Matrix

| Conflict Type | Layer Encountered | Cause | State of Database | Recommended Caller Remediation |
| :--- | :--- | :--- | :--- | :--- |
| `ActiveStateConflict` | Mneme (Infinispan) | Active coordination CAS token mismatch; concurrent cache write. | Unmodified | Immediate transparent retry (re-read active state and re-attempt CAS). |
| `AuthoritativePreconditionConflict` | Mnemosyne (PostgreSQL) | Database row version $\ne$ `ExpectedAuthoritativeVersion` (`EXPECTED_VERSION_MISMATCH`), or duplicate ID on CREATE (`RESOURCE_ALREADY_EXISTS`). | Unmodified on failure (or advanced past expected version by prior committer) | Business abort or full re-read and domain-level merge. |
| `OutcomeUnknown` | Transport / Network | Socket timeout or connection drop during SQL `COMMIT`. | Ambiguous (may be committed or rolled back) | Idempotent reconciliation check using correlation/causation ID before retrying. |

### 9.2 Conflict Model Definitions

```java
package net.fhirfactory.harmonia.model.governedwrite;

import java.io.Serializable;
import java.util.Objects;

public enum PreconditionFailureReason {
    RESOURCE_ALREADY_EXISTS,
    EXPECTED_VERSION_MISMATCH
}

public record ActiveStateConflict(
    ResourceKey key,
    String message
) implements Serializable {
    public ActiveStateConflict {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }
}

public record AuthoritativePreconditionConflict(
    ResourceKey key,
    PreconditionFailureReason reason,
    ExpectedAuthoritativeVersion expectedVersion,
    AuthoritativeVersion currentVersion,
    String message
) implements Serializable {
    public AuthoritativePreconditionConflict {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }
}
```

---

## 10. Result Model

Every governed write returns an immutable, sealed `WriteResult<T>`.

### 10.1 Sealed Java Result Hierarchy

```java
package net.fhirfactory.harmonia.model.governedwrite;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public enum AuthoritativeCommitOutcome {
    COMMITTED,
    NOT_COMMITTED,
    UNKNOWN
}

public enum ConvergenceStatus {
    CONVERGED,
    DEGRADED,
    NOT_APPLICABLE
}

public sealed interface WriteResult<T> extends Serializable permits
        WriteResult.Committed,
        WriteResult.ActiveConflict,
        WriteResult.AuthoritativeConflict,
        WriteResult.OutcomeUnknown,
        WriteResult.NotCommitted {

    ResourceKey key();
    AuthoritativeCommitOutcome commitOutcome();
    ConvergenceStatus convergenceStatus();

    default boolean isCommitted() {
        return commitOutcome() == AuthoritativeCommitOutcome.COMMITTED;
    }

    default boolean isOutcomeUnknown() {
        return commitOutcome() == AuthoritativeCommitOutcome.UNKNOWN;
    }

    default boolean isConflict() {
        return this instanceof ActiveConflict || this instanceof AuthoritativeConflict;
    }

    // Permitted sealed record variants

    record Committed<T>(
        ResourceKey key,
        T resource,
        AuthoritativeVersion version,
        ConvergenceStatus convergenceStatus,
        String degradationMessage
    ) implements WriteResult<T> {
        public Committed {
            Objects.requireNonNull(key, "key must not be null");
            Objects.requireNonNull(resource, "resource must not be null");
            Objects.requireNonNull(version, "version must not be null");
            Objects.requireNonNull(convergenceStatus, "convergenceStatus must not be null");
            if (convergenceStatus == ConvergenceStatus.NOT_APPLICABLE) {
                throw new IllegalArgumentException("Committed write must not have NOT_APPLICABLE convergence status");
            }
        }
        @Override public AuthoritativeCommitOutcome commitOutcome() { return AuthoritativeCommitOutcome.COMMITTED; }
        
        public Optional<String> degradationReason() {
            return Optional.ofNullable(degradationMessage);
        }
    }

    record ActiveConflict<T>(
        ResourceKey key,
        ActiveStateConflict conflict
    ) implements WriteResult<T> {
        @Override public AuthoritativeCommitOutcome commitOutcome() { return AuthoritativeCommitOutcome.NOT_COMMITTED; }
        @Override public ConvergenceStatus convergenceStatus() { return ConvergenceStatus.NOT_APPLICABLE; }
    }

    record AuthoritativeConflict<T>(
        ResourceKey key,
        AuthoritativePreconditionConflict conflict
    ) implements WriteResult<T> {
        @Override public AuthoritativeCommitOutcome commitOutcome() { return AuthoritativeCommitOutcome.NOT_COMMITTED; }
        @Override public ConvergenceStatus convergenceStatus() { return ConvergenceStatus.NOT_APPLICABLE; }
    }

    record OutcomeUnknown<T>(
        ResourceKey key,
        String message
    ) implements WriteResult<T> {
        @Override public AuthoritativeCommitOutcome commitOutcome() { return AuthoritativeCommitOutcome.UNKNOWN; }
        @Override public ConvergenceStatus convergenceStatus() { return ConvergenceStatus.NOT_APPLICABLE; }
    }

    record NotCommitted<T>(
        ResourceKey key,
        String reason
    ) implements WriteResult<T> {
        @Override public AuthoritativeCommitOutcome commitOutcome() { return AuthoritativeCommitOutcome.NOT_COMMITTED; }
        @Override public ConvergenceStatus convergenceStatus() { return ConvergenceStatus.NOT_APPLICABLE; }
    }
}
```

### 10.2 Commit Success with Degraded Convergence (INV-04)
If the database commit succeeds in Mnemosyne, durability has been achieved. If Mneme subsequently fails to converge (e.g., cache node crash, Hot Rod timeout, or CAS retry limit reached), the result is:
- `commitOutcome()` = `AuthoritativeCommitOutcome.COMMITTED`
- `convergenceStatus()` = `ConvergenceStatus.DEGRADED`
- `isCommitted()` = `true`
- The method **returns a `WriteResult.Committed<T>`** and does **not** throw an exception or report failure.

---

## 11. Version Model

Harmonia strictly isolates four distinct version domains to prevent cross-tier coupling and semantic confusion:

```
+--------------------------------------------------------------------------------------------------+
|                                    FOUR VERSION DOMAINS                                          |
+--------------------------+-------------------------+----------------------+----------------------+
| 1. Mneme Entry Token     | 2. Mnemosyne DB Version | 3. FHIR Meta Version | 4. HTTP ETag         |
+--------------------------+-------------------------+----------------------+----------------------+
| Owner: Infinispan HotRod | Owner: Mnemosyne DB     | Owner: Calliope FHIR | Owner: Pylai Gateway |
| Type: Opaque 64-bit long | Type: Monotonic long    | Type: String ("1")   | Type: ETag W/"1"     |
| Life: Volatile/Cache-run | Life: Permanent/Durable | Life: Serialized DTO | Life: Wire Boundary  |
| Rule: CAS equality only  | Rule: Atomic N -> N+1   | Rule: = DB Version   | Rule: If-Match map   |
+--------------------------+-------------------------+----------------------+----------------------+
```

### 11.1 Version Domain Comparison Matrix

| Attribute | Domain 1: Mneme Entry Token | Domain 2: Mnemosyne DB Version | Domain 3: FHIR `meta.versionId` | Domain 4: HTTP ETag / `If-Match` |
| :--- | :--- | :--- | :--- | :--- |
| **Owning Subsystem** | Hestia :: Mneme | Hestia :: Mnemosyne | Calliope :: FHIR Models | Pylai :: REST Gateways |
| **Data Type** | `long` (Hot Rod metadata) | `long` / `BIGINT` | `java.lang.String` | `java.lang.String` (HTTP Header) |
| **Progression** | Opaque non-sequential token | Monotonic sequential ($1, 2, 3\dots$) | Exact string representation of DB version | `W/"<version>"` or `"<version>"` |
| **Lifetime** | Lost on eviction, flush, restart | Permanent, immutable historical versions | Bounded to serialized payload lifecycle | Transient HTTP request/response cycle |
| **Comparison Semantics** | Exact value equality (`==`) in Hot Rod CAS | Numeric equality (`=`) in SQL `WHERE` clause | Lexical equality for client display | RFC 7232 weak/strong ETag matching |

### 11.2 Domain Conflation Prohibitions
1. **Never use Hot Rod tokens as FHIR `meta.versionId`**: Cache entry tokens fluctuate with eviction/repopulation and must never be exposed externally as resource versions.
2. **Never apply arithmetic to Hot Rod tokens**: `token + 1` is invalid and corrupts Infinispan CAS operations.
3. **Pylai isolates ETags**: Inbound `If-Match` headers are stripped of quotes and `W/` prefixes at the gateway perimeter and converted into `ExpectedAuthoritativeVersion`.

---

## 12. Guarded Mneme Convergence

Following an authoritative commit to Mnemosyne, the updated state must converge back into Mneme to ensure subsequent reads observe the latest version.

### 12.1 Convergence CAS Loop Algorithm (Task 08.02B Correction)

```
                       START CONVERGENCE (CommittedVer = V_c)
                                        |
                                        v
                            [Attempt = 1; Max = 3]
                                        |
                                        v
+----------------------------> Read Mneme Entry & Token
|                                       |
|                                       v
|                        Is Cache Version >= V_c?
|                                /             \
|                           YES /               \ NO
|                              v                 v
|                      [Preserve Newer]    Attempt CAS replaceWithVersion()
|                              |                 /              \
|                              |         SUCCESS/                \ FAIL
|                              |               v                  v
|                              |          [CONVERGED]      Attempt < Max?
|                              |               |              /        \
|                              |               |          YES/          \ NO
|                              |               |            v            v
|                              |               |    [Increment Attempt]  [Invalidate Entry]
|                              |               |            |            |
|                              v               v            |            v
+-----------------------------------------------------------+    [DEGRADED_CAS_EXHAUSTED]
```

```java
public ConvergenceStatus converge(
    ResourceKey key, 
    T committedResource, 
    CommittedAuthoritativeVersion committedVersion
) {
    int maxAttempts = 3;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
        try {
            MetadataValue<T> currentMeta = remoteCache.getWithMetadata(key);
            if (currentMeta != null) {
                long cachedVersion = extractAuthoritativeVersion(currentMeta.getValue());
                // Newer-Version Invariant: Never overwrite newer cache state with older state
                if (cachedVersion >= committedVersion.versionNumber()) {
                    return ConvergenceStatus.CONVERGED;
                }
                boolean casSuccess = remoteCache.replaceWithVersion(
                    key, 
                    committedResource, 
                    currentMeta.getVersion()
                );
                if (casSuccess) {
                    return ConvergenceStatus.CONVERGED;
                }
            } else {
                T existing = remoteCache.putIfAbsent(key, committedResource);
                if (existing == null) {
                    return ConvergenceStatus.CONVERGED;
                }
            }
        } catch (Exception e) {
            log.warn("Mneme cache convergence failed on attempt {}", attempt, e);
            return ConvergenceStatus.DEGRADED_CACHE_UNAVAILABLE;
        }
    }
    // Invalidate entry if CAS loop exhausted to prevent stale reads
    try {
        remoteCache.remove(key);
    } catch (Exception e) {
        log.warn("Failed to invalidate cache entry after exhausted convergence", e);
    }
    return ConvergenceStatus.DEGRADED_CACHE_CAS_EXHAUSTED;
}
```

### 12.2 Newer-Version Invariant
A write completing out-of-order must never overwrite a newer version already converged into the cache. If `cachedVersion >= committedVersion`, convergence terminates immediately with `CONVERGED`.

---

## 13. Commit Outcome Semantics

In distributed systems, network partitions or process crashes during a database commit leave the outcome ambiguous.

### 13.1 CommitOutcomeUnknown Protocol
When a database commit call encounters an unconfirmed outcome due to a network timeout or connection reset:
1. **Do NOT assume failure**: The SQL `COMMIT` may have reached PostgreSQL and committed before the ACK was lost.
2. **Do NOT assume success**: The transaction may have rolled back.
3. **Return `WriteResult.OutcomeUnknown<T>`**: The result variant encapsulates:
   - `ResourceKey`
   - `AuthoritativeCommitOutcome.UNKNOWN`
   - Diagnostic failure message
4. **Idempotent Reconciliation**:
   - The calling workflow (Ergon / Ponos) must execute a deterministic reconciliation check before retrying.
   - The reconciliation query checks Mnemosyne for an entity matching `ResourceKey` where `last_causation_id == securityContext.causationId()`.
   - If found, the commit succeeded; the workflow proceeds. If not found and the version is unchanged, the workflow safely retries.

---

## 14. Security and Provenance Context

To prevent context proliferation and duplicate models, the governed write contract directly reuses Harmonia's canonical security and provenance models from `themis-api`.

### 14.1 Direct ThemisSecurityContext Reuse

The caller-facing `GovernedWriter` methods directly accept `ThemisSecurityContext`:

```java
public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext);
}
```

- **Authentication & Authorization**: `ThemisSecurityContext` provides requesting principal, executing principal, security domain, and granted authorities.
- **Correlation & Causation**: `ThemisSecurityContext` carries `correlationId` and `causationId` for distributed trace lineage and idempotent commit reconciliation.
- **Audit Logging**: Operations dispatch non-PHI audit events to `ThemisAuditService` referencing correlation and causation identifiers.

### 14.2 Existing Workflow and Persistence Carriers

`GovernedWriter` avoids introducing redundant wrapper classes:

- `ThemisSecurityContext` is passed directly for authorization; no second principal, authority, or security-domain model is introduced.
- For persistence-oriented callers, `PersistenceOperationEnvelope` supplies operation identity, expected version, and security context.
- For asynchronous workflow callers, `Pragma` supplies correlation/causation IDs, source, originating/executing principals, and security context.
- Workflow adapters pass `securityContext` directly into `GovernedWriter` without discarding provenance or creating parallel envelopes.

---

## 15. API Placement and Service Boundaries

The governed write architecture is distributed across existing subprojects according to strict architectural responsibilities:

```
+-----------------------------------------------------------------------------------+
|                                 MODULE PLACEMENT                                  |
|                                                                                   |
|  [ calliope :: net.fhirfactory.harmonia.model.governedwrite ]                     |
|  * Pure Contract Layer (Implemented in Task 08 Step 08.04A/B):                     |
|    - ResourceKey, ActiveStateToken, ActiveStateTokenBridge, AuthoritativeVersion   |
|    - ExpectedAuthoritativeVersion, GovernedRead<T>, GovernedWriter                |
|    - ActiveStateCoordinator, ActiveStateCoordinationResult                        |
|    - WriteResult<T> (Sealed Hierarchy: Committed, ActiveConflict, etc.)           |
|    - ActiveStateConflict, AuthoritativePreconditionConflict                       |
|    - AuthoritativeCommitOutcome, ConvergenceStatus, PreconditionFailureReason     |
|  * Reuses ThemisSecurityContext from themis-api                                   |
|  * ZERO dependencies on Infinispan, JPA, Hibernate, or HTTP libraries             |
|                                                                                   |
|  [ hestia :: mneme-cluster (Implemented in Task 08 Step 08.04B) ]                 |
|  * HotRodActiveStateCoordinator (Production Hot Rod CAS & Token Adapter)          |
|  * active-coordination-cache (infinispan.xml, REPL_SYNC, zero persistence stores) |
|  * ActiveCoordinationUnavailableException                                         |
|                                                                                   |
|  [ hestia :: mnemosyne-clinical (Runtime Persistence Planned - Step 08.04C) ]     |
|  * MnemosynePersistencePort (Conditional SQL / JPA Update Adapter)                |
|  * FhirResourceEntity / FhirResourceRepository                                    |
|                                                                                   |
|  [ Callers: pylai-fhir-registry, energeia-erga, iris-befe ]                        |
|  * Use GovernedWriter exclusively. Zero direct cache/DB mutations.                |
+-----------------------------------------------------------------------------------+
```

---

## 16. Bypass Prevention and Legacy Remediation

Direct writes to caches or databases bypass concurrency control and introduce data corruption risks.

### 16.1 Catalog of Existing Bypass Risk Locations

| Component | File Path | Current Bypass Behavior | Remediation Plan |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `iris/iris-befe/.../FhirCacheService.java` | Direct `remoteCache.put` and independent version manufacturing. | Replace with `GovernedWriter.update()`. |
| **Energeia Erga** | `energeia/erga/.../TaskCacheService.java` | Direct `remoteCache.put` and cache clearing. | Encapsulate within governed task pipeline. |
| **Mneme Store** | `hestia/mneme-persistence/.../FhirRestCacheStore.java` | Unconditional write-behind PUT and delete. | Deprecate write-behind in favor of synchronous GovernedWriter. |
| **Mnemosyne Service**| `hestia/mnemosyne-clinical/.../FhirStorageService.java`| CREATE upserts; non-atomic update increments; soft DELETE exposed. | Restrict to package-private `MnemosynePersistencePort`; remove DELETE. |
| **HAPI Providers**| `.../hapifhir/provider/*ResourceProvider.java` | Direct `@Delete` annotations and unconditional updates. | Refactor to invoke `GovernedWriter`. |

### 16.2 ArchUnit Bypass Enforcement Strategy
Architecture test guardrails assert:
1. `net.fhirfactory.harmonia.model.governedwrite..` has **zero** dependencies on Infinispan, JPA/Hibernate, or HTTP framework packages (`GovernedWriteContractArchitectureTest`).
2. Subsequent runtime migration tasks will enforce that callers outside Hestia persistence ports cannot access `RemoteCache` or raw repositories directly.

---

## 17. Developer Usage Examples

### 17.1 Example 1: Idiomatic Governed CREATE

```java
public WriteResult<Practitioner> registerPractitioner(
    Practitioner practitioner, 
    ThemisSecurityContext securityCtx
) {
    ResourceKey key = ResourceKey.of("Practitioner", practitioner.getIdElement().getIdPart());

    WriteResult<Practitioner> result = governedWriter.create(key, practitioner, securityCtx);
    
    if (result instanceof WriteResult.Committed<Practitioner> committed) {
        log.info("Practitioner registered: {} version {}", key, committed.version().value());
        return committed;
    } else if (result instanceof WriteResult.AuthoritativeConflict<Practitioner> authConflict) {
        log.warn("Practitioner already exists: {} reason: {}", key, authConflict.conflict().reason());
        return authConflict;
    } else if (result instanceof WriteResult.ActiveConflict<Practitioner> activeConflict) {
        log.warn("Active cache coordination conflict during registration: {}", key);
        return activeConflict;
    } else if (result instanceof WriteResult.OutcomeUnknown<Practitioner> unknown) {
        log.error("Ambiguous commit outcome during registration: {} - {}", key, unknown.message());
        return unknown;
    }
    
    return result;
}
```

### 17.2 Example 2: Idiomatic Governed Read-Modify-Write UPDATE

```java
public WriteResult<Practitioner> updatePractitionerContact(
    ResourceKey key, 
    ContactPoint newTelecom, 
    ThemisSecurityContext securityCtx
) {
    int maxRetries = 3;
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        GovernedRead<Practitioner> current = governedReader.read(key)
            .orElseThrow(() -> new ResourceNotFoundException("Practitioner not found: " + key));

        Practitioner proposed = current.resource().copy();
        proposed.addTelecom(newTelecom);

        WriteResult<Practitioner> result = governedWriter.update(current, proposed, securityCtx);
        
        if (result instanceof WriteResult.Committed<Practitioner> committed) {
            if (committed.convergenceStatus() == ConvergenceStatus.DEGRADED) {
                log.warn("Practitioner updated with degraded cache convergence: {}", key);
            }
            return committed;
        } else if (result instanceof WriteResult.ActiveConflict<Practitioner>) {
            log.info("Active cache contention on {}, retrying ({}/{})", key, attempt, maxRetries);
            continue; // Transparent retry on cache race
        } else if (result instanceof WriteResult.AuthoritativeConflict<Practitioner> authConflict) {
            log.warn("Authoritative DB version conflict on {}: {}", key, authConflict.conflict().reason());
            return authConflict; // Precondition failure - abort or reload
        } else {
            return result;
        }
    }
    
    return WriteResult.notCommitted(key, "Exceeded retry limit due to active cache contention on " + key);
}
```

### 17.3 Example 3: Idiomatic Active-State Observation and CAS Progression

```java
public boolean coordinateTaskExecution(
    ResourceKey taskKey, 
    ActiveStateCoordinator coordinator
) {
    // 1. Observe current active state token from Mneme
    ActiveStateToken observedToken = coordinator.observe(taskKey);

    // 2. Perform optimistic distributed progression attempt
    ActiveStateCoordinationResult result = coordinator.consume(taskKey, observedToken);

    return switch (result) {
        case CONSUMED -> {
            log.info("Successfully claimed active state execution for {}", taskKey);
            yield true; // Proceed with downstream workflow or persistence
        }
        case STALE -> {
            log.warn("Active state claim rejected as STALE for {} (lost race to competitor)", taskKey);
            yield false; // Abort or re-observe
        }
        case UNAVAILABLE -> {
            log.error("Active coordination cluster is UNAVAILABLE for {}", taskKey);
            yield false; // Visible failure, no silent local fallback
        }
    };
}
```

---

## 18. Contract Test Plan

The following 16 mandatory test scenarios verify every facet of the governed write contract.

| Scenario ID | Name | Preconditions | Execution Sequence | Expected Outcome | Mandatory Assertions |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **ST-01** | Governed CREATE on Non-Existent Resource | Resource does not exist in Mneme or Mnemosyne. | Call `create(key, res, secCtx)`. | `WriteResult.Committed<T>` (`CONVERGED`). | DB row inserted with `version=1`; Mneme entry present with `version=1`; `committed.version().value().equals("1")`. |
| **ST-02** | Governed CREATE on Existing Resource | Resource already exists in Mnemosyne with `version=1`. | Call `create(key, res, secCtx)`. | `WriteResult.AuthoritativeConflict<T>`. | `commitOutcome() == NOT_COMMITTED`; reason is `RESOURCE_ALREADY_EXISTS`; no DB/cache modification. |
| **ST-03** | Governed UPDATE (Happy Path) | Resource exists at version 1; `GovernedRead` obtained. | Call `update(read_v1, proposed, secCtx)`. | `WriteResult.Committed<T>` (`CONVERGED`). | DB `version` advanced to 2; Mneme entry updated with version 2; `committed.version().value().equals("2")`. |
| **ST-04** | Governed UPDATE with Stale Active Token | Active token modified in Mneme by concurrent thread after read. | Call `update(read_v1, proposed, secCtx)`. | `WriteResult.ActiveConflict<T>`. | Mneme CAS fails; Mnemosyne DB is **NOT** called; version remains 1; `commitOutcome() == NOT_COMMITTED`. |
| **ST-05** | Governed UPDATE with Stale DB Version | Active token valid, but DB version advanced to 2 by out-of-band write. | Call `update(read_v1, proposed, secCtx)`. | `WriteResult.AuthoritativeConflict<T>`. | Mneme claim is released; DB conditional update affects 0 rows; reason is `EXPECTED_VERSION_MISMATCH`. |
| **ST-06** | Governed UPDATE on Deleted/Missing Resource | Resource missing in Mnemosyne. | Call `update(read_missing, proposed, secCtx)`. | `WriteResult.AuthoritativeConflict<T>`. | 0 DB rows updated; reason indicates `EXPECTED_VERSION_MISMATCH`; `commitOutcome() == NOT_COMMITTED`. |
| **ST-07** | Cold Read followed by Governed UPDATE | Resource exists in DB (version 1), absent in Mneme (cache cold). | 1. `read(key)` loads DB & seeds Mneme.<br>2. Call `update()`. | `WriteResult.Committed<T>` (`CONVERGED`). | Cache seeded; update executes; DB version advanced to 2; cache converged to 2. |
| **ST-08** | Concurrent UPDATEs Racing on Active Token | Two threads obtain same `GovernedRead(v1)`. | Thread 1 and Thread 2 both invoke `update()`. | Thread 1: `Committed`.<br>Thread 2: `ActiveConflict`. | Exactly one thread commits; DB version is 2; no lost updates. |
| **ST-09** | Concurrent UPDATEs Cache Bypass Race | Thread 1 updates DB directly; Thread 2 uses `GovernedWriter`. | Thread 2 invokes `update(read_v1)`. | Thread 2 yields `AuthoritativeConflict`. | DB version mismatch detected by SQL conditional update; 0 rows updated; `EXPECTED_VERSION_MISMATCH`. |
| **ST-10** | Governed UPDATE with Mneme Unavailable | Mneme cluster down; failover policy enabled. | Call `update(read, proposed, secCtx)`. | `WriteResult.Committed<T>` (`DEGRADED`). | DB committed version 2; convergence records `DEGRADED`; `isCommitted() == true`. |
| **ST-11** | Governed UPDATE with Degraded Convergence | DB commit succeeds; Mneme crashes during convergence. | DB succeeds; cache fails. | `WriteResult.Committed<T>` (`DEGRADED`). | No exception thrown to caller; DB version is 2; `isCommitted() == true`. |
| **ST-12** | Governed Convergence CAS Race | Concurrent write populates Mneme during convergence loop. | Convergence encounters CAS failure on attempt 1. | Loop rereads and succeeds on attempt 2 (`CONVERGED`). | Cache converges to committed version without errors. |
| **ST-13** | Out-of-Order Convergence (Newer-Version Invariant) | Tx 2 (v3) converges before Tx 1 (v2). | Tx 1 attempts convergence with `v2`. | Convergence detects `cachedVersion (3) >= committedVersion (2)` and skips replacement. | The authoritative v2 write remains committed; cache retains version 3. |
| **ST-14** | Network Timeout during Authoritative Commit | DB connection drops while awaiting SQL `COMMIT` response. | Socket timeout on commit. | `WriteResult.OutcomeUnknown<T>`. | `commitOutcome() == UNKNOWN`; caller invokes reconciliation query; no blind retry. |
| **ST-15** | Themis Security Gate Deny | Caller lacks required authority. | Themis authorizer evaluates context. | `WriteResult.NotCommitted<T>` (or auth failure). | Operation aborted; zero DB mutations; audit log captures security rejection. |
| **ST-16** | Governed Lifecycle Deactivation | Active practitioner transitioned to `status=inactive`. | Call `update()` with deactivated entity. | `WriteResult.Committed<T>` (`CONVERGED`). | Processed strictly as conditional UPDATE; version advanced; zero SQL `DELETE`. |

---

## 19. Deferred Questions and Future Extensions

To maintain architectural focus and keep the core write contract small and robust, the following capabilities are explicitly deferred:

1. **Resource-Specific Clinical Consistency Tiers (Task 08.02B Correction)**:
   - Defining differentiated consistency tiers (e.g., immediate synchronous consistency for Patient/Encounter vs eventual consistency for Telemetry/Observations) is deferred to future subproject-specific policy specifications.
   - All governed resources currently follow the unified Strong Hybrid contract defined herein.
2. **Semantic / Three-Way Merge Conflicts**:
   - Automated field-level semantic merging of conflicting FHIR updates is deferred. The platform strictly enforces optimistic locking (first-committer-wins, loser-aborts).
3. **Multi-Resource Distributed Sagas**:
   - Cross-resource atomic transactions across multiple FHIR resource types (e.g., atomic multi-resource bundles) are deferred to dedicated Praxis saga orchestrators.
4. **Physical Archival and Retention Purge**:
   - Per ADR-020, regulatory data retention purge and offline archival are managed outside the application runtime framework.

---

## 20. References

- [ADR-018: Mnemosyne Authoritative Durable State Boundary](../architecture-decisions.md#adr-018-----mnemosyne-defines-the-authoritative-durable-state-boundary)
- [ADR-019: Mneme Distributed Resource Access and Coordination](../architecture-decisions.md#adr-019--mneme-owns-distributed-resource-access-and-coordination)
- [ADR-020: Lifecycle State Transitions vs Physical Deletion](../architecture-decisions.md#adr-020--governed-information-uses-lifecycle-state-rather-than-physical-deletion)
- [Harmonia Persistence Lifecycle Architecture](../architecture/persistence-lifecycle.md)
- [Mneme Concept Specification](../concepts/mneme.md)
- [Mnemosyne Concept Specification](../concepts/mnemosyne.md)
- [Themis Security Architecture](../security/architecture.md)
