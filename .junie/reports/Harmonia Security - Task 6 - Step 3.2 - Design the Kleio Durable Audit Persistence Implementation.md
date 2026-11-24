I am reviewing the repository's existing Jakarta EE resource and producer conventions to ensure the CDI DataSource injection pattern aligns with Harmonia's deployables, and I will then inspect and update the durable persistence plan accordingly.

I confirmed the CDI producer conventions across Harmonia's WildFly deployables and reviewed the exact plan sections requiring updates. Next, I will edit the plan file to decouple DataSource ownership via the `@KleioAudit` qualifier, fix the two-dimensional comparator for deterministic audit ordering, and align the PostgreSQL concurrency test with speculative lock resolution.

**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
    - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
    - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
    - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
    - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
    - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
    - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
    - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
    - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
    - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
    - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
    - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
    - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
    - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
    - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
    - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
    - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
    - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
    - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
    - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
    - Credentials / secrets: Never logged at any level.
    - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already e

**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
    - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
    - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
    - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
    - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
    - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
    - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
    - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
    - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
    - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
    - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
    - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
    - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
    - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
    - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
    - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
    - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
    - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
    - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
    - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
    - Credentials / secrets: Never logged at any level.
    - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
    - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
    - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
    - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
  `ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership and Runtime Composition Model**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables such as `iris-befe` (the Backend-For-Frontend gateway where audit events enter from the presentation tier, or where Themis audits are evaluated) and `task-processor` (Energeia Ponos workflow engine) serve as composition roots.
    - In WildFly / Jakarta EE containers, the managed `DataSource` is configured in `standalone.xml` and registered in JNDI (e.g. `java:comp/DefaultDataSource` or `java:/datasources/HarmoniaFhirDS`).
    - `kleio-persistence` provides a CDI producer class `KleioPersistenceProducer` that acquires the managed `javax.sql.DataSource` (via `@Resource(lookup = "${harmonia.audit.datasource.jndi:java:comp/DefaultDataSource}")` or programmatic JNDI lookup with fallback for tests), and produces the `FhirContext` singleton (`FhirContext.forR5()`).
    - `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository` support both CDI constructor injection (`@Inject`) and programmatic constructor invocation for standalone/test execution without container overhead.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── cdi/            # KleioPersistenceProducer (DataSource & FhirContext CDI producers)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAuditServiceH2Test.java              # Fast unit/functional tests
                └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Contracts**
1. **Canonical `AuditQuery`** (in `kleio-core`):
   ```java
   public record AuditQuery(
       String eventId,
       Instant startTime,
       Instant endTime,
       AuditClassification classification,
       String principalId,
       String targetId,
       AuditAction action,
       AuditOutcome outcome,
       String correlationId,
       String operationId,
       int limit
   ) {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Builder pattern for fluent predicate construction
   }
   ```
2. **Persistence DTO `PersistedAuditEventRow`** (in `kleio-persistence`):
   ```java
   public record PersistedAuditEventRow(
       long id,
       String fhirId,
       long versionId,
       String resourceJson,
       boolean isDeleted,
       Instant lastUpdated
   ) {}
   ```

**Serialization Boundary**
```
HarmoniaAuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.toFhir)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser())
String resourceJson  ──>  hie_fhir_resources.resource_json
```
And reverse:
```
hie_fhir_resources.resource_json
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser().parseResource)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.fromFhir)
HarmoniaAuditEvent
```
- **`FhirContext` Lifecycle**: Thread-safe singleton produced via CDI `@Produces @ApplicationScoped` or passed into constructor.
- **`IParser` Lifecycle**: Created per operation via `fhirContext.newJsonParser()` (cheap to instantiate, thread-confined).
- **Malformed Persisted Evidence**: If JSON cannot be parsed or mapped, throws `AuditIntegrityException(eventId, "Malformed persisted audit event JSON", cause)`.

**Atomic Append & Idempotency Transaction Algorithm**  
The execution flow for `DurableAuditService.append(HarmoniaAuditEvent event)`:
1. **Canonical Validation**: Validate canonical domain constraints via `Objects.requireNonNull(event, "event must not be null")`. Do NOT add artificial domain requirements (e.g. `source` is optional if allowed in canonical model).
2. **Serialization**: Convert `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)`, then serialize to JSON `resourceJson`.
3. **Transaction Start**: Method annotated with `jakarta.transaction.Transactional(Transactional.TxType.REQUIRED)`. Container begins or enlists in active JTA transaction.
4. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
   Connection is obtained from `dataSource.getConnection()`. Under `@Transactional`, this connection automatically enlists in the container JTA transaction. The repository does NOT call `conn.commit()` or `conn.rollback()`.
5. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - New row inserted.
    - Return `event`.
6. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Within the SAME transaction, query the existing row:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - **Soft-Delete Check (Fail-Closed)**: If `row.isDeleted() == true`, throw `AuditIntegrityException`:
      ```java
      throw new AuditIntegrityException(event.eventId(),
          "Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted");
      ```
      Never resurrect, overwrite, or update.
    - **Deserialization**: Parse `row.resourceJson()` to FHIR R5 `AuditEvent`, then map to `HarmoniaAuditEvent existingEvent`.
    - **Canonical Equality Comparison**:
        - If `existingEvent.equals(event)`:
          **Idempotent replay** — return `existingEvent` (no update to DB, no version bump, no timestamp change).
        - If `!existingEvent.equals(event)`:
          **Integrity violation** — throw `AuditIntegrityException(event.eventId(), "Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.
7. **Commit Semantics**:
    - When `append(...)` returns, the container's `@Transactional` interceptor issues `commit()`.
    - **Commit Failure Guarantee**: If the transaction commit fails at the container boundary (e.g., connection drop, constraint failure, two-phase commit abort), the container throws a `TransactionalException` to the caller. The caller receives failure and NEVER receives apparent append success.

**Audit Time Semantics & Keyset-Paged Query Engine (`find(AuditQuery)`)**
- **Canonical Time Semantics**:
    - `AuditQuery.startTime` and `AuditQuery.endTime` refer strictly to `HarmoniaAuditEvent.recordedAt` (domain occurrence time), NOT persistence `last_updated`.
    - Persistence `last_updated` remains purely storage metadata.
- **Deterministic Keyset Paging Algorithm**:
  To prevent false "no results" responses without loading unbounded candidate sets into memory:
  ```
  results = []
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE results.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page is empty:
          BREAK // Candidate set exhausted; return complete results

      FOR row IN page:
          cursorId = row.id()
          totalScanned++

          IF row.isDeleted():
              THROW AuditIntegrityException("Audit integrity violation: audit event with ID " + row.fhirId() + " is marked deleted")

          candidate = deserializeAndMap(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              results.add(candidate)
              IF results.size() == query.limit():
                  BREAK

      IF page.size() < pageSize:
          BREAK // Candidate set exhausted
  ```
- **Defensive Scan Ceiling Behavior**:
  If `totalScanned >= MAX_SCAN_ROWS` and `results.size() < query.limit()` while the candidate set is NOT exhausted, the query engine throws:
  `AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")`.
  The engine will NEVER silently return a truncated or incomplete result list as though the search were exhaustive.
- **Deterministic Canonical Ordering**:
  Results are sorted deterministically in Java by:
  `Comparator.comparing(HarmoniaAuditEvent::recordedAt).reversed().thenComparing(HarmoniaAuditEvent::eventId).reversed()`.

**Point Read Design (`findById` / `get`)**
- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException("Audit integrity violation: event with ID " + eventId + " exists but is marked deleted")`.
- If present and `is_deleted == false`: parses JSON to `AuditEvent`, maps to `HarmoniaAuditEvent`, and returns `Optional.of(event)`.

**Architecture Diagram**
```mermaid
graph TD
    Client[Caller / WildFly Deployable] -->|append / get / find| DAS[DurableAuditService @ApplicationScoped]
    DAS -->|@Transactional REQUIRED| TM[Jakarta Transaction Manager]
    DAS -->|toFhir / fromFhir| Mapper[HarmoniaAuditEventMapper]
    Mapper -->|R5 Model| Parser[HAPI FHIR R5 Parser]
    DAS -->|insertIfAbsent / findByEventId / findCandidatePage| Repo[JdbcAppendOnlyAuditEventRepository]
    Repo -->|Standard JDBC Connection| DS[Container Managed DataSource]
    DS -->|INSERT ON CONFLICT DO NOTHING| DB[(PostgreSQL hie_fhir_resources)]
    DB -.->|Constraint uk_resource_type_fhir_id| Repo
```

**Residual Risks & Backlog Boundaries**
- **Residual Risks (Step 04)**:
    - Legacy `AuditEventResource` in `iris-befe` and `AuditEventResourceProvider` in `mnemosyne-clinical` continue to expose mutable HTTP `PUT`/`DELETE` endpoints until Step 04 removes them.
    - `ThemisClinicalAuthorizationFilter` continues classifying `AuditEvent` as `CLINICAL` domain until Step 04 aligns it with `AUDIT`.
- **Deferred to AUDIT-BL-02**: Physical migration of audit evidence to a segregated Operations/Audit PostgreSQL database.
- **Deferred to AUDIT-BL-03**: Database-level WORM storage, SQL triggers, and cryptographic Merkle verification chains.
- **Blockers**: None. Moving from Spring to Jakarta EE 10 / CDI / standard JDBC aligns directly with Harmonia's existing WildFly architecture and avoids Spring/Hibernate rollback-only pitfalls.

**Testing**

**Validation Approach**  
Verification of the Kleio durable audit persistence implementation will be conducted using:
1. **Unit and Functional Tests**: Exercising `DurableAuditService` against an embedded H2 database running in `MODE=PostgreSQL`.
2. **Real PostgreSQL Concurrency Tests**: Validating multi-threaded race conditions and `ON CONFLICT` semantics against an actual PostgreSQL instance using Testcontainers (`org.testcontainers:postgresql:1.19.7`).
3. **Restart Durability Simulation**: Proving durability across simulated process terminations and service reinstantiations.
4. **Architecture Tests**: Using ArchUnit to assert strict subproject layering, zero Spring dependencies, and boundary isolation.

**Concrete Functional Test Scenarios (`DurableAuditServiceH2Test`)**
1. **New Event Appen

**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
    - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
    - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
    - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
    - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
    - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
    - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
    - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
    - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
    - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
    - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
    - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
    - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
    - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
    - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
    - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
    - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
    - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
    - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
    - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
    - Credentials / secrets: Never logged at any level.
    - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
    - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
    - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
    - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
  `ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership, Runtime Composition, and Qualified DataSource Injection**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **CDI Qualifier `@KleioAudit`**:
  `kleio-persistence` defines a dedicated CDI qualifier in package `net.fhirfactory.harmonia.kleio.persistence.qualifier`:
  ```java
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  public @interface KleioAudit {}
  ```
- **Qualified DataSource Consumption**:
  `JdbcAppendOnlyAuditEventRepository` consumes the managed DataSource via:
  ```java
  @Inject
  public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
      this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
  }
  ```
  `kleio-persistence` does NOT own the physical JNDI datasource name and does NOT assume or use configuration string interpolation inside `@Resource(lookup = "${...}")`.
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables (e.g. `iris-befe` or `task-processor`) serve as composition roots.
    - The composition root binds/provides the `@KleioAudit` qualified `DataSource` using standard Jakarta EE container resource conventions:
      ```java
      @ApplicationScoped
      public class AuditDataSourceProducer {
          @Resource(lookup = "java:comp/DefaultDataSource")
          private DataSource dataSource;
  
          @Produces
          @KleioAudit
          @ApplicationScoped
          public DataSource produceAuditDataSource() {
              return dataSource;
          }
      }
      ```
    - **AUDIT-BL-02 Boundary Decoupling**: Keeping physical datasource selection in the composition root ensures that relocating audit storage to a dedicated Operations/Audit database (`postgres-ops` / `java:/datasources/HarmoniaOpsDS`) requires only a composition/configuration update in the deployable, with zero code changes to `kleio-persistence`.
- **Clean Test Construction (Zero JNDI Fallback in Production)**:
  `JdbcAppendOnlyAuditEventRepository` provides a public constructor `public JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` allowing unit, functional, and Testcontainers integration tests to supply test DataSources explicitly. There is zero programmatic JNDI fallback logic in production classes.
- **`FhirContext` Lifecycle**:
  `kleio-persistence` provides `net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer` which produces the thread-safe `FhirContext.forR5()` singleton (`@Produces @ApplicationScoped`), adhering to the producer patterns established in `energeia/ponos` and `pylai`.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── qualifier/      # @KleioAudit (CDI Qualifier for audit DataSource injection)
        │   │   ├── cdi/            # FhirContextProducer (CDI producer for FhirContext singleton)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAud

**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
  - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
  - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
  - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
  - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
  - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
  - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
  - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
  - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
  - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
  - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
  - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
  - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
  - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
   - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
   - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
   - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
   - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
   - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
   - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
   - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
   - If absent: returns `Optional.empty()`.
   - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
   - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
   - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
   - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
   - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
   - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
   - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
   - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
   - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
  - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
  - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
  - Credentials / secrets: Never logged at any level.
  - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
  - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
  - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
  - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
`ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership, Runtime Composition, and Qualified DataSource Injection**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **CDI Qualifier `@KleioAudit`**:
  `kleio-persistence` defines a dedicated CDI qualifier in package `net.fhirfactory.harmonia.kleio.persistence.qualifier`:
  ```java
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  public @interface KleioAudit {}
  ```
- **Qualified DataSource Consumption**:
  `JdbcAppendOnlyAuditEventRepository` consumes the managed DataSource via:
  ```java
  @Inject
  public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
      this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
  }
  ```
  `kleio-persistence` does NOT own the physical JNDI datasource name and does NOT assume or use configuration string interpolation inside `@Resource(lookup = "${...}")`.
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables (e.g. `iris-befe` or `task-processor`) serve as composition roots.
    - The composition root binds/provides the `@KleioAudit` qualified `DataSource` using standard Jakarta EE container resource conventions:
      ```java
      @ApplicationScoped
      public class AuditDataSourceProducer {
          @Resource(lookup = "java:comp/DefaultDataSource")
          private DataSource dataSource;
  
          @Produces
          @KleioAudit
          @ApplicationScoped
          public DataSource produceAuditDataSource() {
              return dataSource;
          }
      }
      ```
    - **AUDIT-BL-02 Boundary Decoupling**: Keeping physical datasource selection in the composition root ensures that relocating audit storage to a dedicated Operations/Audit database (`postgres-ops` / `java:/datasources/HarmoniaOpsDS`) requires only a composition/configuration update in the deployable, with zero code changes to `kleio-persistence`.
- **Clean Test Construction (Zero JNDI Fallback in Production)**:
  `JdbcAppendOnlyAuditEventRepository` provides a public constructor `public JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` allowing unit, functional, and Testcontainers integration tests to supply test DataSources explicitly. There is zero programmatic JNDI fallback logic in production classes.
- **`FhirContext` Lifecycle**:
  `kleio-persistence` provides `net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer` which produces the thread-safe `FhirContext.forR5()` singleton (`@Produces @ApplicationScoped`), adhering to the producer patterns established in `energeia/ponos` and `pylai`.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── qualifier/      # @KleioAudit (CDI Qualifier for audit DataSource injection)
        │   │   ├── cdi/            # FhirContextProducer (CDI producer for FhirContext singleton)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAud

**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
  - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
  - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
  - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
  - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
  - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
  - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
  - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
  - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
  - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
  - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
  - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
  - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
  - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
   - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
   - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
   - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
   - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
   - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
   - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
   - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
   - If absent: returns `Optional.empty()`.
   - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
   - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
   - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
   - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
   - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
   - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
   - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
   - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
   - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
  - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
  - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
  - Credentials / secrets: Never logged at any level.
  - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
  - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
  - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
  - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
`ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership, Runtime Composition, and Qualified DataSource Injection**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **CDI Qualifier `@KleioAudit`**:
  `kleio-persistence` defines a dedicated CDI qualifier in package `net.fhirfactory.harmonia.kleio.persistence.qualifier`:
  ```java
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  public @interface KleioAudit {}
  ```
- **Qualified DataSource Consumption**:
  `JdbcAppendOnlyAuditEventRepository` consumes the managed DataSource via:
  ```java
  @Inject
  public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
      this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
  }
  ```
  `kleio-persistence` does NOT own the physical JNDI datasource name and does NOT assume or use configuration string interpolation inside `@Resource(lookup = "${...}")`.
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables (e.g. `iris-befe` or `task-processor`) serve as composition roots.
    - The composition root binds/provides the `@KleioAudit` qualified `DataSource` using standard Jakarta EE container resource conventions:
      ```java
      @ApplicationScoped
      public class AuditDataSourceProducer {
          @Resource(lookup = "java:comp/DefaultDataSource")
          private DataSource dataSource;
  
          @Produces
          @KleioAudit
          @ApplicationScoped
          public DataSource produceAuditDataSource() {
              return dataSource;
          }
      }
      ```
    - **AUDIT-BL-02 Boundary Decoupling**: Keeping physical datasource selection in the composition root ensures that relocating audit storage to a dedicated Operations/Audit database (`postgres-ops` / `java:/datasources/HarmoniaOpsDS`) requires only a composition/configuration update in the deployable, with zero code changes to `kleio-persistence`.
- **Clean Test Construction (Zero JNDI Fallback in Production)**:
  `JdbcAppendOnlyAuditEventRepository` provides a public constructor `public JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` allowing unit, functional, and Testcontainers integration tests to supply test DataSources explicitly. There is zero programmatic JNDI fallback logic in production classes.
- **`FhirContext` Lifecycle**:
  `kleio-persistence` provides `net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer` which produces the thread-safe `FhirContext.forR5()` singleton (`@Produces @ApplicationScoped`), adhering to the producer patterns established in `energeia/ponos` and `pylai`.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── qualifier/      # @KleioAudit (CDI Qualifier for audit DataSource injection)
        │   │   ├── cdi/            # FhirContextProducer (CDI producer for FhirContext singleton)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAuditServiceH2Test.java              # Fast unit/functional tests
                └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Contracts**
1. **Canonical `AuditQuery`** (in `kleio-core`):
   ```java
   public record AuditQuery(
       String eventId,
       Instant startTime,
       Instant endTime,
       AuditClassification classification,
       String principalId,
       String targetId,
       AuditAction action,
       AuditOutcome outcome,
       String correlationId,
       String operationId,
       int limit
   ) {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Builder pattern for fluent predicate construction
   }
   ```
2. **Persistence DTO `PersistedAuditEventRow`** (in `kleio-persistence`):
   ```java
   public record PersistedAuditEventRow(
       long id,
       String fhirId,
       long versionId,
       String resourceJson,
       boolean isDeleted,
       Instant lastUpdated
   ) {}
   ```

**Serialization Boundary**
```
HarmoniaAuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.toFhir)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser())
String resourceJson  ──>  hie_fhir_resources.resource_json
```
And reverse:
```
hie_fhir_resources.resource_json
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser().parseResource)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.fromFhir)
HarmoniaAuditEvent
```
- **`FhirContext` Lifecycle**: Thread-safe singleton produced via CDI `@Produces @ApplicationScoped` or passed into constructor.
- **`IParser` Lifecycle**: Created per operation via `fhirContext.newJsonParser()` (cheap to instantiate, thread-confined).
- **Malformed Persisted Evidence**: If JSON cannot be parsed or mapped, throws `AuditIntegrityException(eventId, "Malformed persisted audit event JSON", cause)`.

**Atomic Append & Idempotency Transaction Algorithm**  
The execution flow for `DurableAuditService.append(HarmoniaAuditEvent event)`:
1. **Canonical Validation**: Validate canonical domain constraints via `Objects.requireNonNull(event, "event must not be null")`. Do NOT add artificial domain requirements (e.g. `source` is optional if allowed in canonical model).
2. **Serialization**: Convert `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)`, then serialize to JSON `resourceJson`.
3. **Transaction Start**: Method annotated with `jakarta.transaction.Transactional(Transactional.TxType.REQUIRED)`. Container begins or enlists in active JTA transaction.
4. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
   Connection is obtained from the qualified `@KleioAudit DataSource` via `dataSource.getConnection()`. Under `@Transactional`, this connection automatically enlists in the container JTA transaction. The repository does NOT call `conn.commit()` or `conn.rollback()`.
    - **PostgreSQL Speculative Lock Resolution**: When concurrent transactions attempt to insert the same `(resource_type, fhir_id)`, PostgreSQL's unique index insertion acquires a speculative lock. Competing transactions wait and resolve safely while the active transaction completes. No deadlock occurs, and no exception is raised.
5. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - New row inserted.
    - Return `event`.
6. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - The winning insert transaction commits. The losing transaction receives `rowsAffected == 0` without transaction abort or rollback-only corruption.
    - Within the SAME transaction, the losing transaction queries the existing row:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - The winning row committed by the first transaction is now visible after conflict resolution. (Timing/immediacy is deliberately not asserted as part of the contract).
    - **Soft-Delete Check (Fail-Closed)**: If `row.isDeleted() == true`, throw `AuditIntegrityException`:
      ```java
      throw new AuditIntegrityException(event.eventId(),
          "Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted");
      ```
      Never resurrect, overwrite, or update.
    - **Deserialization**: Parse `row.resourceJson()` to FHIR R5 `AuditEvent`, then map to `HarmoniaAuditEvent existingEvent`.
    - **Canonical Equality Comparison**:
        - If `existingEvent.equals(event)`:
          **Idempotent replay** — return `existingEvent` (no update to DB, no version bump, no timestamp change).
        - If `!existingEvent.equals(event)`:
          **Integrity violation** — throw `AuditIntegrityException(event.eventId(), "Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.
7. **Commit Semantics**:
    - When `append(...)` returns, the container's `@Transactional` interceptor issues `commit()`.
    - **Commit Failure Guarantee**: If the transaction commit fails at the container boundary (e.g., connection drop, constraint failure, two-phase commit abort), the container throws a `TransactionalException` to the caller. The caller receives failure and NEVER receives apparent append success.

**Audit Time Semantics & Keyset-Paged Query Engine (`find(AuditQuery)`)**
- **Canonical Time Semantics**:
    - `AuditQuery.startTime` and `AuditQuery.endTime` refer strictly to `HarmoniaAuditEvent.recordedAt` (domain occurrence time), NOT persistence `last_updated`.
    - Persistence `last_updated` remains purely storage metadata.
- **Deterministic Keyset Paging Algorithm**:
  To prevent false "no results" responses without loading unbounded candidate sets into memory:
  ```
  results = []
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE results.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page is empty:
          BREAK // Candidate set exhausted; return complete results

      FOR row IN page:
          cursorId = row.id()
          totalScanned++

          IF row.isDeleted():
              THROW AuditIntegrityException("Audit integrity violation: audit event with ID " + row.fhirId() + " is marked deleted")

          candidate = deserializeAndMap(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              results.add(candidate)
              IF results.size() == query.limit():
                  BREAK

      IF page.size() < pageSize:
          BREAK // Candidate set exhausted
  ```
- **Defensive Scan Ceiling Behavior**:
  If `totalScanned >= MAX_SCAN_ROWS` and `results.size() < query.limit()` while the candidate set is NOT exhausted, the query engine throws:
  `AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")`.
  The engine will NEVER silently return a truncated or incomplete result list as though the search were exhaustive.
- **Deterministic Canonical Ordering**:
  Results are sorted deterministically in Java using an explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(
          HarmoniaAuditEvent::recordedAt,
          Comparator.reverseOrder())
      .thenComparing(
          HarmoniaAuditEvent::eventId,
          Comparator.reverseOrder());
  ```
  This ensures `recordedAt DESC` is primary, and `eventId DESC` is the deterministic secondary tie-breaker.
  *(Important Java distinction: Avoid `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, because the final `.reversed()` call reverses the entire composite comparator. Using `Comparator.reverseOrder()` inside each dimension inverts each dimension independently).*

**Point Read Design (`findById` / `get`**

**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
    - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
    - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
    - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
    - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
    - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
    - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
    - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
    - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
    - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
    - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
    - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
    - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
    - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
    - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
    - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
    - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
    - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
    - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
    - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
    - Credentials / secrets: Never logged at any level.
    - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
    - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
    - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
    - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
  `ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership, Runtime Composition, and Qualified DataSource Injection**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **CDI Qualifier `@KleioAudit`**:
  `kleio-persistence` defines a dedicated CDI qualifier in package `net.fhirfactory.harmonia.kleio.persistence.qualifier`:
  ```java
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  public @interface KleioAudit {}
  ```
- **Qualified DataSource Consumption**:
  `JdbcAppendOnlyAuditEventRepository` consumes the managed DataSource via:
  ```java
  @Inject
  public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
      this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
  }
  ```
  `kleio-persistence` does NOT own the physical JNDI datasource name and does NOT assume or use configuration string interpolation inside `@Resource(lookup = "${...}")`.
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables (e.g. `iris-befe` or `task-processor`) serve as composition roots.
    - The composition root binds/provides the `@KleioAudit` qualified `DataSource` using standard Jakarta EE container resource conventions:
      ```java
      @ApplicationScoped
      public class AuditDataSourceProducer {
          @Resource(lookup = "java:comp/DefaultDataSource")
          private DataSource dataSource;
  
          @Produces
          @KleioAudit
          @ApplicationScoped
          public DataSource produceAuditDataSource() {
              return dataSource;
          }
      }
      ```
    - **AUDIT-BL-02 Boundary Decoupling**: Keeping physical datasource selection in the composition root ensures that relocating audit storage to a dedicated Operations/Audit database (`postgres-ops` / `java:/datasources/HarmoniaOpsDS`) requires only a composition/configuration update in the deployable, with zero code changes to `kleio-persistence`.
- **Clean Test Construction (Zero JNDI Fallback in Production)**:
  `JdbcAppendOnlyAuditEventRepository` provides a public constructor `public JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` allowing unit, functional, and Testcontainers integration tests to supply test DataSources explicitly. There is zero programmatic JNDI fallback logic in production classes.
- **`FhirContext` Lifecycle**:
  `kleio-persistence` provides `net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer` which produces the thread-safe `FhirContext.forR5()` singleton (`@Produces @ApplicationScoped`), adhering to the producer patterns established in `energeia/ponos` and `pylai`.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── qualifier/      # @KleioAudit (CDI Qualifier for audit DataSource injection)
        │   │   ├── cdi/            # FhirContextProducer (CDI producer for FhirContext singleton)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAuditServiceH2Test.java              # Fast unit/functional tests
                └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Contracts**
1. **Canonical `AuditQuery`** (in `kleio-core`):
   ```java
   public record AuditQuery(
       String eventId,
       Instant startTime,
       Instant endTime,
       AuditClassification classification,
       String principalId,
       String targetId,
       AuditAction action,
       AuditOutcome outcome,
       String correlationId,
       String operationId,
       int limit
   ) {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Builder pattern for fluent predicate construction
   }
   ```
2. **Persistence DTO `PersistedAuditEventRow`** (in `kleio-persistence`):
   ```java
   public record PersistedAuditEventRow(
       long id,
       String fhirId,
       long versionId,
       String resourceJson,
       boolean isDeleted,
       Instant lastUpdated
   ) {}
   ```

**Serialization Boundary**
```
HarmoniaAuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.toFhir)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser())
String resourceJson  ──>  hie_fhir_resources.resource_json
```
And reverse:
```
hie_fhir_resources.resource_json
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser().parseResource)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.fromFhir)
HarmoniaAuditEvent
```
- **`FhirContext` Lifecycle**: Thread-safe singleton produced via CDI `@Produces @ApplicationScoped` or passed into constructor.
- **`IParser` Lifecycle**: Created per operation via `fhirContext.newJsonParser()` (cheap to instantiate, thread-confined).
- **Malformed Persisted Evidence**: If JSON cannot be parsed or mapped, throws `AuditIntegrityException(eventId, "Malformed persisted audit event JSON", cause)`.

**Atomic Append & Idempotency Transaction Algorithm**  
The execution flow for `DurableAuditService.append(HarmoniaAuditEvent event)`:
1. **Canonical Validation**: Validate canonical domain constraints via `Objects.requireNonNull(event, "event must not be null")`. Do NOT add artificial domain requirements (e.g. `source` is optional if allowed in canonical model).
2. **Serialization**: Convert `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)`, then serialize to JSON `resourceJson`.
3. **Transaction Start**: Method annotated with `jakarta.transaction.Transactional(Transactional.TxType.REQUIRED)`. Container begins or enlists in active JTA transaction.
4. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
   Connection is obtained from the qualified `@KleioAudit DataSource` via `dataSource.getConnection()`. Under `@Transactional`, this connection automatically enlists in the container JTA transaction. The repository does NOT call `conn.commit()` or `conn.rollback()`.
    - **PostgreSQL Speculative Lock Resolution**: When concurrent transactions attempt to insert the same `(resource_type, fhir_id)`, PostgreSQL's unique index insertion acquires a speculative lock. Competing transactions wait and resolve safely while the active transaction completes. No deadlock occurs, and no exception is raised.
5. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - New row inserted.
    - Return `event`.
6. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - The winning insert transaction commits. The losing transaction receives `rowsAffected == 0` without transaction abort or rollback-only corruption.
    - Within the SAME transaction, the losing transaction queries the existing row:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - The winning row committed by the first transaction is now visible after conflict resolution. (Timing/immediacy is deliberately not asserted as part of the contract).
    - **Soft-Delete Check (Fail-Closed)**: If `row.isDeleted() == true`, throw `AuditIntegrityException`:
      ```java
      throw new AuditIntegrityException(event.eventId(),
          "Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted");
      ```
      Never resurrect, overwrite, or update.
    - **Deserialization**: Parse `row.resourceJson()` to FHIR R5 `AuditEvent`, then map to `HarmoniaAuditEvent existingEvent`.
    - **Canonical Equality Comparison**:
        - If `existingEvent.equals(event)`:
          **Idempotent replay** — return `existingEvent` (no update to DB, no version bump, no timestamp change).
        - If `!existingEvent.equals(event)`:
          **Integrity violation** — throw `AuditIntegrityException(event.eventId(), "Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.
7. **Commit Semantics**:
    - When `append(...)` returns, the container's `@Transactional` interceptor issues `commit()`.
    - **Commit Failure Guarantee**: If the transaction commit fails at the container boundary (e.g., connection drop, constraint failure, two-phase commit abort), the container throws a `TransactionalException` to the caller. The caller receives failure and NEVER receives apparent append success.

**Audit Time Semantics & Keyset-Paged Query Engine (`find(AuditQuery)`)**
- **Canonical Time Semantics**:
    - `AuditQuery.startTime` and `AuditQuery.endTime` refer strictly to `HarmoniaAuditEvent.recordedAt` (domain occurrence time), NOT persistence `last_updated`.
    - Persistence `last_updated` remains purely storage metadata.
- **Deterministic Keyset Paging Algorithm**:
  To prevent false "no results" responses without loading unbounded candidate sets into memory:
  ```
  results = []
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE results.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page is empty:
          BREAK // Candidate set exhausted; return complete results

      FOR row IN page:
          cursorId = row.id()
          totalScanned++

          IF row.isDeleted():
              THROW AuditIntegrityException("Audit integrity violation: audit event with ID " + row.fhirId() + " is marked deleted")

          candidate = deserializeAndMap(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              results.add(candidate)
              IF results.size() == query.limit():
                  BREAK

      IF page.size() < pageSize:
          BREAK // Candidate set exhausted
  ```
- **Defensive Scan Ceiling Behavior**:
  If `totalScanned >= MAX_SCAN_ROWS` and `results.size() < query.limit()` while the candidate set is NOT exhausted, the query engine throws:
  `AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")`.
  The engine will NEVER silently return a truncated or incomplete result list as though the search were exhaustive.
- **Deterministic Canonical Ordering**:
  Results are sorted deterministically in Java using an explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(
          HarmoniaAuditEvent::recordedAt,
          Comparator.reverseOrder())
      .thenComparing(
          HarmoniaAuditEvent::eventId,
          Comparator.reverseOrder());
  ```
  This ensures `recordedAt DESC` is primary, and `eventId DESC` is the deterministic secondary tie-breaker.
  *(Important Java distinction: Avoid `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, because the final `.reversed()` call reverses the entire composite comparator. Using `Comparator.reverseOrder()` inside each dimension inverts each dimension independently).*

**Point Read Design (`findById` / `get`)**
- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException("Audit integrity violation: event with ID " + eventId + " exists but is marked deleted")`.
- If present and `is_deleted == false`: parses JSON to `AuditEvent`, maps to `HarmoniaAuditEvent`, and returns `Optional.of(event)`.

**Architecture Diagram**
```mermaid
graph TD
    Client[Caller / WildFly Deployable] -->|append / get / find| DAS[DurableAuditService @ApplicationScoped]
    DAS -->|@Transactional REQUIRED| TM[Jakarta Transaction Manager]
    DAS -->|toFhir / fromFhir| Mapper[HarmoniaAuditEventMapper]
    Mapper -->|R5 Model| Parser[HAPI FHIR R5 Parser]
    DAS -->|insertIfAbsent / findByEventId / findCandidatePage| Repo[JdbcAppendOnlyAuditEventRepository]
    Deployable[Host Deployable / Composition Root] -->|@Produces @KleioAudit| DS[Container Managed DataSource]
    DS -.->|@Inject @KleioAudit| Repo
    Repo -->|Standard JDBC Connection| DB[(PostgreSQL hie_fhir_resources)]
    DB -.->|Constraint uk_resource_type_fhir_id| Repo
```

**Residual Risks & Backlog Boundaries**
- **Residual Risks (Step 04)**:
    - Legacy `AuditEventResource` in `iris-befe` and `AuditEventResourceProvider` in `mnemosyne-clinical` continue to expose mutable HTTP `PUT`/`DELETE` endpoints until Step 04 removes them.
    - `ThemisClinicalAuthorizationFilter` continues classifying `AuditEvent` as `CLINICAL` domain until Step 04 aligns it with `AUDIT`.
- **Deferred to AUDIT-BL-02**: Physical migration of audit evidence to a segregated Operations/Audit PostgreSQL database.
- **Deferred to AUDIT-BL-03**: Database-level WORM storage, SQL triggers, and cryptographic Merkle verification chains.
- **Blockers**: None. Moving from Spring to Jakarta EE 10 / CDI / standard JDBC aligns directly with Harmonia's existing WildFly architecture and avoids Spring/Hibernate rollback-only pitfalls.

**Testing**

**Validation Approach**  
Verification of the Kleio durable audit persistence implementation will be conducted using:
1. **Unit and Functional Tests**: Exercising `DurableAuditService` against an embedded H2 database running in `MODE=PostgreSQL`.
2. **Real PostgreSQL Concurrency Tests**: Validating multi-threaded race conditions and `ON CONFLICT` semantics against an actual PostgreSQL instance using Testcontainers (`org.testcontainers:postgresql:1.19.7`).
3. **Restart Durability Simulation**: Proving durability across simulated process terminations and service reinstantiations.
4. **Architecture Tests**: Using ArchUnit to assert strict subproject layering, zero Spring dependencies, and boundary isolation.

**Concrete Functional Test Scenarios (`DurableAuditServiceH2Test`)**
1. **New Event Append**:
    - `append(newEvent)` persists record to `hie_fhir_resources`.
    - Returns successful event; database contains exactly 1 row with `version_id = 1`, `is_deleted = false`.
2. **Point Read Canonical Equality**:
    - `findById(eventId)` returns `Optional.of(event)` matching original event under `HarmoniaAuditEvent.equals()`.
3. **Identical Replay Idempotency**:
    - Calling `append(event)` twice with identical content returns success on both calls.
    - Database row count remains 1; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain unmutated.
4. **Divergent Event Conflict**:
    - Calling `append(event2)` where `event2.eventId().equals(event1.eventId())` but attributes differ throws `AuditIntegrityException`.
    - Persisted database evidence is untouched.
5. **Soft-Deleted Row Append Conflict**:
    - If a row exists with `is_deleted = true`, calling `append()` with matching `eventId` throws `AuditIntegrityException` (never resurrects or updates).
6. **Soft-Deleted Row Read Conflict**:
    - Calling `findById(deletedEventId)` throws `AuditIntegrityException`.
7. **Unknown ID Read**:
    - Calling `findById("unknown-id")` returns `Optional.empty()`.
8. **Malformed Persisted JSON**:
    - Storing corrupted JSON in `resource_json` causes `findById()` and `append()` collision resolution to fail with `AuditIntegrityException`.
9. **Query Predicate Filtering on `recordedAt`**:
    - `find(AuditQuery)` correctly filters by:
        - Canonical time range (`startTime <= recordedAt <= endTime`), completely decoupled from database `last_updated`.
        - `classification` (SECURITY, SYSTEM, CLINICAL, INTEGRATION)
        - `principalId` (matching initiating or executing principal)
        - `targetId`
        - `action` (CREATE, READ, UPDATE, DELETE, EXECUTE)
        - `outcome` (SUCCESS, DENIED, FAILED)
        - `correlationId`
        - `operationId`
10. **Keyset Paging & Two-Dimensional Query Ordering**:
    - Evaluates query ordering with test datasets containing records with identical `recordedAt` timestamps but different `eventId` values, proving that primary ordering (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate independently and deterministically.
    - Query respects `query.limit()`.
    - Paged retrieval accumulates matches across multiple pages until limit is reached or candidate set exhausted.
11. **Soft-Deleted Row in Query Candidate Stream**:
    - If a candidate row in the query stream has `is_deleted = true`, throws `AuditIntegrityException` (fail-closed integrity alarm).
12. **Scan Ceiling Enforcement**:
    - If candidate scan reaches `MAX_SCAN_ROWS` before satisfying limit on an unexhausted set, throws `AuditPersistenceException`.
13. **Database Failure Handling**:
    - If database is unavailable, `append()` throws `AuditPersistenceException` and never returns premature success.
14. **Bypass Verification**:
    - Asserts zero invocations or imports of `FhirCacheService`, Infinispan `auditevent-cache`, or `FhirRestCacheStore`.

**Real PostgreSQL Concurrency Test Strategy (`DurableAuditServicePostgreSqlConcurrencyTest`)**  
Executes against a real PostgreSQL 16 Alpine container managed by Testcontainers:
- **Scenario A: Concurrent Identical Append**:
    - 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`.
    - Assertions: Exactly 1 row physically inserted in `hie_fhir_resources`; all 10 threads receive success; metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is identical and unmutated.
- **Scenario B: Concurrent Divergent Append**:
    - 10 concurrent threads submit divergent payloads sharing the same `eventId`.
    - Assertions: Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly and is never overwritten.
- **Scenario C: `ON CONFLICT` Transaction Non-Corruption**:
    - Verifies that PostgreSQL native `ON CONFLICT DO NOTHING` returns rows affected = 0 and allows subsequent `SELECT` statements within the same transaction to succeed without `Transaction rolled back` errors.
- **Scenario D: Winning Row Visibility**:
    - Verifies that competing threads immediately read and compare against the committed winning row without isolation deadlocks.

**Process Restart Simulation**
- Initialize `DurableAuditService` instance 1 on an embedded data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Initialize fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add ArchUnit rules to `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**:
    - Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**:
    - Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Lay



**Requirements**

**Overview & Goals**  
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
    - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
    - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
    - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
    - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
    - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
    - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
    - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
    - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
    - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
    - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
    - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
    - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
    - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
    - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
    - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
    - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
    - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
    - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
    - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
    - Credentials / secrets: Never logged at any level.
    - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**  
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
    - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
    - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
    - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
  `ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership, Runtime Composition, and Qualified DataSource Injection**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **CDI Qualifier `@KleioAudit`**:
  `kleio-persistence` defines a dedicated CDI qualifier in package `net.fhirfactory.harmonia.kleio.persistence.qualifier`:
  ```java
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  public @interface KleioAudit {}
  ```
- **Qualified DataSource Consumption**:
  `JdbcAppendOnlyAuditEventRepository` consumes the managed DataSource via:
  ```java
  @Inject
  public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
      this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
  }
  ```
  `kleio-persistence` does NOT own the physical JNDI datasource name and does NOT assume or use configuration string interpolation inside `@Resource(lookup = "${...}")`.
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables (e.g. `iris-befe` or `task-processor`) serve as composition roots.
    - The composition root binds/provides the `@KleioAudit` qualified `DataSource` using standard Jakarta EE container resource conventions:
      ```java
      @ApplicationScoped
      public class AuditDataSourceProducer {
          @Resource(lookup = "java:comp/DefaultDataSource")
          private DataSource dataSource;
  
          @Produces
          @KleioAudit
          @ApplicationScoped
          public DataSource produceAuditDataSource() {
              return dataSource;
          }
      }
      ```
    - **AUDIT-BL-02 Boundary Decoupling**: Keeping physical datasource selection in the composition root ensures that relocating audit storage to a dedicated Operations/Audit database (`postgres-ops` / `java:/datasources/HarmoniaOpsDS`) requires only a composition/configuration update in the deployable, with zero code changes to `kleio-persistence`.
- **Clean Test Construction (Zero JNDI Fallback in Production)**:
  `JdbcAppendOnlyAuditEventRepository` provides a public constructor `public JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` allowing unit, functional, and Testcontainers integration tests to supply test DataSources explicitly. There is zero programmatic JNDI fallback logic in production classes.
- **`FhirContext` Lifecycle**:
  `kleio-persistence` provides `net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer` which produces the thread-safe `FhirContext.forR5()` singleton (`@Produces @ApplicationScoped`), adhering to the producer patterns established in `energeia/ponos` and `pylai`.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── qualifier/      # @KleioAudit (CDI Qualifier for audit DataSource injection)
        │   │   ├── cdi/            # FhirContextProducer (CDI producer for FhirContext singleton)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAuditServiceH2Test.java              # Fast unit/functional tests
                └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Contracts**
1. **Canonical `AuditQuery`** (in `kleio-core`):
   ```java
   public record AuditQuery(
       String eventId,
       Instant startTime,
       Instant endTime,
       AuditClassification classification,
       String principalId,
       String targetId,
       AuditAction action,
       AuditOutcome outcome,
       String correlationId,
       String operationId,
       int limit
   ) {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Builder pattern for fluent predicate construction
   }
   ```
2. **Persistence DTO `PersistedAuditEventRow`** (in `kleio-persistence`):
   ```java
   public record PersistedAuditEventRow(
       long id,
       String fhirId,
       long versionId,
       String resourceJson,
       boolean isDeleted,
       Instant lastUpdated
   ) {}
   ```

**Serialization Boundary**
```
HarmoniaAuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.toFhir)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser())
String resourceJson  ──>  hie_fhir_resources.resource_json
```
And reverse:
```
hie_fhir_resources.resource_json
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser().parseResource)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.fromFhir)
HarmoniaAuditEvent
```
- **`FhirContext` Lifecycle**: Thread-safe singleton produced via CDI `@Produces @ApplicationScoped` or passed into constructor.
- **`IParser` Lifecycle**: Created per operation via `fhirContext.newJsonParser()` (cheap to instantiate, thread-confined).
- **Malformed Persisted Evidence**: If JSON cannot be parsed or mapped, throws `AuditIntegrityException(eventId, "Malformed persisted audit event JSON", cause)`.

**Atomic Append & Idempotency Transaction Algorithm**  
The execution flow for `DurableAuditService.append(HarmoniaAuditEvent event)`:
1. **Canonical Validation**: Validate canonical domain constraints via `Objects.requireNonNull(event, "event must not be null")`. Do NOT add artificial domain requirements (e.g. `source` is optional if allowed in canonical model).
2. **Serialization**: Convert `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)`, then serialize to JSON `resourceJson`.
3. **Transaction Start**: Method annotated with `jakarta.transaction.Transactional(Transactional.TxType.REQUIRED)`. Container begins or enlists in active JTA transaction.
4. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
   Connection is obtained from the qualified `@KleioAudit DataSource` via `dataSource.getConnection()`. Under `@Transactional`, this connection automatically enlists in the container JTA transaction. The repository does NOT call `conn.commit()` or `conn.rollback()`.
    - **PostgreSQL Speculative Lock Resolution**: When concurrent transactions attempt to insert the same `(resource_type, fhir_id)`, PostgreSQL's unique index insertion acquires a speculative lock. Competing transactions wait and resolve safely while the active transaction completes. No deadlock occurs, and no exception is raised.
5. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - New row inserted.
    - Return `event`.
6. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - The winning insert transaction commits. The losing transaction receives `rowsAffected == 0` without transaction abort or rollback-only corruption.
    - Within the SAME transaction, the losing transaction queries the existing row:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - The winning row committed by the first transaction is now visible after conflict resolution. (Timing/immediacy is deliberately not asserted as part of the contract).
    - **Soft-Delete Check (Fail-Closed)**: If `row.isDeleted() == true`, throw `AuditIntegrityException`:
      ```java
      throw new AuditIntegrityException(event.eventId(),
          "Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted");
      ```
      Never resurrect, overwrite, or update.
    - **Deserialization**: Parse `row.resourceJson()` to FHIR R5 `AuditEvent`, then map to `HarmoniaAuditEvent existingEvent`.
    - **Canonical Equality Comparison**:
        - If `existingEvent.equals(event)`:
          **Idempotent replay** — return `existingEvent` (no update to DB, no version bump, no timestamp change).
        - If `!existingEvent.equals(event)`:
          **Integrity violation** — throw `AuditIntegrityException(event.eventId(), "Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.
7. **Commit Semantics**:
    - When `append(...)` returns, the container's `@Transactional` interceptor issues `commit()`.
    - **Commit Failure Guarantee**: If the transaction commit fails at the container boundary (e.g., connection drop, constraint failure, two-phase commit abort), the container throws a `TransactionalException` to the caller. The caller receives failure and NEVER receives apparent append success.

**Audit Time Semantics & Keyset-Paged Query Engine (`find(AuditQuery)`)**
- **Canonical Time Semantics**:
    - `AuditQuery.startTime` and `AuditQuery.endTime` refer strictly to `HarmoniaAuditEvent.recordedAt` (domain occurrence time), NOT persistence `last_updated`.
    - Persistence `last_updated` remains purely storage metadata.
- **Deterministic Keyset Paging Algorithm**:
  To prevent false "no results" responses without loading unbounded candidate sets into memory:
  ```
  results = []
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE results.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page is empty:
          BREAK // Candidate set exhausted; return complete results

      FOR row IN page:
          cursorId = row.id()
          totalScanned++

          IF row.isDeleted():
              THROW AuditIntegrityException("Audit integrity violation: audit event with ID " + row.fhirId() + " is marked deleted")

          candidate = deserializeAndMap(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              results.add(candidate)
              IF results.size() == query.limit():
                  BREAK

      IF page.size() < pageSize:
          BREAK // Candidate set exhausted
  ```
- **Defensive Scan Ceiling Behavior**:
  If `totalScanned >= MAX_SCAN_ROWS` and `results.size() < query.limit()` while the candidate set is NOT exhausted, the query engine throws:
  `AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")`.
  The engine will NEVER silently return a truncated or incomplete result list as though the search were exhaustive.
- **Deterministic Canonical Ordering**:
  Results are sorted deterministically in Java using an explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(
          HarmoniaAuditEvent::recordedAt,
          Comparator.reverseOrder())
      .thenComparing(
          HarmoniaAuditEvent::eventId,
          Comparator.reverseOrder());
  ```
  This ensures `recordedAt DESC` is primary, and `eventId DESC` is the deterministic secondary tie-breaker.
  *(Important Java distinction: Avoid `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, because the final `.reversed()` call reverses the entire composite comparator. Using `Comparator.reverseOrder()` inside each dimension inverts each dimension independently).*

**Point Read Design (`findById` / `get`)**
- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException("Audit integrity violation: event with ID " + eventId + " exists but is marked deleted")`.
- If present and `is_deleted == false`: parses JSON to `AuditEvent`, maps to `HarmoniaAuditEvent`, and returns `Optional.of(event)`.

**Architecture Diagram**
```mermaid
graph TD
    Client[Caller / WildFly Deployable] -->|append / get / find| DAS[DurableAuditService @ApplicationScoped]
    DAS -->|@Transactional REQUIRED| TM[Jakarta Transaction Manager]
    DAS -->|toFhir / fromFhir| Mapper[HarmoniaAuditEventMapper]
    Mapper -->|R5 Model| Parser[HAPI FHIR R5 Parser]
    DAS -->|insertIfAbsent / findByEventId / findCandidatePage| Repo[JdbcAppendOnlyAuditEventRepository]
    Deployable[Host Deployable / Composition Root] -->|@Produces @KleioAudit| DS[Container Managed DataSource]
    DS -.->|@Inject @KleioAudit| Repo
    Repo -->|Standard JDBC Connection| DB[(PostgreSQL hie_fhir_resources)]
    DB -.->|Constraint uk_resource_type_fhir_id| Repo
```

**Residual Risks & Backlog Boundaries**
- **Residual Risks (Step 04)**:
    - Legacy `AuditEventResource` in `iris-befe` and `AuditEventResourceProvider` in `mnemosyne-clinical` continue to expose mutable HTTP `PUT`/`DELETE` endpoints until Step 04 removes them.
    - `ThemisClinicalAuthorizationFilter` continues classifying `AuditEvent` as `CLINICAL` domain until Step 04 aligns it with `AUDIT`.
- **Deferred to AUDIT-BL-02**: Physical migration of audit evidence to a segregated Operations/Audit PostgreSQL database.
- **Deferred to AUDIT-BL-03**: Database-level WORM storage, SQL triggers, and cryptographic Merkle verification chains.
- **Blockers**: None. Moving from Spring to Jakarta EE 10 / CDI / standard JDBC aligns directly with Harmonia's existing WildFly architecture and avoids Spring/Hibernate rollback-only pitfalls.

**Testing**

**Validation Approach**  
Verification of the Kleio durable audit persistence implementation will be conducted using:
1. **Unit and Functional Tests**: Exercising `DurableAuditService` against an embedded H2 database running in `MODE=PostgreSQL`.
2. **Real PostgreSQL Concurrency Tests**: Validating multi-threaded race conditions and `ON CONFLICT` semantics against an actual PostgreSQL instance using Testcontainers (`org.testcontainers:postgresql:1.19.7`).
3. **Restart Durability Simulation**: Proving durability across simulated process terminations and service reinstantiations.
4. **Architecture Tests**: Using ArchUnit to assert strict subproject layering, zero Spring dependencies, and boundary isolation.

**Concrete Functional Test Scenarios (`DurableAuditServiceH2Test`)**
1. **New Event Append**:
    - `append(newEvent)` persists record to `hie_fhir_resources`.
    - Returns successful event; database contains exactly 1 row with `version_id = 1`, `is_deleted = false`.
2. **Point Read Canonical Equality**:
    - `findById(eventId)` returns `Optional.of(event)` matching original event under `HarmoniaAuditEvent.equals()`.
3. **Identical Replay Idempotency**:
    - Calling `append(event)` twice with identical content returns success on both calls.
    - Database row count remains 1; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain unmutated.
4. **Divergent Event Conflict**:
    - Calling `append(event2)` where `event2.eventId().equals(event1.eventId())` but attributes differ throws `AuditIntegrityException`.
    - Persisted database evidence is untouched.
5. **Soft-Deleted Row Append Conflict**:
    - If a row exists with `is_deleted = true`, calling `append()` with matching `eventId` throws `AuditIntegrityException` (never resurrects or updates).
6. **Soft-Deleted Row Read Conflict**:
    - Calling `findById(deletedEventId)` throws `AuditIntegrityException`.
7. **Unknown ID Read**:
    - Calling `findById("unknown-id")` returns `Optional.empty()`.
8. **Malformed Persisted JSON**:
    - Storing corrupted JSON in `resource_json` causes `findById()` and `append()` collision resolution to fail with `AuditIntegrityException`.
9. **Query Predicate Filtering on `recordedAt`**:
    - `find(AuditQuery)` correctly filters by:
        - Canonical time range (`startTime <= recordedAt <= endTime`), completely decoupled from database `last_updated`.
        - `classification` (SECURITY, SYSTEM, CLINICAL, INTEGRATION)
        - `principalId` (matching initiating or executing principal)
        - `targetId`
        - `action` (CREATE, READ, UPDATE, DELETE, EXECUTE)
        - `outcome` (SUCCESS, DENIED, FAILED)
        - `correlationId`
        - `operationId`
10. **Keyset Paging & Two-Dimensional Query Ordering**:
    - Evaluates query ordering with test datasets containing records with identical `recordedAt` timestamps but different `eventId` values, proving that primary ordering (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate independently and deterministically.
    - Query respects `query.limit()`.
    - Paged retrieval accumulates matches across multiple pages until limit is reached or candidate set exhausted.
11. **Soft-Deleted Row in Query Candidate Stream**:
    - If a candidate row in the query stream has `is_deleted = true`, throws `AuditIntegrityException` (fail-closed integrity alarm).
12. **Scan Ceiling Enforcement**:
    - If candidate scan reaches `MAX_SCAN_ROWS` before satisfying limit on an unexhausted set, throws `AuditPersistenceException`.
13. **Database Failure Handling**:
    - If database is unavailable, `append()` throws `AuditPersistenceException` and never returns premature success.
14. **Bypass Verification**:
    - Asserts zero invocations or imports of `FhirCacheService`, Infinispan `auditevent-cache`, or `FhirRestCacheStore`.

**Real PostgreSQL Concurrency Test Strategy (`DurableAuditServicePostgreSqlConcurrencyTest`)**  
Executes against a real PostgreSQL 16 Alpine container managed by Testcontainers:
- **Scenario A: Concurrent Identical Append**:
    - 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`.
    - Assertions: Exactly 1 row physically inserted in `hie_fhir_resources`; all 10 threads receive success; metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is identical and unmutated.
- **Scenario B: Concurrent Divergent Append**:
    - 10 concurrent threads submit divergent payloads sharing the same `eventId`.
    - Assertions: Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly and is never overwritten.
- **Scenario C: `ON CONFLICT` Transaction Non-Corruption**:
    - Verifies that PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` for conflicting rows and allows subsequent `SELECT` statements within the same transaction to succeed without transaction abort or `Transaction rolled back` errors.
- **Scenario D: PostgreSQL Speculative Locking & Conflict Resolution**:
    - Verifies that concurrent append transactions resolve safely without deadlocks when racing on the same `(resource_type, fhir_id)`:
        - Competing transactions wait/resolve safely during PostgreSQL unique index speculative insertion.
        - Exactly one physical insert wins (`rowsAffected == 1`).
        - Competing appends receive `rowsAffected == 0` without transaction corruption or unhandled database exceptions.
        - After the winning transaction commits and conflict resolution completes, competing transactions execute `SELECT`, successfully observe the committed winning row, and perform canonical comparison.
        - Identical evidence returns idempotent success, while divergent evidence throws `AuditIntegrityException`.
        - Proves: no overwrite, no duplicate row, no transaction corruption, no deadlocks under the tested race, winning evidence is visible after conflict resolution, and correct identical/divergent outcomes are returned. (Timing or immediacy is deliberately not asserted as part of the semantic contract).

**Process Restart Simulation**
- Initialize `DurableAuditService` instance 1 on an embedded data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Initialize fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add ArchUnit rules to `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**:
    - Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**:
    - Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**:
    - Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`.
    - Classes in `net.fhirfactory.harmonia.kleio.persistence..` must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**:
    - Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run all architecture tests including new Kleio persistence rules

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 2. Run Kleio subproject unit, integration, and PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 3. Run full repository build and test suite

mvn clean test
```

**Delivery Steps**

**Step 1: Define Kleio Persistence Module and Canonical Query Contracts**  
The `kleio/kleio-core` module defines the canonical `AuditQuery` contract, and the new `kleio/kleio-persistence` submodule is registered in parent POMs with Jakarta EE 10 and Testcontainers dependencies.

- Create canonical query model `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` with immutable builder supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
- Add `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)` to `AuditService` in `kleio-core`.
- Update `InMemoryAuditService` to support `find(AuditQuery query)` evaluating canonical predicates including `recordedAt` time filtering in-memory.
- Create POM for `kleio/kleio-persistence` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `slf4j-api`, `postgresql` (runtime), and test dependencies `h2`, `testcontainers:postgresql`, and `testcontainers:junit-jupiter`.
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` in `kleio/pom.xml` and the project root `pom.xml`.

**Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero `UPDATE` or `DELETE` capabilities and provides deterministic keyset-paged scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `id`, `fhirId`, `versionId`, `resourceJson`, `isDeleted`, and `lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `0`.
    - `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Manage connection acquisition via container-managed `DataSource.getConnection()`, allowing participation in active JTA transactions without manual commit/rollback.

**Step 3: Implement DurableAuditService and Serialization Boundary**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and Jakarta Transactions (`@Transactional`), providing durably committed appends, atomic idempotency checks, fail-closed soft-delete detection, and paged audit queries.

- Define CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` providing the singleton R5 `FhirContext`.
- Document deployable composition pattern: host WildFly deployable provides `@Produces @KleioAudit @ApplicationScoped DataSource` using `@Resource(lookup = "java:comp/DefaultDataSource")` (or configured deployment JNDI resource), leaving physical JNDI configuration outside `kleio-persistence`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`.
- Inject `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext` (configured for R5). Provide public constructor for direct test instantiation.
- Implement `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
    - Validates event non-nullity without adding artificial mandatory domain fields.
    - Serializes via `HarmoniaAuditEventMapper.toFhir()` and HAPI R5 JSON parser.
    - Invokes `insertIfAbsent()`. If `true`, transaction commits and returns `event`.
    - If `false` (conflict), queries existing row: if `is_deleted == true`, throws fail-closed `AuditIntegrityException`. Otherwise deserializes to `HarmoniaAuditEvent` and asserts `equals()`: identical returns `existingEvent` (idempotent replay, zero mutation); divergent throws `AuditIntegrityException`.
- Implement `findById(String eventId)` / `get(String eventId)`: fetches row; throws `AuditIntegrityException` if `is_deleted == true`; deserializes and returns `Optional<HarmoniaAuditEvent>`.
- Implement paged `find(AuditQuery query)`: retrieves candidate pages via keyset cursor (`id < cursorId`); aborts with `AuditIntegrityException` if soft-deleted row encountered; filters by canonical `recordedAt` and other query predicates; enforces `MAX_SCAN_ROWS = 10000` ceiling (throwing `AuditPersistenceException` if ceiling reached before exhausting candidates); sorts deterministically using explicit two-dimensional comparator (`Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`).
- Adhere strictly to Task 05 logging policy: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.

**Step 4: Build Architecture Guardrails and Comprehensive Test Suite**  
Architecture test suite, fast H2 functional tests, and Testcontainers real PostgreSQL concurrency tests verify durability, idempotency, concurrent races, restart recovery, and strict module isolation.

- Add ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java` verifying `kleio-persistence` depends only on `kleio-core`, `kleio-fhir`, and Jakarta EE APIs, with zero dependencies on `org.springframework..`, `hestia`, `iris`, or `themis-core`.
- Implement `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating functional test scenarios: new append durable commit, point read canonical equality, identical replay no-op, divergent replay `AuditIntegrityException`, soft-deleted row detection, malformed JSON handling, `recordedAt` predicate filtering, keyset paging, scan ceiling enforcement, and independent two-dimensional ordering (equal `recordedAt`, distinct `eventId`).
- Implement `DurableAuditServicePostgreSqlConcurrencyTest` against real PostgreSQL 16 using Testcontainers, verifying race safety under simultaneous identical appends (1 physical row, all threads succeed), simultaneous divergent appends (1 winner, competing threads catch `AuditIntegrityException`), `ON CONFLICT` non-corruption of JTA transactions, and speculative lock resolution (proving no overwrite, no duplicate rows, no transaction corruption, no deadlocks, and winning row visibility after conflict resolution without asserting timing or immediacy).
- Implement process restart test simulating JVM reboot by disposing and recreating service instances against the persistent data store.



I completed the revisions in the durable audit persistence plan, addressing DataSource ownership, canonical sorting, and PostgreSQL concurrency semantics. I am now submitting the updated plan for final review.

**Requirements**

**Overview & Goals**
Task 06 establishes immutable Harmonia audit evidence. Step 03.1 confirmed that the existing AuditEvent persistence path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) is mutable, acknowledges before durable commitment, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

The goal of Step 03 is to design and implement a durable, immutable, append-only persistence boundary for Kleio audit evidence in a new subproject module: `kleio/kleio-persistence` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC).

**Scope**
- **In Scope (Step 03)**:
    - Create library module `kleio/kleio-persistence` implementing `DurableAuditService` (which implements canonical `AuditService`).
    - Durable persistence transaction boundary: acknowledgement means data is durably committed to PostgreSQL before returning success.
    - Append-only standard JDBC repository adapter operating against the existing PostgreSQL clinical database (`fhir_node_*`), reusing table `hie_fhir_resources` with zero DDL migrations.
    - Atomic append with idempotent replay for identical canonical evidence, and fail-closed `AuditIntegrityException` for divergent evidence.
    - Fail-closed handling for legacy soft-deleted (`is_deleted = true`) audit rows across append, point-read, and paged query paths.
    - Durable point reads (`findById` / `get`) and bounded canonical queries (`find(AuditQuery)`) using deterministic keyset-paged scanning.
    - Correct canonical audit time filtering based on `HarmoniaAuditEvent.recordedAt` rather than persistence `last_updated`.
    - Concurrency test suite using Testcontainers PostgreSQL to verify atomic `ON CONFLICT` and thread safety against real PostgreSQL semantics.
    - Architecture tests enforcing strict unidirectional dependencies, Jakarta EE compliance, and boundary isolation.
- **Out of Scope (Deferred)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and fixing `ThemisClinicalAuthorizationFilter` security domain classification (`CLINICAL` -> `AUDIT`).
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit PostgreSQL database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM storage, SQL triggers (`BEFORE UPDATE OR DELETE RAISE EXCEPTION`), cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.

**User Stories**
- **As a Harmonia Security Auditor**, I want all security authorization decisions and system events committed durably to the database before the caller receives an acknowledgment, so that no audit evidence is ever lost due to cache crashes or queue drops.
- **As a System Integrator**, I want audit event append operations to be strictly idempotent, so that network retries of the exact same event succeed without creating duplicate rows or mutating timestamps/versions.
- **As an Evidence Compliance Officer**, I want any attempt to re-submit an existing event ID with conflicting or altered evidence to fail immediately with an `AuditIntegrityException`, so that audit tampering is detected and prevented.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to raise an integrity alarm, because an audit record marked deleted is itself evidence of unauthorized tampering.
- **As an Auditor Querying History**, I want audit queries filtered by `startTime` and `endTime` to match when the event actually occurred (`recordedAt`), rather than when the database row happened to be inserted (`last_updated`).

**Functional Requirements**
1. **Append Contract**:
    - `AuditService.append(HarmoniaAuditEvent event)` persists evidence durably.
    - Accepts every canonically valid `HarmoniaAuditEvent` without imposing additional mandatory domain fields (e.g. `AuditSource` remains optional if valid in canonical domain model).
    - If `eventId` is absent: inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction before returning.
    - If `eventId` exists and stored evidence matches canonically: returns existing event as an idempotent replay without mutating any database columns (`resource_json`, `version_id`, `last_updated`, `is_deleted` remain untouched).
    - If `eventId` exists and stored evidence diverges canonically: throws `AuditIntegrityException`.
    - If `eventId` exists and stored row has `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; never resurrects, overwrites, or clears deleted flag).
2. **Point Read Contract**:
    - `AuditService.findById(String eventId)` / `get(String eventId)` retrieves active evidence.
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted = false`: deserializes JSON and maps to `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted = true`: throws `AuditIntegrityException` (fail-closed; does not hide tampering behind 404/empty).
3. **Query Contract**:
    - `AuditService.find(AuditQuery query)` executes bounded search matching criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `HarmoniaAuditEvent.recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted = true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied and candidate set is not exhausted, throws `AuditPersistenceException` rather than silently returning a truncated/incomplete result.
    - Returns deterministic canonical chronological order (`recordedAt DESC, eventId DESC`).

**Non-Functional Requirements & Guardrails**
- **Durability**: Success returned to caller implies container-managed transaction commit (ACID durable guarantee).
- **Task 05 Logging Compliance**:
    - `DEBUG` / `TRACE`: PHI/details permitted only where deliberate, necessary, and policy-compliant.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe. No unmasked patient IDs, clinical observations, or authentication secrets.
    - Credentials / secrets: Never logged at any level.
    - Operational logs emit structural metadata (`eventId`, `action`, `outcome`, `classification`) and never dump complete JSON payloads or token credentials.
- **Concurrency & Race Safety**: Multi-threaded or multi-node concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL row/index locking without unhandled constraint violations, deadlocks, or transaction aborts. Winning evidence becomes visible to competing transactions after conflict resolution, guaranteeing identical evidence succeeds idempotently and divergent evidence throws `AuditIntegrityException`.
- **Boundary Decoupling**: Zero Spring framework dependencies in Kleio persistence. Zero compile-time dependency on Hestia/Mnemosyne, Iris, or Artemis. Zero use of Infinispan cache or write-behind.

**Technical Design**

**Current Implementation & Exploration Findings**
Step 03.1 revealed that `AuditEvent` currently shares the generic FHIR storage mechanism:
- **Table**: `hie_fhir_resources` in the Clinical PostgreSQL database (`fhir_node_*`).
- **Unique Constraint**: `uk_resource_type_fhir_id` on `(resource_type, fhir_id)` already exists.
- **Generic Service**: `FhirStorageService.createResource()` performs an in-place overwrite/upsert if `(resource_type, fhir_id)` exists.
- **Write-Behind**: `FhirRestCacheStore.write()` converts cache puts to HTTP `PUT` requests, acknowledging the caller before persistence completes and swallowing persistence errors.
- **Query Support**: `AuditEventResourceProvider.search()` only supports `_id`, `name`, and `identifier`, lacking time-range, principal, target, and correlation filtering.
- **Runtime Environment**: Harmonia deployables run on WildFly 31 (Jakarta EE 10, JDK 21). `iris-befe`, `energeia-ponos`, and `pylai-mllp-*` all use Jakarta CDI (`@ApplicationScoped`, `@Inject`, `@Produces`), Jakarta Transactions (`@Transactional`), and standard Jakarta EE container services.

**Key Decisions & Justifications**

**Decision 1: Persistence Technology — Standard JDBC with Jakarta EE 10**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`) managed by Jakarta CDI and Jakarta Transactions (`jakarta.transaction.Transactional`).
- **Alternatives Rejected**:
    - *Spring JDBC (`JdbcTemplate`) / Spring Data*: Harmonia has a deliberate architectural preference for Jakarta EE APIs and open specification/implementation separation. Mnemosyne uses Spring Boot internally, but `kleio-persistence` must remain independent of Hestia/Mnemosyne and must not introduce Spring into the Kleio subsystem.
    - *Jakarta Persistence (JPA / Hibernate)*: In JPA/Hibernate, when an `INSERT` encounters a unique constraint violation, Hibernate marks the underlying session/transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Checking duplicate IDs in JPA requires nested transactions or catching exceptions outside the transaction. Furthermore, JPA entity caching and dirty checking introduce overhead and risk accidental mutation of immutable audit records.
    - *Plain unmanaged JDBC*: Lacks container transaction synchronization and connection pooling.
- **Rationale**: Standard JDBC gives explicit, direct control over native SQL:
  ```sql
  INSERT INTO hie_fhir_resources (
      resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
  ) VALUES (
      'AuditEvent', ?, 1, ?, false, ?
  ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
  ```
  `ps.executeUpdate()` returns `1` if inserted, and `0` if a row already exists. Under PostgreSQL, `ON CONFLICT DO NOTHING` causes zero database exceptions and does not mark the JTA transaction as rollback-only. Duplicate IDs become an expected algorithmic branch (`rowsAffected == 0`).

**Decision 2: Storage Shape — Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical PostgreSQL database without DDL changes.
- **Alternatives Rejected**:
    - *Dedicated Audit Table in Step 03*: Introducing a dedicated table requires Flyway/DDL migration scripts, modifies Docker Compose initialization, and would still need to be migrated when physical relocation to the Operations database occurs in AUDIT-BL-02.
    - *Reusing `FhirResourceEntity` / `FhirStorageService`*: Directly using classes from `hestia/mnemosyne-clinical` violates ArchUnit package layering rules (`kleio` must not depend on `hestia`) and couples Kleio to mutable storage logic.
- **Rationale**: `hie_fhir_resources` already contains all necessary columns (`id`, `resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and enforces `@UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})`. By accessing it through a dedicated, insert-only JDBC repository inside `kleio-persistence`, we enforce logical immutability and zero coupling to `hestia`.

**Decision 3: Framework Ownership, Runtime Composition, and Qualified DataSource Injection**
- **Chosen Approach**: `kleio-persistence` is a library module with Jakarta CDI annotations (`beans.xml` with `bean-discovery-mode="annotated"`).
- **CDI Qualifier `@KleioAudit`**:
  `kleio-persistence` defines a dedicated CDI qualifier in package `net.fhirfactory.harmonia.kleio.persistence.qualifier`:
  ```java
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  public @interface KleioAudit {}
  ```
- **Qualified DataSource Consumption**:
  `JdbcAppendOnlyAuditEventRepository` consumes the managed DataSource via:
  ```java
  @Inject
  public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
      this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
  }
  ```
  `kleio-persistence` does NOT own the physical JNDI datasource name and does NOT assume or use configuration string interpolation inside `@Resource(lookup = "${...}")`.
- **Runtime Deployables (Composition Roots)**:
    - WildFly deployables (e.g. `iris-befe` or `task-processor`) serve as composition roots.
    - The composition root binds/provides the `@KleioAudit` qualified `DataSource` using standard Jakarta EE container resource conventions:
      ```java
      @ApplicationScoped
      public class AuditDataSourceProducer {
          @Resource(lookup = "java:comp/DefaultDataSource")
          private DataSource dataSource;
  
          @Produces
          @KleioAudit
          @ApplicationScoped
          public DataSource produceAuditDataSource() {
              return dataSource;
          }
      }
      ```
    - **AUDIT-BL-02 Boundary Decoupling**: Keeping physical datasource selection in the composition root ensures that relocating audit storage to a dedicated Operations/Audit database (`postgres-ops` / `java:/datasources/HarmoniaOpsDS`) requires only a composition/configuration update in the deployable, with zero code changes to `kleio-persistence`.
- **Clean Test Construction (Zero JNDI Fallback in Production)**:
  `JdbcAppendOnlyAuditEventRepository` provides a public constructor `public JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` allowing unit, functional, and Testcontainers integration tests to supply test DataSources explicitly. There is zero programmatic JNDI fallback logic in production classes.
- **`FhirContext` Lifecycle**:
  `kleio-persistence` provides `net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer` which produces the thread-safe `FhirContext.forR5()` singleton (`@Produces @ApplicationScoped`), adhering to the producer patterns established in `energeia/ponos` and `pylai`.
- **No Reverse Dependencies**: Kleio does NOT depend on Iris, Hestia, or Themis. Deployables depend downward on `kleio-persistence`.

**Module & Package Architecture**
```
kleio/
├── kleio-core/               # Domain models, canonical interfaces, exceptions
│   └── net.fhirfactory.harmonia.kleio.audit.
│       ├── model/            # HarmoniaAuditEvent, AuditQuery, enums
│       └── service/          # AuditService, InMemoryAuditService, AuditIntegrityException
├── kleio-fhir/               # Step 02 FHIR R5 conversion contracts
│   └── net.fhirfactory.harmonia.kleio.fhir/
│       └── HarmoniaAuditEventMapper (toFhir, fromFhir)
└── kleio-persistence/        # NEW: Step 03 Durable persistence implementation
    ├── pom.xml               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    └── src/
        ├── main/
        │   ├── java/net/fhirfactory/harmonia/kleio/persistence/
        │   │   ├── qualifier/      # @KleioAudit (CDI Qualifier for audit DataSource injection)
        │   │   ├── cdi/            # FhirContextProducer (CDI producer for FhirContext singleton)
        │   │   ├── exception/      # AuditPersistenceException
        ���   │   ├── model/          # PersistedAuditEventRow
        │   │   ├── repository/     # AppendOnlyAuditEventRepository, JdbcAppendOnlyAuditEventRepository
        │   │   └── service/        # DurableAuditService
        │   └── resources/META-INF/
        │       └── beans.xml       # CDI 4.0 bean-discovery-mode="annotated"
        └── test/
            └── java/net/fhirfactory/harmonia/kleio/persistence/
                ├── DurableAuditServiceH2Test.java              # Fast unit/functional tests
                └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Contracts**
1. **Canonical `AuditQuery`** (in `kleio-core`):
   ```java
   public record AuditQuery(
       String eventId,
       Instant startTime,
       Instant endTime,
       AuditClassification classification,
       String principalId,
       String targetId,
       AuditAction action,
       AuditOutcome outcome,
       String correlationId,
       String operationId,
       int limit
   ) {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Builder pattern for fluent predicate construction
   }
   ```
2. **Persistence DTO `PersistedAuditEventRow`** (in `kleio-persistence`):
   ```java
   public record PersistedAuditEventRow(
       long id,
       String fhirId,
       long versionId,
       String resourceJson,
       boolean isDeleted,
       Instant lastUpdated
   ) {}
   ```

**Serialization Boundary**
```
HarmoniaAuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.toFhir)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser())
String resourceJson  ──>  hie_fhir_resources.resource_json
```
And reverse:
```
hie_fhir_resources.resource_json
       │
       ▼ (HAPI R5 JsonParser via fhirContext.newJsonParser().parseResource)
org.hl7.fhir.r5.model.AuditEvent
       │
       ▼ (HarmoniaAuditEventMapper.fromFhir)
HarmoniaAuditEvent
```
- **`FhirContext` Lifecycle**: Thread-safe singleton produced via CDI `@Produces @ApplicationScoped` or passed into constructor.
- **`IParser` Lifecycle**: Created per operation via `fhirContext.newJsonParser()` (cheap to instantiate, thread-confined).
- **Malformed Persisted Evidence**: If JSON cannot be parsed or mapped, throws `AuditIntegrityException(eventId, "Malformed persisted audit event JSON", cause)`.

**Atomic Append & Idempotency Transaction Algorithm**
The execution flow for `DurableAuditService.append(HarmoniaAuditEvent event)`:
1. **Canonical Validation**: Validate canonical domain constraints via `Objects.requireNonNull(event, "event must not be null")`. Do NOT add artificial domain requirements (e.g. `source` is optional if allowed in canonical model).
2. **Serialization**: Convert `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)`, then serialize to JSON `resourceJson`.
3. **Transaction Start**: Method annotated with `jakarta.transaction.Transactional(Transactional.TxType.REQUIRED)`. Container begins or enlists in active JTA transaction.
4. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
   Connection is obtained from the qualified `@KleioAudit DataSource` via `dataSource.getConnection()`. Under `@Transactional`, this connection automatically enlists in the container JTA transaction. The repository does NOT call `conn.commit()` or `conn.rollback()`.
    - **PostgreSQL Speculative Lock Resolution**: When concurrent transactions attempt to insert the same `(resource_type, fhir_id)`, PostgreSQL's unique index insertion acquires a speculative lock. Competing transactions wait and resolve safely while the active transaction completes. No deadlock occurs, and no exception is raised.
5. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - New row inserted.
    - Return `event`.
6. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - The winning insert transaction commits. The losing transaction receives `rowsAffected == 0` without transaction abort or rollback-only corruption.
    - Within the SAME transaction, the losing transaction queries the existing row:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - The winning row committed by the first transaction is now visible after conflict resolution. (Timing/immediacy is deliberately not asserted as part of the contract).
    - **Soft-Delete Check (Fail-Closed)**: If `row.isDeleted() == true`, throw `AuditIntegrityException`:
      ```java
      throw new AuditIntegrityException(event.eventId(),
          "Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted");
      ```
      Never resurrect, overwrite, or update.
    - **Deserialization**: Parse `row.resourceJson()` to FHIR R5 `AuditEvent`, then map to `HarmoniaAuditEvent existingEvent`.
    - **Canonical Equality Comparison**:
        - If `existingEvent.equals(event)`:
          **Idempotent replay** — return `existingEvent` (no update to DB, no version bump, no timestamp change).
        - If `!existingEvent.equals(event)`:
          **Integrity violation** — throw `AuditIntegrityException(event.eventId(), "Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.
7. **Commit Semantics**:
    - When `append(...)` returns, the container's `@Transactional` interceptor issues `commit()`.
    - **Commit Failure Guarantee**: If the transaction commit fails at the container boundary (e.g., connection drop, constraint failure, two-phase commit abort), the container throws a `TransactionalException` to the caller. The caller receives failure and NEVER receives apparent append success.

**Audit Time Semantics & Keyset-Paged Query Engine (`find(AuditQuery)`)**
- **Canonical Time Semantics**:
    - `AuditQuery.startTime` and `AuditQuery.endTime` refer strictly to `HarmoniaAuditEvent.recordedAt` (domain occurrence time), NOT persistence `last_updated`.
    - Persistence `last_updated` remains purely storage metadata.
- **Deterministic Keyset Paging Algorithm**:
  To prevent false "no results" responses without loading unbounded candidate sets into memory:
  ```
  results = []
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE results.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page is empty:
          BREAK // Candidate set exhausted; return complete results

      FOR row IN page:
          cursorId = row.id()
          totalScanned++

          IF row.isDeleted():
              THROW AuditIntegrityException("Audit integrity violation: audit event with ID " + row.fhirId() + " is marked deleted")

          candidate = deserializeAndMap(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              results.add(candidate)
              IF results.size() == query.limit():
                  BREAK

      IF page.size() < pageSize:
          BREAK // Candidate set exhausted
  ```
- **Defensive Scan Ceiling Behavior**:
  If `totalScanned >= MAX_SCAN_ROWS` and `results.size() < query.limit()` while the candidate set is NOT exhausted, the query engine throws:
  `AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")`.
  The engine will NEVER silently return a truncated or incomplete result list as though the search were exhaustive.
- **Deterministic Canonical Ordering**:
  Results are sorted deterministically in Java using an explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(
          HarmoniaAuditEvent::recordedAt,
          Comparator.reverseOrder())
      .thenComparing(
          HarmoniaAuditEvent::eventId,
          Comparator.reverseOrder());
  ```
  This ensures `recordedAt DESC` is primary, and `eventId DESC` is the deterministic secondary tie-breaker.
  *(Important Java distinction: Avoid `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, because the final `.reversed()` call reverses the entire composite comparator. Using `Comparator.reverseOrder()` inside each dimension inverts each dimension independently).*

**Point Read Design (`findById` / `get`)**
- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException("Audit integrity violation: event with ID " + eventId + " exists but is marked deleted")`.
- If present and `is_deleted == false`: parses JSON to `AuditEvent`, maps to `HarmoniaAuditEvent`, and returns `Optional.of(event)`.

**Architecture Diagram**
```mermaid
graph TD
    Client[Caller / WildFly Deployable] -->|append / get / find| DAS[DurableAuditService @ApplicationScoped]
    DAS -->|@Transactional REQUIRED| TM[Jakarta Transaction Manager]
    DAS -->|toFhir / fromFhir| Mapper[HarmoniaAuditEventMapper]
    Mapper -->|R5 Model| Parser[HAPI FHIR R5 Parser]
    DAS -->|insertIfAbsent / findByEventId / findCandidatePage| Repo[JdbcAppendOnlyAuditEventRepository]
    Deployable[Host Deployable / Composition Root] -->|@Produces @KleioAudit| DS[Container Managed DataSource]
    DS -.->|@Inject @KleioAudit| Repo
    Repo -->|Standard JDBC Connection| DB[(PostgreSQL hie_fhir_resources)]
    DB -.->|Constraint uk_resource_type_fhir_id| Repo
```

**Residual Risks & Backlog Boundaries**
- **Residual Risks (Step 04)**:
    - Legacy `AuditEventResource` in `iris-befe` and `AuditEventResourceProvider` in `mnemosyne-clinical` continue to expose mutable HTTP `PUT`/`DELETE` endpoints until Step 04 removes them.
    - `ThemisClinicalAuthorizationFilter` continues classifying `AuditEvent` as `CLINICAL` domain until Step 04 aligns it with `AUDIT`.
- **Deferred to AUDIT-BL-02**: Physical migration of audit evidence to a segregated Operations/Audit PostgreSQL database.
- **Deferred to AUDIT-BL-03**: Database-level WORM storage, SQL triggers, and cryptographic Merkle verification chains.
- **Blockers**: None. Moving from Spring to Jakarta EE 10 / CDI / standard JDBC aligns directly with Harmonia's existing WildFly architecture and avoids Spring/Hibernate rollback-only pitfalls.

**Testing**

**Validation Approach**
Verification of the Kleio durable audit persistence implementation will be conducted using:
1. **Unit and Functional Tests**: Exercising `DurableAuditService` against an embedded H2 database running in `MODE=PostgreSQL`.
2. **Real PostgreSQL Concurrency Tests**: Validating multi-threaded race conditions and `ON CONFLICT` semantics against an actual PostgreSQL instance using Testcontainers (`org.testcontainers:postgresql:1.19.7`).
3. **Restart Durability Simulation**: Proving durability across simulated process terminations and service reinstantiations.
4. **Architecture Tests**: Using ArchUnit to assert strict subproject layering, zero Spring dependencies, and boundary isolation.

**Concrete Functional Test Scenarios (`DurableAuditServiceH2Test`)**
1. **New Event Append**:
    - `append(newEvent)` persists record to `hie_fhir_resources`.
    - Returns successful event; database contains exactly 1 row with `version_id = 1`, `is_deleted = false`.
2. **Point Read Canonical Equality**:
    - `findById(eventId)` returns `Optional.of(event)` matching original event under `HarmoniaAuditEvent.equals()`.
3. **Identical Replay Idempotency**:
    - Calling `append(event)` twice with identical content returns success on both calls.
    - Database row count remains 1; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain unmutated.
4. **Divergent Event Conflict**:
    - Calling `append(event2)` where `event2.eventId().equals(event1.eventId())` but attributes differ throws `AuditIntegrityException`.
    - Persisted database evidence is untouched.
5. **Soft-Deleted Row Append Conflict**:
    - If a row exists with `is_deleted = true`, calling `append()` with matching `eventId` throws `AuditIntegrityException` (never resurrects or updates).
6. **Soft-Deleted Row Read Conflict**:
    - Calling `findById(deletedEventId)` throws `AuditIntegrityException`.
7. **Unknown ID Read**:
    - Calling `findById("unknown-id")` returns `Optional.empty()`.
8. **Malformed Persisted JSON**:
    - Storing corrupted JSON in `resource_json` causes `findById()` and `append()` collision resolution to fail with `AuditIntegrityException`.
9. **Query Predicate Filtering on `recordedAt`**:
    - `find(AuditQuery)` correctly filters by:
        - Canonical time range (`startTime <= recordedAt <= endTime`), completely decoupled from database `last_updated`.
        - `classification` (SECURITY, SYSTEM, CLINICAL, INTEGRATION)
        - `principalId` (matching initiating or executing principal)
        - `targetId`
        - `action` (CREATE, READ, UPDATE, DELETE, EXECUTE)
        - `outcome` (SUCCESS, DENIED, FAILED)
        - `correlationId`
        - `operationId`
10. **Keyset Paging & Two-Dimensional Query Ordering**:
    - Evaluates query ordering with test datasets containing records with identical `recordedAt` timestamps but different `eventId` values, proving that primary ordering (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate independently and deterministically.
    - Query respects `query.limit()`.
    - Paged retrieval accumulates matches across multiple pages until limit is reached or candidate set exhausted.
11. **Soft-Deleted Row in Query Candidate Stream**:
    - If a candidate row in the query stream has `is_deleted = true`, throws `AuditIntegrityException` (fail-closed integrity alarm).
12. **Scan Ceiling Enforcement**:
    - If candidate scan reaches `MAX_SCAN_ROWS` before satisfying limit on an unexhausted set, throws `AuditPersistenceException`.
13. **Database Failure Handling**:
    - If database is unavailable, `append()` throws `AuditPersistenceException` and never returns premature success.
14. **Bypass Verification**:
    - Asserts zero invocations or imports of `FhirCacheService`, Infinispan `auditevent-cache`, or `FhirRestCacheStore`.

**Real PostgreSQL Concurrency Test Strategy (`DurableAuditServicePostgreSqlConcurrencyTest`)**
Executes against a real PostgreSQL 16 Alpine container managed by Testcontainers:
- **Scenario A: Concurrent Identical Append**:
    - 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`.
    - Assertions: Exactly 1 row physically inserted in `hie_fhir_resources`; all 10 threads receive success; metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is identical and unmutated.
- **Scenario B: Concurrent Divergent Append**:
    - 10 concurrent threads submit divergent payloads sharing the same `eventId`.
    - Assertions: Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly and is never overwritten.
- **Scenario C: `ON CONFLICT` Transaction Non-Corruption**:
    - Verifies that PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` for conflicting rows and allows subsequent `SELECT` statements within the same transaction to succeed without transaction abort or `Transaction rolled back` errors.
- **Scenario D: PostgreSQL Speculative Locking & Conflict Resolution**:
    - Verifies that concurrent append transactions resolve safely without deadlocks when racing on the same `(resource_type, fhir_id)`:
        - Competing transactions wait/resolve safely during PostgreSQL unique index speculative insertion.
        - Exactly one physical insert wins (`rowsAffected == 1`).
        - Competing appends receive `rowsAffected == 0` without transaction corruption or unhandled database exceptions.
        - After the winning transaction commits and conflict resolution completes, competing transactions execute `SELECT`, successfully observe the committed winning row, and perform canonical comparison.
        - Identical evidence returns idempotent success, while divergent evidence throws `AuditIntegrityException`.
        - Proves: no overwrite, no duplicate row, no transaction corruption, no deadlocks under the tested race, winning evidence is visible after conflict resolution, and correct identical/divergent outcomes are returned. (Timing or immediacy is deliberately not asserted as part of the semantic contract).

**Process Restart Simulation**
- Initialize `DurableAuditService` instance 1 on an embedded data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Initialize fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**
Add ArchUnit rules to `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**:
    - Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**:
    - Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**:
    - Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`.
    - Classes in `net.fhirfactory.harmonia.kleio.persistence..` must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**:
    - Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run all architecture tests including new Kleio persistence rules

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 2. Run Kleio subproject unit, integration, and PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 3. Run full repository build and test suite

mvn clean test
```

**Delivery Steps**

**Step 1: Define Kleio Persistence Module and Canonical Query Contracts**
The `kleio/kleio-core` module defines the canonical `AuditQuery` contract, and the new `kleio/kleio-persistence` submodule is registered in parent POMs with Jakarta EE 10 and Testcontainers dependencies.

- Create canonical query model `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` with immutable builder supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
- Add `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)` to `AuditService` in `kleio-core`.
- Update `InMemoryAuditService` to support `find(AuditQuery query)` evaluating canonical predicates including `recordedAt` time filtering in-memory.
- Create POM for `kleio/kleio-persistence` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `slf4j-api`, `postgresql` (runtime), and test dependencies `h2`, `testcontainers:postgresql`, and `testcontainers:junit-jupiter`.
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` in `kleio/pom.xml` and the project root `pom.xml`.

**Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero `UPDATE` or `DELETE` capabilities and provides deterministic keyset-paged scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `id`, `fhirId`, `versionId`, `resourceJson`, `isDeleted`, and `lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `0`.
    - `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Manage connection acquisition via container-managed `DataSource.getConnection()`, allowing participation in active JTA transactions without manual commit/rollback.

**Step 3: Implement DurableAuditService and Serialization Boundary**
`DurableAuditService` implements `AuditService` using Jakarta CDI and Jakarta Transactions (`@Transactional`), providing durably committed appends, atomic idempotency checks, fail-closed soft-delete detection, and paged audit queries.

- Define CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` providing the singleton R5 `FhirContext`.
- Document deployable composition pattern: host WildFly deployable provides `@Produces @KleioAudit @ApplicationScoped DataSource` using `@Resource(lookup = "java:comp/DefaultDataSource")` (or configured deployment JNDI resource), leaving physical JNDI configuration outside `kleio-persistence`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`.
- Inject `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext` (configured for R5). Provide public constructor for direct test instantiation.
- Implement `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
    - Validates event non-nullity without adding artificial mandatory domain fields.
    - Serializes via `HarmoniaAuditEventMapper.toFhir()` and HAPI R5 JSON parser.
    - Invokes `insertIfAbsent()`. If `true`, transaction commits and returns `event`.
    - If `false` (conflict), queries existing row: if `is_deleted == true`, throws fail-closed `AuditIntegrityException`. Otherwise deserializes to `HarmoniaAuditEvent` and asserts `equals()`: identical returns `existingEvent` (idempotent replay, zero mutation); divergent throws `AuditIntegrityException`.
- Implement `findById(String eventId)` / `get(String eventId)`: fetches row; throws `AuditIntegrityException` if `is_deleted == true`; deserializes and returns `Optional<HarmoniaAuditEvent>`.
- Implement paged `find(AuditQuery query)`: retrieves candidate pages via keyset cursor (`id < cursorId`); aborts with `AuditIntegrityException` if soft-deleted row encountered; filters by canonical `recordedAt` and other query predicates; enforces `MAX_SCAN_ROWS = 10000` ceiling (throwing `AuditPersistenceException` if ceiling reached before exhausting candidates); sorts deterministically using explicit two-dimensional comparator (`Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`).
- Adhere strictly to Task 05 logging policy: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.

**Step 4: Build Architecture Guardrails and Comprehensive Test Suite**
Architecture test suite, fast H2 functional tests, and Testcontainers real PostgreSQL concurrency tests verify durability, idempotency, concurrent races, restart recovery, and strict module isolation.

- Add ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java` verifying `kleio-persistence` depends only on `kleio-core`, `kleio-fhir`, and Jakarta EE APIs, with zero dependencies on `org.springframework..`, `hestia`, `iris`, or `themis-core`.
- Implement `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating functional test scenarios: new append durable commit, point read canonical equality, identical replay no-op, divergent replay `AuditIntegrityException`, soft-deleted row detection, malformed JSON handling, `recordedAt` predicate filtering, keyset paging, scan ceiling enforcement, and independent two-dimensional ordering (equal `recordedAt`, distinct `eventId`).
- Implement `DurableAuditServicePostgreSqlConcurrencyTest` against real PostgreSQL 16 using Testcontainers, verifying race safety under simultaneous identical appends (1 physical row, all threads succeed), simultaneous divergent appends (1 winner, competing threads catch `AuditIntegrityException`), `ON CONFLICT` non-corruption of JTA transactions, and speculative lock resolution (proving no overwrite, no duplicate rows, no transaction corruption, no deadlocks, and winning row visibility after conflict resolution without asserting timing or immediacy).
- Implement process restart test simulating JVM reboot by disposing and recreating service instances against the persistent data store.