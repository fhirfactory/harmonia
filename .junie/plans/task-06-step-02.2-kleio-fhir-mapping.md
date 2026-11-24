---
sessionId: session-260924-072430-291r
---

# Requirements

### Goal & Outcome
Implement the frozen Task 06 Step 02.1A mapping contract between the canonical Harmonia audit model (`HarmoniaAuditEvent`) and the FHIR R5 persistence representation (`AuditEvent`). Create the dedicated `kleio-fhir` module and implement `HarmoniaAuditEventMapper` providing deterministic, stateless, and lossless bidirectional mapping ensuring that $\text{fromFhir}(\text{toFhir}(event)) \equiv event$ holds for all canonical durable components, while isolating persistence metadata and enforcing fail-fast validation on malformed inputs.

### Scope
- **In Scope**:
  - Scaffolding new Maven module `kleio/kleio-fhir` and wiring into `kleio/pom.xml` and root `pom.xml`.
  - Defining centralized constants (`HarmoniaAuditFhirConstants`) for the 5 frozen extension URLs, governed CodeSystems, and role codes.
  - Implementing `HarmoniaAuditEventMapper` with `toFhir(HarmoniaAuditEvent)` and `fromFhir(AuditEvent)` covering all 17 canonical fields.
  - Preserving sub-millisecond `Instant` precision via string-preserving HAPI R5 `InstantType` handling.
  - Ensuring order-independent reverse mapping for agents, entities, attributes, and codings.
  - Isolating persistence metadata (`AuditEvent.meta.versionId`, `meta.lastUpdated`, `meta.security`) so it never alters the reconstructed event or target security labels.
  - Implementing fail-fast validation (`HarmoniaAuditMappingException`) for missing required fields, corrupt codings, duplicate singletons, and conflicting attribute keys.
  - Focused unit and round-trip test suite in `kleio-fhir` covering 15 frozen verification scenarios.
  - Updating ArchUnit architecture tests in `paradeigma-test` to enforce boundary rules for `kleio-fhir` and preserve `kleio-core` FHIR-free isolation.
- **Out of Scope**:
  - Audit event database persistence or storage adapters (Step 03).
  - Mutable AuditEvent CRUD or REST endpoint convergence (Step 04 / Iris BEFE / Mnemosyne).
  - Modifying `HarmoniaAuditEvent` or domain records in `kleio-core`.
  - Provenance resource mapping or Task 07–10 work.
  - Git operations (commit, push, branch).

### Done When
- `kleio/kleio-fhir` builds cleanly with dependencies only on `kleio-core`, `calliope`, and HAPI FHIR R5 structures.
- `HarmoniaAuditEventMapper` successfully round-trips all 17 canonical fields with $\text{fromFhir}(\text{toFhir}(event)) \equiv event$.
- The 5-extension register (`security-domain`, `correlation-id`, `causation-id`, `operation-id`, `audit-attribute`) is implemented exactly without deprecated `audit-event-id` or `agent-attribute`.
- Persistence metadata (`meta.versionId`, `meta.lastUpdated`, `meta.security`) is strictly ignored during reverse mapping.
- All 15 round-trip test scenarios in `kleio-fhir` pass.
- ArchUnit tests in `paradeigma-test` and unit tests in `kleio-core` pass with zero regressions.

# Technical Design

### Decisions
- **Chose string-preserving `InstantType(Instant.toString())` and `Instant.parse(getValueAsString())` / not `Date.from(Instant)`**: Preserves nanoseconds across round-trips without precision loss. Java `Date` truncates to milliseconds, violating exact record equality.
- **Chose centralized constants class `HarmoniaAuditFhirConstants` in `kleio-fhir` / not modifying Calliope or scattering literals**: Keeps FHIR audit mapping URLs and vocabulary self-contained in `kleio-fhir`, references existing `HarmoniaSecurityCodeSystem` systems (`labels`, `authorities`), and eliminates literal typos.
- **Chose stateless utility/instance `HarmoniaAuditEventMapper` / not CDI/Spring bean**: Guarantees deterministic, thread-safe, network-free, and lifecycle-free execution matching repository converter conventions (e.g. `PragmaFhirConverter`).
- **Chose strict fail-fast validation via `HarmoniaAuditMappingException` (`IllegalArgumentException`) / not silent null/fallback defaults**: Auditing is security evidence; missing or conflicting canonical data in Harmonia-owned structures must be caught immediately rather than manufacturing corrupt domain events.
- **Chose matching agents by role coding (`INITIATOR`, `EXECUTOR`) and requestor flag / not list index position**: FHIR agents may appear in arbitrary order; semantic identification prevents agent swapping during round-trips.
- **Chose target security labels via `AuditEvent.entity.securityLabel` / not `AuditEvent.meta.security`**: Target labels describe the audited entity (`AuditTarget`), whereas `meta.security` describes access controls for the audit record itself.

### Approach & Touches
- **Module Scaffolding (`kleio/kleio-fhir`)**:
  - `kleio/kleio-fhir/pom.xml`: inherits from `net.fhirfactory.harmonia:kleio:1.0.0-SNAPSHOT`. Dependencies: `kleio-core`, `calliope`, `ca.uhn.hapi.fhir:hapi-fhir-base`, `ca.uhn.hapi.fhir:hapi-fhir-structures-r5`, `slf4j-api`, plus test scope `junit-jupiter`, `assertj-core`.
  - `kleio/pom.xml`: add `<module>kleio-fhir</module>` to `<modules>` and `kleio-fhir` to `<dependencyManagement>`.
  - `pom.xml`: add `kleio-fhir` to root `<dependencyManagement>`.
- **Terminology & Constants (`net.fhirfactory.harmonia.kleio.fhir.constants.HarmoniaAuditFhirConstants`)**:
  - Centralizes the 5 frozen extension URLs:
    - `EXTENSION_SECURITY_DOMAIN = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain"`
    - `EXTENSION_CORRELATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id"`
    - `EXTENSION_CAUSATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id"`
    - `EXTENSION_OPERATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id"`
    - `EXTENSION_AUDIT_ATTRIBUTE = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute"`
  - Centralizes CodeSystem URIs:
    - `SYSTEM_AUDIT_CLASSIFICATION = "http://harmonia.fhirfactory.net/security/audit-classification"`
    - `SYSTEM_AUDIT_ACTION = "http://harmonia.fhirfactory.net/security/audit-action"`
    - `SYSTEM_AUDIT_OUTCOME = "http://harmonia.fhirfactory.net/security/audit-outcome"`
    - `SYSTEM_AGENT_ROLE = "http://harmonia.fhirfactory.net/security/agent-role"` (codes `INITIATOR`, `EXECUTOR`)
    - `SYSTEM_PRINCIPAL_TYPE = "http://harmonia.fhirfactory.net/security/principal-type"`
    - `SYSTEM_ENTITY_ROLE = "http://harmonia.fhirfactory.net/security/entity-role"` (code `TARGET`)
    - `SYSTEM_SOURCE_SUBSYSTEM = "http://harmonia.fhirfactory.net/source/subsystem"`
    - `SYSTEM_THEMIS_DECISION = "http://harmonia.fhirfactory.net/security/themis-decision"`
    - `SYSTEM_THEMIS_DECISION_REASON = "http://harmonia.fhirfactory.net/security/themis-decision-reason"`
    - `SYSTEM_THEMIS_POLICY = "http://harmonia.fhirfactory.net/security/themis-policy"`
    - Reuses `HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM` and `HarmoniaSecurityCodeSystem.AUTHORITY_CODE_SYSTEM`.
- **Mapper Implementation (`net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper`)**:
  - `toFhir(HarmoniaAuditEvent event)`:
    - Sets `AuditEvent.id` directly via `event.eventId()`.
    - Sets `recordedElement` using `new InstantType(event.recordedAt().toString())`.
    - Sets `category` with classification coding.
    - Sets `code` with exact Harmonia action coding; sets generic FHIR `action` (`C`, `R`, `U`, `D`, `E`) as compatibility projection.
    - Sets `outcome.code` with Harmonia outcome coding.
    - Maps `initiatingPrincipal` to agent (`requestor=true`, role `INITIATOR`, `who.identifier` value/system, `type` coding).
    - Maps `executingPrincipal` to agent (`requestor=false`, role `EXECUTOR`, `who.identifier` value/system, `type` coding).
    - Maps `securityDomain` to top-level extension.
    - Maps `target` to entity with role `TARGET`, `what.reference` (`resourceType/resourceId`), and `detail` for target `securityDomain`.
    - Maps `securityLabels` to TARGET `entity.securityLabel` codings.
    - Maps `authorizationEvidence` (`decisionId`, `decision`, `reason`, `message`) to `outcome.detail` and `policyId` to `agent.policy`.
    - Maps `authorities` to `agent.authorization` codings.
    - Maps `source` to `source.observer.identifier` (`component`) and `source.type` (`subsystem`).
    - Maps `correlationId`, `causationId`, `operationId` to respective top-level extensions.
    - Maps `attributes` map entries to `audit-attribute` complex extensions with `key` and `value` sub-extensions.
  - `fromFhir(AuditEvent fhir)`:
    - Extracts `eventId` from `fhir.getIdPart()`. Fails fast if blank.
    - Extracts `recordedAt` via `Instant.parse(fhir.getRecordedElement().getValueAsString())`. Fails fast if missing.
    - Locates classification, action, and outcome codings by system. Fails fast if missing or unrecognized.
    - Iterates agents: matches `INITIATOR` (`requestor=true`) and `EXECUTOR` (`requestor=false`). Fails fast on duplicate initiator or executor agents.
    - Extracts target entity by `TARGET` role coding: reads `what` reference (`resourceType`, `resourceId`), `detail` for `securityDomain`, and `entity.securityLabel` for `securityLabels`.
    - Reconstructs `authorizationEvidence` from `outcome.detail` and `agent.policy`.
    - Collects `authorities` from `agent.authorization` matching `AUTHORITY_CODE_SYSTEM`.
    - Reconstructs `source` from `source.type` and `source.observer`.
    - Reads top-level extensions `security-domain`, `correlation-id`, `causation-id`, `operation-id`. Fails fast on duplicate singleton extensions.
    - Reconstructs `attributes` map from `audit-attribute` sub-extensions. Fails fast on conflicting duplicate keys.
    - Completely ignores `meta.versionId`, `meta.lastUpdated`, and `meta.security`.
- **Architecture Test Updates (`paradeigma/paradeigma-test`)**:
  - `paradeigma/paradeigma-test/pom.xml`: add `kleio-fhir` dependency.
  - `SecurityEnforcementArchitectureTest.java`:
    - Add test asserting `net.fhirfactory.harmonia.kleio.fhir..` depends only on `kleio.audit.model`, `calliope`, HAPI/FHIR R5, and JDK, and never on JPA (`jakarta.persistence..`, `ca.uhn.fhir.jpa..`, `hestia..`) or presentation (`iris..`).
    - Add test asserting `themis` subproject has zero dependencies on `kleio.fhir`.
    - Confirm existing test `kleioAuditModelMustNotDependOnHapiOrFhir` continues to enforce `kleio-core` isolation.

### Nuances, Risks & Edge Cases
- **Instant Precision**: Round-trip tests must assert equality for instants created with `Instant.now()` (containing nanoseconds), proving that nanosecond precision is not truncated by `Date` conversions.
- **Persistence Metadata Immunity**: A dedicated test must construct an `AuditEvent`, add arbitrary tags to `meta.security`, set `meta.versionId = "42"` and `meta.lastUpdated = new Date()`, and assert that `fromFhir()` reconstructs the identical `HarmoniaAuditEvent` without polluting `securityLabels`.
- **Agent Order Independence**: A test must swap agent list positions (`[executor, initiator]`) and assert that `fromFhir()` maps `initiatingPrincipal` and `executingPrincipal` correctly without relying on index 0/1.
- **Target Entity Order Independence**: If foreign entities are present alongside the `TARGET` entity, the mapper must selectively bind the target entity via the `TARGET` role coding.
- **No Deprecated Extensions**: Assert that neither `audit-event-id` nor `agent-attribute` extension exists on generated `AuditEvent`.

### Contracts

#### HarmoniaAuditFhirConstants
```java
package net.fhirfactory.harmonia.kleio.fhir.constants;

public final class HarmoniaAuditFhirConstants {
    private HarmoniaAuditFhirConstants() {}

    // 5 Frozen Extensions
    public static final String EXTENSION_SECURITY_DOMAIN = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain";
    public static final String EXTENSION_CORRELATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id";
    public static final String EXTENSION_CAUSATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id";
    public static final String EXTENSION_OPERATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id";
    public static final String EXTENSION_AUDIT_ATTRIBUTE = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute";

    // Sub-extension keys for audit-attribute
    public static final String SUB_EXTENSION_KEY = "key";
    public static final String SUB_EXTENSION_VALUE = "value";

    // Governed CodeSystems
    public static final String SYSTEM_AUDIT_CLASSIFICATION = "http://harmonia.fhirfactory.net/security/audit-classification";
    public static final String SYSTEM_AUDIT_ACTION = "http://harmonia.fhirfactory.net/security/audit-action";
    public static final String SYSTEM_AUDIT_OUTCOME = "http://harmonia.fhirfactory.net/security/audit-outcome";
    public static final String SYSTEM_AGENT_ROLE = "http://harmonia.fhirfactory.net/security/agent-role";
    public static final String SYSTEM_PRINCIPAL_TYPE = "http://harmonia.fhirfactory.net/security/principal-type";
    public static final String SYSTEM_ENTITY_ROLE = "http://harmonia.fhirfactory.net/security/entity-role";
    public static final String SYSTEM_SOURCE_SUBSYSTEM = "http://harmonia.fhirfactory.net/source/subsystem";
    public static final String SYSTEM_THEMIS_DECISION = "http://harmonia.fhirfactory.net/security/themis-decision";
    public static final String SYSTEM_THEMIS_DECISION_REASON = "http://harmonia.fhirfactory.net/security/themis-decision-reason";
    public static final String SYSTEM_THEMIS_POLICY = "http://harmonia.fhirfactory.net/security/themis-policy";
    public static final String SYSTEM_ENTITY_DETAIL = "http://harmonia.fhirfactory.net/security/entity-detail";

    // Governed Codes
    public static final String CODE_AGENT_ROLE_INITIATOR = "INITIATOR";
    public static final String CODE_AGENT_ROLE_EXECUTOR = "EXECUTOR";
    public static final String CODE_ENTITY_ROLE_TARGET = "TARGET";
    public static final String CODE_DETAIL_SECURITY_DOMAIN = "security-domain";
}
```

#### HarmoniaAuditEventMapper Interface / Methods
```java
package net.fhirfactory.harmonia.kleio.fhir.mapper;

import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import org.hl7.fhir.r5.model.AuditEvent;

public class HarmoniaAuditEventMapper {
    public AuditEvent toFhir(HarmoniaAuditEvent event) { ... }
    public HarmoniaAuditEvent fromFhir(AuditEvent auditEvent) { ... }
}
```

# Testing

- Scenario 1 (Minimal Event Round-Trip): Minimal event with only mandatory fields (`eventId`, `recordedAt`, `classification`, `action`, `outcome`) round-trips with exact equality.
- Scenario 2 (Fully Populated Event Round-Trip): Event with all 17 fields populated round-trips with exact equality $\text{fromFhir}(\text{toFhir}(event)) \equiv event$.
- Scenario 3 (Agent Distinction & Permutations): Initiator-only, executor-only, and dual-principal events round-trip correctly; swapping FHIR agent array ordering (`[executor, initiator]`) produces equal `HarmoniaAuditEvent`.
- Scenario 4 (Target Labels & Persistence Metadata Immunity): `securityLabels` round-trip through `entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` or modifying `meta.versionId` / `meta.lastUpdated` has zero effect on reconstructed event.
- Scenario 5 (Order Independence): Authorities, target security labels, entity detail, and attribute extensions in randomized order reconstruct identical canonical sets/maps.
- Scenario 6 (Instant Nanosecond Precision): High-precision `Instant` with nanoseconds round-trips without truncation.
- Scenario 7 (Deprecated Extension Omission): Generated `AuditEvent` never contains `audit-event-id` or `agent-attribute` extensions.
- Scenario 8 (Fail-Fast Validation): Malformed FHIR input (missing ID, missing timestamp, duplicate initiator agents, duplicate singleton extensions, conflicting attribute keys) throws `HarmoniaAuditMappingException`.
- Regression Targets: `kleio/kleio-core` unit tests (`HarmoniaAuditEventTest`, `AuditServiceTest`) and `paradeigma/paradeigma-test` architecture suite (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

# Assumptions & Open Questions

- **Significant Assumption — Nanosecond String Handling via `InstantType`**:
  - *Chosen Option*: Construct `InstantType` with `recordedAt.toString()` and parse reverse with `Instant.parse(recordedElement.getValueAsString())`.
  - *Rationale*: HAPI R5 `InstantType` parses and formats ISO-8601 strings preserving nanosecond precision, whereas `Date.from(Instant)` truncates to milliseconds and fails exact object equality.
  - *Viable Alternative*: Truncate canonical `HarmoniaAuditEvent.recordedAt` to milliseconds at constructor boundary.
  - *Impact*: Preserves full JDK `Instant` fidelity without modifying domain records in `kleio-core`.
- **Significant Assumption — Centralized Vocabulary in `kleio-fhir`**:
  - *Chosen Option*: Centralize audit extension URLs and CodeSystems in `HarmoniaAuditFhirConstants` inside `kleio-fhir`, referencing existing `HarmoniaSecurityCodeSystem` systems for `labels` and `authorities`.
  - *Rationale*: Confines mapping constants to the new boundary, respects Calliope unidirectional layering (Calliope cannot depend on Kleio), and avoids unnecessary cross-module churn.
  - *Viable Alternative*: Add audit URLs to `calliope`.
  - *Impact*: Module remains self-contained; constants can be promoted to `calliope` if needed by downstream modules in later steps.

# Delivery Steps

### ✓ Step 1: Scaffold kleio-fhir Module and Terminology Constants
Goal: Create the `kleio-fhir` module, configure Maven reactor dependencies, and implement centralized terminology and exception types.
Scope: `kleio/kleio-fhir/pom.xml`, `kleio/pom.xml`, `pom.xml`, `kleio/kleio-fhir/src/main/java/net/fhirfactory/harmonia/kleio/fhir/constants/HarmoniaAuditFhirConstants.java`, `kleio/kleio-fhir/src/main/java/net/fhirfactory/harmonia/kleio/fhir/exception/HarmoniaAuditMappingException.java`
Acceptance Criteria:
- [ ] Create `kleio/kleio-fhir/pom.xml` inheriting from `kleio`, depending on `kleio-core`, `calliope`, HAPI FHIR R5 (`hapi-fhir-base`, `hapi-fhir-structures-r5`), `slf4j-api`, `junit-jupiter`, and `assertj-core`.
- [ ] Register `kleio-fhir` in `kleio/pom.xml` `<modules>` and dependency management blocks of `kleio/pom.xml` and root `pom.xml`.
- [ ] Create `HarmoniaAuditFhirConstants` defining the 5 frozen extension URLs (`security-domain`, `correlation-id`, `causation-id`, `operation-id`, `audit-attribute`), CodeSystems (`audit-classification`, `audit-action`, `audit-outcome`, `agent-role`, `principal-type`, `entity-role`, `source/subsystem`, `themis-decision`, `themis-decision-reason`, `themis-policy`), and role codes (`INITIATOR`, `EXECUTOR`, `TARGET`).
- [ ] Re-export or reference existing `HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM` and `HarmoniaSecurityCodeSystem.AUTHORITY_CODE_SYSTEM`.
- [ ] Create `HarmoniaAuditMappingException` extending `IllegalArgumentException`.
- [ ] Verify clean reactor build and compilation of `kleio-fhir`.
Verification: `mvn compile -pl kleio/kleio-fhir -am` → green

### ✓ Step 2: Implement HarmoniaAuditEventMapper and Round-Trip Test Suite
Goal: Implement bidirectional `HarmoniaAuditEventMapper` covering all 17 canonical fields and 5 extensions, nanosecond precision preservation, fail-fast validation, and comprehensive round-trip tests.
Scope: `kleio/kleio-fhir/src/main/java/net/fhirfactory/harmonia/kleio/fhir/mapper/HarmoniaAuditEventMapper.java`, `kleio/kleio-fhir/src/test/java/net/fhirfactory/harmonia/kleio/fhir/mapper/HarmoniaAuditEventMapperTest.java`
Acceptance Criteria:
- [ ] Implement `toFhir(HarmoniaAuditEvent event)` mapping all 17 fields: `eventId ↔ id`, `recordedAt ↔ recorded` (string-preserving `InstantType`), `classification ↔ category`, `action ↔ code & action`, `outcome ↔ outcome.code`, dual agents (`requestor`, role `INITIATOR`/`EXECUTOR`, `principalId`, `principalType`, `sourceDomain`), `securityDomain` extension, `target ↔ entity` (role `TARGET`, `what`, `securityDomain` detail), target `securityLabels ↔ entity.securityLabel`, `authorizationEvidence ↔ outcome.detail & agent.policy`, `authorities ↔ agent.authorization`, `source ↔ source`, trace extensions (`correlation-id`, `causation-id`, `operation-id`), and `attributes ↔ audit-attribute` complex extensions.
- [ ] Verify `toFhir()` emits neither `audit-event-id` nor `agent-attribute`.
- [ ] Implement `fromFhir(AuditEvent fhir)` reconstructing `HarmoniaAuditEvent`, matching agents and entities by role codings and requestor flags independent of list order.
- [ ] Implement persistence metadata isolation: `fromFhir()` ignores `meta.versionId`, `meta.lastUpdated`, and `meta.security` (ensuring `meta.security` never populates `securityLabels`).
- [ ] Implement fail-fast validation in `fromFhir()` throwing `HarmoniaAuditMappingException` on missing ID, missing timestamp, missing/unrecognized codings, duplicate initiator/executor agents, duplicate singleton extensions, or conflicting attribute keys.
- [ ] Add unit tests in `HarmoniaAuditEventMapperTest` covering all 15 scenarios: minimal event, fully populated event, initiator only, executor only, reversed agent list order, target labels through `entity.securityLabel`, persistence metadata immunity, set/coding order independence, attribute order independence, duplicate extension failures, duplicate agent role failures, eventId preservation, nanosecond Instant precision, and absence of deprecated extensions.
Verification: `mvn test -pl kleio/kleio-fhir -am` → green

### ✓ Step 3: Enforce Architecture Guardrails and Run Full Layering Verification
Goal: Update ArchUnit architecture tests to assert `kleio-fhir` dependency boundaries, confirm `kleio-core` remains strictly FHIR-free, and verify zero regressions across the codebase.
Scope: `paradeigma/paradeigma-test/pom.xml`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java`
Acceptance Criteria:
- [ ] Add `kleio-fhir` dependency to `paradeigma/paradeigma-test/pom.xml`.
- [ ] In `SecurityEnforcementArchitectureTest`, add ArchUnit rule verifying `net.fhirfactory.harmonia.kleio.fhir..` depends only on `kleio.audit.model`, `calliope`, HAPI/FHIR R5, and JDK, and never on JPA/persistence (`jakarta.persistence..`, `ca.uhn.fhir.jpa..`, `hestia..`) or presentation (`iris..`).
- [ ] In `SecurityEnforcementArchitectureTest`, add ArchUnit rule verifying `themis` subproject has zero dependencies on `kleio-fhir`.
- [ ] Verify existing ArchUnit test `kleioAuditModelMustNotDependOnHapiOrFhir` continues to pass, ensuring `kleio-core` remains 100% FHIR-free.
- [ ] Verify `PackageLayeringArchitectureTest` succeeds with the new module wired into the reactor.
- [ ] Run full test suites for `kleio-core`, `kleio-fhir`, and architecture test suite in `paradeigma-test`.
Verification: `mvn test -pl kleio/kleio-core,kleio/kleio-fhir -am && mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green