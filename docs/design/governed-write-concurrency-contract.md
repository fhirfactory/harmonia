# Governed Write and Concurrency Contract `[DESIGNED/PLANNED]`

This document is the authoritative engineering and architectural specification for **Harmonia's Governed Write and Concurrency Contract**. It formalises the Strong Hybrid persistence and concurrency architecture across the platform, establishing strict contracts between caller workflows (Pylai, Energeia Ponos/Erga/Praxis, Iris BEFE) and the storage subsystems: **Mneme** (distributed in-memory cache and active coordination grid) and **Mnemosyne** (authoritative relational JPA persistence).

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
Under [ADR-018](../architecture-decisions.md#adr-018--mnemosyne-defines-the-authoritative-durable-state-boundary):
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
package net.fhirfactory.harmonia.hestia.governance.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Unique identifier for a governed resource.
 */
public record ResourceKey(
    String resourceType,
    String resourceId
) {
    public ResourceKey {
        Objects.requireNonNull(resourceType, "resourceType must not be null");
        Objects.requireNonNull(resourceId, "resourceId must not be null");
    }

    public String toQualifiedPath() {
        return resourceType + "/" + resourceId;
    }
}

/**
 * Opaque coordination token representing an active entry state in Mneme (Infinispan).
 * Carries NO arithmetic meaning and must never be incremented by client code.
 */
public record ActiveCoordinationToken(
    long opaqueToken
) {
    public static final ActiveCoordinationToken ABSENT = new ActiveCoordinationToken(-1L);

    public boolean isPresent() {
        return opaqueToken != -1L;
    }
}

/**
 * Authoritative version expected in Mnemosyne durable persistence.
 */
public record ExpectedAuthoritativeVersion(
    long versionNumber
) {
    public static final ExpectedAuthoritativeVersion NONE = new ExpectedAuthoritativeVersion(0L);

    public boolean isNewResource() {
        return versionNumber == 0L;
    }
}

/**
 * Immutable envelope representing the result of a governed read.
 */
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion,
    Instant readTimestamp
) {
    public GovernedRead {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(activeToken, "activeToken must not be null");
        Objects.requireNonNull(authoritativeVersion, "authoritativeVersion must not be null");
        Objects.requireNonNull(readTimestamp, "readTimestamp must not be null");
    }
}
```

### 4.2 Read Semantics and Cold Reads
1. **Cache Hit (Warm Read)**:
   - The reader issues a Hot Rod `getWithMetadata(key)` to Mneme.
   - If the entry exists, `GovernedRead<T>` is constructed using the cached payload, the opaque entry version from `MetadataValue.getVersion()` as `ActiveCoordinationToken`, and the extracted persistence version as `ExpectedAuthoritativeVersion`.
2. **Cache Miss (Cold Read)**:
   - The reader queries Mnemosyne durable storage.
   - If found, Mnemosyne returns the persisted entity and its database `versionId`.
   - The reader seeds Mneme using a conditional `putIfAbsent` or loads metadata, obtaining a fresh `ActiveCoordinationToken`.
   - If not found, `GovernedRead<T>` is not produced; a `ResourceNotFoundException` or empty optional is returned.

---

## 5. CREATE Contract

The `create` primitive establishes a new authoritative resource in Mnemosyne and initializes its representation in Mneme.

```java
public interface GovernedWriter {
    <T> WriteResult<T> create(
        ResourceKey key, 
        T resource, 
        GovernedWriteContext context
    ) throws ActiveStateConflictException, AuthoritativeStateConflictException;
    
    <T> WriteResult<T> update(
        GovernedRead<T> current, 
        T proposed, 
        GovernedWriteContext context
    ) throws ActiveStateConflictException, AuthoritativeStateConflictException;
}
```

### 5.1 Strict Uniqueness & No Upsert
- **No Upsert Semantics**: CREATE asserts that no prior entity with the given `ResourceKey` exists in durable persistence.
- **Duplicate Rejection**: If an entity with the specified identifier already exists in Mnemosyne, CREATE fails immediately with an `AuthoritativeStateConflictException` (`DUPLICATE_RESOURCE`). It **MUST NOT** overwrite or update the existing entity.

### 5.2 End-to-End CREATE Sequence
```
Caller                GovernedWriter           Themis            Mneme (Cache)       Mnemosyne (DB)
  |                         |                    |                     |                   |
  |--- create(key, res) --->|                    |                     |                   |
  |                         |-- authorize(res) ->|                     |                   |
  |                         |<- PERMIT ----------|                     |                   |
  |                         |                                          |                   |
  |                         |-- putIfAbsent(key, res) ---------------->|                   |
  |                         |<- OK (or cache unavailable) -------------|                   |
  |                         |                                                              |
  |                         |-- insertResource(key, res, version=1) ---------------------->|
  |                         |<- Committed (version=1) -------------------------------------|
  |                         |                                                              |
  |                         |-- converge(key, res, v=1) -------------->|                   |
  |                         |<- Converged -----------------------------|                   |
  |                         |                                                              |
  |<- WriteResult(v=1) -----|                                                              |
```

1. **Phase 1: Authorization**: Evaluate `ThemisSecurityContext` against default-deny policies for `CREATE` on the target resource type.
2. **Phase 2: Active Coordination Check**: Execute `putIfAbsent` on Mneme. If an active entry already exists, verify its authoritative status.
3. **Phase 3: Authoritative Insert**: Execute an atomic SQL `INSERT` into Mnemosyne with initial `version_id = 1`. If a unique constraint violation occurs, abort and throw `AuthoritativeStateConflictException`.
4. **Phase 4: Guarded Convergence**: Update Mneme with the committed version 1 representation via CAS or put.
5. **Phase 5: Return Result**: Return `WriteResult<T>` with status `COMMITTED_CONVERGED` and committed version `1`.

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
  |                    |   (key, prop, token)   |                    |                       |
  |                    |<- CAS Success / Fail --|                    |                       |
  |                    |   [If Fail: Throw ActiveStateConflictException]                     |
  |                    |                        |                    |                       |
  |=== Phase 3: Security & Business Validation ==============================================|
  |                    |-- authorize(UPDATE) ----------------------->|                       |
  |                    |<- PERMIT -----------------------------------|                       |
  |                    |                                                                     |
  |=== Phase 4: Authoritative Persistence Commit ============================================|
  |                    |-- conditionalUpdate(key, prop, expectedVer=N) --------------------->|
  |                    |   [Atomic: UPDATE ... SET ver=N+1 WHERE id=id AND ver=N]            |
  |                    |<- Success: CommittedVersion = N+1 ----------------------------------|
  |                    |   [If 0 rows: Throw AuthoritativeStateConflictException]            |
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

Mneme (Infinispan 15.0.3) provides distributed in-memory concurrency coordination via Hot Rod client metadata and CAS primitives.

### 7.1 Opaque Tokens (Task 08.02B Correction)
- **Opaque Contract**: The `ActiveCoordinationToken` encapsulates the 64-bit version returned by Infinispan's `MetadataValue.getVersion()`.
- **No Arithmetic Semantics**: Client code, gateways, and workflow engines **MUST NOT** assign arithmetic meaning to this token (e.g., assuming `token_2 = token_1 + 1`). Hot Rod version identifiers are internal generation tokens, not sequential counters.
- **CAS Primitive**: Active coordination uses `RemoteCache.replaceWithVersion(key, value, activeToken.opaqueToken())`.

### 7.2 Non-Authoritative Boundary (Task 08.02B Correction)
- **Token Consumption $\ne$ Authority**: Successfully executing `replaceWithVersion` on Mneme coordinates only the active in-flight cache state. It does **not** grant permission to perform an unconditional database write.
- **Strict Precondition**: The subsequent authoritative write to Mnemosyne **must still** supply and verify `ExpectedAuthoritativeVersion`.

### 7.3 Active Coordination Failure Modes
1. **CAS Rejection (`false`)**: Another concurrent thread or cluster node updated the cache entry between the read and the coordination phase.
   - *Action*: Throw `ActiveStateConflictException`. The caller may reread and retry.
2. **Entry Evicted/Missing**: The cache entry expired or was evicted between read and update.
   - *Action*: Fall back directly to the authoritative persistence phase (conditional database write), followed by cache reload during convergence.
3. **Mneme Cluster Partition / Outage**: Hot Rod communication fails with timeout or connection exception.
   - *Action*: If configured for strict active coordination, raise `ActiveStateConflictException`. If configured for high-availability fallback, proceed directly to Mnemosyne conditional update, marking convergence as `DEGRADED_CACHE_UNAVAILABLE`.

---

## 8. Authoritative Persistence Contract

Mnemosyne defines the durable persistence boundary, implemented via PostgreSQL 16 and HAPI FHIR JPA entities (`FhirResourceEntity`).

### 8.1 Conditional Update Specification

```java
package net.fhirfactory.harmonia.hestia.governance.port;

import net.fhirfactory.harmonia.hestia.governance.model.*;

public interface MnemosynePersistencePort {

    <T> CommittedAuthoritativeVersion insertResource(
        ResourceKey key,
        T resource,
        GovernedWriteContext context
    ) throws AuthoritativeStateConflictException;

    <T> CommittedAuthoritativeVersion conditionalUpdate(
        ResourceKey key,
        T resource,
        ExpectedAuthoritativeVersion expectedVersion,
        GovernedWriteContext context
    ) throws AuthoritativeStateConflictException;
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
  - `updatedRows == 0`: Precondition failed. The entity was either mutated by a concurrent transaction or deleted. The transaction rolls back and throws `AuthoritativeStateConflictException`.

---

## 9. Conflict Model

Harmonia distinguishes three mutually exclusive write conflict and error types:

```
                                  WRITE ERROR TAXONOMY
                                            |
        +-----------------------------------+-----------------------------------+
        |                                   |                                   |
        v                                   v                                   v
+-----------------------+       +-------------------------+       +--------------------------+
|  ActiveStateConflict  |       | AuthoritativeStateConf. |       |   CommitOutcomeUnknown   |
+-----------------------+       +-------------------------+       +--------------------------+
| * Mneme Hot Rod CAS   |       | * Mnemosyne PostgreSQL  |       | * Ambiguous network      |
|   mismatch or race.   |       |   optimistic lock fail. |       |   timeout during commit. |
| * Durable DB state    |       | * Duplicate CREATE key. |       | * DB state unconfirmed.  |
|   NOT modified.       |       | * Durable DB advanced.  |       | * Caller must reconcile. |
| * Fast retry possible.|       | * Re-read required.     |       | * Do NOT auto-retry.     |
+-----------------------+       +-------------------------+       +--------------------------+
```

### 9.1 Conflict Classification Matrix

| Conflict Type | Layer Encountered | Cause | State of Database | Recommended Caller Remediation |
| :--- | :--- | :--- | :--- | :--- |
| `ActiveStateConflict` | Mneme (Infinispan) | Active coordination CAS token mismatch; concurrent cache write. | Unmodified | Immediate transparent retry (re-read active state and re-attempt CAS). |
| `AuthoritativeStateConflict` | Mnemosyne (PostgreSQL) | Database row version $\ne$ `ExpectedAuthoritativeVersion`, or duplicate ID on CREATE. | Advanced past expected version | Business abort or full re-read and domain-level merge. |
| `CommitOutcomeUnknown` | Transport / Network | Socket timeout or connection drop during SQL `COMMIT`. | Ambiguous (may be committed or rolled back) | Idempotent reconciliation check using correlation/causation ID before retrying. |

---

## 10. Result Model

Every governed write returns an immutable `WriteResult<T>`.

### 10.1 Java Result Model Shapes

```java
package net.fhirfactory.harmonia.hestia.governance.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public enum WriteStatus {
    COMMITTED_CONVERGED,
    COMMITTED_CONVERGENCE_DEGRADED,
    CONFLICT,
    UNKNOWN_OUTCOME,
    FAILED
}

public enum ConvergenceStatus {
    CONVERGED,
    DEGRADED_CACHE_UNAVAILABLE,
    DEGRADED_CACHE_CAS_EXHAUSTED,
    SKIPPED
}

public record CommittedAuthoritativeVersion(
    long versionNumber
) {
    public CommittedAuthoritativeVersion {
        if (versionNumber <= 0L) {
            throw new IllegalArgumentException("Committed version must be positive");
        }
    }
}

public record WriteResult<T>(
    ResourceKey key,
    T resource,
    WriteStatus status,
    CommittedAuthoritativeVersion committedVersion,
    ActiveCoordinationToken activeToken,
    ConvergenceStatus convergenceStatus,
    Instant commitTimestamp,
    String correlationId
) {
    public WriteResult {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(convergenceStatus, "convergenceStatus must not be null");
    }

    public boolean isSuccessful() {
        return status == WriteStatus.COMMITTED_CONVERGED 
            || status == WriteStatus.COMMITTED_CONVERGENCE_DEGRADED;
    }
}
```

### 10.2 Commit Success with Degraded Convergence (INV-04)
If the database commit succeeds in Mnemosyne, durability has been achieved. If Mneme subsequently fails to converge (e.g., cache node crash, Hot Rod timeout, or CAS retry limit reached), the result is:
- `WriteStatus` = `COMMITTED_CONVERGENCE_DEGRADED`
- `ConvergenceStatus` = `DEGRADED_CACHE_UNAVAILABLE` or `DEGRADED_CACHE_CAS_EXHAUSTED`
- The method **returns normally** and does **not** throw an exception.

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
When a database commit call fails due to a network timeout or connection reset:
1. **Do NOT assume failure**: The SQL `COMMIT` may have reached PostgreSQL and committed before the ACK was lost.
2. **Do NOT assume success**: The transaction may have rolled back.
3. **Raise `CommitOutcomeUnknownException`**: The exception encapsulates:
   - `ResourceKey`
   - `ExpectedAuthoritativeVersion`
   - `GovernedWriteContext` (including `correlationId` and `causationId`)
4. **Idempotent Reconciliation**:
   - The calling workflow (Ergon / Ponos) must execute a deterministic reconciliation check before retrying.
   - The reconciliation query checks Mnemosyne for an entity matching `ResourceKey` where `last_causation_id == context.causationId()`.
   - If found, the commit succeeded; the workflow proceeds. If not found and the version is unchanged, the workflow safely retries.

---

## 14. Security and Provenance Context

To prevent context proliferation and duplicate models, the governed write contract directly reuses Harmonia's existing security and provenance models.

### 14.1 Reused Context Records

```java
package net.fhirfactory.harmonia.hestia.governance.model;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import java.time.Instant;
import java.util.Objects;

/**
 * Carries security, provenance, and correlation context for governed writes.
 */
public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem,
    Instant requestTimestamp
) {
    public GovernedWriteContext {
        Objects.requireNonNull(securityContext, "securityContext must not be null");
        Objects.requireNonNull(correlationId, "correlationId must not be null");
        Objects.requireNonNull(causationId, "causationId must not be null");
        Objects.requireNonNull(requestTimestamp, "requestTimestamp must not be null");
    }
}
```

- **Security Gate**: Evaluates `ThemisAuthorizer.evaluate(securityContext, request)` before mutating state.
- **Audit Logging**: Successful and failed mutations dispatch non-PHI audit events to `ThemisAuditService` referencing `correlationId` and `causationId`.

---

## 15. API Placement and Service Boundaries

The governed write contract is distributed across subprojects according to strict architectural responsibilities:

```
+-----------------------------------------------------------------------------------+
|                                 MODULE PLACEMENT                                  |
|                                                                                   |
|  [ Calliope / hestia-api ]                                                        |
|  * GovernedReader, GovernedWriter (Public Contracts)                             |
|  * GovernedRead, WriteResult, ResourceKey, Token Records                          |
|  * ActiveStateConflictException, AuthoritativeStateConflictException              |
|                                                                                   |
|  [ hestia :: mneme-cluster / mneme-core ]                                         |
|  * MnemeActiveCoordinator (Internal Hot Rod CAS & Token Adapter)                  |
|  * MnemeConvergencePort (Guarded CAS Loop Implementation)                         |
|                                                                                   |
|  [ hestia :: mnemosyne-clinical / mnemosyne-core ]                                |
|  * MnemosynePersistencePort (Conditional SQL / JPA Update Adapter)                |
|  * FhirResourceEntity / FhirResourceRepository                                    |
|                                                                                   |
|  [ hestia :: hestia-governance ]                                                  |
|  * GovernedWriteManager (Coordinates 5-Phase UPDATE & CREATE pipelines)          |
|                                                                                   |
|  [ Callers: pylai-fhir-registry, energeia-erga, iris-befe ]                       |
|  * Uses GovernedWriter exclusively. Zero direct cache/DB mutations.               |
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
Subsequent implementation tasks will introduce ArchUnit rules asserting:
1. `noClasses().that().resideOutsideOfPackage("..hestia..").should().dependOnClassesThat().resideInAPackage("org.infinispan.client.hotrod..")`
2. `noClasses().that().resideOutsideOfPackage("..hestia.governance..").should().callMethod("..FhirResourceRepository", "save..")`

---

## 17. Developer Usage Examples

### 17.1 Example 1: Idiomatic Governed CREATE

```java
public WriteResult<Practitioner> registerPractitioner(
    Practitioner practitioner, 
    ThemisSecurityContext securityCtx
) {
    ResourceKey key = new ResourceKey("Practitioner", practitioner.getIdElement().getIdPart());
    GovernedWriteContext writeCtx = new GovernedWriteContext(
        securityCtx, 
        UUID.randomUUID().toString(), 
        UUID.randomUUID().toString(), 
        "Pylai-Gateway", 
        Instant.now()
    );

    try {
        return governedWriter.create(key, practitioner, writeCtx);
    } catch (AuthoritativeStateConflictException e) {
        log.warn("Practitioner already exists: {}", key);
        throw new DuplicateResourceException("Resource already exists", e);
    } catch (ActiveStateConflictException e) {
        log.warn("Active coordination conflict during registration: {}", key);
        throw new RetryableException("Concurrent registration attempt", e);
    }
}
```

### 17.2 Example 2: Idiomatic Governed Read-Modify-Write UPDATE

```java
public WriteResult<Practitioner> updatePractitionerContact(
    ResourceKey key, 
    ContactPoint newTelecom, 
    ThemisSecurityContext securityCtx
) {
    GovernedWriteContext writeCtx = new GovernedWriteContext(
        securityCtx, 
        UUID.randomUUID().toString(), 
        UUID.randomUUID().toString(), 
        "Iris-Administration", 
        Instant.now()
    );

    int maxRetries = 3;
    for (int i = 0; i < maxRetries; i++) {
        GovernedRead<Practitioner> current = governedReader.read(key)
            .orElseThrow(() -> new ResourceNotFoundException("Practitioner not found: " + key));

        Practitioner proposed = current.resource().copy();
        proposed.addTelecom(newTelecom);

        try {
            return governedWriter.update(current, proposed, writeCtx);
        } catch (ActiveStateConflictException e) {
            log.info("Active coordination contention on {}, retrying ({}/{})", key, i + 1, maxRetries);
        } catch (AuthoritativeStateConflictException e) {
            log.warn("Authoritative conflict on {}, aborting update", key);
            throw new PreconditionFailedException("Resource modified by concurrent transaction", e);
        }
    }
    throw new ConcurrencyLimitExceededException("Failed to update practitioner after retries: " + key);
}
```

---

## 18. Contract Test Plan

The following 16 mandatory test scenarios verify every facet of the governed write contract.

| Scenario ID | Name | Preconditions | Execution Sequence | Expected Outcome | Mandatory Assertions |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **ST-01** | Governed CREATE on Non-Existent Resource | Resource does not exist in Mneme or Mnemosyne. | Call `create(key, res, ctx)`. | Success (`COMMITTED_CONVERGED`). | DB row inserted with `version_id=1`; Mneme entry present with `version=1`; `WriteResult.committedVersion=1`. |
| **ST-02** | Governed CREATE on Existing Resource | Resource already exists in Mnemosyne with `version_id=1`. | Call `create(key, res, ctx)`. | Abort with `AuthoritativeStateConflictException`. | No database modification; no duplicate row; Mneme untouched. |
| **ST-03** | Governed UPDATE (Happy Path) | Resource exists at version 1; `GovernedRead` obtained. | Call `update(read_v1, proposed, ctx)`. | Success (`COMMITTED_CONVERGED`). | DB `version_id` advanced to 2; Mneme entry updated with version 2; `WriteResult.committedVersion=2`. |
| **ST-04** | Governed UPDATE with Stale Active Token | Active token modified in Mneme by concurrent thread after read. | Call `update(read_v1, proposed, ctx)`. | Abort at Phase 2 with `ActiveStateConflictException`. | Mneme CAS fails; Mnemosyne DB is **NOT** called; version remains 1. |
| **ST-05** | Governed UPDATE with Stale DB Version | Active token valid, but DB version advanced to 2 by out-of-band write. | Call `update(read_v1, proposed, ctx)`. | Abort at Phase 4 with `AuthoritativeStateConflictException`. | Mneme CAS executed; DB conditional update updates 0 rows; DB transaction rolls back. |
| **ST-06** | Governed UPDATE on Deleted/Missing Resource | Resource missing in Mnemosyne. | Call `update(read_missing, proposed, ctx)`. | Abort with `AuthoritativeStateConflictException`. | 0 DB rows updated; error indicates `RESOURCE_NOT_FOUND`. |
| **ST-07** | Cold Read followed by Governed UPDATE | Resource exists in DB (version 1), absent in Mneme (cache cold). | 1. `read(key)` loads DB & seeds Mneme.<br>2. Call `update()`. | Success (`COMMITTED_CONVERGED`). | Cache seeded; update executes; DB version advanced to 2; cache converged to 2. |
| **ST-08** | Concurrent UPDATEs Racing on Active Token | Two threads obtain same `GovernedRead(v1)`. | Thread 1 and Thread 2 both invoke `update()`. | Thread 1: Success.<br>Thread 2: `ActiveStateConflictException`. | Exactly one thread commits; DB version is 2; no lost updates. |
| **ST-09** | Concurrent UPDATEs Cache Bypass Race | Thread 1 updates DB directly; Thread 2 uses `GovernedWriter`. | Thread 2 invokes `update(read_v1)`. | Thread 2 fails at Phase 4 (`AuthoritativeStateConflict`). | DB version mismatch detected by SQL conditional update; 0 rows updated. |
| **ST-10** | Governed UPDATE with Mneme Unavailable | Mneme cluster down; failover policy enabled. | Call `update(read, proposed, ctx)`. | Success (`COMMITTED_CONVERGENCE_DEGRADED`). | DB committed version 2; convergence records `DEGRADED_CACHE_UNAVAILABLE`. |
| **ST-11** | Governed UPDATE with Degraded Convergence | DB commit succeeds; Mneme crashes during convergence. | Phase 4 succeeds; Phase 5 throws Hot Rod exception. | Returns `WriteResult` with `COMMITTED_CONVERGENCE_DEGRADED`. | No exception thrown to caller; DB version is 2; audit logs degraded state. |
| **ST-12** | Governed Convergence CAS Race | Concurrent write populates Mneme during convergence loop. | Phase 5 encounters CAS failure on attempt 1. | Loop rereads and succeeds on attempt 2 (`CONVERGED`). | Cache converges to committed version without throwing errors. |
| **ST-13** | Out-of-Order Convergence (Newer-Version Invariant) | Tx 2 (v3) converges before Tx 1 (v2). | Tx 1 attempts convergence with `v2`. | Convergence detects `cachedVersion (3) >= committedVersion (2)`. | Convergence aborts write; cache retains version 3. |
| **ST-14** | Network Timeout during Authoritative Commit | DB connection drops while awaiting SQL `COMMIT` response. | Phase 4 encounters socket timeout. | Throws `CommitOutcomeUnknownException`. | Context preserved; caller invokes reconciliation query; no blind retry. |
| **ST-15** | Themis Security Gate Deny | Caller lacks `Practitioner.Edit` authority. | Phase 3 Themis authorizer evaluates context. | Throws `ThemisAuthorisationException` (`DENY`). | Operation aborted; zero DB mutations; audit log captures security rejection. |
| **ST-16** | Governed Lifecycle Deactivation | Active practitioner transitioned to `status=inactive`. | Call `update()` with deactivated entity. | Success (`COMMITTED_CONVERGED`). | Processed strictly as conditional UPDATE; version advanced; no SQL `DELETE`. |

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

- [ADR-018: Mnemosyne Authoritative Durable State Boundary](../architecture-decisions.md#adr-018--mnemosyne-defines-the-authoritative-durable-state-boundary)
- [ADR-019: Mneme Distributed Resource Access and Coordination](../architecture-decisions.md#adr-019--mneme-owns-distributed-resource-access-and-coordination)
- [ADR-020: Lifecycle State Transitions vs Physical Deletion](../architecture-decisions.md#adr-020--governed-information-uses-lifecycle-state-rather-than-physical-deletion)
- [Harmonia Persistence Lifecycle Architecture](../architecture/persistence-lifecycle.md)
- [Mneme Concept Specification](../concepts/mneme.md)
- [Mnemosyne Concept Specification](../concepts/mnemosyne.md)
- [Themis Security Architecture](../security/architecture.md)
