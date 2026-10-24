# Data Governance Reconciliation Report: RAD-1.1.13 Architecture Continuum Baseline vs. Java Codebase Implementation

**Report Identifier:** `.junie/reports/2026-10-23-data-governance-reconciliation-report.md`  
**Date:** 2026-10-23  
**Architecture Baseline Release:** RAD-1.1.13 (`architecture-release.json`)  
**Git Branch / Commit:** `Data-Governence-Services` / `ff141340fdb0ab14ca46401bef5a7058138b7aad`  
**Working Tree State:** Tracked tree clean; pre-existing untracked `.junie/plans/data-governance-services-reconciliation.md` preserved without modification  
**Author / Engine:** Junie Autonomous Programmer (JetBrains)  
**Governance Procedure:** Harmonia Architecture / Code Governance Lifecycle (Code Round-Trip Review)  
**Audience:** Solution Architect (Mark Hunter), Harmonia Architecture Review Board (ARB), Core Engineering  

---

## 1. Executive Summary & Purpose

Pursuant to the Harmonia Architecture Governance Lifecycle, `AGENTS.md`, and the RAD-1.1.13 Architecture Continuum baseline, this report delivers an objective, evidence-based reconciliation between canonical Enterprise Architecture Continuum definitions and the existing Java implementation across Calliope, Mneme, Mnemosyne, and Pylai.

### Core Objectives
1. **Eliminate Architectural Drift:** Systematically map durable implementation constructs against Continuum architectural responsibilities without premature code refactoring or speculative redesigns.
2. **Safeguard Clinical Invariants:** Enforce non-negotiable patient safety boundaries, including Mneme/Mnemosyne State Separation (Invariant 8 / AX-05), Dual-Write Ingress Safety (Invariant 4 / REC-001), Destination State Tracking (Invariant 5 / REC-002), Default-Deny Security (Invariant 6 / AX-06 / AX-07), and Prohibition of Physical Deletion (ADR-020).
3. **Formulate Solution Architect Dispositions:** Deliver structured, bounded decision options with concrete evidence, trade-offs, and guardrails for unresolved architectural tensions.
4. **Propose Bounded First Build Increment:** Provide a prioritized advisory recommendation for the first solution build increment aligned with the Master Convergence Plan (`docs/implementation/harmonia-convergence-runtime-integration-plan.md`), contingent on explicit Solution Architect authorization.

---

## 2. Repository Baseline & Governance Conformance

### 2.1 Working Tree & Release Baseline
- **Git Branch:** `Data-Governence-Services`
- **Head Commit:** `ff141340fdb0ab14ca46401bef5a7058138b7aad` ("Complete 1.1.13 Archi workbench remediation")
- **Tracked Working Tree:** Clean (verified via `git status`). Untracked pre-existing file `.junie/plans/data-governance-services-reconciliation.md` preserved without alteration in compliance with NFR-1.
- **Architecture Release Version:** `1.1.13` (defined in `architecture-release.json:2`).

### 2.2 Architecture Review Praxis & Provenance Alignment
The analysis adheres to the common Review Praxis stages defined in `enterprise-architecture/governance/architecture-review-praxis.md`:
1. **Stage 1 (Establish the task and originating state, line 104):** Operative scope bounded to Data Governance Continuum elements and Java modules; authority loaded progressively from canonical Principle elements.
2. **Stage 2 (Prepare the candidate HCRL, line 130):** Structural discrepancies and authority gaps inventoried for subsequent HCRL iteration logging.
3. **Stage 3 (Obtain and preserve adjudication, line 147):** Structured disposition items prepared for Solution Architect determination; no self-authorized approvals.
4. **Stage 4 (Apply the authorised scope, line 161):** Strictly read-only analysis; zero mutations applied to production Java code, Continuum files, or Archi workbench models (NFR-1).
5. **Stage 5 (Report and verify the incorporated result, line 180):** Delivery of this report under `.junie/reports/`.
6. **Stage 6 (Confirm and complete the transition, line 245):** Awaiting formal Solution Architect sign-off and prospective HCRL iteration assignment.

*Governance & Provenance Notes:*
- The phrase "architecture code-roundtrip" describes the operational round-trip workflow spanning the six Praxis stages rather than a separately named standalone Praxis stage.
- HCRL 1.1.13-002 is closed; no prospective HCRL iteration (e.g. `1.1.14-001`) was pre-allocated for this round-trip pass.
- Attributable system timestamps for task issuance (`Task-Issued-At`) and completed report emission (`Report-Produced-At`) entering agent interaction are preserved as unresolved rather than inferred from filesystem metadata or repository commits.
- This report is delivered under `.junie/reports/` as a working deliverable; formal iteration evidence will be placed under `enterprise-architecture/design/evidence/<RAD-version>/<iteration>/` upon HCRL allocation.

---

## 3. Authoritative Architectural Foundations

### 3.1 Governing Architectural Axioms
All analysis is derived normatively from the canonical Principle elements linked via `enterprise-architecture/governance/architectural-axioms.md`:
- **AX-01 (Harmonia Is Health-Information Centric):** Health information integrity and clinical safety supersede operational convenience.
- **AX-02 (Standards at the Boundary; Harmonia Within the Boundary):** External standards govern the membrane; Harmonia owns internal semantics.
- **AX-03 (Native Standards Models Remain Native):** Standard interoperability models retain native fidelity without destructive translation.
- **AX-04 (Harmonia Owns the Semantics; Engines Provide the Machinery):** Platform components own domain semantics while underlying engines (Infinispan, HAPI FHIR, Artemis) supply operational execution.
- **AX-05 (Active State and Authoritative Durable State Are Distinct):** Active distributed state (Mneme) is reconstructable and non-authoritative; durable persistence (Mnemosyne) alone establishes authoritative truth.
- **AX-06 (Information Authority Is Explicit):** Authority over information mutation and lifecycle progression must be unambiguous.
- **AX-07 (Security Is Intrinsic to Managed Operations):** Default-deny policy evaluation must gate all data access through Themis without caller self-grants.
- **AX-08 (Evidence Records Meaning, Not Machinery):** Audit and evidence capture reflect domain semantics rather than ephemeral execution plumbing.
- **AX-09 (Transient Operational State Is Ephemeral by Default):** Non-authoritative intermediate operational state is discarded or reconstructed as needed.
- **AX-10 (Distribution, Load and Failure Are Normal Operating Conditions):** Architecture assumes distributed failure and partial partitions.
- **AX-11 (Managed Information Access Is Highly Available and Responsive):** Read access is optimized without compromising durable consistency.
- **AX-12 (Hide Plumbing, Not Information):** Infrastructure mechanics must not obscure clinical information or operational intent.
- **AX-13 (Harmonia Management Has an Explicit Boundary):** Interoperability gateways isolate internal operational semantics from external protocols.
- **AX-14 (Semantic Distinctions Are Preserved):** Distinct conceptual domains and version spaces must remain un-conflated.
- **AX-15 (Uncertainty Is Preserved Until Resolved):** System state under failure or partial completion must be recorded as uncertain rather than collapsed into false success or unrecorded failure.
- **AX-16 (Operational Activity & Entity State Progress Together):** Workflow execution and managed entity lifecycles advance synchronously.
- **AX-17 (Architectural Authority and Explicit Uncertainty):** Unratified claims, experimental implementations, and proposals must remain distinct from approved authority.
- **AX-18 (Integration and Coordination Do Not Confer Domain Authority):** Transport gateways and caching coordinators coordinate and integrate but do not possess domain ownership.

### 3.2 Canonical Architecture Continuum Elements & Candidate Standing
The Data Governance subsystem is represented across six canonical Application Component elements. Per AX-17 and AGENTS §1.2, all six elements hold the formal element-level semantic standing `standing.semantic: "Candidate — accepted Step-B baseline; final Solution Architecture unapproved"` (line 11 of each element file). At the source-assertion provenance level (source revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`), contributing assertions carry the standing `"Candidate — adjudicated Domain05 Step-B baseline; final Solution Architecture unapproved"`. Element semantic standing and source contribution standing are distinct governance attributes, and neither constitutes universal approval. Individual assertions represent candidate definitions subject to Solution Architect adjudication (e.g., Calliope-DataSets assertion **F25** carries the explicit qualifier `"Proposed"`, indicating a proposed capability rather than an approved runtime service):

| Component Name | Canonical UUIDv4 | Element Standing (`standing.semantic`, line 11) & Source Revision (`aadcd552...`) | Key Candidate Continuum Assertions & References |
| :--- | :--- | :--- | :--- |
| **Mneme** | `1af8a56358f7429fabfb57be66542a99` | Element: `Candidate — accepted Step-B baseline; final Solution Architecture unapproved`<br>Source: `Candidate — adjudicated Domain05 Step-B baseline` | **F16** (`:2501`): Candidate four principal exposed services (Context Load, Context Save, Adhoc Resource Access, Adhoc Search; "No fifth service").<br>**F17** (`:2507`): Supporting authoritative progression and guarded convergence; "physical DELETE not lifecycle".<br>**F18** (`:2513`): Adhoc Resource Access / Adhoc Search for bounded runtime needs.<br>**D04** (`:2519`) & **M05** (`:2525`): C11 point carriers are supporting contracts constrained by four-service surface; mapping deferred to CDG-31/45/47. |
| **Mnemosyne** | `7985e647bf9e4ca3ba70faf9e3e4ae02` | Element: `Candidate — accepted Step-B baseline; final Solution Architecture unapproved`<br>Source: `Candidate — adjudicated Domain05 Step-B baseline` | **F19** (`:63`): Durable Clinical / C&G / O&A preservation across three semantic persistence domains.<br>**F42** (`:71`): Persistence oversight and authoritative outcome reporting.<br>**F43** (`:95`): Search Management as first-class Mnemosyne responsibility.<br>**F44** (`:103`) / **D30** (`:87`): Research Metadata Management.<br>**D29** (`:79`): Harmonia-level persistence search metadata.<br>**M16** (`:111`): Application management vs durable persistence collaboration. |
| **Calliope** (Parent) | `466ad8f75a954c0581b5bba090443849` | Element: `Candidate — accepted Step-B baseline; final Solution Architecture unapproved`<br>Source: `Candidate — adjudicated Domain05 Step-B baseline` | **F02** (`:79`): Canonical conversion support and interface-specific semantic processing.<br>**F46** (`:87`): Shared Security and Assurance Nomenclature.<br>**D07** (`:95`): Topic metadata and Filter definitions.<br>**D16** (`:103`): Governed Models / Metadata Types / Criteria / Topic / Filter definitions.<br>**M06** (`:111`): Governed definitions and runtime DataSet collaboration. |
| **Calliope-Models** | `307b3440a507403f8bc214d290bffb6a` | Element: `Candidate — accepted Step-B baseline; final Solution Architecture unapproved`<br>Source: `Candidate — adjudicated Domain05 Step-B baseline` | **F01** (`:47, :138`): Foundational design/build-time library (Models, Metadata Types, Criteria, Topics, Filters, Core Element Libraries). Pure build dependency; zero runtime persistence or operational execution ownership. |
| **Calliope-DataSets** | `53bf6815c7a54ed0abde9b1aad311843` | Element: `Candidate — accepted Step-B baseline; final Solution Architecture unapproved`<br>Source: `Candidate — adjudicated Domain05 Step-B baseline` | **F45** (`:47, :139`): Candidate runtime governed DataSet management and authoritative ontology review (ValueSets, CodeSets, Structured Metadata). Owns content, not persistence machinery; accesses via Mneme/Mnemosyne.<br>**F25** (`:55, :145`): **Proposed** — clinical terminology lookup, ValueSet expansion/validation, selective authoring (proposed capability, not approved runtime service). |
| **Pylai** | `e3e4147fea3640198dc251ed41ee854d` | Element: `Candidate — accepted Step-B baseline; final Solution Architecture unapproved`<br>Source: `Candidate — adjudicated Domain05 Step-B baseline` | **F21** (`:55`): External interoperability membrane across messaging, RMI, APIs, files; AX-13 fail-closed non-destructive publication.<br>**F47** (`:63`): Interaction progression and logical Praxis boundary handover.<br>**D09** (`:71`): Clinical exchange/content (Patient, Encounter, Location, ServiceRequest, Observation, DiagnosticReport, Communication, Consent). |

---

## 4. Java Implementation Inventory & Structural Mapping

### 4.1 Subproject Survey & Component Matrix

```
+----------------------------------------------------------------------------------------------------+
|                                    PYLAI INTEROPERABILITY MEMBRANE                                 |
|  pylai-mllp-in: IncomingAdtMessageProcessor (Ingress Gateway)                                      |
|  pylai-mllp-out: OutboundStateLifecycleManager (Egress Gateway)                                    |
|  pylai-mllp-base: DefaultCommunicationService (Infinispan Cache Bridge)                           |
|  pylai-fhir-registry: FhirRestGatewayController, PylaiFhirPublicationProjector [Library Jar]       |
+----------------------------------------------------------------------------------------------------+
                                      | Governed Operations (C11)
                                      v
+----------------------------------------------------------------------------------------------------+
|                                CALLIOPE PURE DOMAIN & CARRIERS (C11)                               |
|  net.fhirfactory.harmonia.model.governedwrite.*                                                    |
|  - GovernedReader, GovernedWriter, GovernedAccess, GovernedRead                                    |
|  - ResourceKey, ActiveStateToken, AuthoritativeVersion, WriteResult, ActiveStateConflict           |
+----------------------------------------------------------------------------------------------------+
              |                                                      |
              v                                                      v
+------------------------------------------+       +-------------------------------------------------+
|          MNEME ACTIVE STATE TIER         |       |         MNEMOSYNE AUTHORITATIVE TIER            |
|  hestia/mneme-cluster:                   |       |  hestia/mnemosyne-api:                          |
|  - access/DefaultGovernedReader          |       |  - AuthoritativePersistencePort                 |
|  - access/DefaultGovernedWriter          |       |  - AuthoritativePersistenceResult               |
|  - coordination/HotRodActiveStateCoord.  |       |  hestia/mnemosyne-clinical:                     |
|  - convergence/HotRodMnemeConvergence    |       |  - AuthoritativePersistenceService (@Primary)   |
|  - convergence/MnemeCachedResource       |       |  - AuthoritativeFhirResourceController (mTLS)   |
|  hestia/mneme-persistence:               |       |  - HapiJpaAuthoritativePersistenceAdapter (Exp) |
|  - client/MnemeAuthoritativeHttpClient   |       |  - FhirStorageService (In-Process Legacy)       |
|  - store/FhirRestCacheStore (SPI Store)  |       |                                                 |
+------------------------------------------+       +-------------------------------------------------+
```

### 4.2 Detailed Component Inventory & Verified Locations

| Subsystem / Module | Java Class / Interface | Physical Location (Verified Path & Lines) | Continuum Role & Implementation Responsibility |
| :--- | :--- | :--- | :--- |
| **Calliope** (`calliope`) | `GovernedReader` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedReader.java:21–41` | Pure interface declaring single-resource read: `<T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext)` (`:40`). |
| **Calliope** (`calliope`) | `GovernedWriter` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedWriter.java:23–51` | Pure interface declaring `<T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext)` (`:39`) and `<T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)` (`:50`). |
| **Calliope** (`calliope`) | `GovernedAccess` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedAccess.java:21–28` | Composite interface combining `GovernedReader` and `GovernedWriter`. |
| **Calliope** (`calliope`) | `GovernedRead` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedRead.java:24–54` | Immutable carrier record binding resource payload `T`, `ResourceKey`, `AuthoritativeVersion`, and `ActiveStateToken`. |
| **Calliope** (`calliope`) | `ResourceKey` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ResourceKey.java:24–54` | Immutable value record representing FHIR resource type and logical identifier (`toQualifiedPath()`). |
| **Calliope** (`calliope`) | `AuthoritativeVersion` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/AuthoritativeVersion.java:26–61` | Opaque String record representing Mnemosyne durable version. `longValue()` falls back to `0L` on non-numeric parse (`:45–51`). |
| **Calliope** (`calliope`) | `ActiveStateToken` | `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateToken.java:22–65` | Opaque wrapper around a single `long` version number, readable only through `ActiveStateTokenBridge`. |
| **Mneme** (`mneme-cluster`) | `DefaultGovernedReader` | `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/access/DefaultGovernedReader.java:35–200` | Implements `GovernedReader`: validates boundary, evaluates Themis security, reads authoritatively from Mnemosyne via `AuthoritativePersistencePort.read(key)` (`:124`), converges local cache (`:167`), and observes `ActiveStateToken` (`:181`). |
| **Mneme** (`mneme-cluster`) | `DefaultGovernedWriter` | `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/access/DefaultGovernedWriter.java:38–262` | Implements `GovernedWriter`: validates boundary and security, consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume(...)` (`:206`), and persists through `AuthoritativePersistencePort` (`:120, 223`). |
| **Mneme** (`mneme-cluster`) | `HotRodActiveStateCoordinator` | `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/coordination/HotRodActiveStateCoordinator.java:41–202` | Implements hegemon observation (`:67–87`), CAS-based consumption (`:90–111`), and hegemon acquisition/confirmation (`:143–200`). |
| **Mneme** (`mneme-cluster`) | `HotRodMnemeConvergence` | `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java:42–202` | Applies post-commit authoritative version convergence into local cache; enforces "never overwrite newer cache state" (`:158–170`). |
| **Mneme** (`mneme-cluster`) | `MnemeCachedResource` | `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java:31–143` | Wraps cached FHIR resource JSON and embeds `AuthoritativeVersion` as private extension (`AUTHORITATIVE_VERSION_EXT_URL`, `:43, 69`). |
| **Mneme** (`mneme-persistence`) | `MnemeAuthoritativeHttpClient` | `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/MnemeAuthoritativeHttpClient.java:51–437` | REST client connecting to `/api/authoritative/fhir/*` over mTLS; maps HTTP 404 to `AuthoritativePersistenceResult.Absent` (`:160–162`). |
| **Mneme** (`mneme-persistence`) | `FhirRestCacheStore` | `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java:43–239` | Custom Infinispan SPI `NonBlockingStore` (`:44`) configured for synchronous write-through across 14 caches in `hestia/mneme-cluster/src/main/resources/infinispan.xml:59–243`. |
| **Mnemosyne** (`mnemosyne-api`) | `AuthoritativePersistencePort` | `hestia/mnemosyne-api/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java:25–64` | Contract declaring atomic `read`, `create` (If-None-Match), and `update` (If-Match) persistence operations. |
| **Mnemosyne** (`mnemosyne-clinical`) | `AuthoritativePersistenceService` | `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java:55–316` | `@Service @Primary` implementation of `AuthoritativePersistencePort` backed by Spring Data `FhirResourceRepository` and `TransactionTemplate`. |
| **Mnemosyne** (`mnemosyne-clinical`) | `AuthoritativeFhirResourceController` | `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/controller/AuthoritativeFhirResourceController.java:55–366` | Spring REST controller exposing `/api/authoritative/fhir/{type}/{id}` with mTLS, ETag precondition validation, and Themis authorization. Maps `Absent` to 404 and `NotCommitted` to 500 (`:123–133`). |
| **Mnemosyne** (`mnemosyne-clinical`) | `HapiJpaAuthoritativePersistenceAdapter` | `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/HapiJpaAuthoritativePersistenceAdapter.java:50–380` | Adapter delegating to HAPI FHIR JPA `IFhirResourceDao`. Marked `"Experimental"` (`:51`). Returns `Absent` on null/not-found (`:114–116`, `:125–127`), `NotCommitted` on deleted (`:128–130`). |
| **Mnemosyne** (`mnemosyne-clinical`) | `FhirStorageService` | `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java:45–636` | In-process persistence service handling legacy `/fhir` routes; implements generic soft-delete (`deleted = true`, `:288–301`) and self-grant fallback (`:86–100`). |
| **Pylai** (`pylai-mllp-in`) | `IncomingAdtMessageProcessor` | `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/hl7/IncomingAdtMessageProcessor.java:260–413` | Inbound MLLP pipeline transforming HL7 ADT to `Communication` and `Task`, saving to cache, and emitting Petasos `ErgonEvent`. Generates `AA` ACK at `:314`; fallback `AE` NACK catch block at `:324–327`. |
| **Pylai** (`pylai-mllp-base`) | `DefaultCommunicationService` | `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultCommunicationService.java:38–284` | Cache bridge managing `Communication` entries. Always writes local `communicationStore` first (`:90`); `getRemoteCache()` returns null on error (`:59–67`); swallows remote put exception with `log.warn` (`:92–101`). |
| **Pylai** (`pylai-mllp-out`) | `OutboundStateLifecycleManager` | `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/lifecycle/OutboundStateLifecycleManager.java:95–226` | Manages outbound lifecycle across 5 stages with catch blocks swallowing exceptions at lines 111–113, 144–146, 161–163, 173–175, 187–189. |
| **Pylai** (`pylai-fhir-registry`) | `FhirRestGatewayController` | `pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java:50–368` | Presentation controller directly calling `FhirStorageService` in-process (`:165, 203, 257`); maps `meta.versionId` to ETag (`:209–212`). Undeployed library jar in compose. |
| **Pylai** (`pylai-fhir-registry`) | `PylaiFhirPublicationProjector` | `pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/publication/PylaiFhirPublicationProjector.java:40–205` | Sanitizes FHIR payloads prior to egress using `FhirPublicationPolicy`, stripping internal extensions (`AUTHORITATIVE_VERSION_EXT_URL`). |

---

## 5. Semantic Taxonomy & Evolutionary Mapping (FR-3, FR-4)

### 5.1 Comprehensive Component Taxonomy
In accordance with FR-3 and FR-4, discovered Java components and architectural concepts are classified into seven structural categories:

```
+----------------------------------------------------------------------------------------------------+
| 1. DIRECT ALIGNMENT                                                                                |
|    - AuthoritativePersistencePort (hestia/mnemosyne-api): Directly maps to Mnemosyne durable port. |
|    - HotRodActiveStateCoordinator (hestia/mneme-cluster): Implements hegemon observation, CAS      |
|      consumption, and acquisition coordination.                                                    |
|    - HotRodMnemeConvergence (hestia/mneme-cluster): Implements post-commit cache convergence.       |
|    - PylaiFhirPublicationProjector (pylai-fhir-registry): Implements AX-13 non-destructive egress. |
+----------------------------------------------------------------------------------------------------+
| 2. SEMANTIC ALIGNMENT UNDER EVOLVED NAMING                                                         |
|    - GovernedReader.read() (calliope): Realizes candidate single-resource Adhoc Resource Access    |
|      (Continuum F18) with authoritative read and guarded convergence.                              |
|    - GovernedWriter.create() / update() (calliope): Realizes candidate state progression (F17).    |
|    - GovernedRead<T> (calliope): Encapsulates payload, ResourceKey, AuthoritativeVersion, and       |
|      ActiveStateToken as an atomic carrier across the read-update lifecycle.                       |
+----------------------------------------------------------------------------------------------------+
| 3. UNREPRESENTED IMPLEMENTATION CONCEPTS                                                           |
|    - ActiveStateToken (calliope): Ephemeral Hot Rod generation token; missing formal continuum     |
|      representation as an ArchiMate Data Object (deferred under CDG-31).                           |
|    - AuthoritativeVersion (calliope): Opaque string durable version carrier (deferred CDG-47).     |
|    - WriteResult / ActiveStateConflict (calliope): Concurrency outcome carriers (deferred CDG-47). |
+----------------------------------------------------------------------------------------------------+
| 4. UNMAPPED ARCHITECTURAL CONCEPTS                                                                 |
|    - Context Load & Context Save (Continuum F16): Composite graph working-set aggregation schemas  |
|      defined in continuum but unmapped in Java code (CDG-45).                                      |
|    - Adhoc Search (Continuum F16 / F43): First-class search service unmapped in C11 contracts.     |
+----------------------------------------------------------------------------------------------------+
| 5. HISTORICAL & OBSOLETE CONSTRUCTS                                                                |
|    - Legacy /fhir endpoint & FhirStorageService: In-process storage path; bypasses GovernedAccess.  |
|    - FhirRestCacheStore: Synchronous write-through store; slated for retirement at Milestone M4.3. |
|    - OperationsRestCacheStore: Legacy caches configured in hestia/mneme-cluster/src/main/resources/ |
|      infinispan.xml:250–288.                                                                       |
|    - PersistenceOperationEnvelope (Continuum D04): Historical carrier shape replaced by C11.      |
+----------------------------------------------------------------------------------------------------+
| 6. GENUINE CONFLICTS & INVARIANT TENSIONS                                                          |
|    - Pylai FHIR Registry -> mnemosyne-clinical POM dependency (pylai-fhir-registry/pom.xml:55–58). |
|    - FhirStorageService:86–100: Defaults null principal to self-granted service:mnemosyne (AX-07).|
|    - DefaultCommunicationService:92–101: Swallows cache put failure; emits AA ACK (Invariant 4).   |
|    - OutboundStateLifecycleManager:111–189: Five exception-swallowing catch blocks (Invariant 5).  |
|    - AuthoritativePersistenceService:104: Returns NotCommitted for absent entities (HTTP 500).     |
+----------------------------------------------------------------------------------------------------+
| 7. UNCERTAIN RUNTIME CAPABILITIES                                                                  |
|    - FhirRestCacheStore HTTP 8080 vs HTTPS 8443 / mTLS reachability in Docker runtime.             |
|    - AuthoritativeVersion longValue() fallback to 0L during non-numeric version comparison.        |
+----------------------------------------------------------------------------------------------------+
```

### 5.2 Continuum Candidate Four-Service Surface vs. C11 Supporting Point Carriers
- **Continuum Candidate Architecture:** Candidate assertion **F16** (`enterprise-architecture/continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md:2501`) and **M05** (`:2525`) define Mneme's principal exposed interaction surface as four candidate services:
  1. `Context Load` (preferred, performance-optimized working set acquisition)
  2. `Context Save` (persisting modified working sets and Pragma outputs)
  3. `Adhoc Resource Access` (bounded runtime retrieval outside preloaded Context)
  4. `Adhoc Search` (authoritative persistence query outside active Context)
  *Candidate Rule:* F16 states **"No fifth service or exhaustive preload"**. Supporting point carriers (`ResourceKey`, `GovernedRead`, `ActiveStateToken`, `AuthoritativeVersion`, `WriteResult`) are classified under **D04** (`:2519`) and **M05** (`:2525`) as **supporting contracts (C11)** constrained by this surface, with exact mappings formally deferred to CDG-31, CDG-45, and CDG-47.
- **Java Implementation Reality:**
  - `calliope` declares C11 point interfaces:
    - `GovernedReader.read(ResourceKey key, ThemisSecurityContext securityContext): Optional<GovernedRead<T>>` (`calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedReader.java:40`)
    - `GovernedWriter.create(ResourceKey key, T resource, ThemisSecurityContext securityContext): WriteResult<T>` (`calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedWriter.java:39`)
    - `GovernedWriter.update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext): WriteResult<T>` (`calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedWriter.java:50`)
  - Note: No method takes `ActiveStateToken` or `AuthoritativeVersion` directly as parameters; these travel inside `GovernedRead<T>`.
  - In `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/access/DefaultGovernedReader.java:83–193`, execution is **authoritative-read first**: it validates the boundary, checks Themis security, reads authoritatively from Mnemosyne via `AuthoritativePersistencePort.read(key)` (`:124`), converges local cache via `ActiveStateConvergencePort.converge(...)` (`:167`), and observes an `ActiveStateToken` via `ActiveStateCoordinator.observe(...)` (`:181`).
  - In `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/access/DefaultGovernedWriter.java:162–245`, execution validates the boundary and security, consumes `current.activeToken()` via `ActiveStateCoordinator.consume(...)` (`:206`), and persists through `AuthoritativePersistencePort.update(...)` (`:223`).
  - Composite `ContextLoad` and `ContextSave` graph aggregation schemas are unmapped in current code; Context Load/Save remain principal Mneme responsibilities (not delegated to Ergon activity units or Search).

```
+---------------------------------------------------------------------------------------------------+
| CONTINUUM CANDIDATE SERVICE SURFACE (F16 / M05)                                                   |
|   1. Context Load          2. Context Save          3. Adhoc Resource Access    4. Adhoc Search   |
+---------------------------------------------------------------------------------------------------+
                                                      ^
                                                      | Structural Realisation
+---------------------------------------------------------------------------------------------------+
| JAVA C11 SUPPORTING POINT CONTRACTS (calliope)                                                    |
|   - GovernedReader.read(ResourceKey, ThemisSecurityContext): Optional<GovernedRead<T>>            |
|   - GovernedWriter.create(ResourceKey, T, ThemisSecurityContext): WriteResult<T>                  |
|   - GovernedWriter.update(GovernedRead<T>, T, ThemisSecurityContext): WriteResult<T>              |
+---------------------------------------------------------------------------------------------------+
```

---

## 6. Lifecycle Distinctions Across Five Version Domains (Reconciliation Model)

To reconcile implementation constructs against AX-05 and AX-14, this report synthesizes a structural model distinguishing **five separate version domains**. Conflating these domains creates cache coherency anomalies, race conditions, and corrupted audit provenance:

```
+----------------------------------------------------------------------------------------------------+
| 1. ActiveStateToken (Mneme Active Tier)                                                            |
|    - Ephemeral cache entry generation (Infinispan Hot Rod version).                                |
|    - Reconstructable upon restart; validated via CAS; invalidation does not alter durable data.    |
+----------------------------------------------------------------------------------------------------+
| 2. AuthoritativeVersion (Mnemosyne Durable Tier)                                                   |
|    - Opaque non-blank String record (AuthoritativeVersion.java:26–35).                             |
|    - Committed exclusively by PostgreSQL transactions; permanent audit anchor.                     |
|    - Correctness Risk: longValue() returns 0L on non-numeric parse (:45–51).                       |
|      MnemeCachedResource:69 stores longValue(); HotRodMnemeConvergence:158–170 evaluates           |
|      cachedVer >= targetVer. If non-numeric, version collapses to 0, risking false convergence.    |
+----------------------------------------------------------------------------------------------------+
| 3. HTTP ETag (Wire Transport Representation)                                                       |
|    - Weak entity tag (W/"{version}") emitted on REST endpoints.                                    |
|    - Transport precondition vehicle for If-Match / If-None-Match concurrency headers.              |
+----------------------------------------------------------------------------------------------------+
| 4. FHIR meta.versionId (Domain Payload Representation)                                             |
|    - String attribute embedded inside FHIR JSON/XML resource bodies (Resource.meta.versionId).      |
|    - Standard interoperability metadata; non-authoritative for internal transaction locking.       |
|    - Observed Mapping: AuthoritativePersistenceService:145–157, 261–277 assigns/increments         |
|      meta.versionId to match database sequence version.                                            |
|    - Observed Mapping: FhirRestGatewayController:209–212 derives ETag from meta.versionId.        |
+----------------------------------------------------------------------------------------------------+
| 5. Versioned Temporal Context (Mneme Context Tier - Candidate F16)                                 |
|    - Aggregated execution envelope for working sets and Pragma task state.                         |
|    - Candidate F16 asserts: Context version differs from underlying resource state/version.        |
+----------------------------------------------------------------------------------------------------+
```

---

## 7. Architectural Discrepancy Assessment Matrix

| # | Discrepancy / Area | Category | Architecture / Axiom Reference | Observed Code / Configuration State |
| :--- | :--- | :--- | :--- | :--- |
| **1** | **Convergence Plan Status & Authority Contradiction** | **Authority Gap & Plan Inconsistency** | AX-17; Plan §6 (`:258, 312, 352`), §12 (`:667, 722`), §13 (`:726, 747–753`); `docs/architecture-decisions.md` | Plan §12 (`:722`) carries the status line "CONVERGENCE COMPLETE" following the M8 exit criterion; it reads as a target or ambiguous line, not proof of current status. Plan §13 (`:747–753`) conflicts next M2.3 vs complete M2.3 (`:312`) and M2.4 (`:352`). Plan claims ADR-021/022 accepted, but `docs/architecture-decisions.md` ends at ADR-020 (line 837). |
| **2** | **Mnemosyne Persistence Adapter Dual-Implementation** | **Authority Gap & Candidate Gap** | AX-05, AX-17; Plan §6 / M2.2 Path B (`:307`); `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java:55` | Plan claims M2 Path B verification via `HapiJpaAuthoritativePersistenceAdapter`, but production code uses `@Service @Primary` `AuthoritativePersistenceService` (`:55–56`, custom Spring Data repository). HAPI adapter is marked `"Experimental"` (`:51`). |
| **3** | **Absent Entity Read Result Contract (404 vs 500)** | **Contract Inconsistency & Test Failure** | AX-15; Plan §6 / M2.4 (`:352, 356, 362`); `hestia/mnemosyne-api/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java:35–38`; `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java:103–109` | `AuthoritativePersistenceService.read()` returns `NotCommitted` for absent entities, controller maps to HTTP 500 (`:123–133`). `AuthoritativeFhirResourceIntegrationTest:67` fails (expected 404, got 500) despite Plan §6 M2.4 heading marked "COMPLETE / CONFORMANT" (`:352`). `DefaultGovernedReader` throws `ActiveCoordinationUnavailableException` on NotCommitted (`:139–144`). `HapiJpaAuthoritativePersistenceAdapter` returns `Absent` for null/not-found (`:114–116, 125–127`), and `NotCommitted` for deleted entities (`:128–130`). The port Javadoc itself says `read` returns not-committed "if absent or deleted" (`AuthoritativePersistencePort.java:35–38`), supporting the `@Primary` service, whereas the controller, HTTP client (`:160–162`), HAPI adapter and test expect `Absent`: a contract inconsistency, not a plain defect. SA confirmation of the READ result contract for never-existed resources is requested, plus disposition of soft-deleted resources (`Absent` vs `NotCommitted`) and of UPDATE outcomes (plan `:362` and HTTP client `:334–336` map absent UPDATE 404 to `NotCommitted`; `@Primary` service returns `Conflict`/412 at `AuthoritativePersistenceService.java:280–290`). |
| **4** | **Mneme Four-Service Surface vs C11 Point Contracts** | **Candidate Gap & Unresolved Mapping** | Continuum F16 (`:2501`), F17 (`:2507`), D04 (`:2519`), M05 (`:2525`) | C11 point carriers (`GovernedReader`/`GovernedWriter`) implement atomic point access (Adhoc Resource Access / mutation progression). Composite `ContextLoad`/`ContextSave` schemas are unmapped. F16 specifies no fifth service; carrier mapping deferred to CDG-31/45/47. |
| **5** | **Inbound MLLP Dual-Write Exception Swallowing (REC-001)** | **Observed Invariant Violation** | Invariant 4 (REC-001); Invariant 8.2, 8.4; Axiom AX-15, AX-18 | `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultCommunicationService.java`: writes local `communicationStore` map first (`:90`, Invariant 8.2); `getRemoteCache()` returns null on failure (`:59–67`); direct `RemoteCache.put` is raw cache mutation (`:97`, Invariant 8.4); exception during remote put swallowed with `log.warn` (`:92–101`). `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/hl7/IncomingAdtMessageProcessor.java:314` then emits `AA` ACK instead of reaching `AE` NACK catch block (`:324–327`). |
| **6** | **Outbound MLLP Lifecycle Exception Swallowing (REC-002)** | **Observed Invariant Violation** | Invariant 5 (REC-002); Axiom AX-15 | `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/lifecycle/OutboundStateLifecycleManager.java:111–113, 144–146, 161–163, 173–175, 187–189` contains 5 catch blocks swallowing exceptions during lifecycle init, Task/Provenance updates, and Petasos event publishing with `LOG.warn`. |
| **7** | **Legacy `FhirRestCacheStore` & Soft-Delete Cascade** | **Uncertain Runtime & Candidate Conflict** | ADR-020; Invariant 8; Axiom AX-05 | `hestia/mneme-cluster/src/main/resources/infinispan.xml:59–243` configures synchronous write-through via `FhirRestCacheStore` (implements `NonBlockingStore`, `:44`). Cache remove calls REST DELETE (`:125–135`) -> `@Delete` provider -> `FhirStorageService.deleteResource()` (generic soft-delete tombstone, `:288–301`). Reachability uncertain (HTTP 8080 vs HTTPS 8443/mTLS). |
| **8** | **Pylai FHIR Registry Direct Persistence Coupling** | **Source-Level Boundary Conflict** | Invariant 8.1; Invariant 9; Axiom AX-05, AX-13, AX-18 | `pylai/pylai-fhir-registry/pom.xml:55–58` imports `mnemosyne-clinical`; `FhirRestGatewayController:165, 203, 257` calls `FhirStorageService` directly in-process. Module is currently an undeployed library jar in Docker compose. |
| **9** | **Persistence Security Fallback & Self-Grant** | **Observed Invariant Violation** | Invariant 6 (Default-Deny); Axiom AX-07 | `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java:86–100` defaults `null` security principal to `service:mnemosyne` with full self-granted CRUD/Admin permissions on internal storage paths. |

---

## 8. Structured Solution Architect Disposition Items

### Disposition Item 1: Master Convergence Plan Sequencing & Active Milestone Authority
- **Category:** Authority Gap & Plan Inconsistency.
- **Architectural Citation:** Axiom AX-17; Plan §6 (`:258, 312, 352`), §12 (`:667, 722`), §13 (`:726, 747–753`); AGENTS §6.2, §6.4.
- **Context & Evidence:** Plan §12 (`:722`) carries the status line "Programme status: CONVERGENCE COMPLETE." following the M8 exit criterion; it reads as a target or ambiguous line, not proof of current status. Plan §13 (`:747–753`) asserts Milestone M2 is current and M2.3 is next, which directly conflicts with the Plan §6 M2.3 (`:312`) and M2.4 (`:352`) headings claiming "COMPLETE / CONFORMANT" — even though M2.4's integration test (`AuthoritativeFhirResourceIntegrationTest:67`) fails deterministically (expected 404, got 500).
- **Bounded Question for Solution Architect:**  
  *Which milestone and exact step is the operative baseline for subsequent work, and should the Master Convergence Plan be updated under its authorized maintenance procedure (§15 Plan Maintenance, line 812 / AGENTS §6.4) prior to authorizing any solution build increment?*
- **Conforming Decision Paths:**
  - **Path 1A (Reconcile Plan to Active Milestone M2):** Authorize formal plan maintenance to correct internal contradiction, setting current position to Milestone M2 (Step M2.4 missing-READ result-contract resolution, conditional on Item 3 Solution Architect disposition) and next step to M3.1.
  - **Path 1B (Ratify Milestone M2 as Accepted and Authorize M3.1 Entry):** Accept M2 as substantively complete, with M2.4 missing-READ result-contract resolution conditional on Item 3 Solution Architect disposition, authorizing transition to Milestone M3 (Governed Access Integration).
- **Dependencies & Guardrails:** AGENTS §6.2 rule: "One plan. One current milestone. One next step." No implementation commenced without milestone alignment.

---

### Disposition Item 2: Mnemosyne Persistence Engine & Decision Register Authority
- **Category:** Authority Gap & Candidate Gap.
- **Architectural Citation:** Axiom AX-17; Plan §6 / M2.2 (`:307`), §13 (`:731–734`); `docs/architecture-decisions.md`.
- **Context & Evidence:** The convergence plan claims ADR-021 and ADR-022 are accepted. However, `docs/architecture-decisions.md` contains decisions only up to ADR-020 (`:837–1072`). In code, `AuthoritativePersistenceService` is `@Primary` (`:55–56`) over `FhirResourceRepository`, while `HapiJpaAuthoritativePersistenceAdapter` is marked `"Experimental"` (`:51`).
- **Bounded Question for Solution Architect:**  
  *What is the authoritative persistence architecture for Mnemosyne, where is the authoritative provenance for claimed ADR-021/022, and which adapter should be formally ratified in the Decision Register?*
- **Conforming Decision Paths:**
  - **Path 2A (Retain Custom Repository Pending SA Decision):** Retain `@Primary` `AuthoritativePersistenceService` for immediate M2 test execution without drafting duplicate or speculative ADRs.
  - **Path 2B (Locate Provenance / Ratify HAPI JPA Native Path):** Locate historical ADR-022 provenance; if HAPI JPA is mandated, promote `HapiJpaAuthoritativePersistenceAdapter` to `@Primary` and deprecate custom repository.
- **Dependencies & Guardrails:** AX-17; agents must never draft duplicate or speculative ADRs without Solution Architect instruction.

---

### Disposition Item 3: Missing Entity Read Result Contract (404 vs 500)
- **Category:** Contract Inconsistency (port Javadoc vs controller/client/adapter/test) with Test Failure.
- **Architectural Citation:** Axioms AX-14, AX-15, AX-17; Plan §6 / M2.4 (`:352, 356, 362`); `hestia/mnemosyne-api/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java:35–38`; `hestia/mnemosyne-api/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java:33–119`.
- **Context & Evidence:** `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java:103–109` returns `NotCommitted("Resource not found")` on absent entities. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/controller/AuthoritativeFhirResourceController.java:123–133` maps `Absent` to HTTP 404 and `NotCommitted` to HTTP 500. `AuthoritativeFhirResourceIntegrationTest:67` fails (expected 404, got 500), despite Plan §6 M2.4 being headed "COMPLETE / CONFORMANT" (`:352`). In `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/access/DefaultGovernedReader.java:139–144`, `NotCommitted` triggers `ActiveCoordinationUnavailableException` instead of returning `Optional.empty()` (`:135–137`). `HapiJpaAuthoritativePersistenceAdapter` returns `Absent` on null/not-found (`:114–116, 125–127`) and `NotCommitted` on deleted (`:128–130`). **Contrary contract evidence:** the port Javadoc (`hestia/mnemosyne-api/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java:35–38`) documents `read` as not committed "if absent or deleted", which supports the service's `NotCommitted` and the plan READ line (`:356`, HTTP 404 -> `NotCommitted`). **UPDATE is a separate question:** plan `:362` (absent UPDATE 404 -> `NotCommitted`) agrees with `MnemeAuthoritativeHttpClient.java:334–336` (UPDATE 404 -> `NotCommitted`), while the `@Primary` service returns `Conflict` (HTTP 412) for an absent or deleted UPDATE target (`AuthoritativePersistenceService.java:280–290`) and the controller maps UPDATE `NotCommitted` to 500 (`:351–355`). Neither path yields `Absent`; this is not part of the READ increment.
- **Bounded Question for Solution Architect:**  
  *Should `AuthoritativePersistenceService.read()` be corrected to return `AuthoritativePersistenceResult.Absent` for never-existed entities, and with the port Javadoc (`:35–38`) corrected accordingly; what is the status mapping for soft-deleted entities (Absent vs NotCommitted); and what is the intended UPDATE outcome for absent/deleted targets (NotCommitted per plan `:362`/client, Conflict per service)?*
- **Conforming Decision Paths:**
  - **Path 3A (Conditional on SA confirming `Absent` as the READ contract for never-existed resources):** Update `AuthoritativePersistenceService.read()` to return `AuthoritativePersistenceResult.Absent` on never-existed entities only, correct the port Javadoc (`AuthoritativePersistencePort.java:35–38`) and Plan §6 / M2.4 READ line (`:356` only; `:362` is unchanged), add/adjust the focused test, and re-verify the integration test.
  - **Path 3B (SA Disposition on Deleted Entities and UPDATE Outcomes):** Adjudicate authoritative lifecycle mapping for soft-deleted entities (ADR-020, AX-14/15) and the absent/deleted UPDATE result contract (NotCommitted vs Conflict); both remain unresolved and outside the first increment.
- **Dependencies & Guardrails:** `MnemeAuthoritativeHttpClient` already maps HTTP 404 to `Absent` (`:160–162`).

---

### Disposition Item 4: Mneme Service Surface & C11 Point Carrier Mapping
- **Category:** Unresolved Mapping / Candidate Gap.
- **Architectural Citation:** Continuum F16 (`:2501`), F17 (`:2507`), F18 (`:2513`), D04 (`:2519`), M05 (`:2525`).
- **Context & Evidence:** Candidate assertion F16 defines four principal services (Context Load, Context Save, Adhoc Resource Access, Adhoc Search) and disallows a fifth service. C11 carriers (`GovernedReader`, `GovernedWriter`) provide single-resource point contracts (`read`, `create`, `update`).
- **Bounded Question for Solution Architect:**  
  *How are C11 point carriers formally mapped against Mneme's candidate four principal services in Milestones M2/M3, and when will composite Context Load/Save schemas (CDG-45) be specified?*
- **Conforming Decision Paths:**
  - **Path 4A (Adjudicate C11 Carriers as Supporting Point Substrate):** Formalize C11 carriers as supporting contracts realizing Adhoc Resource Access (F18) and internal mutation progression (F17) for M2/M3, preserving Context Load/Save as principal Mneme responsibilities to be developed under CDG-45 without inventing a fifth exposed service.
  - **Path 4B (Require Context Schema Definition Prior to M3):** Specify composite `ContextLoad`/`ContextSave` structures in `calliope` prior to production wiring.
- **Dependencies & Guardrails:** Invariant 8; AX-05; Context version must remain distinct from resource version (candidate F16); Context management remains Mneme responsibility (not delegated to Ergon activity units or Search).

---

### Disposition Item 5: Inbound Ingress Exception Handling (REC-001 / Invariant 4)
- **Category:** Observed Invariant Violation.
- **Architectural Citation:** Invariant 4 (Dual-Write Safety / REC-001); Invariant 8.2, 8.4; Axiom AX-15, AX-18.
- **Context & Evidence:** In `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultCommunicationService.java`, local map is written first (`:90`), `getRemoteCache()` returns null on failure (`:59–67`), and remote cache write exceptions are caught with `log.warn` and swallowed (`:92–101`). `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/hl7/IncomingAdtMessageProcessor.java:314` generates `AA` ACK over MLLP despite cache failure without reaching the `AE` NACK catch block (`:324–327`).
- **Bounded Question for Solution Architect:**  
  *Should `DefaultCommunicationService` propagate cache write exceptions to `IncomingAdtMessageProcessor` to trigger an MLLP `AE` NACK per Invariant 4 / REC-001?*
- **Conforming Decision Paths:**
  - **Path 5A (Enforce Invariant 4 Fail-Closed AE NACK):** Propagate cache write exceptions out of `DefaultCommunicationService`, enabling `IncomingAdtMessageProcessor` to catch and emit an MLLP `AE` NACK for sender retry.
  - **Path 5B (HCRL Amendment for Asynchronous Cache Projection):** If cache downtime must not block MLLP ACK, submit an HCRL amendment to reclassify cache population as an asynchronous projection after durable Petasos queue commit.
- **Dependencies & Guardrails:** Invariant 4; Invariant 7 (Zero-PHI in logs); upstream clinical sender retry capability. Kept as a separate disposition item, not bundled into Milestone M2.

---

### Disposition Item 6: Outbound Egress Exception Handling (REC-002 / Invariant 5)
- **Category:** Observed Invariant Violation.
- **Architectural Citation:** Invariant 5 (Destination Fan-Out State Tracking / REC-002); Axiom AX-15.
- **Context & Evidence:** `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/lifecycle/OutboundStateLifecycleManager.java:111–113, 144–146, 161–163, 173–175, 187–189` contains 5 catch blocks that swallow exceptions during lifecycle initialization, Task/Provenance updates, and Petasos event publishing with `LOG.warn`.
- **Bounded Question for Solution Architect:**  
  *What is the authoritative uncertainty-handling strategy under AX-15 for outbound lifecycle update failures that avoids silent catch-and-log swallowing while preventing duplicate dispatch?*
- **Conforming Decision Paths:**
  - **Path 6A (Fail-Closed Delivery Error Propagation):** Rethrow critical lifecycle failures to mark outbound dispatch attempt as failed in Petasos, triggering dead-lettering or retry.
  - **Path 6B (Structured Task Status / Kleio Error Capture):** Record explicit uncertainty in `Task.output` and emit high-priority diagnostic alerts to Kleio without aborting completed physical dispatches.
- **Dependencies & Guardrails:** Invariant 5; AX-15; any new task status values require formal HCRL adjudication.

---

### Disposition Item 7: Legacy `FhirRestCacheStore` & Staged Retirement
- **Category:** Uncertain Runtime & Candidate Conflict.
- **Architectural Citation:** ADR-020 (No Physical Delete); Invariant 8; Axiom AX-05; AGENTS §6.3.
- **Context & Evidence:** `hestia/mneme-cluster/src/main/resources/infinispan.xml:59–243` configures synchronous write-through via `FhirRestCacheStore` (implements `NonBlockingStore`, `:44`). Explicit cache remove invokes REST DELETE (`:125–135`), reaching `@Delete` providers and generic soft-delete (`FhirStorageService:288–301`). Runtime connectivity is uncertain due to port 8080 vs HTTPS 8443/mTLS configuration. AGENTS §6.3 forbids premature removal before governed access is ready.
- **Bounded Question for Solution Architect:**  
  *Should `FhirRestCacheStore` remain in place during Milestone M3 and be formally retired at Milestone M4.3 (Application Migration) as specified in the convergence plan once `GovernedAccess` is proven?*
- **Conforming Decision Paths:**
  - **Path 7A (Staged M4.3 Retirement per Plan):** Retain store configuration through M3, verify Docker runtime status, and decommission at Step M4.3 after `GovernedAccess` is operational.
  - **Path 7B (Disable Write-Through DELETE in M3 Option):** Solution Architect option to patch `FhirRestCacheStore.delete()` to a no-op during M3 to eliminate deletion cascade risk prior to full M4.3 retirement.
- **Dependencies & Guardrails:** Invariant 8; ADR-020; explicit cache remove/clear must never delete durable Mnemosyne state; capacity eviction does not invoke delete.

---

### Disposition Item 8: Pylai FHIR Registry Architecture & Persistence Decoupling
- **Category:** Source-Level Boundary Conflict.
- **Architectural Citation:** Invariant 8.1; Invariant 9; Axiom AX-05, AX-13, AX-18.
- **Context & Evidence:** `pylai/pylai-fhir-registry/pom.xml:55–58` declares compile dependency on `mnemosyne-clinical`; controller calls `FhirStorageService` directly in-process (`:165, 203, 257`). Module is an undeployed library jar in Docker compose.
- **Bounded Question for Solution Architect:**  
  *Should `pylai-fhir-registry` be scheduled for refactoring to consume `GovernedReader` and `PylaiFhirPublicationProjector` as part of Milestone M4 (Application Migration)?*
- **Conforming Decision Paths:**
  - **Path 8A (Migrate in Milestone M4 per Convergence Plan):** Retain current module structure as undeployed; refactor to `GovernedReader` in Milestone M4, breaking compile coupling to `mnemosyne-clinical`.
  - **Path 8B (Immediate Decoupling in M3):** Include registry refactoring in Milestone M3 Governed Access scope.
- **Dependencies & Guardrails:** Invariant 1; Invariant 8.1; Invariant 9; AX-18.

---

### Disposition Item 9: Persistence Layer Security Context Hardening
- **Category:** Observed Invariant Violation.
- **Architectural Citation:** Invariant 6 (Default-Deny); Axiom AX-07.
- **Context & Evidence:** `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/service/FhirStorageService.java:86–100` defaults `null` security principal to `service:mnemosyne` with full self-granted authorities on internal storage paths.
- **Bounded Question for Solution Architect:**  
  *Should all persistence layer entry points require an explicit `ThemisSecurityContext`, failing closed on null or unauthenticated callers?*
- **Conforming Decision Paths:**
  - **Path 9A (Enforce Fail-Closed Security Context across Persistence Tier Option):** Solution Architect option to remove `null` principal self-grant in `FhirStorageService`, requiring valid `ThemisSecurityContext` and throwing `SecurityException` on unauthenticated calls.
  - **Path 9B (Explicit System Task Identity Token):** Require internal system bootstrap callers to supply an explicit cryptographic or system token rather than defaulting on null arguments.
- **Dependencies & Guardrails:** Invariant 6; Axiom AX-07.

---

## 9. Master Convergence Plan Inconsistency Catalog

| Plan Section | Exact Recorded Text in Plan | Implementation Reality | Conflict / Ambiguity Analysis |
| :--- | :--- | :--- | :--- |
| **§12 / Status (`:722`)** | `"Programme status: CONVERGENCE COMPLETE."` | Milestone M2 has a failing integration test; Milestone M3 (Governed Access) is unwired; Milestone M4 is pending. | Target / exit criterion line following M8 exit criterion reads "Programme status: CONVERGENCE COMPLETE." (`:722`), which is ambiguous rather than proven current status, conflicting with §13 stating convergence is at Milestone M2. |
| **§13 / Position vs §6 Headings (`:312, 352, 731–753`)** | `"ADR-021 Mneme/Mnemosyne semantic ownership --- ACCEPTED."`<br>`"ADR-022 Mnemosyne/HAPI JPA persistence architecture --- ACCEPTED."`<br>`"Current milestone: M2 ... Next convergence implementation step: M2.3"` | `docs/architecture-decisions.md` contains only ADR-001 through ADR-020 (`:837–1072`). Headings for §6 M2.3 (`:312`) and M2.4 (`:352`) claim "COMPLETE / CONFORMANT" despite `AuthoritativeFhirResourceIntegrationTest:67` failing, while §13 lists M2.3 as next. | Authority gap under AX-17 regarding missing ADR-021/022, combined with contradictory milestone progression indicators (M2.4 marked COMPLETE while integration test fails; §13 listing M2.3 as next). |
| **§6 / M2.2 (`:307`)** | `"Path B (Mnemosyne Durable Persistence Proof): Verified transactional persistence below the HTTP boundary via HapiJpaAuthoritativePersistenceAdapter against PostgreSQL"` | Production code designates `AuthoritativePersistenceService` (`@Service @Primary`, `:55–56`) over `FhirResourceRepository` as the active operational path. `HapiJpaAuthoritativePersistenceAdapter` is marked `"Experimental"` (`:51`). | Plan records Path B proof using HAPI JPA adapter, but codebase uses custom Spring Data JPA repository as primary. |
| **§6 / M2.4 (`:356, 362`)** | `"1. Initial READ of absent resource: HTTP 404 -> AuthoritativePersistenceResult.NotCommitted"` and `"7. Absent UPDATE ... HTTP 404 -> AuthoritativePersistenceResult.NotCommitted"` | `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java:104` returns `NotCommitted`, which `AuthoritativeFhirResourceController:129–133` maps to HTTP 500. `AuthoritativeFhirResourceIntegrationTest:67` fails (expected 404, got 500). | READ (`:356`): inconsistent with the HTTP client READ mapping (404 -> `Absent`, `:160–162`), controller, HAPI adapter and test, but consistent with the port Javadoc (`AuthoritativePersistencePort.java:35–38`) and the `@Primary` service; correct only if SA confirms `Absent`. UPDATE (`:362`): consistent with the HTTP client (`:334–336`, 404 -> `NotCommitted`) but differs from the `@Primary` service (`Conflict`/412, `AuthoritativePersistenceService.java:280–290`); unresolved SA question, not to be changed to `Absent`. |

---

## 10. Prioritized Candidate First Build Increment (Advisory)

In accordance with AGENTS §6.2 and §6.3, no solution build is authorized or commenced by this report. The following single, bounded build increment is proposed for Solution Architect consideration, contingent on Solution Architect disposition on Milestone M2 sequencing:

```
+----------------------------------------------------------------------------------------------------+
| CONDITIONAL CANDIDATE FIRST INCREMENT: Milestone M2.4 Missing-READ Result Contract & Test Stability |
|                                                                                                    |
| - Precondition: Solution Architect confirms Milestone M2.4 as current operative step (Item 1).     |
| - Bounded Code Scope:                                                                              |
|   * Only if SA confirms Absent as the READ contract: fix AuthoritativePersistenceService.java:    |
|     103-109 to return AuthoritativePersistenceResult.Absent on never-existed entities only        |
|     (deleted-entity and UPDATE outcomes are separate SA dispositions).                            |
| - Bounded Plan Scope:                                                                              |
|   * If confirmed: correct plan §6 / M2.4 READ line 356 only and port Javadoc :35-38; line 362    |
|     (UPDATE) is NOT changed and UPDATE outcomes are excluded (separate SA question).             |
| - Target Verification:                                                                             |
|   * Execute AuthoritativeFhirResourceIntegrationTest (expected 100% PASS, line 67 receives 404).    |
| - Strict Exclusions (Guardrails per AGENTS §6.3):                                                  |
|   * Zero persistence engine or adapter changes (no ADR ratification).                              |
|   * Zero Inbound MLLP / REC-001 modifications (kept as separate Disposition Item 5).              |
|   * Zero GovernedAccess wiring (deferred to Milestone M3 upon explicit SA authorization).         |
+----------------------------------------------------------------------------------------------------+
                                                  |
                                                  v (Upon subsequent explicit SA authorization of Milestone M3)
+----------------------------------------------------------------------------------------------------+
| SUBSEQUENT INCREMENT: Milestone M3 Governed Access Production Wiring                               |
|                                                                                                    |
| - Note: DefaultGovernedReader and DefaultGovernedWriter already exist in hestia/mneme-cluster/     |
|   with comprehensive unit tests (DefaultGovernedReaderTest, DefaultGovernedWriterTest).             |
| - Scope: Provide Spring/CDI bean configuration and wire HotRodActiveStateCoordinator,              |
|   HotRodMnemeConvergence, and MnemeAuthoritativeHttpClient into GovernedAccess.                    |
| - Verification: Execute Milestone M3.3 failure/recovery verification.                              |
| - Strict Exclusions: Zero application consumer migration (deferred to Milestone M4).               |
+----------------------------------------------------------------------------------------------------+
```

---

## 11. Test & Verification Conformance Record

| Verification Suite / Test Target | Command Executed | Results / Status | Elapsed Time | Conformance & Capability Notes |
| :--- | :--- | :--- | :--- | :--- |
| **ArchUnit Architecture Suite** | `mvn -q -o test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` | **PASS** (90 / 90 tests in 11 classes) | ~18s | Verifies static package layering, POM dependency isolation, and import restrictions. Does NOT detect runtime Invariant 8 violations (in-process calls in undeployed library jars) or runtime Invariant 6 self-grants. |
| **Authoritative REST Integration Test** | `mvn -q -o test -pl hestia/mnemosyne-clinical -am -Dtest='AuthoritativeFhirResourceIntegrationTest' -Dsurefire.failIfNoSpecifiedTests=false` | **FAIL** (Line 67: `expected:<404> but was:<500>`) | ~23s | Deterministic failure traced to `AuthoritativePersistenceService:104` returning `NotCommitted` on a missing-resource read (contract inconsistency pending SA confirmation). |
| **Tracked Repository Status Check** | `git status` | **PASS** (Tracked working tree clean) | <1s | Tracked tree clean; untracked pre-existing `.junie/plans` file preserved. |
| **Git Baseline & Commit Verification** | `git log -1 --format="%H %s"` | **PASS** (`ff141340fdb0ab...`) | <1s | Commit `ff141340` on branch `Data-Governence-Services`. |

---

## 12. Investigation Limitations

The findings in this report are subject to the following explicit operational boundaries:
1. **Source Inspection & Configuration Analysis:** All conclusions are derived from static source code inspection, POM dependency graphs, and XML/YAML configuration files.
2. **Docker Runtime & Port Reachability:** Docker runtime state, active container networking, and port reachability (e.g. HTTP 8080 vs HTTPS 8443 / mTLS between `FhirRestCacheStore` and `AuthoritativeFhirResourceController`) were inferred from configuration and source code rather than verified against live container instances.
3. **Zero Runtime Mutation:** No Docker containers were spawned, executed, or altered during this investigation.

---

## 13. Discoveries for Future Steps

- **Execution commands & speed:**
  - ArchUnit suite: `mvn -q -o test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` passes 90 tests across 11 classes in ~18s.
  - Focused integration test: `mvn -q -o test -pl hestia/mnemosyne-clinical -am -Dtest='AuthoritativeFhirResourceIntegrationTest' -Dsurefire.failIfNoSpecifiedTests=false` fails deterministically on line 67 in ~23s.
- **Difficulties & solutions:**
  - The missing-entity 404 vs 500 failure is localized to `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java:103–109` returning `NotCommitted`, which conflicts with the controller/client/adapter/test expectation of `Absent` but matches the port Javadoc (`AuthoritativePersistencePort.java:35–38`); resolution is conditional on SA confirmation of the READ contract.
  - `DefaultGovernedReader` in `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/access/DefaultGovernedReader.java:83–193` is already authoritative-read first; it reads authoritatively from Mnemosyne, converges the cache, and observes the active token.
  - `DefaultGovernedReader` and `DefaultGovernedWriter` already possess unit test suites (`DefaultGovernedReaderTest`, `DefaultGovernedWriterTest`), meaning Milestone M3 is strictly about dependency wiring and runtime failure/recovery verification.
  - Inbound exception swallowing is in `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultCommunicationService.java:92–101` and inbound processor `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/hl7/IncomingAdtMessageProcessor.java:314, 324–327` (REC-001).
  - Outbound exception swallowing is in `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/lifecycle/OutboundStateLifecycleManager.java:111–113, 144–146, 161–163, 173–175, 187–189` across five distinct lifecycle blocks (REC-002).
  - `AuthoritativeVersion` is an opaque `String` record with `longValue()` fallback to `0L` (`:45–51`), creating potential risk in `HotRodMnemeConvergence:158–170` if non-numeric version strings are utilized.
- **Recommendations for Next Steps:**
  - Submit this report to Solution Architect Mark Hunter for disposition on Items 1–9.
  - Upon Solution Architect adjudication, execute the conditional bounded Milestone M2.4 never-existed READ fix (only if the SA confirms Absent as the READ contract) before proceeding to Milestone M3.
