# Harmonia Provider Registry — Architecture Overview

### 1. Architectural Objective

The Harmonia Provider Registry establishes an authoritative, HL7 FHIR Release 5 (R5) compliant healthcare directory within the Harmonia Health Integration Environment (HIE). It provides high-performance synchronous read and multi-parameter search paths, coupled with governed, asynchronous, event-driven change management pipelines.

```
                     HARMONIA

                 Provider Registry
                       │
      ┌────────────────┼────────────────┐
      │                │                │
   PYLAI             PONOS          MNEMOSYNE
 FHIR APIs       Change Processing    Persistence
      │                │                │
      │             ERGON              │
      │          Work Definitions       │
      │                │                │
      │             PRAGMA              │
      │         Change Instances        │
      │                │                │
      └────────────────┴────────────────┘
```

### 2. Core Subsystems and Layering

1. **Pylai (`pylai-fhir-registry`)**:
   - Exposes the public FHIR REST API boundary for 7 Provider Registry resources: `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, and `Group`.
   - Exposes dynamic metadata discovery via `GET /metadata` returning a compliant `CapabilityStatement`.
   - Exposes asynchronous task polling via standard `GET /Task/{id}`.
   - Handles HTTP authentication, request-level structural FHIR parsing, and resource-level RBAC authorization filters.

2. **Petasos (`petasos-api`, `petasos-core`, `petasos-artemis`)**:
   - High-availability, clustered messaging framework.
   - Manages durable message queue destinations (`harmonia.provider.registry.change.request`).
   - Ensures payload encapsulation, correlation tracking, and message deduplication.

3. **Energeia / Ponos & Praxis (`energeia/ponos`, `energeia/praxis`)**:
   - Asynchronous workflow orchestration engine.
   - Defines and registers the Provider Registry Change Pipeline (`seq-provider-registry-change-pipeline`).
   - Consumes change requests from Artemis queues and dispatches `Pragma` task instances to matching per-resource Ergon activities.

4. **Energeia / Erga (`energeia/erga`)**:
   - Single-responsibility Task Processing Activities (Erga):
     - `PractitionerChangeErgon`
     - `PractitionerRoleChangeErgon`
     - `OrganizationChangeErgon`
     - `LocationChangeErgon`
     - `HealthcareServiceChangeErgon`
     - `EndpointChangeErgon`
     - `GroupChangeErgon`
   - Executes structural verification, duplicate business identifier detection, referential integrity checking, automated approval, and durable persistence.

5. **Hestia / Mnemosyne (`hestia/mnemosyne-clinical`)**:
   - Authoritative FHIR R5 persistence layer backed by relational PostgreSQL/H2 storage (`hie_fhir_resources`).
   - Hybrid relational-document model with indexed metadata columns (`resource_type`, `fhir_id`, `version_id`, `is_deleted`, `last_updated`) and full lossless FHIR JSON LOB.
   - Provides sub-50ms indexed multi-parameter searches, optimistic concurrency control (`If-Match` / `ETag`), and referential integrity validation.

6. **Calliope (`calliope`)**:
   - Canonical domain models (`Pragma`, `PragmaCheckpoint`, `PragmaStatus`, `ErgonPayload`, `Topic`, `ProviderRegistryChangePragma`).
   - Standard error and validation codes (`PR-VAL-001` to `PR-VAL-010`).
   - Transparent bi-directional converters between internal `Pragma` models and external FHIR R5 `Task` resources.

### 3. Architecture Flow Diagram

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
    REST -->|C. HTTP 202 Accepted Location: /Task/id| Client

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
    TaskEP -->|Z. HTTP 200 OK FHIR Task + OperationOutcome| Client
```
