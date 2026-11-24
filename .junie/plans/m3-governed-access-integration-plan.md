---
sessionId: session-261002-100545-1hcw
---

# Aim

Implement Milestone **M3.1 — Governed READ Integration & Active-State Generation Observation**, establishing Harmonia's production point-read path (`GovernedReader`) under Mneme subsystem ownership. The path unifies fail-closed Calliope boundary validation, Themis authorization (`SecurityException` on DENY), authoritative point retrieval from Mnemosyne via `AuthoritativePersistencePort`, post-read active-state cache convergence via `ActiveStateConvergencePort`, and post-convergence `ActiveStateToken` observation.

# Architectural Constraints

- **Subsystem Ownership**: Mneme owns application-facing Governed Access (`DefaultGovernedReader`, `DefaultGovernedWriter`). Mnemosyne owns authoritative durable state and version progression.
- **Fail-Closed Security**: Themis READ authorization is evaluated locally before authoritative retrieval. A `DENY` decision throws canonical `SecurityException` (`AccessDenied != ResourceAbsent`).
- **Authoritative Absence**: `Optional.empty()` is returned if and only if an authorized authoritative read from Mnemosyne yields HTTP 404 (`NotCommitted`).
- **Convergence & Token Invariant**: A valid `ActiveStateToken` represents an observed generation of a successfully converged Mneme active representation. Token observation (`ActiveStateCoordinator.observe(key)`) occurs strictly *after* successful cache convergence (`ConvergenceStatus.CONVERGED`).
- **Degraded Convergence**: If convergence degrades or fails (`ConvergenceStatus.DEGRADED`), no valid `ActiveStateToken` may be returned; an `IllegalStateException` is thrown fail-closed.
- **Version Domain Separation**: Mneme `ActiveStateToken` and Mnemosyne `AuthoritativeVersion` remain strictly distinct.
- **Scope Discipline**: Zero physical DELETE (ADR-020), zero search/batch (M5), zero application migration (M4), zero MicroK8s (M6), zero two-level UPDATE concurrency/rebase (M3.2/M3.3), zero distributed Docker integration (M3.4).

# Repository Findings

- **GovernedReader Contract**: `net.fhirfactory.harmonia.model.governedwrite.GovernedReader` in `calliope` defines `<T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext)`.
- **GovernedAccess Composition**: `net.fhirfactory.harmonia.model.governedwrite.GovernedAccess` in `calliope` extends `GovernedReader` and `GovernedWriter`.
- **Mislocated Writer**: `DefaultGovernedWriter` currently resides in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`), violating Mneme ownership of application-facing access. It must be relocated to `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`).
- **Boundary Validation**: `GovernedBoundaryValidator.requireManagedType(ResourceKey)` in `calliope` enforces managed resource types fail-closed (throwing `IllegalArgumentException`).
- **Themis Authorization**: `ThemisAuthorizer` in `themis-api` evaluates `ThemisAuthorizationRequest` with `ThemisAction.READ`, `ThemisPrincipal`, `ThemisResource`, and `ThemisSecurityContext`.
- **Authoritative Persistence Port**: `AuthoritativePersistencePort<IBaseResource>` in `hestia/mnemosyne-api` exposes `read(ResourceKey)`, returning `AuthoritativePersistenceResult<T>` (`Committed`, `NotCommitted`, `OutcomeUnknown`). Backed in production by `MnemeAuthoritativeHttpClient` in `hestia/mneme-persistence`.
- **Active Convergence Port**: `ActiveStateConvergencePort` in `calliope` exposes `converge(ResourceKey, T, AuthoritativeVersion)`, implemented by `HotRodMnemeConvergence` in `hestia/mneme-cluster`, returning `ConvergenceStatus.CONVERGED` or `DEGRADED`.
- **Active State Observation**: `ActiveStateCoordinator.observe(ResourceKey)` in `calliope`, implemented by `HotRodActiveStateCoordinator` in `hestia/mneme-cluster`, retrieves the current opaque `ActiveStateToken`.
- **Missing Dependency**: `hestia/mneme-cluster/pom.xml` currently lacks a direct dependency on `net.fhirfactory.harmonia:themis-api`.
- **Architecture Tests**: `GovernedWriteCompositionArchitectureTest` currently asserts `DefaultGovernedWriter` resides in `mnemosyne-clinical` and must be updated to assert Mneme package `net.fhirfactory.harmonia.hestia.mneme.access`.

# Files to Change

| File | Module | Action | Description |
| :--- | :--- | :--- | :--- |
| `pom.xml` | `hestia/mneme-cluster` | Modify | Add `<dependency>` on `net.fhirfactory.harmonia:themis-api`. |
| `DefaultGovernedReader.java` | `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) | Add | Create Mneme-owned implementation of `GovernedReader`. |
| `DefaultGovernedWriter.java` | `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) | Relocate | Move from `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) to establish correct subsystem package. |
| `DefaultGovernedReaderTest.java` | `hestia/mneme-cluster` | Add | Unit tests for `DefaultGovernedReader` covering all read outcomes and invariants. |
| `DefaultGovernedWriterTest.java` | `hestia/mneme-cluster` | Relocate | Move test class alongside relocated `DefaultGovernedWriter`. |
| `GovernedWriteCompositionArchitectureTest.java` | `paradeigma/paradeigma-test` | Modify | Update package rules to assert `DefaultGovernedReader` and `DefaultGovernedWriter` reside in `net.fhirfactory.harmonia.hestia.mneme.access`. |

# Implementation Steps

1. **Configure Dependencies & Relocate Governed Access**:
   - Add `themis-api` dependency to `hestia/mneme-cluster/pom.xml`.
   - Relocate `DefaultGovernedWriter` and its unit test `DefaultGovernedWriterTest` from `hestia/mnemosyne-clinical` to `hestia/mneme-cluster` under package `net.fhirfactory.harmonia.hestia.mneme.access`.
   - Update `GovernedWriteCompositionArchitectureTest` in `paradeigma-test` to enforce the new package location.

2. **Implement `DefaultGovernedReader`**:
   - Create `DefaultGovernedReader` in `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) implementing `GovernedReader`.
   - Inject `ThemisAuthorizer`, `AuthoritativePersistencePort<IBaseResource>`, `ActiveStateConvergencePort`, and `ActiveStateCoordinator`.
   - Implement `read(key, securityContext)` with the following sequence:
     1. **Calliope Validation**: `GovernedBoundaryValidator.requireManagedType(key)` (throws `IllegalArgumentException` on unmanaged types).
     2. **Themis Authorization**: Build `ThemisAuthorizationRequest(ThemisAction.READ, ...)` and call `themisAuthorizer.authorize(req)`. If `decision == null || decision.isDeny()`, throw `SecurityException("Themis authorization denied: " + reason)`.
     3. **Authoritative Point Read**: Call `persistencePort.read(key)`.
        - If `result instanceof AuthoritativePersistenceResult.NotCommitted`: return `Optional.empty()` (resource absent authoritatively, zero cache convergence).
        - If `result instanceof AuthoritativePersistenceResult.OutcomeUnknown unknown`: throw `IllegalStateException("Authoritative read outcome unknown: " + unknown.message())`.
        - If `result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed`: proceed to convergence.
     4. **Mneme Active Convergence**: Call `convergencePort.converge(key, committed.persistedResource(), committed.authoritativeVersion())`.
        - If status is `DEGRADED` (or exception occurs): throw `IllegalStateException("Active-state cache convergence degraded for " + key)` fail-closed (do not return a false or unverified token).
     5. **Active State Observation**: Call `activeStateCoordinator.observe(key)` *after* successful convergence to obtain fresh `ActiveStateToken`.
     6. **GovernedRead Construction**: Return `Optional.of(GovernedRead.of(key, (T) committed.persistedResource(), activeToken, committed.authoritativeVersion()))`.

3. **Verify with Unit and Architecture Tests**:
   - Implement comprehensive `DefaultGovernedReaderTest` in `hestia/mneme-cluster`.
   - Run unit and ArchUnit suites to ensure strict compliance with architectural rules.

# Tests

### Unit Test Scenarios (`DefaultGovernedReaderTest`):
- `read_unmanagedType_throwsIllegalArgumentException`: Unsupported resource type rejected fail-closed before authorization or persistence.
- `read_themisDenied_throwsSecurityException`: Themis policy denial throws `SecurityException` fail-closed (`AccessDenied != ResourceAbsent`).
- `read_absentResource_returnsEmptyOptional`: Authoritative HTTP 404 / `NotCommitted` returns `Optional.empty()` with zero convergence or token observation.
- `read_existingResource_returnsGovernedReadWithPostConvergenceToken`: Successful authoritative retrieval executes convergence, then observes token, returning `Optional.of(GovernedRead)`.
- `read_convergenceDegraded_throwsIllegalStateException`: Post-read convergence returning `DEGRADED` throws `IllegalStateException` fail-closed (no invalid token emitted).
- `read_outcomeUnknown_throwsIllegalStateException`: Indeterminate transport result throws `IllegalStateException`.
- `read_nullArguments_throwsNullPointerException`: Null key or security context rejected immediately.

### Architecture Tests (`GovernedWriteCompositionArchitectureTest`):
- `defaultGovernedReaderMustResideInMnemeCluster`: Verifies `DefaultGovernedReader` resides in `net.fhirfactory.harmonia.hestia.mneme.access`.
- `defaultGovernedWriterMustResideInMnemeCluster`: Verifies `DefaultGovernedWriter` resides in `net.fhirfactory.harmonia.hestia.mneme.access`.
- `defaultGovernedReaderMustNotDependOnInfinispanOrJpa`: Asserts zero direct JPA/Infinispan leaks into `DefaultGovernedReader`.

# Acceptance Criteria

1. `DefaultGovernedReader` resides in `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`) and implements `GovernedReader`.
2. `DefaultGovernedWriter` is relocated to `hestia/mneme-cluster` (`net.fhirfactory.harmonia.hestia.mneme.access`).
3. Calliope boundary validation (`GovernedBoundaryValidator`) rejects unmanaged types fail-closed with `IllegalArgumentException`.
4. Themis authorization denial throws `SecurityException` fail-closed and is never translated to `Optional.empty()`.
5. Authoritative persistence read is performed via `AuthoritativePersistencePort<IBaseResource>`; absent resources return `Optional.empty()`.
6. Successful authoritative read converges Mneme active data cache via `ActiveStateConvergencePort`.
7. `ActiveStateToken` is observed strictly *after* successful cache convergence.
8. Failed/degraded convergence throws `IllegalStateException` fail-closed and never emits a fabricated active token.
9. All unit tests in `DefaultGovernedReaderTest` and `DefaultGovernedWriterTest` pass cleanly.
10. All ArchUnit tests in `paradeigma-test` pass with zero regressions.

# Conflicting Repository Findings

1. **DefaultGovernedWriter Placement**: Currently located in `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java`, conflicting with the axiom that Mneme owns application-facing governed access. Fixed in M3.1 via relocation to `hestia/mneme-cluster`.
2. **ArchUnit Test Location Rule**: `GovernedWriteCompositionArchitectureTest.defaultGovernedWriterMustResideInMnemosyneClinical` expects `DefaultGovernedWriter` in `net.fhirfactory.harmonia.hapifhir.governed..`. Fixed in M3.1 by updating the rule to require `net.fhirfactory.harmonia.hestia.mneme.access..`.
3. **Themis Dependency in Mneme Cluster**: `hestia/mneme-cluster/pom.xml` lacks `themis-api`, required for local Themis authorization evaluation in `DefaultGovernedReader` and `DefaultGovernedWriter`. Fixed in M3.1 by declaring `<dependency>` on `net.fhirfactory.harmonia:themis-api`.