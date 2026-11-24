---
sessionId: session-261023-191640-1n0i
---

# Requirements

### Overview & Goals
The goal of this initiative is to initiate implementation-oriented development for Harmonia Data Governance Services by executing the first real-world cycle of the established Harmonia Architecture / Code Governance Lifecycle (Code Round-Trip). The primary utility of this effort is to maximize overall system reliability, eliminate architectural drift, and ensure that health information operations adhere strictly to canonical governance principles without wasting engineering effort on premature redesigns or speculative code rewrites.

By rigorously reconciling the current Enterprise Architecture baseline (RAD-1.1.13) against the substantial existing Java implementation across Mneme, Calliope, Mnemosyne, and Pylai, this work produces an authoritative starting point and decision report for the Solution Architect to commission the first bounded `solution build` increment.

### Scope
- **In Scope**:
  - Baseline confirmation of Git branch `Data-Governence-Services` and EA release 1.1.13.
  - Identification and traversal of authoritative governance contracts (`enterprise-architecture/governance/`).
  - Deep inspection and structural mapping of Data Governance Continuum elements (Mneme, Mnemosyne, Calliope, Pylai, Managed Information, State, Concurrency, Persistence).
  - Deep inspection of existing Java implementation across `hestia/`, `calliope/`, `pylai/`, and related modules.
  - Semantic reconciliation of durable implementation concepts against architectural responsibilities.
  - Identification of naming evolutions, unrepresented implementation concepts, missing architectural implementations, and genuine conflicts.
  - Formulation of formal questions requiring Solution Architect disposition.
  - Production of the formal Data Governance Reconciliation Report under `.junie/reports/` proposing candidates for the first bounded solution build increment.
- **Out of Scope**:
  - Direct modification or refactoring of existing Java production code.
  - Direct mutation of canonical Continuum element files or ArchiMate workbench models.
  - Redesign of Digital Twin or Ponos execution semantics.
  - Implementation of the first solution build increment prior to explicit Solution Architect authorization.
  - Creation of new governance praxes, documentation engines, or automation tooling.

### User Stories
- **As the Solution Architect**, I want an objective, evidence-based reconciliation between the RAD-1.1.13 Enterprise Architecture and the existing Java codebase, so that I can make authoritative design dispositions and authorize a safe, high-utility first build increment.
- **As a Harmonia Core Developer**, I want clear mappings between architectural concepts (such as Context Management, Active State Hegemony, and Authoritative Progression) and concrete Java interfaces, so that subsequent implementation strictly conforms to platform invariants.
- **As a System Integrator**, I want explicit boundaries between transient active cache state and durable authoritative persistence, so that clinical data integrity and provenance are safeguarded across distributed workflows.

### Functional Requirements
- **FR-1 Baseline & Governance Verification**: Establish and record the verified branch state, clean working tree status, and architecture release baseline (1.1.13).
- **FR-2 Architectural Authority Extraction**: Extract and synthesize all Data Governance responsibilities and constraints from `enterprise-architecture/continuum/` and governing axioms (AX-01 through AX-17).
- **FR-3 Implementation Mapping**: Survey and classify Java interfaces, classes, and packages across `calliope`, `hestia/mneme-*`, `hestia/mnemosyne-*`, and `pylai` against continuum responsibilities.
- **FR-4 Taxonomy & Evolution Documentation**: Document the naming evolution and structural transitions (e.g. from historical facades to current `GovernedReader`/`GovernedWriter`/`ActiveStateCoordinator` patterns).
- **FR-5 Conflict & Ambiguity Cataloguing**: Highlight genuine architectural gaps, conceptual discrepancies, and invariant tensions requiring Solution Architect review.
- **FR-6 First Build Increment Recommendations**: Propose prioritized, bounded candidate increments aligned with the master convergence roadmap (`docs/implementation/harmonia-convergence-runtime-integration-plan.md`).
- **FR-7 Formal Report Delivery**: Author and deliver a comprehensive Markdown report under `.junie/reports/`.

### Non-Functional Requirements
- **NFR-1 Non-Destructive Operation**: No production source code, configuration, or architectural continuum files may be modified or deleted during reconciliation.
- **NFR-2 Provenance & Auditability**: All findings must cite exact file paths, line ranges, canonical IDs, and commit revisions in accordance with Architecture Change Provenance.
- **NFR-3 Utilitarian Soundness**: Analysis must prioritize durable architectural invariants and high-consequence patient safety boundaries (e.g. Invariant 8: Mneme/Mnemosyne separation, Invariant 6: default-deny security, Invariant 7: zero-PHI diagnostic logging).

# Technical Design

### Current Implementation
Harmonia contains a rich existing implementation foundation spanning multiple years of evolution:
- **Calliope (`calliope/`)**: Contains pure domain models and governed access contracts (`net.fhirfactory.harmonia.model.governedwrite.*`), including `GovernedAccess`, `GovernedReader`, `GovernedWriter`, `ResourceKey`, `ActiveStateToken`, `AuthoritativeVersion`, `WriteResult`, and `ActiveStateConflict`.
- **Hestia / Mneme (`hestia/mneme-cluster`, `hestia/mneme-persistence`)**: Implements active distributed state management, HotRod/Infinispan coordination (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`), active hegemon tracking, and REST-based cache stores (`FhirRestCacheStore`).
- **Hestia / Mnemosyne (`hestia/mnemosyne-api`, `hestia/mnemosyne-clinical`, `hestia/mnemosyne-operations`)**: Implements authoritative persistence ports (`AuthoritativePersistencePort`), JPA adapters over HAPI FHIR JPA (`HapiJpaAuthoritativePersistenceAdapter`), and entity persistence services (`FhirStorageService`).
- **Pylai (`pylai/`)**: Establishes standards-compliant ingress and egress gateways (FHIR REST, MLLP) enforcing the external publication boundary.
- **Enterprise Architecture (`enterprise-architecture/continuum/`)**: Baseline 1.1.13 defines canonical element files for Mneme (`1af8a56358f7429fabfb57be66542a99`), Mnemosyne (`7985e647bf9e4ca3ba70faf9e3e4ae02`), Calliope-Models (`307b3440a507403f8bc214d290bffb6a`), and Calliope-DataSets (`53bf6815c7a54ed0abde9b1aad311843`).

### Key Decisions
- **Decision 1: Follow Architecture Review Praxis / Code Round-Trip as Established**: Rather than inventing new governance machinery, the reconciliation strictly applies `enterprise-architecture/governance/architecture-review-praxis.md` and `architecture-governance.md`.
  - *Rationale*: Reusing validated governance paths maximizes efficiency, minimizes process overhead, and preserves continuous traceability.
- **Decision 2: Principle of Semantic Continuity Over Lexical Equality**: Classes and continuum elements are evaluated based on durable operational responsibilities rather than exact 1:1 name matches.
  - *Rationale*: Subsystems have evolved lexically (e.g. Context Load/Save vs `GovernedReader`/`GovernedWriter`), but the core responsibility boundaries remain constant.
- **Decision 3: Zero-Mutation Investigation Phase**: Absolutely no modifications to production Java code or Continuum element files will be performed during this planning and reconciliation step.
  - *Rationale*: Prevents accidental regressions or preemptive architectural commitments before the Solution Architect reviews the findings and authorizes the first build increment.
- **Decision 4: Strict Separation of Version Domains**: Adhere to AX-05 and the Convergence Plan by treating active tokens (Infinispan), HTTP ETags, FHIR `meta.versionId`, and Mnemosyne `AuthoritativeVersion` as distinct version spaces.
  - *Rationale*: Eliminates race conditions and prevents cache state from being falsely treated as durable truth.

### Architecture Diagram
```mermaid
graph TD
    subgraph Pylai [Pylai Gateway Membrane]
        Ingress[Inbound MLLP / REST Gateways]
        Egress[Outbound Gateways - AX-13 Boundary]
    end

    subgraph Calliope [Calliope Domain & Governed Contracts]
        GovContracts[GovernedReader / GovernedWriter / ResourceKey]
        PureModels[Domain Models / Pragma / Topic]
    end

    subgraph Mneme [Mneme Active State Tier]
        ActiveCoord[ActiveStateCoordinator / Hegemon]
        Infinispan[Infinispan Active Cache / Token]
        ContextMgr[Context Load / Save / Adhoc Access]
    end

    subgraph Themis [Themis Security Engine]
        Authz[Default-Deny Policy Enforcement - AX-06]
    end

    subgraph Mnemosyne [Mnemosyne Authoritative Tier]
        AuthPort[AuthoritativePersistencePort]
        HapiJpa[HAPI FHIR JPA Adapter]
        Postgres[(PostgreSQL Durable Store - AX-05)]
    end

    Ingress -->|Governed Operation| GovContracts
    GovContracts -->|Active State Check| Mneme
    Mneme -->|Evaluate Context| Themis
    Mneme -->|Authoritative Progression N to N+1| AuthPort
    AuthPort --> HapiJpa
    HapiJpa --> Postgres
    AuthPort -->|Convergence Notice| Mneme
    GovContracts -->|Filtered Output| Egress
```

### Data Models / Contracts
The primary contracts governing the Data Governance Services interaction are:
1. **Governed Read & Write Contract (`calliope`)**:
   - `GovernedReader.read(ResourceKey key, ThemisSecurityContext ctx): GovernedRead<T>`
   - `GovernedWriter.write(ResourceKey key, T resource, ExpectedAuthoritativeVersion expected, ActiveStateToken token): WriteResult<T>`
2. **Active State Coordination Contract (`calliope` / `hestia`)**:
   - `ActiveStateCoordinator.acquireHegemony(ResourceKey key): ActiveStateCoordinationResult`
   - `ActiveStateToken`: Ephemeral lock and coordination token held during in-flight mutations.
3. **Authoritative Persistence Contract (`mnemosyne-api`)**:
   - `AuthoritativePersistencePort.createIfAbsent(ResourceKey key, IBaseResource resource): AuthoritativePersistenceResult`
   - `AuthoritativePersistencePort.updateIfExpectedPredecessor(ResourceKey key, IBaseResource resource, AuthoritativeVersion expected): AuthoritativePersistenceResult`
   - Version progression: Enforces strictly monotonically increasing version integers $N \rightarrow N+1$.
4. **Convergence Port (`calliope` / `hestia`)**:
   - `ActiveStateConvergencePort.converge(ResourceKey key, AuthoritativeVersion committedVersion, IBaseResource resource): ConvergenceStatus`

### Risks & Mitigations
- **Risk**: Lexical discrepancy between Continuum "Four-Service Surface" (Context Load, Context Save, Adhoc Resource Access, Adhoc Search) and Java `GovernedReader`/`GovernedWriter`.
  - *Mitigation*: Explicitly document the mapping in the reconciliation report and present it for Solution Architect disposition.
- **Risk**: Misinterpreting HAPI FHIR JPA storage mechanisms as direct application-facing APIs (violating Invariant 8 / AX-05).
  - *Mitigation*: Re-assert that all application access must route through Mneme governed facades, isolating JPA internals to Mnemosyne.
- **Risk**: Inadvertently introducing physical DELETE semantics into FHIR data governance.
  - *Mitigation*: Strictly enforce the invariant that lifecycle transitions are recorded as authoritative status progressions, never database purges.

# Testing

### Validation Approach
Because this activity is strictly an architectural investigation and reconciliation task, validation consists of:
1. **Repository & Governance Conformance Verification**: Ensuring that Git baseline, branch state, and architecture release JSON match official baselines without uncommitted residue.
2. **ArchUnit & Architectural Test Verification**: Executing existing architecture test suites to verify that baseline invariants (e.g. `ParadeigmaIsolationArchitectureTest`, `PetasosApiIsolationArchitectureTest`, `IrisDecouplingArchitectureTest`) remain 100% green.
3. **Traceability & Provenance Audit**: Cross-verifying all citations, canonical element UUIDs, and class references between the report and the actual codebase.
4. **Boundary & Invariant Conformance Checks**: Confirming that all analysis adheres to Architectural Axioms AX-01 through AX-17 and Invariants 1 through 10.

### Key Scenarios
- **Scenario 1: Full Architecture Suite Execution**:
  - Run `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` to establish that existing production code obeys structural boundaries prior to any build increment.
- **Scenario 2: Continuum Responsibility Coverage Audit**:
  - Verify that every core Data Governance capability in the Continuum has an identified implementation counterpart, an identified evolutionary path, or is flagged as pending disposition.
- **Scenario 3: Master Convergence Plan Alignment**:
  - Verify that proposed candidates for the first `solution build` increment correspond directly to Milestone M2 (Distributed Authoritative Path) or Milestone M3 (Governed Access) in `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.

### Edge Cases
- **Unmapped Active-State Edge Cases**: Concurrency conflict scenarios where active hegemon acquisition fails or times out while PostgreSQL transactions are pending.
- **Version Skew Scenarios**: Divergence between Infinispan cache generation numbers and Mnemosyne PostgreSQL sequential version IDs.
- **Failure-Closed Ingress**: Behavior when downstream Petasos or Mnemosyne is unavailable during inbound gateway ingestion (asserting AE NACK over MLLP per REC-001).

# Delivery Steps

### ✓ Step 1: Establish Repository Baseline and Continuum Authority Scope
The baseline repository status, branch configuration, and Architecture Continuum authority are confirmed and recorded.

- Inspect and verify Git branch `Data-Governence-Services` and confirm a clean working tree against `architecture-release.json` (version 1.1.13).
- Review `enterprise-architecture/governance/architecture-review-praxis.md`, `architecture-governance.md`, `architecture-change-provenance.md`, and `architectural-axioms.md` to establish the authoritative Code Round-Trip procedure.
- Extract the canonical Data Governance continuum elements for Mneme (`1af8a56358f7429fabfb57be66542a99`), Mnemosyne (`7985e647bf9e4ca3ba70faf9e3e4ae02`), Calliope (`466ad8f75a954c0581b5bba090443849`, `307b3440a507403f8bc214d290bffb6a`, `53bf6815c7a54ed0abde9b1aad311843`), and Pylai (`e3e4147fea3640198dc251ed41ee854d`).
- Record baseline governance constraints and formal task issuance metadata under the governance provenance model.

### ✓ Step 2: Audit Existing Implementation and Reconcile Responsibilities
The persistent Java classes, interfaces, and module structures across Calliope, Mneme, Mnemosyne, and Pylai are systematically audited against Continuum responsibilities.

- Examine `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/` (`GovernedAccess`, `GovernedReader`, `GovernedWriter`, `ActiveStateToken`, `AuthoritativeVersion`, `ResourceKey`, `WriteResult`, `ActiveStateConflict`).
- Examine `hestia/mneme-cluster/` and `hestia/mneme-persistence/` (`DefaultGovernedReader`, `DefaultGovernedWriter`, `HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `FhirRestCacheStore`).
- Examine `hestia/mnemosyne-api/` and `hestia/mnemosyne-clinical/` (`AuthoritativePersistencePort`, `AuthoritativePersistenceService`, `HapiJpaAuthoritativePersistenceAdapter`, `FhirStorageService`).
- Examine `pylai/` (`pylai-fhir-registry`, `pylai-mllp-in`, `pylai-mllp-out`) for standards-facing ingress/egress boundaries and publication filtering.
- Classify discovered implementations into direct alignment, semantic alignment under evolved naming, unrepresented implementation concepts, and unmapped architectural concepts.

### ✓ Step 3: Identify Discrepancies and Formulate Solution Architect Dispositions
Genuine architectural discrepancies, naming evolutions, and deferred questions are formulated as structured decision items for the Solution Architect.

- Identify semantic mismatches between continuum definitions (e.g. Mneme's 4-service surface: Context Load, Context Save, Adhoc Resource Access, Adhoc Search) and current Java interfaces (`GovernedReader`/`GovernedWriter`/`ActiveStateCoordinator`).
- Analyse lifecycle distinctions between active tokens, HTTP ETags, FHIR `meta.versionId`, and Mnemosyne `AuthoritativeVersion`.
- Evaluate the persistence boundary invariants (absence of physical DELETE, fail-closed security context, strictly non-authoritative cache state).
- Formulate specific, bounded decision points for the Solution Architect with concrete architectural evidence and trade-off analysis.

### ✓ Step 4: Author Data Governance Reconciliation Report and First Build Increment Proposal
A comprehensive reconciliation report and candidate first solution build increment are authored and placed under `.junie/reports/`.

- Compile the structured report containing baseline confirmation, governance process references, architectural sources, examined Java components, persistent concept maps, naming evolutions, identified conflicts, and SA disposition requests.
- Formulate advisory recommendations for the first bounded `solution build` increment aligned with Milestone M2/M3 of `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.
- Validate the report against Architecture Change Provenance standards and ensure zero unrequested modifications were made to production source code or Continuum elements.