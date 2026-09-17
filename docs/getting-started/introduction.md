# Getting Started: Introduction to Harmonia

The **Harmonia Health Integration Environment (HIE)** is an enterprise-grade, high-throughput, fault-tolerant healthcare interoperability platform. It is engineered to connect disparate clinical and operational systems—such as Patient Administration Systems (PAS), Electronic Medical Record systems (EMR), Laboratory Information Systems (LIS/LMS), and Radiology Information Systems (RIS/PACS)—into a cohesive, secure, and fully auditable health data exchange ecosystem.

---

## 1. Healthcare Context & The Interoperability Challenge `[IMPLEMENTED]`

Modern healthcare facilities rely on dozens of heterogeneous clinical applications developed by different vendors across different decades. These systems communicate through a mix of legacy protocols (such as HL7 v2.x over Minimal Lower Layer Protocol - MLLP) and modern standards (such as HL7 FHIR R5 over HTTP REST).

```
+---------------------------------------------------------------------------------------+
|                               HOSPITAL HETEROGENEITY                                  |
|                                                                                       |
|  +--------------------+   HL7 v2.x MLLP    +---------------------------------------+  |
|  | PAS / Admitting    | -----------------> |                                       |  |
|  +--------------------+                    |                                       |  |
|                                            |                                       |  |
|  +--------------------+   HL7 v2.x MLLP    |       HARMONIA HEALTH                 |  |
|  | Laboratory (LMS)   | <----------------> |    INTEGRATION ENVIRONMENT            |  |
|  +--------------------+                    |                                       |  |
|                                            |  * Ingress Protocol Translation       |  |
|  +--------------------+   HL7 v2.x MLLP    |  * Default-Deny Security (Themis)     |  |
|  | Radiology (RIS)    | <----------------> |  * Resilient Message Queuing (Petasos)|  |
|  +--------------------+                    |  * Task Orchestration (Energeia)      |  |
|                                            |  * In-Memory Caching (Mneme)          |  |
|  +--------------------+   FHIR R5 REST     |  * Relational Persistence (Mnemosyne) |  |
|  | Modern EMR / Apps  | <----------------> |  * Clinical Collaboration (Agora)     |  |
|  +--------------------+                    |                                       |  |
|                                            +---------------------------------------+  |
+---------------------------------------------------------------------------------------+
```

### Common Failure Modes in Healthcare Integration
Traditional point-to-point interface engines often suffer from critical operational and safety vulnerabilities:
1. **Unacknowledged Data Loss**: When downstream queues or persistence engines fail, legacy engines often emit false "Application Accept" (AA) acknowledgments back to the sender, dropping clinical events without sender visibility.
2. **Brittle Fan-Out Tracking**: When an admission event (ADT^A01) must fan out to pharmacy, laboratory, and billing, failures at one destination are frequently swallowed, leaving patient records out of sync.
3. **Protected Health Information (PHI) Leakage**: Diagnostic log files frequently dump raw message payloads, exposing sensitive patient identifiers to system operators and log aggregators.
4. **Lack of Default-Deny Security**: Interface engines traditionally operate inside hospital perimeters with implicit trust, lacking fine-grained role- and attribute-based security controls.

Harmonia addresses these challenges directly through formal architectural invariants, zero-loss transaction boundaries, and strict non-PHI logging enforcement.

---

## 2. Core Architectural Philosophy `[IMPLEMENTED]`

Harmonia is built upon five foundational tenets:

1. **Zero-Loss Reliability**: Inbound gateways enforce persistence-before-acknowledgment (REC-001). An upstream sender receives an `AA` (Application Accept) acknowledgment *only* after the clinical event has been reliably committed to persistent storage and queued. If downstream delivery fails, an `AE` (Application Error) is returned, forcing the upstream sender to retry.
2. **Strict Dual-State Persistence**: Ephemeral in-flight state is isolated from durable clinical history. Fast, distributed in-memory caching (**Mneme**, powered by Infinispan 15.0.3) provides sub-millisecond task coordination, while authoritative relational storage (**Mnemosyne**, powered by PostgreSQL 16 and HAPI FHIR R5 JPA) preserves lifelong clinical records.
3. **Default-Deny Security Governance**: Every request, task execution, and storage mutation is governed by **Themis**. Access is denied by default; explicit cryptographic and role-based authorities (`ThemisPrincipal`, `HarmoniaAuthorityEnum`) are required to read, transform, or persist clinical entities.
4. **Zero-PHI Diagnostic Logging**: Operational logs at `INFO`, `WARN`, and `ERROR` levels strictly omit Protected Health Information (PHI). Traceability is achieved via deterministic correlation identifiers (`TaskSequenceId`, `PragmaId`, `MessageId`), while full payload debugging is gated behind the specialized `PhiLogger` pipeline.
5. **Architectural Decoupling**: Systems are separated into discrete, independently scalable subsystems. Presentation layers never query relational databases directly; messaging APIs expose zero JMS/broker types; and synthetic simulation frameworks are strictly forbidden from leaking into production code.

---

## 3. The 5-Tier Platform Architecture `[IMPLEMENTED]`

Harmonia organizes its capabilities into five distinct architectural tiers, supplemented by cross-cutting security, canonical modeling, and collaboration frameworks:

```
+-----------------------------------------------------------------------------------+
| 1. PRESENTATION TIER (Iris)                                                       |
|    - iris-befe (WildFly 31 REST Gateway)                                          |
|    - iris-clinical (Clinical Viewer SPA)  |  iris-console (Operations Monitor SPA)|
|    - iris-administration (Provider Registry Management SPA)                       |
+-----------------------------------------------------------------------------------+
                                       | Hot Rod / REST
                                       v
+-----------------------------------------------------------------------------------+
| 2. PROTOCOL GATEWAY TIER (Pylai)                                                  |
|    - pylai-mllp-in (Netty Inbound MLLP:2575)                                      |
|    - pylai-mllp-out (Netty Outbound MLLP Dispatcher)                              |
|    - pylai-fhir-registry (FHIR R5 REST API Gateway)                               |
+-----------------------------------------------------------------------------------+
                                       | Petasos Transport
                                       v
+-----------------------------------------------------------------------------------+
| 3. WORKFLOW & TASK EXECUTION TIER (Energeia)                                      |
|    - ponos (WorkEngine Task Worker Service)                                       |
|    - erga (Discrete Activity Execution Library)                                   |
|    - praxis (TaskSequence Blueprints & Orchestration)                             |
+-----------------------------------------------------------------------------------+
                                       | JMS Failover
                                       v
+-----------------------------------------------------------------------------------+
| 4. RESILIENT MESSAGING TIER (Petasos)                                             |
|    - petasos-api (Pure Transport Abstraction Contracts)                          |
|    - petasos-core (Sliding Window Deduplication & Envelopes)                      |
|    - petasos-artemis (Apache ActiveMQ Artemis 2.33.0 HA Cluster Adapter)          |
+-----------------------------------------------------------------------------------+
                                       | Hot Rod / JDBC
                                       v
+-----------------------------------------------------------------------------------+
| 5. DUAL-STATE PERSISTENCE TIER (Hestia)                                           |
|    - Mneme (Infinispan 15.0.3 Clustered In-Memory Cache Grid)                     |
|    - Mnemosyne Clinical (HAPI FHIR R5 JPA Server on PostgreSQL 16)                |
|    - Mnemosyne Operations (Spring Data JPA Operations Repository)                 |
+-----------------------------------------------------------------------------------+
```

### Cross-Cutting Subsystems:
- **Themis** `[IMPLEMENTED]`: Default-deny authorization engine and non-PHI security audit logging.
- **Calliope** `[IMPLEMENTED]`: Canonical domain schemas, HL7 v2-to-FHIR R5 converters, and event envelope structures.
- **Agora** `[IMPLEMENTED]`: Matrix Synapse collaboration gateway, mapping patient encounters into secure Matrix Spaces and rooms.
- **Paradeigma** `[IMPLEMENTED]`: Isolated synthetic clinical simulation framework for testing, validation, and resilience verification.

---

## 4. Status Classification Standard `[IMPLEMENTED]`

To ensure complete clarity between operational code and architectural roadmaps, all capabilities throughout this documentation suite are tagged with four explicit status badges:

| Status Badge | Definition | Verification Criteria |
| :--- | :--- | :--- |
| `[IMPLEMENTED]` | Fully implemented in Java or TypeScript source code. | Verified by active code in the repository and passing automated unit/integration tests. |
| `[CONFIGURED]` | Deployed, parameterised, and wired in infrastructure descriptors. | Verified in Kubernetes manifests, Docker Compose, or Helm/Kustomize files. |
| `[DESIGNED/PLANNED]` | Architecturally specified and targeted for implementation. | Detailed interface contracts and designs exist, but implementation is in progress or planned. |
| `[EXAMPLE/REFERENCE]` | Illustrative sample data, tutorial payloads, or test fixtures. | Non-production artifacts used for documentation or demonstration purposes. |

---

## 5. Next Steps `[IMPLEMENTED]`

To continue exploring Harmonia:
- **[Architecture at a Glance](architecture-at-a-glance.md)**: Explore the end-to-end data flow and subsystem boundaries.
- **[Terminology & Etymology](terminology.md)**: Master the Greek architectural naming conventions and domain concepts.
- **[Build Instructions](build.md)**: Compile the Maven and npm projects from source.
- **[First Deployment](first-deployment.md)**: Launch a local test environment and verify message flows.
