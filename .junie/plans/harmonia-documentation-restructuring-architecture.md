---
sessionId: session-261004-115201-16kz
---

# Working Documentation Architecture & Motivation Review

### Overview & Working Status

The documentation restructuring plan establishes the working architectural taxonomy and records governing rules for future migration. Exactly **ONE** canonical documentation body will be maintained in Markdown, with 1:1 publication representations in LaTeX (for vector PDF) and OpenDocument (ODT):

```text
docs/markdown/    ──> Authoritative Canonical Content (Git-native editing, review, diffs)
docs/latex/       ──> 1:1 Publication Representation (Vector TikZ diagrams, navigable PDF)
docs/odt/         ──> 1:1 Publication Representation (LibreOffice / Word navigable specification)
```

**Status & Guardrails**:
- The documentation taxonomy under `docs/markdown/` is an agreed working taxonomy for planning purposes.
- The Domain 01 Motivation Architecture is **under active architectural review and is not yet frozen**.
- Canonical content authoring, directory creation, file migration, and publication pipeline implementation are **not approved for execution** in this planning phase.
- No repository files are modified. All planning proceeds strictly through read-only architectural analysis.

---

### Architectural Decisions & Resolutions

1. **Architectural Axioms remain Motivation / Principles**:
   - Canonical location: `docs/markdown/01-motivation/principles/architectural-axioms.md`.
   - Architectural Axioms are fundamental principles, not governance mechanisms.
   - Conceptual hierarchy:
     ```text
     AXIOM / PRINCIPLE  (What is fundamentally true of Harmonia)
               │
               ▼
     ARCHITECTURE       (Designed according to that principle)
               │
               ▼
     GOVERNANCE         (How we ensure the architecture continues to comply)
     ```
   - No axioms directory exists under `13-governance-decisions/`. Domain 13 houses governance mechanisms (`architecture-decisions/`, `guardrails/`, `conformance/`, `exceptions/`, `assessments/`).

2. **Business Architecture remains Generic**:
   - Structure: `03-business-architecture/` contains `actors-roles/`, `business-services/`, `processes/`, `events/`, and `business-views/`.
   - Harmonia business behaviour is not presumed to be exclusively clinical.
   - Strategic capabilities belong to `02-strategy/` rather than being duplicated under Business Architecture.

3. **Information Architecture owns Logical Information and Persistence Semantics**:
   - Structure: `04-information-architecture/` contains `concepts/`, `canonical-models/`, `information-models/`, `terminology/`, `persistence-semantics/`, `lifecycle/`, and `information-views/`.
   - Governs logical information state, authoritative vs managed state, active vs durable state (AX-05), lifecycle/progression, and retention semantics.
   - Implementation-specific persistence details (SQL DDL, PostgreSQL schemas, connection pools, JPA, Infinispan cache topologies, K8s storage) are strictly excluded and placed in Application Middleware (`05`) and Technology Architecture (`07`).

4. **Application Architecture reflects Architectural Roles, Not Maven Modules**:
   - Structure: `05-application-architecture/` contains `components/`, `services/`, `interfaces/`, `middleware/`, `execution-models/`, and `application-views/`.
   - Classifications reflect architectural responsibilities rather than mirroring source code packaging.
   - Distinguishes application components, application services, interfaces, architectural concepts, execution models, and middleware enablers.

5. **Middleware / Enablers provide Generic Architectural Treatment**:
   - Retained under `05-application-architecture/middleware/` covering enabling technologies (WildFly, Spring, Infinispan, PostgreSQL, Kubernetes application capabilities, ActiveMQ Artemis, HAPI FHIR, Matrix).
   - Clear distinction:
     * *Application / Middleware*: What does the technology provide, and what constraints does it introduce?
     * *Application / Component*: How does a Harmonia component use that technology?
     * *Technology Architecture*: How is that technology deployed, hosted, connected, and operated?

6. **Integration Architecture establishes Interoperability Membrane vs Internal Bus**:
   - Structure: `06-integration-architecture/` contains `interoperability/`, `internal-messaging/`, `protocols/`, `transformation-mapping/`, `correlation-provenance/`, `error-handling/`, and `integration-views/`.
   - `interoperability/` represents the architectural concern; Pylai and its gateway implementations realise that concern.
   - Maintains strict separation between external interoperability membrane (fail-closed, AX-13) and internal transport fabric (Petasos clustered queues).

7. **Technology Architecture uses Generic Infrastructure Taxonomy**:
   - Structure: `07-technology-architecture/` contains `platforms/`, `compute/`, `network/`, `storage/`, `container-platform/`, `deployment-topology/`, `configuration/`, and `technology-views/`.
   - Generic across runtimes and hosting models, documenting Linux, Docker, MicroK8s/Kubernetes, and hardware topologies within coherent architectural concerns.

8. **Security Architecture uses Durable Security Taxonomy**:
   - Structure: `08-security-architecture/` contains `principles/`, `identity/`, `authentication/`, `authorisation/`, `policy/`, `information-security/`, `audit-provenance/`, `threat-risk/`, and `security-views/`.
   - PHI protection, Zero-PHI logging (AX-07), Themis default-deny policy governance, and Kleio audit evidence boundaries are classified within these durable concerns rather than hardcoding implementation names.
   - Foundational axioms remain authoritative under `01-motivation/principles/`.

9. **Resilience and Operability captures System Qualities**:
   - Structure: `09-resilience-operability/` contains `availability/`, `scalability/`, `failure-recovery/`, `concurrency/`, `observability/`, `performance/`, `operability/`, and `operational-views/`.
   - Focuses on comprehensive architectural qualities and runtime behaviours, avoiding reduction to simple operational runbooks.

10. **Verification and Simulation governs Architecture Assurance**:
    - Structure: `10-verification-simulation/` contains `architecture-assurance/`, `conformance-testing/`, `testing/`, `simulation/`, and `scenarios/`.
    - Paradeigma is classified within `simulation/` as an enabler rather than being encoded as the domain taxonomy.
    - Focuses on answering: "How do we demonstrate that Harmonia behaves according to its architecture, contracts, invariants, and requirements?"

11. **Solutions follow Isolated Solution-Pack Model**:
    - Structure: `11-solutions/` contains solution packs (e.g., `provider-directory/`).
    - Provides composite end-to-end narratives referencing canonical architecture domains without redefining capabilities.

12. **Implementation and Migration isolates Convergence Roadmaps**:
    - Structure: `12-implementation-migration/` contains `roadmaps/`, `work-packages/`, `migration/`, `deployment/`, `transition-architectures/`, and `backlogs/`.
    - Master convergence plan lives under `roadmaps/`. Transient Junie session logs and execution reports remain strictly quarantined in `.junie/` outside canonical documentation.

13. **Governance and Decisions maintains Conceptual Rigour**:
    - Structure: `13-governance-decisions/` contains `architecture-decisions/`, `guardrails/`, `conformance/`, `exceptions/`, and `assessments/`.
    - Strict conceptual distinctions: Principle/Axiom (guides), Requirement/Constraint (must satisfy), Guardrail (boundary), Architecture Decision (deliberate choice), Conformance (compliance assessment), Exception (approved departure).

14. **Reference Registers maintain Markdown Format Initially**:
    - Structure: `99-reference/` contains `glossary/`, `registers/`, and `developer-guides/`.
    - Registers remain Markdown tables without introducing premature structured data (YAML) formats.

15. **File Granularity balances Navigation and Coherence**:
    - Split files by coherent architectural subject. No monolithic single-file domains; no fragmentation into per-heading or per-axiom micro-files.

16. **Publication Pipeline Proof-of-Concept Strategy**:
    - Transformation implementation (Pandoc, Python generator, filters) is deferred. Domain 01 (Motivation) serves as the proof-of-concept for Markdown to LaTeX/PDF and ODT generation.

17. **Incremental Domain-by-Domain Migration Sequence**:
    - Migration strictly proceeds one domain at a time with architectural review between milestones, avoiding big-bang conversions.

---

### Frozen Working Target Taxonomy

The canonical repository documentation structure under `docs/markdown/`:

```text
docs/markdown/
├── README.md
├── 01-motivation/
│   ├── README.md
│   ├── stakeholders/
│   ├── drivers-assessments/
│   ├── goals-outcomes/
│   ├── principles/
│   └── requirements-constraints/
├── 02-strategy/
│   ├── README.md
│   ├── capabilities/
│   ├── resources/
│   ├── courses-of-action/
│   ├── capability-maps/
│   └── strategic-views/
├── 03-business-architecture/
│   ├── README.md
│   ├── actors-roles/
│   ├── business-services/
│   ├── processes/
│   ├── events/
│   └── business-views/
├── 04-information-architecture/
│   ├── README.md
│   ├── concepts/
│   ├── canonical-models/
│   ├── information-models/
│   ├── terminology/
│   ├── persistence-semantics/
│   ├── lifecycle/
│   └── information-views/
├── 05-application-architecture/
│   ├── README.md
│   ├── components/
│   ├── services/
│   ├── interfaces/
│   ├── middleware/
│   ├── execution-models/
│   └── application-views/
├── 06-integration-architecture/
│   ├── README.md
│   ├── interoperability/
│   ├── internal-messaging/
│   ├── protocols/
│   ├── transformation-mapping/
│   ├── correlation-provenance/
│   ├── error-handling/
│   └── integration-views/
├── 07-technology-architecture/
│   ├── README.md
│   ├── platforms/
│   ├── compute/
│   ├── network/
│   ├── storage/
│   ├── container-platform/
│   ├── deployment-topology/
│   ├── configuration/
│   └── technology-views/
├── 08-security-architecture/
│   ├── README.md
│   ├── principles/
│   ├── identity/
│   ├── authentication/
│   ├── authorisation/
│   ├── policy/
│   ├── information-security/
│   ├── audit-provenance/
│   ├── threat-risk/
│   └── security-views/
├── 09-resilience-operability/
│   ├── README.md
│   ├── availability/
│   ├── scalability/
│   ├── failure-recovery/
│   ├── concurrency/
│   ├── observability/
│   ├── performance/
│   ├── operability/
│   └── operational-views/
├── 10-verification-simulation/
│   ├── README.md
│   ├── architecture-assurance/
│   ├── conformance-testing/
│   ├── testing/
│   ├── simulation/
│   └── scenarios/
├── 11-solutions/
│   ├── README.md
│   └── provider-directory/
├── 12-implementation-migration/
│   ├── README.md
│   ├── roadmaps/
│   ├── work-packages/
│   ├── migration/
│   ├── deployment/
│   ├── transition-architectures/
│   └── backlogs/
├── 13-governance-decisions/
│   ├── README.md
│   ├── architecture-decisions/
│   ├── guardrails/
│   ├── conformance/
│   ├── exceptions/
│   └── assessments/
└── 99-reference/
    ├── README.md
    ├── glossary/
    ├── registers/
    └── developer-guides/
```

*Note: This tree defines architectural ownership; leaf directories are populated as content is authored or migrated, not created as empty placeholders.*

---

### Domain README Learning Framework

Each major domain `README.md` will answer the 6 core architectural learning-tool questions:
1. **What is this architectural domain?** (Scope, definition, boundaries)
2. **Why does Harmonia need it?** (Harmonia rationale, health data integration context)
3. **Which TOGAF / ArchiMate concepts are relevant?** (Formal framework alignment and viewpoints)
4. **How does this domain relate to preceding and following domains?** (Upstream drivers, downstream realisations)
5. **What Harmonia documentation belongs here?** (Concrete inclusions and specifications)
6. **What specifically does NOT belong here?** (Exclusions and cross-domain demarcations)

---

### Analysis of Taxonomy Coherence & Contradictions

A formal review against Harmonia Architectural Axioms (`AX-01` through `AX-15`) and repository guardrails (`AGENTS.md`) confirms that **zero genuine contradictions exist**:
- **Axioms vs Governance**: Locating `architectural-axioms.md` under `01-motivation/principles/` resolves the circularity of axioms governing themselves, properly establishing principles as supreme intent and governance mechanisms (`13`) as compliance verification.
- **State Demarcation (AX-05)**: Restricting `04-information-architecture/` to logical persistence semantics, lifecycle progression, and retention while assigning physical SQL DDL, JPA, and Infinispan cache topologies to `05-middleware` and `07-technology` enforces the invariant that active state and durable state have distinct architectural owners.
- **Interoperability Membrane (AX-13)**: Placing public gateway boundaries under `06-integration-architecture/interoperability/` while keeping clustered queues under `internal-messaging/` directly reflects the invariant that egress terminates Harmonia management and external contracts must never leak internal operational semantics.
- **Simulation Isolation (Invariant 1)**: Housing Paradeigma within `10-verification-simulation/simulation/` instead of an uncoordinated root folder enforces the strict rule that Paradeigma is an architectural verification mechanism, not a production dependency.

---