Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Map the current production clinical FHIR CREATE/UPDATE write path and the likely code/report locations for this exploration-only task. Identify concrete entry points, downstream services/providers/stores, and likely bypass/concurrency/version/audit/delete artifacts. Include precise file paths and line numbers for the most relevant classes/methods so later analysis can avoid re-searching. Also note whether the attached `docs/latex/chapters/appendix-decisions.tex` appears relevant contextually. Focus on production code; tests only if they directly evidence current behavior.

**Constraints**  
Read-only exploration only. Do not modify code or run broad builds. Prefer static tracing; run only narrow, existing commands if essential.



Checking recent IDE activity

Open file

Exploration is Ready

**Task 08.01 current-state clinical write-path exploration**

**1. Executive summary**

**OBSERVED:** There are two materially different production write surfaces:

1. **Iris BEFE JAX-RS resources** under `iris/iris-befe/.../rest`. `POST` and `PUT` parse concrete FHIR R5 resources, call `FhirCacheService.saveResource`, and perform an unconditional `RemoteCache.put`. The configured Mneme persistence store then sends a downstream HTTP `PUT` to Mnemosyne. BEFE returns `201 Created` or `200 OK` after `saveResource` returns.
2. **Pylai FHIR Provider Registry gateway** under `pylai/pylai-fhir-registry`. `POST` and `PUT` validate and enqueue a Pragma through Petasos, then immediately return `202 Accepted`. The later Provider Registry Ergon calls `FhirStorageService` directly and writes PostgreSQL through JPA.

**OBSERVED:** The BEFE path is not a FHIR-authoritative create/update protocol. A BEFE `POST` becomes `RemoteCache.put`, then `FhirRestCacheStore.write`, then Mnemosyne HTTP `PUT`; on Mnemosyne, that invokes the generic update method, which creates if no row exists and otherwise increments the database version. The BEFE resource response is generated before any explicit read-back or version reconciliation.

**OBSERVED:** The Pylai path is asynchronous and carries `If-Match` through Pragma metadata, but the direct Mnemosyne FHIR providers do not receive an expected version. The Provider Registry Ergon does pass `ifMatch` into the overloaded `FhirStorageService.updateResource` method.

**OBSERVED:** Production BEFE update uses unconditional `RemoteCache.put`. No production use of `replace`, `replaceWithVersion`, `putIfAbsent`, or `getWithMetadata` was found in the clinical write path. The Mneme laboratory directly characterizes this as last-writer-wins and a lost-update risk (`hestia/mneme-cluster/src/test/.../MnemeConcurrencyAndVersionScenarioTest.java:78-177, 267-359`).

**OBSERVED:** Mneme cache configuration has replicated caches in `SYNC` mode and a `FhirRestCacheStore`, but no `<async>` or write-behind configuration in the relevant cache definitions (`hestia/mneme-cluster/src/main/resources/infinispan.xml:56-71`, repeated for each clinical cache). `FhirRestCacheStore.write` propagates a failed downstream result as a failed `CompletionStage` (`.../FhirRestCacheStore.java:99-121`).

**INFERRED:** For the BEFE path, a successful `RemoteCache.put` should include successful completion of the synchronous store write and therefore a successful downstream Mnemosyne HTTP 2xx response. The implementation does not itself expose a durable-commit token or prove PostgreSQL commit to the caller; the Spring `@Transactional` service boundary strongly implies that normal 2xx generation occurs after transaction commit, but this should not be treated as an explicit cross-system contract without runtime evidence.

**OBSERVED:** No automatic Provenance creation or Kleio audit dispatch is connected to either the BEFE cache write or the Mnemosyne generic FHIR persistence methods. The Pylai Pragma preserves requester/source/principal/authorities/security context and correlation metadata, but the inspected Ergon commit path does not dispatch audit evidence.

**2. Current production entry points**

**Iris BEFE / Mneme entry points**

The concrete JAX-RS resources are listed by:

`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/`

The directory contains `PersonResource`, `RelatedPersonResource`, `PractitionerResource`, `PractitionerRoleResource`, `OrganizationResource`, `LocationResource`, `HealthcareServiceResource`, `GroupResource`, `CommunicationResource`, `ConsentResource`, `DocumentReferenceResource`, and `ProvenanceResource`, plus `AuditEventResource`, task and operational resources. The concrete resource classes use the same CRUD pattern as `PractitionerResource`:

- `PractitionerResource.java:33-40` — `/fhir/Practitioner`, injected `FhirCacheService`.
- `PractitionerResource.java:64-75` — `POST`, blank-body check, parser, `saveResource`, `201 Created`.
- `PractitionerResource.java:78-90` — `PUT /{id}`, blank-body check, parser, force path ID, `saveResource`, `200 OK`.
- `PractitionerResource.java:92-100` — physical active-cache `DELETE` via `deleteResource`.

`PersonResource`, `RelatedPersonResource`, `OrganizationResource`, `LocationResource`, `HealthcareServiceResource`, `GroupResource`, `CommunicationResource`, `ConsentResource`, `DocumentReferenceResource`, and `ProvenanceResource` follow this same direct cache-backed pattern. `ProvenanceResource.java:64-90` confirms that Provenance itself is treated as an ordinary cache-backed CRUD resource; it is not automatically generated by other writes.

The BEFE request authorization filter is:

`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`

- `:118-142` — identifies `/fhir/*`, maps HTTP method/resource ID to a Themis action, and denies malformed/unsupported requests.
- `:144-207` — extracts the trusted container principal, builds security context/resource/action, evaluates Themis, and denies on null/denied decisions.
- `:209-216` — stores principal, authorities, decision, and security context for downstream processing.

**OBSERVED:** The filter authorizes the HTTP request, but `FhirCacheService.saveResource` does not consume the resulting authorization decision and performs no additional Themis call.

**Pylai Provider Registry entry points**

`pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java`

- `:236-278` — asynchronous `POST /{resourceType}` create endpoint.
- `:280-324` — asynchronous `PUT /{resourceType}/{id}` update endpoint; accepts optional `If-Match` at `:288` and passes it to the submission service at `:301-312`.
- `:248-265` and `:294-311` — invokes `FhirSecurityInterceptor.authorize`, then propagates Themis principal, authorities, security context, source, correlation ID, and `If-Match`.
- `:268-277` and `:314-323` — returns an FHIR Task representation with `202 Accepted`, status location, correlation ID, and `Retry-After`; no durable clinical resource has been committed at this point.

Supported Pylai Provider Registry types are explicitly limited to Practitioner, PractitionerRole, Organization, Location, HealthcareService, Endpoint, and Group (`calliope/src/main/java/net/fhirfactory/harmonia/model/registry/ProviderRegistryConstants.java:70-90`).

**Direct Mnemosyne FHIR entry points**

The HAPI servlet is registered at `/fhir/*` by:

`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/FhirServerConfig.java:26-40`

It discovers all `IResourceProvider` beans through:

`.../config/JpaRestfulServer.java:31-57`

Representative direct provider:

`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/PractitionerResourceProvider.java`

- `:33-45` — provider registration for Practitioner.
- `:47-55` — HAPI `@Create`, calls `storageService.createResource`.
- `:62-69` — HAPI `@Update`, calls `storageService.updateResource(id, resource)` with no expected version.
- `:71-77` — HAPI `@Delete`, calls soft-delete service.

The other Mnemosyne providers use the same pattern for Communication, Consent, DocumentReference, Endpoint, Group, HealthcareService, Location, Organization, Person, PractitionerRole, Provenance, RelatedPerson, and Task. Their direct update methods generally call the no-expected-version overload.

**3. Current CREATE sequence**

**A. Representative BEFE Practitioner create**

```text
FHIR caller
  -> BEFE PractitionerResource.create
  -> FhirCacheService.saveResource
  -> RemoteCache.put(id, json)
  -> replicated SYNC Infinispan cache
  -> FhirRestCacheStore.write
  -> HapiFhirRestClient.saveResourceJson(..., PUT)
  -> Mnemosyne HAPI Practitioner @Update
  -> FhirStorageService.updateResource(id, resource, expectedVersion=null)
  -> FhirResourceRepository.findByResourceTypeAndFhirId
  -> new FhirResourceEntity if absent
  -> repository.save
  -> Spring transaction completion / PostgreSQL
  -> HTTP 2xx to cache store
  -> RemoteCache.put returns
  -> BEFE returns HTTP 201
```

Key evidence:

- `iris/.../PractitionerResource.java:64-75` parses the payload and calls `saveResource`.
- `iris/.../FhirCacheService.java:160-192` derives/generates the ID, applies default security tag, increments/sets FHIR `meta.versionId`, serializes JSON, and calls `putResourceJson`.
- `iris/.../FhirCacheService.java:152-157` calls unconditional `remoteCache.put(id, jsonPayload)`.
- `hestia/.../FhirRestCacheStore.java:100-121` always maps a cache write to `saveResourceJson`, and returns a failed stage when the REST client reports non-2xx.
- `hestia/.../HapiFhirRestClient.java:74-94` always sends HTTP `PUT`; every 2xx status is treated as success, and the response body is discarded.
- `hestia/.../FhirStorageService.java:224-285` updates an existing entity or creates one if no entity exists. It sets the resource ID and meta version, saves through the repository, and returns the resource.

**OBSERVED:** The BEFE POST does not use a downstream POST. The persistence adapter has only a generic `write` method and translates it to HTTP PUT. Therefore the downstream Mnemosyne interaction is an update-style FHIR operation, with upsert behavior implemented in `FhirStorageService.updateResource`.

**OBSERVED:** A BEFE create with no ID generates a UUID at `FhirCacheService.java:163-166`; a BEFE create with an ID preserves the logical ID. Mnemosyne's update method creates a database row if the ID is absent from PostgreSQL (`FhirStorageService.java:246-267`).

**OBSERVED:** Validation on the BEFE path consists primarily of blank-body checking and HAPI parser conversion in each concrete resource class. No resource-specific business validation, reference validation, or create/update existence policy is present in the BEFE resource handlers inspected.

**B. Representative Pylai Provider Registry create**

```text
FHIR caller
  -> FhirRestGatewayController.createResource
  -> FhirSecurityInterceptor.authorize
  -> ChangeRequestSubmissionService.submitChangeRequest(CREATE)
  -> parse/structural validation
  -> ProviderRegistryChangePragma.buildChangeRequestPragma
  -> PragmaCacheService.savePragma
  -> Petasos.send(PetasosMessage)
  -> HTTP 202 Task response

later:
  Petasos/Artemis consumer and Praxis/Ergon pipeline
  -> AbstractProviderRegistryChangeErgon.processErgon
  -> reference/business validation
  -> independent Themis persistence authorization
  -> FhirStorageService.createResource
  -> repository.save
  -> PostgreSQL transaction
  -> Pragma COMPLETED and resulting version metadata
```

Evidence:

- `ChangeRequestSubmissionService.java:102-158` validates body/resource type, parses FHIR, forces the URL ID for update, and builds the Pragma.
- `ChangeRequestSubmissionService.java:160-176` attaches principal, authorities, security context, and policy version.
- `ChangeRequestSubmissionService.java:178-205` stores the Pragma, publishes Petasos, catches publication exceptions, and still returns a `SubmissionResult`.
- `AbstractProviderRegistryChangeErgon.java:117-196` extracts the resource and performs reference/business validation.
- `...AbstractProviderRegistryChangeErgon.java:218-259` performs a second Themis persistence authorization check.
- `...AbstractProviderRegistryChangeErgon.java:262-303` invokes `FhirStorageService.createResource`, records the resulting FHIR version, adds persisted output, and marks the Pragma completed.

**4. Current UPDATE sequence**

**BEFE update**

`PractitionerResource.update` (`iris/.../PractitionerResource.java:78-90`) parses the payload, overwrites its resource ID with `Practitioner/{id}`, and calls the same `FhirCacheService.saveResource` used by create.

`FhirCacheService.java:176-192` derives the next FHIR version from the submitted payload's existing `meta.versionId`, not from `getWithMetadata` or a durable read. It then unconditionally puts the complete JSON string.

**OBSERVED:** There is no `If-Match` header handling in BEFE resource classes, no ETag comparison, no `replaceWithVersion`, no `getWithMetadata`, and no explicit stale-write check.

The Mnemosyne side (`FhirStorageService.java:245-284`) reads the database row and increments its own persisted `versionId`. If an expected version is supplied, it compares it; the BEFE cache-store PUT supplies no expected version header, so this check is bypassed.

**Pylai update**

Pylai accepts `If-Match` and stores it as Pragma metadata:

- `FhirRestGatewayController.java:283-312`.
- `ChangeRequestSubmissionService.java:150-157` passes it to `ProviderRegistryChangePragma.buildChangeRequestPragma`.
- `ProviderRegistryChangePragma.java:162-175` stores it under `METADATA_IF_MATCH`.
- `AbstractProviderRegistryChangeErgon.java:210-212` reads it.
- `...AbstractProviderRegistryChangeErgon.java:273-279` passes it to `storageService.updateResource(resourceId, resource, ifMatch)`.

`FhirStorageService.java:250-260` strips `W/` and quotes and parses a numeric expected version. A numeric mismatch throws `PreconditionFailedException`; malformed expected versions are silently ignored by the `NumberFormatException` catch. The Ergon maps that exception to a failed Pragma/concurrency outcome at `AbstractProviderRegistryChangeErgon.java:305-312`.

**OBSERVED:** This is an application-level PostgreSQL version comparison available only on the Pylai/Ergon path. It is not used by the direct Mnemosyne HAPI provider update methods, which call the no-expected-version overload (`PractitionerResourceProvider.java:62-69`), nor by the BEFE cache-store path.

**5. Current success boundaries**

**BEFE path**

Potential success points, in order:

1. FHIR parser succeeds in the BEFE resource handler.
2. `FhirCacheService.saveResource` mutates the resource and serializes it.
3. `RemoteCache.put` completes. With the configured synchronous store, this is expected to include cache replication and store completion.
4. `FhirRestCacheStore.write` receives a `true` REST result and completes successfully.
5. Mnemosyne FHIR HTTP endpoint returns 2xx.
6. `FhirStorageService` returns after `repository.save`; Spring transaction interception then commits before the HTTP request completes in normal operation.
7. BEFE returns `201` for POST or `200` for PUT.

**OBSERVED:** The external caller receives success only after `saveResource` returns (`PractitionerResource.java:71-75, 85-89`).

**INFERRED:** A normal successful BEFE `RemoteCache.put` means the REST client observed a Mnemosyne 2xx response, because the cache store is configured without asynchronous write-behind and failed REST results complete exceptionally (`FhirRestCacheStore.java:111-121`).

**INFERRED:** In the normal Spring transaction path, the 2xx response should follow successful transaction completion because `createResource`/`updateResource` are `@Transactional` (`FhirStorageService.java:143-149, 224-235`). This is not represented to BEFE as a separate commit acknowledgement, and `HapiFhirRestClient` does not inspect response content or return a version.

**At the instant BEFE returns success, the durable fact definitely established by code:**

- **OBSERVED:** Mneme contains the submitted JSON after the successful cache put.
- **INFERRED:** Mnemosyne returned HTTP 2xx from its FHIR endpoint.
- **INFERRED:** The corresponding JPA transaction normally committed before that HTTP response.
- **OBSERVED:** No explicit proof/read-back exists that the cache value equals the JSON/version finally stored by Mnemosyne.

**Pylai path**

**OBSERVED:** Pylai returns `202 Accepted` after local Pragma status storage and the `petasos.send` call returns. It does not wait for Ergon validation or PostgreSQL persistence.

**OBSERVED:** `ChangeRequestSubmissionService` catches any Petasos publication exception and still returns a successful `SubmissionResult` (`ChangeRequestSubmissionService.java:183-205`). Therefore even the work-acceptance boundary is not unambiguously guaranteed by the current controller behavior.

**OBSERVED:** The actual authoritative resource success is represented later by `PragmaStatus.COMPLETED` after `FhirStorageService` returns (`AbstractProviderRegistryChangeErgon.java:292-300`).

**6. Version model**

| Version dimension | Current location/representation | Used by | Relationship to others |
|---|---|---|---|
| FHIR `meta.versionId` | String in FHIR JSON; set by BEFE as numeric string in `FhirCacheService.java:181-188`; set by Mnemosyne from `Long versionId` in `FhirStorageService.java:269-279` | External resource representation and returned FHIR payload | Not equated to Infinispan entry version. BEFE derives from submitted payload; Mnemosyne derives from DB row. |
| Infinispan entry version | Hot Rod metadata `long`, visible through `MetadataValue` | Available through `getWithMetadata` and conditional operations | Production BEFE path does not read or use it. Laboratory uses it in `MnemeConcurrencyAndVersionScenarioTest.java:105-146, 294-339`. |
| Mnemosyne/JPA persisted version | `Long versionId` field on `FhirResourceEntity`, default `1L`, not annotated with JPA `@Version` (`FhirResourceEntity.java:45-48`) | `FhirStorageService` explicit comparison/increment | Independent from Hot Rod version. Used only when expected version is supplied on `FhirStorageService.updateResource`. |
| HTTP ETag / If-Match | Pylai controller accepts raw `If-Match`; read controller emits weak ETag from FHIR `meta.versionId` (`FhirRestGatewayController.java:168-178, 283-312`) | Pylai async Provider Registry update and Pylai read response | Pylai passes raw value into numeric Mnemosyne expected-version check. BEFE neither emits nor consumes ETag/If-Match. |

**OBSERVED:** No production code converts an Infinispan entry version into FHIR `meta.versionId`, or vice versa. The laboratory deliberately treats them as separate values (`MnemeConcurrencyAndVersionScenarioTest.java:102-114, 160-168, 341-349`).

**OBSERVED:** Mnemosyne's persisted `versionId` is copied into the resource's FHIR `meta.versionId` when Mnemosyne constructs the response (`FhirStorageService.java:269-284`). The BEFE cache store discards the downstream response body, so the cache is not refreshed with that resulting representation.

**OBSERVED:** The generic `PersistenceOperationEnvelope` contains an optional `Long expectedVersion` and explicit CREATE/UPDATE/DELETE constructors (`calliope/.../PersistenceOperationEnvelope.java:56-80, 143-175, 436-480`), but no production clinical write path located here consumes that envelope. `PersistenceOperationType` still includes DELETE (`.../PersistenceOperationType.java:20-27`).

**7. Concurrency behavior**

**OBSERVED:** Production BEFE updates are unconditional whole-resource string replacements. `FhirCacheService` only uses `RemoteCache.get` for reads and `RemoteCache.put` for writes (`FhirCacheService.java:138-155`); there is no conditional operation.

**OBSERVED/test evidence:** The Mneme laboratory scenario executes two reads with equal Hot Rod entry versions, then two unconditional puts. The second write overwrites the first (`MnemeConcurrencyAndVersionScenarioTest.java:105-177`). The multi-field scenario records the first writer's independent field as lost (`:240-265`).

**OBSERVED/test evidence:** The laboratory's `replaceWithVersion` scenario proves Infinispan can reject a stale entry version, but explicitly records that this capability is not used in production (`:318-359`).

**OBSERVED:** Mnemosyne has an explicit version check only when `expectedVersion` is nonblank (`FhirStorageService.java:250-260`). Direct HAPI provider updates and cache-store PUTs do not pass it.

**CONCLUSION:** Two BEFE writers reading the same active resource and subsequently updating it can both succeed at the Mneme layer; the later unconditional whole-resource write can overwrite the earlier update. This is **OBSERVED** for the current Hot Rod operation pattern and **INFERRED** for the complete production BEFE-to-Mnemosyne sequence because no runtime integration test was run here.

**8. Failure-window matrix**

| Failure point | Mneme state | Mnemosyne state | Caller result / recovery | Risk classification |
|---|---|---|---|---|
| A. Mneme unavailable before write | No new cache entry; `requireRemoteCache` throws or Hot Rod operation fails | No downstream request, so unchanged/unknown | BEFE exception-to-response mapping is not defined in resource class; likely non-success, but exact HTTP status is **UNKNOWN**. Caller may retry. | **OBSERVED/UNKNOWN**; no duplicate unless caller retries; no new durable loss. |
| B. Mneme operation reaches store but Mnemosyne REST fails | Cache put should fail under synchronous store; replication/cache visibility may depend on Infinispan failure ordering | Unchanged if request rejected before transaction, or possibly changed if failure is after commit; exact state is **UNKNOWN** | Store converts false/non-2xx to `PersistenceException`; exception propagates from store stage. | **OBSERVED** propagation; **UNKNOWN** final split-brain state at network boundary. |
| C. Mnemosyne receives operation but JPA/PostgreSQL commit fails | Cache write may already have been accepted/replicated before store completion; exact retained state is **UNKNOWN** | Transaction should roll back/fail in normal Spring behavior; exact database state depends on failure point | HTTP should be non-2xx/error; BEFE store stage fails. No automatic cache compensation exists. | **INFERRED** inconsistency window; retry possible but duplicate/upsert behavior is caller-dependent. |
| D. Mnemosyne commits but upstream caller fails afterward | Cache and database may contain committed data; caller sees timeout/error | Committed | Retry can repeat whole-resource write; BEFE create is effectively downstream PUT/upsert. | **OBSERVED/INFERRED** duplicate processing possible; no idempotency protocol beyond logical ID. |
| E. Mnemosyne commits but Mneme lacks resulting state | Database contains authoritative row; cache may be absent/stale if cache/store failure occurs after downstream commit | Committed | BEFE may receive failure despite durable commit, or later read may miss/stale; no automatic refresh/read-back. | **INFERRED** divergence; retry can overwrite/repeat. |
| F. Two writers read same active resource and both update | Both unconditional `put` operations can succeed; last value remains | Each store PUT invokes no expected-version update. DB version increments sequentially if both commits occur; later whole resource wins | Both callers can receive success on different paths; stale update not rejected on BEFE. | **OBSERVED/test evidence** for Mneme; lost update risk. |
| G. Second writer uses stale representation | BEFE overwrites cache unconditionally; Pylai with valid numeric If-Match can fail at Mnemosyne | BEFE path has no expected version; Pylai path can return conflict/fail Pragma | Pylai conflict becomes failed Pragma; BEFE normally succeeds. | **OBSERVED** path divergence. |
| H. Runtime failure during sequence | May leave cache value, no value, or replicated value depending on failure point | May be unchanged or committed; exact point is **UNKNOWN** | No BEFE transaction spanning cache and DB; no automatic reconciliation. Pylai Pragma may remain accepted/in-progress/failed depending point. | **INFERRED** inconsistency and retry ambiguity. |
| I. Network failure between persistence integration and Mnemosyne | Cache/store completion is uncertain; cache may contain value if failure occurred after remote server commit | May be unchanged or committed but response lost | `HttpClient.sendAsync` completes exceptionally or non-2xx; store failure propagates. No idempotency/reconciliation routine found. | **OBSERVED/UNKNOWN**; duplicate processing possible on retry. |

For Pylai specifically, failure at Petasos publication is more serious than a normal exception path: `ChangeRequestSubmissionService.java:184-201` logs the exception and continues to return a successful `202`. Thus the caller can be told that a change request was accepted even though no durable broker acceptance is established by the inspected code.

**9. DELETE/lifecycle artefacts**

| Artefact | Classification | Evidence |
|---|---|---|
| BEFE `@DELETE` handlers | **A: governed physical resource deletion inconsistent with ADR-020**, at the API semantic level | `PractitionerResource.java:92-100` and equivalent resources expose DELETE. |
| BEFE `FhirCacheService.deleteResource` | **C: Mneme active-state removal/eviction** | `FhirCacheService.java:195-199` calls `RemoteCache.remove`. |
| Mneme `FhirRestCacheStore.delete` and `HapiFhirRestClient.deleteResource` | **A/E: legacy wired deletion path inconsistent with ADR-020** | `FhirRestCacheStore.java:124-132`; `HapiFhirRestClient.java:97-117` sends HTTP DELETE. |
| Mnemosyne HAPI provider `@Delete` methods | **A: governed FHIR delete API artefact** | `PractitionerResourceProvider.java:71-77` and equivalent providers. |
| Mnemosyne `FhirStorageService.deleteResource` | **A at API contract level; internally a soft-delete/lifecycle-like state change** | `FhirStorageService.java:288-304` sets `isDeleted=true`, saves entity, and logs “Soft-deleted”; it is not a domain lifecycle operation exposed as an UPDATE. |
| `FhirResourceEntity.is_deleted` | **Legacy/pre-ADR lifecycle marker** | `FhirResourceEntity.java:23-57, 111-117`; repository queries exclude deleted rows. |
| `repository.delete` / physical JPA deletion | **Not observed** in the clinical FHIR repository/service. | Repository is `JpaRepository`, but inspected service uses `save` and `deleted` flag; no direct `repository.delete` hit in clinical code. |
| `PersistenceOperationType.DELETE` and DELETE envelope constructors | **E: legacy/unwired architectural artefact** for this current clinical path | `PersistenceOperationType.java:20-27`; envelope validation/factory at `PersistenceOperationEnvelope.java:143-154, 472-480`. |
| Archive-table behavior | **UNKNOWN/not observed** in inspected production clinical code. | No archive implementation was located by targeted search. |

No Pylai FHIR Registry DELETE mapping was found; its controller exposes create/update request submission only.

**10. Write-path bypass analysis**

**OBSERVED production access paths:**

- BEFE concrete resources directly obtain a `RemoteCache` through `FhirCacheService` and write JSON (`iris/.../FhirCacheService.java:119-192`).
- Mneme’s `FhirRestCacheStore` directly calls Mnemosyne REST (`hestia/mneme-persistence/.../FhirRestCacheStore.java:100-132`).
- Mnemosyne HAPI providers directly call `FhirStorageService`; all provider CRUD is registered by `JpaRestfulServer`.
- Energeia Provider Registry Ergon directly calls `FhirStorageService` (`AbstractProviderRegistryChangeErgon.java:262-284`).
- `FhirStorageService` directly owns the JPA repository (`FhirStorageService.java:55-71, 189-205, 246-284`).

**OBSERVED:** These paths establish different semantics for the same resource class:

- BEFE: unconditional Hot Rod whole-resource PUT, followed by Mnemosyne HTTP PUT without expected version.
- Direct Mnemosyne FHIR API: JPA service update without expected version.
- Pylai Provider Registry: queued processing with optional numeric `If-Match`/expected-version comparison at Mnemosyne.

No production clinical caller was found directly using `replaceWithVersion`, `getWithMetadata`, or a common guarded write facade.

**11. Petasos/asynchronous path findings**

**OBSERVED:** The BEFE direct resource write path does not cross Petasos or Artemis. It is synchronous relative to the cache operation and configured persistence-store call.

**OBSERVED:** The Pylai Provider Registry path explicitly crosses Petasos/Artemis in `ChangeRequestSubmissionService.java:183-201`. The external `202` is generated after the `petasos.send` call returns, before authoritative resource persistence. The later persistence transition is performed by the Provider Registry Ergon (`AbstractProviderRegistryChangeErgon.java:262-303`).

**UNKNOWN:** The exact deployed Ponos/Camel consumer wiring from `harmonia.provider.registry.change.request` to every concrete Ergon cannot be fully established from the targeted production search; the queue constant is present in `ProviderRegistryConstants.java:33-39` and Ponos defaults at `energeia/ponos/.../QueueConfig.java:30-33`, while the concrete commit logic is directly visible in `AbstractProviderRegistryChangeErgon`.

**12. Audit, provenance, identity, and security observations**

**BEFE**

- Themis principal/security context is created by `ThemisClinicalAuthorizationFilter.java:144-216`.
- `FhirCacheService.saveResource` does not accept principal/context parameters and does not dispatch an audit event (`FhirCacheService.java:160-192`).
- Default FHIR security labels are applied to the resource (`FhirCacheService.java:172-174`), but no Provenance resource is generated.
- Provenance can be manually written as a normal cache resource (`ProvenanceResource.java:64-90`).
- AuditEvent is a separate read/query surface backed by Kleio `AuditService`; `AuditEventResource.java:41-176` contains no create/update method.

**Pylai/Ergon**

- Pylai captures principal, authorities, security context, requester, source system, correlation ID, operation, and optional `If-Match` (`ChangeRequestSubmissionService.java:143-176`; `ProviderRegistryChangePragma.java:131-178`).
- The Pragma carries the submitted FHIR resource and metadata through Petasos.
- Ergon performs independent persistence authorization (`AbstractProviderRegistryChangeErgon.java:218-259`) and records resulting FHIR version in Pragma metadata (`:285-300`).
- The inspected change submission and Ergon commit code does not invoke Kleio audit services or generate a FHIR Provenance resource.
- When Ergon calls `FhirStorageService.createResource/updateResource`, it does not pass the originating principal/authorities/context overload; the service therefore uses its fallback service principal/authorities in `authorizePersistence` (`FhirStorageService.java:86-130`).

**CONCLUSION:** Source identity and security context are preserved in the Pylai Pragma, but propagation into the actual FHIR entity/audit/provenance record is not observed. The BEFE context is used for request authorization but is not passed into cache persistence.

**13. Relevance of `docs/latex/chapters/appendix-decisions.tex`**

**OBSERVED:** This file is directly relevant as contextual architecture evidence, not as current implementation evidence.

- ADR-002 (`:16-18`) identifies Pylai as external protocol owner.
- ADR-003 (`:19-20`) distinguishes Mnemosyne durable state from Mneme cache state.
- ADR-005 (`:25-26`) assigns policy decisions to Themis.
- ADR-013 (`:49-50`) assigns immutable audit/provenance ownership to Kleio.
- ADR-014 (`:52-53`) assigns durable processing transitions to Petasos.
- ADR-018 (`:64-119`) explicitly states that cache write/replication is not durable application-state acceptance and distinguishes Mnemosyne authoritative state from Petasos work acceptance.

The appendix is therefore useful for interpreting the observed gaps, but it must not be used to infer that the current implementation already conforms to those decisions.

**14. Architectural gaps to carry into Step 08.02 (without designing them here)**

These are observations requiring a later deliberate protocol decision, not recommendations implemented in this step:

1. BEFE and Pylai provide different write and response semantics for overlapping Provider Registry resources.
2. BEFE cache JSON and Mnemosyne persisted JSON can receive independently assigned FHIR version values; no cache refresh from Mnemosyne response exists.
3. Hot Rod entry version and FHIR/JPA version are independent and are not compared.
4. Direct HAPI updates bypass the only observed expected-version check.
5. Pylai `202` is returned before clinical persistence, and publication failure is swallowed.
6. The cache-store HTTP client treats any 2xx as success and discards returned resource/version/ETag data.
7. There is no observed common write facade that centralizes Themis context, concurrency token, persistence result, provenance, and audit handling.
8. DELETE APIs and soft-delete state predate ADR-020 and remain wired in several production paths.
9. The exact behavior for failures after Mnemosyne commits but before the HTTP response reaches the cache store cannot be established statically.
10. The current code does not answer where the authoritative concurrency decision should occur; that is intentionally left for Step 08.02.

**15. Questions not answerable from current static evidence**

- Whether the deployed Infinispan runtime always waits for the `NonBlockingStore.write` completion before returning the Hot Rod `put` response under the exact production version/configuration.
- Whether every Mnemosyne HTTP 2xx observed by `HapiFhirRestClient` necessarily follows PostgreSQL commit under all servlet/transaction failure modes.
- The exact caller-visible HTTP status mapping for an exception escaping a BEFE JAX-RS resource.
- Whether a downstream commit can complete while the cache store's HTTP client reports failure for every network failure mode.
- Whether all production Ponos deployments wire the Provider Registry queue to all eight concrete Ergon handlers.
- Whether any deployment-specific interceptor creates audit/provenance evidence outside the inspected production Java path.
- Whether the old DELETE/persistence-envelope artifacts are used by an external integration not represented in this repository.

No code, configuration, or tests were modified, and no broad build was run. The only direct behavior evidence beyond static production tracing is the existing Mneme laboratory source cited above; it was not executed in this exploration.



ANALYSIS

**Task description**  
Using the mapped repository evidence, synthesize a structured current-state report for Task 08.01. Organize it into the requested deliverable sections, keep conclusions classified as OBSERVED / INFERRED / UNKNOWN, and include concise current-state CREATE and UPDATE sequence diagrams plus a failure-window matrix. Highlight the strongest evidence lines and any limitations where static code cannot prove runtime behavior.

**Constraints**  
Read-only synthesis only. Do not introduce design proposals for Step 08.02.



**Executive Summary**

Evaluating the clinical write path requires assessing how systemic design choices impact clinical safety, workflow efficiency, and data integrity. In Harmonia's current implementation, two distinct architectural write models exist with divergent guarantees and trade-offs:

1. **Synchronous Iris BEFE REST Tier (`iris/iris-befe`)**: Clinical JAX-RS resources (`/fhir/*`) parse payloads, set a default security label, and invoke an unconditional `RemoteCache.put` via `FhirCacheService`. Under the synchronous `FhirRestCacheStore` configuration, this triggers a downstream HTTP `PUT` to Mnemosyne (`hestia/mnemosyne-clinical`). BEFE issues a `201 Created` or `200 OK` once the cache write returns.
2. **Asynchronous Pylai Provider Registry Gateway (`pylai/pylai-fhir-registry`)**: The REST gateway authenticates via Themis, encapsulates the change inside a `ProviderRegistryChangePragma`, publishes a Petasos message, and immediately returns HTTP `202 Accepted`. Authoritative persistence and validation occur asynchronously via Energeia Erga and `FhirStorageService`.

**Key Architectural Findings:**
- **[OBSERVED]** The Iris BEFE write path is not a FHIR-authoritative create/update protocol. Both `POST` and `PUT` map to `RemoteCache.put`, which the persistence store converts to an HTTP `PUT` to Mnemosyne. On Mnemosyne, this triggers an upsert in `FhirStorageService.updateResource`.
- **[OBSERVED]** Concurrency in the BEFE path relies entirely on unconditional whole-resource overwrites (`RemoteCache.put`). Neither Hot Rod version checks (`replaceWithVersion`), HTTP `If-Match`/ETags, nor JPA `@Version` annotations are active in the BEFE-to-Mnemosyne path, creating a tangible risk of lost updates and silent data overwrites.
- **[OBSERVED]** Task 07 removed write-behind buffering from clinical caches (`hestia/mneme-cluster/src/main/resources/infinispan.xml`). `FhirRestCacheStore.write` synchronously waits on downstream HTTP completion and propagates non-2xx status as exceptions.
- **[INFERRED]** A successful `RemoteCache.put` strongly implies downstream HTTP 2xx completion and JPA transaction commit on Mnemosyne, but BEFE performs no post-commit read-back or version reconciliation.
- **[OBSERVED]** No automated FHIR `Provenance` generation or Kleio audit log dispatch is integrated into either the BEFE cache write or the Mnemosyne storage pipeline.

---

**Current CREATE Sequence**

**Sequence Diagram (Representative BEFE Clinical Resource Create)**

```text
Caller                Iris BEFE             FhirCacheService       Mneme (Infinispan)    FhirRestCacheStore    HapiFhirRestClient     Mnemosyne (HAPI)     FhirStorageService     PostgreSQL
  |                       |                        |                       |                     |                     |                      |                    |                   |
  |-- POST /fhir/Resource ->|                        |                       |                     |                     |                      |                    |                   |
  |   (JSON payload)      |-- authorize() [Themis] |                       |                     |                     |                      |                    |                   |
  |                       |-- saveResource() ----->|                       |                     |                     |                      |                    |                   |
  |                       |   (assign UUID, meta)  |-- RemoteCache.put() ->|                     |                     |                      |                    |                   |
  |                       |                        |                       |-- store.write() --->|                     |                      |                    |                   |
  |                       |                        |                       |                     |-- saveResourceJson->|                      |                    |                   |
  |                       |                        |                       |                     |   (HTTP PUT)        |-- HTTP PUT --------->|                    |                   |
  |                       |                        |                       |                     |                     |                      |-- @Update -------->|                   |
  |                       |                        |                       |                     |                     |                      |                    |-- findById() ---->|
  |                       |                        |                       |                     |                     |                      |                    |<-- null ----------|
  |                       |                        |                       |                     |                     |                      |                    |-- save(Entity) -->|
  |                       |                        |                       |                     |                     |                      |                    |<-- committed -----|
  |                       |                        |                       |                     |                     |<-- 200 OK -----------|<-- return Resource-|                   |
  |                       |                        |                       |                     |<-- CompletableFuture|                      |                    |                   |
  |                       |                        |                       |<-- Store complete --|                     |                      |                    |                   |
  |                       |                        |<-- put complete ------|                     |                     |                      |                    |                   |
  |<-- 201 Created -------|<-- return -------------|                       |                     |                     |                      |                    |                   |
```

**Detailed Stage Breakdown**

1. **JAX-RS Resource Handler** (`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/PractitionerResource.java:64-75`)
    - **Input:** Raw JSON payload; HTTP `POST`.
    - **Processing:** `ThemisClinicalAuthorizationFilter:118-216` authorizes request. Resource method validates non-blank payload, invokes HAPI parser, and calls `FhirCacheService.saveResource`.
    - **Output:** Returns HTTP `201 Created` with serialized resource.
2. **Cache Abstraction Service** (`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java:160-192`)
    - **Processing:** Generates UUID if `id` is blank; adds default security tags (`:172-174`); sets initial numeric `meta.versionId = "1"`; calls unconditional `remoteCache.put(id, json)`.
    - **Synchronous:** Blocks until Hot Rod client operation completes.
3. **Synchronous Persistence Store Adapter** (`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/hestia/mneme/persistence/FhirRestCacheStore.java:99-121`)
    - **Processing:** Non-blocking cache store interceptor invokes `HapiFhirRestClient.saveResourceJson(resourceType, id, json)`. Non-2xx responses complete the `CompletionStage` exceptionally.
4. **REST Client Transmission** (`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/hestia/mneme/persistence/HapiFhirRestClient.java:74-94`)
    - **Processing:** Transmits HTTP `PUT` to Mnemosyne REST endpoint (`/fhir/{resourceType}/{id}`).
5. **Mnemosyne FHIR Provider & Storage** (`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/PractitionerResourceProvider.java:62-69` and `.../service/FhirStorageService.java:224-285`)
    - **Processing:** Provider routes to `FhirStorageService.updateResource(id, resource, expectedVersion=null)`. Queries PostgreSQL via `FhirResourceRepository`. When not found, creates a new `FhirResourceEntity` (upsert), increments version to 1, saves to database within `@Transactional` boundary, and returns committed resource.

---

**Current UPDATE Sequence**

**Sequence Diagram (Representative BEFE Clinical Resource Update)**

```text
Caller                Iris BEFE             FhirCacheService       Mneme (Infinispan)    FhirRestCacheStore    HapiFhirRestClient     Mnemosyne (HAPI)     FhirStorageService     PostgreSQL
  |                       |                        |                       |                     |                     |                      |                    |                   |
  |-- PUT /fhir/Res/{id} ->|                       |                       |                     |                     |                      |                    |                   |
  |   (JSON payload)      |-- authorize() [Themis] |                       |                     |                     |                      |                    |                   |
  |                       |-- saveResource() ----->|                       |                     |                     |                      |                    |                   |
  |                       |   (increment meta.ver) |-- RemoteCache.put() ->|                     |                     |                      |                    |                   |
  |                       |                        |                       |-- store.write() --->|                     |                      |                    |                   |
  |                       |                        |                       |                     |-- saveResourceJson->|                      |                    |                   |
  |                       |                        |                       |                     |   (HTTP PUT)        |-- HTTP PUT --------->|                    |                   |
  |                       |                        |                       |                     |                     |                      |-- @Update -------->|                   |
  |                       |                        |                       |                     |                     |                      |                    |-- findById() ---->|
  |                       |                        |                       |                     |                     |                      |                    |<-- Entity (v1) ---|
  |                       |                        |                       |                     |                     |                      |                    |-- save(Entity v2)->|
  |                       |                        |                       |                     |                     |                      |                    |<-- committed -----|
  |                       |                        |                       |                     |                     |<-- 200 OK -----------|<-- return Resource-|                   |
  |                       |                        |                       |                     |<-- CompletableFuture|                      |                    |                   |
  |                       |                        |<-- put complete ------|<-- Store complete --|                     |                      |                    |                   |
  |<-- 200 OK ------------|<-- return -------------|                       |                     |                     |                      |                    |                   |
```

**Concurrency and Stale-Write Findings**
- **[OBSERVED]** BEFE `PractitionerResource.update` (`PractitionerResource.java:78-90`) parses incoming JSON, enforces path ID, and calls `saveResource`.
- **[OBSERVED]** `FhirCacheService.saveResource` (`FhirCacheService.java:176-192`) inspects submitted payload `meta.versionId`, parses it as an integer, increments it, and issues an unconditional `RemoteCache.put(id, json)`.
- **[OBSERVED]** Production BEFE update code **does not use** `replace()`, `replaceWithVersion()`, `putIfAbsent()`, `getWithMetadata()`, or HTTP `If-Match`.
- **[OBSERVED]** Direct Mnemosyne providers invoke `storageService.updateResource(id, resource)` with `expectedVersion = null`, bypassing database-level version validation.
- **[OBSERVED / TEST EVIDENCE]** The laboratory scenario in `hestia/mneme-cluster/src/test/.../MnemeConcurrencyAndVersionScenarioTest.java:78-177` confirms that concurrent unconditional `put()` calls result in lost updates.

---

**Current Success Boundary**

```text
Boundary Milestones in Write Path:
[1] Body Parsed -> [2] Cache Put Issued -> [3] Store Invoked -> [4] HTTP 2xx Received -> [5] JPA Committed -> [6] HTTP 200/201 Returned
                                                                                           ^
                                                                                           |
                                                             Durable Application State Fact
```

- **[OBSERVED]** External callers receive HTTP `200/201` only after `FhirCacheService.saveResource` successfully returns.
- **[OBSERVED]** With synchronous cache stores, `RemoteCache.put()` blocks until `FhirRestCacheStore.write()` completes.
- **[INFERRED]** Because Mnemosyne's storage method is annotated with Spring `@Transactional` (`FhirStorageService.java:224-235`), a downstream HTTP 200/201 response indicates that PostgreSQL transaction commit completed prior to the HTTP response return.
- **Durable Fact Established upon Success:**
    - **[OBSERVED]** Mneme contains the submitted JSON payload.
    - **[INFERRED]** Mnemosyne received HTTP `PUT` and committed the entity to PostgreSQL.
    - **[OBSERVED]** No proof or read-back exists reconciling the Mneme cached JSON with the final state/version assigned by Mnemosyne.

---

**Version Model**

| Dimension | Representation & Location | Owner | Concurrency Participation | Relationship to Other Versions |
|---|---|---|---|---|
| **FHIR `meta.versionId`** | String inside JSON payload (`FhirCacheService:181-188`, `FhirStorageService:269-279`) | BEFE (client side) / Mnemosyne (persisted side) | None in BEFE; used in Pylai `If-Match` parsing | **[OBSERVED]** Not synchronized with Hot Rod version. BEFE derives it from payload; Mnemosyne overwrites it from DB `versionId`. |
| **Infinispan Entry Version** | Hot Rod numeric metadata (`MetadataValue.getVersion()`) | Infinispan cluster engine | Available via `replaceWithVersion()`, but **unused** in production BEFE | **[OBSERVED]** Completely isolated; never mapped or converted to FHIR or JPA version. |
| **Mnemosyne Persisted Version** | `Long versionId` on `FhirResourceEntity:45-48` | `FhirStorageService` / PostgreSQL | Evaluated only if `expectedVersion != null` | **[OBSERVED]** Incremented per JPA update; not annotated with `@Version`. Copied to FHIR `meta.versionId` on Mnemosyne response. |
| **HTTP `If-Match` / ETag** | HTTP Header (`FhirRestGatewayController:283-312`) | External caller / Pylai | Evaluated in Pylai asynchronous change Ergon | **[OBSERVED]** Stripped of quotes/`W/` and matched against DB `versionId`. Ignored on BEFE path. |

---

**Concurrency Behaviour**

- **[OBSERVED]** Production BEFE writes use unconditional whole-resource string replacements (`remoteCache.put(id, json)`).
- **[OBSERVED]** If two clients concurrently read version 1 and submit updates, both cache writes succeed sequentially. The second `put()` silently overwrites changes made by the first.
- **[OBSERVED]** Mnemosyne processes each downstream HTTP `PUT` independently. If both succeed, `FhirResourceEntity.versionId` increments to 3, but the second writer's content completely overwrites the first writer's content.
- **[OBSERVED]** Pylai Provider Registry gate checks `If-Match` when supplied (`AbstractProviderRegistryChangeErgon.java:273-312`), throwing `PreconditionFailedException` on mismatch, but this protection is completely absent from the BEFE clinical path.

---

**Failure-Window Matrix**

| Failure Point | Mneme State | Mnemosyne State | Caller Result | Recovery / Risk Classification |
|---|---|---|---|---|
| **A. Mneme unavailable before write** | No entry written | No request sent | JAX-RS error propagated | **[OBSERVED]** Low risk. Clean failure; caller can retry safely. |
| **B. Mneme write reaches store, but Mnemosyne fails** | Operation fails; stage completes exceptionally | Unchanged or transaction rolled back | HTTP 500 / exception | **[OBSERVED]** Store propagates exception; no partial cache entry left in normal flow. |
| **C. Mnemosyne receives operation, JPA commit fails** | Exception propagated back to store | Rolled back | Caller receives HTTP failure | **[INFERRED]** Consistent rollback, but no explicit compensation routine. |
| **D. Mnemosyne commits, upstream caller fails/times out** | Contains updated JSON | Contains committed row | Timeout / network drop | **[INFERRED]** Durable state persisted; retry causes idempotent whole-resource overwrite. |
| **E. Mnemosyne commits, cache fails post-commit** | Absent or stale | Committed | Exception or stale read | **[INFERRED]** Inconsistency window between cache and database; no auto-reconciliation. |
| **F. Two writers concurrently update same resource** | Last writer's payload stored | Last writer's payload committed | Both callers receive HTTP 200 | **[OBSERVED]** High risk. Silent lost update; data loss of earlier concurrent write. |
| **G. Second writer operates from stale representation** | Overwrites with stale data | Overwrites with stale data | Caller receives HTTP 200 | **[OBSERVED]** Stale overwrite accepted due to lack of `If-Match` check. |
| **H. Application crash during write sequence** | Transient state in memory | Unchanged if pre-commit, committed if post-commit | Connection reset | **[UNKNOWN]** Exact recovery depends on crash timing; no distributed transaction coordinator. |
| **I. Network partition between Mneme and Mnemosyne** | Store fails exceptionally | Uncertain (committed or dropped) | Caller receives failure | **[OBSERVED / UNKNOWN]** Potential split-brain where DB committed but caller was notified of failure. |

---

**Revisit Step 07 Write-Behind Changes**

- **[OBSERVED]** `infinispan.xml` configures clinical caches in `SYNC` mode with `<persistence>` using `FhirRestCacheStore`. No `<async>` write-behind element is configured.
- **[OBSERVED]** `FhirRestCacheStore.write` returns a `CompletionStage` that completes exceptionally on non-2xx HTTP status from Mnemosyne.
- **Guarantees Established:**
    - `RemoteCache.put() success => Mnemosyne HTTP 2xx succeeded` **[OBSERVED]**.
    - `RemoteCache.put() success => Mnemosyne PostgreSQL transaction committed` **[INFERRED]** (backed by Spring `@Transactional` execution semantics).

---

**DELETE / Lifecycle Artefact Classification**

| Artefact | Location | Classification | Rationale |
|---|---|---|---|
| **BEFE `@DELETE` Handlers** | `iris/.../PractitionerResource.java:92-100` | **A: Inconsistent with ADR-020** | Exposes physical DELETE at REST API boundary. |
| **BEFE `deleteResource`** | `iris/.../FhirCacheService.java:195-199` | **C: Active-state removal** | Executes `remoteCache.remove(id)`. |
| **Cache Store `delete`** | `hestia/.../FhirRestCacheStore.java:124-132` | **A / E: Legacy wired path** | Dispatches HTTP `DELETE` to Mnemosyne REST client. |
| **HAPI Provider `@Delete`** | `hestia/.../PractitionerResourceProvider:71-77` | **A: Inconsistent with ADR-020** | Direct FHIR provider delete endpoint. |
| **Mnemosyne `deleteResource`** | `hestia/.../FhirStorageService.java:288-304` | **B / E: Soft-delete implementation** | Sets `isDeleted = true` and updates database entity. |
| **Entity `is_deleted` column** | `hestia/.../FhirResourceEntity.java:49-51` | **E: Legacy lifecycle marker** | Filtered by repository queries; pre-dates ADR-020. |
| **`PersistenceOperationType.DELETE`** | `calliope/.../PersistenceOperationType:25` | **E: Unwired architectural artefact** | Enum constant present; unused by clinical write path. |

---

**Write-Path Bypass Analysis**

- **[OBSERVED]** Multiple unaligned write paths exist for clinical resources:
    1. **BEFE JAX-RS Resource -> `FhirCacheService` -> Hot Rod `RemoteCache.put` -> Mnemosyne HTTP `PUT`** (unconditional upsert, no expected version).
    2. **Pylai Gateway -> Petasos Queue -> Energeia Ergon -> `FhirStorageService`** (asynchronous, enforces `If-Match` if supplied).
    3. **Direct Mnemosyne HAPI Provider -> `FhirStorageService`** (direct synchronous update, `expectedVersion = null`).
- **Impact:** Callers interacting through different entry points experience fundamentally conflicting concurrency semantics and validation guarantees.

---

**Petasos / Asynchronous Path Findings**

- **[OBSERVED]** The **BEFE clinical write path is entirely synchronous** with respect to Petasos and ActiveMQ Artemis. No Petasos messages are published or consumed during BEFE creates or updates.
- **[OBSERVED]** The **Pylai Provider Registry path is asynchronous** via Petasos (`ChangeRequestSubmissionService.java:183-201`). Work acceptance (`202 Accepted`) occurs upon Petasos publication prior to durable PostgreSQL commit.

---

**Audit and Provenance Findings**

- **Principal & Security Context:**
    - **BEFE:** `ThemisClinicalAuthorizationFilter` authenticates the request and attaches security context, but `FhirCacheService.saveResource` does not forward context to Mnemosyne or persistence layers.
    - **Pylai:** `ProviderRegistryChangePragma` retains principal and security context through Petasos, but Ergon execution does not propagate these into the database entity.
- **Provenance & Audit:**
    - **[OBSERVED]** No automatic FHIR `Provenance` resource is generated on create or update. `ProvenanceResource` exists only as an independent CRUD endpoint.
    - **[OBSERVED]** Kleio `AuditService` is not invoked during either BEFE or Mnemosyne write operations.

---

**Architectural Gaps Requiring Step 08.02 Decision**

1. **Write Protocol Sequence:** Deciding whether the authoritative protocol requires `Mneme coordination -> Mnemosyne commit` or `Mnemosyne commit -> Mneme cache refresh`.
2. **Authoritative Concurrency Anchor:** Establishing where optimistic locking / version checks must be enforced (Hot Rod `replaceWithVersion`, HTTP `If-Match`, or database-level `@Version`).
3. **Response & Cache Synchronization:** Eliminating divergent FHIR version generation between BEFE and Mnemosyne.
4. **Lifecycle Alignment (ADR-020):** Eliminating physical REST DELETE endpoints in favor of governed lifecycle UPDATE transitions.
5. **Audit & Provenance Integration:** Connecting Kleio audit dispatch and automatic Provenance creation to the authoritative commit path.

---

**Questions That Cannot Be Answered From Current Code**

- **[UNKNOWN]** Whether every network timeout between `FhirRestCacheStore` and Mnemosyne guarantees PostgreSQL transaction rollback under extreme connection reset conditions.
- **[UNKNOWN]** The exact HTTP error payload returned to external clients when an unhandled exception escapes a WildFly/BEFE JAX-RS resource method.
- **[UNKNOWN]** Whether third-party or external consumers depend on the legacy `is_deleted` column structure outside this repository.

Analysis is Ready

**Executive Summary**

Evaluating the clinical write path requires assessing how systemic design choices impact clinical safety, workflow efficiency, and data integrity. In Harmonia's current implementation, two distinct architectural write models exist with divergent guarantees and trade-offs:

1. **Synchronous Iris BEFE REST Tier (`iris/iris-befe`)**: Clinical JAX-RS resources (`/fhir/*`) parse payloads, set a default security label, and invoke an unconditional `RemoteCache.put` via `FhirCacheService`. Under the synchronous `FhirRestCacheStore` configuration, this triggers a downstream HTTP `PUT` to Mnemosyne (`hestia/mnemosyne-clinical`). BEFE issues a `201 Created` or `200 OK` once the cache write returns.
2. **Asynchronous Pylai Provider Registry Gateway (`pylai/pylai-fhir-registry`)**: The REST gateway authenticates via Themis, encapsulates the change inside a `ProviderRegistryChangePragma`, publishes a Petasos message, and immediately returns HTTP `202 Accepted`. Authoritative persistence and validation occur asynchronously via Energeia Erga and `FhirStorageService`.

**Key Architectural Findings:**
- **[OBSERVED]** The Iris BEFE write path is not a FHIR-authoritative create/update protocol. Both `POST` and `PUT` map to `RemoteCache.put`, which the persistence store converts to an HTTP `PUT` to Mnemosyne. On Mnemosyne, this triggers an upsert in `FhirStorageService.updateResource`.
- **[OBSERVED]** Concurrency in the BEFE path relies entirely on unconditional whole-resource overwrites (`RemoteCache.put`). Neither Hot Rod version checks (`replaceWithVersion`), HTTP `If-Match`/ETags, nor JPA `@Version` annotations are active in the BEFE-to-Mnemosyne path, creating a tangible risk of lost updates and silent data overwrites.
- **[OBSERVED]** Task 07 removed write-behind buffering from clinical caches (`hestia/mneme-cluster/src/main/resources/infinispan.xml`). `FhirRestCacheStore.write` synchronously waits on downstream HTTP completion and propagates non-2xx status as exceptions.
- **[INFERRED]** A successful `RemoteCache.put` strongly implies downstream HTTP 2xx completion and JPA transaction commit on Mnemosyne, but BEFE performs no post-commit read-back or version reconciliation.
- **[OBSERVED]** No automated FHIR `Provenance` generation or Kleio audit log dispatch is integrated into either the BEFE cache write or the Mnemosyne storage pipeline.

---

**Current CREATE Sequence**

**Sequence Diagram (Representative BEFE Clinical Resource Create)**

```text
Caller                Iris BEFE             FhirCacheService       Mneme (Infinispan)    FhirRestCacheStore    HapiFhirRestClient     Mnemosyne (HAPI)     FhirStorageService     PostgreSQL
  |                       |                        |                       |                     |                     |                      |                    |                   |
  |-- POST /fhir/Resource ->|                        |                       |                     |                     |                      |                    |                   |
  |   (JSON payload)      |-- authorize() [Themis] |                       |                     |                     |                      |                    |                   |
  |                       |-- saveResource() ----->|                       |                     |                     |                      |                    |                   |
  |                       |   (assign UUID, meta)  |-- RemoteCache.put() ->|                     |                     |                      |                    |                   |
  |                       |                        |                       |-- store.write() --->|                     |                      |                    |                   |
  |                       |                        |                       |                     |-- saveResourceJson->|                      |                    |                   |
  |                       |                        |                       |                     |   (HTTP PUT)        |-- HTTP PUT --------->|                    |                   |
  |                       |                        |                       |                     |                     |                      |-- @Update -------->|                   |
  |                       |                        |                       |                     |                     |                      |                    |-- findById() ---->|
  |                       |                        |                       |                     |                     |                      |                    |<-- null ----------|
  |                       |                        |                       |                     |                     |                      |                    |-- save(Entity) -->|
  |                       |                        |                       |                     |                     |                      |                    |<-- committed -----|
  |                       |                        |                       |                     |                     |<-- 200 OK -----------|<-- return Resource-|                   |
  |                       |                        |                       |                     |<-- CompletableFuture|                      |                    |                   |
  |                       |                        |                       |<-- Store complete --|                     |                      |                    |                   |
  |                       |                        |<-- put complete ------|                     |                     |                      |                    |                   |
  |<-- 201 Created -------|<-- return -------------|                       |                     |                     |                      |                    |                   |
```

**Detailed Stage Breakdown**

1. **JAX-RS Resource Handler** (`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/PractitionerResource.java:64-75`)
    - **Input:** Raw JSON payload; HTTP `POST`.
    - **Processing:** `ThemisClinicalAuthorizationFilter:118-216` authorizes request. Resource method validates non-blank payload, invokes HAPI parser, and calls `FhirCacheService.saveResource`.
    - **Output:** Returns HTTP `201 Created` with serialized resource.
2. **Cache Abstraction Service** (`iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java:160-192`)
    - **Processing:** Generates UUID if `id` is blank; adds default security tags (`:172-174`); sets initial numeric `meta.versionId = "1"`; calls unconditional `remoteCache.put(id, json)`.
    - **Synchronous:** Blocks until Hot Rod client operation completes.
3. **Synchronous Persistence Store Adapter** (`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/hestia/mneme/persistence/FhirRestCacheStore.java:99-121`)
    - **Processing:** Non-blocking cache store interceptor invokes `HapiFhirRestClient.saveResourceJson(resourceType, id, json)`. Non-2xx responses complete the `CompletionStage` exceptionally.
4. **REST Client Transmission** (`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/hestia/mneme/persistence/HapiFhirRestClient.java:74-94`)
    - **Processing:** Transmits HTTP `PUT` to Mnemosyne REST endpoint (`/fhir/{resourceType}/{id}`).
5. **Mnemosyne FHIR Provider & Storage** (`hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/PractitionerResourceProvider.java:62-69` and `.../service/FhirStorageService.java:224-285`)
    - **Processing:** Provider routes to `FhirStorageService.updateResource(id, resource, expectedVersion=null)`. Queries PostgreSQL via `FhirResourceRepository`. When not found, creates a new `FhirResourceEntity` (upsert), increments version to 1, saves to database within `@Transactional` boundary, and returns committed resource.

---

**Current UPDATE Sequence**

**Sequence Diagram (Representative BEFE Clinical Resource Update)**

```text
Caller                Iris BEFE             FhirCacheService       Mneme (Infinispan)    FhirRestCacheStore    HapiFhirRestClient     Mnemosyne (HAPI)     FhirStorageService     PostgreSQL
  |                       |                        |                       |                     |                     |                      |                    |                   |
  |-- PUT /fhir/Res/{id} ->|                       |                       |                     |                     |                      |                    |                   |
  |   (JSON payload)      |-- authorize() [Themis] |                       |                     |                     |                      |                    |                   |
  |                       |-- saveResource() ----->|                       |                     |                     |                      |                    |                   |
  |                       |   (increment meta.ver) |-- RemoteCache.put() ->|                     |                     |                      |                    |                   |
  |                       |                        |                       |-- store.write() --->|                     |                      |                    |                   |
  |                       |                        |                       |                     |-- saveResourceJson->|                      |                    |                   |
  |                       |                        |                       |                     |   (HTTP PUT)        |-- HTTP PUT --------->|                    |                   |
  |                       |                        |                       |                     |                     |                      |-- @Update -------->|                   |
  |                       |                        |                       |                     |                     |                      |                    |-- findById() ---->|
  |                       |                        |                       |                     |                     |                      |                    |<-- Entity (v1) ---|
  |                       |                        |                       |                     |                     |                      |                    |-- save(Entity v2)->|
  |                       |                        |                       |                     |                     |                      |                    |<-- committed -----|
  |                       |                        |                       |                     |                     |<-- 200 OK -----------|<-- return Resource-|                   |
  |                       |                        |                       |                     |<-- CompletableFuture|                      |                    |                   |
  |                       |                        |<-- put complete ------|<-- Store complete --|                     |                      |                    |                   |
  |<-- 200 OK ------------|<-- return -------------|                       |                     |                     |                      |                    |                   |
```

**Concurrency and Stale-Write Findings**
- **[OBSERVED]** BEFE `PractitionerResource.update` (`PractitionerResource.java:78-90`) parses incoming JSON, enforces path ID, and calls `saveResource`.
- **[OBSERVED]** `FhirCacheService.saveResource` (`FhirCacheService.java:176-192`) inspects submitted payload `meta.versionId`, parses it as an integer, increments it, and issues an unconditional `RemoteCache.put(id, json)`.
- **[OBSERVED]** Production BEFE update code **does not use** `replace()`, `replaceWithVersion()`, `putIfAbsent()`, `getWithMetadata()`, or HTTP `If-Match`.
- **[OBSERVED]** Direct Mnemosyne providers invoke `storageService.updateResource(id, resource)` with `expectedVersion = null`, bypassing database-level version validation.
- **[OBSERVED / TEST EVIDENCE]** The laboratory scenario in `hestia/mneme-cluster/src/test/.../MnemeConcurrencyAndVersionScenarioTest.java:78-177` confirms that concurrent unconditional `put()` calls result in lost updates.

---

**Current Success Boundary**

```text
Boundary Milestones in Write Path:
[1] Body Parsed -> [2] Cache Put Issued -> [3] Store Invoked -> [4] HTTP 2xx Received -> [5] JPA Committed -> [6] HTTP 200/201 Returned
                                                                                           ^
                                                                                           |
                                                             Durable Application State Fact
```

- **[OBSERVED]** External callers receive HTTP `200/201` only after `FhirCacheService.saveResource` successfully returns.
- **[OBSERVED]** With synchronous cache stores, `RemoteCache.put()` blocks until `FhirRestCacheStore.write()` completes.
- **[INFERRED]** Because Mnemosyne's storage method is annotated with Spring `@Transactional` (`FhirStorageService.java:224-235`), a downstream HTTP 200/201 response indicates that PostgreSQL transaction commit completed prior to the HTTP response return.
- **Durable Fact Established upon Success:**
    - **[OBSERVED]** Mneme contains the submitted JSON payload.
    - **[INFERRED]** Mnemosyne received HTTP `PUT` and committed the entity to PostgreSQL.
    - **[OBSERVED]** No proof or read-back exists reconciling the Mneme cached JSON with the final state/version assigned by Mnemosyne.

---

**Version Model**

| Dimension | Representation & Location | Owner | Concurrency Participation | Relationship to Other Versions |
|---|---|---|---|---|
| **FHIR `meta.versionId`** | String inside JSON payload (`FhirCacheService:181-188`, `FhirStorageService:269-279`) | BEFE (client side) / Mnemosyne (persisted side) | None in BEFE; used in Pylai `If-Match` parsing | **[OBSERVED]** Not synchronized with Hot Rod version. BEFE derives it from payload; Mnemosyne overwrites it from DB `versionId`. |
| **Infinispan Entry Version** | Hot Rod numeric metadata (`MetadataValue.getVersion()`) | Infinispan cluster engine | Available via `replaceWithVersion()`, but **unused** in production BEFE | **[OBSERVED]** Completely isolated; never mapped or converted to FHIR or JPA version. |
| **Mnemosyne Persisted Version** | `Long versionId` on `FhirResourceEntity:45-48` | `FhirStorageService` / PostgreSQL | Evaluated only if `expectedVersion != null` | **[OBSERVED]** Incremented per JPA update; not annotated with `@Version`. Copied to FHIR `meta.versionId` on Mnemosyne response. |
| **HTTP `If-Match` / ETag** | HTTP Header (`FhirRestGatewayController:283-312`) | External caller / Pylai | Evaluated in Pylai asynchronous change Ergon | **[OBSERVED]** Stripped of quotes/`W/` and matched against DB `versionId`. Ignored on BEFE path. |

---

**Concurrency Behaviour**

- **[OBSERVED]** Production BEFE writes use unconditional whole-resource string replacements (`remoteCache.put(id, json)`).
- **[OBSERVED]** If two clients concurrently read version 1 and submit updates, both cache writes succeed sequentially. The second `put()` silently overwrites changes made by the first.
- **[OBSERVED]** Mnemosyne processes each downstream HTTP `PUT` independently. If both succeed, `FhirResourceEntity.versionId` increments to 3, but the second writer's content completely overwrites the first writer's content.
- **[OBSERVED]** Pylai Provider Registry gate checks `If-Match` when supplied (`AbstractProviderRegistryChangeErgon.java:273-312`), throwing `PreconditionFailedException` on mismatch, but this protection is completely absent from the BEFE clinical path.

---

**Failure-Window Matrix**

| Failure Point | Mneme State | Mnemosyne State | Caller Result | Recovery / Risk Classification |
|---|---|---|---|---|
| **A. Mneme unavailable before write** | No entry written | No request sent | JAX-RS error propagated | **[OBSERVED]** Low risk. Clean failure; caller can retry safely. |
| **B. Mneme write reaches store, but Mnemosyne fails** | Operation fails; stage completes exceptionally | Unchanged or transaction rolled back | HTTP 500 / exception | **[OBSERVED]** Store propagates exception; no partial cache entry left in normal flow. |
| **C. Mnemosyne receives operation, JPA commit fails** | Exception propagated back to store | Rolled back | Caller receives HTTP failure | **[INFERRED]** Consistent rollback, but no explicit compensation routine. |
| **D. Mnemosyne commits, upstream caller fails/times out** | Contains updated JSON | Contains committed row | Timeout / network drop | **[INFERRED]** Durable state persisted; retry causes idempotent whole-resource overwrite. |
| **E. Mnemosyne commits, cache fails post-commit** | Absent or stale | Committed | Exception or stale read | **[INFERRED]** Inconsistency window between cache and database; no auto-reconciliation. |
| **F. Two writers concurrently update same resource** | Last writer's payload stored | Last writer's payload committed | Both callers receive HTTP 200 | **[OBSERVED]** High risk. Silent lost update; data loss of earlier concurrent write. |
| **G. Second writer operates from stale representation** | Overwrites with stale data | Overwrites with stale data | Caller receives HTTP 200 | **[OBSERVED]** Stale overwrite accepted due to lack of `If-Match` check. |
| **H. Application crash during write sequence** | Transient state in memory | Unchanged if pre-commit, committed if post-commit | Connection reset | **[UNKNOWN]** Exact recovery depends on crash timing; no distributed transaction coordinator. |
| **I. Network partition between Mneme and Mnemosyne** | Store fails exceptionally | Uncertain (committed or dropped) | Caller receives failure | **[OBSERVED / UNKNOWN]** Potential split-brain where DB committed but caller was notified of failure. |

---

**Revisit Step 07 Write-Behind Changes**

- **[OBSERVED]** `infinispan.xml` configures clinical caches in `SYNC` mode with `<persistence>` using `FhirRestCacheStore`. No `<async>` write-behind element is configured.
- **[OBSERVED]** `FhirRestCacheStore.write` returns a `CompletionStage` that completes exceptionally on non-2xx HTTP status from Mnemosyne.
- **Guarantees Established:**
    - `RemoteCache.put() success => Mnemosyne HTTP 2xx succeeded` **[OBSERVED]**.
    - `RemoteCache.put() success => Mnemosyne PostgreSQL transaction committed` **[INFERRED]** (backed by Spring `@Transactional` execution semantics).

---

**DELETE / Lifecycle Artefact Classification**

| Artefact | Location | Classification | Rationale |
|---|---|---|---|
| **BEFE `@DELETE` Handlers** | `iris/.../PractitionerResource.java:92-100` | **A: Inconsistent with ADR-020** | Exposes physical DELETE at REST API boundary. |
| **BEFE `deleteResource`** | `iris/.../FhirCacheService.java:195-199` | **C: Active-state removal** | Executes `remoteCache.remove(id)`. |
| **Cache Store `delete`** | `hestia/.../FhirRestCacheStore.java:124-132` | **A / E: Legacy wired path** | Dispatches HTTP `DELETE` to Mnemosyne REST client. |
| **HAPI Provider `@Delete`** | `hestia/.../PractitionerResourceProvider:71-77` | **A: Inconsistent with ADR-020** | Direct FHIR provider delete endpoint. |
| **Mnemosyne `deleteResource`** | `hestia/.../FhirStorageService.java:288-304` | **B / E: Soft-delete implementation** | Sets `isDeleted = true` and updates database entity. |
| **Entity `is_deleted` column** | `hestia/.../FhirResourceEntity.java:49-51` | **E: Legacy lifecycle marker** | Filtered by repository queries; pre-dates ADR-020. |
| **`PersistenceOperationType.DELETE`** | `calliope/.../PersistenceOperationType:25` | **E: Unwired architectural artefact** | Enum constant present; unused by clinical write path. |

---

**Write-Path Bypass Analysis**

- **[OBSERVED]** Multiple unaligned write paths exist for clinical resources:
    1. **BEFE JAX-RS Resource -> `FhirCacheService` -> Hot Rod `RemoteCache.put` -> Mnemosyne HTTP `PUT`** (unconditional upsert, no expected version).
    2. **Pylai Gateway -> Petasos Queue -> Energeia Ergon -> `FhirStorageService`** (asynchronous, enforces `If-Match` if supplied).
    3. **Direct Mnemosyne HAPI Provider -> `FhirStorageService`** (direct synchronous update, `expectedVersion = null`).
- **Impact:** Callers interacting through different entry points experience fundamentally conflicting concurrency semantics and validation guarantees.

---

**Petasos / Asynchronous Path Findings**

- **[OBSERVED]** The **BEFE clinical write path is entirely synchronous** with respect to Petasos and ActiveMQ Artemis. No Petasos messages are published or consumed during BEFE creates or updates.
- **[OBSERVED]** The **Pylai Provider Registry path is asynchronous** via Petasos (`ChangeRequestSubmissionService.java:183-201`). Work acceptance (`202 Accepted`) occurs upon Petasos publication prior to durable PostgreSQL commit.

---

**Audit and Provenance Findings**

- **Principal & Security Context:**
    - **BEFE:** `ThemisClinicalAuthorizationFilter` authenticates the request and attaches security context, but `FhirCacheService.saveResource` does not forward context to Mnemosyne or persistence layers.
    - **Pylai:** `ProviderRegistryChangePragma` retains principal and security context through Petasos, but Ergon execution does not propagate these into the database entity.
- **Provenance & Audit:**
    - **[OBSERVED]** No automatic FHIR `Provenance` resource is generated on create or update. `ProvenanceResource` exists only as an independent CRUD endpoint.
    - **[OBSERVED]** Kleio `AuditService` is not invoked during either BEFE or Mnemosyne write operations.

---

**Architectural Gaps Requiring Step 08.02 Decision**

1. **Write Protocol Sequence:** Deciding whether the authoritative protocol requires `Mneme coordination -> Mnemosyne commit` or `Mnemosyne commit -> Mneme cache refresh`.
2. **Authoritative Concurrency Anchor:** Establishing where optimistic locking / version checks must be enforced (Hot Rod `replaceWithVersion`, HTTP `If-Match`, or database-level `@Version`).
3. **Response & Cache Synchronization:** Eliminating divergent FHIR version generation between BEFE and Mnemosyne.
4. **Lifecycle Alignment (ADR-020):** Eliminating physical REST DELETE endpoints in favor of governed lifecycle UPDATE transitions.
5. **Audit & Provenance Integration:** Connecting Kleio audit dispatch and automatic Provenance creation to the authoritative commit path.

---

**Questions That Cannot Be Answered From Current Code**

- **[UNKNOWN]** Whether every network timeout between `FhirRestCacheStore` and Mnemosyne guarantees PostgreSQL transaction rollback under extreme connection reset conditions.
- **[UNKNOWN]** The exact HTTP error payload returned to external clients when an unhandled exception escapes a WildFly/BEFE JAX-RS resource method.
- **[UNKNOWN]** Whether third-party or external consumers depend on the legacy `is_deleted` column structure outside this repository.



**Summary**
- Produced the Task 08.01 current-state clinical write-path report from repository evidence only, covering BEFE, Pylai, Mneme, Mnemosyne, versioning, success boundaries, failure windows, delete/lifecycle artefacts, bypasses, and audit/provenance.
- Established that current production uses divergent write paths: a synchronous BEFE -> Mneme -> Mnemosyne HTTP PUT path with unconditional cache writes, and an asynchronous Pylai -> Petasos -> Ergon -> Mnemosyne path with optional If-Match handling.
  **Changes**
- Identified the primary production entry points and evidence lines, including BEFE resources (`iris/iris-befe/.../PractitionerResource.java:64-100`), authorization (`.../ThemisClinicalAuthorizationFilter.java:118-216`), cache write logic (`.../FhirCacheService.java:152-199`), Mneme persistence adapter (`hestia/mneme-persistence/.../FhirRestCacheStore.java:99-132`), REST client (`.../HapiFhirRestClient.java:74-117`), Mnemosyne providers (`hestia/mnemosyne-clinical/.../PractitionerResourceProvider.java:47-77`), and storage service (`.../FhirStorageService.java:224-304`).
- Confirmed Step 07 effects from configuration and code: relevant clinical caches are synchronous with no write-behind (`hestia/mneme-cluster/src/main/resources/infinispan.xml:56-71`), and downstream persistence failures are propagated by the cache store.
- Verified current concurrency/version behavior: BEFE clinical writes use unconditional `RemoteCache.put` with no `replaceWithVersion`, `getWithMetadata`, or `If-Match`; the only observed expected-version enforcement is on the asynchronous Pylai Provider Registry path (`pylai/pylai-fhir-registry/.../FhirRestGatewayController.java:280-324`, `energeia/erga/.../AbstractProviderRegistryChangeErgon.java:273-312`).
  **Verification**
- Validation was performed by static tracing of production code and targeted repository evidence only; no production code or tests were modified.
- Existing laboratory evidence for lost-update behavior was incorporated from `hestia/mneme-cluster/src/test/.../MnemeConcurrencyAndVersionScenarioTest.java:78-177, 267-359`.
  **Notes**
- The attached file content block was empty, but `docs/latex/chapters/appendix-decisions.tex` was treated as contextual ADR support; it informs interpretation but was not used as proof of current implementation behavior.

