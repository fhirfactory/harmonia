Optional spending limit; leave empty for no limit: 37
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect the current Step 04.4 change surface in the repository before planning. Focus on the current Iris BEFE AuditEvent implementation and the exact smallest code/test areas likely required for this step.

Please map, with concise findings and approximate line numbers:
- the exact `AuditEventResource.java` path and its current injected dependencies;
- GET by id, SEARCH, POST, PUT, DELETE methods and current response / error handling conventions;
- any current `FhirCacheService` usage and AuditEvent-specific cache coupling in Iris BEFE;
- existing tests for `AuditEventResource` and closely related BEFE FHIR resource tests that show response/OperationOutcome conventions;
- the existing `AuditService`, `AuditQuery`, and `HarmoniaAuditEventMapper` signatures and likely call patterns for read/search;
- likely integrity/not-found exception types or result contracts from the Kleio audit path;
- any existing architecture tests most relevant to this step.

Highlight architectural ambiguity or blocker only if Step 04.4 would require changing the frozen Task 06 design.



Exploration is Ready

Read-only Step 04.4 change-surface findings (no files modified).

**1. Current BEFE resource**

**Path:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`

- `@Path("/fhir/AuditEvent")`, `@RequestScoped`, FHIR JSON + generic JSON media types: lines 33–37.
- Only injected dependency is `FhirCacheService cacheService`: lines 39–40.
- It imports HAPI `IParser`, `AuditEvent`, `Bundle`, `StringUtils`, and `java.net.URI`: lines 20–31.

Current methods:

- **SEARCH** `GET /fhir/AuditEvent`: lines 42–50. Parameters are `_id`, `name`, and `identifier` (lines 44–46). It calls `cacheService.searchAsBundle("AuditEvent", id, name, identifier)`, serializes through `cacheService.getJsonParser()`, and returns HTTP 200 with a serialized FHIR `Bundle`.
- **READ** `GET /fhir/AuditEvent/{id}`: lines 52–62. It calls `cacheService.getResourceJson("AuditEvent", id)`. A null result returns HTTP 404 with a hand-built OperationOutcome JSON whose diagnostic is `AuditEvent/{id} not found` (lines 56–59); otherwise it returns HTTP 200 with the cached JSON.
- **CREATE** `POST /fhir/AuditEvent`: lines 64–76. Blank payload returns HTTP 400 with no entity (lines 66–68); otherwise it parses with the cache service's HAPI parser, calls `saveResource`, and returns HTTP 201 plus a Location `/api/fhir/AuditEvent/{id}` and serialized resource (lines 69–75).
- **UPDATE** `PUT /fhir/AuditEvent/{id}`: lines 78–90. Blank payload returns 400; payload is parsed, forced to `AuditEvent/{id}`, saved through `FhirCacheService`, and returned as HTTP 200.
- **DELETE** `DELETE /fhir/AuditEvent/{id}`: lines 92–100. It calls `cacheService.deleteResource`; success is 204, false is a bare 404.
- **PATCH:** no resource method currently exists. The Themis filter maps PATCH to UPDATE, so the security boundary can deny it independently of resource method exposure.

There is no local exception handling around HAPI parse/serialization or cache failures; those exceptions are not converted by this resource to OperationOutcome responses.

**2. Existing BEFE conventions / tests**

The closest resource convention is `CommunicationResource.java` (same structure and line layout, especially lines 42–100); other generic resources such as `TaskResource.java` use the same cache-backed CRUD/search pattern. The generic not-found OperationOutcome is hand-built in each resource (for example `CommunicationResource.java:54–62` and `TaskResource.java:59–69`).

There is no dedicated `AuditEventResourceTest` under `iris/iris-befe/src/test`. The direct current test is:

- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java:155–205`, where `testNewResourceControllers` injects `FhirCacheService`, creates an AuditEvent through `auditController.create(...)` at lines 166–171, and reads it back at lines 171–172. This test must be changed because it encodes the legacy external CREATE/cache behavior.
- The same test demonstrates the generic response assertions: status 201, Location-derived ID, status 200, and serialized entity checks (for example lines 62–94).
- `FhirCacheServiceTest.java` is generic cache/security-tag coverage (for example lines 43–124), not AuditEvent-specific.
- `ThemisClinicalAuthorizationFilterTest.java` covers action mapping including PATCH→UPDATE at lines 449–452 and broad security regression cases; it should be preserved rather than duplicated in the resource.

The Iris BEFE test source tree currently contains no AuditEvent-named test file; the relevant existing classes are the integration test, authorization-filter test, runtime-composition test, and generic cache-service test.

**3. Current cache coupling and remaining cache artifacts**

`AuditEventResource` is directly coupled to the generic cache at lines 27, 39–40, 47–48, 55, 69–72, 84–87, and 95. `FhirCacheService` itself still has AuditEvent-specific cache behavior:

- `resolveCacheName("AuditEvent") -> "auditevent-cache"`: `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java:83–102`, specifically line 96.
- Its generic search implementation has an AuditEvent-specific `name` branch matching action/agent display: lines 250–285, specifically 271–273; identifier matching has no AuditEvent branch and therefore cannot match AuditEvent identifiers in the current implementation: lines 288–319.
- JSON parser construction is generic HAPI R5 with pretty-printing at lines 72–81.

Global references to the cache remain outside the resource and are out of scope for this BEFE-only step:

- `hestia/mneme-cluster/src/main/resources/infinispan.xml:194–196` defines the shared `auditevent-cache`.
- `hestia/mneme-persistence/src/test/.../FhirRestCacheStoreTest.java:149` maps that cache to AuditEvent.
- `iris/iris-befe/src/main/java/.../FhirCacheService.java:96` maps the resource type to the cache.
- `iris/iris-befe/src/main/java/.../OperationsAggregatorService.java:54` declares `CACHE_AUDIT_EVENT = "auditevent-cache"`.

The `iris-befe` POM already declares `kleio-persistence` at `iris/iris-befe/pom.xml:56–60`, plus HAPI R5 at lines 94–102. It has no PostgreSQL dependency declaration. A BEFE composition-root `AuditDataSourceProducer` exists at `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/AuditDataSourceProducer.java:18–57`; it binds the existing WildFly `KleioAuditDS` to `@KleioAudit` and is not injected by `AuditEventResource`. It should not be broadened or replaced for this step.

**4. Canonical Kleio service/query/mapper signatures**

**`AuditService`**

`kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditService.java`:

- `HarmoniaAuditEvent append(HarmoniaAuditEvent event)`: lines 34–41 — trusted producer capability, not an external BEFE operation.
- `Optional<HarmoniaAuditEvent> findById(String eventId)`: lines 43–49.
- Default `get(String eventId)` delegates to `findById`: lines 51–59.
- `List<HarmoniaAuditEvent> find(AuditQuery query)`: lines 61–68; contract documents canonical `recordedAt DESC, eventId DESC` ordering and query limit.
- Other convenience reads (`findByCorrelationId`, `findByPrincipal`, `getRecentEvents`) are lines 70–92 but are not needed unless a FHIR parameter is mapped to them.

Runtime composition is already tested in `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/KleioRuntimeCompositionTest.java`: CDI resolves `AuditService` to `DurableAuditService` at lines 134–170, with `HarmoniaAuditEventMapper` also resolvable at lines 157–160. The test exercises point read through `findById` at lines 172–216.

**`AuditQuery`**

`kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/AuditQuery.java` is an immutable record at lines 29–41 with fields:

`eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and `limit`.

The builder is at lines 131–212. Limits normalize to default 50 and maximum 1000 at lines 43–53. Matching uses canonical `HarmoniaAuditEvent.recordedAt` for time filters and canonical event fields at lines 80–124.

For the current external parameter set, only:

- `_id` can be faithfully mapped: `_id -> AuditQuery.builder().eventId(id).build()`.
- `name` has no corresponding canonical `AuditQuery` field and the legacy cache meaning (AuditEvent action/agent display substring) is not faithfully representable.
- `identifier` has no corresponding canonical `AuditQuery` field or a defined FHIR AuditEvent identifier mapping in `AuditQuery`; the legacy cache implementation does not actually match it for AuditEvent.

Therefore `name` and `identifier` should be treated as explicitly unsupported/fail-closed rather than approximated. No FHIR time/action/outcome/etc. query parameters are currently declared by this resource, but the query contract could represent canonical fields if a future API deliberately adds exact mappings.

**`HarmoniaAuditEventMapper`**

`kleio/kleio-fhir/src/main/java/net/fhirfactory/harmonia/kleio/fhir/mapper/HarmoniaAuditEventMapper.java`:

- Class declaration/stateless mapper: lines 52–64.
- `AuditEvent toFhir(HarmoniaAuditEvent event)`: lines 66–75; null/invalid canonical data raises `HarmoniaAuditMappingException`.
- It preserves the canonical `eventId` exactly as FHIR `AuditEvent.id`: lines 78–84, specifically `auditEvent.setId(event.eventId())`.

The likely adapter flow is therefore `auditService.findById(id)` → `Optional.orElse` not-found handling → `mapper.toFhir(event)` → a BEFE-owned HAPI R5 JSON parser/`FhirContext` serialization. Search is `auditService.find(AuditQuery)` → map each canonical event with `mapper.toFhir` → FHIR search-set Bundle → serialize. The mapper itself is stateless and currently has a public no-arg constructor; runtime CDI registration is already demonstrated by `KleioRuntimeCompositionTest`.

**5. Integrity and not-found contracts**

`DurableAuditService` (`kleio/kleio-persistence/src/main/java/net/fhirfactory/harmonia/kleio/persistence/service/DurableAuditService.java`) provides the authoritative behavior:

- `findById` returns `Optional.empty()` for null/blank IDs or missing rows: lines 167–176.
- Soft-deleted point-read rows throw `AuditIntegrityException`: lines 178–184.
- Malformed persisted JSON or failed canonical mapping also throws `AuditIntegrityException`: lines 186–193.
- `find(AuditQuery)` requires a non-null query, scans through the repository, throws `AuditIntegrityException` on soft-deleted or malformed candidates at lines 202–238, and throws `AuditPersistenceException` when the scan ceiling is exceeded at lines 250–255.
- It sorts the final result canonically using `recordedAt DESC` then `eventId DESC` at lines 61–63 and 258–259.

Exception types:

- `AuditIntegrityException` is an unchecked `IllegalStateException`, with optional event ID, at `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditIntegrityException.java:20–65`.
- `AuditPersistenceException` is an unchecked `RuntimeException` for persistence/connectivity/scan-ceiling failures, at `kleio/kleio-persistence/src/main/java/net/fhirfactory/harmonia/kleio/persistence/exception/AuditPersistenceException.java:20–33`.
- `HarmoniaAuditMappingException` is an unchecked `IllegalArgumentException` at `kleio/kleio-fhir/src/main/java/net/fhirfactory/harmonia/kleio/fhir/exception/HarmoniaAuditMappingException.java:18–28`.

The current BEFE resource has no generic exception mapper, so Step 04.4 will need a local, non-leaky mapping strategy for these failures if they are to become safe FHIR server errors. Integrity/persistence/mapping failures must not become empty Bundles or 404s. Existing authorization failures use a proper HAPI `OperationOutcome` in `ThemisClinicalAuthorizationFilter.java:326–347`, with 401/403 and sanitized diagnostics; that is the closest established error-construction convention.

**6. Authorization and architecture guardrails**

The existing security boundary is already in place and should not be duplicated in the resource:

- `ThemisClinicalAuthorizationFilter.java:127–168` parses FHIR path/action and classifies `AuditEvent` as `HarmoniaSecurityLabelEnum.AUDIT` with AUDIT security domain/label at lines 159–167.
- `AuditReadPolicy.java:26–70` applies only to AUDIT READ/SEARCH (lines 52–60) and accepts `audit.read` or `system.admin` at lines 63–68.
- `SecurityEnforcementArchitectureTest.java:901–945` verifies immutable deny-policy registration, `/fhir/AuditEvent` path, and AuditEvent→AUDIT classification. The broader test also protects canonical audit model/package boundaries and safe audit string representation.
- `ThemisClinicalAuthorizationFilterTest.java` verifies PATCH action mapping (`PATCH -> UPDATE`) at lines 449–452 and broader allow/deny behavior; preserve these tests unchanged unless a focused AuditEvent case is missing.

Relevant existing architectural tests:

- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java:39–95` forbids PostgreSQL/JPA/Hibernate/JPA-server dependencies in Iris POMs and BEFE source; it does not currently assert AuditEventResource’s Kleio-vs-cache dependency boundary.
- `SecurityEnforcementArchitectureTest.java` is the relevant frozen security guardrail, especially lines 901–945.
- `PackageLayeringArchitectureTest.java:45–128` enforces broad one-way package layering, but has no focused Iris AuditEvent rule.

A small new architectural guardrail would be justified for `AuditEventResource`: assert dependency on the `AuditService` abstraction and mapper, and absence of `FhirCacheService`, Kleio persistence repository/JDBC/DataSource, and database/JPA/Hibernate types. Existing `IrisDecouplingArchitectureTest` can be extended, but it currently uses source/POM token checks rather than a targeted ArchUnit resource rule.

**7. Architectural ambiguity/blocker assessment**

No frozen Task 06 design blocker was found: the existing `AuditService`, `AuditQuery`, `HarmoniaAuditEventMapper`, CDI runtime composition, and Themis boundary support the requested reader adapter.

The only implementation decision requiring explicit fail-closed behavior is legacy SEARCH compatibility: current BEFE exposes `_id`, `name`, and `identifier`, but only `_id` maps exactly to `AuditQuery.eventId`. `name` and `identifier` cannot be faithfully represented by the frozen query contract and should not be silently approximated. Shared `auditevent-cache` configuration, `FhirRestCacheStore`, `OperationsAggregatorService`, and Mnemosyne/Hestia persistence artifacts remain outside this BEFE-only step and should be reported for later cleanup rather than modified now.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Use the repository findings already established in the attached prior work and the latest exploration. The current Step 04.4 scope is BEFE-only convergence of `AuditEventResource` onto canonical Kleio read/search, not Step 04.3 security work and not Step 04.5 Mnemosyne direct endpoints.

Prioritize the smallest clean implementation path that truthfully satisfies the issue text and the required final report. The plan should explicitly account for:
- inspecting and then refactoring `AuditEventResource` from `FhirCacheService` to CDI-injected `AuditService` plus canonical `HarmoniaAuditEventMapper`;
- preserving BEFE response and OperationOutcome conventions where appropriate;
- explicit handling of unsupported legacy search params (`name`, `identifier`) rather than silent approximation;
- safe mapping of not-found vs integrity/persistence/mapping failures;
- removing external POST/PUT/DELETE exposure from the BEFE resource and ensuring PATCH remains absent;
- identifying only clearly dead BEFE-local AuditEvent cache coupling for removal/reporting;
- focused tests for read/search/mutation-surface/architecture boundary/security regression;
- proportional architecture guardrails.

Also incorporate execution-speed guidance for narrow test commands first, and capture anything that must be reported but intentionally deferred to Step 04.5.

**Constraints**  
No git operations. Plan only Step 04.4 scope.



**Requirements**

**Goal**  
Converge the Iris BEFE FHIR `AuditEvent` REST resource (`AuditEventResource.java`) from the legacy mutable Infinispan cache path onto the canonical, append-only Kleio audit capability (`AuditService` + `HarmoniaAuditEventMapper`), exposing a thin, read-only FHIR R5 view over immutable audit evidence.

**Scope**
- **In Scope:**
    - Refactoring `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` to inject `AuditService` and `HarmoniaAuditEventMapper` via CDI.
    - Implementing `GET /fhir/AuditEvent/{id}` (READ) via `AuditService.findById(id)` with canonical FHIR R5 mapping.
    - Implementing `GET /fhir/AuditEvent` (SEARCH) via `AuditService.find(AuditQuery)`, mapping `_id` to `AuditQuery.eventId` and rejecting unsupported legacy parameters (`name`, `identifier`) fail-closed with HTTP 400 `OperationOutcome`.
    - Handling not-found (HTTP 404 `OperationOutcome`) and integrity/persistence errors (HTTP 500 `OperationOutcome`) safely with zero PHI / internal error leakage.
    - Removing external mutation methods (`POST`, `PUT`, `DELETE`) from `AuditEventResource` and verifying `PATCH` remains absent.
    - Removing `FhirCacheService` dependency and coupling from `AuditEventResource`.
    - Updating `FhirRestResourceIntegrationTest` to remove legacy mutable `AuditEvent` cache assertions.
    - Adding a dedicated `AuditEventResourceTest` covering read, search, fail-closed validation, error mapping, and mutation method absence.
    - Adding an architectural boundary test in `IrisDecouplingArchitectureTest` verifying `AuditEventResource` dependencies.
- **Out of Scope (Deferred to Step 04.5 or later):**
    - Mnemosyne direct `AuditEventResourceProvider` mutation endpoints and `FhirStorageService` (Step 04.5).
    - Shared cluster Infinispan `auditevent-cache` configuration (`infinispan.xml`, `FhirRestCacheStoreTest`, `OperationsAggregatorService`).
    - Database schema, permissions, triggers, WORM hardening, or dedicated DB relocation (Tasks 07–10).
    - Frontend UI SPAs (`AuditEventView.vue`, `securityStore.ts`).
    - Creating synthetic audit producer callers (`AuditService.append(...)` is trusted internal only).

**Done When**
- `AuditEventResource` exposes only `@GET` read and search endpoints backed exclusively by `AuditService` and `HarmoniaAuditEventMapper`.
- No `POST`, `PUT`, `DELETE`, or `PATCH` methods exist on `AuditEventResource`.
- `FhirCacheService` is no longer injected or referenced in `AuditEventResource`.
- Unsupported query parameters (`name`, `identifier`) fail closed with HTTP 400 `OperationOutcome`.
- Dedicated `AuditEventResourceTest`, updated `FhirRestResourceIntegrationTest`, `ThemisClinicalAuthorizationFilterTest`, `KleioRuntimeCompositionTest`, and `*ArchitectureTest` pass cleanly.

**Technical Design**

**Decisions**
- **Inject `AuditService` & `HarmoniaAuditEventMapper` / not persistence classes or `FhirCacheService`**: Iris BEFE is presentation-only and must depend strictly on Kleio's service and mapper abstractions, preserving the CDI runtime composition verified in `KleioRuntimeCompositionTest`.
- **Fail-closed on unsupported search parameters (`name`, `identifier`) with HTTP 400 / not silent approximation**: `AuditQuery` only supports canonical fields (`eventId`, `recordedAt`, etc.); `name` (legacy cache substring) and `identifier` (unmatched for AuditEvent) cannot be faithfully represented. Approximating would violate audit fidelity.
- **Map `AuditIntegrityException` and `AuditPersistenceException` to HTTP 500 `OperationOutcome` / not 404 or empty Bundle**: Tampered, soft-deleted, or corrupted audit records must not appear missing or empty; they must signal a server-side integrity fault with sanitized, zero-PHI diagnostics.
- **Remove `POST`, `PUT`, `DELETE` methods / not retain dead methods**: Removing mutation methods from the JAX-RS resource eliminates dead code while Themis `ThemisClinicalAuthorizationFilter` + `AuditImmutabilityDenyPolicy` continues to enforce default-deny at the security perimeter.

**Approach & Touches**
- **Primary resource:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
    - Replace `@Inject FhirCacheService cacheService` with `@Inject AuditService auditService` and `@Inject HarmoniaAuditEventMapper auditMapper` (with lazy fallback initialization for `auditMapper` and `FhirContext` / `IParser` in non-CDI test environments).
    - `read(String id)`: call `auditService.findById(id)`. If empty -> HTTP 404 `OperationOutcome`. If found -> `auditMapper.toFhir(event)` -> FHIR JSON 200. Catch integrity/persistence/mapping exceptions -> HTTP 500 `OperationOutcome`.
    - `search(String id, String name, String identifier)`: if `name` or `identifier` is non-blank -> return HTTP 400 `OperationOutcome`. Otherwise construct `AuditQuery` (with `eventId(id)` if `id` is present), call `auditService.find(query)`, map results with `auditMapper.toFhir(...)`, assemble into a FHIR `Bundle` (`type = searchset`, total count, entries with `fullUrl`), and return FHIR JSON 200. Catch exceptions -> HTTP 500 `OperationOutcome`.
    - Remove `create(...)`, `update(...)`, and `delete(...)` methods.
- **Integration test update:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
    - Remove `testNewResourceControllers` legacy AuditEvent create/read block (lines 166–173).
- **New resource test:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
    - Unit & mock tests verifying READ (found, not found, integrity error, serialization), SEARCH (default query, `_id` query, unsupported `name`/`identifier` 400, ordering preservation, bundle structure), and reflection assertions proving absence of `POST`, `PUT`, `PATCH`, `DELETE` annotations/methods.
- **Architecture guardrail:** `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
    - Add test asserting `AuditEventResource` depends only on `AuditService` / `HarmoniaAuditEventMapper` and does not import or depend on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.

**Nuances, Risks & Corners**
- **Zero-PHI Diagnostic Logging & Error Entities**: Diagnostics in `OperationOutcome` on 400, 404, or 500 must strictly contain static descriptors and IDs without echoing user inputs, query parameters, stack traces, or SQL state.
- **CDI vs Direct Instantiation in Tests**: `AuditEventResource` should allow injection of dependencies or package-private setters / constructor injection so unit tests can test it without spinning up a full EE container.
- **Deterministic Search Ordering**: `AuditService.find(...)` already guarantees canonical `recordedAt DESC, eventId DESC` ordering; `AuditEventResource` must preserve this order when assembling the `Bundle` entries without re-sorting.
- **Legacy BEFE Cache Configuration**: `FhirCacheService.resolveCacheName("auditevent")` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` remain in codebase; they are not touched in this BEFE-only step to avoid breaking shared cluster references, and will be reported for Step 04.5 cleanup.

**Contracts**
- Point Read Response:
    - Success: HTTP 200 OK with FHIR R5 `AuditEvent` JSON (`application/fhir+json`).
    - Not Found: HTTP 404 Not Found with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-found","diagnostics":"AuditEvent/<id> not found"}]}`).
    - Server / Integrity Error: HTTP 500 Internal Server Error with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"exception","diagnostics":"Internal error retrieving AuditEvent"}]}`).
- Search Response:
    - Success: HTTP 200 OK with FHIR R5 `Bundle` JSON (`resourceType: Bundle`, `type: searchset`, `total: N`, `entry: [...]`).
    - Invalid Parameter: HTTP 400 Bad Request with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-supported","diagnostics":"Search parameter not supported for AuditEvent"}]}`).

**Testing**

**Checklist of Scenarios**
- **READ - Found**: `GET /fhir/AuditEvent/{id}` returns HTTP 200 with matching canonical fields mapped to FHIR `AuditEvent`, `id` preserved, `recorded` instant intact.
- **READ - Not Found**: `GET /fhir/AuditEvent/{non-existent}` returns HTTP 404 with standard `OperationOutcome`.
- **READ - Integrity Failure**: `AuditIntegrityException` thrown by service results in HTTP 500 `OperationOutcome` with sanitized diagnostics (no stack trace or DB details).
- **SEARCH - Unfiltered**: `GET /fhir/AuditEvent` returns HTTP 200 `Bundle` (`type=searchset`) containing events in canonical order (`recordedAt DESC, eventId DESC`).
- **SEARCH - By ID**: `GET /fhir/AuditEvent?_id=EVT-1` maps to `AuditQuery.builder().eventId("EVT-1")` and returns HTTP 200 `Bundle`.
- **SEARCH - Unsupported Parameter (`name` / `identifier`)**: `GET /fhir/AuditEvent?name=foo` or `?identifier=bar` returns HTTP 400 `OperationOutcome` fail-closed.
- **Mutation Rejection**: Reflection test confirms `AuditEventResource` has zero `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
- **Regression Targets**: `ThemisClinicalAuthorizationFilterTest` (security domain classification and `AuditReadPolicy` / `AuditImmutabilityDenyPolicy` enforcement), `KleioRuntimeCompositionTest` (CDI service graph resolution).

**Assumptions & Open Questions**

**Significant Assumptions**
- **Unsupported Legacy Search Parameters**: `name` and `identifier` query parameters on `GET /fhir/AuditEvent` are rejected with HTTP 400 Bad Request `OperationOutcome` rather than ignored or partially matched. Rationale: Audit evidence queries must be exact and fail-closed to avoid misleading clients into believing a complete filtered search was performed. Alternative: silently ignoring unsupported params was rejected as it creates false audit confidence.
- **BEFE Shared Cache Constants**: `FhirCacheService.resolveCacheName` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` are left intact for this step. Rationale: prompt explicitly mandates not touching shared Infinispan/Mnemosyne write-behind infrastructure in Step 04.4; full retirement belongs to Step 04.5.

**Delivery Steps**

**Step 1: Converge AuditEventResource to Read-Only Kleio Access & Add Resource Tests**  
Goal: Refactor `AuditEventResource` to use `AuditService` and `HarmoniaAuditEventMapper` for READ/SEARCH, eliminate mutation endpoints and `FhirCacheService`, update existing integration tests, and add comprehensive `AuditEventResourceTest`.  
Scope:
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
  Acceptance Criteria:
- [ ] `AuditEventResource` injects `AuditService` and `HarmoniaAuditEventMapper`, with zero references to `FhirCacheService`.
- [ ] `read(String id)` delegates to `auditService.findById(id)`, converts result using `auditMapper.toFhir(event)`, returns HTTP 200 with FHIR JSON, and returns HTTP 404 `OperationOutcome` when not found.
- [ ] `search(String id, String name, String identifier)` maps `_id` to `AuditQuery.eventId` and executes `auditService.find(query)`, returning a FHIR `Bundle` with entries preserving canonical ordering.
- [ ] Non-blank `name` or `identifier` query parameters return HTTP 400 `OperationOutcome` with safe diagnostic message.
- [ ] `AuditIntegrityException` and `AuditPersistenceException` are caught and mapped to HTTP 500 `OperationOutcome` with zero PHI/sensitive data leakage.
- [ ] `POST`, `PUT`, `DELETE` methods are completely removed from `AuditEventResource` (and `PATCH` remains absent).
- [ ] `FhirRestResourceIntegrationTest` is updated to remove legacy mutable `AuditEvent` cache assertions.
- [ ] `AuditEventResourceTest` provides complete coverage of READ, SEARCH, 404, 400, 500, and mutation endpoint absence.
  Verification: `mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 2: Add Iris AuditEvent Architecture Guardrail & Verify Security Regressions**  
Goal: Enforce architectural boundaries preventing `AuditEventResource` from coupling to cache or persistence implementations, and verify the full Themis security regression suite and architecture suite.  
Scope:
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
  Acceptance Criteria:
- [ ] `IrisDecouplingArchitectureTest` contains a dedicated test asserting `AuditEventResource` depends only on `AuditService` and `HarmoniaAuditEventMapper`, and forbids dependencies on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.
- [ ] All 50+ Themis authorization tests in `ThemisClinicalAuthorizationFilterTest` remain 100% green (AUDIT domain classification, `audit.read` authorization for READ/SEARCH, explicit deny for CREATE/UPDATE/DELETE/PATCH, admin override denial).
- [ ] All architecture tests in `paradeigma-test` pass with zero failures.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



REVIEW THE PLAN

**Task description**  
Review the current delivery plan.

**Feedback**  
Focus on whether the plan is the smallest clean path for Step 04.4, especially: truthful handling of unsupported legacy search params (`name`, `identifier`), safe distinction between not-found and integrity/persistence failures, removal of external mutation methods from the BEFE resource, proportional testing/guardrails, and whether anything essential is missing for the required completion report.

**Constraints**  
Keep review scoped to Step 04.4 only; do not drift into Step 04.5 or broader cache refactoring.



Plan Approved

**Verification**
- **AuditEventResource** (`iris/iris-befe/.../rest/AuditEventResource.java`): Confirmed current state matches the plan exactly — only `@Inject FhirCacheService cacheService`, `@GET` search (`_id`/`name`/`identifier`), `@GET /{id}` read with hand-built 404 OperationOutcome, and `@POST`/`@PUT`/`@DELETE` mutation methods. No PATCH exists. The plan's refactor targets (inject `AuditService` + `HarmoniaAuditEventMapper`, remove mutation methods, drop `FhirCacheService`) are accurate.
- **AuditService** (`kleio-core`): Confirmed `Optional<HarmoniaAuditEvent> findById(String)` and `List<HarmoniaAuditEvent> find(AuditQuery)` exist with documented canonical ordering (`recordedAt DESC, eventId DESC`). Plan's READ/SEARCH flows map correctly. `append(...)` is present but correctly excluded as trusted-internal.
- **HarmoniaAuditEventMapper** (`kleio-fhir`): Confirmed stateless, public no-arg constructor, `toFhir(...)` preserves `eventId` → `AuditEvent.id` and throws `HarmoniaAuditMappingException` on bad input. Plan's use is correct.
- **AuditQuery** (`kleio-core`): Confirmed immutable record with `eventId` field + `builder().eventId(...)`. No `name`/`identifier`/FHIR-`_id`-name fields — so the plan's fail-closed HTTP 400 for `name`/`identifier` is truthful and justified, not a fabricated limitation.
- **Exceptions**: `AuditIntegrityException` (kleio-core) and `AuditPersistenceException` (kleio-persistence) both exist as claimed; `HarmoniaAuditMappingException` exists (referenced in the approach text). iris-befe POM declares `kleio-persistence` (transitively pulling kleio-core/kleio-fhir), so all three are on the classpath.
- **Integration test**: Confirmed the legacy AuditEvent create/read block at `FhirRestResourceIntegrationTest.java:166–173`; removing it (as planned) is correct and won't strand the `injectService` cache helper since the whole block is dropped.
- **Architecture test**: `IrisDecouplingArchitectureTest` is entirely source-text/POM token based (not ArchUnit); the plan's extension in the same style is consistent with the file and proportionate.
- **Per-step Verification**: Both steps use real, narrowly-scoped `mvn test -pl ... -Dtest=...` commands targeting suites the step touches/adds — valid post-step done-signals.

**Issues**  
None blocking.

**Notes**
- Minor, non-blocking: catching `AuditPersistenceException` imports `net.fhirfactory.harmonia.kleio.persistence.exception.*` into the resource, which is a (contract-level) dependency on the persistence module. The plan's guardrail forbids only repos/JDBC/DataSource, so there is no conflict, and iris-befe already depends on kleio-persistence for runtime composition. A slightly cleaner alternative would be a broad `catch (RuntimeException)` → 500 to keep the resource entirely on the kleio-core service abstraction; not required.
- The acceptance criteria explicitly name `AuditIntegrityException`/`AuditPersistenceException` for 500 mapping; the mapper's `HarmoniaAuditMappingException` is only covered in the prose ("integrity/persistence/mapping exceptions"). The executor should ensure the mapping-exception path (or a broad catch) is genuinely exercised so a mapper failure doesn't escape as an uncaught 500 with a leaky stack — the plan's intent covers this, just worth keeping explicit.
- Completion-report items 17 (remaining cache artefacts) and 18 (deferred to 04.5) are addressed via the Scope/Assumptions sections; the full 19-item report is an execution deliverable, not a plan requirement.



**Requirements**

**Goal**  
Converge the Iris BEFE FHIR `AuditEvent` REST resource (`AuditEventResource.java`) from the legacy mutable Infinispan cache path onto the canonical, append-only Kleio audit capability (`AuditService` + `HarmoniaAuditEventMapper`), exposing a thin, read-only FHIR R5 view over immutable audit evidence.

**Scope**
- **In Scope:**
    - Refactoring `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` to inject `AuditService` and `HarmoniaAuditEventMapper` via CDI.
    - Implementing `GET /fhir/AuditEvent/{id}` (READ) via `AuditService.findById(id)` with canonical FHIR R5 mapping.
    - Implementing `GET /fhir/AuditEvent` (SEARCH) via `AuditService.find(AuditQuery)`, mapping `_id` to `AuditQuery.eventId` and rejecting unsupported legacy parameters (`name`, `identifier`) fail-closed with HTTP 400 `OperationOutcome`.
    - Handling not-found (HTTP 404 `OperationOutcome`) and integrity/persistence errors (HTTP 500 `OperationOutcome`) safely with zero PHI / internal error leakage.
    - Removing external mutation methods (`POST`, `PUT`, `DELETE`) from `AuditEventResource` and verifying `PATCH` remains absent.
    - Removing `FhirCacheService` dependency and coupling from `AuditEventResource`.
    - Updating `FhirRestResourceIntegrationTest` to remove legacy mutable `AuditEvent` cache assertions.
    - Adding a dedicated `AuditEventResourceTest` covering read, search, fail-closed validation, error mapping, and mutation method absence.
    - Adding an architectural boundary test in `IrisDecouplingArchitectureTest` verifying `AuditEventResource` dependencies.
- **Out of Scope (Deferred to Step 04.5 or later):**
    - Mnemosyne direct `AuditEventResourceProvider` mutation endpoints and `FhirStorageService` (Step 04.5).
    - Shared cluster Infinispan `auditevent-cache` configuration (`infinispan.xml`, `FhirRestCacheStoreTest`, `OperationsAggregatorService`).
    - Database schema, permissions, triggers, WORM hardening, or dedicated DB relocation (Tasks 07–10).
    - Frontend UI SPAs (`AuditEventView.vue`, `securityStore.ts`).
    - Creating synthetic audit producer callers (`AuditService.append(...)` is trusted internal only).

**Done When**
- `AuditEventResource` exposes only `@GET` read and search endpoints backed exclusively by `AuditService` and `HarmoniaAuditEventMapper`.
- No `POST`, `PUT`, `DELETE`, or `PATCH` methods exist on `AuditEventResource`.
- `FhirCacheService` is no longer injected or referenced in `AuditEventResource`.
- Unsupported query parameters (`name`, `identifier`) fail closed with HTTP 400 `OperationOutcome`.
- Dedicated `AuditEventResourceTest`, updated `FhirRestResourceIntegrationTest`, `ThemisClinicalAuthorizationFilterTest`, `KleioRuntimeCompositionTest`, and `*ArchitectureTest` pass cleanly.

**Technical Design**

**Decisions**
- **Inject `AuditService` & `HarmoniaAuditEventMapper` / not persistence classes or `FhirCacheService`**: Iris BEFE is presentation-only and must depend strictly on Kleio's service and mapper abstractions, preserving the CDI runtime composition verified in `KleioRuntimeCompositionTest`.
- **Fail-closed on unsupported search parameters (`name`, `identifier`) with HTTP 400 / not silent approximation**: `AuditQuery` only supports canonical fields (`eventId`, `recordedAt`, etc.); `name` (legacy cache substring) and `identifier` (unmatched for AuditEvent) cannot be faithfully represented. Approximating would violate audit fidelity.
- **Map `AuditIntegrityException` and `AuditPersistenceException` to HTTP 500 `OperationOutcome` / not 404 or empty Bundle**: Tampered, soft-deleted, or corrupted audit records must not appear missing or empty; they must signal a server-side integrity fault with sanitized, zero-PHI diagnostics.
- **Remove `POST`, `PUT`, `DELETE` methods / not retain dead methods**: Removing mutation methods from the JAX-RS resource eliminates dead code while Themis `ThemisClinicalAuthorizationFilter` + `AuditImmutabilityDenyPolicy` continues to enforce default-deny at the security perimeter.

**Approach & Touches**
- **Primary resource:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
    - Replace `@Inject FhirCacheService cacheService` with `@Inject AuditService auditService` and `@Inject HarmoniaAuditEventMapper auditMapper` (with lazy fallback initialization for `auditMapper` and `FhirContext` / `IParser` in non-CDI test environments).
    - `read(String id)`: call `auditService.findById(id)`. If empty -> HTTP 404 `OperationOutcome`. If found -> `auditMapper.toFhir(event)` -> FHIR JSON 200. Catch integrity/persistence/mapping exceptions -> HTTP 500 `OperationOutcome`.
    - `search(String id, String name, String identifier)`: if `name` or `identifier` is non-blank -> return HTTP 400 `OperationOutcome`. Otherwise construct `AuditQuery` (with `eventId(id)` if `id` is present), call `auditService.find(query)`, map results with `auditMapper.toFhir(...)`, assemble into a FHIR `Bundle` (`type = searchset`, total count, entries with `fullUrl`), and return FHIR JSON 200. Catch exceptions -> HTTP 500 `OperationOutcome`.
    - Remove `create(...)`, `update(...)`, and `delete(...)` methods.
- **Integration test update:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
    - Remove `testNewResourceControllers` legacy AuditEvent create/read block (lines 166–173).
- **New resource test:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
    - Unit & mock tests verifying READ (found, not found, integrity error, serialization), SEARCH (default query, `_id` query, unsupported `name`/`identifier` 400, ordering preservation, bundle structure), and reflection assertions proving absence of `POST`, `PUT`, `PATCH`, `DELETE` annotations/methods.
- **Architecture guardrail:** `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
    - Add test asserting `AuditEventResource` depends only on `AuditService` / `HarmoniaAuditEventMapper` and does not import or depend on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.

**Nuances, Risks & Corners**
- **Zero-PHI Diagnostic Logging & Error Entities**: Diagnostics in `OperationOutcome` on 400, 404, or 500 must strictly contain static descriptors and IDs without echoing user inputs, query parameters, stack traces, or SQL state.
- **CDI vs Direct Instantiation in Tests**: `AuditEventResource` should allow injection of dependencies or package-private setters / constructor injection so unit tests can test it without spinning up a full EE container.
- **Deterministic Search Ordering**: `AuditService.find(...)` already guarantees canonical `recordedAt DESC, eventId DESC` ordering; `AuditEventResource` must preserve this order when assembling the `Bundle` entries without re-sorting.
- **Legacy BEFE Cache Configuration**: `FhirCacheService.resolveCacheName("auditevent")` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` remain in codebase; they are not touched in this BEFE-only step to avoid breaking shared cluster references, and will be reported for Step 04.5 cleanup.

**Contracts**
- Point Read Response:
    - Success: HTTP 200 OK with FHIR R5 `AuditEvent` JSON (`application/fhir+json`).
    - Not Found: HTTP 404 Not Found with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-found","diagnostics":"AuditEvent/<id> not found"}]}`).
    - Server / Integrity Error: HTTP 500 Internal Server Error with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"exception","diagnostics":"Internal error retrieving AuditEvent"}]}`).
- Search Response:
    - Success: HTTP 200 OK with FHIR R5 `Bundle` JSON (`resourceType: Bundle`, `type: searchset`, `total: N`, `entry: [...]`).
    - Invalid Parameter: HTTP 400 Bad Request with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-supported","diagnostics":"Search parameter not supported for AuditEvent"}]}`).

**Testing**

**Checklist of Scenarios**
- **READ - Found**: `GET /fhir/AuditEvent/{id}` returns HTTP 200 with matching canonical fields mapped to FHIR `AuditEvent`, `id` preserved, `recorded` instant intact.
- **READ - Not Found**: `GET /fhir/AuditEvent/{non-existent}` returns HTTP 404 with standard `OperationOutcome`.
- **READ - Integrity Failure**: `AuditIntegrityException` thrown by service results in HTTP 500 `OperationOutcome` with sanitized diagnostics (no stack trace or DB details).
- **SEARCH - Unfiltered**: `GET /fhir/AuditEvent` returns HTTP 200 `Bundle` (`type=searchset`) containing events in canonical order (`recordedAt DESC, eventId DESC`).
- **SEARCH - By ID**: `GET /fhir/AuditEvent?_id=EVT-1` maps to `AuditQuery.builder().eventId("EVT-1")` and returns HTTP 200 `Bundle`.
- **SEARCH - Unsupported Parameter (`name` / `identifier`)**: `GET /fhir/AuditEvent?name=foo` or `?identifier=bar` returns HTTP 400 `OperationOutcome` fail-closed.
- **Mutation Rejection**: Reflection test confirms `AuditEventResource` has zero `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
- **Regression Targets**: `ThemisClinicalAuthorizationFilterTest` (security domain classification and `AuditReadPolicy` / `AuditImmutabilityDenyPolicy` enforcement), `KleioRuntimeCompositionTest` (CDI service graph resolution).

**Assumptions & Open Questions**

**Significant Assumptions**
- **Unsupported Legacy Search Parameters**: `name` and `identifier` query parameters on `GET /fhir/AuditEvent` are rejected with HTTP 400 Bad Request `OperationOutcome` rather than ignored or partially matched. Rationale: Audit evidence queries must be exact and fail-closed to avoid misleading clients into believing a complete filtered search was performed. Alternative: silently ignoring unsupported params was rejected as it creates false audit confidence.
- **BEFE Shared Cache Constants**: `FhirCacheService.resolveCacheName` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` are left intact for this step. Rationale: prompt explicitly mandates not touching shared Infinispan/Mnemosyne write-behind infrastructure in Step 04.4; full retirement belongs to Step 04.5.

**Delivery Steps**

*** Step 1: Converge AuditEventResource to Read-Only Kleio Access & Add Resource Tests**  
Goal: Refactor `AuditEventResource` to use `AuditService` and `HarmoniaAuditEventMapper` for READ/SEARCH, eliminate mutation endpoints and `FhirCacheService`, update existing integration tests, and add comprehensive `AuditEventResourceTest`.  
Scope:
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
  Acceptance Criteria:
- [ ] `AuditEventResource` injects `AuditService` and `HarmoniaAuditEventMapper`, with zero references to `FhirCacheService`.
- [ ] `read(String id)` delegates to `auditService.findById(id)`, converts result using `auditMapper.toFhir(event)`, returns HTTP 200 with FHIR JSON, and returns HTTP 404 `OperationOutcome` when not found.
- [ ] `search(String id, String name, String identifier)` maps `_id` to `AuditQuery.eventId` and executes `auditService.find(query)`, returning a FHIR `Bundle` with entries preserving canonical ordering.
- [ ] Non-blank `name` or `identifier` query parameters return HTTP 400 `OperationOutcome` with safe diagnostic message.
- [ ] `AuditIntegrityException` and `AuditPersistenceException` are caught and mapped to HTTP 500 `OperationOutcome` with zero PHI/sensitive data leakage.
- [ ] `POST`, `PUT`, `DELETE` methods are completely removed from `AuditEventResource` (and `PATCH` remains absent).
- [ ] `FhirRestResourceIntegrationTest` is updated to remove legacy mutable `AuditEvent` cache assertions.
- [ ] `AuditEventResourceTest` provides complete coverage of READ, SEARCH, 404, 400, 500, and mutation endpoint absence.
  Verification: `mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 2: Add Iris AuditEvent Architecture Guardrail & Verify Security Regressions**  
Goal: Enforce architectural boundaries preventing `AuditEventResource` from coupling to cache or persistence implementations, and verify the full Themis security regression suite and architecture suite.  
Scope:
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
  Acceptance Criteria:
- [ ] `IrisDecouplingArchitectureTest` contains a dedicated test asserting `AuditEventResource` depends only on `AuditService` and `HarmoniaAuditEventMapper`, and forbids dependencies on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.
- [ ] All 50+ Themis authorization tests in `ThemisClinicalAuthorizationFilterTest` remain 100% green (AUDIT domain classification, `audit.read` authorization for READ/SEARCH, explicit deny for CREATE/UPDATE/DELETE/PATCH, admin override denial).
- [ ] All architecture tests in `paradeigma-test` pass with zero failures.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Implement the BEFE-only Step 04.4 convergence described in the current step. Refactor `AuditEventResource` from the legacy cache-backed mutable CRUD surface to a thin read-only adapter over canonical Kleio audit evidence.

Key requirements for this executor pass:
- Inspect and preserve local BEFE response conventions where appropriate, but make `AuditEventResource` depend on CDI-injected `AuditService` plus canonical `HarmoniaAuditEventMapper`, not `FhirCacheService`.
- Implement READ via `auditService.findById(id)` -> `HarmoniaAuditEventMapper.toFhir(...)` -> FHIR JSON response.
- Implement SEARCH via `auditService.find(AuditQuery)` with explicit mapping `_id -> AuditQuery.eventId`; preserve returned ordering from `AuditService` and build the expected FHIR Bundle/API representation.
- Treat non-blank `name` and `identifier` as explicitly unsupported/fail-closed per the plan; do not silently approximate legacy cache semantics.
- Remove external POST/PUT/DELETE methods from the resource; PATCH must remain absent.
- Safely distinguish not-found from server-side integrity/persistence/mapping failures. Do not leak PHI, SQL, stack traces, or raw exception messages in logs or external OperationOutcome diagnostics.
- Update/remove the legacy AuditEvent create/read integration assertions in `FhirRestResourceIntegrationTest` and add a focused `AuditEventResourceTest` covering read/search/not-found/unsupported-params/server-failure/mutation-surface absence.
- Keep Step 04.5 and broader cache infrastructure cleanup out of scope.

Known from previous steps:
- Step 04.3 is already complete: Themis classifies `AuditEvent` as AUDIT and explicit-denies mutation, including for `system.admin`.
- Current `AuditEventResource` exposes GET/POST/PUT/DELETE and only injects `FhirCacheService`.
- `AuditService` offers `Optional<HarmoniaAuditEvent> findById(String)` and `List<HarmoniaAuditEvent> find(AuditQuery)`.
- `HarmoniaAuditEventMapper.toFhir(...)` preserves `eventId` as FHIR `AuditEvent.id`.
- `AuditQuery` can faithfully represent `_id` via `eventId`, but not current legacy `name` or `identifier` semantics.
- `DurableAuditService` throws `AuditIntegrityException` for malformed/deleted evidence and `AuditPersistenceException` for persistence/scan-ceiling failures; these must not be converted to 404 or empty success.

Optimize execution speed:
- Start with narrow file-scoped edits and focused tests first.
- Use small command timeouts initially (60–120s) and widen only if necessary.
- Prefer narrow module-scoped verification commands over broad suites.
- Report exact commands run, timeouts, and any execution blockers in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Accomplishments**

Step 1 has been completed successfully:
1. **Refactored `AuditEventResource.java` (`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`)**:
    - Replaced legacy cache injection (`FhirCacheService`) with CDI-injected `AuditService` and `HarmoniaAuditEventMapper`.
    - Implemented `GET /fhir/AuditEvent/{id}` (READ) delegating to `auditService.findById(id)` -> `auditMapper.toFhir(event)` -> FHIR JSON response, returning HTTP 404 `OperationOutcome` when not found.
    - Implemented `GET /fhir/AuditEvent` (SEARCH) mapping `_id` to `AuditQuery.eventId` and executing `auditService.find(query)` to return a FHIR R5 `Bundle` (`type=searchset`, `total`, `fullUrl`) preserving canonical descending sort order (`recordedAt DESC, eventId DESC`).
    - Implemented fail-closed validation on unsupported legacy search parameters (`name`, `identifier`), returning HTTP 400 `OperationOutcome` with code `not-supported`.
    - Implemented safe error handling mapping `AuditIntegrityException`, `AuditPersistenceException`, `HarmoniaAuditMappingException`, and other server failures to HTTP 500 `OperationOutcome` with code `exception` and sanitized diagnostics with zero PHI, SQL, or internal stack trace leakage.
    - Removed legacy mutation methods (`POST`, `PUT`, `DELETE`), keeping `PATCH` absent.

2. **Updated `FhirRestResourceIntegrationTest.java` (`iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`)**:
    - Removed legacy mutable `AuditEvent` create and read cache assertions.

3. **Created `AuditEventResourceTest.java` (`iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`)**:
    - Implemented 14 unit and reflection tests covering:
        - `read` found (HTTP 200 with mapped fields)
        - `read` not found (HTTP 404 with OperationOutcome)
        - `read` blank id (HTTP 404)
        - `read` integrity/persistence/mapping exceptions (HTTP 500 with zero sensitive data leak)
        - `search` unfiltered with canonical ordering preservation (HTTP 200 Bundle)
        - `search` by `_id` mapped to `AuditQuery.eventId` (HTTP 200 Bundle)
        - `search` with unsupported `name` or `identifier` (HTTP 400 OperationOutcome fail-closed)
        - `search` integrity/persistence exceptions (HTTP 500)
        - Reflection assertions verifying absence of `@POST`, `@PUT`, `@DELETE`, `@PATCH` methods or methods named `create`, `update`, `delete`, `patch`
        - Reflection assertions verifying complete absence of `FhirCacheService` field.

**Verification**  
Executed:  
`mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest,FhirCacheServiceTest" -Dsurefire.failIfNoSpecifiedTests=false`  
Result: **76 tests run, 0 failures, 0 errors, 0 skipped**.

Executed:  
`mvn test -pl paradeigma/paradeigma-test -am -Dtest="IrisDecouplingArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`  
Result: **BUILD SUCCESS, 0 failures, 0 errors**.

**Discoveries for future steps**
- **Execution commands & speed**: Running module-scoped tests with `-am` and explicit test selector `-Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` completes in ~5 seconds.
- **Recommendations**: In environments without a running Docker daemon, broad full-repo test runs should exclude Testcontainers-dependent tests (e.g. `DurableAuditServicePostgreSqlConcurrencyTest`) when testing downstream modules.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Verify only the step-1 scope: `AuditEventResource` convergence to read-only Kleio access plus resource/integration tests. Focus on:
- `AuditEventResource` now depending on `AuditService` and `HarmoniaAuditEventMapper`, with no `FhirCacheService` usage;
- correct READ and SEARCH flows, especially `_id -> AuditQuery.eventId` mapping and preservation of ordering from `AuditService`;
- explicit fail-closed handling of unsupported `name` / `identifier` params;
- safe separation of 404 not-found vs 500 integrity/persistence/mapping failures without leaking internals;
- complete removal of POST/PUT/DELETE surface and continued absence of PATCH;
- adequacy and truthfulness of `AuditEventResourceTest` and integration-test updates.
  Do not judge step-2 architecture guardrail work here except to note if step 1 accidentally crossed into it.



Implementation Approved

**Verification**
- Read `AuditEventResource.java` independently: it now injects `AuditService` + `HarmoniaAuditEventMapper` via `@Inject`, has zero `FhirCacheService` references, and only exposes two `@GET` methods (`search`, `read`). No `@POST`/`@PUT`/`@DELETE`/`@PATCH` methods or `create/update/delete/patch` method names exist.
- READ flow: `auditService.findById(id.trim())` → empty → 404 `OperationOutcome` (NOTFOUND); present → `auditMapper.toFhir(event)` → 200 FHIR JSON; exceptions → 500 `OperationOutcome` (EXCEPTION) with static diagnostic. Blank id defensively handled as 404.
- SEARCH flow: non-blank `name`/`identifier` fails closed with 400 (NOTSUPPORTED) *before* touching the service (verified via `verifyNoInteractions`); `_id` maps to `AuditQuery.builder().eventId(id.trim())`; results assembled into `searchset` Bundle with `total` and `fullUrl`, preserving service ordering (no re-sort); exceptions → 500.
- Error sanitization: diagnostics are static strings; logs emit only exception class simple name (and event id, not PHI). Tests assert absence of `PostgreSQL`, IP, `deadbeef`, `checksum` in output.
- Verified exception/mapper signatures used by tests against actual Kleio sources (`AuditIntegrityException(eventId,message)` and `(message)` constructors exist; `HarmoniaAuditEventMapper` is a non-final public class, so the mock in the mapping-exception test is valid).
- Confirmed the legacy AuditEvent create/read block and its `@DisplayName` reference were fully removed from `FhirRestResourceIntegrationTest` (no remaining `AuditEvent` references).
- Ran the step's verification command: `AuditEventResourceTest` 14/14, `FhirRestResourceIntegrationTest` 5/5, `KleioRuntimeCompositionTest` 3/3, `ThemisClinicalAuthorizationFilterTest` 50/50 — all green (build exit 0).
- Confirmed executor did not cross into Step 2 scope (`IrisDecouplingArchitectureTest` untouched).

**Issues**  
None blocking.

**Notes**
- The 404 diagnostic echoes the raw (untrimmed) path `id` as `"AuditEvent/" + id + " not found"`. This matches the plan's explicit contract (`AuditEvent/<id> not found`) and the id is an audit event identifier (not PHI), so it is acceptable; just noting the mild tension with the "no user-input echo" nuance.
- `@Consumes({...})` remains on the class though the resource no longer accepts request bodies. Harmless/vestigial for GET-only endpoints; not worth changing.

**Discoveries for future steps**
- Verification command completes fast (~a few seconds of test execution; JVM/Weld warmup dominates). Tests run offline with `-o` fine. No Docker/Testcontainers needed for this selector — avoid pulling in `DurableAuditServicePostgreSqlConcurrencyTest` on broad runs.



**Requirements**

**Goal**  
Converge the Iris BEFE FHIR `AuditEvent` REST resource (`AuditEventResource.java`) from the legacy mutable Infinispan cache path onto the canonical, append-only Kleio audit capability (`AuditService` + `HarmoniaAuditEventMapper`), exposing a thin, read-only FHIR R5 view over immutable audit evidence.

**Scope**
- **In Scope:**
    - Refactoring `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` to inject `AuditService` and `HarmoniaAuditEventMapper` via CDI.
    - Implementing `GET /fhir/AuditEvent/{id}` (READ) via `AuditService.findById(id)` with canonical FHIR R5 mapping.
    - Implementing `GET /fhir/AuditEvent` (SEARCH) via `AuditService.find(AuditQuery)`, mapping `_id` to `AuditQuery.eventId` and rejecting unsupported legacy parameters (`name`, `identifier`) fail-closed with HTTP 400 `OperationOutcome`.
    - Handling not-found (HTTP 404 `OperationOutcome`) and integrity/persistence errors (HTTP 500 `OperationOutcome`) safely with zero PHI / internal error leakage.
    - Removing external mutation methods (`POST`, `PUT`, `DELETE`) from `AuditEventResource` and verifying `PATCH` remains absent.
    - Removing `FhirCacheService` dependency and coupling from `AuditEventResource`.
    - Updating `FhirRestResourceIntegrationTest` to remove legacy mutable `AuditEvent` cache assertions.
    - Adding a dedicated `AuditEventResourceTest` covering read, search, fail-closed validation, error mapping, and mutation method absence.
    - Adding an architectural boundary test in `IrisDecouplingArchitectureTest` verifying `AuditEventResource` dependencies.
- **Out of Scope (Deferred to Step 04.5 or later):**
    - Mnemosyne direct `AuditEventResourceProvider` mutation endpoints and `FhirStorageService` (Step 04.5).
    - Shared cluster Infinispan `auditevent-cache` configuration (`infinispan.xml`, `FhirRestCacheStoreTest`, `OperationsAggregatorService`).
    - Database schema, permissions, triggers, WORM hardening, or dedicated DB relocation (Tasks 07–10).
    - Frontend UI SPAs (`AuditEventView.vue`, `securityStore.ts`).
    - Creating synthetic audit producer callers (`AuditService.append(...)` is trusted internal only).

**Done When**
- `AuditEventResource` exposes only `@GET` read and search endpoints backed exclusively by `AuditService` and `HarmoniaAuditEventMapper`.
- No `POST`, `PUT`, `DELETE`, or `PATCH` methods exist on `AuditEventResource`.
- `FhirCacheService` is no longer injected or referenced in `AuditEventResource`.
- Unsupported query parameters (`name`, `identifier`) fail closed with HTTP 400 `OperationOutcome`.
- Dedicated `AuditEventResourceTest`, updated `FhirRestResourceIntegrationTest`, `ThemisClinicalAuthorizationFilterTest`, `KleioRuntimeCompositionTest`, and `*ArchitectureTest` pass cleanly.

**Technical Design**

**Decisions**
- **Inject `AuditService` & `HarmoniaAuditEventMapper` / not persistence classes or `FhirCacheService`**: Iris BEFE is presentation-only and must depend strictly on Kleio's service and mapper abstractions, preserving the CDI runtime composition verified in `KleioRuntimeCompositionTest`.
- **Fail-closed on unsupported search parameters (`name`, `identifier`) with HTTP 400 / not silent approximation**: `AuditQuery` only supports canonical fields (`eventId`, `recordedAt`, etc.); `name` (legacy cache substring) and `identifier` (unmatched for AuditEvent) cannot be faithfully represented. Approximating would violate audit fidelity.
- **Map `AuditIntegrityException` and `AuditPersistenceException` to HTTP 500 `OperationOutcome` / not 404 or empty Bundle**: Tampered, soft-deleted, or corrupted audit records must not appear missing or empty; they must signal a server-side integrity fault with sanitized, zero-PHI diagnostics.
- **Remove `POST`, `PUT`, `DELETE` methods / not retain dead methods**: Removing mutation methods from the JAX-RS resource eliminates dead code while Themis `ThemisClinicalAuthorizationFilter` + `AuditImmutabilityDenyPolicy` continues to enforce default-deny at the security perimeter.

**Approach & Touches**
- **Primary resource:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
    - Replace `@Inject FhirCacheService cacheService` with `@Inject AuditService auditService` and `@Inject HarmoniaAuditEventMapper auditMapper` (with lazy fallback initialization for `auditMapper` and `FhirContext` / `IParser` in non-CDI test environments).
    - `read(String id)`: call `auditService.findById(id)`. If empty -> HTTP 404 `OperationOutcome`. If found -> `auditMapper.toFhir(event)` -> FHIR JSON 200. Catch integrity/persistence/mapping exceptions -> HTTP 500 `OperationOutcome`.
    - `search(String id, String name, String identifier)`: if `name` or `identifier` is non-blank -> return HTTP 400 `OperationOutcome`. Otherwise construct `AuditQuery` (with `eventId(id)` if `id` is present), call `auditService.find(query)`, map results with `auditMapper.toFhir(...)`, assemble into a FHIR `Bundle` (`type = searchset`, total count, entries with `fullUrl`), and return FHIR JSON 200. Catch exceptions -> HTTP 500 `OperationOutcome`.
    - Remove `create(...)`, `update(...)`, and `delete(...)` methods.
- **Integration test update:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
    - Remove `testNewResourceControllers` legacy AuditEvent create/read block (lines 166–173).
- **New resource test:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
    - Unit & mock tests verifying READ (found, not found, integrity error, serialization), SEARCH (default query, `_id` query, unsupported `name`/`identifier` 400, ordering preservation, bundle structure), and reflection assertions proving absence of `POST`, `PUT`, `PATCH`, `DELETE` annotations/methods.
- **Architecture guardrail:** `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
    - Add test asserting `AuditEventResource` depends only on `AuditService` / `HarmoniaAuditEventMapper` and does not import or depend on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.

**Nuances, Risks & Corners**
- **Zero-PHI Diagnostic Logging & Error Entities**: Diagnostics in `OperationOutcome` on 400, 404, or 500 must strictly contain static descriptors and IDs without echoing user inputs, query parameters, stack traces, or SQL state.
- **CDI vs Direct Instantiation in Tests**: `AuditEventResource` should allow injection of dependencies or package-private setters / constructor injection so unit tests can test it without spinning up a full EE container.
- **Deterministic Search Ordering**: `AuditService.find(...)` already guarantees canonical `recordedAt DESC, eventId DESC` ordering; `AuditEventResource` must preserve this order when assembling the `Bundle` entries without re-sorting.
- **Legacy BEFE Cache Configuration**: `FhirCacheService.resolveCacheName("auditevent")` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` remain in codebase; they are not touched in this BEFE-only step to avoid breaking shared cluster references, and will be reported for Step 04.5 cleanup.

**Contracts**
- Point Read Response:
    - Success: HTTP 200 OK with FHIR R5 `AuditEvent` JSON (`application/fhir+json`).
    - Not Found: HTTP 404 Not Found with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-found","diagnostics":"AuditEvent/<id> not found"}]}`).
    - Server / Integrity Error: HTTP 500 Internal Server Error with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"exception","diagnostics":"Internal error retrieving AuditEvent"}]}`).
- Search Response:
    - Success: HTTP 200 OK with FHIR R5 `Bundle` JSON (`resourceType: Bundle`, `type: searchset`, `total: N`, `entry: [...]`).
    - Invalid Parameter: HTTP 400 Bad Request with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-supported","diagnostics":"Search parameter not supported for AuditEvent"}]}`).

**Testing**

**Checklist of Scenarios**
- **READ - Found**: `GET /fhir/AuditEvent/{id}` returns HTTP 200 with matching canonical fields mapped to FHIR `AuditEvent`, `id` preserved, `recorded` instant intact.
- **READ - Not Found**: `GET /fhir/AuditEvent/{non-existent}` returns HTTP 404 with standard `OperationOutcome`.
- **READ - Integrity Failure**: `AuditIntegrityException` thrown by service results in HTTP 500 `OperationOutcome` with sanitized diagnostics (no stack trace or DB details).
- **SEARCH - Unfiltered**: `GET /fhir/AuditEvent` returns HTTP 200 `Bundle` (`type=searchset`) containing events in canonical order (`recordedAt DESC, eventId DESC`).
- **SEARCH - By ID**: `GET /fhir/AuditEvent?_id=EVT-1` maps to `AuditQuery.builder().eventId("EVT-1")` and returns HTTP 200 `Bundle`.
- **SEARCH - Unsupported Parameter (`name` / `identifier`)**: `GET /fhir/AuditEvent?name=foo` or `?identifier=bar` returns HTTP 400 `OperationOutcome` fail-closed.
- **Mutation Rejection**: Reflection test confirms `AuditEventResource` has zero `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
- **Regression Targets**: `ThemisClinicalAuthorizationFilterTest` (security domain classification and `AuditReadPolicy` / `AuditImmutabilityDenyPolicy` enforcement), `KleioRuntimeCompositionTest` (CDI service graph resolution).

**Assumptions & Open Questions**

**Significant Assumptions**
- **Unsupported Legacy Search Parameters**: `name` and `identifier` query parameters on `GET /fhir/AuditEvent` are rejected with HTTP 400 Bad Request `OperationOutcome` rather than ignored or partially matched. Rationale: Audit evidence queries must be exact and fail-closed to avoid misleading clients into believing a complete filtered search was performed. Alternative: silently ignoring unsupported params was rejected as it creates false audit confidence.
- **BEFE Shared Cache Constants**: `FhirCacheService.resolveCacheName` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` are left intact for this step. Rationale: prompt explicitly mandates not touching shared Infinispan/Mnemosyne write-behind infrastructure in Step 04.4; full retirement belongs to Step 04.5.

**Delivery Steps**

**✓ Step 1: Converge AuditEventResource to Read-Only Kleio Access & Add Resource Tests**  
Goal: Refactor `AuditEventResource` to use `AuditService` and `HarmoniaAuditEventMapper` for READ/SEARCH, eliminate mutation endpoints and `FhirCacheService`, update existing integration tests, and add comprehensive `AuditEventResourceTest`.  
Scope:
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
  Acceptance Criteria:
- [ ] `AuditEventResource` injects `AuditService` and `HarmoniaAuditEventMapper`, with zero references to `FhirCacheService`.
- [ ] `read(String id)` delegates to `auditService.findById(id)`, converts result using `auditMapper.toFhir(event)`, returns HTTP 200 with FHIR JSON, and returns HTTP 404 `OperationOutcome` when not found.
- [ ] `search(String id, String name, String identifier)` maps `_id` to `AuditQuery.eventId` and executes `auditService.find(query)`, returning a FHIR `Bundle` with entries preserving canonical ordering.
- [ ] Non-blank `name` or `identifier` query parameters return HTTP 400 `OperationOutcome` with safe diagnostic message.
- [ ] `AuditIntegrityException` and `AuditPersistenceException` are caught and mapped to HTTP 500 `OperationOutcome` with zero PHI/sensitive data leakage.
- [ ] `POST`, `PUT`, `DELETE` methods are completely removed from `AuditEventResource` (and `PATCH` remains absent).
- [ ] `FhirRestResourceIntegrationTest` is updated to remove legacy mutable `AuditEvent` cache assertions.
- [ ] `AuditEventResourceTest` provides complete coverage of READ, SEARCH, 404, 400, 500, and mutation endpoint absence.
  Verification: `mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 2: Add Iris AuditEvent Architecture Guardrail & Verify Security Regressions**  
Goal: Enforce architectural boundaries preventing `AuditEventResource` from coupling to cache or persistence implementations, and verify the full Themis security regression suite and architecture suite.  
Scope:
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
  Acceptance Criteria:
- [ ] `IrisDecouplingArchitectureTest` contains a dedicated test asserting `AuditEventResource` depends only on `AuditService` and `HarmoniaAuditEventMapper`, and forbids dependencies on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.
- [ ] All 50+ Themis authorization tests in `ThemisClinicalAuthorizationFilterTest` remain 100% green (AUDIT domain classification, `audit.read` authorization for READ/SEARCH, explicit deny for CREATE/UPDATE/DELETE/PATCH, admin override denial).
- [ ] All architecture tests in `paradeigma-test` pass with zero failures.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Requirements**

**Goal**  
Converge the Iris BEFE FHIR `AuditEvent` REST resource (`AuditEventResource.java`) from the legacy mutable Infinispan cache path onto the canonical, append-only Kleio audit capability (`AuditService` + `HarmoniaAuditEventMapper`), exposing a thin, read-only FHIR R5 view over immutable audit evidence.

**Scope**
- **In Scope:**
    - Refactoring `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` to inject `AuditService` and `HarmoniaAuditEventMapper` via CDI.
    - Implementing `GET /fhir/AuditEvent/{id}` (READ) via `AuditService.findById(id)` with canonical FHIR R5 mapping.
    - Implementing `GET /fhir/AuditEvent` (SEARCH) via `AuditService.find(AuditQuery)`, mapping `_id` to `AuditQuery.eventId` and rejecting unsupported legacy parameters (`name`, `identifier`) fail-closed with HTTP 400 `OperationOutcome`.
    - Handling not-found (HTTP 404 `OperationOutcome`) and integrity/persistence errors (HTTP 500 `OperationOutcome`) safely with zero PHI / internal error leakage.
    - Removing external mutation methods (`POST`, `PUT`, `DELETE`) from `AuditEventResource` and verifying `PATCH` remains absent.
    - Removing `FhirCacheService` dependency and coupling from `AuditEventResource`.
    - Updating `FhirRestResourceIntegrationTest` to remove legacy mutable `AuditEvent` cache assertions.
    - Adding a dedicated `AuditEventResourceTest` covering read, search, fail-closed validation, error mapping, and mutation method absence.
    - Adding an architectural boundary test in `IrisDecouplingArchitectureTest` verifying `AuditEventResource` dependencies.
- **Out of Scope (Deferred to Step 04.5 or later):**
    - Mnemosyne direct `AuditEventResourceProvider` mutation endpoints and `FhirStorageService` (Step 04.5).
    - Shared cluster Infinispan `auditevent-cache` configuration (`infinispan.xml`, `FhirRestCacheStoreTest`, `OperationsAggregatorService`).
    - Database schema, permissions, triggers, WORM hardening, or dedicated DB relocation (Tasks 07–10).
    - Frontend UI SPAs (`AuditEventView.vue`, `securityStore.ts`).
    - Creating synthetic audit producer callers (`AuditService.append(...)` is trusted internal only).

**Done When**
- `AuditEventResource` exposes only `@GET` read and search endpoints backed exclusively by `AuditService` and `HarmoniaAuditEventMapper`.
- No `POST`, `PUT`, `DELETE`, or `PATCH` methods exist on `AuditEventResource`.
- `FhirCacheService` is no longer injected or referenced in `AuditEventResource`.
- Unsupported query parameters (`name`, `identifier`) fail closed with HTTP 400 `OperationOutcome`.
- Dedicated `AuditEventResourceTest`, updated `FhirRestResourceIntegrationTest`, `ThemisClinicalAuthorizationFilterTest`, `KleioRuntimeCompositionTest`, and `*ArchitectureTest` pass cleanly.

**Technical Design**

**Decisions**
- **Inject `AuditService` & `HarmoniaAuditEventMapper` / not persistence classes or `FhirCacheService`**: Iris BEFE is presentation-only and must depend strictly on Kleio's service and mapper abstractions, preserving the CDI runtime composition verified in `KleioRuntimeCompositionTest`.
- **Fail-closed on unsupported search parameters (`name`, `identifier`) with HTTP 400 / not silent approximation**: `AuditQuery` only supports canonical fields (`eventId`, `recordedAt`, etc.); `name` (legacy cache substring) and `identifier` (unmatched for AuditEvent) cannot be faithfully represented. Approximating would violate audit fidelity.
- **Map `AuditIntegrityException` and `AuditPersistenceException` to HTTP 500 `OperationOutcome` / not 404 or empty Bundle**: Tampered, soft-deleted, or corrupted audit records must not appear missing or empty; they must signal a server-side integrity fault with sanitized, zero-PHI diagnostics.
- **Remove `POST`, `PUT`, `DELETE` methods / not retain dead methods**: Removing mutation methods from the JAX-RS resource eliminates dead code while Themis `ThemisClinicalAuthorizationFilter` + `AuditImmutabilityDenyPolicy` continues to enforce default-deny at the security perimeter.

**Approach & Touches**
- **Primary resource:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
    - Replace `@Inject FhirCacheService cacheService` with `@Inject AuditService auditService` and `@Inject HarmoniaAuditEventMapper auditMapper` (with lazy fallback initialization for `auditMapper` and `FhirContext` / `IParser` in non-CDI test environments).
    - `read(String id)`: call `auditService.findById(id)`. If empty -> HTTP 404 `OperationOutcome`. If found -> `auditMapper.toFhir(event)` -> FHIR JSON 200. Catch integrity/persistence/mapping exceptions -> HTTP 500 `OperationOutcome`.
    - `search(String id, String name, String identifier)`: if `name` or `identifier` is non-blank -> return HTTP 400 `OperationOutcome`. Otherwise construct `AuditQuery` (with `eventId(id)` if `id` is present), call `auditService.find(query)`, map results with `auditMapper.toFhir(...)`, assemble into a FHIR `Bundle` (`type = searchset`, total count, entries with `fullUrl`), and return FHIR JSON 200. Catch exceptions -> HTTP 500 `OperationOutcome`.
    - Remove `create(...)`, `update(...)`, and `delete(...)` methods.
- **Integration test update:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
    - Remove `testNewResourceControllers` legacy AuditEvent create/read block (lines 166–173).
- **New resource test:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
    - Unit & mock tests verifying READ (found, not found, integrity error, serialization), SEARCH (default query, `_id` query, unsupported `name`/`identifier` 400, ordering preservation, bundle structure), and reflection assertions proving absence of `POST`, `PUT`, `PATCH`, `DELETE` annotations/methods.
- **Architecture guardrail:** `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
    - Add test asserting `AuditEventResource` depends only on `AuditService` / `HarmoniaAuditEventMapper` and does not import or depend on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.

**Nuances, Risks & Corners**
- **Zero-PHI Diagnostic Logging & Error Entities**: Diagnostics in `OperationOutcome` on 400, 404, or 500 must strictly contain static descriptors and IDs without echoing user inputs, query parameters, stack traces, or SQL state.
- **CDI vs Direct Instantiation in Tests**: `AuditEventResource` should allow injection of dependencies or package-private setters / constructor injection so unit tests can test it without spinning up a full EE container.
- **Deterministic Search Ordering**: `AuditService.find(...)` already guarantees canonical `recordedAt DESC, eventId DESC` ordering; `AuditEventResource` must preserve this order when assembling the `Bundle` entries without re-sorting.
- **Legacy BEFE Cache Configuration**: `FhirCacheService.resolveCacheName("auditevent")` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` remain in codebase; they are not touched in this BEFE-only step to avoid breaking shared cluster references, and will be reported for Step 04.5 cleanup.

**Contracts**
- Point Read Response:
    - Success: HTTP 200 OK with FHIR R5 `AuditEvent` JSON (`application/fhir+json`).
    - Not Found: HTTP 404 Not Found with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-found","diagnostics":"AuditEvent/<id> not found"}]}`).
    - Server / Integrity Error: HTTP 500 Internal Server Error with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"exception","diagnostics":"Internal error retrieving AuditEvent"}]}`).
- Search Response:
    - Success: HTTP 200 OK with FHIR R5 `Bundle` JSON (`resourceType: Bundle`, `type: searchset`, `total: N`, `entry: [...]`).
    - Invalid Parameter: HTTP 400 Bad Request with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-supported","diagnostics":"Search parameter not supported for AuditEvent"}]}`).

**Testing**

**Checklist of Scenarios**
- **READ - Found**: `GET /fhir/AuditEvent/{id}` returns HTTP 200 with matching canonical fields mapped to FHIR `AuditEvent`, `id` preserved, `recorded` instant intact.
- **READ - Not Found**: `GET /fhir/AuditEvent/{non-existent}` returns HTTP 404 with standard `OperationOutcome`.
- **READ - Integrity Failure**: `AuditIntegrityException` thrown by service results in HTTP 500 `OperationOutcome` with sanitized diagnostics (no stack trace or DB details).
- **SEARCH - Unfiltered**: `GET /fhir/AuditEvent` returns HTTP 200 `Bundle` (`type=searchset`) containing events in canonical order (`recordedAt DESC, eventId DESC`).
- **SEARCH - By ID**: `GET /fhir/AuditEvent?_id=EVT-1` maps to `AuditQuery.builder().eventId("EVT-1")` and returns HTTP 200 `Bundle`.
- **SEARCH - Unsupported Parameter (`name` / `identifier`)**: `GET /fhir/AuditEvent?name=foo` or `?identifier=bar` returns HTTP 400 `OperationOutcome` fail-closed.
- **Mutation Rejection**: Reflection test confirms `AuditEventResource` has zero `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
- **Regression Targets**: `ThemisClinicalAuthorizationFilterTest` (security domain classification and `AuditReadPolicy` / `AuditImmutabilityDenyPolicy` enforcement), `KleioRuntimeCompositionTest` (CDI service graph resolution).

**Assumptions & Open Questions**

**Significant Assumptions**
- **Unsupported Legacy Search Parameters**: `name` and `identifier` query parameters on `GET /fhir/AuditEvent` are rejected with HTTP 400 Bad Request `OperationOutcome` rather than ignored or partially matched. Rationale: Audit evidence queries must be exact and fail-closed to avoid misleading clients into believing a complete filtered search was performed. Alternative: silently ignoring unsupported params was rejected as it creates false audit confidence.
- **BEFE Shared Cache Constants**: `FhirCacheService.resolveCacheName` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` are left intact for this step. Rationale: prompt explicitly mandates not touching shared Infinispan/Mnemosyne write-behind infrastructure in Step 04.4; full retirement belongs to Step 04.5.

**Delivery Steps**

**✓ Step 1: Converge AuditEventResource to Read-Only Kleio Access & Add Resource Tests**  
Goal: Refactor `AuditEventResource` to use `AuditService` and `HarmoniaAuditEventMapper` for READ/SEARCH, eliminate mutation endpoints and `FhirCacheService`, update existing integration tests, and add comprehensive `AuditEventResourceTest`.  
Scope:
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
  Acceptance Criteria:
- [ ] `AuditEventResource` injects `AuditService` and `HarmoniaAuditEventMapper`, with zero references to `FhirCacheService`.
- [ ] `read(String id)` delegates to `auditService.findById(id)`, converts result using `auditMapper.toFhir(event)`, returns HTTP 200 with FHIR JSON, and returns HTTP 404 `OperationOutcome` when not found.
- [ ] `search(String id, String name, String identifier)` maps `_id` to `AuditQuery.eventId` and executes `auditService.find(query)`, returning a FHIR `Bundle` with entries preserving canonical ordering.
- [ ] Non-blank `name` or `identifier` query parameters return HTTP 400 `OperationOutcome` with safe diagnostic message.
- [ ] `AuditIntegrityException` and `AuditPersistenceException` are caught and mapped to HTTP 500 `OperationOutcome` with zero PHI/sensitive data leakage.
- [ ] `POST`, `PUT`, `DELETE` methods are completely removed from `AuditEventResource` (and `PATCH` remains absent).
- [ ] `FhirRestResourceIntegrationTest` is updated to remove legacy mutable `AuditEvent` cache assertions.
- [ ] `AuditEventResourceTest` provides complete coverage of READ, SEARCH, 404, 400, 500, and mutation endpoint absence.
  Verification: `mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

*** Step 2: Add Iris AuditEvent Architecture Guardrail & Verify Security Regressions**  
Goal: Enforce architectural boundaries preventing `AuditEventResource` from coupling to cache or persistence implementations, and verify the full Themis security regression suite and architecture suite.  
Scope:
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
  Acceptance Criteria:
- [ ] `IrisDecouplingArchitectureTest` contains a dedicated test asserting `AuditEventResource` depends only on `AuditService` and `HarmoniaAuditEventMapper`, and forbids dependencies on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.
- [ ] All 50+ Themis authorization tests in `ThemisClinicalAuthorizationFilterTest` remain 100% green (AUDIT domain classification, `audit.read` authorization for READ/SEARCH, explicit deny for CREATE/UPDATE/DELETE/PATCH, admin override denial).
- [ ] All architecture tests in `paradeigma-test` pass with zero failures.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Step 1 is done and reviewed. Now finish only the remaining architecture-boundary and verification work for Step 04.4.

Required outcomes:
- Add the smallest useful architecture/decoupling guardrail for the new BEFE AuditEvent boundary, proving `AuditEventResource` depends on the Kleio `AuditService` abstraction and canonical mapper, and does not depend on `FhirCacheService`, Kleio repository/persistence implementation, JDBC/DataSource, PostgreSQL/JPA/Hibernate.
- Keep the guardrail proportional and in the style of the existing architecture tests; avoid brittle source-text noise beyond what the current test suite already uses.
- Verify security regression suites relevant to Step 04.4 remain green, especially `ThemisClinicalAuthorizationFilterTest` and the architecture tests.
- If step 1 already implicitly satisfied part of this, confirm exactly what still needed changing versus pure verification.

Known from previous steps:
- `AuditEventResource` now uses `AuditService` + `HarmoniaAuditEventMapper`, exposes only GET read/search, and has no `FhirCacheService` reference.
- Step 04.3 security remains authoritative: Themis classifies `AuditEvent` as AUDIT and mutation is denied canonically.
- Step 1 review confirmed `IrisDecouplingArchitectureTest` was still untouched, so this step should likely modify only architecture-test coverage plus any narrowly related verification adjustments.
- Narrow verification command already succeeded for `AuditEventResourceTest`, `FhirRestResourceIntegrationTest`, `KleioRuntimeCompositionTest`, and `ThemisClinicalAuthorizationFilterTest`.

Optimize execution speed:
- Start with the architecture test file and the narrowest relevant test commands.
- Use short command timeouts first and widen only if necessary.
- Report exact commands run and any environment blockers.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

Step 2 has been completed successfully:
1. **Implemented Architectural Guardrail**:
    - Added a new test method `auditEventResourceMustNotDependOnCacheOrPersistence` to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`.
    - This test verifies that `AuditEventResource.java` imports no forbidden cache/persistence dependencies (`FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, `org.hibernate.*`) while correctly importing `AuditService` and `HarmoniaAuditEventMapper`.

2. **Verification**:
    - Executed the full architecture suite: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → **55 tests run, 0 failures**.
    - Executed the security regression suite: `mvn test -pl iris/iris-befe -am -Dtest="ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` → **50 tests run, 0 failures**.

All requirements for Step 04.4 have been met.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Review only the step-2 scope: the new architecture guardrail plus regression verification around it. Focus on whether the added `IrisDecouplingArchitectureTest` assertion is proportional, accurately enforces the intended boundary for `AuditEventResource`, and avoids over-brittle or misleading checks. Also verify the claimed test execution/results if observable and note any residual concerns.



Implementation Approved

**Verification**
- Inspected the sole change: new `auditEventResourceMustNotDependOnCacheOrPersistence` test in `IrisDecouplingArchitectureTest.java` (lines 160–190). It is a source-text token check on `AuditEventResource.java`, consistent with the file's existing static-source-check style (the file already uses `Files.readString` + `doesNotContain` for its other rules), so the guardrail is proportional and not out of place.
- Confirmed the assertion accurately enforces the intended boundary: forbids `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql`, `jakarta.persistence`, `org.hibernate`, and requires `AuditService` + `HarmoniaAuditEventMapper`.
- Independently verified `AuditEventResource.java` actually satisfies the guardrail: no forbidden tokens present, both required tokens imported (lines 27–30). The check is not a false-pass.
- Ran full architecture suite `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` → 55 tests, 0 failures (IrisDecouplingArchitectureTest now 5 tests). ~build success.
- Ran `mvn test -pl iris/iris-befe -am -Dtest="ThemisClinicalAuthorizationFilterTest"` → 50 tests, 0 failures. Security regression intact.

**Issues**  
None.

**Notes**
- The guardrail is a substring text check (like the rest of the file), so tokens such as `AuditService`/`HarmoniaAuditEventMapper` would also match if only present in a comment. This is acceptable given the established convention in this file and the real imports back it up; not worth a stricter ArchUnit rule for a single presentation-tier class.
- `java.sql` and `DataSource` are broad substrings but appropriate for a deny-list boundary; low false-positive risk for this resource.

**Discoveries for future steps**
- `IrisDecouplingArchitectureTest` alone runs in ~0.26s; full `*ArchitectureTest` suite compiles+runs in well under the 700s timeout (effective ~1–2 min including build). ThemisClinicalAuthorizationFilterTest ~2s.



**Requirements**

**Goal**  
Converge the Iris BEFE FHIR `AuditEvent` REST resource (`AuditEventResource.java`) from the legacy mutable Infinispan cache path onto the canonical, append-only Kleio audit capability (`AuditService` + `HarmoniaAuditEventMapper`), exposing a thin, read-only FHIR R5 view over immutable audit evidence.

**Scope**
- **In Scope:**
    - Refactoring `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` to inject `AuditService` and `HarmoniaAuditEventMapper` via CDI.
    - Implementing `GET /fhir/AuditEvent/{id}` (READ) via `AuditService.findById(id)` with canonical FHIR R5 mapping.
    - Implementing `GET /fhir/AuditEvent` (SEARCH) via `AuditService.find(AuditQuery)`, mapping `_id` to `AuditQuery.eventId` and rejecting unsupported legacy parameters (`name`, `identifier`) fail-closed with HTTP 400 `OperationOutcome`.
    - Handling not-found (HTTP 404 `OperationOutcome`) and integrity/persistence errors (HTTP 500 `OperationOutcome`) safely with zero PHI / internal error leakage.
    - Removing external mutation methods (`POST`, `PUT`, `DELETE`) from `AuditEventResource` and verifying `PATCH` remains absent.
    - Removing `FhirCacheService` dependency and coupling from `AuditEventResource`.
    - Updating `FhirRestResourceIntegrationTest` to remove legacy mutable `AuditEvent` cache assertions.
    - Adding a dedicated `AuditEventResourceTest` covering read, search, fail-closed validation, error mapping, and mutation method absence.
    - Adding an architectural boundary test in `IrisDecouplingArchitectureTest` verifying `AuditEventResource` dependencies.
- **Out of Scope (Deferred to Step 04.5 or later):**
    - Mnemosyne direct `AuditEventResourceProvider` mutation endpoints and `FhirStorageService` (Step 04.5).
    - Shared cluster Infinispan `auditevent-cache` configuration (`infinispan.xml`, `FhirRestCacheStoreTest`, `OperationsAggregatorService`).
    - Database schema, permissions, triggers, WORM hardening, or dedicated DB relocation (Tasks 07–10).
    - Frontend UI SPAs (`AuditEventView.vue`, `securityStore.ts`).
    - Creating synthetic audit producer callers (`AuditService.append(...)` is trusted internal only).

**Done When**
- `AuditEventResource` exposes only `@GET` read and search endpoints backed exclusively by `AuditService` and `HarmoniaAuditEventMapper`.
- No `POST`, `PUT`, `DELETE`, or `PATCH` methods exist on `AuditEventResource`.
- `FhirCacheService` is no longer injected or referenced in `AuditEventResource`.
- Unsupported query parameters (`name`, `identifier`) fail closed with HTTP 400 `OperationOutcome`.
- Dedicated `AuditEventResourceTest`, updated `FhirRestResourceIntegrationTest`, `ThemisClinicalAuthorizationFilterTest`, `KleioRuntimeCompositionTest`, and `*ArchitectureTest` pass cleanly.

**Technical Design**

**Decisions**
- **Inject `AuditService` & `HarmoniaAuditEventMapper` / not persistence classes or `FhirCacheService`**: Iris BEFE is presentation-only and must depend strictly on Kleio's service and mapper abstractions, preserving the CDI runtime composition verified in `KleioRuntimeCompositionTest`.
- **Fail-closed on unsupported search parameters (`name`, `identifier`) with HTTP 400 / not silent approximation**: `AuditQuery` only supports canonical fields (`eventId`, `recordedAt`, etc.); `name` (legacy cache substring) and `identifier` (unmatched for AuditEvent) cannot be faithfully represented. Approximating would violate audit fidelity.
- **Map `AuditIntegrityException` and `AuditPersistenceException` to HTTP 500 `OperationOutcome` / not 404 or empty Bundle**: Tampered, soft-deleted, or corrupted audit records must not appear missing or empty; they must signal a server-side integrity fault with sanitized, zero-PHI diagnostics.
- **Remove `POST`, `PUT`, `DELETE` methods / not retain dead methods**: Removing mutation methods from the JAX-RS resource eliminates dead code while Themis `ThemisClinicalAuthorizationFilter` + `AuditImmutabilityDenyPolicy` continues to enforce default-deny at the security perimeter.

**Approach & Touches**
- **Primary resource:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
    - Replace `@Inject FhirCacheService cacheService` with `@Inject AuditService auditService` and `@Inject HarmoniaAuditEventMapper auditMapper` (with lazy fallback initialization for `auditMapper` and `FhirContext` / `IParser` in non-CDI test environments).
    - `read(String id)`: call `auditService.findById(id)`. If empty -> HTTP 404 `OperationOutcome`. If found -> `auditMapper.toFhir(event)` -> FHIR JSON 200. Catch integrity/persistence/mapping exceptions -> HTTP 500 `OperationOutcome`.
    - `search(String id, String name, String identifier)`: if `name` or `identifier` is non-blank -> return HTTP 400 `OperationOutcome`. Otherwise construct `AuditQuery` (with `eventId(id)` if `id` is present), call `auditService.find(query)`, map results with `auditMapper.toFhir(...)`, assemble into a FHIR `Bundle` (`type = searchset`, total count, entries with `fullUrl`), and return FHIR JSON 200. Catch exceptions -> HTTP 500 `OperationOutcome`.
    - Remove `create(...)`, `update(...)`, and `delete(...)` methods.
- **Integration test update:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
    - Remove `testNewResourceControllers` legacy AuditEvent create/read block (lines 166–173).
- **New resource test:** `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
    - Unit & mock tests verifying READ (found, not found, integrity error, serialization), SEARCH (default query, `_id` query, unsupported `name`/`identifier` 400, ordering preservation, bundle structure), and reflection assertions proving absence of `POST`, `PUT`, `PATCH`, `DELETE` annotations/methods.
- **Architecture guardrail:** `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
    - Add test asserting `AuditEventResource` depends only on `AuditService` / `HarmoniaAuditEventMapper` and does not import or depend on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.

**Nuances, Risks & Corners**
- **Zero-PHI Diagnostic Logging & Error Entities**: Diagnostics in `OperationOutcome` on 400, 404, or 500 must strictly contain static descriptors and IDs without echoing user inputs, query parameters, stack traces, or SQL state.
- **CDI vs Direct Instantiation in Tests**: `AuditEventResource` should allow injection of dependencies or package-private setters / constructor injection so unit tests can test it without spinning up a full EE container.
- **Deterministic Search Ordering**: `AuditService.find(...)` already guarantees canonical `recordedAt DESC, eventId DESC` ordering; `AuditEventResource` must preserve this order when assembling the `Bundle` entries without re-sorting.
- **Legacy BEFE Cache Configuration**: `FhirCacheService.resolveCacheName("auditevent")` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` remain in codebase; they are not touched in this BEFE-only step to avoid breaking shared cluster references, and will be reported for Step 04.5 cleanup.

**Contracts**
- Point Read Response:
    - Success: HTTP 200 OK with FHIR R5 `AuditEvent` JSON (`application/fhir+json`).
    - Not Found: HTTP 404 Not Found with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-found","diagnostics":"AuditEvent/<id> not found"}]}`).
    - Server / Integrity Error: HTTP 500 Internal Server Error with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"exception","diagnostics":"Internal error retrieving AuditEvent"}]}`).
- Search Response:
    - Success: HTTP 200 OK with FHIR R5 `Bundle` JSON (`resourceType: Bundle`, `type: searchset`, `total: N`, `entry: [...]`).
    - Invalid Parameter: HTTP 400 Bad Request with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-supported","diagnostics":"Search parameter not supported for AuditEvent"}]}`).

**Testing**

**Checklist of Scenarios**
- **READ - Found**: `GET /fhir/AuditEvent/{id}` returns HTTP 200 with matching canonical fields mapped to FHIR `AuditEvent`, `id` preserved, `recorded` instant intact.
- **READ - Not Found**: `GET /fhir/AuditEvent/{non-existent}` returns HTTP 404 with standard `OperationOutcome`.
- **READ - Integrity Failure**: `AuditIntegrityException` thrown by service results in HTTP 500 `OperationOutcome` with sanitized diagnostics (no stack trace or DB details).
- **SEARCH - Unfiltered**: `GET /fhir/AuditEvent` returns HTTP 200 `Bundle` (`type=searchset`) containing events in canonical order (`recordedAt DESC, eventId DESC`).
- **SEARCH - By ID**: `GET /fhir/AuditEvent?_id=EVT-1` maps to `AuditQuery.builder().eventId("EVT-1")` and returns HTTP 200 `Bundle`.
- **SEARCH - Unsupported Parameter (`name` / `identifier`)**: `GET /fhir/AuditEvent?name=foo` or `?identifier=bar` returns HTTP 400 `OperationOutcome` fail-closed.
- **Mutation Rejection**: Reflection test confirms `AuditEventResource` has zero `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
- **Regression Targets**: `ThemisClinicalAuthorizationFilterTest` (security domain classification and `AuditReadPolicy` / `AuditImmutabilityDenyPolicy` enforcement), `KleioRuntimeCompositionTest` (CDI service graph resolution).

**Assumptions & Open Questions**

**Significant Assumptions**
- **Unsupported Legacy Search Parameters**: `name` and `identifier` query parameters on `GET /fhir/AuditEvent` are rejected with HTTP 400 Bad Request `OperationOutcome` rather than ignored or partially matched. Rationale: Audit evidence queries must be exact and fail-closed to avoid misleading clients into believing a complete filtered search was performed. Alternative: silently ignoring unsupported params was rejected as it creates false audit confidence.
- **BEFE Shared Cache Constants**: `FhirCacheService.resolveCacheName` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` are left intact for this step. Rationale: prompt explicitly mandates not touching shared Infinispan/Mnemosyne write-behind infrastructure in Step 04.4; full retirement belongs to Step 04.5.

**Delivery Steps**

**✓ Step 1: Converge AuditEventResource to Read-Only Kleio Access & Add Resource Tests**  
Goal: Refactor `AuditEventResource` to use `AuditService` and `HarmoniaAuditEventMapper` for READ/SEARCH, eliminate mutation endpoints and `FhirCacheService`, update existing integration tests, and add comprehensive `AuditEventResourceTest`.  
Scope:
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java`
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`
  Acceptance Criteria:
- [ ] `AuditEventResource` injects `AuditService` and `HarmoniaAuditEventMapper`, with zero references to `FhirCacheService`.
- [ ] `read(String id)` delegates to `auditService.findById(id)`, converts result using `auditMapper.toFhir(event)`, returns HTTP 200 with FHIR JSON, and returns HTTP 404 `OperationOutcome` when not found.
- [ ] `search(String id, String name, String identifier)` maps `_id` to `AuditQuery.eventId` and executes `auditService.find(query)`, returning a FHIR `Bundle` with entries preserving canonical ordering.
- [ ] Non-blank `name` or `identifier` query parameters return HTTP 400 `OperationOutcome` with safe diagnostic message.
- [ ] `AuditIntegrityException` and `AuditPersistenceException` are caught and mapped to HTTP 500 `OperationOutcome` with zero PHI/sensitive data leakage.
- [ ] `POST`, `PUT`, `DELETE` methods are completely removed from `AuditEventResource` (and `PATCH` remains absent).
- [ ] `FhirRestResourceIntegrationTest` is updated to remove legacy mutable `AuditEvent` cache assertions.
- [ ] `AuditEventResourceTest` provides complete coverage of READ, SEARCH, 404, 400, 500, and mutation endpoint absence.
  Verification: `mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**✓ Step 2: Add Iris AuditEvent Architecture Guardrail & Verify Security Regressions**  
Goal: Enforce architectural boundaries preventing `AuditEventResource` from coupling to cache or persistence implementations, and verify the full Themis security regression suite and architecture suite.  
Scope:
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
  Acceptance Criteria:
- [ ] `IrisDecouplingArchitectureTest` contains a dedicated test asserting `AuditEventResource` depends only on `AuditService` and `HarmoniaAuditEventMapper`, and forbids dependencies on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.
- [ ] All 50+ Themis authorization tests in `ThemisClinicalAuthorizationFilterTest` remain 100% green (AUDIT domain classification, `audit.read` authorization for READ/SEARCH, explicit deny for CREATE/UPDATE/DELETE/PATCH, admin override denial).
- [ ] All architecture tests in `paradeigma-test` pass with zero failures.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Completed Task 06 Step 04.4: Iris BEFE `AuditEventResource` is now a read-only Kleio-backed surface for READ/SEARCH, no longer using the legacy BEFE cache path.
- External mutation surface was removed from the BEFE resource, while Step 04.3 Themis security behavior remained green.

**Changes**
- Refactored `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` to inject `AuditService` + `HarmoniaAuditEventMapper`, implement READ via `findById`, SEARCH via `AuditQuery` (`_id -> eventId`), return 404 for not found, 400 for unsupported `name`/`identifier`, and sanitized 500s for integrity/persistence/mapping failures.
- Removed BEFE `POST`/`PUT`/`DELETE` methods for `AuditEvent` (and `PATCH` remains absent), and removed the resource’s dependency on `FhirCacheService`; updated `FhirRestResourceIntegrationTest` and added focused `AuditEventResourceTest` coverage for read/search/error/mutation-surface behavior.
- Added `auditEventResourceMustNotDependOnCacheOrPersistence` to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java` to guard the boundary: allow `AuditService`/mapper, forbid cache/persistence/JDBC/JPA dependencies.

**Verification**
- Passed: `mvn test -pl iris/iris-befe -am -Dtest="AuditEventResourceTest,FhirRestResourceIntegrationTest,KleioRuntimeCompositionTest,ThemisClinicalAuthorizationFilterTest,FhirCacheServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` and `mvn test -pl iris/iris-befe -am -Dtest="ThemisClinicalAuthorizationFilterTest" -Dsurefire.failIfNoSpecifiedTests=false`.
- Passed: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="IrisDecouplingArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` and `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.

**Notes**
- Remaining BEFE/shared AuditEvent cache artifacts (for example `FhirCacheService.resolveCacheName("AuditEvent")`, `OperationsAggregatorService.CACHE_AUDIT_EVENT`, shared Infinispan/Mnemosyne config) were intentionally left for later cleanup; direct Mnemosyne AuditEvent endpoint changes remain deferred to Step 04.5.

