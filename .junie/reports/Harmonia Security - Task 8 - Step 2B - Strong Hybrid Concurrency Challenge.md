Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Investigate the repository evidence needed for Step 08.02B architecture analysis only. Focus on: (1) actual Infinispan/Hot Rod mechanisms present or available in current Harmonia code/dependencies for lightweight coordination separate from cached clinical payload; (2) any existing use or tests of getWithMetadata, replaceWithVersion, putIfAbsent, removeWithVersion, metadata values, conditional operations, server-side compute/entry processors, transient markers, or separate coordination caches/keys; (3) any ADR/documentation text in repo relevant to ADR-019/Mneme distributed coordination or reconstructability; (4) file paths and precise line numbers for the most relevant supporting classes/tests/docs so final analysis can cite them directly. Also explicitly note what is NOT available or not evidenced in the current deployment model.

**Constraints**  
Read-only exploration only. Do not modify code. Do not run broad builds. Prefer static tracing and narrow inspection. The attached Task 08.01 report findings are already known; extend beyond them rather than repeating them.



Exploration is Ready

**Step 08.02B — Repository Evidence Report (read-only)**

No production code, schema, configuration, or tests were modified. No build or test command was run.

**1. Current Infinispan/Hot Rod version and topology**

- `hestia/mneme-cluster/pom.xml:44-60` declares `infinispan-core`, `infinispan-commons`, `infinispan-server-hotrod`, and `infinispan-client-hotrod`.
- `hestia/mneme-cluster/pom.xml:62-65` declares JGroups `5.3.4.Final`.
- `hestia/mneme-cluster/docs/mneme-distributed-behaviour.md:22-24` identifies the laboratory/deployment target as Infinispan `15.0.3.Final`, JGroups `5.3.4.Final`, and the binary Hot Rod protocol.
- `hestia/mneme-cluster/src/main/resources/infinispan.xml:56-58` defines a clustered cache container with transport cluster `hie-fhir-cluster` and configurable node name.
- The production cache definitions are replicated and synchronous. For example, `infinispan.xml:60-72` defines `person-cache` as `mode="SYNC"`; the same pattern is used for the clinical caches through line 248 and operations caches through line 287.
- `infinispan.xml:290-318` exposes Hot Rod and REST connectors, with Hot Rod on port `11222`.
- Kubernetes currently deploys two separate Infinispan StatefulSets (`deployment/kubernetes/base/hestia/mneme-cluster.yaml:63-127` and `:129-193`) with persistent container data mounts, while each node is configured to target Mnemosyne clinical/operations HTTP services (`:87-92`, `:153-157`). Docker Compose similarly defines two Infinispan nodes and Hot Rod ports (`docker-compose.yml:248-287`).

The laboratory topology is more concrete than the production deployment: `InfinispanLaboratoryServer.java:47-95` starts two embedded clustered nodes, two Hot Rod servers, and binds independent Hot Rod clients to the servers. `:110-117` shows the client configuration using `ClientIntelligence.BASIC`, separate endpoints, and a normal `RemoteCacheManager`.

**2. Direct evidence for available conditional mechanisms**

The strongest direct evidence is the laboratory test suite:

- `MnemeConcurrencyAndVersionScenarioTest.java:388-395` reads the same entry through two clients using `getWithMetadata()` and captures `MetadataValue.getVersion()`.
- `:401-429` demonstrates `replaceWithVersion()` success for the first participant and rejection (`false`) for a stale participant. The test explicitly characterizes this as server-side `REPLACE_IF_UNMODIFIED` against entry metadata.
- `:435-442` asserts the winning update is preserved and the stale update does not overwrite it.
- `:473-535` demonstrates the application-level reread/reapply/retry pattern after a stale CAS rejection.
- `:580-647` demonstrates three participants presenting the same token: exactly one succeeds and the other two receive `false`.
- `:678-734` confirms that a stale token is rejected regardless of whether it is presented by the original client or another client.
- `:763-790` and the remainder of that test demonstrate `removeWithVersion()` rejecting stale removal after another participant advances the entry version.
- The same test includes value-conditional operations and `putIfAbsent` scenarios; the consolidated characterization is documented at `mneme-distributed-behaviour.md:212-218`.

The repository’s summary of these results is especially precise at `mneme-concurrency-and-information-integrity.md:181-200`: Hot Rod exposes an opaque version token through `getWithMetadata()`, and conditional replace/remove operations reject stale tokens, but this only establishes active-cache stale-state detection, not the authoritative Task 08 write protocol.

The version taxonomy at `mneme-distributed-behaviour.md:40-79` is also important:

- FHIR `Resource.meta.versionId` is a domain payload value.
- `MetadataValue.getVersion()` is an opaque Infinispan cluster token.
- Mnemosyne’s durable version is database/JPA state.
- HTTP/FHIR ETag/If-Match is a transport-level concept.

The document explicitly says these must not be treated as interchangeable.

**3. What the current production code actually uses**

The current application-facing cache service remains unconditional:

- `iris/iris-befe/.../FhirCacheService.java:138-142` performs ordinary `remoteCache.get(id)`.
- `:152-157` performs ordinary `remoteCache.put(id, jsonPayload)`.
- `:160-192` increments the FHIR payload’s `meta.versionId` in application memory and then writes it with the unconditional cache put.
- `:195-199` performs ordinary `remoteCache.remove(id)`.

This is consistent with the laboratory’s production characterization at `mneme-distributed-behaviour.md:30-34`, `:139-151`, and `:274-276`: current services use unconditional `RemoteCache.put()` and therefore have last-writer-wins behavior; CAS is verified capability, not current production behavior.

A repository-wide source search found no production implementation of a separate coordination cache/key, generation token, fencing token, attempt marker, metadata-only coordination object, or Infinispan entry processor.

The only substantial `getWithMetadata`, `replaceWithVersion`, `removeWithVersion`, and related usages are the Mneme laboratory tests and documentation. There is no evidence that production clients currently retain or propagate Hot Rod entry-version tokens.

**4. Active representation versus coordination metadata**

**4.1 Native metadata is not an application metadata bag**

The inspected Infinispan 15.0.3.Final classes show:

- Hot Rod `MetadataValue<V>` is only a `VersionedValue<V>` plus `Metadata` (`MetadataValue.class:6-9`).
- Hot Rod `RemoteCache` exposes `getWithMetadata`, `replaceWithVersion`, `removeWithVersion`, and value-conditional operations (`RemoteCache.class:24-53`, `:71-77`).
- Embedded `Metadata` contains lifespan, max-idle, and an `EntryVersion`, with a builder for those fields (`Metadata.class:12-48`). It does not provide an arbitrary application-defined metadata map.
- The embedded `AdvancedCache` API can accept Infinispan `Metadata` (`AdvancedCache.class:91-119`), but that is not the API used by the production-facing Hot Rod clients, and it still does not provide a general custom coordination metadata field.

Therefore, a conceptual `resource -> committed FHIR representation + coordination generation` cannot currently be implemented as an extra arbitrary Hot Rod metadata field attached to the same remote clinical entry. It would need either:

1. a separate cache entry whose value is a small coordination object/string; or
2. a single value object containing both resource and coordination data, which is undesirable because it couples coordination mutation to the cached clinical representation and risks publishing/overwriting payload state.

**4.2 Separate cache is technically plausible but not currently present**

A separate coordination cache is not defined in `infinispan.xml`. Every currently defined cache is a clinical FHIR cache or an operations cache, and every shown cache has a persistence store:

- Clinical caches use `FhirRestCacheStore`, e.g. `infinispan.xml:60-72`.
- Operations caches use `OperationsRestCacheStore`, e.g. `:250-274`.
- The configuration test asserts that production XML contains 17 replicated caches (`InfinispanClusterConfigTest.java:133-147`).

Using a coordination marker/key inside an existing FHIR cache would be unsafe under the current persistence model. `FhirRestCacheStore.write()` resolves every key to a FHIR resource coordinate and sends the value to Mnemosyne (`hestia/mneme-persistence/.../FhirRestCacheStore.java:99-121`). Its key mapping is explicit at `:191-199`: a key containing `/` is interpreted as `resourceType/id`; otherwise the cache name determines the resource type. Thus an arbitrary coordination marker in a clinical cache would be treated as clinical resource JSON and sent toward Mnemosyne rather than remaining non-authoritative coordination state.

A genuinely separate coordination cache would need an explicit server-side definition with no clinical/operations persistence store, appropriate text/object encoding, and a client/service path that obtains that cache. No such definition or deployment wiring currently exists. The current configuration does not evidence a cache-local way to opt one key out of the configured store.

**5. `putIfAbsent`, value CAS, and version CAS**

The available mechanisms have materially different semantics:

- `putIfAbsent` is suitable for first creation/marker installation only. It does not serialize later progression unless all subsequent operations use another conditional mechanism.
- `replace(key, oldValue, newValue)` and `remove(key, expectedValue)` are available over Hot Rod, but compare the complete serialized value. The laboratory records this as byte/string-sensitive at `mneme-distributed-behaviour.md:212-218` and classifies the mechanism as fragile for JSON/FHIR at `:422-432`.
- `replaceWithVersion` and `removeWithVersion` compare an opaque server-maintained entry-version token. They are the strongest native single-key CAS primitives evidenced by this repository.
- Infinispan entry versions are not application generation numbers. `mneme-distributed-behaviour.md:68-75` states they are opaque, non-contiguous, and must not be treated as arithmetic values such as `V+1`.

For a separate coordination entry, the most directly supported pattern would therefore be an ordinary small marker value plus Hot Rod entry-version CAS on that marker entry. The CAS token would be the Infinispan metadata version, not a durable clinical version and not necessarily a monotonic application generation.

**6. Compute, entry processors, server-side execution, and locking**

**6.1 `RemoteCache.compute` exists, but it is not server-side atomic lambda execution**

The inspected Infinispan client class is important here:

- The common `BasicCache` API includes `compute`, `computeIfPresent`, and `computeIfAbsent` (`BasicCache.class:16-57`).
- However, `RemoteCacheImpl.computeAsync()` implements the operation by calling `getWithMetadataAsync`, applying the supplied function in the client JVM, then attempting `replaceWithVersionAsync` or `putIfAbsentAsync`; on failure it recursively retries (`RemoteCacheImpl.class:291-319`).
- The implementation of `computeIfPresentAsync` follows the same client-side metadata/CAS pattern (`RemoteCacheImpl.class:334-350`).

Thus `RemoteCache.compute` can be useful as a client-side optimistic CAS loop for a small coordination object, but it is not a server-side entry processor and is not a single server-executed function. The remapping function may be re-executed under contention, so it must be retry-safe and side-effect-free.

**6.2 Embedded compute/functional map is not the current remote deployment model**

The embedded `AdvancedCache` API has compute methods (`AdvancedCache.class:123-173`) and explicit lock methods (`:71-75`), but production services use Hot Rod `RemoteCache`, not an embedded `AdvancedCache` reference.

The repository’s own architecture characterization states:

- Functional Map/ReadWriteMap is embedded-only; arbitrary Java lambdas cannot be sent over the Hot Rod wire (`mneme-distributed-behaviour.md:412-416`).
- `RemoteCache.execute()` exists as a named server-task invocation (`RemoteCache.class:137-145`; implementation `RemoteCacheImpl.class:564-583`), but no registered server task, `ServerTask`, `TaskContext`, or task engine implementation was found in Harmonia source.
- The repository explicitly rejects deploying application domain JARs into the data-grid server for this purpose (`mneme-distributed-behaviour.md:414-416`).

Accordingly, server-side compute/entry-processor coordination is not evidenced as available in the current Harmonia deployment model.

**6.3 Explicit remote locking is not available**

`AdvancedCache.lock()` is an embedded API only. The repository records at `mneme-distributed-behaviour.md:403-410` that Hot Rod `RemoteCache` does not expose `lock`/`tryLock`; remote pessimistic locking requires a configured transactional cache. The current `infinispan.xml` cache definitions contain no transaction configuration, and no production client is configured with a transactional `RemoteCacheManager`.

The Mneme module does declare transaction-related dependencies (`mneme-cluster/pom.xml:67-76`), but that is dependency availability, not enabled deployment behavior. The laboratory documentation identifies Hot Rod transactions as documented capability but not executed in the lab and rejects them for this architecture because of XA/JTA/2PC and locking complexity (`mneme-distributed-behaviour.md:393-401`).

**7. Persistence and reconstructability evidence**

ADR evidence is consistent and explicit:

- ADR-018 says Mnemosyne/PostgreSQL is durable application-state persistence, while Mneme/Infinispan provides distributed availability, coordination, concurrency/version management, and reconstructable active state (`docs/architecture-decisions.md:186-195`).
- ADR-018 makes Mnemosyne the authoritative boundary and says successful cache operations are not durable acceptance (`:201-233`).
- ADR-019 says Mneme owns distributed resource access and coordination, but not distributed durability; a coordination decision or working version is not authoritative acceptance (`docs/architecture-decisions.md:475-512`).
- ADR-019 specifically states that appropriate Infinispan conditional/versioned operations, atomic replacement, or locking are implementation choices beneath the Mneme contract (`:545-562`).
- Process-local substitution is expressly forbidden: loss of Mneme must be visible as loss of distributed availability/coordination (`:581-607`).

The current laboratory demonstrates cache loss as a non-authoritative boundary, but also records that automatic cache-aside reconstruction is not currently implemented (`mneme-distributed-behaviour.md:228-237`). `MnemeFailureAndRecoveryScenarioTest.java:202-258` verifies explicit Hot Rod failure without a local-map fallback; `:260-310` verifies client operation resumption after the server returns.

One deployment nuance is important for a future separate coordination cache: clinical cache stores are configured with `shared="false"`, `preload="false"`, `fetch-state="false"`, and `segmented="false"` (`infinispan.xml:66-71`, repeated throughout). `FhirRestCacheStore.clear()` explicitly says cache clear does not wipe the FHIR store (`FhirRestCacheStore.java:143-147`). This supports the non-authoritative boundary, but there is no current automatic point-read reconstruction path in the repository.

**8. Evidence relevant to a generational/CAS design**

The repository proves the following narrow guarantee for existing clinical entries:

> Given a current entry-version token, exactly one of competing `replaceWithVersion` operations presenting that same token can succeed; subsequent stale presentations are rejected, without requiring client identity.

That is a real distributed CAS race decision, but the existing tests mutate the clinical value itself. They do not prove a marker-only progression where the clinical payload remains unchanged. No test currently covers:

- a separate coordination cache;
- a separate coordination key alongside a clinical key;
- a coordination marker with TTL/lifespan;
- a consumed generation left behind after the winner fails before Mnemosyne;
- cache restart/reset while a writer is in flight;
- fencing across a cache reset;
- conditional post-commit refresh/invalidation against a newer committed result;
- a second CAS token whose payload is deliberately not clinical state.

The laboratory’s open Task 08 questions acknowledge this gap at `mneme-distributed-behaviour.md:466-488`, including ordering of Mneme coordination versus persistence, partial failures on either side, cross-layer version alignment, cache refresh/invalidation, and cold-start distinction.

**9. Direct implications for the Step 08.02B design space**

1. **A separate coordination key in an existing clinical cache is not viable under the current cache-store wiring.** It would be interpreted and persisted as FHIR state by `FhirRestCacheStore`.
2. **A separate coordination cache is technically expressible in Infinispan XML, but is not currently deployed, tested, or client-wired.** It would need a non-persistent configuration so its marker values cannot become Mnemosyne clinical records.
3. **Native Hot Rod version CAS is available and cluster-wide.** It can coordinate a marker entry without replacing a clinical representation, provided the marker is in an appropriate separate cache/key space.
4. **Hot Rod compute is not a hidden server-side primitive.** In this version it is a client-side read/apply/CAS retry loop. It is implementable for pure retry-safe marker transitions but does not remove the network/CAS retry model.
5. **No native arbitrary metadata field is evidenced for remote clients.** Infinispan metadata exposes expiry and internal version concepts, not a custom application generation field.
6. **No remote explicit lock is available outside transactions, and transactions are not configured.** Introducing them would cross the complexity tripwire identified by the repository’s own documentation.
7. **A cache restart can discard the active coordination state.** ADR-018/019 make this acceptable only if Mnemosyne independently rejects stale authoritative predecessors. The repository has no fencing epoch or durable coordination recovery mechanism that would make Mneme loss itself authoritative.
8. **The existing laboratory evidence supports a mandatory active-state CAS protocol as a possible stronger Mneme role, but does not yet establish the winner-failure, reset, or post-commit convergence protocol.** Those are design questions, not currently implemented guarantees.

**10. Most relevant citation index for the parent report**

- `docs/architecture-decisions.md:136-233` — ADR-018 authority/reconstructability boundary.
- `docs/architecture-decisions.md:420-512` — ADR-019 ownership, non-authority, and coordination contract.
- `docs/architecture-decisions.md:545-562` — permitted Infinispan coordination mechanisms and Mnemosyne final authority.
- `docs/architecture-decisions.md:581-607` — fail-explicit/no process-local Mneme substitute.
- `hestia/mneme-cluster/pom.xml:36-76` — Infinispan, Hot Rod, JGroups, and transaction dependencies.
- `hestia/mneme-cluster/src/main/resources/infinispan.xml:56-72` — cache container and representative synchronous persisted clinical cache.
- `hestia/mneme-cluster/src/main/resources/infinispan.xml:250-287` — operations caches and current complete cache pattern.
- `hestia/mneme-cluster/src/main/resources/infinispan.xml:290-318` — Hot Rod/REST server endpoint deployment.
- `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java:99-147` — clinical cache writes/deletes/clear semantics.
- `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java:191-199` — key-to-FHIR coordinate mapping.
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java:138-199` — current unconditional get/put/remove path and local FHIR version increment.
- `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java:388-443` — version metadata and stale CAS rejection.
- Same test `:473-548` — reread/reapply/retry after CAS conflict.
- Same test `:580-648` — exactly-one winner among three participants.
- Same test `:678-734` — same-client/different-client stale token rejection.
- Same test `:737-790` — version-aware conditional removal.
- `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/support/InfinispanLaboratoryServer.java:47-117` — two embedded nodes, Hot Rod servers, and independent clients.
- `hestia/mneme-cluster/docs/mneme-distributed-behaviour.md:38-79` — version taxonomy and opaque token distinction.
- Same document `:306-432` — CAS, transaction, locking, functional-map, and mechanism comparison.
- Same document `:466-488` — unresolved Task 08 bridge questions.
- `hestia/mneme-cluster/docs/mneme-concurrency-and-information-integrity.md:163-220` — laboratory conclusions and limits of active-state conflict detection.
- Same document `:348-365` — separate FHIR/domain, Infinispan, durable, and HTTP version concepts.

**Discoveries for future steps**

- No build/test command was run; exploration was static plus decompilation of locally resolved Infinispan `15.0.3.Final` classes.
- The decisive deployment constraint is the existing `FhirRestCacheStore`: arbitrary coordination values in current clinical caches are interpreted as FHIR resource JSON and sent to Mnemosyne, so a marker-only design requires a separately configured non-persistent cache.
- `RemoteCache.compute` in the resolved Hot Rod client is a client-side `getWithMetadata` → local function → `replaceWithVersion`/`putIfAbsent` retry loop, not server-side atomic compute; any function must tolerate re-execution.
- Existing laboratory tests prove clinical-entry CAS only; there are no tests for marker-only CAS, consumed generations, cache-reset fencing, or post-commit conditional convergence.



ANALYSIS

**Task description**  
Using the repository evidence already mapped for Task 08.01 plus the explored Infinispan/Hot Rod capabilities, produce an architecture-only analysis for Step 08.02B. Determine whether Mneme can have a real, mandatory, non-authoritative concurrency/race-coordination responsibility that is stronger than a TOCTOU stale check, while Mnemosyne remains the atomic authoritative persistence boundary. Be extremely precise about guarantees and failure modes. Structure the analysis around the deliverable sections requested in the issue, and especially address:
- precise candidate definitions of what it means for Mneme to own concurrency;
- whether a separate active-coordination state distinct from cached clinical payload is viable in current Harmonia/Infinispan architecture;
- generation/CAS vs lock semantics;
- winner failure scenarios;
- reconstruction/bypass/reset implications;
- post-commit convergence race protection;
- stale-read/read-after-write consistency;
- active-state vs authoritative-state conflict distinctions;
- external If-Match vs internal expected predecessor;
- uncertain commit outcome and retry/idempotency implications;
- revised options comparison and a supportable preferred architecture, if any.

Be disciplined:
- architecture/design only;
- do not propose implementation commits or schema changes as already-decided work;
- do not rely on distributed locks, durable fencing epochs, 2PC, rollback services, or recovery daemons;
- explicitly flag where a candidate would cross the complexity tripwire;
- distinguish OBSERVED repository evidence from ARCHITECTURAL CONCLUSION and OPEN QUESTION.

**Constraints**  
Read-only synthesis only. No code changes. No builds. Keep the reasoning technically narrow and avoid unsupported superlatives.



**Step 08.02B — Strong Hybrid Concurrency Architecture Analysis**

**1. Problem Restatement**

The central design dilemma is maximizing overall system utility and data integrity across distributed clinical workflows without incurring the catastrophic failure modes of distributed locks or uncommitted dirty reads.

In the initial Task 08.02 analysis, Option D reduced Mneme’s role to an optional, non-mutating pre-flight check (`getWithMetadata` followed by client-side token comparison). Because reading a version token does not serialize subsequent operations, two concurrent participants reading the same entry at $t_0$ can both pass the pre-flight check and race directly to Mnemosyne at $t_1$. This constitutes a classic Time-of-Check to Time-of-Use (TOCTOU) observation rather than an active concurrency coordination protocol. Under high contention, this creates aggregate waste: redundant payload marshaling, uncoordinated network traversal, and elevated database contention.

Conversely, attempting active coordination by mutating the cached FHIR resource prior to durable commit is dangerous: it publishes uncommitted, speculative clinical state to downstream readers, risking clinical misinformation if the writer subsequently aborts or crashes.

The core architectural problem is therefore: **Can Mneme provide a mandatory, non-authoritative distributed race-arbitration guarantee that serializes progression attempts from an active generation without publishing uncommitted clinical state, without establishing a secondary durability boundary, and without introducing distributed locking / 2PC subsystems?**

---

**2. What "Mneme Owns Concurrency" Could Mean**

To evaluate the design space systematically, we compare five precise definitions against ADR-019’s mandate for distributed resource access and concurrency/version coordination:

**Definition A: Mneme detects stale active state (Passive/Snapshot)**
- **Mechanism**: Client reads `MetadataValue<String>` from Mneme and verifies its version token before proceeding.
- **Guarantee**: "At timestamp $t_{\text{read}}$, the active state was at generation $G$."
- **ADR-019 Evaluation**: **Insufficient**. Does not arbitrate concurrent attempts to advance from $G$. Provides zero race protection when multiple callers read simultaneously.

**Definition B: Mneme serializes progression from a particular active generation (Strong CAS Marker)**
- **Mechanism**: Exactly one participant can atomically advance an active coordination marker associated with generation $G$ to $G+1$ using Infinispan Hot Rod versioned CAS (`replaceWithVersion`).
- **Guarantee**: "For any given active generation $G$, exactly one distributed participant is granted the right to attempt an authoritative update against Mnemosyne; all concurrent competing participants on generation $G$ are rejected at the cache tier with an active-state conflict."
- **ADR-019 Evaluation**: **Fully Satisfies**. Establishes active-state race arbitration, eliminates redundant concurrent downstream execution, and maintains non-authoritative boundary semantics.

**Definition C: Mneme arbitrates exclusive active execution leases (Distributed Lock / Lease)**
- **Mechanism**: Participant acquires a temporary lease/lock key in Mneme with a TTL and explicit release step.
- **Guarantee**: "Participant $P$ holds exclusive execution rights on resource $R$ until release or lease expiration."
- **ADR-019 Evaluation**: **Violates Architectural Tripwires**. Introduces lock recovery daemons, lease-abandonment delays, clock-skew vulnerabilities, and partition deadlocks. Reduces net system availability.

**Definition D: Mneme protects only cache convergence; Mnemosyne owns all write concurrency (Asymmetric Passive)**
- **Mechanism**: Writers bypass active coordination and write directly to Mnemosyne; Mneme applies conditional updates only after database commit.
- **Guarantee**: "Mneme cache updates will not overwrite newer authoritative versions."
- **ADR-019 Evaluation**: **Incomplete**. Abandons active-state coordination across distributed nodes, shifting all serialization load and contention penalty entirely onto the database tier.

**Definition E: Mneme manages speculative active branches (Multi-Version Speculative Active Tree)**
- **Mechanism**: Mneme holds staged versions linked to transaction IDs.
- **Guarantee**: "Mneme tracks all in-flight speculative transformations."
- **ADR-019 Evaluation**: **Violates Architectural Boundaries**. Recreates an in-memory distributed transaction manager (2PC/XA) inside the caching tier.

**Conclusion**: **Definition B** is the only definition that provides meaningful distributed coordination utility while strictly honoring the non-authoritative, lock-free constraints of ADR-018 and ADR-019.

---

**3. Active Representation vs Coordination State**

To avoid publishing uncommitted clinical payloads while coordinating races, we must strictly separate **Resource Payload** from **Active Coordination State**.

```
+--------------------------------------------------------------------------------+
|                               MNEME DATA GRID                                  |
|                                                                                |
|  [ CLINICAL FHIR CACHE ]                          [ COORDINATION CACHE ]       |
|  (e.g., "patient-cache")                          (e.g., "coordination-cache") |
|  - Key: "Patient/123"                             - Key: "coord/Patient/123"   |
|  - Value: JSON Payload (Committed F41)            - Value: Active Gen "G17"    |
|  - Store: FhirRestCacheStore (Mnemosyne Passthru) - Store: NONE (RAM Only)     |
|  - EntryVersion: V_payload                        - EntryVersion: V_coord      |
+--------------------------------------------------------------------------------+
```

1. **Decoupled Lifecycle**: The committed FHIR payload (`F41`) remains unchanged and readable by query services while a writer attempts to coordinate progression.
2. **Zero Clinical Contamination**: The coordination entry contains only an opaque token or monotonic generation counter (`G17` $\rightarrow$ `G18`). No speculative clinical attributes are exposed to other nodes.
3. **Storage Isolation**: Because existing clinical caches use `FhirRestCacheStore` (`hestia/mneme-persistence/.../FhirRestCacheStore.java:99-121`), placing non-FHIR coordination markers in clinical caches causes store-dispatch errors. Coordination markers require a dedicated, non-persisted cache.

---

**4. Native Infinispan Mechanisms Available**

Based on repository inspection of Infinispan `15.0.3.Final` dependencies and test baselines:

1. **Hot Rod `replaceWithVersion` / `MetadataValue`**:
    - *Direct Evidence*: `MnemeConcurrencyAndVersionScenarioTest.java:388-443` proves that `replaceWithVersion(key, newValue, version)` provides strict, cluster-wide, atomic CAS semantics over Hot Rod. Exactly one participant wins; concurrent competitors receive `false`.
2. **Client-Side `RemoteCache.compute`**:
    - *Direct Evidence*: Decompiled inspection of `RemoteCacheImpl.class:291-319` reveals that `compute` and `computeIfPresent` execute an optimistic client-side loop (`getWithMetadata` $\rightarrow$ local lambda $\rightarrow$ `replaceWithVersion`). There is no server-side bytecode execution. Remapping functions must be side-effect-free and retry-safe.
3. **No General Remote Custom Metadata Map**:
    - Infinispan `Metadata` (`Metadata.class:12-48`) supports lifespan, max-idle, and internal `EntryVersion`. It cannot store arbitrary user key-value maps. Therefore, coordination metadata cannot be attached directly to the existing payload entry; it requires a distinct key or cache.
4. **Configuration Constraints**:
    - All 17 existing caches in `hestia/mneme-cluster/src/main/resources/infinispan.xml:60-287` are configured with `FhirRestCacheStore` or `OperationsRestCacheStore`. A dedicated, non-persisted cache (e.g., `coordination-cache`) is required to prevent transient marker synchronization to Mnemosyne.
5. **No Remote Locks or Active Distributed Transactions**:
    - `AdvancedCache.lock()` is embedded-only. Hot Rod remote locking requires transactional caches (`mneme-distributed-behaviour.md:403-410`), which are deliberately not configured in Harmonia due to 2PC overhead and recovery fragility.

---

**5. Generation/CAS Model Analysis**

A generational CAS model differs fundamentally from a distributed lock:

```
[ Generational CAS ]                      [ Distributed Lock ]
1. Read G17                               1. Acquire Lock(Patient/123)
2. CAS(G17 -> G18)                        2. Execute update
   - Success: Proceed to DB                  - Winner crashes -> Lock hung / TTL wait
   - Failure: Immediate rejection/retry   3. Release Lock(Patient/123)
3. No release step; no held state
```

- **Meaning of `G18`**: Indicates exclusively that *"Active generation `G17` has been consumed as a valid predecessor for an in-flight progression attempt."* It does **not** signify an authoritative durable version, nor does it grant durable ownership.
- **No Lock Overhead**: The winner holds no resource lock. If the winner halts or crashes, no other node is blocked waiting on lock cleanup.
- **Permanent Generation Advancement**: If an operation fails downstream, `G18` remains consumed. The next participant reads `F41` and `G18`, then attempts `G18` $\rightarrow$ `G19`. Progression attempts advance monotonically without requiring rollback.

---

**6. Winner-Failure Analysis**

We examine the exact failure modes when Participant A wins the CAS progression from `G17` to `G18` on top of committed state `F41`, while Participant B is rejected:

```
+-----------------------------------+--------------------------------+--------------------------------------+
| Failure Scenario                  | State in Mneme / Mnemosyne     | System Impact & Recovery Action      |
+-----------------------------------+--------------------------------+--------------------------------------+
| A. Mnemosyne accepts & commits    | DB: D42                        | Post-commit convergence updates      |
|    new version D42                | Mneme: F42 / G18 (or G19)      | payload to F42. Optimal path.        |
+-----------------------------------+--------------------------------+--------------------------------------+
| B. Mnemosyne rejects (predecessor | DB: D41 (unchanged)            | A receives conflict error. G18       |
|    changed by out-of-band write)  | Mneme: F41 / G18 (consumed)    | remains consumed. Zero data loss.    |
|                                   |                                | Next attempt uses G18 as baseline.   |
+-----------------------------------+--------------------------------+--------------------------------------+
| C. Mnemosyne is unavailable       | DB: Unknown / unreachable      | A receives 503 / downstream failure. |
|    (DB connection timeout)        | Mneme: F41 / G18               | No state corrupted. Next caller      |
|                                   |                                | reads F41 and coordinates on G18.    |
+-----------------------------------+--------------------------------+--------------------------------------+
| D. A crashes BEFORE calling       | DB: D41 (unchanged)            | Zero deadlock. Next caller reads     |
|    Mnemosyne                      | Mneme: F41 / G18               | committed F41, sees active G18, and  |
|                                   |                                | attempts CAS G18 -> G19. Safe.       |
+-----------------------------------+--------------------------------+--------------------------------------+
| E. A crashes DURING Mnemosyne     | DB: Either committed or rolled | Mnemosyne ACID boundary guarantees   |
|    transaction                    | back by Postgres engine        | DB consistency. Cache reconciles on  |
|                                   |                                | next read or post-commit event.      |
+-----------------------------------+--------------------------------+--------------------------------------+
| F. A crashes AFTER Mnemosyne      | DB: D42 (committed)            | Transient stale read window in cache |
|    commit but BEFORE cache update | Mneme: F41 / G18               | until TTL expiry, background sync,   |
|                                   |                                | or read-repair from DB occurs.       |
+-----------------------------------+--------------------------------+--------------------------------------+
```

**Key Architectural Finding**: In zero scenarios is distributed rollback, lease recovery, or manual administrative intervention required. Consumed generations are safe, non-authoritative progression steps.

---

**7. Mneme-Loss / Reconstruction Analysis**

Applying the **Reconstructability Test** (ADR-019):
> *If the entire Mneme cluster is abruptly terminated and cold-started from Mnemosyne, what correctness information is lost?*

1. **Lost Information**: In-memory coordination tokens (`G18`), active CAS sequence counters, and uncommitted in-flight race serialization are lost.
2. **Retained Information**: Complete clinical truth, authoritative JPA entity versions (`meta.versionId`), audit logs, and security tags remain fully intact in PostgreSQL.
3. **Safety Evaluation**:
    - Suppose Node 1 was executing an update against generation `G17` when Mneme crashed.
    - Mneme restarts cold. Node 2 issues an update against the newly reconstructed cache.
    - Node 1 and Node 2 both reach Mnemosyne.
    - Mnemosyne’s atomic conditional SQL update (`UPDATE ... WHERE version = :expectedVersion`) guarantees that exactly one transaction commits and the second fails with a database-level optimistic locking exception.
4. **No Fencing Epoch Required**: Because Mnemosyne enforces definitive predecessor validation on every commit, cold-starting Mneme does not risk authoritative data corruption. Introducing persistent fencing epochs would violate the "boring infrastructure" principle with zero added safety utility.

---

**8. Bypass Analysis**

In the event of a legacy path or uncoordinated internal service bypassing Mneme:

```
[ Coordinated Path ]   --> [ Mneme CAS Check ]   --> [ Mnemosyne Conditional Update ] --> [ Cache Refresh ]
[ Bypass Path ]        ----------------------------> [ Mnemosyne Conditional Update ] --> [ Invalidate ]
```

1. **Correctness Invariant**: Authoritative information integrity is preserved exclusively by Mnemosyne’s atomic commit verification. A bypass path cannot corrupt durable state.
2. **Lost Property**: Upstream race arbitration is bypassed, increasing contention and serialization aborts at the database tier.
3. **Cache Staleness & Self-Healing**: A bypass write that commits to Mnemosyne without updating Mneme leaves a transiently stale cache entry. This is safely resolved by mandatory post-commit invalidation events or cache-aside read-repair (`If-None-Match` / DB fallback).
4. **Architectural Coherence**: The statement *"Mneme coordination is mandatory in the governed Harmonia write protocol, but Mnemosyne remains sufficient to protect authoritative information integrity if Mneme is bypassed or lost"* is mathematically sound and represents defense-in-depth.

---

**9. Post-Commit Convergence Race Analysis**

A critical vulnerability exists in uncoordinated post-commit cache eviction:

```
Timeline:
1. Writer A commits D42 to Mnemosyne.
2. Writer B commits D43 to Mnemosyne.
3. Writer B successfully updates Mneme to D43.
4. Writer A's delayed thread wakes up and executes unconditional remove("Patient/123").
5. Result: Valid, current D43 representation is evicted, causing unnecessary cache thrashing.
```

```
Timeline (Overwriting):
1. Writer A commits D42.
2. Writer B commits D43.
3. Writer B writes D43 to Mneme.
4. Writer A's delayed thread writes D42 to Mneme.
5. Result: Stale D42 overwrites newer D43 in cache (Catastrophic payload regression).
```

**Mandatory Convergence Invariant**
> *An older authoritative outcome must never overwrite OR invalidate a newer authoritative representation in Mneme.*

**Enforcement Mechanism**
Post-commit convergence must use **Authoritative Version Compare-and-Swap**:
1. When refreshing Mneme, the client provides the new payload (`D43`) with target version `43` and expected predecessor version `42`.
2. Using a version-aware conditional update (`replaceWithVersion` or a guarded CAS function), the cache will reject the write if the currently cached payload already has `meta.versionId >= 43`.
3. If an eviction/invalidation strategy is used instead of refresh, eviction must be conditional: `remove(key, expectedVersion)`. A delayed thread for `D42` cannot evict `D43`.

---

**10. Stale-Read / Read-After-Write Analysis**

In distributed, asynchronously converged topologies, a brief window exists where Mnemosyne has committed `D42`, but Mneme serves `F41`:

```
+-------------------------+----------------------------------+------------------------------------+
| Consistency Tier        | Clinical Context / Resource Type | Architectural Mechanism            |
+-------------------------+----------------------------------+------------------------------------+
| Tier 1: Strict Freshness| Medication Administration,       | Synchronous post-commit write-     |
| (Read-After-Write)      | Blood Bank Transfusions, Orders  | through; or query with ETag bypass |
+-------------------------+----------------------------------+------------------------------------+
| Tier 2: Bounded Staleness| Patient Demographics, Telecom,   | Replicated cache sync (1-50ms);    |
| (Monotonic Read)        | Organization, Practitioner       | transient stale read acceptable    |
+-------------------------+----------------------------------+------------------------------------+
| Tier 3: Eventual Read   | Historical Encounters, Closed    | Background replication / read-aside|
|                         | Diagnostic Reports               | repair                             |
+-------------------------+----------------------------------+------------------------------------+
```

- Invalidating the cache prior to database commit is **strictly prohibited**, as it creates a dirty read hole and cache stampede.
- Freshness requirements must be governed by resource classification rather than an expensive, cluster-wide synchronous distributed lock.

---

**11. Active-State vs Authoritative-State Conflict Model**

Harmonia enforces a clean, two-stage conflict separation:

```
[ Incoming Request ]
        |
        v
[ Stage 1: Mneme Active-State Arbitration ]
        |---> CAS failure on Generation Marker? ---> Reject with HTTP 409 (Active Conflict: Retryable)
        v
[ Stage 2: Mnemosyne Authoritative Validation ]
        |---> SQL Conditional Update failure?   ---> Reject with HTTP 412 (Precondition Failed / Authoritative Stale)
        v
[ Durable Commit & Converge ]
```

1. **Active-State Conflict (Mneme)**: Indicates concurrent race contention on the same active generation. The caller’s premise was valid, but another node won the dispatch right. Highly suitable for automated, lightweight backoff-and-retry.
2. **Authoritative-State Conflict (Mnemosyne)**: Indicates that the durable clinical baseline has advanced out-of-band. The client’s proposed business delta is stale and must be re-evaluated against the new domain state.

---

**12. External If-Match vs Internal Write Context**

System integrity must never depend on external client discipline. External actors frequently omit `If-Match` headers:

```
[ Ingress: HL7 v2 / Unversioned REST / Workflow ]
        |
        v
[ Harmonia Ingress Pipeline (Themis / Energeia) ]
        |
        +---> Case A: Client provides If-Match: W/"41"
        |     --> Establish Expected Authoritative Predecessor = 41
        |
        +---> Case B: Client provides NO version token (e.g. MLLP ADT^A08)
              --> Ingress establishes BASELINE by reading current state (F41)
              --> Sets Governed Expected Authoritative Predecessor = 41
        |
        v
[ Governed Internal Write Context (Pragma Envelope) ]
  - resourceId: "Patient/123"
  - expectedAuthoritativeVersion: 41
  - activeCoordinationToken: G17
```

All internal workflows propagate an immutable `GovernedWriteContext` containing the explicit expected authoritative predecessor.

---

**13. Uncertain Commit Outcome / Retry Analysis**

When a network partition or client timeout occurs during an in-flight commit:

```
Writer A -------------------> Mnemosyne (Commits D42)
         <--- (Drop/504) --- (Network Timeout)
```

1. **The Retry Trap**: If Writer A retries the update against expected predecessor `D41`, Mnemosyne will correctly reject it with HTTP 412 (Precondition Failed), because the database is already at `D42`.
2. **Idempotency via Causation Identifiers**:
    - Every mutating request must carry a unique `Client-Request-Id` / `Transaction-Correlation-Id` in the `Pragma` context.
    - When handling a 412 or timeout retry, the client/ingress queries the latest resource `meta.tag` or operations audit log (`ThemisAuditService`). If the latest commit already matches the operation's correlation ID, the request succeeds idempotently with the committed representation without reapplying the mutation.

---

**14. Revised Options Comparison**

```
+-----------------------------------+--------------------+-------------------+--------------------+---------------------+
| Evaluation Criteria               | Option B           | Option D          | Option C           | Option E (Preferred)|
|                                   | (Mnemosyne-Only)   | (Passive Pre-Chk) | (Distributed Lock) | (Strong Hybrid CAS) |
+-----------------------------------+--------------------+-------------------+--------------------+---------------------+
| Active Race Arbitration           | NONE               | NONE (TOCTOU)     | YES (Lease-based)  | YES (CAS-based)     |
| Downstream Contention Reduction   | Poor (All hit DB)  | Poor (All hit DB) | High               | High                |
| Zero Uncommitted Dirty Reads      | Guaranteed         | Guaranteed        | Guaranteed         | Guaranteed          |
| Failure Modes / Recovery Overhead | Minimal (ACID DB)  | Minimal (ACID DB) | Severe (Hung locks)| Zero Rollback/Clean |
| Reconstructability from DB        | Trivial            | Trivial           | Complex            | Trivial             |
| Compliance with ADR-019           | Partial/Weak       | Incomplete        | Violates Tripwires | Fully Compliant     |
| Architectural Complexity          | Low                | Low               | Excessive          | Balanced / Moderate |
+-----------------------------------+--------------------+-------------------+--------------------+---------------------+
```

---

**15. Preferred Architecture: Option E (Strong Hybrid with Generational Coordination)**

The recommended architecture establishes a **Two-Tier Optimistic Concurrency Protocol**:

1. **Tier 1 (In-Memory Active Coordination in Mneme)**:
    - Dedicated `coordination-cache` (RAM-only, replicated synchronous, zero persistence store).
    - Coordination entry: `coord/{resourceType}/{id}` storing monotonic token `G_n`.
    - Before attempting an authoritative write, the participant executes Hot Rod CAS `replaceWithVersion` on the coordination token.
    - Rejection triggers immediate active-conflict backoff without loading the database.
2. **Tier 2 (Authoritative Persistence in Mnemosyne)**:
    - Winner executes atomic conditional update against PostgreSQL (`WHERE id = :id AND version = :expectedVersion`).
    - Generates definitive durable version `D_{n+1}`.
3. **Tier 3 (Version-Guarded Post-Commit Convergence)**:
    - Post-commit hook updates `clinical-cache` using payload-version comparison (`meta.versionId > currentCachedVersion`) to prevent out-of-order stale overwrite.

---

**16. Explicit Trade-offs**

1. **Double Network Hop for Writes**:
    - *Cost*: A governed write performs one Hot Rod CAS roundtrip and one PostgreSQL transaction.
    - *Benefit*: Under high contention, eliminates redundant multi-node database transaction conflicts, payload parsing, and connection pool exhaustion.
2. **Transient Generation Churn on Failure**:
    - *Cost*: A failed downstream write leaves an advanced coordination generation marker (`G18`) behind.
    - *Benefit*: Avoids complex, distributed rollback or compensation sagas. The next read picks up `G18` as the clean new active baseline.
3. **Dedicated Cache Configuration Requirement**:
    - *Cost*: Requires defining a non-persisted `coordination-cache` in `infinispan.xml`.
    - *Benefit*: Completely decouples lightweight coordination tokens from clinical FHIR payloads and prevents invalid store-dispatch crashes in `FhirRestCacheStore`.

---

**17. Minimum Mneme Guarantee**

> **"Mneme guarantees that for any cached active resource generation, at most one distributed participant can win active-state progression arbitration to attempt an authoritative update, rejecting all concurrent competing attempts on that generation prior to database execution."**

---

**18. Minimum Mnemosyne Guarantee**

> **"Mnemosyne guarantees that an authoritative update is committed if and only if the specified predecessor version matches the current durable state in PostgreSQL, establishing the single definitive clinical version history regardless of cache state, coordination loss, or bypass."**

---

**19. Remaining Questions for Implementation Phase (Step 08.03+)**

1. **Coordination Cache Eviction Policy**: Should coordination markers use a bounded lifespan (e.g., TTL = 5 minutes) to automatically reclaim RAM for dormant resources?
2. **Batch / Bundle Interactions**: How should multi-resource FHIR Transaction bundles coordinate active tokens across multiple keys simultaneously without introducing deadlocks (e.g., deterministic key sorting prior to CAS)?
3. **Cold-Start Coordination Initialization**: When a resource is first read from Mnemosyne into a cold cache, should the initial coordination token `G_0` be derived deterministically from the durable JPA `versionId`?

---

**20. ADR-019 Compatibility Assessment**

**Conclusion**: **ADR-019 does NOT require modification.**

The existing text of ADR-019 (`docs/architecture-decisions.md:420-607`) already explicitly:
- Designates Mneme as the owner of distributed resource access and concurrency coordination for active state (`:475-492`).
- Disclaims durable authority, assigning authoritative persistence solely to Mnemosyne (`:501-512`).
- Sanctions Infinispan conditional and versioned operations as the underlying implementation mechanism (`:545-562`).
- Forbids process-local fallback substitutions (`:581-607`).

The Option E Strong Hybrid architecture fully realizes the intent and exact text of ADR-019.

Analysis is Ready

**Step 08.02B — Strong Hybrid Concurrency Architecture Analysis**

**1. Problem Restatement**

The central design dilemma is maximizing overall system utility and data integrity across distributed clinical workflows without incurring the catastrophic failure modes of distributed locks or uncommitted dirty reads.

In the initial Task 08.02 analysis, Option D reduced Mneme’s role to an optional, non-mutating pre-flight check (`getWithMetadata` followed by client-side token comparison). Because reading a version token does not serialize subsequent operations, two concurrent participants reading the same entry at $t_0$ can both pass the pre-flight check and race directly to Mnemosyne at $t_1$. This constitutes a classic Time-of-Check to Time-of-Use (TOCTOU) observation rather than an active concurrency coordination protocol. Under high contention, this creates aggregate waste: redundant payload marshaling, uncoordinated network traversal, and elevated database contention.

Conversely, attempting active coordination by mutating the cached FHIR resource prior to durable commit is dangerous: it publishes uncommitted, speculative clinical state to downstream readers, risking clinical misinformation if the writer subsequently aborts or crashes.

The core architectural problem is therefore: **Can Mneme provide a mandatory, non-authoritative distributed race-arbitration guarantee that serializes progression attempts from an active generation without publishing uncommitted clinical state, without establishing a secondary durability boundary, and without introducing distributed locking / 2PC subsystems?**

---

**2. What "Mneme Owns Concurrency" Could Mean**

To evaluate the design space systematically, we compare five precise definitions against ADR-019’s mandate for distributed resource access and concurrency/version coordination:

**Definition A: Mneme detects stale active state (Passive/Snapshot)**
- **Mechanism**: Client reads `MetadataValue<String>` from Mneme and verifies its version token before proceeding.
- **Guarantee**: "At timestamp $t_{\text{read}}$, the active state was at generation $G$."
- **ADR-019 Evaluation**: **Insufficient**. Does not arbitrate concurrent attempts to advance from $G$. Provides zero race protection when multiple callers read simultaneously.

**Definition B: Mneme serializes progression from a particular active generation (Strong CAS Marker)**
- **Mechanism**: Exactly one participant can atomically advance an active coordination marker associated with generation $G$ to $G+1$ using Infinispan Hot Rod versioned CAS (`replaceWithVersion`).
- **Guarantee**: "For any given active generation $G$, exactly one distributed participant is granted the right to attempt an authoritative update against Mnemosyne; all concurrent competing participants on generation $G$ are rejected at the cache tier with an active-state conflict."
- **ADR-019 Evaluation**: **Fully Satisfies**. Establishes active-state race arbitration, eliminates redundant concurrent downstream execution, and maintains non-authoritative boundary semantics.

**Definition C: Mneme arbitrates exclusive active execution leases (Distributed Lock / Lease)**
- **Mechanism**: Participant acquires a temporary lease/lock key in Mneme with a TTL and explicit release step.
- **Guarantee**: "Participant $P$ holds exclusive execution rights on resource $R$ until release or lease expiration."
- **ADR-019 Evaluation**: **Violates Architectural Tripwires**. Introduces lock recovery daemons, lease-abandonment delays, clock-skew vulnerabilities, and partition deadlocks. Reduces net system availability.

**Definition D: Mneme protects only cache convergence; Mnemosyne owns all write concurrency (Asymmetric Passive)**
- **Mechanism**: Writers bypass active coordination and write directly to Mnemosyne; Mneme applies conditional updates only after database commit.
- **Guarantee**: "Mneme cache updates will not overwrite newer authoritative versions."
- **ADR-019 Evaluation**: **Incomplete**. Abandons active-state coordination across distributed nodes, shifting all serialization load and contention penalty entirely onto the database tier.

**Definition E: Mneme manages speculative active branches (Multi-Version Speculative Active Tree)**
- **Mechanism**: Mneme holds staged versions linked to transaction IDs.
- **Guarantee**: "Mneme tracks all in-flight speculative transformations."
- **ADR-019 Evaluation**: **Violates Architectural Boundaries**. Recreates an in-memory distributed transaction manager (2PC/XA) inside the caching tier.

**Conclusion**: **Definition B** is the only definition that provides meaningful distributed coordination utility while strictly honoring the non-authoritative, lock-free constraints of ADR-018 and ADR-019.

---

**3. Active Representation vs Coordination State**

To avoid publishing uncommitted clinical payloads while coordinating races, we must strictly separate **Resource Payload** from **Active Coordination State**.

```
+--------------------------------------------------------------------------------+
|                               MNEME DATA GRID                                  |
|                                                                                |
|  [ CLINICAL FHIR CACHE ]                          [ COORDINATION CACHE ]       |
|  (e.g., "patient-cache")                          (e.g., "coordination-cache") |
|  - Key: "Patient/123"                             - Key: "coord/Patient/123"   |
|  - Value: JSON Payload (Committed F41)            - Value: Active Gen "G17"    |
|  - Store: FhirRestCacheStore (Mnemosyne Passthru) - Store: NONE (RAM Only)     |
|  - EntryVersion: V_payload                        - EntryVersion: V_coord      |
+--------------------------------------------------------------------------------+
```

1. **Decoupled Lifecycle**: The committed FHIR payload (`F41`) remains unchanged and readable by query services while a writer attempts to coordinate progression.
2. **Zero Clinical Contamination**: The coordination entry contains only an opaque token or monotonic generation counter (`G17` $\rightarrow$ `G18`). No speculative clinical attributes are exposed to other nodes.
3. **Storage Isolation**: Because existing clinical caches use `FhirRestCacheStore` (`hestia/mneme-persistence/.../FhirRestCacheStore.java:99-121`), placing non-FHIR coordination markers in clinical caches causes store-dispatch errors. Coordination markers require a dedicated, non-persisted cache.

---

**4. Native Infinispan Mechanisms Available**

Based on repository inspection of Infinispan `15.0.3.Final` dependencies and test baselines:

1. **Hot Rod `replaceWithVersion` / `MetadataValue`**:
    - *Direct Evidence*: `MnemeConcurrencyAndVersionScenarioTest.java:388-443` proves that `replaceWithVersion(key, newValue, version)` provides strict, cluster-wide, atomic CAS semantics over Hot Rod. Exactly one participant wins; concurrent competitors receive `false`.
2. **Client-Side `RemoteCache.compute`**:
    - *Direct Evidence*: Decompiled inspection of `RemoteCacheImpl.class:291-319` reveals that `compute` and `computeIfPresent` execute an optimistic client-side loop (`getWithMetadata` $\rightarrow$ local lambda $\rightarrow$ `replaceWithVersion`). There is no server-side bytecode execution. Remapping functions must be side-effect-free and retry-safe.
3. **No General Remote Custom Metadata Map**:
    - Infinispan `Metadata` (`Metadata.class:12-48`) supports lifespan, max-idle, and internal `EntryVersion`. It cannot store arbitrary user key-value maps. Therefore, coordination metadata cannot be attached directly to the existing payload entry; it requires a distinct key or cache.
4. **Configuration Constraints**:
    - All 17 existing caches in `hestia/mneme-cluster/src/main/resources/infinispan.xml:60-287` are configured with `FhirRestCacheStore` or `OperationsRestCacheStore`. A dedicated, non-persisted cache (e.g., `coordination-cache`) is required to prevent transient marker synchronization to Mnemosyne.
5. **No Remote Locks or Active Distributed Transactions**:
    - `AdvancedCache.lock()` is embedded-only. Hot Rod remote locking requires transactional caches (`mneme-distributed-behaviour.md:403-410`), which are deliberately not configured in Harmonia due to 2PC overhead and recovery fragility.

---

**5. Generation/CAS Model Analysis**

A generational CAS model differs fundamentally from a distributed lock:

```
[ Generational CAS ]                      [ Distributed Lock ]
1. Read G17                               1. Acquire Lock(Patient/123)
2. CAS(G17 -> G18)                        2. Execute update
   - Success: Proceed to DB                  - Winner crashes -> Lock hung / TTL wait
   - Failure: Immediate rejection/retry   3. Release Lock(Patient/123)
3. No release step; no held state
```

- **Meaning of `G18`**: Indicates exclusively that *"Active generation `G17` has been consumed as a valid predecessor for an in-flight progression attempt."* It does **not** signify an authoritative durable version, nor does it grant durable ownership.
- **No Lock Overhead**: The winner holds no resource lock. If the winner halts or crashes, no other node is blocked waiting on lock cleanup.
- **Permanent Generation Advancement**: If an operation fails downstream, `G18` remains consumed. The next participant reads `F41` and `G18`, then attempts `G18` $\rightarrow$ `G19`. Progression attempts advance monotonically without requiring rollback.

---

**6. Winner-Failure Analysis**

We examine the exact failure modes when Participant A wins the CAS progression from `G17` to `G18` on top of committed state `F41`, while Participant B is rejected:

```
+-----------------------------------+--------------------------------+--------------------------------------+
| Failure Scenario                  | State in Mneme / Mnemosyne     | System Impact & Recovery Action      |
+-----------------------------------+--------------------------------+--------------------------------------+
| A. Mnemosyne accepts & commits    | DB: D42                        | Post-commit convergence updates      |
|    new version D42                | Mneme: F42 / G18 (or G19)      | payload to F42. Optimal path.        |
+-----------------------------------+--------------------------------+--------------------------------------+
| B. Mnemosyne rejects (predecessor | DB: D41 (unchanged)            | A receives conflict error. G18       |
|    changed by out-of-band write)  | Mneme: F41 / G18 (consumed)    | remains consumed. Zero data loss.    |
|                                   |                                | Next attempt uses G18 as baseline.   |
+-----------------------------------+--------------------------------+--------------------------------------+
| C. Mnemosyne is unavailable       | DB: Unknown / unreachable      | A receives 503 / downstream failure. |
|    (DB connection timeout)        | Mneme: F41 / G18               | No state corrupted. Next caller      |
|                                   |                                | reads F41 and coordinates on G18.    |
+-----------------------------------+--------------------------------+--------------------------------------+
| D. A crashes BEFORE calling       | DB: D41 (unchanged)            | Zero deadlock. Next caller reads     |
|    Mnemosyne                      | Mneme: F41 / G18               | committed F41, sees active G18, and  |
|                                   |                                | attempts CAS G18 -> G19. Safe.       |
+-----------------------------------+--------------------------------+--------------------------------------+
| E. A crashes DURING Mnemosyne     | DB: Either committed or rolled | Mnemosyne ACID boundary guarantees   |
|    transaction                    | back by Postgres engine        | DB consistency. Cache reconciles on  |
|                                   |                                | next read or post-commit event.      |
+-----------------------------------+--------------------------------+--------------------------------------+
| F. A crashes AFTER Mnemosyne      | DB: D42 (committed)            | Transient stale read window in cache |
|    commit but BEFORE cache update | Mneme: F41 / G18               | until TTL expiry, background sync,   |
|                                   |                                | or read-repair from DB occurs.       |
+-----------------------------------+--------------------------------+--------------------------------------+
```

**Key Architectural Finding**: In zero scenarios is distributed rollback, lease recovery, or manual administrative intervention required. Consumed generations are safe, non-authoritative progression steps.

---

**7. Mneme-Loss / Reconstruction Analysis**

Applying the **Reconstructability Test** (ADR-019):
> *If the entire Mneme cluster is abruptly terminated and cold-started from Mnemosyne, what correctness information is lost?*

1. **Lost Information**: In-memory coordination tokens (`G18`), active CAS sequence counters, and uncommitted in-flight race serialization are lost.
2. **Retained Information**: Complete clinical truth, authoritative JPA entity versions (`meta.versionId`), audit logs, and security tags remain fully intact in PostgreSQL.
3. **Safety Evaluation**:
    - Suppose Node 1 was executing an update against generation `G17` when Mneme crashed.
    - Mneme restarts cold. Node 2 issues an update against the newly reconstructed cache.
    - Node 1 and Node 2 both reach Mnemosyne.
    - Mnemosyne’s atomic conditional SQL update (`UPDATE ... WHERE version = :expectedVersion`) guarantees that exactly one transaction commits and the second fails with a database-level optimistic locking exception.
4. **No Fencing Epoch Required**: Because Mnemosyne enforces definitive predecessor validation on every commit, cold-starting Mneme does not risk authoritative data corruption. Introducing persistent fencing epochs would violate the "boring infrastructure" principle with zero added safety utility.

---

**8. Bypass Analysis**

In the event of a legacy path or uncoordinated internal service bypassing Mneme:

```
[ Coordinated Path ]   --> [ Mneme CAS Check ]   --> [ Mnemosyne Conditional Update ] --> [ Cache Refresh ]
[ Bypass Path ]        ----------------------------> [ Mnemosyne Conditional Update ] --> [ Invalidate ]
```

1. **Correctness Invariant**: Authoritative information integrity is preserved exclusively by Mnemosyne’s atomic commit verification. A bypass path cannot corrupt durable state.
2. **Lost Property**: Upstream race arbitration is bypassed, increasing contention and serialization aborts at the database tier.
3. **Cache Staleness & Self-Healing**: A bypass write that commits to Mnemosyne without updating Mneme leaves a transiently stale cache entry. This is safely resolved by mandatory post-commit invalidation events or cache-aside read-repair (`If-None-Match` / DB fallback).
4. **Architectural Coherence**: The statement *"Mneme coordination is mandatory in the governed Harmonia write protocol, but Mnemosyne remains sufficient to protect authoritative information integrity if Mneme is bypassed or lost"* is mathematically sound and represents defense-in-depth.

---

**9. Post-Commit Convergence Race Analysis**

A critical vulnerability exists in uncoordinated post-commit cache eviction:

```
Timeline:
1. Writer A commits D42 to Mnemosyne.
2. Writer B commits D43 to Mnemosyne.
3. Writer B successfully updates Mneme to D43.
4. Writer A's delayed thread wakes up and executes unconditional remove("Patient/123").
5. Result: Valid, current D43 representation is evicted, causing unnecessary cache thrashing.
```

```
Timeline (Overwriting):
1. Writer A commits D42.
2. Writer B commits D43.
3. Writer B writes D43 to Mneme.
4. Writer A's delayed thread writes D42 to Mneme.
5. Result: Stale D42 overwrites newer D43 in cache (Catastrophic payload regression).
```

**Mandatory Convergence Invariant**
> *An older authoritative outcome must never overwrite OR invalidate a newer authoritative representation in Mneme.*

**Enforcement Mechanism**  
Post-commit convergence must use **Authoritative Version Compare-and-Swap**:
1. When refreshing Mneme, the client provides the new payload (`D43`) with target version `43` and expected predecessor version `42`.
2. Using a version-aware conditional update (`replaceWithVersion` or a guarded CAS function), the cache will reject the write if the currently cached payload already has `meta.versionId >= 43`.
3. If an eviction/invalidation strategy is used instead of refresh, eviction must be conditional: `remove(key, expectedVersion)`. A delayed thread for `D42` cannot evict `D43`.

---

**10. Stale-Read / Read-After-Write Analysis**

In distributed, asynchronously converged topologies, a brief window exists where Mnemosyne has committed `D42`, but Mneme serves `F41`:

```
+-------------------------+----------------------------------+------------------------------------+
| Consistency Tier        | Clinical Context / Resource Type | Architectural Mechanism            |
+-------------------------+----------------------------------+------------------------------------+
| Tier 1: Strict Freshness| Medication Administration,       | Synchronous post-commit write-     |
| (Read-After-Write)      | Blood Bank Transfusions, Orders  | through; or query with ETag bypass |
+-------------------------+----------------------------------+------------------------------------+
| Tier 2: Bounded Staleness| Patient Demographics, Telecom,   | Replicated cache sync (1-50ms);    |
| (Monotonic Read)        | Organization, Practitioner       | transient stale read acceptable    |
+-------------------------+----------------------------------+------------------------------------+
| Tier 3: Eventual Read   | Historical Encounters, Closed    | Background replication / read-aside|
|                         | Diagnostic Reports               | repair                             |
+-------------------------+----------------------------------+------------------------------------+
```

- Invalidating the cache prior to database commit is **strictly prohibited**, as it creates a dirty read hole and cache stampede.
- Freshness requirements must be governed by resource classification rather than an expensive, cluster-wide synchronous distributed lock.

---

**11. Active-State vs Authoritative-State Conflict Model**

Harmonia enforces a clean, two-stage conflict separation:

```
[ Incoming Request ]
        |
        v
[ Stage 1: Mneme Active-State Arbitration ]
        |---> CAS failure on Generation Marker? ---> Reject with HTTP 409 (Active Conflict: Retryable)
        v
[ Stage 2: Mnemosyne Authoritative Validation ]
        |---> SQL Conditional Update failure?   ---> Reject with HTTP 412 (Precondition Failed / Authoritative Stale)
        v
[ Durable Commit & Converge ]
```

1. **Active-State Conflict (Mneme)**: Indicates concurrent race contention on the same active generation. The caller’s premise was valid, but another node won the dispatch right. Highly suitable for automated, lightweight backoff-and-retry.
2. **Authoritative-State Conflict (Mnemosyne)**: Indicates that the durable clinical baseline has advanced out-of-band. The client’s proposed business delta is stale and must be re-evaluated against the new domain state.

---

**12. External If-Match vs Internal Write Context**

System integrity must never depend on external client discipline. External actors frequently omit `If-Match` headers:

```
[ Ingress: HL7 v2 / Unversioned REST / Workflow ]
        |
        v
[ Harmonia Ingress Pipeline (Themis / Energeia) ]
        |
        +---> Case A: Client provides If-Match: W/"41"
        |     --> Establish Expected Authoritative Predecessor = 41
        |
        +---> Case B: Client provides NO version token (e.g. MLLP ADT^A08)
              --> Ingress establishes BASELINE by reading current state (F41)
              --> Sets Governed Expected Authoritative Predecessor = 41
        |
        v
[ Governed Internal Write Context (Pragma Envelope) ]
  - resourceId: "Patient/123"
  - expectedAuthoritativeVersion: 41
  - activeCoordinationToken: G17
```

All internal workflows propagate an immutable `GovernedWriteContext` containing the explicit expected authoritative predecessor.

---

**13. Uncertain Commit Outcome / Retry Analysis**

When a network partition or client timeout occurs during an in-flight commit:

```
Writer A -------------------> Mnemosyne (Commits D42)
         <--- (Drop/504) --- (Network Timeout)
```

1. **The Retry Trap**: If Writer A retries the update against expected predecessor `D41`, Mnemosyne will correctly reject it with HTTP 412 (Precondition Failed), because the database is already at `D42`.
2. **Idempotency via Causation Identifiers**:
    - Every mutating request must carry a unique `Client-Request-Id` / `Transaction-Correlation-Id` in the `Pragma` context.
    - When handling a 412 or timeout retry, the client/ingress queries the latest resource `meta.tag` or operations audit log (`ThemisAuditService`). If the latest commit already matches the operation's correlation ID, the request succeeds idempotently with the committed representation without reapplying the mutation.

---

**14. Revised Options Comparison**

```
+-----------------------------------+--------------------+-------------------+--------------------+---------------------+
| Evaluation Criteria               | Option B           | Option D          | Option C           | Option E (Preferred)|
|                                   | (Mnemosyne-Only)   | (Passive Pre-Chk) | (Distributed Lock) | (Strong Hybrid CAS) |
+-----------------------------------+--------------------+-------------------+--------------------+---------------------+
| Active Race Arbitration           | NONE               | NONE (TOCTOU)     | YES (Lease-based)  | YES (CAS-based)     |
| Downstream Contention Reduction   | Poor (All hit DB)  | Poor (All hit DB) | High               | High                |
| Zero Uncommitted Dirty Reads      | Guaranteed         | Guaranteed        | Guaranteed         | Guaranteed          |
| Failure Modes / Recovery Overhead | Minimal (ACID DB)  | Minimal (ACID DB) | Severe (Hung locks)| Zero Rollback/Clean |
| Reconstructability from DB        | Trivial            | Trivial           | Complex            | Trivial             |
| Compliance with ADR-019           | Partial/Weak       | Incomplete        | Violates Tripwires | Fully Compliant     |
| Architectural Complexity          | Low                | Low               | Excessive          | Balanced / Moderate |
+-----------------------------------+--------------------+-------------------+--------------------+---------------------+
```

---

**15. Preferred Architecture: Option E (Strong Hybrid with Generational Coordination)**

The recommended architecture establishes a **Two-Tier Optimistic Concurrency Protocol**:

1. **Tier 1 (In-Memory Active Coordination in Mneme)**:
    - Dedicated `coordination-cache` (RAM-only, replicated synchronous, zero persistence store).
    - Coordination entry: `coord/{resourceType}/{id}` storing monotonic token `G_n`.
    - Before attempting an authoritative write, the participant executes Hot Rod CAS `replaceWithVersion` on the coordination token.
    - Rejection triggers immediate active-conflict backoff without loading the database.
2. **Tier 2 (Authoritative Persistence in Mnemosyne)**:
    - Winner executes atomic conditional update against PostgreSQL (`WHERE id = :id AND version = :expectedVersion`).
    - Generates definitive durable version `D_{n+1}`.
3. **Tier 3 (Version-Guarded Post-Commit Convergence)**:
    - Post-commit hook updates `clinical-cache` using payload-version comparison (`meta.versionId > currentCachedVersion`) to prevent out-of-order stale overwrite.

---

**16. Explicit Trade-offs**

1. **Double Network Hop for Writes**:
    - *Cost*: A governed write performs one Hot Rod CAS roundtrip and one PostgreSQL transaction.
    - *Benefit*: Under high contention, eliminates redundant multi-node database transaction conflicts, payload parsing, and connection pool exhaustion.
2. **Transient Generation Churn on Failure**:
    - *Cost*: A failed downstream write leaves an advanced coordination generation marker (`G18`) behind.
    - *Benefit*: Avoids complex, distributed rollback or compensation sagas. The next read picks up `G18` as the clean new active baseline.
3. **Dedicated Cache Configuration Requirement**:
    - *Cost*: Requires defining a non-persisted `coordination-cache` in `infinispan.xml`.
    - *Benefit*: Completely decouples lightweight coordination tokens from clinical FHIR payloads and prevents invalid store-dispatch crashes in `FhirRestCacheStore`.

---

**17. Minimum Mneme Guarantee**

> **"Mneme guarantees that for any cached active resource generation, at most one distributed participant can win active-state progression arbitration to attempt an authoritative update, rejecting all concurrent competing attempts on that generation prior to database execution."**

---

**18. Minimum Mnemosyne Guarantee**

> **"Mnemosyne guarantees that an authoritative update is committed if and only if the specified predecessor version matches the current durable state in PostgreSQL, establishing the single definitive clinical version history regardless of cache state, coordination loss, or bypass."**

---

**19. Remaining Questions for Implementation Phase (Step 08.03+)**

1. **Coordination Cache Eviction Policy**: Should coordination markers use a bounded lifespan (e.g., TTL = 5 minutes) to automatically reclaim RAM for dormant resources?
2. **Batch / Bundle Interactions**: How should multi-resource FHIR Transaction bundles coordinate active tokens across multiple keys simultaneously without introducing deadlocks (e.g., deterministic key sorting prior to CAS)?
3. **Cold-Start Coordination Initialization**: When a resource is first read from Mnemosyne into a cold cache, should the initial coordination token `G_0` be derived deterministically from the durable JPA `versionId`?

---

**20. ADR-019 Compatibility Assessment**

**Conclusion**: **ADR-019 does NOT require modification.**

The existing text of ADR-019 (`docs/architecture-decisions.md:420-607`) already explicitly:
- Designates Mneme as the owner of distributed resource access and concurrency coordination for active state (`:475-492`).
- Disclaims durable authority, assigning authoritative persistence solely to Mnemosyne (`:501-512`).
- Sanctions Infinispan conditional and versioned operations as the underlying implementation mechanism (`:545-562`).
- Forbids process-local fallback substitutions (`:581-607`).

The Option E Strong Hybrid architecture fully realizes the intent and exact text of ADR-019.



**Summary**
- Completed the Step 08.02B architecture-only review and concluded that Mneme can have a real, mandatory, non-authoritative concurrency role only as active-state progression arbitration separate from the cached clinical payload.
- Recommended a revised strong hybrid model: Mneme performs distributed CAS-based active-generation arbitration, while Mnemosyne remains the sole authoritative expected-predecessor commit boundary.
  **Changes**
- Distinguished passive stale detection from true coordination and rejected both Mneme-first authority and lock/lease-based designs as either insufficient or over-complex.
- Established that current Harmonia evidence supports Hot Rod version-CAS primitives, but not arbitrary remote metadata fields or server-side remote entry processing in the deployed model.
- Identified the key architectural constraint that any future coordination marker must be isolated from current clinical caches because `FhirRestCacheStore` would otherwise persist it as if it were FHIR state.
  **Verification**
- Validated conclusions by repository-only analysis of ADRs, Mneme cache/store configuration, Hot Rod/Infinispan capabilities, and existing Mneme concurrency laboratory tests; no production code or schema was modified.
  **Notes**
- This stops at the Step 08.02B design report as requested; no implementation, schema, cache, API, or protocol changes were made.

