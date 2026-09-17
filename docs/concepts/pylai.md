# Concept: Pylai `[IMPLEMENTED]`

Pylai is Harmonia's protocol gateway subsystem, managing the external network boundary, protocol adapters, wire framing, and message translation between external hospital protocols (HL7 v2.x MLLP, FHIR R5 REST) and internal canonical task streams.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Πύλαι* (Pylai, plural of *πύλη*)
- **Etymology**: Ancient Greek noun referring to the great gates of a city, fortress, or mountain pass (e.g., *Thermopylae* — the Hot Gates).
- **Mythological & Historical Context**: In the Greek world, the *pylai* were the vital checkpoints where visitors, merchants, and messengers entered a polis. The gates were heavily guarded; sentinels verified the traveler's identity, collected duties, and ensured that hostile forces or pestilence were barred from the civic interior.
- **Architectural Rationale**: Pylai guards the perimeter of the Harmonia cluster. It is the sole entry and exit point for external clinical systems. Like the ancient gatekeepers, Pylai enforces perimeter security (via Themis), strips away external framing, verifies payload integrity, and ensures that no message passes into the internal messaging backbone without proper registration and guaranteed delivery acceptance.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Pylai comprises three primary functional gateways:

```
+---------------------------------------------------------------------------------------+
|                                    PYLAI SUBSYSTEM                                    |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +---------------------------------------+   +------------------------------------+  |
|   |         INBOUND MLLP GATEWAY          |   |       OUTBOUND MLLP GATEWAY        |  |
|   |            (pylai-mllp-in)            |   |          (pylai-mllp-out)          |  |
|   |                                       |   |                                    |  |
|   | * TCP Listener on Port 2575           |   | * Dedicated Egress Queue Consumers |  |
|   | * Netty MLLP Byte Framing (\x0b..\x1c)|   | * Dynamic Destination Registry     |  |
|   | * HL7 v2 Parser (ADT, ORU, ORM, MFN)  |   | * Remote MLLP Socket Dispatchers   |  |
|   | * Dual-Write Safety (REC-001)         |   | * Fan-Out Telemetry (REC-002)      |  |
|   | * Synchronous AA/AE ACK Generation    |   | * Remote ACK Validation            |  |
|   +---------------------------------------+   +------------------------------------+  |
|                        |                                         ^                    |
|                        v                                         |                    |
|   +--------------------------------------------------------------------------------+  |
|   |                           FHIR REST REGISTRY GATEWAY                           |  |
|   |                             (pylai-fhir-registry)                              |  |
|   |                                                                                |  |
|   | * High-Throughput HTTP REST API Gateway on Port 8080                           |  |
|   | * Direct Provider Registry CRUD Operations (Practitioner, Organization)        |  |
|   | * Themis Ingress Policy Evaluation Gate                                        |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

1. **Inbound MLLP Gateway (`pylai-mllp-in`)**: Terminates incoming TCP connections, parses HL7 v2 messages, verifies authorization via Themis, writes initial state to Mneme, and enqueues tasks to Petasos before emitting an `AA` acknowledgment.
2. **Outbound MLLP Gateway (`pylai-mllp-out`)**: Consumes dispatch events from dedicated per-destination Petasos queues (`petasos.queue.mllp.outbound.<endpoint-id>`), establishes outbound MLLP connections to downstream hospital systems, transmits messages, and awaits synchronous remote acknowledgments.
3. **FHIR REST Registry Gateway (`pylai-fhir-registry`)**: Exposes RESTful endpoints for Provider Registry master entities, evaluating Themis authorization before querying or mutating clinical storage.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Pylai Owns
- TCP socket binding, connection lifecycle, and Netty channel management on MLLP port `2575`.
- MLLP byte framing (`0x0B` start block, `0x1C 0x0D` end block).
- HL7 v2.x parsing and mapping into FHIR `Communication` resources and `Task`/`Pragma` models.
- Upstream MLLP acknowledgment generation (`AA`, `AE`, `AR`).
- Outbound MLLP connection pooling, remote socket management, and delivery timeout handling.
- Egress queue bindings and destination-specific dispatchers (`pylai-mllp-out`).
- Ingress REST API endpoints for Provider Registry search and CRUD (`pylai-fhir-registry`).
- Synthetic HL7 v2 CLI load generation and testing tool (`pylai-mllp-cli`).

### What Pylai Explicitly Does NOT Own (Anti-Responsibilities)
- Durable relational database schema management or direct SQL queries (owned by Mnemosyne).
- Long-running multi-stage workflow sequence orchestration (owned by Energeia).
- Security policy definition or authority resolution logic (delegated to Themis).
- Internal cluster message queue broker management (owned by Petasos).

---

## 4. Key Architectural Guarantees `[IMPLEMENTED]`

### Ingress Dual-Write Safety (REC-001) `[IMPLEMENTED]`
Pylai guarantees that no HL7 `AA` (Application Accept) acknowledgment is returned to an external sender until the message has been successfully accepted by the downstream Petasos queue and Mneme cache. If publishing fails (e.g., broker disconnect), Pylai catches the exception, rolls back ephemeral state, and returns an `AE` (Application Error) NACK, instructing the sender to retry.

### Destination Fan-Out Telemetry (REC-002) `[IMPLEMENTED]`
When messages are fanned out to multiple outbound systems, `pylai-mllp-out` reports granular per-destination delivery status (`SENT`, `ACKED`, `FAILED`, `TIMEOUT`) into `Task.output` extensions, ensuring full end-to-end auditability.

---

## 5. Key Classes & Modules `[IMPLEMENTED]`

| Component | Module Name | Technology | Key Classes | Status |
| :--- | :--- | :--- | :--- | :--- |
| **MLLP Foundation** | `pylai-mllp-base` | Java 21, Netty | `MllpFrameDecoder`, `MllpFrameEncoder`, `MllpDestinationRegistry` | `[IMPLEMENTED]` |
| **Inbound Gateway** | `pylai-mllp-in` | Spring Boot 3.2.5, Netty, Camel | `IncomingAdtMessageProcessor`, `PylaiMllpInApplication` | `[IMPLEMENTED]` |
| **Outbound Dispatcher** | `pylai-mllp-out` | Spring Boot 3.2.5, Netty, Camel | `OutboundMllpDispatcher`, `OutboundTaskResourceBuilder` | `[IMPLEMENTED]` |
| **FHIR REST Gateway** | `pylai-fhir-registry` | Spring Boot 3.2.5, HAPI FHIR | `FhirRegistryRestGateway`, `ProviderRegistryEndpoint` | `[IMPLEMENTED]` |
| **MLLP Testing CLI** | `pylai-mllp-cli` | Java 21 CLI (Picocli) | `MllpTestClientCommand`, `SyntheticAdtSender` | `[IMPLEMENTED]` |
