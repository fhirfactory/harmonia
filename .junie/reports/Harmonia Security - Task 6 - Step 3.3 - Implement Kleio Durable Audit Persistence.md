I will investigate the existing kleio module structure, the root pom.xml, and existing architecture tests in paradeigma to ground the implementation plan in the current codebase state.

I verified the hie_fhir_resources table structure, Docker environment availability for PostgreSQL Testcontainers, and the existing architecture test conventions across the repository. I am now submitting the comprehensive delivery plan and technical proposal to implement durable append-only Kleio audit persistence.**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**  
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**  
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**  
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

**Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**  
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

**Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

**Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

**Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**  
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.



**Requirements**

**Overview & Goals**
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

**Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

**Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

**Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

**Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.

**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**  
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**  
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**  
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

*** Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**  
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

**Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

**Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

**Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**  
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.



Now I'm opening the implementation plan document at line 260 to understand the specific details and context of the durable audit persistence feature I need to work on. This will help me grasp the requirements and design decisions before proceeding.

I examined the existing audit contracts and in-memory implementation in kleio-core. Next, I will check the available model types in kleio-core and implement the canonical AuditQuery record with its builder and matching logic.

I verified the domain models and matching logic for audit events and targets. Next, I will inspect AuditServiceTest in kleio-core to understand existing test patterns before adding AuditQuery and implementing the new search contracts.

I created the AuditQuery model and updated the core AuditService interface. Next, I will implement find(AuditQuery) in InMemoryAuditService using canonical chronological ordering and verify all existing unit tests in kleio-core.

I configured the canonical sort comparator in the in-memory audit service. Next, I will implement the find(AuditQuery) method and run the kleio-core unit tests to confirm basic functionality.

I verified the query model and in-memory service changes with comprehensive unit tests. Next, I will inspect the project POM files and scaffold the new kleio-persistence module with its configuration and dependencies.

I reviewed the root and subsystem Maven definitions. Next, I will check the dependency properties and register kleio-persistence in the root and kleio parent POMs.

I updated the root POM properties and dependency declarations. Next, I will add Testcontainers dependency management and configure kleio/pom.xml with the new submodule.

I created the build configuration for the persistence submodule. Next, I will add the CDI beans configuration and verify that the reactor compiles cleanly.

I completed the query contracts and submodule scaffolding for Step 1. Next, I will transition the plan status to mark Step 1 complete and begin Step 2: implementing the append-only JDBC repository and keyset scanner.

**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-

**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**  
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**  
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**  
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

**✓ Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**  
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

*** Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

**Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

**Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**  
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.



I transitioned the plan to Step 2. Next, I will create the KleioAudit CDI qualifier, AuditPersistenceException, and PersistedAuditEventRow DTO required for the persistence repository.

I created the append-only repository contract. Next, I will implement JdbcAppendOnlyAuditEventRepository using standard JDBC with qualified DataSource injection and write unit tests for it.

I created the JDBC repository implementation and its unit test suite. Next, I will run the tests to confirm that parameter binding, keyset paging, and exception translation work as expected.

I set up a targeted test to check H2 SQL dialect compatibility for ON CONFLICT statements. Next, I will execute the test to determine the exact syntax supported by H2 in PostgreSQL mode.

I updated the test probe with the necessary imports. Next, I will run the test to compare the execution of ON CONFLICT DO NOTHING with and without conflict targets.

I aligned the repository insert statement with universal PostgreSQL and H2 ON CONFLICT DO NOTHING semantics. Next, I will run the full unit test suite for the persistence module to verify all repository contracts.

**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**  
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**  
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**  
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

**✓ Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**  
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

**✓ Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

*** Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

**Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**  
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.



I verified the exception constructors and mapper contracts. Next, I will create the FhirContextProducer CDI bean and implement DurableAuditService according to the approved transactional and keyset scanning algorithms.

I implemented the DurableAuditService and created the full H2 functional test suite. Next, I will run the tests to validate durability, replay idempotency, tamper detection, keyset scanning, and restart recovery.

**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**  
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**  
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**  
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

**✓ Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**  
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

**✓ Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

**✓ Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

*** Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**  
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.



I confirmed that the H2 functional test suite passes all 13 test scenarios. Next, I will check Docker daemon connectivity and implement the PostgreSQL Testcontainers concurrency test suite.

I created the real PostgreSQL concurrency test suite for Testcontainers. Next, I will add the missing import and run the PostgreSQL concurrency tests to verify speculative locking and transaction non-corruption.

I added logging dependencies to the persistence test configuration to capture Docker client diagnostics. Now I will rerun the PostgreSQL concurrency test to see why Testcontainers was unable to connect to the Docker daemon.

I diagnosed the Docker client connection error: Docker 29 enforces a minimum API version of 1.44, which requires pinning the Docker client API version. Next, I will add a test configuration file to specify the API version and re-run the PostgreSQL concurrency suite.

I confirmed that the PostgreSQL concurrency tests pass for speculative locking, identical replay, and collision handling. Now I will inspect the architecture test dependencies to ensure paradeigma-test can analyze kleio-persistence for the architectural guardrail rules.

I verified the existing architectural tests in paradeigma-test. Next, I will add the four ArchUnit rules to SecurityEnforcementArchitectureTest enforcing Kleio core/FHIR independence, persistence layering, and decoupling from other subsystems.

**Requirements**

**Overview & Goals**  
In healthcare integration environments, audit evidence is the foundation of patient safety, clinical accountability, and regulatory compliance. An audit system that acknowledges receipt before durable persistence, relies on asynchronous write-behind caches, or allows in-place overwrites poses catastrophic systemic risk: audit evidence can vanish during unexpected outages, or be silently corrupted.

Task 06 establishes an immutable, durable audit evidence boundary for the Harmonia Health Integration Environment. Investigation in Step 03.1 revealed that the legacy `AuditEvent` path (Iris BEFE -> Infinispan cache -> write-behind -> HTTP PUT -> Mnemosyne -> PostgreSQL `hie_fhir_resources`) acknowledges callers before persistence, uses asynchronous write-behind, and permits in-place overwrites and soft deletes.

Step 03.3 delivers the implementation of the approved Step 03.2 architectural plan. It creates a dedicated library module `kleio/kleio-persistence` implementing `DurableAuditService` using standard Jakarta EE 10 APIs (CDI 4.0, Jakarta Transactions 2.0, container-managed DataSource, and standard JDBC). The implementation guarantees that acknowledgement signifies durable persistence in PostgreSQL, enforces atomic append with idempotent replay, detects tampering on divergent replays and soft-deleted rows, executes bounded keyset candidate scanning with deterministic chronological ordering, and prevents deadlocks under concurrent execution.

**Scope**
- **In Scope (Step 03.3)**:
    - Implement `kleio/kleio-persistence` module and register it in parent POMs.
    - Implement CDI qualifier `@KleioAudit` for decoupled container DataSource consumption.
    - Implement `PersistedAuditEventRow` DTO and `AuditPersistenceException`.
    - Implement `AppendOnlyAuditEventRepository` and `JdbcAppendOnlyAuditEventRepository` executing standard JDBC against table `hie_fhir_resources`.
    - Implement `FhirContextProducer` for thread-safe R5 parser lifecycle management.
    - Implement `DurableAuditService` supporting `@Transactional(TxType.REQUIRED)` durable append, atomic idempotent replay, fail-closed handling of soft-deleted rows, deterministic keyset-paged search, and point reads.
    - Update `kleio-core` with canonical `AuditQuery` model and extend `AuditService` and `InMemoryAuditService`.
    - Ensure canonical ordering: explicit two-dimensional comparator (`recordedAt DESC, eventId DESC`).
    - Strict canonical ID preservation: `HarmoniaAuditEvent.eventId` is mandatory and must never be generated, normalized, or replaced by `DurableAuditService`.
    - Comprehensive H2 functional test suite (`DurableAuditServiceH2Test`) and process restart recovery simulation.
    - Real PostgreSQL concurrency test suite (`DurableAuditServicePostgreSqlConcurrencyTest`) using Testcontainers PostgreSQL.
    - ArchUnit architecture guardrails in `SecurityEnforcementArchitectureTest`.
- **Out of Scope (Deferred to Step 04 & Future Milestones)**:
    - Step 04: Removing legacy Iris BEFE mutation endpoints (`PUT`/`DELETE /api/fhir/AuditEvent`), removing UI delete buttons, disabling Mnemosyne provider `@Update`/`@Delete`, and realigning `ThemisClinicalAuthorizationFilter` security domain from `CLINICAL` to `AUDIT`.
    - AUDIT-BL-02: Physical relocation of audit records to a dedicated Operations/Audit database (`postgres-ops` / `ops_node_*`).
    - AUDIT-BL-03: Database-level WORM triggers, SQL immutable constraints, cryptographic hash chains, or Merkle evidence trees.
    - Tasks 07–10: Volatile fallback removal, authoritative clinical writes, cache-aside reads, and authoritative clinical search.
    - Implementation of Provenance resources.

**User Stories**
- **As a Healthcare Security Officer**, I want every security authorization decision and administrative action durably committed to PostgreSQL before success is acknowledged, so that system crashes or network partitions never cause loss of critical forensic evidence.
- **As a System Integrator**, I want audit append operations to be strictly idempotent for identical events, so that upstream retries following network glitches succeed without creating duplicate rows or corrupting timestamps.
- **As a Forensic Compliance Auditor**, I want any attempt to re-submit an existing event ID with altered attributes to fail immediately with an `AuditIntegrityException`, so that malicious tampering or accidental collision is instantly detected and blocked.
- **As an Incident Investigator**, I want any encounter with a soft-deleted audit record to trigger an immediate integrity exception, because deletion of audit history is prima facie evidence of unauthorized interference.
- **As an Auditor Querying History**, I want audit searches by time range to filter strictly on when the event actually occurred (`HarmoniaAuditEvent.recordedAt`) rather than when the database row was written (`last_updated`), and I want results ordered deterministically (`recordedAt DESC, eventId DESC`).

**Functional Requirements**
1. **Append Contract (`append(HarmoniaAuditEvent event)`)**:
    - Persists evidence durably before returning success to the caller.
    - Accepts every canonically valid `HarmoniaAuditEvent` without enforcing artificial domain restrictions (e.g. `source` remains optional if allowed by the canonical model).
    - `DurableAuditService` MUST NOT generate an `eventId`, MUST NOT normalize an `eventId`, and MUST NOT replace an `eventId`. The event supplied must already contain its valid canonical `eventId`.
    - New Evidence (`rowsAffected == 1`): inserts record with `version_id = 1`, `is_deleted = false`, `last_updated = timestamp`, and commits container transaction.
    - Existing Evidence (`rowsAffected == 0`): retrieves existing row within the same transaction.
        - If stored row has `is_deleted == true`: throws `AuditIntegrityException` (fail-closed; never resurrects or overwrites).
        - Deserializes FHIR JSON to `HarmoniaAuditEvent existingEvent`.
        - Canonical equality comparison:
            - If `existingEvent.equals(event)`: idempotent success — returns `existingEvent` without mutating database columns (`version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched).
            - If `!existingEvent.equals(event)`: integrity violation — throws `AuditIntegrityException`.
2. **Point Read Contract (`findById(String eventId)` / `get(String eventId)`)**:
    - If absent: returns `Optional.empty()`.
    - If present and `is_deleted == false`: deserializes JSON and returns `Optional.of(HarmoniaAuditEvent)`.
    - If present and `is_deleted == true`: throws `AuditIntegrityException` (fail-closed).
    - If present but JSON is corrupted: throws `AuditIntegrityException`.
3. **Query Contract (`find(AuditQuery query)`)**:
    - Matches criteria: `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit`.
    - Evaluates `startTime` and `endTime` strictly against canonical `recordedAt`, never against persistence `last_updated`.
    - Uses paged keyset candidate scanning (`id < cursorId ORDER BY id DESC LIMIT pageSize`) to retrieve candidates from SQL.
    - Evaluates canonical domain predicates in Java across candidate pages until requested `query.limit()` is accumulated or candidate set is exhausted.
    - Fail-closed on soft deletes: if an `is_deleted == true` row is encountered in the candidate stream, immediately throws `AuditIntegrityException`.
    - Scan Ceiling Enforcement: enforces an absolute safety scan ceiling (`MAX_SCAN_ROWS = 10000`). If ceiling is reached before query limit is satisfied on an unexhausted candidate set, throws `AuditPersistenceException` rather than silently returning a truncated result.
    - Deterministic Canonical Ordering: canonical result ordering is exactly `recordedAt DESC, eventId DESC`, using an explicit comparator preventing Java chained `.reversed()` inversion bugs.

**Non-Functional Requirements & Guardrails**
- **Durability Guarantee**: Success returned implies container-managed transaction commit (ACID durable guarantee).
- **Zero ORM / Write-Behind Dependency**: Standard JDBC only. Zero JPA/Hibernate, Spring Data, Infinispan cache, or write-behind queues in `kleio-persistence`.
- **Concurrency & Speculative Lock Resolution**: Concurrent appends serialize safely at the database unique index via `ON CONFLICT DO NOTHING`. Competing append transactions wait and resolve safely under PostgreSQL speculative locks without unhandled constraint violations, deadlocks, or transaction aborts.
- **Task 05 PHI Logging Compliance**:
    - `DEBUG` / `TRACE`: Detailed operational metadata.
    - `INFO` / `WARN` / `ERROR`: Strictly PHI-safe with structural metadata only (`eventId`, `action`, `outcome`, `classification`). Zero unmasked patient IDs, clinical observations, or authentication secrets.
- **Architectural Isolation**: Strict unidirectional dependencies: `kleio-persistence` -> `kleio-core` and `kleio-fhir`. Zero dependencies on Hestia, Iris, Artemis, or Themis Core. Zero dependencies from Calliope, Themis, or Kleio Core into `kleio-persistence`.

**Technical Design**

**Current Implementation**  
In the current codebase:
- `kleio/` contains `kleio-core` (canonical domain model `HarmoniaAuditEvent`, `AuditService`, `InMemoryAuditService`) and `kleio-fhir` (lossless FHIR R5 mapper `HarmoniaAuditEventMapper`).
- `hie_fhir_resources` table exists in PostgreSQL with unique constraint `uk_resource_type_fhir_id` on `(resource_type, fhir_id)`.
- Existing audit persistence in `mnemosyne-clinical` and `iris-befe` routes audit events through mutable JPA upserts and asynchronous Infinispan cache write-behind, permitting overwrites and silent loss.
- `SecurityEnforcementArchitectureTest` in `paradeigma-test` validates package boundaries and isolation invariants.

**Key Decisions & Justifications**

**Decision 1: Standard JDBC with Jakarta Transactions over JPA/Hibernate**
- **Chosen Approach**: Standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`) managed by Jakarta Transactions (`@Transactional(TxType.REQUIRED)`).
- **Rationale**: When JPA/Hibernate encounters a unique constraint violation, Hibernate marks the entire session and transaction as rollback-only. Any subsequent `SELECT` within that transaction fails with `Transaction marked as rollback-only`. Using native JDBC with PostgreSQL `INSERT ... ON CONFLICT (resource_type, fhir_id) DO NOTHING` allows `ps.executeUpdate()` to return `0` without throwing database exceptions or corrupting the JTA transaction, enabling immediate in-transaction collision resolution.

**Decision 2: Reusing `hie_fhir_resources` for Step 03**
- **Chosen Approach**: Reuse the existing `hie_fhir_resources` table in the clinical database without DDL changes.
- **Rationale**: Maximizes operational stability by eliminating high-risk DDL migrations and deployment downtime during Step 03. The table already possesses all required columns (`resource_type`, `fhir_id`, `version_id`, `resource_json`, `is_deleted`, `last_updated`) and unique constraint `uk_resource_type_fhir_id`. Enforcing insert-only semantics in `JdbcAppendOnlyAuditEventRepository` provides logical immutability immediately. Segregation to a dedicated Operations database is deferred to AUDIT-BL-02.

**Decision 3: Qualified `@KleioAudit` DataSource Consumption**
- **Chosen Approach**: `JdbcAppendOnlyAuditEventRepository` consumes `@Inject @KleioAudit DataSource dataSource`. It does NOT own the physical JNDI name and does not perform configuration interpolation or programmatic JNDI fallbacks.
- **Rationale**: Decouples `kleio-persistence` completely from deployment-specific JNDI bindings. The host deployable (composition root) supplies the qualified `DataSource`. When audit storage is moved to a dedicated database in AUDIT-BL-02, only the composition root changes; `kleio-persistence` requires zero code modifications. For tests, public constructors allow direct instantiation with test DataSources.

**Decision 4: Deterministic Two-Dimensional Comparator**
- **Chosen Approach**: Explicit two-dimensional comparator:
  ```java
  Comparator<HarmoniaAuditEvent> canonicalOrder = Comparator
      .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
      .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());
  ```
- **Rationale**: Avoids the subtle Java trap of `Comparator.comparing(...).reversed().thenComparing(...).reversed()`, where the final `.reversed()` reverses the entire composite comparator (flipping the primary sort order). Explicitly applying `Comparator.reverseOrder()` inside each dimension ensures `recordedAt DESC` is primary and `eventId DESC` is the deterministic secondary tie-breaker.

**Proposed Changes & Module Architecture**

```
kleio/
├── pom.xml                                   # Registers kleio-core, kleio-fhir, kleio-persistence
├── kleio-core/
│   └── src/main/java/net/fhirfactory/harmonia/kleio/audit/
│       ├── model/
│       │   └── AuditQuery.java               # NEW: Canonical query criteria record & builder
│       └── service/
│           ├── AuditService.java             # Add find(AuditQuery) and get(String)
│           └── InMemoryAuditService.java     # Implement find(AuditQuery) with canonical filtering
├── kleio-fhir/                               # Lossless FHIR R5 mapper (unchanged)
└── kleio-persistence/                        # NEW MODULE
    ├── pom.xml                               # Jakarta EE 10 provided, PostgreSQL runtime, Testcontainers test
    ├── src/main/resources/META-INF/
    │   └── beans.xml                         # CDI 4.0 bean-discovery-mode="annotated"
    ├── src/main/java/net/fhirfactory/harmonia/kleio/persistence/
    │   ├── qualifier/
    │   │   └── KleioAudit.java               # CDI qualifier for audit DataSource
    │   ├── cdi/
    │   │   └── FhirContextProducer.java      # Produces singleton FhirContext.forR5()
    │   ├── exception/
    │   │   └── AuditPersistenceException.java# Unchecked persistence exception
    │   ├── model/
    │   │   └── PersistedAuditEventRow.java   # Internal DTO mapping hie_fhir_resources row
    │   ├── repository/
    │   │   ├── AppendOnlyAuditEventRepository.java      # Repository interface
    │   │   └── JdbcAppendOnlyAuditEventRepository.java  # JDBC implementation
    │   └── service/
    │       └── DurableAuditService.java      # ApplicationScoped, Transactional AuditService
    └── src/test/java/net/fhirfactory/harmonia/kleio/persistence/
        ├── DurableAuditServiceH2Test.java              # Unit/functional & restart tests
        └── DurableAuditServicePostgreSqlConcurrencyTest.java # Real PostgreSQL concurrency tests
```

**Data Models & Serialization Boundary**

1. **`AuditQuery`** (`kleio-core`):
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
   ) implements Serializable {
       public static final int DEFAULT_LIMIT = 50;
       public static final int MAX_LIMIT = 1000;
       // Immutable Builder pattern with validation and clamping
   }
   ```

2. **`PersistedAuditEventRow`** (`kleio-persistence`):
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

3. **Serialization Flow**:
    - `HarmoniaAuditEvent` -> `HarmoniaAuditEventMapper.toFhir(event)` -> `org.hl7.fhir.r5.model.AuditEvent` -> `fhirContext.newJsonParser().encodeResourceToString(fhir)` -> `resource_json`.
    - Reverse: `resource_json` -> `fhirContext.newJsonParser().parseResource(AuditEvent.class, json)` -> `HarmoniaAuditEventMapper.fromFhir(fhir)` -> `HarmoniaAuditEvent`.
    - If JSON is unparseable or cannot map to canonical domain: throw `AuditIntegrityException`.

**Atomic Append & Idempotency Transaction Algorithm**

Method: `@Transactional(Transactional.TxType.REQUIRED) HarmoniaAuditEvent append(HarmoniaAuditEvent event)`:
1. **Precondition Validation**: Assert `event != null`. `HarmoniaAuditEvent.eventId` is mandatory and pre-validated; `DurableAuditService` NEVER generates, normalizes, or replaces it.
2. **Serialization**: Convert `event` to FHIR JSON `resourceJson`.
3. **Insert Attempt**:
   ```sql
   INSERT INTO hie_fhir_resources (
       resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated
   ) VALUES (
       'AuditEvent', ?, 1, ?, false, ?
   ) ON CONFLICT (resource_type, fhir_id) DO NOTHING;
   ```
4. **Branch 1: Insert Succeeded (`rowsAffected == 1`)**:
    - Return `event`. The container transaction commits on method exit, guaranteeing durability.
5. **Branch 2: Conflict Encountered (`rowsAffected == 0`)**:
    - Execute query within same transaction:
      ```sql
      SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
      FROM hie_fhir_resources
      WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
      ```
    - If row is marked `is_deleted == true`: throw fail-closed `AuditIntegrityException("Audit integrity violation: event with ID " + event.eventId() + " exists but is marked deleted")`.
    - Deserialize `resourceJson` into `HarmoniaAuditEvent existingEvent`.
    - If `existingEvent.equals(event)`: return `existingEvent` (idempotent replay, zero DB mutations).
    - If `!existingEvent.equals(event)`: throw `AuditIntegrityException("Conflicting audit event with ID " + event.eventId() + " already exists with divergent content")`.

**Audit Time Semantics & Keyset-Paged Query Engine**

- **Occurrence Time vs Storage Time**: `AuditQuery.startTime` and `AuditQuery.endTime` filter strictly on `HarmoniaAuditEvent.recordedAt` (when the event occurred), never on database `last_updated`.
- **Keyset Candidate Scanning**:
  ```
  cursorId = Long.MAX_VALUE
  totalScanned = 0
  pageSize = 200
  MAX_SCAN_ROWS = 10000

  WHILE matching.size() < query.limit() AND totalScanned < MAX_SCAN_ROWS:
      page = repository.findCandidatePage(cursorId, pageSize)
      IF page.isEmpty():
          BREAK
      FOR row IN page:
          cursorId = row.id()
          totalScanned++
          IF row.isDeleted():
              THROW AuditIntegrityException
          candidate = deserialize(row.resourceJson())
          IF candidate matches AuditQuery predicates:
              matching.add(candidate)
              IF matching.size() == query.limit():
                  BREAK
      IF page.size() < pageSize:
          BREAK

  IF totalScanned >= MAX_SCAN_ROWS AND matching.size() < query.limit():
      IF repository.hasMoreRows(cursorId):
          THROW AuditPersistenceException("Audit query scan ceiling of 10000 rows exceeded before query criteria could be completed")

  Sort matching by (recordedAt DESC, eventId DESC)
  RETURN unmodifiableList(matching)
  ```

**Point Read Design (`findById` / `get`)**

- Executes:
  ```sql
  SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated
  FROM hie_fhir_resources
  WHERE resource_type = 'AuditEvent' AND fhir_id = ?;
  ```
- If absent: returns `Optional.empty()`.
- If present and `is_deleted == true`: throws `AuditIntegrityException` (tampering detected; never hidden behind empty).
- If present and active: parses JSON, maps to `HarmoniaAuditEvent`, returns `Optional.of(event)`. If parsing fails, throws `AuditIntegrityException`.

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

**Risks & Mitigations**
- **Risk: Lock contention during concurrent appends on identical event ID**:
    - *Mitigation*: PostgreSQL speculative lock resolution serializes concurrent insertions cleanly without deadlocks. The winning transaction commits, and losing transactions receive `rowsAffected == 0` without transaction abort, seamlessly falling into idempotent replay verification.
- **Risk: Full table scan performance degradation**:
    - *Mitigation*: Keyset candidate paging uses descending primary key index scans (`id < cursorId ORDER BY id DESC LIMIT 200`), bounded by a strict 10,000-row defensive ceiling to prevent resource exhaustion.
- **Risk: Accidental mutation of persisted evidence**:
    - *Mitigation*: `AppendOnlyAuditEventRepository` exposes zero `update()` or `delete()` methods. Idempotent replays execute zero SQL updates. Soft-deleted rows trigger immediate integrity alarms.

**Testing**

**Validation Approach**  
Verification of the durable audit persistence implementation employs a multi-tiered test strategy:
1. **In-Memory & Fast Functional Tests**: Verifying canonical domain query filtering and in-memory evaluation in `kleio-core`.
2. **H2 Functional Suite (`DurableAuditServiceH2Test`)**: Exercising `DurableAuditService` against an embedded H2 database in PostgreSQL mode to validate durable commits, idempotency, integrity violations, keyset paging, and soft-delete detection.
3. **Restart Durability Simulation**: Simulating process termination and reinstantiation to verify data persistence across service lifecycles.
4. **Real PostgreSQL Concurrency Tests (`DurableAuditServicePostgreSqlConcurrencyTest`)**: Testing thread-safety, `ON CONFLICT` semantics, speculative locking, and visibility against an actual PostgreSQL 16 container using Testcontainers.
5. **ArchUnit Architecture Guardrails**: Enforcing strict subproject boundaries, zero Spring dependencies, and layer isolation in `SecurityEnforcementArchitectureTest`.

**Key Scenarios (H2 Functional Suite)**
- **New Event Append**: `append(event)` inserts exactly 1 row with `version_id = 1`, `is_deleted = false`, and returns `event`.
- **Point Read Equality**: `findById(eventId)` and `get(eventId)` retrieve active records matching the original event under `HarmoniaAuditEvent.equals()`.
- **Identical Replay Idempotency**: Submitting identical evidence twice returns success on both calls. Database contains exactly 1 row; `version_id`, `last_updated`, `resource_json`, and `is_deleted` remain untouched.
- **Divergent Event Collision**: Submitting an event with the same `eventId` but different attributes throws `AuditIntegrityException`. Database record is unmutated.
- **Soft-Deleted Row Collision**: Submitting an event whose `eventId` exists with `is_deleted = true` throws `AuditIntegrityException` (never resurrects or updates).
- **Soft-Deleted Row Point Read**: Calling `findById` on a soft-deleted record throws `AuditIntegrityException`.
- **Unknown ID Point Read**: Calling `findById("missing")` returns `Optional.empty()`.
- **Malformed Persisted JSON**: Storing corrupted JSON causes `findById` and `append` conflict resolution to throw `AuditIntegrityException`.
- **Canonical Time Filtering**: `find(AuditQuery)` filters on `recordedAt` between `startTime` and `endTime`, completely decoupled from persistence `last_updated`.
- **Predicate Filtering**: Verifies exact filtering on `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, and `operationId`.
- **Keyset Paging & Independent Ordering**: Evaluates datasets where multiple events have identical `recordedAt` timestamps but different `eventId` values, proving that primary sorting (`recordedAt DESC`) and secondary tie-breaking (`eventId DESC`) operate deterministically.
- **Soft-Deleted Row in Query Stream**: If a candidate row in the query candidate scan has `is_deleted = true`, immediately throws `AuditIntegrityException`.
- **Defensive Scan Ceiling**: Exceeding `MAX_SCAN_ROWS = 10000` before satisfying query limit on an unexhausted set throws `AuditPersistenceException`.
- **Database Failure Translation**: Database connectivity failure during `append()` translates to `AuditPersistenceException` and never returns premature success.

**PostgreSQL Concurrency Test Strategy (Testcontainers)**  
Running against PostgreSQL 16 container (`org.testcontainers:postgresql:1.19.7`):
- **Scenario A (Concurrent Identical Append)**: 10 concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Exactly 1 physical row is inserted; all 10 threads receive success; metadata is unmutated.
- **Scenario B (Concurrent Divergent Append)**: 10 concurrent threads submit divergent payloads sharing the same `eventId`. Exactly 1 winner succeeds in physical insertion; competing 9 threads catch `AuditIntegrityException`; persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
- **Scenario C (`ON CONFLICT` Transaction Non-Corruption)**: Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
- **Scenario D (Speculative Locking & Conflict Resolution)**: Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.

**Restart Durability Simulation**
- Instantiate `DurableAuditService` instance 1 on persistent data store.
- Append audit event `EVT-RESTART-001`.
- Close instance 1 and simulate process termination.
- Instantiate fresh `DurableAuditService` instance 2 pointing to the same data store.
- Invoke `instance2.findById("EVT-RESTART-001")` and verify canonical equality with the original event.

**Architecture Guardrail Tests**  
Add the following ArchUnit rules in `paradeigma/paradeigma-test/.../SecurityEnforcementArchitectureTest.java`:
1. **Kleio Core Independence**: Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
2. **Kleio FHIR Independence**: Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
3. **Kleio Persistence Layering & Jakarta EE Compliance**: Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
4. **No External Dependents on Kleio Persistence**: Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.

**Verification Commands**
```bash

# 1. Run Kleio subproject unit, functional, and Testcontainers PostgreSQL concurrency tests

mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence -am

# 2. Run architecture guardrail suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run full repository reactor test suite

mvn test
```

**Delivery Steps**

**✓ Step 1: Define Canonical Query Contracts in kleio-core and Scaffold kleio-persistence Module**  
The canonical `AuditQuery` model and repository search methods are defined in `kleio-core`, and the new `kleio-persistence` library submodule is scaffolded with Jakarta EE 10 and test dependencies in the Maven reactor.

- Define canonical query record `AuditQuery` in package `net.fhirfactory.harmonia.kleio.audit.model` supporting `eventId`, `startTime`, `endTime`, `classification`, `principalId`, `targetId`, `action`, `outcome`, `correlationId`, `operationId`, and bounded `limit` (default 50, maximum 1000) with an immutable builder and canonical predicate matching logic.
- Extend `AuditService` interface in `net.fhirfactory.harmonia.kleio.audit.service` with `List<HarmoniaAuditEvent> find(AuditQuery query)` and `default Optional<HarmoniaAuditEvent> get(String eventId)`.
- Update `InMemoryAuditService` in `net.fhirfactory.harmonia.kleio.audit.service` to implement `find(AuditQuery query)`, filtering in-memory records based on `recordedAt` (occurrence time) rather than arrival time.
- Add comprehensive unit tests in `AuditServiceTest` to verify `find(AuditQuery)` predicate filtering, bounding, and in-memory evaluation.
- Create submodule `kleio/kleio-persistence` with `pom.xml` declaring dependencies on `kleio-core`, `kleio-fhir`, `jakarta.platform:jakarta.jakartaee-api` (scope provided), `org.postgresql:postgresql` (scope runtime), `org.slf4j:slf4j-api`, `com.h2database:h2` (scope test), `org.testcontainers:postgresql:1.19.7` (scope test), `org.testcontainers:junit-jupiter:1.19.7` (scope test), `org.junit.jupiter:junit-jupiter` (scope test), and `org.assertj:assertj-core` (scope test).
- Add `beans.xml` with `bean-discovery-mode="annotated"` in `kleio-persistence/src/main/resources/META-INF/`.
- Register `kleio-persistence` module in `kleio/pom.xml` and add its dependency management entry in root `pom.xml`.
- Execute `mvn clean test-compile -pl kleio/kleio-core,kleio/kleio-persistence` to verify clean module compilation and reactor dependency alignment.

**✓ Step 2: Implement Append-Only Standard JDBC Repository and Keyset Paged Scanner**  
A dedicated, insert-only standard JDBC repository executes native SQL against `hie_fhir_resources` with zero update or delete capabilities and provides deterministic keyset-paged candidate scanning.

- Create internal DTO record `PersistedAuditEventRow` in `net.fhirfactory.harmonia.kleio.persistence.model` capturing `long id`, `String fhirId`, `long versionId`, `String resourceJson`, `boolean isDeleted`, and `Instant lastUpdated`.
- Define repository contract `AppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` exposing:
    - `boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated)`
    - `Optional<PersistedAuditEventRow> findByEventId(String eventId)`
    - `List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize)`
- Implement `@ApplicationScoped JdbcAppendOnlyAuditEventRepository` in `net.fhirfactory.harmonia.kleio.persistence.repository` using standard JDBC (`javax.sql.DataSource`, `PreparedStatement`, `ResultSet`):
    - Injects `@Inject @KleioAudit DataSource dataSource`.
    - Provides a public constructor `JdbcAppendOnlyAuditEventRepository(DataSource dataSource)` for direct test instantiation without CDI container or JNDI lookups.
    - Implements `insertIfAbsent`: executes `INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, ?) ON CONFLICT (resource_type, fhir_id) DO NOTHING`. Returns `true` if `rowsAffected == 1`, `false` if `rowsAffected == 0`.
    - Implements `findByEventId`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND fhir_id = ?`.
    - Implements `findCandidatePage`: executes `SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated FROM hie_fhir_resources WHERE resource_type = 'AuditEvent' AND id < ? ORDER BY id DESC LIMIT ?`.
- Ensure connection acquisition via `dataSource.getConnection()` naturally enlists in container JTA transactions without manual `commit()` or `rollback()`.
- Add unit tests validating SQL generation, parameter binding, result set mapping, and exception translation into `AuditPersistenceException`.

**✓ Step 3: Implement DurableAuditService with Idempotent Replay, Deterministic Ordering, and CDI Integration**  
`DurableAuditService` implements `AuditService` using Jakarta CDI and `@Transactional`, guaranteeing durably committed appends, idempotent replay of identical evidence, fail-closed handling of soft-deleted rows, deterministic chronological sorting, and keyset-paged scanning.

- Create CDI qualifier `@KleioAudit` in `net.fhirfactory.harmonia.kleio.persistence.qualifier`.
- Create `AuditPersistenceException` in `net.fhirfactory.harmonia.kleio.persistence.exception`.
- Create CDI producer `FhirContextProducer` in `net.fhirfactory.harmonia.kleio.persistence.cdi` producing singleton `FhirContext.forR5()` as `@Produces @ApplicationScoped`.
- Implement `@ApplicationScoped DurableAuditService` in `net.fhirfactory.harmonia.kleio.persistence.service`:
    - Injects `AppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`. Provide public constructor for direct test instantiation.
    - Implements `@Transactional(Transactional.TxType.REQUIRED) append(HarmoniaAuditEvent event)`:
        - Enforces non-null event validation without generating, normalizing, or replacing the canonical `event.eventId()`.
        - Serializes `event` to FHIR R5 `AuditEvent` via `HarmoniaAuditEventMapper.toFhir(event)` and HAPI JSON parser.
        - Executes `repository.insertIfAbsent(event.eventId(), resourceJson, Instant.now())`.
        - If `true` (inserted): transaction commits and returns `event`.
        - If `false` (existing row): retrieves existing row. If `row.isDeleted() == true`, immediately throws fail-closed `AuditIntegrityException`. Deserializes `row.resourceJson()` to `HarmoniaAuditEvent existingEvent`. If `existingEvent.equals(event)`, returns `existingEvent` as idempotent replay (zero mutations to database). If divergent, throws `AuditIntegrityException`.
    - Implements `findById(String eventId)` and `get(String eventId)`: fetches row; if absent, returns `Optional.empty()`; if `isDeleted() == true`, throws fail-closed `AuditIntegrityException`; if active, deserializes and returns `Optional.of(event)`. If JSON is unparseable or malformed, throws `AuditIntegrityException`.
    - Implements `find(AuditQuery query)`: executes keyset paging scanning candidate pages (`id < cursorId`); throws `AuditIntegrityException` if any soft-deleted row is encountered; deserializes and evaluates canonical query predicates; enforces `MAX_SCAN_ROWS = 10000` scan ceiling, throwing `AuditPersistenceException` if ceiling is reached before exhausting candidates; sorts deterministically using an explicit two-dimensional comparator: `Comparator.comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder()).thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder())`.
    - Implements convenience aliases: `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` delegating to `find(AuditQuery)`.
    - Strictly adheres to Task 05 logging guidelines: `DEBUG`/`TRACE` for detailed diagnostics, `INFO`/`WARN`/`ERROR` strictly PHI-safe with structural metadata only.
- Implement comprehensive functional test suite `DurableAuditServiceH2Test` against embedded H2 in PostgreSQL mode validating all append, replay, collision, point-read, time-range filtering, and scan ceiling flows, including an explicit test proving `eventId DESC` ordering when `recordedAt` timestamps are equal.
- Implement process restart simulation test in `DurableAuditServiceH2Test` verifying that audit evidence survives service termination and recreation against the persistent database.

**✓ Step 4: Establish Real PostgreSQL Concurrency Verification and ArchUnit Guardrail Enforcement**  
Real PostgreSQL Testcontainers concurrency tests prove thread safety, absence of deadlocks, and speculative lock resolution, while ArchUnit rules enforce strict module decoupling and boundary isolation.

- Implement real PostgreSQL concurrency test suite `DurableAuditServicePostgreSqlConcurrencyTest` using Testcontainers (`org.testcontainers:postgresql:1.19.7`) against PostgreSQL 16:
    - Scenario A (Concurrent Identical Append): Multiple concurrent threads submit the exact same `HarmoniaAuditEvent` using a `CountDownLatch`. Asserts exactly 1 row physically inserted in `hie_fhir_resources`, all threads receive success, and metadata (`version_id`, `last_updated`, `resource_json`, `is_deleted`) is unmutated.
    - Scenario B (Concurrent Divergent Append): Multiple concurrent threads submit divergent events with the same `eventId`. Asserts exactly 1 winner succeeds in physical insertion, competing threads catch `AuditIntegrityException`, and persisted evidence in `hie_fhir_resources` matches winner exactly without overwrite.
    - Scenario C (`ON CONFLICT` Transaction Non-Corruption): Verifies PostgreSQL native `ON CONFLICT DO NOTHING` returns `rowsAffected == 0` without transaction abort, allowing subsequent `SELECT` within the same transaction to succeed without rollback-only errors.
    - Scenario D (Speculative Locking & Visibility): Verifies concurrent append transactions resolve safely without deadlocks under PostgreSQL unique index speculative locks, and winning evidence is observable by competing transactions after conflict resolution.
- Add ArchUnit rules to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
    - Rule 1 (Kleio Core Independence): Classes in `net.fhirfactory.harmonia.kleio.audit..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, `org.springframework..`, `ca.uhn.fhir..`, or `org.hl7.fhir..`.
    - Rule 2 (Kleio FHIR Independence): Classes in `net.fhirfactory.harmonia.kleio.fhir..` must have zero dependencies on `jakarta.persistence..`, `java.sql..`, `javax.sql..`, or `org.springframework..`.
    - Rule 3 (Kleio Persistence Layering & Jakarta EE Compliance): Classes in `net.fhirfactory.harmonia.kleio.persistence..` may depend on `kleio.audit.model..`, `kleio.audit.service..`, `kleio.fhir..`, `jakarta.enterprise..`, `jakarta.transaction..`, `jakarta.inject..`, `jakarta.annotation..`, `javax.sql..`, `java.sql..`. Must NOT depend on `org.springframework..`, `net.fhirfactory.harmonia.hestia..`, `net.fhirfactory.harmonia.iris..`, `net.fhirfactory.harmonia.petasos..`, or `net.fhirfactory.harmonia.themis.core..`.
    - Rule 4 (No External Dependents on Kleio Persistence): Classes in `net.fhirfactory.harmonia.themis..` must NOT depend on `net.fhirfactory.harmonia.kleio.persistence..`.
- Execute full test suite across `kleio-core`, `kleio-fhir`, `kleio-persistence`, and architecture tests via `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,paradeigma/paradeigma-test -am`.



I verified the PostgreSQL concurrency tests and the updated architectural guardrails. Now I will execute the full repository test suite to confirm that no regressions were introduced across other modules.

**Summary**
- Implemented Task 06 Step 03.3 durable append-only Kleio audit persistence library (`kleio-persistence`) establishing an immutable evidence boundary using Jakarta EE 10 (CDI 4.0, JTA 2.0) and standard JDBC against PostgreSQL table `hie_fhir_resources`.
- Enforced durable write-through commits on append, idempotent replay for identical evidence, fail-closed handling for soft-deleted rows, deterministic chronological sorting (`recordedAt DESC, eventId DESC`), and deadlock-free PostgreSQL speculative lock resolution.

**Changes**
- **Canonical Model & Service Extensions (`kleio-core`)**: Added immutable `AuditQuery` record with fluent builder and predicate evaluation, and extended `AuditService` and `InMemoryAuditService` with `find(AuditQuery)` and backward-compatible `get(String)` point-read alias.
- **Persistence Foundation & DTOs (`kleio-persistence`)**: Created `@KleioAudit` CDI qualifier for decoupled container DataSource consumption, `AuditPersistenceException`, `PersistedAuditEventRow` DTO, and `FhirContextProducer` for thread-safe R5 parsing.
- **Append-Only JDBC Repository (`JdbcAppendOnlyAuditEventRepository`)**: Implemented insert-only repository executing native SQL with `ON CONFLICT DO NOTHING`, zero update/delete capabilities, and keyset-paged scanning (`id < cursorId ORDER BY id DESC LIMIT ?`).
- **Durable Audit Service (`DurableAuditService`)**: Implemented `@Transactional(TxType.REQUIRED)` audit service preserving canonical `eventId` immutability, executing fail-closed soft-delete rejection, verifying canonical equality on conflict replays, and enforcing a 10,000-row defensive scan ceiling.
- **Architecture & Build Reactor Alignment**: Registered `kleio-persistence` in root and `kleio/pom.xml`, and added 4 ArchUnit guardrail rules in `SecurityEnforcementArchitectureTest` verifying strict module independence and zero leakage from Themis or Hestia into Kleio persistence.

**Verification**
- **Functional & Durability Test Suite**: Executed `DurableAuditServiceH2Test` and `JdbcAppendOnlyAuditEventRepositoryTest` covering 18 test cases validating atomic appends, idempotent replays, divergent collisions, soft-delete fail-closed checks, tie-breaker ordering (`eventId DESC` on equal `recordedAt`), and process restart durability simulation.
- **Real PostgreSQL Concurrency Suite**: Executed `DurableAuditServicePostgreSqlConcurrencyTest` against PostgreSQL 16 Testcontainers covering 4 scenarios demonstrating single-winner insertion under concurrency, idempotent multi-thread replays, transaction non-corruption, and deadlock-free speculative lock resolution.
- **Architecture & Full Reactor Verification**: Verified 52 ArchUnit rules in `paradeigma-test` and ran the full Harmonia repository test suite (`mvn test`), achieving a 100% clean build across all reactor modules with zero regressions.

