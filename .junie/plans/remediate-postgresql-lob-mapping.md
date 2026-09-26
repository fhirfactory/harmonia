---
sessionId: session-260926-082416-tjyr
---

# Requirements

### Overview & Goals
Remediate the PostgreSQL persistence mapping defect in `mnemosyne-clinical` where reading `FhirResourceEntity.resourceJson` during PostgreSQL verification fails with `JpaSystemException: Unable to access lob stream` caused by `PSQLException: Large Objects may not be used in auto-commit mode`.

### Scope
- **In Scope**:
  - Investigation and remediation of `@Lob` and column mapping on `FhirResourceEntity.resourceJson`.
  - Ensuring `resource_json` maps directly to standard PostgreSQL `TEXT` (native text data type) without JDBC Large Object (OID) streaming requirements.
  - Validation via `AuthoritativePersistencePostgreSqlConcurrencyTest` using real PostgreSQL 16 Testcontainers.
  - Execution and verification of the full `mnemosyne-clinical` test suite and ArchUnit architecture tests.
  - Structured reporting of concurrency metrics (CREATE participant counts, committed counts, conflict reasons, UPDATE outcomes, version states).
- **Out of Scope**:
  - Redesigning the concurrency protocol or conditional write engine.
  - Modifying authoritative conditional UPDATE semantics or CREATE uniqueness constraints (`uk_resource_type_fhir_id`).
  - Advancing to STEP 08.04D.

### User Stories
- As a **Clinical Persistence Service**, I want FHIR JSON payloads stored as native database text so that entities can be read efficiently in transactional and non-transactional contexts without Large Object stream lifecycle errors.
- As a **Harmonia Platform Engineer**, I want PostgreSQL concurrency tests to reliably assert multi-threaded CREATE and UPDATE invariants against real PostgreSQL instances with zero mapping regressions.

### Functional Requirements
- **FR-1**: `FhirResourceEntity.resourceJson` must map to standard SQL/PostgreSQL `TEXT` column type without creating or referencing PostgreSQL `pg_largeobject` OIDs.
- **FR-2**: Repository reads (e.g. `findByResourceTypeAndFhirId`) must return the full JSON string in standard auto-commit read modes without throwing `PSQLException: Large Objects may not be used in auto-commit mode`.
- **FR-3**: PostgreSQL concurrency scenarios (concurrent CREATE collision, concurrent UPDATE race, chained sequential progression) must pass all assertions deterministically under Testcontainers.

### Non-Functional Requirements
- **NFR-1: Compatibility**: The mapping must remain fully compatible with both H2 in-memory databases (development/unit testing) and PostgreSQL 16 (production and containerized integration testing).
- **NFR-2: Zero Architectural Leakage**: Entity changes must maintain compliance with ADR-020 and pass all `*ArchitectureTest` rules.

# Technical Design

### Current Implementation & Root Cause Analysis
In `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/model/FhirResourceEntity.java`:
```java
@Lob
@Column(name = "resource_json", nullable = false, columnDefinition = "TEXT")
private String resourceJson;
```
When JPA `@Lob` is used on a `String` field with the PostgreSQL dialect in Hibernate 6:
1. Hibernate treats the field as a JDBC `CLOB` / PostgreSQL `OID` (Large Object) stream.
2. PostgreSQL manages Large Objects through `pg_largeobject` descriptors, requiring an active transaction block.
3. Reading `resourceJson` outside an active Spring `@Transactional` context (such as repository queries in test assertions or diagnostic lookups in `AuthoritativePersistenceService`) fails when JDBC auto-commit is enabled:
   `org.postgresql.util.PSQLException: Large Objects may not be used in auto-commit mode`
   wrapped in `org.springframework.orm.jpa.JpaSystemException: Unable to access lob stream`.

### Key Decisions
- **Decision 1: Remove `@Lob` annotation**:
  - *Chosen Approach*: Remove `@Lob` from `FhirResourceEntity.resourceJson` and maintain `@Column(name = "resource_json", nullable = false, columnDefinition = "TEXT")`.
  - *Rationale*: FHIR JSON documents are application text strings (typically 2 KB – 100 KB), not opaque binary blobs or multi-gigabyte LOBs. PostgreSQL's native `TEXT` column type supports strings up to 1 GB with automatic TOAST storage and allows standard JDBC `getString()` reads without Large Object stream state or transaction boundaries.
- **Decision 2: Avoid test-only transaction wrapping workarounds**:
  - *Chosen Approach*: Fix the entity mapping directly so production reads and test assertions share the same robust behavior.
  - *Rationale*: Wrapping test assertions in transaction helpers masks the defect for production consumers reading outside write transactions.

### Proposed Changes
1. **`hestia/mnemosyne-clinical/.../FhirResourceEntity.java`**:
   - Remove the `@Lob` annotation on `resourceJson`.
   - Maintain `@Column(name = "resource_json", nullable = false, columnDefinition = "TEXT")`.
2. **Persistence & Repository Verification**:
   - Ensure `FhirResourceRepository.updateIfVersionMatches` and `AuthoritativePersistenceService` continue operating seamlessly with standard `String` bindings.

### Data Models / Contracts
```java
@Entity
@Table(
    name = "hie_fhir_resources",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})
    },
    indexes = {
        @Index(name = "idx_resource_type_fhir_id", columnList = "resource_type, fhir_id"),
        @Index(name = "idx_resource_type_deleted", columnList = "resource_type, is_deleted")
    }
)
public class FhirResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_type", nullable = false, length = 64)
    private String resourceType;

    @Column(name = "fhir_id", nullable = false, length = 128)
    private String fhirId;

    @Column(name = "version_id", nullable = false)
    private Long versionId = 1L;

    @Column(name = "resource_json", nullable = false, columnDefinition = "TEXT")
    private String resourceJson;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated = Instant.now();
    
    // ... Constructors, getters, and setters
}
```

### Components Affected
- `net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity` (Mnemosyne Clinical JPA model)
- `net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePostgreSqlConcurrencyTest` (PostgreSQL concurrency verification)

### File Structure
- `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/model/FhirResourceEntity.java` (modified)
- `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePostgreSqlConcurrencyTest.java` (verified)

### Risks & Mitigations
- **Risk**: H2 in-memory testing dialect compatibility.
  - *Mitigation*: H2 with `MODE=PostgreSQL` natively supports the `TEXT` columnDefinition as a variable-length character string.
- **Risk**: Database migrations or existing DDL conflicts.
  - *Mitigation*: Schema generation in tests uses Hibernate `create-drop` / `update`, which maps `TEXT` directly to PostgreSQL `text` data type.

# Testing

### Validation Approach
Verify the mapping fix against both isolated unit tests and real containerized PostgreSQL instances, followed by full module and architecture verification.

### Key Scenarios & Test Execution
1. **Scenario 1: Concurrent CREATE Collision (`testConcurrentCreateCollision`)**
   - 10 threads attempt simultaneous CREATE for the same `ResourceKey`.
   - Exactly 1 participant commits (version 1).
   - 9 participants receive `RESOURCE_ALREADY_EXISTS` conflict.
   - Assert persisted state via `repository.findByResourceTypeAndFhirId` without LOB stream errors.
2. **Scenario 2: Concurrent UPDATE Race (`testConcurrentUpdateRaceFromSamePredecessor`)**
   - 10 threads attempt simultaneous conditional UPDATE against predecessor V1.
   - Exactly 1 participant commits (advancing to version 2).
   - 9 participants receive `EXPECTED_VERSION_MISMATCH` conflict.
   - Assert database state via repository lookup.
3. **Scenario 3: Chained Sequential Progression (`testChainedSequentialProgression`)**
   - Monotonic sequential progression V1 -> V2 -> V3 -> V4 -> V5.
   - Assert final entity version 5 and payload content `"Version-5"`.

### Explicit Reporting Requirements
Upon completion of test execution, report the following exact metrics:
- Concurrent CREATE participant count: `10`
- Committed CREATE count: `1`
- Remaining CREATE outcomes: `9` (`RESOURCE_ALREADY_EXISTS`)
- Persisted CREATE state: `versionId=1, isDeleted=false`
- Concurrent UPDATE participant count: `10`
- Committed UPDATE count: `1`
- Remaining UPDATE outcomes: `9` (`EXPECTED_VERSION_MISMATCH`)
- Persisted UPDATE state: `versionId=2, isDeleted=false`
- Sequential progression final state: `versionId=5, payload contains "Version-5"`

### Module & Architecture Verification
- Run `mvn test -pl hestia/mnemosyne-clinical`
- Run `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`

# Delivery Steps

### ✓ Step 1: Remediate FhirResourceEntity JPA Column Mapping
`FhirResourceEntity.resourceJson` is mapped cleanly as a standard PostgreSQL `TEXT` column without `@Lob` Large Object semantics.

- Remove the `@Lob` annotation from `FhirResourceEntity.java` on field `resourceJson`.
- Retain `@Column(name = "resource_json", nullable = false, columnDefinition = "TEXT")` to guarantee standard text column mapping across PostgreSQL and H2.
- Ensure all repository query bindings and entity constructors continue operating on standard Java `String` representations without Large Object / OID streams.

### ✓ Step 2: Execute PostgreSQL Concurrency Suite and Validate Concurrency Invariants
The PostgreSQL Testcontainers concurrency test runs all 3 scenarios with zero errors or failures.

- Execute `AuthoritativePersistencePostgreSqlConcurrencyTest` against the PostgreSQL 16 Testcontainers environment.
- Confirm 3 tests run with 0 failures, 0 errors, and 0 skipped.
- Validate that repository reads (`findByResourceTypeAndFhirId`) in test verification and conflict diagnosis operate successfully without requiring active transaction wrappers.
- Capture and log explicit metrics across all concurrency and sequential scenarios (participant counts, commit counts, conflict outcomes, and database versions).

### ✓ Step 3: Verify Clinical Suite and ArchUnit Architecture Rules
All clinical unit/integration tests and architectural boundary tests pass cleanly.

- Execute the full `mnemosyne-clinical` test suite (`mvn test -pl hestia/mnemosyne-clinical`).
- Execute repository architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`), specifically including `MnemosyneAuthoritativePersistenceArchitectureTest`.
- Prepare the structured concurrency execution summary report detailing the CREATE, UPDATE, and sequential progression outcomes.