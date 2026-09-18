# Concept: Mneme `[IMPLEMENTED]`

Mneme is Harmonia's high-speed, distributed in-memory data grid within the Hestia subsystem, providing sub-millisecond ephemeral state management, cache clustering, and asynchronous write-behind staging for active clinical integration tasks.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Μνήμη* (Mneme)
- **Etymology**: Ancient Greek feminine noun meaning memory, remembrance, or the faculty of recall.
- **Mythological Context**: In the ancient Boeotian tradition of Mount Helicon, before the nine canonical Olympian Muses were recognized, there were three elder Muses: *Aoide* (song), *Melete* (practice/deliberation), and *Mneme* (memory). Mneme represented the immediate, active, and accessible working memory necessary for oral poets and rhapsodes to recite epic verse in real time.
- **Architectural Rationale**: Just as human thought requires an ultra-fast working memory (RAM) distinct from deep archival recollection, Harmonia requires an ultra-fast, distributed in-memory cache grid to coordinate sub-millisecond task transitions, checkpoint snapshots, and duplicate checks without repeatedly incurring the latency of disk-bound relational databases.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Mneme is powered by **Infinispan 15.0.3.Final**, deployed in a clustered, fault-tolerant topology communicating via JGroups and the Hot Rod binary protocol:

```
+---------------------------------------------------------------------------------------+
|                                    MNEME CACHE GRID                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +---------------------------------------+   +------------------------------------+  |
|   |         INFINISPAN NODE 1             |   |         INFINISPAN NODE 2          |  |
|   |                                       |   |                                    |  |
|   | * Hot Rod Binary Server (:11222)      |   | * Hot Rod Binary Server (:11223)   |  |
|   | * JGroups Cluster Discovery (:7800)   |<==|=> JGroups Cluster Discovery (:7801)|  |
|   |                                       |   |                                    |  |
|   | Caches:                               |   | Caches:                            |  |
|   |  - task-cache (Replicated / TTL)      |   |  - task-cache (Replicated / TTL)   |  |
|   |  - tasksequence-cache (Replicated)    |   |  - tasksequence-cache (Replicated) |  |
|   |  - dedup-cache (Distributed / LRU)    |   |  - dedup-cache (Distributed / LRU) |  |
|   +---------------------------------------+   +------------------------------------+  |
|                        |                                         |                    |
|                        +====================+====================+                    |
|                                             |                                         |
|                                             v                                         |
|                   +---------------------------------------------------+               |
|                   |       mneme-persistence (NonBlockingStore SPI)    |               |
|                   |   - Asynchronous write-behind cache store adapter |               |
|                   |   - Offloads committed tasks to Mnemosyne JPA     |               |
|                   +---------------------------------------------------+               |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

### Primary In-Memory Caches `[CONFIGURED]`
1. **`task-cache`**: Stores ephemeral `Pragma` task execution states during active workflow execution. Synchronously replicated across grid nodes.
2. **`tasksequence-cache`**: Stores validated `Praxis` workflow blueprints seeded at startup. Provides sub-microsecond blueprint lookups for worker threads.
3. **`dedup-cache`**: Stores message control IDs and payload hashes in an LRU sliding-window buffer, protecting gateways from processing duplicate MLLP frames.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Mneme Owns
- Infinispan 15.0.3 clustered data grid configuration (`infinispan.xml`).
- JGroups network discovery and inter-node replication protocols (`port 7800`).
- Hot Rod binary RPC server endpoints (`port 11222`, `11223`).
- Custom Infinispan `NonBlockingStore` SPI implementations for asynchronous write-behind staging (`mneme-persistence`).
- Ephemeral in-flight task caching and sliding window deduplication buffers.

### What Mneme Explicitly Does NOT Own (Anti-Responsibilities)
- Permanent historical record archiving (owned by Mnemosyne).
- Authoritative FHIR R5 relational schema generation (owned by Mnemosyne Clinical).
- Consumer loop orchestration or worker thread allocation (owned by Ponos).
- Network protocol termination like MLLP (owned by Pylai).

---

## 4. Key Classes & Configuration `[IMPLEMENTED]`

| Component | Module Name | Role | Status |
| :--- | :--- | :--- | :--- |
| **Cluster Topology** | `mneme-cluster` | Infinispan configuration and container packaging | `[CONFIGURED]` |
| **Cache Store SPI** | `mneme-persistence` | Custom non-blocking write-behind store implementation | `[IMPLEMENTED]` |
| `FhirRestCacheStore` | `mneme-persistence` | Asynchronously commits cached FHIR resources to Mnemosyne | `[IMPLEMENTED]` |
| `OperationsCacheStore`| `mneme-persistence`| Asynchronously commits task telemetry to Mnemosyne Operations | `[IMPLEMENTED]` |

### Network Ports `[CONFIGURED]`
- Port `11222`: Primary Hot Rod binary client endpoint (Infinispan Node 1).
- Port `11223`: Secondary Hot Rod binary client endpoint (Infinispan Node 2).
- Port `7800`: JGroups TCP cluster discovery and state transfer (Node 1).
- Port `7801`: JGroups TCP cluster discovery and state transfer (Node 2).
