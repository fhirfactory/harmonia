---
sessionId: session-261002-130554-1qda
---

# Requirements

### Aim

Implement Milestone **M3.1 — Governed READ Integration & Active-State Generation Observation**, establishing Harmonia's production point-read path (`GovernedReader`) under Mneme subsystem ownership (`hestia/mneme-cluster`, package `net.fhirfactory.harmonia.hestia.mneme.access`). This unifies fail-closed Calliope boundary validation, Themis READ authorization (`SecurityException` on DENY), typed authoritative point retrieval from Mnemosyne via `AuthoritativePersistencePort` (`Committed` vs `Absent`), post-read active-state cache convergence via `ActiveStateConvergencePort`, and post-convergence `ActiveStateToken` observation.

### Architectural Constraints

- **Subsystem Ownership**: Mneme owns application-facing Governed Access (`DefaultGovernedReader`, `DefaultGovernedWriter`). Mnemosyne owns authoritative durable state and version progression.
- **Fail-Closed Security**: Themis READ authorization is evaluated locally before authoritative retrieval. A `DENY` decision throws canonical `SecurityException` (`AccessDenied != ResourceAbsent` and `AccessDenied != PersistenceOutcome`).
- **Typed Authoritative Absence**: Mnemosyne authoritative absence is modeled as a first-class typed outcome `AuthoritativePersistenceResult.Absent<T>`, not a failure reason or unstructured string. `Optional.empty()` is returned if and only if an authorized authoritative read returns `Absent`.
- **410 Status Discipline**: Mnemosyne authoritative endpoint (`/api/authoritative/fhir`) returns HTTP 404 on absent resources and does not treat HTTP 410 as authoritative absence. HTTP 410 remains `NotCommitted`; no deletion or tombstone semantics are introduced in M3.1 (preserving ADR-020).
- **Error Outcome Fail-Closed**: All `NotCommitted` outcomes (HTTP 400, 401, 403, 410, pre-transmission transport rejections) and `OutcomeUnknown` indeterminate outcomes fail closed by throwing an exception; they are never collapsed into `Optional.empty()`.
- **Convergence & Token Invariant**: A valid `ActiveStateToken` represents an observed generation of a successfully converged Mneme active representation. Token observation (`ActiveStateCoordinator.observe(key)`) occurs strictly *after* successful cache convergence (`ConvergenceStatus.CONVERGED`).
- **Degraded Convergence**: If convergence cannot establish a valid active representation (`ConvergenceStatus.DEGRADED`), M3.1 must not fabricate or return a valid `ActiveStateToken`. An `ActiveCoordinationUnavailableException` is thrown fail-closed.
- **Version Domain Separation**: Mneme `ActiveStateToken` and Mnemosyne `AuthoritativeVersion` remain strictly distinct.
- **Scope Discipline**: Zero physical DELETE (ADR-020), zero search/batch (M5), zero application migration (M4), zero MicroK8s (M6), zero two-level UPDATE concurrency/rebase (M3.2/M3.3), zero distributed Docker integration (M3.4).

### Acceptance Criteria

1. `AuthoritativePersistenceResult.Absent<T>` is added to the sealed `AuthoritativePersistenceResult` hierarchy in `hestia/mnemosyne-api`.
2. `MnemeAuthoritativeHttpClient.read(key)` maps HTTP 404 to `AuthoritativePersistenceResult.Absent<T>`, and preserves HTTP 400, 401, 403, 410, and pre-transmission failures as `NotCommitted`.
3. `DefaultGovernedReader` resides in `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) and implements `GovernedReader`.
4. `DefaultGovernedWriter` is relocated to `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`).
5. Calliope boundary validation (`GovernedBoundaryValidator`) rejects unmanaged types fail-closed with `IllegalArgumentException`.
6. Themis authorization denial throws canonical `SecurityException` fail-closed and is never translated to `Optional.empty()`.
7. Authoritative persistence read returning `Absent` maps strictly to `Optional.empty()`.
8. Authoritative persistence read returning `NotCommitted` or `OutcomeUnknown` fails closed by throwing an exception.
9. Successful authoritative read (`Committed`) converges Mneme active data cache via `ActiveStateConvergencePort`.
10. `ActiveStateToken` is observed strictly *after* successful cache convergence (`CONVERGED`).
11. Degraded convergence (`DEGRADED`) throws `ActiveCoordinationUnavailableException` fail-closed and never emits a fabricated active token.
12. Contract and client tests prove `Absent` is distinguishable from every `NotCommitted` failure mode.
13. All unit tests in `DefaultGovernedReaderTest` and `DefaultGovernedWriterTest` pass cleanly.
14. All ArchUnit tests in `paradeigma-test` pass with zero regressions.

# Technical Design

### Repository Findings

- **Existing GovernedReader Contract**: `net.fhirfactory.harmonia.model.governedwrite.GovernedReader` in `calliope` defines `<T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext)`.
- **Existing GovernedAccess Composition**: `net.fhirfactory.harmonia.model.governedwrite.GovernedAccess` in `calliope` extends `GovernedReader` and `GovernedWriter`.
- **Mislocated Writer**: `DefaultGovernedWriter` currently resides in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`), violating Mneme subsystem ownership.
- **Boundary Validation**: `GovernedBoundaryValidator.requireManagedType(ResourceKey)` in `calliope` validates managed types fail-closed (throwing `IllegalArgumentException`).
- **Themis Authorization**: `ThemisAuthorizer` in `themis-api` evaluates `ThemisAuthorizationRequest` with `ThemisAction.READ`, `ThemisPrincipal`, `ThemisResource`, and `ThemisSecurityContext`.
- **Authoritative Persistence Hierarchy**: `AuthoritativePersistenceResult<T>` in `hestia/mnemosyne-api` is sealed permits `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown`. Requires `Absent` outcome.
- **Mnemosyne Authoritative Endpoint 410 Inspection**: `AuthoritativeFhirResourceController` (`hestia/mnemosyne-clinical`) explicitly returns HTTP 404 for absent resources. It does not emit HTTP 410. Furthermore, ADR-020 prohibits physical deletion. Therefore, HTTP 410 is not an authoritative absence indicator; only HTTP 404 represents absence.
- **Mneme Persistence Client**: `MnemeAuthoritativeHttpClient` in `hestia/mneme-persistence` maps HTTP 404 to `NotCommitted`, conflating absence with request errors and TLS failures. Must map HTTP 404 to `Absent`.
- **Active Convergence Port**: `ActiveStateConvergencePort` in `calliope` exposes `converge(ResourceKey, T, AuthoritativeVersion)`, returning `ConvergenceStatus.CONVERGED` or `DEGRADED`. Implemented by `HotRodMnemeConvergence` in `hestia/mneme-cluster`.
- **Active State Observation**: `ActiveStateCoordinator.observe(ResourceKey)` in `calliope`, implemented by `HotRodActiveStateCoordinator` in `hestia/mneme-cluster`, retrieves opaque `ActiveStateToken`.
- **Module Dependency**: `hestia/mneme-cluster/pom.xml` lacks direct `<dependency>` on `net.fhirfactory.harmonia:themis-api`.
- **Architecture Tests**: `GovernedWriteCompositionArchitectureTest` currently asserts `DefaultGovernedWriter` in `mnemosyne-clinical`; must be updated for `net.fhirfactory.harmonia.hestia.mneme.access`.

### Files to Change

| File | Module | Action | Description |
| :--- | :--- | :--- | :--- |
| `AuthoritativePersistenceResult.java` | `hestia/mnemosyne-api` | Modify | Add `record Absent<T>(String message) implements AuthoritativePersistenceResult<T>`, update `permits` clause, and remove `outcome()` from root interface to avoid forcing `Absent` into `NOT_COMMITTED`. |
| `MnemeAuthoritativeHttpClient.java` | `hestia/mneme-persistence` | Modify | Map HTTP 404 specifically to `Absent`; keep HTTP 410, 400, 401/403, and pre-transmission failures as `NotCommitted`. |
| `MnemeAuthoritativeHttpClientTest.java` | `hestia/mneme-persistence` | Modify | Assert 404 returns `Absent`, 410 returns `NotCommitted`, and verify `Absent` is distinguishable from all `NotCommitted` modes. |
| `pom.xml` | `hestia/mneme-cluster` | Modify | Add `<dependency>` on `net.fhirfactory.harmonia:themis-api`. |
| `DefaultGovernedWriter.java` | `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) | Relocate | Move from `hestia/mnemosyne-clinical` to `hestia/mneme-cluster` to establish correct subsystem package. |
| `DefaultGovernedReader.java` | `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) | Add | Create Mneme implementation of `GovernedReader` mapping `Absent` -> `Optional.empty()` and failing closed on errors. |
| `DefaultGovernedWriterTest.java` | `hestia/mneme-cluster` | Relocate | Move test class alongside relocated `DefaultGovernedWriter`. |
| `DefaultGovernedReaderTest.java` | `hestia/mneme-cluster` | Add | Unit tests for `DefaultGovernedReader` covering all read outcomes and invariants. |
| `GovernedWriteCompositionArchitectureTest.java` | `paradeigma/paradeigma-test` | Modify | Update package rules to assert `DefaultGovernedReader` and `DefaultGovernedWriter` reside in `net.fhirfactory.harmonia.hestia.mneme.access`. |

### Implementation Steps

1. **Typed Authoritative Absence (`AuthoritativePersistenceResult.Absent`)**:
   - In `AuthoritativePersistenceResult.java`, add `Absent<T>(String message)` to sealed hierarchy with `outcome()` returning `AuthoritativeCommitOutcome.NOT_COMMITTED`.
   - Update `MnemeAuthoritativeHttpClient.read(...)` to return `new AuthoritativePersistenceResult.Absent<>(...)` on HTTP 404.
   - Preserve HTTP 410 as `NotCommitted` ("Resource gone / deleted on authoritative server: ...").
   - Update `MnemeAuthoritativeHttpClientTest` asserting HTTP 404 produces `Absent` and HTTP 410 produces `NotCommitted`.
2. **Subsystem Relocation & Dependency Configuration**:
   - Add `themis-api` dependency to `hestia/mneme-cluster/pom.xml`.
   - Relocate `DefaultGovernedWriter` and `DefaultGovernedWriterTest` to `hestia/mneme-cluster` under `net.fhirfactory.harmonia.hestia.mneme.access`.
3. **`DefaultGovernedReader` Execution Flow**:
   - **Step 1 (Boundary Validation)**: Call `GovernedBoundaryValidator.requireManagedType(key)`. Throws `IllegalArgumentException` on unmanaged types.
   - **Step 2 (Themis Authorization)**: Build `ThemisAuthorizationRequest` with `ThemisAction.READ` and evaluate via `themisAuthorizer.authorize(...)`. If `DENY` or evaluation error, throw canonical `SecurityException` (`AccessDenied != ResourceAbsent`).
   - **Step 3 (Authoritative READ)**: Invoke `persistencePort.read(key)`.
     - If `result instanceof Absent`: return `Optional.empty()` (zero cache convergence, zero token observation).
     - If `result instanceof NotCommitted nc`: throw `ActiveCoordinationUnavailableException("Authoritative read not committed: " + nc.failureMessage())` fail-closed.
     - If `result instanceof OutcomeUnknown ou`: throw `ActiveCoordinationUnavailableException("Authoritative read outcome unknown: " + ou.message())` fail-closed.
     - If `result instanceof Conflict`: throw `ActiveCoordinationUnavailableException("Authoritative read encountered unexpected conflict")` fail-closed.
     - If `result instanceof Committed<T> committed`: extract payload and `AuthoritativeVersion`.
   - **Step 4 (Mneme Convergence)**: Invoke `convergencePort.converge(key, payload, authoritativeVersion)`.
     - If status is `DEGRADED`: throw `ActiveCoordinationUnavailableException("Active-state cache convergence degraded for " + key)` fail-closed (never fabricate a token).
   - **Step 5 (Observe Token)**: Call `activeStateCoordinator.observe(key)` to retrieve the opaque `ActiveStateToken`.
   - **Step 6 (Return Envelope)**: Return `Optional.of(GovernedRead.of(key, payload, activeToken, authoritativeVersion))`.

### Conflicting Repository Findings

1. **Root outcome() Contract Constraint in AuthoritativePersistenceResult**: Root `AuthoritativePersistenceResult` declared abstract `AuthoritativeCommitOutcome outcome()`, forcing all subtypes into write-oriented commit states (`COMMITTED`, `NOT_COMMITTED`, `UNKNOWN`). Forcing `Absent` into `NOT_COMMITTED` would conflate read absence with commit failure. Resolved by removing `outcome()` from root interface (defaulting `isCommitted()` to `this instanceof Committed`) while retaining `outcome()` on mutation-specific subtypes.
2. **Unstructured Absence in Persistence Result**: `AuthoritativePersistenceResult` previously lacked a typed absence outcome, causing `MnemeAuthoritativeHttpClient` to report HTTP 404 as `NotCommitted`. Resolved by introducing typed `AuthoritativePersistenceResult.Absent<T>`.
3. **DefaultGovernedWriter Placement**: Currently located in `hestia/mnemosyne-clinical`, conflicting with the axiom that Mneme owns application-facing governed access. Resolved by relocating to `hestia/mneme-cluster`.
4. **ArchUnit Test Location Rule**: `GovernedWriteCompositionArchitectureTest.defaultGovernedWriterMustResideInMnemosyneClinical` expects `DefaultGovernedWriter` in `net.fhirfactory.harmonia.hapifhir.governed..`. Resolved by updating the rule to require `net.fhirfactory.harmonia.hestia.mneme.access..`.
5. **Missing Themis Dependency in Mneme Cluster**: `hestia/mneme-cluster/pom.xml` lacks `themis-api`, required for local Themis authorization evaluation in `DefaultGovernedReader`. Resolved by adding `<dependency>`.

# Testing

### Validation Approach

Verification of M3.1 combines contract/client unit tests for persistence outcome differentiation, isolated unit tests for `DefaultGovernedReader` using mocks, and ArchUnit structural verification of subsystem boundaries.

### Unit & Contract Tests

- **`MnemeAuthoritativeHttpClientTest`**:
  - `read_404_returnsAbsent`: Proves HTTP 404 returns `AuthoritativePersistenceResult.Absent`.
  - `read_410_returnsNotCommitted`: Proves HTTP 410 returns `AuthoritativePersistenceResult.NotCommitted`.
  - `read_absent_isDistinguishableFromAllNotCommitted`: Proves `Absent` type is structurally distinguishable from HTTP 400, 401, 403, and pre-transmission transport rejections without string parsing.
- **`DefaultGovernedReaderTest`**:
  - `read_validResource_returnsGovernedReadWithPostConvergenceToken`: Authorized existing resource executes convergence, then observes token, returning `Optional.of(GovernedRead)` with payload, active token, and authoritative version.
  - `read_unmanagedType_throwsIllegalArgumentException`: Unmanaged type rejected fail-closed via Calliope validator.
  - `read_themisDenied_throwsSecurityException`: Themis policy denial throws canonical `SecurityException` (`AccessDenied != ResourceAbsent`).
  - `read_absentResource_returnsEmptyOptional`: Authoritative `Absent` returns `Optional.empty()` with zero convergence or token observation.
  - `read_notCommitted_throwsActiveCoordinationUnavailableException`: Authoritative `NotCommitted` fails closed.
  - `read_outcomeUnknown_throwsActiveCoordinationUnavailableException`: Indeterminate transport outcome fails closed.
  - `read_convergenceDegraded_throwsActiveCoordinationUnavailableException`: Post-read convergence returning `DEGRADED` throws `ActiveCoordinationUnavailableException` fail-closed (no fabricated token emitted).
  - `read_nullArguments_throwsException`: Null key or security context rejected fail-closed.

### Architecture Tests (`GovernedWriteCompositionArchitectureTest`)

- `defaultGovernedReaderMustResideInMnemeCluster`: Verifies `DefaultGovernedReader` resides in `net.fhirfactory.harmonia.hestia.mneme.access`.
- `defaultGovernedWriterMustResideInMnemeCluster`: Verifies `DefaultGovernedWriter` resides in `net.fhirfactory.harmonia.hestia.mneme.access`.
- `governedAccessClassesMustNotDependOnInfinispanOrJpa`: Asserts zero direct JPA/Hibernate/Infinispan leaks into `DefaultGovernedReader` or `DefaultGovernedWriter`.
- `governedAccessSourceMustNotContainForbiddenImports`: Static source check asserting zero forbidden imports in governed access classes.

# Delivery Steps

### ✓ Step 1: Establish Typed Authoritative Absence in Mnemosyne API and Persistence Client
Introduce `AuthoritativePersistenceResult.Absent<T>` and update `MnemeAuthoritativeHttpClient` to distinguish true absence from request/transport failures without forcing write commit semantics onto read absence.

- In `hestia/mnemosyne-api`, update `AuthoritativePersistenceResult.java` sealed hierarchy to permit and declare `record Absent<T>(String message) implements AuthoritativePersistenceResult<T>`.
- Remove abstract `AuthoritativeCommitOutcome outcome()` from root `AuthoritativePersistenceResult<T>` interface; update `isCommitted()` to `this instanceof Committed`; retain `outcome()` on write subtypes (`Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown`) to avoid forcing `Absent` into `NOT_COMMITTED`.
- In `hestia/mneme-persistence`, update `MnemeAuthoritativeHttpClient.read(...)`: map HTTP 404 to `new AuthoritativePersistenceResult.Absent<>(...)`; maintain HTTP 410, 400, 401, 403, and pre-transmission transport failures as `NotCommitted`.
- In `MnemeAuthoritativeHttpClientTest.java`, update tests to verify HTTP 404 returns `Absent`, HTTP 410 returns `NotCommitted`, and contractually verify `Absent` is distinguishable from all `NotCommitted` modes without string parsing.

### ✓ Step 2: Relocate Governed Access to Mneme Subsystem and Configure Dependencies
Establish correct subsystem boundaries by moving Governed Access orchestration into Mneme and declaring required dependencies.

- Add `<dependency>` for `net.fhirfactory.harmonia:themis-api` to `hestia/mneme-cluster/pom.xml`.
- Relocate `DefaultGovernedWriter.java` and `DefaultGovernedWriterTest.java` from `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) to `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`).
- Update package declarations and imports across relocated writer classes.
- Ensure `DefaultGovernedWriter` compiles cleanly within `hestia/mneme-cluster`.

### ✓ Step 3: Implement DefaultGovernedReader with Post-Convergence Token Observation
Implement `DefaultGovernedReader` in Mneme adhering to strict fail-closed security, authoritative point retrieval, and post-convergence token observation.

- Create `DefaultGovernedReader` in `hestia/mneme-cluster` under `net.fhirfactory.harmonia.hestia.mneme.access` implementing `net.fhirfactory.harmonia.model.governedwrite.GovernedReader`.
- Inject `ThemisAuthorizer`, `AuthoritativePersistencePort<IBaseResource>`, `ActiveStateConvergencePort`, and `ActiveStateCoordinator`.
- Implement Calliope managed-type validation via `GovernedBoundaryValidator.requireManagedType(key)` (throwing `IllegalArgumentException` on unmanaged types).
- Implement Themis READ authorization; on `DENY` or authorization failure, throw canonical `SecurityException` fail-closed (`AccessDenied != ResourceAbsent`).
- Execute authoritative point read via `persistencePort.read(key)`. Map `result instanceof Absent` to `Optional.empty()`.
- On `NotCommitted` or `OutcomeUnknown`, throw `ActiveCoordinationUnavailableException` fail-closed.
- On successful authoritative retrieval (`Committed`), invoke `convergencePort.converge(key, payload, version)`.
- If convergence status is not `CONVERGED` (e.g. `DEGRADED`), throw `ActiveCoordinationUnavailableException` fail-closed without fabricating an `ActiveStateToken`.
- On `CONVERGED`, observe fresh `ActiveStateToken` via `activeStateCoordinator.observe(key)`.
- Return `Optional.of(GovernedRead.of(key, payload, activeToken, authoritativeVersion))`.

### * Step 4: Establish Unit, Contract, and Architecture Test Conformance for M3.1
Verify M3.1 invariants, absence differentiation, and subsystem boundaries through comprehensive unit and ArchUnit tests.

- Implement `DefaultGovernedReaderTest` in `hestia/mneme-cluster` verifying: authorized existing resource, unmanaged type rejection, Themis denial (`SecurityException`), authoritative absence (`Absent` -> `Optional.empty()`), degraded convergence (`ActiveCoordinationUnavailableException`), `NotCommitted` fail-closed, and transport failure.
- Update `GovernedWriteCompositionArchitectureTest` in `paradeigma/paradeigma-test`: assert `DefaultGovernedReader` and `DefaultGovernedWriter` reside in `net.fhirfactory.harmonia.hestia.mneme.access` and verify zero JPA/Hibernate/Infinispan leaks.
- Run `mvn test -pl hestia/mnemosyne-api,hestia/mneme-persistence,hestia/mneme-cluster,paradeigma/paradeigma-test` to confirm all tests pass cleanly.