---
sessionId: session-260923-185851-5gu3
---

# Requirements

### Goal & Outcome
Evolve `ThemisAuditEvent` into Harmonia's canonical, immutable, typed audit data object and align `ThemisAuditService` and `InMemoryThemisAuditService` with append-only repository semantics. When complete, Harmonia components will record and query strongly-typed audit evidence with stable identity, typed classification, actions, outcomes, dual-principal tracking, and safe string representations, with zero dependencies on FHIR, persistence, or messaging frameworks.

### Scope
- **In Scope**:
  - Evolving `ThemisAuditEvent` in `themis-audit` with mandatory `eventId`, mandatory `recordedAt`, typed classification, action, outcome, initiating/executing principals, target projection, optional authorization evidence, immutable authority snapshot, provenance triad (correlation/causation/operation), source metadata, bounded attributes, and safe `toString()`.
  - Creating supporting immutable value objects (`ThemisAuditClassification`, `ThemisAuditAction`, `ThemisAuditOutcome`, `ThemisAuditTarget`, `ThemisAuditSource`, `ThemisAuditAuthorizationEvidence`).
  - Implementing `ThemisAuditEvent.fromDecision(...)` factory preserving trusted context without fabricating synthetic identities or missing provenance.
  - Aligning `ThemisAuditService` to append-only semantics (`append`, `findById`, `findByCorrelationId`, `findByPrincipal`, `getRecentEvents`, convenience `recordDecision`; zero mutation methods).
  - Updating `InMemoryThemisAuditService` with idempotent append on identical events and `ThemisAuditIntegrityException` on ID collision with divergent content.
  - Adding unit and architecture tests for immutability, safe string representations, append-only service semantics, and boundary guardrails against forbidden dependencies.
- **Out of Scope**:
  - FHIR R5 `AuditEvent` mapping and converters (Task 06 Step 02).
  - Database schema changes, triggers, JPA entities, or durable persistence implementations.
  - REST gateway endpoints (`AuditEventResource`), UI components (`AuditEventView.vue`), or provider modifications.
  - Broad platform-wide audit emission across other subsystems.
  - Tasks 07–10 persistence and caching mechanisms.

### Done When
- `ThemisAuditEvent` and all nested value objects are immutable records enforcing mandatory `eventId` and `recordedAt`, typed classification/action/outcome, and defensive copying of all collections and attributes.
- `ThemisAuditEvent.toString()` produces a bounded, sanitized representation that suppresses raw authority codes, attribute contents, PHI markers (`PATIENT-PHI-MARKER-92831`), and secret tokens (`TOKEN-SECRET-MARKER-81742`).
- `ThemisAuditService` exposes append-only repository methods with zero mutation methods (`update`, `delete`, `replace`, `clear`).
- `InMemoryThemisAuditService` correctly appends new events, handles identical replays idempotently without duplicate log growth, and rejects conflicting same-ID events with `ThemisAuditIntegrityException`.
- Architecture guardrail tests confirm zero dependencies on FHIR/HAPI, JPA, JMS/Artemis, or Jakarta SecurityContext across all audit model types.

# Technical Design

### Decisions
- Chose dedicated `ThemisAuditAction` enum / not reusing/expanding `ThemisAction` — keeps general subsystem audit vocabulary distinct from authorization request intents and cleanly supports `AUTHORIZE` without polluting request models.
- Chose append-only `ThemisAuditService` with `recordDecision` convenience delegating to `fromDecision` + `append` / not removing `recordDecision` — maintains compatibility with existing authorization evaluators and tests while enforcing append-only semantics.
- Chose `ThemisAuditIntegrityException` / not generic `IllegalStateException` — provides an explicit, typed, and easily assertable failure signal for audit event tampering or conflicting ID reuse.
- Chose keeping canonical audit models in `themis-audit` under `net.fhirfactory.harmonia.themis.audit.model` / not moving to `themis-api` — preserves existing package and module boundaries without unnecessary dependency tree churn.

### Approach & Touches
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditEvent.java`: evolve record definition with compact constructor enforcing non-null required fields, defensive copying, safe `toString()`, fluent builder, and `fromDecision(...)` static factory.
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditClassification.java`: new enum for broad audit categories (`SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`).
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditAction.java`: new enum for audited actions (`CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`) with mapping method `fromThemisAction(ThemisAction)`.
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditOutcome.java`: new enum for typed outcome (`SUCCESS`, `FAILURE`, `DENIED`).
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditTarget.java`: new immutable record capturing `resourceType`, `resourceId`, and `securityDomain` without clinical payload.
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditSource.java`: new immutable record capturing `subsystem` and `component`.
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditAuthorizationEvidence.java`: new immutable record capturing evaluated decision evidence (`decisionId`, `decision`, `reason`, `policyId`, `message`).
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditService.java`: evolve interface to `append`, `findById`, `findByCorrelationId`, `findByPrincipal`, `getRecentEvents`, and convenience `recordDecision`; drop `recordEvent` and remove `clear()` from interface.
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditIntegrityException.java`: unchecked exception thrown on append integrity conflict.
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/InMemoryThemisAuditService.java`: update to implement new append contract, idempotency check, integrity conflict detection, and query methods; retain test-only `clear()`.
- `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditEventTest.java`: comprehensive unit tests for model invariants, immutability, safe `toString()`, and factory behavior.
- `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditServiceTest.java`: updated test suite covering append, idempotency, integrity violations, and queries.
- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: add architecture assertions for forbidden dependencies on `themis-audit` model classes.
- Analogs: Follow `ThemisSecurityContext` and `ThemisPrincipal` in `themis-api` for safe `toString()` formatting and defensive collection copying; use `HarmoniaSecurityConstants` for domain vocabulary.

### Nuances & Risks
- **No identity or provenance fabrication**: when `ThemisSecurityContext` lacks `executingPrincipal`, `causationId`, or `operationId`, they must remain null/absent; do not manufacture "unknown", "service:unknown", or random UUIDs.
- **Safe string suppression**: `ThemisAuditEvent.toString()` must emit collection counts only (`authoritiesCount=N`, `attributeCount=N`) and exclude raw authority codes, attribute contents, and sensitive text.
- **Semantic equality on append**: `InMemoryThemisAuditService` must verify full semantic equality before treating an existing `eventId` as an idempotent replay; any divergent field triggers `ThemisAuditIntegrityException`.
- **Ring-buffer capacity trimming**: trimming applies exclusively to newly inserted events; idempotent duplicate appends must not cause premature eviction of older records.

### Contracts
```java
public record ThemisAuditEvent(
    String eventId,
    Instant recordedAt,
    ThemisAuditClassification classification,
    ThemisAuditAction action,
    ThemisAuditOutcome outcome,
    ThemisPrincipal initiatingPrincipal,
    ThemisPrincipal executingPrincipal,
    String securityDomain,
    ThemisAuditTarget target,
    ThemisAuditAuthorizationEvidence authorizationEvidence,
    Set<ThemisAuthority> authorities,
    Set<ThemisSecurityLabel> securityLabels,
    String correlationId,
    String causationId,
    String operationId,
    ThemisAuditSource source,
    Map<String, String> attributes
) implements Serializable
```
```java
public interface ThemisAuditService {
    ThemisAuditEvent append(ThemisAuditEvent event);
    Optional<ThemisAuditEvent> findById(String eventId);
    List<ThemisAuditEvent> findByCorrelationId(String correlationId);
    List<ThemisAuditEvent> findByPrincipal(String principalId);
    List<ThemisAuditEvent> getRecentEvents(int limit);
    ThemisAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision);
}
```

# Testing

- Must-hold: `ThemisAuditEvent` enforces non-null `eventId`, `recordedAt`, `classification`, `action`, and `outcome`, with defensive copies of collections and maps.
- Must-hold: `ThemisAuditEvent.fromDecision(...)` maps initiating principal, target, decision evidence, security domain, and correlation ID without fabricating missing executing principal, causation ID, or operation ID.
- Safe logging: `ThemisAuditEvent.toString()` suppresses raw authority codes and attribute values, never leaking synthetic markers `PATIENT-PHI-MARKER-92831` or `TOKEN-SECRET-MARKER-81742`.
- Idempotency & integrity: `InMemoryThemisAuditService.append(...)` appends new events, returns existing event on identical replay without duplicate log entries, and throws `ThemisAuditIntegrityException` when an existing `eventId` is re-appended with modified content.
- Query behavior: `findById`, `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` accurately filter and return recorded events from `InMemoryThemisAuditService`.
- Regression: Existing authorization decision auditing in `ThemisAuditServiceTest` and `ThemisDefenceInDepthAcceptanceTest` continues to compile and pass cleanly.
- Architecture guardrails: ArchUnit / reflection tests in `SecurityEnforcementArchitectureTest` confirm `ThemisAuditEvent` and all audit model types have zero dependencies on FHIR/HAPI, JPA, JMS/Artemis, or Jakarta SecurityContext.

# Assumptions & Open Questions

- **Action vocabulary separation**: Chose a dedicated `ThemisAuditAction` enum over modifying `ThemisAction`. Rationale: `ThemisAction` models requested operations on resources (e.g. READ, CREATE), whereas audit events need to record operations like `AUTHORIZE` that are never requested by principals. Alternative: Add `AUTHORIZE` to `ThemisAction` and share it across both authorization and audit models. Impact: Keeps authorization requests clean and uncoupled from audit representation.
- **Interface mutation cleanup**: Chose to remove `clear()` from `ThemisAuditService` interface and retain it only on `InMemoryThemisAuditService`. Rationale: Production audit repositories are append-only; `clear()` is purely a test harness reset mechanism. Alternative: Keep `clear()` on the canonical interface. Impact: Public contract strictly reflects immutability invariants.
- **Integrity violation exception**: Chose `ThemisAuditIntegrityException` extending `IllegalStateException`. Rationale: Explicit type distinguishes audit tampering / ID collision from generic argument validation failures. Alternative: Throw standard `IllegalStateException`. Impact: Clear, typed error handling in service callers.

# Delivery Steps

### ✓ Step 1: Canonical Audit Value Objects and Event Contract
Goal: Implement immutable typed audit model records, enums, builder, safe string representation, and `fromDecision(...)` factory in `themis-audit`.
Scope: `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/*`, `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditEventTest.java`
Acceptance Criteria:
- [ ] Create `ThemisAuditClassification` enum with categories `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
- [ ] Create `ThemisAuditAction` enum with actions `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE` and conversion mapping from `ThemisAction`.
- [ ] Create `ThemisAuditOutcome` enum with states `SUCCESS`, `FAILURE`, `DENIED`.
- [ ] Create immutable value objects `ThemisAuditTarget`, `ThemisAuditSource`, and `ThemisAuditAuthorizationEvidence`.
- [ ] Evolve `ThemisAuditEvent` as an immutable record with compact constructor enforcing non-null required fields, defensive copying for collections and attributes, convenience accessors for backwards compatibility, and fluent builder.
- [ ] Implement safe `toString()` on `ThemisAuditEvent` and nested value objects suppressing authority sets, attribute values, and sensitive markers.
- [ ] Implement `ThemisAuditEvent.fromDecision(...)` factory preserving trusted request/decision context without fabricating missing identities or identifiers.
- [ ] Add `ThemisAuditEventTest` verifying immutability, defensive copies, mandatory fields, dual principal distinction, safe string suppression of `PATIENT-PHI-MARKER-92831` and `TOKEN-SECRET-MARKER-81742`, and factory behavior.
Verification: `mvn test -pl themis/themis-audit -Dtest=ThemisAuditEventTest` → green

### ✓ Step 2: Append-Only Service Contract and In-Memory Implementation
Goal: Evolve `ThemisAuditService` and `InMemoryThemisAuditService` to enforce append-only semantics, idempotent replay, and collision integrity failure.
Scope: `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/*`, `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditServiceTest.java`
Acceptance Criteria:
- [ ] Update `ThemisAuditService` to define `append(ThemisAuditEvent)`, `findById(String)`, `findByCorrelationId(String)`, `findByPrincipal(String)`, `getRecentEvents(int)`, and convenience `recordDecision(ThemisAuthorizationRequest, ThemisAuthorizationDecision)`.
- [ ] Remove `recordEvent` and `clear()` from canonical `ThemisAuditService` interface to prohibit mutation semantics.
- [ ] Introduce `ThemisAuditIntegrityException` in `net.fhirfactory.harmonia.themis.audit.service`.
- [ ] Update `InMemoryThemisAuditService` to append new events, return existing event on identical replay without duplicate entries, and throw `ThemisAuditIntegrityException` when an existing `eventId` is re-appended with modified content.
- [ ] Implement `findById`, `findByCorrelationId`, `findByPrincipal`, and `getRecentEvents` in `InMemoryThemisAuditService`, retaining capacity trimming on genuinely new appends.
- [ ] Retain `clear()` only on `InMemoryThemisAuditService` for test resets.
- [ ] Update `ThemisAuditServiceTest` to cover `append`, idempotent replay, integrity violation throwing, query methods, ring-buffer capacity limit, and `recordDecision` compatibility.
Verification: `mvn test -pl themis/themis-audit` → green

### ✓ Step 3: Architecture Guardrails and System Integrity Validation
Goal: Enforce architectural dependency boundaries for the canonical audit model and verify entire Themis test suite and architecture suite pass.
Scope: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`
Acceptance Criteria:
- [ ] Add architecture test in `SecurityEnforcementArchitectureTest` verifying `ThemisAuditEvent` and all types in `net.fhirfactory.harmonia.themis.audit.model` have zero dependencies on HAPI/FHIR (`ca.uhn.fhir..`, `org.hl7.fhir..`).
- [ ] Add architecture test verifying zero dependencies on JPA (`jakarta.persistence..`, `javax.persistence..`).
- [ ] Add architecture test verifying zero dependencies on JMS/Artemis (`jakarta.jms..`, `javax.jms..`, `org.apache.activemq..`).
- [ ] Add architecture test verifying zero dependencies on Jakarta SecurityContext (`jakarta.security.enterprise..`, `jakarta.ws.rs.core.SecurityContext`).
- [ ] Add architecture test asserting `ThemisAuditService` contains no mutation methods (`update*`, `delete*`, `replace*`, `patch*`, `clear`).
- [ ] Verify that all existing tests in `themis-api`, `themis-core`, `themis-audit`, and `paradeigma-test` execute and pass.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest=SecurityEnforcementArchitectureTest -Dsurefire.failIfNoSpecifiedTests=false` → green