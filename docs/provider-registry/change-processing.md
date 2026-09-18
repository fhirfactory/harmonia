# Harmonia Provider Registry — Change Processing & Workflows

### 1. Governed Write Pipeline Architecture

Every write interaction (HTTP `POST` or `PUT`) is treated as an explicit **Request for Change** rather than an immediate, direct mutation of authoritative database state.

```
External Consumer
      │
      │ POST / PUT
      ▼
    PYLAI
      │
      ├── Authentication & Security Interceptor
      ├── Structural FHIR Parsing & Validation
      └── Create Pragma & Publish PetasosMessage
              │
              ├──► HTTP 202 Accepted (Location: /Task/{id})
              │
              ▼
           PETASOS (harmonia.provider.registry.change.request)
              │
              ▼
            PONOS (PragmaWorkflowDispatcher)
              │
              ▼
            PRAXIS (seq-provider-registry-change-pipeline)
              │
              ▼
            ERGON (Per-Resource Activity)
              │
              ├── Referential Integrity Validation
              ├── Business Identifier & Duplicate Detection
              ├── Automated Approval
              ├── Durable Commit & Version Increment (Mnemosyne)
              ▼
          COMPLETED (Available for Sync Read/Search)
```

### 2. Pragma State Machine

```
[RECEIVED] 
    │ (Payload parsed and queued via Petasos)
    ▼
[VALIDATING] 
    │ (Ergon executes structural, identifier & reference validation)
    ├─── Invalid References / Malformed ──► [REJECTED] (OperationOutcome recorded)
    ├─── Concurrency / Stale ETag Conflict ──► [FAILED] (409/412 Conflict recorded)
    ▼
[APPROVED] 
    │ (Automated validation pass)
    ▼
[COMMITTING] 
    │ (Persisting resource & incrementing version in Mnemosyne)
    ▼
[COMPLETED] (Resource available for GET/SEARCH; Task.output updated)
```

### 3. Step-by-Step Change Trace Example: `PUT /Practitioner/pract-100`

1. **Client Submission**:
   Client submits `PUT /Practitioner/pract-100` with `If-Match: W/"2"`.
2. **Pylai Ingestion**:
   - Evaluates RBAC permissions (`Practitioner.update.request`).
   - Parses FHIR payload and creates `Pragma` with metadata `operation=UPDATE`, `resourceId=pract-100`, `ifMatch=W/"2"`.
   - Records checkpoint `INGEST (RECEIVED)`.
   - Publishes `PetasosMessage` to queue `harmonia.provider.registry.change.request`.
   - Responds with `HTTP 202 Accepted` (`Location: /Task/{pragmaId}`).
3. **Ponos Queue Consumption**:
   `PragmaWorkflowDispatcher` consumes message and identifies sequence `seq-provider-registry-change-pipeline`.
4. **Ergon Execution (`PractitionerChangeErgon`)**:
   - Inspects `Pragma.input` and extracts updated `Practitioner` resource.
   - Validates name and identifier uniqueness against existing database records.
   - Transitions state to `APPROVED` -> records checkpoint.
   - Transitions state to `COMMITTING` -> records checkpoint.
   - Invokes `FhirStorageService.updateResource("pract-100", practitioner, "W/\"2\"")`.
   - `FhirStorageService` verifies version matches 2, increments `version_id` to 3, updates `last_updated`, and commits to `hie_fhir_resources`.
5. **Completion & Audit**:
   - Transitions Pragma to `COMPLETED` with metadata `resultingVersion=3`.
   - Stores updated `Practitioner` in `Pragma.output`.
   - Any client polling `GET /Task/{pragmaId}` receives `status: completed` and the new version reference.
