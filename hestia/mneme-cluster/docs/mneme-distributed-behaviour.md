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

### The Invariant Distinction
- **Mneme**: Coordinates **active distributed working state** across nodes in cluster memory (`REPL_SYNC` / Hot Rod). Mneme state is fast, distributed, and reconstructable, but **never authoritative** and **never secretly local**.
- **Mnemosyne**: Commits and owns **authoritative durable state** in PostgreSQL/HAPI FHIR R5 JPA storage.

---

## 2. Distinction of Version Dimensions

A central finding of the laboratory is the clear decoupling between three distinct version concepts across the system:

| Version Dimension | Representation | Managed By | Semantics |
| :--- | :--- | :--- | :--- |
| **FHIR Resource Version** | `Resource.meta.versionId` (e.g. `"1"`, `"2"`, `"7"`) | Domain / Application / FHIR Parser | Domain business entity version embedded inside the JSON text payload. Opaque to Infinispan. |
| **Infinispan Entry Version** | `MetadataValue.getVersion()` / `Metadata.version` (numeric `long`) | Infinispan Hot Rod Protocol Engine | Native cluster-wide version number tracking entry cache lifecycle. Used by Hot Rod for optimistic concurrency (`replaceWithVersion`). |
| **Mnemosyne Durable Version** | Database sequence / JPA transaction entity version | Mnemosyne JPA Persistence Engine | Authoritative durable persistence sequence. Decoupled from transient cache versions. |

---

## 3. Laboratory Scenarios & Characterisation Findings

The laboratory executes against a real in-process clustered Infinispan environment (`EmbeddedCacheManager` nodes `node-1` and `node-2` with `REPL_SYNC` caches) bound to independent `HotRodServer` sockets and exercised by independent `RemoteCacheManager` Hot Rod clients (`Client-A` and `Client-B`).

### Scenario 01: Distributed Resource Visibility
- **Purpose**: Verify that state written by `Client-A` to `Node-1` is visible to an independent `Client-B` reading from `Node-2` over the Hot Rod wire protocol.
- **Participants**: `Client-A` (Hot Rod port 1), `Client-B` (Hot Rod port 2), `Node-1`, `Node-2`.
- **Actions**: `Client-A` executes `RemoteCache.put("Person/123", jsonPayload)`. `Client-B` executes `RemoteCache.getWithMetadata("Person/123")`.
- **Observed Behaviour**: `Client-B` immediately read the exact payload and received the valid Infinispan entry version.
- **Infinispan Mechanism**: Hot Rod TCP `PUT` on `Node-1` $\rightarrow$ JGroups `REPL_SYNC` cluster replication $\rightarrow$ Hot Rod TCP `GET_WITH_METADATA` on `Node-2`.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 02: Update Propagation
- **Purpose**: Observe what happens when `Client-A` updates an existing distributed resource and how `Client-B` observes the update.
- **Participants**: `Client-A`, `Client-B`, `Node-1`, `Node-2`.
- **Actions**: Initial state established. `Client-A` executes synchronous `RemoteCache.put("Person/456", updatedJson)`. `Client-B` executes `getWithMetadata("Person/456")`.
- **Observed Behaviour**: After `Client-A`'s synchronous `put()` returned, `Client-B`'s next read observed the updated value (`"Jones"`, FHIR version `2`) and the advanced entry version.
- **Infinispan Mechanism**: Synchronous replication guarantees that the update is replicated across all cluster members before the Hot Rod `PUT` operation returns success to the client.
- **ADR-019 Assessment**: **DEMONSTRATED**

### Scenario 03: Uncoordinated Concurrent Update (Last-Writer-Wins)
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
  - Distributed coordination / Stale-write prevention: **NOT CURRENTLY IMPLEMENTED**

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

## 4. Classification of `OperationsAggregatorService.localPragmaStore`

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

## 5. ADR-019 Capability Assessment Matrix

| ADR-019 Capability | Assessment | Evidence / Notes |
| :--- | :--- | :--- |
| **Distributed Resource Availability** | **DEMONSTRATED** | Verified across Scenarios 01, 02, 05, 07 via `REPL_SYNC` and Hot Rod wire protocol. |
| **Distributed Coordination** | **NOT CURRENTLY IMPLEMENTED** | Production code uses unconditional `RemoteCache.put()`, resulting in last-writer-wins (Scenario 03). |
| **Version Coordination** | **DEMONSTRATED (Available Capability)** / **NOT CURRENTLY USED** | Infinispan `replaceWithVersion()` successfully prevents stale overwrites (Scenario 04); production services do not yet invoke it. |
| **Read-Dominant Access** | **DEMONSTRATED** | 100% cache hit rate under 10:1 read/write workload without persistence round-trips (Scenario 07). |
| **Reconstructability** | **DEMONSTRATED (Boundary)** / **NOT CURRENTLY IMPLEMENTED (Cache-Aside)** | Non-authoritative boundary confirmed (Scenario 06); automatic point-read cache-aside from Mnemosyne belongs to Task 09. |
| **No Process-Local Substitution** | **DEMONSTRATED** | Step 03 fail-explicit invariant confirmed on cache outage (Scenario 08); `localPragmaStore` classified for remediation. |
| **Not a Durability Boundary** | **DEMONSTRATED** | ADR-018/ADR-019 boundary confirmed across cache loss (Scenario 06) and persistence failure propagation (Scenario 09). |

---

## 6. Forward Architectural Gaps

1. **Task 08 (Authoritative Clinical Writes & Coordination)**:
   - Production services must transition from unconditional `RemoteCache.put()` to optimistic concurrency coordination using `replaceWithVersion()`.
   - Write paths must explicitly coordinate FHIR resource versions (`meta.versionId`), Infinispan entry versions, and Mnemosyne durable transaction commits.
2. **Task 09 (Authoritative-Backed Read-Through / Cache-Aside)**:
   - When a cache miss occurs (`Scenario 06`), client read services must implement a formal cache-aside lookup against authoritative Mnemosyne REST/JPA endpoints to reconstruct working state.
3. **Iris BEFE Remediation**:
   - Refactor `OperationsAggregatorService.localPragmaStore` to write through to Mneme `task-cache` or `pragma-cache` rather than maintaining a node-local `ConcurrentHashMap`.
