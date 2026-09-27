---
sessionId: session-260927-095355-tubz
---

# Assessment Framework & Axioms

### Assessment Purpose & Authority Hierarchy

The purpose of this assessment plan is to define the methodology and execution framework for a rigorous, read-only architectural conformance assessment of the Harmonia repository against the foundational architectural baseline.

In accordance with `AGENTS.md` and `docs/architectural-axioms.md`, the assessment applies the strict architectural authority hierarchy:
1. `docs/architectural-axioms.md` (Highest architectural authority)
2. `AGENTS.md` (Repository guardrails and invariants)
3. Applicable accepted Architecture Decision Records (`ADR-001` through `ADR-020`)
4. Applicable requirements and design contracts
5. Existing source code, unit/integration tests, and historical plans/reports (treated as empirical evidence of current/historical state, never as authority that conflicting behaviour is correct)

### Exact Architectural Axioms (AX-01 to AX-13)

The assessment evaluates the repository against the exact normative text and titles established in `docs/architectural-axioms.md`:

| Axiom ID | Exact Repository Title | Core Normative Mandate |
| :--- | :--- | :--- |
| **AX-01** | **Harmonia Is Health-Information Centric** | Harmonia exists to manage, protect, integrate and govern health information. Where health-information semantics and generic infrastructure semantics conflict, health-information semantics prevail. Generic middleware concepts must not displace health data models. |
| **AX-02** | **Standards at the Boundary; Harmonia Within the Boundary** | External interfaces must conform to healthcare interoperability standards. Internal processing, active state, durable storage and orchestration are governed by Harmonia architecture and need not mimic external standards machinery. Pylai acts as the interoperability membrane. |
| **AX-03** | **Native Standards Models Remain Native** | Where Harmonia adopts an external standard model (such as HL7 FHIR R5), the standard model must remain structurally native. Harmonia must not create parallel or derivative representations of native standards models solely to carry management metadata. |
| **AX-04** | **Harmonia Owns the Semantics; Engines Provide the Machinery** | Harmonia defines the meaning, lifecycle, information authority and governance of managed data. Underlying technology engines (Infinispan, PostgreSQL, ActiveMQ Artemis, HAPI FHIR) provide implementation machinery. Harmonia must preferentially use native engine capabilities that satisfy Harmonia semantics. |
| **AX-05** | **Active State and Authoritative Durable State Are Distinct** | Mneme manages active distributed state for high-availability access and coordination. Mnemosyne establishes authoritative durable state. Active cache state is reconstructable and never authoritative. Presentation and application tiers must not treat raw cache access as an alternative persistence path. |
| **AX-06** | **Information Authority Is Explicit** | Every piece of managed information has an explicit Information Authority classification (`AUTHORITATIVE`, `INFORMATIONAL`, `ANECDOTAL`). Authority is preserved across transformations, persists through lifecycle states, and is not created or destroyed by transport mechanisms. |
| **AX-07** | **Security Is Intrinsic to Managed Operations** | Security is not an afterthought or an external wrapper. All operations execute within an established security context. Authorization evaluation (Themis) defaults to deny. Security context is operational context and must not automatically become persisted resource content. |
| **AX-08** | **Evidence Records Meaning, Not Machinery** | Kleio audit evidence and provenance record architecturally significant transitions and clinical meaning, not transient diagnostic machinery. Accepted audit evidence is append-only, permanent, and immutable. |
| **AX-09** | **Transient Operational State Is Ephemeral by Default** | Intermediate processing state, queues, and transient runtime contexts are ephemeral and recoverable. Failure or restart of transient infrastructure must not corrupt durable state or require manual reconstruction of in-flight data. |
| **AX-10** | **Distribution, Load and Failure Are Normal Operating Conditions** | Harmonia is designed for distributed execution across heterogeneous environments. Network degradation, node failures, and high load are normal operating conditions. Failure must be visible rather than silently masked by semantically weaker local fallbacks. |
| **AX-11** | **Managed Information Access Is Highly Available and Responsive** | Presentation and operational access to managed information must be responsive and highly available via Mneme active-state coordination, while maintaining authoritative consistency with Mnemosyne durable state. Latency optimizations must not compromise information integrity. |
| **AX-12** | **Hide Plumbing, Not Information** | Internal implementation mechanisms, transport plumbing, and container orchestration must be encapsulated and hidden behind governed architectural boundaries, while clinical and operational information semantics remain transparent and accessible to authorized consumers. |
| **AX-13** | **Harmonia Management Has an Explicit Boundary** | Ingress into Harmonia establishes governance, security context, and information authority tracking. Egress terminates Harmonia management of the emitted representation. Harmonia does not claim operational governance over external third-party systems post-egress. |

### Classification Taxonomy

Every material finding in the final assessment report will be classified under exactly one of the following five categories:
1. `CONFORMANT`: The implementation, design, or contract is materially consistent with the applicable axioms and normative guardrails.
2. `CONFORMANT — CLARIFICATION REQUIRED`: The implementation is materially consistent, but documentation, subsystem ownership, or architectural intent exhibits ambiguity requiring clarification.
3. `LEGACY / HISTORICAL ONLY`: The artefact reflects an older architecture or historical transition but is not part of the active runtime architecture.
4. `AXIOM CONFLICT`: The implementation, design, ADR, or rule materially contradicts an explicit normative statement or necessary consequence of one or more axioms.
5. `UNRESOLVED ARCHITECTURAL QUESTION`: The axioms establish constraints, but do not provide sufficient criteria to determine the definitive architectural choice between valid alternatives without an explicit architectural decision.

### Semantic Disambiguation Principles

To avoid premature or false conflict classifications, the assessment applies strict semantic evaluation before assessing architectural placement:
- **`FHIR meta.security` vs Operational Security Context:** The assessment distinguishes domain-level confidentiality/security labels intrinsic to health data (e.g., FHIR `meta.security` tags indicating privacy classification) from dynamic, transient operational execution context (e.g., executing principal identity, token credentials, transient roles).
- **FHIR `Provenance` vs Kleio Audit Evidence:** The assessment distinguishes interoperability-facing clinical provenance assertions from immutable, append-only system audit evidence managed by Kleio.
- **`Pragma` vs FHIR `Task`:** The assessment evaluates whether `Pragma` represents an internal workflow execution envelope (carrying runtime orchestration metadata) and FHIR `Task` represents a standardized clinical/interoperability task, rather than assuming they are identical concepts.
- **Internal Private Extensions vs Egress Leakage:** Use of Harmonia-private FHIR extension URIs (`http://fhirfactory.net/harmonia/*`) is permitted internally within the Harmonia boundary; an axiom conflict arises only if private operational extensions are emitted across external egress boundaries without a fail-closed projection membrane.
- **Distinct Version Domains:** `FHIR meta.versionId`, HTTP `ETag`, Mneme active-state tokens, and Mnemosyne authoritative versions are recognized as distinct version domains with separate lifecycle and concurrency semantics.
- **Native Machinery vs Custom Implementations:** Duplication of native platform machinery (e.g. HAPI FHIR JPA) is classified as an `UNRESOLVED ARCHITECTURAL QUESTION` until it is verified whether the native engine satisfies Harmonia's required authoritative-state semantics.

# Candidate Investigation Areas

### Candidate Findings Investigation Plan

The following candidate investigation areas (MAT-01 through MAT-10) will be rigorously examined against exact repository source files, configuration, and normative axiom texts during assessment execution:

#### Candidate MAT-01: Iris BEFE Cache Mutation and Deletion Pathways
- **Target Files:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java`, `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/PractitionerResource.java`
- **Applicable Axioms:** `AX-05` (Active State vs Authoritative Durable State), `AX-10` (Distribution, Load and Failure), `AX-11` (Responsive Access)
- **Investigation Objective:** Verify whether `saveResource(...)` and `deleteResource(...)` invoke raw `remoteCache.put` and `remoteCache.remove` directly against Infinispan, bypassing `Mnemosyne` durable storage and `GovernedWriter`. Evaluate whether state created in BEFE is resilient against cache restarts.
- **Evaluation Criteria:** Determine if this constitutes an `AXIOM CONFLICT` with `AX-05` and `ADR-018`/`ADR-020`.

#### Candidate MAT-02: Iris BEFE In-Memory RemoteCache Scans
- **Target Files:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java` (`searchResources`)
- **Applicable Axioms:** `AX-04` (Semantics vs Machinery), `AX-05` (State Separation), `AX-11` (Responsive Access)
- **Investigation Objective:** Examine `remoteCache.values()` invocations to evaluate whether search requests pull full remote cache datasets across the network into local JVM heap for linear string filtering.
- **Evaluation Criteria:** Evaluate whether this violates `ADR-010` (authoritative-backed search) and engine indexing capabilities (`AX-04`, `AX-11`).

#### Candidate MAT-03: Pylai FHIR Gateway Egress Operational Metadata Projection
- **Target Files:** `pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java`, `calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/PragmaFhirConverter.java`
- **Applicable Axioms:** `AX-02` (Standards at Boundary), `AX-12` (Hide Plumbing), `AX-13` (Explicit Boundary)
- **Investigation Objective:** Determine whether outbound REST endpoints (`getTaskStatus`, `readResource`, `searchResources`) emit internal operational extension URIs (`http://fhirfactory.net/harmonia/task/*`, `praxis-id`, `checkpoint-step`) directly to external clients without a fail-closed sanitization projection membrane.
- **Evaluation Criteria:** Verify whether egress filtering satisfies the fail-closed projection boundary mandated by `AX-02` and `AX-13`.

#### Candidate MAT-04: Security Context Injection and Resource Immutability
- **Target Files:** `calliope/src/main/java/net/fhirfactory/harmonia/model/security/FhirSecurityTagManager.java`, `calliope/src/main/java/net/fhirfactory/harmonia/model/pragma/PragmaFhirConverter.java`
- **Applicable Axioms:** `AX-07` (Security Intrinsic), `AX-06` (Information Authority)
- **Investigation Objective:** Analyze whether transient caller security credentials, principal identifiers, and default confidentiality tags are written directly into persisted FHIR resource bodies (`meta.security`, extensions) prior to storage.
- **Evaluation Criteria:** Disambiguate clinical confidentiality classification from dynamic operational context. Determine if automatic caller-context persistence contradicts `AX-07`.

#### Candidate MAT-05: Direct SQL Database Coupling in Iris BEFE
- **Target Files:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/AuditDataSourceProducer.java`, `iris/iris-befe/pom.xml`
- **Applicable Axioms:** `AX-04` (Engines Provide Machinery), `AX-05` (State Separation), `AX-12` (Hide Plumbing)
- **Investigation Objective:** Check whether `iris-befe` configures a container `@DataSourceDefinition` connecting directly to PostgreSQL (`org.postgresql.ds.PGSimpleDataSource`) to execute JDBC queries via `kleio-persistence`.
- **Evaluation Criteria:** Assess whether direct database connectivity in presentation tiers violates presentation tier decoupling (`AGENTS.md` Invariant 3) and `AX-05`.

#### Candidate MAT-06: Mnemosyne Persistence Architecture & HAPI FHIR JPA Capabilities
- **Target Files:** `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java`, `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/repository/FhirResourceRepository.java`, `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/model/FhirResourceEntity.java`
- **Applicable Axioms:** `AX-04` (Harmonia Owns Semantics; Engines Provide Machinery), `AX-01` (Health-Information Centric), `AX-05` (Authoritative Durable State)
- **Investigation Objective:** Investigate the custom relational table `hie_fhir_resources` storing text JSON blobs with custom version calculation. Assess whether native HAPI FHIR JPA Server (`hapi-fhir-jpaserver-base`) satisfies Harmonia's authoritative-state and multi-model requirements.
- **Evaluation Criteria:** In accordance with classification discipline, evaluate whether this constitutes an `UNRESOLVED ARCHITECTURAL QUESTION` or an `AXIOM CONFLICT`.

#### Candidate MAT-07: Process-Local Fallbacks Under Cache Degradation
- **Target Files:** `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultCommunicationService.java`, `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/service/PraxisService.java`
- **Applicable Axioms:** `AX-10` (Distribution, Load and Failure Are Normal Operating Conditions)
- **Investigation Objective:** Check whether gateway and workflow services catch remote cache exceptions and silently fall back to local `ConcurrentHashMap` memory stores.
- **Evaluation Criteria:** Determine whether silent fallback violates the `AX-10` mandate that failure must be visible rather than silently replaced by semantically weaker local behaviour.

#### Candidate MAT-08: Ingress Security Context Minting & Header Trust
- **Target Files:** `pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java`, `pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/interceptor/FhirSecurityInterceptor.java`
- **Applicable Axioms:** `AX-07` (Security Intrinsic to Managed Operations), `AX-13` (Explicit Boundary)
- **Investigation Objective:** Inspect how `ThemisSecurityContext` is instantiated at Pylai ingress. Verify whether principal identity and authorities are accepted directly from unauthenticated HTTP headers (`X-Requester`, `X-Source-System`).
- **Evaluation Criteria:** Determine whether ingress context minting adheres to trusted boundary authentication and default-deny governance (`AX-07`).

#### Candidate MAT-09: Provenance Lifecycle & Kleio Ownership in MLLP Gateway
- **Target Files:** `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultProvenanceService.java`
- **Applicable Axioms:** `AX-05` (State Separation), `AX-08` (Evidence Records Meaning, Not Machinery)
- **Investigation Objective:** Examine `DefaultProvenanceService` methods for cache-based `Provenance` storage and `delete(...)` / `remoteCache.remove` invocations.
- **Evaluation Criteria:** Disambiguate transient protocol provenance from immutable Kleio evidence. Determine if physical deletion of provenance contradicts `AX-08` and `ADR-013`/`ADR-020`.

#### Candidate MAT-10: Mechanical Architecture Test Coverage and Semantic Fidelity
- **Target Files:** `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/*`
- **Applicable Axioms:** `AX-12` (Hide Plumbing, Not Information), `AX-05`, `AX-13`
- **Investigation Objective:** Review existing ArchUnit suites (`IrisDecouplingArchitectureTest`, `MnemosyneAuthoritativePersistenceArchitectureTest`, `GovernedWriteContractArchitectureTest`, etc.) to verify whether tests enforce semantic invariants or merely check surface package imports/strings.
- **Evaluation Criteria:** Identify gaps where runtime violations bypass static tests, classifying coverage under `CONFORMANT — CLARIFICATION REQUIRED`.

# ADR & Platform Machinery Review

### Architecture Decision Records Assessment Methodology

The assessment will verify all 20 accepted ADRs directly from `docs/architecture-decisions.md` against the Architectural Axioms:

| ADR ID | Exact Repository Title | Axiom Alignment Verification Focus |
| :--- | :--- | :--- |
| **ADR-001** | **Petasos Owns Messaging Runtime** | Verify Petasos encapsulation of ActiveMQ Artemis and Ponos workflow independence (`AX-04`, `AX-09`). |
| **ADR-002** | **Pylai Owns External Interface and Protocol Behaviour** | Verify protocol adapter boundary isolation and domain logic exclusion (`AX-02`, `AX-13`). |
| **ADR-003** | **Mnemosyne Owns Durable Application State** | Verify authoritative durable persistence vs non-authoritative active cache state (`AX-05`). |
| **ADR-004** | **Calliope Owns Canonical Models and Semantic Governance** | Verify canonical schema governance and runtime engine independence (`AX-01`, `AX-03`). |
| **ADR-005** | **Themis Owns Security Policy Decisions** | Verify centralized default-deny authorization logic and trusted authentication boundaries (`AX-07`). |
| **ADR-006** | **FHIR R5 Is the Clinical Interoperability Boundary** | Verify standard model retention and translation of proprietary source models (`AX-01`, `AX-02`, `AX-03`). |
| **ADR-007** | **Harmonia Is an Interoperability Authority, Not Universal Clinical Truth** | Verify source authority preservation and provenance tracking (`AX-01`, `AX-06`). |
| **ADR-008** | **Iris-Clinical Begins as a Longitudinal Record Viewer** | Verify read-oriented viewer scope against active write/delete endpoints in BEFE (`AX-05`, `AX-11`). |
| **ADR-009** | **Information Authority Is Cross-Cutting** | Verify cross-cutting modeling of `AUTHORITATIVE`, `INFORMATIONAL`, and `ANECDOTAL` authority (`AX-06`). |
| **ADR-010** | **Clinical Search Must Be Authoritative-Backed** | Verify that search operations execute against durable state rather than transient cache memory (`AX-05`, `AX-11`). |
| **ADR-011** | **Agora Uses Restricted Self-Hosted Matrix/Synapse** | Verify Matrix protocol encapsulation and non-federated room governance (`AX-04`, `AX-10`). |
| **ADR-012** | **Paradeigma Is an Isolated Exemplar/Test Subsystem** | Verify total quarantine of simulation models and test harnesses from production artifacts (`AX-04`, `AX-12`). |
| **ADR-013** | **Kleio Owns Audit Evidence and Provenance** | Verify append-only immutability of audit records and provenance evidence (`AX-08`). |
| **ADR-014** | **Petasos Owns Durable Processing Transition Boundaries** | Verify durable transfer of work across independent processing stages (`AX-04`, `AX-09`). |
| **ADR-015** | **Replay Is Anchored to Explicit Durable Transitions** | Verify that replay creates new traceable attempts linked to Petasos transitions (`AX-08`, `AX-09`). |
| **ADR-016** | **Audit Is Anchored to Architecturally Significant Transitions** | Verify audit event generation filtering to significant boundary transitions (`AX-08`). |
| **ADR-017** | **Processing Pressure Is Expressed as Durable Backlog** | Verify backlog buffering in Artemis queues rather than JVM heap memory (`AX-09`, `AX-11`). |
| **ADR-018** | **Mnemosyne Defines the Authoritative Durable State Boundary** | Verify explicit persistence port governance and rejection of cache-store persistence (`AX-05`). |
| **ADR-019** | **Mneme Owns Distributed Resource Access and Coordination** | Verify active-state coordination, reconstructability, and Hot Rod near-caching (`AX-05`, `AX-11`). |
| **ADR-020** | **Governed Information Uses Lifecycle State Rather Than Physical Deletion** | Verify that persistence ports forbid physical deletion in favour of domain lifecycle progression (`AX-05`, `AX-08`). |

### Native Platform Machinery Evaluation Methodology

The assessment will evaluate technology engine integration across three dimensions:
1. **Appropriate Native Machinery Utilization:** Confirm where native engine capabilities (e.g., ActiveMQ Artemis persistent queues, Infinispan Hot Rod versioned metadata, HAPI FHIR R5 Core parsers, PostgreSQL append-only locking) correctly establish required Harmonia semantics.
2. **Duplication of Engine Capabilities:** Analyze where Harmonia implementations hand-roll machinery already provided by underlying platforms (e.g., custom relational JSON table vs HAPI FHIR JPA storage; in-memory cache filtering vs native index queries).
3. **Semantic Invariant Validity:** Identify where Harmonia relies on platform behaviours that fail to guarantee claimed invariants (e.g., assuming Infinispan cache entries are durable without persistence; assuming `ConcurrentHashMap` provides distributed resilience).

# Mechanical Enforcement & Review Order

### Mechanical Architecture Enforcement Evaluation

The assessment will examine the existing ArchUnit test suite in `paradeigma/paradeigma-test` to determine:
- **Protected Invariants:** Verify which axiom consequences are effectively enforced (e.g., `ParadeigmaIsolationArchitectureTest`, `PetasosApiIsolationArchitectureTest`, `AgoraIsolationArchitectureTest`, `PackageLayeringArchitectureTest`, `GovernedWriteContractArchitectureTest`).
- **Unprotected Invariants:** Identify high-risk architectural rules lacking automated enforcement (e.g., direct `RemoteCache` access in presentation modules, egress metadata leakage, string-based DataSource definitions, silent fallback stores).
- **Test Alignment:** Detect tests that enforce historical or superficial structural conventions rather than current axiom invariants.

### Unresolved Architectural Questions to Investigate

The assessment will investigate the following foundational questions requiring architectural determination:
1. **Mnemosyne Storage Engine Strategy:** Does HAPI FHIR JPA Server (`hapi-fhir-jpaserver-base`) satisfy Harmonia's multi-model and authoritative-state semantics, or is a custom relational storage architecture required?
2. **Kleio Audit Table Separation:** Does sharing the physical database table `hie_fhir_resources` between mutable clinical resources and immutable audit events compromise durable separation of concerns?
3. **Pylai Ingress Authentication & Context Minting:** What standard authentication mechanism (e.g., mTLS, OAuth2/OIDC JWT validation, API Gateway header signature) must be established at Pylai ingress to mint immutable `ThemisSecurityContext` instances?
4. **Pragma Domain Model vs Native FHIR Task:** Does `Pragma` represent an internal workflow orchestration envelope distinct from FHIR `Task`, or should Harmonia standardize directly on HAPI FHIR `Task` with private extensions?

### Prioritized Review Order

The assessment findings will be reviewed in the following prioritized sequence:
1. **Priority 1: External Interoperability Boundaries & Egress Sanitization (Candidate MAT-03, MAT-08)**
   - Protects healthcare standards compliance, security boundary integrity, and prevents internal operational leakage.
2. **Priority 2: Active State vs Authoritative Durable State Separation (Candidate MAT-01, MAT-02, MAT-05, MAT-09)**
   - Resolves direct cache mutation, unindexed cluster scans, and presentation tier database coupling.
3. **Priority 3: Security Context & Immutability Governance (Candidate MAT-04)**
   - Ensures transient operational security context is decoupled from persisted clinical resources.
4. **Priority 4: Distribution Resilience & Visible Failure (Candidate MAT-07)**
   - Eliminates silent in-memory fallbacks to prevent split-brain states under failure.
5. **Priority 5: Persistence Engine Strategy & Native Machinery (Candidate MAT-06, Question 1)**
   - Clarifies HAPI FHIR JPA adoption vs governed relational persistence.
6. **Priority 6: Mechanical Enforcement Suite Expansion (Candidate MAT-10)**
   - Establishes automated ArchUnit rules to prevent future architectural regression.

# Delivery Steps

### ✓ Step 1: Evaluate Ingress/Egress Boundaries, Operational Metadata Containment, and Security Context Propagation
Investigate and document evidence regarding Pylai gateway boundaries, egress projection filtering, and security context minting.

- Inspect `pylai-fhir-registry` and `calliope` (`PragmaFhirConverter`, `FhirRestGatewayController`) to evaluate whether private operational extension URIs are exposed across egress boundaries (`AX-02`, `AX-12`, `AX-13`, Candidate MAT-03).
- Inspect Pylai HTTP interceptors and controllers to evaluate `ThemisSecurityContext` minting from incoming request headers (`AX-07`, `AX-13`, Candidate MAT-08).
- Analyze `FhirSecurityTagManager` and resource persistence flows to evaluate the boundary between transient security context and persisted clinical data (`AX-07`, Candidate MAT-04).

### ✓ Step 2: Evaluate State Separation, Active Cache Usage, and Presentation Tier Decoupling
Investigate and document evidence regarding Mneme active state, Mnemosyne durable state, and presentation tier decoupling.

- Inspect `iris-befe` (`FhirCacheService`, `PractitionerResource`) for raw Infinispan `remoteCache.put` and `remoteCache.remove` invocations bypassing Mnemosyne (`AX-05`, `AX-10`, `AX-11`, Candidate MAT-01).
- Analyze `FhirCacheService.searchResources` cluster-wide value scanning against authoritative-backed search requirements (`AX-04`, `AX-05`, `AX-11`, `ADR-010`, Candidate MAT-02).
- Inspect `AuditDataSourceProducer` and `iris-befe/pom.xml` for direct PostgreSQL DataSource definitions and JDBC dependencies (`AX-05`, `AX-12`, Candidate MAT-05).
- Inspect `pylai-mllp-base` (`DefaultProvenanceService`) for cache-based provenance mutations and physical deletions (`AX-05`, `AX-08`, Candidate MAT-09).

### ✓ Step 3: Evaluate Persistence Engines, Native Platform Machinery, and Workflow Lifecycle Semantics
Investigate and document evidence regarding Mnemosyne persistence, platform machinery utilization, and error handling.

- Inspect `hestia/mnemosyne-clinical` (`AuthoritativePersistenceService`, `FhirResourceRepository`, `FhirResourceEntity`) to assess custom relational storage vs native HAPI FHIR JPA Server capabilities (`AX-04`, `AX-01`, Candidate MAT-06).
- Inspect `DefaultCommunicationService` and `PraxisService` for silent `ConcurrentHashMap` fallback behaviour during cache degradation (`AX-10`, Candidate MAT-07).
- Review native engine usage across Artemis (`Petasos`), Infinispan (`Hestia/Mneme`), and PostgreSQL (`Kleio`, `Mnemosyne`) against claimed semantic invariants.

### ✓ Step 4: Evaluate Mechanical Architecture Tests, ADR Consistency, and Synthesize Final Conformance Report
Verify all 20 ADRs, assess ArchUnit test coverage, and produce the comprehensive architectural conformance report.

- Validate ADR-001 through ADR-020 directly against `docs/architecture-decisions.md` and assess consistency with exact axioms AX-01 to AX-13.
- Evaluate ArchUnit tests in `paradeigma/paradeigma-test` for semantic invariant coverage and identify missing mechanical guardrails (`AX-12`, Candidate MAT-10).
- Synthesize all collected evidence into the finalized Architectural Conformance Report following the required structure (Executive Summary, Conformance Matrix, Material Findings, ADR Assessment, Native Machinery Assessment, Mechanical Enforcement, Unresolved Questions, Review Order).