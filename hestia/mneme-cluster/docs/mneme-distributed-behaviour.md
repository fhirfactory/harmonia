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

# Mneme Distributed Behaviour Laboratory & Architectural Characterisation

## 1. Executive Summary & Context

This document records the empirical findings of the **Mneme Distributed Behaviour Laboratory** within Harmonia (**Task 07 Step 04**). The laboratory characterizes, explores, and documents the actual distributed behavior of Mneme (Infinispan data grid and Hot Rod clients) in relation to **ADR-019** (*Distributed Resource Access and Coordination for Active State*) and **ADR-018** (*Rejection of Distributed Durability in Favor of Authoritative Persistence*).

The laboratory environment and characterisation research run against **Infinispan 15.0.3.Final** clustered via **JGroups 5.3.4.Final** over the binary Hot Rod protocol.

### The Invariant Distinction
- **Mneme**: Coordinates **active distributed working state** across nodes in cluster memory (`REPL_SYNC` / Hot Rod). Mneme state is fast, distributed, and reconstructable, but **never authoritative** and **never secretly local**.
- **Mnemosyne**: Commits and owns **authoritative durable state** in PostgreSQL/HAPI FHIR R5 JPA storage.

### Core Architectural Separation Principle
Throughout this characterisation, a strict boundary is maintained across three dimensions:
1. **OBSERVED CURRENT HARMONIA BEHAVIOUR**: What production services currently execute (`RemoteCache.put()` unconditional writes resulting in Last-Writer-Wins).
2. **AVAILABLE INFINISPAN CAPABILITY**: Verified capabilities exposed by Infinispan 15.0.3.Final Hot Rod clients (`replaceWithVersion`, `removeWithVersion`, value-conditional operations, and transactions).
3. **UNRESOLVED AUTHORITATIVE-WRITE / TASK 08 QUESTIONS**: Open architectural design questions bridging transient active-state coordination to persistent database commits.

---

## 2. Version Taxonomy in Harmonia

A critical finding of the laboratory is the clear decoupling between four distinct version concepts across the system stack:

```
+----------------------------------------------------------------------------------------------------+
|                                    HARMONIA VERSION TAXONOMY                                       |
+------------------------------+----------------------------------+----------------------------------+
| Dimension                    | Managed By                       | Architectural Purpose            |
+------------------------------+----------------------------------+----------------------------------+
| FHIR Resource Version        | Domain Application / FHIR Parser | Clinical/business domain version |
| (Resource.meta.versionId)    | (embedded inside JSON payload)   | tracking entity lifecycle diffs. |
+------------------------------+----------------------------------+----------------------------------+
| Infinispan Entry Version     | Infinispan Hot Rod Protocol      | Cluster-wide 64-bit concurrency  |
| (MetadataValue.getVersion()) | Engine (cluster memory metadata) | token for optimistic CAS updates.|
+------------------------------+----------------------------------+----------------------------------+
| Mnemosyne Durable Version    | PostgreSQL / JPA Persistence     | Authoritative committed sequence |
| (DB Sequence / JPA @Version) | (relational database engine)     | and ACID transaction boundary.   |
+------------------------------+----------------------------------+----------------------------------+
| HTTP / FHIR ETag             | REST Presentation Layer          | Transport-level optimistic check |
| (ETag: W/"<versionId>")      | (Pylai / Iris HTTP Gateway)      | in FHIR REST client interactions.|
+------------------------------+----------------------------------+----------------------------------+
```

### Detailed Taxonomy Breakdown

1. **FHIR Resource Version (`Resource.meta.versionId`)**:
   - **Type**: String (e.g. `"1"`, `"2"`, `"7"`).
   - **Scope**: Clinical domain payload.
   - **Characteristics**: Managed by the HAPI FHIR model or business transformation logic. It is embedded directly within the serialized JSON text. Infinispan treats this entire string as an opaque byte sequence; the data grid never inspects or parses the JSON payload to enforce business version rules.
2. **Infinispan Entry Version (`MetadataValue.getVersion()`)**:
   - **Type**: Numeric 64-bit `long` (opaque cluster token).
   - **Scope**: Hot Rod / Data Grid cluster memory metadata.
   - **Characteristics**: Managed natively by Infinispan on primary owner nodes. Every modification (`put`, `replaceWithVersion`) advances this token. It is non-contiguous and opaque to the application (must not assume arithmetic increments like `V+1`). Used exclusively for transient distributed Compare-And-Set (CAS) coordination.
3. **Mnemosyne Durable Version (JPA Sequence / Transaction Commit)**:
   - **Type**: Relational database sequence / `@Version` integer / timestamp.
   - **Scope**: Authoritative PostgreSQL storage.
   - **Characteristics**: Managed by the relational DBMS during JPA transaction commits. Decoupled from transient cache versions. Mnemosyne durable state survives total cluster restarts and cache wipes.
4. **HTTP / FHIR ETag (`ETag: W/"<versionId>"`)**:
   - **Type**: Weak or strong HTTP entity tag header.
   - **Scope**: Client-to-Gateway REST transport.
   - **Characteristics**: Corresponds to `Resource.meta.versionId` for `If-Match` / `If-None-Match` HTTP preconditions.

### Worked End-to-End Multi-Layer Version Lifecycle

```
External Client                  Iris / Gateway                Mneme (Hot Rod Cache)          Mnemosyne (PostgreSQL)
      |                                |                                |                                |
      | 1. HTTP GET /Patient/123       |                                |                                |
      |------------------------------->|                                |                                |
      |                                | 2. getWithMetadata("Patient/123")                              |
      |                                |------------------------------->|                                |
      |                                |    Returns: JSON (meta.v="1"), |                                |
      |                                |    Entry Version = 1001004     |                                |
      | 3. HTTP 200 OK                 |                                |                                |
      |    ETag: W/"1", Body: JSON     |                                |                                |
      |<-------------------------------|                                |                                |
      |                                |                                |                                |
      | 4. HTTP PUT /Patient/123       |                                |                                |
      |    If-Match: W/"1"             |                                |                                |
      |    Body: JSON (telecom update) |                                |                                |
      |------------------------------->|                                |                                |
      |                                | 5. Check If-Match == "1" (OK)  |                                |
      |                                | 6. replaceWithVersion("Patient/123", JSON_v2, 1001004)          |
      |                                |------------------------------->|                                |
      |                                |    [CAS Check on Entry Version 1001004]                         |
      |                                |    SUCCESS -> Entry Version becomes 1001009                     |
      |                                |<-------------------------------|                                |
      |                                |                                |                                |
      |                                | 7. Authoritative Write: Commit JPA Transaction                  |
      |                                |---------------------------------------------------------------->|
      |                                |    INSERT/UPDATE hfj_resource / ResourceTable                   |
      |                                |    Durable Version Sequence -> 504                              |
      |                                |<----------------------------------------------------------------|
      | 8. HTTP 200 OK                 |                                |                                |
      |    ETag: W/"2", Body: JSON_v2  |                                |                                |
      |<-------------------------------|                                |                                |
```

---

## 3. Laboratory Scenarios & Characterisation Findings

The laboratory executes against a real in-process clustered Infinispan environment (`EmbeddedCacheManager` nodes `node-1` and `node-2` with `REPL_SYNC` caches) bound to independent `HotRodServer` sockets and exercised by independent `RemoteCacheManager` Hot Rod clients (`Client-A`, `Client-B`, and `Client-C`).

### Scenario 01: Distributed Resource Visibility
- **Purpose**: Verify that state written by `Client-A` to `Node-1` is visible to an independent `Client-B` reading from `Node-2` over the Hot Rod wire protocol.
- **Participants**: `Client-A` (Hot Rod port 1), `Client-B` (Hot Rod port 2), `Node-1`, `Node-2`.
- **Actions**: `Client-A` executes `RemoteCache.put("Person/123", jsonPayload)`. `Client-B` executes `RemoteCache.getWithMetadata("Person/123")`.
- **Observed Behaviour**: `Client-B` immediately read the exact payload and received a valid Infinispan entry version token.
- **Infinispan Mechanism**: Hot Rod TCP `PUT` on `Node-1` $\rightarrow$ JGroups `REPL_SYNC` cluster replication $\rightarrow$ Hot Rod TCP `GET_WITH_METADATA` on `Node-2`.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 02: Update Propagation
- **Purpose**: Observe what happens when `Client-A` updates an existing distributed resource and how `Client-B` observes the update.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Initial state established. `Client-A` executes synchronous `RemoteCache.put("Person/456", updatedJson)`. `Client-B` executes `getWithMetadata("Person/456")`.
- **Observed Behaviour**: After `Client-A`'s synchronous `put()` returned, `Client-B`'s next read observed the updated value (`"Jones"`, FHIR version `2`) and the advanced entry version.
- **Infinispan Mechanism**: Synchronous replication guarantees that the update is replicated across all cluster members before the Hot Rod `PUT` operation returns success to the client.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 03: Uncoordinated Concurrent Update (Last-Writer-Wins Baseline)
- **Purpose**: Characterise what actually happens when two independent participants read the same distributed resource and subsequently update it using current unconditional operations (`RemoteCache.put`).
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**:
  1. Both `Client-A` and `Client-B` read initial state (`Person/123`, `Smith`, FHIR v7, Infinispan v1).
  2. `Client-A` writes Candidate A (`Jones`, FHIR v8) via `put()`.
  3. `Client-B` writes Candidate B (`Brown`, FHIR v8) via `put()`, starting from stale initial read.
  4. Both clients perform subsequent reads.
- **Observed Behaviour**: Both unconditional writes succeeded. `Client-B`'s write silently overwrote `Client-A`'s write. Final state across both clients was Candidate B (`Brown`).
- **Infinispan Mechanism**: Hot Rod `PUT` is unconditional by default; it does not check prior version or reject stale updates.
- **ADR-019 Assessment**:
  - Distributed resource availability: **DEMONSTRATED**
  - Distributed coordination / Stale-write prevention: **NOT CURRENTLY IMPLEMENTED IN PRODUCTION**

### Scenario 03-A (Part A): Multi-Field Uncoordinated Lost Update
- **Purpose**: Visibly demonstrate the multi-field lost update problem on complex JSON resources when using unconditional `put()`.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Initial state set for `Person/mf-101` with `telecom=555-0100` and `address=100 Main St`. `Client-A` reads initial state and updates `telecom -> 555-9999`. `Client-B` reads initial state and updates `address -> 200 Elm St`. Both write using `RemoteCache.put()`.
- **Observed Behaviour**: `Client-B`'s unconditional write silently overwrote the entire JSON record. `Client-A`'s `telecom` modification was completely erased (`555-0100` remained).
- **Infinispan Mechanism**: Unconditional `put()` transmits the full string payload and overwrites the target key without diffing or version validation.
- **ADR-019 Assessment**: **OBSERVED CURRENT BEHAVIOUR (Lost Update Risk)**

### Scenario 04: Experimental Infinispan Version / Conditional Update
- **Purpose**: Demonstrate whether Infinispan Hot Rod exposes entry version metadata and native conditional update operations (`replaceWithVersion`) capable of detecting and rejecting stale concurrent updates.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**:
  1. Both clients read initial state and capture Infinispan version `V1`.
  2. `Client-A` executes `replaceWithVersion("Person/456", candidateA, V1)`.
  3. `Client-B` executes `replaceWithVersion("Person/456", candidateB, V1)` using stale version `V1`.
- **Observed Behaviour**: `Client-A`'s conditional replace succeeded (`true`), advancing entry version to `V2`. `Client-B`'s stale update was rejected (`false`), preventing overwrite. Final distributed value remained Candidate A (`Jones`).
- **Infinispan Mechanism**: Hot Rod `REPLACE_IF_UNMODIFIED` (`replaceWithVersion`) evaluates entry version match on the cluster before applying modification.
- **ADR-019 Assessment**: **DEMONSTRATED** (*Available Infinispan Capability*) / **NOT CURRENTLY USED** (*Harmonia Production Services*)

### Scenario 04-A (Part B): Multi-Field Version-Aware Conditional Update
- **Purpose**: Demonstrate how `replaceWithVersion` protects multi-field resources from silent erasure.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Resource `Person/mf-102` initialized. Both clients capture version token `vInitial`. `Client-A` updates `telecom` via `replaceWithVersion(..., vInitial)`. `Client-B` attempts to update `address` via `replaceWithVersion(..., vInitial)`.
- **Observed Behaviour**: `Client-A` succeeded (`true`). `Client-B`'s stale update was rejected (`false`). `Client-A`'s `telecom` update (`555-9999`) was preserved.
- **Infinispan Mechanism**: Primary owner node validates entry version token; version mismatch triggers immediate `false` return without modifying cache entry.
- **ADR-019 Assessment**: **DEMONSTRATED (Available Capability)**

### Scenario 04-B (Part C): Conflict Resolution with Reread, Merge, and Retry (OCC Loop)
- **Purpose**: Demonstrate the complete optimistic concurrency control (OCC) resolution pattern in application code.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: `Person/mf-103` initialized. `Client-A` updates `telecom` via `replaceWithVersion`. `Client-B` attempt is rejected (`false`). `Client-B` then rereads latest state via `getWithMetadata`, merges its intended `address` update with the newly read `telecom` value, and retries `replaceWithVersion` using the new version token.
- **Observed Behaviour**: `Client-B`'s retried conditional replace succeeded (`true`). Final state preserved both `Client-A`'s `telecom` (`555-9999`) and `Client-B`'s `address` (`200 Elm St`).
- **Infinispan Mechanism**: Atomic CAS at data grid layer combined with client-side merge and retry logic.
- **ADR-019 Assessment**: **DEMONSTRATED (Recommended Pattern for Harmonia Workflows)**

### Scenario 04-C (Part D): Three-Way Distributed Concurrency (CAS Coordination)
- **Purpose**: Demonstrate atomic CAS coordination across three independent client participants (`Client-A`, `Client-B`, `Client-C`).
- **Participants**: `Client-A` (Node-1), `Client-B` (Node-2), `Client-C` (Node-1).
- **Actions**: All three clients read the same starting version token for `Person/mf-104`. All three attempt `replaceWithVersion` presenting the original token.
- **Observed Behaviour**: Exactly one participant succeeded (Client-A); the remaining two participants were rejected (`false`). All three participants converged on Candidate A.
- **Infinispan Mechanism**: Primary partition owner serializes Hot Rod CAS requests; the first commit advances the token, invalidating subsequent attempts.
- **ADR-019 Assessment**: **DEMONSTRATED (Available Capability)**

### Scenario 04-D (Part E): Same-Client vs Different-Client Stale Representations
- **Purpose**: Verify that version tokens are cluster-wide entry metadata rather than client-local tracking identifiers.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Evaluated stale version rejection when submitted by the same client instance (`Client-A`) vs an independent client instance (`Client-B`).
- **Observed Behaviour**: Stale version tokens were rejected identically (`false`) regardless of client instance identity.
- **Infinispan Mechanism**: Hot Rod version validation operates strictly on entry metadata stored in cluster memory.
- **ADR-019 Assessment**: **DEMONSTRATED (Available Capability)**

### Scenario 04-E (Part F): Conditional Removal via `removeWithVersion`
- **Purpose**: Demonstrate Hot Rod conditional deletion to prevent deleting concurrently modified records.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Resource `Person/mf-106` created. `Client-B` modifies the entry (advancing version). `Client-A` attempts `removeWithVersion` presenting the initial stale version token. `Client-A` then reads current version and retries `removeWithVersion`.
- **Observed Behaviour**: Stale removal returned `false` and entry was preserved. Current-version removal returned `true` and entry was removed.
- **Infinispan Mechanism**: Hot Rod `REMOVE_IF_UNMODIFIED` evaluates version equality prior to entry deletion.
- **ADR-019 Assessment**: **DEMONSTRATED (Available Capability)**

### Scenario 04-F (Part G): Value-Conditional Operations vs Version-Conditional Operations
- **Purpose**: Characterise Hot Rod `putIfAbsent`, `replace(key, oldValue, newValue)`, and `remove(key, expectedValue)`.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Exercised `putIfAbsent` for atomic creation, `replace` with exact string equality, and `remove` with expected string value.
- **Observed Behaviour**: `replace(k, old, new)` and `remove(k, expectedValue)` succeeded only when providing the exact byte/string payload; any non-identical payload resulted in rejection (Note: Infinispan value-comparison operations are strictly sensitive to serialized payload variations).
- **Infinispan Mechanism**: Full payload byte-comparison on the cluster node.
- **ADR-019 Assessment**: **DEMONSTRATED (Available Capability)**

### Scenario 05: Participant Failure (Surviving Client Resilience)
- **Purpose**: Observe distributed cluster state and surviving participants when one Hot Rod client participant terminates or disconnects.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: `Client-A` writes initial state. `Client-A` disconnects (`RemoteCacheManager.stop()`). Surviving `Client-B` reads and updates the resource on `Node-2`.
- **Observed Behaviour**: `Client-A` termination had zero impact on cluster state. Surviving `Client-B` continued normal read and write operations without interruption.
- **Infinispan Mechanism**: Hot Rod clients are stateless socket consumers; cluster state is held in the server data grid independently of client lifecycle.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 06: Cache Loss / Reconstructability
- **Purpose**: Demonstrate that Mneme state is non-authoritative working state; when cache state is cleared or lost, Mneme does not hold durable truth.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Working state established in cache. `RemoteCache.clear()` executed. Subsequent reads attempted by `Client-A` and `Client-B`.
- **Observed Behaviour**: Reads returned `null` (cache miss). No automatic reconstruction occurred in current implementation.
- **Infinispan Mechanism**: Cache clear evicts in-memory and store entries across the cluster.
- **ADR-019 Assessment**:
  - Non-authoritative working memory boundary: **DEMONSTRATED**
  - Automatic cache-aside read-through from Mnemosyne: **NOT CURRENTLY IMPLEMENTED** (Scheduled for Task 09)

### Scenario 07: Read-Dominant Workload (~10:1 Ratio)
- **Purpose**: Demonstrate the intended Mneme workload model: serving a small active resource subset with high read frequency and low write frequency (~10:1 ratio).
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Active set of 5 resources. Workload of 100 reads and 10 writes executed deterministically across alternating clients.
- **Observed Behaviour**: 100% cache hit rate (100/100 reads) served directly from cluster memory without disk or backend persistence round-trips. Observed ratio was exactly 10.0 : 1.
- **Infinispan Mechanism**: In-memory hash indexing on `REPL_SYNC` caches provides sub-millisecond Hot Rod socket reads.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 08: Mneme Unavailable (Fail-Explicit Semantics)
- **Purpose**: Verify Task 07 Step 03 invariant: when required Mneme infrastructure is unavailable, operations fail explicitly without falling back to process-local maps (`ConcurrentHashMap`).
- **Participants**: `Client-A`, `Server-1` (stopped).
- **Actions**: `Server-1` stopped. `Client-A` attempts `RemoteCache.put()`.
- **Observed Behaviour**: Operation immediately threw `HotRodClientException` / `TransportException`. Zero process-local fallback maps were populated.
- **Infinispan Mechanism**: Hot Rod client connection pool throws transport exceptions when backend endpoints are unreachable.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 09: Required Persistence Failure Semantics
- **Purpose**: Verify Task 07 Step 02 invariant: when required persistence operations fail in backing cache-stores (`FhirRestCacheStore` / `OperationsRestCacheStore`), the failure must not be swallowed.
- **Participants**: `FhirRestCacheStore`, `OperationsRestCacheStore`.
- **Actions**: Downstream HTTP 500 error encountered during store write.
- **Observed Behaviour**: `NonBlockingStore.write()` completed exceptionally with `PersistenceException`, propagating the error to the caller without false durability acknowledgement.
- **Infinispan Mechanism**: Infinispan NonBlockingStore SPI propagates asynchronous stage exceptions to the cache operation boundary.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 10: Recovery After Mneme Availability Returns
- **Purpose**: Verify that when Mneme availability is restored following an outage, clients cleanly reconnect and resume distributed operations without relying on stale process-local state.
- **Participants**: `Client-A`, `Server-1`.
- **Actions**: Outage simulated (server stopped $\rightarrow$ operation throws exception). Server restarted on same port. `Client-A` executes subsequent `put()` and `get()`.
- **Observed Behaviour**: Client automatically reconnected upon server restoration and resumed normal operations cleanly.
- **Infinispan Mechanism**: Hot Rod client channel pool re-establishes TCP connections upon subsequent request execution.
- **ADR-019 Assessment**: **DEMONSTRATED**

---

## 4. Deep Dive: Concurrent Updates, Version Coordination, and Distributed Concurrency

### 4.1 Observed Current Harmonia Behaviour: Multi-Field Uncoordinated Lost Updates

Harmonia production services currently invoke `RemoteCache.put(key, value)`. This operation is unconditional. When two distributed participants modify different attributes of a single resource simultaneously, the second write silently and completely obliterates the changes made by the first write.

```
Timeline: Uncoordinated Multi-Field Lost Update (Current Production Semantics)

Client-A (Node-1)                      Infinispan Cluster                       Client-B (Node-2)
   |                                           |                                       |
   |-- 1. get("Person/101") ------------------>|                                       |
   |   Returns: {telecom:"555-0100",           |                                       |
   |             address:"100 Main St"}        |                                       |
   |                                           |<-- 2. get("Person/101") --------------|
   |                                           |    Returns: {telecom:"555-0100",      |
   |                                           |              address:"100 Main St"}   |
   |                                           |                                       |
   |-- 3. put("Person/101", ------------------>|                                       |
   |          {telecom:"555-9999",             |                                       |
   |           address:"100 Main St"})         | [Cluster State: telecom=555-9999]     |
   |   Returns: OK                             |                                       |
   |                                           |-- 4. put("Person/101", -------------->|
   |                                           |          {telecom:"555-0100", (STALE) |
   |                                           |           address:"200 Elm St"})      |
   |                                           |                                       |
   |                                           | [Cluster State: telecom=555-0100,     |
   |                                           |                 address=200 Elm St]   |
   |                                           | Returns: OK                           |
   |                                           |                                       |
   |                                   * * * FINAL RESULT * * *                        |
   | Client-A's update (telecom: 555-9999) is COMPLETELY LOST without error or notice. |
```

### 4.2 Available Infinispan Capability: Version-Aware Optimistic Rejection

Using Infinispan Hot Rod `getWithMetadata()` and `replaceWithVersion()`, the data grid tracks a 64-bit cluster entry version. When Client-B presents a stale version token, the cluster rejects the modification atomically:

```
Timeline: Version-Aware Conditional Rejection (Available Infinispan Capability)

Client-A (Node-1)                      Infinispan Cluster                       Client-B (Node-2)
   |                                           |                                       |
   |-- 1. getWithMetadata("Person/102") ------>|                                       |
   |   Returns: Entry Version = 1001           |                                       |
   |                                           |<-- 2. getWithMetadata("Person/102") --|
   |                                           |    Returns: Entry Version = 1001      |
   |                                           |                                       |
   |-- 3. replaceWithVersion(key, JSON_A, 1001)|                                       |
   |------------------------------------------>|                                       |
   |                                           | [CAS Check: current (1001) == 1001]   |
   |                                           | [Commit: Entry Version -> 1002]       |
   |   Returns: true (SUCCESS)                 |                                       |
   |                                           |<-- 4. replaceWithVersion(k, JSON_B,   |
   |                                           |                          1001 [STALE])|
   |                                           | [CAS Check: current (1002) != 1001]   |
   |                                           | Returns: false (REJECTED) ------------>|
   |                                           |                                       |
   |                                   * * * FINAL RESULT * * *                        |
   | Client-A's update is PRESERVED. Client-B is NOTIFIED of conflict via return false. |
```

### 4.3 Optimistic Concurrency Resolution: Reread, Merge, and Retry Pattern

When `replaceWithVersion` returns `false`, application logic can resolve the conflict by reading current cluster state, applying its domain mutation on top of the latest state, and retrying `replaceWithVersion`:

```
Timeline: Complete OCC Resolution Loop (Reread + Merge + Retry)

Client-A (Node-1)                      Infinispan Cluster                       Client-B (Node-2)
   |                                           |                                       |
   |-- 1. replaceWithVersion(..., v1) -------->|                                       |
   |   Returns: true [Version -> v2]           |                                       |
   |                                           |<-- 2. replaceWithVersion(..., v1) ----|
   |                                           |    Returns: false (CONFLICT) -------->|
   |                                           |                                       |
   |                                           |<-- 3. getWithMetadata("Person/103") --|
   |                                           |    Returns: Value={telecom:555-9999}, |
   |                                           |             Version = v2              |
   |                                           |                                       |
   |                                           |    [Client-B Merges Local Delta:      |
   |                                           |     telecom: 555-9999 (from server)   |
   |                                           |     address: 200 Elm St (local)]      |
   |                                           |                                       |
   |                                           |<-- 4. replaceWithVersion(..., v2) ----|
   |                                           | [CAS Check: current (v2) == v2]       |
   |                                           | [Commit: Entry Version -> v3]         |
   |                                           | Returns: true (SUCCESS) ------------->|
   |                                           |                                       |
   |                                   * * * FINAL RESULT * * *                        |
   | Both logical modifications (telecom=555-9999, address=200 Elm St) are PRESERVED.   |
```

### 4.4 Multi-Participant Scalability & Client Independence

The laboratory experimentally verified:
1. **Three-Way CAS Serialization (Scenario 04-C)**: When `Client-A`, `Client-B`, and `Client-C` concurrently contend for the same starting version token, exactly one client succeeds and all subsequent contenders receive `false`.
2. **Cluster-Scoped Tokens (Scenario 04-D)**: Entry version tokens represent cluster-wide metadata on primary partition owners. A stale version token is rejected identically whether presented by the originating client or an independent peer.

### 4.5 Hot Rod Versioned Removal (`removeWithVersion`)

Infinispan Hot Rod provides `removeWithVersion(key, version)` (`REMOVE_IF_UNMODIFIED`). This prevents race conditions where one client attempts to delete an entry while another client concurrently updates it. If the entry version has advanced, the deletion returns `false` and the updated entry remains intact in cluster memory.

### 4.6 Value-Conditional Operations vs Version-Conditional Operations

Infinispan 15.0.3.Final exposes two distinct categories of conditional operations over Hot Rod:

```
+----------------------------------------------------------------------------------------------------+
|                      VALUE-CONDITIONAL vs VERSION-CONDITIONAL COMPARISON                           |
+------------------------------+----------------------------------+----------------------------------+
| Characteristic               | Value-Conditional (replace/remove)| Version-Conditional (replace/rem) |
+------------------------------+----------------------------------+----------------------------------+
| Evaluated Token              | Full serialized payload (bytes)  | 64-bit numeric long token        |
| Network Bandwidth            | Transmits entire Old + New bytes | Transmits New bytes + 8-byte int |
| Payload Sensitivity          | Sensitive to whitespace, formatting| Completely agnostic to JSON format|
| Mutation Detection           | Cannot detect ABA payload returns| Strictly monotonic / advancing   |
| Suitability for JSON/FHIR    | POOR (fragile string equality)   | EXCELLENT (opaque metadata CAS)  |
+------------------------------+----------------------------------+----------------------------------+
```

### 4.7 Research Analysis: Hot Rod Transactions (Optimistic & Pessimistic)

- **Protocol Support**: Hot Rod in Infinispan 15.0.3.Final supports distributed transactions via `RemoteCacheManager` configured with `TransactionMode.TRANSACTIONAL` and a JTA `TransactionManagerLookup`.
- **Server Requirements**: Requires server-side transactional cache configuration (`<transaction mode="NON_XA" locking="OPTIMISTIC"/>` or `FULL_XA`).
- **Conflict Handling**:
  - *Optimistic Mode*: Client buffers mutations locally; upon `commit()`, server performs write-skew checks on entry versions. Conflicts throw `RollbackException`.
  - *Pessimistic Mode*: Keys are locked across cluster nodes on read/write.
- **Laboratory Status**: **Documented Infinispan Capability (Not Executed in Lab)**.
- **Architectural Assessment for ADR-019**: Distributed 2PC/XA transactions over Hot Rod introduce substantial protocol latency, two-phase network handshakes, and client buffering. For Harmonia's single-resource clinical workflows and ~10:1 read-dominant model, distributed JTA transactions over Hot Rod represent unnecessary complexity and protocol overhead compared to lightweight `replaceWithVersion`.

### 4.8 Research Analysis: Pessimistic & Explicit Locking in Remote Architectures

- **Remote Hot Rod Boundary**: The Hot Rod `RemoteCache` API **does not expose explicit locking methods** such as `lock(K key)` or `tryLock()`. Those APIs exist exclusively in embedded Infinispan (`AdvancedCache.lock()`).
- **Implicit Pessimistic Locking**: Available remotely only inside transactional caches configured with pessimistic locking.
- **Operational Vulnerabilities**:
  - *Network Partitions & Client Crashes*: If a remote client acquires a lock and abruptly disconnects or crashes, the locked keys remain blocked across the cluster until the server-side lock acquisition timeout expires.
  - *Throughput Degradation*: Read-dominant caching (~10:1) suffers severely under pessimistic locks because concurrent readers and writers are blocked.
- **Laboratory Status**: **Documented Architectural Anti-Pattern (Not Supported over Remote Wire)**.

### 4.9 Research Analysis: Functional Map & Server-Side Execution Boundaries

- **Embedded Functional Map (`FunctionalMap`, `ReadWriteMap`)**: Allows passing lambdas (`readWriteMap.eval(key, function)`) to execute atomic mutations directly on primary owner nodes.
- **Boundary Restriction**: **Strictly Embedded Only**. Hot Rod does not support arbitrary Java lambdas over TCP wire sockets. Executing server-side logic remotely requires deploying application domain JARs onto the Infinispan server classpath via Infinispan Server Tasks (`RemoteCache.execute()`).
- **Architectural Assessment for ADR-019**: Deploying application domain models into the Mneme data grid cluster violates Harmonia's decoupling boundaries. Mneme must remain an agnostic data grid.

### 4.10 Mechanisms Comparative Matrix

The following matrix summarizes the technical characteristics of all investigated concurrency mechanisms in Infinispan 15.0.3.Final:

| Concurrency Mechanism | Scope | Hot Rod Wire Support | Conflict Detection | Latency / Blocking | Multi-Key Atomic | App Retry Needed | Current Harmonia Usage | ADR-019 Assessment | Caveats |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Unconditional `put()`** | Single Key | Yes | None (LWW) | Non-blocking | No | No | **Production Default** | Baseline Only | High risk of lost updates on multi-field objects. |
| **`putIfAbsent()`** | Single Key | Yes | Creation Only | Non-blocking | No | Yes (on conflict) | Available, Not Used | High for Cache Fill | Cannot coordinate subsequent updates. |
| **`replace(k, oldVal, newVal)`** | Single Key | Yes | Exact String Equality | Non-blocking | No | Yes | Available, Not Used | Low for JSON/FHIR | Fragile; fails on formatting/whitespace differences. |
| **`getWithMetadata` + `replaceWithVersion`** | Single Key | Yes | Cluster Entry Version | Non-blocking | No | Yes | **Laboratory Verified** | **Primary ADR-019 Model** | Application must implement OCC reread/merge/retry loop. |
| **`removeWithVersion`** | Single Key | Yes | Cluster Entry Version | Non-blocking | No | Yes | **Laboratory Verified** | High for Lifecycle | Prevents deleting concurrently updated entries. |
| **Optimistic Hot Rod Transactions** | Multi-Key | Yes | Write-Skew Check on Commit | Non-blocking until commit | Yes | Yes (on rollback) | Not Used | Low (Overhead) | Requires XA/JTA setup; 2PC protocol round-trip cost. |
| **Pessimistic Hot Rod Transactions** | Multi-Key | Yes | Cluster Lock on Key Access | Blocking | Yes | Deadlock Retry | Not Used | Anti-Pattern for ADR-019 | Blocks readers; vulnerability to abandoned locks on client crash. |
| **Hot Rod CAS Versioned Ops** | Single Key | Yes | Atomic Cluster Version CAS | Non-blocking | No | Yes | Target Model | High | Opaque numeric token; decoupled from business version. |
| **Functional Map API (`ReadWriteMap`)** | Single / Multi | **Embedded Only** | Server-side Lambda Execution | Non-blocking | Yes | No | Non-Applicable | Incompatible with Hot Rod | Requires deploying domain JARs to data grid server classpath. |

---

## 5. Classification of `OperationsAggregatorService.localPragmaStore`

During the architectural inspection of `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/OperationsAggregatorService.java` (lines 79, 584–647), the field `localPragmaStore` was evaluated under **ADR-019**:

- **Observed Code**:
  - `private final Map<String, PragmaSummary> localPragmaStore = new ConcurrentHashMap<>();`
  - `storePragma(PragmaSummary)` writes exclusively to `localPragmaStore`.
  - `getPragma(String)` reads from `localPragmaStore` first, and on miss reads `task-cache` and populates `localPragmaStore`.
  - `getPragmasForWorkflow(String)` merges entries from `localPragmaStore` with keys in `task-cache`.
- **Classification**: **Category B — Prohibited Process-Local Mneme Substitute**
- **Reasoning**:
  `Pragma` models active canonical workflow state. Storing pragmas exclusively in a process-local `ConcurrentHashMap` creates node-local memory silos. If multiple Iris BEFE instances run behind a load balancer, instances cannot observe pragmas created on sibling nodes.
- **Status**: Identified and classified. In accordance with laboratory rules, **no production code changes were made in Step 04**. Remediation is recommended for subsequent tasks.

---

## 6. ADR-019 Capability Assessment Matrix

| ADR-019 Capability | Assessment | Evidence / Notes |
| :--- | :--- | :--- |
| **Distributed Resource Availability** | **DEMONSTRATED** | Verified across Scenarios 01, 02, 05, 07 via `REPL_SYNC` and Hot Rod wire protocol. |
| **Distributed Coordination** | **NOT CURRENTLY IMPLEMENTED** | Production code uses unconditional `RemoteCache.put()`, resulting in last-writer-wins (Scenario 03, 03-A). |
| **Version Coordination** | **DEMONSTRATED (Available Capability)** / **NOT CURRENTLY USED** | Infinispan `replaceWithVersion()` successfully prevents stale overwrites (Scenario 04, 04-A to 04-F); production services do not yet invoke it. |
| **Read-Dominant Access** | **DEMONSTRATED** | 100% cache hit rate under 10:1 read/write workload without persistence round-trips (Scenario 07). |
| **Reconstructability** | **DEMONSTRATED (Boundary)** / **NOT CURRENTLY IMPLEMENTED (Cache-Aside)** | Non-authoritative boundary confirmed (Scenario 06); automatic point-read cache-aside from Mnemosyne belongs to Task 09. |
| **No Process-Local Substitution** | **DEMONSTRATED** | Step 03 fail-explicit invariant confirmed on cache outage (Scenario 08); `localPragmaStore` classified for remediation. |
| **Not a Durability Boundary** | **DEMONSTRATED** | ADR-018/ADR-019 boundary confirmed across cache loss (Scenario 06) and persistence failure propagation (Scenario 09). |

---

## 7. Unresolved Architectural Questions & Design Boundary for Task 08 (Authoritative Clinical Writes)

The findings of this laboratory establish that Infinispan Hot Rod natively supports robust, non-blocking optimistic concurrency control (`replaceWithVersion`). However, because Mneme is non-authoritative (ADR-018), transitioning Harmonia write workflows from unconditional `put()` to coordinated updates requires resolving explicit architectural questions in **Task 08**.

These items are recorded here as **open architectural questions for Task 08**, not solved implementations:

1. **Ordering of Coordination vs. Persistence**:
   - Should a clinical write commit to authoritative Mnemosyne PostgreSQL storage *first* and then update Mneme, or should optimistic coordination occur in Mneme *first* before triggering durable commit?
   - If persistence occurs first, what coordinates concurrent writers attempting to commit to the database?
2. **Dual-Write Partial Failure Windows**:
   - What is the failure recovery procedure if Mneme optimistic coordination succeeds (`replaceWithVersion` returns `true`), but the subsequent Mnemosyne database write fails (e.g. database timeout, constraint violation, or disk full)?
   - Conversely, what is the recovery procedure if Mnemosyne commits durable state successfully, but the subsequent Mneme cache update fails or is partitioned?
3. **Cross-Layer Optimistic Version Alignment**:
   - How should a failure in Mnemosyne's durable JPA version check (e.g., `OptimisticLockException` or HTTP `412 Precondition Failed`) be reflected back to the Mneme active cache entry?
   - How does the application layer map an Infinispan entry version mismatch to an external FHIR REST `ETag` / `If-Match` precondition failure?
4. **Cache Invalidation vs. Cache Write-Through**:
   - On an authoritative write, should the mutating service perform a coordinated `replaceWithVersion` with the new working state (write-through), or issue a `removeWithVersion` (invalidation) and let subsequent readers reconstruct working state on demand via Task 09 cache-aside?
5. **Conflict Resolution & Merge Ownership**:
   - In which layer of the Harmonia processing pipeline should OCC conflict detection trigger a reread, merge, and retry loop (e.g., inside the Ergon activity execution engine, gateway ingress handlers, or specific entity repository wrappers)?
   - What is the maximum retry threshold before returning an explicit concurrency error to the caller?
6. **Authoritative State Verification on Cold Start & Reconstruction**:
   - Since Mneme working state is non-authoritative and reconstructable (ADR-018/ADR-019), how does a newly started node distinguish between a cold cache miss and a stale cached entry following an isolated network partition?
