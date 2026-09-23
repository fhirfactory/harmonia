---
sessionId: session-260923-145455-vpbq
---

# Requirements

### Overview & Goals
The objective of this exploration is to establish a rigorous, read-only architectural baseline of Harmonia's current `AuditEvent` and security audit handling. High-utility audit logs are essential for patient safety, system accountability, and regulatory non-repudiation. When audit evidence can be altered or destroyed, the aggregate utility of the security architecture is compromised. This exploration catalogs every mechanism by which audit records are created, persisted, retrieved, updated, overwritten, or deleted across all subprojects.

### Scope
- **In Scope**:
  - Investigation of all audit representations across `themis`, `hestia`, `iris`, `energeia`, and `calliope`.
  - Detailed mapping of the complete write, mutation, read, and search flows.
  - Identification of all mutation surfaces (`PUT`, `PATCH`, `DELETE`, replacement).
  - Assessment of Themis authorization models, identity schemes, and database persistence behaviors.
  - Identification of candidate enforcement boundaries for Task 06 without premature implementation.
- **Out of Scope**:
  - Any code or schema modifications to production or test files.
  - Implementation of Task 06 immutability features, database triggers, or REST reconfigurations.
  - Commencing Task 07 (volatile persistence fallback), Task 08 (authoritative writes), Task 09 (cache-aside reads), or Task 10 (authoritative search).

### Evidentiary Objectives & Invariants
- Establish repo reality against the core invariant: *Once accepted as audit evidence, an `AuditEvent` is immutable*.
- Evaluate the risk of evidentiary loss resulting from generic CRUD inheritance.
- Provide a clean foundation for subsequent security hardening stages.

# Technical Design

### Current Implementation & Disconnect
Harmonia currently operates two isolated audit mechanisms with zero bridging:
1. **Themis Audit Engine (`themis-audit`)**: `InMemoryThemisAuditService` maintains an ephemeral in-memory ring buffer of `ThemisAuditEvent` records representing authorization decisions. It has a default limit of 1,000 entries and is never written to disk, database, or message queues.
2. **FHIR AuditEvent Store (`hestia` & `iris-befe`)**: Treats FHIR `AuditEvent` as a standard clinical CRUD resource inheriting generic update and deletion capabilities across REST, cache, and database layers.

### Key Architectural Findings
- **Active Mutation Endpoints**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` publishes `@PUT` (update) and `@DELETE` (delete) endpoints.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java` implements `@Update` and `@Delete` interactions.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java` executes JPA `entityManager.merge()` (in-place update) and `entityManager.remove()` (hard delete from PostgreSQL).
- **Themis Policy Bypass**:
  - `ThemisClinicalAuthorizationFilter.java` hardcodes `securityDomain="CLINICAL"` for all `/api/fhir/*` paths.
  - This bypasses `AuditReadPolicy` (which requires `securityDomain="AUDIT"`).
  - Under `ClinicalAuthorizationPolicy`, actors with `clinical.update` authority or `CLINICAL_ADMIN` roles can freely invoke `PUT /api/fhir/AuditEvent/{id}`.
- **Frontend Mutation Controls**:
  - `iris/iris-clinical/src/views/AuditEventView.vue` renders an active delete button (trash icon) calling `securityStore.deleteAuditEvent()`, which executes HTTP `DELETE`.
- **Database & Storage Reality**:
  - `AuditEvent` records reside in the polymorphic `clinical_resources` table (`ResourceEntity`).
  - No database triggers, constraints, or append-only tables exist; updates overwrite existing JSON payloads, incrementing `version_id` without retaining history.
- **Replay & Idempotency Gaps**:
  - No deduplication keys or idempotency mechanisms exist; replayed writes either duplicate records (if ID is omitted) or overwrite existing records in place.

### Candidate Enforcement Boundaries
1. **REST Gateway Tier**: Disable `@PUT`, `@PATCH`, and `@DELETE` on `AuditEventResource` and `AuditEventResourceProvider`, returning HTTP `405 Method Not Allowed`.
2. **Themis Security Tier**: Route `AuditEvent` through `AuditReadPolicy` or a dedicated `AuditImmutabilityPolicy` that unconditionally denies all actions except `CREATE`, `READ`, and `SEARCH`.
3. **Storage Service Tier**: Guard `FhirStorageService` and `FhirCacheService` with explicit checks that reject updates or deletions targeting `AuditEvent`.
4. **Queue Pipeline Tier**: Validate envelopes in `DualWritePersistErgon` to drop or reject non-create operations for `AuditEvent`.
5. **Database Tier**: Attach PostgreSQL `BEFORE UPDATE OR DELETE` triggers on audit storage tables to guarantee append-only immutability at the lowest level.

# Testing & Gaps

### Validation Baseline
Current test suites confirm that audit mutability is actively permitted:
- `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProviderTest.java` contains passing unit tests (`testUpdateAuditEvent` and `testDeleteAuditEvent`) asserting that updates and deletions succeed.
- `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditServiceTest.java` verifies ring-buffer trimming and in-memory decision logging.
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java` validates controller initialization and creation/reading.

### Test Gaps to Address in Task 06
- Absence of negative tests asserting the rejection of `UPDATE`, `PATCH`, and `DELETE` on `AuditEvent`.
- Absence of ArchUnit rules enforcing that no controller or provider exposes mutation methods for audit evidence.
- Inversion required for `AuditEventResourceProviderTest` to ensure that update and delete attempts result in errors rather than successful mutations.

### Downstream Task Alignment
- **Task 07**: Remove volatile persistence fallback (`localFallbackCaches` in `FhirCacheService`).
- **Task 08**: Enforce authoritative synchronous clinical writes to Mnemosyne.
- **Task 09**: Implement cache-aside point reads with PostgreSQL fallback.
- **Task 10**: Implement authoritative clinical search against the database.

# Delivery Steps

### * Step 1: Audit Model and Write Path Discovery
All production audit models, services, creation triggers, and wire representations are mapped across Themis, Hestia, and Iris.

- Trace `ThemisAuditEvent` and `ThemisAuditEventDto` lifecycle within `themis-audit`, establishing its in-memory ring-buffer bounds and lack of durable persistence.
- Inspect `AuditEventResource` in `iris-befe` and `AuditEventResourceProvider` in `mnemosyne-clinical` to identify FHIR R5 `AuditEvent` handling and HAPI parser configurations.
- Map the asynchronous Petasos transport path through `petasos.queue.dualwrite.persist` and `DualWritePersistErgon`.
- Document the disconnect between Themis security evaluation events and FHIR `AuditEvent` persistence.

###   Step 2: Mutation Surface and Authorization Analysis
Every endpoint, service method, and UI control capable of updating, patching, or deleting audit evidence is identified and documented.

- Audit `AuditEventResource` in `iris-befe` for `@PUT` and `@DELETE` method definitions and request handling.
- Audit `AuditEventResourceProvider` and `FhirStorageService` in `mnemosyne-clinical` for `@Update`, `@Delete`, and JPA `merge`/`remove` calls.
- Inspect `ThemisClinicalAuthorizationFilter` and canonical Themis policies to document how `securityDomain="CLINICAL"` bypasses `AuditReadPolicy` and permits audit mutation by clinical roles.
- Review `iris-clinical` (`AuditEventView.vue` and `securityStore.ts`) to locate frontend deletion triggers and manual creation controls.

###   Step 3: Persistence Semantics and Candidate Boundary Synthesis
Durable storage semantics, replay vulnerabilities, and candidate architectural enforcement boundaries are cataloged for Task 06 design.

- Inspect PostgreSQL schema definitions, JPA entities (`ResourceEntity`), and table mappings (`clinical_resources`) to confirm the absence of database-level append-only enforcement.
- Evaluate retry and redelivery behaviors to verify duplicate generation and in-place record overwriting risks.
- Compare captured provenance against Task 04 security context concepts (requesting principal, executing principal, correlation ID, causation ID).
- Formulate and evaluate candidate immutability enforcement boundaries across REST, Themis policy, storage service, and database tiers.