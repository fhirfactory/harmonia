---
sessionId: session-260924-215244-loce
---

# Requirements

### Goal / Outcome
Execute the final architecture convergence, multi-boundary verification, and hardening pass for Task 06 (Immutable Audit Evidence). The task verifies that once accepted by Kleio, a Harmonia AuditEvent cannot be created, modified, patched, or deleted through any supported application path, closes any remaining proportionate guardrail gaps, documents residual risks, and formally closes Task 06.

### Scope
- **In Scope**:
  - Verification of canonical boundary ownership (`HarmoniaAuditEvent`, `AuditService` append-only semantics).
  - Verification of durable persistence invariants (PostgreSQL `INSERT ... ON CONFLICT DO NOTHING`, idempotent canonical replay, collision integrity failure, no SQL updates/deletes).
  - Multi-boundary external mutation verification across Themis, BEFE, Mnemosyne, Iris Clinical, passive cache/write-behind, and Pylai routes.
  - Architecture guardrail gap analysis and addition of any missing targeted ArchUnit assertions (e.g., forbidding direct AuditEvent persistence calls).
  - Executing full relevant regression suites (ArchUnit, Kleio, Themis, BEFE, Mnemosyne, Iris Vitest).
  - Residual-risk classification and compiling the final Task 06 closure report.
- **Out of Scope**:
  - Redesigning the audit, cache, or persistence architecture.
  - Implementing AUDIT-BL-02 (relocation to dedicated Operations DB) or AUDIT-BL-03 (database WORM triggers/permissions).
  - Implementing Petasos transitions, replay engine, or Provenance models (deferred to subsequent tasks).
  - ActiveMQ / JMS modifications or broad cache restructuring (Task 07).

### Done When
1. All canonical, durable persistence, and external mutation boundaries are proven to enforce immutability without mutation bypasses.
2. Architecture test suite (`SecurityEnforcementArchitectureTest`) and module tests across all layers pass cleanly.
3. Residual artifacts are cataloged (Accepted vs. Backlog vs. Blocking), with the absence of production producers explicitly handled as a functional integration backlog item.
4. The authoritative Task 06 invariant and scope boundaries are formally documented in the closure report.

# Technical Design

### Decisions
- **Verification and hardening over redesign**: Maximize utility and stability by preserving the existing durable Kleio architecture rather than creating new abstractions or churn.
- **Treat absence of production audit producers as a documented backlog/enablement item**: The core audit append contract (`AuditService.append`) and durable PostgreSQL persistence are verified and functioning; wiring producer callers belongs to respective domain workflows (Pylai/Themis/Energeia) and does not invalidate the immutability boundary.
- **Retain passive `auditevent-cache` configuration as a documented non-blocking residual**: Because Mnemosyne has no AuditEvent provider and `FhirStorageService` fails closed on AuditEvent mutation, the passive cache cannot execute an end-to-end mutation; broad cache cleanup is deferred to Task 07.
- **Targeted ArchUnit guardrails for boundary protection**: If any subtle exposure exists (e.g. direct repository access), harden ArchUnit rules rather than restructuring working production code.

### Approach & Touches
- **Kleio Core & Persistence**:
  - `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java` (immutable record, defensive copies).
  - `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditService.java` (append/read/query contract).
  - `kleio/kleio-persistence/src/main/java/net/fhirfactory/harmonia/kleio/persistence/service/DurableAuditService.java` (idempotency, collision detection).
  - `kleio/kleio-persistence/src/main/java/net/fhirfactory/harmonia/kleio/persistence/repository/JdbcAppendOnlyAuditEventRepository.java` (atomic append, no update/delete SQL).
- **Security & Façades**:
  - `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/AuditImmutabilityDenyPolicy.java` (explicit deny for mutation).
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` (read/search only; no POST/PUT/PATCH/DELETE).
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java` (fast-reject on AuditEvent mutations).
  - `iris/iris-clinical/src/views/AuditEventView.vue` and `securityStore.ts` (read-only UI and search queries).
- **Architecture Enforcement**:
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java` (comprehensive boundary guardrails).

### Nuances, Risks & Corners
- **Docker Environment Dependency**: `DurableAuditServicePostgreSqlConcurrencyTest` requires a live Docker daemon for Testcontainers. In environments without Docker, H2-based integration tests (`DurableAuditServiceH2Test`, `JdbcAppendOnlyAuditEventRepositoryTest`) provide deterministic coverage, while PostgreSQL concurrency tests serve as the full integration verification when Docker is active.
- **Failing Closed on Unknown Parameters**: External BEFE search rejects non-`_id` query parameters (e.g., `name`, `identifier`) to prevent arbitrary query leakage.

# Testing

- Verify canonical immutability: `HarmoniaAuditEvent` record invariants and `AuditService` API contract.
- Verify durable append semantics: Idempotent replay of identical event, `AuditIntegrityException` on divergent event with same ID, and soft-delete failure.
- Verify Themis explicit deny: AUDIT CREATE/UPDATE/DELETE denied across all roles including `system.admin`.
- Verify BEFE read-only façade: Absence of `@POST`, `@PUT`, `@DELETE`, `@PATCH` endpoints and absence of `FhirCacheService` injection.
- Verify Mnemosyne isolation: `FhirStorageService` rejection of create, update, delete, and soft-delete resurrection without hitting repository.
- Verify Iris UI read-only assurance: Vitest assertions for lack of mutation handlers, buttons, or store mutation methods.
- Regression suites: Full ArchUnit suite (`SecurityEnforcementArchitectureTest`) and subproject unit/integration test suites.

# Assumptions & Open Questions

- **Absence of Production Producer Callers**:
  - *Option Chosen*: Classify as a documented functional enablement backlog item rather than a Task 06 blocker.
  - *Rationale*: Task 06 establishes and hardens the immutable audit persistence boundary. Upstream event emission will be integrated as producers (Themis, Energeia, Pylai) adopt the contract.
  - *Alternative*: Require artificial producer wiring in Step 04.7, which would violate the non-redesign constraint and expand scope unnecessarily.
  - *Impact*: Documented in final closure report; no production code changes required for this slice.
- **Passive Cache Configuration**:
  - *Option Chosen*: Retain passive `auditevent-cache` configuration in `infinispan.xml` as an accepted non-blocking residual.
  - *Rationale*: Mnemosyne rejects AuditEvent at the storage boundary and registers no AuditEvent provider, preventing end-to-end mutation execution. Broad cache lifecycle redesign belongs to Task 07.

# Delivery Steps

### ✓ Step 1: Verify Canonical Boundary and Durable Append Semantics
Goal: Prove that the Kleio canonical model remains immutable, AuditService enforces append-only semantics, and durable persistence prevents modification or collision corruption.
Scope: `kleio/kleio-core`, `kleio/kleio-fhir`, `kleio/kleio-persistence`, `iris/iris-befe/src/test/.../KleioRuntimeCompositionTest.java`.
Acceptance Criteria:
- [ ] `HarmoniaAuditEvent` remains an immutable record with defensive copies and no mutation methods.
- [ ] `AuditService` interface declares only append/query/read methods (no update, replace, patch, delete).
- [ ] `HarmoniaAuditEventMapper` maintains round-trip canonical fidelity (`fromFhir(toFhir(event)) == event`).
- [ ] `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository` enforce atomic insert, idempotent replay, collision rejection, and soft-delete rejection.
- [ ] BEFE runtime composition correctly wires `DurableAuditService` with `@KleioAudit` DataSource.
Verification: `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,iris/iris-befe -am -Dtest="HarmoniaAuditEvent*,AuditService*,HarmoniaAuditEventMapper*,DurableAuditServiceH2*,JdbcAppendOnly*,KleioRuntimeComposition*"` → green

### ✓ Step 2: Verify External Mutation Boundaries and Close Guardrail Gaps
Goal: Validate that all external boundaries (Themis, BEFE, Mnemosyne, Iris UI, Pylai, and Cache) block AuditEvent mutation, and ensure ArchUnit architecture tests strictly enforce these boundaries.
Scope: `themis/themis-core`, `iris/iris-befe`, `hestia/mnemosyne-clinical`, `iris/iris-clinical`, `paradeigma/paradeigma-test`.
Acceptance Criteria:
- [ ] Themis `AuditImmutabilityDenyPolicy` denies CREATE, UPDATE, DELETE for AUDIT domain across all roles (including `system.admin`).
- [ ] BEFE `AuditEventResource` exposes only read/search endpoints and rejects non-`_id` search parameters.
- [ ] Mnemosyne `FhirStorageService` fails closed on AuditEvent mutation before repository access, and no `AuditEventResourceProvider` exists.
- [ ] Iris Clinical frontend (`AuditEventView.vue` and `securityStore.ts`) contains no mutation forms, buttons, or store mutation methods.
- [ ] `SecurityEnforcementArchitectureTest` contains ArchUnit assertions covering Kleio independence, Mnemosyne isolation, BEFE read-only contracts, and canonical exclusivity.
Verification: `mvn test -pl paradeigma/paradeigma-test,themis/themis-core,hestia/mnemosyne-clinical,iris/iris-befe -am -Dtest="SecurityEnforcementArchitectureTest,AuditImmutabilityDenyPolicyTest,FhirStorageServiceSecurityTest,AuditEventResourceTest" && (cd iris/iris-clinical && npm test)` → green

### ✓ Step 3: Execute Full Regression, Compile Residual-Risk Inventory, and Close Task 06
Goal: Run full system regression verification, document residual risks and backlog items, and formally close Task 06 with the authoritative invariant statement.
Scope: Full repository test suite, `.junie/plans/task-06-closure-report.md` (or summary delivery).
Acceptance Criteria:
- [ ] Broadest practical regression test suites pass cleanly across all touched subprojects.
- [ ] Residual items are categorized: Accepted (read-only Pylai façade), Backlog (AUDIT-BL-02 physical relocation, AUDIT-BL-03 database WORM controls, passive write-behind cache cleanup in Task 07, production producer integration), Blocking (None).
- [ ] Final closure report documents the trusted append path, external read path, verified mutation boundaries, guardrails, and test results.
- [ ] Formally affirms: "Once accepted by Kleio as audit evidence, a Harmonia AuditEvent cannot be modified or deleted through any supported application path." (Application-level immutability guarantee).
Verification: `mvn test -pl kleio/kleio-core,kleio/kleio-fhir,kleio/kleio-persistence,themis/themis-core,iris/iris-befe,hestia/mnemosyne-clinical,paradeigma/paradeigma-test -am -Dtest="*Audit*,*Kleio*,*FhirStorageServiceSecurityTest*,SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false && (cd iris/iris-clinical && npm test)` → green