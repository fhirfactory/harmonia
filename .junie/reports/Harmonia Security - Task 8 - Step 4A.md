Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

**Delivery Steps**

**✓ Step 1: Implementation**  
Optional spending limit; leave empty for no limit: 50  
Required for Goal Mode: Auto  
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Map the existing repository locations and artifacts needed to produce the Step 08.03 governed write/concurrency contract design document. Focus on: (1) current architecture/design documentation structure and the best destination path for the new document; (2) existing ADR or design docs related to ADR-018/019/020 and governed writes; (3) existing Java packages/interfaces that would likely host a future governed write facade versus Mneme/Mnemosyne primitives; (4) current code paths that illustrate bypass risks (direct RemoteCache writes, direct persistence update entry points). Provide precise file paths and line references where possible. Read-only only.



Exploration is Ready

Read-only repository exploration completed; no files were modified and the Step 08.03 design document was not created (implementation/document-authoring belongs to the parent task).

**1. Documentation structure and recommended destination**

- `docs/README.md:9-60` is the master documentation information architecture. It has separate `concepts/`, `modules/`, `architecture/`, `integration/`, `security/`, and provider-registry sections, but no `docs/design/` section.
- A glob check for `docs/design/**/*.md` confirmed that `docs/design/` does not currently exist.
- The requested destination `docs/design/governed-write-concurrency-contract.md` is therefore a new, sensible architecture/design location rather than an existing convention. The document should either create that directory and then add it to the documentation index, or, if avoiding a new category, use `docs/architecture/` alongside `persistence-lifecycle.md`; the explicit task request strongly favors `docs/design/`.
- Existing persistence and architecture documents that should be cross-linked from the new document:
    - `docs/architecture-decisions.md`
    - `docs/architecture/persistence-lifecycle.md`
    - `docs/persistence-architecture.md`
    - `docs/concepts/mneme.md`
    - `docs/concepts/mnemosyne.md`
    - `docs/provider-registry/architecture.md`
    - `docs/integration/rest.md`
    - `docs/persistence-recovery-gaps.md`

Note: `docs/architecture.md` is actually a Petasos architecture document despite its generic filename; it is not the best home for this contract.

**2. ADR and existing design baseline**

**ADR-018 — Mnemosyne authoritative durable boundary**

`docs/architecture-decisions.md:136-233` establishes that:

- Mneme/Infinispan is distributed, reconstructable, non-authoritative state (`:226-233`).
- Mnemosyne/PostgreSQL is the authoritative durable application-state boundary (`:201-224`).
- Cache presence, replication, or a successful cache operation is not durable acceptance (`:206-224`).
- The responsibility matrix at `:235-262` separates Mneme distributed cache/access, Mnemosyne durable persistence, Petasos durable work transfer, and Kleio evidence.
- Failure semantics at `:306-340` require persistence failure to remain visible; local memory or asynchronous persistence attempts must not turn unavailable durability into apparent success.

**ADR-019 — Mneme distributed access and coordination**

`docs/architecture-decisions.md:420-512` is the primary source for the future Mneme primitive:

- Mneme owns distributed resource access and coordination, not distributed durability (`:475-503`).
- The explicit division is “Mneme coordinates active distributed state; Mnemosyne commits authoritative durable state” (`:505-512`).
- `:545-562` says concurrency/version coordination must be distributed and substantially transparent to ordinary client code, while authoritative persisted-version semantics remain Mnemosyne’s responsibility.
- `:581-607` prohibits silently replacing Mneme with process-local maps/collections when Mneme is unavailable.
- `:609-646` sketches the cache hit/miss relationship and distinguishes active coordination from authoritative write.
- `:648-710` reinforces that cache restart/eviction is not authoritative loss and that the write bridge must be explicit.

**ADR-020 — no authoritative physical DELETE**

`docs/architecture-decisions.md:757-818` defines the no-delete invariant:

- Governed information has no physical DELETE semantics (`:805-818`).
- Lifecycle retirement/deactivation is an authoritative UPDATE (`:825-836`, `:848-870`).
- Mneme eviction/invalidation is explicitly distinct from governed deletion (`:831-846`).
- `removeWithVersion()` may be an infrastructure/cache primitive, but it must not be exposed as governed resource deletion (`:872-887`).

**Related existing documentation**

- `docs/architecture/persistence-lifecycle.md:24-29` identifies Mneme as Tier 2 and Mnemosyne/PostgreSQL as Tier 3; `:109-113` restates ADR-020.
- `docs/concepts/mneme.md:56-70` gives the Mneme ownership boundary and explicitly assigns permanent/authoritative FHIR state to Mnemosyne.
- `docs/concepts/mnemosyne.md:49-74` assigns transactions, JPA/DAO, and relational authority to Mnemosyne.
- `docs/persistence-architecture.md:124-159` provides a subsystem ownership matrix; `:224-237` describes the intended optimistic-concurrency/no-delete model.
- `docs/provider-registry/architecture.md:44-63` identifies the current Provider Registry split: Erga performs validation/persistence orchestration, Mnemosyne owns storage, and Calliope owns canonical models.
- `docs/integration/rest.md:62-133` describes the current asynchronous Pylai -> Pragma -> Petasos -> Ponos -> Mnemosyne workflow and currently documents `If-Match` as an external concern.
- `docs/persistence-recovery-gaps.md:42-66` documents existing worker coordination/recovery gaps (REC-003/REC-004) and should not be treated as the governed-write contract.

There are stale/overstated claims worth flagging in the eventual design document: `docs/persistence-architecture.md:231-233` says optimistic locking is via a JPA `@Version` column, but the inspected entity has no `@Version` annotation; and `docs/architecture/overview.md:44-57` still depicts direct Hot Rod puts and Mneme write-behind as the primary flow. These are current-baseline descriptions, not proof that the Step 08.03 protocol exists.

**3. Existing reusable context/model artifacts**

**Calliope persistence envelope**

`calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationEnvelope.java:39-70` is the closest existing cross-module contract artifact:

- It is an immutable, serializable, transport-independent envelope.
- Existing fields include `operationId`, operation type, resource type/id, `expectedVersion`, payload, originating principal/authorities, security domain, correlation ID, causation ID, timestamps, authorization decision, executing principal, and attributes (`:54-70`).
- Validation and immutable-copy behavior are implemented in the same file (`:71-170`, and derived-copy methods around `:307-330`).
- It currently explicitly supports `CREATE`, `UPDATE`, and `DELETE`, with DELETE payload semantics (`:40-47`, `:74-80`), so the Step 08.03 design should reuse its context fields where appropriate but must not blindly adopt its DELETE operation for governed clinical writes.
- `calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationType.java:18-27` currently enumerates `CREATE`, `UPDATE`, and `DELETE`.

**Themis security context**

`themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java:29-52` is an immutable serializable record containing requesting and executing principals, security domain, authorities, correlation/causation IDs, tenant/client data, request time, and immutable attributes. `ThemisAuthorizationRequest.java` and `ThemisAuthorizer.java` provide the existing default-deny authorization request contract (`themis/themis-api/.../ThemisAuthorizationRequest.java:28-52`; `.../ThemisAuthorizer.java:42-50`).

This is preferable to inventing a second security context. The eventual governed-write context can carry/refer to this existing Themis context and existing `PersistenceOperationEnvelope` provenance fields rather than duplicating them.

**Pragma/workflow context**

`calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/Pragma.java:62-124` carries correlation ID, causation ID, source, input/output, checkpoints, originating/executing principals, and security context. `ProviderRegistryChangePragma.java:71-109` exposes operation/resource/id/source/If-Match/resulting-version metadata, and `:149-189` constructs the current change request with metadata and an initial checkpoint.

This is the existing asynchronous workflow carrier, not a replacement for the future governed write API. It is a strong source of provenance/context when an Ergon workflow submits a governed operation.

**4. Likely future API placement**

**No existing governed facade**

No production interface matching a governed writer, active coordinator, conditional persistence port, or convergence port was found. The repository currently contains concrete services and persistence implementations only.

**Mneme primitive location**

- `hestia/mneme-cluster` contains no `src/main/java` production tree according to the repository search; it appears configuration/resource-oriented.
- `hestia/mneme-persistence` is an Infinispan `NonBlockingStore` adapter rather than a public domain API. `FhirRestCacheStore.java:43-64` initializes the store and REST client; `:81-97` loads from Mnemosyne; `:99-121` writes through to the REST client.
- The test-only concurrency evidence is in `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java`. It demonstrates `getWithMetadata()` and opaque entry-token reads at `:294-308`, `replaceWithVersion()` success at `:318-328`, stale-token rejection at `:330-339`, and three participants observing one starting token and attempting CAS at `:580-608`.
- This suggests future Mneme-facing primitives belong under Hestia/Mneme, but ordinary callers should not receive `RemoteCache`, `MetadataValue`, or raw Infinispan version types. The public abstraction should be a small opaque token/context interface implemented behind Hestia.

**Mnemosyne primitive location**

- `hestia/mnemosyne-clinical/.../service/FhirStorageService.java` is the current authoritative persistence entry point. It already owns Themis authorization, JSON encoding, repository access, and version assignment.
- `.../repository/FhirResourceRepository.java:27-40` is a Spring Data repository with direct `JpaRepository` access and lookup/exists methods. It is an infrastructure-only candidate for a future conditional persistence adapter, not a public caller API.
- `.../model/FhirResourceEntity.java:23-57` is the JPA entity. Its `versionId` field is at `:46-47`, but there is no inspected `@Version` annotation; this conflicts with the broader documentation claim noted above.

**Orchestration/facade candidate**

The sequence “active coordination -> authoritative conditional persistence -> guarded convergence” does not naturally belong entirely in Mneme or entirely in Mnemosyne:

- Mneme should expose only active-state read/coordination/convergence primitives.
- Mnemosyne should expose authoritative read/create/conditional-update primitives.
- A future governed facade/orchestrator should coordinate the sequence and be called by workflow/gateway services.
- Existing callers that would eventually use the facade include `energeia/erga` change activities and Pylai submission/processing paths.
- No new deployable subsystem is indicated by the current module structure; a Java contract plus implementation in an existing Hestia/appropriate orchestration module is the lower-complexity direction.

The POM graph currently makes the boundary difficult: `energeia/erga/pom.xml:44-54` directly depends on `mnemosyne-clinical`, and `:87-91` directly depends on Infinispan Hot Rod. That is an architectural bypass-enablement risk to address in the later implementation/architecture-test steps, not in Step 08.03.

**5. Current bypass risks and precise code locations**

**A. Direct unconditional RemoteCache writes**

1. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java`
    - Raw `RemoteCache` acquisition and availability handling: `:119-136`.
    - Unconditional read: `:138-149`.
    - Unconditional write API `putResourceJson`: `:152-157`.
    - `saveResource` independently manufactures/increments FHIR `meta.versionId`: `:160-188`; this is exactly the version-domain conflation the future contract must prevent.

2. `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheService.java`
    - Injects/exposes `RemoteCacheManager`: `:55-88`.
    - Direct task/provenance `remoteCache.put(...)`: `:246-276`.
    - Direct task removal: `:380-387`.
    - Direct cache clearing: `:389-394`.
    - `saveTask`/`markTaskAsProcessed` mutate cached FHIR Task representations without a governed authoritative predecessor protocol (the surrounding save path is `:210-243`).

3. Scenario evidence
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java:80-103` explicitly documents that two unconditional `RemoteCache.put` operations both succeed and last-writer-wins.
    - `MnemeFailureAndRecoveryScenarioTest.java:93-124` demonstrates direct `put` before and after participant failure and `:279-317` demonstrates direct `put` after Mneme restart. These are useful tests of raw infrastructure behavior, not governed-write semantics.

**B. Mneme write-behind bypass of authoritative protocol**

`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`:

- `write(...)` accepts any cache entry and calls `HapiFhirRestClient.saveResourceJson(...)` without an authoritative predecessor: `:99-121`.
- `delete(...)` calls the REST client's delete operation: `:124-132`.
- `containsKey(...)` checks remote persistence presence: `:134-141`.
- `clear()` only avoids clearing FHIR persistence, but is still infrastructure mutation: `:143-147`.

The store is therefore a major later bypass boundary: a cache write can currently become an unconditional Mnemosyne PUT, and a cache delete can become a persistence DELETE.

**C. Direct unconditional Mnemosyne REST mutations**

`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HapiFhirRestClient.java`:

- `saveResourceJson(...)` builds an HTTP PUT with no conditional predecessor header: `:74-95`.
- `deleteResource(...)` builds HTTP DELETE and treats 404/410 as success: `:97-116`.
- `getResourceJson(...)` handles GET/404/410: `:52-71`.

These mechanics should remain hidden behind a later Mnemosyne primitive; ordinary callers should not receive this client.

**D. Mnemosyne service semantics that do not yet satisfy the contract**

`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java`:

- CREATE accepts caller/server ID and sets version 1 initially, but if the logical resource already exists it increments and overwrites it rather than rejecting duplicate CREATE: `:143-207`, especially `:189-205`.
- UPDATE accepts an expected version but silently ignores malformed expected-version text (`NumberFormatException` is swallowed): `:230-285`, especially `:250-259`.
- UPDATE creates a new entity when no existing entity is found (`:245-267`) instead of enforcing UPDATE-only-existing semantics.
- UPDATE increments `entity.getVersionId()` in application code and saves unconditionally (`:261-284`); the conditional atomic database operation required by the future contract is not present.
- Physical/soft delete remains exposed: `:288-304`; it authorizes DELETE, sets `deleted=true`, saves, and logs “Soft-deleted”. This must remain identified as legacy code for this step, but the future governed facade must contain no delete operation.
- `authorizePersistence(...)` is the existing storage-side Themis gate: `:83-130`. This is reusable but does not provide concurrency correctness.

**E. Direct resource providers**

The HAPI providers call `FhirStorageService` directly and expose CRUD annotations. Example:

- `hestia/mnemosyne-clinical/.../provider/TaskResourceProvider.java:54-63` calls `createResource`.
- `:65-78` calls `getResource` and `updateResource` without an expected predecessor.
- `:80-86` exposes `@Delete`.

The same pattern is repeated in the other provider classes under `.../hapifhir/provider/` (Communication, Consent, DocumentReference, Endpoint, Group, HealthcareService, Location, Organization, Person, Practitioner, PractitionerRole, Provenance, RelatedPerson).

**F. Direct Ergon persistence entry point**

`energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/registry/AbstractProviderRegistryChangeErgon.java:262-285` directly commits approved resources through `FhirStorageService`:

- UPDATE calls `storageService.updateResource(resourceId, resource, ifMatch)` (`:273-278`).
- Otherwise it calls `storageService.createResource(resource)` (`:278-280`).
- Its preceding Themis persistence authorization gate is `:240-259`.
- It catches HAPI `PreconditionFailedException` and turns it into an OperationOutcome at `:305-312`, but the current model is still direct FhirStorageService access rather than a typed ActiveStateConflict/AuthoritativeStateConflict result.

**G. Current Pylai boundary and context carrier**

- `pylai/pylai-fhir-registry/.../FhirRestGatewayController.java:237-277` receives CREATE, extracts principal/authorities/security context/correlation/source headers, and submits a change request.
- `:280-323` receives UPDATE and passes HTTP `If-Match` into the submission flow.
- `ChangeRequestSubmissionService.java:102-112` accepts operation/resource/id/payload, Themis context, source/correlation, and `ifMatch`.
- `:150-176` stores these in a `ProviderRegistryChangePragma`; `:178-205` caches the initial Pragma and publishes Petasos work.

This is the likely external ingress/caller boundary, not the location for raw CAS or JPA logic.

**6. Version-domain evidence**

The repository already demonstrates the four-domain split needed by Step 08.03, but current code blurs them:

1. **Mneme/Infinispan entry version**: opaque Hot Rod token obtained from `MetadataValue.getVersion()` in `MnemeConcurrencyAndVersionScenarioTest.java:294-308`; compared by `replaceWithVersion` at `:318-339`. The test itself records this separately from FHIR `meta.versionId`.
2. **Mnemosyne persistence version**: `FhirResourceEntity.versionId` at `FhirResourceEntity.java:46-47`, manipulated by `FhirStorageService` at `:193-199` and `:261-279`.
3. **FHIR `Resource.meta.versionId`**: manufactured by current cache service (`FhirCacheService.java:181-188`) and Mnemosyne service (`FhirStorageService.java:172-185`, `:269-279`).
4. **HTTP ETag/If-Match**: accepted at Pylai controller `FhirRestGatewayController.java:283-311`, stored in Pragma metadata by `ProviderRegistryChangePragma.java:170-175`, and parsed by `FhirStorageService.java:250-259`.

The test’s explicit “opaque token”/separate FHIR version treatment is the best existing evidence for the contract correction: no application arithmetic should be assigned to the Infinispan token, and `replaceWithVersion` must not be presented as a FHIR version comparison.

**7. Recommended scope for the Step 08.03 design document**

The document should identify only a small conceptual set of contracts, with implementation ownership mapped as follows:

- Public governed facade/orchestrator: an existing service/API boundary usable by Pylai and Ergon/Ponos callers; no new deployable subsystem.
- Mneme internal primitive: opaque active token read/consume/CAS and guarded convergence/invalidation; no clinical authority and no raw `RemoteCache` exposure.
- Mnemosyne internal primitive: authoritative read/create/conditional-update and authoritative result/version; no cache mechanics.
- Calliope: reuse canonical `PersistenceOperationEnvelope`, `Pragma`, FHIR/security models, and avoid adding infrastructure mechanics to Calliope.
- Themis: reuse `ThemisSecurityContext`, principal, authority, and authorization decision models.
- Pylai: owns HTTP `ETag`/`If-Match` mapping only; it should not define the internal predecessor model.
- Ergon/Ponos: caller/workflow integration only; no direct RemoteCache or direct unconditional persistence in the eventual safe path.

The document should explicitly call out the existing concrete classes above as migration/bypass targets and state that no implementation restrictions are being applied in Step 08.03.

**Discoveries for future steps**

- No production `mneme-cluster/src/main/java` exists; future Mneme contracts likely need a new API/service surface within existing Hestia modules rather than extending a current service.
- `PersistenceOperationEnvelope` already carries most required provenance/security/idempotency fields but currently models DELETE; reuse must be selective.
- The strongest CAS evidence is test-only: `MnemeConcurrencyAndVersionScenarioTest.java:294-339` and `:580-608`; it clearly separates opaque Infinispan tokens from FHIR `meta.versionId`.
- Current authoritative write code is not atomic/strict: CREATE upserts existing IDs, UPDATE creates missing IDs, malformed expected versions are ignored, and `FhirResourceEntity.versionId` lacks the documented `@Version` annotation.
- `FhirRestCacheStore.write()` -> `HapiFhirRestClient.saveResourceJson()` is the most consequential hidden bypass because cache write-behind currently performs unconditional authoritative PUT.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Constraints**  
Architecture/design deliverable only. The plan must stay strictly within the task boundary: produce the Step 08.03 governed write/concurrency contract design document, and only any minimal documentation-index adjustment if truly needed because the destination path is new. Do not include production code, tests, cache config, schema, or implementation work. Emphasize incorporation of the 08.02B corrections from the attached report, explicit handling of contract sections 1-19 from the issue, and a reviewable doc-first workflow.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

**Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



REVIEW THE PLAN

**Task description**  
Review the proposed delivery plan.

**Feedback**  
Focus on whether a single-step doc-authoring plan is sufficient for this architecture-only task, whether it keeps implementation strictly out of scope, and whether it covers the need to incorporate the Step 08.02B corrections plus all required contract sections and examples/test plan from the issue.



Plan Approved

**Verification**
- Confirmed `docs/design/` does not exist in the repository — the plan's choice to create a new `docs/design/` directory (rather than misusing `docs/architecture.md`, which is Petasos-specific) is correct and grounded.
- Confirmed `docs/README.md` contains a "Documentation Information Architecture" tree (lines 9–139); the plan's step to register the new document there is accurate and necessary.
- Verified all referenced artifacts exist at the claimed paths: `ThemisSecurityContext` (themis-api), `PersistenceOperationEnvelope` (calliope), and the four bypass classes (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`).
- Verified the Verification command (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" ...`) is the canonical AGENTS.md architecture command and the seven `*ArchitectureTest` classes exist — it is a valid regression guard confirming no production code was disturbed.
- Scope discipline: plan's "Out of Scope" and per-step acceptance criteria mirror the parent task's MUST-NOT list (no production Java, no Infinispan XML, no `@Version`, no schema/JPA changes, no DELETE removal). Content-verification is handled via the acceptance-criteria checklist plus a git-status check for zero modified production files.

**Feedback focus assessment**
1. **Single-step sufficiency**: Appropriate. The deliverable is one design document — a single checkable unit. Splitting it artificially would add ceremony without value.
2. **Implementation kept out of scope**: Yes. Doc-first guardrail is explicit; conceptual Java appears only as Markdown code blocks; verification includes a git-status check that no production files changed.
3. **08.02B corrections + all sections + examples/test plan**: All four corrections (opaque token / no arithmetic, Mneme non-authority, CAS convergence loop, deferral of clinical consistency tiers) are explicitly listed in decisions and step acceptance criteria. All 19 doc sections, the 16-scenario contract test plan, the 4-domain version model, conflict/result models, and developer usage examples are enumerated.

**Issues**  
None blocking. The done-signal for a doc-only task cannot automatically verify prose content; the plan appropriately compensates with a detailed acceptance-criteria checklist and a git-status no-production-change check, which is proportionate for a Simple architecture/design deliverable.

**Notes**
- The Verification `mvn` command will trivially pass since no code is changed; its real value is confirming the doc author did not accidentally touch production/config — the plan correctly pairs it with the git-status check for that purpose.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

*** Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Priorities:
- Produce the Step 08.03 architecture/design document only, with strong fidelity to the issue sections and implementation boundary.
- Incorporate the 08.02B corrections explicitly: no arithmetic meaning assigned to Infinispan entry versions; Mneme token consumption is not authority to update; guarded convergence requires a real CAS/reread loop contract; do not classify resource-specific consistency tiers in this step.
- Keep the contract small and hard to misuse. Prefer a handful of concepts over elaborate hierarchies.
- Reuse existing repository context models where appropriate instead of inventing duplicates, especially for security/provenance/correlation context.
- Explicitly call out current bypass points and recommended future prevention mechanisms, but do not implement those restrictions now.
- Because `docs/design/` is new, update `docs/README.md` only as minimally necessary to register the new document.

Known from previous steps:
- ADR-018 authoritative boundary: `docs/architecture-decisions.md:136-233`.
- ADR-019 Mneme coordination boundary: `docs/architecture-decisions.md:420-710`.
- ADR-020 no authoritative physical DELETE: `docs/architecture-decisions.md:757-887`.
- Reusable context artifacts: `calliope/.../PersistenceOperationEnvelope.java`, `themis/themis-api/.../ThemisSecurityContext.java`, `calliope/.../Pragma.java`.
- Current bypass/legacy risk points: `iris/.../FhirCacheService.java`, `energeia/erga/.../TaskCacheService.java`, `hestia/mneme-persistence/.../FhirRestCacheStore.java`, `hestia/mnemosyne-clinical/.../FhirStorageService.java`, and HAPI providers under `.../hapifhir/provider/`.
- Attached 08.02B report already established that current Hot Rod CAS evidence exists in Mneme lab tests, while production code still uses unconditional writes.

Execution discipline:
- Start with narrow file inspections only.
- Use small command timeouts (60-120s max per command) if you run shell commands.
- Prefer targeted verification: check modified files and repository status; only run heavier commands if truly needed.
- Report any commands run and relevant observations in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Governed Write and Concurrency Contract Design Specification (Step 08.03)**

**Summary of Accomplishments**

1. **Authored Authoritative Design Specification**: Created [`docs/design/governed-write-concurrency-contract.md`](/Users/markhunter/Development/SourceCode/Projects/github/harmonia/docs/design/governed-write-concurrency-contract.md), establishing the definitive Strong Hybrid persistence and concurrency architecture contract between caller workflows (Pylai, Energeia Ponos/Erga/Praxis, Iris BEFE) and the storage tiers: **Mneme** (distributed cache & active coordination grid) and **Mnemosyne** (authoritative relational JPA persistence).

2. **Full Section Coverage Across All 19 Required Domains**:
    - **Section 1: Purpose** — Standardizing write and concurrency architecture across the platform.
    - **Section 2: Architectural Context** — Grounded in Strong Hybrid model, ADR-018 (Mnemosyne durable boundary), ADR-019 (Mneme distributed coordination), and ADR-020 (lifecycle transitions vs physical DELETE).
    - **Section 3: Core Architectural Invariants** — Formally defined INV-01 through INV-05 (separation of coordination vs authority, monotonic authoritative progression, guarded convergence, commit visibility over degraded convergence, zero-delete lifecycle).
    - **Section 4: Governed Read Model** — Conceptual Java records (`GovernedRead<T>`, `ResourceKey`, `ActiveCoordinationToken`, `ExpectedAuthoritativeVersion`) and cold-read/warm-read semantics.
    - **Section 5: CREATE Contract** — Strict uniqueness semantics (no upsert, rejection of duplicate keys with `AuthoritativeStateConflictException`), and 5-step create lifecycle.
    - **Section 6: UPDATE Contract** — Complete 5-phase end-to-end execution sequence (Read, Active Coordination CAS, Themis Validation, Authoritative Commit, Guarded Convergence).
    - **Section 7: Active Coordination Contract** — Formally incorporated Task 08.02B corrections: opaque Hot Rod tokens without arithmetic meaning (`token + 1` prohibited), active token consumption is not authority to bypass database predecessor checks, and failure handling.
    - **Section 8: Authoritative Persistence Contract** — `MnemosynePersistencePort` interface and atomic SQL conditional update specification (`WHERE id=:id AND version_id=:expectedVersion`).
    - **Section 9: Conflict Model** — Distinct taxonomy separating `ActiveStateConflict` (in-memory contention, fast retry) from `AuthoritativeStateConflict` (durable state advanced, abort/re-read) and `CommitOutcomeUnknown` (ambiguous transport timeout).
    - **Section 10: Result Model** — `WriteResult<T>`, `WriteStatus`, and `ConvergenceStatus`, specifying that durable database commit success with degraded cache convergence reports success (`COMMITTED_CONVERGENCE_DEGRADED`) to prevent duplicate caller retries.
    - **Section 11: Version Model** — Formal 4-domain matrix separating Mneme entry tokens, Mnemosyne DB versions, FHIR `meta.versionId`, and HTTP ETags (`If-Match`), with explicit conflation prohibitions.
    - **Section 12: Guarded Mneme Convergence** — CAS convergence loop algorithm with re-read and enforcement of the Newer-Version Invariant ($V_{cache} \ge V_{committed}$).
    - **Section 13: Commit Outcome Semantics** — Handling ambiguous network timeouts via `CommitOutcomeUnknownException` and idempotent correlation/causation reconciliation without guessing outcomes.
    - **Section 14: Security and Provenance Context** — Reusing existing models (`ThemisSecurityContext` and `PersistenceOperationEnvelope` / `Pragma` context fields) without duplicate context classes.
    - **Section 15: API Placement and Service Boundaries** — Clean separation of contracts (`calliope` / `hestia-api`), active coordinator (`hestia/mneme-*`), persistence port (`hestia/mnemosyne-*`), and orchestrator facade (`hestia/hestia-governance`).
    - **Section 16: Bypass Prevention and Legacy Remediation** — Detailed catalog of existing bypass points (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`, HAPI providers) and future ArchUnit rule definitions.
    - **Section 17: Developer Usage Examples** — Idiomatic Java code snippets for Governed CREATE, Governed UPDATE with retry, and conflict handling.
    - **Section 18: Contract Test Plan** — 16 mandatory verification scenarios (ST-01 to ST-16) detailing preconditions, execution sequence, expected results, and mandatory assertions.
    - **Section 19: Deferred Questions and Future Extensions** — Formally deferred resource-specific clinical consistency tiers (per Task 08.02B review), semantic 3-way merges, distributed multi-resource sagas, and physical retention purges.
    - **Section 20: References** — Links to authoritative ADRs, concepts, and persistence lifecycle documents.

3. **Updated Documentation Information Architecture**: Updated [`docs/README.md`](/Users/markhunter/Development/SourceCode/Projects/github/harmonia/docs/README.md) to register the new `docs/design/` directory and index `governed-write-concurrency-contract.md`.

4. **Zero Production Code Alterations**: No production `.java` source files, schemas, XML descriptors, or test configurations were modified.

5. **Verification**: Executed ArchUnit architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`), passing 57/57 tests with 0 failures, 0 errors.

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` completes in ~10 seconds across all 33 Maven modules.
- **Recommendations**: When implementing the Java interfaces and adapters in subsequent steps, place pure interfaces in `hestia-api` (or `calliope`) and encapsulate `RemoteCacheManager` and `FhirResourceRepository` behind package-private ports to eliminate direct bypass risks.
- **Fragile APIs**: Existing `FhirStorageService` methods swallow `NumberFormatException` during `If-Match` parsing and perform upserts on CREATE; these should be replaced with strict conditional operations conforming to `MnemosynePersistencePort`.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Please verify the architecture/design document and docs index update against the issue requirements. Focus especially on:
- whether the document truly stays within the implementation boundary (no production-code changes disguised as design);
- whether it explicitly incorporates the 08.02B corrections: opaque Infinispan token with no arithmetic meaning, Mneme token consumption is not authority to update, guarded convergence requires a true CAS/reread loop, and no resource-specific consistency tier classifications in this step;
- whether all required contract areas are covered with correct semantics: governed read, CREATE, UPDATE, active coordination, authoritative persistence, conflict model, result model, version model, commit-outcome-unknown, security/provenance context, API placement, bypass prevention, developer examples, and contract test plan;
- whether the new `docs/design/` path is properly integrated in `docs/README.md`.
  If there are issues, separate real defects from cosmetic preferences.



Implementation Approved

**Verification**
- **README integration** (`docs/README.md`): The diff cleanly inserts a `design/` node into the documentation tree with a description and a link to `governed-write-concurrency-contract.md`, placed logically between `architecture/` and `middleware/`. Correct and minimal.
- **Document completeness** (`docs/design/governed-write-concurrency-contract.md`, 794 lines): All 19 required sections are present (1 Purpose … 19 Deferred Questions), plus a References section. Structure matches the plan's section-by-section mapping.
- **Task 08.02B corrections explicitly incorporated**:
    - Opaque token, no arithmetic meaning — §7.1 and §11.2 (`token + 1` explicitly prohibited); record javadoc in §4.1 also states it.
    - Mneme token consumption ≠ authority to update — §7.2 and INV-01 (§3).
    - Guarded convergence via true CAS/re-read loop — §12.1 provides a real `getWithMetadata` → newer-version check → `replaceWithVersion` retry loop with newer-version invariant and exhaustion/invalidation handling.
    - Deferral of resource-specific clinical consistency tiers — §19.1 explicitly defers them; no tier classification appears elsewhere.
- **Required contract areas covered with correct semantics**: Governed read (§4, cold/warm), CREATE strict-no-upsert (§5), UPDATE 5-phase sequence (§6), active coordination + failure modes (§7), authoritative conditional SQL update with row-count semantics (§8), 3-way conflict taxonomy (§9), result model with `WriteStatus`/`ConvergenceStatus` and commit-success-with-degraded-convergence INV-04 (§10), 4-domain version model with ownership/lifetime/comparison matrix and conflation prohibitions (§11), CommitOutcomeUnknown protocol + idempotent reconciliation (§13), reuse of `ThemisSecurityContext` for security/provenance (§14), API placement (§15), bypass catalog + ArchUnit strategy (§16), developer examples (§17), and all 16 contract test scenarios ST-01…ST-16 with preconditions/expected outcomes/assertions (§18).
- **Conceptual Java shapes present**: `GovernedRead`, `ResourceKey`, `ActiveCoordinationToken`, `ExpectedAuthoritativeVersion`, `GovernedWriter`, `GovernedWriteContext`, `WriteResult`, `MnemosynePersistencePort`, token/version records — all as Markdown code blocks only.
- **Doc-first guardrail honored**: Executor's own diff touched only `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`, and a `.junie/plans` file. No production `.java`, schema, or config was modified by this step. (Other modified files in the working tree — test java, latex, architecture-decisions.md — are pre-existing changes from earlier steps, not this executor's work.)
- **Referenced links resolve**: `architecture-decisions.md`, `architecture/persistence-lifecycle.md`, `concepts/mneme.md`, `concepts/mnemosyne.md`, `security/architecture.md` all exist; ADR-018/019/020 headings exist.
- **Regression**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → BUILD SUCCESS, 57/57 tests pass (0 failures/errors). Ran ~10–20s after build.

**Issues**  
None blocking.

**Notes**
- Minor (non-blocking) doc-link concern: the ADR-018 link `#adr-018--mnemosyne-defines-...` uses two hyphens, but the ADR-018 heading in `architecture-decisions.md` uses three literal hyphens (`ADR-018 --- Mnemosyne ...`), whereas ADR-019/020 use an em-dash. On GitHub's anchor algorithm the ADR-018 slug would contain five hyphens, so that single anchor link may not resolve. ADR-019/020 links are fine. This is a pre-existing inconsistency in the source heading, not a contract-content defect; worth a trivial fixup in a later pass.
- The working tree contains many unrelated modified files (mneme-cluster test java, latex chapters, architecture-decisions.md). These are outside Step 1's scope and were not produced by this executor's diff; they do not affect this step's verdict, and the ArchUnit suite remains green regardless.

**Discoveries for future steps**
- ArchUnit regression command runs in ~10–20s once modules are built; a 600s timeout is far more than needed — ~120s is sufficient on warm cache. Full suite = 57 tests across 7 ArchitectureTest classes.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

**✓ Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Authored the Step 08.03 governed write/concurrency contract design at `docs/design/governed-write-concurrency-contract.md`, defining the Strong Hybrid contract between Mneme coordination and Mnemosyne authoritative persistence.
- Kept the work strictly architecture-only: no production Java, schema, cache config, or protocol implementation changes were made.

**Changes**
- Added the full design document covering purpose, invariants, governed read/CREATE/UPDATE contracts, active coordination, authoritative persistence, conflict/result/version models, guarded convergence, commit-outcome semantics, security/provenance context, API placement, bypass prevention, developer examples, test plan, and deferred questions.
- Incorporated the Step 08.02B corrections explicitly: opaque Mneme token semantics, Mneme non-authority, real CAS/reread convergence requirements, and deferral of resource-specific consistency tiers.
- Updated `docs/README.md` to register the new `docs/design/` documentation location and link the contract document.

**Verification**
- Reviewed the documentation changes against the issue requirements; reviewer found no blocking issues.
- Ran `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` successfully (57/57 tests passed).

**Notes**
- No PR/branch workflow was activated because the task did not request shipping this work via a dedicated branch or pull request.

This task has no prior planning phase. Before implementation, analyze the task and codebase, define acceptance criteria if not explicitly provided in the task description, and plan your approach.   
The Reviewer must independently define its own acceptance criteria and will verify them.

**Step 2: Update / Follow-up**  
You have executed the wrong task.

The work just completed is effectively a refinement/re-run of Step 08.03.  
Retain any useful documentation corrections you made, but DO NOT continue  
working on the 08.03 design document.

We are now performing:

HARMONIA — TASK 08  
STEP 08.04A — FOUNDATIONAL GOVERNED-WRITE CONTRACTS

The purpose of 08.04A is specifically to IMPLEMENT the small Java  
caller-facing contract so that we can inspect the actual API before  
implementing the runtime protocol.

This step MUST create production Java contract types and their unit tests.

It MUST NOT implement Mneme CAS, Mnemosyne conditional persistence,  
convergence, or migrate any existing write path.


IMPLEMENT ONLY THE MINIMUM CONTRACT VOCABULARY

First inspect the existing module dependency graph and choose the smallest  
existing suitable module/package.

DO NOT create:

    hestia-governance
    hestia-api
    a new Maven module
    a new subsystem
    a new deployable service

If no existing module can host the contract without an unacceptable  
dependency cycle, STOP and report that problem rather than inventing a module.


Implement approximately the following concepts, simplifying where possible:

    ResourceKey

    ActiveCoordinationToken

    AuthoritativeVersion
        or ExpectedAuthoritativeVersion

    GovernedRead<T>

    GovernedWriteContext
        ONLY if actually required;
        reuse existing ThemisSecurityContext /
        PersistenceOperationEnvelope / Pragma context rather than
        duplicating correlation, causation, principal or provenance fields

    GovernedWriter

    WriteResult<T>

    minimal conflict/precondition types


REQUIRED SEMANTICS

ActiveCoordinationToken:

    immutable
    opaque
    no arithmetic
    no ordering
    no Infinispan/Hot Rod type exposed

GovernedRead<T>:

    resource key
    resource
    active coordination token
    authoritative predecessor/version

GovernedWriter:

    CREATE
    UPDATE

There is NO governed DELETE.

UPDATE should accept the GovernedRead<T> so the caller does not manually  
assemble concurrency tokens.


AUTHORITATIVE PRECONDITION MODEL

Do NOT collapse these into one vague conflict:

    RESOURCE_ALREADY_EXISTS

        CREATE expected authoritative absence.

    EXPECTED_VERSION_MISMATCH

        UPDATE expected authoritative predecessor X.

They may share a small conceptual AuthoritativePreconditionConflict model.

Keep:

    ActiveStateConflict

separate.


AUTHORITATIVE COMMIT OUTCOME

Represent explicitly:

    COMMITTED
    NOT_COMMITTED
    UNKNOWN

UNKNOWN is first-class.

Do NOT reduce it to:

    success = false

and do not expose it merely as a generic exception that encourages blind  
retry.


WRITE RESULT

Design WriteResult<T> so invalid state combinations are difficult to create.

It must cleanly represent:

    successful authoritative commit

    active-state conflict

    authoritative precondition conflict

    known non-commit/failure

    authoritative outcome UNKNOWN

    committed but Mneme convergence degraded

Prefer a small sealed result model if that makes the invariants clearer.

Do not create a large nullable status bag.


TESTS

Add unit tests demonstrating the ACTUAL API:

1. GovernedRead carries active and authoritative concurrency context.
2. ActiveCoordinationToken is opaque.
3. CREATE conflict can express RESOURCE_ALREADY_EXISTS.
4. UPDATE conflict can express EXPECTED_VERSION_MISMATCH.
5. Active conflict is distinct from authoritative conflict.
6. COMMITTED / NOT_COMMITTED / UNKNOWN are distinct.
7. committed + degraded convergence is representable as committed.
8. no governed DELETE exists.
9. invalid WriteResult combinations cannot readily be constructed.


ARCHITECTURE GUARDRAILS

Add only low-risk architecture tests appropriate to the new contract:

    no Infinispan dependency exposed
    no RemoteCache exposed
    no JPA dependency exposed
    no HTTP framework dependency exposed
    no governed DELETE

Do NOT block or migrate existing legacy write paths yet.


DO NOT IMPLEMENT

    Mneme CAS
    coordination cache
    guarded convergence
    Mnemosyne conditional SQL/JPA
    @Version
    database/schema changes
    BEFE migration
    Pylai migration
    Ergon/Ponos migration
    retries
    semantic conflict handling
    information-authority handling
    DELETE removal


REQUIRED FINAL OUTPUT

Show me the ACTUAL resulting Java API, including package names.

Show examples using the ACTUAL types for:

    CREATE
    UPDATE
    ActiveStateConflict
    ResourceAlreadyExists
    ExpectedVersionMismatch
    CommitOutcomeUnknown
    CommittedButConvergenceDegraded

List every production Java file and test file added or changed.

Explain why the selected existing module/package is the correct dependency  
location.

Run the relevant unit tests and architecture tests.

Explicitly confirm:

    no write path was migrated;
    no new Maven module was created;
    no new subsystem was created;
    no runtime concurrency mechanism was implemented.

STOP after 08.04A.

Do not begin 08.04B.



**Delivery Steps**

**✓ Step 1: Implementation**  
Optional spending limit; leave empty for no limit: 50  
Required for Goal Mode: Auto  
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Map the existing repository locations and artifacts needed to produce the Step 08.03 governed write/concurrency contract design document. Focus on: (1) current architecture/design documentation structure and the best destination path for the new document; (2) existing ADR or design docs related to ADR-018/019/020 and governed writes; (3) existing Java packages/interfaces that would likely host a future governed write facade versus Mneme/Mnemosyne primitives; (4) current code paths that illustrate bypass risks (direct RemoteCache writes, direct persistence update entry points). Provide precise file paths and line references where possible. Read-only only.



Exploration is Ready

Read-only repository exploration completed; no files were modified and the Step 08.03 design document was not created (implementation/document-authoring belongs to the parent task).

**1. Documentation structure and recommended destination**

- `docs/README.md:9-60` is the master documentation information architecture. It has separate `concepts/`, `modules/`, `architecture/`, `integration/`, `security/`, and provider-registry sections, but no `docs/design/` section.
- A glob check for `docs/design/**/*.md` confirmed that `docs/design/` does not currently exist.
- The requested destination `docs/design/governed-write-concurrency-contract.md` is therefore a new, sensible architecture/design location rather than an existing convention. The document should either create that directory and then add it to the documentation index, or, if avoiding a new category, use `docs/architecture/` alongside `persistence-lifecycle.md`; the explicit task request strongly favors `docs/design/`.
- Existing persistence and architecture documents that should be cross-linked from the new document:
    - `docs/architecture-decisions.md`
    - `docs/architecture/persistence-lifecycle.md`
    - `docs/persistence-architecture.md`
    - `docs/concepts/mneme.md`
    - `docs/concepts/mnemosyne.md`
    - `docs/provider-registry/architecture.md`
    - `docs/integration/rest.md`
    - `docs/persistence-recovery-gaps.md`

Note: `docs/architecture.md` is actually a Petasos architecture document despite its generic filename; it is not the best home for this contract.

**2. ADR and existing design baseline**

**ADR-018 — Mnemosyne authoritative durable boundary**

`docs/architecture-decisions.md:136-233` establishes that:

- Mneme/Infinispan is distributed, reconstructable, non-authoritative state (`:226-233`).
- Mnemosyne/PostgreSQL is the authoritative durable application-state boundary (`:201-224`).
- Cache presence, replication, or a successful cache operation is not durable acceptance (`:206-224`).
- The responsibility matrix at `:235-262` separates Mneme distributed cache/access, Mnemosyne durable persistence, Petasos durable work transfer, and Kleio evidence.
- Failure semantics at `:306-340` require persistence failure to remain visible; local memory or asynchronous persistence attempts must not turn unavailable durability into apparent success.

**ADR-019 — Mneme distributed access and coordination**

`docs/architecture-decisions.md:420-512` is the primary source for the future Mneme primitive:

- Mneme owns distributed resource access and coordination, not distributed durability (`:475-503`).
- The explicit division is “Mneme coordinates active distributed state; Mnemosyne commits authoritative durable state” (`:505-512`).
- `:545-562` says concurrency/version coordination must be distributed and substantially transparent to ordinary client code, while authoritative persisted-version semantics remain Mnemosyne’s responsibility.
- `:581-607` prohibits silently replacing Mneme with process-local maps/collections when Mneme is unavailable.
- `:609-646` sketches the cache hit/miss relationship and distinguishes active coordination from authoritative write.
- `:648-710` reinforces that cache restart/eviction is not authoritative loss and that the write bridge must be explicit.

**ADR-020 — no authoritative physical DELETE**

`docs/architecture-decisions.md:757-818` defines the no-delete invariant:

- Governed information has no physical DELETE semantics (`:805-818`).
- Lifecycle retirement/deactivation is an authoritative UPDATE (`:825-836`, `:848-870`).
- Mneme eviction/invalidation is explicitly distinct from governed deletion (`:831-846`).
- `removeWithVersion()` may be an infrastructure/cache primitive, but it must not be exposed as governed resource deletion (`:872-887`).

**Related existing documentation**

- `docs/architecture/persistence-lifecycle.md:24-29` identifies Mneme as Tier 2 and Mnemosyne/PostgreSQL as Tier 3; `:109-113` restates ADR-020.
- `docs/concepts/mneme.md:56-70` gives the Mneme ownership boundary and explicitly assigns permanent/authoritative FHIR state to Mnemosyne.
- `docs/concepts/mnemosyne.md:49-74` assigns transactions, JPA/DAO, and relational authority to Mnemosyne.
- `docs/persistence-architecture.md:124-159` provides a subsystem ownership matrix; `:224-237` describes the intended optimistic-concurrency/no-delete model.
- `docs/provider-registry/architecture.md:44-63` identifies the current Provider Registry split: Erga performs validation/persistence orchestration, Mnemosyne owns storage, and Calliope owns canonical models.
- `docs/integration/rest.md:62-133` describes the current asynchronous Pylai -> Pragma -> Petasos -> Ponos -> Mnemosyne workflow and currently documents `If-Match` as an external concern.
- `docs/persistence-recovery-gaps.md:42-66` documents existing worker coordination/recovery gaps (REC-003/REC-004) and should not be treated as the governed-write contract.

There are stale/overstated claims worth flagging in the eventual design document: `docs/persistence-architecture.md:231-233` says optimistic locking is via a JPA `@Version` column, but the inspected entity has no `@Version` annotation; and `docs/architecture/overview.md:44-57` still depicts direct Hot Rod puts and Mneme write-behind as the primary flow. These are current-baseline descriptions, not proof that the Step 08.03 protocol exists.

**3. Existing reusable context/model artifacts**

**Calliope persistence envelope**

`calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationEnvelope.java:39-70` is the closest existing cross-module contract artifact:

- It is an immutable, serializable, transport-independent envelope.
- Existing fields include `operationId`, operation type, resource type/id, `expectedVersion`, payload, originating principal/authorities, security domain, correlation ID, causation ID, timestamps, authorization decision, executing principal, and attributes (`:54-70`).
- Validation and immutable-copy behavior are implemented in the same file (`:71-170`, and derived-copy methods around `:307-330`).
- It currently explicitly supports `CREATE`, `UPDATE`, and `DELETE`, with DELETE payload semantics (`:40-47`, `:74-80`), so the Step 08.03 design should reuse its context fields where appropriate but must not blindly adopt its DELETE operation for governed clinical writes.
- `calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationType.java:18-27` currently enumerates `CREATE`, `UPDATE`, and `DELETE`.

**Themis security context**

`themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java:29-52` is an immutable serializable record containing requesting and executing principals, security domain, authorities, correlation/causation IDs, tenant/client data, request time, and immutable attributes. `ThemisAuthorizationRequest.java` and `ThemisAuthorizer.java` provide the existing default-deny authorization request contract (`themis/themis-api/.../ThemisAuthorizationRequest.java:28-52`; `.../ThemisAuthorizer.java:42-50`).

This is preferable to inventing a second security context. The eventual governed-write context can carry/refer to this existing Themis context and existing `PersistenceOperationEnvelope` provenance fields rather than duplicating them.

**Pragma/workflow context**

`calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/Pragma.java:62-124` carries correlation ID, causation ID, source, input/output, checkpoints, originating/executing principals, and security context. `ProviderRegistryChangePragma.java:71-109` exposes operation/resource/id/source/If-Match/resulting-version metadata, and `:149-189` constructs the current change request with metadata and an initial checkpoint.

This is the existing asynchronous workflow carrier, not a replacement for the future governed write API. It is a strong source of provenance/context when an Ergon workflow submits a governed operation.

**4. Likely future API placement**

**No existing governed facade**

No production interface matching a governed writer, active coordinator, conditional persistence port, or convergence port was found. The repository currently contains concrete services and persistence implementations only.

**Mneme primitive location**

- `hestia/mneme-cluster` contains no `src/main/java` production tree according to the repository search; it appears configuration/resource-oriented.
- `hestia/mneme-persistence` is an Infinispan `NonBlockingStore` adapter rather than a public domain API. `FhirRestCacheStore.java:43-64` initializes the store and REST client; `:81-97` loads from Mnemosyne; `:99-121` writes through to the REST client.
- The test-only concurrency evidence is in `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java`. It demonstrates `getWithMetadata()` and opaque entry-token reads at `:294-308`, `replaceWithVersion()` success at `:318-328`, stale-token rejection at `:330-339`, and three participants observing one starting token and attempting CAS at `:580-608`.
- This suggests future Mneme-facing primitives belong under Hestia/Mneme, but ordinary callers should not receive `RemoteCache`, `MetadataValue`, or raw Infinispan version types. The public abstraction should be a small opaque token/context interface implemented behind Hestia.

**Mnemosyne primitive location**

- `hestia/mnemosyne-clinical/.../service/FhirStorageService.java` is the current authoritative persistence entry point. It already owns Themis authorization, JSON encoding, repository access, and version assignment.
- `.../repository/FhirResourceRepository.java:27-40` is a Spring Data repository with direct `JpaRepository` access and lookup/exists methods. It is an infrastructure-only candidate for a future conditional persistence adapter, not a public caller API.
- `.../model/FhirResourceEntity.java:23-57` is the JPA entity. Its `versionId` field is at `:46-47`, but there is no inspected `@Version` annotation; this conflicts with the broader documentation claim noted above.

**Orchestration/facade candidate**

The sequence “active coordination -> authoritative conditional persistence -> guarded convergence” does not naturally belong entirely in Mneme or entirely in Mnemosyne:

- Mneme should expose only active-state read/coordination/convergence primitives.
- Mnemosyne should expose authoritative read/create/conditional-update primitives.
- A future governed facade/orchestrator should coordinate the sequence and be called by workflow/gateway services.
- Existing callers that would eventually use the facade include `energeia/erga` change activities and Pylai submission/processing paths.
- No new deployable subsystem is indicated by the current module structure; a Java contract plus implementation in an existing Hestia/appropriate orchestration module is the lower-complexity direction.

The POM graph currently makes the boundary difficult: `energeia/erga/pom.xml:44-54` directly depends on `mnemosyne-clinical`, and `:87-91` directly depends on Infinispan Hot Rod. That is an architectural bypass-enablement risk to address in the later implementation/architecture-test steps, not in Step 08.03.

**5. Current bypass risks and precise code locations**

**A. Direct unconditional RemoteCache writes**

1. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java`
    - Raw `RemoteCache` acquisition and availability handling: `:119-136`.
    - Unconditional read: `:138-149`.
    - Unconditional write API `putResourceJson`: `:152-157`.
    - `saveResource` independently manufactures/increments FHIR `meta.versionId`: `:160-188`; this is exactly the version-domain conflation the future contract must prevent.

2. `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheService.java`
    - Injects/exposes `RemoteCacheManager`: `:55-88`.
    - Direct task/provenance `remoteCache.put(...)`: `:246-276`.
    - Direct task removal: `:380-387`.
    - Direct cache clearing: `:389-394`.
    - `saveTask`/`markTaskAsProcessed` mutate cached FHIR Task representations without a governed authoritative predecessor protocol (the surrounding save path is `:210-243`).

3. Scenario evidence
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java:80-103` explicitly documents that two unconditional `RemoteCache.put` operations both succeed and last-writer-wins.
    - `MnemeFailureAndRecoveryScenarioTest.java:93-124` demonstrates direct `put` before and after participant failure and `:279-317` demonstrates direct `put` after Mneme restart. These are useful tests of raw infrastructure behavior, not governed-write semantics.

**B. Mneme write-behind bypass of authoritative protocol**

`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`:

- `write(...)` accepts any cache entry and calls `HapiFhirRestClient.saveResourceJson(...)` without an authoritative predecessor: `:99-121`.
- `delete(...)` calls the REST client's delete operation: `:124-132`.
- `containsKey(...)` checks remote persistence presence: `:134-141`.
- `clear()` only avoids clearing FHIR persistence, but is still infrastructure mutation: `:143-147`.

The store is therefore a major later bypass boundary: a cache write can currently become an unconditional Mnemosyne PUT, and a cache delete can become a persistence DELETE.

**C. Direct unconditional Mnemosyne REST mutations**

`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HapiFhirRestClient.java`:

- `saveResourceJson(...)` builds an HTTP PUT with no conditional predecessor header: `:74-95`.
- `deleteResource(...)` builds HTTP DELETE and treats 404/410 as success: `:97-116`.
- `getResourceJson(...)` handles GET/404/410: `:52-71`.

These mechanics should remain hidden behind a later Mnemosyne primitive; ordinary callers should not receive this client.

**D. Mnemosyne service semantics that do not yet satisfy the contract**

`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java`:

- CREATE accepts caller/server ID and sets version 1 initially, but if the logical resource already exists it increments and overwrites it rather than rejecting duplicate CREATE: `:143-207`, especially `:189-205`.
- UPDATE accepts an expected version but silently ignores malformed expected-version text (`NumberFormatException` is swallowed): `:230-285`, especially `:250-259`.
- UPDATE creates a new entity when no existing entity is found (`:245-267`) instead of enforcing UPDATE-only-existing semantics.
- UPDATE increments `entity.getVersionId()` in application code and saves unconditionally (`:261-284`); the conditional atomic database operation required by the future contract is not present.
- Physical/soft delete remains exposed: `:288-304`; it authorizes DELETE, sets `deleted=true`, saves, and logs “Soft-deleted”. This must remain identified as legacy code for this step, but the future governed facade must contain no delete operation.
- `authorizePersistence(...)` is the existing storage-side Themis gate: `:83-130`. This is reusable but does not provide concurrency correctness.

**E. Direct resource providers**

The HAPI providers call `FhirStorageService` directly and expose CRUD annotations. Example:

- `hestia/mnemosyne-clinical/.../provider/TaskResourceProvider.java:54-63` calls `createResource`.
- `:65-78` calls `getResource` and `updateResource` without an expected predecessor.
- `:80-86` exposes `@Delete`.

The same pattern is repeated in the other provider classes under `.../hapifhir/provider/` (Communication, Consent, DocumentReference, Endpoint, Group, HealthcareService, Location, Organization, Person, Practitioner, PractitionerRole, Provenance, RelatedPerson).

**F. Direct Ergon persistence entry point**

`energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/registry/AbstractProviderRegistryChangeErgon.java:262-285` directly commits approved resources through `FhirStorageService`:

- UPDATE calls `storageService.updateResource(resourceId, resource, ifMatch)` (`:273-278`).
- Otherwise it calls `storageService.createResource(resource)` (`:278-280`).
- Its preceding Themis persistence authorization gate is `:240-259`.
- It catches HAPI `PreconditionFailedException` and turns it into an OperationOutcome at `:305-312`, but the current model is still direct FhirStorageService access rather than a typed ActiveStateConflict/AuthoritativeStateConflict result.

**G. Current Pylai boundary and context carrier**

- `pylai/pylai-fhir-registry/.../FhirRestGatewayController.java:237-277` receives CREATE, extracts principal/authorities/security context/correlation/source headers, and submits a change request.
- `:280-323` receives UPDATE and passes HTTP `If-Match` into the submission flow.
- `ChangeRequestSubmissionService.java:102-112` accepts operation/resource/id/payload, Themis context, source/correlation, and `ifMatch`.
- `:150-176` stores these in a `ProviderRegistryChangePragma`; `:178-205` caches the initial Pragma and publishes Petasos work.

This is the likely external ingress/caller boundary, not the location for raw CAS or JPA logic.

**6. Version-domain evidence**

The repository already demonstrates the four-domain split needed by Step 08.03, but current code blurs them:

1. **Mneme/Infinispan entry version**: opaque Hot Rod token obtained from `MetadataValue.getVersion()` in `MnemeConcurrencyAndVersionScenarioTest.java:294-308`; compared by `replaceWithVersion` at `:318-339`. The test itself records this separately from FHIR `meta.versionId`.
2. **Mnemosyne persistence version**: `FhirResourceEntity.versionId` at `FhirResourceEntity.java:46-47`, manipulated by `FhirStorageService` at `:193-199` and `:261-279`.
3. **FHIR `Resource.meta.versionId`**: manufactured by current cache service (`FhirCacheService.java:181-188`) and Mnemosyne service (`FhirStorageService.java:172-185`, `:269-279`).
4. **HTTP ETag/If-Match**: accepted at Pylai controller `FhirRestGatewayController.java:283-311`, stored in Pragma metadata by `ProviderRegistryChangePragma.java:170-175`, and parsed by `FhirStorageService.java:250-259`.

The test’s explicit “opaque token”/separate FHIR version treatment is the best existing evidence for the contract correction: no application arithmetic should be assigned to the Infinispan token, and `replaceWithVersion` must not be presented as a FHIR version comparison.

**7. Recommended scope for the Step 08.03 design document**

The document should identify only a small conceptual set of contracts, with implementation ownership mapped as follows:

- Public governed facade/orchestrator: an existing service/API boundary usable by Pylai and Ergon/Ponos callers; no new deployable subsystem.
- Mneme internal primitive: opaque active token read/consume/CAS and guarded convergence/invalidation; no clinical authority and no raw `RemoteCache` exposure.
- Mnemosyne internal primitive: authoritative read/create/conditional-update and authoritative result/version; no cache mechanics.
- Calliope: reuse canonical `PersistenceOperationEnvelope`, `Pragma`, FHIR/security models, and avoid adding infrastructure mechanics to Calliope.
- Themis: reuse `ThemisSecurityContext`, principal, authority, and authorization decision models.
- Pylai: owns HTTP `ETag`/`If-Match` mapping only; it should not define the internal predecessor model.
- Ergon/Ponos: caller/workflow integration only; no direct RemoteCache or direct unconditional persistence in the eventual safe path.

The document should explicitly call out the existing concrete classes above as migration/bypass targets and state that no implementation restrictions are being applied in Step 08.03.

**Discoveries for future steps**

- No production `mneme-cluster/src/main/java` exists; future Mneme contracts likely need a new API/service surface within existing Hestia modules rather than extending a current service.
- `PersistenceOperationEnvelope` already carries most required provenance/security/idempotency fields but currently models DELETE; reuse must be selective.
- The strongest CAS evidence is test-only: `MnemeConcurrencyAndVersionScenarioTest.java:294-339` and `:580-608`; it clearly separates opaque Infinispan tokens from FHIR `meta.versionId`.
- Current authoritative write code is not atomic/strict: CREATE upserts existing IDs, UPDATE creates missing IDs, malformed expected versions are ignored, and `FhirResourceEntity.versionId` lacks the documented `@Version` annotation.
- `FhirRestCacheStore.write()` -> `HapiFhirRestClient.saveResourceJson()` is the most consequential hidden bypass because cache write-behind currently performs unconditional authoritative PUT.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Constraints**  
Architecture/design deliverable only. The plan must stay strictly within the task boundary: produce the Step 08.03 governed write/concurrency contract design document, and only any minimal documentation-index adjustment if truly needed because the destination path is new. Do not include production code, tests, cache config, schema, or implementation work. Emphasize incorporation of the 08.02B corrections from the attached report, explicit handling of contract sections 1-19 from the issue, and a reviewable doc-first workflow.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

**Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



REVIEW THE PLAN

**Task description**  
Review the proposed delivery plan.

**Feedback**  
Focus on whether a single-step doc-authoring plan is sufficient for this architecture-only task, whether it keeps implementation strictly out of scope, and whether it covers the need to incorporate the Step 08.02B corrections plus all required contract sections and examples/test plan from the issue.



Plan Approved

**Verification**
- Confirmed `docs/design/` does not exist in the repository — the plan's choice to create a new `docs/design/` directory (rather than misusing `docs/architecture.md`, which is Petasos-specific) is correct and grounded.
- Confirmed `docs/README.md` contains a "Documentation Information Architecture" tree (lines 9–139); the plan's step to register the new document there is accurate and necessary.
- Verified all referenced artifacts exist at the claimed paths: `ThemisSecurityContext` (themis-api), `PersistenceOperationEnvelope` (calliope), and the four bypass classes (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`).
- Verified the Verification command (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" ...`) is the canonical AGENTS.md architecture command and the seven `*ArchitectureTest` classes exist — it is a valid regression guard confirming no production code was disturbed.
- Scope discipline: plan's "Out of Scope" and per-step acceptance criteria mirror the parent task's MUST-NOT list (no production Java, no Infinispan XML, no `@Version`, no schema/JPA changes, no DELETE removal). Content-verification is handled via the acceptance-criteria checklist plus a git-status check for zero modified production files.

**Feedback focus assessment**
1. **Single-step sufficiency**: Appropriate. The deliverable is one design document — a single checkable unit. Splitting it artificially would add ceremony without value.
2. **Implementation kept out of scope**: Yes. Doc-first guardrail is explicit; conceptual Java appears only as Markdown code blocks; verification includes a git-status check that no production files changed.
3. **08.02B corrections + all sections + examples/test plan**: All four corrections (opaque token / no arithmetic, Mneme non-authority, CAS convergence loop, deferral of clinical consistency tiers) are explicitly listed in decisions and step acceptance criteria. All 19 doc sections, the 16-scenario contract test plan, the 4-domain version model, conflict/result models, and developer usage examples are enumerated.

**Issues**  
None blocking. The done-signal for a doc-only task cannot automatically verify prose content; the plan appropriately compensates with a detailed acceptance-criteria checklist and a git-status no-production-change check, which is proportionate for a Simple architecture/design deliverable.

**Notes**
- The Verification `mvn` command will trivially pass since no code is changed; its real value is confirming the doc author did not accidentally touch production/config — the plan correctly pairs it with the git-status check for that purpose.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

*** Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Priorities:
- Produce the Step 08.03 architecture/design document only, with strong fidelity to the issue sections and implementation boundary.
- Incorporate the 08.02B corrections explicitly: no arithmetic meaning assigned to Infinispan entry versions; Mneme token consumption is not authority to update; guarded convergence requires a real CAS/reread loop contract; do not classify resource-specific consistency tiers in this step.
- Keep the contract small and hard to misuse. Prefer a handful of concepts over elaborate hierarchies.
- Reuse existing repository context models where appropriate instead of inventing duplicates, especially for security/provenance/correlation context.
- Explicitly call out current bypass points and recommended future prevention mechanisms, but do not implement those restrictions now.
- Because `docs/design/` is new, update `docs/README.md` only as minimally necessary to register the new document.

Known from previous steps:
- ADR-018 authoritative boundary: `docs/architecture-decisions.md:136-233`.
- ADR-019 Mneme coordination boundary: `docs/architecture-decisions.md:420-710`.
- ADR-020 no authoritative physical DELETE: `docs/architecture-decisions.md:757-887`.
- Reusable context artifacts: `calliope/.../PersistenceOperationEnvelope.java`, `themis/themis-api/.../ThemisSecurityContext.java`, `calliope/.../Pragma.java`.
- Current bypass/legacy risk points: `iris/.../FhirCacheService.java`, `energeia/erga/.../TaskCacheService.java`, `hestia/mneme-persistence/.../FhirRestCacheStore.java`, `hestia/mnemosyne-clinical/.../FhirStorageService.java`, and HAPI providers under `.../hapifhir/provider/`.
- Attached 08.02B report already established that current Hot Rod CAS evidence exists in Mneme lab tests, while production code still uses unconditional writes.

Execution discipline:
- Start with narrow file inspections only.
- Use small command timeouts (60-120s max per command) if you run shell commands.
- Prefer targeted verification: check modified files and repository status; only run heavier commands if truly needed.
- Report any commands run and relevant observations in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Governed Write and Concurrency Contract Design Specification (Step 08.03)**

**Summary of Accomplishments**

1. **Authored Authoritative Design Specification**: Created [`docs/design/governed-write-concurrency-contract.md`](/Users/markhunter/Development/SourceCode/Projects/github/harmonia/docs/design/governed-write-concurrency-contract.md), establishing the definitive Strong Hybrid persistence and concurrency architecture contract between caller workflows (Pylai, Energeia Ponos/Erga/Praxis, Iris BEFE) and the storage tiers: **Mneme** (distributed cache & active coordination grid) and **Mnemosyne** (authoritative relational JPA persistence).

2. **Full Section Coverage Across All 19 Required Domains**:
    - **Section 1: Purpose** — Standardizing write and concurrency architecture across the platform.
    - **Section 2: Architectural Context** — Grounded in Strong Hybrid model, ADR-018 (Mnemosyne durable boundary), ADR-019 (Mneme distributed coordination), and ADR-020 (lifecycle transitions vs physical DELETE).
    - **Section 3: Core Architectural Invariants** — Formally defined INV-01 through INV-05 (separation of coordination vs authority, monotonic authoritative progression, guarded convergence, commit visibility over degraded convergence, zero-delete lifecycle).
    - **Section 4: Governed Read Model** — Conceptual Java records (`GovernedRead<T>`, `ResourceKey`, `ActiveCoordinationToken`, `ExpectedAuthoritativeVersion`) and cold-read/warm-read semantics.
    - **Section 5: CREATE Contract** — Strict uniqueness semantics (no upsert, rejection of duplicate keys with `AuthoritativeStateConflictException`), and 5-step create lifecycle.
    - **Section 6: UPDATE Contract** — Complete 5-phase end-to-end execution sequence (Read, Active Coordination CAS, Themis Validation, Authoritative Commit, Guarded Convergence).
    - **Section 7: Active Coordination Contract** — Formally incorporated Task 08.02B corrections: opaque Hot Rod tokens without arithmetic meaning (`token + 1` prohibited), active token consumption is not authority to bypass database predecessor checks, and failure handling.
    - **Section 8: Authoritative Persistence Contract** — `MnemosynePersistencePort` interface and atomic SQL conditional update specification (`WHERE id=:id AND version_id=:expectedVersion`).
    - **Section 9: Conflict Model** — Distinct taxonomy separating `ActiveStateConflict` (in-memory contention, fast retry) from `AuthoritativeStateConflict` (durable state advanced, abort/re-read) and `CommitOutcomeUnknown` (ambiguous transport timeout).
    - **Section 10: Result Model** — `WriteResult<T>`, `WriteStatus`, and `ConvergenceStatus`, specifying that durable database commit success with degraded cache convergence reports success (`COMMITTED_CONVERGENCE_DEGRADED`) to prevent duplicate caller retries.
    - **Section 11: Version Model** — Formal 4-domain matrix separating Mneme entry tokens, Mnemosyne DB versions, FHIR `meta.versionId`, and HTTP ETags (`If-Match`), with explicit conflation prohibitions.
    - **Section 12: Guarded Mneme Convergence** — CAS convergence loop algorithm with re-read and enforcement of the Newer-Version Invariant ($V_{cache} \ge V_{committed}$).
    - **Section 13: Commit Outcome Semantics** — Handling ambiguous network timeouts via `CommitOutcomeUnknownException` and idempotent correlation/causation reconciliation without guessing outcomes.
    - **Section 14: Security and Provenance Context** — Reusing existing models (`ThemisSecurityContext` and `PersistenceOperationEnvelope` / `Pragma` context fields) without duplicate context classes.
    - **Section 15: API Placement and Service Boundaries** — Clean separation of contracts (`calliope` / `hestia-api`), active coordinator (`hestia/mneme-*`), persistence port (`hestia/mnemosyne-*`), and orchestrator facade (`hestia/hestia-governance`).
    - **Section 16: Bypass Prevention and Legacy Remediation** — Detailed catalog of existing bypass points (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`, HAPI providers) and future ArchUnit rule definitions.
    - **Section 17: Developer Usage Examples** — Idiomatic Java code snippets for Governed CREATE, Governed UPDATE with retry, and conflict handling.
    - **Section 18: Contract Test Plan** — 16 mandatory verification scenarios (ST-01 to ST-16) detailing preconditions, execution sequence, expected results, and mandatory assertions.
    - **Section 19: Deferred Questions and Future Extensions** — Formally deferred resource-specific clinical consistency tiers (per Task 08.02B review), semantic 3-way merges, distributed multi-resource sagas, and physical retention purges.
    - **Section 20: References** — Links to authoritative ADRs, concepts, and persistence lifecycle documents.

3. **Updated Documentation Information Architecture**: Updated [`docs/README.md`](/Users/markhunter/Development/SourceCode/Projects/github/harmonia/docs/README.md) to register the new `docs/design/` directory and index `governed-write-concurrency-contract.md`.

4. **Zero Production Code Alterations**: No production `.java` source files, schemas, XML descriptors, or test configurations were modified.

5. **Verification**: Executed ArchUnit architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`), passing 57/57 tests with 0 failures, 0 errors.

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` completes in ~10 seconds across all 33 Maven modules.
- **Recommendations**: When implementing the Java interfaces and adapters in subsequent steps, place pure interfaces in `hestia-api` (or `calliope`) and encapsulate `RemoteCacheManager` and `FhirResourceRepository` behind package-private ports to eliminate direct bypass risks.
- **Fragile APIs**: Existing `FhirStorageService` methods swallow `NumberFormatException` during `If-Match` parsing and perform upserts on CREATE; these should be replaced with strict conditional operations conforming to `MnemosynePersistencePort`.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Please verify the architecture/design document and docs index update against the issue requirements. Focus especially on:
- whether the document truly stays within the implementation boundary (no production-code changes disguised as design);
- whether it explicitly incorporates the 08.02B corrections: opaque Infinispan token with no arithmetic meaning, Mneme token consumption is not authority to update, guarded convergence requires a true CAS/reread loop, and no resource-specific consistency tier classifications in this step;
- whether all required contract areas are covered with correct semantics: governed read, CREATE, UPDATE, active coordination, authoritative persistence, conflict model, result model, version model, commit-outcome-unknown, security/provenance context, API placement, bypass prevention, developer examples, and contract test plan;
- whether the new `docs/design/` path is properly integrated in `docs/README.md`.
  If there are issues, separate real defects from cosmetic preferences.



Implementation Approved

**Verification**
- **README integration** (`docs/README.md`): The diff cleanly inserts a `design/` node into the documentation tree with a description and a link to `governed-write-concurrency-contract.md`, placed logically between `architecture/` and `middleware/`. Correct and minimal.
- **Document completeness** (`docs/design/governed-write-concurrency-contract.md`, 794 lines): All 19 required sections are present (1 Purpose … 19 Deferred Questions), plus a References section. Structure matches the plan's section-by-section mapping.
- **Task 08.02B corrections explicitly incorporated**:
    - Opaque token, no arithmetic meaning — §7.1 and §11.2 (`token + 1` explicitly prohibited); record javadoc in §4.1 also states it.
    - Mneme token consumption ≠ authority to update — §7.2 and INV-01 (§3).
    - Guarded convergence via true CAS/re-read loop — §12.1 provides a real `getWithMetadata` → newer-version check → `replaceWithVersion` retry loop with newer-version invariant and exhaustion/invalidation handling.
    - Deferral of resource-specific clinical consistency tiers — §19.1 explicitly defers them; no tier classification appears elsewhere.
- **Required contract areas covered with correct semantics**: Governed read (§4, cold/warm), CREATE strict-no-upsert (§5), UPDATE 5-phase sequence (§6), active coordination + failure modes (§7), authoritative conditional SQL update with row-count semantics (§8), 3-way conflict taxonomy (§9), result model with `WriteStatus`/`ConvergenceStatus` and commit-success-with-degraded-convergence INV-04 (§10), 4-domain version model with ownership/lifetime/comparison matrix and conflation prohibitions (§11), CommitOutcomeUnknown protocol + idempotent reconciliation (§13), reuse of `ThemisSecurityContext` for security/provenance (§14), API placement (§15), bypass catalog + ArchUnit strategy (§16), developer examples (§17), and all 16 contract test scenarios ST-01…ST-16 with preconditions/expected outcomes/assertions (§18).
- **Conceptual Java shapes present**: `GovernedRead`, `ResourceKey`, `ActiveCoordinationToken`, `ExpectedAuthoritativeVersion`, `GovernedWriter`, `GovernedWriteContext`, `WriteResult`, `MnemosynePersistencePort`, token/version records — all as Markdown code blocks only.
- **Doc-first guardrail honored**: Executor's own diff touched only `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`, and a `.junie/plans` file. No production `.java`, schema, or config was modified by this step. (Other modified files in the working tree — test java, latex, architecture-decisions.md — are pre-existing changes from earlier steps, not this executor's work.)
- **Referenced links resolve**: `architecture-decisions.md`, `architecture/persistence-lifecycle.md`, `concepts/mneme.md`, `concepts/mnemosyne.md`, `security/architecture.md` all exist; ADR-018/019/020 headings exist.
- **Regression**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → BUILD SUCCESS, 57/57 tests pass (0 failures/errors). Ran ~10–20s after build.

**Issues**  
None blocking.

**Notes**
- Minor (non-blocking) doc-link concern: the ADR-018 link `#adr-018--mnemosyne-defines-...` uses two hyphens, but the ADR-018 heading in `architecture-decisions.md` uses three literal hyphens (`ADR-018 --- Mnemosyne ...`), whereas ADR-019/020 use an em-dash. On GitHub's anchor algorithm the ADR-018 slug would contain five hyphens, so that single anchor link may not resolve. ADR-019/020 links are fine. This is a pre-existing inconsistency in the source heading, not a contract-content defect; worth a trivial fixup in a later pass.
- The working tree contains many unrelated modified files (mneme-cluster test java, latex chapters, architecture-decisions.md). These are outside Step 1's scope and were not produced by this executor's diff; they do not affect this step's verdict, and the ArchUnit suite remains green regardless.

**Discoveries for future steps**
- ArchUnit regression command runs in ~10–20s once modules are built; a 600s timeout is far more than needed — ~120s is sufficient on warm cache. Full suite = 57 tests across 7 ArchitectureTest classes.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

**✓ Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Authored the Step 08.03 governed write/concurrency contract design at `docs/design/governed-write-concurrency-contract.md`, defining the Strong Hybrid contract between Mneme coordination and Mnemosyne authoritative persistence.
- Kept the work strictly architecture-only: no production Java, schema, cache config, or protocol implementation changes were made.

**Changes**
- Added the full design document covering purpose, invariants, governed read/CREATE/UPDATE contracts, active coordination, authoritative persistence, conflict/result/version models, guarded convergence, commit-outcome semantics, security/provenance context, API placement, bypass prevention, developer examples, test plan, and deferred questions.
- Incorporated the Step 08.02B corrections explicitly: opaque Mneme token semantics, Mneme non-authority, real CAS/reread convergence requirements, and deferral of resource-specific consistency tiers.
- Updated `docs/README.md` to register the new `docs/design/` documentation location and link the contract document.

**Verification**
- Reviewed the documentation changes against the issue requirements; reviewer found no blocking issues.
- Ran `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` successfully (57/57 tests passed).

**Notes**
- No PR/branch workflow was activated because the task did not request shipping this work via a dedicated branch or pull request.

This task has no prior planning phase. Before implementation, analyze the task and codebase, define acceptance criteria if not explicitly provided in the task description, and plan your approach.   
The Reviewer must independently define its own acceptance criteria and will verify them.

*** Step 2: Update / Follow-up**  
You have executed the wrong task.

The work just completed is effectively a refinement/re-run of Step 08.03.  
Retain any useful documentation corrections you made, but DO NOT continue  
working on the 08.03 design document.

We are now performing:

HARMONIA — TASK 08  
STEP 08.04A — FOUNDATIONAL GOVERNED-WRITE CONTRACTS

The purpose of 08.04A is specifically to IMPLEMENT the small Java  
caller-facing contract so that we can inspect the actual API before  
implementing the runtime protocol.

This step MUST create production Java contract types and their unit tests.

It MUST NOT implement Mneme CAS, Mnemosyne conditional persistence,  
convergence, or migrate any existing write path.


IMPLEMENT ONLY THE MINIMUM CONTRACT VOCABULARY

First inspect the existing module dependency graph and choose the smallest  
existing suitable module/package.

DO NOT create:

    hestia-governance
    hestia-api
    a new Maven module
    a new subsystem
    a new deployable service

If no existing module can host the contract without an unacceptable  
dependency cycle, STOP and report that problem rather than inventing a module.


Implement approximately the following concepts, simplifying where possible:

    ResourceKey

    ActiveCoordinationToken

    AuthoritativeVersion
        or ExpectedAuthoritativeVersion

    GovernedRead<T>

    GovernedWriteContext
        ONLY if actually required;
        reuse existing ThemisSecurityContext /
        PersistenceOperationEnvelope / Pragma context rather than
        duplicating correlation, causation, principal or provenance fields

    GovernedWriter

    WriteResult<T>

    minimal conflict/precondition types


REQUIRED SEMANTICS

ActiveCoordinationToken:

    immutable
    opaque
    no arithmetic
    no ordering
    no Infinispan/Hot Rod type exposed

GovernedRead<T>:

    resource key
    resource
    active coordination token
    authoritative predecessor/version

GovernedWriter:

    CREATE
    UPDATE

There is NO governed DELETE.

UPDATE should accept the GovernedRead<T> so the caller does not manually  
assemble concurrency tokens.


AUTHORITATIVE PRECONDITION MODEL

Do NOT collapse these into one vague conflict:

    RESOURCE_ALREADY_EXISTS

        CREATE expected authoritative absence.

    EXPECTED_VERSION_MISMATCH

        UPDATE expected authoritative predecessor X.

They may share a small conceptual AuthoritativePreconditionConflict model.

Keep:

    ActiveStateConflict

separate.


AUTHORITATIVE COMMIT OUTCOME

Represent explicitly:

    COMMITTED
    NOT_COMMITTED
    UNKNOWN

UNKNOWN is first-class.

Do NOT reduce it to:

    success = false

and do not expose it merely as a generic exception that encourages blind  
retry.


WRITE RESULT

Design WriteResult<T> so invalid state combinations are difficult to create.

It must cleanly represent:

    successful authoritative commit

    active-state conflict

    authoritative precondition conflict

    known non-commit/failure

    authoritative outcome UNKNOWN

    committed but Mneme convergence degraded

Prefer a small sealed result model if that makes the invariants clearer.

Do not create a large nullable status bag.


TESTS

Add unit tests demonstrating the ACTUAL API:

1. GovernedRead carries active and authoritative concurrency context.
2. ActiveCoordinationToken is opaque.
3. CREATE conflict can express RESOURCE_ALREADY_EXISTS.
4. UPDATE conflict can express EXPECTED_VERSION_MISMATCH.
5. Active conflict is distinct from authoritative conflict.
6. COMMITTED / NOT_COMMITTED / UNKNOWN are distinct.
7. committed + degraded convergence is representable as committed.
8. no governed DELETE exists.
9. invalid WriteResult combinations cannot readily be constructed.


ARCHITECTURE GUARDRAILS

Add only low-risk architecture tests appropriate to the new contract:

    no Infinispan dependency exposed
    no RemoteCache exposed
    no JPA dependency exposed
    no HTTP framework dependency exposed
    no governed DELETE

Do NOT block or migrate existing legacy write paths yet.


DO NOT IMPLEMENT

    Mneme CAS
    coordination cache
    guarded convergence
    Mnemosyne conditional SQL/JPA
    @Version
    database/schema changes
    BEFE migration
    Pylai migration
    Ergon/Ponos migration
    retries
    semantic conflict handling
    information-authority handling
    DELETE removal


REQUIRED FINAL OUTPUT

Show me the ACTUAL resulting Java API, including package names.

Show examples using the ACTUAL types for:

    CREATE
    UPDATE
    ActiveStateConflict
    ResourceAlreadyExists
    ExpectedVersionMismatch
    CommitOutcomeUnknown
    CommittedButConvergenceDegraded

List every production Java file and test file added or changed.

Explain why the selected existing module/package is the correct dependency  
location.

Run the relevant unit tests and architecture tests.

Explicitly confirm:

    no write path was migrated;
    no new Maven module was created;
    no new subsystem was created;
    no runtime concurrency mechanism was implemented.

STOP after 08.04A.

Do not begin 08.04B.



EXPLORATION

**Task description**  
Identify the smallest existing module and package that can host the new Step 08.04A governed-write caller-facing contract types without creating a dependency cycle. Focus on existing module dependencies and package conventions for pure cross-cutting API/model contracts.

Map:
- candidate modules/packages for `ResourceKey`, `ActiveCoordinationToken`, `AuthoritativeVersion`/`ExpectedAuthoritativeVersion`, `GovernedRead<T>`, `GovernedWriter`, `WriteResult<T>`, and minimal conflict/precondition types;
- whether `GovernedWriteContext` is needed or whether existing types (`ThemisSecurityContext`, `PersistenceOperationEnvelope`, `Pragma`) should be referenced instead;
- any architecture-test location best suited for low-risk guardrails (no Infinispan/RemoteCache/JPA/HTTP exposure, no governed DELETE);
- concrete dependency risks that would make a candidate unacceptable.

Please provide recommended target module/package plus 1-2 rejected alternatives with reasons, and cite precise file paths/lines where possible. Read-only only.



Exploration is Ready

<UPDATE>
<PREVIOUS_STEP>
Read-only mapping completed for the 08.04A hosting decision: Calliope is the smallest viable shared contract module; Themis, Petasos, and Hestia infrastructure modules have distinct ownership or dependency risks. Existing context carriers and centralized architecture-test conventions were also verified.
</PREVIOUS_STEP>
</UPDATE>

**Recommendation**

Host the foundational caller-facing contract in the existing **`calliope`** JAR, under a new bounded model package:

```text
calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/
```

Recommended production package:

```text
net.fhirfactory.harmonia.model.governedwrite
```

This is preferable to placing the types beside `PersistenceOperationEnvelope` in `model.persistence`: the existing persistence package explicitly supports `DELETE`, while the new governed-write package can be guarded as a separate contract surface whose `GovernedWriter` exposes only `create` and `update`.

The package can contain the minimum vocabulary:

- `ResourceKey`
- `ActiveCoordinationToken`
- `ExpectedAuthoritativeVersion` (or `AuthoritativeVersion`)
- `GovernedRead<T>`
- `GovernedWriter`
- `WriteResult<T>` and its small result variants/factories
- `ActiveStateConflict`
- `AuthoritativePreconditionConflict` with distinct reasons `RESOURCE_ALREADY_EXISTS` and `EXPECTED_VERSION_MISMATCH`
- minimal outcome/convergence value types needed to represent `COMMITTED`, `NOT_COMMITTED`, `UNKNOWN`, and committed-but-degraded convergence

Do **not** create `GovernedWriteContext` in 08.04A unless the final method signatures demonstrate a real need. Prefer passing the existing `ThemisSecurityContext` directly. It already carries requesting/executing principals, security domain, authorities, correlation ID, causation ID, tenant/client data, request time, and immutable attributes at `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java:35-52`. `PersistenceOperationEnvelope` and `Pragma` should remain adapter/workflow carriers rather than being duplicated or made mandatory parameters of the small caller-facing API.

A minimal conceptual shape would therefore be closer to:

```java
package net.fhirfactory.harmonia.model.governedwrite;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext);
    <T> WriteResult<T> update(
            GovernedRead<T> current,
            T proposed,
            ThemisSecurityContext securityContext);
}
```

`GovernedRead<T>` should accept the already-read `ResourceKey`, resource, `ActiveCoordinationToken`, and expected authoritative predecessor so callers cannot manually reconstruct concurrency state. `ActiveCoordinationToken` should be an immutable opaque type with no numeric accessor, arithmetic, ordering, Infinispan type, or Hot Rod type. `ExpectedAuthoritativeVersion` may expose an authoritative version value because database/FHIR predecessor comparison is a different domain from the opaque active token.

`WriteResult<T>` should be a sealed/small sum-type-style API rather than a nullable status bag. Separate result variants or factories should make these states explicit and difficult to combine incorrectly:

- committed authoritative result, with a convergence state that can be `CONVERGED` or `DEGRADED`;
- active-state conflict;
- authoritative precondition conflict;
- known non-commit/failure;
- authoritative outcome unknown.

The result model must not represent `UNKNOWN` as merely `success=false`, and must not expose it only as a generic exception encouraging blind retry.

**Why Calliope is the correct dependency location**

1. **It is already the repository's shared model library.** The root reactor includes `calliope` as the first platform module at `pom.xml:45-55`. Calliope's own POM identifies it as the canonical model/schema library at `calliope/pom.xml:31-35`.
2. **Existing model package conventions match the proposed placement.** Calliope already groups transport-independent contracts and models under `net.fhirfactory.harmonia.model.persistence`, `.pragma`, `.security`, `.topic`, and related bounded packages. `PersistenceOperationEnvelope` is in `net.fhirfactory.harmonia.model.persistence` at `calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationEnvelope.java:18`.
3. **The contract can remain implementation-free.** Calliope's production dependencies are model/serialization oriented: Themis API at `calliope/pom.xml:37-43`, HAPI structures at `:44-54`, Jackson at `:56-72`, and logging at `:80-90`. No Infinispan, JPA, Spring, or HTTP dependency is required for the new package.
4. **There is no dependency cycle.** `themis-api` currently depends only on logging/Jackson for production and test dependencies at `themis/themis-api/pom.xml:37-61`; it does not depend on Calliope. Calliope already depends on the lower-level Themis API, so using `ThemisSecurityContext` introduces no new reverse dependency or cycle.
5. **Existing callers already depend on Calliope.** Examples include Erga at `energeia/erga/pom.xml:37-43` and Pylai FHIR Registry at `pylai/pylai-fhir-registry/pom.xml:37-48`; Mneme persistence and Mnemosyne clinical also depend on Calliope at `hestia/mneme-persistence/pom.xml:63-67` and `hestia/mnemosyne-clinical/pom.xml:36-42`. The contract is therefore consumable by callers, orchestration, and both storage sides without introducing a new module.
6. **The existing layering rule permits this direction.** `PackageLayeringArchitectureTest` documents the intended lower-to-higher direction and prohibits `net.fhirfactory.harmonia.model..` from depending on Hestia, Petasos implementations, workflows, gateways, Iris, Agora, or Paradeigma at `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java:30-66`. The proposed package only references JDK types and the existing Themis API contract, which is not among the forbidden higher-layer packages.

**Existing context artifacts and whether a new context is needed**

**ThemisSecurityContext: use directly**

`ThemisSecurityContext` is already immutable/serializable and contains the security and lineage fields needed by a caller-facing write boundary at `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java:35-52`. Themis authorization APIs already accept this context directly, for example `ThemisAuthorizer.authorize(..., ThemisSecurityContext context)` at `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/ThemisAuthorizer.java:41-50`.

Adding `GovernedWriteContext` with duplicate principal, correlation, causation, or security-domain fields would create a second security/provenance vocabulary. The minimum contract should accept `ThemisSecurityContext` directly if security context is required in the interface.

**PersistenceOperationEnvelope: adapter input, not the writer signature**

`PersistenceOperationEnvelope` is a strong existing provenance carrier: it is described as immutable and transport-independent at `calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationEnvelope.java:39-50`, and imports Themis provenance/security types at `:24-27`. It carries operation identity, expected version, payload, authorization evidence, principals, correlation/causation, and attributes.

It should not be the direct `GovernedWriter` context in 08.04A because its existing operation vocabulary explicitly includes `CREATE`, `UPDATE`, and `DELETE` at `PersistenceOperationType.java:20-27`. An adapter may map only CREATE/UPDATE envelopes to the governed contract while rejecting or leaving legacy DELETE handling outside this new API.

**Pragma: workflow adapter, not the storage contract**

`Pragma` is explicitly a transport/workflow-independent task carrier at `calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/Pragma.java:46-58`; it holds correlation/causation, source, input/output, checkpoints, and principal/security context fields beginning at `:62-120`. `ProviderRegistryChangePragma` maps operation/resource/source/If-Match/result metadata and constructs the asynchronous request at `calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/ProviderRegistryChangePragma.java:71-109` and `:131-191`.

It should be adapted by workflow callers rather than embedded into `GovernedWriter`, since the 08.04A API is a small resource write contract, not a replacement for the Petasos/Pragma workflow envelope.

**Candidate map**

| Candidate | Fit | Dependency/cycle assessment | Recommendation |
|---|---|---|---|
| `calliope`, `net.fhirfactory.harmonia.model.governedwrite` | Shared immutable models and caller-facing contract; callers and storage modules already depend on Calliope | No new dependency needed except the already-existing Themis API contract dependency; no reverse dependency from Themis API | **Recommended** |
| `calliope`, existing `net.fhirfactory.harmonia.model.persistence` | Semantically related and already contains `PersistenceOperationEnvelope` | Technically viable, but existing package includes a legacy DELETE model, making “no governed DELETE” architecture checks less precise and conflating legacy persistence envelopes with the new governed API | Rejected in favor of a sibling bounded package |
| `themis/themis-api`, `net.fhirfactory.harmonia.themis.api.model` | Pure API module and already contains immutable security contracts | Wrong ownership: Themis should own authorization contracts, not Mneme/Mnemosyne concurrency/write outcomes; it would make persistence governance a security-module concern and risk coupling future storage vocabulary to Themis | Rejected |
| `petasos/petasos-api`, `net.fhirfactory.harmonia.petasos.api` | Pure caller-facing API style and already has transport message/context contracts | Petasos is explicitly the messaging abstraction; placing storage concurrency outcomes there couples governed writes to transport and violates the separation between persistence governance and messaging. Its existing API namespace and POM are transport-specific at `petasos/petasos-api/pom.xml:31-35` and `:37-68` | Rejected |
| `hestia/mneme-persistence` or `hestia/mneme-cluster` | Mneme is the eventual active-coordination owner | Unacceptable for a public contract: `mneme-persistence` directly depends on Infinispan Core/SPI at `hestia/mneme-persistence/pom.xml:36-47`; `mneme-cluster` directly depends on Infinispan Core, Server, and Hot Rod at `hestia/mneme-cluster/pom.xml:44-60`. It is infrastructure, not a neutral caller API | Rejected |
| `hestia/mnemosyne-clinical` | Mnemosyne is the eventual authoritative persistence owner | Unacceptable leakage and wrong direction: it includes Spring Web/JPA and HAPI server/JPA dependencies at `hestia/mnemosyne-clinical/pom.xml:54-106`, plus PostgreSQL at `:108-117`; exposing the contract there would force callers to depend on the implementation boundary | Rejected |

**Architecture-test location**

Use the existing centralized architecture-test module:

```text
paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/
```

Add a focused future test class such as:

```text
GovernedWriteContractArchitectureTest.java
```

This is the best existing location because:

- the directory already contains the repository-wide ArchUnit suite, including `PackageLayeringArchitectureTest`, `PetasosApiIsolationArchitectureTest`, and other boundary tests;
- `paradeigma-test/pom.xml:156-186` includes JUnit, AssertJ, and `archunit-junit5` test dependencies;
- existing tests import production classes while excluding tests, e.g. `PackageLayeringArchitectureTest.java:36-42` and `PetasosApiIsolationArchitectureTest.java:46-63`;
- the standard architecture command already executes this suite.

The new guardrail should target only `net.fhirfactory.harmonia.model.governedwrite..`, not all of `net.fhirfactory.harmonia.model.persistence..`, because the latter contains the existing `PersistenceOperationEnvelope`/`PersistenceOperationType.DELETE` vocabulary.

Low-risk rules to add later:

1. Governed-write package classes must not depend on `org.infinispan..`, `org.infinispan.client..`, `org.infinispan.commons..`, or `RemoteCache`.
2. They must not depend on `jakarta.persistence..`, `org.hibernate..`, Spring framework packages, HAPI JPA/server packages, PostgreSQL packages, or HTTP client/server packages.
3. `GovernedWriter` must expose only `create` and `update`; no `delete`, `remove`, or physical-delete operation should be present in its public methods.
4. The rules should not prohibit existing legacy paths yet. They should inspect only the new package, consistent with the requirement not to migrate or block existing write paths in 08.04A.

`PackageLayeringArchitectureTest` is suitable for the module-direction check, but a dedicated focused test is preferable for the new API's forbidden dependency surface and no-delete method shape.

**Unit-test location**

Place contract unit tests in Calliope alongside the new package:

```text
calliope/src/test/java/net/fhirfactory/harmonia/model/governedwrite/
```

This matches the existing model-test convention; for example, `PersistenceOperationEnvelopeTest` is colocated under `calliope/src/test/java/net/fhirfactory/harmonia/model/persistence/` and exercises immutable construction, validation, serialization, equality, and safe string behavior (`PersistenceOperationEnvelopeTest.java:41-73`, `:75-80`, and `:682-705`).

The nine required tests can be implemented without any runtime storage dependency:

- construct `GovernedRead<T>` and assert both concurrency contexts are retained;
- verify `ActiveCoordinationToken` exposes no numeric/ordering API and no infrastructure type;
- create `RESOURCE_ALREADY_EXISTS` and `EXPECTED_VERSION_MISMATCH` authoritative conflicts separately;
- assert active and authoritative conflicts are distinct types;
- assert `COMMITTED`, `NOT_COMMITTED`, and `UNKNOWN` are distinct outcome values;
- represent committed plus degraded convergence as a committed result with degraded convergence metadata;
- reflect on `GovernedWriter` and assert it has no delete method;
- verify invalid result combinations are rejected or impossible through the public constructors/factories.

**Concrete dependency risks to preserve in implementation**

- Do not add a dependency from Calliope to any Hestia, Petasos implementation, Energeia, Pylai, Iris, or Paradeigma module. The existing layering test explicitly forbids those dependencies for `model..` at `PackageLayeringArchitectureTest.java:46-66`.
- Do not move the contract into `mneme-persistence` merely because it is the first module with CAS infrastructure; that would expose Infinispan through the public contract and make caller dependencies implementation-bound.
- Do not move it into `mnemosyne-clinical`; that would expose JPA/Spring/HAPI persistence dependencies and invert the intended authority boundary.
- Do not make `themis-api` depend on Calliope to host the contract. The current dependency direction is safe because `calliope` depends on `themis-api` (`calliope/pom.xml:37-43`) while `themis-api` has no Calliope dependency (`themis/themis-api/pom.xml:37-61`). Reversing that direction would create a cycle if Calliope later retained its existing Themis API dependency.
- Do not use `PersistenceOperationEnvelope` as a new result/precondition model without explicitly separating its legacy DELETE semantics; preserve it as an adapter/workflow envelope.
- Do not add raw HTTP `ETag`/`If-Match`, JPA entities, repository types, `RemoteCache`, `MetadataValue`, Hot Rod versions, or Infinispan classes to the public contract.

**Scope confirmation**

This exploration was strictly read-only. No Java files, tests, POMs, architecture tests, modules, subsystems, write paths, CAS mechanisms, persistence mechanisms, or configuration files were modified; no tests or builds were run.



IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Implement only Step 08.04A: the minimum caller-facing governed-write contract vocabulary as production Java types plus unit tests and low-risk architecture tests. Do not continue the 08.03 design work except to preserve any useful doc corrections already made.

Recommended hosting decision from exploration:
- Use existing module `calliope`
- Use new package `net.fhirfactory.harmonia.model.governedwrite`
- Keep architecture guardrail tests in `paradeigma/paradeigma-test/.../arch/`
- Prefer reusing `ThemisSecurityContext` directly rather than inventing a duplicate `GovernedWriteContext`, unless you find a truly unavoidable need.

Required implementation scope:
- Production types for approximately: `ResourceKey`, `ActiveCoordinationToken`, `AuthoritativeVersion` or `ExpectedAuthoritativeVersion`, `GovernedRead<T>`, `GovernedWriter`, `WriteResult<T>`, and minimal conflict/precondition types.
- No governed DELETE. `GovernedWriter` must expose CREATE and UPDATE only.
- `GovernedRead<T>` must carry resource key, resource, active coordination token, and authoritative predecessor/version.
- `ActiveCoordinationToken` must be immutable and opaque: no arithmetic, no ordering, no Infinispan/Hot Rod type exposure.
- Keep `ActiveStateConflict` distinct from authoritative precondition conflicts.
- Authoritative precondition model must distinguish `RESOURCE_ALREADY_EXISTS` from `EXPECTED_VERSION_MISMATCH`.
- Authoritative commit outcome must explicitly represent `COMMITTED`, `NOT_COMMITTED`, and `UNKNOWN`.
- `WriteResult<T>` should make invalid combinations hard to construct; prefer a small sealed result model or similarly constrained design over a nullable status bag.
- Represent committed-but-convergence-degraded as committed, not as non-commit.

Required tests:
1. `GovernedRead` carries both concurrency contexts.
2. `ActiveCoordinationToken` is opaque.
3. CREATE conflict can express `RESOURCE_ALREADY_EXISTS`.
4. UPDATE conflict can express `EXPECTED_VERSION_MISMATCH`.
5. Active conflict is distinct from authoritative conflict.
6. `COMMITTED` / `NOT_COMMITTED` / `UNKNOWN` are distinct.
7. committed + degraded convergence is representable as committed.
8. no governed DELETE exists.
9. invalid `WriteResult` combinations cannot readily be constructed.

Required low-risk architecture guardrails for the new contract package only:
- no Infinispan dependency exposed
- no `RemoteCache` exposed
- no JPA dependency exposed
- no HTTP framework dependency exposed
- no governed DELETE

Critical out-of-scope items:
- DO NOT implement Mneme CAS, coordination cache, guarded convergence runtime, Mnemosyne conditional SQL/JPA, `@Version`, database/schema changes, BEFE/Pylai/Ergon/Ponos migration, retries, semantic conflict handling, information-authority handling, or DELETE removal from legacy code.
- DO NOT create `hestia-governance`, `hestia-api`, a new Maven module, a new subsystem, or a new deployable service.
- If you discover an unavoidable dependency-cycle problem with `calliope`, stop and report it rather than inventing a new module.

Execution discipline:
- Start with narrow inspections of existing package/test conventions and current Calliope/Themis dependencies.
- Use targeted commands with 60-120s timeouts.
- Prefer narrow Maven test runs first (new Calliope tests + focused architecture test) before the broader architecture suite.
- Report the exact production files and test files added/changed, the actual resulting API signatures/package names, commands run, and whether any pre-existing failures or unrelated working-tree changes were observed.

Known from previous steps:
- `calliope` already depends on `themis-api`; `themis-api` does not depend on `calliope`, so using `ThemisSecurityContext` in Calliope is cycle-safe.
- Existing shared-model patterns exist in `calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/` and tests under the matching `src/test/java` path.
- `PackageLayeringArchitectureTest` already guards `net.fhirfactory.harmonia.model..` from higher-layer dependencies.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Harmonia — Task 08 (Step 08.04A)**  
**Foundational Governed-Write Contracts Implementation**

**1. Overview and Hosting Decision**

The foundational caller-facing governed-write contract has been implemented in the existing **`calliope`** module under the package **`net.fhirfactory.harmonia.model.governedwrite`**.

**Why `calliope` / `net.fhirfactory.harmonia.model.governedwrite` is the correct dependency location:**
1. **Shared Canonical Model Role**: `calliope` is Harmonia's foundational model library (`pom.xml:45-55`, `calliope/pom.xml:31-35`), already hosting cross-cutting, transport-independent contracts.
2. **Cycle-Safe Dependency Graph**: `calliope` already depends on `themis-api` (`calliope/pom.xml:37-43`), while `themis-api` has zero Calliope dependencies (`themis/themis-api/pom.xml:37-61`). The contract can directly reference `ThemisSecurityContext` without creating a circular dependency.
3. **Consumer Layering Compatibility**: Callers (Pylai gateways, Energeia Ponos/Erga/Praxis, Iris BEFE) and persistence implementations (Hestia Mneme, Mnemosyne) already depend on `calliope`.
4. **Architectural Isolation**: Placing the contract in `calliope` keeps it 100% free of Infinispan/Hot Rod, JPA/Hibernate, Spring Web, and database driver dependencies.
5. **Separation from Legacy Persistence**: Placing the contract in `net.fhirfactory.harmonia.model.governedwrite` rather than `net.fhirfactory.harmonia.model.persistence` preserves a clean boundary where `GovernedWriter` exposes only `create` and `update` (with zero physical `DELETE` semantics per ADR-020).

---

**2. Actual Resulting Java API**

**Package**: `net.fhirfactory.harmonia.model.governedwrite`

**A. Resource & Concurrency Identifiers**

```java
// Immutable resource key (type + id)
public record ResourceKey(String resourceType, String id) implements Serializable {
    public static ResourceKey of(String resourceType, String id);
}

// Immutable, opaque active-coordination token (Mneme)
// No arithmetic, no numeric increment, no ordering comparisons, no Infinispan/Hot Rod type exposure
public final class ActiveCoordinationToken implements Serializable {
    public static ActiveCoordinationToken of(String opaqueValue);
    public static ActiveCoordinationToken none();
    public boolean isPresent();
    public String asOpaqueString();
}

// Authoritative version assigned and persisted by Mnemosyne
public record AuthoritativeVersion(String value) implements Serializable {
    public static AuthoritativeVersion of(String value);
    public static AuthoritativeVersion of(long versionNumber);
    public ExpectedAuthoritativeVersion toExpected();
}

// Expected predecessor version for conditional write (none for CREATE, specific version for UPDATE)
public final class ExpectedAuthoritativeVersion implements Serializable {
    public static ExpectedAuthoritativeVersion none();
    public static ExpectedAuthoritativeVersion of(AuthoritativeVersion version);
    public static ExpectedAuthoritativeVersion of(String versionString);
    public static ExpectedAuthoritativeVersion of(long versionNumber);
    public boolean isNone();
    public Optional<AuthoritativeVersion> version();
    public Optional<String> value();
}
```

**B. Governed Read Context**

```java
// Packaging resource payload with active token (Mneme) and authoritative predecessor (Mnemosyne)
public record GovernedRead<T>(
        ResourceKey key,
        T resource,
        ActiveCoordinationToken activeToken,
        AuthoritativeVersion authoritativeVersion
) implements Serializable {
    public static <T> GovernedRead<T> of(
            ResourceKey key,
            T resource,
            ActiveCoordinationToken activeToken,
            AuthoritativeVersion authoritativeVersion);
    public ExpectedAuthoritativeVersion expectedAuthoritativeVersion();
}
```

**C. Preconditions, Outcomes, and Conflicts**

```java
// First-class authoritative commit outcome (UNKNOWN is not collapsed into failure)
public enum AuthoritativeCommitOutcome {
    COMMITTED,
    NOT_COMMITTED,
    UNKNOWN
}

// Active coordination cache (Mneme) convergence status
public enum ConvergenceStatus {
    CONVERGED,
    DEGRADED,
    NOT_APPLICABLE
}

// Granular authoritative precondition failure reasons
public enum PreconditionFailureReason {
    RESOURCE_ALREADY_EXISTS,
    EXPECTED_VERSION_MISMATCH
}

// In-memory active coordination conflict (Mneme CAS contention)
public record ActiveStateConflict(
        ResourceKey key,
        String message
) implements Serializable {
    public static ActiveStateConflict of(ResourceKey key, String message);
}

// Durable database boundary precondition conflict (Mnemosyne)
public record AuthoritativePreconditionConflict(
        ResourceKey key,
        PreconditionFailureReason reason,
        ExpectedAuthoritativeVersion expectedVersion,
        AuthoritativeVersion currentVersion,
        String message
) implements Serializable {
    public static AuthoritativePreconditionConflict resourceAlreadyExists(ResourceKey key);
    public static AuthoritativePreconditionConflict resourceAlreadyExists(ResourceKey key, AuthoritativeVersion currentVersion);
    public static AuthoritativePreconditionConflict expectedVersionMismatch(
            ResourceKey key, ExpectedAuthoritativeVersion expectedVersion, AuthoritativeVersion currentVersion);
    public Optional<AuthoritativeVersion> currentVersionOptional();
}
```

**D. Sealed Result Model (`WriteResult<T>`)**

```java
public sealed interface WriteResult<T> extends Serializable permits
        WriteResult.Committed,
        WriteResult.ActiveConflict,
        WriteResult.AuthoritativeConflict,
        WriteResult.OutcomeUnknown,
        WriteResult.NotCommitted {

    ResourceKey key();
    AuthoritativeCommitOutcome commitOutcome();
    ConvergenceStatus convergenceStatus();

    default boolean isCommitted() { return commitOutcome() == AuthoritativeCommitOutcome.COMMITTED; }
    default boolean isOutcomeUnknown() { return commitOutcome() == AuthoritativeCommitOutcome.UNKNOWN; }
    default boolean isConflict() { return this instanceof ActiveConflict || this instanceof AuthoritativeConflict; }
    default Optional<T> committedResource();
    default Optional<AuthoritativeVersion> committedVersion();
    default Optional<ActiveStateConflict> activeConflict();
    default Optional<AuthoritativePreconditionConflict> authoritativeConflict();
    default Optional<String> degradationReason();
    default Optional<String> failureReason();

    // Static factories
    static <T> Committed<T> committed(ResourceKey key, T resource, AuthoritativeVersion version);
    static <T> Committed<T> committedDegraded(ResourceKey key, T resource, AuthoritativeVersion version, String degradationReason);
    static <T> ActiveConflict<T> activeStateConflict(ActiveStateConflict conflict);
    static <T> ActiveConflict<T> activeStateConflict(ResourceKey key, String message);
    static <T> AuthoritativeConflict<T> authoritativeConflict(AuthoritativePreconditionConflict conflict);
    static <T> OutcomeUnknown<T> outcomeUnknown(ResourceKey key, String message);
    static <T> NotCommitted<T> notCommitted(ResourceKey key, String reason);
}
```

**E. Caller-Facing Writer Interface**

```java
package net.fhirfactory.harmonia.model.governedwrite;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext);
}
```

---

**3. Usage Examples with Actual Types**

**Example 1: Governed CREATE**
```java
GovernedWriter writer = getGovernedWriter();
ResourceKey key = ResourceKey.of("Practitioner", "prac-001");
Practitioner practitioner = buildPractitioner("prac-001");
ThemisSecurityContext securityContext = currentSecurityContext();

WriteResult<Practitioner> result = writer.create(key, practitioner, securityContext);

if (result.isCommitted()) {
    AuthoritativeVersion version = result.committedVersion().orElseThrow();
    Practitioner committed = result.committedResource().orElseThrow();
    // Successfully committed to Mnemosyne (version = 1)
}
```

**Example 2: Governed UPDATE**
```java
// Given an existing GovernedRead obtained from read flow:
GovernedRead<Practitioner> current = ...;
Practitioner proposed = current.resource().copy();
proposed.addIdentifier().setValue("NEW-ID");

WriteResult<Practitioner> result = writer.update(current, proposed, securityContext);
```

**Example 3: Handling ActiveStateConflict (Mneme In-Memory Contention)**
```java
if (result instanceof WriteResult.ActiveConflict<Practitioner> active) {
    ActiveStateConflict conflict = active.conflict();
    // Transient active-coordination CAS contention in Mneme.
    // Safe to re-read and retry quickly.
}
```

**Example 4: Handling ResourceAlreadyExists (CREATE Precondition Failure)**
```java
if (result instanceof WriteResult.AuthoritativeConflict<Practitioner> auth) {
    AuthoritativePreconditionConflict conflict = auth.conflict();
    if (conflict.reason() == PreconditionFailureReason.RESOURCE_ALREADY_EXISTS) {
        // Resource already authoritatively exists in Mnemosyne.
        // Return 409 Conflict / PreconditionFailed to client.
    }
}
```

**Example 5: Handling ExpectedVersionMismatch (UPDATE Precondition Failure)**
```java
if (result instanceof WriteResult.AuthoritativeConflict<Practitioner> auth) {
    AuthoritativePreconditionConflict conflict = auth.conflict();
    if (conflict.reason() == PreconditionFailureReason.EXPECTED_VERSION_MISMATCH) {
        ExpectedAuthoritativeVersion expected = conflict.expectedVersion();
        Optional<AuthoritativeVersion> current = conflict.currentVersionOptional();
        // Database version moved ahead. Must re-read current state before retrying.
    }
}
```

**Example 6: Handling CommitOutcomeUnknown (Network Timeout / Ambiguous State)**
```java
if (result.isOutcomeUnknown()) {
    // WriteResult.OutcomeUnknown: database connection timed out during commit dispatch.
    // Outcome is UNKNOWN: DO NOT blind-retry as a new write.
    // Trigger reconciliation via correlationId / causationId.
}
```

**Example 7: Handling CommittedButConvergenceDegraded (Committed with Cache Degradation)**
```java
if (result.isCommitted() && result.convergenceStatus() == ConvergenceStatus.DEGRADED) {
    // Authoritative commit SUCCEEDED in Mnemosyne database.
    // Active cache update (Mneme) was degraded or skipped.
    // Treated as successful commit to prevent duplicate retries.
    String reason = result.degradationReason().orElse("Unknown degradation");
}
```

---

**4. Inventory of Added and Changed Files**

**Production Java Files (`calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/`)**
- `ResourceKey.java`
- `ActiveCoordinationToken.java`
- `AuthoritativeVersion.java`
- `ExpectedAuthoritativeVersion.java`
- `GovernedRead.java`
- `AuthoritativeCommitOutcome.java`
- `ConvergenceStatus.java`
- `PreconditionFailureReason.java`
- `ActiveStateConflict.java`
- `AuthoritativePreconditionConflict.java`
- `WriteResult.java`
- `GovernedWriter.java`

**Unit Test Files (`calliope/src/test/java/net/fhirfactory/harmonia/model/governedwrite/`)**
- `GovernedWriteContractTest.java` (9 comprehensive unit tests covering concurrency contexts, token opacity, distinct conflict reasons, commit outcomes, degraded convergence, zero DELETE methods, and invalid result construction rejection).

**Architecture Test Files (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`)**
- `GovernedWriteContractArchitectureTest.java` (5 ArchUnit & source inspection rules ensuring zero Infinispan, JPA, HTTP, or DELETE exposure).

---

**5. Test Execution & Verification**

1. **Calliope Unit Tests**:
   ```bash
   mvn test -pl calliope -Dtest=GovernedWriteContractTest
   ```
   **Result**: `Tests run: 9, Failures: 0, Errors: 0, Skipped: 0` (BUILD SUCCESS).
   Full Calliope suite: `Tests run: 134, Failures: 0, Errors: 0, Skipped: 0`.

2. **Paradeigma Architecture Tests**:
   ```bash
   mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
   ```
   **Result**: `Tests run: 62, Failures: 0, Errors: 0, Skipped: 0` across all 8 architecture test classes (including 5/5 in `GovernedWriteContractArchitectureTest`).

---

**6. Explicit Confirmations**

- **No write path was migrated** (existing write paths in BEFE, Pylai, Ergon, and Hestia remain unchanged).
- **No new Maven module was created** (hosted entirely inside existing `calliope` and `paradeigma-test`).
- **No new subsystem was created**.
- **No runtime concurrency mechanism was implemented** (no Infinispan CAS, no Mnemosyne SQL update, no `@Version` additions, no cache config).
- **Execution has stopped after Step 08.04A**.

---

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl calliope -Dtest=GovernedWriteContractTest` runs in ~1.9s. Full reactor architecture suite runs in ~10s.
- **Recommendations**: In Step 08.04B/runtime implementation, adapters implementing `GovernedWriter` should be placed in orchestration/service packages (e.g. `energeia-erga` or `hestia-mnemosyne`) while consuming these pure `calliope` contracts.
- **Fragile APIs**: `WriteResult.Committed` must always enforce that `convergenceStatus` is either `CONVERGED` or `DEGRADED`; `NOT_APPLICABLE` is strictly reserved for uncommitted results.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Please verify the actual Step 08.04A implementation against the issue-update requirements.

Focus on:
- whether `calliope` / `net.fhirfactory.harmonia.model.governedwrite` is the correct existing dependency location and no new module/subsystem was created;
- whether the production API is truly minimal and caller-facing, with no runtime protocol implementation, no Mneme CAS/JPA/HTTP/Infinispan leakage, and no governed DELETE;
- whether `GovernedWriter` exposes only CREATE and UPDATE, with UPDATE accepting `GovernedRead<T>`;
- whether `ActiveCoordinationToken` is actually opaque/immutable/no ordering/no arithmetic/no infra types;
- whether the authoritative precondition model distinguishes `RESOURCE_ALREADY_EXISTS` from `EXPECTED_VERSION_MISMATCH`, while `ActiveStateConflict` remains distinct;
- whether `WriteResult<T>` correctly and safely represents COMMITTED / NOT_COMMITTED / UNKNOWN plus committed-but-degraded-convergence and makes invalid combinations hard to construct;
- whether the requested unit tests and low-risk architecture tests really exist and cover the stated scenarios;
- whether no write path was migrated and no unrelated production behavior changed.

Please separate real defects from cosmetic preferences and cite specific files/lines where possible.



Implementation Approved

**Verification**
- **Correct hosting location**: Contracts placed in existing module `calliope` under new package `net.fhirfactory.harmonia.model.governedwrite`. No `pom.xml` was modified (git status shows no build-file changes), so no new Maven module/subsystem was created. `calliope` already depends on `themis-api` (cycle-safe reuse of `ThemisSecurityContext`).
- **Minimal caller-facing API, no runtime protocol**: All 12 production types are pure records/enums/sealed interface with zero Infinispan/Hot Rod/JPA/HTTP imports. No CAS, conditional SQL, convergence, or `@Version` logic implemented.
- **GovernedWriter** (`GovernedWriter.java:39,50`): exposes only `create` and `update`; `update` accepts `GovernedRead<T>` so callers don't reassemble concurrency tokens. Test #8 reflectively asserts `containsExactlyInAnyOrder("create","update")` and no delete/remove.
- **GovernedRead** (`GovernedRead.java:29-53`): carries key, resource, `ActiveCoordinationToken`, and `AuthoritativeVersion`; exposes `expectedAuthoritativeVersion()`. Non-null validated.
- **ActiveCoordinationToken** (diff/`ActiveCoordinationToken.java`): immutable final class, opaque (`toString` masks value), not `Comparable`, no arithmetic/ordering/numeric accessors, no Infinispan types. Test #2 verifies opacity via reflection.
- **Conflict model**: `ActiveStateConflict` (distinct record) vs `AuthoritativePreconditionConflict` with `PreconditionFailureReason.{RESOURCE_ALREADY_EXISTS, EXPECTED_VERSION_MISMATCH}` and dedicated factories. Tests #3/#4/#5 confirm distinction.
- **WriteResult<T>** (`WriteResult.java`): sealed interface with 5 permitted records; `Committed` compact constructor rejects `NOT_APPLICABLE` convergence (line 152-154); `AuthoritativeCommitOutcome` fixed per-variant; `OutcomeUnknown` is first-class (not `success=false`, not a blind-retry exception). Committed-but-degraded represented as `Committed` with `DEGRADED` (test #7). Test #9 confirms invalid combinations throw.
- **Tests exist and cover scope**: 9 unit tests in `GovernedWriteContractTest.java` (all 9 required scenarios) + 5 architecture guardrails in `GovernedWriteContractArchitectureTest.java` (Infinispan, JPA/DB, HTTP, no DELETE method, forbidden-import source scan).
- **Executed**: `mvn test -pl calliope -Dtest=GovernedWriteContractTest` → success (exit 0). Full ArchUnit suite → BUILD SUCCESS (~10s, all modules green), confirming no regression and no production write path disturbed.
- **No write-path migration**: git status shows only new governedwrite main/test packages, the new arch test, and doc/plan edits (`docs/design/governed-write-concurrency-contract.md`, `.junie/plans`). BEFE/Pylai/Ergon/Hestia untouched.

**Issues**  
None.

**Notes**
- `ActiveCoordinationToken.asOpaqueString()` returns the raw value — acceptable and necessary for serialization/adapter round-tripping; it exposes no ordering/arithmetic and does not leak infra types, so opacity intent is preserved.
- The doc edits to `governed-write-concurrency-contract.md` are minor 08.03 refinements (removing invented `hestia-governance`/`hestia-api` placement, clarifying opaque coordination claim). Consistent with the instruction to retain useful doc corrections without continuing 08.03 work.

**Discoveries for future steps**
- `mvn test -pl calliope -Dtest=GovernedWriteContractTest` runs in ~2s; full ArchUnit suite `-pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` ~10s on warm cache — 120s timeout is ample.
- `ThemisSecurityContext` has a 7-arg backward-compatible constructor `(principal, correlationId, causationId, tenantId, clientIp, requestedAt, attributes)` in addition to the 10-arg canonical one — useful for constructing test contexts.
- For 08.04B, `GovernedWriter` implementations belong in an orchestration/service module (energeia-erga or hestia-mnemosyne) consuming these pure calliope contracts; `WriteResult.Committed` invariant reserves `ConvergenceStatus.NOT_APPLICABLE` strictly for non-committed results.



**Delivery Steps**

**✓ Step 1: Implementation**  
Optional spending limit; leave empty for no limit: 50  
Required for Goal Mode: Auto  
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Map the existing repository locations and artifacts needed to produce the Step 08.03 governed write/concurrency contract design document. Focus on: (1) current architecture/design documentation structure and the best destination path for the new document; (2) existing ADR or design docs related to ADR-018/019/020 and governed writes; (3) existing Java packages/interfaces that would likely host a future governed write facade versus Mneme/Mnemosyne primitives; (4) current code paths that illustrate bypass risks (direct RemoteCache writes, direct persistence update entry points). Provide precise file paths and line references where possible. Read-only only.



Exploration is Ready

Read-only repository exploration completed; no files were modified and the Step 08.03 design document was not created (implementation/document-authoring belongs to the parent task).

**1. Documentation structure and recommended destination**

- `docs/README.md:9-60` is the master documentation information architecture. It has separate `concepts/`, `modules/`, `architecture/`, `integration/`, `security/`, and provider-registry sections, but no `docs/design/` section.
- A glob check for `docs/design/**/*.md` confirmed that `docs/design/` does not currently exist.
- The requested destination `docs/design/governed-write-concurrency-contract.md` is therefore a new, sensible architecture/design location rather than an existing convention. The document should either create that directory and then add it to the documentation index, or, if avoiding a new category, use `docs/architecture/` alongside `persistence-lifecycle.md`; the explicit task request strongly favors `docs/design/`.
- Existing persistence and architecture documents that should be cross-linked from the new document:
    - `docs/architecture-decisions.md`
    - `docs/architecture/persistence-lifecycle.md`
    - `docs/persistence-architecture.md`
    - `docs/concepts/mneme.md`
    - `docs/concepts/mnemosyne.md`
    - `docs/provider-registry/architecture.md`
    - `docs/integration/rest.md`
    - `docs/persistence-recovery-gaps.md`

Note: `docs/architecture.md` is actually a Petasos architecture document despite its generic filename; it is not the best home for this contract.

**2. ADR and existing design baseline**

**ADR-018 — Mnemosyne authoritative durable boundary**

`docs/architecture-decisions.md:136-233` establishes that:

- Mneme/Infinispan is distributed, reconstructable, non-authoritative state (`:226-233`).
- Mnemosyne/PostgreSQL is the authoritative durable application-state boundary (`:201-224`).
- Cache presence, replication, or a successful cache operation is not durable acceptance (`:206-224`).
- The responsibility matrix at `:235-262` separates Mneme distributed cache/access, Mnemosyne durable persistence, Petasos durable work transfer, and Kleio evidence.
- Failure semantics at `:306-340` require persistence failure to remain visible; local memory or asynchronous persistence attempts must not turn unavailable durability into apparent success.

**ADR-019 — Mneme distributed access and coordination**

`docs/architecture-decisions.md:420-512` is the primary source for the future Mneme primitive:

- Mneme owns distributed resource access and coordination, not distributed durability (`:475-503`).
- The explicit division is “Mneme coordinates active distributed state; Mnemosyne commits authoritative durable state” (`:505-512`).
- `:545-562` says concurrency/version coordination must be distributed and substantially transparent to ordinary client code, while authoritative persisted-version semantics remain Mnemosyne’s responsibility.
- `:581-607` prohibits silently replacing Mneme with process-local maps/collections when Mneme is unavailable.
- `:609-646` sketches the cache hit/miss relationship and distinguishes active coordination from authoritative write.
- `:648-710` reinforces that cache restart/eviction is not authoritative loss and that the write bridge must be explicit.

**ADR-020 — no authoritative physical DELETE**

`docs/architecture-decisions.md:757-818` defines the no-delete invariant:

- Governed information has no physical DELETE semantics (`:805-818`).
- Lifecycle retirement/deactivation is an authoritative UPDATE (`:825-836`, `:848-870`).
- Mneme eviction/invalidation is explicitly distinct from governed deletion (`:831-846`).
- `removeWithVersion()` may be an infrastructure/cache primitive, but it must not be exposed as governed resource deletion (`:872-887`).

**Related existing documentation**

- `docs/architecture/persistence-lifecycle.md:24-29` identifies Mneme as Tier 2 and Mnemosyne/PostgreSQL as Tier 3; `:109-113` restates ADR-020.
- `docs/concepts/mneme.md:56-70` gives the Mneme ownership boundary and explicitly assigns permanent/authoritative FHIR state to Mnemosyne.
- `docs/concepts/mnemosyne.md:49-74` assigns transactions, JPA/DAO, and relational authority to Mnemosyne.
- `docs/persistence-architecture.md:124-159` provides a subsystem ownership matrix; `:224-237` describes the intended optimistic-concurrency/no-delete model.
- `docs/provider-registry/architecture.md:44-63` identifies the current Provider Registry split: Erga performs validation/persistence orchestration, Mnemosyne owns storage, and Calliope owns canonical models.
- `docs/integration/rest.md:62-133` describes the current asynchronous Pylai -> Pragma -> Petasos -> Ponos -> Mnemosyne workflow and currently documents `If-Match` as an external concern.
- `docs/persistence-recovery-gaps.md:42-66` documents existing worker coordination/recovery gaps (REC-003/REC-004) and should not be treated as the governed-write contract.

There are stale/overstated claims worth flagging in the eventual design document: `docs/persistence-architecture.md:231-233` says optimistic locking is via a JPA `@Version` column, but the inspected entity has no `@Version` annotation; and `docs/architecture/overview.md:44-57` still depicts direct Hot Rod puts and Mneme write-behind as the primary flow. These are current-baseline descriptions, not proof that the Step 08.03 protocol exists.

**3. Existing reusable context/model artifacts**

**Calliope persistence envelope**

`calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationEnvelope.java:39-70` is the closest existing cross-module contract artifact:

- It is an immutable, serializable, transport-independent envelope.
- Existing fields include `operationId`, operation type, resource type/id, `expectedVersion`, payload, originating principal/authorities, security domain, correlation ID, causation ID, timestamps, authorization decision, executing principal, and attributes (`:54-70`).
- Validation and immutable-copy behavior are implemented in the same file (`:71-170`, and derived-copy methods around `:307-330`).
- It currently explicitly supports `CREATE`, `UPDATE`, and `DELETE`, with DELETE payload semantics (`:40-47`, `:74-80`), so the Step 08.03 design should reuse its context fields where appropriate but must not blindly adopt its DELETE operation for governed clinical writes.
- `calliope/src/main/java/net/fhirfactory/harmonia/model/persistence/PersistenceOperationType.java:18-27` currently enumerates `CREATE`, `UPDATE`, and `DELETE`.

**Themis security context**

`themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java:29-52` is an immutable serializable record containing requesting and executing principals, security domain, authorities, correlation/causation IDs, tenant/client data, request time, and immutable attributes. `ThemisAuthorizationRequest.java` and `ThemisAuthorizer.java` provide the existing default-deny authorization request contract (`themis/themis-api/.../ThemisAuthorizationRequest.java:28-52`; `.../ThemisAuthorizer.java:42-50`).

This is preferable to inventing a second security context. The eventual governed-write context can carry/refer to this existing Themis context and existing `PersistenceOperationEnvelope` provenance fields rather than duplicating them.

**Pragma/workflow context**

`calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/Pragma.java:62-124` carries correlation ID, causation ID, source, input/output, checkpoints, originating/executing principals, and security context. `ProviderRegistryChangePragma.java:71-109` exposes operation/resource/id/source/If-Match/resulting-version metadata, and `:149-189` constructs the current change request with metadata and an initial checkpoint.

This is the existing asynchronous workflow carrier, not a replacement for the future governed write API. It is a strong source of provenance/context when an Ergon workflow submits a governed operation.

**4. Likely future API placement**

**No existing governed facade**

No production interface matching a governed writer, active coordinator, conditional persistence port, or convergence port was found. The repository currently contains concrete services and persistence implementations only.

**Mneme primitive location**

- `hestia/mneme-cluster` contains no `src/main/java` production tree according to the repository search; it appears configuration/resource-oriented.
- `hestia/mneme-persistence` is an Infinispan `NonBlockingStore` adapter rather than a public domain API. `FhirRestCacheStore.java:43-64` initializes the store and REST client; `:81-97` loads from Mnemosyne; `:99-121` writes through to the REST client.
- The test-only concurrency evidence is in `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java`. It demonstrates `getWithMetadata()` and opaque entry-token reads at `:294-308`, `replaceWithVersion()` success at `:318-328`, stale-token rejection at `:330-339`, and three participants observing one starting token and attempting CAS at `:580-608`.
- This suggests future Mneme-facing primitives belong under Hestia/Mneme, but ordinary callers should not receive `RemoteCache`, `MetadataValue`, or raw Infinispan version types. The public abstraction should be a small opaque token/context interface implemented behind Hestia.

**Mnemosyne primitive location**

- `hestia/mnemosyne-clinical/.../service/FhirStorageService.java` is the current authoritative persistence entry point. It already owns Themis authorization, JSON encoding, repository access, and version assignment.
- `.../repository/FhirResourceRepository.java:27-40` is a Spring Data repository with direct `JpaRepository` access and lookup/exists methods. It is an infrastructure-only candidate for a future conditional persistence adapter, not a public caller API.
- `.../model/FhirResourceEntity.java:23-57` is the JPA entity. Its `versionId` field is at `:46-47`, but there is no inspected `@Version` annotation; this conflicts with the broader documentation claim noted above.

**Orchestration/facade candidate**

The sequence “active coordination -> authoritative conditional persistence -> guarded convergence” does not naturally belong entirely in Mneme or entirely in Mnemosyne:

- Mneme should expose only active-state read/coordination/convergence primitives.
- Mnemosyne should expose authoritative read/create/conditional-update primitives.
- A future governed facade/orchestrator should coordinate the sequence and be called by workflow/gateway services.
- Existing callers that would eventually use the facade include `energeia/erga` change activities and Pylai submission/processing paths.
- No new deployable subsystem is indicated by the current module structure; a Java contract plus implementation in an existing Hestia/appropriate orchestration module is the lower-complexity direction.

The POM graph currently makes the boundary difficult: `energeia/erga/pom.xml:44-54` directly depends on `mnemosyne-clinical`, and `:87-91` directly depends on Infinispan Hot Rod. That is an architectural bypass-enablement risk to address in the later implementation/architecture-test steps, not in Step 08.03.

**5. Current bypass risks and precise code locations**

**A. Direct unconditional RemoteCache writes**

1. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java`
    - Raw `RemoteCache` acquisition and availability handling: `:119-136`.
    - Unconditional read: `:138-149`.
    - Unconditional write API `putResourceJson`: `:152-157`.
    - `saveResource` independently manufactures/increments FHIR `meta.versionId`: `:160-188`; this is exactly the version-domain conflation the future contract must prevent.

2. `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheService.java`
    - Injects/exposes `RemoteCacheManager`: `:55-88`.
    - Direct task/provenance `remoteCache.put(...)`: `:246-276`.
    - Direct task removal: `:380-387`.
    - Direct cache clearing: `:389-394`.
    - `saveTask`/`markTaskAsProcessed` mutate cached FHIR Task representations without a governed authoritative predecessor protocol (the surrounding save path is `:210-243`).

3. Scenario evidence
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/scenario/MnemeConcurrencyAndVersionScenarioTest.java:80-103` explicitly documents that two unconditional `RemoteCache.put` operations both succeed and last-writer-wins.
    - `MnemeFailureAndRecoveryScenarioTest.java:93-124` demonstrates direct `put` before and after participant failure and `:279-317` demonstrates direct `put` after Mneme restart. These are useful tests of raw infrastructure behavior, not governed-write semantics.

**B. Mneme write-behind bypass of authoritative protocol**

`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`:

- `write(...)` accepts any cache entry and calls `HapiFhirRestClient.saveResourceJson(...)` without an authoritative predecessor: `:99-121`.
- `delete(...)` calls the REST client's delete operation: `:124-132`.
- `containsKey(...)` checks remote persistence presence: `:134-141`.
- `clear()` only avoids clearing FHIR persistence, but is still infrastructure mutation: `:143-147`.

The store is therefore a major later bypass boundary: a cache write can currently become an unconditional Mnemosyne PUT, and a cache delete can become a persistence DELETE.

**C. Direct unconditional Mnemosyne REST mutations**

`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HapiFhirRestClient.java`:

- `saveResourceJson(...)` builds an HTTP PUT with no conditional predecessor header: `:74-95`.
- `deleteResource(...)` builds HTTP DELETE and treats 404/410 as success: `:97-116`.
- `getResourceJson(...)` handles GET/404/410: `:52-71`.

These mechanics should remain hidden behind a later Mnemosyne primitive; ordinary callers should not receive this client.

**D. Mnemosyne service semantics that do not yet satisfy the contract**

`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java`:

- CREATE accepts caller/server ID and sets version 1 initially, but if the logical resource already exists it increments and overwrites it rather than rejecting duplicate CREATE: `:143-207`, especially `:189-205`.
- UPDATE accepts an expected version but silently ignores malformed expected-version text (`NumberFormatException` is swallowed): `:230-285`, especially `:250-259`.
- UPDATE creates a new entity when no existing entity is found (`:245-267`) instead of enforcing UPDATE-only-existing semantics.
- UPDATE increments `entity.getVersionId()` in application code and saves unconditionally (`:261-284`); the conditional atomic database operation required by the future contract is not present.
- Physical/soft delete remains exposed: `:288-304`; it authorizes DELETE, sets `deleted=true`, saves, and logs “Soft-deleted”. This must remain identified as legacy code for this step, but the future governed facade must contain no delete operation.
- `authorizePersistence(...)` is the existing storage-side Themis gate: `:83-130`. This is reusable but does not provide concurrency correctness.

**E. Direct resource providers**

The HAPI providers call `FhirStorageService` directly and expose CRUD annotations. Example:

- `hestia/mnemosyne-clinical/.../provider/TaskResourceProvider.java:54-63` calls `createResource`.
- `:65-78` calls `getResource` and `updateResource` without an expected predecessor.
- `:80-86` exposes `@Delete`.

The same pattern is repeated in the other provider classes under `.../hapifhir/provider/` (Communication, Consent, DocumentReference, Endpoint, Group, HealthcareService, Location, Organization, Person, Practitioner, PractitionerRole, Provenance, RelatedPerson).

**F. Direct Ergon persistence entry point**

`energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/registry/AbstractProviderRegistryChangeErgon.java:262-285` directly commits approved resources through `FhirStorageService`:

- UPDATE calls `storageService.updateResource(resourceId, resource, ifMatch)` (`:273-278`).
- Otherwise it calls `storageService.createResource(resource)` (`:278-280`).
- Its preceding Themis persistence authorization gate is `:240-259`.
- It catches HAPI `PreconditionFailedException` and turns it into an OperationOutcome at `:305-312`, but the current model is still direct FhirStorageService access rather than a typed ActiveStateConflict/AuthoritativeStateConflict result.

**G. Current Pylai boundary and context carrier**

- `pylai/pylai-fhir-registry/.../FhirRestGatewayController.java:237-277` receives CREATE, extracts principal/authorities/security context/correlation/source headers, and submits a change request.
- `:280-323` receives UPDATE and passes HTTP `If-Match` into the submission flow.
- `ChangeRequestSubmissionService.java:102-112` accepts operation/resource/id/payload, Themis context, source/correlation, and `ifMatch`.
- `:150-176` stores these in a `ProviderRegistryChangePragma`; `:178-205` caches the initial Pragma and publishes Petasos work.

This is the likely external ingress/caller boundary, not the location for raw CAS or JPA logic.

**6. Version-domain evidence**

The repository already demonstrates the four-domain split needed by Step 08.03, but current code blurs them:

1. **Mneme/Infinispan entry version**: opaque Hot Rod token obtained from `MetadataValue.getVersion()` in `MnemeConcurrencyAndVersionScenarioTest.java:294-308`; compared by `replaceWithVersion` at `:318-339`. The test itself records this separately from FHIR `meta.versionId`.
2. **Mnemosyne persistence version**: `FhirResourceEntity.versionId` at `FhirResourceEntity.java:46-47`, manipulated by `FhirStorageService` at `:193-199` and `:261-279`.
3. **FHIR `Resource.meta.versionId`**: manufactured by current cache service (`FhirCacheService.java:181-188`) and Mnemosyne service (`FhirStorageService.java:172-185`, `:269-279`).
4. **HTTP ETag/If-Match**: accepted at Pylai controller `FhirRestGatewayController.java:283-311`, stored in Pragma metadata by `ProviderRegistryChangePragma.java:170-175`, and parsed by `FhirStorageService.java:250-259`.

The test’s explicit “opaque token”/separate FHIR version treatment is the best existing evidence for the contract correction: no application arithmetic should be assigned to the Infinispan token, and `replaceWithVersion` must not be presented as a FHIR version comparison.

**7. Recommended scope for the Step 08.03 design document**

The document should identify only a small conceptual set of contracts, with implementation ownership mapped as follows:

- Public governed facade/orchestrator: an existing service/API boundary usable by Pylai and Ergon/Ponos callers; no new deployable subsystem.
- Mneme internal primitive: opaque active token read/consume/CAS and guarded convergence/invalidation; no clinical authority and no raw `RemoteCache` exposure.
- Mnemosyne internal primitive: authoritative read/create/conditional-update and authoritative result/version; no cache mechanics.
- Calliope: reuse canonical `PersistenceOperationEnvelope`, `Pragma`, FHIR/security models, and avoid adding infrastructure mechanics to Calliope.
- Themis: reuse `ThemisSecurityContext`, principal, authority, and authorization decision models.
- Pylai: owns HTTP `ETag`/`If-Match` mapping only; it should not define the internal predecessor model.
- Ergon/Ponos: caller/workflow integration only; no direct RemoteCache or direct unconditional persistence in the eventual safe path.

The document should explicitly call out the existing concrete classes above as migration/bypass targets and state that no implementation restrictions are being applied in Step 08.03.

**Discoveries for future steps**

- No production `mneme-cluster/src/main/java` exists; future Mneme contracts likely need a new API/service surface within existing Hestia modules rather than extending a current service.
- `PersistenceOperationEnvelope` already carries most required provenance/security/idempotency fields but currently models DELETE; reuse must be selective.
- The strongest CAS evidence is test-only: `MnemeConcurrencyAndVersionScenarioTest.java:294-339` and `:580-608`; it clearly separates opaque Infinispan tokens from FHIR `meta.versionId`.
- Current authoritative write code is not atomic/strict: CREATE upserts existing IDs, UPDATE creates missing IDs, malformed expected versions are ignored, and `FhirResourceEntity.versionId` lacks the documented `@Version` annotation.
- `FhirRestCacheStore.write()` -> `HapiFhirRestClient.saveResourceJson()` is the most consequential hidden bypass because cache write-behind currently performs unconditional authoritative PUT.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Constraints**  
Architecture/design deliverable only. The plan must stay strictly within the task boundary: produce the Step 08.03 governed write/concurrency contract design document, and only any minimal documentation-index adjustment if truly needed because the destination path is new. Do not include production code, tests, cache config, schema, or implementation work. Emphasize incorporation of the 08.02B corrections from the attached report, explicit handling of contract sections 1-19 from the issue, and a reviewable doc-first workflow.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

**Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



REVIEW THE PLAN

**Task description**  
Review the proposed delivery plan.

**Feedback**  
Focus on whether a single-step doc-authoring plan is sufficient for this architecture-only task, whether it keeps implementation strictly out of scope, and whether it covers the need to incorporate the Step 08.02B corrections plus all required contract sections and examples/test plan from the issue.



Plan Approved

**Verification**
- Confirmed `docs/design/` does not exist in the repository — the plan's choice to create a new `docs/design/` directory (rather than misusing `docs/architecture.md`, which is Petasos-specific) is correct and grounded.
- Confirmed `docs/README.md` contains a "Documentation Information Architecture" tree (lines 9–139); the plan's step to register the new document there is accurate and necessary.
- Verified all referenced artifacts exist at the claimed paths: `ThemisSecurityContext` (themis-api), `PersistenceOperationEnvelope` (calliope), and the four bypass classes (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`).
- Verified the Verification command (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" ...`) is the canonical AGENTS.md architecture command and the seven `*ArchitectureTest` classes exist — it is a valid regression guard confirming no production code was disturbed.
- Scope discipline: plan's "Out of Scope" and per-step acceptance criteria mirror the parent task's MUST-NOT list (no production Java, no Infinispan XML, no `@Version`, no schema/JPA changes, no DELETE removal). Content-verification is handled via the acceptance-criteria checklist plus a git-status check for zero modified production files.

**Feedback focus assessment**
1. **Single-step sufficiency**: Appropriate. The deliverable is one design document — a single checkable unit. Splitting it artificially would add ceremony without value.
2. **Implementation kept out of scope**: Yes. Doc-first guardrail is explicit; conceptual Java appears only as Markdown code blocks; verification includes a git-status check that no production files changed.
3. **08.02B corrections + all sections + examples/test plan**: All four corrections (opaque token / no arithmetic, Mneme non-authority, CAS convergence loop, deferral of clinical consistency tiers) are explicitly listed in decisions and step acceptance criteria. All 19 doc sections, the 16-scenario contract test plan, the 4-domain version model, conflict/result models, and developer usage examples are enumerated.

**Issues**  
None blocking. The done-signal for a doc-only task cannot automatically verify prose content; the plan appropriately compensates with a detailed acceptance-criteria checklist and a git-status no-production-change check, which is proportionate for a Simple architecture/design deliverable.

**Notes**
- The Verification `mvn` command will trivially pass since no code is changed; its real value is confirming the doc author did not accidentally touch production/config — the plan correctly pairs it with the git-status check for that purpose.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

*** Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Priorities:
- Produce the Step 08.03 architecture/design document only, with strong fidelity to the issue sections and implementation boundary.
- Incorporate the 08.02B corrections explicitly: no arithmetic meaning assigned to Infinispan entry versions; Mneme token consumption is not authority to update; guarded convergence requires a real CAS/reread loop contract; do not classify resource-specific consistency tiers in this step.
- Keep the contract small and hard to misuse. Prefer a handful of concepts over elaborate hierarchies.
- Reuse existing repository context models where appropriate instead of inventing duplicates, especially for security/provenance/correlation context.
- Explicitly call out current bypass points and recommended future prevention mechanisms, but do not implement those restrictions now.
- Because `docs/design/` is new, update `docs/README.md` only as minimally necessary to register the new document.

Known from previous steps:
- ADR-018 authoritative boundary: `docs/architecture-decisions.md:136-233`.
- ADR-019 Mneme coordination boundary: `docs/architecture-decisions.md:420-710`.
- ADR-020 no authoritative physical DELETE: `docs/architecture-decisions.md:757-887`.
- Reusable context artifacts: `calliope/.../PersistenceOperationEnvelope.java`, `themis/themis-api/.../ThemisSecurityContext.java`, `calliope/.../Pragma.java`.
- Current bypass/legacy risk points: `iris/.../FhirCacheService.java`, `energeia/erga/.../TaskCacheService.java`, `hestia/mneme-persistence/.../FhirRestCacheStore.java`, `hestia/mnemosyne-clinical/.../FhirStorageService.java`, and HAPI providers under `.../hapifhir/provider/`.
- Attached 08.02B report already established that current Hot Rod CAS evidence exists in Mneme lab tests, while production code still uses unconditional writes.

Execution discipline:
- Start with narrow file inspections only.
- Use small command timeouts (60-120s max per command) if you run shell commands.
- Prefer targeted verification: check modified files and repository status; only run heavier commands if truly needed.
- Report any commands run and relevant observations in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Governed Write and Concurrency Contract Design Specification (Step 08.03)**

**Summary of Accomplishments**

1. **Authored Authoritative Design Specification**: Created [`docs/design/governed-write-concurrency-contract.md`](/Users/markhunter/Development/SourceCode/Projects/github/harmonia/docs/design/governed-write-concurrency-contract.md), establishing the definitive Strong Hybrid persistence and concurrency architecture contract between caller workflows (Pylai, Energeia Ponos/Erga/Praxis, Iris BEFE) and the storage tiers: **Mneme** (distributed cache & active coordination grid) and **Mnemosyne** (authoritative relational JPA persistence).

2. **Full Section Coverage Across All 19 Required Domains**:
    - **Section 1: Purpose** — Standardizing write and concurrency architecture across the platform.
    - **Section 2: Architectural Context** — Grounded in Strong Hybrid model, ADR-018 (Mnemosyne durable boundary), ADR-019 (Mneme distributed coordination), and ADR-020 (lifecycle transitions vs physical DELETE).
    - **Section 3: Core Architectural Invariants** — Formally defined INV-01 through INV-05 (separation of coordination vs authority, monotonic authoritative progression, guarded convergence, commit visibility over degraded convergence, zero-delete lifecycle).
    - **Section 4: Governed Read Model** — Conceptual Java records (`GovernedRead<T>`, `ResourceKey`, `ActiveCoordinationToken`, `ExpectedAuthoritativeVersion`) and cold-read/warm-read semantics.
    - **Section 5: CREATE Contract** — Strict uniqueness semantics (no upsert, rejection of duplicate keys with `AuthoritativeStateConflictException`), and 5-step create lifecycle.
    - **Section 6: UPDATE Contract** — Complete 5-phase end-to-end execution sequence (Read, Active Coordination CAS, Themis Validation, Authoritative Commit, Guarded Convergence).
    - **Section 7: Active Coordination Contract** — Formally incorporated Task 08.02B corrections: opaque Hot Rod tokens without arithmetic meaning (`token + 1` prohibited), active token consumption is not authority to bypass database predecessor checks, and failure handling.
    - **Section 8: Authoritative Persistence Contract** — `MnemosynePersistencePort` interface and atomic SQL conditional update specification (`WHERE id=:id AND version_id=:expectedVersion`).
    - **Section 9: Conflict Model** — Distinct taxonomy separating `ActiveStateConflict` (in-memory contention, fast retry) from `AuthoritativeStateConflict` (durable state advanced, abort/re-read) and `CommitOutcomeUnknown` (ambiguous transport timeout).
    - **Section 10: Result Model** — `WriteResult<T>`, `WriteStatus`, and `ConvergenceStatus`, specifying that durable database commit success with degraded cache convergence reports success (`COMMITTED_CONVERGENCE_DEGRADED`) to prevent duplicate caller retries.
    - **Section 11: Version Model** — Formal 4-domain matrix separating Mneme entry tokens, Mnemosyne DB versions, FHIR `meta.versionId`, and HTTP ETags (`If-Match`), with explicit conflation prohibitions.
    - **Section 12: Guarded Mneme Convergence** — CAS convergence loop algorithm with re-read and enforcement of the Newer-Version Invariant ($V_{cache} \ge V_{committed}$).
    - **Section 13: Commit Outcome Semantics** — Handling ambiguous network timeouts via `CommitOutcomeUnknownException` and idempotent correlation/causation reconciliation without guessing outcomes.
    - **Section 14: Security and Provenance Context** — Reusing existing models (`ThemisSecurityContext` and `PersistenceOperationEnvelope` / `Pragma` context fields) without duplicate context classes.
    - **Section 15: API Placement and Service Boundaries** — Clean separation of contracts (`calliope` / `hestia-api`), active coordinator (`hestia/mneme-*`), persistence port (`hestia/mnemosyne-*`), and orchestrator facade (`hestia/hestia-governance`).
    - **Section 16: Bypass Prevention and Legacy Remediation** — Detailed catalog of existing bypass points (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`, HAPI providers) and future ArchUnit rule definitions.
    - **Section 17: Developer Usage Examples** — Idiomatic Java code snippets for Governed CREATE, Governed UPDATE with retry, and conflict handling.
    - **Section 18: Contract Test Plan** — 16 mandatory verification scenarios (ST-01 to ST-16) detailing preconditions, execution sequence, expected results, and mandatory assertions.
    - **Section 19: Deferred Questions and Future Extensions** — Formally deferred resource-specific clinical consistency tiers (per Task 08.02B review), semantic 3-way merges, distributed multi-resource sagas, and physical retention purges.
    - **Section 20: References** — Links to authoritative ADRs, concepts, and persistence lifecycle documents.

3. **Updated Documentation Information Architecture**: Updated [`docs/README.md`](/Users/markhunter/Development/SourceCode/Projects/github/harmonia/docs/README.md) to register the new `docs/design/` directory and index `governed-write-concurrency-contract.md`.

4. **Zero Production Code Alterations**: No production `.java` source files, schemas, XML descriptors, or test configurations were modified.

5. **Verification**: Executed ArchUnit architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`), passing 57/57 tests with 0 failures, 0 errors.

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` completes in ~10 seconds across all 33 Maven modules.
- **Recommendations**: When implementing the Java interfaces and adapters in subsequent steps, place pure interfaces in `hestia-api` (or `calliope`) and encapsulate `RemoteCacheManager` and `FhirResourceRepository` behind package-private ports to eliminate direct bypass risks.
- **Fragile APIs**: Existing `FhirStorageService` methods swallow `NumberFormatException` during `If-Match` parsing and perform upserts on CREATE; these should be replaced with strict conditional operations conforming to `MnemosynePersistencePort`.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Please verify the architecture/design document and docs index update against the issue requirements. Focus especially on:
- whether the document truly stays within the implementation boundary (no production-code changes disguised as design);
- whether it explicitly incorporates the 08.02B corrections: opaque Infinispan token with no arithmetic meaning, Mneme token consumption is not authority to update, guarded convergence requires a true CAS/reread loop, and no resource-specific consistency tier classifications in this step;
- whether all required contract areas are covered with correct semantics: governed read, CREATE, UPDATE, active coordination, authoritative persistence, conflict model, result model, version model, commit-outcome-unknown, security/provenance context, API placement, bypass prevention, developer examples, and contract test plan;
- whether the new `docs/design/` path is properly integrated in `docs/README.md`.
  If there are issues, separate real defects from cosmetic preferences.



Implementation Approved

**Verification**
- **README integration** (`docs/README.md`): The diff cleanly inserts a `design/` node into the documentation tree with a description and a link to `governed-write-concurrency-contract.md`, placed logically between `architecture/` and `middleware/`. Correct and minimal.
- **Document completeness** (`docs/design/governed-write-concurrency-contract.md`, 794 lines): All 19 required sections are present (1 Purpose … 19 Deferred Questions), plus a References section. Structure matches the plan's section-by-section mapping.
- **Task 08.02B corrections explicitly incorporated**:
    - Opaque token, no arithmetic meaning — §7.1 and §11.2 (`token + 1` explicitly prohibited); record javadoc in §4.1 also states it.
    - Mneme token consumption ≠ authority to update — §7.2 and INV-01 (§3).
    - Guarded convergence via true CAS/re-read loop — §12.1 provides a real `getWithMetadata` → newer-version check → `replaceWithVersion` retry loop with newer-version invariant and exhaustion/invalidation handling.
    - Deferral of resource-specific clinical consistency tiers — §19.1 explicitly defers them; no tier classification appears elsewhere.
- **Required contract areas covered with correct semantics**: Governed read (§4, cold/warm), CREATE strict-no-upsert (§5), UPDATE 5-phase sequence (§6), active coordination + failure modes (§7), authoritative conditional SQL update with row-count semantics (§8), 3-way conflict taxonomy (§9), result model with `WriteStatus`/`ConvergenceStatus` and commit-success-with-degraded-convergence INV-04 (§10), 4-domain version model with ownership/lifetime/comparison matrix and conflation prohibitions (§11), CommitOutcomeUnknown protocol + idempotent reconciliation (§13), reuse of `ThemisSecurityContext` for security/provenance (§14), API placement (§15), bypass catalog + ArchUnit strategy (§16), developer examples (§17), and all 16 contract test scenarios ST-01…ST-16 with preconditions/expected outcomes/assertions (§18).
- **Conceptual Java shapes present**: `GovernedRead`, `ResourceKey`, `ActiveCoordinationToken`, `ExpectedAuthoritativeVersion`, `GovernedWriter`, `GovernedWriteContext`, `WriteResult`, `MnemosynePersistencePort`, token/version records — all as Markdown code blocks only.
- **Doc-first guardrail honored**: Executor's own diff touched only `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`, and a `.junie/plans` file. No production `.java`, schema, or config was modified by this step. (Other modified files in the working tree — test java, latex, architecture-decisions.md — are pre-existing changes from earlier steps, not this executor's work.)
- **Referenced links resolve**: `architecture-decisions.md`, `architecture/persistence-lifecycle.md`, `concepts/mneme.md`, `concepts/mnemosyne.md`, `security/architecture.md` all exist; ADR-018/019/020 headings exist.
- **Regression**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → BUILD SUCCESS, 57/57 tests pass (0 failures/errors). Ran ~10–20s after build.

**Issues**  
None blocking.

**Notes**
- Minor (non-blocking) doc-link concern: the ADR-018 link `#adr-018--mnemosyne-defines-...` uses two hyphens, but the ADR-018 heading in `architecture-decisions.md` uses three literal hyphens (`ADR-018 --- Mnemosyne ...`), whereas ADR-019/020 use an em-dash. On GitHub's anchor algorithm the ADR-018 slug would contain five hyphens, so that single anchor link may not resolve. ADR-019/020 links are fine. This is a pre-existing inconsistency in the source heading, not a contract-content defect; worth a trivial fixup in a later pass.
- The working tree contains many unrelated modified files (mneme-cluster test java, latex chapters, architecture-decisions.md). These are outside Step 1's scope and were not produced by this executor's diff; they do not affect this step's verdict, and the ArchUnit suite remains green regardless.

**Discoveries for future steps**
- ArchUnit regression command runs in ~10–20s once modules are built; a 600s timeout is far more than needed — ~120s is sufficient on warm cache. Full suite = 57 tests across 7 ArchitectureTest classes.



**Requirements**

**Goal / Outcome**  
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

**Scope**
- **In Scope**:
    - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
    - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
    - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
    - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
    - Implementing production Java interfaces, services, or adapters.
    - Modifying Infinispan XML or cache configuration.
    - Modifying database schemas, JPA entities, or `@Version` annotations.
    - Implementing conditional REST/JPA write methods.
    - Removing legacy DELETE endpoints or production code in this step.

**Done When**
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

**Technical Design**

**Decisions**
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

**Approach & Touches**
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
    - Section 1: Purpose
    - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
    - Section 3: Invariants (Separation of coordination vs authoritative commit)
    - Section 4: Governed Read Model (`GovernedRead<T>`)
    - Section 5: CREATE Contract (Strict uniqueness, no upsert)
    - Section 6: UPDATE Contract (Complete end-to-end sequence)
    - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
    - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
    - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
    - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
    - Section 11: Version Model (4 domains matrix & comparison rules)
    - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
    - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
    - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
    - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
    - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
    - Section 17: Developer Usage Examples (Idiomatic developer experience)
    - Section 18: Contract Test Plan (16 mandatory verification scenarios)
    - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

**Nuances / Risks / Corners**
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

**Contracts (Conceptual Interface Shapes in Design Doc)**
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

**Testing**

**Checklist**
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

**Assumptions & Open Questions**

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

**Delivery Steps**

**✓ Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index**  
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.  
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`  
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Authored the Step 08.03 governed write/concurrency contract design at `docs/design/governed-write-concurrency-contract.md`, defining the Strong Hybrid contract between Mneme coordination and Mnemosyne authoritative persistence.
- Kept the work strictly architecture-only: no production Java, schema, cache config, or protocol implementation changes were made.

**Changes**
- Added the full design document covering purpose, invariants, governed read/CREATE/UPDATE contracts, active coordination, authoritative persistence, conflict/result/version models, guarded convergence, commit-outcome semantics, security/provenance context, API placement, bypass prevention, developer examples, test plan, and deferred questions.
- Incorporated the Step 08.02B corrections explicitly: opaque Mneme token semantics, Mneme non-authority, real CAS/reread convergence requirements, and deferral of resource-specific consistency tiers.
- Updated `docs/README.md` to register the new `docs/design/` documentation location and link the contract document.

**Verification**
- Reviewed the documentation changes against the issue requirements; reviewer found no blocking issues.
- Ran `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` successfully (57/57 tests passed).

**Notes**
- No PR/branch workflow was activated because the task did not request shipping this work via a dedicated branch or pull request.

This task has no prior planning phase. Before implementation, analyze the task and codebase, define acceptance criteria if not explicitly provided in the task description, and plan your approach.   
The Reviewer must independently define its own acceptance criteria and will verify them.

**✓ Step 2: Update / Follow-up**  
You have executed the wrong task.

The work just completed is effectively a refinement/re-run of Step 08.03.  
Retain any useful documentation corrections you made, but DO NOT continue  
working on the 08.03 design document.

We are now performing:

HARMONIA — TASK 08  
STEP 08.04A — FOUNDATIONAL GOVERNED-WRITE CONTRACTS

The purpose of 08.04A is specifically to IMPLEMENT the small Java  
caller-facing contract so that we can inspect the actual API before  
implementing the runtime protocol.

This step MUST create production Java contract types and their unit tests.

It MUST NOT implement Mneme CAS, Mnemosyne conditional persistence,  
convergence, or migrate any existing write path.


IMPLEMENT ONLY THE MINIMUM CONTRACT VOCABULARY

First inspect the existing module dependency graph and choose the smallest  
existing suitable module/package.

DO NOT create:

    hestia-governance
    hestia-api
    a new Maven module
    a new subsystem
    a new deployable service

If no existing module can host the contract without an unacceptable  
dependency cycle, STOP and report that problem rather than inventing a module.


Implement approximately the following concepts, simplifying where possible:

    ResourceKey

    ActiveCoordinationToken

    AuthoritativeVersion
        or ExpectedAuthoritativeVersion

    GovernedRead<T>

    GovernedWriteContext
        ONLY if actually required;
        reuse existing ThemisSecurityContext /
        PersistenceOperationEnvelope / Pragma context rather than
        duplicating correlation, causation, principal or provenance fields

    GovernedWriter

    WriteResult<T>

    minimal conflict/precondition types


REQUIRED SEMANTICS

ActiveCoordinationToken:

    immutable
    opaque
    no arithmetic
    no ordering
    no Infinispan/Hot Rod type exposed

GovernedRead<T>:

    resource key
    resource
    active coordination token
    authoritative predecessor/version

GovernedWriter:

    CREATE
    UPDATE

There is NO governed DELETE.

UPDATE should accept the GovernedRead<T> so the caller does not manually  
assemble concurrency tokens.


AUTHORITATIVE PRECONDITION MODEL

Do NOT collapse these into one vague conflict:

    RESOURCE_ALREADY_EXISTS

        CREATE expected authoritative absence.

    EXPECTED_VERSION_MISMATCH

        UPDATE expected authoritative predecessor X.

They may share a small conceptual AuthoritativePreconditionConflict model.

Keep:

    ActiveStateConflict

separate.


AUTHORITATIVE COMMIT OUTCOME

Represent explicitly:

    COMMITTED
    NOT_COMMITTED
    UNKNOWN

UNKNOWN is first-class.

Do NOT reduce it to:

    success = false

and do not expose it merely as a generic exception that encourages blind  
retry.


WRITE RESULT

Design WriteResult<T> so invalid state combinations are difficult to create.

It must cleanly represent:

    successful authoritative commit

    active-state conflict

    authoritative precondition conflict

    known non-commit/failure

    authoritative outcome UNKNOWN

    committed but Mneme convergence degraded

Prefer a small sealed result model if that makes the invariants clearer.

Do not create a large nullable status bag.


TESTS

Add unit tests demonstrating the ACTUAL API:

1. GovernedRead carries active and authoritative concurrency context.
2. ActiveCoordinationToken is opaque.
3. CREATE conflict can express RESOURCE_ALREADY_EXISTS.
4. UPDATE conflict can express EXPECTED_VERSION_MISMATCH.
5. Active conflict is distinct from authoritative conflict.
6. COMMITTED / NOT_COMMITTED / UNKNOWN are distinct.
7. committed + degraded convergence is representable as committed.
8. no governed DELETE exists.
9. invalid WriteResult combinations cannot readily be constructed.


ARCHITECTURE GUARDRAILS

Add only low-risk architecture tests appropriate to the new contract:

    no Infinispan dependency exposed
    no RemoteCache exposed
    no JPA dependency exposed
    no HTTP framework dependency exposed
    no governed DELETE

Do NOT block or migrate existing legacy write paths yet.


DO NOT IMPLEMENT

    Mneme CAS
    coordination cache
    guarded convergence
    Mnemosyne conditional SQL/JPA
    @Version
    database/schema changes
    BEFE migration
    Pylai migration
    Ergon/Ponos migration
    retries
    semantic conflict handling
    information-authority handling
    DELETE removal


REQUIRED FINAL OUTPUT

Show me the ACTUAL resulting Java API, including package names.

Show examples using the ACTUAL types for:

    CREATE
    UPDATE
    ActiveStateConflict
    ResourceAlreadyExists
    ExpectedVersionMismatch
    CommitOutcomeUnknown
    CommittedButConvergenceDegraded

List every production Java file and test file added or changed.

Explain why the selected existing module/package is the correct dependency  
location.

Run the relevant unit tests and architecture tests.

Explicitly confirm:

    no write path was migrated;
    no new Maven module was created;
    no new subsystem was created;
    no runtime concurrency mechanism was implemented.

STOP after 08.04A.

Do not begin 08.04B.



**Summary**
- Implemented Step 08.04A in existing module `calliope`, adding the caller-facing governed-write contract in package `net.fhirfactory.harmonia.model.governedwrite` with no new Maven module or subsystem.
- Kept the work strictly contract-level: no write path migration, no Mneme CAS/runtime concurrency logic, no Mnemosyne conditional persistence/JPA changes, and no governed DELETE API.

**Changes**
- Added production types: `ResourceKey`, `ActiveCoordinationToken`, `AuthoritativeVersion`, `ExpectedAuthoritativeVersion`, `GovernedRead<T>`, `GovernedWriter`, `WriteResult<T>`, `ActiveStateConflict`, `AuthoritativePreconditionConflict`, and supporting enums for commit outcome, convergence, and precondition reason.
- Added `calliope` unit tests covering the required API scenarios and `paradeigma-test` architecture guardrails ensuring no Infinispan/RemoteCache, JPA, HTTP, or governed DELETE leakage from the new contract package.
- Reused `ThemisSecurityContext` directly in `GovernedWriter` signatures instead of inventing a duplicate write-context carrier.

**Verification**
- Reviewer confirmed the API is minimal, caller-facing, cycle-safe in `calliope`, and that existing BEFE/Pylai/Ergon/Hestia write paths were untouched.
- Passed `mvn test -pl calliope -Dtest=GovernedWriteContractTest` and `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.

**Notes**
- Also retained the earlier useful documentation corrections to `docs/design/governed-write-concurrency-contract.md` without continuing 08.03 work.

