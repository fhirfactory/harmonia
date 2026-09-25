Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Perform a read-only repository exploration for Task 06 Step 04.1 focused on audit immutability convergence and bypass paths.

Map the concrete code locations and current runtime paths for:
- Kleio baseline artifacts: `HarmoniaAuditEvent`, `AuditService`, `DurableAuditService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, `AuditQuery`, and any `@KleioAudit` DataSource producer/binding/composition roots.
- All `AuditEvent` entry/mutation/read paths across the repo, including BEFE/API, Mnemosyne/HAPI providers, storage layers, UI/service clients, cache/write-behind, repository/database access, and internal producers.
- Security classification/authorization handling for AuditEvent requests, especially `ThemisClinicalAuthorizationFilter`, any AUDIT vs CLINICAL domain routing, `AuditReadPolicy`, role mapping, and FHIR resource/path classification.
- Direct database mutation risks for `hie_fhir_resources` / `resource_type='AuditEvent'` reachable by application code.
- Existing architecture tests/guardrails relevant to Step 04.

For each important finding, cite exact repository paths plus class/method names when available. Distinguish VERIFIED vs INFERRED vs NOT FOUND where possible. Focus on gathering facts, not proposing implementation yet.

Please return:
1. A concise high-signal inventory of relevant files/classes.
2. The discovered end-to-end paths for AuditEvent read/create/update/delete if present.
3. Composition-root/runtime wiring findings for DurableAuditService and @KleioAudit.
4. Security-domain findings for AuditEvent.
5. Gaps/unknowns requiring follow-up exploration.



Open file

Open file

Exploration is Ready

**Task 06 — Step 04.1 Exploration Report: Audit Convergence and Immutability Bypass Paths**

**1. Executive Summary**

This exploration establishes the concrete baseline for **Task 06 Step 04: Establishing Kleio as the Authoritative Audit Evidence Boundary**.

The repository investigation verified that while **Kleio persistence** (`kleio-persistence` and `kleio-core`) provides a provably immutable, append-only, tamper-detecting audit evidence foundation, **Kleio is completely unwired from the runtime system**. No production subsystem currently calls `AuditService`, no CDI producer exists for `@KleioAudit DataSource`, and Iris BEFE has no Maven dependency on `kleio`.

Concurrently, a **complete legacy mutable bypass path** exists end-to-end:
1. **Iris UI (`AuditEventView.vue`)** exposes active "Emit Audit Event" (POST) and "Delete" (DELETE) controls.
2. **Iris BEFE (`AuditEventResource`)** exposes HTTP `POST`, `PUT`, `DELETE`, `GET (read)`, and `GET (search)`.
3. **Themis Security Filter (`ThemisClinicalAuthorizationFilter`)** unconditionally classifies `/api/fhir/AuditEvent` under the `CLINICAL` domain rather than `AUDIT`, entirely bypassing `AuditReadPolicy` and permitting users with standard `clinical.create`/`clinical.update` authorities to create or modify audit records.
4. **Cache & Write-Behind (`FhirCacheService` + `auditevent-cache`)** performs in-memory/volatile acknowledgements before durable persistence and writes behind to Mnemosyne.
5. **Mnemosyne (`AuditEventResourceProvider`)** exposes HAPI `@Create`, `@Update`, and `@Delete` endpoints directly mutating `hie_fhir_resources`, with resurrection capabilities on soft-deleted rows.
6. **Direct Mnemosyne Access** allows anyone with network access to port 8080 to bypass BEFE authentication entirely, defaulting to internal administrative persistence authorities.

To achieve audit immutability, Step 04 must converge reading/searching through Kleio, eliminate all external mutation surfaces, disable Mnemosyne mutable provider operations on `AuditEvent`, wire the Kleio runtime composition root, and align Themis authorization with the `AUDIT` security domain.

---

**2. Current Kleio Runtime / Persistence Baseline**

**Artifact Inventory & Locations**
- **`HarmoniaAuditEvent`** (`net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent` in `kleio/kleio-core`): Canonical immutable record representing structured audit evidence, source, principal, action, classification, and outcome.
- **`AuditService`** (`net.fhirfactory.harmonia.kleio.audit.service.AuditService` in `kleio/kleio-core`): Append-only audit contract exposing `append`, `findById` / `get`, `find(AuditQuery)`, `findByCorrelationId`, `findByPrincipal`, `getRecentEvents`, and `recordDecision`. No update or delete methods exist.
- **`DurableAuditService`** (`net.fhirfactory.harmonia.kleio.persistence.service.DurableAuditService` in `kleio/kleio-persistence`): `@ApplicationScoped` production implementation of `AuditService`.
- **`AppendOnlyAuditEventRepository`** (`net.fhirfactory.harmonia.kleio.persistence.repository.AppendOnlyAuditEventRepository` in `kleio/kleio-persistence`): Repository SPI defining `insertIfAbsent`, `findByEventId`, and `findCandidatePage`. Zero update/delete contracts.
- **`JdbcAppendOnlyAuditEventRepository`** (`net.fhirfactory.harmonia.kleio.persistence.repository.JdbcAppendOnlyAuditEventRepository` in `kleio/kleio-persistence`): `@ApplicationScoped` JDBC implementation using SQL `INSERT ... ON CONFLICT DO NOTHING`.
- **`HarmoniaAuditEventMapper`** (`net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper` in `kleio/kleio-fhir`): Bidirectional mapper between `HarmoniaAuditEvent` and HAPI FHIR R5 `org.hl7.fhir.r5.model.AuditEvent`.
- **`AuditQuery`** (`net.fhirfactory.harmonia.kleio.audit.model.AuditQuery` in `kleio/kleio-core`): Immutable query object with bounded keyset pagination and filtering.
- **`@KleioAudit` DataSource Qualifier** (`net.fhirfactory.harmonia.kleio.persistence.qualifier.KleioAudit` in `kleio/kleio-persistence`): Jakarta CDI `@Qualifier` for injecting the container-managed audit `DataSource`.

**Direct Verification of Kleio Invariants**  
[VERIFIED via `DurableAuditService.java` and `JdbcAppendOnlyAuditEventRepository.java`]:
- **Append new evidence (`insertIfAbsent`)**: Executes `INSERT INTO hie_fhir_resources ... ON CONFLICT DO NOTHING`. If 1 row inserted, returns event immediately.
- **Identical `eventId` + identical canonical evidence**: If insert returns 0 rows, queries existing row by `eventId`, parses FHIR JSON, maps to `HarmoniaAuditEvent`, compares via `equals`. If identical, returns existing event (idempotent success).
- **Identical `eventId` + divergent canonical evidence**: Throws `AuditIntegrityException("Conflicting audit event with ID ... already exists with divergent content")`.
- **Soft-deleted persisted evidence encountered**: If `row.isDeleted() == true` during replay, point-read (`findById`), or candidate query scan (`find`), immediately logs integrity violation and throws `AuditIntegrityException`.
- **Update capability**: Zero update methods exist in `AuditService`, `DurableAuditService`, or `AppendOnlyAuditEventRepository`.
- **Delete capability**: Zero delete methods exist in `AuditService`, `DurableAuditService`, or `AppendOnlyAuditEventRepository`.

---

**3. Complete AuditEvent Path Inventory**

**Path A: Legacy Iris BEFE JAX-RS REST Endpoint**
- **Entry Point**: `POST /api/fhir/AuditEvent`, `PUT /api/fhir/AuditEvent/{id}`, `DELETE /api/fhir/AuditEvent/{id}`, `GET /api/fhir/AuditEvent/{id}`, `GET /api/fhir/AuditEvent`
- **Module**: `iris/iris-befe`
- **Class / Method**: `net.fhirfactory.harmonia.befe.rest.AuditEventResource` (`create`, `update`, `delete`, `read`, `search`)
- **Caller**: Iris Clinical UI or external HTTP REST clients.
- **Security Boundary**: `ThemisClinicalAuthorizationFilter`
- **Persistence Mechanism**: `FhirCacheService` -> Infinispan Hot Rod `auditevent-cache` -> write-behind `FhirRestCacheStore` -> Mnemosyne HTTP -> `hie_fhir_resources`
- **Mutability**: CREATE: Yes, UPDATE: Yes, PATCH: No (not implemented), DELETE: Yes, Resurrection: Yes.
- **Uses Kleio**: No.
- **Durable Before Success**: No (success acknowledged upon writing to local cache/Infinispan).
- **Canonical Equality**: Bypassed entirely; arbitrary JSON accepted.

**Path B: Direct Mnemosyne Clinical HAPI Provider**
- **Entry Point**: `POST /fhir/AuditEvent`, `PUT /fhir/AuditEvent/{id}`, `DELETE /fhir/AuditEvent/{id}`, `GET /fhir/AuditEvent/{id}`, `GET /fhir/AuditEvent`
- **Module**: `hestia/mnemosyne-clinical`
- **Class / Method**: `net.fhirfactory.harmonia.hapifhir.provider.AuditEventResourceProvider` (`create`, `update`, `delete`, `read`, `search`)
- **Caller**: Infinispan write-behind (`FhirRestCacheStore`) or direct HTTP callers hitting port 8080.
- **Security Boundary**: `FhirStorageService.authorizePersistence()` (defaults to `service:mnemosyne` with full authorities when called without context; no container auth filter on Mnemosyne `/fhir/*`).
- **Persistence Mechanism**: `FhirStorageService` -> `FhirResourceRepository` (Spring Data JPA) -> `hie_fhir_resources` table.
- **Mutability**: CREATE: Yes, UPDATE: Yes, PATCH: No, DELETE: Yes (soft-delete `is_deleted = true`), Resurrection: Yes (explicitly resets `is_deleted = false` and increments `version_id`).
- **Uses Kleio**: No.
- **Durable Before Success**: Yes (synchronous PostgreSQL transaction in Spring).
- **Canonical Equality**: Bypassed entirely.

**Path C: Iris Clinical UI Management Surface**
- **Entry Point**: Vue 3 SPA views and stores
- **Module**: `iris/iris-clinical`
- **Class / Method**:
    - `src/views/AuditEventView.vue`: "Emit Audit Event" modal (lines 79–83, 147–196); per-row "Delete" trash action (lines 137–140).
    - `src/stores/securityStore.ts`: `createAuditEvent(audit)` (POST), `deleteAuditEvent(id)` (DELETE), `fetchAuditEvents(filter)` (GET).
- **Caller**: End-user in browser.
- **Security Boundary**: Upstream Elytron OIDC token forwarded to BEFE.
- **Uses Kleio**: No.

**Path D: Kleio Immutable Audit Boundary (Target / Baseline)**
- **Entry Point**: Java API `AuditService.append(HarmoniaAuditEvent)` / `AuditService.recordDecision(...)`
- **Module**: `kleio/kleio-persistence`
- **Class / Method**: `net.fhirfactory.harmonia.kleio.persistence.service.DurableAuditService`
- **Caller**: Planned trusted internal subsystems (Themis, Pylai, Ponos, Iris).
- **Security Boundary**: Internal Java contract / container security context.
- **Persistence Mechanism**: `JdbcAppendOnlyAuditEventRepository` -> JDBC `INSERT ... ON CONFLICT DO NOTHING` on `hie_fhir_resources`.
- **Mutability**: CREATE (append-only): Yes, UPDATE: Impossible, PATCH: Impossible, DELETE: Impossible, Resurrection: Triggers `AuditIntegrityException`.
- **Uses Kleio**: Yes.
- **Durable Before Success**: Yes (JDBC commit required before method return).

---

**4. Audit Path Matrix**

| ID | Source | Entry Point | Operation | Security Domain | Authorization | Intermediate Components | Persistence Target | Uses Kleio? | Durable Before Success? | Mutation Possible? | Step 04 Action |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **P-01** | Iris Clinical UI | `AuditEventView.vue` (Emit button) | CREATE | CLINICAL (misclassified) | `clinical.create` | `securityStore.ts` -> BEFE `POST /api/fhir/AuditEvent` | `auditevent-cache` -> write-behind | No | No | Yes | **REMOVE** from UI |
| **P-02** | Iris Clinical UI | `AuditEventView.vue` (Trash button) | DELETE | CLINICAL (misclassified) | `clinical.delete` (denied at BEFE filter) | `securityStore.ts` -> BEFE `DELETE /api/fhir/AuditEvent/{id}` | N/A | No | N/A | Blocked by Themis filter | **REMOVE** from UI & Store |
| **P-03** | Iris Clinical UI | `AuditEventView.vue` (Search/Table) | READ / SEARCH | CLINICAL (misclassified) | `clinical.read` / `clinical.search` | `securityStore.ts` -> BEFE `GET /api/fhir/AuditEvent` | `auditevent-cache` | No | N/A | No | **ROUTE THROUGH KLEIO** |
| **P-04** | External API | `AuditEventResource.create()` | POST | CLINICAL (misclassified) | `clinical.create` | BEFE JAX-RS -> `FhirCacheService.saveResource()` | `auditevent-cache` -> write-behind | No | No | Yes | **DENY / REMOVE** (405/403) |
| **P-05** | External API | `AuditEventResource.update()` | PUT | CLINICAL (misclassified) | `clinical.update` | BEFE JAX-RS -> `FhirCacheService.saveResource()` | `auditevent-cache` -> write-behind | No | No | Yes | **DENY / REMOVE** (405/403) |
| **P-06** | External API | `AuditEventResource.delete()` | DELETE | CLINICAL (misclassified) | Blocked by `ClinicalAuthorizationPolicy` | BEFE JAX-RS -> `FhirCacheService.deleteResource()` | `auditevent-cache` -> write-behind | No | No | Blocked at filter, but open in resource | **DENY / REMOVE** (405/403) |
| **P-07** | External API | `AuditEventResource.read()` | GET /{id} | CLINICAL (misclassified) | `clinical.read` (should be `audit.read`) | BEFE JAX-RS -> `FhirCacheService.getResourceJson()` | `auditevent-cache` | No | N/A | No | **READ THROUGH KLEIO** |
| **P-08** | External API | `AuditEventResource.search()` | GET | CLINICAL (misclassified) | `clinical.search` (should be `audit.read`) | BEFE JAX-RS -> `FhirCacheService.searchAsBundle()` | `auditevent-cache` | No | N/A | No | **READ THROUGH KLEIO** |
| **P-09** | Infinispan Write-Behind | `FhirRestCacheStore.write()` | PUT /fhir/AuditEvent/{id} | Internal Mnemosyne | Unauthenticated HTTP / Service default | `HapiFhirRestClient` | Mnemosyne `hie_fhir_resources` | No | Yes | Yes | **REMOVE** write-behind for AuditEvent |
| **P-10** | Direct Mnemosyne | `AuditEventResourceProvider.create()` | @Create | Unprotected HTTP / Service default | Service fallback: `provider.resource.create` | HAPI RestfulServer -> `FhirStorageService.createResource()` | `hie_fhir_resources` | No | Yes | Yes (and resurrects) | **HARDEN / DISABLE** |
| **P-11** | Direct Mnemosyne | `AuditEventResourceProvider.update()` | @Update | Unprotected HTTP / Service default | Service fallback: `provider.resource.update` | HAPI RestfulServer -> `FhirStorageService.updateResource()` | `hie_fhir_resources` | No | Yes | Yes (and resurrects) | **HARDEN / DISABLE** |
| **P-12** | Direct Mnemosyne | `AuditEventResourceProvider.delete()` | @Delete | Unprotected HTTP / Service default | Service fallback: `provider.resource.delete` | HAPI RestfulServer -> `FhirStorageService.deleteResource()` | `hie_fhir_resources` | No | Yes | Yes (soft-delete) | **HARDEN / DISABLE** |
| **P-13** | Direct Mnemosyne | `AuditEventResourceProvider.read()` | @Read | Unprotected HTTP / Service default | Service fallback: `provider.read` | HAPI RestfulServer -> `FhirStorageService.getResource()` | `hie_fhir_resources` | No | Yes | No | **KEEP / HARDEN** |
| **P-14** | Direct Mnemosyne | `AuditEventResourceProvider.search()` | @Search | Unprotected HTTP / Service default | Service fallback: `provider.search` | HAPI RestfulServer -> `FhirStorageService.searchResources()` | `hie_fhir_resources` | No | Yes | No | **KEEP / HARDEN** |
| **P-15** | Internal Subsystem | `DurableAuditService.append()` | APPEND | AUDIT | Java API internal | Kleio Mapper -> `JdbcAppendOnlyAuditEventRepository` | `hie_fhir_resources` (append-only) | Yes | Yes | No (tamper-evident) | **KEEP / CONVERGE ALL PRODUCERS** |

---

**5. Security-Domain and Authorization Findings**

**Classification in `ThemisClinicalAuthorizationFilter` [VERIFIED]**  
In `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java` (lines 159–174), the filter extracts the resource type from the URL path, but **hardcodes the security domain and security labels to `CLINICAL`**:
```java
ThemisResource target = ThemisResource.builder()
        .resourceType(resourceType)
        .resourceId(resourceId)
        .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
        .securityLabels(Set.of(HarmoniaSecurityLabelEnum.CLINICAL.toThemisLabel()))
        .build();
```

**Policy Routing & Misclassification Impact [VERIFIED]**
1. **`AuditReadPolicy` is never reached**: `AuditReadPolicy.appliesTo()` explicitly checks:
   `DOMAIN.equalsIgnoreCase(request.target().securityDomain()) || request.target().hasSecurityLabel(DOMAIN)` where `DOMAIN = "AUDIT"`. Because the filter supplies `CLINICAL`, this condition is false.
2. **`ClinicalAuthorizationPolicy` evaluates `AuditEvent` instead**:
    - `GET /api/fhir/AuditEvent/{id}` is evaluated under action `READ` against `AUTH_CLINICAL_READ` (`clinical.read`).
    - `GET /api/fhir/AuditEvent` is evaluated under action `SEARCH` against `AUTH_CLINICAL_SEARCH` (`clinical.search`).
    - `POST /api/fhir/AuditEvent` is evaluated under action `CREATE` against `AUTH_CLINICAL_CREATE` (`clinical.create`). A user with clinician rights can emit arbitrary synthetic audit events!
    - `PUT /api/fhir/AuditEvent/{id}` is evaluated under action `UPDATE` against `AUTH_CLINICAL_UPDATE` (`clinical.update`). A user with clinician rights can overwrite audit events!
    - `DELETE` is explicitly denied by `ClinicalAuthorizationPolicy` line 174 (`"DELETE action is not permitted on Clinical resources"`).
3. **Target Security Classification**:
   If `resourceType.equalsIgnoreCase("AuditEvent")`:
    - Security domain must be classified as **`AUDIT`** (`HarmoniaSecurityConstants.LABEL_AUDIT`).
    - Required read authority must be **`audit.read`** (`HarmoniaSecurityConstants.AUTH_AUDIT_READ`) or **`system.admin`**.
    - External mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) must be unconditionally denied (`ThemisDecisionReason.ACTION_NOT_PERMITTED` or HTTP 405 Method Not Allowed).

---

**6. BEFE / API Findings**

**Endpoint Analysis (`AuditEventResource.java`) [VERIFIED]**
- **`GET /api/fhir/AuditEvent` (`search`)**: Invokes `cacheService.searchAsBundle("AuditEvent", id, name, identifier)`. Returns JSON bundle from cache.
- **`GET /api/fhir/AuditEvent/{id}` (`read`)**: Invokes `cacheService.getResourceJson("AuditEvent", id)`. Returns JSON from cache.
- **`POST /api/fhir/AuditEvent` (`create`)**: Parses JSON to `AuditEvent`, passes to `cacheService.saveResource(resource)`, returns HTTP 201 Created.
- **`PUT /api/fhir/AuditEvent/{id}` (`update`)**: Parses JSON, sets ID, passes to `cacheService.saveResource(resource)`, returns HTTP 200 OK.
- **`DELETE /api/fhir/AuditEvent/{id}` (`delete`)**: Invokes `cacheService.deleteResource("AuditEvent", id)`, returns HTTP 204 No Content.
- **`PATCH`**: Not implemented in BEFE.

**Architectural Recommendation for BEFE**
1. **Mutations**: Remove or unconditionally reject (`405 Method Not Allowed` / `403 Forbidden`) `POST`, `PUT`, `DELETE` in `AuditEventResource`.
2. **Reads & Searches**:
    - The authoritative evidence resides in `hie_fhir_resources` via Kleio.
    - `FhirCacheService` reads from `auditevent-cache`, which is a volatile replicated cache subject to eviction, desynchronization, and bypass.
    - BEFE `AuditEventResource` should inject and delegate read/search operations to **Kleio `AuditService`** (Option B/C: delegating through `AuditService.findById()` and `AuditService.find(AuditQuery)` mapped to FHIR via `HarmoniaAuditEventMapper`).
    - This eliminates presentation dependency on a mutable, volatile cache.

---

**7. Mnemosyne / HAPI Provider Findings**

**Provider Analysis (`AuditEventResourceProvider.java`) [VERIFIED]**
- Annotated with `@Component`, registered with `JpaRestfulServer`.
- Exposes:
    - `@Create` -> `storageService.createResource(resource)`
    - `@Read` -> `storageService.getResource("AuditEvent", theId.getIdPart())`
    - `@Update` -> `storageService.updateResource(theId.getIdPart(), resource)`
    - `@Delete` -> `storageService.deleteResource("AuditEvent", theId.getIdPart())`
    - `@Search` -> `storageService.searchResources(...)`
- There is no `@Patch` method.

**Generic Storage Layer Vulnerabilities (`FhirStorageService.java`) [VERIFIED]**
- `FhirStorageService` is a generic storage engine for all resource types.
- **Resurrection Vulnerability**:
    - In `createResource()` (lines 183–195): If an entity with the same `(resource_type, fhir_id)` exists, it executes `entity.setDeleted(false)`, overwrites `entity.setResourceJson(json)`, and increments `versionId`.
    - In `updateResource()` (lines 252–257): If existing entity is found, it executes `entity.setDeleted(false)`, overwrites JSON, and increments `versionId`.
- **Soft-Delete**: `deleteResource()` executes `entity.setDeleted(true)`.

**Smallest Safe Hardening Mechanism**
- **Do NOT disable generic `updateResource` / `deleteResource` across `FhirStorageService`** (clinical resources require them).
- **In `AuditEventResourceProvider`**:
    - Remove `@Create`, `@Update`, and `@Delete` methods entirely, OR have them throw `ForbiddenOperationException("AuditEvent records are append-only immutable audit evidence and cannot be mutated or deleted via FHIR REST")`.
- **In `FhirStorageService` guard**:
    - Add an explicit invariant check in `updateResource` and `deleteResource`:
      ```java
      if ("AuditEvent".equalsIgnoreCase(resourceType)) {
          throw new ForbiddenOperationException("AuditEvent records are immutable and cannot be updated or deleted");
      }
      ```
    - This guards against any internal or generic caller bypassing the provider.

---

**8. Iris UI Findings**

**Current Controls (`iris-clinical/src/views/AuditEventView.vue`) [VERIFIED]**
1. **Header Action**: `<button @click="showCreateModal = true" class="btn btn-primary"><Plus :size="16" /><span>Emit Audit Event</span></button>`.
2. **Create Modal**: Form capturing `action`, `code`, `severity`, and `agent`, invoking `handleCreate()` -> `store.createAuditEvent(audit)` -> `POST /api/fhir/AuditEvent`.
3. **Table Row Action**: `<button @click="audit.id && store.deleteAuditEvent(audit.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete"><Trash2 :size="14" /></button>`.
4. **Search / Filter Action**: Search input filtering action/agent via `GET /api/fhir/AuditEvent?name=...`.
5. **View Detail Action**: Modal viewing FHIR JSON representation.

**Recommendation for Step 04 UI Convergence**
- **Retain**: Search input, table viewing, severity/action badges, and FHIR JSON detail modal.
- **Remove**: "Emit Audit Event" button, Create Modal, and per-row Delete Trash icon.
- **Clean Store**: Remove `createAuditEvent` and `deleteAuditEvent` from `iris-clinical/src/stores/securityStore.ts`.

---

**9. Internal Audit Producers**

**Producer Audit**
- **Themis**: Evaluates authorization requests. Policy evaluator has helper methods, but does NOT currently call `AuditService.append()` or `recordDecision()`.
- **Pylai / Ponos / Ergon / Iris**: Do NOT currently produce `HarmoniaAuditEvent` or inject `AuditService`.
- **`InMemoryAuditService` Assessment [VERIFIED]**:
    - Located in `kleio/kleio-core/src/main/java/.../InMemoryAuditService.java`.
    - An unannotated POJO with no Spring or CDI annotations (`@Component`, `@ApplicationScoped`, etc.).
    - Used exclusively in unit tests (`AuditServiceTest.java`).
    - `kleio-persistence` has `beans.xml` with `bean-discovery-mode="annotated"`. Thus, CDI cannot discover or instantiate `InMemoryAuditService` in production.
    - It is **test-only** and does not leak into production CDI runtime.

---

**10. Direct Database Mutation Risk**

**Repository & Table Analysis [VERIFIED]**
- **Table**: `hie_fhir_resources` with columns `(id, resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated)`.
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- **Kleio Repository**: `JdbcAppendOnlyAuditEventRepository` only runs:
    - `INSERT INTO hie_fhir_resources (...) VALUES ('AuditEvent', ...) ON CONFLICT DO NOTHING`
    - `SELECT ... WHERE resource_type = 'AuditEvent'`
      Zero `UPDATE` or `DELETE` SQL queries exist in Kleio.
- **Spring Data JPA Repository**: `FhirResourceRepository` extends `JpaRepository<FhirResourceEntity, Long>`.
  Reachable by `FhirStorageService`. Application code calling `FhirStorageService.updateResource()` or `deleteResource()` issues SQL updates against `hie_fhir_resources`.
- **Finding**: Closing the mutation methods in `AuditEventResourceProvider` and adding an explicit `AuditEvent` guard in `FhirStorageService` completely eliminates application-level database mutation risks for `AuditEvent`.

---

**11. Composition Root / Runtime Wiring**

**Critical Missing Wiring Discovered [VERIFIED]**
1. **No `@KleioAudit DataSource` Producer**:
   `JdbcAppendOnlyAuditEventRepository` requires `@Inject @KleioAudit DataSource dataSource`. There is **zero** producer for `@KleioAudit DataSource` in the entire codebase. CDI initialization will fail with an unsatisfied dependency if `kleio-persistence` is booted.
2. **Iris BEFE Missing Kleio Dependencies**:
   `iris/iris-befe/pom.xml` does not include `kleio-core`, `kleio-fhir`, or `kleio-persistence`.
3. **CDI Ambiguity**:
   `DurableAuditService` is annotated `@ApplicationScoped`. `InMemoryAuditService` is not annotated. There is no CDI ambiguity between `AuditService` implementations once `kleio-persistence` is active.
4. **`FhirContext` and `HarmoniaAuditEventMapper`**:
   `FhirContextProducer` in `kleio-persistence` provides `@ApplicationScoped` producers for both `FhirContext` and `HarmoniaAuditEventMapper`.

---

**12. Existing Architecture Guardrails**

**Current ArchUnit Tests in `paradeigma-test`**
- `ParadeigmaIsolationArchitectureTest`: Asserts zero production dependencies on Paradeigma.
- `SecurityEnforcementArchitectureTest`: Asserts Themis policy and security context structures.
- `IrisDecouplingArchitectureTest`: Asserts Iris does not import JPA/Hibernate/Postgres.
- `PackageLayeringArchitectureTest`: Asserts module dependency layering.

**Recommended Step 04 Architecture Guardrails**
1. **`AuditEventImmutabilityArchitectureTest`**:
    - `AuditEventResource` must not declare `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
    - `AuditEventResourceProvider` must not declare `@Create`, `@Update`, `@Delete`, or `@Patch` methods.
    - Production classes outside `kleio` must not instantiate `InMemoryAuditService`.
    - Production classes must not invoke `FhirCacheService.saveResource` or `deleteResource` with `AuditEvent`.
    - Iris BEFE must not declare any reverse dependency into `kleio-persistence` internals directly, interacting only via `AuditService` contract.

---

**13. Gaps Preventing System-Wide Audit Immutability**

1. **Routing Gap**: `ThemisClinicalAuthorizationFilter` routes `/api/fhir/AuditEvent` to `CLINICAL` domain instead of `AUDIT`, bypassing `AuditReadPolicy`.
2. **BEFE Exposure Gap**: `AuditEventResource` exposes mutable HTTP endpoints (`POST`, `PUT`, `DELETE`).
3. **Mnemosyne Exposure Gap**: `AuditEventResourceProvider` exposes mutable HAPI endpoints (`@Create`, `@Update`, `@Delete`).
4. **Storage Layer Bypass Gap**: `FhirStorageService` does not reject updates or deletes on `AuditEvent`, allowing resurrection.
5. **Cache/Write-Behind Gap**: BEFE reads/writes `AuditEvent` through volatile `auditevent-cache` with async write-behind.
6. **Composition Root Gap**: No `@KleioAudit DataSource` producer exists in the runtime container, and BEFE does not wire `AuditService`.
7. **UI Gap**: `AuditEventView.vue` provides manual emit and delete UI controls.

---

**14. Proposed Converged Target Flow**

**A. Trusted Internal Production (Append-Only Evidence)**
```
Harmonia Subsystem (Themis / Pylai / Ponos / Iris)
    │
    ▼ (Java API)
HarmoniaAuditEvent
    │
    ▼
AuditService.append(event)
    │
    ▼
DurableAuditService
    │
    ├─► Serialize to FHIR R5 JSON (HarmoniaAuditEventMapper)
    │
    └─► JdbcAppendOnlyAuditEventRepository.insertIfAbsent()
             │
             ▼
        PostgreSQL (hie_fhir_resources)
        INSERT ... ON CONFLICT DO NOTHING
        (Returns durable confirmation; detects collision/tampering/resurrection)
```

**B. External / User Access (Authoritative Read/Search)**
```
Authorised Audit Reader
    │
    ▼ HTTP GET /api/fhir/AuditEvent (or /{id})
ThemisClinicalAuthorizationFilter
    │ (Classified as AUDIT domain)
    ▼
ThemisAuthorizer -> AuditReadPolicy
    │ (Requires audit.read or system.admin)
    ▼
AuditEventResource (BEFE)
    │
    ▼ (Read / Search Query)
AuditService (DurableAuditService)
    │
    ▼
JdbcAppendOnlyAuditEventRepository.findByEventId() / findCandidatePage()
    │
    ▼
Return immutable FHIR R5 AuditEvent Bundle / Resource
```

**C. Prohibited Operations (Fail-Closed)**
- `POST /api/fhir/AuditEvent` -> `405 Method Not Allowed` / `403 Forbidden`
- `PUT /api/fhir/AuditEvent/{id}` -> `405 Method Not Allowed` / `403 Forbidden`
- `PATCH /api/fhir/AuditEvent/{id}` -> `405 Method Not Allowed` / `403 Forbidden`
- `DELETE /api/fhir/AuditEvent/{id}` -> `405 Method Not Allowed` / `403 Forbidden`
- Direct Mnemosyne `@Create`, `@Update`, `@Delete` -> Disabled/Rejected in Provider and Storage Service.
- Volatile write-behind for AuditEvent -> Retired from `infinispan.xml`.

---

**15. Recommended Step 04 Implementation Blocks**

****Block 04.1: Explore Convergence and Bypass Paths** *(Current Step — COMPLETED)***  
Read-only analysis and inventory of all paths, boundaries, and gaps.

****Block 04.2: Establish Runtime Kleio Composition & Container Wiring****
- Provide container `@KleioAudit DataSource` producer in WildFly / BEFE runtime.
- Add `kleio-core`, `kleio-fhir`, and `kleio-persistence` dependencies to `iris-befe`.
- Verify CDI injection of `AuditService` (resolving to `DurableAuditService`).
- Add integration test proving `DurableAuditService` activates in the container.

****Block 04.3: Themis Security Domain & Authorization Convergence****
- Update `ThemisClinicalAuthorizationFilter`: detect `resourceType.equalsIgnoreCase("AuditEvent")` and set `securityDomain = "AUDIT"` with label `AUDIT`.
- Ensure requests reach `AuditReadPolicy`, requiring `audit.read` or `system.admin`.
- Ensure mutation attempts fail closed with default-deny if not rejected earlier.

****Block 04.4: Converge External AuditEvent Surface in BEFE (Read/Search Delegation & Mutation Denials)****
- Remove or explicitly deny (`405 Method Not Allowed`) `POST`, `PUT`, `DELETE` methods in `AuditEventResource`.
- Refactor `read()` and `search()` in `AuditEventResource` to delegate directly to `AuditService` instead of `FhirCacheService`.
- Retire `auditevent-cache` write-behind path from `infinispan.xml`.

****Block 04.5: Disable Direct Mnemosyne AuditEvent Mutation****
- Remove `@Create`, `@Update`, `@Delete` from `AuditEventResourceProvider` (or throw `ForbiddenOperationException`).
- Add defensive guard in `FhirStorageService.updateResource()` and `deleteResource()` preventing operations on `resourceType = "AuditEvent"`.

****Block 04.6: Iris UI Convergence****
- Remove "Emit Audit Event" button and modal from `AuditEventView.vue`.
- Remove "Delete" action button from `AuditEventView.vue`.
- Remove `createAuditEvent` and `deleteAuditEvent` from `securityStore.ts`.

****Block 04.7: Architecture Guardrails & Acceptance Verification****
- Create ArchUnit test suite asserting no mutable AuditEvent endpoints, no write-behind on AuditEvent, and strict AUDIT domain classification.
- Execute integration tests verifying audit immutability, tamper detection, and end-to-end audit reading.

---

**16. Risks, Dependencies, and Deferred Backlog**

- **Risk: Iris Decoupling Invariant (Invariant 3)**:
  `iris-befe` is prohibited from importing JPA/Hibernate or PostgreSQL drivers.
  *Mitigation*: Kleio persistence (`kleio-persistence`) uses pure JDBC (`javax.sql.DataSource`, `java.sql.Connection`) and does not import JPA, Hibernate, or PostgreSQL driver classes in its production code. The DataSource is container-managed (JNDI in WildFly).
- **Deferred Backlog**:
    - `AUDIT-BL-02`: Relocation of audit storage to a dedicated audit database separate from `hie_fhir_resources` (Task 06 Step 04 maintains storage in `hie_fhir_resources` as established in Step 03).
    - `AUDIT-BL-03`: PostgreSQL database-level WORM triggers and table partitioning (deferred to future infrastructure hardening).

---

**17. Next Implementation Block (Step 04.2) Target Files**

For **Step 04.2 (Runtime Kleio Composition & Container Wiring)**, the following files will be created or modified:
1. `iris/iris-befe/pom.xml`: Add dependencies on `kleio-core`, `kleio-fhir`, and `kleio-persistence`.
2. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/KleioDataSourceProducer.java` *(New File)*: CDI producer class producing `@Produces @KleioAudit DataSource` from container JNDI resource (e.g., `java:jboss/datasources/HarmoniaDS` or configured fallback).
3. `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/KleioCdiWiringTest.java` *(New Test File)*: Verification test confirming CDI context resolves `AuditService` to `DurableAuditService`.

---

**Discoveries for Future Steps**
- `DurableAuditService` is fully functional with unit/integration tests in `kleio-persistence`, but has zero callers in production code.
- `ThemisClinicalAuthorizationFilter` hardcodes domain `CLINICAL`, completely shielding `AuditReadPolicy` from ever being evaluated.
- `AuditEventResource` in BEFE and `AuditEventResourceProvider` in Mnemosyne both permit full mutable CRUD on audit records today.
- `InMemoryAuditService` is an unannotated POJO used only in unit tests; no risk of accidental CDI production injection.
- Mnemosyne `FhirStorageService` explicitly resets `deleted = false` on conflict, enabling resurrection of deleted records.
- Invariant 3 allows container-managed DataSource injection in Iris BEFE as long as JPA/Hibernate/driver imports are avoided.

**EXPLORATION ONLY — NO CODE MODIFICATIONS MADE.**  
**STOPPING AS MANDATED BY STEP 04.1.**



EXPLORATION

**Task description**  
Perform a second focused read-only pass to tighten repository-grounded evidence for the Step 04.1 report.

Please verify and cite exact repository paths, class names, method names, and line numbers where feasible for these areas:
- BEFE `AuditEventResource`, `FhirCacheService`, and any cache config / write-behind classes handling `AuditEvent`.
- Mnemosyne `AuditEventResourceProvider`, `FhirStorageService`, `FhirResourceRepository`, and the concrete create/update/delete/resurrection logic for `AuditEvent` in `hie_fhir_resources`.
- `ThemisClinicalAuthorizationFilter`, `AuditReadPolicy`, and any role/authority constants used for audit vs clinical read/search/create/update/delete.
- Iris UI files (`AuditEventView.vue`, stores/services) that expose AuditEvent create/edit/delete/read/search capabilities.
- Any deploy/config files showing direct Mnemosyne exposure, BEFE routes, or Infinispan cache names for AuditEvent.
- Existing ArchUnit tests touching Kleio, security, Iris decoupling, or package layering relevant to proposed Step 04 guardrails.

Please also explicitly list any operation you searched for but did NOT find (e.g. PATCH surfaces, direct SQL DELETE for AuditEvent, UI edit controls), with NOT FOUND status.

Return the result as a structured evidence list suitable for citation in a final report.



Exploration is Ready

**Task 06 — Step 04.1: Audit Convergence and Immutability Bypass Paths**  
**Grounded Repository Evidence Report**

This report presents verified repository-grounded evidence for **Task 06 Step 04.1**, citing exact file paths, class names, method names, and line numbers across all requested areas.

---

**1. Iris BEFE Audit Surfaces & Cache Architecture**

**A. JAX-RS Resource: `AuditEventResource`**
- **File**: `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
- **Class**: `net.fhirfactory.harmonia.befe.rest.AuditEventResource`
- **Annotations**:
    - Line 48: `@Path("/fhir/AuditEvent")` (Resolved under root application path `/api`, producing full endpoint `/api/fhir/AuditEvent` via `JaxRsActivator.java` line 23)
    - Line 49: `@ApplicationScoped`
    - Lines 51–52: `@Produces(MediaType.APPLICATION_JSON)`, `@Consumes(MediaType.APPLICATION_JSON)`
    - Line 55: `@Inject private FhirCacheService cacheService;`
- **Endpoints Exposed**:
    1. **SEARCH (`GET /api/fhir/AuditEvent`)** [VERIFIED]:
        - Lines 66–72: `public Response search(@QueryParam("_id") String id, @QueryParam("name") String name, @QueryParam("identifier") String identifier)`
        - Invokes `cacheService.searchAsBundle("AuditEvent", id, name, identifier)`.
    2. **READ (`GET /api/fhir/AuditEvent/{id}`)** [VERIFIED]:
        - Lines 74–85: `public Response read(@PathParam("id") String id)`
        - Invokes `cacheService.getResourceJson("AuditEvent", id)`.
    3. **CREATE (`POST /api/fhir/AuditEvent`)** [VERIFIED]:
        - Lines 87–104: `public Response create(String resourceJson)`
        - Parses JSON to HAPI R5 `AuditEvent`, calls `cacheService.saveResource(resource)`, returns HTTP 201 Created.
    4. **UPDATE (`PUT /api/fhir/AuditEvent/{id}`)** [VERIFIED]:
        - Lines 106–126: `public Response update(@PathParam("id") String id, String resourceJson)`
        - Parses JSON, sets `resource.setId(id)`, calls `cacheService.saveResource(resource)`, returns HTTP 200 OK.
    5. **DELETE (`DELETE /api/fhir/AuditEvent/{id}`)** [VERIFIED]:
        - Lines 128–136: `public Response delete(@PathParam("id") String id)`
        - Calls `cacheService.deleteResource("AuditEvent", id)`, returns HTTP 204 No Content.

**B. In-Memory Cache Service: `FhirCacheService`**
- **File**: `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java`
- **Class**: `net.fhirfactory.harmonia.befe.service.FhirCacheService`
- **Annotations / Injection**:
    - Line 48: `@ApplicationScoped`
    - Lines 51–54: `@Inject private InfinispanManager infinispanManager;`
- **Cache Name Mapping**:
    - Lines 56–65 (`getCache`): Resolves `resourceType.toLowerCase(Locale.ROOT) + "-cache"`, mapping `"AuditEvent"` to `"auditevent-cache"`.
- **Cache Operations**:
    - Lines 67–83 (`getResourceJson`): Directly retrieves payload via `cache.get(id)`.
    - Lines 85–103 (`saveResource`): Extracts FHIR ID and calls `cache.put(id, json)`. **Acknowledges success to caller immediately upon writing to volatile/in-memory cache before durable persistence occurs!**
    - Lines 105–117 (`deleteResource`): Calls `cache.remove(id)`.
    - Lines 119–147 (`searchAsBundle`): Scans `cache.values()`, parses JSON in-memory, and filters matches.

**C. Infinispan Grid & Write-Behind Persistence Store**
- **Cache Definition File**: `hestia/mneme-cluster/src/main/resources/infinispan.xml`
    - Lines 194–206:
      ```xml
      <replicated-cache name="auditevent-cache" mode="SYNC" statistics="true">
          <encoding>
              <key media-type="text/plain"/>
              <value media-type="text/plain"/>
          </encoding>
          <persistence>
              <store class="net.fhirfactory.harmonia.persistence.store.FhirRestCacheStore" shared="false" preload="false" fetch-state="false" segmented="false">
                  <property name="serverUrl">${fhir.server.url:http://mnemosyne-clinical-1:8080/fhir}</property>
                  <property name="timeoutSeconds">10</property>
                  <write-behind modification-queue-size="1024" />
              </store>
          </persistence>
      </replicated-cache>
      ```
- **Write-Behind Store Implementation**: `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`
    - Lines 104–114 (`write`): Dispatches cache write entries asynchronously via `restClient.saveResourceJson(coord.resourceType(), coord.id(), jsonPayload)`.
    - Lines 124–135 (`delete`): Dispatches cache eviction/delete entries asynchronously via `restClient.deleteResource(coord.resourceType(), coord.id())`.
    - Line 229: Explicit mapping `case "auditevent" -> "AuditEvent";`.
- **Downstream REST Client**: `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HapiFhirRestClient.java`
    - Lines 58–69 (`saveResourceJson`): Executes HTTP `PUT ${serverUrl}/${resourceType}/${id}` against Mnemosyne.
    - Lines 76–85 (`deleteResource`): Executes HTTP `DELETE ${serverUrl}/${resourceType}/${id}` against Mnemosyne.

---

**2. Mnemosyne Clinical Storage & Mutation Logic**

**A. HAPI FHIR Provider: `AuditEventResourceProvider`**
- **File**: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java`
- **Class**: `net.fhirfactory.harmonia.hapifhir.provider.AuditEventResourceProvider implements IResourceProvider`
- **Annotation**: Line 37: `@Component`
- **Contract Methods**:
    - Lines 42–45: `getResourceType()` returns `AuditEvent.class`.
    - Lines 47–52: `@Create public MethodOutcome create(@ResourceParam AuditEvent resource, ...)` -> delegates to `storageService.createResource(resource)`.
    - Lines 54–61: `@Read public AuditEvent read(@IdParam IdType theId, ...)` -> delegates to `storageService.getResource("AuditEvent", theId.getIdPart())`.
    - Lines 63–71: `@Update public MethodOutcome update(@IdParam IdType theId, @ResourceParam AuditEvent resource, ...)` -> delegates to `storageService.updateResource(theId.getIdPart(), resource)`.
    - Lines 73–81: `@Delete public MethodOutcome delete(@IdParam IdType theId, ...)` -> delegates to `storageService.deleteResource("AuditEvent", theId.getIdPart())`.
    - Lines 83–99: `@Search public List<IBaseResource> search(...)` -> delegates to `storageService.searchResources(...)`.

**B. Core Storage Engine: `FhirStorageService`**
- **File**: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java`
- **Class**: `net.fhirfactory.harmonia.hapifhir.service.FhirStorageService`
- **Annotation**: Line 54: `@Service`
- **Mutation & Resurrection Logic in PostgreSQL (`hie_fhir_resources`)**:
    - **Create / Resurrection Path** (Lines 183–195):
      ```java
      existingOpt.ifPresentOrElse(entity -> {
          entity.setDeleted(false);
          entity.setResourceJson(json);
          entity.setVersionId(entity.getVersionId() != null ? entity.getVersionId() + 1 : 1L);
          entity.setLastUpdated(Instant.now());
          savedEntityRef.set(resourceRepository.save(entity));
      }, () -> { ... });
      ```
      *Impact*: If an audit event with the same ID already exists (even if previously soft-deleted), it **resurrects** the entity (`setDeleted(false)`), overwrites the JSON, and increments `version_id`.
    - **Update Path** (Lines 252–257):
      ```java
      existing.setDeleted(false);
      existing.setResourceJson(json);
      existing.setVersionId(existing.getVersionId() != null ? existing.getVersionId() + 1 : 1L);
      existing.setLastUpdated(Instant.now());
      FhirResourceEntity saved = resourceRepository.save(existing);
      ```
      *Impact*: Completely replaces the audit event JSON payload with arbitrary content and resurrects soft-deleted records.
    - **Delete Path** (Lines 294–297):
      ```java
      existing.setDeleted(true);
      existing.setLastUpdated(Instant.now());
      resourceRepository.save(existing);
      ```
      *Impact*: Soft-deletes the row in `hie_fhir_resources` by marking `is_deleted = true`.
    - **Security Bypass in AuthorizePersistence** (Lines 99–158, specifically line 144):
      Direct REST requests to Mnemosyne have no `ThemisSecurityContext`. Line 144 falls back to `HarmoniaServiceIdentities.PRINCIPAL_MNEMOSYNE` with full persistence authorities (`provider.resource.create/update/delete`), completely bypassing BEFE authentication and authorization filters.

**C. JPA Repository & Model**
- **File**: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/repository/FhirResourceRepository.java`
    - Lines 33–45: `public interface FhirResourceRepository extends JpaRepository<FhirResourceEntity, Long>` exposes `findByResourceTypeAndFhirId`, `findByResourceTypeAndFhirIdAndDeletedFalse`, and `findByResourceType`.
- **File**: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/model/FhirResourceEntity.java`
    - Lines 40–44: Maps to table `hie_fhir_resources` with unique constraint `uk_resource_type_fhir_id` on `("resource_type", "fhir_id")`.
    - Columns: `id` (bigserial), `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`.

---

**3. Themis Security Domain & Authorization Routing**

**A. Security Filter: `ThemisClinicalAuthorizationFilter`**
- **File**: `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`
- **Class**: `net.fhirfactory.harmonia.befe.security.ThemisClinicalAuthorizationFilter implements ContainerRequestFilter`
- **Annotations**: Line 57: `@Provider`, Line 58: `@PreMatching`
- **Root Cause of Audit Misclassification**:
    - Lines 128–131: Parses `resourceType` from the URI path (e.g. `"AuditEvent"`).
    - Lines 159–164: Hardcodes target security domain and labels to `CLINICAL`:
      ```java
      ThemisResource target = ThemisResource.builder()
              .resourceType(resourceType)
              .resourceId(resourceId)
              .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
              .securityLabels(Set.of(HarmoniaSecurityLabelEnum.CLINICAL.toThemisLabel()))
              .build();
      ```
    - Lines 167–174: Hardcodes context security domain to `CLINICAL`:
      ```java
      ThemisSecurityContext secContext = ThemisSecurityContext.builder()
              .requestingPrincipal(principal)
              .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE)
              .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
              .authorities(authorities)
              ...
      ```
    - Lines 226–237 (`mapMethodToAction`):
        - `GET` with ID -> `ThemisAction.READ`
        - `GET` without ID -> `ThemisAction.SEARCH`
        - `POST` -> `ThemisAction.CREATE`
        - `PUT` / `PATCH` -> `ThemisAction.UPDATE`
        - `DELETE` -> `ThemisAction.DELETE`

**B. Audit Policy vs Clinical Policy**
- **Audit Policy File**: `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/AuditReadPolicy.java`
    - Lines 31–35: `POLICY_ID = "audit-read-policy"`, `DOMAIN = "AUDIT"`, `AUTH_AUDIT_READ = "audit.read"`, `AUTH_SYSTEM_ADMIN = "system.admin"`.
    - Lines 52–60 (`appliesTo`):
      ```java
      boolean matchesDomain = DOMAIN.equalsIgnoreCase(request.target().securityDomain())
              || request.target().hasSecurityLabel(DOMAIN);
      boolean matchesAction = request.action() == ThemisAction.READ || request.action() == ThemisAction.SEARCH;
      return matchesDomain && matchesAction;
      ```
    - **VERIFIED**: Because `ThemisClinicalAuthorizationFilter` sets domain to `CLINICAL`, `AuditReadPolicy.appliesTo()` evaluates to `false`. **`AuditReadPolicy` is never evaluated for any AuditEvent request.**
- **Clinical Policy File**: `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/ClinicalAuthorizationPolicy.java`
    - Lines 34–42: `POLICY_ID = "clinical-authorization-policy"`, `DOMAIN = "CLINICAL"`.
    - Lines 59–65 (`appliesTo`): Evaluates all requests where domain is `CLINICAL`. Therefore, `ClinicalAuthorizationPolicy` handles all `AuditEvent` requests!
    - Lines 118–186 (`evaluate`):
        - `READ`: Allowed if request has `clinical.read` (line 120).
        - `SEARCH`: Allowed if request has `clinical.search` (line 131).
        - `CREATE`: Allowed if request has `clinical.create` (line 142).
        - `UPDATE`: Allowed if request has `clinical.update` (line 153).
        - `DELETE`: Explicitly denied at line 174 (`"DELETE action is not permitted on Clinical resources"`).

**C. Security Constants & Role Mappings**
- **File**: `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/constants/HarmoniaSecurityConstants.java`
    - Lines 38–43: `LABEL_AUDIT = "AUDIT"`, `LABEL_CLINICAL = "CLINICAL"`.
    - Lines 56–60: `AUTH_CLINICAL_READ = "clinical.read"`, `AUTH_CLINICAL_SEARCH = "clinical.search"`, `AUTH_CLINICAL_CREATE = "clinical.create"`, `AUTH_CLINICAL_UPDATE = "clinical.update"`.
    - Line 65: `AUTH_AUDIT_READ = "audit.read"`.
    - Line 67: `AUTH_SYSTEM_ADMIN = "system.admin"`.
    - Lines 75–82: Roles `CLINICAL_READ`, `CLINICAL_WRITE`, `CLINICAL_ADMIN`, `AUD_RDR`, `SYS_ADM`.
- **File**: `calliope/src/main/java/net/fhirfactory/harmonia/model/security/HarmoniaRoleEnum.java`
    - Lines 57–60: `CLINICAL_READ` maps to `clinical.read`, `clinical.search`.
    - Lines 61–66: `CLINICAL_WRITE` maps to `clinical.read`, `clinical.search`, `clinical.create`, `clinical.update`.
    - Lines 74–76: `AUD_RDR` maps to `audit.read`.
    - Lines 80–82: `SYS_ADM` maps to `system.admin`.

---

**4. Iris UI Audit Capabilities & Controls**

**A. Vue 3 View: `AuditEventView.vue`**
- **File**: `iris/iris-clinical/src/views/AuditEventView.vue`
- **READ / SEARCH Capabilities** [VERIFIED]:
    - Lines 37–39: `onMounted(() => { store.fetchAuditEvents(); });` triggers initial load.
    - Lines 41–43 & Lines 86–100: Search filter input bound to `searchName`, invoking `store.fetchAuditEvents(searchName.value)`.
    - Lines 103–144: Render table of audit events.
    - Lines 62–65, 134–136, 199–211: View Details eye button opening JSON inspect modal.
- **CREATE Surface (Mutating)** [VERIFIED]:
    - Lines 79–82: Header button `<button @click="showCreateModal = true" class="btn btn-primary"><span>Emit Audit Event</span></button>`.
    - Lines 45–60: `handleCreate()` constructing an `AuditEvent` payload and calling `store.createAuditEvent(audit)`.
    - Lines 146–196: Create Modal form allowing user input for `action` (C, R, U, D, E), `code`, `severity`, and `agent`.
- **DELETE Surface (Mutating)** [VERIFIED]:
    - Lines 137–140: Per-row trash icon button: `<button @click="audit.id && store.deleteAuditEvent(audit.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">`.

**B. Pinia Store: `securityStore.ts`**
- **File**: `iris/iris-clinical/src/stores/securityStore.ts`
- **Methods**:
    - Lines 66–77: `fetchAuditEvents(name?: string)` invokes `fhirApi.search<AuditEvent>('AuditEvent', params)`.
    - Lines 79–88: `createAuditEvent(audit: Partial<AuditEvent>)` invokes `fhirApi.create<AuditEvent>('AuditEvent', audit)`.
    - Lines 90–98: `deleteAuditEvent(id: string)` invokes `fhirApi.delete('AuditEvent', id)`.

**C. Client Layer: `fhirClient.ts`**
- **File**: `iris/iris-clinical/src/api/fhirClient.ts`
    - Lines 31–42: `search()` -> HTTP `GET /api/fhir/${resourceType}`
    - Lines 44–47: `get()` -> HTTP `GET /api/fhir/${resourceType}/${id}`
    - Lines 49–52: `create()` -> HTTP `POST /api/fhir/${resourceType}`
    - Lines 54–57: `update()` -> HTTP `PUT /api/fhir/${resourceType}/${id}`
    - Lines 59–61: `delete()` -> HTTP `DELETE /api/fhir/${resourceType}/${id}`

**D. Administration Governance View: `AuditGovernanceView.vue`**
- **File**: `iris/iris-administration/src/views/AuditGovernanceView.vue`
    - Lines 22–41: Static UI placeholder view. Contains zero API calls and zero data mutation surfaces.

---

**5. Deployment and Exposure Topology**

**A. Docker Compose Service Mapping**
- **File**: `docker-compose.yml`
- **Direct Mnemosyne Port Exposure** [VERIFIED]:
    - Lines 149–172 (`hapi-fhir-jpa-server-1`): Maps port `"8081:8080"` directly to host network.
    - Lines 173–196 (`hapi-fhir-jpa-server-2`): Maps port `"8082:8080"` directly to host network.
    - *Vulnerability*: Any client on the network can connect directly to `http://localhost:8081/fhir/AuditEvent` or `http://localhost:8082/fhir/AuditEvent`, completely bypassing BEFE and Themis authorization.
- **BEFE Port Exposure**:
    - Lines 292–310 (`befe`): Maps ports `"8080:8080"`, `"8090:8090"`, and `"9990:9990"`.

**B. Route Configuration**
- **BEFE Root Path**: `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/JaxRsActivator.java` (Line 23: `@ApplicationPath("/api")`).
- **Mnemosyne Servlet Mapping**: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/FhirServerConfig.java` (Lines 35–39: Servlet mapped to `"/fhir/*"`).

---

**6. Relevant Existing ArchUnit Tests**

**A. Security & Kleio Boundary Tests**
- **File**: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`
    - Lines 80–84 (`kleioAuditServiceContractDefined`): Asserts `AuditService` is an interface.
    - Lines 86–117 (`kleioFhirBoundaryEnforcement`): Enforces that `net.fhirfactory.harmonia.kleio.fhir..` has zero dependencies on `jakarta.persistence..`, `ca.uhn.fhir.jpa..`, `hestia..`, or `iris..`.
    - Lines 120–129 (`themisMustNotDependOnKleioFhir`): Asserts `themis..` does not depend on `kleio.fhir..`.
    - Lines 220–268 (`irisClinicalFhirEndpointsMustBeProtectedByCommonAuthorizationFilter`): Asserts `ThemisClinicalAuthorizationFilter` exists with `@Provider` and `@PreMatching`, and all BEFE clinical resources are routed under `/fhir/*`.
    - Lines 534–545 (`canonicalAuditModelResidesUnderKleioPackage`): Asserts all canonical audit models reside strictly in `net.fhirfactory.harmonia.kleio.audit.model`.
    - Lines 547–570 (`kleioAuditModelMustNotDependOnHapiOrFhir`): Asserts `HarmoniaAuditEvent` and audit models have zero dependencies on HAPI/FHIR.
    - Lines 572–595 (`kleioAuditModelMustNotDependOnJpa`): Asserts zero dependencies on JPA.
    - Lines 597–622 (`kleioAuditModelMustNotDependOnJmsOrArtemis`): Asserts zero dependencies on JMS/Artemis.
    - Lines 624–649 (`kleioAuditModelMustNotDependOnJakartaSecurityContext`): Asserts zero dependencies on Jakarta SecurityContext.
    - Lines 651–670 (`auditServiceMustNotContainMutationMethods`): Uses reflection to assert that `AuditService` interface methods do not start with `update`, `delete`, `replace`, `patch`, or equal `clear`.
    - Lines 673–711 (`harmoniaAuditEventMustUseSafeStringRepresentation`): Asserts `toString()` suppresses raw authorities and attributes.
    - Lines 725–747 (`noCanonicalThemisAuditClassesExist`): Asserts zero legacy `ThemisAuditEvent` or `ThemisAuditService` classes exist.
    - Lines 750–763 (`themisMustNotDependOnKleio`): Asserts Themis API and Core do not depend on Kleio.
    - Lines 766–796 (`kleioMustOnlyDependOnThemisApiAndNotThemisCore`): Asserts Kleio depends only on Themis API, zero dependencies on Themis Core, Calliope, Hestia, Petasos, Ponos, Pylai, Iris, or Agora.
    - Lines 799–846 (`zeroDuplicateCanonicalAuditModelsAcrossThemisAndKleio`): Asserts canonical audit models reside exclusively in Kleio.
    - Lines 849–898 (`kleioSubprojectIsolationAndPersistenceLayering`): Enforces Kleio persistence layering rules.

**B. Iris Decoupling Architecture Tests**
- **File**: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
    - Lines 39–64 (`irisPomsMustNotDeclareDatabaseOrJpaDependencies`): Asserts Iris POMs do not declare PostgreSQL, JPA, or Hibernate dependencies.
    - Lines 67–95 (`irisBefeSourcesMustNotImportJpaOrPostgres`): Asserts zero imports of `jakarta.persistence`, `javax.persistence`, `org.hibernate`, `org.postgresql`, `java.sql.DriverManager`, `ca.uhn.fhir.jpa` in `iris-befe`.
    - Lines 98–119 (`irisSpasMustNotContainBackendDatabaseDrivers`): Asserts SPAs do not import database drivers.

**C. Package Layering Tests**
- **File**: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java`
    - Lines 46–69 (`calliopeMustNotDependOnHigherLayers`): Enforces Calliope independence.
    - Lines 72–93 (`themisApiMustNotDependOnHigherLayers`): Enforces Themis API independence.

---

**7. Explicit "NOT FOUND" Operations**

The following operations were systematically searched for across the entire repository and **DO NOT EXIST**:

| Searched Operation | Scope Searched | Status | Verification Detail |
| :--- | :--- | :--- | :--- |
| **BEFE AuditEvent `@PATCH` Endpoint** | `iris/iris-befe` | **NOT FOUND** | Zero `@PATCH` annotations exist on `AuditEventResource` or across the `iris-befe` subproject. |
| **Mnemosyne HAPI FHIR `@Patch` Operation** | `hestia/mnemosyne-clinical` | **NOT FOUND** | Zero `@Patch` annotations exist on `AuditEventResourceProvider` or in `mnemosyne-clinical`. |
| **Iris UI AuditEvent Edit / Update Controls** | `iris/iris-clinical` | **NOT FOUND** | `AuditEventView.vue` has no edit buttons, forms, or update handlers; `securityStore.ts` does not implement `updateAuditEvent`. |
| **Direct SQL `DELETE FROM hie_fhir_resources`** | Entire repository (`.`) | **NOT FOUND** | No hard SQL `DELETE` exists in application code. Deletions in Mnemosyne are executed via JPA soft-delete (`entity.setDeleted(true)`). |
| **Direct SQL `UPDATE hie_fhir_resources` for AuditEvent** | `kleio` | **NOT FOUND** | `JdbcAppendOnlyAuditEventRepository` only issues `INSERT ... ON CONFLICT DO NOTHING` and `SELECT ... WHERE resource_type = 'AuditEvent'`. |
| **`@KleioAudit DataSource` CDI Producer** | Entire repository (`.`) | **NOT FOUND** | `JdbcAppendOnlyAuditEventRepository` requires `@Inject @KleioAudit DataSource`, but no `@Produces @KleioAudit DataSource` CDI producer exists in the codebase. |
| **Kleio Maven Dependency in `iris-befe`** | `iris/iris-befe/pom.xml` | **NOT FOUND** | `iris-befe` does not declare dependencies on `kleio-core`, `kleio-fhir`, or `kleio-persistence`. |

---

**Discoveries for Future Steps**
- **Execution commands**: The ArchUnit test suite runs via:
  `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` (~15s).
- **Direct port bypass**: Mnemosyne HAPI FHIR ports `8081` and `8082` are exposed directly to the host in `docker-compose.yml`, which bypasses BEFE security filters unless hardened at provider or service level.
- **Cache write-behind risk**: In `infinispan.xml`, `auditevent-cache` is configured with `FhirRestCacheStore` write-behind, causing volatile immediate success responses before Mnemosyne persistence.
- **Themis filter classification**: In `ThemisClinicalAuthorizationFilter.java` (lines 159–164), `securityDomain` is hardcoded to `CLINICAL`, causing all `AuditEvent` requests to route to `ClinicalAuthorizationPolicy` instead of `AuditReadPolicy`.
- **UI clean-up target**: `AuditEventView.vue` contains an active "Emit Audit Event" modal and per-row "Delete" button, calling `store.createAuditEvent` and `store.deleteAuditEvent`.
- **Zero SQL hard deletes**: All deletions in Mnemosyne are JPA soft-deletes (`is_deleted = true`).
- **Resurrection in Mnemosyne**: `FhirStorageService.createResource` and `updateResource` both explicitly execute `entity.setDeleted(false)`, resurrecting soft-deleted records.
- **Missing Kleio CDI producer**: `JdbcAppendOnlyAuditEventRepository` injects `@KleioAudit DataSource`, but no CDI producer exists for this qualifier in the entire repository.

**READ-ONLY EXPLORATION PASS COMPLETED — ZERO CODE MODIFICATIONS MADE.**



ANALYSIS

**Task description**  
Using the repository findings already gathered for Step 04.1, synthesize the smallest repository-grounded converged architecture and implementation breakdown for Step 04.

Focus on:
- Whether BEFE read/search should stay on generic FHIR infrastructure or delegate to Kleio `AuditService`, and why.
- The smallest safe way to make AuditEvent immutable without breaking generic mutable CRUD for other FHIR resources.
- Exact Step 04 implementation block ordering, dependencies, and smallest useful granularity.
- Exact files/classes most likely to change in the *next* implementation block only.
- Call out any places where the earlier exploration may have made assumptions that should be labeled INFERRED vs VERIFIED.

Return a concise design/recommendation memo, not code.



**Step 04: Authoritative Audit Evidence Boundary — Architectural Synthesis & Implementation Plan**

**1. Executive Summary & Architectural Positioning**

Task 06 Step 03 established that Kleio persistence (`kleio-persistence`, `kleio-core`, `kleio-fhir`) is append-only, durable, and tamper-detecting. However, repository exploration confirms that Kleio remains completely unwired from the live runtime. Concurrently, a legacy mutable path spans the Iris UI, Iris BEFE, Infinispan cache with write-behind, and Mnemosyne HAPI FHIR storage, exposing HTTP `POST`, `PUT`, and `DELETE` endpoints and enabling resource resurrection.

To maximize overall system welfare, clinical trust, and regulatory defensibility while minimizing operational friction and code disruption, **Kleio must be established as the sole authoritative audit evidence boundary**.

External access must be strictly constrained to read and search operations evaluated under the `AUDIT` security domain, while all external and backend mutation capabilities are unconditionally eliminated.

---

**2. BEFE Read/Search: Kleio Delegation vs. Generic FHIR Infrastructure**

**Evaluation of Options**

* **Option A: Retain Generic FHIR Infrastructure (`FhirCacheService` / `auditevent-cache`)**
    * *Operational Analysis*: `FhirCacheService.searchResources` scans an in-memory/Hot Rod Infinispan cache, decodes all JSON entries in RAM, and applies client-side string filtering.
    * *Critical Defect*: Kleio writes directly to PostgreSQL (`hie_fhir_resources`) via JDBC and has zero awareness of Infinispan. If trusted subsystems append audit evidence via `AuditService`, those records will **never appear** in `auditevent-cache`.
    * *Integrity Defect*: The cache cannot perform tamper detection (e.g., detecting soft-deleted or altered database rows) and relies on volatile acknowledgements and async write-behind.
    * *Cost-Benefit*: Preserving Option A introduces split-brain state, stale audit logs, higher memory overhead, and severe audit vulnerability.

* **Option B: Delegate BEFE Read & Search to Kleio `AuditService` (Recommended)**
    * *Operational Analysis*: BEFE `AuditEventResource.read(id)` delegates to `AuditService.findById(id)` (or `get(id)`), and `AuditEventResource.search(...)` maps HTTP query parameters into `AuditQuery` and delegates to `AuditService.find(query)`. Canonical `HarmoniaAuditEvent` results are mapped back to FHIR R5 `AuditEvent` / `Bundle` using `HarmoniaAuditEventMapper`.
    * *Systemic Resilience*: Reads query the authoritative PostgreSQL store directly. Every read activates Kleio's tamper detection: encountering a soft-deleted row (`is_deleted = true`) or corrupted payload immediately throws `AuditIntegrityException`.
    * *Clean Decommissioning*: Allows `auditevent-cache` and its asynchronous `FhirRestCacheStore` write-behind pipeline to be retired completely, eliminating volatile persistence risk and cache drift.
    * *Decoupling Compliance*: Invariant 3 in `AGENTS.md` and `IrisDecouplingArchitectureTest` permit container-managed DataSource injection and JDBC in BEFE, strictly forbidding only JPA/Hibernate (`jakarta.persistence..`, `org.hibernate..`, `ca.uhn.fhir.jpa..`) and direct driver imports (`org.postgresql..`). `kleio-persistence` conforms to these rules.

* **Conclusion**: BEFE read and search **must delegate directly to Kleio `AuditService`**. This yields maximum data integrity, zero cache drift, deterministic audit verification, and minimal systemic complexity.

---

**3. Smallest Safe Mechanism for AuditEvent Immutability**

To eliminate all mutation surfaces without breaking generic mutable CRUD required for clinical resources (`Patient`, `Practitioner`, `Task`, `Encounter`), defenses are layered across four boundaries:

**Boundary 1: Iris BEFE REST Resource (`AuditEventResource.java`)**
* Completely eliminate `@POST`, `@PUT`, and `@DELETE` method endpoints (or retain them solely to return HTTP `405 Method Not Allowed` with a clear explanation).
* Only `@GET` (`read` and `search`) remains accessible.

**Boundary 2: Themis Security Classification (`ThemisClinicalAuthorizationFilter.java`)**
* In `ThemisClinicalAuthorizationFilter`, intercept `resourceType.equalsIgnoreCase("AuditEvent")`.
* Reclassify the `ThemisResource` and `ThemisSecurityContext` domain to `AUDIT` (`HarmoniaSecurityConstants.LABEL_AUDIT`) rather than `CLINICAL`.
* Requests automatically route to `AuditReadPolicy`:
    * `ThemisAction.READ` and `ThemisAction.SEARCH` require `audit.read` (`HarmoniaSecurityConstants.AUTH_AUDIT_READ`) or `system.admin` (`HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN`).
    * Any mutation verb (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) is rejected fail-closed with `ThemisDecisionReason.ACTION_NOT_PERMITTED` or `DENY`.

**Boundary 3: Mnemosyne HAPI Provider (`AuditEventResourceProvider.java`)**
* Remove `@Create`, `@Update`, and `@Delete` annotations and methods from `AuditEventResourceProvider` (or configure them to throw `ForbiddenOperationException("AuditEvent records are append-only immutable evidence")`).
* HAPI FHIR automatically rejects unsupported verbs. Only `@Read` and `@Search` remain if direct provider access is queried internally.

**Boundary 4: Mnemosyne Core Storage Engine (`FhirStorageService.java`)**
* Do **not** modify generic storage logic for clinical resources.
* Add an explicit fail-closed guard at the top of `FhirStorageService`:
    * In `createResource(IBaseResource resource)`: If `resource.fhirType().equalsIgnoreCase("AuditEvent")`, throw `ForbiddenOperationException` to prevent the resurrection bypass (`entity.setDeleted(false)` on existing entities).
    * In `updateResource(String id, IBaseResource resource)`: If `resource.fhirType().equalsIgnoreCase("AuditEvent")`, throw `ForbiddenOperationException`.
    * In `deleteResource(String resourceType, String id)`: If `resourceType.equalsIgnoreCase("AuditEvent")`, throw `ForbiddenOperationException`.

**Boundary 5: Cache & Write-Behind Configuration (`infinispan.xml`)**
* Remove the `auditevent-cache` configuration and its `FhirRestCacheStore` write-behind definition from `infinispan.xml`, closing the background HTTP PUT/DELETE mutation route.

---

**4. Verified Findings vs. Inferred Assumptions**

To ensure accurate engineering decisions, previous observations are categorized below:

**Verified Facts (`VERIFIED`)**
1. `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository` are fully implemented in `kleio-persistence` with complete tamper detection and append-only semantics (`VERIFIED`).
2. `iris/iris-befe/pom.xml` currently has zero dependencies on `kleio-core`, `kleio-fhir`, or `kleio-persistence` (`VERIFIED`).
3. There is no CDI producer in the repository for `@KleioAudit DataSource` (`VERIFIED`).
4. `ThemisClinicalAuthorizationFilter` hardcodes `securityDomain = "CLINICAL"`, completely preventing `AuditReadPolicy` from evaluating `AuditEvent` requests (`VERIFIED`).
5. `AuditEventResource` in BEFE exposes `@POST`, `@PUT`, and `@DELETE` methods delegating to `FhirCacheService` (`VERIFIED`).
6. `AuditEventResourceProvider` in Mnemosyne exposes `@Create`, `@Update`, and `@Delete` methods delegating to `FhirStorageService` (`VERIFIED`).
7. `FhirStorageService.createResource` and `updateResource` explicitly execute `entity.setDeleted(false)`, resurrecting soft-deleted rows in `hie_fhir_resources` (`VERIFIED`).
8. `AuditEventView.vue` contains active "Emit Audit Event" (modal) and per-row "Delete" (trash icon) controls (`VERIFIED`).
9. `InMemoryAuditService` is an unannotated POJO without CDI annotations and is used only in test code (`VERIFIED`).

**Inferred Assumptions (`INFERRED`)**
1. *WildFly JNDI Configuration (`INFERRED`)*:
    * It was assumed WildFly exposes a pre-configured JNDI DataSource (`java:jboss/datasources/HarmoniaDS` or `java:comp/DefaultDataSource`).
    * *Reality*: Neither `standalone.xml` nor BEFE code declares a JNDI DataSource. Mnemosyne uses Spring Boot properties (`SPRING_DATASOURCE_URL`), and BEFE currently uses environment variables (`INFINISPAN_HOST`).
    * *Grounding*: `KleioDataSourceProducer` must support JNDI lookup with a robust fallback to standard environment variables (`AUDIT_DB_URL`, `AUDIT_DB_USER`, `AUDIT_DB_PASSWORD`) or default connection properties, ensuring portability across Docker, Kubernetes, and local test environments.
2. *Query Parameter Parity (`INFERRED`)*:
    * It was assumed `AuditQuery` directly matches the query parameters of `AuditEventResource.search()` (`_id`, `name`, `identifier`).
    * *Reality*: `AuditQuery` supports structured fields: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and `limit`. In `FhirCacheService`, `name` was used as a fuzzy match against action codes and agent display names.
    * *Grounding*: When `AuditEventResource.search` delegates to `AuditQuery`, it should map `_id` to `eventId`, parse `name` into `action` (if matching an `AuditAction` enum) or `principalId`, or map accordingly.
3. *User Role Assignment in Development (`INFERRED`)*:
    * It was assumed current UI test users hold `AUD_RDR` or `SYS_ADM`.
    * *Reality*: If test tokens only possess `CLINICAL_WRITE`, switching the domain to `AUDIT` will cause `AuditReadPolicy` to return HTTP `403 Forbidden` unless the user's role mapping includes `audit.read`.
    * *Grounding*: Verification must ensure test tokens or mock contexts provide `audit.read` or `system.admin`.

---

**5. Recommended Step 04 Implementation Blocks**

To allow incremental verification and maintain clean review boundaries, Step 04 is partitioned into six cohesive blocks:

```
Step 04.1 (Current: Exploration & Synthesis)
     │
     ▼
Step 04.2 (Runtime Kleio Composition & Container Wiring in BEFE)
     │
     ├────────────────────────────────────────┐
     ▼                                        ▼
Step 04.3 (Themis AUDIT Domain Filter)   Step 04.5 (Mnemosyne Immutability Guards)
     │                                        │
     ▼                                        │
Step 04.4 (BEFE AuditEvent Convergence &      │
           Cache Write-Behind Retirement)     │
     │                                        │
     ├────────────────────────────────────────┘
     ▼
Step 04.6 (Iris UI Immutability Convergence)
     │
     ▼
Step 04.7 (Architecture Guardrails & Verification)
```

**Block 04.1: Explore Convergence and Bypass Paths *(COMPLETED)***
* Comprehensive repository inventory, bypass trace, and architectural synthesis.

**Block 04.2: Runtime Kleio Composition & Container Wiring in BEFE**
* **Scope**:
    * Add `kleio-core`, `kleio-fhir`, and `kleio-persistence` dependencies to `iris/iris-befe/pom.xml`.
    * Implement `KleioDataSourceProducer.java` producing `@Produces @KleioAudit DataSource` with support for container JNDI and JDBC environment variable fallback.
    * Verify CDI container bootstrapping and injection of `AuditService` (resolving unambiguously to `DurableAuditService`).
* **Dependencies**: Step 03.3.

**Block 04.3: Themis Security Domain & Authorization Convergence**
* **Scope**:
    * Update `ThemisClinicalAuthorizationFilter.java` to classify `/api/fhir/AuditEvent` under `HarmoniaSecurityConstants.LABEL_AUDIT` (`"AUDIT"`).
    * Direct requests to `AuditReadPolicy`, enforcing `audit.read` or `system.admin`.
    * Ensure all mutation actions fail closed with `DENY`.
* **Dependencies**: Block 04.1.

**Block 04.4: Converge External BEFE `AuditEventResource` & Retire Audit Write-Behind**
* **Scope**:
    * In `AuditEventResource.java`: Remove or reject with HTTP 405 `create()`, `update()`, and `delete()`.
    * Refactor `read()` and `search()` in `AuditEventResource` to delegate to `AuditService` (`findById` and `find(AuditQuery)`) and serialize results via `HarmoniaAuditEventMapper`.
    * Remove `auditevent-cache` from `hestia/mneme-cluster/src/main/resources/infinispan.xml`.
* **Dependencies**: Block 04.2, Block 04.3.

**Block 04.5: Disable Direct Mnemosyne Mutation & Close Resurrection Bypass**
* **Scope**:
    * In `AuditEventResourceProvider.java`: Remove `@Create`, `@Update`, and `@Delete` annotations and methods.
    * In `FhirStorageService.java`: Add explicit fail-closed immutability guards in `createResource`, `updateResource`, and `deleteResource` for `AuditEvent`.
* **Dependencies**: Block 04.1 (can run in parallel with Block 04.3/04.4).

**Block 04.6: Iris UI Immutability Convergence**
* **Scope**:
    * In `iris-clinical/src/views/AuditEventView.vue`: Remove the "Emit Audit Event" button, form modal, and table-row trash button. Retain search, table display, severity badges, and JSON inspect modal.
    * In `iris-clinical/src/stores/securityStore.ts`: Remove `createAuditEvent()` and `deleteAuditEvent()`.
* **Dependencies**: Block 04.4.

**Block 04.7: Architecture Guardrails & Acceptance Verification**
* **Scope**:
    * Implement `AuditEventImmutabilityArchitectureTest` in `paradeigma/paradeigma-test`:
        * Assert zero `@POST`, `@PUT`, `@PATCH`, `@DELETE` methods in `AuditEventResource`.
        * Assert zero `@Create`, `@Update`, `@Delete`, `@Patch` methods in `AuditEventResourceProvider`.
        * Assert zero production dependencies on `InMemoryAuditService`.
        * Assert no `auditevent-cache` write-behind definition in `infinispan.xml`.
    * Execute integration tests proving end-to-end append, durable point-read, search, and tamper detection rejection.
* **Dependencies**: Blocks 04.2–04.6.

---

**6. Next Implementation Block (Step 04.2) Target Files & Scope**

The next execution block is **Step 04.2: Runtime Kleio Composition & Container Wiring in BEFE**.

**Files to Modify or Create:**
1. `iris/iris-befe/pom.xml`:
    * Add dependencies:
        * `net.fhirfactory.harmonia:kleio-core`
        * `net.fhirfactory.harmonia:kleio-fhir`
        * `net.fhirfactory.harmonia:kleio-persistence`
    * Ensure runtime driver exclusions or scoping do not violate `IrisDecouplingArchitectureTest`.
2. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/KleioDataSourceProducer.java` *(New Class)*:
    * CDI producer providing `@Produces @KleioAudit @ApplicationScoped DataSource`.
    * Implements primary lookup via container JNDI (e.g., `java:comp/DefaultDataSource` or configured property) with environment variable fallback (`AUDIT_DB_URL`, `AUDIT_DB_USER`, `AUDIT_DB_PASSWORD`) using a lightweight container connection pool.
3. `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/KleioCdiWiringTest.java` *(New Test Class)*:
    * Verifies that CDI initializes `AuditService` injected as `DurableAuditService` without ambiguity.
    * Asserts that `HarmoniaAuditEventMapper` and `FhirContext` resolve correctly within the BEFE context.

---

**Discoveries for Future Steps**
- **Invariant 3 Preservation**: `kleio-persistence` uses pure JDBC (`javax.sql.DataSource`) and imports zero JPA/Hibernate or PostgreSQL driver classes; adding it to `iris-befe` complies with Invariant 3.
- **Cache Invalidation Avoided**: Delegating BEFE reads directly to `AuditService` avoids cache desynchronization, as Kleio writes directly to PostgreSQL.
- **Resurrection Prevention**: Mnemosyne's `createResource` and `updateResource` resurrect soft-deleted entities; explicit guards in `FhirStorageService` are required.
- **Port Exposure Risk**: Docker Compose maps Mnemosyne directly to host ports `8081` and `8082`, making provider-level and service-level immutability guards essential.
- **Test Command**: Architecture suite can be validated via `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.

**SYNTHESIS COMPLETED — READY FOR STEP 04.2 IMPLEMENTATION.**

Analysis is Ready

**Step 04: Authoritative Audit Evidence Boundary — Architectural Synthesis & Implementation Plan**

**1. Executive Summary & Architectural Positioning**

Task 06 Step 03 established that Kleio persistence (`kleio-persistence`, `kleio-core`, `kleio-fhir`) is append-only, durable, and tamper-detecting. However, repository exploration confirms that Kleio remains completely unwired from the live runtime. Concurrently, a legacy mutable path spans the Iris UI, Iris BEFE, Infinispan cache with write-behind, and Mnemosyne HAPI FHIR storage, exposing HTTP `POST`, `PUT`, and `DELETE` endpoints and enabling resource resurrection.

To maximize overall system welfare, clinical trust, and regulatory defensibility while minimizing operational friction and code disruption, **Kleio must be established as the sole authoritative audit evidence boundary**.

External access must be strictly constrained to read and search operations evaluated under the `AUDIT` security domain, while all external and backend mutation capabilities are unconditionally eliminated.

---

**2. BEFE Read/Search: Kleio Delegation vs. Generic FHIR Infrastructure**

**Evaluation of Options**

* **Option A: Retain Generic FHIR Infrastructure (`FhirCacheService` / `auditevent-cache`)**
    * *Operational Analysis*: `FhirCacheService.searchResources` scans an in-memory/Hot Rod Infinispan cache, decodes all JSON entries in RAM, and applies client-side string filtering.
    * *Critical Defect*: Kleio writes directly to PostgreSQL (`hie_fhir_resources`) via JDBC and has zero awareness of Infinispan. If trusted subsystems append audit evidence via `AuditService`, those records will **never appear** in `auditevent-cache`.
    * *Integrity Defect*: The cache cannot perform tamper detection (e.g., detecting soft-deleted or altered database rows) and relies on volatile acknowledgements and async write-behind.
    * *Cost-Benefit*: Preserving Option A introduces split-brain state, stale audit logs, higher memory overhead, and severe audit vulnerability.

* **Option B: Delegate BEFE Read & Search to Kleio `AuditService` (Recommended)**
    * *Operational Analysis*: BEFE `AuditEventResource.read(id)` delegates to `AuditService.findById(id)` (or `get(id)`), and `AuditEventResource.search(...)` maps HTTP query parameters into `AuditQuery` and delegates to `AuditService.find(query)`. Canonical `HarmoniaAuditEvent` results are mapped back to FHIR R5 `AuditEvent` / `Bundle` using `HarmoniaAuditEventMapper`.
    * *Systemic Resilience*: Reads query the authoritative PostgreSQL store directly. Every read activates Kleio's tamper detection: encountering a soft-deleted row (`is_deleted = true`) or corrupted payload immediately throws `AuditIntegrityException`.
    * *Clean Decommissioning*: Allows `auditevent-cache` and its asynchronous `FhirRestCacheStore` write-behind pipeline to be retired completely, eliminating volatile persistence risk and cache drift.
    * *Decoupling Compliance*: Invariant 3 in `AGENTS.md` and `IrisDecouplingArchitectureTest` permit container-managed DataSource injection and JDBC in BEFE, strictly forbidding only JPA/Hibernate (`jakarta.persistence..`, `org.hibernate..`, `ca.uhn.fhir.jpa..`) and direct driver imports (`org.postgresql..`). `kleio-persistence` conforms to these rules.

* **Conclusion**: BEFE read and search **must delegate directly to Kleio `AuditService`**. This yields maximum data integrity, zero cache drift, deterministic audit verification, and minimal systemic complexity.

---

**3. Smallest Safe Mechanism for AuditEvent Immutability**

To eliminate all mutation surfaces without breaking generic mutable CRUD required for clinical resources (`Patient`, `Practitioner`, `Task`, `Encounter`), defenses are layered across four boundaries:

**Boundary 1: Iris BEFE REST Resource (`AuditEventResource.java`)**
* Completely eliminate `@POST`, `@PUT`, and `@DELETE` method endpoints (or retain them solely to return HTTP `405 Method Not Allowed` with a clear explanation).
* Only `@GET` (`read` and `search`) remains accessible.

**Boundary 2: Themis Security Classification (`ThemisClinicalAuthorizationFilter.java`)**
* In `ThemisClinicalAuthorizationFilter`, intercept `resourceType.equalsIgnoreCase("AuditEvent")`.
* Reclassify the `ThemisResource` and `ThemisSecurityContext` domain to `AUDIT` (`HarmoniaSecurityConstants.LABEL_AUDIT`) rather than `CLINICAL`.
* Requests automatically route to `AuditReadPolicy`:
    * `ThemisAction.READ` and `ThemisAction.SEARCH` require `audit.read` (`HarmoniaSecurityConstants.AUTH_AUDIT_READ`) or `system.admin` (`HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN`).
    * Any mutation verb (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) is rejected fail-closed with `ThemisDecisionReason.ACTION_NOT_PERMITTED` or `DENY`.

**Boundary 3: Mnemosyne HAPI Provider (`AuditEventResourceProvider.java`)**
* Remove `@Create`, `@Update`, and `@Delete` annotations and methods from `AuditEventResourceProvider` (or configure them to throw `ForbiddenOperationException("AuditEvent records are append-only immutable evidence")`).
* HAPI FHIR automatically rejects unsupported verbs. Only `@Read` and `@Search` remain if direct provider access is queried internally.

**Boundary 4: Mnemosyne Core Storage Engine (`FhirStorageService.java`)**
* Do **not** modify generic storage logic for clinical resources.
* Add an explicit fail-closed guard at the top of `FhirStorageService`:
    * In `createResource(IBaseResource resource)`: If `resource.fhirType().equalsIgnoreCase("AuditEvent")`, throw `ForbiddenOperationException` to prevent the resurrection bypass (`entity.setDeleted(false)` on existing entities).
    * In `updateResource(String id, IBaseResource resource)`: If `resource.fhirType().equalsIgnoreCase("AuditEvent")`, throw `ForbiddenOperationException`.
    * In `deleteResource(String resourceType, String id)`: If `resourceType.equalsIgnoreCase("AuditEvent")`, throw `ForbiddenOperationException`.

**Boundary 5: Cache & Write-Behind Configuration (`infinispan.xml`)**
* Remove the `auditevent-cache` configuration and its `FhirRestCacheStore` write-behind definition from `infinispan.xml`, closing the background HTTP PUT/DELETE mutation route.

---

**4. Verified Findings vs. Inferred Assumptions**

To ensure accurate engineering decisions, previous observations are categorized below:

**Verified Facts (`VERIFIED`)**
1. `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository` are fully implemented in `kleio-persistence` with complete tamper detection and append-only semantics (`VERIFIED`).
2. `iris/iris-befe/pom.xml` currently has zero dependencies on `kleio-core`, `kleio-fhir`, or `kleio-persistence` (`VERIFIED`).
3. There is no CDI producer in the repository for `@KleioAudit DataSource` (`VERIFIED`).
4. `ThemisClinicalAuthorizationFilter` hardcodes `securityDomain = "CLINICAL"`, completely preventing `AuditReadPolicy` from evaluating `AuditEvent` requests (`VERIFIED`).
5. `AuditEventResource` in BEFE exposes `@POST`, `@PUT`, and `@DELETE` methods delegating to `FhirCacheService` (`VERIFIED`).
6. `AuditEventResourceProvider` in Mnemosyne exposes `@Create`, `@Update`, and `@Delete` methods delegating to `FhirStorageService` (`VERIFIED`).
7. `FhirStorageService.createResource` and `updateResource` explicitly execute `entity.setDeleted(false)`, resurrecting soft-deleted rows in `hie_fhir_resources` (`VERIFIED`).
8. `AuditEventView.vue` contains active "Emit Audit Event" (modal) and per-row "Delete" (trash icon) controls (`VERIFIED`).
9. `InMemoryAuditService` is an unannotated POJO without CDI annotations and is used only in test code (`VERIFIED`).

**Inferred Assumptions (`INFERRED`)**
1. *WildFly JNDI Configuration (`INFERRED`)*:
    * It was assumed WildFly exposes a pre-configured JNDI DataSource (`java:jboss/datasources/HarmoniaDS` or `java:comp/DefaultDataSource`).
    * *Reality*: Neither `standalone.xml` nor BEFE code declares a JNDI DataSource. Mnemosyne uses Spring Boot properties (`SPRING_DATASOURCE_URL`), and BEFE currently uses environment variables (`INFINISPAN_HOST`).
    * *Grounding*: `KleioDataSourceProducer` must support JNDI lookup with a robust fallback to standard environment variables (`AUDIT_DB_URL`, `AUDIT_DB_USER`, `AUDIT_DB_PASSWORD`) or default connection properties, ensuring portability across Docker, Kubernetes, and local test environments.
2. *Query Parameter Parity (`INFERRED`)*:
    * It was assumed `AuditQuery` directly matches the query parameters of `AuditEventResource.search()` (`_id`, `name`, `identifier`).
    * *Reality*: `AuditQuery` supports structured fields: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and `limit`. In `FhirCacheService`, `name` was used as a fuzzy match against action codes and agent display names.
    * *Grounding*: When `AuditEventResource.search` delegates to `AuditQuery`, it should map `_id` to `eventId`, parse `name` into `action` (if matching an `AuditAction` enum) or `principalId`, or map accordingly.
3. *User Role Assignment in Development (`INFERRED`)*:
    * It was assumed current UI test users hold `AUD_RDR` or `SYS_ADM`.
    * *Reality*: If test tokens only possess `CLINICAL_WRITE`, switching the domain to `AUDIT` will cause `AuditReadPolicy` to return HTTP `403 Forbidden` unless the user's role mapping includes `audit.read`.
    * *Grounding*: Verification must ensure test tokens or mock contexts provide `audit.read` or `system.admin`.

---

**5. Recommended Step 04 Implementation Blocks**

To allow incremental verification and maintain clean review boundaries, Step 04 is partitioned into six cohesive blocks:

```
Step 04.1 (Current: Exploration & Synthesis)
     │
     ▼
Step 04.2 (Runtime Kleio Composition & Container Wiring in BEFE)
     │
     ├────────────────────────────────────────┐
     ▼                                        ▼
Step 04.3 (Themis AUDIT Domain Filter)   Step 04.5 (Mnemosyne Immutability Guards)
     │                                        │
     ▼                                        │
Step 04.4 (BEFE AuditEvent Convergence &      │
           Cache Write-Behind Retirement)     │
     │                                        │
     ├────────────────────────────────────────┘
     ▼
Step 04.6 (Iris UI Immutability Convergence)
     │
     ▼
Step 04.7 (Architecture Guardrails & Verification)
```

**Block 04.1: Explore Convergence and Bypass Paths *(COMPLETED)***
* Comprehensive repository inventory, bypass trace, and architectural synthesis.

**Block 04.2: Runtime Kleio Composition & Container Wiring in BEFE**
* **Scope**:
    * Add `kleio-core`, `kleio-fhir`, and `kleio-persistence` dependencies to `iris/iris-befe/pom.xml`.
    * Implement `KleioDataSourceProducer.java` producing `@Produces @KleioAudit DataSource` with support for container JNDI and JDBC environment variable fallback.
    * Verify CDI container bootstrapping and injection of `AuditService` (resolving unambiguously to `DurableAuditService`).
* **Dependencies**: Step 03.3.

**Block 04.3: Themis Security Domain & Authorization Convergence**
* **Scope**:
    * Update `ThemisClinicalAuthorizationFilter.java` to classify `/api/fhir/AuditEvent` under `HarmoniaSecurityConstants.LABEL_AUDIT` (`"AUDIT"`).
    * Direct requests to `AuditReadPolicy`, enforcing `audit.read` or `system.admin`.
    * Ensure all mutation actions fail closed with `DENY`.
* **Dependencies**: Block 04.1.

**Block 04.4: Converge External BEFE `AuditEventResource` & Retire Audit Write-Behind**
* **Scope**:
    * In `AuditEventResource.java`: Remove or reject with HTTP 405 `create()`, `update()`, and `delete()`.
    * Refactor `read()` and `search()` in `AuditEventResource` to delegate to `AuditService` (`findById` and `find(AuditQuery)`) and serialize results via `HarmoniaAuditEventMapper`.
    * Remove `auditevent-cache` from `hestia/mneme-cluster/src/main/resources/infinispan.xml`.
* **Dependencies**: Block 04.2, Block 04.3.

**Block 04.5: Disable Direct Mnemosyne Mutation & Close Resurrection Bypass**
* **Scope**:
    * In `AuditEventResourceProvider.java`: Remove `@Create`, `@Update`, and `@Delete` annotations and methods.
    * In `FhirStorageService.java`: Add explicit fail-closed immutability guards in `createResource`, `updateResource`, and `deleteResource` for `AuditEvent`.
* **Dependencies**: Block 04.1 (can run in parallel with Block 04.3/04.4).

**Block 04.6: Iris UI Immutability Convergence**
* **Scope**:
    * In `iris-clinical/src/views/AuditEventView.vue`: Remove the "Emit Audit Event" button, form modal, and table-row trash button. Retain search, table display, severity badges, and JSON inspect modal.
    * In `iris-clinical/src/stores/securityStore.ts`: Remove `createAuditEvent()` and `deleteAuditEvent()`.
* **Dependencies**: Block 04.4.

**Block 04.7: Architecture Guardrails & Acceptance Verification**
* **Scope**:
    * Implement `AuditEventImmutabilityArchitectureTest` in `paradeigma/paradeigma-test`:
        * Assert zero `@POST`, `@PUT`, `@PATCH`, `@DELETE` methods in `AuditEventResource`.
        * Assert zero `@Create`, `@Update`, `@Delete`, `@Patch` methods in `AuditEventResourceProvider`.
        * Assert zero production dependencies on `InMemoryAuditService`.
        * Assert no `auditevent-cache` write-behind definition in `infinispan.xml`.
    * Execute integration tests proving end-to-end append, durable point-read, search, and tamper detection rejection.
* **Dependencies**: Blocks 04.2–04.6.

---

**6. Next Implementation Block (Step 04.2) Target Files & Scope**

The next execution block is **Step 04.2: Runtime Kleio Composition & Container Wiring in BEFE**.

**Files to Modify or Create:**
1. `iris/iris-befe/pom.xml`:
    * Add dependencies:
        * `net.fhirfactory.harmonia:kleio-core`
        * `net.fhirfactory.harmonia:kleio-fhir`
        * `net.fhirfactory.harmonia:kleio-persistence`
    * Ensure runtime driver exclusions or scoping do not violate `IrisDecouplingArchitectureTest`.
2. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/KleioDataSourceProducer.java` *(New Class)*:
    * CDI producer providing `@Produces @KleioAudit @ApplicationScoped DataSource`.
    * Implements primary lookup via container JNDI (e.g., `java:comp/DefaultDataSource` or configured property) with environment variable fallback (`AUDIT_DB_URL`, `AUDIT_DB_USER`, `AUDIT_DB_PASSWORD`) using a lightweight container connection pool.
3. `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/KleioCdiWiringTest.java` *(New Test Class)*:
    * Verifies that CDI initializes `AuditService` injected as `DurableAuditService` without ambiguity.
    * Asserts that `HarmoniaAuditEventMapper` and `FhirContext` resolve correctly within the BEFE context.

---

**Discoveries for Future Steps**
- **Invariant 3 Preservation**: `kleio-persistence` uses pure JDBC (`javax.sql.DataSource`) and imports zero JPA/Hibernate or PostgreSQL driver classes; adding it to `iris-befe` complies with Invariant 3.
- **Cache Invalidation Avoided**: Delegating BEFE reads directly to `AuditService` avoids cache desynchronization, as Kleio writes directly to PostgreSQL.
- **Resurrection Prevention**: Mnemosyne's `createResource` and `updateResource` resurrect soft-deleted entities; explicit guards in `FhirStorageService` are required.
- **Port Exposure Risk**: Docker Compose maps Mnemosyne directly to host ports `8081` and `8082`, making provider-level and service-level immutability guards essential.
- **Test Command**: Architecture suite can be validated via `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.

**SYNTHESIS COMPLETED — READY FOR STEP 04.2 IMPLEMENTATION.**



**Summary**
- Produced the Step 04.1 exploration report for audit convergence and immutability bypass paths without modifying production code.
- Verified Kleio is append-only and tamper-detecting, but not yet wired into runtime; legacy mutable AuditEvent paths remain active through BEFE, cache/write-behind, Mnemosyne, and UI surfaces.

**Changes**
- Mapped current Kleio baseline, runtime composition gaps, and `@KleioAudit` wiring status.
- Inventoried BEFE, Mnemosyne, UI, cache, security, and direct database mutation paths for `AuditEvent`, including whether each path can create, update, delete, resurrect, or bypass Kleio.
- Confirmed `/api/fhir/AuditEvent` is currently classified as `CLINICAL`, so `AuditReadPolicy` is bypassed and `clinical.*` authorities govern audit reads/searches and some mutations.
- Recommended a smallest converged Step 04 plan: wire Kleio into BEFE, route read/search through `AuditService`, deny/remove external mutation surfaces, disable Mnemosyne AuditEvent mutation, retire AuditEvent write-behind/cache mutation, and add architecture guardrails.

**Verification**
- Validated findings through repository exploration of BEFE REST resources, Themis policies/filtering, Infinispan/write-behind config, Mnemosyne HAPI providers/storage, Iris UI code, Docker exposure, and existing ArchUnit tests.

**Notes**
- No implementation was started, consistent with the Step 04.1 exploration-only scope.

