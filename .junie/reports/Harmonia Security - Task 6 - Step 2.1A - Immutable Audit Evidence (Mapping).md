Optional spending limit; leave empty for no limit: 35
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Perform a tightly scoped, read-only exploration for Task 06 Step 02.1A. Focus only on the current Kleio audit model/tests and directly relevant Themis API usage.

Determine:
1. exact current `HarmoniaAuditEvent` eventId validation/build behavior and all tests touching eventId generation/validation/idempotency;
2. exact current use of `ThemisPrincipal.attributes` within Kleio audit model/tests and any repository evidence that these attributes are required as canonical durable audit semantics;
3. whether a minimal audit-specific principal value object or similar bounded representation would be the cleanest local change point inside kleio-core;
4. exact current semantics/tests around `securityLabels` so we can correct the mapping contract to target `AuditEvent.entity.securityLabel` rather than `meta.security`;
5. precise file paths and line ranges that would need modification if Step 02.1A requires code/test changes.

Return concise findings with file paths/line numbers and explicit recommendations about whether a model change in kleio-core appears necessary for eventId validity and principal-attribute durability.

**Constraints**  
Read-only only. Do not modify code. Do not broaden into full repository mapping design again. Focus only on the correction areas above and directly relevant tests/usages.



Load skill: session-history

Exploration is Ready

**Task 06 Step 02.1A — Focused Read-Only Findings**

**Executive findings**

- **Event identity:** A Kleio model change appears necessary before Step 02.2. `eventId` currently rejects only `null` and blank strings; it does not enforce the actual FHIR logical-ID character/length constraints. Because the revised contract requires `HarmoniaAuditEvent.eventId ↔ AuditEvent.id` with no identity extension and no mapper normalization, validation must be strengthened in `HarmoniaAuditEvent` itself.
- **Principal attributes:** No Kleio audit behavior reads or derives semantics from `ThemisPrincipal.attributes`. The only Kleio test use supplies secret-valued attributes to verify safe logging. However, attributes currently remain part of `ThemisPrincipal` record equality and therefore part of `HarmoniaAuditEvent` equality transitively. A mapper that simply omits them would not satisfy exact round-trip equality.
- **Recommended principal boundary:** If the frozen contract intentionally excludes arbitrary principal attributes, the cleanest local change is a Kleio-owned bounded audit-principal value object containing only `principalId`, `principalType`, and `sourceDomain`. Do not change `ThemisPrincipal` globally. Until that model decision is made, retaining `ThemisPrincipal` requires durable preservation of its attributes; silently dropping them is not valid.
- **Security labels:** Current `HarmoniaAuditEvent.securityLabels` are target labels copied from `ThemisResource.securityLabels()` by `fromDecision(...)`. The corrected durable location is `AuditEvent.entity.securityLabel`, not `AuditEvent.meta.security`. `meta.security` is separate persistence/protection metadata for the AuditEvent resource itself and must be excluded from Harmonia event equality/round-trip reconstruction.

**1. Current eventId contract and behavior**

**Production model**

`kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java`

- Record component declaration: **lines 50–67**, with `eventId` at line 51.
- Compact constructor validation: **lines 70–78**.
    - `Objects.requireNonNull(eventId, ...)` at line 71.
    - `eventId.isBlank()` rejection at lines 72–73.
    - No length check, character whitelist, or normalization.
- Builder setter: **lines 361–364**; it stores the supplied string unchanged.
- Builder generation: **line 576**; when no ID is supplied, it generates `UUID.randomUUID().toString()`.
- Explicit `fromDecision(...)` overload: **lines 205–208** checks only non-null arguments, then constructs the canonical record at **lines 261–279**, so invalid nonblank IDs currently reach the permissive constructor.
- `toBuilder()` copies the exact event ID at **lines 309–327**; it does not normalize it.

The generated UUID form is compatible with the inspected repository R5 logical-ID constraint. Explicit IDs such as the current test values (`evt-*`, `custom-evt-*`, `conflict-evt-*`) are also compatible. The current contract nevertheless permits invalid IDs containing characters outside `[A-Za-z0-9.-]` and IDs longer than 64 characters.

**Required Step 02.1A decision**

Strengthen the compact constructor with the actual FHIR logical-ID invariant: exactly 1–64 characters and only ASCII letters, digits, hyphen, or dot. Do not trim or rewrite input. The builder and explicit `fromDecision(...)` paths will then inherit the same rejection through construction, while generated UUIDs remain unchanged.

A suitable validation rule is equivalent to:

```text
[A-Za-z0-9.-]{1,64}
```

This should be treated as a domain invariant, not as mapper-side validation, because `eventId` is also the key used by `AuditService` idempotency.

**Event-ID tests currently present**

`kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java`

- **Lines 55–107:** mandatory-field test. It covers null `eventId` at **61–66** and blank event ID at **68–74**, but not illegal characters, excessive length, or builder/fromDecision invalid IDs.
- **Lines 121–130 and 132–152:** builder-created event with explicit ID and immutability checks.
- **Lines 268–302:** explicit `fromDecision("evt-dec-1", ...)` and preservation assertion at **302**.
- **Lines 429–460:** Java serialization round trip, including explicit `evt-ser-1` and assertion at **456**.
- **Lines 463–518:** builder/toBuilder behavior; original ID `evt-builder-1` at **468**, changed ID `evt-builder-2` at **510**, and assertions at **488** and **514**.

`kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/service/AuditServiceTest.java`

- **Lines 139–159:** direct append and lookup by exact event ID.
- **Lines 161–186:** identical event replay with the same ID; the test appends the same event three times and verifies equality/no log growth.
- **Lines 188–229:** same ID with divergent action; verifies `AuditIntegrityException`, event ID propagation, and preservation of the original record.
- **Lines 303–310:** replay of an existing event does not cause ring-buffer eviction.
- Other explicit IDs are used at **143**, **165**, **193**, **206**, and **343**; all current literals are compatible with the proposed invariant.

`kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/InMemoryAuditService.java`

- **Lines 64–82:** exact `event.eventId()` map key; same ID plus `existing.equals(event)` is idempotent, while same ID plus divergent full record throws `AuditIntegrityException`.
- **Lines 84–95:** only genuinely new IDs affect the bounded event log.

**Files/ranges that would need modification for identity enforcement**

No files were modified. If implementation is approved, the smallest change set is:

1. `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java:70–78` — add strict logical-ID validation, with no normalization.
2. `kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java:55–107` — add invalid-character, over-64-character, and valid-boundary tests; optionally add builder and explicit `fromDecision(...)` rejection tests near existing ID tests.
3. `AuditServiceTest.java` should not require behavior changes; existing idempotency/conflict tests already establish the required semantics. A focused test using a valid generated UUID and/or invalid append construction may be added only if desired.

`kleio-core` remains free of HAPI/FHIR dependencies because the validation is a plain Java string invariant.

**2. ThemisPrincipal.attributes investigation**

**Themis API definition**

`themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisPrincipal.java:25–71`

- `ThemisPrincipal` is a Java record with `principalId`, `PrincipalType`, `sourceDomain`, and `Map<String,String> attributes` at **lines 28–33**.
- The constructor defensively copies attributes at **lines 35–37**.
- Convenience factories at **lines 39–61** always create empty attributes.
- `toString()` reports only `attributeCount`, not contents, at **lines 63–70**.
- Because it is a record, attributes participate in `ThemisPrincipal.equals()`/`hashCode()`.

**Kleio production use**

`HarmoniaAuditEvent` stores `ThemisPrincipal` directly in both record components at **lines 56–57**. It copies the request principal and context executor without projection in `fromDecision(...)`:

- initiating principal selection: **lines 217–220**;
- executing principal selection: **lines 221–223**;
- builder legacy principal construction uses `ThemisPrincipal.of(...)` at **lines 591–594**.

No Kleio production code reads `initiatingPrincipal().attributes()` or `executingPrincipal().attributes()`. The audit service queries only principal IDs; `AuditService` documents that behavior at **lines 58–64**, and `InMemoryAuditService` uses IDs in its query logic.

**Kleio test use**

The only direct attribute-bearing principals found in Kleio audit tests are the safe-string fixtures:

`kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java:179–265`

- Initiator and executor contain `sessionSecret`/`serviceSecret` at **lines 182–193**.
- The event is constructed at **lines 205–224**.
- Assertions at **lines 226–265** verify that principal attributes, PHI markers, authority contents, authorization message contents, and arbitrary event attributes do not leak through `toString()`.
- This test establishes redaction, not a requirement that principal attributes are durable audit semantics.

Other tests use factory-created principals with empty attributes, for example:

- dual-principal test: **lines 154–177**;
- `fromDecision(...)` test: **lines 268–336**;
- explicit context provenance test: **lines 338–374**;
- serialization test: **lines 429–460**;
- builder/accessor test: **lines 463–518**.

The serialization test proves equality preservation for an event whose principal has empty attributes; it does not establish persistence of non-empty principal attributes.

**Repository evidence and recommendation**

The repository-wide evidence supplied by the focused search shows principal attributes are used by general Themis/security-context and other non-Kleio code, but no current Kleio audit behavior assigns them audit meaning. Do not alter `ThemisPrincipal` globally: its attributes remain useful to authorization and context consumers.

The important contract issue is that the current `HarmoniaAuditEvent` record equality includes the complete nested `ThemisPrincipal`. Therefore there are only two semantically coherent Step 02.2 choices:

1. **Keep the current model:** persist principal attributes, requiring a governed agent-attribute representation and accepting the PHI/secret governance burden; or
2. **Adopt the requested bounded audit semantics:** replace the two audit-event principal components with a Kleio-owned immutable audit-principal value object containing only `principalId`, `principalType`, and `sourceDomain`, and project Themis principals into it when creating audit events.

For the stated preferred contract, option 2 is cleaner and safer. Merely ignoring attributes in the mapper while retaining `ThemisPrincipal` fields would make `fromFhir(toFhir(event))` unequal for events containing attributes. Overriding event equality to ignore nested attributes would also be undesirable: it would silently change the established full-record equality used by append integrity detection.

**Files/ranges implicated if the bounded principal model is approved**

No changes were made. A future focused model change would likely involve:

- New file: `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/AuditPrincipal.java` (or an equivalent clearly audit-scoped value object).
- `HarmoniaAuditEvent.java:20–24` imports; **lines 56–57** component types; **lines 217–223** `fromDecision(...)` projection; **lines 309–327** `toBuilder()`; **lines 330–347** builder fields; **lines 405–423** principal setters; **lines 591–594** legacy principal construction.
- `HarmoniaAuditEventTest.java:154–177`, **182–193**, **268–336**, **338–374**, **429–460**, and **463–518** for type/projection/equality/serialization coverage.
- `AuditServiceTest.java:58–137` and **231–275** for service-level principal assertions and dual-principal queries.

The exact external impact should be checked before implementation because `ThemisPrincipal` is widely used outside Kleio, even though no current evidence justifies changing it globally.

**3. Security-label semantics**

**Current source semantics**

`themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisResource.java`

- Resource labels and attributes are distinct fields at **lines 29–35**.
- Labels are defensively copied at **lines 37–41**.
- `securityLabels()` is an alias for the resource label set at **lines 93–95**.

`kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/AuditTarget.java:24–63`

- `AuditTarget` contains only `resourceType`, `resourceId`, and `securityDomain` at **lines 28–32**.
- Its comment explicitly describes a target projection without clinical payload or sensitive attributes at **lines 24–26**.
- `fromResource(...)` projects only the three target fields at **lines 48���53**; it does not place labels inside `AuditTarget`.

`HarmoniaAuditEvent.java`

- Event-level `securityLabels` component: **line 62**.
- Defensive immutable copy: **line 81**.
- `fromDecision(...)` copies `request.target().securityLabels()` into the event at **lines 248–250**.
- The constructed event receives those labels at **line 273**.

**Tests establishing current behavior**

`HarmoniaAuditEventTest.java`

- Defensive copying and immutability: **lines 109–152**, especially source labels at **115–116**, event construction at **127–130**, and assertions at **141–149**.
- `fromDecision(...)` target-label preservation: **lines 268–272** supplies a `RESTRICTED` target label; **lines 329–330** verifies one event security label.
- Safe string test adds an event label at **line 217**, but only verifies redaction/count behavior.

**Revised mapping consequence**

The mapping contract must treat `HarmoniaAuditEvent.securityLabels` as labels of the **audited target**, because they originate from `ThemisResource`. The round-trip destination is therefore:

```text
HarmoniaAuditEvent.securityLabels
    ↔ AuditEvent.entity.securityLabel
```

`AuditEvent.meta.security` must not be used as the reconstruction source for this field. A persistence adapter may later add an `AUDIT` or equivalent label to `AuditEvent.meta.security` to protect/classify the stored audit resource, but that metadata is outside `HarmoniaAuditEvent` and must not alter event equality or reconstructed target labels.

No Kleio model change is needed for this correction; it is a Step 02.2 mapper contract correction.

**4. Recommended frozen decisions for review**

1. **Identity:** strengthen `HarmoniaAuditEvent` event-ID validation to the actual R5 logical-ID constraint; preserve exact input; no `audit-event-id` extension.
2. **Idempotency:** retain exact `eventId` as the append key. Same ID plus full semantic equality remains idempotent; same ID plus divergent full content remains an integrity violation. `InMemoryAuditService` already implements this at **lines 72–81**.
3. **Principal data:** do not modify `ThemisPrincipal` globally. If arbitrary attributes are excluded from durable audit semantics, introduce an audit-specific bounded principal before mapper implementation; otherwise the mapper must preserve attributes.
4. **Labels:** map event labels only from/to `AuditEvent.entity.securityLabel`; ignore persistence-only `meta.security` for Harmonia round-trip equality.
5. **No mapper-side normalization:** invalid IDs must fail before FHIR conversion rather than being trimmed, replaced, hashed, or moved to an identity extension.

**5. Confirmation of scope**

- Read-only exploration only.
- No Java, test, Maven, FHIR, persistence, REST, UI, or architecture files were modified.
- No tests or build commands were run, as implementation and verification are outside this read-only subtask.

**Discoveries for future steps**

- `eventId` validation can be localized to `HarmoniaAuditEvent` constructor lines 70–78; existing builder/fromDecision paths already funnel through that constructor.
- The current only non-empty principal attributes in Kleio are secret-valued safe-string fixtures at `HarmoniaAuditEventTest.java:182–193`; no audit semantic assertion depends on them.
- `fromDecision(...)` already provides the exact source needed for target-label mapping: `request.target().securityLabels()` at `HarmoniaAuditEvent.java:248–250`.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
This follow-up is Task 06 Step 02.1A: refine and freeze the HarmoniaAuditEvent <-> FHIR R5 AuditEvent contract, with possible focused `kleio-core` model/test changes only if needed.

Known from prior read-only work:
- `HarmoniaAuditEvent` currently has permissive `eventId` validation (non-null/non-blank only) and uses `ThemisPrincipal` directly, so principal attributes currently participate transitively in canonical equality.
- `securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and therefore represent audited-target labels, not `AuditEvent.meta.security`.
- `kleio-core` is intentionally FHIR-free.
- Existing mutable FHIR AuditEvent CRUD surfaces are out of scope.

Emphasize a minimal, coherent plan that:
1. first confirms whether code changes are required for eventId validity and principal durability semantics;
2. performs only the smallest necessary `kleio-core`/test changes;
3. then produces the revised/frozen mapping contract and Step 02.2 round-trip invariant.

Include targeted verification sequencing with narrow Maven commands first and relevant architecture verification if code changes occur.



**Requirements**

**Goal & Outcome**  
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

**Scope**
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

**Done When**
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

**Technical Design**

**Decisions**
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

**Approach & Touches**
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

**Nuances, Risks & Edge Cases**
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

**Contracts**

**AuditPrincipal Record**
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

**Event ID Validation Pattern**
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

**Final Field-by-Field Mapping Matrix**

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

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

**Revised Final Extension Register**
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
   *Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

**Testing**

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

**Assumptions & Open Questions**

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

**Delivery Steps**

**Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**  
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

**Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract**  
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



REVIEW THE PLAN

**Task description**  
Review the proposed delivery plan for this Step 02.1A refinement task.

**Feedback**  
Focus on whether the plan is minimal and properly sequenced for a mixed design+small-domain-change task: it should isolate any required `kleio-core` changes first (eventId invariant and possibly principal representation), verify them narrowly, then freeze the revised mapping contract. Flag if the proposed step on bounded `AuditPrincipal` risks being too broad or should be conditioned more explicitly on the model-decision outcome from the issue.



Plan Approved

**Verification**
- **Current model confirmed**: `HarmoniaAuditEvent` (kleio-core) uses `ThemisPrincipal initiatingPrincipal/executingPrincipal` record components with permissive `eventId` validation (null + `isBlank()` only, lines 70-83). This matches the plan's premise.
- **eventId regex correctness**: The plan's `^[A-Za-z0-9\-.]{1,64}$` exactly matches the FHIR R5 `Resource.id` logical-id constraint. All existing test literals (`evt-dec-1`, `evt-ser-1`, `arch-event-001`, `evt-builder-2`, generated UUIDs) satisfy it, and the current blank test (`"   "`) is still correctly rejected with `IllegalArgumentException`.
- **Change containment**: Grepped all consumers of `HarmoniaAuditEvent.initiatingPrincipal()/executingPrincipal()/originatingPrincipal()`. They exist only in kleio-core (main + tests) and `SecurityEnforcementArchitectureTest`. The `initiatingPrincipal()`/`executingPrincipal()` hits in `ThemisDefenceInDepthAcceptanceTest`, Pragma, PersistenceOperationEnvelope, ThemisSecurityContext, PetasosMessage are on unrelated types, so the `AuditPrincipal` component-type change does NOT ripple outside the plan's declared scope.
- **Arch-test impact checked**: `SecurityEnforcementArchitectureTest` scans the whole `kleio.audit.model` dir for forbidden imports (line 804) — a new `AuditPrincipal.java` importing only `themis.api` types passes. The `canonicalAuditModelExclusivityRule` and `KLEIO_AUDIT_MODEL_CLASSES` are name-specific and are not broken by adding `AuditPrincipal`. The safe-`toString` test uses builder + authorities/attributes and remains valid.
- **InMemoryAuditService**: `findByPrincipal` calls `.principalId()` on the principal components — `AuditPrincipal` exposes `principalId()`, so query logic survives.
- **Security-label re-anchoring** to `AuditEvent.entity.securityLabel` matches `fromDecision(...)` sourcing labels from `ThemisResource.securityLabels()` (lines 248-250). Correct.
- **Mapping matrix arithmetic** verified: NATIVE(2)+CODED(10)+EXTENSION(5)=17 canonical fields; extension register correctly pruned to 5 (removing `audit-event-id`, `agent-attribute`).
- **Verification commands** are real project tooling and correctly narrow (`mvn test -pl kleio/kleio-core` + arch tests via `-Dtest="*ArchitectureTest"`).

**Issues**  
None blocking.

**Notes**
- **Minimality tradeoff (the feedback's focus area)**: The plan chose the `AuditPrincipal` value object over the smaller documented alternative (constructor-normalization stripping attributes to `Map.of()` on `ThemisPrincipal`). This is the larger of the two options, but it is (a) explicitly sanctioned by issue Section 3 ("a dedicated bounded audit-principal value object"), (b) grounded in prior read-only research showing no audit semantic depends on `ThemisPrincipal.attributes`, and (c) more canonical — it mirrors the existing `AuditTarget`/`AuditAuthorizationEvidence` projection pattern and avoids a misleading empty `attributes()` getter. It is contained to kleio-core + paradeigma-test. I do not consider the smaller alternative *objectively* better (it trades code size for a contract inconsistency), so this is not a PASS_WITH_CONCERNS — but the executor should treat the `AuditPrincipal` decision as the plan's biggest single change and update the dual-principal tests (e.g. `HarmoniaAuditEventTest` ~lines 359-365 assert `initiatingPrincipal()`/`executingPrincipal()` equal `ThemisPrincipal` instances — these WILL need updating to `AuditPrincipal`, which the plan's Step 1 acceptance criteria already call out).
- **Step 2 verification is vestigial**: Step 2 is a pure design-freeze deliverable, yet its `Verification:` re-runs `mvn test -pl kleio/kleio-core` (same as Step 1) which does not actually validate the frozen contract. This is acceptable since the frozen matrix/register/round-trip invariant live in the plan document itself and are checkable via the concrete acceptance criteria, but the command adds no real signal for that step.
- **Minor**: When adding the pattern check, keep an `IllegalArgumentException` message containing "eventId" so the existing `hasMessageContaining("eventId")` assertions stay green. Not explicitly stated in the plan but consistent with its intent.

**Discoveries for future steps**
- `SecurityEnforcementArchitectureTest` walks the entire `kleio-core/.../audit/model` source dir for forbidden imports (HAPI/FHIR, JPA, JMS) — any new class there must stay pure-JDK + themis-api only.
- `HarmoniaAuditEvent` accessors `principalId()/principalType()/sourceDomain()/originatingPrincipal()` are hand-written aliases delegating to the initiating principal; the `AuditPrincipal` switch must preserve these to keep `InMemoryAuditService.findByPrincipal` and legacy builder tests compiling.



**Requirements**

**Goal & Outcome**  
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

**Scope**
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

**Done When**
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

**Technical Design**

**Decisions**
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

**Approach & Touches**
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

**Nuances, Risks & Edge Cases**
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

**Contracts**

**AuditPrincipal Record**
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

**Event ID Validation Pattern**
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

**Final Field-by-Field Mapping Matrix**

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

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

**Revised Final Extension Register**
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
   *Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

**Testing**

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

**Assumptions & Open Questions**

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

**Delivery Steps**

**Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**  
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

**Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract**  
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

**Requirements**

**Goal & Outcome**  
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

**Scope**
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

**Done When**
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

**Technical Design**

**Decisions**
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

**Approach & Touches**
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

**Nuances, Risks & Edge Cases**
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

**Contracts**

**AuditPrincipal Record**
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

**Event ID Validation Pattern**
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

**Final Field-by-Field Mapping Matrix**

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

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

**Revised Final Extension Register**
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
   *Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

**Testing**

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

**Assumptions & Open Questions**

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

**Delivery Steps**

*** Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**  
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

**Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract**  
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



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Implement only the smallest coherent `kleio-core` changes needed to satisfy Step 02.1A's revised contract.

Priorities:
1. strengthen `HarmoniaAuditEvent.eventId` so it is directly compatible with FHIR R5 `Resource.id` with fail-fast validation and no normalization;
2. remove arbitrary principal attributes from canonical audit-event equality/durable semantics via the cleanest local Kleio-owned bounded principal representation;
3. preserve existing append/idempotency semantics, `fromDecision(...)` semantics, dual-principal distinction, and safe string redaction;
4. keep `kleio-core` completely HAPI/FHIR-free.

Known from previous steps:
- `HarmoniaAuditEvent` currently validates `eventId` only as non-null/non-blank in `kleio/kleio-core/.../HarmoniaAuditEvent.java:70-78` and builder default IDs use `UUID.randomUUID().toString()`.
- `InMemoryAuditService` keys idempotency on exact `event.eventId()` and treats same ID + equal event as replay, same ID + divergent event as integrity violation.
- `ThemisPrincipal.attributes` are not read by Kleio audit logic; only safe-string tests currently use non-empty attributes as secret fixtures.
- `HarmoniaAuditEvent.securityLabels` come from `request.target().securityLabels()` in `fromDecision(...)`, so they are target labels; no Kleio model change is needed for that correction in this step.
- `SecurityEnforcementArchitectureTest` scans the whole `kleio.audit.model` package for forbidden imports, so any new model class must remain pure JDK + themis-api only.
- Preserve compatibility helpers like `principalId()`, `principalType()`, `sourceDomain()`, and any builder/accessor behavior that reasonably fits the bounded principal model.

Execution guidance:
- Prefer a dedicated `AuditPrincipal` value object in `kleio.audit.model` over silently stripping attributes inside `ThemisPrincipal`.
- Keep error messages mentioning `eventId` so existing assertion style remains valid.
- Update only the tests actually affected: event-id boundary validation, principal projection/equality semantics, safe string behavior, and any compilation fallout from the bounded principal type change.
- Run narrow commands first with 60-120s timeouts; report exact commands and elapsed times. Start with module-scoped tests and only widen if needed.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**

**Summary of Changes**

1. **Created `AuditPrincipal` Value Object**:
    - Path: `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/AuditPrincipal.java`
    - Immutable `record AuditPrincipal(String principalId, PrincipalType principalType, String sourceDomain) implements Serializable`.
    - Provided static factories: `of(principalId, principalType, sourceDomain)`, `of(principalId, principalType)`, `from(ThemisPrincipal)`.
    - Provided projection helper: `toThemisPrincipal()`.
    - Suppresses arbitrary runtime attributes; `toString()` emits bounded structural metadata (`AuditPrincipal[principalId=..., principalType=..., sourceDomain=...]`) with zero attribute exposure.

2. **Hardened `HarmoniaAuditEvent` Event Identity and Principal Boundaries**:
    - Path: `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java`
    - Added `private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");`.
    - Compact constructor validates `eventId` against `EVENT_ID_PATTERN`, failing fast with `IllegalArgumentException` on spaces, blank strings, illegal characters (colons, underscores, slashes, etc.), or strings longer than 64 characters without normalization.
    - Replaced `ThemisPrincipal initiatingPrincipal, ThemisPrincipal executingPrincipal` components with `AuditPrincipal initiatingPrincipal, AuditPrincipal executingPrincipal`.
    - Preserved backward-compatible accessors: `originatingPrincipal()` returning `AuditPrincipal`, `initiatingThemisPrincipal()`, `principalId()`, `principalType()`, `sourceDomain()`.
    - Updated `fromDecision(...)` to project request and context principals into `AuditPrincipal` via `AuditPrincipal.from(...)`.
    - Provided builder overloads accepting `AuditPrincipal` and `ThemisPrincipal` (auto-projecting to `AuditPrincipal`).

3. **Updated and Added Tests in `HarmoniaAuditEventTest`**:
    - Path: `kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java`
    - Added `testEventIdBoundaryValidation`: verified 1-character, 64-character, UUID-36, numeric, and dot/hyphen strings pass; verified blank, space, underscore, colon, slash, special characters, and >64 character IDs are rejected by constructors, builder, and `fromDecision(...)`.
    - Added `testBoundedPrincipalInvariants`: verified `AuditPrincipal` value equality ignores source `ThemisPrincipal.attributes`, ensuring exact canonical audit equality ($event_1 \equiv event_2$) regardless of external attribute differences.
    - Updated `testDualPrincipalDistinction`, `testSafeStringSuppression`, and `fromDecision` tests to assert `AuditPrincipal` representations and verify absence of `attributeCount` on principals while redacting PHI markers and secret tokens.

4. **Updated ArchUnit Suite in `paradeigma-test`**:
    - Path: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`
    - Added `AuditPrincipal.class` to `KLEIO_AUDIT_MODEL_CLASSES`, `canonicalAuditModelExclusivityRule`, and `noAuditModelInThemis`.
    - Confirmed zero HAPI/FHIR, zero JPA, and zero JMS imports in `kleio-core`.

**Verification**

Executed:
- `mvn test -pl kleio/kleio-core` (24 tests pass, 0 failures, elapsed ~2.3s).
- `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` (49 architecture tests pass, 0 failures, elapsed ~14s).
- Full combined verification command:
  `mvn test -pl kleio/kleio-core && mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` -> BUILD SUCCESS.

---

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl kleio/kleio-core` completes in ~2.3 seconds. When running ArchUnit tests across modules with `-am -Dtest="*ArchitectureTest"`, always append `-Dsurefire.failIfNoSpecifiedTests=false` to prevent non-test reactor modules from failing the build (~14s total).
- **AuditPrincipal equality**: Since `AuditPrincipal` excludes arbitrary attributes, any `HarmoniaAuditEvent` constructed with a `ThemisPrincipal` carrying transient/session attributes will have deterministic record equality matching an event constructed with attribute-free `AuditPrincipal`.
- **Target security labels**: `HarmoniaAuditEvent.securityLabels` cleanly originate from `ThemisResource.securityLabels()` (the target) and are already stored in `Set<ThemisSecurityLabel>` on the event, ready for direct mapping to `AuditEvent.entity.securityLabel` in Step 02.2.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Focus on whether the `kleio-core` changes are the minimal coherent implementation of Step 02.1A's required domain corrections:
- `eventId` must now be directly compatible with FHIR R5 logical IDs without normalization or fallback extensions;
- arbitrary `ThemisPrincipal.attributes` must no longer participate in canonical durable audit-event equality;
- dual-principal semantics, `fromDecision(...)`, append idempotency/integrity, and safe logging must remain intact;
- no HAPI/FHIR dependencies may enter `kleio-core`.

Please scrutinize the new `AuditPrincipal` choice versus alternatives, verify the actual test coverage and architecture impact, and flag any hidden regressions or overreach.



Implementation Approved

**Verification**
- **AuditPrincipal value object** (`kleio-core/.../audit/model/AuditPrincipal.java`): Immutable record `(principalId, principalType, sourceDomain) implements Serializable` with `of(id,type,domain)`, `of(id,type)`, `from(ThemisPrincipal)` (null-safe), `toThemisPrincipal()`, and a safe `toString()` that omits any attribute count. Pure JDK + themis-api; zero HAPI/FHIR. Matches the plan contract exactly.
- **EVENT_ID_PATTERN**: `^[A-Za-z0-9\-.]{1,64}$` precompiled and enforced in the compact constructor. Null → `NullPointerException`; blank/illegal/oversize → `IllegalArgumentException` with an `eventId`-containing message. Because both the builder and both `fromDecision(...)` overloads funnel through the canonical constructor, validation is enforced uniformly (verified by `testEventIdBoundaryValidation`, which exercises constructor, builder, and `fromDecision`).
- **Bounded principals**: Record components changed to `AuditPrincipal`; `fromDecision(...)` projects request/context principals via `AuditPrincipal.from(...)`. `testBoundedPrincipalInvariants` proves event equality/hashCode ignore arbitrary `ThemisPrincipal.attributes` (different attrs → equal events), satisfying the exact round-trip prerequisite without an `agent-attribute` extension.
- **Builder overloads**: Both `AuditPrincipal` and `ThemisPrincipal` accepted for `initiatingPrincipal`/`executingPrincipal`/`principal`/`originatingPrincipal` (auto-projecting). Legacy granular setters (`principalId`/`principalType`/`sourceDomain`) still resolve into an `AuditPrincipal` at build time.
- **Compatibility accessors** preserved: `originatingPrincipal()`, `initiatingThemisPrincipal()`, `principalId()`, `principalType()`, `sourceDomain()`, plus the existing `timestamp()`, `decisionId()`, etc.
- **Safe logging**: `testSafeStringSuppression` confirms PHI/secret/authority markers, attribute keys/values, and principal attributes are all redacted; nested `AuditPrincipal[...]` appears without attribute count.
- **Service semantics intact**: `AuditServiceTest` (idempotent replay, collision integrity via `AuditIntegrityException`, dual-principal query) uses the `ThemisPrincipal` builder overloads and `principalId()` accessor unchanged — no modification needed, still compiles and passes.
- **ArchUnit**: `SecurityEnforcementArchitectureTest` adds `AuditPrincipal.class` to `KLEIO_AUDIT_MODEL_CLASSES`; zero HAPI/FHIR, JPA, JMS, and Jakarta SecurityContext dependencies asserted for the whole package.
- **No external fallout**: `HarmoniaAuditEvent` is consumed only within `kleio-core` and `paradeigma-test`; the numerous `originatingPrincipal()`/`initiatingPrincipal()` hits in calliope/energeia/iris belong to `ThemisSecurityContext`/`Pragma`, not the audit event, so the component type change breaks nothing.
- **Builds green**: `mvn test -pl kleio/kleio-core` → BUILD SUCCESS (exit 0). `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → BUILD SUCCESS (exit 0).

**Issues**  
None.

**Notes**
- Executor also added planning markdown files under `.junie/plans/` (options-draft.md and the step-02.1a plan). These are outside the code scope but harmless and non-blocking.
- The `AuditPrincipal` choice (over constructor-side attribute stripping of `ThemisPrincipal`) is the stronger option: it gives compile-time type safety mirroring `AuditTarget`/`AuditAuthorizationEvidence` and avoids a misleading empty `attributes()` getter — consistent with the plan's stated decision.

**Discoveries for future steps**
- `mvn test -pl kleio/kleio-core` runs in a few seconds; the ArchUnit command takes longer (reactor `-am` build) — a 300s/600s timeout split is comfortable.
- `HarmoniaAuditEvent` has no production consumers outside `kleio-core`, so Step 02.2's `kleio-fhir` mapper can rely on the frozen `AuditPrincipal`/`securityLabels` shapes without touching other modules.



**Requirements**

**Goal & Outcome**  
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

**Scope**
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

**Done When**
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

**Technical Design**

**Decisions**
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

**Approach & Touches**
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

**Nuances, Risks & Edge Cases**
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

**Contracts**

**AuditPrincipal Record**
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

**Event ID Validation Pattern**
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

**Final Field-by-Field Mapping Matrix**

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

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

**Revised Final Extension Register**
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
   *Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

**Testing**

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**  
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

**Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract**  
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



**Requirements**

**Goal & Outcome**  
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

**Scope**
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

**Done When**
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

**Technical Design**

**Decisions**
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

**Approach & Touches**
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

**Nuances, Risks & Edge Cases**
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

**Contracts**

**AuditPrincipal Record**
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

**Event ID Validation Pattern**
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

**Final Field-by-Field Mapping Matrix**

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

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

**Revised Final Extension Register**
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
   *Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

**Testing**

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**  
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

*** Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract**  
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



WORKING

**Task description**  
Fully complete step 2.

**Feedback**  
Produce the final frozen Step 02.1A contract/report based on the now-updated `kleio-core` model and the previously established repository/FHIR findings.

The report must explicitly incorporate the Step 02.1A corrections:
- `eventId` now maps directly to `AuditEvent.id` with no `audit-event-id` extension, because `kleio-core` now enforces FHIR-compatible logical IDs;
- `securityLabels` represent audited-target labels and therefore round-trip through `AuditEvent.entity.securityLabel`, not `meta.security`;
- arbitrary principal attributes are no longer canonical durable audit semantics because the audit model now uses bounded `AuditPrincipal` rather than `ThemisPrincipal`.

Please deliver a concise but complete structured report that includes:
1. exploration findings relevant to the three corrections;
2. the model changes made and why;
3. exact tests changed/added;
4. exact commands/results already used for verification;
5. revised final field-by-field mapping matrix for every canonical field and relevant nested field;
6. final extension register (minimal set only);
7. explicit Step 02.2 round-trip invariant;
8. any unresolved blocker;
9. confirmation that no persistence/REST/UI work was performed.

Use the current repository state as authoritative after step 1. Keep the tone as a final implementation contract for review, not a speculative brainstorm.

**Constraints**  
Do not modify code. Do not implement Step 02.2. Do not introduce new repository work. Focus only on freezing the revised mapping contract.



Code is Ready

**Step 02.1A: Formalized & Frozen Canonical Mapping Contract**  
**`HarmoniaAuditEvent` (Domain) ↔ `org.hl7.fhir.r5.model.AuditEvent` (Durable Persistence)**

---

**1. Executive Summary & Governance Context**

This specification documents and formally freezes the canonical semantic mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing the authoritative implementation blueprint for **Step 02.2** (`HarmoniaAuditEventMapper` in `kleio-fhir`).

Following the completion of Step 1 (model hardening in `kleio-core`), the three architectural corrections required for Step 02.1A are codified:
1. **Direct Event Identity (`AuditEvent.id`)**: `HarmoniaAuditEvent.eventId` strictly enforces FHIR logical-ID syntax (`^[A-Za-z0-9\-.]{1,64}$`) at the domain constructor boundary, eliminating the need for mapper normalization, slugification, or an `audit-event-id` extension.
2. **Re-Anchored Target Security Labels (`AuditEvent.entity.securityLabel`)**: `HarmoniaAuditEvent.securityLabels` originate from the audited target resource (`ThemisResource.securityLabels()`) and map strictly to the target entity (`AuditEvent.entity.securityLabel`). `AuditEvent.meta.security` is formally decoupled as persistence/repository protection metadata and excluded from Harmonia domain equality.
3. **Bounded Principal Model (`AuditPrincipal`)**: Arbitrary runtime/session attributes (`ThemisPrincipal.attributes`) are eliminated from durable audit equality through the introduction of the immutable `AuditPrincipal` value object (`principalId`, `principalType`, `sourceDomain`), removing PHI/credential leakage risks and eliminating the `agent-attribute` extension.

**Governance & Layering Boundaries**
- **`kleio-core`**: Remains a pure Java domain module containing only canonical records, value objects, and append-only service contracts. In accordance with `AGENTS.md` and `PackageLayeringArchitectureTest`, `kleio-core` has **zero** compile or runtime dependencies on HAPI FHIR, JPA, or web frameworks.
- **`kleio-fhir` (Step 02.2)**: A dedicated module to be created under `kleio/` to house `HarmoniaAuditEventMapper`. It will depend unidirectionally on `kleio-core`, `calliope`, and HAPI FHIR R5 structures.
- **`calliope`**: Retains governance of cross-domain security vocabularies (`HarmoniaSecurityCodeSystem`), canonical schemas, and shared terminology URIs.
- **Hestia Mnemosyne & Iris BEFE**: Existing mutable CRUD/cache REST endpoints (`AuditEventResourceProvider`, `AuditEventResource`) remain untouched until Step 04 convergence.

---

**2. Exploration Findings Relevant to the Three Step 02.1A Corrections**

1. **Event Identity Invariants**:
    - Inspection of local HAPI 7.2.0 (`org.hl7.fhir.r5:6.1.2.2`) confirmed that FHIR R5 `AuditEvent` inherits `id` and `meta` from `Resource` and has no native `AuditEvent.identifier` field.
    - FHIR `Resource.id` requires `[A-Za-z0-9\-.]{1,64}`. Previously, `HarmoniaAuditEvent` validated `eventId` only as non-null and non-blank (`!eventId.isBlank()`), which allowed characters invalid in FHIR (spaces, colons, underscores, slashes) and lengths exceeding 64 characters.
    - Hardening `eventId` in the domain model guarantees that every valid domain event ID maps directly and losslessly to `AuditEvent.id` without mapper fallback logic or secondary identity extensions.

2. **Security Label Semantics**:
    - In `HarmoniaAuditEvent.fromDecision(...)`, `securityLabels` are copied from `request.target().securityLabels()`, which represents sensitivity tags on the clinical/business target resource (e.g. `CLINICAL`, `RESTRICTED`).
    - In FHIR R5, target resource security classifications belong on `AuditEvent.entity.securityLabel`.
    - `AuditEvent.meta.security` describes access control rules governing the `AuditEvent` record itself in a FHIR repository (e.g. `AUDIT` tagging applied by `FhirStorageService`). Conflating the two corrupted reverse-mapping equivalence.

3. **Principal Attributes & Immutability**:
    - `ThemisPrincipal.attributes` captures ephemeral runtime session state (HTTP headers, client IPs, tokens, temporary claims).
    - In Kleio, audit queries (`AuditService.findByPrincipal(...)`) and decision logging operate exclusively on `principalId`. No audit logic consumed principal attributes.
    - Because `HarmoniaAuditEvent.equals()` evaluates all components, retaining `ThemisPrincipal` with arbitrary attributes either forced proprietary extensions (`agent-attribute`) or broke round-trip equality ($event \not\equiv reconstructed$). Bounding the principal model to `(principalId, principalType, sourceDomain)` resolves this cleanly.

---

**3. `kleio-core` Model Changes Made and Rationale (Step 1 Implementation)**

1. **Dedicated `AuditPrincipal` Record**:
    - **Path**: `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/AuditPrincipal.java`
    - **Structure**:
      ```java
      public record AuditPrincipal(
              String principalId,
              PrincipalType principalType,
              String sourceDomain
      ) implements Serializable
      ```
    - **Capabilities**:
        - Static factories: `of(id, type, domain)`, `of(id, type)`, `from(ThemisPrincipal)`.
        - Projection helper: `toThemisPrincipal()` (returns `ThemisPrincipal` with empty attributes).
        - Safe `toString()`: Suppresses attribute counts and sensitive markers, emitting `AuditPrincipal[principalId=..., principalType=..., sourceDomain=...]`.
    - **Rationale**: Establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`; eliminates PHI/token leakage (`TOKEN-SECRET-MARKER-81742`) from audit evidence.

2. **Strict Event Identity Validation**:
    - **Path**: `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java`
    - **Pattern**: `private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");`
    - **Compact Constructor Enforcement**:
      ```java
      Objects.requireNonNull(eventId, "eventId must not be null");
      if (!EVENT_ID_PATTERN.matcher(eventId).matches()) {
          throw new IllegalArgumentException("eventId must match pattern " + EVENT_ID_PATTERN.pattern() +
                  " (1-64 alphanumeric, hyphen, or dot characters): " + eventId);
      }
      ```
    - **Rationale**: Fails fast on invalid characters or lengths > 64; guarantees 100% compatibility with FHIR `Resource.id`.

3. **Dual-Principal Component Hardening & Compatibility**:
    - Components updated to `AuditPrincipal initiatingPrincipal, AuditPrincipal executingPrincipal`.
    - `fromDecision(...)`: Projects `request.principal()` and `request.context().executingPrincipal()` via `AuditPrincipal.from(...)`.
    - Builder overloads: Accept both `AuditPrincipal` and `ThemisPrincipal` (auto-projecting via `AuditPrincipal.from(...)`).
    - Compatibility accessors preserved: `originatingPrincipal()`, `initiatingThemisPrincipal()`, `principalId()`, `principalType()`, `sourceDomain()`.

---

**4. Exact Tests Changed and Added**

**`kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEventTest.java`**
- `testEventIdBoundaryValidation`:
    - Valid boundaries verified: 1 character (`"a"`), 64 characters (`"a".repeat(64)`), UUID-36, numeric-only (`"1234567890"`), mixed dot/hyphen (`"Event-123.456-ABC"`, `"a.b-c.1-2"`).
    - Invalid boundaries rejected: empty string (`""`), blank (`"   "`), illegal characters (`"evt:1"`, `"evt_1"`, `"evt/1"`, `"evt#1"`, `"evt@1"`, `"evt 1"`, `"evt$1"`, `"evt!1"`), and length > 64 (`"a".repeat(65)`).
    - Verified across compact constructor, builder, and `fromDecision(...)`.
- `testBoundedPrincipalInvariants`:
    - Verified `AuditPrincipal` equality ignores source `ThemisPrincipal.attributes`.
    - Verified `HarmoniaAuditEvent` record equality ($event_1 \equiv event_2 \equiv event_3$) holds when built with `ThemisPrincipal` carrying divergent attributes or direct `AuditPrincipal`.
    - Verified `AuditPrincipal.toString()` suppresses `attributeCount`.
- Updates to existing tests:
    - `testDualPrincipalDistinction`: Updated to verify `AuditPrincipal` component instances.
    - `testSafeStringSuppression`: Verified secrets/tokens in source principals are not present in `HarmoniaAuditEvent.toString()`.
    - `testFromDecisionWithExplicitContextProvenance` & serialization round-trip: Verified compatibility with `AuditPrincipal`.

**`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`**
- Added `AuditPrincipal.class` to `KLEIO_AUDIT_MODEL_CLASSES`.
- Verified `AuditPrincipal` adheres to strict architectural isolation (resides in `kleio.audit.model`, pure JDK/themis-api, zero HAPI/FHIR, zero JPA, zero JMS).

**`kleio/kleio-core/src/test/java/net/fhirfactory/harmonia/kleio/audit/service/AuditServiceTest.java`**
- Verified all 10 service tests (idempotent replay, collision detection throwing `AuditIntegrityException`, dual-principal lookups, ring-buffer capacity) pass without modification.

---

**5. Exact Verification Commands and Results**

| Command | Scope | Elapsed Time | Results |
|---|---|---|---|
| `mvn test -pl kleio/kleio-core` | Kleio Core Unit Tests | 1.26s | **BUILD SUCCESS** (24 tests run, 0 failures, 0 errors, 0 skipped) |
| `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` | Full Reactor Architecture Tests (31 modules) | 9.84s | **BUILD SUCCESS** (49 architecture tests run across 7 suites, 0 failures, 0 errors) |

---

**6. Revised Final Field-by-Field Mapping Matrix**

**Classification Legend**:
- **NATIVE (2)**: Direct 1:1 mapping to native FHIR R5 core element.
- **CODED (10)**: Mapped to native FHIR R5 element paired with a governed Harmonia CodeSystem.
- **EXTENSION (5)**: Mapped to a justified, governed Harmonia extension.
- **NOT PERSISTED (3)**: Repository/persistence metadata ignored during reverse mapping.

| # | Harmonia Field | Mandatory | Proposed FHIR R5 Element | Class | Forward Mapping (`Harmonia -> FHIR`) | Reverse Mapping (`FHIR -> Harmonia`) | Cardinality | Governed Terminology / System | Loss Risk |
|---|---|---|---|---|---|---|---|---|---|
| 1 | `eventId` | Mandatory | `AuditEvent.id` | `NATIVE` | `setAuditEventId(eventId)` (direct logical ID) | `getIdPart()` | 1..1 | Direct logical ID `[A-Za-z0-9\-.]{1,64}` | None |
| 2 | `recordedAt` | Mandatory | `AuditEvent.recorded` | `NATIVE` | `new InstantType(Date.from(recordedAt))` | `recorded.getValue().toInstant()` | 1..1 | ISO 8601 UTC Instant | None |
| 3 | `classification` | Mandatory | `AuditEvent.category` | `CODED` | Add `CodeableConcept` with `audit-classification` system | Parse code matching `audit-classification` | 1..* | `http://harmonia.fhirfactory.net/security/audit-classification` | None |
| 4 | `action` | Mandatory | `AuditEvent.action` & `AuditEvent.code` | `CODED` | Set native `action` (`C`,`R`,`U`,`D`,`E`); set `code` with exact `action.name()` | Read `AuditEvent.code` coding | 1..1 | `http://harmonia.fhirfactory.net/security/audit-action` | None |
| 5 | `outcome` | Mandatory | `AuditEvent.outcome.code` | `CODED` | Set `outcome.code` coding with `outcome.name()` | Read `outcome.code` coding | 1..1 | `http://harmonia.fhirfactory.net/security/audit-outcome` | None |
| 6 | `initiatingPrincipal` | Optional | `AuditEvent.agent` (`requestor=true`) | `CODED` | Create agent: `requestor=true`, role `INITIATOR`, `who.identifier.value=principalId`, `type=principalType` | Match agent with `requestor=true` or role `INITIATOR`; parse `AuditPrincipal` | 0..1 | `.../agent-role`, `.../principal-type` | None |
| 7 | `executingPrincipal` | Optional | `AuditEvent.agent` (`requestor=false`) | `CODED` | Create agent: `requestor=false`, role `EXECUTOR`, `who.identifier.value=principalId`, `type=principalType` | Match agent with role `EXECUTOR`; parse `AuditPrincipal` | 0..1 | `.../agent-role`, `.../principal-type` | None |
| 8 | `securityDomain` | Optional | Extension `security-domain` | `EXTENSION` | Add extension `.../security-domain` with `StringType(securityDomain)` | Read extension `.../security-domain` | 0..1 | String / URI | None |
| 9 | `target` | Optional | `AuditEvent.entity` | `CODED` | Create entity with role `TARGET`, `what` reference/identifier (`resourceType/resourceId`), `detail` for target `securityDomain` | Match entity with role `TARGET`; extract `resourceType`, `resourceId`, and `securityDomain` detail | 0..1 | `http://harmonia.fhirfactory.net/security/entity-role` | None |
| 10 | `authorizationEvidence` | Optional | `AuditEvent.outcome.detail` & `agent.policy` | `CODED` | Map `decisionId`, `decision`, `reason`, `message` to `outcome.detail`; map `policyId` to `agent.policy` | Extract decision parameters from `outcome.detail` and `agent.policy` | 0..1 | `.../themis-decision-reason` | None |
| 11 | `authorities` | Optional | `AuditEvent.agent.authorization` | `CODED` | Map each authority to `agent.authorization` coding | Extract all codings matching system into `Set<ThemisAuthority>` | 0..* | `http://harmonia.fhirfactory.net/security/authorities` | None |
| 12 | `securityLabels` | Optional | `AuditEvent.entity.securityLabel` | `CODED` | Map each label to `entity.securityLabel` Coding on the TARGET entity | Extract matching codings from TARGET `entity.securityLabel` | 0..* | `http://harmonia.fhirfactory.net/security/labels` | None |
| 13 | `correlationId` | Optional | Extension `correlation-id` | `EXTENSION` | Add extension `.../correlation-id` with `StringType` | Read extension `.../correlation-id` | 0..1 | String / UUID | None |
| 14 | `causationId` | Optional | Extension `causation-id` | `EXTENSION` | Add extension `.../causation-id` with `StringType` | Read extension `.../causation-id` | 0..1 | String / UUID | None |
| 15 | `operationId` | Optional | Extension `operation-id` | `EXTENSION` | Add extension `.../operation-id` with `StringType` | Read extension `.../operation-id` | 0..1 | String / UUID | None |
| 16 | `source` | Optional | `AuditEvent.source` | `CODED` | Set `source.observer.identifier=component`, `source.type=subsystem` | Extract `subsystem` from `source.type` and `component` from `source.observer` | 0..1 | `http://harmonia.fhirfactory.net/source/subsystem` | None |
| 17 | `attributes` | Optional | Extension `audit-attribute` | `EXTENSION` | For each map entry, add sub-extensions `key` and `value` | Reconstruct `Map<String, String>` from sub-extensions | 0..* | Governed key-value strings | None |

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
1. `AuditEvent.meta.versionId`: Repository storage sequence counter; ignored during reverse mapping.
2. `AuditEvent.meta.lastUpdated`: Repository write timestamp; ignored during reverse mapping.
3. `AuditEvent.meta.security`: Storage access control classification (e.g. `AUDIT` tag applied by Hestia); ignored during reverse mapping so it never pollutes domain target `securityLabels`.

---

**7. Final Pruned Extension Register (Exactly 5 Extensions)**

The extension register is finalized and pruned to exactly **5 top-level extensions**:

1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain`
    - **Type**: `StringType` (`0..1`)
    - **Context**: `AuditEvent`
    - **Justification**: FHIR R5 `AuditEvent` has no top-level tenancy/domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id`
    - **Type**: `StringType` (`0..1`)
    - **Context**: `AuditEvent`
    - **Justification**: FHIR R5 has no native workflow tracing correlation element (`basedOn` requires a structured `Reference`).
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id`
    - **Type**: `StringType` (`0..1`)
    - **Context**: `AuditEvent`
    - **Justification**: FHIR R5 has no native causation tracking element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id`
    - **Type**: `StringType` (`0..1`)
    - **Context**: `AuditEvent`
    - **Justification**: FHIR R5 has no native invocation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute`
    - **Type**: Complex Backbone (`0..*`) with sub-extensions:
        - `key`: `StringType` (conforming to `^[a-zA-Z0-9_\-\.]{1,64}$`)
        - `value`: `StringType` ($\le 256$ chars)
    - **Context**: `AuditEvent`
    - **Justification**: FHIR R5 has no top-level generic key-value metadata map.

**Explicitly Removed Extensions**
- **`audit-event-id` (REMOVED)**: Redundant because `HarmoniaAuditEvent.eventId` strictly enforces FHIR logical-ID syntax and maps 1:1 to `AuditEvent.id`.
- **`agent-attribute` (REMOVED)**: Redundant because audit principals are bounded to `AuditPrincipal` (`principalId`, `principalType`, `sourceDomain`), excluding arbitrary runtime attributes.

---

**8. Governed Terminology & System URIs**

| Concept | Canonical System URI | Permitted / Governed Codes |
|---|---|---|
| Classification | `http://harmonia.fhirfactory.net/security/audit-classification` | `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM` |
| Action | `http://harmonia.fhirfactory.net/security/audit-action` | `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE` |
| Outcome | `http://harmonia.fhirfactory.net/security/audit-outcome` | `SUCCESS`, `FAILURE`, `DENIED` |
| Agent Role | `http://harmonia.fhirfactory.net/security/agent-role` | `INITIATOR`, `EXECUTOR` |
| Principal Type | `http://harmonia.fhirfactory.net/security/principal-type` | `HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS` |
| Entity Role | `http://harmonia.fhirfactory.net/security/entity-role` | `TARGET` |
| Entity Detail | `http://harmonia.fhirfactory.net/security/entity-detail` | `securityDomain` |
| Authorities | `http://harmonia.fhirfactory.net/security/authorities` | Governed authority string codes (e.g. `provider.read`) |
| Security Labels | `http://harmonia.fhirfactory.net/security/labels` | `CLINICAL`, `RESTRICTED`, `AUDIT`, `INTERNAL`, `PROVIDER_REGISTRY`, `ADMINISTRATIVE` |
| Source Subsystem | `http://harmonia.fhirfactory.net/source/subsystem` | Subproject codes (`THEMIS`, `KLEIO`, `ENERGEIA`, `PYLAI`, `HESTIA`, `CALLIOPE`, `IRIS`, `AGORA`, `PETASOS`) |
| Source Component | `http://harmonia.fhirfactory.net/source/component` | Specific internal service identifiers |

---

**9. Formal Step 02.2 Round-Trip Invariant Specification**

The Step 02.2 mapping contract formally establishes the exact round-trip equivalence invariant:

$$\text{fromFhir}(\text{toFhir}(event)) \equiv event$$

**Invariant Verification Rules**
1. **Mathematical Record Equality**: For any valid `HarmoniaAuditEvent event`, the reconstructed instance must satisfy `event.equals(reconstructed)` and `event.hashCode() == reconstructed.hashCode()`.
2. **Field-by-Field Preservation**:
    - `eventId`: Preserved identically via `AuditEvent.id`.
    - `recordedAt`: Instant truncated to millisecond precision matching FHIR `InstantType`.
    - `classification`, `action`, `outcome`: Recovered losslessly via governed Codings.
    - `initiatingPrincipal`, `executingPrincipal`: Reconstructed as `AuditPrincipal(id, type, domain)` matching initiator/executor roles. Reconstructed instances are independent of the ordering of entries in `AuditEvent.agent`.
    - `securityDomain`, `correlationId`, `causationId`, `operationId`: Reconstructed from their respective extensions; null if extension omitted.
    - `target`: Reconstructed from `AuditEvent.entity` where role is `TARGET`.
    - `authorizationEvidence`: Reconstructed from `AuditEvent.outcome.detail` and `AuditEvent.agent.policy`.
    - `authorities`: Reconstructed as `Set<ThemisAuthority>` from `agent.authorization`.
    - `securityLabels`: Reconstructed as `Set<ThemisSecurityLabel>` from `entity.securityLabel` on the target entity.
    - `source`: Reconstructed from `AuditEvent.source`.
    - `attributes`: Reconstructed as `Map<String, String>` from `audit-attribute` extensions.
3. **Persistence Metadata Isolation**:
    - The reverse mapper must ignore `AuditEvent.meta.versionId`, `AuditEvent.meta.lastUpdated`, and `AuditEvent.meta.security`.
    - Injecting arbitrary persistence metadata into the FHIR `AuditEvent` must **not** alter the reconstructed `HarmoniaAuditEvent` or violate equivalence:
      $$\text{fromFhir}(\text{toFhir}(event) \cup \{\text{meta.security, meta.versionId}\}) \equiv event$$
4. **Historical Provenance Invariant**:
    - Authorities mapped to `AuditEvent.agent.authorization` represent historical execution evidence at the time of the event and must never be interpreted or exposed as active authorization capability.

---

**10. Architectural Governance Boundary & Step 02.2 Implementation Blueprint**

```
harmonia/
├── calliope/              [Shared schemas, FHIR R5 base structures, HarmoniaSecurityCodeSystem]
├── kleio/
│   ├── kleio-core/        [Pure Java domain: HarmoniaAuditEvent, AuditPrincipal, AuditService]
│   └── kleio-fhir/        [NEW in Step 02.2: HarmoniaAuditEventMapper, FHIR converters]
└── hestia/ & iris/        [Existing mutable AuditEvent CRUD - untouched until Step 04]
```

- **Module Creation in Step 02.2**:
    - Create `kleio/kleio-fhir` declared in `kleio/pom.xml` and managed in root `pom.xml`.
    - Dependencies: `kleio-core`, `calliope`, `ca.uhn.hapi.fhir:hapi-fhir-base`, `ca.uhn.hapi.fhir:hapi-fhir-structures-r5`.
- **Mapper Class**:
  `net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper` with signatures:
    - `public AuditEvent toFhir(HarmoniaAuditEvent event)`
    - `public HarmoniaAuditEvent fromFhir(AuditEvent auditEvent)`

---

**11. Unresolved Blockers Analysis**

**Zero Blockers Identified.**
- The `kleio-core` model was successfully hardened and verified in Step 1.
- All 17 canonical fields map cleanly with zero semantic loss.
- The 5-item extension register completely covers all non-native concepts.
- Step 02.2 is unblocked and ready for immediate implementation.

---

**12. Confirmation of Constraints**

- **No Code Modifications**: No production or test source code was modified during this step.
- **No Step 02.2 Implementation**: No mapper classes or FHIR dependencies were introduced.
- **No Persistence / REST / UI Modifications**: Hestia Mnemosyne, Iris BEFE, and database surfaces were completely untouched.
- **Verification**: `kleio-core` test suite remains fully green.

---

**Discoveries for Future Steps**
- **Execution commands & speed**: `mvn test -pl kleio/kleio-core` runs in ~1.26s; full ArchUnit verification across 31 modules runs in ~9.8s with `-Dsurefire.failIfNoSpecifiedTests=false`.
- **Mapper dual-agent ordering**: In `toFhir`, add initiator first (`requestor=true`), then executor (`requestor=false`). In `fromFhir`, iterate over `agent` list and match by `requestor` flag and `agent-role` coding so agent list ordering never breaks round-trip equality.
- **Instant precision**: FHIR R5 `InstantType` supports millisecond/microsecond precision; ensure `toFhir` uses `Date.from(recordedAt)` and `fromFhir` extracts via `getValue().toInstant()`.
- **Target entity role**: Always check `entity.role` coding for `TARGET` when resolving `AuditTarget` and target `securityLabels` to avoid collision if auxiliary audit entities are added in the future.
- **Persistence metadata exclusion**: Explicitly test in Step 02.2 that setting `meta.versionId("2")` and adding `HarmoniaSecurityLabelEnum.AUDIT` to `meta.security` does not affect `fromFhir(...)` output.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Focus on whether the frozen Step 02.1A contract accurately reflects the now-updated `kleio-core` model and the required corrections from the issue:
- no `audit-event-id` extension;
- `securityLabels` round-trip via `AuditEvent.entity.securityLabel`, not `meta.security`;
- bounded principals with no `agent-attribute` extension;
- exact Step 02.2 round-trip invariant excluding persistence-only FHIR metadata.

Also verify the report is internally consistent with the performed tests/commands and does not quietly drift into Step 02.2 implementation or broader out-of-scope changes.



Checking recent IDE activity

Implementation Approved

**Verification**
- **Authoritative frozen contract correct**: `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md` freezes all 17 canonical fields with classifications 2 NATIVE / 10 CODED / 5 EXTENSION / 3 NOT PERSISTED (verified count matches the matrix rows).
- **Correction 1 (event identity)**: `eventId ↔ AuditEvent.id` (NATIVE, 1..1); `audit-event-id` extension explicitly removed (lines 15, 120, 149). Matches code: `HarmoniaAuditEvent.EVENT_ID_PATTERN = ^[A-Za-z0-9\-.]{1,64}$` (line 71) enforced in the compact constructor.
- **Correction 2 (security labels)**: `securityLabels ↔ AuditEvent.entity.securityLabel` (CODED, line 131); `meta.security` isolated as NOT PERSISTED (line 141). Matches code (`Set<ThemisSecurityLabel> securityLabels`).
- **Correction 3 (bounded principals)**: dual-agent native `AuditEvent.agent` mapping with `requestor`/role; `agent-attribute` extension removed (line 149). Matches code: `AuditPrincipal(principalId, principalType, sourceDomain)` with no attributes; `HarmoniaAuditEvent` uses `AuditPrincipal` for both principals.
- **Correction 4 (round-trip invariant)**: `fromFhir(toFhir(event)) ≡ event` formalized (line 31/207) with explicit exclusion of `meta.versionId`, `meta.lastUpdated`, `meta.security`.
- **Extension register**: exactly 5 (`security-domain`, `correlation-id`, `causation-id`, `operation-id`, `audit-attribute`) — verified.
- **Governance boundary**: kleio-core domain-only, kleio-fhir in 02.2, Calliope vocabularies, Hestia/Iris untouched until Step 04 — documented.
- **Non-regression**: `mvn test -pl kleio/kleio-core` → BUILD SUCCESS, exit 0 (HarmoniaAuditEventTest: 14, AuditServiceTest: 10; 0 failures/errors).
- **No scope drift**: no `kleio-fhir` module, no `HarmoniaAuditEventMapper`, no HAPI/FHIR dependency added to kleio-core, no persistence/REST/UI changes. Executor's step-2 changes are documentation only.

**Issues**  
None blocking.

**Suggested alternative**  
The similarly-named canonical mapping report `.junie/reports/Harmonia Security - Task 6 - Step 2.1 - Immutable Audit Evidence (Mapping).md` (1708 lines) is an append-log that still contains the **superseded pre-correction design** and directly contradicts the freeze:
- line 534: `eventId | AuditEvent.id & extension audit-event-id | NATIVE / EXTENSION` (audit-event-id retained);
- `securityLabels` mapped to `meta.security`;
- `agent-attribute` and `agent-attribute`-style principal-attribute extensions retained (sections 10, 21);
- line 1639 lists `audit-event-id` as the resolution; the file ends "This report concludes Step 02.1".

Because this is the file a Step 02.2 implementer is most likely to open as "the mapping report", the retained contradictory content is a real confusion risk. The better approach is cheap: append the corrected Step 02.1A frozen contract to that report (or add a superseding header note pointing to `task-06-step-02.1a-audit-mapping-contract.md`) so the canonical mapping report reflects the freeze rather than the contradictory superseded design. Switching cost: cheap (documentation-only edit). This is not a defect in the freeze itself — the authoritative plan file is fully correct — hence PASS_WITH_CONCERNS rather than FAIL.

**Notes**
- Step 2 is a documentation/contract-freeze step; the code (AuditPrincipal, eventId validation, bounded principals, security-label field) was implemented and approved in Step 1. This review confirmed the frozen contract accurately reflects the current code and that Step 2 introduced no code changes.
- The `.junie/reports/...Mapping.md` file's stale content originates from earlier read-only Step 02.1 exploration/design sessions (pre-corrections), not from fabricated claims in this step.

**Discoveries for future steps**
- `mvn test -pl kleio/kleio-core -q` runs in a few seconds (24 tests); use it as the fast non-regression gate for kleio-core doc/model changes.
- Authoritative frozen mapping contract lives in `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md` (Contracts section); the `.junie/reports/...Step 2.1...Mapping.md` file is a stale append-log with the superseded design — do NOT use it as the 02.2 implementation reference.



WORKING

**Task description**  
Within step 2, do only the part in ## Task scope.

**Task scope**  
Resolve the documentation-confusion risk from review by updating the stale Step 02.1 mapping report so it clearly points to the frozen Step 02.1A contract and no longer appears authoritative for Step 02.2 implementation. The cheapest acceptable fix is either:
- add a prominent superseding note at the top of `.junie/reports/Harmonia Security - Task 6 - Step 2.1 - Immutable Audit Evidence (Mapping).md` that Step 02.1A supersedes the earlier design and that the authoritative implementation contract is `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md`, explicitly listing the key corrections (no `audit-event-id`, securityLabels via `entity.securityLabel`, no `agent-attribute` / bounded principals),
  or
- otherwise make an equally clear minimal documentation update that removes ambiguity for Step 02.2 implementers.

Do not rewrite the whole report unless necessary; keep the fix minimal and targeted.

**Feedback**  
Reviewer verdict was PASS_WITH_CONCERNS. I agree the frozen contract itself is correct, but the stale earlier mapping report is likely to mislead Step 02.2 implementation because it still documents the superseded design. This is a cheap high-value quality improvement, so please make it now.

Use narrow verification only (e.g. inspect the updated file). Report exactly what was changed.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on documentation changes.



Code is Ready

The stale Step 02.1 mapping report (`.junie/reports/Harmonia Security - Task 6 - Step 2.1 - Immutable Audit Evidence (Mapping).md`) has been updated to clearly identify it as superseded.

A prominent notice has been added at the top of the file, directing implementers to the authoritative Step 02.1A contract (`.junie/plans/task-06-step-02.1a-audit-mapping-contract.md`) and summarizing the three key mapping corrections:

1. `eventId` maps directly to `AuditEvent.id` (no `audit-event-id` extension).
2. `securityLabels` map to `AuditEvent.entity.securityLabel` (not `meta.security`).
3. Principal attributes are excluded via bounded `AuditPrincipal` (no `agent-attribute` extension).

This ensures that Step 02.2 implementers are aware of the frozen, correct mapping contract and avoids confusion stemming from the superseded report.



IMPLEMENTATION REVIEW

**Task description**  
Within step 2, do only the part in ## Task scope.

**Task scope**  
Resolve the documentation-confusion risk from review by updating the stale Step 02.1 mapping report so it clearly points to the frozen Step 02.1A contract and no longer appears authoritative for Step 02.2 implementation. The cheapest acceptable fix is either:
- add a prominent superseding note at the top of `.junie/reports/Harmonia Security - Task 6 - Step 2.1 - Immutable Audit Evidence (Mapping).md` that Step 02.1A supersedes the earlier design and that the authoritative implementation contract is `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md`, explicitly listing the key corrections (no `audit-event-id`, securityLabels via `entity.securityLabel`, no `agent-attribute` / bounded principals),
  or
- otherwise make an equally clear minimal documentation update that removes ambiguity for Step 02.2 implementers.

Do not rewrite the whole report unless necessary; keep the fix minimal and targeted.

**Feedback**  
Review only this scoped documentation fix. Confirm the superseding note is prominent, specific enough to prevent Step 02.2 confusion, and minimal in blast radius.



Implementation Approved

**Verification**
- **Scope of this pass**: The task scope was strictly a minimal documentation fix — adding a prominent superseding note to the stale Step 02.1 mapping report so it no longer misleads Step 02.2 implementers. Only this scope was reviewed.
- **Note present & prominent**: `.junie/reports/Harmonia Security - Task 6 - Step 2.1 - Immutable Audit Evidence (Mapping).md` lines 5–10 contain a blockquote `NOTICE` at the very top of the substantive content, before the EXPLORATION body. It renders prominently.
- **Points to authoritative contract**: The note correctly names `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md` as the authoritative implementation contract; confirmed that file exists via glob search.
- **Lists all three required corrections**: (1) `eventId` maps directly to `AuditEvent.id` (no `audit-event-id` extension); (2) `securityLabels` map to `AuditEvent.entity.securityLabel` (not `meta.security`); (3) principal attributes excluded via bounded `AuditPrincipal` (no `agent-attribute` extension). These exactly match the corrections demanded by the issue and the frozen contract.
- **Minimal blast radius**: Only a ~6-line blockquote was added; the remainder of the stale report is untouched (as intended — it stays as a historical append-log now clearly marked superseded).
- **No scope drift / no regression**: Change is documentation-only. No code, POM, test, or module changes; no `kleio-fhir` module or `HarmoniaAuditEventMapper` introduced; no HAPI/FHIR dependency added to kleio-core. This directly resolves the PASS_WITH_CONCERNS concern raised in the prior review.

**Issues**  
None.

**Notes**
- This pass addresses exactly the earlier reviewer's `Suggested alternative` (the stale report was the file a 02.2 implementer would most likely open). The confusion risk is now removed with a cheap, targeted edit.
- Lines 1–4 of the report are pre-existing unrelated config-echo lines ("Optional spending limit…"); the note at line 5 is still effectively the first substantive content and is sufficiently prominent. Not a blocker.

**Discoveries for future steps**
- Authoritative frozen mapping contract: `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md`. The `.junie/reports/…Step 2.1…Mapping.md` file is a stale append-log, now explicitly marked superseded at its top — do not use it as the 02.2 implementation reference.



**Requirements**

**Goal & Outcome**  
Refine and freeze the canonical mapping contract between the Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), establishing a verified implementation contract for Step 02.2. Harden `kleio-core` domain invariants so that `eventId` strictly aligns with FHIR `Resource.id` (`[A-Za-z0-9\-.]{1,64}`) and audit principals are bounded without arbitrary attributes, re-anchor security labels to `AuditEvent.entity.securityLabel`, prune the extension register to 5 essential items, and formally lock the exact round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) with zero HAPI/FHIR contamination in `kleio-core`.

**Scope**
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

**Done When**
- `HarmoniaAuditEvent` rejects invalid `eventId` values (spaces, colons, underscores, slashes, >64 characters) and accepts valid boundary IDs and generated UUIDs.
- `HarmoniaAuditEvent` principal equality ignores or excludes arbitrary principal attributes, guaranteeing exact round-trip equivalence without an `agent-attribute` extension.
- Security label mapping targets `AuditEvent.entity.securityLabel` on the target entity; `meta.security` is isolated as persistence metadata.
- Field-by-field mapping table and 5-item extension register are fully frozen.
- Step 02.2 round-trip invariant ($\text{fromFhir}(\text{toFhir}(event)) \equiv event$) is mathematically defined.
- All unit tests in `kleio-core` and ArchUnit architecture tests pass with zero HAPI/FHIR dependencies in `kleio-core`.

**Technical Design**

**Decisions**
- **Chose dedicated `AuditPrincipal` value object / not persisting arbitrary `ThemisPrincipal.attributes` via extension**: Audit principals represent historical identity evidence (`principalId`, `principalType`, `sourceDomain`), not transient execution state. Bounding the principal model eliminates PHI/credential leakage risks (`TOKEN-SECRET-MARKER-81742`), avoids proprietary extensions, and ensures exact record equality ($event \equiv reconstructed$).
  *(Alternative rejected: Normalizing `ThemisPrincipal` by stripping attributes at constructor boundary — functional, but leaves an empty `attributes()` getter in the public API; `AuditPrincipal` establishes compile-time type safety matching `AuditTarget` and `AuditAuthorizationEvidence`.)*
- **Chose precompiled regex `^[A-Za-z0-9\-.]{1,64}$` in `HarmoniaAuditEvent` / not mapper-side normalization**: Enforces FHIR logical-ID compliance at the domain constructor boundary. Fails fast on malformed IDs; normal `UUID.randomUUID().toString()` (36 hex + hyphens) is 100% compliant. Prevents mapper-side slugification, truncation, or hashing that would corrupt idempotency keys.
  *(Alternative rejected: `audit-event-id` extension fallback — redundant when domain IDs already conform to FHIR `id` rules.)*
- **Chose `AuditEvent.entity.securityLabel` for target security labels / not `AuditEvent.meta.security`**: `HarmoniaAuditEvent.securityLabels` originate from `ThemisResource.securityLabels()` in `fromDecision(...)` and describe the audited resource. `meta.security` describes repository protection/classification of the AuditEvent resource itself.
  *(Alternative rejected: Mapping to `meta.security` — conflates target classification with audit record classification.)*
- **Chose minimal 5-extension register / not proprietary action, classification, or target extensions**: Native R5 elements (`category`, `code`, `action`, `outcome`, `entity`, `source`, `agent`) natively represent core audit concepts when paired with governed Harmonia CodeSystems.
- **Chose keeping `ThemisPrincipal` untouched in `themis-api` / not altering it globally**: Non-audit callers (security context, authorization engine) continue to require attributes; domain bounding is isolated to Kleio.

**Approach & Touches**
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

**Nuances, Risks & Edge Cases**
- **Fail-Fast ID Invariant**: The domain must reject invalid IDs immediately. A mapper in Step 02.2 must never encounter a non-conforming `eventId` or perform runtime fallback logic.
- **Persistence Metadata Isolation**: FHIR server-managed fields (`meta.versionId`, `meta.lastUpdated`, repository-level `meta.security`) are NOT audit evidence and must be ignored during reverse mapping.
- **Historical Provenance vs Active Grants**: In accordance with Task 04 invariants, authority snapshots mapped to `AuditEvent.agent.authorization` represent historical evidence at execution time and must never be interpreted as active capability.
- **Zero HAPI/FHIR Dependencies**: `AuditPrincipal` and `HarmoniaAuditEvent` must remain 100% pure JDK types with zero HAPI or FHIR imports.

**Contracts**

**AuditPrincipal Record**
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

**Event ID Validation Pattern**
```java
private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");
```

**Final Field-by-Field Mapping Matrix**

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

**Excluded Persistence Metadata (NOT PERSISTED in HarmoniaAuditEvent)**
- `AuditEvent.meta.versionId`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.lastUpdated`: Generated by storage repository; ignored during reverse mapping.
- `AuditEvent.meta.security`: Stored resource protection/classification metadata (e.g. `AUDIT` tag); ignored during reverse mapping so it never pollutes target `securityLabels`.

**Revised Final Extension Register**
1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (0..1, `StringType`): Top-level tenancy realm. Native R5 has no top-level domain element.
2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (0..1, `StringType`): Distributed workflow trace boundary. Native R5 has no top-level correlation string.
3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (0..1, `StringType`): Direct causing work item. Native R5 has no causation element.
4. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (0..1, `StringType`): Granular invocation instance trace. Native R5 has no operation trace element.
5. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (0..*, complex with `key`: `StringType`, `value`: `StringType`): Bounded metadata escape hatch. Native R5 has no generic top-level key-value map.
   *Explicitly removed*: `audit-event-id` (maps directly to `AuditEvent.id`) and `agent-attribute` (principal model is strictly bounded).

**Testing**

- Scenario 1 (Event ID Boundary Validation): `HarmoniaAuditEvent` rejects null, blank (`""`, `"   "`), invalid characters (`evt:1`, `evt_1`, `evt/1`, `evt#1`, `evt@1`, `evt 1`), and strings longer than 64 characters; accepts 1-character (`"a"`), 64-character, standard UUID-36, and dot/hyphen strings.
- Scenario 2 (Bounded Principal Invariants): `AuditPrincipal` correctly encapsulates `(principalId, principalType, sourceDomain)` with zero attributes; `HarmoniaAuditEvent` equality ignores external principal attributes; safe `toString()` emits `AuditPrincipal[...]` without attribute count.
- Scenario 3 (Target Security Label Isolation): `HarmoniaAuditEvent.securityLabels` map to `AuditEvent.entity.securityLabel`; adding arbitrary codings to `AuditEvent.meta.security` does not affect reverse mapping or event equality.
- Scenario 4 (Dual Principal Preservation): Distinct `initiatingPrincipal` (`requestor=true`, `INITIATOR`) and `executingPrincipal` (`requestor=false`, `EXECUTOR`) are preserved regardless of agent list order.
- Scenario 5 (Decision Conversion Fidelity): `HarmoniaAuditEvent.fromDecision(...)` projects request and decision into bounded audit principals and target entities without fabricating missing IDs.
- Scenario 6 (Idempotent Append & Conflict Semantics): `InMemoryAuditService` preserves append idempotency for identical events and throws `AuditIntegrityException` for same ID with divergent payload.
- Scenario 7 (Safe Logging Redaction): Diagnostic string representations redact raw messages, authorities, and test markers (`PATIENT-PHI-MARKER-92831`, `TOKEN-SECRET-MARKER-81742`).
- Regression Targets: Full test suite in `kleio-core` (`HarmoniaAuditEventTest`, `AuditServiceTest`) and ArchUnit test suite in `paradeigma-test` (`SecurityEnforcementArchitectureTest`, `PackageLayeringArchitectureTest`).

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Harden Event Identity and Bounded Principal Invariants in kleio-core**  
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

**✓ Step 2: Formalize and Freeze the HarmoniaAuditEvent ↔ FHIR R5 AuditEvent Mapping Contract**  
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



**Summary**
- Refined and froze the Step 02.1A `HarmoniaAuditEvent ↔ FHIR R5 AuditEvent` contract, including direct `eventId ↔ AuditEvent.id`, target-label mapping via `AuditEvent.entity.securityLabel`, and a bounded audit-principal model.
- Made the smallest required `kleio-core` changes so Step 02.2 can implement the mapper without FHIR leakage into the domain model.

**Changes**
- Added `AuditPrincipal` and updated `HarmoniaAuditEvent` to use bounded principals; arbitrary `ThemisPrincipal.attributes` no longer participate in canonical durable audit equality.
- Hardened `HarmoniaAuditEvent.eventId` to strict FHIR logical-ID validation (`[A-Za-z0-9\-.]{1,64}`) with fail-fast rejection and no normalization or fallback extension.
- Updated/added focused tests for event-ID boundaries, bounded-principal equality/redaction, and kept the frozen mapping contract plus superseding note in the earlier Step 02.1 mapping report.

**Verification**
- Passed `mvn test -pl kleio/kleio-core` and `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.
- Verified no HAPI/FHIR dependencies entered `kleio-core`, append idempotency/integrity semantics remain unchanged, and no persistence/REST/UI work was performed.

**Notes**
- Final frozen Step 02.1A contract removes `audit-event-id` and `agent-attribute`, keeps only the 5 required top-level extensions, and defines the Step 02.2 invariant as `fromFhir(toFhir(event)) ≡ event` excluding persistence-only metadata like `meta.versionId`, `meta.lastUpdated`, and audit-resource `meta.security`.

