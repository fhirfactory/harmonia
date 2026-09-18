# Harmonia Integration Architecture Overview `[IMPLEMENTED]`

Harmonia provides a robust, healthcare-grade integration backbone designed to connect heterogeneous clinical and administrative systems—including Patient Administration Systems (PAS), Electronic Medical Record systems (EMR), Laboratory Information Management Systems (LMS), and Radiology Information Systems / Picture Archiving and Communication Systems (RIS/PACS).

---

## 1. Integration Topology & Core Boundaries `[IMPLEMENTED]`

Harmonia bridges classical healthcare protocols (HL7 v2.x over MLLP) and modern standards (HL7 FHIR R5 over HTTP/REST) through a decoupled, event-driven gateway architecture:

```mermaid
graph LR
    subgraph ExternalSystems ["External Healthcare Infrastructure"]
        PAS[PAS / Admitting System]
        EMR[EMR Clinical Core]
        LMS[Laboratory LIS / LMS]
        RIS[Radiology RIS / PACS]
    end

    subgraph PerimeterTier ["Perimeter Gateway Tier (Pylai)"]
        MLLPIN["pylai-mllp-in<br/>Port 2575 (Netty/Camel)"]
        MLLPOUT["pylai-mllp-out<br/>Outbound Senders"]
        FHIRREG["pylai-fhir-registry<br/>Port 8080 (REST)"]
    end

    subgraph MessagingTier ["Messaging Backbone (Petasos)"]
        ARTEMIS["ActiveMQ Artemis 2.33.0<br/>petasos.queue.*"]
    end

    subgraph ExecutionTier ["Workflow Execution (Energeia)"]
        PONOS["Ponos WorkEngine<br/>Camel Pipelines"]
        ERGA["Erga Activities<br/>(AdtDistribution, Mappers)"]
    end

    subgraph PersistenceTier ["Dual-State Persistence (Hestia)"]
        MNEME["Mneme (Infinispan 15.0.3)<br/>Task & Cache Grid"]
        MNEMOSYNE["Mnemosyne (PostgreSQL 16)<br/>HAPI FHIR R5 JPA"]
    end

    PAS -->|MLLP ADT^A01| MLLPIN
    MLLPIN -->|AA / AE ACK| PAS
    MLLPIN -->|Write Communication & Task| MNEME
    MLLPIN -->|Publish ErgonEvent| ARTEMIS

    ARTEMIS -->|Consume TaskEvent| PONOS
    PONOS -->|Execute Workflow| ERGA
    ERGA -->|Checkpoints & Fan-out| MNEME
    ERGA -->|Persist Clinical FHIR| MNEMOSYNE
    ERGA -->|Outbound MLLP Queues| ARTEMIS

    ARTEMIS -->|Consume Outbound| MLLPOUT
    MLLPOUT -->|MLLP Dispatch| EMR
    MLLPOUT -->|MLLP Dispatch| LMS
    MLLPOUT -->|MLLP Dispatch| RIS

    EMR -->|FHIR R5 Read/Search| FHIRREG
    FHIRREG -->|Fetch Data| MNEMOSYNE
```

---

## 2. Ingress & Egress Invariants `[IMPLEMENTED]`

Harmonia integration pipelines strictly adhere to two core healthcare integration architectural invariants:

### 2.1 Ingress Dual-Write Safety (REC-001) `[IMPLEMENTED]`
- When an external system transmits a clinical message (e.g., HL7 ADT^A01) to `pylai-mllp-in`, Harmonia **never** emits an `AA` (Application Accept) acknowledgement prematurely.
- Inbound messages are parsed, transformed into FHIR `Communication` and synthetic `Task` resources, cached in Mneme, and dispatched to the Petasos messaging queue.
- Only after both the cache write and the Petasos queue publish succeed is the synchronous `AA` ACK returned over the TCP MLLP connection.
- If any persistence or queue dispatch error occurs, an `AE` (Application Error) NACK is returned, prompting the upstream sender to retry.

### 2.2 Destination Fan-Out State Tracking (REC-002) `[IMPLEMENTED]`
- When a single ingested event triggers downstream notifications to multiple target endpoints (e.g., distributing an ADT event to EMR, LMS, and RIS), the system does not lose visibility into individual delivery statuses.
- The workflow engine (`AdtDistributionErgon`) records discrete sub-checkpoints for each destination queue inside the canonical `Pragma` task envelope (`destinationQueue`, `status=QUEUED`).
- Each outbound delivery instance captures response acknowledgments (`AA`, `AE`, `AR`), network latency, and error codes in structured FHIR extensions (`http://example.org/hie/destination-delivery-status`).

---

## 3. Integration Suite Navigation `[IMPLEMENTED]`

The integration specification suite consists of the following detailed technical guides:

| Document | Focus Area | Key Technologies & Subsystems | Status |
| :--- | :--- | :--- | :--- |
| [**pylai.md**](pylai.md) | Perimeter Protocol Gateways | Netty, Apache Camel 4.4, MLLP TCP Server, JAX-RS | `[IMPLEMENTED]` |
| [**hl7-v2.md**](hl7-v2.md) | HL7 v2.x Processing & Parsing | HAPI HL7 v2, Terser, ADT/MFN/ORM/ORU | `[IMPLEMENTED]` |
| [**fhir.md**](fhir.md) | FHIR R5 Data Models & Registry | HAPI FHIR R5 JPA, Provider Registry, Task Resources | `[IMPLEMENTED]` |
| [**rest.md**](rest.md) | Synchronous & Asynchronous REST | FHIR REST API, HTTP 202 Accepted, Task Tracking | `[IMPLEMENTED]` |
| [**messaging.md**](messaging.md) | Petasos Message Transport | PetasosMessage envelopes, Queue naming, Artemis | `[IMPLEMENTED]` |
| [**correlation.md**](correlation.md) | Correlation & Causation Lineage | TaskSequenceId, PragmaId, MessageControlId | `[IMPLEMENTED]` |
| [**error-handling.md**](error-handling.md) | Resiliency, Retries & DLQ | ACK/NACK semantics, Exponential Backoff, Dead-Letter Queues | `[IMPLEMENTED]` |

---

## 4. Operational & Network Listener Register `[CONFIGURED]`

The integration tier binds the following production network listeners:

| Subsystem / Gateway | Port | Protocol | Interface | Direction | Encryption |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `pylai-mllp-in` (ADT Ingress) | `2575` | MLLP / TCP | `0.0.0.0:2575` | Inbound | Clear / Optional TLS |
| `pylai-mllp-in` (Health / Admin) | `8080` | HTTP / REST | `0.0.0.0:8080` | Inbound | Clear (Internal Mesh) |
| `pylai-fhir-registry` (FHIR REST) | `8080` | HTTP / REST | `0.0.0.0:8080` | Inbound | TLS at Ingress |
| `pylai-mllp-out` (Health / Admin) | `8080` | HTTP / REST | `0.0.0.0:8080` | Inbound | Clear (Internal Mesh) |
| Outbound Destination Endpoints | `2576-2578` | MLLP / TCP | Configurable | Outbound | Target Defined |
