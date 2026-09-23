---
sessionId: session-260923-201130-kxol
---

# Requirements

### Goal & Outcome
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

### Scope
- **In Scope**:
  - Creating top-level aggregator `kleio/pom.xml` and core module `kleio/kleio-core/pom.xml`.
  - Migrating and renaming canonical audit types into `net.fhirfactory.harmonia.kleio.audit.model`: `HarmoniaAuditEvent` (from `ThemisAuditEvent`), `AuditClassification`, `AuditAction`, `AuditOutcome`, `AuditTarget`, `AuditSource`, and `AuditAuthorizationEvidence`.
  - Migrating and renaming audit service contracts into `net.fhirfactory.harmonia.kleio.audit.service`: `AuditService` (from `ThemisAuditService`), `InMemoryAuditService` (from `InMemoryThemisAuditService`), and `AuditIntegrityException` (from `ThemisAuditIntegrityException`).
  - Preserving Step 01.1 invariants: mandatory `eventId`/`recordedAt`, dual principal tracking, target projection, `HarmoniaAuditEvent.fromDecision(...)` factory, immutable snapshots, and safe `toString()` suppressing PHI (`PATIENT-PHI-MARKER-92831`) and credentials (`TOKEN-SECRET-MARKER-81742`).
  - Cleaning up vestigial classification compatibility: removing unused `CLASSIFICATION_AUDIT` and removing silent `"AUDIT" -> SECURITY` fallback in favor of strict enum parsing.
  - Decommissioning `themis-audit`: removing module from root `pom.xml` and `themis/pom.xml`, and deleting the module directory.
  - Migrating external consumer `paradeigma-test` (`ThemisDefenceInDepthAcceptanceTest` and POM dependencies).
  - Updating ArchUnit guardrails in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` to enforce Kleio ownership, forbidden dependencies, and absence of old audit classes.
- **Out of Scope**:
  - FHIR R5 `AuditEvent` durable mapping and conversion (Task 06 Step 02).
  - Provenance implementation (`HarmoniaProvenance` or FHIR `Provenance`).
  - Speculative modules (`kleio-api`, `kleio-model`, `kleio-persistence`, `kleio-reporting`).
  - Database schema changes, JPA entities, REST gateway endpoints, or Iris UI modifications.
  - Emitting audit records across other Harmonia subsystems or completing Task 06 Step 01.2 durable persistence.

### Done When
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

# Technical Design

### Decisions
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

### Approach & Touches
- Root POM `/home/hunterm/Development/Code/harmonia/pom.xml`: add `<module>kleio</module>`, add managed dependency for `kleio-core`, remove managed dependency for `themis-audit`.
- Aggregator POM `kleio/pom.xml`: parent `harmonia-parent`, declare child module `kleio-core`.
- Module POM `kleio/kleio-core/pom.xml`: jar packaging, parent `kleio`, compile dependencies on `themis-api` and `slf4j-api`, test dependencies on `junit-jupiter` and `assertj-core`.
- Model classes in `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/`:
  - `HarmoniaAuditEvent.java`: immutable record, compact constructor validating mandatory fields (`eventId`, `recordedAt`, `classification`, `action`, `outcome`), defensive copying of collections/maps, safe `toString()`, fluent builder, and `fromDecision(...)` factory.
  - `AuditClassification.java`: enum with `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
  - `AuditAction.java`: enum (`CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`) with `fromThemisAction(ThemisAction)` mapping.
  - `AuditOutcome.java`: enum (`SUCCESS`, `FAILURE`, `DENIED`) with `fromDecision(ThemisDecision)` mapping.
  - `AuditTarget.java`: immutable target descriptor record (`resourceType`, `resourceId`, `securityDomain`) with `fromResource(ThemisResource)`.
  - `AuditSource.java`: immutable source descriptor record (`subsystem`, `component`).
  - `AuditAuthorizationEvidence.java`: immutable authorization evidence record with `fromDecision(ThemisAuthorizationDecision)`.
- Service classes in `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/`:
  - `AuditService.java`: interface with `append`, `findById`, `findByCorrelationId`, `findByPrincipal`, `getRecentEvents`, and `recordDecision`.
  - `InMemoryAuditService.java`: thread-safe in-memory ring-buffer implementation enforcing idempotent replay and collision integrity failure.
  - `AuditIntegrityException.java`: unchecked exception thrown on append ID collision with divergent payload.
- Decommissioning:
  - `themis/pom.xml`: remove `<module>themis-audit</module>`.
  - `themis/themis-audit/`: delete entire directory and contents.
- External consumer migration:
  - `paradeigma/paradeigma-test/pom.xml`: update test dependency from `themis-audit` to `kleio-core`.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/ThemisDefenceInDepthAcceptanceTest.java`: import and instantiate `InMemoryAuditService`, remove unused `ThemisAuditEvent` import.
- Architecture guardrails:
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: update audit package/class constants, migrate forbidden dependency rules to `HarmoniaAuditEvent`, assert `AuditService` has no mutation methods, add rules asserting no `ThemisAuditEvent` exists and `themis-api` does not depend on Kleio.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java`: update Calliope and Themis API layering rules to forbid `net.fhirfactory.harmonia.kleio..`.
- Analogs: Follow `agora/pom.xml` and `petasos/pom.xml` for aggregator structure; follow `petasos-api/pom.xml` for minimal dependency leaf JAR structure; follow `ThemisSecurityContext` for immutability and safe string conventions.

### Nuances & Risks
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

### Contracts
```java
package net.fhirfactory.harmonia.kleio.audit.model;

public record HarmoniaAuditEvent(
    String eventId,
    Instant recordedAt,
    AuditClassification classification,
    AuditAction action,
    AuditOutcome outcome,
    ThemisPrincipal initiatingPrincipal,
    ThemisPrincipal executingPrincipal,
    String securityDomain,
    AuditTarget target,
    AuditAuthorizationEvidence authorizationEvidence,
    Set<ThemisAuthority> authorities,
    Set<ThemisSecurityLabel> securityLabels,
    String correlationId,
    String causationId,
    String operationId,
    AuditSource source,
    Map<String, String> attributes
) implements Serializable
```
```java
package net.fhirfactory.harmonia.kleio.audit.service;

public interface AuditService {
    HarmoniaAuditEvent append(HarmoniaAuditEvent event);
    Optional<HarmoniaAuditEvent> findById(String eventId);
    List<HarmoniaAuditEvent> findByCorrelationId(String correlationId);
    List<HarmoniaAuditEvent> findByPrincipal(String principalId);
    List<HarmoniaAuditEvent> getRecentEvents(int limit);
    HarmoniaAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision);
    default List<HarmoniaAuditEvent> getEventsByCorrelationId(String correlationId) { return findByCorrelationId(correlationId); }
    default List<HarmoniaAuditEvent> getEventsByPrincipal(String principalId) { return findByPrincipal(principalId); }
}
```

# Testing

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

# Assumptions & Open Questions

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

# Delivery Steps

### ✓ Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service
Goal: Create `kleio` aggregator and `kleio-core` JAR module, migrate and rename canonical audit model types, service interfaces, in-memory implementation, and unit test suites with legacy `AUDIT` classification cleanup.
Scope: `kleio/pom.xml`, `kleio/kleio-core/pom.xml`, `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/**`, `kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/**`
Acceptance Criteria:
- [ ] Create `kleio/pom.xml` aggregator declaring child module `kleio-core` with parent `harmonia-parent`.
- [ ] Create `kleio/kleio-core/pom.xml` declaring compile dependencies on `themis-api` and `slf4j-api`, plus test dependencies on `junit-jupiter` and `assertj-core`.
- [ ] Implement `HarmoniaAuditEvent`, `AuditClassification`, `AuditAction`, `AuditOutcome`, `AuditTarget`, `AuditSource`, and `AuditAuthorizationEvidence` under package `net.fhirfactory.harmonia.kleio.audit.model`.
- [ ] Enforce Step 01.1 invariants on `HarmoniaAuditEvent`: mandatory non-null required fields, defensive collection copies, dual principal distinction, `fromDecision(...)` factory, and safe `toString()` suppressing credentials and PHI markers.
- [ ] Remove legacy `CLASSIFICATION_AUDIT` constant; ensure builder string overload strictly parses defined `AuditClassification` enum values and rejects invalid values.
- [ ] Implement `AuditService`, `InMemoryAuditService`, and `AuditIntegrityException` under package `net.fhirfactory.harmonia.kleio.audit.service`, preserving append-only semantics, idempotent replay, ring-buffer capacity, and integrity conflict detection.
- [ ] Migrate and update test suites `HarmoniaAuditEventTest` and `AuditServiceTest` to `kleio-core`, verifying all model invariants, immutability, safe `toString()`, and service behaviors.
Verification: `mvn test -pl kleio/kleio-core` → green

### ✓ Step 2: Decommission Themis-Audit and Migrate External Test Consumers
Goal: Remove `themis-audit` from the repository build and migrate external test consumers (`paradeigma-test`) to `kleio-core`.
Scope: `pom.xml`, `themis/pom.xml`, `themis/themis-audit/` (deletion), `paradeigma/paradeigma-test/pom.xml`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/ThemisDefenceInDepthAcceptanceTest.java`
Acceptance Criteria:
- [ ] Add `<module>kleio</module>` to root `pom.xml` `<modules>` and add managed `kleio-core` artifact under `<dependencyManagement>`.
- [ ] Remove `themis-audit` from root `pom.xml` `<dependencyManagement>` and remove `<module>themis-audit</module>` from `themis/pom.xml`.
- [ ] Delete `themis/themis-audit` directory completely; ensure no legacy alias or forwarding classes remain.
- [ ] Update `paradeigma/paradeigma-test/pom.xml` to replace dependency on `themis-audit` with `kleio-core`.
- [ ] Update `ThemisDefenceInDepthAcceptanceTest.java` imports to use `net.fhirfactory.harmonia.kleio.audit.service.InMemoryAuditService` and remove unused `ThemisAuditEvent` import.
- [ ] Verify that `ThemisDefenceInDepthAcceptanceTest` executes and passes cleanly against `kleio-core`.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ThemisDefenceInDepthAcceptanceTest` → green

### ✓ Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification
Goal: Update and expand architecture tests in `paradeigma-test` to enforce Kleio ownership, forbidden dependencies, and absence of legacy audit classes, then verify all affected modules across the reactor.
Scope: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java`
Acceptance Criteria:
- [ ] Update `SecurityEnforcementArchitectureTest` to assert canonical audit model resides under package `net.fhirfactory.harmonia.kleio.audit.model` and resolve model source files from `kleio/kleio-core`.
- [ ] Migrate forbidden dependency architecture rules (zero HAPI/FHIR, JPA, JMS/Artemis, Jakarta SecurityContext) to `HarmoniaAuditEvent` and Kleio audit model types.
- [ ] Update interface reflection test in `SecurityEnforcementArchitectureTest` to assert `AuditService` contains no mutation methods (`update*`, `delete*`, `replace*`, `patch*`, `clear`).
- [ ] Add architecture rule confirming no canonical `ThemisAuditEvent` or `ThemisAuditService` classes exist in the repository.
- [ ] Add architecture rule confirming `themis-api` and `themis-core` do not depend on `net.fhirfactory.harmonia.kleio..`.
- [ ] Add architecture rule confirming Kleio depends only on `themis-api` and has zero dependencies on `themis-core`.
- [ ] Add architecture rule confirming zero duplicate canonical audit models exist across Themis and Kleio packages.
- [ ] Update `PackageLayeringArchitectureTest` so Calliope and Themis API layering rules explicitly forbid dependencies on `net.fhirfactory.harmonia.kleio..`.
- [ ] Execute reactor test covering `themis-api`, `themis-core`, `kleio-core`, and `paradeigma-test` architecture suites.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest"` → green