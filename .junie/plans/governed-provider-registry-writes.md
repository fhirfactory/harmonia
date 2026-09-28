---
sessionId: session-260927-161728-1r6b
---

# Requirements

### Overview & Goals
Establish the architectural findings, stop-condition evaluation, and corrected execution sequence for **Harmonia Convergence — Goal 2 (Governed Provider Registry Writes)**.

In accordance with Architectural Invariants 1–5, AX-05, and ADR-020, Goal 2 implementation is paused at the mandatory architectural stop conditions:
1. **Unresolved Runtime Invocation Boundary**: No existing executable mechanism connects the WildFly presentation tier (`iris-befe`) to the authoritative write orchestrator (`DefaultGovernedWriter` / `AuthoritativePersistencePort`) in the Spring Boot Mnemosyne runtime without violating Invariant 3 (presentation decoupling) or Invariant 5 (no invented transports).
2. **Point-Read Prerequisite for UPDATE**: Legitimate `GovernedRead<T>` envelopes (carrying `ActiveStateToken` and `ExpectedAuthoritativeVersion`) cannot be manufactured by presentation code or inferred from HTTP headers; they require a governed point-read path (prerequisite from Goal 3A).

### Scope
- **In Scope (Plan Correction & Stop-Condition Analysis)**:
  - Exhaustive proof of the runtime boundary between `iris-befe` and `mnemosyne-clinical`.
  - Identification of the `GovernedRead<T>` acquisition path and prerequisite dependency on governed point-read.
  - Clarification of version domain separation (`meta.versionId`, `ETag`, `ActiveStateToken`, `AuthoritativeVersion`).
  - Analysis of resource-specific lifecycle semantics vs HTTP DELETE interface handling under ADR-020.
  - Revised stop-condition assessment and grounded implementation sequence.
- **Out of Scope**:
  - Direct implementation of write code prior to architectural resolution of the runtime boundary.
  - Inventing ad-hoc REST/RPC transports or embedding Mnemosyne persistence into WildFly.
  - Fabricating synthetic management tokens in the presentation layer.

# Technical Design

### 1. BEFE -> Mnemosyne Governed-Write Runtime Path
- **Runtime Evidence**:
  - `iris-befe` runs as a `.war` deployment inside WildFly 31+ Jakarta EE 10 (`harmonia-befe`, port 8080).
  - `DefaultGovernedWriter` and `AuthoritativePersistenceService` reside in `hestia/mnemosyne-clinical` running as a standalone Spring Boot application (`harmonia-hapi-fhir-1`, port 8081).
  - `iris-befe` maintains Hot Rod TCP connectivity to Infinispan (`harmonia-infinispan-node1`, port 11222) via `HotRodClientProducer` (`RemoteCacheManager`).
- **Invocation Path Analysis**:
  - Java CDI injection cannot inject Spring beans residing in a separate JVM/container.
  - Invariant 3 strictly forbids `iris-befe` from adding JPA/Hibernate/PostgreSQL dependencies to run `AuthoritativePersistencePort` in-process.
  - Hot Rod connectivity to Infinispan provides active cache mutation and lock coordination (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`), but does **not** provide execution dispatch to `DefaultGovernedWriter` or `AuthoritativePersistencePort` in Mnemosyne.
  - There is **no existing executable cross-JVM invocation mechanism** (no EJB remoting, no existing internal REST/RPC client, no Artemis message flow) connecting `iris-befe` to `DefaultGovernedWriter`.
- **Status**:
  **Goal 2 is blocked by an unresolved application-to-authoritative-write runtime boundary.** An architectural decision is required to establish the approved platform mechanism for cross-runtime invocation.

### 2. GovernedRead Acquisition Path for UPDATE
- **Contract Requirement**:
  - `GovernedAccess.update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)` strictly requires a valid `GovernedRead<T>` record.
  - `GovernedRead<T>` encapsulates:
    1. `ResourceKey` (type and ID);
    2. `T resource` (pristine business representation);
    3. `ActiveStateToken` (Hot Rod metadata version for Mneme active coordination);
    4. `AuthoritativeVersion` (PostgreSQL sequence version for Mnemosyne optimistic concurrency).
- **Prohibited Syntheses**:
  - Applications and presentation tiers SHALL NOT manufacture `ActiveStateToken` or `AuthoritativeVersion`.
  - Applications SHALL NOT infer tokens from HTTP `ETag`, FHIR `meta.versionId`, local counters, or uncoordinated cache reads.
- **Legitimate Source**:
  - The only legitimate source of a `GovernedRead<T>` is a prior call to `GovernedReader.read(ResourceKey key, ThemisSecurityContext securityContext)`.
  - Goal 1 established the pure interface `GovernedReader`, but implementation of point-read execution (cache-aside with authoritative fallback) is assigned to Goal 3A.
- **Status**:
  **Governed point READ is a prerequisite for UPDATE/lifecycle in Goal 2.** The minimal point-read execution capability (Goal 3A subset for Provider Registry) must precede or accompany Goal 2 UPDATE.

### 3. Version / ETag Mapping Status
- **Domain Independence**:
  Harmonia maintains four strictly independent versioning domains:
  1. `FHIR meta.versionId`: FHIR resource business version identifier.
  2. `HTTP ETag`: Presentation transport cache-validation header (e.g. `W/"..."`).
  3. `Mneme ActiveStateToken`: Volatile active coordination token managed by `ActiveStateCoordinator` in Infinispan.
  4. `Mnemosyne AuthoritativeVersion`: Durable transactional sequence version managed by `AuthoritativePersistencePort` in PostgreSQL.
- **Presentation Mapping Purity**:
  - There is no repository evidence or architectural rule establishing `authoritativeVersion == meta.versionId == ETag`.
  - The presentation response mapper must not synthesize or assume equivalence (such as `ETag: W/"{authoritativeVersion}"`).
  - Presentation mapping of external HTTP concurrency headers (`If-Match`, `ETag`) to domain read context remains a separate bounded architectural question.

### 4. DELETE / Lifecycle Interface Status
- **ADR-020 Invariants**:
  - Physical deletion (`remoteCache.remove`, SQL `DELETE`) is prohibited for governed information.
  - Lifecycle state changes are authoritative updates.
  - Cache eviction is an operational Mneme concern and does not constitute authoritative deletion.
- **Resource-Specific Lifecycle Semantics**:
  Lifecycle transitions are not a generic `active = false` flag:
  - `Practitioner`, `PractitionerRole`, `Organization`, `HealthcareService`, `Group`: FHIR boolean `.active` field (`true`/`false`).
  - `Location`: FHIR `LocationStatus` enum (`ACTIVE`, `SUSPENDED`, `INACTIVE`).
  - `Endpoint`: FHIR `EndpointStatus` enum (`ACTIVE`, `SUSPENDED`, `ERROR`, `OFF`, `ENTERED_IN_ERROR`, `TEST`).
- **HTTP DELETE Endpoint Handling**:
  - HTTP DELETE must not be silently repurposed to mutate arbitrary fields without explicit domain consensus.
  - In Goal 2, physical cache deletion in `iris-befe` is eliminated immediately.
  - Where explicit lifecycle transition semantics are not defined or authorized for an external endpoint, HTTP DELETE must fail closed, returning `405 Method Not Allowed` or `501 Not Implemented` with a diagnostic `OperationOutcome`.

### 5. Revised Dependency & Stop-Condition Assessment
- **Stop Condition 1 (Runtime Path)**: **TRIGGERED / BLOCKED**. `GovernedAccess` in WildFly cannot invoke `DefaultGovernedWriter` / `AuthoritativePersistencePort` in Spring Boot without an unresolved cross-runtime invocation mechanism.
- **Stop Condition 2 (Point-Read Prerequisite)**: **TRIGGERED / BLOCKED**. Governed UPDATE cannot execute because no legitimate `GovernedRead<T>` provider exists prior to implementing the point-read capability.
- **Stop Condition 3 (Presentation Decoupling)**: **CONFIRMED**. `iris-befe` must not solve runtime connectivity by importing JPA or connecting directly to PostgreSQL.
- **Stop Condition 4 (MAT-06 Isolation)**: **CONFIRMED**. Authoritative persistence remains encapsulated behind `AuthoritativePersistencePort`, independent of underlying relational vs JPA server decisions.

# Testing

### Validation Approach
Verification focuses on architectural conformance and boundary enforcement:
- ArchUnit rules in `paradeigma-test` asserting presentation decoupling (Invariant 3) and prohibition of raw cache mutations in `iris-befe`.
- Contract tests in `calliope` verifying that `GovernedWriter` and `GovernedReader` enforce pure domain types without presentation coupling.

### Conformance Rules
- `IrisDecouplingArchitectureTest`: Zero direct JPA, Hibernate, or PostgreSQL driver dependencies in `iris-befe`.
- `IrisGovernedWriteArchitectureTest`: Zero invocations of `RemoteCache.put`, `RemoteCache.remove`, `FhirCacheService.saveResource`, or `FhirCacheService.deleteResource` from `iris-befe` presentation controllers.

# Delivery Steps

###   Step 1: Architectural Decision — Resolve Application-to-Mnemosyne Write Invocation Mechanism
The cross-runtime invocation mechanism connecting iris-befe in WildFly to DefaultGovernedWriter and AuthoritativePersistencePort in Mnemosyne is formally resolved and specified.

- Formulate the minimal architectural decision for cross-runtime write invocation without violating Invariant 3 (no direct JPA/PostgreSQL in BEFE) or Invariant 5 (no ad-hoc transports).
- Document the concrete communication protocol and serialization contract between WildFly and Mnemosyne runtime containers.
- Align Themis security context propagation across the container boundary.

###   Step 2: Prerequisite Implementation — Establish Minimal Governed Point-Read Path
A minimal governed point-read execution engine is established to provide legitimate GovernedRead envelopes for UPDATE operations.

- Implement the point-read provider (`GovernedReader`) in Mneme capable of returning typed `GovernedRead<T>` with valid `ActiveStateToken` and `AuthoritativeVersion`.
- Ground point-read cache-aside retrieval and fallback behavior for Provider Registry resource types.
- Ensure the application layer never synthesizes management tokens locally.

###   Step 3: Eliminate Physical Deletion and Raw Cache Mutation in iris-befe
Physical deletion is eliminated from iris-befe Provider Registry endpoints in accordance with ADR-020, and raw cache mutation methods are removed.

- Remove `saveResource`, `putResourceJson`, and `deleteResource` from `FhirCacheService`.
- Refactor `@DELETE` endpoints in Provider Registry controllers to fail-closed (`405 Method Not Allowed` / `501 Not Implemented`) where explicit lifecycle transitions are not defined, or execute governed status updates where explicitly authorized.
- Eliminate all direct `RemoteCache` mutation calls across `iris-befe`.

###   Step 4: Implement Governed Provider Registry CREATE and UPDATE
Provider Registry REST endpoints in iris-befe execute CREATE and UPDATE exclusively through GovernedAccess over the approved runtime mechanism.

- Wire `GovernedAccess` in `iris-befe` to delegate CREATE and UPDATE operations across the resolved runtime invocation path to `DefaultGovernedWriter`.
- Implement `FhirWriteResultResponseMapper` to map domain `WriteResult<T>` outcomes to pure HTTP/FHIR responses without conflating `AuthoritativeVersion` with `ETag`.
- Migrate `PractitionerResource`, `PractitionerRoleResource`, `OrganizationResource`, `LocationResource`, `HealthcareServiceResource`, and `GroupResource` to use `GovernedAccess`.

###   Step 5: Mechanical Conformance Verification & ArchUnit Guardrails
Repository-wide ArchUnit architecture tests and unit test suites mechanically prevent future write governance regressions.

- Implement `IrisGovernedWriteArchitectureTest` asserting zero `RemoteCache` mutations, zero physical deletion calls, and strict Invariant 3 presentation decoupling.
- Run full Maven test suite to verify end-to-end conformance across all modules.