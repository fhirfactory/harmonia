---
sessionId: session-260923-212812-vf7x
---

# Requirements

### Goal & Outcome
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

### Scope
- **In Scope**:
  - Enforcing strict FHIR logical-ID validation (`[A-Za-z0-9\-.]{1,64}`) on `HarmoniaAuditEvent.eventId` in `kleio-core` to guarantee direct mapping to `AuditEvent.id` without normalization or an `audit-event-id` extension.
  - Bounding audit principal representation (`AuditPrincipal` value object or constructor normalization) so that arbitrary `ThemisPrincipal.attributes` do not participate in canonical audit equality.
  - Correcting security label semantics: mapping `HarmoniaAuditEvent.securityLabels` strictly to `AuditEvent.entity.securityLabel` (audited target), decoupling persistence-only `AuditEvent.meta.security`.
  - Pruning the extension register: removing `audit-event-id` and `agent-attribute`, leaving exactly 5 top-level extensions (`security-domain`, `correlation-id`, `causation-id`, `operation-id`, `audit-attribute`).
  - Freezing the field-by-field mapping classification for all 17 canonical fields (NATIVE, CODED, EXTENSION, NOT PERSISTED).
  - Formally defining the Step 02.2 round-trip invariant across all 17 fields, accounting for excluded persistence metadata.
  - Comprehensive unit and architecture tests in `kleio-core` and `paradeigma-test`.
- **Out of Scope**:
  - Implementing `HarmoniaAuditEventMapper` or creating `kleio-fhir` (Step 02.2).
  - Modifying `ThemisPrincipal` globally in `themis-api`.
  - Modifying Hestia Mnemosyne or Iris BEFE mutable AuditEvent CRUD surfaces (Step 04).
  - Implementing database persistence or append immutability engines (Step 03).
  - Git operations (commit, push, checkout).

### Done When
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

# Technical Design

### Decisions
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

### Approach & Touches
- **Bounded Principal Value Object**:
  `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/AuditPrincipal.java` (new):
  - Immutable record `(String principalId, PrincipalType principalType, String sourceDomain) implements Serializable`.
  - Static factories `of(id, type, domain)`, `of(id, type)`, `from(ThemisPrincipal)`.
  - Projection helper `toThemisPrincipal()` for external compatibility.
  - Safe `toString()` suppressing sensitive markers.
- **HarmoniaAuditEvent Contract Hardening**:
  `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java`:
  - Declare `private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");`.
  - In compact constructor: validate `eventId` against `EVENT_ID_PATTERN`, throwing `IllegalArgumentException` on failure.
  - Component types for dual principals: `AuditPrincipal initiatingPrincipal, AuditPrincipal executingPrincipal`.
  - Builder support: overload `initiatingPrincipal` and `executingPrincipal` to accept both `AuditPrincipal` and `ThemisPrincipal` (auto-projecting via `AuditPrincipal.from(...)`).
  - `fromDecision(...)`: project request principal and context executing principal via `AuditPrincipal.from(...)`.
  - Accessors: preserve `originatingPrincipal()` returning `AuditPrincipal` (or `initiatingThemisPrincipal()`), alongside existing `principalId()`, `principalType()`, `sourceDomain()`.
- **Unit Test Updates**:
  `kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java`:
  - Add parameterized/focused tests asserting `eventId` boundary conditions: valid (1 char, 64 chars, UUID, dots/hyphens) and invalid (spaces, blank, colons, underscores, slashes, >64 chars).
  - Update `testDualPrincipalDistinction` and `testSafeStringSuppression` to verify `AuditPrincipal` invariants and verify that principal attributes never enter the audit event.
  - Assert `event.initiatingPrincipal()` has no attributes.
- **Service Verification**:
  `kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/service/AuditServiceTest.java`:
  - Ensure all existing tests (idempotent replay, collision integrity, dual-principal query) pass without modification.
- **Architecture Test Verification**:
  `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
  - Confirm `AuditPrincipal` satisfies ArchUnit rules (resides in `kleio.audit.model`, zero HAPI/FHIR, zero JPA, zero JMS).
- **Analogs**: Follow `AuditTarget` for projection pattern from Themis types; follow `ThemisAuthority` for trimmed single-purpose value object.

### Nuances, Risks & Edge Cases
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

### Contracts

#### AuditPrincipal Record
```java
package net.fhirfactory.harmonia.kleio.audit.model;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import java.io.Serializable;

public record AuditPrincipal(
        String principalId,
        PrincipalType principalType,
        String sourceDomain
) implements Serializable {
    public static AuditPrincipal of(String principalId, PrincipalType principalType, String sourceDomain) {
        return new AuditPrincipal(principalId, principalType, sourceDomain);
    }
    public static AuditPrincipal of(String principalId, PrincipalType principalType) {
        return new AuditPrincipal(principalId, principalType, null);
    }
    public static AuditPrincipal from(ThemisPrincipal principal) {
        if (principal == null) return null;
        return new AuditPrincipal(principal.principalId(), principal.principalType(), principal.sourceDomain());
    }
    public ThemisPrincipal toThemisPrincipal() {
        return ThemisPrincipal.of(principalId, principalType, sourceDomain);
    }
}
```

#### Event ID Validation Pattern
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

#### Final Field-by-Field Mapping Matrix

| # | Harmonia Field | Harmonia Semantics | Mandatory | Proposed FHIR R5 Element | Class | Forward Mapping (`Harmonia -> FHIR`) | Reverse Mapping (`FHIR -> Harmonia`) | Cardinality | Governed Terminology / System | Loss Risk |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | `eventId` | Canonical audit event identity | Mandatory | `AuditEvent.id` | `NATIVE` | `setAuditEventId(eventId)` | `getIdPart()` | 1..1 | Direct logical ID `[A-Za-z0-9\-.]{1,64}` | None |
| 2 | `recordedAt` | Recording instant | Mandatory | `AuditEvent.recorded` | `NATIVE` | `new InstantType(Date.from(recordedAt))` | `recorded.getValue().toInstant()` | 1..1 | ISO 8601 UTC Instant | None |
| 3 | `classification` | Architectural domain classification | Mandatory | `AuditEvent.category` | `CODED` | Add `CodeableConcept` with `audit-classification` system | Parse code matching `audit-classification` | 1..* | `http://harmonia.fhirfactory.net/security/audit-classification` | None |
| 4 | `action` | Operational verb | Mandatory | `AuditEvent.action` & `AuditEvent.code` | `CODED` | Set native `action` (`C`,`R`,`U`,`D`,`E`); set `code` with exact `action.name()` | Read `AuditEvent.code` coding | 1..1 | `http://harmonia.fhirfactory.net/security/audit-action` | None |
| 5 | `outcome` | Execution/policy outcome | Mandatory | `AuditEvent.outcome.code` | `CODED` | Set `outcome.code` coding with `outcome.name()` | Read `outcome.code` coding | 1..1 | `http://harmonia.fhirfactory.net/security/audit-outcome` | None |
| 6 | `initiatingPrincipal` | Originating requester identity | Optional | `AuditEvent.agent` (`requestor=true`) | `CODED` | Create agent: `requestor=true`, role `INITIATOR`, `who.identifier.value=principalId`, `type=principalType` | Match agent with `requestor=true` or role `INITIATOR`; parse `AuditPrincipal` | 0..1 | `.../agent-role`, `.../principal-type` | None |
| 7 | `executingPrincipal` | Delegated pipeline executor | Optional | `AuditEvent.agent` (`requestor=false`) | `CODED` | Create agent: `requestor=false`, role `EXECUTOR`, `who.identifier.value=principalId`, `type=principalType` | Match agent with role `EXECUTOR`; parse `AuditPrincipal` | 0..1 | `.../agent-role`, `.../principal-type` | None |
| 8 | `securityDomain` | Tenancy/security domain | Optional | Extension `security-domain` | `EXTENSION` | Add extension `.../security-domain` with `StringType(securityDomain)` | Read extension `.../security-domain` | 0..1 | String / URI | None |
| 9 | `target` | Audited resource/entity reference | Optional | `AuditEvent.entity` | `CODED` | Create entity with role `TARGET`, `what` reference/identifier (`resourceType/resourceId`), `detail` for target `securityDomain` | Match entity with role `TARGET`; extract `resourceType`, `resourceId`, and `securityDomain` detail | 0..1 | `http://harmonia.fhirfactory.net/security/entity-role` | None |
| 10 | `authorizationEvidence` | Themis evaluation evidence | Optional | `AuditEvent.outcome.detail` & `agent.policy` | `CODED` | Map `decisionId`, `decision`, `reason`, `message` to `outcome.detail`; map `policyId` to `agent.policy` | Extract decision parameters from `outcome.detail` and `agent.policy` | 0..1 | `.../themis-decision-reason` | None |
| 11 | `authorities` | Historical authority snapshot | Optional | `AuditEvent.agent.authorization` | `CODED` | Map each authority to `agent.authorization` coding | Extract all codings matching system into `Set<ThemisAuthority>` | 0..* | `http://harmonia.fhirfactory.net/security/authorities` | None |
| 12 | `securityLabels` | Target security/sensitivity labels | Optional | `AuditEvent.entity.securityLabel` | `CODED` | Map each label to `entity.securityLabel` Coding on the TARGET entity | Extract matching codings from TARGET `entity.securityLabel` | 0..* | `http://harmonia.fhirfactory.net/security/labels` | None |
| 13 | `correlationId` | End-to-end workflow boundary ID | Optional | Extension `correlation-id` | `EXTENSION` | Add extension `.../correlation-id` with `StringType` | Read extension `.../correlation-id` | 0..1 | String / UUID | None |
| 14 | `causationId` | Direct preceding work item ID | Optional | Extension `causation-id` | `EXTENSION` | Add extension `.../causation-id` with `StringType` | Read extension `.../causation-id` | 0..1 | String / UUID | None |
| 15 | `operationId` | Specific invocation trace ID | Optional | Extension `operation-id` | `EXTENSION` | Add extension `.../operation-id` with `StringType` | Read extension `.../operation-id` | 0..1 | String / UUID | None |
| 16 | `source` | Subsystem and component origin | Optional | `AuditEvent.source` | `CODED` | Set `source.observer.identifier=component`, `source.type=subsystem` | Extract `subsystem` from `source.type` and `component` from `source.observer` | 0..1 | `http://harmonia.fhirfactory.net/source/subsystem` | None |
| 17 | `attributes` | Small metadata key-values | Optional | Extension `audit-attribute` | `EXTENSION` | For each map entry, add sub-extensions `key` and `value` | Reconstruct `Map<String, String>` from sub-extensions | 0..* | Governed key-value strings | None |

#### Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

#### Revised Final Extension Register
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
*Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

# Testing

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

# Assumptions & Open Questions

- **Significant Assumption — Bounded Principal Value Object**:
  - *Chosen Option*: Introduce `AuditPrincipal(String principalId, PrincipalType principalType, String sourceDomain)` in `net.fhirfactory.harmonia.kleio.audit.model`, replacing direct `ThemisPrincipal` component usage in `HarmoniaAuditEvent`.
  - *Rationale*: Principal attributes in `ThemisPrincipal` represent ephemeral runtime/session state. Audit evidence requires only authenticated identity and domain boundary; historical authorization is captured in `authorities` and `authorizationEvidence`. Eliminating attributes prevents accidental credential/PHI leakage and ensures exact record equality ($event \equiv reconstructed$) without an `agent-attribute` extension.
  - *Viable Alternative*: Constructor normalization (retaining `ThemisPrincipal` signature while stripping attributes to `Map.of()`).
  - *Impact*: Enforces bounded principal invariant at compile time; cleanly aligns with `AuditTarget` and `AuditAuthorizationEvidence`.
- **Significant Assumption — Direct Event ID Mapping**:
  - *Chosen Option*: Map `HarmoniaAuditEvent.eventId` directly to `AuditEvent.id`, backed by `[A-Za-z0-9\-.]{1,64}` domain validation, and remove `audit-event-id` extension.
  - *Rationale*: R5 logical IDs support 1–64 characters including alphanumeric, hyphens, and dots. Enforcing this constraint in the domain guarantees 100% lossless compatibility with native FHIR REST endpoints.
  - *Viable Alternative*: Retain `audit-event-id` extension and auto-generate FHIR IDs.
  - *Impact*: Zero proprietary identity extensions; direct queryability via native FHIR `_id`.
- **Significant Assumption — Security Label Target Re-anchoring**:
  - *Chosen Option*: Map `HarmoniaAuditEvent.securityLabels` strictly to `AuditEvent.entity.securityLabel` (audited entity), treating `AuditEvent.meta.security` as repository protection metadata.
  - *Rationale*: In `HarmoniaAuditEvent.fromDecision(...)`, security labels originate from `ThemisResource.securityLabels()`. They characterize the target resource, not the audit event record itself.
  - *Viable Alternative*: Map to `meta.security`.
  - *Impact*: Prevents target labels from being conflated with audit store access control labels.

# Delivery Steps

### ✓ Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core
Goal: Implement strict FHIR-compliant `eventId` validation and bounded principal representation in `kleio-core`, verifying all invariants and backward compatibility through unit and architecture tests.
Scope: `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/**`, `kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/**`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/**`
Acceptance Criteria:
- [ ] Create `AuditPrincipal` immutable record (`principalId`, `principalType`, `sourceDomain`) with static factories `of` and `from(ThemisPrincipal)` and conversion method `toThemisPrincipal()`.
- [ ] Add `EVENT_ID_PATTERN` (`^[A-Za-z0-9\-.]{1,64}$`) check to `HarmoniaAuditEvent` compact constructor; throw `IllegalArgumentException` on invalid characters, spaces, or length > 64.
- [ ] Update `HarmoniaAuditEvent` record components to use `AuditPrincipal` for `initiatingPrincipal` and `executingPrincipal`.
- [ ] Provide builder overloads accepting `ThemisPrincipal` (auto-projecting to `AuditPrincipal`) and `AuditPrincipal`.
- [ ] Update `HarmoniaAuditEvent.fromDecision(...)` to project request and context principals to `AuditPrincipal`.
- [ ] Preserve compatibility accessors: `originatingPrincipal()`, `principalId()`, `principalType()`, `sourceDomain()`.
- [ ] Add unit tests in `HarmoniaAuditEventTest` covering valid boundary event IDs, invalid characters, length > 64, null/blank IDs, and bounded principal behavior.
- [ ] Verify `HarmoniaAuditEvent.toString()` preserves safe redaction of secrets, tokens, and PHI markers.
- [ ] Verify `InMemoryAuditService` idempotent replay and collision integrity tests pass without regressions.
- [ ] Verify `kleio-core` has zero compile or runtime dependencies on HAPI/FHIR, and reactor architecture tests pass.
Verification: `mvn test -pl kleio/kleio-core && mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` → green

### ✓ Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract
Goal: Document and freeze the authoritative field-by-field semantic mapping table, revised 5-item extension register, terminology requirements, and exact round-trip invariant specification for Step 02.2 implementation.
Scope: Mapping specification deliverables (`HarmoniaAuditEvent <-> FHIR R5 AuditEvent` contract freeze)
Acceptance Criteria:
- [ ] Freeze field-by-field mapping classifications for all 17 canonical fields: NATIVE (2), CODED (10), EXTENSION (5), NOT PERSISTED (3 persistence metadata elements).
- [ ] Formalize event identity: `HarmoniaAuditEvent.eventId ↔ AuditEvent.id` (1..1, NATIVE); confirm removal of `audit-event-id` extension.
- [ ] Formalize target security labels: `HarmoniaAuditEvent.securityLabels ↔ AuditEvent.entity.securityLabel` (CODED); isolate `AuditEvent.meta.security` as persistence-only metadata.
- [ ] Formalize bounded dual-agent mapping: `initiatingPrincipal` (`requestor=true`, role `INITIATOR`) and `executingPrincipal` (`requestor=false`, role `EXECUTOR`) via native `AuditEvent.agent` elements (`who`, `type`, `role`); confirm removal of `agent-attribute` extension.
- [ ] Finalize the minimal extension register to exactly 5 extensions: `security-domain`, `correlation-id`, `causation-id`, `operation-id`, and `audit-attribute`.
- [ ] Formally define the Step 02.2 round-trip invariant: $\text{fromFhir}(\text{toFhir}(event)) \equiv event$ across all 17 canonical fields, specifying that persistence-only metadata (`meta.versionId`, `meta.lastUpdated`, `meta.security`) must not affect equivalence.
- [ ] Document the governance boundary: `kleio-core` remains domain-only; `kleio-fhir` will house the mapper in Step 02.2; Calliope owns cross-domain vocabularies; existing mutable CRUD endpoints in Hestia/Iris remain untouched until Step 04.
Verification: `mvn test -pl kleio/kleio-core` → green