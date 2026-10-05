# ICT Foundation Capability Lenses (18 Technical Enablement Lenses)

## Overview & Architecture

The ICT Foundation Capability Tier encompasses **18 cross-cutting technical enablement lenses** through which Enterprise Capabilities are evaluated, shaped, and ultimately realized in technology.

### Foundational Distinction: Lenses vs. Capabilities
> **The 18 ICT Foundation capabilities are NOT peers of the Business Enabling capabilities, nor are they an alternative set of Enterprise Capabilities.**

In enterprise architecture, teams frequently confuse *what business functionality is required* with *what technical discipline is applied*. 

In Harmonia Strategy:
- **Enterprise Capabilities (EC-01 .. EC-13)** define the *reusable architectural functions* required across healthcare features (e.g., Activity & Execution, Context Management, Managed State).
- **ICT Foundation Capabilities (01 .. 18)** function as **technical lenses or modifiers** applied when evaluating how those Enterprise Capabilities will be realized technologically.

```text
┌────────────────────────────────────────────────────────────────────────┐
│ ENTERPRISE CAPABILITY TIER (EC-01 .. EC-13)                            │
│ What reusable architectural functions are required                     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ evaluated through
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ ICT FOUNDATION CAPABILITY LENSES (18 Lenses)                           │
│ Cross-cutting technical enablement considerations                      │
│ (Compute, Storage, Network, Security, Resilience, Observability)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ guides selection of
                                    ▼
┌──────────────��─────────────────────────────────────────────────────────┐
│ TECHNOLOGY REALISATION (Domain 07 — Technology Architecture)           │
│ Concrete software products, databases, message brokers, protocols      │
│ (PostgreSQL, Infinispan, Artemis, Camel, Netty, Kubernetes, Spring)    │
└────────────────────────────────────────────────────────────────────────┘
```

For example:
- **EC-10 (Activity & Execution)** is evaluated through:
  - Lens 10 (*Compute & Execution Services*): How are concurrency, worker scheduling, and process lifecycles managed?
  - Lens 16 (*Resilience & Continuity*): How are execution timeouts, retries, and failure escalations handled?
  - Lens 07 (*Security & Digital Trust*): How is execution gated by policy and security tokens?
  - Lens 15 (*Observability & Operational Management*): How are task metrics and queues monitored?
- **EC-08 (Interoperability & Exchange)** is evaluated through:
  - Lens 04 (*Integration & Interoperability*): How are boundary protocols adapted and transformed?
  - Lens 12 (*Network & Connectivity Services*): How are secure network channels, mutual TLS, and routing paths maintained?
  - Lens 07 (*Security & Digital Trust*): How are boundary transport certificates and tokens authenticated?

---

## The Technology-Substitution Test

To maintain enduring architectural documentation and prevent transient software products from masquerading as capabilities, Harmonia applies the **Technology-Substitution Test**:

$$\text{\bf Question: } \text{\it "If the named product, protocol, programming language, or deployment mechanism were replaced tomorrow, would the capability still exist?"}$$

- **If YES**: It represents an enduring capability or technical enablement lens.
- **If NO**: It is a technology realization, concrete software product, or transient implementation detail.

### Application Examples

| Proposed Construct | Substitution Test Analysis | Determination | Proper Architectural Home |
| :--- | :--- | :--- | :--- |
| **ActiveMQ Artemis** | If replaced with Apache Kafka or RabbitMQ tomorrow, reliable asynchronous message exchange still exists. | **Technology Realisation** (NOT a capability) | Technology Architecture (Domain 07) |
| **Infinispan Grid** | If replaced with Redis or Hazelcast tomorrow, active distributed state access still exists. | **Technology Realisation** (NOT a capability) | Technology Architecture (Domain 07) |
| **PostgreSQL Database**| If replaced with CockroachDB or Oracle tomorrow, authoritative durable persistence still exists. | **Technology Realisation** (NOT a capability) | Technology Architecture (Domain 07) |
| **HAPI FHIR Library** | If replaced with an in-house parser or another FHIR SDK tomorrow, canonical health modeling and exchange still exists. | **Technology Realisation** (NOT a capability) | Application Architecture (Domain 05) |
| **Apache Camel** | If replaced with Spring Integration or native routing daemons tomorrow, activity orchestration still exists. | **Technology Realisation** (NOT a capability) | Application Architecture (Domain 05) |
| **Netty MLLP Gateway** | If replaced with Mina or native sockets tomorrow, boundary protocol ingress still exists. | **Technology Realisation** (NOT a capability) | Integration Architecture (Domain 06) |
| **Vue 3 SPA** | If replaced with React or native client dashboards tomorrow, contextual presentation still exists. | **Technology Realisation** (NOT a capability) | Application Architecture (Domain 05) |
| **Interoperability & Exchange** | If underlying protocols or tools change, the need to adapt boundaries and exchange clinical data endures. | **Enterprise Capability (EC-08)** | Domain 02 Strategy |
| **Storage & Persistence Services** | If underlying storage engines change, the architectural discipline of data storage and recovery endures. | **ICT Foundation Lens (Lens 11)** | Domain 02 Strategy (Lens) |

---

## The 18 ICT Foundation Capabilities

```text
┌────────────────────────────────────────────────────────────────────────┐
│  01  Digital Interaction & Experience                                  │
│  02  Application Services                                              │
│  03  Information & Data Services                                       │
│  04  Integration & Interoperability                                    │
│  05  Process, Workflow & Automation                                    │
│  06  Identity & Access Services                                        │
│  07  Security & Digital Trust                                          │
│  08  Communications & Collaboration                                    │
│  09  Platform & Middleware Services                                    │
│  10  Compute & Execution Services                                      │
│  11  Storage & Persistence Services                                    │
│  12  Network & Connectivity Services                                   │
│  13  Endpoint, Device & Peripheral Services                            │
│  14  Software Engineering & Delivery                                   │
│  15  Observability & Operational Management                            │
│  16  Resilience & Continuity                                           │
│  17  ICT Service Management                                            │
│  18  Technology Architecture & Governance                              │
└────────────────────────────────────────────────────────────────────────┘
```

---

### Detailed Profiles: The 18 Enablement Lenses

#### 01. Digital Interaction & Experience
- **Focus**: User interface architectures, frontend rendering paradigms, accessibility, multi-device layouts, and human-computer interaction patterns.
- **Enabling Question**: *How do human actors perceive, navigate, and interact with platform services across web, mobile, and embedded form factors?*
- **Relationship to Strategy**: Shapes realization of `EC-11 Interaction & Experience`.

#### 02. Application Services
- **Focus**: Application service boundaries, microservice/modular monolith patterns, business logic encapsulation, and application component lifecycles.
- **Enabling Question**: *How are functional responsibilities encapsulated into cohesive, independently deployable software units?*
- **Relationship to Strategy**: Shapes decomposition of strategic logical components into software modules.

#### 03. Information & Data Services
- **Focus**: Data modeling standards, relational/document/graph paradigms, schema validation, data serialization, and information lifecycle management.
- **Enabling Question**: *How is information structured, serialized, validated, and queried across component boundaries?*
- **Relationship to Strategy**: Shapes realization of `EC-04 Information Management` and `EC-13 Semantic Governance`.

#### 04. Integration & Interoperability
- **Focus**: Boundary protocol translation, message mapping, asynchronous messaging paradigms, enterprise integration patterns (EIP), and contract testing.
- **Enabling Question**: *How are diverse external communication contracts adapted to internal canonical representations without data loss?*
- **Relationship to Strategy**: Shapes realization of `EC-08 Interoperability & Exchange`.

#### 05. Process, Workflow & Automation
- **Focus**: State machine execution engines, long-running orchestration, saga choreography, compensation handling, and event-driven automation.
- **Enabling Question**: *How are multi-step, asynchronous business processes coordinated and tracked to completion?*
- **Relationship to Strategy**: Shapes realization of `EC-10 Activity & Execution` and `EC-03 Managed State & Lifecycle`.

#### 06. Identity & Access Services
- **Focus**: Identity federation, token management, authentication protocols (OAuth2/OIDC), single sign-on (SSO), and cryptographic credential issuance.
- **Enabling Question**: *How are digital identities verified, federated, and asserted across platform boundaries?*
- **Relationship to Strategy**: Shapes realization of `EC-02 Context Management` and `EC-06 Policy & Control`.

#### 07. Security & Digital Trust
- **Focus**: Cryptographic protection at rest and in transit, default-deny policy evaluation, role/attribute-based authorization, key management, and zero-trust boundaries.
- **Enabling Question**: *How is unauthorized access prevented, privacy guaranteed, and tamper-evident cryptographic assurance established?*
- **Relationship to Strategy**: Shapes realization of `EC-06 Policy & Control` and `EC-07 Provenance & Traceability`.

#### 08. Communications & Collaboration
- **Focus**: Secure real-time messaging protocols, presence management, chat room and channel topologies, federation, and push notifications.
- **Enabling Question**: *How are synchronous and asynchronous human collaboration streams routed, federated, and secured?*
- **Relationship to Strategy**: Shapes realization of `EC-11 Interaction & Experience` and Agora collaboration enablement.

#### 09. Platform & Middleware Services
- **Focus**: Message queuing infrastructure, distributed cache fabrics, container runtimes, service discovery mechanisms, and service meshes.
- **Enabling Question**: *What shared middleware fabrics provide reliable inter-process communication, caching, and runtime hosting?*
- **Relationship to Strategy**: Realizes distributed messaging and caching for `EC-08`, `EC-09`, and `EC-04`.

#### 10. Compute & Execution Services
- **Focus**: Process execution models, CPU scheduling, thread management, asynchronous non-blocking event loops, worker pools, and serverless runtimes.
- **Enabling Question**: *How are computing resources allocated, scheduled, and supervised to progress platform activities?*
- **Relationship to Strategy**: Realizes runtime execution mechanics for `EC-10 Activity & Execution`.

#### 11. Storage & Persistence Services
- **Focus**: Durable persistence management, transaction and consistency boundaries, write-ahead journaling, tamper-evident audit logging, and distributed storage systems.
- **Enabling Question**: *How is committed information durably preserved, recovered after failure, and isolated across tenants?*
- **Relationship to Strategy**: Realizes durable preservation for `EC-04 Information Management` (Mnemosyne).

#### 12. Network & Connectivity Services
- **Focus**: IP routing, virtual private networks (VPN), software-defined networking (SDN), load balancing, firewall egress rules, and mutual TLS (mTLS).
- **Enabling Question**: *How are physical and virtual network pathways secured, segmented, and monitored against network failure?*
- **Relationship to Strategy**: Underpins secure transport for `EC-08 Interoperability & Exchange`.

#### 13. Endpoint, Device & Peripheral Services
- **Focus**: Medical device interface protocols, serial connectivity, edge device telemetry, mobile client device management, and hardware encryption.
- **Enabling Question**: *How are physical clinical devices, bedside monitors, and mobile handsets securely connected and managed?*
- **Relationship to Strategy**: Shapes edge device communication for `EC-08` and Device Administration.

#### 14. Software Engineering & Delivery
- **Focus**: Continuous integration/continuous deployment (CI/CD), automated testing harnesses, static analysis, container image packaging, and deployment automation.
- **Enabling Question**: *How is software built, verified against architectural invariants, and safely promoted to production environments?*
- **Relationship to Strategy**: Governs engineering practices across all realization domains.

#### 15. Observability & Operational Management
- **Focus**: Distributed transaction tracing, structured telemetry logging, metric collection, operational dashboards, and proactive alerting.
- **Enabling Question**: *How is runtime behavior monitored, performance bottlenecks diagnosed, and system health verified in real time?*
- **Relationship to Strategy**: Shapes realization of `EC-12 Operational Assurance` and `EC-07 Provenance & Traceability`.

#### 16. Resilience & Continuity
- **Focus**: Circuit breaking, retry back-off policies, dead-letter queuing, high availability failover, disaster recovery, and chaos engineering.
- **Enabling Question**: *How does the platform prevent cascading failures, preserve state during outages, and recover gracefully?*
- **Relationship to Strategy**: Shapes realization of `EC-12 Operational Assurance`.

#### 17. ICT Service Management
- **Focus**: Incident management, change advisory governance, service level agreements (SLAs), capacity planning, and problem resolution lifecycles.
- **Enabling Question**: *How are IT operations governed, operational incidents managed, and service commitments maintained?*
- **Relationship to Strategy**: Aligns operational runtime procedures with enterprise service expectations.

#### 18. Technology Architecture & Governance
- **Focus**: Technology lifecycle evaluation, technical standards compliance, architecture decision governance (ADRs), and technical debt management.
- **Enabling Question**: *How are technology choices evaluated, governed, and evolved over time to prevent architectural erosion?*
- **Relationship to Strategy**: Governs the transition from Strategy Architecture (Domain 02) to Technology Architecture (Domain 07).

---

## Downstream Progression

The 18 ICT Foundation lenses provide the technical criteria through which Strategy concepts transition into concrete architecture:
- **Domain 05 (Application Architecture)**: Translates capabilities into software modules using Lenses 02, 05, and 08.
- **Domain 06 (Integration Architecture)**: Designs boundary gateways and transformation pipelines using Lenses 04, 08, and 12.
- **Domain 07 (Technology Architecture)**: Selects concrete products, databases, and deployment platforms using Lenses 09, 10, 11, and 12.
- [Enterprise Capabilities (EC-01 .. EC-13)](enterprise-capabilities.md): Reusable capabilities evaluated through these lenses.
- [Capability Tier Progression Model](../capability-maps/capability-tier-model.md): Detailed vertical derivation rules.
