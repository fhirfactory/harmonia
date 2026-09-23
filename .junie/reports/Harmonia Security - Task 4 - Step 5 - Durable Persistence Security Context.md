Brave Mode must remain enabled for the Goal Mode to work



You already have a plan in this session. Would you like to continue working on it, or start a new session to create a new plan?

Entering **Plan** Mode



Now I'm opening the task-04-step-05-persistence-security-context file to understand the planned approach for persisting the security context, so I can align the implementation with the intended design.

I verified the exact structure of Harmonia's service identities, authority enums, and architecture tests in Calliope, Themis, and Paradeigma. The investigation confirmed that `SERVICE:mneme` and `CLINICAL_DELETE` fit naturally into existing registries and that `ThemisAuthorizationDecision` provides immutable decision evidence without becoming a capability token. I am now updating the implementation plan to comprehensively capture the canonical persistence contract, fail-closed validation rules, and the boundary separating this contract definition from Task 08's runtime write path.**Requirements**

**Overview & Goals**  
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**  
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**  
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**  
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**  
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**  
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**  
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [ ] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [ ] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [ ] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [ ] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [ ] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [ ] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [ ] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [ ] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---



**Requirements**

**Overview & Goals**
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [ ] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [ ] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [ ] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [ ] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [ ] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [ ] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [ ] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [ ] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---

I will inspect the plan file to review the top-level steps and structure before beginning execution.

I will update the plan file to add the concrete delivery steps and mark Step 1 as in progress. Then I will implement the canonical persistence contract in Calliope.**Requirements**

**Overview & Goals**  
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**  
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**  
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**  
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**  
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**  
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**  
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [ ] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [ ] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [ ] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [ ] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [ ] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [ ] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [ ] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [ ] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---

**Delivery Steps**

*** Step 1: Implement Canonical Persistence Operation Contract in Calliope**  
Define `PersistenceOperationEnvelope<T>`, `PersistenceOperationType`, and `CLINICAL_DELETE` authority in Calliope with full serialization, immutability, context bridging, and fail-closed validations, along with comprehensive unit tests.

- Implement `PersistenceOperationType` enum (`CREATE`, `UPDATE`, `DELETE`) in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope` under `net.fhirfactory.harmonia.model.security`.
- Implement `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`:
    - Immutable fields: `operationId`, `operationType`, `resourceType`, `resourceId`, `expectedVersion`, `payload`, `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, `executingPrincipal`, `createdAt`, `attributes`.
    - Implements `Serializable` with Jackson annotations (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`).
    - Controlled executor transitions via `withExecutingPrincipal(ThemisPrincipal)` preserving all originating provenance and correlation/causation IDs.
    - Bidirectional bridging via `toSecurityContext()` and `fromSecurityContext(...)`.
    - Fail-closed builder and constructor validation preventing construction with missing mandatory fields, denied authorization decisions, or invalid payloads (CREATE/UPDATE requiring payload, DELETE requiring null payload).
    - Static factory helpers `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
- Add unit test `PersistenceOperationEnvelopeTest` in `calliope` validating contract immutability, builder validation, executor transitions, context bridging, and JSON round-tripping.
- Add unit test coverage for `HarmoniaAuthorityEnum.CLINICAL_DELETE`.

**Step 2: Register Canonical SERVICE:mneme Identity in Themis Core**  
Canonicalize `SERVICE:mneme` in `HarmoniaServiceIdentities` and assign its default least-privilege clinical authorities.

- Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/identities/HarmoniaServiceIdentities.java`.
- Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
- Add/update unit test assertions in `themis-core` (`HarmoniaSecurityConstantsTest`) verifying `ID_MNEME`, `PRINCIPAL_MNEME`, and authority resolution.

**Step 3: Architecture Governance & System-Wide Verification**  
Enforce architectural invariants, identity registration, and package layering in ArchUnit tests, and run test suites across Calliope, Themis, and Paradeigma.

- Update `SecurityEnforcementArchitectureTest` in `paradeigma-test` to assert `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and its required authority bindings.
- Assert package layering and that `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` without depending on JPA, Hibernate, or storage modules.
- Run test suites for `calliope`, `themis-core`, and `paradeigma-test` to ensure full verification and zero regressions.



I will create the PersistenceOperationType enum and HarmoniaAuthorityEnum updates in Calliope, followed by the PersistenceOperationEnvelope implementation.

I will now create the PersistenceOperationEnvelope class in Calliope with all required fields, builder, validation, and bridging methods.

I will now write unit tests in Calliope to verify the contract immutability, fail-closed validation, executor transition semantics, context bridging, authority mappings, and JSON round-tripping.

I will create PersistenceOperationEnvelopeTest in Calliope and run the Calliope test suite.

I will update PersistenceOperationEnvelopeTest to use the canonical ThemisDecisionReason and re-run the Calliope tests.

I will add Jackson annotations to ThemisAuthorizationDecision to ignore computed boolean helper methods during JSON serialization, then recompile themis-api and run the Calliope tests.

I will update the plan file to mark Step 1 as completed and Step 2 as in progress before starting Step 2.

**Requirements**

**Overview & Goals**  
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**  
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**  
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**  
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**  
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**  
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**  
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [ ] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [ ] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [ ] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [ ] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [ ] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [ ] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [ ] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [ ] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---

**Delivery Steps**

**✓ Step 1: Implement Canonical Persistence Operation Contract in Calliope**  
Define `PersistenceOperationEnvelope<T>`, `PersistenceOperationType`, and `CLINICAL_DELETE` authority in Calliope with full serialization, immutability, context bridging, and fail-closed validations, along with comprehensive unit tests.

- Implement `PersistenceOperationType` enum (`CREATE`, `UPDATE`, `DELETE`) in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope` under `net.fhirfactory.harmonia.model.security`.
- Implement `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`:
    - Immutable fields: `operationId`, `operationType`, `resourceType`, `resourceId`, `expectedVersion`, `payload`, `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, `executingPrincipal`, `createdAt`, `attributes`.
    - Implements `Serializable` with Jackson annotations (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`).
    - Controlled executor transitions via `withExecutingPrincipal(ThemisPrincipal)` preserving all originating provenance and correlation/causation IDs.
    - Bidirectional bridging via `toSecurityContext()` and `fromSecurityContext(...)`.
    - Fail-closed builder and constructor validation preventing construction with missing mandatory fields, denied authorization decisions, or invalid payloads (CREATE/UPDATE requiring payload, DELETE requiring null payload).
    - Static factory helpers `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
- Add unit test `PersistenceOperationEnvelopeTest` in `calliope` validating contract immutability, builder validation, executor transitions, context bridging, and JSON round-tripping.
- Add unit test coverage for `HarmoniaAuthorityEnum.CLINICAL_DELETE`.

*** Step 2: Register Canonical SERVICE:mneme Identity in Themis Core**  
Canonicalize `SERVICE:mneme` in `HarmoniaServiceIdentities` and assign its default least-privilege clinical authorities.

- Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/identities/HarmoniaServiceIdentities.java`.
- Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
- Add/update unit test assertions in `themis-core` (`HarmoniaSecurityConstantsTest`) verifying `ID_MNEME`, `PRINCIPAL_MNEME`, and authority resolution.

**Step 3: Architecture Governance & System-Wide Verification**  
Enforce architectural invariants, identity registration, and package layering in ArchUnit tests, and run test suites across Calliope, Themis, and Paradeigma.

- Update `SecurityEnforcementArchitectureTest` in `paradeigma-test` to assert `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and its required authority bindings.
- Assert package layering and that `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` without depending on JPA, Hibernate, or storage modules.
- Run test suites for `calliope`, `themis-core`, and `paradeigma-test` to ensure full verification and zero regressions.



I will inspect HarmoniaSecurityConstantsTest in themis-core before updating HarmoniaServiceIdentities.

I will add the canonical service:mneme identity and least-privilege authorities to HarmoniaServiceIdentities.

I will create HarmoniaServiceIdentitiesTest to verify registration and immutability of the mneme identity and authorities.

I will mark Step 2 complete and Step 3 in progress in the plan file before proceeding to architecture governance and system-wide verification.**Requirements**

**Overview & Goals**  
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**  
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**  
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**  
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**  
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**  
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**  
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [ ] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [ ] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [ ] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [ ] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [ ] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [ ] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [ ] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [ ] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---

**Delivery Steps**

**✓ Step 1: Implement Canonical Persistence Operation Contract in Calliope**  
Define `PersistenceOperationEnvelope<T>`, `PersistenceOperationType`, and `CLINICAL_DELETE` authority in Calliope with full serialization, immutability, context bridging, and fail-closed validations, along with comprehensive unit tests.

- Implement `PersistenceOperationType` enum (`CREATE`, `UPDATE`, `DELETE`) in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope` under `net.fhirfactory.harmonia.model.security`.
- Implement `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`:
    - Immutable fields: `operationId`, `operationType`, `resourceType`, `resourceId`, `expectedVersion`, `payload`, `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, `executingPrincipal`, `createdAt`, `attributes`.
    - Implements `Serializable` with Jackson annotations (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`).
    - Controlled executor transitions via `withExecutingPrincipal(ThemisPrincipal)` preserving all originating provenance and correlation/causation IDs.
    - Bidirectional bridging via `toSecurityContext()` and `fromSecurityContext(...)`.
    - Fail-closed builder and constructor validation preventing construction with missing mandatory fields, denied authorization decisions, or invalid payloads (CREATE/UPDATE requiring payload, DELETE requiring null payload).
    - Static factory helpers `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
- Add unit test `PersistenceOperationEnvelopeTest` in `calliope` validating contract immutability, builder validation, executor transitions, context bridging, and JSON round-tripping.
- Add unit test coverage for `HarmoniaAuthorityEnum.CLINICAL_DELETE`.

**✓ Step 2: Register Canonical SERVICE:mneme Identity in Themis Core**  
Canonicalize `SERVICE:mneme` in `HarmoniaServiceIdentities` and assign its default least-privilege clinical authorities.

- Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/identities/HarmoniaServiceIdentities.java`.
- Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
- Add/update unit test assertions in `themis-core` (`HarmoniaSecurityConstantsTest`) verifying `ID_MNEME`, `PRINCIPAL_MNEME`, and authority resolution.

*** Step 3: Architecture Governance & System-Wide Verification**  
Enforce architectural invariants, identity registration, and package layering in ArchUnit tests, and run test suites across Calliope, Themis, and Paradeigma.

- Update `SecurityEnforcementArchitectureTest` in `paradeigma-test` to assert `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and its required authority bindings.
- Assert package layering and that `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` without depending on JPA, Hibernate, or storage modules.
- Run test suites for `calliope`, `themis-core`, and `paradeigma-test` to ensure full verification and zero regressions.



I will add architecture governance tests for the SERVICE:mneme identity and persistence operation envelope contracts in SecurityEnforcementArchitectureTest.

I will update the assertion to use isEnum() predicate and run the architecture tests.

I will update the plan checklist and mark Step 3 as completed in the plan file.

**Requirements**

**Overview & Goals**  
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**  
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**  
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**  
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**  
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**  
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**  
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [x] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [x] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [x] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [x] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [x] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [x] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [x] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [x] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---

**Delivery Steps**

**✓ Step 1: Implement Canonical Persistence Operation Contract in Calliope**  
Define `PersistenceOperationEnvelope<T>`, `PersistenceOperationType`, and `CLINICAL_DELETE` authority in Calliope with full serialization, immutability, context bridging, and fail-closed validations, along with comprehensive unit tests.

- Implement `PersistenceOperationType` enum (`CREATE`, `UPDATE`, `DELETE`) in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope` under `net.fhirfactory.harmonia.model.security`.
- Implement `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`:
    - Immutable fields: `operationId`, `operationType`, `resourceType`, `resourceId`, `expectedVersion`, `payload`, `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, `executingPrincipal`, `createdAt`, `attributes`.
    - Implements `Serializable` with Jackson annotations (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`).
    - Controlled executor transitions via `withExecutingPrincipal(ThemisPrincipal)` preserving all originating provenance and correlation/causation IDs.
    - Bidirectional bridging via `toSecurityContext()` and `fromSecurityContext(...)`.
    - Fail-closed builder and constructor validation preventing construction with missing mandatory fields, denied authorization decisions, or invalid payloads (CREATE/UPDATE requiring payload, DELETE requiring null payload).
    - Static factory helpers `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
- Add unit test `PersistenceOperationEnvelopeTest` in `calliope` validating contract immutability, builder validation, executor transitions, context bridging, and JSON round-tripping.
- Add unit test coverage for `HarmoniaAuthorityEnum.CLINICAL_DELETE`.

**✓ Step 2: Register Canonical SERVICE:mneme Identity in Themis Core**  
Canonicalize `SERVICE:mneme` in `HarmoniaServiceIdentities` and assign its default least-privilege clinical authorities.

- Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/identities/HarmoniaServiceIdentities.java`.
- Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
- Add/update unit test assertions in `themis-core` (`HarmoniaSecurityConstantsTest`) verifying `ID_MNEME`, `PRINCIPAL_MNEME`, and authority resolution.

**✓ Step 3: Architecture Governance & System-Wide Verification**  
Enforce architectural invariants, identity registration, and package layering in ArchUnit tests, and run test suites across Calliope, Themis, and Paradeigma.

- Update `SecurityEnforcementArchitectureTest` in `paradeigma-test` to assert `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and its required authority bindings.
- Assert package layering and that `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` without depending on JPA, Hibernate, or storage modules.
- Run test suites for `calliope`, `themis-core`, and `paradeigma-test` to ensure full verification and zero regressions.
  Enforce architectural invariants, identity registration, and package layering in ArchUnit tests, and run test suites across Calliope, Themis, and Paradeigma.

- Update `SecurityEnforcementArchitectureTest` in `paradeigma-test` to assert `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and its required authority bindings.
- Assert package layering and that `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` without depending on JPA, Hibernate, or storage modules.
- Run test suites for `calliope`, `themis-core`, and `paradeigma-test` to ensure full verification and zero regressions.



**Requirements**

**Overview & Goals**  
Harmonia Security Task 04 Step 05 establishes the canonical, transport-independent persistence-operation and security provenance contract for the platform. This contract defines how originating provenance, authorization-decision evidence, and execution boundaries are durably represented for `CREATE`, `UPDATE`, and `DELETE` operations independently of transport mechanisms, cached state, or runtime write topologies.

To maximize systemic reliability, clinical data safety, and verifiable non-repudiation, the design enforces a strict separation between:
1. **Durable Originating Provenance**: Immutable historical evidence of who requested an operation, under what authority snapshot, in what security domain, with what root correlation ID, and at what timestamp.
2. **Dynamic Execution Authorization**: Real-time policy evaluation determining whether the currently executing service (e.g. `SERVICE:mneme`) is authorized to perform persistence execution at the persistence boundary.
3. **Authorization-Decision Evidence**: Immutable proof that Themis evaluated and approved the operation at request time, distinctly separated from raw authority snapshots so it can never be misused as a durable delegated capability token.

This step is bounded strictly to **defining and implementing the canonical persistence contract, identities, and architecture governance**. It does **not** alter the live clinical write architecture or retro-fit write-behind caching, which are explicitly reserved for Task 08.

**Scope**
- **In Scope**:
    - **New Canonical Persistence Contract Types** in `calliope` under package `net.fhirfactory.harmonia.model.persistence`:
        - `PersistenceOperationType`: Enum defining supported operations (`CREATE`, `UPDATE`, `DELETE`).
        - `PersistenceOperationEnvelope<T>`: Generic, immutable, transport-independent persistence command envelope with full Jackson/Java serialization support.
    - **Canonical Registration of `SERVICE:mneme`**:
        - Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
        - Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - **Authority Enum Extension**:
        - Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope`.
    - **Durable Provenance & Execution Semantics**:
        - First-class support for `DELETE` operations where no FHIR resource payload exists.
        - Explicit tracking of `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `ThemisAuthorizationDecision`.
        - Executor transition mechanism (`withExecutingPrincipal`) preserving root originating provenance across execution hops (`SERVICE:iris-befe` -> `SERVICE:mneme` -> `SERVICE:mnemosyne`).
        - Fail-closed builder and constructor validation preventing construction of malformed, unauthenticated, or unprovenanced envelopes.
    - **Verification & Governance**:
        - Unit tests in `calliope` and `themis-core` validating contract immutability, serialization round-tripping, executor derivation, and validation rules.
        - ArchUnit architecture tests in `paradeigma-test` enforcing identity registration, package layering, and contract invariants.

- **Out of Scope (Explicitly Deferred)**:
    - Altering `FhirCacheService` write architecture or `RemoteCache` value types (deferred to Task 08).
    - Modifying `FhirRestCacheStore`, `HapiFhirRestClient`, or Infinispan write-behind queue configurations (deferred to Task 08).
    - Altering Mnemosyne HTTP inbound authentication, providers, or `FhirResourceEntity` database schema (deferred to Task 08).
    - CORS, diagnostic logging sanitization, and PHI log masking (Task 05).
    - Immutable FHIR `AuditEvent` persistence generation (Task 06).
    - Elimination of BEFE `localFallbackCaches` (Task 07).
    - Authoritative Clinical writes replacing write-behind (Task 08).
    - Cache-aside point reads and authoritative search (Tasks 09 & 10).

**User Stories**
- **As a Compliance & Security Auditor**, I want all durable persistence operations to carry immutable, non-repudiable proof of the originating human or service requester, correlation lineage, and initial authorization decision, so that audit logs cannot be spoofed or decoupled during background persistence processing.
- **As a Platform Architect**, I want a transport-independent persistence-operation envelope that supports `CREATE`, `UPDATE`, and `DELETE` with equal fidelity, so that future authoritative clinical write pipelines (Task 08) can execute without relying on fragile cache-store callbacks or lossy transport headers.
- **As a Security Engineer**, I want the system to enforce that originating authority snapshots are historical provenance only, so that downstream worker services cannot elevate permissions or use an envelope as a permanent delegated capability token.

**Functional Requirements**
1. **First-Class Persistence Operation Types**:
    - The contract must support `CREATE`, `UPDATE`, and `DELETE` via `PersistenceOperationType`.
    - `DELETE` must carry complete originating provenance and correlation lineage without requiring a FHIR resource body.
    - `CREATE` and `UPDATE` must validate that a non-null, non-blank payload is present.
2. **Durable Originating Provenance**:
    - The envelope must capture `originatingPrincipal` (`ThemisPrincipal`), `originatingAuthorities` (`Set<ThemisAuthority>`), `securityDomain` (String), `correlationId` (String), `causationId` (String), and `requestedAt` (`Instant`).
    - Originating provenance must remain strictly immutable across all execution transitions.
3. **Authorization-Decision Evidence**:
    - The envelope must hold a mandatory `ThemisAuthorizationDecision` proving that Themis evaluated and approved the operation at request capture time.
    - The decision evidence must be structurally distinct from the originating authorities snapshot to prevent confusing authority presence with authorized permission.
4. **Controlled Executor Transitions**:
    - The envelope must provide `withExecutingPrincipal(ThemisPrincipal)` returning a new immutable copy with the updated executor while keeping all originating provenance, correlation IDs, and authorization decision intact.
    - Causation ID must not be modified when merely transitioning executing principals across service hops.
5. **Context Bridging**:
    - The envelope must provide bidirectional bridge methods (`toSecurityContext()` and `fromSecurityContext(...)`) allowing seamless translation between `ThemisSecurityContext` and the persistence envelope without exposing transport-specific attributes (e.g. `clientIp`, `tenantId`).
6. **Canonical Service Identity**:
    - `service:mneme` must be canonically registered in `HarmoniaServiceIdentities` as a `PrincipalType.SERVICE` principal with least-privilege clinical read/write/delete authorities.

**Non-Functional Requirements**
- **Fail-Closed by Design**: Any attempt to construct a `PersistenceOperationEnvelope` with missing mandatory fields (null principal, null decision, missing correlation ID, or null payload on CREATE/UPDATE) must immediately throw `IllegalArgumentException` or `NullPointerException`.
- **Serialization Independence**: Envelopes must implement `java.io.Serializable` and be fully annotated for Jackson JSON serialization (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`), allowing them to travel over Hot Rod, JMS, HTTP, or message queues.
- **Zero Production Disruption**: Implementation must introduce zero breaking changes to existing production paths in `iris-befe`, `mneme-persistence`, `mneme-cluster`, or `mnemosyne-clinical`.

---

**Technical Design**

**Current Implementation & Context Loss**  
In the current repository architecture:
1. `ThemisClinicalAuthorizationFilter` in `iris-befe` creates a valid `ThemisSecurityContext` with `requestingPrincipal`, `executingPrincipal = service:iris-befe`, correlation ID, and authority snapshot.
2. The context is held in CDI `@RequestScoped` `ThemisSecurityContextProvider`, but `FhirCacheService.saveResource()` and `deleteResource()` drop this context completely, writing only raw String keys and JSON payloads to Infinispan `RemoteCache`.
3. Infinispan `FhirRestCacheStore.write()` receives only a `MarshallableEntry<String, String>` (key and JSON), while `delete()` receives only the resource ID (`Object key`).
4. `HapiFhirRestClient` transmits unauthenticated HTTP PUT and DELETE requests with no security, principal, authority, correlation, or causation headers.
5. `FhirStorageService` in `mnemosyne-clinical` receives context-free HTTP calls and manufactures a synthetic `service:mnemosyne` identity with broad administrator authorities.

Step 05 defines the canonical contract that rectifies this architecture for Task 08 without prematurely breaking current write-behind operations.

**Key Decisions**

**1. Dedicated `PersistenceOperationEnvelope<T>` vs. Reusing `Pragma`**
- **Decision**: Create a new `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- **Rationale**: `Pragma` is strictly a workflow execution model coupled to Ponos/Ergon task orchestration (tracking Task status, checkpoints, input/output payload topics). Persistence is a lower-level, single-responsibility storage mutation that must not be coupled to the workflow engine or Apache Camel pipelines.

**2. Envelope-Carried Provenance vs. Embedding in FHIR Resources**
- **Decision**: Provenance is carried within `PersistenceOperationEnvelope<T>`, completely outside the clinical FHIR resource payload.
- **Rationale**: Embedding internal Harmonia security metadata in FHIR `meta.extension` or `meta.security` violates clinical domain purity (`meta.security` is for confidentiality classification). Crucially, clinical `DELETE` operations have no FHIR resource body, making resource-embedded provenance impossible for deletions.

**3. Flattened Provenance Snapshot vs. Embedding `ThemisSecurityContext`**
- **Decision**: Flatten the essential provenance fields into `PersistenceOperationEnvelope<T>` and provide explicit `toSecurityContext()` and `fromSecurityContext(...)` bridge methods.
- **Rationale**: `ThemisSecurityContext` carries ephemeral transport/runtime attributes (`clientIp`, `tenantId`, mutable request properties) that do not belong in durable persistence records. A flattened, strongly-typed model guarantees clean JSON schema serialization, prevents nested object bloat, and provides clear schema versioning while allowing instant reconstruction of `ThemisSecurityContext` when needed for policy evaluation.

**4. Authorization-Decision Evidence vs. Capability Tokens**
- **Decision**: Envelope carries `ThemisAuthorizationDecision` alongside `originatingAuthorities`.
- **Rationale**: Distinguishes "these authorities were held by the requester" from "Themis evaluated policy and authorized this specific action at request time". Originating authorities are historical provenance only. A downstream persistence service must never treat envelope contents as a permanent delegated capability token; it must evaluate dynamic authorization:
  `originating request authorized at capture time (proven by ThemisAuthorizationDecision)` **AND** `executing service authorized to persist at execution time`.

**5. Canonical `SERVICE:mneme` Identity**
- **Decision**: Register `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`).
- **Rationale**: Provides a stable, canonical service identity representing Harmonia's persistence and caching subsystem, decoupled from specific Infinispan write-behind implementation details and ready for direct use in Task 08.

**6. Executing Principal Transitions**
- **Decision**: Implement `withExecutingPrincipal(ThemisPrincipal)` on `PersistenceOperationEnvelope<T>`.
- **Rationale**: Enables the canonical execution sequence:
    - Ingress: `originatingPrincipal = HUMAN/SERVICE`, `executingPrincipal = SERVICE:iris-befe`
    - Persistence Dispatch: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mneme`
    - Storage Ingress: `originatingPrincipal = unchanged`, `executingPrincipal = SERVICE:mnemosyne`
      Transitioning executors derives a new immutable envelope while keeping originating provenance, authorization decision, correlation ID, and causation ID intact.

**Data Models & Contracts**

**1. `PersistenceOperationType` Enum**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
```java
public enum PersistenceOperationType {
    CREATE,
    UPDATE,
    DELETE
}
```

**2. `PersistenceOperationEnvelope<T>` Class**  
Package: `net.fhirfactory.harmonia.model.persistence` in `calliope`.
- **Type Parameters**: `<T>` (typically `String` representing FHIR R5 JSON; nullable for `DELETE`).
- **Annotations**: `@JsonInclude(JsonInclude.Include.NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Implements**: `Serializable`.
- **Fields**:
    - `operationId`: `String` (mandatory unique UUID for this persistence command).
    - `operationType`: `PersistenceOperationType` (mandatory `CREATE`, `UPDATE`, `DELETE`).
    - `resourceType`: `String` (mandatory FHIR resource type, e.g. "Person", "Consent").
    - `resourceId`: `String` (mandatory logical resource identifier).
    - `expectedVersion`: `Long` (optional optimistic locking version; null if unversioned).
    - `payload`: `T` (mandatory for `CREATE` and `UPDATE`; must be null for `DELETE`).
    - `originatingPrincipal`: `ThemisPrincipal` (mandatory initiating human or system).
    - `originatingAuthorities`: `Set<ThemisAuthority>` (immutable snapshot of requester authorities).
    - `securityDomain`: `String` (mandatory security domain, e.g. "CLINICAL").
    - `correlationId`: `String` (mandatory root tracing UUID).
    - `causationId`: `String` (mandatory immediate causal trigger ID).
    - `requestedAt`: `Instant` (mandatory timestamp of ingress request).
    - `authorizationDecision`: `ThemisAuthorizationDecision` (mandatory decision evidence).
    - `executingPrincipal`: `ThemisPrincipal` (mandatory active service executing the operation).
    - `createdAt`: `Instant` (envelope generation timestamp).
    - `attributes`: `Map<String, String>` (immutable operational/tracing metadata).

- **Key Methods**:
    - `withExecutingPrincipal(ThemisPrincipal newExecutor)`: returns a new immutable envelope copy with updated executor.
    - `toSecurityContext()`: reconstructs a valid `ThemisSecurityContext` for Themis evaluation.
    - `static <T> PersistenceOperationEnvelope<T> fromSecurityContext(...)`: creates an envelope initialized from an active `ThemisSecurityContext` and `ThemisAuthorizationDecision`.
    - Static factory helpers: `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
    - Fluent builder pattern with fail-closed validation on `.build()`.

**3. `HarmoniaAuthorityEnum` Extension**  
Package: `net.fhirfactory.harmonia.model.security` in `calliope`.
- Add: `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")`.

**4. `HarmoniaServiceIdentities` Extension**  
Package: `net.fhirfactory.harmonia.themis.core.identities` in `themis-core`.
- Add: `public static final String ID_MNEME = "service:mneme";`
- Add: `public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");`
- Update `SERVICE_AUTHORITIES`:
  ```java
  SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
          ThemisAuthority.of("clinical.read"),
          ThemisAuthority.of("clinical.create"),
          ThemisAuthority.of("clinical.update"),
          ThemisAuthority.of("clinical.delete"),
          ThemisAuthority.of("system.integration")
  ));
  ```

**Architecture Diagram**
```mermaid
graph TD
    subgraph IrisPresentation [Iris Presentation Tier]
        Ingress[OIDC Ingress Request] --> Filter[ThemisClinicalAuthorizationFilter]
        Filter -->|1. Evaluate Request| ThemisCheck[Themis Policy Engine]
        ThemisCheck -->|2. Allow + Decision Evidence| Decision[ThemisAuthorizationDecision]
        Filter --> ContextProvider[ThemisSecurityContextProvider]
        ContextProvider --> EnvelopeBuilder[Build PersistenceOperationEnvelope]
        Decision --> EnvelopeBuilder
    end

    subgraph PersistenceContract [Canonical Persistence Contract (Calliope)]
        EnvelopeBuilder --> Envelope[PersistenceOperationEnvelope]
        Envelope -.->|Fields: originatingPrincipal, authorities, decision, correlationId| Envelope
    end

    subgraph ServiceBoundaries [Service Execution Boundary Transitions]
        Envelope -->|withExecutingPrincipal| Hop1[executingPrincipal: SERVICE:iris-befe]
        Hop1 -->|withExecutingPrincipal| Hop2[executingPrincipal: SERVICE:mneme]
        Hop2 -->|withExecutingPrincipal| Hop3[executingPrincipal: SERVICE:mnemosyne]
    end

    subgraph DownstreamGovernance [Task 08 Downstream Verification]
        Hop3 --> DualGate[Themis Dual-Principal Enforcement]
        DualGate -->|Originator Approved AND Executor Approved| Postgres[(PostgreSQL Commit)]
    end
```

**Dual-Principal & Fail-Closed Invariants**
1. **Dual-Principal Formula**: An operation is permitted if and only if:
   `originating request authorized at request time (proven by ThemisAuthorizationDecision)`
   **AND**
   `current executor authorized to persist at execution time (e.g. SERVICE:mneme)`
2. **No Authority Unioning**: Originating authorities and executing service authorities must never be combined into a single set.
3. **Fail-Closed Envelope Validation**:
    - `operationId`, `operationType`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, and `executingPrincipal` must be non-null and non-blank.
    - If `operationType` is `CREATE` or `UPDATE`, `payload` must not be null/blank.
    - If `operationType` is `DELETE`, `payload` must be null.
    - If `authorizationDecision.isDenied()`, envelope creation is rejected.

**Explicit Confirmation of Unaltered Components**  
As required by the Step 05 architectural boundaries, the implementation will **NOT** alter any of the following:
1. `FhirCacheService` write architecture or method contracts in `iris-befe`.
2. `RemoteCache` key/value types or Hot Rod client configurations.
3. `FhirRestCacheStore` implementation in `mneme-persistence`.
4. `HapiFhirRestClient` HTTP client implementation in `mneme-persistence`.
5. Mnemosyne HTTP endpoint authentication or REST provider classes in `mnemosyne-clinical`.
6. `FhirResourceEntity` database entity schema, JPA mappings, or database tables.
7. Infinispan `infinispan.xml` cache configuration or write-behind queue settings.
8. `AuditEvent` generation or audit persistence behaviour (reserved for Task 06).
9. Live clinical write behaviour or synchronous write routing (reserved for Task 08).

**Deferred Integration Work for Task 08 (Authoritative Clinical Writes)**  
Task 08 will take the contract created in Step 05 and wire it into the production write path:
1. Refactor `FhirCacheService` to wrap incoming FHIR JSON into `PersistenceOperationEnvelope<String>`.
2. Implement authenticated service-to-service communication between Iris, Mneme, and Mnemosyne (mTLS or internal JWT service tokens).
3. Replace the unauthenticated HAPI REST client with an authenticated persistence client carrying `PersistenceOperationEnvelope`.
4. Update `FhirStorageService` in `mnemosyne-clinical` to unpack `PersistenceOperationEnvelope`, eliminate synthetic `service:mnemosyne` identity manufacture, and enforce fail-closed dual-principal authorization.
5. Persist durable provenance and tombstone metadata into PostgreSQL tables during commit.

---

**Testing**

**Validation Approach**  
Verification relies on focused unit tests in `calliope` and `themis-core`, accompanied by ArchUnit architecture governance tests in `paradeigma-test`. Tests run locally and in CI via Maven without requiring external network services or running databases.

**Key Scenarios Checklist**
- [x] **Contract Builder & Immutability**:
    - Verify `PersistenceOperationEnvelope` instantiation for `CREATE`, `UPDATE`, and `DELETE`.
    - Assert that `DELETE` envelopes successfully carry all originating provenance while holding a null payload.
    - Verify that `originatingAuthorities` and `attributes` collections are wrapped in unmodifiable sets/maps.
- [x] **Fail-Closed Validation**:
    - Assert `NullPointerException` / `IllegalArgumentException` when `operationId`, `resourceType`, `resourceId`, `originatingPrincipal`, `correlationId`, `authorizationDecision`, or `executingPrincipal` is missing.
    - Assert validation failure when `CREATE` or `UPDATE` has a null payload.
    - Assert validation failure when `DELETE` has a non-null payload.
    - Assert rejection if `authorizationDecision.isDenied()`.
- [x] **Executor Transitions & Provenance Preservation**:
    - Test `withExecutingPrincipal(ThemisPrincipal)`: assert that executing principal updates to `SERVICE:mneme`, while `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, and `authorizationDecision` remain identical.
- [x] **Bidirectional Context Bridging**:
    - Test `toSecurityContext()`: assert reconstructed `ThemisSecurityContext` contains identical principal, executor, domain, authorities, correlation, and causation IDs.
    - Test `fromSecurityContext(...)`: assert envelope is correctly populated from a `ThemisSecurityContext` and authorization decision.
- [x] **JSON Serialization Round-Trip**:
    - Verify Jackson serialization and deserialization of `PersistenceOperationEnvelope<String>` using `ObjectMapper` with `JavaTimeModule`.
    - Assert zero loss of timestamps (`Instant`), UUIDs, enums, authority sets, and payload strings across JSON round-tripping.
- [x] **Authority & Service Identity Registries**:
    - Verify `HarmoniaAuthorityEnum.CLINICAL_DELETE` maps to `clinical.delete` and resolves via `fromCode("clinical.delete")`.
    - Verify `HarmoniaServiceIdentities.ID_MNEME` and `PRINCIPAL_MNEME` exist and return the exact approved authority set: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
    - Verify that `HarmoniaServiceIdentities.getAuthorities(ID_MNEME)` is unmodifiable.
- [x] **ArchUnit Governance Invariants**:
    - Assert `SecurityEnforcementArchitectureTest` checks `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and authority bindings.
    - Assert `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` in `calliope` without depending on JPA, Hibernate, or storage modules.
- [x] **Regression Suite**:
    - Verify that all existing unit and architecture tests in `calliope`, `themis-api`, `themis-core`, and `paradeigma-test` remain 100% green.

---

**Delivery Steps**

**✓ Step 1: Implement Canonical Persistence Operation Contract in Calliope**  
Define `PersistenceOperationEnvelope<T>`, `PersistenceOperationType`, and `CLINICAL_DELETE` authority in Calliope with full serialization, immutability, context bridging, and fail-closed validations, along with comprehensive unit tests.

- Implement `PersistenceOperationType` enum (`CREATE`, `UPDATE`, `DELETE`) in `calliope` under `net.fhirfactory.harmonia.model.persistence`.
- Add `CLINICAL_DELETE("clinical.delete", "Delete Clinical resources")` to `HarmoniaAuthorityEnum` in `calliope` under `net.fhirfactory.harmonia.model.security`.
- Implement `PersistenceOperationEnvelope<T>` in `calliope` under `net.fhirfactory.harmonia.model.persistence`:
    - Immutable fields: `operationId`, `operationType`, `resourceType`, `resourceId`, `expectedVersion`, `payload`, `originatingPrincipal`, `originatingAuthorities`, `securityDomain`, `correlationId`, `causationId`, `requestedAt`, `authorizationDecision`, `executingPrincipal`, `createdAt`, `attributes`.
    - Implements `Serializable` with Jackson annotations (`@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties(ignoreUnknown = true)`).
    - Controlled executor transitions via `withExecutingPrincipal(ThemisPrincipal)` preserving all originating provenance and correlation/causation IDs.
    - Bidirectional bridging via `toSecurityContext()` and `fromSecurityContext(...)`.
    - Fail-closed builder and constructor validation preventing construction with missing mandatory fields, denied authorization decisions, or invalid payloads (CREATE/UPDATE requiring payload, DELETE requiring null payload).
    - Static factory helpers `ofCreate(...)`, `ofUpdate(...)`, `ofDelete(...)`.
- Add unit test `PersistenceOperationEnvelopeTest` in `calliope` validating contract immutability, builder validation, executor transitions, context bridging, and JSON round-tripping.
- Add unit test coverage for `HarmoniaAuthorityEnum.CLINICAL_DELETE`.

**✓ Step 2: Register Canonical SERVICE:mneme Identity in Themis Core**  
Canonicalize `SERVICE:mneme` in `HarmoniaServiceIdentities` and assign its default least-privilege clinical authorities.

- Define `ID_MNEME = "service:mneme"` and `PRINCIPAL_MNEME` in `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/identities/HarmoniaServiceIdentities.java`.
- Register least-privilege persistence authorities for `service:mneme` in `SERVICE_AUTHORITIES`: `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`.
- Add/update unit test assertions in `themis-core` (`HarmoniaSecurityConstantsTest`) verifying `ID_MNEME`, `PRINCIPAL_MNEME`, and authority resolution.

**✓ Step 3: Architecture Governance & System-Wide Verification**  
Enforce architectural invariants, identity registration, and package layering in ArchUnit tests, and run test suites across Calliope, Themis, and Paradeigma.

- Update `SecurityEnforcementArchitectureTest` in `paradeigma-test` to assert `HarmoniaServiceIdentities.PRINCIPAL_MNEME` and its required authority bindings.
- Assert package layering and that `PersistenceOperationEnvelope` resides strictly within `net.fhirfactory.harmonia.model.persistence` without depending on JPA, Hibernate, or storage modules.
- Run test suites for `calliope`, `themis-core`, and `paradeigma-test` to ensure full verification and zero regressions.



**Summary**
- Defined and implemented Harmonia's canonical, transport-independent persistence security and provenance contract (`PersistenceOperationEnvelope<T>` and `PersistenceOperationType`) without modifying existing live write paths.
- Canonicalized the `SERVICE:mneme` persistence identity in `HarmoniaServiceIdentities` with least-privilege clinical and integration authorities.

**Changes**
- Implemented `PersistenceOperationEnvelope<T>` and `PersistenceOperationType` in Calliope under `net.fhirfactory.harmonia.model.persistence`, enforcing immutable originating provenance, request-time authorization-decision evidence, fail-closed validation, context bridging, and executor transition derivation.
- Added `CLINICAL_DELETE` to `HarmoniaAuthorityEnum` in Calliope to complete granular clinical authorization codes.
- Registered `ID_MNEME` and `PRINCIPAL_MNEME` in `HarmoniaServiceIdentities` (`themis-core`) with least-privilege authorities (`clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, and `system.integration`).
- Added `@JsonIgnoreProperties(ignoreUnknown = true)` and `@JsonIgnore` on helper getters in `ThemisAuthorizationDecision` (`themis-api`) to ensure lossless Jackson JSON serialization across transport boundaries.
- Added unit tests in `calliope` (`PersistenceOperationEnvelopeTest`, `HarmoniaAuthorityEnumTest`) and `themis-core` (`HarmoniaServiceIdentitiesTest`), and updated architecture tests in `paradeigma-test` (`SecurityEnforcementArchitectureTest`).

**Verification**
- Executed unit and JSON round-trip tests in `calliope` (15/15 targeted passed, 125/125 module suite passed) and `themis-core` (98/98 passed).
- Executed the complete platform architecture test suite in `paradeigma-test` (31/31 passed) validating package layering, identity registration, and isolation invariants.

