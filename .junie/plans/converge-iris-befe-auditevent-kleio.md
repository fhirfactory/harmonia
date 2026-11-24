---
sessionId: session-260924-202431-1h87
---

# Requirements

### Goal
Converge the Iris BEFE FHIR `AuditEvent` REST resource (`AuditEventResource.java`) from the legacy mutable Infinispan cache path onto the canonical, append-only Kleio audit capability (`AuditService` + `HarmoniaAuditEventMapper`), exposing a thin, read-only FHIR R5 view over immutable audit evidence.

### Scope
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

### Done When
- `AuditEventResource` exposes only `@GET` read and search endpoints backed exclusively by `AuditService` and `HarmoniaAuditEventMapper`.
- No `POST`, `PUT`, `DELETE`, or `PATCH` methods exist on `AuditEventResource`.
- `FhirCacheService` is no longer injected or referenced in `AuditEventResource`.
- Unsupported query parameters (`name`, `identifier`) fail closed with HTTP 400 `OperationOutcome`.
- Dedicated `AuditEventResourceTest`, updated `FhirRestResourceIntegrationTest`, `ThemisClinicalAuthorizationFilterTest`, `KleioRuntimeCompositionTest`, and `*ArchitectureTest` pass cleanly.

# Technical Design

### Decisions
- **Inject `AuditService` & `HarmoniaAuditEventMapper` / not persistence classes or `FhirCacheService`**: Iris BEFE is presentation-only and must depend strictly on Kleio's service and mapper abstractions, preserving the CDI runtime composition verified in `KleioRuntimeCompositionTest`.
- **Fail-closed on unsupported search parameters (`name`, `identifier`) with HTTP 400 / not silent approximation**: `AuditQuery` only supports canonical fields (`eventId`, `recordedAt`, etc.); `name` (legacy cache substring) and `identifier` (unmatched for AuditEvent) cannot be faithfully represented. Approximating would violate audit fidelity.
- **Map `AuditIntegrityException` and `AuditPersistenceException` to HTTP 500 `OperationOutcome` / not 404 or empty Bundle**: Tampered, soft-deleted, or corrupted audit records must not appear missing or empty; they must signal a server-side integrity fault with sanitized, zero-PHI diagnostics.
- **Remove `POST`, `PUT`, `DELETE` methods / not retain dead methods**: Removing mutation methods from the JAX-RS resource eliminates dead code while Themis `ThemisClinicalAuthorizationFilter` + `AuditImmutabilityDenyPolicy` continues to enforce default-deny at the security perimeter.

### Approach & Touches
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

### Nuances, Risks & Corners
- **Zero-PHI Diagnostic Logging & Error Entities**: Diagnostics in `OperationOutcome` on 400, 404, or 500 must strictly contain static descriptors and IDs without echoing user inputs, query parameters, stack traces, or SQL state.
- **CDI vs Direct Instantiation in Tests**: `AuditEventResource` should allow injection of dependencies or package-private setters / constructor injection so unit tests can test it without spinning up a full EE container.
- **Deterministic Search Ordering**: `AuditService.find(...)` already guarantees canonical `recordedAt DESC, eventId DESC` ordering; `AuditEventResource` must preserve this order when assembling the `Bundle` entries without re-sorting.
- **Legacy BEFE Cache Configuration**: `FhirCacheService.resolveCacheName("auditevent")` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` remain in codebase; they are not touched in this BEFE-only step to avoid breaking shared cluster references, and will be reported for Step 04.5 cleanup.

### Contracts
- Point Read Response:
  - Success: HTTP 200 OK with FHIR R5 `AuditEvent` JSON (`application/fhir+json`).
  - Not Found: HTTP 404 Not Found with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-found","diagnostics":"AuditEvent/<id> not found"}]}`).
  - Server / Integrity Error: HTTP 500 Internal Server Error with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"exception","diagnostics":"Internal error retrieving AuditEvent"}]}`).
- Search Response:
  - Success: HTTP 200 OK with FHIR R5 `Bundle` JSON (`resourceType: Bundle`, `type: searchset`, `total: N`, `entry: [...]`).
  - Invalid Parameter: HTTP 400 Bad Request with FHIR R5 `OperationOutcome` JSON (`{"resourceType":"OperationOutcome","issue":[{"severity":"error","code":"not-supported","diagnostics":"Search parameter not supported for AuditEvent"}]}`).

# Testing

### Checklist of Scenarios
- **READ - Found**: `GET /fhir/AuditEvent/{id}` returns HTTP 200 with matching canonical fields mapped to FHIR `AuditEvent`, `id` preserved, `recorded` instant intact.
- **READ - Not Found**: `GET /fhir/AuditEvent/{non-existent}` returns HTTP 404 with standard `OperationOutcome`.
- **READ - Integrity Failure**: `AuditIntegrityException` thrown by service results in HTTP 500 `OperationOutcome` with sanitized diagnostics (no stack trace or DB details).
- **SEARCH - Unfiltered**: `GET /fhir/AuditEvent` returns HTTP 200 `Bundle` (`type=searchset`) containing events in canonical order (`recordedAt DESC, eventId DESC`).
- **SEARCH - By ID**: `GET /fhir/AuditEvent?_id=EVT-1` maps to `AuditQuery.builder().eventId("EVT-1")` and returns HTTP 200 `Bundle`.
- **SEARCH - Unsupported Parameter (`name` / `identifier`)**: `GET /fhir/AuditEvent?name=foo` or `?identifier=bar` returns HTTP 400 `OperationOutcome` fail-closed.
- **Mutation Rejection**: Reflection test confirms `AuditEventResource` has zero `@POST`, `@PUT`, `@PATCH`, or `@DELETE` methods.
- **Regression Targets**: `ThemisClinicalAuthorizationFilterTest` (security domain classification and `AuditReadPolicy` / `AuditImmutabilityDenyPolicy` enforcement), `KleioRuntimeCompositionTest` (CDI service graph resolution).

# Assumptions & Open Questions

### Significant Assumptions
- **Unsupported Legacy Search Parameters**: `name` and `identifier` query parameters on `GET /fhir/AuditEvent` are rejected with HTTP 400 Bad Request `OperationOutcome` rather than ignored or partially matched. Rationale: Audit evidence queries must be exact and fail-closed to avoid misleading clients into believing a complete filtered search was performed. Alternative: silently ignoring unsupported params was rejected as it creates false audit confidence.
- **BEFE Shared Cache Constants**: `FhirCacheService.resolveCacheName` and `OperationsAggregatorService.CACHE_AUDIT_EVENT` are left intact for this step. Rationale: prompt explicitly mandates not touching shared Infinispan/Mnemosyne write-behind infrastructure in Step 04.4; full retirement belongs to Step 04.5.

# Delivery Steps

### ✓ Step 1: Converge AuditEventResource to Read-Only Kleio Access & Add Resource Tests
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

### ✓ Step 2: Add Iris AuditEvent Architecture Guardrail & Verify Security Regressions
Goal: Enforce architectural boundaries preventing `AuditEventResource` from coupling to cache or persistence implementations, and verify the full Themis security regression suite and architecture suite.
Scope:
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java`
Acceptance Criteria:
- [ ] `IrisDecouplingArchitectureTest` contains a dedicated test asserting `AuditEventResource` depends only on `AuditService` and `HarmoniaAuditEventMapper`, and forbids dependencies on `FhirCacheService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `DataSource`, `java.sql.*`, `jakarta.persistence.*`, or `org.hibernate.*`.
- [ ] All 50+ Themis authorization tests in `ThemisClinicalAuthorizationFilterTest` remain 100% green (AUDIT domain classification, `audit.read` authorization for READ/SEARCH, explicit deny for CREATE/UPDATE/DELETE/PATCH, admin override denial).
- [ ] All architecture tests in `paradeigma-test` pass with zero failures.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green