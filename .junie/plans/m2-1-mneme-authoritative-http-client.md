---
sessionId: session-260930-032147-1dzg
---

# Requirements

### Overview & Goals
Milestone M1 established and validated a stable, reproducible Docker runtime baseline. In accordance with the repository authority hierarchy and the master convergence roadmap (`docs/implementation/harmonia-convergence-runtime-integration-plan.md`), the current milestone is **M2 — Distributed Authoritative Path**, and the current and only authorized implementation step is **M2.1 — Mneme authoritative HTTP client**.

The goal of M2.1 is to construct a client-side HTTP adapter in Mneme (`hestia/mneme-persistence`) implementing `AuthoritativePersistencePort<IBaseResource>` that interacts synchronously with the dedicated internal authoritative HTTP API boundary in Mnemosyne (`/api/authoritative/fhir/*`). This client acts as the distributed bridge allowing Mneme to invoke authoritative point `READ`, `CREATE-if-absent`, and `UPDATE-if-expected-predecessor` operations with exact Harmonia semantics (`Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown`) without exposing raw HTTP transport semantics, bypassing the authoritative boundary, or violating architectural invariants.

### Scope
- **In Scope (M2.1)**:
  - Definition and placement of `MnemeAuthoritativeHttpClient` implementing `AuthoritativePersistencePort<IBaseResource>` in `hestia/mneme-persistence`.
  - Shared contract extraction into a lightweight `hestia/mnemosyne-api` module (containing `AuthoritativePersistencePort` and `AuthoritativePersistenceResult`), allowing both `mneme-persistence` and `mnemosyne-clinical` to share the interfaces without leaking Spring Data JPA/Hibernate/PostgreSQL into Mneme.
  - Wire-contract client mapping for the dedicated internal authoritative HTTP API:
    - Authoritative `READ`: `GET /api/authoritative/fhir/{resourceType}/{id}`
    - Authoritative `CREATE-if-absent`: `PUT /api/authoritative/fhir/{resourceType}/{id}` with `If-None-Match: *`
    - Authoritative `UPDATE-if-expected-predecessor`: `PUT /api/authoritative/fhir/{resourceType}/{id}` with `If-Match: W/"{expectedVersion}"`
  - Explicit semantic outcome classification: `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown`.
  - Conservative transport uncertainty classification: `NotCommitted` returned only when it is positively established that the request could not have reached Mnemosyne; all ambiguous in-flight, socket-reuse, read-timeout, or mid-stream IO failures map to `OutcomeUnknown`.
  - Zero blind retry enforcement on mutating operations (`CREATE`, `UPDATE`).
  - Strict authoritative-version transport mapping (`ETag` <-> `AuthoritativeVersion` / `ExpectedAuthoritativeVersion`) maintaining domain separation without fallbacks to `meta.versionId` or cache tokens.
  - Endpoint and timeout configuration via environment variables / properties without hardcoding localhost or developer workstation IPs.
  - Unit, WireMock, and architectural invariant tests.

- **Out of Scope (Deferred to M2.2, M2.3, and Later Steps)**:
  - M2.2: Mnemosyne container deployment and distributed network topology wiring in Docker Compose.
  - M2.3: Deployment-level authenticated service identity mapping to trusted Mneme service identity and subsequent Themis authorization.
  - M3: Governed access orchestrators (`DefaultGovernedReader`, `DefaultGovernedWriter`) and cache convergence loop integration.
  - M4: Application migration (Iris, Pylai, Agora, Energeia).
  - M5: Authoritative search operations.
  - M6: MicroK8s runtime provisioning.
  - Physical DELETE operations (strictly forbidden by ADR-020 and Invariant 8).
  - Removal of transitional Infinispan cache store SPI mechanisms (`FhirRestCacheStore`, `OperationsRestCacheStore`).
  - Unrelated MAT findings remediation.

### User Stories
- **As an Integration Architect**, I want Mneme to communicate with Mnemosyne over a clean `AuthoritativePersistencePort` HTTP client adapter consuming the internal authoritative endpoint (`/api/authoritative/fhir/*`) so that distributed authoritative state operations are decoupled from local in-memory caching and JPA persistence implementations (AX-05 / Invariant 8).
- **As a Distributed Systems Engineer**, I want the HTTP client to fail conservatively to `OutcomeUnknown` on any transport uncertainty and avoid transparent retries so that double-commits or duplicate resource creations are impossible in distributed edge cases (AX-01, AX-05).
- **As a Security Engineer**, I want the client to fail closed on unauthenticated or unauthorized transport responses without inventing unvetted client-side auth tokens or bypass headers before M2.3 is reached (AX-07 / Invariant 6).

### Functional Requirements
- **FR-1 (Port Implementation)**: The client must implement `AuthoritativePersistencePort<IBaseResource>` with methods `read(ResourceKey)`, `create(ResourceKey, T)`, and `update(ResourceKey, T, ExpectedAuthoritativeVersion)`.
- **FR-2 (No DELETE or Search)**: The client must not expose or implement physical `delete` or `search` operations.
- **FR-3 (Strict Precondition Enforcement)**:
  - `CREATE` must supply `If-None-Match: *` to enforce create-if-absent semantics.
  - `UPDATE` must supply `If-Match: W/"{expectedVersion}"` to enforce optimistic version locking against the expected predecessor.
- **FR-4 (Semantic Wire Mapping)**:
  - HTTP `200 OK` / `201 Created` with valid body and valid `ETag` -> `AuthoritativePersistenceResult.Committed`.
  - HTTP `412 Precondition Failed` / `409 Conflict` / `428 Precondition Required` -> `AuthoritativePersistenceResult.Conflict`.
  - HTTP `400 Bad Request` / `404 Not Found` / `410 Gone` -> `AuthoritativePersistenceResult.NotCommitted`.
  - HTTP `401 Unauthorized` / `403 Forbidden` -> `AuthoritativePersistenceResult.NotCommitted` (fail-closed).
  - Missing/malformed `ETag` or HTTP `5xx` -> `AuthoritativePersistenceResult.OutcomeUnknown`.
- **FR-5 (Conservative Transport Uncertainty)**:
  - `NotCommitted` is returned only when failure is positively established to have occurred before request transmission (e.g. local argument error, DNS resolution failure before connection).
  - Any connection reset, request timeout, stream abort, socket reuse error, or ambiguous failure maps conservatively to `OutcomeUnknown`.
- **FR-6 (No Transparent Retry)**: Mutating requests (`CREATE`, `UPDATE`) must never be transparently retried by the HTTP client engine.
- **FR-7 (Version Domain Separation)**: `ETag` transport representation is decoded to `AuthoritativeVersion`; `ETag` is not conflated with active-state cache tokens or inferred from `meta.versionId`.

# Technical Design

### 1. Current-State Findings & Architectural Baseline
Investigation of the Harmonia codebase establishes the following baseline:
1. **Server-Side Authoritative Path**: The authoritative persistence boundary is implemented in `hestia/mnemosyne-clinical` via HAPI FHIR JPA (`HapiJpaAuthoritativePersistenceAdapter.java` backed by `IFhirResourceDao<?>` and PostgreSQL). It exposes atomic point `read`, `create` (with `If-None-Match: *`), and `update` (with `If-Match: W/"{version}"`).
2. **Dedicated Internal Authoritative HTTP Boundary**: The single architectural path for M2.1 is:
   ``` text
   MnemeAuthoritativeHttpClient
     → internal authoritative HTTP API (/api/authoritative/fhir/*)
     → Step 3.3 Mnemosyne authoritative controller
     → AuthoritativePersistencePort
     → HapiJpaAuthoritativePersistenceAdapter
     → HAPI DAO/JPA
     → PostgreSQL
   ```
   This path is strictly distinct from any public/gateway HAPI FHIR server (`JpaRestfulServer` at `/fhir/*`).
3. **Persistence Port Placement & Maven Dependency Structure**:
   - `AuthoritativePersistencePort<T extends IBaseResource>` and `AuthoritativePersistenceResult<T>` currently reside in `hestia/mnemosyne-clinical`.
   - `hestia/mnemosyne-clinical` contains heavy implementation dependencies: Spring Boot Web/Data JPA, Hibernate, and PostgreSQL driver.
   - `hestia/mneme-persistence` must NOT depend on `hestia/mnemosyne-clinical` as that would leak server-side JPA/database infrastructure into the Mneme cache tier (violating Invariant 3/8 and AX-05).
   - Moving persistence capability contracts to `calliope` would violate Calliope's architectural purity (Calliope owns pure domain models, not subsystem persistence SPIs per ADR-021/022).
   - **Resolution**: Extract `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` into a lightweight shared API submodule `hestia/mnemosyne-api` within Hestia. It depends only on `calliope` and HAPI structures (`hapi-fhir-base`, `hapi-fhir-structures-r5`). Both `mneme-persistence` and `mnemosyne-clinical` depend cleanly on `mnemosyne-api`.
4. **Canonical Domain Models in Calliope**: `calliope` contains foundational domain models (`AuthoritativeVersion`, `ExpectedAuthoritativeVersion`, `AuthoritativeCommitOutcome`, `AuthoritativePreconditionConflict`, `PreconditionFailureReason`, `ResourceKey`, `WriteResult`, `GovernedReader`, `GovernedWriter`).

---

### 2. Relevant Existing Classes and Interfaces & Shared-Contract Placement

| Module | Package | Class / Interface | Current Responsibility | Change for M2.1 |
| :--- | :--- | :--- | :--- | :--- |
| `calliope` | `net.fhirfactory.harmonia.model.governedwrite` | `AuthoritativeVersion` | Immutable wrapper for authoritative version identifier (`1`, `2`, ...) | **No change** (use as-is) |
| `calliope` | `net.fhirfactory.harmonia.model.governedwrite` | `ExpectedAuthoritativeVersion` | Expected version precondition (`SPECIFIC`, `ANY_EXISTING`, `NONE`) | **No change** (use as-is) |
| `calliope` | `net.fhirfactory.harmonia.model.governedwrite` | `AuthoritativeCommitOutcome` | Enum (`COMMITTED`, `NOT_COMMITTED`, `UNKNOWN`) | **No change** (use as-is) |
| `calliope` | `net.fhirfactory.harmonia.model.governedwrite` | `AuthoritativePreconditionConflict` | DTO capturing key, reason, expected vs actual version | **No change** (use as-is) |
| `calliope` | `net.fhirfactory.harmonia.model.governedwrite` | `ResourceKey` | Canonical resource key (`resourceType` + `id`) | **No change** (use as-is) |
| `hestia/mnemosyne-api` *(new submodule)* | `net.fhirfactory.harmonia.hapifhir.persistence` | `AuthoritativePersistencePort` | Internal persistence capability interface for READ, CREATE, UPDATE | **Move to `mnemosyne-api`**; shared cleanly by Mneme and Mnemosyne |
| `hestia/mnemosyne-api` *(new submodule)* | `net.fhirfactory.harmonia.hapifhir.persistence.model` | `AuthoritativePersistenceResult` | Sealed result hierarchy (`Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown`) | **Move to `mnemosyne-api`**; shared cleanly by Mneme and Mnemosyne |
| `hestia/mnemosyne-clinical` | `net.fhirfactory.harmonia.hapifhir.persistence` | `HapiJpaAuthoritativePersistenceAdapter` | Server-side HAPI JPA implementation of `AuthoritativePersistencePort` | **Reference as server baseline**; update dependency to `mnemosyne-api` |
| `hestia/mneme-persistence` | `net.fhirfactory.harmonia.persistence.client` | `HapiFhirRestClient` | Legacy transitional HTTP client for Infinispan SPI cache stores | **Retain unmodified** for legacy SPI |
| `hestia/mneme-persistence` | `net.fhirfactory.harmonia.persistence.client` | `MnemeAuthoritativeHttpClient` *(new)* | Production HTTP client implementing `AuthoritativePersistencePort<IBaseResource>` | **Add in M2.1** |
| `hestia/mneme-persistence` | `net.fhirfactory.harmonia.persistence.config` | `MnemeAuthoritativeClientConfig` *(new)* | Configuration holder for endpoint URL, connect timeout, request timeout | **Add in M2.1** |

---

### 3. Dedicated Internal Authoritative HTTP Wire Contract (Step 3.3)

The dedicated internal authoritative HTTP API on Mnemosyne operates under base path `/api/authoritative/fhir`:

#### Authoritative-Version Transport Representation
- **Normative Header**: `ETag` is the normative transport header for authoritative version responses (`ETag: W/"{versionId}"` or `ETag: "{versionId}"`).
- **Precondition Headers**: Requests use `If-None-Match: *` for CREATE-if-absent and `If-Match: W/"{expectedVersion}"` for UPDATE-if-expected-predecessor.
- **Diagnostic Headers**: If the server also emits `X-Harmonia-Authoritative-Version`, `ETag` remains the normative contract for version negotiation and parsing.
- **Version Mapping**: The client extracts the version string from `ETag` (stripping any `W/` prefix and enclosing quotes) to construct `AuthoritativeVersion.of(...)`.
- **Strict Domain Rule**: There is NO fallback to `meta.versionId` or Mneme active-state tokens if `ETag` is missing. Missing or malformed `ETag` maps strictly to `OutcomeUnknown`.

#### A. Authoritative Point READ
- **URI**: `/api/authoritative/fhir/{resourceType}/{id}`
- **HTTP Method**: `GET`
- **Request Headers**:
  - `Accept`: `application/fhir+json, application/json`
- **Request Body**: None
- **Response Headers**:
  - `ETag`: `W/"{versionId}"` (required on 200 OK)
  - `Content-Type`: `application/fhir+json; charset=UTF-8`
- **Response Body**: Deserialized FHIR Resource JSON (R5)
- **Status Mappings**:
  - `200 OK` (with valid body and `ETag`) -> `AuthoritativePersistenceResult.Committed(resource, AuthoritativeVersion.of(version))`
  - `404 Not Found` / `410 Gone` -> `AuthoritativePersistenceResult.NotCommitted("Resource not found")`
  - `400 Bad Request` -> `AuthoritativePersistenceResult.NotCommitted("Malformed request")`
  - `401 Unauthorized` / `403 Forbidden` -> `AuthoritativePersistenceResult.NotCommitted("Authentication/authorization denied (fail-closed)")`
  - Missing or malformed `ETag` -> `AuthoritativePersistenceResult.OutcomeUnknown("Missing or malformed authoritative version header in 200 response")`
  - `500 / 502 / 503 / 504` or unparseable response -> `AuthoritativePersistenceResult.OutcomeUnknown`

#### B. Authoritative CREATE (CREATE-if-absent)
- **URI**: `/api/authoritative/fhir/{resourceType}/{id}`
- **HTTP Method**: `PUT`
- **Request Headers**:
  - `Content-Type`: `application/fhir+json; charset=UTF-8`
  - `Accept`: `application/fhir+json, application/json`
  - `If-None-Match`: `*` *(enforces create-if-absent)*
- **Request Body**: Proposed FHIR Resource JSON
- **Response Headers**:
  - `ETag`: `W/"1"` (initial authoritative version)
- **Response Body**: Persisted FHIR Resource JSON
- **Status Mappings**:
  - `201 Created` / `200 OK` (with valid body and `ETag`) -> `AuthoritativePersistenceResult.Committed(persistedResource, AuthoritativeVersion.of(v))`
  - `412 Precondition Failed` / `409 Conflict` -> `AuthoritativePersistenceResult.Conflict(AuthoritativePreconditionConflict.resourceAlreadyExists(key, currentVer))`
  - `400 Bad Request` / `422 Unprocessable` -> `AuthoritativePersistenceResult.NotCommitted("Malformed payload / validation failed")`
  - `401 Unauthorized` / `403 Forbidden` -> `AuthoritativePersistenceResult.NotCommitted("Security context rejected (fail-closed)")`
  - Missing or malformed `ETag` -> `AuthoritativePersistenceResult.OutcomeUnknown("Missing or malformed authoritative version in CREATE response")`
  - `5xx` / timeout / indeterminate -> `AuthoritativePersistenceResult.OutcomeUnknown`

#### C. Authoritative UPDATE (UPDATE-if-expected-predecessor)
- **URI**: `/api/authoritative/fhir/{resourceType}/{id}`
- **HTTP Method**: `PUT`
- **Request Headers**:
  - `Content-Type`: `application/fhir+json; charset=UTF-8`
  - `Accept`: `application/fhir+json, application/json`
  - `If-Match`: `W/"{expectedVersion}"`
- **Request Body**: Proposed updated FHIR Resource JSON
- **Response Headers**:
  - `ETag`: `W/"{nextVersion}"`
- **Response Body**: Persisted updated FHIR Resource JSON
- **Status Mappings**:
  - `200 OK` (with valid body and `ETag`) -> `AuthoritativePersistenceResult.Committed(updatedResource, AuthoritativeVersion.of(nextV))`
  - `412 Precondition Failed` / `409 Conflict` -> `AuthoritativePersistenceResult.Conflict(AuthoritativePreconditionConflict.expectedVersionMismatch(key, expectedVersion, actualVer))`
  - `428 Precondition Required` -> `AuthoritativePersistenceResult.Conflict(AuthoritativePreconditionConflict.expectedVersionMismatch(key, expectedVersion, null, "Precondition required"))`
  - `404 Not Found` -> `AuthoritativePersistenceResult.Conflict(AuthoritativePreconditionConflict.expectedVersionMismatch(key, expectedVersion, null, "Target resource does not exist"))`
  - `400 Bad Request` / `422 Unprocessable` -> `AuthoritativePersistenceResult.NotCommitted("Malformed payload")`
  - `401 Unauthorized` / `403 Forbidden` -> `AuthoritativePersistenceResult.NotCommitted("Security context rejected (fail-closed)")`
  - Missing or malformed `ETag` -> `AuthoritativePersistenceResult.OutcomeUnknown("Missing or malformed authoritative version in UPDATE response")`
  - `5xx` / timeout / indeterminate -> `AuthoritativePersistenceResult.OutcomeUnknown`

---

### 4. Proposed Client Placement & Class Structure
The client is placed in `hestia/mneme-persistence` under `net.fhirfactory.harmonia.persistence.client`:

```
hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/
├── client/
│   ├── MnemeAuthoritativeHttpClient.java       // Implements AuthoritativePersistencePort<IBaseResource>
│   └── HttpTransportFailureClassifier.java    // Conservative transport uncertainty discriminator
└── config/
    └── MnemeAuthoritativeClientConfig.java    // Endpoint URL, connect/request timeouts
```

**Class Responsibilities**:
- `MnemeAuthoritativeHttpClient`:
  - Uses `java.net.http.HttpClient` configured with explicit connection timeout and request timeout.
  - Zero automatic retries configured or allowed.
  - Serializes and deserializes FHIR resources using HAPI FHIR `FhirContext.forR5().newJsonParser()`.
  - Translates `ResourceKey` and `ExpectedAuthoritativeVersion` to internal HTTP URI and headers (`If-None-Match: *`, `If-Match: W/"{version}"`).
  - Translates HTTP responses and transport outcomes to sealed `AuthoritativePersistenceResult<IBaseResource>`.

---

### 5. Semantic Transport-Failure Mapping & Uncertainty

#### Governing Semantic Rule
> **`NotCommitted` may be returned ONLY when it is positively established that the authoritative request could not have reached Mnemosyne. If transmission or execution is uncertain, return `OutcomeUnknown`.**

#### Transport Failure Classification
1. **Positively Established Pre-Transmission Failures (`NotCommitted`)**:
   - Client-side validation failure (e.g. invalid URI, null resource key, missing body before dispatch).
   - `java.net.UnknownHostException`: DNS resolution failed prior to socket creation.
   - `java.net.ConnectException` on an uninitiated fresh connection before any request bytes or headers are dispatched.
   - In these strictly bounded cases, zero request bytes were transmitted to the network.

2. **Conservative Transport Uncertainty (`OutcomeUnknown`)**:
   - `java.net.http.HttpTimeoutException` (Request / Read timeout): Connection was open, request bytes were sent or in-flight, response not received within timeout.
   - `java.net.SocketException` / `IOException` (connection reset by peer, broken pipe, socket closed, connection reuse reset): Occurred during or after request transmission.
   - `java.io.EOFException` / stream truncation: Server or intermediary closed stream mid-flight.
   - Any HTTP `5xx` response status code (`500`, `502`, `503`, `504`).
   - Unparseable HTTP response body or missing `ETag` on 200/201.
   - Because the request may have reached Mnemosyne and committed durable state in PostgreSQL, the client MUST fail conservatively to `OutcomeUnknown`.

#### Blind Retry Prevention
- The HTTP client engine is configured with no automatic retries.
- Client implementation contains no catch-and-retry loops for mutating requests (`CREATE`, `UPDATE`).

---

### 6. Authoritative-Version Mapping & Strict Domain Separation
Harmonia maintains strict domain separation between version representations (Axioms AX-01, AX-05, AX-13):
1. **FHIR `meta.versionId`**: Business specification version in resource payload.
2. **HTTP `ETag`**: Transport-layer caching and precondition header (`W/"1"`, `"1"`).
3. **Mneme Active-State Token**: Infinispan cache entry version/token for cluster coordination.
4. **Mnemosyne `AuthoritativeVersion`**: Durable relational transaction truth in PostgreSQL.

**Explicit Mapping Rules**:
- Client maps `ExpectedAuthoritativeVersion` -> HTTP `If-Match`:
  - `ExpectedAuthoritativeVersion.of("2")` -> `If-Match: W/"2"`
  - `ExpectedAuthoritativeVersion.none()` -> Immediate `Conflict` rejection before network call.
- Client maps HTTP `ETag` -> `AuthoritativeVersion`:
  - Strip weak prefix `W/` and quotes `"`: `W/"2"` -> `AuthoritativeVersion.of("2")`.
- **Strict Separation Guardrail**:
  - A missing or malformed `ETag` transport representation MUST NOT fall back to `meta.versionId` or cache tokens.
  - If `ETag` is missing or malformed on a 200/201 response, the client returns `OutcomeUnknown("Missing or malformed authoritative version in transport response")`.
  - The client SHALL NEVER treat Mneme active-state tokens as `AuthoritativeVersion`.

---

### 7. Security Boundary & M2.3 Deferral
- **Step 3.3 Server Security Baseline**: Dedicated internal authoritative API fails closed on unauthenticated or unauthorized requests.
- **M2.1 Client Responsibility**:
  - The client provides standard extension points for transport security headers/context if configured, but does **NOT** invent custom API keys, shared secrets, caller-controlled user headers, or mock Themis tokens.
  - If server responds with HTTP `401 Unauthorized` or `403 Forbidden`, client maps to `NotCommitted("Security authorization rejected by Mnemosyne")`.
- **Deferred to M2.3**: Authenticated deployment-level service identity mapping to trusted Mneme service identity and subsequent Themis authorization. The concrete transport authentication mechanism is an explicit deployment/architecture decision deferred to M2.3.

---

### 8. Configuration
Configuration parameters for `MnemeAuthoritativeHttpClient`:

| Parameter | Environment Variable | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `mnemosyne.authoritative.url` | `HARMONIA_MNEMOSYNE_AUTHORITATIVE_URL` | `http://mnemosyne-clinical:8080/api/authoritative/fhir` | Base URL of Mnemosyne dedicated internal authoritative endpoint |
| `mnemosyne.connect.timeout.seconds` | `HARMONIA_MNEMOSYNE_CONNECT_TIMEOUT_SEC` | `5` | Socket connect timeout in seconds |
| `mnemosyne.request.timeout.seconds` | `HARMONIA_MNEMOSYNE_REQUEST_TIMEOUT_SEC` | `15` | Total request/response timeout in seconds |

*Constraint*: Configuration must use named container DNS (`mnemosyne-clinical`) or injectable URLs; no hardcoded `localhost` or workstation IP addresses.

---

### 9. Architecture Diagram

``` mermaid
graph TD
    subgraph Mneme Container / Tier 4
        M_APP[Mneme Governed Access] -->|AuthoritativePersistencePort| M_CLIENT[MnemeAuthoritativeHttpClient<br/>hestia/mneme-persistence]
        M_CLIENT -->|HTTP 1.1 Client<br/>No Automatic Retries| TRANSPORT[Java HttpClient]
    end

    subgraph Internal Network: harmonia-network
        TRANSPORT -->|GET /api/authoritative/fhir/Type/id<br/>PUT If-None-Match: *<br/>PUT If-Match: W/ver| S_CTRL[Mnemosyne Authoritative Controller<br/>/api/authoritative/fhir/*]
    end

    subgraph Mnemosyne Container / Tier 2
        S_CTRL -->|AuthoritativePersistencePort| S_ADAPTER[HapiJpaAuthoritativePersistenceAdapter]
        S_ADAPTER --> S_JPA[HAPI FHIR DAO / JPA]
        S_JPA --> S_DB[(PostgreSQL / Durable Truth)]
    end

    classDef client fill:#e1f5fe,stroke:#01579b,stroke-width:2px;
    classDef server fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px;
    class M_CLIENT,TRANSPORT client;
    class S_CTRL,S_ADAPTER,S_JPA,S_DB server;
```

---

### 10. Architecture & Conformance Implications
- **AX-05 / Invariant 8 (State Separation)**: Preserved. Mneme does not touch PostgreSQL/JPA directly; all durable state transitions flow through `AuthoritativePersistencePort` HTTP client.
- **AX-01 / Invariant 2 (Encapsulation)**: Preserved. The client exposes only pure Harmonia domain models (`IBaseResource`, `ResourceKey`, `AuthoritativeVersion`, `AuthoritativePersistenceResult`) and completely encapsulates HTTP transport details.
- **AX-07 / Invariant 6 (Default-Deny Security)**: Preserved. Fails closed on any 401/403.
- **AX-13 / Invariant 9 (Boundary Protection)**: Preserved. External protocols are not leaked; version identifiers are mapped explicitly.
- **ADR-020 (No Physical Delete)**: Preserved. `MnemeAuthoritativeHttpClient` contains zero delete methods.
- **ArchUnit Compliance**: Validated continuously by `MnemosyneAuthoritativePersistenceArchitectureTest` and `PackageLayeringArchitectureTest`.

---

### 11. Stop Conditions & Unresolved Decisions
- **Stop Condition for M2.1**: Once `MnemeAuthoritativeHttpClient` is implemented, unit/WireMock tested, and verified against ArchUnit guardrails, M2.1 is complete.
- **Halt Criteria**: Do not proceed to M2.2 (Docker Compose container deployment of Mnemosyne) or M2.3 (service identity) until M2.1 has been reviewed and approved.

# Testing

### Validation Approach
Testing for M2.1 focuses on unit tests and mock HTTP transport verification using WireMock to simulate all expected status codes, header combinations, payload variations, and network failure modes without requiring live database containers.

### Key Scenarios

#### 1. Authoritative READ Scenarios
- **Scenario 1.1 (200 OK with ETag)**: Server returns 200 with valid FHIR resource and `ETag: W/"1"`.
  - *Expected*: `AuthoritativePersistenceResult.Committed` containing deserialized resource and `AuthoritativeVersion.of("1")`.
- **Scenario 1.2 (200 OK with missing/malformed ETag)**: Server returns 200 without ETag header.
  - *Expected*: `AuthoritativePersistenceResult.OutcomeUnknown` (no fallback to `meta.versionId`).
- **Scenario 1.3 (404 Not Found)**: Server returns 404.
  - *Expected*: `AuthoritativePersistenceResult.NotCommitted` with "Resource not found".
- **Scenario 1.4 (410 Gone)**: Server returns 410.
  - *Expected*: `AuthoritativePersistenceResult.NotCommitted` with "Resource not found / deleted".
- **Scenario 1.5 (401 / 403 Forbidden)**: Server returns 401 or 403.
  - *Expected*: `AuthoritativePersistenceResult.NotCommitted` with fail-closed security message.

#### 2. Authoritative CREATE Scenarios
- **Scenario 2.1 (201 Created with ETag)**: Server returns 201 with `ETag: W/"1"`.
  - *Expected*: `AuthoritativePersistenceResult.Committed` with version 1.
  - *Wire Check*: Request header `If-None-Match: *` was transmitted.
- **Scenario 2.2 (412 Precondition Failed / 409 Conflict)**: Server returns 412/409 indicating resource already exists.
  - *Expected*: `AuthoritativePersistenceResult.Conflict` with `AuthoritativePreconditionConflict.resourceAlreadyExists`.
- **Scenario 2.3 (400 Bad Request)**: Server returns 400 with OperationOutcome.
  - *Expected*: `AuthoritativePersistenceResult.NotCommitted`.
- **Scenario 2.4 (201 Created with missing ETag)**: Server returns 201 without ETag.
  - *Expected*: `AuthoritativePersistenceResult.OutcomeUnknown`.

#### 3. Authoritative UPDATE Scenarios
- **Scenario 3.1 (200 OK Update with ETag)**: Server returns 200 with `ETag: W/"2"`.
  - *Expected*: `AuthoritativePersistenceResult.Committed` with version 2.
  - *Wire Check*: Request header `If-Match: W/"1"` was transmitted when `expectedVersion` is 1.
- **Scenario 3.2 (412 Precondition Failed)**: Server returns 412 due to version mismatch.
  - *Expected*: `AuthoritativePersistenceResult.Conflict` with `EXPECTED_VERSION_MISMATCH`.
- **Scenario 3.3 (428 Precondition Required)**: Server returns 428.
  - *Expected*: `AuthoritativePersistenceResult.Conflict`.
- **Scenario 3.4 (404 Not Found on Update)**: Server returns 404.
  - *Expected*: `AuthoritativePersistenceResult.Conflict` indicating target resource does not exist.

#### 4. Transport Failure & Uncertainty Scenarios
- **Scenario 4.1 (Positively Established Pre-Transmission DNS Failure)**: Hostname cannot be resolved (`UnknownHostException`).
  - *Expected*: `AuthoritativePersistenceResult.NotCommitted`.
- **Scenario 4.2 (Response Timeout / Ambiguous Mid-Stream Socket Error)**: WireMock delays response past request timeout.
  - *Expected*: `AuthoritativePersistenceResult.OutcomeUnknown` with `HttpTimeoutException` cause.
- **Scenario 4.3 (Connection Reset Mid-Flight)**: WireMock abruptly resets TCP connection during or after request transmission.
  - *Expected*: `AuthoritativePersistenceResult.OutcomeUnknown`.
- **Scenario 4.4 (500 Internal Server Error)**: WireMock returns 500.
  - *Expected*: `AuthoritativePersistenceResult.OutcomeUnknown`.
- **Scenario 4.5 (Zero Retries Assertion)**: Verify mutating request (`CREATE`, `UPDATE`) is sent exactly once across transport failure.

#### 5. Architectural & Guardrail Scenarios
- Assert `AuthoritativePersistencePort` has zero `delete` or `remove` methods.
- Assert `MnemeAuthoritativeHttpClient` imports zero JPA, Hibernate, or PostgreSQL classes.
- Assert full ArchUnit suite passes.

### Test Changes
- **New Test Class**: `hestia/mneme-persistence/src/test/java/net/fhirfactory/harmonia/persistence/client/MnemeAuthoritativeHttpClientTest.java` (WireMock-based test covering all scenarios above).

# Implementation Plan

### ✓ Step 1: Establish shared persistence contracts module (hestia/mnemosyne-api)
Extract `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` into a new lightweight submodule `hestia/mnemosyne-api`. Update `hestia/pom.xml`, root `pom.xml` dependencyManagement, `hestia/mnemosyne-clinical/pom.xml`, and `hestia/mneme-persistence/pom.xml` to depend on `hestia/mnemosyne-api`.

### ✓ Step 2: Implement Mneme Authoritative HTTP Client in hestia/mneme-persistence
Implement `MnemeAuthoritativeClientConfig`, `HttpTransportFailureClassifier`, and `MnemeAuthoritativeHttpClient` implementing `AuthoritativePersistencePort<IBaseResource>` for internal authoritative endpoint (`/api/authoritative/fhir/*`) with zero transparent retries, conservative transport failure classification, and strict version mapping.

### ✓ Step 3: Implement comprehensive WireMock and unit tests
Create `MnemeAuthoritativeHttpClientTest` in `hestia/mneme-persistence` testing READ, CREATE, UPDATE, transport uncertainty, zero retries, fail-closed auth, and version header consistency.

### ✓ Step 4: Execute verification suites, ArchUnit tests, and update master roadmap
Run tests in `hestia/mneme-persistence`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test` architecture tests. Update the master convergence plan (`docs/implementation/harmonia-convergence-runtime-integration-plan.md`) with M2.1 completion evidence and next step.