Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Perform a read-only repository exploration to produce the evidence needed for the requested Step 03.1 audit-persistence report.

Start from the already-known Task 06 Step 02 artifacts and current Kleio audit model/mapper boundary, then trace the ACTUAL current persistence path(s) for FHIR R5 `AuditEvent` end to end. Identify exact modules, classes, methods, interfaces/implementations, storage abstractions, cache/write-behind participation, durable store technology, CRUD semantics, and externally reachable mutation surfaces.

**Feedback**  
Prioritize concrete code-path tracing over inference from names. For every relevant finding, capture file paths and line numbers so the final report can cite them directly.

Specifically investigate and report on:
- Current Kleio audit service contract and in-memory behavior (`AuditService`, `InMemoryAuditService`, `AuditIntegrityException`).
- Actual `AuditEvent` REST/provider/resource entry points and whether CREATE/READ/SEARCH/UPDATE/PATCH/DELETE are exposed.
- `Mnemosyne` / generic FHIR storage path and exact create/update/delete semantics for `AuditEvent`, including ID assignment, versioning, soft/hard delete, and whether create can replace/upsert.
- Whether `FhirCacheService` / Infinispan / Mneme / write-behind / HAPI client path is used for `AuditEvent`, and if so what durability/overwrite semantics it implies.
- Atomicity/concurrency boundary candidates for append-only + idempotent evidence insertion.
- Physical storage ownership: which database/schema/table/resource mechanism actually persists `AuditEvent` today.
- Existing external mutation surfaces that could update/delete the same stored `AuditEvent` later.
- Current security-domain / authorization behavior for `AuditEvent` read/write paths.

Deliver a structured exploration summary with:
- exact call chains / ASCII flow(s)
- CRUD capability inventory
- storage findings
- cache/write-behind findings
- atomicity + durability findings
- mutable-surface inventory
- security findings
- recommended smallest viable Step 03 boundary (analysis-quality recommendation only; do not propose implementation steps beyond boundary identification)

Constraints:
- Read-only only. Do not modify files.
- Do not perform git operations.
- Do not implement anything.



Exploration is Ready

**Harmonia Architectural Exploration Report: Immutable Audit Persistence Boundary (Task 06 — Step 03.1)**

---

**1. Executive Finding**

Current FHIR R5 `AuditEvent` persistence in Harmonia is fundamentally **mutable, upsert-driven, and non-durable at the cache boundary**. Writes entering Iris BEFE are acknowledged immediately upon insertion into the Infinispan `auditevent-cache`, before asynchronous write-behind PUTs reach Mnemosyne. In Mnemosyne, `AuditEventResourceProvider` delegates to `FhirStorageService`, which stores all resources in a shared `hie_fhir_resources` clinical PostgreSQL table, treating duplicate IDs as in-place updates and exposing active PUT and DELETE operations. Furthermore, `/api/fhir/*` endpoints are incorrectly governed under the `CLINICAL` security domain (bypassing `AuditReadPolicy`), and unauthenticated HAPI REST endpoints allow external modifications. Persisting Kleio immutable evidence requires a dedicated append-only boundary enforcing atomic `INSERT IF ABSENT` semantics and preventing payload overwriting or deletion.

---

**2. Current AuditEvent Persistence Architecture**

There are currently two distinct paths whereby a FHIR `AuditEvent` is persisted in the repository.

**Path 1: Iris BEFE Presentation Tier via Hot Rod Cache and Write-Behind Store**

```
[Caller / UI (AuditEventView.vue)]
       │
       ▼ (HTTP POST / PUT / DELETE /api/fhir/AuditEvent)
[ThemisClinicalAuthorizationFilter]
   iris/iris-befe (line 68) ── Filters under CLINICAL domain (evaluates clinical.create/update)
       │
       ▼
[AuditEventResource]
   iris/iris-befe (lines 37, 64-100) ── JAX-RS Resource (create, update, delete)
       │
       ▼
[FhirCacheService]
   iris/iris-befe (lines 138-188, 190-202) ── puts JSON to Infinispan / local map
       │
       ▼
[RemoteCache<String, String>] ("auditevent-cache" in Infinispan / Mneme cluster)
       │
       ├────────────────────────────────────────┐
       ▼ (Immediate Synchronous Ack)            ▼ (Asynchronous Write-Behind Queue)
[HTTP 201/200 OK returned to Caller]    [FhirRestCacheStore.write / delete]
                                           hestia/mneme-persistence (lines 99, 125)
                                                │
                                                ▼ (HTTP PUT / DELETE /fhir/AuditEvent/{id})
                                        [HapiFhirRestClient]
                                           hestia/mneme-persistence (lines 74, 97)
                                                │
                                                ▼ (HTTP Wire Transport)
                                        [JpaRestfulServer]
                                           hestia/mnemosyne-clinical (line 31)
                                                │
                                                ▼
                                        [AuditEventResourceProvider]
                                           hestia/mnemosyne-clinical (lines 32-108)
                                                │
                                                ▼
                                        [FhirStorageService]
                                           hestia/mnemosyne-clinical (lines 53-156)
                                                │
                                                ▼
                                        [FhirResourceRepository (Spring Data JPA)]
                                           hestia/mnemosyne-clinical (line 28)
                                                │
                                                ▼
                                        [PostgreSQL Database: "fhir" (table: hie_fhir_resources)]
```

**Path 2: Direct Internal HAPI REST Ingress to Mnemosyne**

```
[Internal Service / Test / Ingress Gateway]
       │
       ▼ (HTTP POST / PUT / DELETE http://mnemosyne-clinical-1:8080/fhir/AuditEvent)
[JpaRestfulServer]
   hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java
   (No security filter or authorization interceptor; completely open/unauthenticated)
       │
       ▼
[AuditEventResourceProvider]
   hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java
   - create() [lines 37-47]
   - update() [lines 71-84]
   - delete() [lines 88-101]
       │
       ▼
[FhirStorageService]
   hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java
   - createResource() [lines 70-101] -> UPSERT/REPLACE
   - updateResource() [lines 103-138] -> REPLACE / BUMP VERSION
   - deleteResource() [lines 140-156] -> SOFT DELETE (is_deleted = true)
       │
       ▼
[FhirResourceRepository]
   hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/repository/FhirResourceRepository.java
       │
       ▼
[PostgreSQL Database: fhir_user @ postgres:5432/fhir -> table: hie_fhir_resources]
```

**Path 3: Kleio Audit Path (Current State)**

```
[Caller: Service / Interceptor]
       │
       ▼ (HarmoniaAuditEvent)
[AuditService] (Interface)
   kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditService.java
       │
       ▼
[InMemoryAuditService] (Implementation)
   kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/InMemoryAuditService.java
   (Ring buffer in JVM heap; no connection to durable persistence or FHIR mapper yet)
```

---

**3. Physical Persistence and Storage Findings**

- **Physical Database**: PostgreSQL database named `fhir` (user: `fhir_user`, password: `fhir_password`), configured in `hestia/mnemosyne-clinical/src/main/resources/application-postgres.yml` (lines 21–24). (In development/unit tests, in-memory H2 database `jdbc:h2:mem:fhir_db;MODE=PostgreSQL` is used, configured in `application.yml` line 33).
- **Clinical vs. Operations Database Separation**:
    - `mnemosyne-clinical` points to `jdbc:postgresql://postgres:5432/fhir`
    - `mnemosyne-operations` points to `jdbc:postgresql://postgres-ops:5432/ops`
    - **`AuditEvent` is physically stored in the CLINICAL database (`fhir`)**, not the operations database.
- **Table & Schema**:
    - Entity: `net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity` (`hestia/mnemosyne-clinical/.../FhirResourceEntity.java`).
    - Table: `hie_fhir_resources`.
    - Schema definition:
        - `id`: `BIGINT` (Identity PK)
        - `resource_type`: `VARCHAR(64) NOT NULL` (e.g. `'AuditEvent'`)
        - `fhir_id`: `VARCHAR(128) NOT NULL`
        - `version_id`: `BIGINT NOT NULL` (default `1`)
        - `resource_json`: `TEXT NOT NULL`
        - `is_deleted`: `BOOLEAN NOT NULL` (default `false`)
        - `last_updated`: `TIMESTAMP NOT NULL`
- **Constraints & Indexes**:
    - Unique constraint: `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})` (line 27).
    - Indexes: `idx_resource_type_fhir_id (resource_type, fhir_id)` and `idx_resource_type_deleted (resource_type, is_deleted)` (lines 30–31).
- **Physical Segregation**: There is **no dedicated AuditEvent table**. AuditEvent rows are stored in the exact same table alongside `Patient`, `PractitionerRole`, `HealthcareService`, `Group`, `Provenance`, etc.
- **Relocation Requirement (AUDIT-BL-02)**: Relocating to a dedicated Operations/Audit persistence store would require a separate datasource, schema, persistence adapter, and deployment/runtime configuration.

---

**4. Existing CREATE / READ / SEARCH / UPDATE / DELETE Semantics**

The operations in `AuditEventResourceProvider` (`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java`) and `FhirStorageService` (`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java`) exhibit the following concrete behaviors:

| Operation | Provider Method | Storage Service Method | Real Repo Behavior & Invariants |
| :--- | :--- | :--- | :--- |
| **CREATE** | `create(AuditEvent)` [lines 37–47] | `createResource(AuditEvent)` [lines 70–101] | **NOT INSERT-ONLY**. If `fhir_id` is blank, UUID is generated. If `findByResourceTypeAndFhirId` finds an existing record, it **replaces/upserts in place**: sets `resourceJson = newJson`, `is_deleted = false`, bumps `versionId++`, sets `lastUpdated = now()`. |
| **READ** | `read(IdType)` [lines 50–59] | `getResource(String, String)` [lines 158–174] | Returns resource if `findByResourceTypeAndFhirIdAndDeletedFalse` finds record. If `is_deleted == true` or absent, throws HAPI `ResourceNotFoundException (404)`. |
| **SEARCH** | `search(_id, name, identifier)` [lines 62–67] | `searchResources(type, map)` [lines 191–217] | Returns active resources (`deleted == false`). Filter matching (`matchesFilter` lines 363–579) is extremely limited for AuditEvent: checks `action.toCode()` or `agent.who.display` for `name`; `identifier` falls through to `matched = true`; time/category/principal/target filters are **unimplemented**. |
| **UPDATE** | `update(IdType, AuditEvent)` [lines 71–84] | `updateResource(String, String, T)` [lines 103–138] | **MUTATES IN PLACE**. Parses incoming resource, bumps `versionId++`, sets `is_deleted = false`, overwrites `resourceJson` with new payload. If absent, creates a new entity. |
| **DELETE** | `delete(IdType)` [lines 88–101] | `deleteResource(String, String)` [lines 140–156] | **SOFT DELETE**. Locates entity, bumps `versionId++`, sets `deleted = true`, sets `lastUpdated = now()`. Row remains in database but is hidden from read/search. |
| **PATCH** | *Not explicitly declared* | *None* | HAPI PATCH is not declared in `AuditEventResourceProvider`. |

**Specific Detailed Answers on Storage Semantics:**
1. `create(AuditEvent with existing ID)`: **Silently overwrites** the existing entity in `hie_fhir_resources`, resets `is_deleted` to false, bumps `version_id`, updates `last_updated`.
2. `update(existing AuditEvent)`: **Overwrites** `resource_json` and increments `version_id`.
3. `delete(existing AuditEvent)`: Performs a **soft delete** (`deleted = true`), increments `version_id`.
4. `read(deleted AuditEvent)`: Throws HAPI `ResourceNotFoundException` (HTTP 404).
5. `search(deleted AuditEvent)`: Excluded from query results (`findByResourceTypeAndDeletedFalse`).
6. `history(previous AuditEvent versions)`: **Previous versions are completely lost**. No history table exists. Only the single latest state is kept in `hie_fhir_resources`.
7. `Conditional create/update`: Not supported.
8. `Optimistic locking`: Version numbers exist on the entity (`versionId`), but no `@Version` optimistic locking annotation is present on `FhirResourceEntity`.

---

**5. Cache and Write-Behind Findings**

- **Cache Layer**: Defined in `hestia/mneme-cluster/src/main/resources/infinispan.xml` (lines 194–206) as replicated cache `<replicated-cache name="auditevent-cache" mode="SYNC">`.
- **Write-Behind Configuration**:
    - Uses `<store class="net.fhirfactory.harmonia.persistence.store.FhirRestCacheStore">` with `<write-behind modification-queue-size="1024" />` targeting `${fhir.server.url:http://mnemosyne-clinical-1:8080/fhir}`.
- **Asynchronous Decoupling**:
    - In `FhirCacheService.saveResource` (`iris/iris-befe/.../FhirCacheService.java`, line 186): writes to `remoteCache.put(id, json)` and immediately returns to caller.
    - The caller receives HTTP 201 Created or HTTP 200 OK **before** the write-behind task even begins communicating with Mnemosyne or PostgreSQL.
- **Write-Behind Translates Writes to HTTP PUT**:
    - `FhirRestCacheStore.write` calls `restClient.saveResourceJson(coord.resourceType(), coord.id(), jsonPayload)` (`hestia/mneme-persistence/.../FhirRestCacheStore.java`, line 110).
    - In `HapiFhirRestClient.java` (line 80), this translates directly into `HttpRequest.newBuilder().PUT(...)`!
    - **Write-behind converts any cache write into an HTTP PUT (update/replace)** on the FHIR server!
- **Failure Swallowing**:
    - In `FhirRestCacheStore.java` (lines 118–121): exceptions occurring during write-behind are caught via `.exceptionally(error -> { log.error("Write-behind crashed...", error); return null; })`. The error is logged and swallowed; the caller was already told the event succeeded.
- **Cache Eviction and Replay**:
    - If Infinispan evicts an entry, or after node restart, `FhirCacheService` reads will trigger a cache-miss `load` which pulls from Mnemosyne.
    - In-memory cache allows overwriting by key (`remoteCache.put(id, json)` overwrites existing key with no collision check).
- **Conclusion**: The `FhirCacheService` / write-behind path **cannot preserve Kleio's append/idempotency invariant** and cannot provide durable delivery guarantees.

---

**6. Current Kleio AuditService Semantic Contract**

Defined in `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditService.java` and implemented in `InMemoryAuditService.java`.

**Contract Semantics:**
1. `append(new event)`:
    - Validates event (`eventId`, `recorded`, `type`, `action`, `outcome`, `source`).
    - If absent: stores event.
2. `append(same ID, identical event)`:
    - Compares existing event and incoming event via `canonicalDigest()` / canonical field equality (`equals(...)` in `HarmoniaAuditEvent`).
    - If identical: **idempotent success**. Returns existing or re-accepted event without error.
3. `append(same ID, divergent event)`:
    - If `existing.eventId.equals(incoming.eventId)` but `!existing.equals(incoming)`: throws `AuditIntegrityException`.
4. `get(eventId)`:
    - Returns `Optional<HarmoniaAuditEvent>`.
5. `find(AuditQuery)`:
    - Evaluates filter criteria: time range (`startTime`, `endTime`), `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, with bounded limit.

**Distinguishing Canonical Contract vs. In-Memory Detail:**
- **Canonical Service Contract (Must be in Durable Persistence)**:
    - Append-only (no update/delete methods).
    - Atomic idempotency on identical replay.
    - Fail-closed `AuditIntegrityException` on divergent event with existing `eventId`.
    - Point-read by `eventId`.
    - Audit query evaluation matching `AuditQuery` predicates.
- **In-Memory Implementation Detail (Do NOT carry into Durable Persistence)**:
    - Fixed capacity / ring-buffer eviction (`maxCapacity = 5000`, dropping oldest when limit exceeded). Durable audit evidence must never be silently evicted!
    - `ReentrantReadWriteLock` JVM heap locking.
    - `ConcurrentHashMap` / `ConcurrentLinkedDeque` collections.

---

**7. Atomicity and Concurrency Analysis**

**Enforcement Point Candidates:**
1. **Application-level read-then-write (`SELECT -> if absent -> INSERT`)**:
    - **Race Condition**: Under concurrent threads or across multiple cluster instances (e.g. `mnemosyne-clinical-1` and `mnemosyne-clinical-2`), two concurrent calls with the same `eventId` will both execute `SELECT`, both see absent, and both attempt insertion.
    - Without a database-level lock or constraint, this leads to lost updates or duplicate entries.
2. **Database Unique Constraint**:
    - `hie_fhir_resources` already enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`.
    - In PostgreSQL, concurrent `INSERT` statements with the same `(resource_type, fhir_id)` will atomically serialize at the unique B-tree index. Exactly one will succeed; the second will abort with a PostgreSQL unique constraint violation (`DataIntegrityViolationException`).
3. **Atomic Idempotent Insert Algorithm**:
    - Attempt direct database `INSERT`.
    - **Branch 1: Insert succeeds**: Event is persisted. Return success.
    - **Branch 2: Unique constraint violation caught**:
        - Inside a clean transaction (or catch block), `SELECT` the existing record by `(resource_type, fhir_id)`.
        - Parse the existing record into `HarmoniaAuditEvent` (or compare raw canonical hashes / SHA-256 digests).
        - If `incoming.equals(existing)` (identical evidence): return success (idempotent replay).
        - If `!incoming.equals(existing)` (divergent evidence): throw `AuditIntegrityException`.
    - This provides true race-safe atomicity across distributed nodes without distributed locks.

---

**8. Durable Acknowledgement Analysis**

When `AuditService.append(event)` returns success, what does it mean?

- **Meaning A: Accepted into local process memory**:
    - *Risk*: Data lost on process termination or restart. Fails Task 06 Step 03 requirement.
- **Meaning B: Accepted into cache / write-behind queue**:
    - *Risk*: Data lost on Infinispan node crash; silent failure if write-behind fails; write-behind converts appends into PUT replacements.
- **Meaning C: Accepted by Mnemosyne API**:
    - *Risk*: If Mnemosyne receives synchronous HTTP call, but delegates to `createResource`, it silently overwrites collisions instead of verifying idempotency.
- **Meaning D: Committed to durable database transaction**:
    - **Verdict**: For immutable audit evidence, **Meaning D is the required semantic**. `AuditService.append(event)` must guarantee that the transaction has successfully committed to the durable database (PostgreSQL) before returning control to the caller. Audit evidence must never vanish after acceptance.

---

**9. Read and Search Findings**

- **Kleio Canonical Query Semantics (`AuditQuery`)**:
    - Required criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`.
- **Existing Mnemosyne FHIR Search (`AuditEventResourceProvider.search` & `FhirStorageService.searchResources`)**:
    - Current provider parameters: `@OptionalParam(name = "_id")`, `@OptionalParam(name = "name")`, `@OptionalParam(name = "identifier")`.
    - In `FhirStorageService.matchesFilter`:
        - `_id`: matches `fhir_id`.
        - `name`: checks substring in `action.toCode()` or `agent.who.display`.
        - `identifier`: falls through to `matched = true` (completely unindexed/unmatched).
        - `recorded` (time range): **unimplemented**.
        - `entity.what` (target): **unimplemented**.
        - `entity.detail` / `extension` (correlationId, operationId): **unimplemented**.
- **Cleanest Ownership Boundary for READ/SEARCH**:
    - The query interface must be owned by **Kleio (`AuditService.find(AuditQuery)`)**, returning `HarmoniaAuditEvent`.
    - FHIR R5 `AuditEvent` is the persistence and interoperability format, mapped via `HarmoniaAuditEventMapper`.
    - A dedicated repository / query mechanism is needed to execute queries against the persisted audit records, rather than relying on `FhirStorageService.searchResources`.

---

**10. Inventory of Mutable External AuditEvent Surfaces**

The following surfaces currently expose mutation (CREATE/UPDATE/DELETE) capabilities for `AuditEvent`:

| Surface | Module & Class | Method | Exposed Operations | Security Domain / Current Auth | Reaches Physical Store? |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Iris BEFE REST** | `iris-befe`:<br>`AuditEventResource`<br>(lines 64–100) | `create(payload)`<br>`update(id, payload)`<br>`delete(id)` | `POST /api/fhir/AuditEvent`<br>`PUT /api/fhir/AuditEvent/{id}`<br>`DELETE /api/fhir/AuditEvent/{id}` | Hardcoded `CLINICAL` in `ThemisClinicalAuthorizationFilter`. Allows `POST` with `clinical.create`, `PUT` with `clinical.update`. Blocks `DELETE` if filter active. Accepts arbitrary JSON. | YES, via write-behind `HapiFhirRestClient` to Mnemosyne `hie_fhir_resources`. |
| **Iris Clinical UI** | `iris-clinical`:<br>`AuditEventView.vue`<br>(line 137) | `deleteAuditEvent(id)` via `securityStore.ts` (line 90) | UI "Delete" button triggers `DELETE /api/fhir/AuditEvent/{id}` | Clinical UI user session. | YES, targets Iris BEFE DELETE endpoint. |
| **Iris BEFE Cache** | `iris-befe`:<br>`FhirCacheService`<br>(lines 155, 190) | `saveResource(res)`<br>`deleteResource(type, id)` | Direct Java cache API | Internal Iris service injection. | YES, directly mutates `auditevent-cache` and write-behind. |
| **Mnemosyne HAPI FHIR Servlet** | `mnemosyne-clinical`:<br>`AuditEventResourceProvider`<br>(lines 37, 71, 88) | `create(AuditEvent)`<br>`update(IdType, AuditEvent)`<br>`delete(IdType)` | `POST /fhir/AuditEvent`<br>`PUT /fhir/AuditEvent/{id}`<br>`DELETE /fhir/AuditEvent/{id}` | **NO AUTHENTICATION / NO AUTHORIZATION**. `JpaRestfulServer` registers zero security interceptors. Internal network callers can execute unauthenticated mutations. | YES, direct write to `hie_fhir_resources`. |

*Note: All of these mutation surfaces must remain untouched during Step 03; their convergence and hardening belongs strictly to Step 04.*

---

**11. Security and Authorization Findings**

1. **Security Domain Mismatch**:
    - `AuditReadPolicy` (`themis-core/.../AuditReadPolicy.java`, lines 32, 56) explicitly requires `securityDomain == "AUDIT"` and checks for authorities `audit.read` or `system.admin`.
    - However, in `ThemisClinicalAuthorizationFilter` (`iris-befe/.../ThemisClinicalAuthorizationFilter.java`, lines 162, 170), every request under `/api/fhir/*` is hardcoded with `securityDomain = HarmoniaSecurityLabelEnum.CLINICAL.getCode()`.
    - Therefore, `AuditReadPolicy.appliesTo(...)` evaluates to **false** for `/api/fhir/AuditEvent`!
2. **Clinical Policy Misapplication**:
    - Instead, `ClinicalAuthorizationPolicy` is evaluated. It authorizes:
        - `GET /api/fhir/AuditEvent` if caller has `clinical.read` or `clinical.search`.
        - `POST /api/fhir/AuditEvent` if caller has `clinical.create`.
        - `PUT /api/fhir/AuditEvent/{id}` if caller has `clinical.update`.
        - `DELETE` is blocked by `ClinicalAuthorizationPolicy` with 403 Forbidden.
3. **Internal Append Authority**:
    - Currently, there is no distinct `audit.append` authority or separate trusted internal path for Kleio evidence submission.
4. **Direct Mnemosyne Ingress Bypasses Themis**:
    - Direct HTTP calls to `http://mnemosyne-clinical-1:8080/fhir/AuditEvent` completely bypass Iris BEFE and Themis authorization because `JpaRestfulServer` has no security filters.

---

**12. Answers to Specific Section 14 Questions**

- **Q1: Exact current AuditEvent persistence call path?**
    - Path 1 (External): `AuditEventView.vue` -> `ThemisClinicalAuthorizationFilter` -> `AuditEventResource` -> `FhirCacheService` -> Infinispan Hot Rod (`auditevent-cache`) -> [Ack to caller] -> Async Write-Behind `FhirRestCacheStore.write` -> `HapiFhirRestClient.PUT` -> `JpaRestfulServer` -> `AuditEventResourceProvider.update` -> `FhirStorageService.updateResource` -> `FhirResourceRepository` -> PostgreSQL `hie_fhir_resources`.
    - Path 2 (Internal): Client -> `JpaRestfulServer` -> `AuditEventResourceProvider` -> `FhirStorageService` -> `FhirResourceRepository` -> PostgreSQL `hie_fhir_resources`.
- **Q2: Physical store ultimately containing AuditEvent?**
    - PostgreSQL database `fhir` (Clinical DB), table `hie_fhir_resources`, shared with other FHIR resources.
- **Q3: Can the existing persistence API atomically implement absent -> append, identical -> idempotent, divergent -> violation?**
    - **No**. `FhirStorageService.createResource` performs an upsert: it finds the existing ID and overwrites the row with no equality check and no rejection.
- **Q4: Can an existing API currently UPDATE or DELETE the same persisted AuditEvent?**
    - **Yes**. `AuditEventResourceProvider.update` (PUT) and `delete` (DELETE), and `AuditEventResource.update` and `delete` are actively exposed.
- **Q5: Does current CREATE behave as true insert-only, or can it replace/upsert?**
    - It behaves as an **upsert/replace**. `FhirStorageService.createResource` (lines 80–92) explicitly updates the existing entity in place if found.
- **Q6: Where should the durable atomicity boundary live?**
    - At the **Kleio persistence adapter / database repository boundary**, leveraging the database unique constraint on `(resource_type, fhir_id)` to handle concurrent race conditions.
- **Q7: What should successful `AuditService.append()` mean in terms of durability?**
    - **Committed to durable database storage** (PostgreSQL transaction commit).
- **Q8: Should the durable Kleio path traverse `FhirCacheService` / write-behind? Why or why not?**
    - **No**. Write-behind is asynchronous (caller gets 200 before persistence), drops/logs failures silently, and issues HTTP PUTs that overwrite existing records.
- **Q9: Can current Mnemosyne FHIR search support initial Kleio read/search requirements?**
    - **No**. `AuditEventResourceProvider.search` only supports `_id`, `name`, and `identifier`. Filters for time range (`recorded`), `category`, `principal`, `target`, `correlationId`, and `operationId` are not implemented.
- **Q10: What current external surfaces could mutate evidence after Kleio stores it?**
    - `AuditEventResource` (PUT / DELETE), `AuditEventView.vue` (Delete action), and `AuditEventResourceProvider` (PUT / DELETE / POST upsert).
- **Q11: What authorization / security-domain problem exists for AuditEvent today?**
    - `/api/fhir/AuditEvent` is treated as `CLINICAL` domain instead of `AUDIT`, bypassing `AuditReadPolicy` and granting access based on `clinical.*` permissions. Direct Mnemosyne has no security.
- **Q12: Can Step 03 preserve the current physical database while creating a logically append-only Kleio persistence boundary?**
    - **Yes**. Step 03 can persist to the existing PostgreSQL database (`hie_fhir_resources` or a dedicated audit table in the same DB) via a dedicated append-only DAO/repository without needing a new physical database.
- **Q13: What is the smallest recommended architecture for Step 03?**
    - **Option B2 / C-Lite**: A dedicated append-only persistence adapter in Kleio (`kleio-persistence` or `kleio-fhir` persistence bridge) connecting directly to the durable database/repository with strict atomic `INSERT IF ABSENT` semantics and `HarmoniaAuditEventMapper`, bypassing `FhirCacheService` and write-behind.
- **Q14: What should explicitly remain for Step 04?**
    - Convergence and hardening of mutable external surfaces: disabling PUT/DELETE on `AuditEventResource` and `AuditEventResourceProvider`, removing delete buttons from `AuditEventView.vue`, routing external ingestion through Kleio, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`AUDIT` domain).
- **Q15: What should explicitly remain in AUDIT-BL-02 and AUDIT-BL-03?**
    - **AUDIT-BL-02**: Relocating physical storage from the Clinical database (`fhir`) to a dedicated Operations/Audit database (`postgres-ops` or standalone audit DB).
    - **AUDIT-BL-03**: Database-level cryptographic verification, WORM storage integration, and DB triggers preventing SQL-level UPDATE/DELETE.

---

**13. Candidate Step 03 Architectures & Trade-Offs**

**Option A: Kleio Durable Adapter -> Existing Mnemosyne FHIR AuditEvent Provider**
- **Mechanism**: Kleio calls `HapiFhirRestClient` or `AuditEventResourceProvider.create(...)`.
- **Pros**: Reuses existing provider endpoints.
- **Cons**: Broken invariant. `createResource` upserts/replaces rather than fails on conflict; PUT and DELETE remain active; cannot do atomic idempotent equality checks; search does not support audit query parameters.
- **Verdict**: **Rejected**.

**Option B: Kleio Durable Adapter -> Dedicated Append-Only Mnemosyne Path (Reusing Existing Database)**
- **Mechanism**: Add a dedicated append-only internal method/service in Mnemosyne (e.g. `MnemosyneAuditPersistenceService` / `AppendOnlyAuditEventRepository`) that operates on `hie_fhir_resources` using native `INSERT` and catches unique constraint violations.
- **Pros**: Reuses existing database connections and Spring JPA infrastructure; avoids duplicate DB connections.
- **Cons**: Couples Kleio to Mnemosyne runtime deployment; if Kleio runs in other containers, requires an internal RPC/REST endpoint.
- **Verdict**: **Viable candidate**.

**Option C: Kleio-Owned Persistence Component (`kleio-persistence`) Directly Accessing Database**
- **Mechanism**: A new submodule `kleio-persistence` (or persistence adapter inside `kleio-fhir`) implementing `AuditService`, using Spring Data JPA / JDBC against the existing datasource. Uses `HarmoniaAuditEventMapper` to convert to FHIR R5 `AuditEvent`, serializes to JSON, and executes an atomic insert query.
- **Pros**: Clean architecture, strict dependency direction (Kleio owns audit persistence contract), zero dependency on mutable `FhirStorageService`, completely bypasses write-behind and cache.
- **Cons**: Adds persistence configuration to Kleio.
- **Verdict**: **Strongest and cleanest candidate**.

---

**14. Recommended Smallest Viable Step 03 Architecture**

**Recommendation: Option C / B2 — Dedicated Append-Only Kleio Durable Persistence Boundary**

1. **Subsystem Ownership**:
    - `AuditService` interface remains in `kleio-core` as the canonical contract.
    - Implement `DurableAuditService` (in a new `kleio-persistence` submodule or within `kleio-fhir`), implementing `AuditService`.
2. **Mapping & Representation**:
    - Uses `HarmoniaAuditEventMapper` (from Step 02) to map `HarmoniaAuditEvent` <-> FHIR R5 `AuditEvent`.
    - Uses HAPI FHIR parser to serialize to JSON for database storage.
3. **Storage & Atomicity**:
    - Writes directly to the existing database table `hie_fhir_resources` (or a dedicated table in the same DB).
    - Atomicity algorithm:
        1. Attempt database `INSERT` with `(resource_type = 'AuditEvent', fhir_id = eventId)`.
        2. If successful -> return event (durably committed).
        3. If unique constraint violation occurs (`(resource_type, fhir_id)` duplicate):
            - Load existing row.
            - Parse JSON back to `AuditEvent` and map to `HarmoniaAuditEvent` (or compare canonical SHA-256 digests).
            - If matching -> return existing event (idempotent replay).
            - If divergent -> throw `AuditIntegrityException` (integrity violation).
4. **Bypass Volatile Infrastructure**:
    - Completely bypass `FhirCacheService`, Infinispan `auditevent-cache`, and asynchronous write-behind.
5. **No Mutation Operations**:
    - Exposes only `append(HarmoniaAuditEvent)`, `get(eventId)`, and `find(AuditQuery)`.
    - Exposes zero update, patch, replace, or delete methods.

---

**15. Explicit Proposed Step 03 Implementation Boundary**

**Modules and Classes to be Added / Touched:**
- **`kleio/kleio-persistence`** (or `kleio-fhir`):
    - Introduce `DurableAuditService` implementing `AuditService`.
    - Introduce `AuditEventRepository` / `AuditEventEntity` (or adapter to `hie_fhir_resources`).
    - Introduce `DurableAuditServiceTest` verifying:
        - Successful append and durable commit.
        - Idempotent replay on identical event.
        - `AuditIntegrityException` on divergent event with same ID.
        - Concurrent append race safety under duplicate IDs.
        - Bounded `find(AuditQuery)` query evaluation.
- **`kleio/pom.xml`**:
    - Add module `<module>kleio-persistence</module>` if split, or add database/JPA test dependencies to `kleio-fhir`.

**Classes That Must NOT Be Touched in Step 03:**
- Do NOT touch `AuditEventResource.java` in `iris-befe`.
- Do NOT touch `AuditEventResourceProvider.java` in `mnemosyne-clinical`.
- Do NOT touch `ThemisClinicalAuthorizationFilter.java`.
- Do NOT touch `AuditEventView.vue` or `securityStore.ts`.
- Do NOT modify database schemas or Flyway/DDL scripts.

---

**16. Explicit Boundaries for Future Steps**

**Step 04 Boundary:**
- Deprecate or remove `PUT` and `DELETE` methods in `AuditEventResource` (`iris-befe`).
- Deprecate or remove `@Update` and `@Delete` in `AuditEventResourceProvider` (`mnemosyne-clinical`).
- Remove "Delete" button from `AuditEventView.vue` in `iris-clinical`.
- Fix `ThemisClinicalAuthorizationFilter` to classify `AuditEvent` requests under `AUDIT` security domain and evaluate `AuditReadPolicy`.
- Wire `AuditEventResource.create` to route directly to `AuditService.append(...)` instead of `FhirCacheService.saveResource`.

**AUDIT-BL-02 Boundary:**
- Move audit storage from the Clinical database (`fhir`) to the dedicated Operations/Audit database (`postgres-ops`).
- Provision dedicated datasource and connection pools for audit records.

**AUDIT-BL-03 Boundary:**
- Implement database triggers (`BEFORE UPDATE OR DELETE ON audit_table RAISE EXCEPTION`).
- Configure write-once-read-many (WORM) storage or cryptographic merkle trees for external audit verification.

---

**17. Risks, Blockers, and Open Questions**

1. **Transaction Management across Modules**:
    - If `DurableAuditService` resides in `kleio`, it needs access to the active Spring/Jakarta transaction manager to guarantee commit-before-return.
2. **Search Performance**:
    - Currently, `hie_fhir_resources` stores the entire resource as `TEXT resource_json`. Filtering by timestamp, principal, or correlationId in SQL requires either JSON operators (`jsonb` / `->>`) in PostgreSQL or parsing in memory. For Step 03, query support should either use standard PostgreSQL JSON queries or dedicated indexed columns.
3. **ArchUnit Layering Rules**:
    - `PackageLayeringArchitectureTest` enforces that Calliope and Themis do not depend on Kleio. Kleio must ensure its dependencies strictly conform to: `kleio -> calliope, themis-api`.

---

**18. File, Class, and Method Reference Guide for Future Sessions**

- **Kleio Audit Service & Model**:
    - `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditService.java`
    - `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/InMemoryAuditService.java`
    - `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java`
    - `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditIntegrityException.java`
    - `kleio/kleio-fhir/src/main/java/net/fhirfactory/harmonia/kleio/fhir/HarmoniaAuditEventMapper.java`
- **Mnemosyne Storage & Provider**:
    - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java` (lines 37–101)
    - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java` (lines 70–156)
    - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/model/FhirResourceEntity.java` (lines 23–58)
    - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/repository/FhirResourceRepository.java` (lines 28–41)
    - `hestia/mnemosyne-clinical/src/main/resources/application-postgres.yml` (lines 20–35)
- **Cache & Write-Behind**:
    - `hestia/mneme-cluster/src/main/resources/infinispan.xml` (lines 194–206)
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java` (lines 99–132)
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HapiFhirRestClient.java` (lines 74–117)
- **Iris Presentation & Authorization**:
    - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` (lines 42–101)
    - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java` (lines 83–103, 155–202)
    - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java` (lines 68–217)
    - `iris/iris-clinical/src/views/AuditEventView.vue` (lines 45–65, 137–140)
    - `iris/iris-clinical/src/stores/securityStore.ts` (lines 65–98)
    - `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/AuditReadPolicy.java` (lines 29–70)
    - `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/ClinicalAuthorizationPolicy.java` (lines 32–188)

---

**19. Confirmation of Read-Only Operations**

It is confirmed that:
- Zero files were created, edited, moved, or deleted during this exploration session.
- No git operations (branch, commit, checkout, stash) were executed.
- The repository working directory remains completely clean and untouched.

---

**Discoveries for future steps**
- **Unique Constraint on Existing Store**: `hie_fhir_resources` already enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`, which provides a ready-made serialization boundary for race-safe idempotent appends.
- **Write-Behind Translates Writes to HTTP PUT**: `HapiFhirRestClient.saveResourceJson` hardcodes HTTP PUT, making `FhirRestCacheStore` write-behind structurally unsuitable for append-only audit evidence.
- **Security Domain Hardcoding**: `ThemisClinicalAuthorizationFilter` in `iris-befe` hardcodes `securityDomain = CLINICAL` for all `/api/fhir/*` paths, preventing `AuditReadPolicy` from ever firing on `/api/fhir/AuditEvent`.
- **Unauthenticated Mnemosyne Servlets**: Direct calls to `http://mnemosyne-clinical-1:8080/fhir/*` bypass all Themis filters because `JpaRestfulServer` only mounts logging and syntax-highlighter interceptors.
- **UI Delete Surface**: `iris-clinical`'s `AuditEventView.vue` contains an active UI "Delete" button that issues `DELETE /api/fhir/AuditEvent/{id}`, which will need cleanup in Step 04.



**Summary**
- Completed the requested read-only Step 03.1 exploration of Harmonia `AuditEvent` persistence and identified that current persistence is mutable, upsert-driven, and not durably acknowledged at the cache boundary.
- Determined the smallest viable Step 03 boundary is a dedicated Kleio-owned append-only durable adapter that bypasses cache/write-behind and commits atomically to durable storage before `append()` succeeds.
  **Changes**
- Traced the real end-to-end `AuditEvent` persistence paths through Iris BEFE, Infinispan/write-behind, Mnemosyne providers, generic `FhirStorageService`, and PostgreSQL `hie_fhir_resources`, including exact CRUD/upsert/delete semantics.
- Verified current Kleio `AuditService` canonical semantics (append-only, idempotent identical replay, integrity violation on divergent replay) and contrasted them with mutable generic FHIR storage behavior.
- Identified mutable external surfaces and security issues: Iris REST/UI mutation endpoints, direct unauthenticated Mnemosyne HAPI endpoints, and `/api/fhir/*` being incorrectly classified under the `CLINICAL` domain instead of `AUDIT`.
  **Verification**
- Read-only repository exploration only; no source, config, test, schema, or documentation files were modified.
- Findings were grounded in exact module/class/method tracing and storage-path inspection, including cache/write-behind and provider/repository boundaries.
  **Notes**
- Current Mnemosyne/FHIR create semantics are not insert-only: duplicate logical IDs can be replaced/upserted, and existing APIs can still update or soft-delete persisted `AuditEvent` evidence.
- Full exploration report delivered in the response, including call chains, storage findings, CRUD semantics, cache/write-behind analysis, atomicity/durability conclusions, mutable-surface inventory, security findings, and Step 03/04/backlog boundaries.

