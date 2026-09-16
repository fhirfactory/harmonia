---
sessionId: session-260916-090505-7sgs
---

# Requirements

### Overview & Goals

The objective of this initiative is to establish a **FHIR-based Provider Registry** within the Harmonia Health Integration Environment (HIE). Harmonia will manage authoritative healthcare directory resources according to HL7 FHIR R5 standards while enforcing governed, asynchronous change management, high-performance read/search paths, durable persistence, and robust referential integrity.

The initial Provider Registry manages seven core FHIR R5 resource types:
1. `Practitioner` — Individual healthcare professionals (doctors, nurses, specialists).
2. `PractitionerRole` — Roles, duties, locations, and service relationships performed by a Practitioner for an Organization.
3. `Organization` — Healthcare provider organisations, hospital trusts, clinics, and legal entities.
4. `Location` — Physical or logical service delivery locations, wards, facilities, and clinics.
5. `HealthcareService` — Healthcare services offered through an organisation and/or location.
6. `Endpoint` — Electronic service endpoints for system interoperability (FHIR REST, secure messaging, HL7 MLLP).
7. `Group` — Logical groupings of providers, practitioners, or related healthcare entities.

### Scope

#### In Scope
- **Direct Module Extension**: Integrating services cleanly into existing Harmonia modules (`pylai`, `energeia/erga`, `energeia/praxis`, `energeia/ponos`, `hestia/mnemosyne-clinical`, `calliope`, `petasos`, `paradeigma`).
- **FHIR REST API (Pylai)**: Synchronous `READ` (by ID) and `SEARCH` (multi-parameter) across all 7 resources; Asynchronous `CREATE` (`POST`) and `UPDATE` (`PUT`) yielding `HTTP 202 Accepted`.
- **Change Status & Tracking**: Standard FHIR `GET /Task/{id}` endpoint mapping internal `Pragma` execution state to FHIR `Task.status` and returning `OperationOutcome` diagnostic information.
- **CapabilityStatement**: `GET /metadata` endpoint exposing accurate metadata describing supported resources, search parameters, operations, and security constraints.
- **Governed Change Workflow**: Event-driven write pipeline powered by `Petasos` messaging, `Ponos` execution engine, `Praxis` sequence definitions, and per-resource `Ergon` processing activities (`PractitionerChangeErgon`, etc.).
- **Validation & Automated First-Iteration Approval**: Request-level structural checks in Pylai; referential integrity, business identifier, duplicate detection, and optimistic locking in Ponos/Erga; auto-approval and durable commit to Mnemosyne.
- **Authoritative Persistence (Mnemosyne)**: Relational PostgreSQL schema (`hie_fhir_resources`) storing indexed metadata and full FHIR R5 JSON with monotonically increasing `version_id`, `last_updated`, and soft-delete capabilities.
- **Security & Authorization**: Resource- and interaction-level permission model separating authentication from authorization (e.g. `Practitioner.read`, `Endpoint.update.request`).
- **Paradeigma Exemplar & Testing**: Synthetic provider directory datasets, comprehensive read/search tests, async write tests, referential rejection tests, concurrency tests, and crash-recovery tests.
- **Technical Documentation**: Comprehensive 11-document specification suite in `docs/provider-registry/`.

#### Out of Scope
- User interface (UI) components and front-end screens (explicitly deferred to subsequent phase).
- `DELETE`, `PATCH`, `_history` REST endpoints, FHIR `Batch`, and FHIR `Transaction` bundles (persistence is versioning-ready, but interaction APIs are deferred).
- Complex probabilistic Practitioner matching algorithms (deterministic identifier and attribute matching is used initially).
- External third-party directory synchronization / federation (reconciliation architecture and extension hooks are established without live external adapters in this iteration).

### User Stories

- **As an API Consumer / Integrated System**, I want to query practitioners, organizations, locations, and healthcare services via FHIR R5 REST endpoints (`GET /Practitioner`, `GET /Organization`, etc.) so that I can look up up-to-date provider information synchronously.
- **As a Source System / Gateway**, I want to submit provider directory additions and modifications via `POST` and `PUT` requests receiving `HTTP 202 Accepted` with a `Task` reference, so that changes are verified, governed, and audited before mutating authoritative state.
- **As a Client Application**, I want to inspect the progress and outcome of my change request via `GET /Task/{id}` so that I know whether my change succeeded (returning the resulting resource URL and version) or failed with detailed `OperationOutcome` diagnostics.
- **As a FHIR Client**, I want to query `GET /metadata` to inspect the server's `CapabilityStatement` and discover supported resources, search parameters, and security policies dynamically.
- **As a System Administrator / Auditor**, I want all change requests to be durably tracked in `Pragma` checkpoints and `AuditEvent` records so that full change provenance is retained across server restarts and crashes.

### Functional Requirements

1. **FHIR Version Baseline**: The Provider Registry strictly implements HL7 FHIR Release 5 (R5), utilizing HAPI FHIR 7.2.0 structures (`hapi-fhir-structures-r5`) consistent with Calliope and Mnemosyne.
2. **First-Class Resource Independence**:
   - `Endpoint` must be persisted and managed as an independent FHIR resource (not an embedded string on PractitionerRole).
   - `Group` must be persisted independently and represent logical healthcare groupings without hardcoding to external IAM/security group constructs.
3. **Synchronous Read & Search**:
   - Direct retrieval via `GET /{resourceType}/{id}`.
   - Synchronous multi-parameter search supporting FHIR search parameters (`_id`, `identifier`, `name`, `active`, `status`, `practitioner`, `organization`, `location`, `service`, `connection-type`, `type`, `actual`).
4. **Asynchronous Governed Writes**:
   - `POST /{resourceType}` and `PUT /{resourceType}/{id}` parse and validate the payload structurally in Pylai, create a durable `Pragma` change instance, publish a `PetasosMessage` to Artemis, and immediately respond with `HTTP 202 Accepted` (`Location: /Task/{pragmaId}`).
   - `Ponos` consumes the change request and executes the corresponding per-resource `Ergon` activity (`PractitionerChangeErgon`, `PractitionerRoleChangeErgon`, etc.).
   - The Ergon validates referential integrity (e.g. verifying that referenced `Practitioner`, `Organization`, `Location`, or `HealthcareService` resources exist and are active in Mnemosyne).
   - Valid requests transition through `RECEIVED` -> `VALIDATING` -> `APPROVED` -> `COMMITTING` -> `COMPLETED`. Invalid requests transition to `REJECTED` or `FAILED` with embedded `OperationOutcome`.
5. **Optimistic Concurrency Control**:
   - `PUT` requests support version checking via `If-Match: W/"{version}"` headers or version metadata.
   - If the current stored version does not match the requested baseline, the change request is rejected with a concurrency conflict `OperationOutcome`.
6. **Crash & Restart Resilience**:
   - Unfinished change requests stored in Artemis journals and Pragma persistent state resume execution upon service restart without duplicate resource generation or state corruption.

### Non-Functional Requirements

- **Performance**: Synchronous read/search queries must execute against indexed database columns (`hie_fhir_resources`) without routing through Ponos queues, ensuring sub-50ms response times for typical lookups.
- **Privacy & Security**: Strict separation of Authentication and Authorization. Payload logging is masked to protect sensitive provider identifiers and PII/PHI.
- **Auditability**: Every write request creates an immutable `Pragma` history with checkpoints detailing requester, source, timestamps, validation status, and final version ID.
- **Modularity**: Clean separation between REST gateway layer (`pylai`), workflow orchestration (`energeia`), durable persistence (`hestia/mnemosyne-clinical`), and canonical domain models (`calliope`).

# Technical Design

### Current Implementation

- **Calliope (`calliope`)**: Canonical schema library using HAPI FHIR R5 (`hapi-fhir-structures-r5`). Defines `Pragma`, `PragmaStatus`, `PragmaCheckpoint`, `ErgonPayload`, `Topic`, and `FhirSecurityTagManager`.
- **Hestia / Mnemosyne (`hestia/mnemosyne-clinical`)**: Spring Boot JPA application with PostgreSQL/H2 storage. Implements `FhirResourceEntity` (hybrid indexed metadata + `resource_json` LOB) and `FhirStorageService`. Existing providers include `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Group`, `Task`, and `Communication` (missing `EndpointResourceProvider` and extended search filtering).
- **Energeia / Ponos / Erga (`energeia`)**: Camel-based task execution framework. `ErgonBase` provides single-responsibility processing activity foundations; `Praxis` defines ordered task execution pipelines; `PragmaWorkflowDispatcher` handles queue-to-route dispatching over Apache ActiveMQ Artemis.
- **Petasos (`petasos`)**: Clustered, high-availability messaging layer encapsulating Artemis connections with `PetasosMessage` envelopes, deduplication (`DuplicateDetector`), and retry semantics.
- **Pylai (`pylai`)**: Interface gateway module currently housing MLLP inbound/outbound adapters (`pylai-mllp-in`, `pylai-mllp-out`, `pylai-mllp-base`).

### Key Decisions

1. **Subsystem Layout — Direct Module Extension**: Extend existing Harmonia modules directly (`pylai` for FHIR REST gateway, `energeia/erga` for change processing Erga, `hestia/mnemosyne-clinical` for storage/search, `calliope` for models) rather than creating a separate top-level suite.
   - *Rationale*: Preserves Harmonia's clean 5-tier architecture (Pylai -> Energeia -> Petasos -> Mnemosyne -> Calliope) and aligns with existing build and deployment configurations.
2. **Ergon Strategy — Per-Resource Erga Activities**: Implement dedicated Ergon activities for each resource type (`PractitionerChangeErgon`, `PractitionerRoleChangeErgon`, `OrganizationChangeErgon`, `LocationChangeErgon`, `HealthcareServiceChangeErgon`, `EndpointChangeErgon`, `GroupChangeErgon`).
   - *Rationale*: Provides strong typing, clean isolation of resource-specific validation logic (e.g. Practitioner identifiers vs PractitionerRole reference chains), and granular observability.
3. **Change Status Contract — FHIR Task Endpoint**: Expose standard FHIR R5 `GET /Task/{id}` backed by the underlying `Pragma` entity.
   - *Rationale*: Standard FHIR compliance allows external FHIR clients to poll and track asynchronous change requests without proprietary REST endpoints, returning `OperationOutcome` in `Task.output` on failure and target resource references on success.
4. **Storage Strategy — Hybrid Relational Document Pattern**: Utilize `hie_fhir_resources` with relational indexing on `resource_type`, `fhir_id`, `version_id`, `is_deleted`, and `last_updated`, while storing complete FHIR R5 representations in the `resource_json` TEXT/JSON column.
   - *Rationale*: Guarantees lossless FHIR R5 fidelity, schema evolution flexibility, and high-performance indexed queries.

### Proposed Changes

#### 1. `calliope` (Canonical Models & Definitions)
- Add `ProviderRegistryTopics` constants (`TOPIC_PROVIDER_REGISTRY_CHANGE_REQUEST`, `TOPIC_PROVIDER_REGISTRY_CHANGE_EVENT`).
- Extend `Pragma` helper utilities and `PragmaFhirConverter` to map Provider Registry change payloads, FHIR `Task`, and `OperationOutcome` diagnostic details.
- Add error and validation outcome codes (`PR-VAL-001` to `PR-VAL-010`) for referential integrity and duplicate identifier violations.

#### 2. `hestia/mnemosyne-clinical` (Authoritative Persistence & Search)
- Add `EndpointResourceProvider` implementing `IResourceProvider` for FHIR R5 `Endpoint` resources and register it in `JpaRestfulServer`.
- Extend `FhirStorageService.searchResources(...)` to support specialized search criteria for all 7 resources:
  - `Practitioner`: `identifier`, `name`, `active`.
  - `PractitionerRole`: `practitioner`, `organization`, `location`, `service`, `active`, `identifier`.
  - `Organization`: `identifier`, `name`, `active`.
  - `Location`: `identifier`, `name`, `organization`, `status`.
  - `HealthcareService`: `identifier`, `name`, `organization`, `location`, `active`.
  - `Endpoint`: `identifier`, `organization`, `status`, `connection-type`.
  - `Group`: `identifier`, `type`, `actual`, `name`.
- Implement `ProviderRegistryReferenceValidator` to verify target resource existence and active status in `hie_fhir_resources` before committing changes.
- Ensure strict optimistic concurrency control on update operations checking `version_id` against client-provided `If-Match` / `ETag`.

#### 3. `energeia/erga` & `energeia/praxis` (Change Processing Workflows)
- Implement per-resource Ergon activities extending `ErgonBase`:
  - `PractitionerChangeErgon`: Validates practitioner name/identifiers, checks duplicate NPI/HPI-I identifiers, writes to Mnemosyne.
  - `PractitionerRoleChangeErgon`: Validates reference links (`practitioner`, `organization`, `location`, `healthcareService`, `endpoint`), checks conflicts, writes to Mnemosyne.
  - `OrganizationChangeErgon`: Validates organization identifiers, hierarchy references, writes to Mnemosyne.
  - `LocationChangeErgon`: Validates managing organization reference, physical coordinates/address, writes to Mnemosyne.
  - `HealthcareServiceChangeErgon`: Validates organization and location references, writes to Mnemosyne.
  - `EndpointChangeErgon`: Validates connection type, URL/address structure, payload types, writes to Mnemosyne.
  - `GroupChangeErgon`: Validates group type, member references, writes to Mnemosyne.
- Register sequence `seq-provider-registry-change-pipeline` in `TaskSequenceDefaultSeeder` and `PraxisService`.
- Wire Artemis change queue (`harmonia.provider.registry.change.request`) in `energeia/ponos`.

#### 4. `pylai` (Interface & REST Gateway)
- Create `pylai-fhir-registry` module in `pylai` exposing:
  - `GET /{resourceType}/{id}`: Direct synchronous read.
  - `GET /{resourceType}?{params}`: Direct synchronous multi-parameter search.
  - `POST /{resourceType}`: Synchronous structural parsing & validation; asynchronous `Pragma` dispatch; returns `HTTP 202 Accepted` with `Location: /Task/{pragmaId}`.
  - `PUT /{resourceType}/{id}`: Version-aware asynchronous update dispatch; returns `HTTP 202 Accepted` with `Location: /Task/{pragmaId}`.
  - `GET /Task/{id}`: FHIR R5 Task status endpoint reflecting Pragma lifecycle and execution outputs.
  - `GET /metadata`: FHIR R5 `CapabilityStatement` listing supported resource types, search parameters, interactions, and security requirements.
- Add security interceptor verifying token scopes and resource-level permissions (`Practitioner.read`, `PractitionerRole.update.request`, etc.).

#### 5. `paradeigma` (Synthetic Exemplars & Testing)
- Create synthetic provider registry exemplars in `paradeigma-common` (Practitioners with Australian HPI-I identifiers, Organizations with HPI-O, linked PractitionerRoles, physical Locations, HealthcareServices, secure electronic Endpoints, and specialty Groups).
- Add integration tests in `paradeigma-test` verifying end-to-end async write pipeline, synchronous reads, referential validation failures, concurrency conflicts, and restart recovery.

### Architecture Diagram

```mermaid
graph TD
    Client[FHIR Client / Source System]

    subgraph "Pylai Gateway Layer (pylai-fhir-registry)"
        REST[FHIR REST Controller / Providers]
        Sec[Auth & RBAC Security Filter]
        Cap[CapabilityStatement Provider /metadata]
        TaskEP[FHIR Task Resource Provider /Task]
    end

    subgraph "Petasos Messaging Layer"
        PQueue[Petasos Artemis Queue<br/>harmonia.provider.registry.change.request]
    end

    subgraph "Energeia Workflow Engine (Ponos / Praxis / Erga)"
        Dispatcher[PragmaWorkflowDispatcher]
        Seq[Praxis Sequence: seq-provider-registry-change-pipeline]
        
        subgraph "Per-Resource Erga Activities"
            PErg[PractitionerChangeErgon]
            PRErg[PractitionerRoleChangeErgon]
            OErg[OrganizationChangeErgon]
            LErg[LocationChangeErgon]
            HSErg[HealthcareServiceChangeErgon]
            EErg[EndpointChangeErgon]
            GErg[GroupChangeErgon]
        end
    end

    subgraph "Hestia Persistence Layer (mnemosyne-clinical)"
        FStore[FhirStorageService]
        RefVal[ProviderRegistryReferenceValidator]
        DB[(PostgreSQL: hie_fhir_resources)]
    end

    %% Read & Search Path
    Client -->|1. GET /Practitioner?name=... (Sync)| Sec
    Sec --> REST
    REST -->|2. Query Search/Read| FStore
    FStore -->|3. SQL Query| DB
    FStore -->|4. FHIR Bundle / Resource| REST
    REST -->|5. HTTP 200 OK| Client

    %% Write Path
    Client -->|A. POST / PUT Resource| Sec
    Sec --> REST
    REST -->|B. Create Pragma & Publish| PQueue
    REST -->|C. HTTP 202 Accepted (Location: /Task/id)| Client

    %% Async Processing Path
    PQueue -->|D. Consume Change Pragma| Dispatcher
    Dispatcher -->|E. Execute Sequence| Seq
    Seq --> PErg & PRErg & OErg & LErg & HSErg & EErg & GErg
    PRErg & HSErg & LErg -->|F. Validate References| RefVal
    RefVal -->|Check Target Exists| DB
    PErg & PRErg & OErg & LErg & HSErg & EErg & GErg -->|G. Persist Approved Resource| FStore
    FStore -->|H. Commit State & Version| DB

    %% Status Polling Path
    Client -->|X. GET /Task/id| TaskEP
    TaskEP -->|Y. Inspect Pragma State| Dispatcher
    TaskEP -->|Z. HTTP 200 OK (FHIR Task + OperationOutcome)| Client
```

### Data Models / Contracts

#### Pragma State Machine for Provider Registry Writes
```
[RECEIVED] 
    │ (Payload parsed and queued via Petasos)
    ▼
[VALIDATING] 
    │ (Ergon executes structural, identifier & reference validation)
    ├─── Invalid References / Malformed ──► [REJECTED] (OperationOutcome recorded)
    ├─── Concurrency / Stale ETag Conflict ──► [FAILED] (409 Conflict recorded)
    ▼
[APPROVED] 
    │ (Automated validation pass)
    ▼
[COMMITTING] 
    │ (Persisting resource & incrementing version in Mnemosyne)
    ▼
[COMPLETED] (Resource available for GET/SEARCH; Task.output updated)
```

#### Database Schema: `hie_fhir_resources`
- `id`: `BIGSERIAL PRIMARY KEY`
- `resource_type`: `VARCHAR(64) NOT NULL` (e.g., `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`, `Task`)
- `fhir_id`: `VARCHAR(128) NOT NULL`
- `version_id`: `BIGINT NOT NULL DEFAULT 1`
- `resource_json`: `TEXT NOT NULL`
- `is_deleted`: `BOOLEAN NOT NULL DEFAULT FALSE`
- `last_updated`: `TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()`
- Constraints & Indexes: `uk_resource_type_fhir_id`, `idx_resource_type_fhir_id`, `idx_resource_type_deleted`

### File Structure Changes

```
harmonia/
├─��� calliope/
│   └── src/main/java/net/fhirfactory/harmonia/model/
│       ├── pragma/ProviderRegistryChangePragma.java
│       └── registry/ProviderRegistryConstants.java
├── pylai/
│   ├── pom.xml (add module pylai-fhir-registry)
│   └── pylai-fhir-registry/
│       ├── pom.xml
│       └── src/main/java/net/fhirfactory/harmonia/pylai/fhir/
│           ├── controller/FhirRestGatewayController.java
│           ├── provider/
│           │   ├── GenericProviderRegistryResourceProvider.java
│           │   ├── CapabilityStatementProvider.java
│           │   └── ProviderRegistryTaskResourceProvider.java
│           ├── security/FhirSecurityInterceptor.java
│           └── service/ChangeRequestSubmissionService.java
├── energeia/
│   ├── erga/src/main/java/net/fhirfactory/harmonia/erga/registry/
│   │   ├── PractitionerChangeErgon.java
│   │   ├── PractitionerRoleChangeErgon.java
│   │   ├── OrganizationChangeErgon.java
│   │   ├── LocationChangeErgon.java
│   │   ├── HealthcareServiceChangeErgon.java
│   │   ├── EndpointChangeErgon.java
│   │   └── GroupChangeErgon.java
│   └── praxis/src/main/java/net/fhirfactory/harmonia/praxis/sequence/
│       └── ProviderRegistrySequenceBuilder.java
├── hestia/
│   └── mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/
│       ├── provider/EndpointResourceProvider.java
│       ├── service/
│       │   ├── FhirStorageService.java (enhanced search filters)
│       │   └── ProviderRegistryReferenceValidator.java
│       └── config/JpaRestfulServer.java
├── paradeigma/
│   ├── paradeigma-common/src/main/java/net/fhirfactory/harmonia/paradeigma/common/
│   │   └── generator/SyntheticProviderRegistryGenerator.java
│   └── paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/
│       ├── ProviderRegistryEndToEndWriteTest.java
│       ├── ProviderRegistrySearchIntegrationTest.java
│       ├── ProviderRegistryReferentialRejectionTest.java
│       └── ProviderRegistryRestartRecoveryTest.java
└── docs/
    └── provider-registry/
        ├── architecture.md
        ├── resource-model.md
        ├── fhir-api.md
        ├── search.md
        ├── change-processing.md
        ├── persistence.md
        ├── validation.md
        ├── security.md
        ├── audit-provenance.md
        ├── failure-recovery.md
        └── testing.md
```

### Risks & Mitigations

- **Risk: Dual-write discrepancy between Artemis messaging queue and Mnemosyne storage.**
  - *Mitigation*: Client receives `202 Accepted` solely after durable Artemis journal ACK. Stored `Pragma` records track checkpoint progression (`RECEIVED` -> `VALIDATING` -> `APPROVED` -> `COMPLETED`).
- **Risk: Dangling references if related resources are submitted out-of-order.**
  - *Mitigation*: The Ergon pipeline verifies referenced entities against `hie_fhir_resources`. If a referenced `Organization` or `Practitioner` does not exist, the `PractitionerRole` change is rejected with a clear `OperationOutcome` diagnostic message pointing to the missing reference.
- **Risk: Lost updates during concurrent PUT requests.**
  - *Mitigation*: Enforce optimistic locking by validating the incoming resource version against the stored `version_id` and failing the request if a mismatch occurs.

# Testing

### Validation Approach

Validation is performed at multiple layers to ensure complete compliance with FHIR R5 specifications, referential integrity rules, and system resiliency requirements:

1. **Unit & Gateway Validation**:
   - Verify FHIR R5 JSON structural parsing and profile compliance in `pylai-fhir-registry`.
   - Verify `CapabilityStatement` generation matches all supported resources, operations, and search parameters.
   - Verify RBAC authorization interceptors allow/deny operations according to token claims.
2. **Workflow & Ergon Activity Validation**:
   - Verify each of the 7 Ergon processing activities (`PractitionerChangeErgon`, etc.) correctly validates input payloads, checks business identifiers, verifies references, and updates `Pragma` checkpoints.
   - Verify referential integrity validation rejects invalid reference targets and accepts valid ones.
   - Verify automatic approval transitions `Pragma` to `COMPLETED` and commits authoritative state to Mnemosyne.
3. **End-to-End Integration & Scenario Testing**:
   - Verify the synchronous read and search paths return newly persisted resources.
   - Verify asynchronous POST/PUT write requests return `HTTP 202 Accepted` with a valid `Task` location.
   - Verify polling `GET /Task/{id}` yields the correct task status and outcome payload.
4. **Resilience & Concurrency Testing**:
   - Verify crash recovery: simulate Ponos broker termination during in-flight change processing, restart Ponos, and assert that processing resumes and completes idempotently without creating duplicate records.
   - Verify optimistic locking: simulate concurrent updates on the same resource and verify that stale version updates are rejected.

### Key Scenarios

1. **Successful Creation of Complete Provider Hierarchy**:
   - Submit `POST /Organization` (Hospital Trust) -> `202 Accepted` -> Ponos persists Organization.
   - Submit `POST /Location` (Hospital Clinic) -> `202 Accepted` -> Ponos validates Organization reference and persists Location.
   - Submit `POST /HealthcareService` (Cardiology Clinic) -> `202 Accepted` -> Ponos validates references and persists HealthcareService.
   - Submit `POST /Endpoint` (FHIR REST Endpoint) -> `202 Accepted` -> Ponos persists Endpoint.
   - Submit `POST /Practitioner` (Dr. Jane Doe) -> `202 Accepted` -> Ponos persists Practitioner.
   - Submit `POST /PractitionerRole` referencing the above Practitioner, Organization, Location, HealthcareService, and Endpoint -> `202 Accepted` -> Ponos validates all references and persists PractitionerRole.
   - Execute `GET /PractitionerRole?practitioner=...` -> Asserts that complete connected graph is returned with valid references.
2. **Rejection on Missing Referential Target**:
   - Submit `POST /PractitionerRole` referencing a non-existent Practitioner ID (`Practitioner/non-existent-999`).
   - Asserts `202 Accepted` is returned initially.
   - Polling `GET /Task/{id}` yields `status: rejected` with an `OperationOutcome` containing issue code `PR-VAL-004` ("Referenced Practitioner does not exist").
   - Asserts that no `PractitionerRole` record was committed to Mnemosyne.
3. **Duplicate Identifier Detection**:
   - Submit `POST /Practitioner` with an existing deterministic National Provider Identifier (e.g. HPI-I).
   - Asserts that change is rejected or flagged as duplicate according to the deterministic duplicate detection rule.
4. **Optimistic Locking Conflict**:
   - Client A reads `Practitioner/100` at version `1`.
   - Client B updates `Practitioner/100` to version `2`.
   - Client A submits `PUT /Practitioner/100` based on version `1` with `If-Match: W/"1"`.
   - Asserts that change request transitions to `FAILED` with a concurrency conflict `OperationOutcome`.
5. **Broker Restart Recovery**:
   - Submit change request, intercept execution after queue receipt before commit, restart the Ponos work engine, and verify the task completes successfully with exactly one persisted resource.

### Test Changes

- **New Test Classes**:
  - `pylai/pylai-fhir-registry/src/test/java/.../FhirRestGatewayControllerTest.java`
  - `pylai/pylai-fhir-registry/src/test/java/.../CapabilityStatementProviderTest.java`
  - `energeia/erga/src/test/java/.../PractitionerChangeErgonTest.java`
  - `energeia/erga/src/test/java/.../PractitionerRoleChangeErgonTest.java`
  - `energeia/erga/src/test/java/.../EndpointChangeErgonTest.java`
  - `hestia/mnemosyne-clinical/src/test/java/.../ProviderRegistrySearchTest.java`
  - `paradeigma/paradeigma-test/src/test/java/.../ProviderRegistryEndToEndWriteTest.java`
  - `paradeigma/paradeigma-test/src/test/java/.../ProviderRegistryRestartRecoveryTest.java`
  - `paradeigma/paradeigma-test/src/test/java/.../ProviderRegistryConcurrencyTest.java`

# Delivery Steps

### * Step 1: Core Domain Models, FHIR R5 Provider Registry Definitions & Mnemosyne Persistence
Authoritative persistence, model definitions, and search capabilities for all seven Provider Registry FHIR resources are established in Calliope and Mnemosyne.

- Extend `calliope` with canonical `ProviderRegistryChangePragma` definitions, topic subscriptions (`TOPIC_PROVIDER_REGISTRY_CHANGE`), error codes, and FHIR R5 converter extensions.
- Add `EndpointResourceProvider` to `hestia/mnemosyne-clinical` and register it in `JpaRestfulServer`.
- Update `FhirStorageService` in `hestia/mnemosyne-clinical` to support comprehensive multi-parameter search for `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, and `Group` (supporting `identifier`, `name`, `active`/`status`, `practitioner`, `organization`, `location`, `service`, `connection-type`, `type`, `actual`).
- Implement referential integrity inspection service in `mnemosyne-clinical` / core persistence layer to validate existence and active status of referenced target resources (e.g., verifying `Practitioner` and `Organization` exist when indexing a `PractitionerRole`).
- Implement optimistic locking and version incrementing (`version_id`, `last_updated`, `Meta.versionId`) with `If-Match` / `ETag` conflict detection.

###   Step 2: Per-Resource Erga Processing Activities, Praxis Workflows & Ponos Dispatcher
Ponos and Erga workflow execution pipeline processes asynchronous provider registry changes with validation, reference checking, duplicate detection, and automated approval.

- Create per-resource change processing activities in `energeia/erga`: `PractitionerChangeErgon`, `PractitionerRoleChangeErgon`, `OrganizationChangeErgon`, `LocationChangeErgon`, `HealthcareServiceChangeErgon`, `EndpointChangeErgon`, and `GroupChangeErgon`.
- Implement validation routines within each Ergon activity for resource structure, profile compliance, business identifier uniqueness, and referential integrity.
- Implement automated first-iteration approval logic and terminal state persistence (`APPROVED` -> `COMPLETED`, or `REJECTED` / `FAILED` with `OperationOutcome` diagnostic details).
- Define and register the Provider Registry Praxis workflow sequence `seq-provider-registry-change-pipeline` in `energeia/praxis/sequence/TaskSequenceDefaultSeeder` and `PraxisService`.
- Wire `Petasos` message routing and Artemis queue destinations (`harmonia.provider.registry.change.request`) in `energeia/ponos` for durable, crash-resilient change processing.

###   Step 3: Pylai FHIR REST Gateway, CapabilityStatement, Async Change APIs & Security
Pylai exposes a secure, asynchronous-governed FHIR REST interface, FHIR Task status tracking, and an accurate CapabilityStatement.

- Implement `pylai-fhir-registry` module in `pylai` exposing standard FHIR R5 REST endpoints for `/Practitioner`, `/PractitionerRole`, `/Organization`, `/Location`, `/HealthcareService`, `/Endpoint`, and `/Group`.
- Implement synchronous `GET` (read by ID and search by parameters) delegating directly to `FhirStorageService` / Provider Registry repository.
- Implement asynchronous `POST` (create) and `PUT` (update) endpoints returning `HTTP 202 Accepted` with `Location: /Task/{pragmaId}`, headers, and correlation tracking.
- Implement standard FHIR R5 `GET /Task/{id}` endpoint mapping internal `Pragma` lifecycle state (`REQUESTED`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`, `REJECTED`, `FAILED`) to FHIR `Task.status` and embedding `OperationOutcome` or resulting resource references in `Task.output`.
- Implement `GET /metadata` endpoint serving a dynamic, compliant `CapabilityStatement` reflecting the exact supported resources, search parameters, and interactions.
- Apply role- and resource-level authorization filters (`Practitioner.read`, `Practitioner.update.request`, etc.) separating authentication from authorization.

###   Step 4: Paradeigma Exemplar Data, End-to-End Testing & Technical Documentation
End-to-end integration and recovery test suite, Paradeigma synthetic exemplars, and comprehensive architecture documentation are delivered.

- Create synthetic exemplar dataset in `paradeigma` modeling realistic healthcare providers, organizations, service locations, electronic endpoints, and practitioner role relationships.
- Implement automated integration tests in `paradeigma-test` and `pylai-fhir-registry` covering synchronous reads/searches, asynchronous POST/PUT lifecycle, referential rejection scenarios, stale concurrent updates (`If-Match`), and broker restart recovery.
- Author complete Markdown documentation suite under `docs/provider-registry/` (`architecture.md`, `resource-model.md`, `fhir-api.md`, `search.md`, `change-processing.md`, `persistence.md`, `validation.md`, `security.md`, `audit-provenance.md`, `failure-recovery.md`, `testing.md`).