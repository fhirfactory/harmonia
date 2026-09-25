# Harmonia Engineering Documentation & Architecture Reference `[IMPLEMENTED]`

Welcome to the authoritative engineering documentation for the **Harmonia Health Integration Environment (HIE)**.

Harmonia is a modular, high-performance, healthcare-grade integration and interoperability platform uniting presentation services (**Iris**), perimeter protocol gateways (**Pylai**), workflow and task processing (**Energeia**: Ponos/Erga/Praxis), resilient messaging (**Petasos**), in-memory caching (**Mneme**), durable relational persistence (**Mnemosyne**), core data foundation (**Hestia**), security policy governance (**Themis**), canonical schemas (**Calliope**), collaboration bridging (**Agora**), and synthetic clinical simulation (**Paradeigma**).

---

## 1. Documentation Information Architecture `[IMPLEMENTED]`

```
docs/
├── README.md                                 # Master documentation index & navigation (this file)
├── AGENTS.md                                 # Authoritative architectural rules for autonomous agents
│
├── getting-started/                          # Introductory guides & first deployment
│   ├── introduction.md                       # Overview of Harmonia, healthcare context & 5 tiers
│   ├── architecture-at-a-glance.md           # End-to-end data flow walkthrough & architecture map
│   ├── terminology.md                        # Greek metaphor register, etymology & healthcare glossary
│   ├── build.md                              # Prerequisites, Maven commands & compilation guide
│   └── first-deployment.md                   # Docker Compose local deployment & MLLP verification runbook
│
├── concepts/                                 # The 16 Core Architectural Concepts
│   ├── harmonia.md                           # Platform cohesion, architecture invariants & lifecycle
│   ├── hestia.md                             # Dual-state persistence foundation (Mneme + Mnemosyne)
│   ├── pylai.md                              # Perimeter protocol gateways (MLLP, FHIR REST)
│   ├── petasos.md                            # Resilient messaging abstraction & Artemis HA adapter
│   ├── energeia.md                           # Workflow orchestration & task execution subsystem
│   ├── ponos.md                              # High-concurrency WorkEngine worker daemons
│   ├── ergon.md                              # Atomic activity execution units & fan-out tracking
│   ├── praxis.md                             # Workflow sequence blueprints & cache seeding
│   ├── pragma.md                             # Canonical task execution state machine envelope
│   ├── mneme.md                              # Distributed in-memory cache grid (Infinispan 15.0.3)
│   ├── mnemosyne.md                          # Durable relational persistence (HAPI FHIR JPA / PostgreSQL)
│   ├── calliope.md                           # Canonical models, DTOs, converters & PHI-safe logging
│   ├── themis.md                             # Default-deny authorization engine & non-PHI audit stream
│   ├── iris.md                               # Presentation tier (WildFly BEFE + Vue 3 SPAs)
│   ├── agora.md                              # Matrix Synapse collaboration gateway & Patient Spaces
│   └── paradeigma.md                         # Synthetic clinical simulation & production isolation
│
├── modules/                                  # Deep-dive specifications for all 9 subprojects & 37 leaf modules
│   ├── calliope.md                           # Canonical models, schemas & converters (1 leaf module)
│   ├── themis.md                             # Themis API, Core policy engine & Audit stream (3 leaf modules)
│   ├── hestia.md                             # Mneme caching grid & Mnemosyne JPA servers (5 leaf modules)
│   ├── petasos.md                            # Messaging API, Core routing & Artemis adapter (4 leaf modules)
│   ├── energeia.md                           # Ponos WorkEngine, Erga activities & Praxis (4 leaf modules)
│   ├── pylai.md                              # MLLP Inbound/Outbound & FHIR REST gateways (5 leaf modules)
│   ├── iris.md                               # Iris BEFE WildFly gateway & Vue 3 SPAs (4 leaf modules)
│   ├── agora.md                              # Matrix AS transaction endpoint & client adapters (4 leaf modules)
│   └── paradeigma.md                         # Synthetic hospital simulators & ArchUnit suites (7 leaf modules)
│
├── architecture/                             # Core platform architectural specifications
│   ├── overview.md                           # 5-tier architecture, system layers & domain decomposition
│   ├── execution-model.md                    # 4-tier Energeia execution hierarchy & Themis gates
│   ├── runtime-architecture.md               # 6-phase end-to-end clinical message lifecycle
│   ├── system-inventory.md                   # Authoritative component & module inventory
│   ├── port-protocol-register.md             # Complete network port, protocol & traffic flow bindings
│   ├── failure-recovery.md                   # Failure matrix, ACK semantics & recovery runbooks
│   ├── persistence-lifecycle.md              # 4-tier storage architecture & transaction boundaries
│   └── convergence-report.md                 # Authoritative 25-point final convergence baseline report
│
├── design/                                   # Platform engineering & concurrency design contracts
│   └── governed-write-concurrency-contract.md # Authoritative Strong Hybrid write & concurrency contract
│
├── middleware/                               # Granular middleware capability profiling
│   ├── overview.md                           # Middleware stack inventory & used vs avoided matrix
│   ├── activeMQ-artemis.md                   # Apache ActiveMQ Artemis 2.33.0 HA clustering & replication
│   ├── infinispan.md                         # Infinispan 15.0.3 distributed cache grid & Hot Rod protocol
│   ├── postgresql.md                         # PostgreSQL 16 topologies, schemas & connection pooling
│   ├── hapi-fhir.md                          # HAPI FHIR R5 JPA storage engine & resource providers
│   ├── matrix-synapse.md                     # Matrix Synapse 1.120.0 homeserver & AS integration
│   ├── wildfly.md                            # WildFly 31.0.1 runtime profile, CDI & JAX-RS BEFE
│   └── kubernetes.md                         # Kubernetes/MicroK8s workloads, StatefulSets & PVCs
│
├── integration/                              # Perimeter gateways, protocols, correlation & error handling
│   ├── overview.md                           # Integration topology, boundaries & REC invariants
│   ├── pylai.md                              # Perimeter protocol gateways & Camel pipelines
│   ├── hl7-v2.md                             # HL7 v2.x parsing, segment extraction & ACK semantics
│   ├── fhir.md                               # FHIR R5 data models & Provider Registry
│   ├── rest.md                               # Synchronous/Asynchronous REST & Task tracking
│   ├── messaging.md                          # PetasosMessage envelopes, queue topology & deduplication
│   ├── correlation.md                        # 5-tier correlation hierarchy & lineage tracking
│   └── error-handling.md                     # Resiliency, retry backoff & DLQ remediation
│
├── configuration/                            # Dual-dimension configuration registers & environments
│   ├── complete-reference.md                 # Authoritative dual-dimension configuration reference
│   ├── configuration-register.md             # Comprehensive environment variable & property register
│   ├── ports-and-protocols.md                # 18 platform network listeners, bindings & Ingress rules
│   └── environment-matrix.md                 # Configuration overlays: Dev, Test, MicroK8s, Prod, Simulation
│
├── deployment/                               # Normal MicroK8s deployment blueprints & runbooks
│   ├── prerequisites.md                      # Host operating system, hardware & kernel prerequisites
│   ├── ubuntu.md                             # Ubuntu host preparation, sysctl, UFW & local DNS
│   ├── microk8s.md                           # MicroK8s snap install, add-ons & container registry
│   ├── normal-deployment.md                  # Normal deployment walkthrough, secrets & Kustomize rollout
│   ├── component-inventory.md                # 25-workload comprehensive deployment inventory across 5 tiers
│   ├── kubernetes-workloads.md               # Kustomize base & overlay resource specifications
│   ├── microk8s-reference-guide.md           # Ubuntu single-node MicroK8s reference deployment guide
│   ├── ansible-orchestration.md              # Playbooks, roles, vault secrets & undeploy workflows
│   ├── startup.md                            # Ordered 6-phase platform startup runbook
│   ├── shutdown.md                           # Graceful reverse-dependency shutdown runbook
│   ├── upgrade.md                            # Rolling updates, StatefulSet upgrades & rollback runbook
│   ├── undeploy.md                           # Safe compute teardown vs guarded data purge runbook
│   └── verification.md                       # Post-deployment verification & automated smoke test suite
│
├── reference/                                # Authoritative formal platform reference registers
│   ├── configuration-register.md             # Formal dual-dimension configuration register
│   └── port-protocol-register.md             # Definitive 18 network listener register
│
├── paradeigma/                               # Synthetic simulation framework & isolation
│   ├── overview.md                           # Subsystem overview, purpose & leaf module inventory
│   ├── concepts.md                           # Classical metaphor, persona modeling & execution profiles
│   ├── architecture.md                       # Component topology & PD-01 to PD-09 interface catalogue
│   ├── deployment.md                         # Docker Compose, container inventory & operational runbooks
│   ├── normal-vs-paradeigma.md               # Normal vs Paradeigma deployment comparison matrix
│   ├── configuration.md                      # Pacing profiles, timer configurations & fault properties
│   ├── scenarios.md                          # Multi-system clinical journeys & provider sync workflows
│   ├── synthetic-data.md                     # Seeded generators, Australian identifiers & clinical ranges
│   ├── security-testing.md                   # Themis actor fixtures & defense-in-depth acceptance
│   ├── failure-injection.md                  # Chaos engineering, transport drops & retry semantics
│   ├── logging-validation.md                 # Dual-gate verification & in-memory PhiLogTestProbe
│   ├── production-isolation.md               # 4-dimension isolation guardrails & ArchUnit assertions
│   └── examples.md                           # Copy-pasteable test fixtures & scenario scripts
│
├── operations/                               # Operational runbooks, monitoring & health
│   ├── phi-sanitized-logging.md              # Zero-PHI logging invariants & diagnostic formats
│   ├── health-readiness.md                   # Probe configurations, metrics & actuator endpoints
│   └── verification-runbook.md               # End-to-end integration and smoke testing runbooks
│
├── security/                                 # Themis security specifications (17 detailed guides)
│   ├── architecture.md                       # Security architecture overview & defence-in-depth
│   ├── policy-model.md                       # ThemisPolicy interfaces & evaluation precedence
│   ├── roles-authorities.md                  # HarmoniaRoleEnum & HarmoniaAuthorityEnum mappings
│   ├── service-identities.md                 # Controlled service identity catalogue
│   ├── pragma-security.md                    # PragmaSecurityContext propagation in FHIR Tasks
│   └── ...                                   # Additional security domain deep-dives
│
└── provider-registry/                        # Provider Registry sub-architecture & FHIR R5 schemas
    ├── architecture.md                       # Server-side Provider Registry architecture
    ├── fhir-api.md                           # FHIR R5 REST API contracts & resources
    └── ...                                   # Validation, search & provenance specifications
```

---

## 2. Subsystem Quick Reference `[IMPLEMENTED]`

| Subproject | Maven Root | Leaf Modules | Description | Key Tech Stack | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Calliope** | `calliope/` | 1 module | Canonical schemas, models, converters, and topic definitions | Java 21, HAPI FHIR Structures, Jackson | `[IMPLEMENTED]` |
| **Themis** | `themis/` | 3 modules | Security policy governance, default-deny authorization, non-PHI audit | Java 21, Jakarta Security, Slf4j | `[IMPLEMENTED]` |
| **Hestia** | `hestia/` | 5 modules | Mneme caching grid and Mnemosyne relational JPA persistence | Infinispan 15.0.3, HAPI FHIR R5 JPA, PostgreSQL 16 | `[IMPLEMENTED]` |
| **Petasos** | `petasos/` | 4 modules | Resilient messaging abstraction and ActiveMQ Artemis HA adapter | Apache ActiveMQ Artemis 2.33.0, Jakarta JMS 3.1 | `[IMPLEMENTED]` |
| **Energeia** | `energeia/` | 4 modules | Ponos WorkEngine, Erga task activities, Praxis task sequences | WildFly 31 (Jakarta EE 10), Apache Camel 4.4 | `[IMPLEMENTED]` |
| **Pylai** | `pylai/` | 5 modules | External protocol gateways (Inbound/Outbound MLLP, FHIR REST) | Netty, Apache Camel 4.4, Jakarta EE 10 / Spring Boot | `[IMPLEMENTED]` |
| **Iris** | `iris/` | 4 modules | Presentation tier (BEFE gateway, Clinical, Console, Admin SPAs) | WildFly 31, Vue 3, Vite, TypeScript, Pinia, Nginx | `[IMPLEMENTED]` |
| **Agora** | `agora/` | 4 modules | Matrix Synapse collaboration gateway & Patient Spaces | Spring Boot 3.2.5, Matrix CS/AS REST, PostgreSQL 16 | `[IMPLEMENTED]` |
| **Paradeigma**| `paradeigma/`| 7 modules | Synthetic clinical simulation (EMR, LMS, PAS, RIS-PAC) | Java 21, Camel, Netty, HAPI HL7v2 / FHIR R5, ArchUnit | `[IMPLEMENTED]` |

**Total**: 9 subprojects, 37 leaf modules.

---

## 3. Status Classification Standard `[IMPLEMENTED]`

All architectural descriptions, parameters, and capabilities in this documentation suite are evaluated and classified under four mutually exclusive categories:
- `[IMPLEMENTED]`: Verified in Java/TypeScript source code and covered by automated tests.
- `[CONFIGURED]`: Declared and wired in deployment descriptors, Kustomize manifests, Docker Compose, or configuration files.
- `[DESIGNED/PLANNED]`: Architectural intent or roadmap capabilities intended for future implementation, never conflated with existing code.
- `[EXAMPLE/REFERENCE]`: Illustrative sample data, tutorial payloads, or testing fixtures.
