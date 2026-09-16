---
sessionId: session-260916-090505-7sgs
---

# Requirements

### Overview & Goals

The objective of this initiative is to update the master LaTeX ArchiMate 3.2 architecture specification (`docs/latex/`) for the **Harmonia Health Information Exchange (HIE)** platform to comprehensively document all new modules, components, services, and functions introduced across the system—specifically the **FHIR R5 Provider Registry Core Services** suite—and to create a dedicated, publication-grade technical **Appendix** detailing its full functionality.

The documentation updates will reflect all newly established platform elements across the platform's 5-tier architecture:
1. **Gateway Tier (`Pylai`)**: The new `pylai-fhir-registry` module exposing synchronous FHIR R5 read/search REST endpoints, asynchronous governed write APIs (`POST`/`PUT` returning `HTTP 202 Accepted` with `Location: /Task/{id}`), dynamic `CapabilityStatement` (`GET /metadata`), FHIR `Task` status polling (`GET /Task/{id}`), and fine-grained RBAC security interceptors.
2. **Canonical Models (`Calliope`)**: The `ProviderRegistryChangePragma` change wrapper and factory, `ProviderRegistryConstants` (topics, queues, metadata keys), and canonical validation error codes (`PR-VAL-001` through `PR-VAL-010`).
3. **Workflow & Processing Tier (`Energeia`)**: The 7 dedicated per-resource Ergon activities (`PractitionerChangeErgon`, `PractitionerRoleChangeErgon`, `OrganizationChangeErgon`, `LocationChangeErgon`, `HealthcareServiceChangeErgon`, `EndpointChangeErgon`, `GroupChangeErgon`), the Praxis sequence `seq-provider-registry-change-pipeline` via `ProviderRegistrySequenceBuilder` and `TaskSequenceDefaultSeeder`, and Artemis queue routing in `Ponos`.
4. **Data & Persistence Tier (`Hestia / Mnemosyne-Clinical`)**: `EndpointResourceProvider`, multi-parameter search filtering engine in `FhirStorageService`, deep referential integrity verification via `ProviderRegistryReferenceValidator`, and version-aware optimistic concurrency control (`If-Match`/`ETag`).
5. **Simulation & Testbed (`Paradeigma`)**: `SyntheticProviderRegistryGenerator` for realistic Australian/international provider graphs, end-to-end asynchronous write lifecycle test harnesses, referential rejection tests, and crash-recovery suites.

### Scope

#### In Scope
- **Chapter Updates (`00-frontmatter.tex` through `06-deployment-ha.tex`)**:
  - `00-frontmatter.tex`: Update Executive Summary, ArchiMate structure, system nomenclature table with `pylai-fhir-registry` and Provider Registry services, and introduce Appendix E.
  - `01-motivation-strategy.tex`: Add Provider Registry motivation, business drivers, national healthcare directory interoperability requirements, and strategic capabilities.
  - `02-business-layer.tex`: Add Provider Registry business actors (Clinicians, Directory Stewards, Credentialing Authorities, External Systems) and business process flows (governed change ingestion, referential verification, automated approval).
  - `03-application-layer.tex`: Update component landscape and detailed section specifications for `pylai-fhir-registry`, Calliope canonical models, Energeia Erga/Praxis/Ponos workflow pipeline, and Mnemosyne persistence.
  - `04-data-architecture.tex`: Add `Endpoint` and full Provider Registry resource schemas to Table 4.2 (15 FHIR R5 resources), document `ProviderRegistryChangePragma`, validation error codes (`PR-VAL-001` to `PR-VAL-010`), and hybrid relational-document database schema (`hie_fhir_resources`).
  - `05-technology-layer.tex`: Update technology nodes and master platform port allocation matrix (Table 5.1).
  - `06-deployment-ha.tex`: Document deployment topology, high-availability replication, and failover pathways for Provider Registry services.
- **Dedicated Appendix E (`docs/latex/chapters/appendix-provider-registry.tex`)**:
  - Author a comprehensive, publication-grade technical guide covering architectural topology, resource relationship model, REST API contracts, asynchronous change processing lifecycle, Ergon validation activities, referential integrity engine, optimistic locking, security rules, and Paradeigma test suites.
- **Master Document Registration (`docs/latex/main.tex`)**:
  - Register `\input{chapters/appendix-provider-registry.tex}` under the appendices section.

#### Out of Scope
- Direct modifications to Java source code or Maven build configurations (focus is strictly on architecture documentation and LaTeX synchronization).
- Modifying UI code or frontend components (frontend provider management UI is deferred to subsequent release).

### User Stories

- **As a Systems Architect / Lead Engineer**, I want the ArchiMate 3.2 documentation to accurately reflect all 27+ Maven modules, REST endpoints, and workflow activities so that the technical specification serves as an authoritative single source of truth for the platform.
- **As an Integration Engineer / Client Developer**, I want a dedicated Provider Registry Appendix detailing exact HTTP verbs, headers, status codes, search parameters, FHIR Task status mapping, and OperationOutcome error codes so that I can integrate external hospital and clinic systems with Harmonia seamlessly.
- **As a System Administrator / DevOps Engineer**, I want the deployment, high-availability, port allocation, and failure-recovery mechanisms for the Provider Registry to be fully documented in the LaTeX manual so that I can deploy and operate the cluster reliably.

### Functional Requirements

1. **ArchiMate 3.2 Standard Compliance**: All newly added sections, tables, listings, and text must adhere strictly to ArchiMate 3.2 architectural concepts, layer definitions, and formatting styles established in `styles/harmonia-doc.sty` and `styles/harmonia-archimate.sty`.
2. **Comprehensive Module Coverage**:
   - `Calliope`: Document canonical change pragma models, metadata contracts, and validation codes (`PR-VAL-001` through `PR-VAL-010`).
   - `Pylai`: Document `pylai-fhir-registry` architecture, `FhirRestGatewayController`, `CapabilityStatementProvider`, `ProviderRegistryTaskResourceProvider`, `FhirSecurityInterceptor`, and `ChangeRequestSubmissionService`.
   - `Energeia`: Document the 7 dedicated per-resource Ergon activities, `ProviderRegistrySequenceBuilder`, and Artemis change queue routing in Ponos.
   - `Hestia / Mnemosyne`: Document `EndpointResourceProvider`, multi-parameter search filtering in `FhirStorageService`, `ProviderRegistryReferenceValidator`, and optimistic locking.
   - `Paradeigma`: Document synthetic provider generation, graph modeling, and end-to-end integration test suites.
3. **Appendix E Specification Structure**:
   - Section E.1: Overview & Engineering Objectives.
   - Section E.2: Architectural Topology & 5-Tier Interaction Model (Synchronous Read/Search vs Asynchronous Governed Writes).
   - Section E.3: FHIR R5 Provider Registry Resource Model & Relationship Topology.
   - Section E.4: REST API Gateway Contract & Interaction Matrix.
   - Section E.5: Governed Change Processing Lifecycle & Pragma State Machine.
   - Section E.6: Per-Resource Ergon Processing Activities & Referential Integrity Engine.
   - Section E.7: Authoritative Persistence, Optimistic Concurrency & Versioning Model.
   - Section E.8: Security, RBAC & Resource-Level Permission Architecture.
   - Section E.9: Paradeigma Exemplar Generation & Verification Testbed.
4. **Valid LaTeX Compilation**: Ensure all LaTeX source files compile cleanly without syntax errors, missing packages, broken cross-references, or undefined labels.

### Non-Functional Requirements

- **Accuracy & Traceability**: All class names, method signatures, REST endpoints, queue names, error codes, and port allocations documented in LaTeX must match the actual Java/Spring Boot/Camel codebase exactly.
- **Formatting Consistency**: Match the typography, listings formatting (`\begin{lstlisting}`), tables (`tabularx`), and cross-referencing conventions of existing chapters and appendices.

# Technical Design

### Current Implementation

- **LaTeX Master Document (`docs/latex/main.tex`)**: Defines document structure, includes custom packages `styles/harmonia-doc` and `styles/harmonia-archimate`, sets up frontmatter, chapters 01--06, and appendices A--D (`appendix-paradeigma.tex`, `appendix-mllp-services.tex`, `appendix-ergon-module.tex`, `appendix-praxis-workflow.tex`).
- **Existing Chapters**:
  - `00-frontmatter.tex`: Executive summary, ArchiMate foundation, and system terminology table.
  - `01-motivation-strategy.tex`: Stakeholders, drivers, principles, and strategic goals.
  - `02-business-layer.tex`: Business actors, clinical ingestion services, business processes.
  - `03-application-layer.tex`: 5-tier component landscape, Iris, Pylai (MLLP only), Petasos, Energeia, Hestia, Calliope.
  - `04-data-architecture.tex`: Pragma models, MLLP models, 5-phase checkpoints, Petasos envelope, 14 FHIR R5 resources (lacks `Endpoint` and Provider Registry pragma/validation specifics).
  - `05-technology-layer.tex`: System runtimes, Infinispan cache grid, port allocation table.
  - `06-deployment-ha.tex`: Clustered replication, failover, disaster recovery.

### Key Decisions

1. **Author Dedicated Appendix E (`appendix-provider-registry.tex`)**: Create a comprehensive, standalone implementation and functional specification appendix matching the style and depth of Appendices B, C, and D.
   - *Rationale*: Appendix B covers MLLP Gateways, Appendix C covers Ergon Activity Modules, and Appendix D covers Praxis Workflows. Adding Appendix E for the FHIR Provider Registry Core Services completes the architectural documentation suite with complete technical depth.
2. **Synchronize Chapters 00 through 06**: Update all core chapters to seamlessly weave in Provider Registry capabilities across all ArchiMate layers (Motivation, Business, Application, Data, Technology, Deployment).
   - *Rationale*: Guarantees that readers of the high-level architecture specification get a holistic view of the entire platform including the new Provider Registry subsystem, while deep implementation details are available in Appendix E.
3. **Exact Codebase Symbol Grounding**: Reference concrete Java classes (`FhirRestGatewayController`, `ProviderRegistryReferenceValidator`, `PractitionerChangeErgon`, `ProviderRegistryConstants`, etc.), REST endpoints (`POST /Practitioner`, `GET /metadata`, `GET /Task/{id}`), Artemis queues (`harmonia.provider.registry.change.request`), and database tables (`hie_fhir_resources`).
   - *Rationale*: Eliminates ambiguity for developers and system integrators.

### Proposed Changes

#### 1. Frontmatter & Master Document Updates
- `docs/latex/main.tex`:
  - Add `\input{chapters/appendix-provider-registry.tex}` to the `\appendix` block.
- `docs/latex/chapters/00-frontmatter.tex`:
  - Update Executive Summary to introduce the FHIR R5 Provider Registry capability and reference Appendix E.
  - Update Table 0.1 (*Harmonia Classical Architectural Nomenclature and System Meaning Matrix*) to include `pylai-fhir-registry`, Provider Registry Erga, and canonical directory services.

#### 2. Core Chapter Enhancements (Chapters 01--06)
- `docs/latex/chapters/01-motivation-strategy.tex`:
  - Add Provider Directory interoperability drivers, FHIR R5 conformance goals, and directory stewardship principles.
- `docs/latex/chapters/02-business-layer.tex`:
  - Add Provider Registry business actors (Directory Stewards, Credentialing Authorities) and business processes for governed directory change ingestion and referential verification.
- `docs/latex/chapters/03-application-layer.tex`:
  - Add `pylai-fhir-registry` under Section 3.2 (*Interface \& Gateway Tier*), documenting `FhirRestGatewayController`, `CapabilityStatementProvider`, `ProviderRegistryTaskResourceProvider`, `FhirSecurityInterceptor`, and `ChangeRequestSubmissionService`.
  - Update Section 3.4 (*Energeia*) to document the 7 per-resource Ergon activities (`PractitionerChangeErgon`, etc.) and the `seq-provider-registry-change-pipeline` Praxis sequence.
  - Update Section 3.5 (*Hestia*) to document `EndpointResourceProvider`, `FhirStorageService` multi-parameter search filtering, `ProviderRegistryReferenceValidator`, and optimistic locking.
- `docs/latex/chapters/04-data-architecture.tex`:
  - Add `ProviderRegistryChangePragma` model listing and metadata specification.
  - Add canonical validation error codes (`PR-VAL-001` through `PR-VAL-010`) table.
  - Update Table 4.2 (*Supported FHIR R5 Resource Schemas*) to include `Endpoint` (15 total resources).
  - Document the hybrid relational-document schema in `hie_fhir_resources` with versioning and soft-delete columns.
- `docs/latex/chapters/05-technology-layer.tex`:
  - Update Table 5.1 (*Master Platform Port Allocation Matrix*) with `pylai-fhir-registry` and directory service ports.
- `docs/latex/chapters/06-deployment-ha.tex`:
  - Document deployment and high-availability configuration for `pylai-fhir-registry` and Artemis change queues.

#### 3. New Appendix E: `docs/latex/chapters/appendix-provider-registry.tex`
- Create a complete ~500+ line LaTeX chapter titled:
  `\chapter{Implementation and Functional Specification for FHIR R5 Provider Registry Core Services}`
  `\label{app:provider_registry}`
- Structure:
  - **Section E.1: Overview \& Engineering Objectives**: Platform context, FHIR R5 baseline, asynchronous governed change philosophy.
  - **Section E.2: Architectural Topology \& 5-Tier Interaction Model**: Flow diagrams and descriptions of synchronous read/search vs asynchronous write pathways (Pylai -> Petasos -> Energeia -> Mnemosyne -> Calliope).
  - **Section E.3: Provider Registry Resource Model \& Relationship Topology**: Detailed specifications for `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, and `Group`.
  - **Section E.4: REST API Gateway Contract \& Interaction Matrix**: Interaction table, HTTP verbs, status codes, search parameters, `GET /metadata` CapabilityStatement, and `GET /Task/{id}` status tracking.
  - **Section E.5: Governed Change Processing Lifecycle \& Pragma State Machine**: Detailed state machine transitions (`RECEIVED`, `VALIDATING`, `APPROVED`, `COMMITTING`, `COMPLETED`, `REJECTED`, `FAILED`), checkpoint audit logging, and `OperationOutcome` diagnostic reporting.
  - **Section E.6: Per-Resource Ergon Processing Activities \& Referential Integrity Engine**: Detailed breakdown of the 7 Erga, `ProviderRegistryReferenceValidator` rules, duplicate detection, and sequence orchestration.
  - **Section E.7: Authoritative Persistence, Optimistic Concurrency \& Versioning Model**: Relational PostgreSQL schema (`hie_fhir_resources`), monotonic versioning, and `If-Match`/`ETag` concurrency control.
  - **Section E.8: Security, RBAC \& Resource-Level Permission Architecture**: Fine-grained authorization scopes (`Practitioner.read`, `Endpoint.update.request`, etc.) and security filter architecture.
  - **Section E.9: Paradeigma Exemplar Generation \& Verification Testbed**: Synthetic provider directory generator, automated test harnesses (write, search, referential rejection, restart recovery, concurrency).

### File Structure Changes

```
docs/latex/
├── main.tex (add appendix-provider-registry.tex)
└── chapters/
    ├── 00-frontmatter.tex (updated)
    ├── 01-motivation-strategy.tex (updated)
    ├── 02-business-layer.tex (updated)
    ├── 03-application-layer.tex (updated)
    ├── 04-data-architecture.tex (updated)
    ├── 05-technology-layer.tex (updated)
    ├── 06-deployment-ha.tex (updated)
    └── appendix-provider-registry.tex (NEW Appendix E)
```

### Risks & Mitigations

- **Risk: Discrepancy between LaTeX documentation and actual Java implementation.**
  - *Mitigation*: Directly ground every code snippet, listing, table, and identifier against the actual Java classes in `calliope`, `pylai-fhir-registry`, `energeia/erga`, `hestia/mnemosyne-clinical`, and `paradeigma`.
- **Risk: LaTeX compilation breakage due to undefined macros, missing environments, or special characters.**
  - *Mitigation*: Strictly adhere to existing macros (`lstlisting`, `tabularx`, `adjustbox`, `hyperref`, `\texttt`, `\textbf`), escape special characters (`_`, `&`, `%`, `#`), and verify structural balance.

# Testing

### Validation Approach

1. **LaTeX Source Consistency & Syntax Verification**:
   - Inspect all LaTeX source files to ensure all opened environments (`\begin{...}`) are properly closed (`\end{...}`).
   - Verify that all cross-reference labels (`\label{...}`) and cross-references (`\ref{...}`) are mutually consistent and uniquely named.
   - Verify that all code listings specify valid languages (`Java`, `XML`, `SQL`, `bash`, `json`) compatible with the `listings` package configuration in `styles/harmonia-doc.sty`.
2. **Codebase Symbol Alignment Verification**:
   - Verify that every class name, package name, constant, queue name, topic, and REST endpoint documented in LaTeX matches the existing codebase.
   - Validate that the 15 FHIR resources listed in Table 4.2 match all HAPI FHIR providers registered in `mnemosyne-clinical` and `pylai-fhir-registry`.
3. **Comprehensive Coverage Check**:
   - Verify that all 5 platform tiers and all newly introduced Provider Registry capabilities (Pylai REST gateway, Calliope pragma models, Energeia Erga/Praxis, Mnemosyne persistence/search, Paradeigma exemplars/tests) are thoroughly documented.

### Key Scenarios

1. **Master Document Structure Verification**: Confirm `main.tex` includes `appendix-provider-registry.tex` and all 7 chapters plus 5 appendices compile in the correct sequence.
2. **Table & Listing Cross-Referencing**: Confirm Table 0.1, Table 4.2, Table 5.1, and all new tables in Appendix E are properly labeled and cross-referenced.
3. **Completeness of Appendix E**: Confirm all 9 core functional sections in Appendix E are fully populated with production-grade technical detail, ArchiMate descriptions, Java listings, and interaction tables.