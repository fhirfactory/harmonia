Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect only the current Task 06 Step 01.1 audit surface and map the exact implementation/dependency footprint needed for Step 01.1A. Determine:
1. exact module dependencies of the current themis-audit module;
2. which current audit model/service types depend on Themis API types and which are pure canonical types;
3. all direct references to `ThemisAuditEvent`;
4. all direct references to `ThemisAuditService`;
5. whether any production code outside the current audit module consumes the Step 01.1 implementation;
6. the smallest coherent Maven/module change required to establish `kleio` ownership without introducing cycles;
7. whether any relevant architecture tests already cover these boundaries.

Also note likely file paths and line numbers for the main related code so they do not need to be rediscovered later.

**Constraints**  
Read-only exploration only. Do not modify files. Focus tightly on the audit surface described in the issue; do not perform broad repository-wide exploration. Report any unexpected dependency-cycle risk explicitly.



Exploration is Ready

**Executive summary**

Read-only, targeted exploration completed for the existing Task 06 Step 01.1 audit surface. The implementation is completely concentrated in `themis/themis-audit`; its only production compile dependency is `themis-api` (plus SLF4J for the in-memory implementation), and no production code outside that module currently consumes the audit classes. The only external consumers are Paradeigma test/architecture code, so the smallest coherent extraction is a new root `kleio` aggregator with one `kleio-core` JAR depending on `themis-api`, followed by removal of `themis-audit` from Themis and migration of the Paradeigma test dependency/imports.

No files were modified and no tests/builds were run, as required by the read-only exploration scope.

**1. Focused exploration findings**

**Current implementation surface**

All current audit production classes are under:

- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/`
- `themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/`

Classes found:

- `ThemisAuditEvent.java` — 634 lines
- `ThemisAuditClassification.java` — 31 lines
- `ThemisAuditAction.java` — 54 lines
- `ThemisAuditOutcome.java` — 46 lines
- `ThemisAuditTarget.java` — 64 lines
- `ThemisAuditSource.java` — 47 lines
- `ThemisAuditAuthorizationEvidence.java` — 74 lines
- `ThemisAuditService.java` — 103 lines
- `InMemoryThemisAuditService.java` — 204 lines
- `ThemisAuditIntegrityException.java` — 66 lines

Current tests are:

- `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditEventTest.java` — 526 lines
- `themis/themis-audit/src/test/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditServiceTest.java` — 378 lines

The implementation already contains more than a minimal model-only surface: the service contract, an in-memory append-only implementation, idempotent replay handling, conflicting-ID integrity detection, ring-buffer capacity, query methods, and `recordDecision(...)` are already present.

**2. Previous ownership structure**

The root POM declares `themis` as a top-level module at `/home/hunterm/Development/Code/harmonia/pom.xml:45-55`.

The Themis aggregator declares three children at:

- `themis/pom.xml:37-41`
    - `themis-api`
    - `themis-core`
    - `themis-audit`

The current audit module is:

- `themis/themis-audit/pom.xml:31-35`
    - artifact: `themis-audit`
    - packaging: `jar`
    - name: `Harmonia :: Themis Audit (Security Decision Auditing)`

Its package ownership is currently:

- `net.fhirfactory.harmonia.themis.audit.model`
- `net.fhirfactory.harmonia.themis.audit.service`

The root dependency-management section also manages `themis-audit` at `/home/hunterm/Development/Code/harmonia/pom.xml:122-126`.

**3. Exact current Maven dependencies**

`themis/themis-audit/pom.xml` contains these direct dependencies:

**Compile/runtime dependencies**

- `net.fhirfactory.harmonia:themis-api` at `themis/themis-audit/pom.xml:37-43`
- `org.slf4j:slf4j-api` at `themis/themis-audit/pom.xml:45-49`

**Test dependencies**

- `org.junit.jupiter:junit-jupiter` at `themis/themis-audit/pom.xml:51-56`
- `org.assertj:assertj-core` at `themis/themis-audit/pom.xml:57-61`

There are no direct dependencies on:

- `themis-core`
- Calliope
- HAPI/FHIR
- JPA/Hibernate
- JMS/Artemis
- HTTP/Jakarta Security APIs
- persistence implementations

The module inherits the Java 21 and plugin configuration from the Themis/root parents. `themis-api` itself declares SLF4J and Jackson annotations, but those are transitive context rather than direct `themis-audit` declarations.

The Paradeigma architecture/test module currently has a direct test-module dependency on `themis-audit` at:

- `paradeigma/paradeigma-test/pom.xml:133-137`

No other POM reference to the `themis-audit` artifact was found outside the root dependency-management entry and that Paradeigma test dependency.

**4. Themis API dependency classification**

**Types that genuinely depend on Themis API types**

| Current type | Evidence | Themis API types used |
|---|---|---|
| `ThemisAuditEvent` | imports `20-28`; record fields `50-67`; decision factory `194-280` | `PrincipalType`, `ThemisAction`, `ThemisAuthority`, `ThemisAuthorizationDecision`, `ThemisAuthorizationRequest`, `ThemisDecision`, `ThemisDecisionReason`, `ThemisPrincipal`, `ThemisSecurityLabel` |
| `ThemisAuditAction` | import `20`; conversion method `34-51` | `ThemisAction` |
| `ThemisAuditOutcome` | import `20`; conversion method `30-43` | `ThemisDecision` |
| `ThemisAuditTarget` | import `20`; `fromResource(...)` `48-53` | `ThemisResource` |
| `ThemisAuditAuthorizationEvidence` | imports `20-22`; `fromDecision(...)` `50-60` | `ThemisAuthorizationDecision`, `ThemisDecision`, `ThemisDecisionReason` |
| `ThemisAuditService` | imports `20-22`; methods `40-81` | `ThemisAuthorizationRequest`, `ThemisAuthorizationDecision`, and the audit event type |
| `InMemoryThemisAuditService` | imports `20-24`; implementation `43-177` | `ThemisAuthorizationRequest`, `ThemisAuthorizationDecision`, `ThemisDecision`, audit event/outcome types |

The model’s Themis dependency is therefore a dependency on stable Themis API security vocabulary, not on `themis-core`. This matches the intended safe direction `kleio-core -> themis-api`.

Notable authorization adapter behavior is in `ThemisAuditEvent.fromDecision(...)`:

- overloads at `ThemisAuditEvent.java:194-195` and `207-210`;
- records the evaluated timestamp or falls back to `Instant.now()` at `212`;
- maps the request action and decision to audit action/outcome at `213-217`;
- preserves the initiating principal and separately captures the executing principal at `219-225`;
- derives security domain from target, context, or principal source domain at `227-234`;
- maps target and authorization evidence at `236-240`;
- combines request/context authorities at `242-248`;
- preserves target security labels, correlation ID, and causation ID at `250-258`;
- leaves `operationId` null at `259`;
- emits source `THEMIS/AUTHORIZATION_ENGINE` at `261`;
- creates the event at `263-281`.

**Pure canonical/supporting types**

- `ThemisAuditClassification` is a pure enum with no imports beyond the package declaration; `themis-audit/.../ThemisAuditClassification.java:18-30`.
- `ThemisAuditSource` only imports `java.io.Serializable`; `.../ThemisAuditSource.java:18-28`.
- `ThemisAuditIntegrityException` only uses `java.lang` types implicitly and has no external dependency; `.../ThemisAuditIntegrityException.java:18-65`.

`ThemisAuditEvent` itself is not strictly independent of Themis API because its canonical fields intentionally retain `ThemisPrincipal`, `ThemisAuthority`, `ThemisSecurityLabel`, and authorization evidence. It is independent of FHIR, persistence, messaging, HTTP, and Jakarta security APIs.

**5. Direct references to `ThemisAuditEvent`**

The exhaustive direct-reference footprint is limited to the following files:

**Production code in the current module**

- `themis/themis-audit/src/main/java/.../audit/model/ThemisAuditEvent.java`
    - declaration at `50`
    - `CLASSIFICATION_AUDIT` at `70`
    - decision factories at `187-207`
    - construction at `263-281`
    - safe `toString()` at `284-304`
    - builder return/construction at `581-630`
- `themis/themis-audit/src/main/java/.../audit/service/ThemisAuditService.java`
    - import at `22`
    - append/query/decision method signatures at `40`, `48`, `56`, `64`, `72`, `81`, `89`, `99`
- `themis/themis-audit/src/main/java/.../audit/service/InMemoryThemisAuditService.java`
    - import at `23`
    - event storage fields at `49-50`
    - append/query/decision methods at `65`, `107`, `120`, `135`, `152`, `171-176`
    - logging helper at `192`

**Current audit tests**

- `themis/themis-audit/src/test/java/.../audit/model/ThemisAuditEventTest.java`
    - package-local uses throughout the 526-line model test
    - mandatory constructor checks begin at `61`
    - serialization test begins at `431`
    - compatibility builder test begins at `473`
- `themis/themis-audit/src/test/java/.../audit/service/ThemisAuditServiceTest.java`
    - import at `33`
    - event creation and assertions begin at `76`, `90`, `97`, `122`, `142`, `164`, `192`, etc.

**External test/architecture code**

- `paradeigma/paradeigma-test/src/test/java/.../ThemisDefenceInDepthAcceptanceTest.java:54` — import only; no further direct `ThemisAuditEvent` use was found in that class.
- `paradeigma/paradeigma-test/src/test/java/.../arch/SecurityEnforcementArchitectureTest.java`
    - import at `33`
    - class list at `468-475`
    - model architecture checks at `478-579`
    - safe-string test at `605-643`

No production source outside `themis/themis-audit` directly references `ThemisAuditEvent`.

**6. Direct references to `ThemisAuditService`**

The exhaustive direct-reference footprint is:

**Production code**

- `themis/themis-audit/src/main/java/.../audit/service/ThemisAuditService.java:31` — interface declaration.
- `themis/themis-audit/src/main/java/.../audit/service/InMemoryThemisAuditService.java:40-43` — Javadoc and implementation.

**Architecture test**

- `paradeigma/paradeigma-test/src/test/java/.../arch/SecurityEnforcementArchitectureTest.java:37` — import.
- `:74` — interface existence assertion.
- `:582-601` — append-only/no-mutation-method architecture check.

The service test uses `InMemoryThemisAuditService` directly and does not separately import or reference `ThemisAuditService` by name.

No production caller outside `themis-audit` was found.

**7. Production consumers outside the current audit module**

None were found.

The only external consumer is test code:

- `paradeigma/paradeigma-test` depends on `themis-audit`.
- `ThemisDefenceInDepthAcceptanceTest` imports and instantiates `InMemoryThemisAuditService` at `:55` and `:91`, `:105`.
- `SecurityEnforcementArchitectureTest` imports the model/service classes and performs architecture checks.

This is a test/verification dependency, not a production dependency. No `src/main` code in Calliope, Themis API/core, Hestia, Petasos, Energeia, Pylai, Iris, Agora, or other production modules was found to consume the Step 01.1 audit implementation.

**8. Legacy `AUDIT` classification compatibility finding**

There are three distinct uses that must not be conflated:

1. `ThemisAuditEvent.CLASSIFICATION_AUDIT` exists at `ThemisAuditEvent.java:70` but has no reference anywhere in the repository.
2. The builder’s generic string overload at `ThemisAuditEvent.java:383-393` calls `ThemisAuditClassification.valueOf(...)` and defaults any invalid value to `SECURITY` in the catch block. It does not explicitly test for `AUDIT`.
3. `ThemisAuditEventTest.java:476` deliberately calls `.classification("AUDIT")`, and `:497` asserts the resulting classification is `SECURITY`.

Therefore:

- There is no production consumer of `CLASSIFICATION_AUDIT`.
- There is no production consumer of an explicit `AUDIT -> SECURITY` compatibility branch.
- There is one test consumer of the generic invalid-string fallback, intentionally documenting the old behavior.
- Other `AUDIT` strings found in Calliope/Themis core are security labels/domains such as `AuditEvent` access policy, not audit-event classification and should not be removed as part of this extraction.

A future implementation step should make an explicit decision about updating/removing the compatibility test and the unused constant rather than silently preserving the old naming.

**9. Smallest coherent Maven/module change**

The smallest coherent ownership correction is:

1. Add a top-level `kleio` module to the root POM module list near the other top-level subsystems.
2. Create only:
    - `kleio/pom.xml` as an aggregator;
    - `kleio/kleio-core/pom.xml` as a JAR.
3. Move/rename the current audit model and service implementation into `kleio-core`, using packages such as:
    - `net.fhirfactory.harmonia.kleio.audit.model`
    - `net.fhirfactory.harmonia.kleio.audit.service`
4. Give `kleio-core` only these direct production dependencies initially:
    - `net.fhirfactory.harmonia:themis-api`;
    - `org.slf4j:slf4j-api`.
5. Carry the existing JUnit/AssertJ dependencies into `kleio-core` test scope.
6. Remove `themis-audit` from `themis/pom.xml:37-41`.
7. Remove the root dependency-management entry for `themis-audit` at `pom.xml:122-126`; add managed `kleio-core` only if reactor consumers need it.
8. Change the Paradeigma test module’s dependency at `paradeigma/paradeigma-test/pom.xml:133-137` from `themis-audit` to `kleio-core`, and migrate its imports/asserted package/source paths.
9. Remove the old `themis/themis-audit` module rather than retaining a forwarding/alias module, because no production compatibility consumer exists.

No `kleio-api`, `kleio-model`, `kleio-audit`, persistence, provenance, or reporting module is justified by the current dependency surface.

**10. Dependency-cycle analysis**

No unexpected cycle risk was found.

Safe intended direction:

```text
kleio-core -> themis-api
```

Current evidence supporting that direction:

- `themis-api/pom.xml` has no dependency on `themis-audit` or Kleio.
- No Themis API source references `net.fhirfactory.harmonia.kleio`.
- `themis-core` does not declare a dependency on `themis-audit`.
- The current audit module already depends only on `themis-api` and SLF4J.
- The root/parent POMs use aggregator relationships; the Themis parent is not a required compile dependency of Kleio.

The main future cycle hazard would be adding `kleio-core` to `themis-api` or `themis-core` merely so Themis can directly own/emit the audit service. The existing `fromDecision(...)` factory shows this is unnecessary: Kleio can consume `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` from `themis-api`, while Themis remains a producer conceptually without depending on Kleio at API/core compile time.

**11. Existing architecture-test coverage**

Relevant existing tests are all in:

`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`

**`SecurityEnforcementArchitectureTest.java`**

Currently covers:

- audit model package/class list at `466-475`;
- no HAPI/FHIR dependencies at `478-501`;
- no JPA dependencies at `503-525`;
- no JMS/Artemis dependencies at `528-552`;
- no Jakarta SecurityContext dependencies at `555-579`;
- service interface append-only/no mutation methods at `582-601`;
- safe `toString()` and suppression of authority/token/PHI markers at `604-643`;
- hard-coded old source path and package assertions in helper methods at `645-685`.

The safe-string test explicitly uses:

- `TOKEN-SECRET-MARKER-81742` at `629`;
- `PATIENT-PHI-MARKER-92831` at `630`;

and asserts they do not appear in the event string at `639-642`.

This test will require package/path/import migration during extraction because it currently imports `net.fhirfactory.harmonia.themis.audit...` and resolves `themis/themis-audit/...` source files.

**`PackageLayeringArchitectureTest.java`**

Current relevant rules:

- Calliope/model packages must not depend on `net.fhirfactory.harmonia.themis.audit..` at `48-66`.
- Themis API must not depend on `net.fhirfactory.harmonia.themis.audit..` at `71-92`.

This provides partial protection against the reverse dependency, but it does not establish Kleio ownership and does not assert that Kleio may depend only on Themis API.

**Missing ownership guardrails**

No current architecture test checks:

- existence of a `kleio` subsystem;
- canonical audit classes under Kleio;
- absence of `ThemisAuditEvent`;
- absence of `themis-audit` ownership;
- no duplicate model in Themis and Kleio;
- Kleio-to-Themis-API-only dependency direction;
- `themis-api` independence from Kleio.

**12. Main paths and line references for the next implementation step**

**Maven**

- `/home/hunterm/Development/Code/harmonia/pom.xml:45-55` — root modules.
- `/home/hunterm/Development/Code/harmonia/pom.xml:102-126` — dependency management, including `themis-audit`.
- `/home/hunterm/Development/Code/harmonia/themis/pom.xml:37-41` — Themis children.
- `/home/hunterm/Development/Code/harmonia/themis/themis-audit/pom.xml:31-62` — current module/dependencies.
- `/home/hunterm/Development/Code/harmonia/paradeigma/paradeigma-test/pom.xml:122-137` — current Themis/audit test dependencies.

**Canonical event and model**

- `/home/hunterm/Development/Code/harmonia/themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/model/ThemisAuditEvent.java`
    - imports/record: `18-68`
    - unused legacy constant: `70`
    - invariants/defensive copying: `72-85`
    - compatibility accessors: `87-184`
    - authorization decision adapter: `187-281`
    - safe string representation: `284-304`
    - builder: `307-633`
- `.../ThemisAuditClassification.java:18-30`
- `.../ThemisAuditAction.java:18-51`
- `.../ThemisAuditOutcome.java:18-43`
- `.../ThemisAuditTarget.java:18-53`
- `.../ThemisAuditSource.java:18-37`
- `.../ThemisAuditAuthorizationEvidence.java:18-60`

**Service**

- `/home/hunterm/Development/Code/harmonia/themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditService.java:18-101`
- `/home/hunterm/Development/Code/harmonia/themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/InMemoryThemisAuditService.java:18-203`
- `/home/hunterm/Development/Code/harmonia/themis/themis-audit/src/main/java/net/fhirfactory/harmonia/themis/audit/service/ThemisAuditIntegrityException.java:18-65`

**Tests/architecture**

- `themis/themis-audit/src/test/java/.../audit/model/ThemisAuditEventTest.java:18-525`
- `themis/themis-audit/src/test/java/.../audit/service/ThemisAuditServiceTest.java:18-377`
- `paradeigma/paradeigma-test/src/test/java/.../ThemisDefenceInDepthAcceptanceTest.java:43-105`
- `paradeigma/paradeigma-test/src/test/java/.../arch/SecurityEnforcementArchitectureTest.java:24-685`
- `paradeigma/paradeigma-test/src/test/java/.../arch/PackageLayeringArchitectureTest.java:45-92`

**13. Verification**

No Maven commands were run. This was intentional because the assigned slice is explicitly read-only exploration and requests no implementation or test execution.

The repository-provided architecture command that will be relevant after implementation is:

```bash
mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
```

The current targeted audit module test command, before migration, would be:

```bash
mvn test -pl themis/themis-audit -am
```

After extraction, the equivalent target should use `kleio/kleio-core` and the migrated Paradeigma architecture suite.

**14. Compatibility and migration impact**

- There are no production callers requiring aliases or forwarding classes.
- The old `ThemisAuditEvent`, `ThemisAuditService`, and `InMemoryThemisAuditService` names are used by tests and architecture checks only.
- The old `ThemisAuditIntegrityException` name is currently local to the service package and should be evaluated for a Kleio-owned rename during extraction.
- Existing test coverage is substantial and should be moved rather than rewritten: mandatory fields, immutability, mappings, decision factory behavior, serialization, safe `toString()`, append/query behavior, idempotency, integrity conflicts, capacity, and compatibility accessors are all already represented.
- Existing test expectations around `.classification("AUDIT")` are the only discovered compatibility behavior directly relevant to the legacy classification cleanup.

**15. Explicit out-of-scope confirmation**

No implementation work was performed. In particular, this exploration did not perform or begin:

- FHIR `AuditEvent` mapping;
- FHIR `Provenance` or `HarmoniaProvenance` implementation;
- durable persistence or database schema work;
- REST endpoint changes;
- Iris/UI work;
- reporting/export work;
- audit generation throughout Harmonia;
- Themis authorization redesign;
- Tasks 07–10.

**Discoveries for future steps**

- The current `themis-audit` surface already implements append/idempotency/integrity/ring-buffer behavior, so extraction should preserve it rather than add Step 01.2 semantics.
- The only external artifact consumer is `paradeigma-test`; there are no production callers, making removal of `themis-audit` low-risk from a compatibility perspective.
- `SecurityEnforcementArchitectureTest` hard-codes both the old package and `themis/themis-audit` filesystem path at lines `466-483` and `645-650`; these are mandatory migration points.
- `CLASSIFICATION_AUDIT` is unused, but `ThemisAuditEventTest` intentionally exercises `.classification("AUDIT")` and expects `SECURITY`; this is a test-only compatibility consumer requiring an explicit migration decision.



PLAN THE TASK

**Task description**  
Plan the delivery work for this ownership extraction and rename, using the exploration findings already gathered. Emphasize a minimal coherent `kleio/kleio-core` introduction, migration/removal of `themis-audit`, package/type renames, test/architecture guardrail migration, targeted verification sequencing, and explicit preservation of current Step 01.1 semantics without implementing out-of-scope future capabilities.

**Feedback**  
Known from exploration:
- `themis-audit` currently depends only on `themis-api` and `slf4j-api`; no production callers outside the module were found.
- External consumers are limited to `paradeigma-test` architecture/acceptance tests.
- Existing audit surface already includes append/query/idempotency/integrity behaviors; preserve them rather than expanding Step 01.2.
- `SecurityEnforcementArchitectureTest` hard-codes old `themis/themis-audit` package/path and must migrate.
- Legacy `CLASSIFICATION_AUDIT` constant is unused; only a test currently relies on `.classification("AUDIT") -> SECURITY` fallback.
- No dependency-cycle risk was found for `kleio-core -> themis-api`.
- Verification should start narrow and optimized: small-scope Maven test commands first, 60-120s per command to start, parallel/narrow scope where possible, and capture exact commands/results.



Open file

**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

**Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

**Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

**Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



REVIEW THE PLAN

**Task description**  
Review the proposed delivery plan for this extraction/migration task.

**Feedback**  
Focus on whether the plan preserves the mandated sequencing from the issue: targeted Kleio extraction with minimal Maven structure, explicit rename to `HarmoniaAuditEvent`, removal of Themis ownership artifacts when possible, architecture guardrails, and verification breadth. Flag if any step is too broad to review/accept safely or if test/architecture migration needs a different sequencing to avoid avoidable rework.



Plan Approved

**Verification**
- **Contract fidelity confirmed**: The current `ThemisAuditEvent` record (`themis/themis-audit/.../model/ThemisAuditEvent.java:50-68`) matches the plan's proposed `HarmoniaAuditEvent` contract field-for-field (17 fields, `implements Serializable`, same mandatory-field validation and defensive copying). The `ThemisAuditService` interface (`.../service/ThemisAuditService.java:31-102`) matches the plan's `AuditService` contract exactly, including the `getEventsByCorrelationId`/`getEventsByPrincipal` default aliases — so the rename preserves Step 01.1 semantics.
- **Legacy cleanup grounded**: `CLASSIFICATION_AUDIT` exists at `ThemisAuditEvent.java:70`; the plan's removal decision is consistent with the exploration finding that it is unused in production.
- **paradeigma-test migration points confirmed real**: `ThemisDefenceInDepthAcceptanceTest.java:54` imports `ThemisAuditEvent` and it is genuinely unused (grep found no other reference); `InMemoryThemisAuditService` is used at lines 55/91/105. The `auditService` field is instantiated but not wired into the controller (dead field) — migration is a straightforward import/type swap.
- **Architecture test migration points confirmed**: `SecurityEnforcementArchitectureTest` hard-codes package constant `net.fhirfactory.harmonia.themis.audit.model..` (line 466), the class list (468-476), filesystem paths `themis/themis-audit/src/...` (608, 647), the reflection package assertion (669), and the `ThemisAuditService` mutation-method reflection check (584-601). All are accurately identified as migration points in the plan.
- **Maven touch points confirmed**: root `pom.xml` modules list (45-55) and `themis-audit` managed dependency (122-126); `themis/pom.xml` module list (37-41). Plan's Approach & Touches references these correctly.
- **No cycle risk**: `kleio-core -> themis-api` direction is sound; nothing in `themis-api`/`themis-core` references audit or kleio.

**Issues**
1. **Step 1 verification command is broken in the planned sequence.** Step 1 creates `kleio/kleio-core` and verifies with `mvn test -pl kleio/kleio-core`, but registering `<module>kleio</module>` in the root `pom.xml` is deferred to Step 2. A `-pl` selector against a module not yet in the reactor fails ("Could not find the selected project in the reactor"), and without `-am` the `themis-api`/`slf4j-api` upstream dependencies are not built. As written, the Step 1 done-signal cannot execute until Step 2's root-POM edit is done, creating avoidable confusion/rework. This is the sequencing risk the review feedback flagged. Impact is limited (cheap fix) so it is a concern rather than a hard fail. See Suggested alternative.

**Suggested alternative**  
Move the root `pom.xml` `<module>kleio</module>` registration (and the managed `kleio-core` dependency entry) from Step 2 into Step 1, alongside creating the `kleio`/`kleio-core` POMs — foundational reactor wiring belongs with module creation. Then change Step 1's verification to `mvn test -pl kleio/kleio-core -am` so upstream `themis-api`/`slf4j` build correctly. Step 2 then narrows to `themis-audit` removal + `paradeigma-test` consumer migration, keeping each step independently verifiable. Cost: cheap (relocating one POM edit and adding `-am`).

**Notes**
- Step 3's final verification (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest"`) builds all upstream modules but, due to the `-Dtest` filter, does not execute `themis-core` / kleio-core unit tests as part of the "broadest reactor test" the parent task requests. This is acceptable under proportionality (unit suites are covered by Steps 1–2), but a final unfiltered reactor test would more fully satisfy the parent-task VERIFICATION section. Non-blocking.
- The plan correctly scopes out Step 01.2/FHIR/provenance work and preserves the `fromDecision(...)` factory and PHI/credential suppression markers.

**Discoveries for future steps**
- `mvn -pl <path>` requires the module to be registered in the root reactor; add module to root `pom.xml` before using `-pl`, and always pair with `-am` for SNAPSHOT intra-reactor deps (`themis-api`).
- `SecurityEnforcementArchitectureTest` mixes three coupling styles that all need migration: ArchUnit package rules, filesystem `Files.readString` path checks (608/647), and reflection `getPackageName()` assertions (669) — missing any one leaves the suite red.
- `auditService` in `ThemisDefenceInDepthAcceptanceTest` is an unwired/dead field; only its type/import needs migrating, no behavioral rewiring required.



**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

*** Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

**Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

**Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Prioritize a minimal coherent `kleio/kleio-core` introduction with the root reactor wiring included in this step so the new module can be verified immediately. Preserve current Step 01.1 semantics exactly while renaming ownership and packages.

Known from previous steps:
- `themis-audit` currently depends only on `themis-api` and `slf4j-api`; no production callers outside the module were found.
- External consumers are limited to `paradeigma-test` and should not be migrated in this step except as needed for compile isolation of step 1 work.
- Existing audit surface already includes append/query/idempotency/integrity behaviors; preserve rather than expand Step 01.2.
- `CLASSIFICATION_AUDIT` is unused in production; only a test exercises `.classification("AUDIT") -> SECURITY` fallback. This step should remove the vestigial compatibility and update tests accordingly.
- No dependency-cycle risk was found for `kleio-core -> themis-api`.

Execution optimization requirements:
- Use narrow, incremental commands first; start with 60-120s timeouts per command.
- Prefer module-scoped verification rather than broad reactor runs in this step.
- Report exact commands run and any timeout/optimization choices in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Accomplishments**

In Step 1, the new `Kleio` subsystem was established with its core module `kleio/kleio-core`, extracting and canonicalizing the audit domain model and service contracts under Kleio ownership without semantic regressions.

**1. Reactor and Module Configuration**
- Updated root `/home/hunterm/Development/Code/harmonia/pom.xml`:
    - Added `<module>kleio</module>` to `<modules>`.
    - Added managed dependency `kleio-core` under `<dependencyManagement>`.
- Created aggregator POM `/home/hunterm/Development/Code/harmonia/kleio/pom.xml`:
    - Inherits from parent `harmonia-parent`.
    - Declares child module `kleio-core`.
    - Configures dependency management for `kleio-core`.
- Created module POM `/home/hunterm/Development/Code/harmonia/kleio/kleio-core/pom.xml`:
    - Declares direct compile dependencies on `net.fhirfactory.harmonia:themis-api` and `org.slf4j:slf4j-api`.
    - Declares test dependencies on `org.junit.jupiter:junit-jupiter` and `org.assertj:assertj-core`.

**2. Canonical Audit Model (`net.fhirfactory.harmonia.kleio.audit.model`)**
- Implemented `AuditClassification`: enum defining `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
- Implemented `AuditAction`: enum (`CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`) with mapping from `ThemisAction` via `fromThemisAction(ThemisAction)`.
- Implemented `AuditOutcome`: enum (`SUCCESS`, `FAILURE`, `DENIED`) with mapping from `ThemisDecision` via `fromDecision(ThemisDecision)`.
- Implemented `AuditTarget`: immutable target projection record (`resourceType`, `resourceId`, `securityDomain`) with `fromResource(ThemisResource)`.
- Implemented `AuditSource`: immutable source descriptor record (`subsystem`, `component`).
- Implemented `AuditAuthorizationEvidence`: immutable decision evidence record (`decisionId`, `decision`, `reason`, `policyId`, `message`) with `fromDecision(ThemisAuthorizationDecision)` and safe `toString()` suppressing message payloads.
- Implemented `HarmoniaAuditEvent`:
    - Compact constructor enforcing non-null/non-blank required fields (`eventId`, `recordedAt`, `classification`, `action`, `outcome`).
    - Defensive copying of sets and maps (`authorities`, `securityLabels`, `attributes`).
    - Preserved backward-compatible accessors (`originatingPrincipal`, `timestamp`, `decisionId`, `decision`, `reason`, `policyId`, `principalId`, `principalType`, `sourceDomain`, `resourceType`, `resourceId`).
    - Static factory `fromDecision(...)` converting request/decision pairs while preserving true provenance without fabricating missing IDs or identities.
    - Safe `toString()` suppressing credentials, authorities, attribute mappings, and PHI markers.
    - Removed legacy `CLASSIFICATION_AUDIT` constant; updated builder `classification(String)` to strictly parse defined enum values via `AuditClassification.valueOf(classification.trim().toUpperCase())` and reject invalid values with `IllegalArgumentException`.

**3. Audit Service Contracts & In-Memory Implementation (`net.fhirfactory.harmonia.kleio.audit.service`)**
- Implemented `AuditService`: append-only service interface declaring `append`, `findById`, `findByCorrelationId`, `findByPrincipal`, `getRecentEvents`, `recordDecision`, and backward-compatible alias methods.
- Implemented `AuditIntegrityException`: unchecked exception extending `IllegalStateException` thrown on conflicting event IDs with divergent payloads.
- Implemented `InMemoryAuditService`: thread-safe in-memory ring-buffer implementation enforcing:
    - Idempotent replay of identical events without log growth.
    - Conflict detection throwing `AuditIntegrityException` for divergent payloads on matching event ID.
    - Ring buffer capacity trimming upon genuine appends.
    - Read/write locking around storage.

**4. Test Verification**
- Created `/home/hunterm/Development/Code/harmonia/kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java`:
    - Verifies mandatory constructor validation, immutability, safe `toString()`, provenance preservation, decision adapters, serialization, builder roundtrip, and strict string classification parsing (confirming `"AUDIT"` is rejected).
- Created `/home/hunterm/Development/Code/harmonia/kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/service/AuditServiceTest.java`:
    - Verifies ALLOW/DENY decision recording, custom appends, idempotent replay, integrity violation exception throwing, dual principal queries, ring buffer capacity and eviction, storage clearing, and null handling.
- Verification command:
    - `mvn test -pl kleio/kleio-core` executed in ~1.3 seconds with 22 tests run, 0 failures, 0 errors.
    - `mvn test -pl kleio/kleio-core -am` executed in ~1.8 seconds with reactor dependencies compiling and testing cleanly.

---

**Discoveries for future steps**

- **Execution commands & speed**: `mvn test -pl kleio/kleio-core` takes ~1.3 seconds; `mvn test -pl kleio/kleio-core -am` takes ~1.8 seconds.
- **Recommendations**: `ThemisPrincipal` requires `new ThemisPrincipal(...)` if all 4 parameters (`principalId`, `principalType`, `sourceDomain`, `attributes`) are supplied because `ThemisPrincipal.of(...)` overloads only exist for 2 and 3 arguments.
- **Decommissioning context for Step 2**: With `kleio-core` verified and compiling against `themis-api`, Step 2 can safely delete `themis-audit`, update `themis/pom.xml`, update `paradeigma-test/pom.xml`, and update test consumers without compile breaks.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Focus on whether the new `kleio/kleio-core` module faithfully preserves Step 01.1 semantics while correctly renaming ownership/types, and whether the legacy `AUDIT` classification compatibility was removed cleanly without unintended regression. Also verify that the root reactor wiring added in this step is coherent and that the new model/service code remains independent of FHIR/JPA/JMS/Jakarta Security APIs.

Known from execution:
- Step 1 added root `<module>kleio</module>` and managed `kleio-core` so the new module can be tested immediately.
- New tests intentionally changed the old `.classification("AUDIT") -> SECURITY` behavior to strict rejection.
- Executor reported module-scoped tests passing with `mvn test -pl kleio/kleio-core` and `mvn test -pl kleio/kleio-core -am`.



Implementation Approved

**Verification**
- **Aggregator POM** (`kleio/pom.xml`): parent `harmonia-parent`, packaging `pom`, declares child module `kleio-core`, with dependencyManagement entry — correct.
- **Module POM** (`kleio/kleio-core/pom.xml`): parent `kleio`, `jar` packaging, compile deps limited to `themis-api` + `slf4j-api`, test deps `junit-jupiter` + `assertj-core` — exactly as specified.
- **Model classes** all present under `net.fhirfactory.harmonia.kleio.audit.model`: `HarmoniaAuditEvent`, `AuditClassification`, `AuditAction`, `AuditOutcome`, `AuditTarget`, `AuditSource`, `AuditAuthorizationEvidence`. Compared `HarmoniaAuditEvent` line-by-line against original `ThemisAuditEvent` — faithful port with correct type renames; all Step 01.1 invariants preserved (mandatory `eventId`/`recordedAt`/`classification`/`action`/`outcome`, blank-eventId guard, defensive copies of authorities/labels/attributes, dual principal tracking, `fromDecision(...)` factory that does NOT fabricate `executingPrincipal`/`causationId`/`operationId`, safe `toString()` suppressing authorities/attributes/PHI).
- **Legacy AUDIT cleanup**: `CLASSIFICATION_AUDIT` constant removed; `Builder.classification(String)` now uses strict `AuditClassification.valueOf(trim().toUpperCase())` with no `try/catch` fallback to SECURITY. Test explicitly asserts `"AUDIT"`, `"UNKNOWN"`, `"INVALID"`, `""` throw `IllegalArgumentException`.
- **Service classes** under `...audit.service`: `AuditService` (append-only interface, no mutation methods), `InMemoryAuditService` (thread-safe RW-locked ring buffer, idempotent replay, integrity conflict detection, eviction on genuine appends only), `AuditIntegrityException` — faithful ports.
- **Tests**: `HarmoniaAuditEventTest` (12 tests) and `AuditServiceTest` (10 tests) cover mandatory fields, immutability/defensive copies, safe `toString` suppression of `PATIENT-PHI-MARKER-92831` and `TOKEN-SECRET-MARKER-81742`, fromDecision provenance (with and without context), DENIED mapping, action/outcome mappings, serialization, builder roundtrip, strict classification parsing, idempotent replay, integrity violation, dual-principal queries, ring-buffer capacity/eviction, clear, and null handling. Meaningful behavior-level assertions, not over-mocked.
- **Root reactor wiring**: `<module>kleio</module>` added to root `pom.xml` modules and managed `kleio-core` added to dependencyManagement. (`themis-audit` still present — correct, its removal is Step 2 scope.)
- **Isolation**: grep confirmed zero imports of HAPI/FHIR, JPA/Hibernate, JMS/Artemis, Jakarta Security, or PostgreSQL in Kleio main source.
- **Build**: `mvn test -pl kleio/kleio-core -o` → BUILD SUCCESS, `Tests run: 22, Failures: 0, Errors: 0, Skipped: 0` (~sub-second test time).

**Issues**  
None.

**Notes**
- The migrated `HarmoniaAuditEvent` retains a broad set of backward-compatibility builder setters and accessors (e.g., `decisionId`, `principalId`, `resourceType` granular setters; `timestamp()`, `originatingPrincipal()` aliases). These have no external callers now, but preserving them is consistent with the step mandate of "zero semantic regressions from Step 01.1" and does not affect correctness — non-blocking.
- Minor behavioral improvement over original: `classification(String)` now trims whitespace before parsing (original did not). Harmless and covered by the `"  WORKFLOW  "` test case.

**Discoveries for future steps**
- `mvn test -pl kleio/kleio-core -o` runs in ~2s (22 tests); offline `-o` flag works since deps are already resolved. Use 300s timeout ceiling but it completes far faster.
- SLF4J emits "No SLF4J providers found" NOP warnings during test — expected (no slf4j binding on test classpath), not a failure.
- `themis-audit` module and its root dependencyManagement entry are still present after Step 1 — this is correct/expected; Step 2 handles decommissioning. Both old and new audit copies currently coexist and compile.



**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

**✓ Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

**Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

**Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

**✓ Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

*** Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

**Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Decommission `themis-audit` completely now that `kleio-core` exists and passed its unit tests. Update only the external consumers actually using the old module/types, and avoid introducing compatibility shims or forwarding classes.

Known from previous steps:
- `kleio/kleio-core` is now registered in the root reactor and managed in root dependencyManagement.
- Step 1 preserved Step 01.1 semantics in Kleio and intentionally removed `.classification("AUDIT") -> SECURITY` fallback.
- The only external consumers identified during exploration were in `paradeigma-test`: its POM dependency, `ThemisDefenceInDepthAcceptanceTest`, and architecture tests to be migrated in step 3.
- `themis-audit` still coexists after step 1; this step should remove the module and filesystem copy entirely.

Execution optimization requirements:
- Use narrow, incremental commands first; start with 60-120s timeouts.
- Prefer targeted compile/test commands for `paradeigma-test` consumer migration before broader runs.
- Report exact commands run and any timeout/optimization choices in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Accomplishments**

In Step 2, `themis-audit` was completely decommissioned from the Harmonia repository build, and its external test consumer (`paradeigma-test`) was migrated to `kleio-core`.

**1. Reactor and Dependency Management Cleanup**
- Root `/home/hunterm/Development/Code/harmonia/pom.xml`:
    - Verified `<module>kleio</module>` is present under `<modules>`.
    - Managed `kleio-core` under `<dependencyManagement>` and removed obsolete `themis-audit` dependency declaration.
- Aggregator `/home/hunterm/Development/Code/harmonia/themis/pom.xml`:
    - Removed `<module>themis-audit</module>` from `<modules>`, leaving only `themis-api` and `themis-core`.

**2. Complete Removal of `themis-audit`**
- Deleted directory `/home/hunterm/Development/Code/harmonia/themis/themis-audit` and all 13 source/POM/test files.
- Ensured no deprecated shims, forwarding classes, or alias artifacts remain in Themis.

**3. Migration of External Consumer (`paradeigma-test`)**
- Updated `/home/hunterm/Development/Code/harmonia/paradeigma/paradeigma-test/pom.xml`:
    - Replaced test dependency `net.fhirfactory.harmonia:themis-audit` with `net.fhirfactory.harmonia:kleio-core`.
    - Configured `<testExclude>**/SecurityEnforcementArchitectureTest.java</testExclude>` in `maven-compiler-plugin` to isolate Step 2 scope and allow compilation of `paradeigma-test` while keeping `SecurityEnforcementArchitectureTest` migration bounded strictly to Step 3.
- Updated `/home/hunterm/Development/Code/harmonia/paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/ThemisDefenceInDepthAcceptanceTest.java`:
    - Removed unused import `ThemisAuditEvent`.
    - Replaced `InMemoryThemisAuditService` import with `net.fhirfactory.harmonia.kleio.audit.service.InMemoryAuditService`.
    - Updated field declaration and instantiation to `InMemoryAuditService`.

**4. Verification**
- Executed `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ThemisDefenceInDepthAcceptanceTest -Dsurefire.failIfNoSpecifiedTests=false`:
    - Reactor built 31/31 modules successfully.
    - Ran `ThemisDefenceInDepthAcceptanceTest` (6 tests run, 0 failures, 0 errors, 0 skipped).
- Executed `mvn test -pl kleio/kleio-core`:
    - Ran 22 tests across `AuditServiceTest` and `HarmoniaAuditEventTest` with 0 failures, 0 errors.

---

**Discoveries for future steps**

- **Execution commands & speed**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ThemisDefenceInDepthAcceptanceTest -Dsurefire.failIfNoSpecifiedTests=false` takes ~6 seconds to compile reactor dependencies and execute the test suite. When running targeted test classes with `-am`, always supply `-Dsurefire.failIfNoSpecifiedTests=false` to prevent surefire failures on upstream modules that lack the targeted test class name.
- **Difficulties & solutions**: `paradeigma-test` has `SecurityEnforcementArchitectureTest.java` in `src/test/java` which directly references old `ThemisAuditEvent` and `themis-audit` types. To prevent compilation failure in Step 2 while preserving strict scope boundaries for Step 3, `paradeigma/paradeigma-test/pom.xml` was configured with a `<testExclude>**/SecurityEnforcementArchitectureTest.java</testExclude>`.
- **Recommendations for Step 3**: When migrating `SecurityEnforcementArchitectureTest.java` and `PackageLayeringArchitectureTest.java` in Step 3, remove the temporary `<testExclude>**/SecurityEnforcementArchitectureTest.java</testExclude>` from `paradeigma/paradeigma-test/pom.xml`. Update file path references in `SecurityEnforcementArchitectureTest` from `themis/themis-audit/src/...` to `kleio/kleio-core/src/...`, update model class references to `HarmoniaAuditEvent`, and assert unidirectional `kleio-core -> themis-api` layering.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Focus especially on whether decommissioning `themis-audit` was complete and clean, whether `paradeigma-test` was migrated with only minimal temporary accommodation, and whether the temporary `SecurityEnforcementArchitectureTest` exclusion in the POM is an acceptable step-local measure rather than a hidden regression. Also verify no compatibility shims or forwarding classes remain.



Implementation Approved

**Verification**
- **Root `pom.xml`**: `<module>kleio</module>` present in `<modules>`; managed `kleio-core` dependency added under `<dependencyManagement>`; no `themis-audit` reference remains (grep for `themis-audit` in all XML returned zero matches).
- **`themis/pom.xml`**: `<modules>` now contains only `themis-api` and `themis-core`; `themis-audit` removed.
- **Directory deletion**: `themis/themis-audit/` no longer exists on the filesystem (`ls` → "No such file or directory"); `git status` shows all 13 files as deleted. Glob hits were only against the git index, not the working tree.
- **`paradeigma-test/pom.xml`**: test dependency migrated from `themis-audit` to `kleio-core`.
- **`ThemisDefenceInDepthAcceptanceTest.java`**: imports `net.fhirfactory.harmonia.kleio.audit.service.InMemoryAuditService` (line 54); field and instantiation use `InMemoryAuditService` (lines 90, 104); no `ThemisAuditEvent` or `InMemoryThemisAuditService` import remains.
- **No shims/forwarding classes**: no `ThemisAuditEvent`/`ThemisAuditService` alias classes remain anywhere; the only residual `net.fhirfactory.harmonia.themis.audit` references are in Step-3-scoped arch tests (`PackageLayeringArchitectureTest` string literals in `resideInAnyPackage`, and the excluded `SecurityEnforcementArchitectureTest`).
- **Verification build**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ThemisDefenceInDepthAcceptanceTest -Dsurefire.failIfNoSpecifiedTests=false` → BUILD SUCCESS; surefire report shows 6 tests run, 0 failures/errors/skipped.
- **testExclude verified effective**: `SecurityEnforcementArchitectureTest.class` is absent from `target/test-classes`, confirming the `<testExclude>` prevents compilation of the file that still imports deleted classes. Since the reactor built successfully, the exclusion is doing its job.

**Issues**  
None blocking.

**Notes**
- The `<testExclude>**/SecurityEnforcementArchitectureTest.java</testExclude>` in `paradeigma-test/pom.xml` is an acceptable step-local measure: it is explicitly bounded to Step 3, which will migrate that test to `HarmoniaAuditEvent`/kleio paths and remove the exclusion. It is a temporary, documented coverage gap, not a hidden regression — the test file is the only remaining consumer of deleted `themis.audit` types.
- `PackageLayeringArchitectureTest` still contains string literals `"net.fhirfactory.harmonia.themis.audit.."` (lines 54, 79). These are `resideInAnyPackage` forbidden-target strings (no imports of deleted classes), so they compile and pass; updating them to `kleio` is Step 3 scope.
- Minor pre-existing observation (not introduced by this step): `auditService` in the acceptance test is instantiated but not exercised by any assertion — this was already the case before migration.

**Discoveries for future steps**
- Verification command `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ThemisDefenceInDepthAcceptanceTest -Dsurefire.failIfNoSpecifiedTests=false` completes in ~1–2 min (reactor build dominates); 600s timeout is comfortable.
- Step 3 must remove the `<testExcludes>` block from `paradeigma-test/pom.xml`, migrate `SecurityEnforcementArchitectureTest` imports/paths/class-list to `net.fhirfactory.harmonia.kleio.audit.*` and `HarmoniaAuditEvent`, and update the two `themis.audit..` string literals in `PackageLayeringArchitectureTest` to `kleio..`.
- Stale surefire `.txt` reports from prior runs persist in `target/surefire-reports/` (e.g., a SecurityEnforcementArchitectureTest report showing 19 tests); verify against `target/test-classes` presence rather than trusting old reports.



**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

**✓ Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

**✓ Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

**Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

**✓ Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

**✓ Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

*** Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



IMPLEMENTATION

**Task description**  
Fully complete step 3.

**Feedback**  
Update and strengthen the architecture guardrails around the new Kleio ownership, then run the broadest practical affected-reactor verification. Remove any temporary Step 2 accommodations once the migrated architecture tests compile and pass.

Known from previous steps:
- `kleio-core` now owns the canonical audit model/service and its unit tests pass.
- `themis-audit` has been removed entirely from the repository and build.
- `paradeigma-test/pom.xml` was temporarily given `<testExclude>**/SecurityEnforcementArchitectureTest.java</testExclude>` to let Step 2 proceed; this must be removed in this step.
- Remaining old ownership references are expected in `SecurityEnforcementArchitectureTest` and string literals in `PackageLayeringArchitectureTest`.
- `SecurityEnforcementArchitectureTest` previously mixed package rules, filesystem source-path checks, and reflection package assertions; all must be migrated.

Execution optimization requirements:
- Start with narrow architecture-test commands and 60-120s timeouts, then widen only after they pass.
- Prefer module/reactor-scoped verification commands that directly cover `paradeigma-test`, `kleio-core`, `themis-api`, and `themis-core`.
- Report exact commands run, elapsed times, and any timeout/parallelization choices in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Completed Work for Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**

**1. Removal of Temporary Accommodations**
- Updated `/home/hunterm/Development/Code/harmonia/paradeigma/paradeigma-test/pom.xml`:
    - Removed `<testExclude>**/SecurityEnforcementArchitectureTest.java</testExclude>` and its enclosing `maven-compiler-plugin` configuration block so `SecurityEnforcementArchitectureTest` compiles and executes in standard reactor builds.

**2. Architecture Guardrails Update in `SecurityEnforcementArchitectureTest.java`**
- Migrated all imports and references from `net.fhirfactory.harmonia.themis.audit.*` to `net.fhirfactory.harmonia.kleio.audit.*` (`HarmoniaAuditEvent`, `AuditClassification`, `AuditAction`, `AuditOutcome`, `AuditTarget`, `AuditSource`, `AuditAuthorizationEvidence`, `AuditService`).
- Updated `themisSecurityContractsAndEngineDefined()` to focus on Themis contracts (`ThemisService`, `ThemisPolicy`, `DeterministicPolicyEvaluator`), and added `kleioAuditServiceContractDefined()` asserting `AuditService` is an interface.
- Added `canonicalAuditModelResidesUnderKleioPackage()` verifying all canonical model classes reside in `net.fhirfactory.harmonia.kleio.audit.model` and `AuditService` resides in `net.fhirfactory.harmonia.kleio.audit.service`.
- Migrated forbidden dependency tests (`kleioAuditModelMustNotDependOnHapiOrFhir`, `kleioAuditModelMustNotDependOnJpa`, `kleioAuditModelMustNotDependOnJmsOrArtemis`, `kleioAuditModelMustNotDependOnJakartaSecurityContext`) to check `HarmoniaAuditEvent` and Kleio audit model types with ArchUnit rules, source inspection of `kleio/kleio-core/src/main/java/...`, and Java reflection checks.
- Updated `auditServiceMustNotContainMutationMethods()` checking that `AuditService` interface declares no mutation methods starting with `update`, `delete`, `replace`, or `patch`, and does not declare `clear()`.
- Updated `harmoniaAuditEventMustUseSafeStringRepresentation()` to verify safe logging behavior, explicit `toString()` override, and suppression of attributes, authorities, tokens, and PHI markers.
- Added new architecture guardrail tests:
    - `noCanonicalThemisAuditClassesExist()`: Asserts no classes with simple name `ThemisAuditEvent`, `ThemisAuditService`, `InMemoryThemisAuditService`, or `ThemisAuditIntegrityException` exist in `net.fhirfactory.harmonia`, confirms `ClassNotFoundException` via reflection, and asserts the filesystem directory `themis/themis-audit` does not exist.
    - `themisMustNotDependOnKleio()`: Asserts `themis.api` and `themis.core` packages do not depend on `net.fhirfactory.harmonia.kleio..`.
    - `kleioMustOnlyDependOnThemisApiAndNotThemisCore()`: Asserts `net.fhirfactory.harmonia.kleio..` has zero dependencies on `themis.core` and zero dependencies on other Harmonia subprojects outside `themis.api`.
    - `zeroDuplicateCanonicalAuditModelsAcrossThemisAndKleio()`: Asserts no classes reside in `net.fhirfactory.harmonia.themis.audit..`, canonical audit model and service types reside exclusively in `net.fhirfactory.harmonia.kleio.audit..`, and no canonical audit classes exist under `net.fhirfactory.harmonia.themis..`.

**3. Subsystem Layering Rules Update in `PackageLayeringArchitectureTest.java`**
- Updated `calliopeMustNotDependOnHigherLayers()` and `themisApiMustNotDependOnHigherLayers()` in `/home/hunterm/Development/Code/harmonia/paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java`:
    - Replaced legacy string literal `"net.fhirfactory.harmonia.themis.audit.."` with `"net.fhirfactory.harmonia.kleio.."`, ensuring Calliope canonical models and Themis API cannot depend on the Kleio subsystem.

**4. Build and Subsystem Verification**
- Executed targeted `SecurityEnforcementArchitectureTest`:
  `mvn test -pl paradeigma/paradeigma-test -am -Dtest=SecurityEnforcementArchitectureTest -Dsurefire.failIfNoSpecifiedTests=false`
  Result: 25 tests run, 0 failures, 0 errors, 0 skipped.
- Executed targeted `PackageLayeringArchitectureTest`:
  `mvn test -pl paradeigma/paradeigma-test -am -Dtest=PackageLayeringArchitectureTest -Dsurefire.failIfNoSpecifiedTests=false`
  Result: 4 tests run, 0 failures, 0 errors, 0 skipped.
- Executed all architecture tests in `paradeigma-test`:
  `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`
  Result: 49 tests run across 7 architecture suites, 0 failures, 0 errors, 0 skipped.
- Executed acceptance and architecture verification:
  `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest" -Dsurefire.failIfNoSpecifiedTests=false`
  Result: 55 tests run, 0 failures, 0 errors, 0 skipped (Total time: 13.257s).
- Executed affected module suites:
  `mvn test -pl kleio/kleio-core`: 22 tests run, 0 failures, 0 errors (Total time: 1.264s).
  `mvn test -pl themis/themis-api,themis/themis-core`: 105 tests run, 0 failures, 0 errors (Total time: 3.421s).
- Executed comprehensive multi-module verification across reactor:
  `mvn test -pl themis/themis-api,themis/themis-core,kleio/kleio-core,paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest,*Test" -Dsurefire.failIfNoSpecifiedTests=false`
  Result: BUILD SUCCESS across all 31 reactor modules in 1m 13s.

---

**Discoveries for future steps**

- **Execution commands & speed**:
    - `mvn test -pl kleio/kleio-core` executes all unit tests in ~1.3s.
    - `mvn test -pl themis/themis-api,themis/themis-core` executes all Themis unit and policy evaluator tests in ~3.4s.
    - `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest" -Dsurefire.failIfNoSpecifiedTests=false` verifies all 7 architecture suites and acceptance tests in ~13.3s.
    - When passing `-am` with `-Dtest=<pattern>`, always specify `-Dsurefire.failIfNoSpecifiedTests=false`; otherwise, upstream reactor modules that lack test classes matching the pattern fail during surefire test execution.
- **Recommendations**:
    - In `SecurityEnforcementArchitectureTest`, caching the repository-wide `JavaClasses` via `getHarmoniaClasses()` keeps all ArchUnit evaluations under 3.5s total execution time.
    - Direct compile dependencies of `kleio-core` should strictly remain limited to `themis-api` and `slf4j-api`. Any future additions (e.g. durable persistence or FHIR conversion) should be created in separate modules (`kleio-fhir`, `kleio-persistence`) to preserve the minimal dependency leaf invariants on `kleio-core`.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 3.

**Feedback**  
Focus on whether the architecture guardrails now truly enforce Kleio ownership and Themis independence without overreaching, whether the temporary Step 2 exclusion was fully removed, and whether the final verification coverage is sufficient and accurately reported. Pay extra attention to the new 'kleio depends only on themis-api and not themis-core' assertions and any risk of false positives/negatives in the filesystem/reflection checks.



Checking recent IDE activity

Implementation Approved

**Verification**
- Reviewed both modified files: `SecurityEnforcementArchitectureTest.java` (876 lines) and `PackageLayeringArchitectureTest.java` (129 lines).
- Confirmed all Step 3 acceptance criteria are met:
    - `canonicalAuditModelResidesUnderKleioPackage()` asserts all 7 model classes reside in `net.fhirfactory.harmonia.kleio.audit.model` and `AuditService` in `...kleio.audit.service`; source-file checks resolve `kleio/kleio-core/src/main/java/...` (lines 628, 799).
    - Forbidden-dependency rules migrated to `HarmoniaAuditEvent`/Kleio model, triple-layered (ArchUnit bytecode rule + source import scan + reflection field/method/ctor scan) for HAPI/FHIR, JPA, JMS/Artemis, Jakarta SecurityContext (lines 498–600).
    - `auditServiceMustNotContainMutationMethods()` iterates real interface methods (append/findById/findByCorrelationId/findByPrincipal/getRecentEvents/recordDecision/aliases) — non-vacuous, forbids `update/delete/replace/patch` prefixes and `clear` (lines 602–622).
    - `noCanonicalThemisAuditClassesExist()` asserts absence via ArchUnit simple-name rule, `Class.forName` → `ClassNotFoundException`, and `themis/themis-audit` directory non-existence (lines 676–699).
    - `themisMustNotDependOnKleio()` and `kleioMustOnlyDependOnThemisApiAndNotThemisCore()` enforce unidirectional boundary (lines 701–748).
    - `zeroDuplicateCanonicalAuditModelsAcrossThemisAndKleio()` enforces exclusivity + no `themis.audit..` package (lines 750–795).
    - `PackageLayeringArchitectureTest` Calliope (line 54) and Themis API (line 79) rules now forbid `net.fhirfactory.harmonia.kleio..`.
- Confirmed the temporary Step 2 `<testExclude>...SecurityEnforcementArchitectureTest...</testExclude>` compiler config was fully removed from `paradeigma-test/pom.xml`; `kleio-core` test dependency present (lines 133–138).
- Ran verification command `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest" -Dsurefire.failIfNoSpecifiedTests=false` → exit code 0. Per-suite results: SecurityEnforcement 25/0/0, PackageLayering 4/0/0, ThemisDefenceInDepth 6/0/0, plus all other arch suites green. `-am` compiles/tests upstream `themis-api`, `themis-core`, `kleio-core`.
- Checked false-positive/negative risks: `getHarmoniaClasses()` imports classes from a classpath that includes `kleio-core` (paradeigma-test depends on it), so Kleio classes are present and dependency rules are not vacuous; package-scoped model imports assert `isNotEmpty()`.

**Issues**  
None.

**Notes**
- Non-blocking: The `kleioMustOnlyDependOnThemisApiAndNotThemisCore` rule only forbids dependencies (does not positively assert a dependency on `themis-api`), but combined with the compile-time module dependency limits and other guardrails this is sufficient — not overreaching.
- The verification relied on `-am` reactor build to cover `themis-api`/`themis-core`/`kleio-core`; their tests run as part of the reactor rather than an explicit separate command, which is consistent with the plan.

**Discoveries for future steps**
- Verification command took ~40s wall; individual arch suites are fast (SecurityEnforcement 1.2s, PackageLayering 1.3s). Always pass `-Dsurefire.failIfNoSpecifiedTests=false` with `-am` + `-Dtest=` or upstream modules without matching tests fail.



**Requirements**

**Goal & Outcome**  
Establish the new `Kleio` subsystem (`kleio/kleio-core`) as the architectural owner of Harmonia audit and provenance evidence. Extract the canonical audit model and append-only service contracts from `themis/themis-audit` into `kleio-core`, rename `ThemisAuditEvent` to `HarmoniaAuditEvent`, decommission `themis-audit`, and update architecture guardrails to lock in the `kleio-core -> themis-api` unidirectional dependency boundary with zero semantic regressions from Step 01.1.

**Scope**
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

**Done When**
- Top-level `kleio/kleio-core` module exists, compiles, and passes unit tests with direct dependencies limited to `themis-api` and `slf4j-api`.
- `HarmoniaAuditEvent` is the sole canonical audit event record; all supporting types are renamed under `net.fhirfactory.harmonia.kleio.audit.model`.
- Legacy `CLASSIFICATION_AUDIT` is removed, and string parsing strictly validates defined `AuditClassification` values.
- `themis/themis-audit` is completely removed without leaving deprecated alias/shim classes.
- `paradeigma-test` compiles and passes against `kleio-core`.
- Architecture guardrails verify canonical audit ownership under Kleio, zero `ThemisAuditEvent` classes, unidirectional `kleio-core -> themis-api` layering, and zero dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext.

**Technical Design**

**Decisions**
- Chose minimal `kleio/kleio-core` module structure / not multi-module `kleio-api` + `kleio-model` + `kleio-core` — avoids speculative Maven layers when model and in-memory service share identical compile dependencies (`themis-api`, `slf4j-api`).
- Chose `AuditService` in `net.fhirfactory.harmonia.kleio.audit.service` / not `HarmoniaAuditService` — package context makes the service purpose unambiguous and aligns with project conventions, paired with `InMemoryAuditService`.
- Chose complete removal of legacy `CLASSIFICATION_AUDIT` and silent `"AUDIT" -> SECURITY` fallback / not keeping deprecated constant — no production callers exist and `AUDIT` describes the mechanism, not the activity classification.
- Chose complete deletion of `themis-audit` / not preserving a forwarding/shim module — no production consumers outside `themis-audit` exist, preventing technical debt and architectural confusion.

**Approach & Touches**
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

**Nuances & Risks**
- **Dependency Direction**: Strict enforcement of `kleio-core -> themis-api`. `themis-api` and `themis-core` must have zero compile-time references to `net.fhirfactory.harmonia.kleio..`.
- **Zero Provenance/Identity Fabrication**: In `HarmoniaAuditEvent.fromDecision(...)`, preserve missing values (`executingPrincipal`, `operationId`, `causationId`) as null/empty when omitted in the request/context; do not inject dummy identifiers.
- **Safe String Protection**: `HarmoniaAuditEvent.toString()` and `AuditAuthorizationEvidence.toString()` must never print sensitive credentials (`TOKEN-SECRET-MARKER-81742`), clinical PHI (`PATIENT-PHI-MARKER-92831`), or raw authority lists.
- **ArchUnit Path Migration**: `SecurityEnforcementArchitectureTest` contains file path checks (`themis/themis-audit/src/...`); these must be updated to `kleio/kleio-core/src/...` to avoid test failures.

**Contracts**
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

**Testing**

- Must-hold: `HarmoniaAuditEvent` enforces mandatory `eventId`, `recordedAt`, `classification`, `action`, `outcome`, and performs defensive copying of all collections and attribute maps.
- Must-hold: `HarmoniaAuditEvent.fromDecision(...)` correctly converts `ThemisAuthorizationRequest` and `ThemisAuthorizationDecision` into a complete audit event without synthesizing missing values.
- Safe logging: `HarmoniaAuditEvent.toString()` suppresses raw authorities, attribute map contents, and sensitive test tokens (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Service immutability: `InMemoryAuditService` appends new events, replays identical events idempotently, and throws `AuditIntegrityException` when an existing `eventId` is re-appended with divergent content.
- Classification parsing: string classification parsing in builder accepts valid names (`"SECURITY"`, `"CLINICAL"`) and throws `IllegalArgumentException` on unrecognized values (including `"AUDIT"`).
- Regression: `ThemisDefenceInDepthAcceptanceTest` passes cleanly using `InMemoryAuditService`.
- Architecture guardrails: ArchUnit tests in `SecurityEnforcementArchitectureTest` and `PackageLayeringArchitectureTest` verify zero forbidden dependencies on HAPI/FHIR, JPA, JMS, or Jakarta SecurityContext, verify canonical model resides under Kleio, verify no `ThemisAuditEvent` exists, and verify `themis-api` does not depend on Kleio.

**Assumptions & Open Questions**

- **Minimal Module Footprint**: Chose `kleio/kleio-core` as the sole initial module in the Kleio subsystem. Rationale: Model types and in-memory service share the exact same runtime dependencies (`themis-api`, `slf4j-api`). Alternative: Separate `kleio-model` and `kleio-core`. Impact: Avoids premature module fragmentation.
- **Service Naming**: Chose `AuditService` over `HarmoniaAuditService` as the interface name. Rationale: The package `net.fhirfactory.harmonia.kleio.audit.service` provides clear namespace disambiguation, and task instructions explicitly prefer `AuditService`. Alternative: `HarmoniaAuditService`. Impact: Cleaner, idiomatic code paired with `InMemoryAuditService`.
- **Legacy Classification Compatibility**: Chose complete removal of `CLASSIFICATION_AUDIT` and string fallback. Rationale: Exploration proved zero production callers exist; preserving `"AUDIT" -> SECURITY` misrepresents audit activity classifications. Alternative: Keep deprecated constant and silent fallback. Impact: Canonical model strictly enforces valid domain classifications.
- **Themis-Audit Removal**: Chose complete deletion of `themis-audit` rather than keeping an empty or forwarding artifact. Rationale: Only `paradeigma-test` referenced `themis-audit`; no production components consume it. Alternative: Retain deprecated forwarding module. Impact: Clean repository architecture without obsolete artifacts.

**Delivery Steps**

**✓ Step 1: Establish Kleio Subsystem and Migrate Canonical Audit Model and Service**  
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

**✓ Step 2: Decommission Themis-Audit and Migrate External Test Consumers**  
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

**✓ Step 3: Establish Architecture Guardrails and Perform Full Subsystem Verification**  
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



**Summary**
- Established new `kleio/kleio-core` ownership for Harmonia audit, renamed the canonical event to `HarmoniaAuditEvent`, and fully extracted/remapped the prior `themis-audit` model/service surface without advancing into Step 01.2 work.
- Removed `themis-audit` cleanly, migrated affected consumers/tests, and added focused architecture guardrails enforcing `kleio-core -> themis-api` with no reverse Themis dependency.

**Changes**
- Added `kleio/pom.xml` and `kleio/kleio-core/pom.xml`; moved/renamed the canonical audit model and service types into `net.fhirfactory.harmonia.kleio.audit.{model,service}`, preserving Step 01.1 semantics including `fromDecision(...)`, immutability, append/idempotency/integrity behavior, and safe `toString()`.
- Removed vestigial legacy audit ownership/compatibility: deleted the entire `themis/themis-audit` module, removed its reactor/dependency-management entries, and dropped the meaningless `"AUDIT" -> SECURITY` classification fallback.
- Migrated tests to Kleio (`HarmoniaAuditEventTest`, `AuditServiceTest`) and updated `paradeigma-test` consumers plus architecture tests to prove Kleio ownership, no remaining canonical Themis audit classes, forbidden dependency isolation, and no `themis-api` dependency on Kleio.

**Verification**
- Passed targeted module tests: `mvn test -pl kleio/kleio-core`, targeted acceptance migration: `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ThemisDefenceInDepthAcceptanceTest -Dsurefire.failIfNoSpecifiedTests=false`, and architecture suites: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.
- Passed broader affected-reactor verification including Themis, Kleio, and Paradeigma coverage: `mvn test -pl themis/themis-api,themis/themis-core,kleio/kleio-core,paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest,ThemisDefenceInDepthAcceptanceTest,*Test" -Dsurefire.failIfNoSpecifiedTests=false`.

**Notes**
- No FHIR `AuditEvent` mapping, provenance implementation, durable persistence, REST/UI/reporting work, or Tasks 07–10 work was performed; Step 01.2 remains for future append-only/persistence-boundary evolution.

