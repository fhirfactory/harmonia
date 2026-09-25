---
sessionId: session-260924-205024-1tmn
---

# Requirements

### Goal / Outcome
Close the remaining Mnemosyne mutation paths that bypass the immutable Kleio audit architecture. Ensure that Mnemosyne exposes no external or internal generic FHIR endpoint or storage facility capable of creating, updating, patching, deleting, or resurrecting `AuditEvent` evidence, leaving Kleio as the sole authoritative audit append owner and Iris BEFE as the authoritative external read/search facade.

### Scope
- **In Scope**:
  - Remove `AuditEventResourceProvider` from `mnemosyne-clinical` so no direct HAPI FHIR AuditEvent endpoints (`/fhir/AuditEvent`) are exposed.
  - Implement a central defense-in-depth immutability guard in `FhirStorageService` rejecting `createResource`, `updateResource`, and `deleteResource` when `resourceType` is `AuditEvent`.
  - Maintain absolute decoupling between `FhirStorageService` and Kleio (no imports or calls to `AuditService`, `DurableAuditService`, or Kleio repositories).
  - Update Mnemosyne integration tests (`FhirResourceCrudIntegrationTest`) to assert rejection of AuditEvent mutation instead of legacy CRUD, while verifying ordinary non-AuditEvent CRUD operations remain intact.
  - Inspect and document remaining write-behind/cache artifacts (`auditevent-cache`, `FhirRestCacheStore`) and prove they cannot mutate AuditEvent.
  - Add ArchUnit architecture guardrails ensuring Mnemosyne does not register mutable AuditEvent providers or import Kleio persistence.
  - Run focused regressions for Kleio append-only integrity, BEFE Step 04.4 read/search, Themis Step 04.3 authorization, and architecture rules.
- **Out of Scope**:
  - Iris UI changes (Step 04.6).
  - Data migration, modification, or deletion of existing `hie_fhir_resources` rows.
  - Dedicated audit database relocation (AUDIT-BL-02) or PostgreSQL trigger changes (AUDIT-BL-03).
  - Broad redesign of Infinispan caching (Task 07) or Clinical write/search pipelines (Tasks 08–10).
  - Modifying Kleio append contracts or BEFE `AuditEventResource`.

### Done When
- `AuditEventResourceProvider.java` is removed from `mnemosyne-clinical` and no Spring `IResourceProvider` bean exposes `AuditEvent`.
- `FhirStorageService` explicitly fails closed with `ForbiddenOperationException` upon any attempt to create, update, or delete an `AuditEvent`.
- Generic persistence for non-AuditEvent clinical resources (`Person`, `Practitioner`, `Provenance`, `Consent`, `DocumentReference`, etc.) functions without regression.
- New/updated architecture tests enforce Mnemosyne AuditEvent provider absence and Kleio isolation.
- All Kleio, BEFE, Themis, Mnemosyne, and ArchUnit test suites pass cleanly.

---

# Technical Design

### Decisions
- **Decision: Remove `AuditEventResourceProvider` entirely rather than retain read/search in Mnemosyne**
  - *Rationale*: Iris BEFE is the authoritative external read/search facade established in Step 04.4. Codebase inspection confirms zero runtime consumers require Mnemosyne AuditEvent read/search. Deleting the provider eliminates the entire `/fhir/AuditEvent` attack surface cleanly without introducing dead code or secondary query APIs.
- **Decision: Small central `assertMutableResourceType` guard in `FhirStorageService`**
  - *Rationale*: Placing a single check at the mutation entry points (`createResource`, `updateResource`, `deleteResource`) provides robust defense-in-depth against direct Java calls without scattering string checks across repository queries or inventing complex policy frameworks.
- **Decision: Throw `ForbiddenOperationException` on AuditEvent mutation attempt**
  - *Rationale*: `ForbiddenOperationException` is already imported and standard across HAPI FHIR and `FhirStorageService` for security and operation rejections, ensuring a standard HTTP 403 / Forbidden response without leaking internal persistence details.
- **Decision: Retain passive cache mapping in `FhirRestCacheStore` as harmless legacy artifact**
  - *Rationale*: Broad Infinispan XML or cache-mapping changes risk touching shared store code outside this step. Because the Mnemosyne endpoint and storage service fail closed on AuditEvent, the cache write-behind path is completely neutralized without invasive cache cluster redesign.

### Approach & Touches
- **Target Files / Components**:
  - Delete `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java`.
  - Update `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java` to add `assertMutableResourceType(String resourceType)`.
  - Update `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/FhirResourceCrudIntegrationTest.java` (`testAuditEventLifecycle` -> assert mutation rejection).
  - Update / add unit tests in `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageServiceSecurityTest.java` (or dedicated test class) verifying generic storage rejection for CREATE/UPDATE/DELETE of AuditEvent.
  - Update `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java` to enforce that Mnemosyne has no `AuditEvent` resource provider and does not depend on Kleio persistence.

### Nuances, Risks & Corners
- **Pylai Generic Gateway Consideration**: `pylai-fhir-registry` has generic `/{resourceType}/{id}` routes delegating to `FhirStorageService.getResource`. It is not deployed for AuditEvent, but since `FhirStorageService` read operations remain read-only and mutations reject AuditEvent, no bypass exists. If a caller requires AuditEvent queries, it must use Iris BEFE.
- **Zero Kleio Coupling in Mnemosyne**: `FhirStorageService` must strictly reject AuditEvent with `ForbiddenOperationException` and must NEVER import or delegate to `AuditService` or `DurableAuditService`.
- **Soft-Delete / Resurrection Defense**: `updateResource` can resurrect soft-deleted entities if called with an existing ID. The `assertMutableResourceType` check at the start of `updateResource` blocks both overwrite and resurrection.
- **PHI / Logging Safety**: Rejection messages must not log FHIR resource payloads or PHI. Log only the rejected operation and resource type.

---

# Testing

### Scenarios & Verification Targets
- **Repro / Mutation Bypass Prevention**: Attempting to CREATE, UPDATE, or DELETE an `AuditEvent` via `FhirStorageService` immediately throws `ForbiddenOperationException` and persists nothing.
- **HAPI Provider Surface**: GET, POST, PUT, DELETE against `/fhir/AuditEvent` on Mnemosyne returns HTTP 404 / 405 (no resource provider registered).
- **Non-AuditEvent CRUD Preservation**: Ordinary lifecycle operations (Create, Read, Update, Delete) for `Person`, `Practitioner`, `Provenance`, `Consent`, `DocumentReference`, and `Organization` succeed without regression.
- **Kleio Append Invariant**: Kleio append tests verify `INSERT ... ON CONFLICT DO NOTHING`, idempotent replay on match, and `AuditIntegrityException` on payload divergence.
- **Iris BEFE Verification**: Step 04.4 `AuditEventResourceTest` confirms READ/SEARCH queries route to `AuditService` with AUDIT domain authorization.
- **Architecture Enforcement**: ArchUnit tests verify absence of `AuditEventResourceProvider`, absence of Kleio dependencies in `mnemosyne-clinical`, and enforce Kleio audit model exclusivity.

---

# Assumptions & Open Questions

- **Assumption: Complete removal of `AuditEventResourceProvider`**: Codebase inspection proved that no legitimate production component relies on Mnemosyne `/fhir/AuditEvent`. Iris BEFE is the sole authoritative audit endpoint. Removing the provider entirely is cleaner and safer than keeping a read-only shell.
- **Assumption: Passive retention of `auditevent-cache` configuration**: `infinispan.xml` contains `auditevent-cache`, but since BEFE does not use `FhirCacheService` for AuditEvent and Mnemosyne rejects any write-behind PUT/DELETE, the cache configuration is neutralized without performing an out-of-scope cache cluster refactor.

---

# Delivery Steps

### ✓ Step 1: Remove AuditEvent Provider and Add Central Storage Immutability Guard
Goal: Eliminate the HAPI AuditEvent endpoint in Mnemosyne and install a central defense-in-depth guard in generic FHIR storage rejecting AuditEvent mutations.
Scope: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java`, `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java`
Acceptance Criteria:
- [ ] `AuditEventResourceProvider.java` is deleted from `hestia/mnemosyne-clinical`.
- [ ] `JpaRestfulServer` no longer discovers or registers an `AuditEvent` resource provider.
- [ ] `FhirStorageService` defines a private `assertMutableResourceType(String resourceType)` method throwing `ForbiddenOperationException` when resource type is `"AuditEvent"` (case-insensitive).
- [ ] `assertMutableResourceType` is invoked at the entry of `createResource(...)`, `updateResource(...)`, and `deleteResource(...)`.
- [ ] `FhirStorageService` contains zero imports of or dependencies on `net.fhirfactory.harmonia.kleio..` classes.
- [ ] Diagnostic logging around rejected AuditEvent mutations is PHI-safe and contains no payload or SQL details.
Verification: `mvn clean test-compile -pl hestia/mnemosyne-clinical` → green

### ✓ Step 2: Update Mnemosyne Tests and Verify Non-AuditEvent CRUD Integrity
Goal: Update Mnemosyne integration and security tests to assert AuditEvent mutation rejection and verify full regression safety for ordinary clinical resources.
Scope: `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/FhirResourceCrudIntegrationTest.java`, `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageServiceSecurityTest.java`
Acceptance Criteria:
- [ ] `FhirResourceCrudIntegrationTest.testAuditEventLifecycle` is updated to assert that AuditEvent cannot be created via the HAPI REST client or `FhirStorageService`.
- [ ] Unit/integration tests verify `createResource`, `updateResource`, and `deleteResource` in `FhirStorageService` fail closed with `ForbiddenOperationException` when given an `AuditEvent`.
- [ ] Verification confirms that soft-deleted AuditEvent resurrection is impossible through `updateResource`.
- [ ] Tests for `Person`, `Practitioner`, `Provenance`, `Consent`, `DocumentReference`, and `Organization` pass without changes to generic CRUD behavior.
Verification: `mvn test -pl hestia/mnemosyne-clinical -Dtest=FhirResourceCrudIntegrationTest,FhirStorageServiceSecurityTest,ProviderRegistrySearchTest,ProviderRegistryReferenceValidatorTest` → green

### ✓ Step 3: Add Architecture Guardrails and Execute Multi-Subproject Regression Suite
Goal: Add ArchUnit rules enforcing Mnemosyne AuditEvent provider absence and run end-to-end regression across Kleio, BEFE, Themis, and architecture suites.
Scope: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`
Acceptance Criteria:
- [ ] `SecurityEnforcementArchitectureTest` includes an ArchUnit rule asserting no class in `net.fhirfactory.harmonia.hapifhir..` provides an `IResourceProvider` for `AuditEvent` or exposes mutable AuditEvent methods.
- [ ] `SecurityEnforcementArchitectureTest` asserts that `hestia/mnemosyne-clinical` has zero dependencies on `net.fhirfactory.harmonia.kleio..`.
- [ ] Kleio test suite passes cleanly, confirming append-only semantics and collision detection are intact.
- [ ] Iris BEFE `AuditEventResourceTest` passes cleanly, confirming authoritative READ/SEARCH via `AuditService`.
- [ ] Themis test suite passes cleanly, confirming AUDIT domain default-deny enforcement.
- [ ] All ArchUnit architecture tests pass cleanly.
Verification: `mvn test -pl kleio/kleio-audit,kleio/kleio-persistence,themis/themis-core,iris/iris-befe,paradeigma/paradeigma-test -Dtest="*Test,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green