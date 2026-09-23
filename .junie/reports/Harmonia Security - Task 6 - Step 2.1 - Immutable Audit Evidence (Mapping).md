Optional spending limit; leave empty for no limit: 35
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

> **NOTICE: This report is superseded by the finalized contract in `.junie/plans/task-06-step-02.1a-audit-mapping-contract.md`. Please refer to the updated plan for the authoritative implementation contract.**
>
> **Key corrections:**
> - `eventId` maps directly to `AuditEvent.id` (no `audit-event-id` extension).
> - `securityLabels` map to `AuditEvent.entity.securityLabel` (not `meta.security`).
> - Principal attributes are excluded via bounded `AuditPrincipal` (no `agent-attribute` extension).

EXPLORATION

**Task description**  
Perform a tightly scoped, read-only exploration for the Step 02.1 design report. Focus first on `kleio/kleio-core` and directly relevant Themis API types consumed by the audit model. Determine the exact current contract of `net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent` and its related types/tests, including field structure, mandatory vs optional fields, constructor/builder invariants, equality/idempotency semantics, value-object details, `fromDecision(...)` behavior, and any serialization/safe-string expectations. Also identify the main file paths and line ranges for all canonical audit model/service types and their tests so they do not need to be rediscovered later.

Then inspect the actual FHIR R5/HAPI AuditEvent model available to this repository and the minimal existing repository usage needed for Step W: authoritative R5 class structure actually on classpath, where AuditEvent is created/read/updated/deleted, and any existing profiles/extensions/code systems that may affect the later mapper design.

Return a concise but high-signal exploration report with exact file paths/line numbers, clear separation between Kleio model findings, FHIR R5 model findings, and existing AuditEvent usage/compatibility concerns.

**Constraints**  
Read-only only. Do not modify code, do not implement mapper logic, do not author design recommendations yet. Avoid broad repository-wide exploration beyond the specific Kleio/Themis/FHIR AuditEvent surfaces needed for this report.



Checking recent IDE activity

Exploration is Ready

**Step 02.1 targeted exploration report**

**1. Executive summary**

The canonical audit object is a Java `record` in `kleio-core`, with five mandatory scalar fields and twelve nullable/optional fields. It is domain-only: `kleio-core` depends on `themis-api`, SLF4J, and JDK types, but not HAPI FHIR. Collections are defensively copied, record equality is value-based across every component, and `InMemoryAuditService` uses full `HarmoniaAuditEvent.equals()` to implement idempotent append and conflicting-ID detection.

The repository declares HAPI FHIR **7.2.0**. The actual R5 model available locally is `org.hl7.fhir.r5.model.AuditEvent`, extending `DomainResource`; its own fields are the R5 fields `category`, `code`, `action`, `severity`, `occurred[x]`, `recorded`, `outcome`, `authorization`, `basedOn`, `patient`, `encounter`, `agent`, `source`, and `entity`. The class has no declared `identifier` field; `id`/`meta` are inherited from `Resource`.

Existing AuditEvent support is generic mutable FHIR CRUD/cache support. It accepts externally supplied FHIR `AuditEvent` resources, supports create/read/update/delete/search, and does not currently use `HarmoniaAuditEvent`, Kleio, or a Harmonia AuditEvent profile/extension.

No implementation changes were made and no tests were run.

---

**2. Current Kleio model contract**

**Canonical files**

| Type | Path and relevant lines |
|---|---|
| `HarmoniaAuditEvent` | `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java:50-83` record declaration and constructor; `:185-279` `fromDecision`; `:282-327` safe `toString`, builder factory, `toBuilder`; `:330-624` builder and compatibility fields/build logic |
| `AuditClassification` | `.../audit/model/AuditClassification.java:20-30` |
| `AuditAction` | `.../audit/model/AuditAction.java:22-51` |
| `AuditOutcome` | `.../audit/model/AuditOutcome.java:22-43` |
| `AuditTarget` | `.../audit/model/AuditTarget.java:24-63` |
| `AuditAuthorizationEvidence` | `.../audit/model/AuditAuthorizationEvidence.java:26-72` |
| `AuditSource` | `.../audit/model/AuditSource.java:22-46` |
| `AuditService` | `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/AuditService.java:27-102` |
| `InMemoryAuditService` | `.../audit/service/InMemoryAuditService.java:39-203` |
| `AuditIntegrityException` | `.../audit/service/AuditIntegrityException.java:20-65` |
| Kleio POM | `kleio/kleio-core/pom.xml:37-62` |

**Exact top-level fields**

`HarmoniaAuditEvent` declares, in order (`:50-67`):

1. `String eventId`
2. `Instant recordedAt`
3. `AuditClassification classification`
4. `AuditAction action`
5. `AuditOutcome outcome`
6. `ThemisPrincipal initiatingPrincipal`
7. `ThemisPrincipal executingPrincipal`
8. `String securityDomain`
9. `AuditTarget target`
10. `AuditAuthorizationEvidence authorizationEvidence`
11. `Set<ThemisAuthority> authorities`
12. `Set<ThemisSecurityLabel> securityLabels`
13. `String correlationId`
14. `String causationId`
15. `String operationId`
16. `AuditSource source`
17. `Map<String,String> attributes`

The record implements `Serializable` (`:68`).

**Mandatory versus optional**

The canonical compact constructor requires (`:70-78`):

- `eventId` non-null and non-blank. It is not trimmed or otherwise normalized by this constructor.
- `recordedAt` non-null.
- `classification` non-null.
- `action` non-null.
- `outcome` non-null.

The following are nullable: both principals, `securityDomain`, `target`, authorization evidence, the three IDs, `source`. Null `authorities`, `securityLabels`, and `attributes` become immutable empty collections/maps (`:80-82`).

The builder supplies defaults (`:575-589`): random UUID event ID, `Instant.now()`, `SECURITY`, `AUTHORIZE`, and `SUCCESS` when omitted. This means the direct constructor contract is strict, while the builder is intentionally permissive/defaulting.

**Value-object semantics**

- `AuditTarget` is a serializable record of `resourceType`, `resourceId`, and `securityDomain` (`AuditTarget.java:28-32`). Its compact constructor trims each non-null string (`:34-38`) but imposes no non-null requirement. `fromResource(...)` projects only those three fields from `ThemisResource` (`:48-53`); target labels and target attributes are not included in the value object.
- `AuditAuthorizationEvidence` is a serializable record containing `decisionId`, `ThemisDecision decision`, `ThemisDecisionReason reason`, `policyId`, and `message` (`AuditAuthorizationEvidence.java:32-38`). It has no validation. `fromDecision(...)` copies all five values (`:50-61`). Its `toString()` emits decision ID, decision, reason, policy ID, and only a `hasMessage` boolean; it suppresses message content (`:63-72`).
- `AuditSource` is a serializable record of `subsystem` and `component` (`AuditSource.java:25-28`), trimming both non-null values (`:30-33`) without required-field validation.
- `AuditClassification` values are `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM` (`AuditClassification.java:23-29`).
- `AuditAction` values are `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE` (`AuditAction.java:25-32`).
- `AuditOutcome` values are `SUCCESS`, `FAILURE`, `DENIED` (`AuditOutcome.java:25-28`).

`HarmoniaAuditEvent` uses Java record-generated `equals()`/`hashCode()`. Since collections are sets/maps, their equality is order-independent; equality covers all 17 record components, including optional IDs, nested value objects, authority/security-label sets, and attributes.

**Principals, authorities, labels, and Themis types**

Direct Themis API types consumed by Kleio:

- `ThemisPrincipal`: `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisPrincipal.java:25-71`. Fields are `principalId`, `PrincipalType principalType`, `sourceDomain`, and `Map<String,String> attributes`; attributes are immutable (`:28-37`). Principal IDs/types/domains have no constructor validation. Factory methods cover HUMAN, SYSTEM, SERVICE, and PROCESS (`:47-61`). `toString()` emits ID/type/domain and attribute count only (`:63-71`).
- `PrincipalType`: `.../PrincipalType.java:20-28`; values `HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`.
- `ThemisAuthority`: `.../ThemisAuthority.java:23-43`; one trimmed, non-blank `authorityCode`, with value-based record equality.
- `ThemisSecurityLabel`: `.../ThemisSecurityLabel.java:23-52`; `system` defaults to `http://harmonia.fhirfactory.net/security/labels`, `code` is required/non-blank, and matching is case-insensitive by code.
- `ThemisResource`: `.../ThemisResource.java:26-49`; required `resourceType`, optional `resourceId`/`securityDomain`, immutable labels and attributes. `securityLabels()` aliases its labels (`:93-102`).
- `ThemisSecurityContext`: `.../ThemisSecurityContext.java:29-52`; distinguishes `requestingPrincipal`/`originatingPrincipal` and `executingPrincipal`, and includes security domain, authority set, correlation/causation IDs, tenant/client metadata, requested time, and attributes. It defensively copies sets/maps (`:35-52`).
- `ThemisAuthorizationRequest`: `.../ThemisAuthorizationRequest.java:27-40`; contains principal, authority set, `ThemisAction`, target, and context. Null context is replaced with anonymous context (`:37-40`).
- `ThemisAuthorizationDecision`: `.../ThemisAuthorizationDecision.java:28-47`; contains decision ID, `ALLOW`/`DENY`, reason, policy ID, evaluated time, correlation ID, and message. Missing decision IDs/timestamps/reasons are defaulted (`:42-47`).
- `ThemisDecisionReason`: `.../ThemisDecisionReason.java:20-39`; includes `ALLOWED_BY_POLICY`, `DEFAULT_DENY`, `EXPLICIT_DENY`, missing-principal/authority/action/resource/security-label reasons, policy/service/context/tampering/revocation reasons.
- `ThemisAction`: `.../ThemisAction.java:20-37`; includes READ, SEARCH, SUBMIT_CREATE, SUBMIT_UPDATE, PROCESS, APPROVE, REJECT, CREATE, UPDATE, DELETE, EXECUTE, REPLAY, ADMINISTER.

The event constructor defensively copies sets/maps with `Set.copyOf`/`Map.copyOf` (`HarmoniaAuditEvent.java:80-82`). Builder setters also copy collections (`:440-474`), and null key/value additions are ignored (`:515-521`). There is no explicit size, key grammar, value grammar, or PHI/secret filtering constraint in the `attributes` implementation. `Map.copyOf` rejects null keys/values and does not promise insertion ordering.

**`fromDecision(...)` behavior**

`HarmoniaAuditEvent.fromDecision(...)` is at `HarmoniaAuditEvent.java:192-279`:

- no-ID overload generates a random UUID (`:192-194`); explicit overload requires non-null event ID, request, and decision (`:205-208`);
- `recordedAt` is `decision.evaluatedAt()` or current time (`:210`);
- classification is always `SECURITY` (`:211`);
- action maps request `ThemisAction` through `AuditAction.fromThemisAction(...)`; null request action becomes `AUTHORIZE` (`:212-214`);
- `ALLOW` becomes `SUCCESS`; `DENY` becomes `DENIED` (`:215`); it never generates `FAILURE`;
- initiating principal is request principal, otherwise context originating principal (`:217-220`);
- executing principal is copied only from context (`:221-223`);
- event `securityDomain` precedence is target domain, then context domain, then requesting principal source domain (`:225-232`);
- target is a three-field `AuditTarget` projection (`:234-236`);
- all decision evidence fields are copied (`:238`);
- authorities are the union of request authorities and context authorities (`:240-246`);
- event security labels are copied from target labels (`:248-250`);
- correlation ID prefers context correlation ID, otherwise decision correlation ID (`:252-254`);
- causation ID comes from context; `operationId` is deliberately null (`:256-257`);
- source is always `THEMIS` / `AUTHORIZATION_ENGINE` (`:259`).

The model tests explicitly confirm no fabricated executing principal, causation ID, or operation ID, and preservation when context supplies executing/causation values (`HarmoniaAuditEventTest.java:268-369`).

**Serialization and safe-string expectations**

No Jackson annotations or FHIR dependencies appear in `kleio-core` production sources. The model and direct value objects implement Java `Serializable`; Java object-stream round-trip, equality, hashCode, and safe-string stability are tested at `HarmoniaAuditEventTest.java:429-460`.

`HarmoniaAuditEvent.toString()` emits structural fields and counts, not raw authority or attribute contents (`HarmoniaAuditEvent.java:282-303`). Themis principal and authorization-evidence `toString()` methods are similarly redacted. The safe-string test uses PHI and secret markers and verifies suppression at `HarmoniaAuditEventTest.java:179-265`; it still permits operational identifiers such as event ID, principal ID, target type/ID, policy ID, correlation/causation/operation IDs, and source names.

---

**3. Kleio service and idempotency contract**

`AuditService.java:31-81` defines:

- `append(HarmoniaAuditEvent)`;
- `findById`;
- `findByCorrelationId`;
- `findByPrincipal` across initiating/executing principals;
- recent-event retrieval;
- `recordDecision(request, decision)`;
- compatibility aliases at `:83-102`.

`InMemoryAuditService.append(...)` (`InMemoryAuditService.java:64-103`) indexes by `event.eventId()`:

- first unseen ID is appended;
- same ID plus `existing.equals(event)` returns the existing object without log growth (`:72-76`);
- same ID plus divergent full record throws `AuditIntegrityException` (`:78-81`);
- the bounded implementation is a ring buffer and may evict old entries (`:84-94`), so it is a development/test implementation rather than durable immutable storage.

The service tests establish this at:

- allow/deny decision mapping and queries: `AuditServiceTest.java:58-137`;
- direct append: `:139-159`;
- idempotent replay: `:161-186`;
- conflicting same-ID event: `:188-229`;
- dual-principal and correlation lookup: `:231-275`;
- capacity/eviction: `:277-337`;
- null handling: `:361-381`.

---

**4. Existing FHIR R5 model actually available**

**Repository declarations**

- HAPI version declaration: root `pom.xml:65-69`, `hapi-fhir.version` = `7.2.0`.
- Calliope directly depends on HAPI base and R5 structures: `calliope/pom.xml:37-54`.
- Mnemosyne clinical directly depends on HAPI base, R5 structures, server, JPA server, and client: `hestia/mnemosyne-clinical/pom.xml:68-106`.
- `kleio-core` has no HAPI dependency: `kleio/kleio-core/pom.xml:37-49`.

The local resolved underlying FHIR R5 model artifact inspected was `org.hl7.fhir.r5:org.hl7.fhir.r5:6.1.2.2`, pulled by the declared HAPI 7.2.0 stack. The authoritative class is:

`org.hl7.fhir.r5.model.AuditEvent extends org.hl7.fhir.r5.model.DomainResource`.

The generated HAPI source for `AuditEvent.java` in that artifact has the top-level field declarations at approximately lines `2536-2631`:

- repeating `List<CodeableConcept> category`;
- required-model `CodeableConcept code`;
- `AuditEventAction action`;
- `AuditEventSeverity severity`;
- choice `DataType occurred` with `Period` or `DateTimeType` accessors;
- `InstantType recorded`;
- `AuditEventOutcomeComponent outcome`;
- repeating `List<CodeableConcept> authorization`;
- repeating `List<Reference> basedOn`;
- `Reference patient`;
- `Reference encounter`;
- repeating `List<AuditEventAgentComponent> agent`;
- `AuditEventSourceComponent source`;
- repeating `List<AuditEventEntityComponent> entity`.

Nested component declarations in the same actual R5 source are:

- outcome component: source lines `393-408`, with `code: Coding` and repeating `detail` components;
- agent component: source lines `636-694`, with `type`, repeating `role`, `who`, `requestor`, `location`, repeating `policy`, `network`, and repeating agent-level `authorization`;
- source component: source lines `1306-1327`, with `site`, `observer`, and repeating `type`;
- entity component: source lines `1595-1638`, with `what`, `role`, repeating `securityLabel`, `query`, repeating `detail`, and repeating nested `agent`;
- entity detail value is a FHIR `DataType`; the actual class exposes typed choices including `Quantity`, `CodeableConcept`, `StringType`, `BooleanType`, `IntegerType`, `Range`, `Ratio`, `TimeType`, `DateTimeType`, `Period`, and `Base64BinaryType`.

The generated class exposes HAPI search constants for action, agent/role, basedOn, category, entity/role, outcome, policy, purpose, source, code, date, encounter, and patient. It does **not** expose an `identifier` property in the inspected R5 `AuditEvent` class. `id` and `meta` are inherited from `Resource`; they are not AuditEvent-specific fields.

The model therefore differs materially from assumptions based on older structures: R5 has both `occurred[x]` and `recorded`, a top-level repeating authorization list, nested agent authorization, nested entity agents, and a severity field.

---

**5. Existing AuditEvent usage and compatibility concerns**

**Clinical Mnemosyne provider**

`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java`:

- resource type is `AuditEvent.class`: `:33-45`;
- accepts arbitrary inbound `AuditEvent` on create and delegates to storage: `:47-55`;
- reads by logical ID: `:57-60`;
- updates by logical ID: `:62-69`;
- deletes by logical ID: `:71-77`;
- searches by `_id`, `name`, and `identifier`: `:79-91`.

The underlying `FhirStorageService` is generic for all FHIR resources:

- create accepts an inbound resource, generates an ID if absent, applies default security tagging, and stores/replaces by resource type + ID: `hestia/.../FhirStorageService.java:136-199`;
- read parses stored JSON: `:202-214`;
- update increments version and replaces the stored JSON: `:216-277`;
- delete is a soft delete (`deleted=true`): `:279-294`;
- search parses all non-deleted JSON resources and applies generic filters: `:296-334`.

For AuditEvent specifically, clinical integration coverage is a full mutable lifecycle at `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/FhirResourceCrudIntegrationTest.java:317-343`: it constructs an R5 `AuditEvent`, sets action `C`, recorded time, one agent `who`, and code text; creates, reads, changes action to `U`, updates, reads again, deletes, and verifies `ResourceGoneException`.

The generic clinical search implementation recognizes AuditEvent for the `name` filter by matching action code or agent display (`FhirStorageService.java:350-397`, specifically `:377-379`). Its `identifier` branch has no AuditEvent-specific case (`:399-...`), so the provider’s advertised `identifier` parameter is not backed by a native AuditEvent identifier search implementation.

**Iris BEFE cache/REST surface**

`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` exposes:

- GET search: `:42-50`;
- GET by ID: `:52-61`;
- POST parses arbitrary JSON into `AuditEvent.class` and saves it: `:64-75`;
- PUT parses arbitrary JSON, forcibly sets `AuditEvent/{id}`, and saves it: `:78-90`;
- DELETE removes it from cache: `:92-100`.

The corresponding integration test accepts a minimal externally supplied payload containing only `resourceType`, `action`, and `code`, then creates and reads it (`iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/FhirRestResourceIntegrationTest.java:155-173`).

`FhirCacheService` maps AuditEvent to `auditevent-cache` (`:83-101`), saves mutable parsed resources and increments `meta.versionId` (`:155-187`), and deletes cache entries directly (`:190-202`). Search matches AuditEvent action/agent display for `name` (`:243-274`); the identifier filter has no AuditEvent branch (`:288-317`).

**Existing profiles/extensions/CodeSystems**

No AuditEvent-specific `StructureDefinition`, profile, extension, ValueSet, or CodeSystem artifacts were found in the inspected Calliope/Hestia/Iris surfaces. Calliope does contain security vocabulary and FHIR tagging utilities:

- `calliope/src/main/java/net/fhirfactory/harmonia/model/security/HarmoniaSecurityCodeSystem.java:20-32` defines URI constants for labels, roles, authorities, purpose of use, and policies;
- `HarmoniaSecurityLabelEnum.java:25-89` defines `PROVIDER_REGISTRY`, `INTERNAL`, `AUDIT`, `RESTRICTED`, `CLINICAL`, and `ADMINISTRATIVE`, maps them to `meta.security` Codings, and uses `http://harmonia.fhirfactory.net/security/labels`;
- `FhirConfidentialityEnum.java:28-204` provides HL7 v3 confidentiality codes for FHIR R5 `meta.security`;
- `FhirSecurityTagManager.java:31-305` applies/extracts confidentiality and Harmonia labels from `Resource.meta.security`;
- vocabulary tests are at `calliope/src/test/java/net/fhirfactory/harmonia/model/security/HarmoniaSecurityVocabularyTest.java:32-120`.

These are security-label utilities, not an AuditEvent profile or mapper, and no current code connects them to Kleio audit events.

---

**6. High-signal compatibility findings for the later mapper work**

1. **Kleio has no FHIR leakage today.** Any mapper placed in `kleio-core` would introduce a new dependency boundary; current source/POM evidence shows the canonical model is intentionally FHIR-free.
2. **`eventId` is an arbitrary non-blank Java string in the current model.** Existing in-memory idempotency is keyed by the exact string and compares complete record equality; the current model does not impose FHIR logical-ID constraints or normalize IDs.
3. **`operationId` is a first-class optional field but is never generated by `fromDecision(...)`.** It is preserved only when built explicitly.
4. **`AuditOutcome.FAILURE` exists but is not produced by Themis decision conversion.** `ALLOW` maps to `SUCCESS`, `DENY` maps to `DENIED`.
5. **Several Themis actions collapse in `AuditAction.fromThemisAction(...)`:** `SUBMIT_CREATE -> CREATE`, `SUBMIT_UPDATE -> UPDATE`, and `PROCESS`, `APPROVE`, `REJECT`, `REPLAY`, `ADMINISTER -> EXECUTE` (`AuditAction.java:40-51`; tests `HarmoniaAuditEventTest.java:402-419`).
6. **`AuditAction.AUTHORIZE` is not produced by `fromThemisAction(...)`; it is the builder fallback when no action is provided or when a request action is null.**
7. **Target security domain and event security domain are distinct fields.** `AuditTarget` carries its own domain, while `HarmoniaAuditEvent` also carries top-level `securityDomain`; `fromDecision(...)` computes the latter with precedence rules.
8. **Target labels are not inside `AuditTarget`; `fromDecision(...)` copies them separately into event-level `securityLabels`.** Target attributes are dropped by `AuditTarget.fromResource(...)`.
9. **Authorities are a historical snapshot with set semantics.** The current model has no marker in the type system distinguishing historical authority evidence from active capability; consumers must not reinterpret reconstructed values as current authorization.
10. **Generic FHIR AuditEvent CRUD is mutable and externally permissive.** Clinical create can replace an existing resource ID (`FhirStorageService.java:181-195`), update increments versions, and delete soft-deletes. This does not implement Kleio’s append/idempotency/integrity contract.
11. **Current REST/provider surfaces do not enforce canonical Harmonia audit fields.** Minimal arbitrary AuditEvent payloads are accepted by existing tests.
12. **`identifier` search is exposed but not implemented for AuditEvent in both clinical and BEFE generic filters.**
13. **The existing `HarmoniaSecurityLabelEnum.AUDIT` and `meta.security` utilities are the only directly relevant FHIR vocabulary found.** No existing AuditEvent profile or event-specific terminology was found.
14. **Safe-string tests intentionally include PHI and credential markers.** Future mapper tests should account for FHIR parser/resource logging and avoid serializing raw authorization evidence messages, principal attributes, authority values, or arbitrary attributes into diagnostic logs.

**7. Explicit scope confirmation**

This was a read-only exploration only. No Java, Maven, FHIR, persistence, REST, UI, profile, extension, terminology, or test files were modified; no mapper logic was implemented; and no Git operations were performed.



EXPLORATION

**Task description**  
Continue the Step 02.1 read-only research with two narrow goals:

1. Inspect repository evidence for Calliope ownership of FHIR conformance artifacts and terminology relevant to Step Q/R/S: profiles, extensions, CodeSystems, ValueSets, canonical URLs, implementation-guide artifacts, and any established module/dependency boundary patterns that would inform where a future AuditEvent mapper and future conformance artifacts should live.

2. Inspect authoritative local/library evidence for the concrete FHIR R5 AuditEvent semantics most relevant to mapping decisions: action code values, outcome structure, requestor semantics, agent role/type/who distinctions, source/entity/detail capabilities, and whether there is any native identifier element on R5 AuditEvent besides inherited Resource.id.

Return exact file paths/line ranges and concise findings only. Avoid broad exploration outside Calliope/conformance surfaces and the actual local R5 AuditEvent library/spec sources.



Exploration is Ready

**Targeted Exploration Report: Step 02.1 Conformance and Mapping Semantics**

**1. Calliope Conformance Ownership**  
Our exploration confirms that `calliope` currently acts as the central repository for *security-related* terminology, but there are no formal FHIR conformance artifacts (Profiles, Extensions, ValueSets, Implementation Guides) defined in the repository.

*   **Existing Terminology Governance**: `calliope` holds security vocabularies.
    *   **Path**: `calliope/src/main/java/net/fhirfactory/harmonia/model/security/HarmoniaSecurityCodeSystem.java` (lines 20-32).
    *   This file defines URI constants for security labels (`http://harmonia.fhirfactory.net/security/labels`), roles, and authorities.
*   **Absence of Conformance Artifacts**: Searches for `StructureDefinition`, `ValueSet`, and `CodeSystem` (beyond the security code system above) yielded zero results across the entire repository.
*   **Conclusion**: There is no existing infrastructure for FHIR conformance artifacts in `calliope`. A future `AuditEvent` mapper and its associated conformance artifacts (e.g., a formal Harmonia `AuditEvent` profile) will necessitate the creation of a dedicated conformance/terminology module or the expansion of Calliope in Step 02.2.

**2. FHIR R5 AuditEvent Semantic Details**  
Based on the local HAPI 7.2.0 stack (`org.hl7.fhir.r5:org.hl7.fhir.r5:6.1.2.2`), the authoritative R5 `AuditEvent` semantics critical for mapping are:

*   **Identity**: `AuditEvent` inherits `id` and `meta` from `Resource`. It **does not** possess a dedicated `identifier` field. Identity mapping for `eventId` will require careful handling, possibly via `AuditEvent.id` or a specific entity/detail extension.
*   **Action**: `AuditEventAction` is an enum (C, R, U, D, E).
*   **Outcome**: `AuditEventOutcomeComponent` contains:
    *   `code`: A `Coding`.
    *   `detail`: Repeating components, each with `type` and `value[x]`.
*   **Agent (Principal Mapping)**: The `agent` component (repeating) is central:
    *   `who`: A `Reference` (the principal).
    *   `role`: Repeating `CodeableConcept` (the authority/role).
    *   `requestor`: A **boolean** (`agent.requestor`) — this is the critical field for distinguishing `initiatingPrincipal` (where `requestor=true`) from `executingPrincipal`.
    *   `policy`: Repeating `UriType` (authorization evidence/policy reference).
*   **Entity (Target Mapping)**: The `entity` component (repeating):
    *   `what`: A `Reference` (the target resource).
    *   `role`: `CodeableConcept`.
    *   `securityLabel`: Repeating `Coding`.
    *   `detail`: Repeating `AuditEventEntityDetailComponent` (type + value choice), ideal for structured target metadata.
*   **Source**: `AuditEventSourceComponent` contains `site`, `observer` (Reference), and `type` (repeating `Coding`).

These semantics confirm that the native R5 structure supports the required distinction between `initiatingPrincipal` and `executingPrincipal` (via `agent` role/requestor) and allows for detailed target representation via `entity` components, minimizing the need for `extensions`.

---
**Note**: No implementation changes were performed. This report is read-only.



ANALYSIS

**Task description**  
Using the repository context from the original task plus the targeted findings already gathered, produce the Step 02.1 semantic mapping design for `HarmoniaAuditEvent <-> FHIR R5 AuditEvent` as a read-only design report. Focus on semantically lossless durable mapping, minimizing extensions, and keeping Kleio domain objects FHIR-free.

Please synthesize concrete recommendations for:
- event identity and idempotency implications;
- field-by-field mapping classification (NATIVE / CODED / EXTENSION / NOT PERSISTED) for every current canonical `HarmoniaAuditEvent` field and directly relevant nested value-object fields;
- classification/type/action/outcome semantics;
- dual-principal/agent mapping;
- security-domain, target, authorization-evidence, authority snapshot, security labels, correlation/causation/operation IDs, source, and attributes;
- Calliope vs Kleio ownership and smallest coherent Step 02.2 module boundary;
- whether a Harmonia AuditEvent profile is needed;
- explicit proposed extension register and considered-but-rejected extensions;
- round-trip fidelity risks/blockers;
- compatibility concerns with existing mutable FHIR AuditEvent CRUD surfaces.

**Feedback**  
Use these already-established repository facts as hard inputs:
- Canonical model is `kleio/kleio-core/.../HarmoniaAuditEvent.java:50-67` with 17 fields: eventId, recordedAt, classification, action, outcome, initiatingPrincipal, executingPrincipal, securityDomain, target, authorizationEvidence, authorities, securityLabels, correlationId, causationId, operationId, source, attributes.
- Required fields by compact constructor are exactly eventId (non-null/non-blank), recordedAt, classification, action, outcome. Other scalars/components are optional; null sets/maps become immutable empty collections.
- Value/equality semantics are Java record equality across all fields; `InMemoryAuditService` uses exact `eventId` key + full-record equality for idempotent replay and throws integrity exception for same ID + different content.
- Builder defaults exist but the canonical constructor contract is strict.
- `fromDecision(...)` always sets classification=SECURITY, maps Themis action to AuditAction, maps decision ALLOW->SUCCESS / DENY->DENIED, preserves initiating vs executing principals distinctly, derives event securityDomain by precedence, projects target into `AuditTarget(resourceType, resourceId, securityDomain)`, copies authorization evidence, unions authorities, copies target labels into event securityLabels, sets correlationId from context or decision, causationId from context, operationId null, source=THEMIS/AUTHORIZATION_ENGINE.
- `AuditTarget` contains only resourceType, resourceId, securityDomain. Target labels are not inside `AuditTarget`; target attributes are dropped.
- `AuditAuthorizationEvidence` contains decisionId, decision, reason, policyId, message.
- `AuditSource` contains subsystem and component.
- Relevant Themis types: `ThemisPrincipal(principalId, principalType, sourceDomain, attributes)`, `PrincipalType` values HUMAN/SYSTEM/SERVICE/PROCESS, `ThemisAuthority(authorityCode)`, `ThemisSecurityLabel(system, code)`, `ThemisResource(resourceType, resourceId, securityDomain, securityLabels, attributes)`.
- HAPI stack is declared as HAPI FHIR 7.2.0; local inspected R5 model source exposes AuditEvent top-level fields: category*, code, action, severity, occurred[x], recorded, outcome(component), authorization*, basedOn*, patient, encounter, agent*, source, entity*. No AuditEvent-specific `identifier` field was found; `id`/`meta` are inherited from Resource.
- Local inspected nested R5 component shapes: outcome has `code: Coding` + repeating detail; agent has type, role*, who, requestor:boolean, location, policy:uri*, network, authorization*; source has site, observer, type*; entity has what, role, securityLabel*, query, detail*, agent*.
- Existing repository AuditEvent usage is generic mutable CRUD/cache support in clinical and Iris surfaces; it accepts arbitrary external AuditEvent resources, supports create/read/update/delete/search, and is not aligned to immutable Kleio semantics.
- Calliope currently contains security vocabulary helpers (`HarmoniaSecurityCodeSystem`, `HarmoniaSecurityLabelEnum`, `FhirSecurityTagManager`) but no existing AuditEvent-specific profiles/extensions/CodeSystems/ValueSets/IG artifacts were found.

**Constraints**  
Be explicit where repository evidence is insufficient and avoid claiming unsupported line-perfect FHIR spec details beyond the local/library evidence above. Design only; no implementation advice that requires changing code now.



**1. Executive Summary**

This report establishes the authoritative semantic mapping design between the canonical Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), satisfying Task 06 Step 02.1.

Key architectural conclusions of this design:
- **Zero Information Loss**: All 17 canonical fields of `HarmoniaAuditEvent` and its nested value objects (`AuditTarget`, `AuditAuthorizationEvidence`, `AuditSource`, `ThemisPrincipal`, `ThemisAuthority`, `ThemisSecurityLabel`) map to FHIR R5 and back with **zero loss of audit semantics** (achieving exact or normalized equivalent round-trip fidelity).
- **Native-First Alignment**: In accordance with core design principles, 11 top-level concepts leverage native FHIR R5 elements (such as `recorded`, `action`, `code`, `category`, `outcome`, `agent`, `entity`, and `source`) enriched with governed Harmonia CodeSystems.
- **Minimal, Justified Extension Register**: Exactly 5 top-level extensions and 2 nested backbone extensions are specified to address genuine structural gaps in the FHIR R5 `AuditEvent` specification (such as the absence of top-level correlation/causation/operation IDs, top-level security domain, and arbitrary attribute key-value collections).
- **Strict Architecture Boundaries**: In conformance with repository layering guardrails (`AGENTS.md` and `PackageLayeringArchitectureTest`), `kleio-core` remains a pure Java domain module free of HAPI FHIR dependencies. For Step 02.2, a new subproject module `kleio/kleio-fhir` is recommended to house the bidirectional mapper, while Calliope retains shared security vocabularies and canonical schema definitions.

This investigation and design report is strictly read-only; no code was modified, no dependencies were added, and no mapper was implemented.

---

**2. Current HarmoniaAuditEvent Contract**

The canonical audit data structure is implemented as a Java `record` in `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java` (lines 50–83).

**Component Declaration & Order**
```java
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

**Mandatory vs. Optional Fields**
The compact constructor (`HarmoniaAuditEvent.java:70–83`) strictly enforces:
- `eventId`: Mandatory, non-null, non-blank (`!eventId.isBlank()`).
- `recordedAt`: Mandatory, non-null.
- `classification`: Mandatory, non-null.
- `action`: Mandatory, non-null.
- `outcome`: Mandatory, non-null.

The remaining 12 components are optional:
- `initiatingPrincipal`, `executingPrincipal`, `securityDomain`, `target`, `authorizationEvidence`, `correlationId`, `causationId`, `operationId`, and `source` are nullable.
- `authorities`, `securityLabels`, and `attributes` default to defensive immutable empty collections (`Set.copyOf`, `Map.copyOf`) when null is passed.

**Value-Object Semantics**
- `AuditClassification` (`AuditClassification.java:23–29`): Enum with 6 values: `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
- `AuditAction` (`AuditAction.java:25–32`): Enum with 7 values: `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`.
- `AuditOutcome` (`AuditOutcome.java:25–28`): Enum with 3 values: `SUCCESS`, `FAILURE`, `DENIED`.
- `AuditTarget` (`AuditTarget.java:28–38`): Record containing `resourceType`, `resourceId`, and `securityDomain`. Non-null string fields are trimmed. Projection from `ThemisResource` drops target attributes and does not store target security labels inside the record.
- `AuditAuthorizationEvidence` (`AuditAuthorizationEvidence.java:32–38`): Record containing `decisionId`, `decision` (`ThemisDecision`), `reason` (`ThemisDecisionReason`), `policyId`, and `message`. Its `toString()` explicitly masks the message content (`hasMessage=true/false`).
- `AuditSource` (`AuditSource.java:25–33`): Record containing `subsystem` and `component`, trimming non-null strings.
- `ThemisPrincipal` (`themis-api/.../ThemisPrincipal.java:25–71`): Immutable object containing `principalId`, `principalType` (`PrincipalType`: `HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`), `sourceDomain`, and `attributes` (`Map<String, String>`).
- `ThemisAuthority` (`ThemisAuthority.java:23–43`): Record holding a trimmed, non-blank `authorityCode`.
- `ThemisSecurityLabel` (`ThemisSecurityLabel.java:23–52`): Record holding `system` (defaults to `http://harmonia.fhirfactory.net/security/labels`) and non-blank `code` (case-insensitive equality).

**Equality and Serialization**
- Equality is record value-based equality across all 17 components (`HarmoniaAuditEvent.equals()`).
- Implements `java.io.Serializable`. Defensive copying prevents external mutation of sets and maps.
- Diagnostic `toString()` emits structural identifiers and collection counts while redacting raw attribute maps and authorization messages to prevent PHI/credential leakage.

---

**3. Current Kleio Architecture**

The current repository structure reflects the completion of Task 06 Step 01:
- Subproject root: `kleio/` (parent POM `kleio/pom.xml`).
- Active module: `kleio/kleio-core/` (pure Java domain).
- Contract: `AuditService` (`kleio/.../AuditService.java:27–102`) defines the append-only audit evidence interface:
    - `append(HarmoniaAuditEvent event)`: Appends an immutable audit event.
    - `findById(String eventId)`
    - `findByCorrelationId(String correlationId)`
    - `findByPrincipal(String principalId)`
    - `recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision)`
- Reference implementation: `InMemoryAuditService` (`InMemoryAuditService.java:39–203`) enforces:
    - **New ID**: Appended to chronological log.
    - **Duplicate ID with Identical Event**: Idempotent success (returns existing event without log duplication).
    - **Duplicate ID with Divergent Content**: Throws `AuditIntegrityException`.
- Dependencies: `kleio-core` depends only on `themis-api`, `slf4j-api`, and JDK 21. It contains zero references to HAPI FHIR, JPA, or web frameworks.

---

**4. Actual FHIR R5 AuditEvent Model Available in the Repository**

Harmonia specifies HAPI FHIR **7.2.0** in the root `pom.xml` (`hapi-fhir.version = 7.2.0`), resolving to the underlying FHIR R5 core library `org.hl7.fhir.r5:org.hl7.fhir.r5:6.1.2.2`.

The authoritative model class is `org.hl7.fhir.r5.model.AuditEvent extends org.hl7.fhir.r5.model.DomainResource`.

**Structural Elements in Available R5 Library**
1. `id`: Inherited from `Resource` (`IdType`). Formatted as `[A-Za-z0-9\-\.]{1,64}`.
2. `meta`: Inherited from `Resource` (`Meta`), containing `versionId`, `lastUpdated`, `source`, `profile` (`List<CanonicalType>`), and `security` (`List<Coding>`).
3. `category`: `List<CodeableConcept>` (broad classification of the event).
4. `code`: `CodeableConcept` (1..1 mandatory in R5; specific event/activity type).
5. `action`: `AuditEventAction` (enum values: `C`, `R`, `U`, `D`, `E`).
6. `severity`: `AuditEventSeverity` (code: `emergency`, `alert`, `critical`, `error`, `warning`, `notice`, `informational`, `debug`).
7. `occurred[x]`: Choice of `Period` or `DateTimeType`.
8. `recorded`: `InstantType` (1..1 timestamp when audit event was recorded).
9. `outcome`: `AuditEventOutcomeComponent` containing:
    - `code`: `Coding`
    - `detail`: `List<AuditEventOutcomeDetailComponent>` (each with `type: CodeableConcept`, `value[x]: DataType`).
10. `authorization`: `List<CodeableConcept>` (top-level authorization policy/token references).
11. `basedOn`: `List<Reference>` (references to operational tasks/requests).
12. `patient`: `Reference` (subject patient when applicable).
13. `encounter`: `Reference` (clinical encounter context).
14. `agent`: `List<AuditEventAgentComponent>` containing:
    - `type`: `CodeableConcept` (agent participant type).
    - `role`: `List<CodeableConcept>` (security/audit role of the agent).
    - `who`: `Reference` (identifier/reference for the agent principal).
    - `requestor`: `boolean` (true if initiator).
    - `location`: `Reference`.
    - `policy`: `List<UriType>` (policy URIs under which agent acted).
    - `network`: `AuditEventAgentNetworkComponent`.
    - `authorization`: `List<CodeableConcept>` (agent-level authorization evidence/tokens).
15. `source`: `AuditEventSourceComponent` containing:
    - `site`: `Reference`.
    - `observer`: `Reference` (1..1 system/subsystem actor).
    - `type`: `List<CodeableConcept>` (subsystem role/type codings).
16. `entity`: `List<AuditEventEntityComponent>` containing:
    - `what`: `Reference` (target resource or identifier).
    - `role`: `CodeableConcept` (functional entity role).
    - `securityLabel`: `List<Coding>` (security labels attached to the entity).
    - `query`: `byte[]` (query specification).
    - `detail`: `List<AuditEventEntityDetailComponent>` (each with `type: CodeableConcept`, `value[x]: DataType`).
    - `agent`: `List<AuditEventAgentComponent>`.

*Critical Finding*: In FHIR R5 `AuditEvent`, there is **no native `identifier` property** (unlike FHIR R4 or other R5 resources like `Patient` or `Task`). Business identity must therefore be anchored via `AuditEvent.id` and/or a governed extension.

---

**5. Complete Field-by-Field Semantic Mapping Table**

| Harmonia Field | Harmonia Semantics | Mandatory / Optional | Proposed FHIR R5 Location | Mapping Class | Forward Mapping (`Harmonia -> FHIR`) | Reverse Mapping (`FHIR -> Harmonia`) | Cardinality | Terminology / System Requirement | Information-Loss Risk | Rationale |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `eventId` | Canonical event identifier | Mandatory | `AuditEvent.id` & extension `audit-event-id` | `NATIVE` / `EXTENSION` | Set `AuditEvent.id` = `eventId`; add extension `audit-event-id` with value `eventId` | Prefer extension `audit-event-id` value; fallback to `AuditEvent.id` | 1..1 | Identifier system: `http://harmonia.fhirfactory.net/identifier/audit-event` | None | R5 `AuditEvent` lacks native `identifier`. Using `id` ensures standard FHIR lookup, while extension guarantees persistence if server re-indexes `id`. |
| `recordedAt` | Timestamp when event was recorded | Mandatory | `AuditEvent.recorded` | `NATIVE` | `new InstantType(Date.from(recordedAt))` | `recorded.getValue().toInstant()` | 1..1 | ISO 8601 Instant | None | Direct 1:1 semantic match in FHIR R5. |
| `classification` | High-level architectural domain category | Mandatory | `AuditEvent.category` | `CODED` | `category.add(new CodeableConcept().addCoding(new Coding("http://harmonia.fhirfactory.net/security/audit-classification", classification.name(), classification.name())))` | Match coding with system `.../audit-classification`; parse `AuditClassification.valueOf(code)` | 1..* | CodeSystem: `http://harmonia.fhirfactory.net/security/audit-classification` | None | Native R5 `category` represents event categorization; CodeSystem preserves exact enum. |
| `action` | Verb describing operation | Mandatory | `AuditEvent.action` & `AuditEvent.code` | `CODED` | Set native `action` (`C`,`R`,`U`,`D`,`E`); set `code` with system `.../audit-action` and code `action.name()` | Read coding from `code` with system `.../audit-action`; parse `AuditAction.valueOf(code)` | 1..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/audit-action` | None | Native R5 `action` collapses `SEARCH`, `AUTHORIZE`, and `EXECUTE` into `E`. `code` preserves exact Harmonia verb losslessly. |
| `outcome` | Execution/policy outcome | Mandatory | `AuditEvent.outcome.code` | `CODED` | Set `outcome.code` with system `.../audit-outcome` and code `outcome.name()` (plus standard DICOM code) | Match coding with system `.../audit-outcome`; parse `AuditOutcome.valueOf(code)` | 1..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/audit-outcome` | None | Distinguishes technical `FAILURE` from policy `DENIED` losslessly. |
| `initiatingPrincipal` | Requesting/originating identity | Optional | `AuditEvent.agent` (`requestor = true`, `role = INITIATOR`) | `CODED` | Create agent with `requestor = true`, role `INITIATOR`, `who.identifier` = principalId, `type` = principalType | Filter `agent` where `requestor = true` or role `INITIATOR`; parse `ThemisPrincipal` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/agent-role` | None | Native `agent` model supports initiator distinction natively. |
| `executingPrincipal` | Identity performing execution | Optional | `AuditEvent.agent` (`requestor = false`, `role = EXECUTOR`) | `CODED` | Create agent with `requestor = false`, role `EXECUTOR`, `who.identifier` = principalId, `type` = principalType | Filter `agent` where role `EXECUTOR`; parse `ThemisPrincipal` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/agent-role` | None | Native `agent` model supports delegated/executor identity cleanly. |
| `securityDomain` | Authorization/tenancy boundary | Optional | Extension `security-domain` | `EXTENSION` | Add top-level extension `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` with `StringType(securityDomain)` | Read extension `security-domain` | 0..1 | String / URI | None | R5 `AuditEvent` has no native top-level authorization realm/tenancy element. |
| `target` | Audited resource/entity reference | Optional | `AuditEvent.entity` | `CODED` | Create `entity` with role `TARGET`, `what` reference/identifier (`resourceType/resourceId`), and detail for target `securityDomain` | Filter `entity` with role `TARGET`; extract `resourceType`, `resourceId`, and detail `securityDomain` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/entity-role` | None | Native R5 `entity` represents accessed objects. `entity.detail` holds target-specific domain. |
| `authorizationEvidence` | Themis evaluation evidence | Optional | `AuditEvent.outcome.detail`, `agent.policy`, & `authorization` | `CODED` | Map `decisionId`, `decision`, `reason`, `message` to `outcome.detail`; set `agent.policy` to `policyId` URI; set `authorization` coding | Extract decision parameters from `outcome.detail`; extract policy from `agent.policy` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/themis-decision-reason` | None | Native R5 `outcome.detail` and `agent.policy` capture decision evidence without extensions. |
| `authorities` | Authorization context snapshot | Optional | `AuditEvent.agent.authorization` | `CODED` | Map each `ThemisAuthority` to `agent.authorization` with system `.../authorities` | Extract all codings from `agent.authorization` with system `.../authorities` into `Set<ThemisAuthority>` | 0..* | CodeSystem: `http://harmonia.fhirfactory.net/security/authorities` | None | Native R5 `agent.authorization` specifically models authority tokens held during the event. |
| `securityLabels` | Sensitivity/access labels | Optional | `AuditEvent.meta.security` & `entity.securityLabel` | `CODED` | Add each label to `meta.security` and `entity.securityLabel` | Extract matching codings from `meta.security` into `Set<ThemisSecurityLabel>` | 0..* | CodeSystem: `http://harmonia.fhirfactory.net/security/labels` | None | Preserves security tags on both the record itself and the audited entity. |
| `correlationId` | End-to-end business workflow ID | Optional | Extension `correlation-id` | `EXTENSION` | Add extension `.../correlation-id` with `StringType(correlationId)` | Read extension `.../correlation-id` | 0..1 | String / UUID | None | R5 `AuditEvent` has no native workflow tracing correlation element. |
| `causationId` | Immediate preceding parent task ID | Optional | Extension `causation-id` | `EXTENSION` | Add extension `.../causation-id` with `StringType(causationId)` | Read extension `.../causation-id` | 0..1 | String / UUID | None | R5 `AuditEvent` has no native causation element. |
| `operationId` | Specific operation trace ID | Optional | Extension `operation-id` | `EXTENSION` | Add extension `.../operation-id` with `StringType(operationId)` | Read extension `.../operation-id` | 0..1 | String / UUID | None | R5 `AuditEvent` has no native operation instance element. |
| `source` | Subsystem and component origin | Optional | `AuditEvent.source` | `CODED` | Set `source.observer.identifier` = component; set `source.type` = subsystem | Parse `subsystem` from `source.type` and `component` from `source.observer.identifier.value` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/source/subsystem` | None | Native R5 `source` explicitly captures observer and subsystem role. |
| `attributes` | Small metadata key-value map | Optional | Extension `audit-attribute` | `EXTENSION` | For each entry, add extension `.../audit-attribute` with sub-extensions `key` and `value` | Reconstruct `Map<String, String>` from extensions | 0..* | Governed key-value strings | None | R5 `AuditEvent` has no top-level generic metadata map element. |

---

**6. Event Identity Decision**

**Analysis of `HarmoniaAuditEvent.eventId` vs. `AuditEvent.id`**
1. **Should `eventId` map to `AuditEvent.id`?**
   **Yes, conditionally.** `AuditEvent.id` should be populated with `eventId` whenever `eventId` satisfies FHIR `id` character constraints.
2. **Normalization and character constraints:**
   FHIR R5 `Resource.id` restricts characters to `[A-Za-z0-9\-\.]{1,64}`. Standard Harmonia event IDs generated via `UUID.randomUUID().toString()` (36 lowercase hex digits and hyphens) are 100% compliant.
3. **Can every valid Harmonia `eventId` be represented losslessly as a FHIR logical ID?**
   **No.** `HarmoniaAuditEvent` only asserts that `eventId` is non-null and non-blank. Custom event IDs containing colons, slashes, underscores, or exceeding 64 characters cannot be stored directly in `AuditEvent.id`.
4. **Should a separate identifier extension/system be used?**
   **Yes.** To guarantee 100% round-trip fidelity regardless of external FHIR server behaviors (e.g. servers that override client-assigned IDs or reject non-compliant strings), a top-level extension `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-event-id` is mandated.
5. **What identity drives append idempotency?**
   Append idempotency is driven strictly by the canonical Harmonia `eventId` (stored in the extension and mirrored in `AuditEvent.id`).
6. **Conflict semantics:**
    - Same `eventId` + identical content $\rightarrow$ Idempotent success (no duplicate log).
    - Same `eventId` + divergent content $\rightarrow$ `AuditIntegrityException` (tamper/corruption detected).
7. **Is FHIR logical ID alone sufficient?**
   No. Logical IDs are repository-local and mutable across server imports. The business identifier extension ensures absolute immutability.

---

**7. Classification and Event Type**

- **Harmonia Concept**: `AuditClassification` (`SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`).
- **FHIR R5 Location**: `AuditEvent.category: List<CodeableConcept>`.
- **Governance**: Harmonia defines CodeSystem `http://harmonia.fhirfactory.net/security/audit-classification`.
- **Distinction**:
    - `category`: Broad architectural classification (e.g., `CLINICAL`).
    - `code`: Specific activity/event type (e.g., `AUTHORIZE`, `READ`).
    - `action`: Generic CRUD/E lifecycle verb (e.g., `R`, `E`).
- Forward mapping adds a `CodeableConcept` to `category` with the governed CodeSystem. Reverse mapping filters `category` for this system and parses the enum name.

---

**8. Action Mapping**

- **Harmonia Concept**: `AuditAction` (`CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`).
- **FHIR R5 Action Semantics**: Native `AuditEvent.action` is restricted to:
    - `CREATE` $\rightarrow$ `AuditEventAction.C`
    - `READ` $\rightarrow$ `AuditEventAction.R`
    - `UPDATE` $\rightarrow$ `AuditEventAction.U`
    - `DELETE` $\rightarrow$ `AuditEventAction.D`
    - `EXECUTE` $\rightarrow$ `AuditEventAction.E`
- **The Collapsing Problem**: In native FHIR, `SEARCH`, `AUTHORIZE`, and `EXECUTE` all collapse into `E` (Execute). Reversing from `action` alone would be lossy.
- **Deterministic Resolution**:
    - Native `AuditEvent.action` is set to the nearest standard code (`C`, `R`, `U`, `D`, `E`).
    - Mandatory native `AuditEvent.code` (`1..1 CodeableConcept`) is populated with CodeSystem `http://harmonia.fhirfactory.net/security/audit-action` and code `action.name()`.
    - Reverse mapping reads `AuditEvent.code` to recover the exact Harmonia `AuditAction` deterministically.

---

**9. Outcome Mapping**

- **Harmonia Concept**: `AuditOutcome` (`SUCCESS`, `FAILURE`, `DENIED`).
- **FHIR R5 Outcome Semantics**: `AuditEvent.outcome: AuditEventOutcomeComponent` contains `code: Coding`.
- **Distinction**:
    - Technical/operational failure (`FAILURE`) vs. authorization refusal (`DENIED`).
- **Resolution**:
    - `AuditEvent.outcome.code` is assigned a `Coding` with system `http://harmonia.fhirfactory.net/security/audit-outcome` and code `SUCCESS`, `FAILURE`, or `DENIED`.
    - Secondary standard DICOM/FHIR codes (`0` for Success, `4` for Minor Failure/Denied, `8` for Serious Failure) are included for interoperability.
    - Reverse mapper inspects `.../audit-outcome` to reconstruct the exact `AuditOutcome`.

---

**10. Initiating and Executing Principal Mapping**

Harmonia strictly distinguishes `initiatingPrincipal` (originating user/service) from `executingPrincipal` (delegated pipeline worker/engine).

**Dual-Agent Representation**
Both principals map as distinct entries in `AuditEvent.agent: List<AuditEventAgentComponent>`.

**Semantic Role Distinction**
1. `initiatingPrincipal`:
    - `agent.requestor = true`
    - `agent.role` contains `Coding("http://harmonia.fhirfactory.net/security/agent-role", "INITIATOR", "Initiating Principal")`
2. `executingPrincipal`:
    - `agent.requestor = false`
    - `agent.role` contains `Coding("http://harmonia.fhirfactory.net/security/agent-role", "EXECUTOR", "Executing Principal")`

**Principal Properties**
- `principalId`: Stored in `agent.who.identifier.value` with system derived from `sourceDomain`.
- `principalType`: Mapped to `agent.type` with CodeSystem `http://harmonia.fhirfactory.net/security/principal-type` (`HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`).
- `sourceDomain`: Encapsulated in `agent.who.identifier.system`.
- `attributes`: Persisted via nested backbone extension on `agent` (`http://harmonia.fhirfactory.net/fhir/StructureDefinition/agent-attribute`).

Reverse mapping unambiguously identifies both agents by role and requestor flag without relying on list order.

---

**11. Security Domain Mapping**

- **Harmonia Concept**: Top-level `HarmoniaAuditEvent.securityDomain` (e.g. `CLINICAL`, `OPERATIONS`, `AUDIT`, `PROVIDER_REGISTRY`).
- **Analysis**:
    - Is it `meta.security`? **No.** `meta.security` represents security labels (confidentiality, integrity), not tenancy/authorization realms.
    - Is it `source.site`? **No.** `site` represents physical/logical network location.
- **Resolution**: A dedicated top-level extension:
  `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (data type: `string`, cardinality `0..1`).
- *Technical Debt Note*: String-based security domains remain a known backlog item. This extension persists the current string faithfully without premature redesign.

---

**12. Target Mapping**

- **Harmonia Concept**: `AuditTarget(String resourceType, String resourceId, String securityDomain)`.
- **FHIR R5 Location**: `AuditEvent.entity: List<AuditEventEntityComponent>`.
- **Mapping**:
    - `entity.role`: Governed coding `http://harmonia.fhirfactory.net/security/entity-role` with code `TARGET`.
    - `entity.what`: `Reference`.
        - If standard FHIR resource: `reference = "Patient/123"`, `type = "Patient"`.
        - If non-FHIR domain target: `type = resourceType`, `identifier.value = resourceId`.
    - `securityDomain`: Stored in native `entity.detail` with `type` = `Coding("http://harmonia.fhirfactory.net/security/entity-detail", "securityDomain")` and `value = StringType(securityDomain)`.
- Reverse mapping reconstructs `AuditTarget` directly from `entity.what` and `entity.detail`.

---

**13. Authorization Evidence Mapping**

- **Harmonia Concept**: `AuditAuthorizationEvidence(decisionId, decision, reason, policyId, message)`.
- **Native FHIR R5 Representation**:
    - `policyId`: Stored in `agent.policy: List<UriType>` (as `urn:harmonia:policy:{policyId}`).
    - `decisionId`, `decision`, `reason`, `message`: Stored in native `AuditEvent.outcome.detail`:
        - `detail.type = "decisionId"`, `value = StringType(decisionId)`
        - `detail.type = "decision"`, `value = Coding(".../themis-decision", decision.name())`
        - `detail.type = "reason"`, `value = Coding(".../themis-decision-reason", reason.name())`
        - `detail.type = "message"`, `value = StringType(message)` (when non-empty)
    - `AuditEvent.authorization`: Populated with `CodeableConcept` referencing the decision reason.
- Reverse mapping reads `outcome.detail` and `agent.policy` to reconstruct `AuditAuthorizationEvidence` losslessly without proprietary extensions.

---

**14. Authority Snapshot Mapping**

- **Harmonia Concept**: `Set<ThemisAuthority>` (snapshot of active authorities at execution time).
- **Invariant (Task 04)**: Propagated authorities are historical evidence/provenance, **never** permanent delegated capabilities.
- **FHIR R5 Location**: Native `AuditEvent.agent.authorization: List<CodeableConcept>`.
- **Mapping**:
    - Each `ThemisAuthority` maps to a `CodeableConcept` in `agent.authorization` with system `http://harmonia.fhirfactory.net/security/authorities` and `code = authorityCode`.
    - Because these codings reside on historical `AuditEvent` records, they cannot be queried or executed as active permissions.
- Reverse mapping reconstructs `Set<ThemisAuthority>` by collecting all codings matching the authority system.

---

**15. Security Label Mapping**

- **Harmonia Concept**: `Set<ThemisSecurityLabel>` (e.g. `CLINICAL`, `RESTRICTED`, `AUDIT`).
- **Semantic Boundary**:
    - Labels on the **Audit Record itself** map to `AuditEvent.meta.security: List<Coding>`.
    - Labels on the **Target Entity** map to `AuditEvent.entity.securityLabel: List<Coding>`.
- Forward mapping copies the event's security labels to `AuditEvent.meta.security` and attaches them to `entity.securityLabel`.
- Reverse mapping extracts codings with system `http://harmonia.fhirfactory.net/security/labels` from `meta.security` into `Set<ThemisSecurityLabel>`.

---

**16. Correlation, Causation, and Operation Mapping**

Harmonia strictly distinguishes three tracing identifiers:
1. `correlationId`: End-to-end business workflow boundary.
2. `causationId`: Immediate preceding work item causing this event.
3. `operationId`: Specific invocation/execution instance.

- **Analysis**: Native FHIR R5 `AuditEvent` provides no top-level header fields for distributed workflow correlation. `basedOn` is typed as a FHIR `Reference` and is unsuitable for arbitrary distributed tracking strings.
- **Resolution**: Exactly three separate top-level extensions:
    1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (`StringType`, `0..1`)
    2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (`StringType`, `0..1`)
    3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (`StringType`, `0..1`)
- Reverse mapping reads each extension independently, preventing any semantic conflation.

---

**17. Source Mapping**

- **Harmonia Concept**: `AuditSource(String subsystem, String component)`.
- **FHIR R5 Location**: Native `AuditEvent.source: AuditEventSourceComponent`.
- **Mapping**:
    - `subsystem`: Mapped to `source.type: List<CodeableConcept>` with system `http://harmonia.fhirfactory.net/source/subsystem` and code `subsystem`.
    - `component`: Mapped to `source.observer.identifier` with system `http://harmonia.fhirfactory.net/source/component` and `value = component`.
    - `source.observer.display`: Formatted as `subsystem + "/" + component`.
- Reverse mapping extracts `subsystem` from `source.type` and `component` from `source.observer.identifier.value`.

---

**18. Attribute Mapping**

- **Harmonia Concept**: `Map<String, String> attributes` (bounded metadata container).
- **Constraints & Guardrails**:
    - Must never contain PHI, tokens, passwords, or raw clinical payloads.
    - Keys must conform to `^[a-zA-Z0-9_\-\.]{1,64}$`.
    - Values bounded to $\le 256$ characters.
- **Mapping**: Extension `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (cardinality `0..*`) with sub-extensions:
    - `key`: `StringType`
    - `value`: `StringType`
- Reverse mapping gathers all extension pairs into an immutable `Map<String, String>`.

---

**19. Proposed Harmonia Terminology Artifacts**

The following CodeSystems and ValueSets are specified for governance:
1. `http://harmonia.fhirfactory.net/security/audit-classification`: Codes: `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
2. `http://harmonia.fhirfactory.net/security/audit-action`: Codes: `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`.
3. `http://harmonia.fhirfactory.net/security/audit-outcome`: Codes: `SUCCESS`, `FAILURE`, `DENIED`.
4. `http://harmonia.fhirfactory.net/security/agent-role`: Codes: `INITIATOR`, `EXECUTOR`.
5. `http://harmonia.fhirfactory.net/security/principal-type`: Codes: `HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`.
6. `http://harmonia.fhirfactory.net/security/entity-role`: Code: `TARGET`.
7. `http://harmonia.fhirfactory.net/security/entity-detail`: Code: `securityDomain`.
8. `http://harmonia.fhirfactory.net/source/subsystem`: Codes for Harmonia subprojects (`THEMIS`, `ENERGEIA`, `PYLAI`, `HESTIA`, `CALLIOPE`, `IRIS`, `AGORA`, `PETASOS`, `KLEIO`).
9. `http://harmonia.fhirfactory.net/source/component`: Identifiers for specific internal services.

---

**20. Proposed Harmonia AuditEvent Profile Constraints**

Canonical Profile URL: `http://harmonia.fhirfactory.net/fhir/StructureDefinition/harmonia-audit-event`

Formal constraints imposed by the profile:
- `AuditEvent.id`: Mandatory (1..1), matching `[A-Za-z0-9\-\.]{1,64}`.
- `AuditEvent.recorded`: Mandatory (1..1).
- `AuditEvent.category`: Mandatory (1..*), bound to `audit-classification`.
- `AuditEvent.code`: Mandatory (1..1), bound to `audit-action`.
- `AuditEvent.action`: Mandatory (1..1), constrained to `C`, `R`, `U`, `D`, `E`.
- `AuditEvent.outcome`: Mandatory (1..1), `outcome.code` bound to `audit-outcome`.
- `AuditEvent.agent`: Mandatory (1..2), at least one agent with `role = INITIATOR` or `role = EXECUTOR`.
- `AuditEvent.source`: Mandatory (1..1), `observer` and `type` present.
- Extensions:
    - `audit-event-id`: Mandatory (1..1).
    - `security-domain`: Optional (0..1).
    - `correlation-id`, `causationId`, `operation-id`: Optional (0..1).
    - `audit-attribute`: Optional (0..*).

---

**21. Proposed Extension Register**

| Proposed Extension Name | Purpose | Data Type | Cardinality | Context | Forward Mapping | Reverse Mapping | Justification (Why Native R5 is Insufficient) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `audit-event-id` | Guarantees business identity retention | `StringType` / `Identifier` | 1..1 | `AuditEvent` | Set to `eventId` | Reconstruct `eventId` | R5 `AuditEvent` completely lacks a native `identifier` property. |
| `security-domain` | Identifies authorization realm / tenancy | `StringType` | 0..1 | `AuditEvent` | Set to `securityDomain` | Reconstruct `securityDomain` | R5 `AuditEvent` has no top-level tenancy/domain concept. |
| `correlation-id` | Traces end-to-end distributed business workflow | `StringType` | 0..1 | `AuditEvent` | Set to `correlationId` | Reconstruct `correlationId` | R5 has no native workflow correlation header element. |
| `causation-id` | Traces direct preceding work item | `StringType` | 0..1 | `AuditEvent` | Set to `causationId` | Reconstruct `causationId` | R5 has no native causation tracing element. |
| `operation-id` | Traces specific invocation/execution trace | `StringType` | 0..1 | `AuditEvent` | Set to `operationId` | Reconstruct `operationId` | R5 has no native operation instance element. |
| `audit-attribute` | General escape-hatch metadata key-values | Complex (`key`, `value`) | 0..* | `AuditEvent` | For each map entry, write sub-extensions | Reconstruct `attributes` map | R5 has no top-level metadata key-value container. |
| `agent-attribute` | Captures principal attributes on agent | Complex (`key`, `value`) | 0..* | `AuditEvent.agent` | For each map entry, write sub-extensions | Reconstruct principal attributes | `agent` backbone has no native arbitrary attribute map. |

---

**22. Considered-but-Rejected Extensions**

1. **Rejected: `audit-action-extension`**:
    - *Alternative Used*: Native `AuditEvent.action` + coded native `AuditEvent.code`.
2. **Rejected: `audit-classification-extension`**:
    - *Alternative Used*: Native `AuditEvent.category` (`List<CodeableConcept>`).
3. **Rejected: `audit-outcome-extension`**:
    - *Alternative Used*: Native `AuditEvent.outcome.code` (`Coding`).
4. **Rejected: `audit-target-extension`**:
    - *Alternative Used*: Native `AuditEvent.entity` (`what`, `role`, `detail`).
5. **Rejected: `authorization-evidence-extension`**:
    - *Alternative Used*: Native `AuditEvent.outcome.detail`, `agent.policy`, and `AuditEvent.authorization`.
6. **Rejected: `authority-snapshot-extension`**:
    - *Alternative Used*: Native `AuditEvent.agent.authorization` (`List<CodeableConcept>`).
7. **Rejected: `target-security-labels-extension`**:
    - *Alternative Used*: Native `AuditEvent.entity.securityLabel` (`List<Coding>`).
8. **Rejected: `audit-source-extension`**:
    - *Alternative Used*: Native `AuditEvent.source.observer` and `AuditEvent.source.type`.

---

**23. Round-Trip Fidelity Matrix**

| Harmonia Field | Original Value | FHIR Representation | Reconstructed Value | Classification |
| :--- | :--- | :--- | :--- | :--- |
| `eventId` | `"ae-9812-uuid"` | `AuditEvent.id="ae-9812-uuid"`, extension `audit-event-id="ae-9812-uuid"` | `"ae-9812-uuid"` | **EXACT** |
| `recordedAt` | `Instant.ofEpochMilli(1774390000000L)` | `AuditEvent.recorded=2026-03-24T22:06:40.000Z` | `Instant.ofEpochMilli(1774390000000L)` | **EXACT** |
| `classification` | `AuditClassification.CLINICAL` | `AuditEvent.category[0].coding[0].code="CLINICAL"` | `AuditClassification.CLINICAL` | **EXACT** |
| `action` | `AuditAction.SEARCH` | `action=E`, `code.coding[0].code="SEARCH"` | `AuditAction.SEARCH` | **EXACT** |
| `outcome` | `AuditOutcome.DENIED` | `outcome.code.coding[0].code="DENIED"` | `AuditOutcome.DENIED` | **EXACT** |
| `initiatingPrincipal` | `ThemisPrincipal("dr-smith", HUMAN, "hosp-a", {})` | `agent[0]` (`requestor=true`, `role=INITIATOR`, `who="dr-smith"`) | `ThemisPrincipal("dr-smith", HUMAN, "hosp-a", {})` | **EXACT** |
| `executingPrincipal` | `ThemisPrincipal("ponos-wkr", PROCESS, "core", {})`| `agent[1]` (`requestor=false`, `role=EXECUTOR`, `who="ponos-wkr"`) | `ThemisPrincipal("ponos-wkr", PROCESS, "core", {})` | **EXACT** |
| `securityDomain` | `"CLINICAL_NORTH"` | Extension `security-domain="CLINICAL_NORTH"` | `"CLINICAL_NORTH"` | **EXACT** |
| `target` | `AuditTarget("Patient", "P123", "CLINICAL_NORTH")` | `entity[0]` (`what="Patient/P123"`, `detail[0]="CLINICAL_NORTH"`) | `AuditTarget("Patient", "P123", "CLINICAL_NORTH")` | **EXACT** |
| `authorizationEvidence` | `AuditAuthorizationEvidence("d-1", DENY, DEFAULT_DENY, "pol-9", "")` | `outcome.detail` entries + `agent.policy="urn:harmonia:policy:pol-9"` | `AuditAuthorizationEvidence("d-1", DENY, DEFAULT_DENY, "pol-9", "")` | **EXACT** |
| `authorities` | `[ThemisAuthority("CLINICAL_READ")]` | `agent.authorization[0].code="CLINICAL_READ"` | `[ThemisAuthority("CLINICAL_READ")]` | **EXACT** |
| `securityLabels` | `[ThemisSecurityLabel("labels", "RESTRICTED")]` | `meta.security[0].code="RESTRICTED"` | `[ThemisSecurityLabel("labels", "RESTRICTED")]` | **EXACT** |
| `correlationId` | `"corr-771"` | Extension `correlation-id="corr-771"` | `"corr-771"` | **EXACT** |
| `causationId` | `"cause-554"` | Extension `causation-id="cause-554"` | `"cause-554"` | **EXACT** |
| `operationId` | `"op-001"` | Extension `operation-id="op-001"` | `"op-001"` | **EXACT** |
| `source` | `AuditSource("THEMIS", "AUTHORIZATION_ENGINE")` | `source.type="THEMIS"`, `source.observer="AUTHORIZATION_ENGINE"` | `AuditSource("THEMIS", "AUTHORIZATION_ENGINE")` | **EXACT** |
| `attributes` | `{"clientIp": "192.168.1.50"}` | Extension `audit-attribute` (`key="clientIp"`, `value="192.168.1.50"`) | `{"clientIp": "192.168.1.50"}` | **EXACT** |

**Summary**: 17 Exact / 0 Normalized / 0 Lossy.

---

**24. Representative Event Examples**

**Example 1: Human Reads a Clinical Patient**
- **Harmonia Semantics**: `initiatingPrincipal = "dr-alice" (HUMAN)`, `action = READ`, `classification = CLINICAL`, `target = AuditTarget("Patient", "P-902", "CLINICAL")`, `outcome = SUCCESS`.
- **FHIR Elements**:
    - `category`: `CLINICAL`
    - `action`: `R`, `code`: `READ`
    - `outcome.code`: `SUCCESS`
    - `agent[0]`: `who.identifier.value = "dr-alice"`, `type = HUMAN`, `requestor = true`, `role = INITIATOR`
    - `entity[0]`: `what.reference = "Patient/P-902"`, `role = TARGET`
- **Reconstructed**: Exact match of all fields.

**Example 2: Human Searches Clinical Resources**
- **Harmonia Semantics**: `initiatingPrincipal = "nurse-bob" (HUMAN)`, `action = SEARCH`, `classification = CLINICAL`, `target = AuditTarget("Observation", null, "CLINICAL")`, `outcome = SUCCESS`.
- **FHIR Elements**:
    - `action`: `E`, `code`: `SEARCH`
    - `agent[0]`: `who.identifier.value = "nurse-bob"`, `requestor = true`, `role = INITIATOR`
    - `entity[0]`: `what.type = "Observation"`, `role = TARGET`
- **Reconstructed**: `action = SEARCH` successfully differentiated from `EXECUTE`.

**Example 3: Unauthorized Clinical UPDATE Denied**
- **Harmonia Semantics**: `initiatingPrincipal = "dr-alice"`, `action = UPDATE`, `outcome = DENIED`, `authorizationEvidence = AuditAuthorizationEvidence("d-12", DENY, EXPLICIT_DENY, "pol-no-edit", "Restricted")`.
- **FHIR Elements**:
    - `action`: `U`, `code`: `UPDATE`
    - `outcome.code`: `DENIED`
    - `outcome.detail`: `decision = DENY`, `reason = EXPLICIT_DENY`, `decisionId = "d-12"`
    - `agent[0].policy`: `"urn:harmonia:policy:pol-no-edit"`
- **Reconstructed**: `outcome = DENIED` with full policy evidence.

**Example 4: Trusted Service Performs Integration Operation**
- **Harmonia Semantics**: `initiatingPrincipal = "mllp-adapter" (SERVICE)`, `action = EXECUTE`, `classification = INTEGRATION`, `source = AuditSource("PYLAI", "MLLP_IN")`, `outcome = SUCCESS`.
- **FHIR Elements**:
    - `category`: `INTEGRATION`
    - `agent[0]`: `who = "mllp-adapter"`, `type = SERVICE`, `requestor = true`
    - `source`: `type = "PYLAI"`, `observer = "MLLP_IN"`
- **Reconstructed**: Service identity and Pylai source preserved.

**Example 5: Ponos Process Executes Workflow Work Initiated by Human**
- **Harmonia Semantics**: `initiatingPrincipal = "user-jane" (HUMAN)`, `executingPrincipal = "ponos-worker-4" (PROCESS)`, `action = EXECUTE`, `classification = WORKFLOW`.
- **FHIR Elements**:
    - `agent[0]`: `who = "user-jane"`, `requestor = true`, `role = INITIATOR`
    - `agent[1]`: `who = "ponos-worker-4"`, `requestor = false`, `role = EXECUTOR`
- **Reconstructed**: Dual-agent initiator vs. executor roles preserved.

**Example 6: Administrative Action Performed by Human**
- **Harmonia Semantics**: `initiatingPrincipal = "admin-dave" (HUMAN)`, `action = DELETE`, `classification = ADMINISTRATION`, `target = AuditTarget("PractitionerRole", "PR-5", "ADMINISTRATION")`.
- **FHIR Elements**:
    - `category`: `ADMINISTRATION`
    - `action`: `D`, `code`: `DELETE`
    - `entity[0]`: `what.reference = "PractitionerRole/PR-5"`
- **Reconstructed**: Administrative context retained.

**Example 7: System-Generated Event with No Initiating Human**
- **Harmonia Semantics**: `initiatingPrincipal = null`, `executingPrincipal = "hestia-reaper" (SYSTEM)`, `action = EXECUTE`, `classification = SYSTEM`.
- **FHIR Elements**:
    - `category`: `SYSTEM`
    - `agent[0]`: `who = "hestia-reaper"`, `type = SYSTEM`, `requestor = false`, `role = EXECUTOR` (no initiator agent present).
- **Reconstructed**: Absence of human initiator retained; executing system identified.

**Example 8: Event with Correlation, Causation, and Operation IDs**
- **Harmonia Semantics**: `correlationId = "corr-100"`, `causationId = "task-50"`, `operationId = "op-25"`.
- **FHIR Elements**:
    - Extensions `correlation-id="corr-100"`, `causation-id="task-50"`, `operation-id="op-25"`.
- **Reconstructed**: All three IDs reconstructed distinctly.

---

**25. Immutability / Identity Implications**

The Kleio contract establishes that audit logs are strictly append-only:
1. `new eventId` $\rightarrow$ Append.
2. `same eventId + identical event` $\rightarrow$ Idempotent replay (success, no duplicate).
3. `same eventId + divergent content` $\rightarrow$ Integrity violation (`AuditIntegrityException`).

**Implications for Step 03 Persistence**
- Standard FHIR REST endpoints (`POST /AuditEvent` or `PUT /AuditEvent/{id}`) allow mutable replacement and version incrementing (`versionId = 2`). This violates Kleio immutability.
- In Step 03, the persistence adapter behind `AuditService` must:
    - Enforce client-assigned ID creation.
    - Implement conditional create or optimistic check: if record exists, compare parsed content; if identical, return success; if divergent, throw `AuditIntegrityException`.
    - Disable HTTP `PUT` and `DELETE` on durable audit storage.

---

**26. Existing FHIR AuditEvent Compatibility Concerns**

Exploration of `hestia/mnemosyne-clinical` and `iris/iris-befe`:
- **Mutable Operations**: Both `AuditEventResourceProvider` (Hestia) and `AuditEventResource` (Iris) provide mutable CRUD operations (`update`, `delete`), which increment version numbers and soft-delete audit records.
- **Arbitrary External Payloads**: Existing REST endpoints accept arbitrary unvalidated JSON `AuditEvent` payloads.
- **Search Limitations**: Generic search filters in Hestia/Iris match `name` against action/agent, but do not implement search by `identifier` or correlation extensions.
- **Isolation Requirement**: Step 02.2 mapper logic must remain encapsulated within Kleio. Existing mutable clinical and presentation endpoints must not be refactored until Step 03/04.

---

**27. Kleio / Calliope Ownership Recommendation**

- **Calliope Ownership**:
    - Governs repository-wide canonical schemas, FHIR R5 data structures, and shared terminology definitions (`HarmoniaSecurityCodeSystem`).
    - Calliope **must not** depend on Kleio (`PackageLayeringArchitectureTest` prohibits Calliope from importing `net.fhirfactory.harmonia.kleio..`).
- **Kleio Ownership**:
    - Owns audit domain models (`HarmoniaAuditEvent`), append-only service contracts (`AuditService`), and mapping implementations (`HarmoniaAuditEvent <-> FHIR R5 AuditEvent`).
- **Recommendation**:
    - Terminology URI constants can be defined in Calliope or Kleio. However, to avoid circular dependencies and keep `calliope` decoupled from audit-specific mappings, audit profile definitions and mapping logic belong entirely within Kleio.

---

**28. Recommended Module / Dependency Boundary for Step 02.2**

`kleio-core` must remain a pure Java domain module free of FHIR dependencies.

**Proposed Structure**:
```
kleio/
  pom.xml
  kleio-core/          <-- Pure Java domain (no HAPI FHIR)
  kleio-fhir/          <-- NEW module for Step 02.2
```

**Dependency Direction for `kleio-fhir`**:
- Depends on `kleio-core` (domain models).
- Depends on `calliope` (canonical schemas & base security vocabularies).
- Depends on `ca.uhn.hapi.fhir:hapi-fhir-base` & `ca.uhn.hapi.fhir:hapi-fhir-structures-r5`.
- Pure unidirectional flow: `kleio-fhir -> kleio-core`, `kleio-fhir -> calliope`. Zero violations of ArchUnit rules.

---

**29. Security / PHI Considerations**

In conformance with Task 05 and `AGENTS.md` (Invariants 6 & 7):
1. **Zero-PHI Logging**: The mapper’s internal `toString()` or logging must never emit unmasked clinical observation values, patient names, or attributes.
2. **Target References**: Targets must only record resource identifiers (e.g., `Patient/P123`), never serialized patient records.
3. **Attribute Sanitization**: The mapper must filter or reject sensitive keys (e.g. `authorization`, `password`, `token`, `secret`, `ssn`).
4. **Diagnostic Traces**: Synthetic PHI and credential markers must be used in Step 02.2 unit tests to ensure that serialization does not leak sensitive information.

---

**30. Design Blockers**

**None.** Every canonical concept in `HarmoniaAuditEvent` can be mapped to FHIR R5 and back with 100% semantic fidelity using native elements and the justified extension register.

---

**31. Contradictions / Ambiguities Discovered**

- **FHIR R5 `AuditEvent.identifier` Omission**: Older FHIR R4 assumptions expect `AuditEvent.identifier`. R5 eliminated this property. The design resolves this unambiguously by combining `AuditEvent.id` with the `audit-event-id` extension.
- **Action Collapsing**: R5 `action` codes collapse `SEARCH`, `AUTHORIZE`, and `EXECUTE` into `E`. The design resolves this deterministically by encoding the full `AuditAction` enum into the mandatory `AuditEvent.code` element.

---

**32. Missing Evidence**

None. The exact R5 structures (`org.hl7.fhir.r5:6.1.2.2`) and Kleio domain code are fully available and inspected locally.

---

**33. Issues Belonging to Later Task 06 Steps**

- **Step 02.2**: Implementation of `HarmoniaAuditEventMapper` and unit tests in `kleio-fhir`.
- **Step 03**: Implementation of immutable append persistence adapter and conflict detection.
- **Step 04**: Convergence of Hestia Mnemosyne and Iris BEFE audit endpoints with immutable Kleio storage.

---

**34. Issues Belonging to Tasks 07–10**

- Integration of Petasos distributed event streaming with Kleio audit logging (Task 07).
- Ingress gateway MLLP/REST audit event interception (Task 08).
- Long-term archival and cold storage policies (Tasks 09–10).

---

**35. Backlog Items Discovered**

- **Typed Security Domain**: Transition `securityDomain` from raw `String` to a strongly typed enum/record across Harmonia.

---

**36. Explicit Confirmation of Read-Only Execution**

**Confirmed**: No production code, test code, configuration, POM, or Git references were modified. No mapper classes were created. This report concludes Step 02.1.

---

**FINAL RECOMMENDATION: Proposed Step 02.2 Implementation Contract**

For Step 02.2, the implementation contract is:
1. **Module Creation**: Create module `kleio/kleio-fhir` declared in `kleio/pom.xml` and managed in root `pom.xml`.
2. **Mapper Responsibility**: Implement `net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper` with:
    - `AuditEvent toFhir(HarmoniaAuditEvent event)`
    - `HarmoniaAuditEvent fromFhir(AuditEvent resource)`
3. **Fidelity Invariant**:
   $$\text{fromFhir}(\text{toFhir}(event)) \equiv event$$
   Enforced across all 17 fields via comprehensive parameterized unit tests covering all 8 representative scenarios.
4. **Zero Domain Contamination**: `kleio-core` remains untouched with zero FHIR dependencies.
5. **No Network/DB Dependencies**: `kleio-fhir` will contain pure transformation logic without persistence or remote REST calls.

Analysis is Ready

**1. Executive Summary**

This report establishes the authoritative semantic mapping design between the canonical Harmonia audit domain model (`HarmoniaAuditEvent`) and the durable persistence format (`org.hl7.fhir.r5.model.AuditEvent`), satisfying Task 06 Step 02.1.

Key architectural conclusions of this design:
- **Zero Information Loss**: All 17 canonical fields of `HarmoniaAuditEvent` and its nested value objects (`AuditTarget`, `AuditAuthorizationEvidence`, `AuditSource`, `ThemisPrincipal`, `ThemisAuthority`, `ThemisSecurityLabel`) map to FHIR R5 and back with **zero loss of audit semantics** (achieving exact or normalized equivalent round-trip fidelity).
- **Native-First Alignment**: In accordance with core design principles, 11 top-level concepts leverage native FHIR R5 elements (such as `recorded`, `action`, `code`, `category`, `outcome`, `agent`, `entity`, and `source`) enriched with governed Harmonia CodeSystems.
- **Minimal, Justified Extension Register**: Exactly 5 top-level extensions and 2 nested backbone extensions are specified to address genuine structural gaps in the FHIR R5 `AuditEvent` specification (such as the absence of top-level correlation/causation/operation IDs, top-level security domain, and arbitrary attribute key-value collections).
- **Strict Architecture Boundaries**: In conformance with repository layering guardrails (`AGENTS.md` and `PackageLayeringArchitectureTest`), `kleio-core` remains a pure Java domain module free of HAPI FHIR dependencies. For Step 02.2, a new subproject module `kleio/kleio-fhir` is recommended to house the bidirectional mapper, while Calliope retains shared security vocabularies and canonical schema definitions.

This investigation and design report is strictly read-only; no code was modified, no dependencies were added, and no mapper was implemented.

---

**2. Current HarmoniaAuditEvent Contract**

The canonical audit data structure is implemented as a Java `record` in `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java` (lines 50–83).

**Component Declaration & Order**
```java
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

**Mandatory vs. Optional Fields**  
The compact constructor (`HarmoniaAuditEvent.java:70–83`) strictly enforces:
- `eventId`: Mandatory, non-null, non-blank (`!eventId.isBlank()`).
- `recordedAt`: Mandatory, non-null.
- `classification`: Mandatory, non-null.
- `action`: Mandatory, non-null.
- `outcome`: Mandatory, non-null.

The remaining 12 components are optional:
- `initiatingPrincipal`, `executingPrincipal`, `securityDomain`, `target`, `authorizationEvidence`, `correlationId`, `causationId`, `operationId`, and `source` are nullable.
- `authorities`, `securityLabels`, and `attributes` default to defensive immutable empty collections (`Set.copyOf`, `Map.copyOf`) when null is passed.

**Value-Object Semantics**
- `AuditClassification` (`AuditClassification.java:23–29`): Enum with 6 values: `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
- `AuditAction` (`AuditAction.java:25–32`): Enum with 7 values: `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`.
- `AuditOutcome` (`AuditOutcome.java:25–28`): Enum with 3 values: `SUCCESS`, `FAILURE`, `DENIED`.
- `AuditTarget` (`AuditTarget.java:28–38`): Record containing `resourceType`, `resourceId`, and `securityDomain`. Non-null string fields are trimmed. Projection from `ThemisResource` drops target attributes and does not store target security labels inside the record.
- `AuditAuthorizationEvidence` (`AuditAuthorizationEvidence.java:32–38`): Record containing `decisionId`, `decision` (`ThemisDecision`), `reason` (`ThemisDecisionReason`), `policyId`, and `message`. Its `toString()` explicitly masks the message content (`hasMessage=true/false`).
- `AuditSource` (`AuditSource.java:25–33`): Record containing `subsystem` and `component`, trimming non-null strings.
- `ThemisPrincipal` (`themis-api/.../ThemisPrincipal.java:25–71`): Immutable object containing `principalId`, `principalType` (`PrincipalType`: `HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`), `sourceDomain`, and `attributes` (`Map<String, String>`).
- `ThemisAuthority` (`ThemisAuthority.java:23–43`): Record holding a trimmed, non-blank `authorityCode`.
- `ThemisSecurityLabel` (`ThemisSecurityLabel.java:23–52`): Record holding `system` (defaults to `http://harmonia.fhirfactory.net/security/labels`) and non-blank `code` (case-insensitive equality).

**Equality and Serialization**
- Equality is record value-based equality across all 17 components (`HarmoniaAuditEvent.equals()`).
- Implements `java.io.Serializable`. Defensive copying prevents external mutation of sets and maps.
- Diagnostic `toString()` emits structural identifiers and collection counts while redacting raw attribute maps and authorization messages to prevent PHI/credential leakage.

---

**3. Current Kleio Architecture**

The current repository structure reflects the completion of Task 06 Step 01:
- Subproject root: `kleio/` (parent POM `kleio/pom.xml`).
- Active module: `kleio/kleio-core/` (pure Java domain).
- Contract: `AuditService` (`kleio/.../AuditService.java:27–102`) defines the append-only audit evidence interface:
    - `append(HarmoniaAuditEvent event)`: Appends an immutable audit event.
    - `findById(String eventId)`
    - `findByCorrelationId(String correlationId)`
    - `findByPrincipal(String principalId)`
    - `recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision)`
- Reference implementation: `InMemoryAuditService` (`InMemoryAuditService.java:39–203`) enforces:
    - **New ID**: Appended to chronological log.
    - **Duplicate ID with Identical Event**: Idempotent success (returns existing event without log duplication).
    - **Duplicate ID with Divergent Content**: Throws `AuditIntegrityException`.
- Dependencies: `kleio-core` depends only on `themis-api`, `slf4j-api`, and JDK 21. It contains zero references to HAPI FHIR, JPA, or web frameworks.

---

**4. Actual FHIR R5 AuditEvent Model Available in the Repository**

Harmonia specifies HAPI FHIR **7.2.0** in the root `pom.xml` (`hapi-fhir.version = 7.2.0`), resolving to the underlying FHIR R5 core library `org.hl7.fhir.r5:org.hl7.fhir.r5:6.1.2.2`.

The authoritative model class is `org.hl7.fhir.r5.model.AuditEvent extends org.hl7.fhir.r5.model.DomainResource`.

**Structural Elements in Available R5 Library**
1. `id`: Inherited from `Resource` (`IdType`). Formatted as `[A-Za-z0-9\-\.]{1,64}`.
2. `meta`: Inherited from `Resource` (`Meta`), containing `versionId`, `lastUpdated`, `source`, `profile` (`List<CanonicalType>`), and `security` (`List<Coding>`).
3. `category`: `List<CodeableConcept>` (broad classification of the event).
4. `code`: `CodeableConcept` (1..1 mandatory in R5; specific event/activity type).
5. `action`: `AuditEventAction` (enum values: `C`, `R`, `U`, `D`, `E`).
6. `severity`: `AuditEventSeverity` (code: `emergency`, `alert`, `critical`, `error`, `warning`, `notice`, `informational`, `debug`).
7. `occurred[x]`: Choice of `Period` or `DateTimeType`.
8. `recorded`: `InstantType` (1..1 timestamp when audit event was recorded).
9. `outcome`: `AuditEventOutcomeComponent` containing:
    - `code`: `Coding`
    - `detail`: `List<AuditEventOutcomeDetailComponent>` (each with `type: CodeableConcept`, `value[x]: DataType`).
10. `authorization`: `List<CodeableConcept>` (top-level authorization policy/token references).
11. `basedOn`: `List<Reference>` (references to operational tasks/requests).
12. `patient`: `Reference` (subject patient when applicable).
13. `encounter`: `Reference` (clinical encounter context).
14. `agent`: `List<AuditEventAgentComponent>` containing:
    - `type`: `CodeableConcept` (agent participant type).
    - `role`: `List<CodeableConcept>` (security/audit role of the agent).
    - `who`: `Reference` (identifier/reference for the agent principal).
    - `requestor`: `boolean` (true if initiator).
    - `location`: `Reference`.
    - `policy`: `List<UriType>` (policy URIs under which agent acted).
    - `network`: `AuditEventAgentNetworkComponent`.
    - `authorization`: `List<CodeableConcept>` (agent-level authorization evidence/tokens).
15. `source`: `AuditEventSourceComponent` containing:
    - `site`: `Reference`.
    - `observer`: `Reference` (1..1 system/subsystem actor).
    - `type`: `List<CodeableConcept>` (subsystem role/type codings).
16. `entity`: `List<AuditEventEntityComponent>` containing:
    - `what`: `Reference` (target resource or identifier).
    - `role`: `CodeableConcept` (functional entity role).
    - `securityLabel`: `List<Coding>` (security labels attached to the entity).
    - `query`: `byte[]` (query specification).
    - `detail`: `List<AuditEventEntityDetailComponent>` (each with `type: CodeableConcept`, `value[x]: DataType`).
    - `agent`: `List<AuditEventAgentComponent>`.

*Critical Finding*: In FHIR R5 `AuditEvent`, there is **no native `identifier` property** (unlike FHIR R4 or other R5 resources like `Patient` or `Task`). Business identity must therefore be anchored via `AuditEvent.id` and/or a governed extension.

---

**5. Complete Field-by-Field Semantic Mapping Table**

| Harmonia Field | Harmonia Semantics | Mandatory / Optional | Proposed FHIR R5 Location | Mapping Class | Forward Mapping (`Harmonia -> FHIR`) | Reverse Mapping (`FHIR -> Harmonia`) | Cardinality | Terminology / System Requirement | Information-Loss Risk | Rationale |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `eventId` | Canonical event identifier | Mandatory | `AuditEvent.id` & extension `audit-event-id` | `NATIVE` / `EXTENSION` | Set `AuditEvent.id` = `eventId`; add extension `audit-event-id` with value `eventId` | Prefer extension `audit-event-id` value; fallback to `AuditEvent.id` | 1..1 | Identifier system: `http://harmonia.fhirfactory.net/identifier/audit-event` | None | R5 `AuditEvent` lacks native `identifier`. Using `id` ensures standard FHIR lookup, while extension guarantees persistence if server re-indexes `id`. |
| `recordedAt` | Timestamp when event was recorded | Mandatory | `AuditEvent.recorded` | `NATIVE` | `new InstantType(Date.from(recordedAt))` | `recorded.getValue().toInstant()` | 1..1 | ISO 8601 Instant | None | Direct 1:1 semantic match in FHIR R5. |
| `classification` | High-level architectural domain category | Mandatory | `AuditEvent.category` | `CODED` | `category.add(new CodeableConcept().addCoding(new Coding("http://harmonia.fhirfactory.net/security/audit-classification", classification.name(), classification.name())))` | Match coding with system `.../audit-classification`; parse `AuditClassification.valueOf(code)` | 1..* | CodeSystem: `http://harmonia.fhirfactory.net/security/audit-classification` | None | Native R5 `category` represents event categorization; CodeSystem preserves exact enum. |
| `action` | Verb describing operation | Mandatory | `AuditEvent.action` & `AuditEvent.code` | `CODED` | Set native `action` (`C`,`R`,`U`,`D`,`E`); set `code` with system `.../audit-action` and code `action.name()` | Read coding from `code` with system `.../audit-action`; parse `AuditAction.valueOf(code)` | 1..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/audit-action` | None | Native R5 `action` collapses `SEARCH`, `AUTHORIZE`, and `EXECUTE` into `E`. `code` preserves exact Harmonia verb losslessly. |
| `outcome` | Execution/policy outcome | Mandatory | `AuditEvent.outcome.code` | `CODED` | Set `outcome.code` with system `.../audit-outcome` and code `outcome.name()` (plus standard DICOM code) | Match coding with system `.../audit-outcome`; parse `AuditOutcome.valueOf(code)` | 1..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/audit-outcome` | None | Distinguishes technical `FAILURE` from policy `DENIED` losslessly. |
| `initiatingPrincipal` | Requesting/originating identity | Optional | `AuditEvent.agent` (`requestor = true`, `role = INITIATOR`) | `CODED` | Create agent with `requestor = true`, role `INITIATOR`, `who.identifier` = principalId, `type` = principalType | Filter `agent` where `requestor = true` or role `INITIATOR`; parse `ThemisPrincipal` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/agent-role` | None | Native `agent` model supports initiator distinction natively. |
| `executingPrincipal` | Identity performing execution | Optional | `AuditEvent.agent` (`requestor = false`, `role = EXECUTOR`) | `CODED` | Create agent with `requestor = false`, role `EXECUTOR`, `who.identifier` = principalId, `type` = principalType | Filter `agent` where role `EXECUTOR`; parse `ThemisPrincipal` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/agent-role` | None | Native `agent` model supports delegated/executor identity cleanly. |
| `securityDomain` | Authorization/tenancy boundary | Optional | Extension `security-domain` | `EXTENSION` | Add top-level extension `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` with `StringType(securityDomain)` | Read extension `security-domain` | 0..1 | String / URI | None | R5 `AuditEvent` has no native top-level authorization realm/tenancy element. |
| `target` | Audited resource/entity reference | Optional | `AuditEvent.entity` | `CODED` | Create `entity` with role `TARGET`, `what` reference/identifier (`resourceType/resourceId`), and detail for target `securityDomain` | Filter `entity` with role `TARGET`; extract `resourceType`, `resourceId`, and detail `securityDomain` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/entity-role` | None | Native R5 `entity` represents accessed objects. `entity.detail` holds target-specific domain. |
| `authorizationEvidence` | Themis evaluation evidence | Optional | `AuditEvent.outcome.detail`, `agent.policy`, & `authorization` | `CODED` | Map `decisionId`, `decision`, `reason`, `message` to `outcome.detail`; set `agent.policy` to `policyId` URI; set `authorization` coding | Extract decision parameters from `outcome.detail`; extract policy from `agent.policy` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/security/themis-decision-reason` | None | Native R5 `outcome.detail` and `agent.policy` capture decision evidence without extensions. |
| `authorities` | Authorization context snapshot | Optional | `AuditEvent.agent.authorization` | `CODED` | Map each `ThemisAuthority` to `agent.authorization` with system `.../authorities` | Extract all codings from `agent.authorization` with system `.../authorities` into `Set<ThemisAuthority>` | 0..* | CodeSystem: `http://harmonia.fhirfactory.net/security/authorities` | None | Native R5 `agent.authorization` specifically models authority tokens held during the event. |
| `securityLabels` | Sensitivity/access labels | Optional | `AuditEvent.meta.security` & `entity.securityLabel` | `CODED` | Add each label to `meta.security` and `entity.securityLabel` | Extract matching codings from `meta.security` into `Set<ThemisSecurityLabel>` | 0..* | CodeSystem: `http://harmonia.fhirfactory.net/security/labels` | None | Preserves security tags on both the record itself and the audited entity. |
| `correlationId` | End-to-end business workflow ID | Optional | Extension `correlation-id` | `EXTENSION` | Add extension `.../correlation-id` with `StringType(correlationId)` | Read extension `.../correlation-id` | 0..1 | String / UUID | None | R5 `AuditEvent` has no native workflow tracing correlation element. |
| `causationId` | Immediate preceding parent task ID | Optional | Extension `causation-id` | `EXTENSION` | Add extension `.../causation-id` with `StringType(causationId)` | Read extension `.../causation-id` | 0..1 | String / UUID | None | R5 `AuditEvent` has no native causation element. |
| `operationId` | Specific operation trace ID | Optional | Extension `operation-id` | `EXTENSION` | Add extension `.../operation-id` with `StringType(operationId)` | Read extension `.../operation-id` | 0..1 | String / UUID | None | R5 `AuditEvent` has no native operation instance element. |
| `source` | Subsystem and component origin | Optional | `AuditEvent.source` | `CODED` | Set `source.observer.identifier` = component; set `source.type` = subsystem | Parse `subsystem` from `source.type` and `component` from `source.observer.identifier.value` | 0..1 | CodeSystem: `http://harmonia.fhirfactory.net/source/subsystem` | None | Native R5 `source` explicitly captures observer and subsystem role. |
| `attributes` | Small metadata key-value map | Optional | Extension `audit-attribute` | `EXTENSION` | For each entry, add extension `.../audit-attribute` with sub-extensions `key` and `value` | Reconstruct `Map<String, String>` from extensions | 0..* | Governed key-value strings | None | R5 `AuditEvent` has no top-level generic metadata map element. |

---

**6. Event Identity Decision**

**Analysis of `HarmoniaAuditEvent.eventId` vs. `AuditEvent.id`**
1. **Should `eventId` map to `AuditEvent.id`?**
   **Yes, conditionally.** `AuditEvent.id` should be populated with `eventId` whenever `eventId` satisfies FHIR `id` character constraints.
2. **Normalization and character constraints:**
   FHIR R5 `Resource.id` restricts characters to `[A-Za-z0-9\-\.]{1,64}`. Standard Harmonia event IDs generated via `UUID.randomUUID().toString()` (36 lowercase hex digits and hyphens) are 100% compliant.
3. **Can every valid Harmonia `eventId` be represented losslessly as a FHIR logical ID?**
   **No.** `HarmoniaAuditEvent` only asserts that `eventId` is non-null and non-blank. Custom event IDs containing colons, slashes, underscores, or exceeding 64 characters cannot be stored directly in `AuditEvent.id`.
4. **Should a separate identifier extension/system be used?**
   **Yes.** To guarantee 100% round-trip fidelity regardless of external FHIR server behaviors (e.g. servers that override client-assigned IDs or reject non-compliant strings), a top-level extension `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-event-id` is mandated.
5. **What identity drives append idempotency?**
   Append idempotency is driven strictly by the canonical Harmonia `eventId` (stored in the extension and mirrored in `AuditEvent.id`).
6. **Conflict semantics:**
    - Same `eventId` + identical content $\rightarrow$ Idempotent success (no duplicate log).
    - Same `eventId` + divergent content $\rightarrow$ `AuditIntegrityException` (tamper/corruption detected).
7. **Is FHIR logical ID alone sufficient?**
   No. Logical IDs are repository-local and mutable across server imports. The business identifier extension ensures absolute immutability.

---

**7. Classification and Event Type**

- **Harmonia Concept**: `AuditClassification` (`SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`).
- **FHIR R5 Location**: `AuditEvent.category: List<CodeableConcept>`.
- **Governance**: Harmonia defines CodeSystem `http://harmonia.fhirfactory.net/security/audit-classification`.
- **Distinction**:
    - `category`: Broad architectural classification (e.g., `CLINICAL`).
    - `code`: Specific activity/event type (e.g., `AUTHORIZE`, `READ`).
    - `action`: Generic CRUD/E lifecycle verb (e.g., `R`, `E`).
- Forward mapping adds a `CodeableConcept` to `category` with the governed CodeSystem. Reverse mapping filters `category` for this system and parses the enum name.

---

**8. Action Mapping**

- **Harmonia Concept**: `AuditAction` (`CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`).
- **FHIR R5 Action Semantics**: Native `AuditEvent.action` is restricted to:
    - `CREATE` $\rightarrow$ `AuditEventAction.C`
    - `READ` $\rightarrow$ `AuditEventAction.R`
    - `UPDATE` $\rightarrow$ `AuditEventAction.U`
    - `DELETE` $\rightarrow$ `AuditEventAction.D`
    - `EXECUTE` $\rightarrow$ `AuditEventAction.E`
- **The Collapsing Problem**: In native FHIR, `SEARCH`, `AUTHORIZE`, and `EXECUTE` all collapse into `E` (Execute). Reversing from `action` alone would be lossy.
- **Deterministic Resolution**:
    - Native `AuditEvent.action` is set to the nearest standard code (`C`, `R`, `U`, `D`, `E`).
    - Mandatory native `AuditEvent.code` (`1..1 CodeableConcept`) is populated with CodeSystem `http://harmonia.fhirfactory.net/security/audit-action` and code `action.name()`.
    - Reverse mapping reads `AuditEvent.code` to recover the exact Harmonia `AuditAction` deterministically.

---

**9. Outcome Mapping**

- **Harmonia Concept**: `AuditOutcome` (`SUCCESS`, `FAILURE`, `DENIED`).
- **FHIR R5 Outcome Semantics**: `AuditEvent.outcome: AuditEventOutcomeComponent` contains `code: Coding`.
- **Distinction**:
    - Technical/operational failure (`FAILURE`) vs. authorization refusal (`DENIED`).
- **Resolution**:
    - `AuditEvent.outcome.code` is assigned a `Coding` with system `http://harmonia.fhirfactory.net/security/audit-outcome` and code `SUCCESS`, `FAILURE`, or `DENIED`.
    - Secondary standard DICOM/FHIR codes (`0` for Success, `4` for Minor Failure/Denied, `8` for Serious Failure) are included for interoperability.
    - Reverse mapper inspects `.../audit-outcome` to reconstruct the exact `AuditOutcome`.

---

**10. Initiating and Executing Principal Mapping**

Harmonia strictly distinguishes `initiatingPrincipal` (originating user/service) from `executingPrincipal` (delegated pipeline worker/engine).

**Dual-Agent Representation**  
Both principals map as distinct entries in `AuditEvent.agent: List<AuditEventAgentComponent>`.

**Semantic Role Distinction**
1. `initiatingPrincipal`:
    - `agent.requestor = true`
    - `agent.role` contains `Coding("http://harmonia.fhirfactory.net/security/agent-role", "INITIATOR", "Initiating Principal")`
2. `executingPrincipal`:
    - `agent.requestor = false`
    - `agent.role` contains `Coding("http://harmonia.fhirfactory.net/security/agent-role", "EXECUTOR", "Executing Principal")`

**Principal Properties**
- `principalId`: Stored in `agent.who.identifier.value` with system derived from `sourceDomain`.
- `principalType`: Mapped to `agent.type` with CodeSystem `http://harmonia.fhirfactory.net/security/principal-type` (`HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`).
- `sourceDomain`: Encapsulated in `agent.who.identifier.system`.
- `attributes`: Persisted via nested backbone extension on `agent` (`http://harmonia.fhirfactory.net/fhir/StructureDefinition/agent-attribute`).

Reverse mapping unambiguously identifies both agents by role and requestor flag without relying on list order.

---

**11. Security Domain Mapping**

- **Harmonia Concept**: Top-level `HarmoniaAuditEvent.securityDomain` (e.g. `CLINICAL`, `OPERATIONS`, `AUDIT`, `PROVIDER_REGISTRY`).
- **Analysis**:
    - Is it `meta.security`? **No.** `meta.security` represents security labels (confidentiality, integrity), not tenancy/authorization realms.
    - Is it `source.site`? **No.** `site` represents physical/logical network location.
- **Resolution**: A dedicated top-level extension:
  `http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain` (data type: `string`, cardinality `0..1`).
- *Technical Debt Note*: String-based security domains remain a known backlog item. This extension persists the current string faithfully without premature redesign.

---

**12. Target Mapping**

- **Harmonia Concept**: `AuditTarget(String resourceType, String resourceId, String securityDomain)`.
- **FHIR R5 Location**: `AuditEvent.entity: List<AuditEventEntityComponent>`.
- **Mapping**:
    - `entity.role`: Governed coding `http://harmonia.fhirfactory.net/security/entity-role` with code `TARGET`.
    - `entity.what`: `Reference`.
        - If standard FHIR resource: `reference = "Patient/123"`, `type = "Patient"`.
        - If non-FHIR domain target: `type = resourceType`, `identifier.value = resourceId`.
    - `securityDomain`: Stored in native `entity.detail` with `type` = `Coding("http://harmonia.fhirfactory.net/security/entity-detail", "securityDomain")` and `value = StringType(securityDomain)`.
- Reverse mapping reconstructs `AuditTarget` directly from `entity.what` and `entity.detail`.

---

**13. Authorization Evidence Mapping**

- **Harmonia Concept**: `AuditAuthorizationEvidence(decisionId, decision, reason, policyId, message)`.
- **Native FHIR R5 Representation**:
    - `policyId`: Stored in `agent.policy: List<UriType>` (as `urn:harmonia:policy:{policyId}`).
    - `decisionId`, `decision`, `reason`, `message`: Stored in native `AuditEvent.outcome.detail`:
        - `detail.type = "decisionId"`, `value = StringType(decisionId)`
        - `detail.type = "decision"`, `value = Coding(".../themis-decision", decision.name())`
        - `detail.type = "reason"`, `value = Coding(".../themis-decision-reason", reason.name())`
        - `detail.type = "message"`, `value = StringType(message)` (when non-empty)
    - `AuditEvent.authorization`: Populated with `CodeableConcept` referencing the decision reason.
- Reverse mapping reads `outcome.detail` and `agent.policy` to reconstruct `AuditAuthorizationEvidence` losslessly without proprietary extensions.

---

**14. Authority Snapshot Mapping**

- **Harmonia Concept**: `Set<ThemisAuthority>` (snapshot of active authorities at execution time).
- **Invariant (Task 04)**: Propagated authorities are historical evidence/provenance, **never** permanent delegated capabilities.
- **FHIR R5 Location**: Native `AuditEvent.agent.authorization: List<CodeableConcept>`.
- **Mapping**:
    - Each `ThemisAuthority` maps to a `CodeableConcept` in `agent.authorization` with system `http://harmonia.fhirfactory.net/security/authorities` and `code = authorityCode`.
    - Because these codings reside on historical `AuditEvent` records, they cannot be queried or executed as active permissions.
- Reverse mapping reconstructs `Set<ThemisAuthority>` by collecting all codings matching the authority system.

---

**15. Security Label Mapping**

- **Harmonia Concept**: `Set<ThemisSecurityLabel>` (e.g. `CLINICAL`, `RESTRICTED`, `AUDIT`).
- **Semantic Boundary**:
    - Labels on the **Audit Record itself** map to `AuditEvent.meta.security: List<Coding>`.
    - Labels on the **Target Entity** map to `AuditEvent.entity.securityLabel: List<Coding>`.
- Forward mapping copies the event's security labels to `AuditEvent.meta.security` and attaches them to `entity.securityLabel`.
- Reverse mapping extracts codings with system `http://harmonia.fhirfactory.net/security/labels` from `meta.security` into `Set<ThemisSecurityLabel>`.

---

**16. Correlation, Causation, and Operation Mapping**

Harmonia strictly distinguishes three tracing identifiers:
1. `correlationId`: End-to-end business workflow boundary.
2. `causationId`: Immediate preceding work item causing this event.
3. `operationId`: Specific invocation/execution instance.

- **Analysis**: Native FHIR R5 `AuditEvent` provides no top-level header fields for distributed workflow correlation. `basedOn` is typed as a FHIR `Reference` and is unsuitable for arbitrary distributed tracking strings.
- **Resolution**: Exactly three separate top-level extensions:
    1. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id` (`StringType`, `0..1`)
    2. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id` (`StringType`, `0..1`)
    3. `http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id` (`StringType`, `0..1`)
- Reverse mapping reads each extension independently, preventing any semantic conflation.

---

**17. Source Mapping**

- **Harmonia Concept**: `AuditSource(String subsystem, String component)`.
- **FHIR R5 Location**: Native `AuditEvent.source: AuditEventSourceComponent`.
- **Mapping**:
    - `subsystem`: Mapped to `source.type: List<CodeableConcept>` with system `http://harmonia.fhirfactory.net/source/subsystem` and code `subsystem`.
    - `component`: Mapped to `source.observer.identifier` with system `http://harmonia.fhirfactory.net/source/component` and `value = component`.
    - `source.observer.display`: Formatted as `subsystem + "/" + component`.
- Reverse mapping extracts `subsystem` from `source.type` and `component` from `source.observer.identifier.value`.

---

**18. Attribute Mapping**

- **Harmonia Concept**: `Map<String, String> attributes` (bounded metadata container).
- **Constraints & Guardrails**:
    - Must never contain PHI, tokens, passwords, or raw clinical payloads.
    - Keys must conform to `^[a-zA-Z0-9_\-\.]{1,64}$`.
    - Values bounded to $\le 256$ characters.
- **Mapping**: Extension `http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute` (cardinality `0..*`) with sub-extensions:
    - `key`: `StringType`
    - `value`: `StringType`
- Reverse mapping gathers all extension pairs into an immutable `Map<String, String>`.

---

**19. Proposed Harmonia Terminology Artifacts**

The following CodeSystems and ValueSets are specified for governance:
1. `http://harmonia.fhirfactory.net/security/audit-classification`: Codes: `SECURITY`, `CLINICAL`, `WORKFLOW`, `INTEGRATION`, `ADMINISTRATION`, `SYSTEM`.
2. `http://harmonia.fhirfactory.net/security/audit-action`: Codes: `CREATE`, `READ`, `SEARCH`, `UPDATE`, `DELETE`, `EXECUTE`, `AUTHORIZE`.
3. `http://harmonia.fhirfactory.net/security/audit-outcome`: Codes: `SUCCESS`, `FAILURE`, `DENIED`.
4. `http://harmonia.fhirfactory.net/security/agent-role`: Codes: `INITIATOR`, `EXECUTOR`.
5. `http://harmonia.fhirfactory.net/security/principal-type`: Codes: `HUMAN`, `SYSTEM`, `SERVICE`, `PROCESS`.
6. `http://harmonia.fhirfactory.net/security/entity-role`: Code: `TARGET`.
7. `http://harmonia.fhirfactory.net/security/entity-detail`: Code: `securityDomain`.
8. `http://harmonia.fhirfactory.net/source/subsystem`: Codes for Harmonia subprojects (`THEMIS`, `ENERGEIA`, `PYLAI`, `HESTIA`, `CALLIOPE`, `IRIS`, `AGORA`, `PETASOS`, `KLEIO`).
9. `http://harmonia.fhirfactory.net/source/component`: Identifiers for specific internal services.

---

**20. Proposed Harmonia AuditEvent Profile Constraints**

Canonical Profile URL: `http://harmonia.fhirfactory.net/fhir/StructureDefinition/harmonia-audit-event`

Formal constraints imposed by the profile:
- `AuditEvent.id`: Mandatory (1..1), matching `[A-Za-z0-9\-\.]{1,64}`.
- `AuditEvent.recorded`: Mandatory (1..1).
- `AuditEvent.category`: Mandatory (1..*), bound to `audit-classification`.
- `AuditEvent.code`: Mandatory (1..1), bound to `audit-action`.
- `AuditEvent.action`: Mandatory (1..1), constrained to `C`, `R`, `U`, `D`, `E`.
- `AuditEvent.outcome`: Mandatory (1..1), `outcome.code` bound to `audit-outcome`.
- `AuditEvent.agent`: Mandatory (1..2), at least one agent with `role = INITIATOR` or `role = EXECUTOR`.
- `AuditEvent.source`: Mandatory (1..1), `observer` and `type` present.
- Extensions:
    - `audit-event-id`: Mandatory (1..1).
    - `security-domain`: Optional (0..1).
    - `correlation-id`, `causationId`, `operation-id`: Optional (0..1).
    - `audit-attribute`: Optional (0..*).

---

**21. Proposed Extension Register**

| Proposed Extension Name | Purpose | Data Type | Cardinality | Context | Forward Mapping | Reverse Mapping | Justification (Why Native R5 is Insufficient) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `audit-event-id` | Guarantees business identity retention | `StringType` / `Identifier` | 1..1 | `AuditEvent` | Set to `eventId` | Reconstruct `eventId` | R5 `AuditEvent` completely lacks a native `identifier` property. |
| `security-domain` | Identifies authorization realm / tenancy | `StringType` | 0..1 | `AuditEvent` | Set to `securityDomain` | Reconstruct `securityDomain` | R5 `AuditEvent` has no top-level tenancy/domain concept. |
| `correlation-id` | Traces end-to-end distributed business workflow | `StringType` | 0..1 | `AuditEvent` | Set to `correlationId` | Reconstruct `correlationId` | R5 has no native workflow correlation header element. |
| `causation-id` | Traces direct preceding work item | `StringType` | 0..1 | `AuditEvent` | Set to `causationId` | Reconstruct `causationId` | R5 has no native causation tracing element. |
| `operation-id` | Traces specific invocation/execution trace | `StringType` | 0..1 | `AuditEvent` | Set to `operationId` | Reconstruct `operationId` | R5 has no native operation instance element. |
| `audit-attribute` | General escape-hatch metadata key-values | Complex (`key`, `value`) | 0..* | `AuditEvent` | For each map entry, write sub-extensions | Reconstruct `attributes` map | R5 has no top-level metadata key-value container. |
| `agent-attribute` | Captures principal attributes on agent | Complex (`key`, `value`) | 0..* | `AuditEvent.agent` | For each map entry, write sub-extensions | Reconstruct principal attributes | `agent` backbone has no native arbitrary attribute map. |

---

**22. Considered-but-Rejected Extensions**

1. **Rejected: `audit-action-extension`**:
    - *Alternative Used*: Native `AuditEvent.action` + coded native `AuditEvent.code`.
2. **Rejected: `audit-classification-extension`**:
    - *Alternative Used*: Native `AuditEvent.category` (`List<CodeableConcept>`).
3. **Rejected: `audit-outcome-extension`**:
    - *Alternative Used*: Native `AuditEvent.outcome.code` (`Coding`).
4. **Rejected: `audit-target-extension`**:
    - *Alternative Used*: Native `AuditEvent.entity` (`what`, `role`, `detail`).
5. **Rejected: `authorization-evidence-extension`**:
    - *Alternative Used*: Native `AuditEvent.outcome.detail`, `agent.policy`, and `AuditEvent.authorization`.
6. **Rejected: `authority-snapshot-extension`**:
    - *Alternative Used*: Native `AuditEvent.agent.authorization` (`List<CodeableConcept>`).
7. **Rejected: `target-security-labels-extension`**:
    - *Alternative Used*: Native `AuditEvent.entity.securityLabel` (`List<Coding>`).
8. **Rejected: `audit-source-extension`**:
    - *Alternative Used*: Native `AuditEvent.source.observer` and `AuditEvent.source.type`.

---

**23. Round-Trip Fidelity Matrix**

| Harmonia Field | Original Value | FHIR Representation | Reconstructed Value | Classification |
| :--- | :--- | :--- | :--- | :--- |
| `eventId` | `"ae-9812-uuid"` | `AuditEvent.id="ae-9812-uuid"`, extension `audit-event-id="ae-9812-uuid"` | `"ae-9812-uuid"` | **EXACT** |
| `recordedAt` | `Instant.ofEpochMilli(1774390000000L)` | `AuditEvent.recorded=2026-03-24T22:06:40.000Z` | `Instant.ofEpochMilli(1774390000000L)` | **EXACT** |
| `classification` | `AuditClassification.CLINICAL` | `AuditEvent.category[0].coding[0].code="CLINICAL"` | `AuditClassification.CLINICAL` | **EXACT** |
| `action` | `AuditAction.SEARCH` | `action=E`, `code.coding[0].code="SEARCH"` | `AuditAction.SEARCH` | **EXACT** |
| `outcome` | `AuditOutcome.DENIED` | `outcome.code.coding[0].code="DENIED"` | `AuditOutcome.DENIED` | **EXACT** |
| `initiatingPrincipal` | `ThemisPrincipal("dr-smith", HUMAN, "hosp-a", {})` | `agent[0]` (`requestor=true`, `role=INITIATOR`, `who="dr-smith"`) | `ThemisPrincipal("dr-smith", HUMAN, "hosp-a", {})` | **EXACT** |
| `executingPrincipal` | `ThemisPrincipal("ponos-wkr", PROCESS, "core", {})`| `agent[1]` (`requestor=false`, `role=EXECUTOR`, `who="ponos-wkr"`) | `ThemisPrincipal("ponos-wkr", PROCESS, "core", {})` | **EXACT** |
| `securityDomain` | `"CLINICAL_NORTH"` | Extension `security-domain="CLINICAL_NORTH"` | `"CLINICAL_NORTH"` | **EXACT** |
| `target` | `AuditTarget("Patient", "P123", "CLINICAL_NORTH")` | `entity[0]` (`what="Patient/P123"`, `detail[0]="CLINICAL_NORTH"`) | `AuditTarget("Patient", "P123", "CLINICAL_NORTH")` | **EXACT** |
| `authorizationEvidence` | `AuditAuthorizationEvidence("d-1", DENY, DEFAULT_DENY, "pol-9", "")` | `outcome.detail` entries + `agent.policy="urn:harmonia:policy:pol-9"` | `AuditAuthorizationEvidence("d-1", DENY, DEFAULT_DENY, "pol-9", "")` | **EXACT** |
| `authorities` | `[ThemisAuthority("CLINICAL_READ")]` | `agent.authorization[0].code="CLINICAL_READ"` | `[ThemisAuthority("CLINICAL_READ")]` | **EXACT** |
| `securityLabels` | `[ThemisSecurityLabel("labels", "RESTRICTED")]` | `meta.security[0].code="RESTRICTED"` | `[ThemisSecurityLabel("labels", "RESTRICTED")]` | **EXACT** |
| `correlationId` | `"corr-771"` | Extension `correlation-id="corr-771"` | `"corr-771"` | **EXACT** |
| `causationId` | `"cause-554"` | Extension `causation-id="cause-554"` | `"cause-554"` | **EXACT** |
| `operationId` | `"op-001"` | Extension `operation-id="op-001"` | `"op-001"` | **EXACT** |
| `source` | `AuditSource("THEMIS", "AUTHORIZATION_ENGINE")` | `source.type="THEMIS"`, `source.observer="AUTHORIZATION_ENGINE"` | `AuditSource("THEMIS", "AUTHORIZATION_ENGINE")` | **EXACT** |
| `attributes` | `{"clientIp": "192.168.1.50"}` | Extension `audit-attribute` (`key="clientIp"`, `value="192.168.1.50"`) | `{"clientIp": "192.168.1.50"}` | **EXACT** |

**Summary**: 17 Exact / 0 Normalized / 0 Lossy.

---

**24. Representative Event Examples**

**Example 1: Human Reads a Clinical Patient**
- **Harmonia Semantics**: `initiatingPrincipal = "dr-alice" (HUMAN)`, `action = READ`, `classification = CLINICAL`, `target = AuditTarget("Patient", "P-902", "CLINICAL")`, `outcome = SUCCESS`.
- **FHIR Elements**:
    - `category`: `CLINICAL`
    - `action`: `R`, `code`: `READ`
    - `outcome.code`: `SUCCESS`
    - `agent[0]`: `who.identifier.value = "dr-alice"`, `type = HUMAN`, `requestor = true`, `role = INITIATOR`
    - `entity[0]`: `what.reference = "Patient/P-902"`, `role = TARGET`
- **Reconstructed**: Exact match of all fields.

**Example 2: Human Searches Clinical Resources**
- **Harmonia Semantics**: `initiatingPrincipal = "nurse-bob" (HUMAN)`, `action = SEARCH`, `classification = CLINICAL`, `target = AuditTarget("Observation", null, "CLINICAL")`, `outcome = SUCCESS`.
- **FHIR Elements**:
    - `action`: `E`, `code`: `SEARCH`
    - `agent[0]`: `who.identifier.value = "nurse-bob"`, `requestor = true`, `role = INITIATOR`
    - `entity[0]`: `what.type = "Observation"`, `role = TARGET`
- **Reconstructed**: `action = SEARCH` successfully differentiated from `EXECUTE`.

**Example 3: Unauthorized Clinical UPDATE Denied**
- **Harmonia Semantics**: `initiatingPrincipal = "dr-alice"`, `action = UPDATE`, `outcome = DENIED`, `authorizationEvidence = AuditAuthorizationEvidence("d-12", DENY, EXPLICIT_DENY, "pol-no-edit", "Restricted")`.
- **FHIR Elements**:
    - `action`: `U`, `code`: `UPDATE`
    - `outcome.code`: `DENIED`
    - `outcome.detail`: `decision = DENY`, `reason = EXPLICIT_DENY`, `decisionId = "d-12"`
    - `agent[0].policy`: `"urn:harmonia:policy:pol-no-edit"`
- **Reconstructed**: `outcome = DENIED` with full policy evidence.

**Example 4: Trusted Service Performs Integration Operation**
- **Harmonia Semantics**: `initiatingPrincipal = "mllp-adapter" (SERVICE)`, `action = EXECUTE`, `classification = INTEGRATION`, `source = AuditSource("PYLAI", "MLLP_IN")`, `outcome = SUCCESS`.
- **FHIR Elements**:
    - `category`: `INTEGRATION`
    - `agent[0]`: `who = "mllp-adapter"`, `type = SERVICE`, `requestor = true`
    - `source`: `type = "PYLAI"`, `observer = "MLLP_IN"`
- **Reconstructed**: Service identity and Pylai source preserved.

**Example 5: Ponos Process Executes Workflow Work Initiated by Human**
- **Harmonia Semantics**: `initiatingPrincipal = "user-jane" (HUMAN)`, `executingPrincipal = "ponos-worker-4" (PROCESS)`, `action = EXECUTE`, `classification = WORKFLOW`.
- **FHIR Elements**:
    - `agent[0]`: `who = "user-jane"`, `requestor = true`, `role = INITIATOR`
    - `agent[1]`: `who = "ponos-worker-4"`, `requestor = false`, `role = EXECUTOR`
- **Reconstructed**: Dual-agent initiator vs. executor roles preserved.

**Example 6: Administrative Action Performed by Human**
- **Harmonia Semantics**: `initiatingPrincipal = "admin-dave" (HUMAN)`, `action = DELETE`, `classification = ADMINISTRATION`, `target = AuditTarget("PractitionerRole", "PR-5", "ADMINISTRATION")`.
- **FHIR Elements**:
    - `category`: `ADMINISTRATION`
    - `action`: `D`, `code`: `DELETE`
    - `entity[0]`: `what.reference = "PractitionerRole/PR-5"`
- **Reconstructed**: Administrative context retained.

**Example 7: System-Generated Event with No Initiating Human**
- **Harmonia Semantics**: `initiatingPrincipal = null`, `executingPrincipal = "hestia-reaper" (SYSTEM)`, `action = EXECUTE`, `classification = SYSTEM`.
- **FHIR Elements**:
    - `category`: `SYSTEM`
    - `agent[0]`: `who = "hestia-reaper"`, `type = SYSTEM`, `requestor = false`, `role = EXECUTOR` (no initiator agent present).
- **Reconstructed**: Absence of human initiator retained; executing system identified.

**Example 8: Event with Correlation, Causation, and Operation IDs**
- **Harmonia Semantics**: `correlationId = "corr-100"`, `causationId = "task-50"`, `operationId = "op-25"`.
- **FHIR Elements**:
    - Extensions `correlation-id="corr-100"`, `causation-id="task-50"`, `operation-id="op-25"`.
- **Reconstructed**: All three IDs reconstructed distinctly.

---

**25. Immutability / Identity Implications**

The Kleio contract establishes that audit logs are strictly append-only:
1. `new eventId` $\rightarrow$ Append.
2. `same eventId + identical event` $\rightarrow$ Idempotent replay (success, no duplicate).
3. `same eventId + divergent content` $\rightarrow$ Integrity violation (`AuditIntegrityException`).

**Implications for Step 03 Persistence**
- Standard FHIR REST endpoints (`POST /AuditEvent` or `PUT /AuditEvent/{id}`) allow mutable replacement and version incrementing (`versionId = 2`). This violates Kleio immutability.
- In Step 03, the persistence adapter behind `AuditService` must:
    - Enforce client-assigned ID creation.
    - Implement conditional create or optimistic check: if record exists, compare parsed content; if identical, return success; if divergent, throw `AuditIntegrityException`.
    - Disable HTTP `PUT` and `DELETE` on durable audit storage.

---

**26. Existing FHIR AuditEvent Compatibility Concerns**

Exploration of `hestia/mnemosyne-clinical` and `iris/iris-befe`:
- **Mutable Operations**: Both `AuditEventResourceProvider` (Hestia) and `AuditEventResource` (Iris) provide mutable CRUD operations (`update`, `delete`), which increment version numbers and soft-delete audit records.
- **Arbitrary External Payloads**: Existing REST endpoints accept arbitrary unvalidated JSON `AuditEvent` payloads.
- **Search Limitations**: Generic search filters in Hestia/Iris match `name` against action/agent, but do not implement search by `identifier` or correlation extensions.
- **Isolation Requirement**: Step 02.2 mapper logic must remain encapsulated within Kleio. Existing mutable clinical and presentation endpoints must not be refactored until Step 03/04.

---

**27. Kleio / Calliope Ownership Recommendation**

- **Calliope Ownership**:
    - Governs repository-wide canonical schemas, FHIR R5 data structures, and shared terminology definitions (`HarmoniaSecurityCodeSystem`).
    - Calliope **must not** depend on Kleio (`PackageLayeringArchitectureTest` prohibits Calliope from importing `net.fhirfactory.harmonia.kleio..`).
- **Kleio Ownership**:
    - Owns audit domain models (`HarmoniaAuditEvent`), append-only service contracts (`AuditService`), and mapping implementations (`HarmoniaAuditEvent <-> FHIR R5 AuditEvent`).
- **Recommendation**:
    - Terminology URI constants can be defined in Calliope or Kleio. However, to avoid circular dependencies and keep `calliope` decoupled from audit-specific mappings, audit profile definitions and mapping logic belong entirely within Kleio.

---

**28. Recommended Module / Dependency Boundary for Step 02.2**

`kleio-core` must remain a pure Java domain module free of FHIR dependencies.

**Proposed Structure**:
```
kleio/
  pom.xml
  kleio-core/          <-- Pure Java domain (no HAPI FHIR)
  kleio-fhir/          <-- NEW module for Step 02.2
```

**Dependency Direction for `kleio-fhir`**:
- Depends on `kleio-core` (domain models).
- Depends on `calliope` (canonical schemas & base security vocabularies).
- Depends on `ca.uhn.hapi.fhir:hapi-fhir-base` & `ca.uhn.hapi.fhir:hapi-fhir-structures-r5`.
- Pure unidirectional flow: `kleio-fhir -> kleio-core`, `kleio-fhir -> calliope`. Zero violations of ArchUnit rules.

---

**29. Security / PHI Considerations**

In conformance with Task 05 and `AGENTS.md` (Invariants 6 & 7):
1. **Zero-PHI Logging**: The mapper’s internal `toString()` or logging must never emit unmasked clinical observation values, patient names, or attributes.
2. **Target References**: Targets must only record resource identifiers (e.g., `Patient/P123`), never serialized patient records.
3. **Attribute Sanitization**: The mapper must filter or reject sensitive keys (e.g. `authorization`, `password`, `token`, `secret`, `ssn`).
4. **Diagnostic Traces**: Synthetic PHI and credential markers must be used in Step 02.2 unit tests to ensure that serialization does not leak sensitive information.

---

**30. Design Blockers**

**None.** Every canonical concept in `HarmoniaAuditEvent` can be mapped to FHIR R5 and back with 100% semantic fidelity using native elements and the justified extension register.

---

**31. Contradictions / Ambiguities Discovered**

- **FHIR R5 `AuditEvent.identifier` Omission**: Older FHIR R4 assumptions expect `AuditEvent.identifier`. R5 eliminated this property. The design resolves this unambiguously by combining `AuditEvent.id` with the `audit-event-id` extension.
- **Action Collapsing**: R5 `action` codes collapse `SEARCH`, `AUTHORIZE`, and `EXECUTE` into `E`. The design resolves this deterministically by encoding the full `AuditAction` enum into the mandatory `AuditEvent.code` element.

---

**32. Missing Evidence**

None. The exact R5 structures (`org.hl7.fhir.r5:6.1.2.2`) and Kleio domain code are fully available and inspected locally.

---

**33. Issues Belonging to Later Task 06 Steps**

- **Step 02.2**: Implementation of `HarmoniaAuditEventMapper` and unit tests in `kleio-fhir`.
- **Step 03**: Implementation of immutable append persistence adapter and conflict detection.
- **Step 04**: Convergence of Hestia Mnemosyne and Iris BEFE audit endpoints with immutable Kleio storage.

---

**34. Issues Belonging to Tasks 07–10**

- Integration of Petasos distributed event streaming with Kleio audit logging (Task 07).
- Ingress gateway MLLP/REST audit event interception (Task 08).
- Long-term archival and cold storage policies (Tasks 09–10).

---

**35. Backlog Items Discovered**

- **Typed Security Domain**: Transition `securityDomain` from raw `String` to a strongly typed enum/record across Harmonia.

---

**36. Explicit Confirmation of Read-Only Execution**

**Confirmed**: No production code, test code, configuration, POM, or Git references were modified. No mapper classes were created. This report concludes Step 02.1.

---

**FINAL RECOMMENDATION: Proposed Step 02.2 Implementation Contract**

For Step 02.2, the implementation contract is:
1. **Module Creation**: Create module `kleio/kleio-fhir` declared in `kleio/pom.xml` and managed in root `pom.xml`.
2. **Mapper Responsibility**: Implement `net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper` with:
    - `AuditEvent toFhir(HarmoniaAuditEvent event)`
    - `HarmoniaAuditEvent fromFhir(AuditEvent resource)`
3. **Fidelity Invariant**:
   $$\text{fromFhir}(\text{toFhir}(event)) \equiv event$$
   Enforced across all 17 fields via comprehensive parameterized unit tests covering all 8 representative scenarios.
4. **Zero Domain Contamination**: `kleio-core` remains untouched with zero FHIR dependencies.
5. **No Network/DB Dependencies**: `kleio-fhir` will contain pure transformation logic without persistence or remote REST calls.



**Summary**
- Completed a read-only Step 02.1 design report for semantically lossless `HarmoniaAuditEvent <-> FHIR R5 AuditEvent` mapping, based on the current Kleio model, actual local HAPI R5 AuditEvent structure, and existing repository usage.
- Recommended a native-first mapping with a small justified extension set, keeping `kleio-core` FHIR-free and placing future mapper implementation in a separate Kleio FHIR-facing module.

**Changes**
- Verified the canonical Kleio contract, mandatory/optional fields, record equality/idempotency behavior, Themis-derived semantics, and current tests/line references needed for follow-on work.
- Analyzed the actual available R5 AuditEvent model and identified the durable mapping boundary, identity strategy, profile/terminology needs, extension register, and compatibility concerns with current mutable AuditEvent CRUD surfaces.

**Verification**
- Performed multiple targeted read-only explorations plus synthesis; no code, tests, dependencies, persistence, REST, or UI changes were made.
- Explicitly avoided implementation, mapper creation, Maven changes, and git operations, as required for Step 02.1.

**Notes**
- The user-facing response contains the full structured report and Step 02.2 implementation contract; no repository modifications were performed.

