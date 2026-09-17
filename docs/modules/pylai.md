# Pylai Subproject Reference: External Protocol Gateways `[IMPLEMENTED]`

Pylai manages external protocol boundary adapters for HL7 v2.x MLLP and FHIR R5 REST interactions, terminating external network connections and translating wire formats into canonical Harmonia tasks.

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
pylai/
├── pylai-mllp-base/       # Core MLLP framing utilities, destination registry & task event producers
├── pylai-mllp-in/         # Inbound MLLP receiver service on port 2575 (Netty / Camel)
├── pylai-mllp-out/        # Outbound MLLP dispatcher service (Netty / Camel / Dedicated Egress Queues)
├── pylai-fhir-registry/   # FHIR REST gateway service for direct clinical & provider registry access
└── pylai-mllp-cli/        # CLI tool for generating and sending synthetic HL7 v2 MLLP trigger events
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `pylai`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `pylai-mllp-base` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:pylai-mllp-base:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.mllpgateway.config`
  - `net.fhirfactory.harmonia.mllpgateway.hl7`
  - `net.fhirfactory.harmonia.mllpgateway.messaging`
  - `net.fhirfactory.harmonia.mllpgateway.model`
  - `net.fhirfactory.harmonia.mllpgateway.service`
- **Key Classes & Interfaces**:
  - `MllpDestinationRegistry`: Thread-safe registry mapping destination IDs to remote host, port, timeout, and egress queue characteristics.
  - `TaskEventProducerService`: Publishes parsed trigger events into Petasos queue `petasos.queue.task.inbound`.
  - `OutboundTaskResourceBuilder`: Constructs outbound FHIR `Task` resources with destination tracking extensions (`http://example.org/hie/destination-delivery-status`).
  - `OutboundCommunicationResourceBuilder` & `OutboundProvenanceResourceBuilder`: Generates audit-trail FHIR resources capturing sender identity, transmission timestamp, and message fingerprints.
  - `AdtProcessingResult` / `MfnProcessingResult` / `OrmProcessingResult` / `OruProcessingResult`: DTOs encapsulating the parsing output and acknowledgment payloads.
- **Runtime Dependencies**: `calliope`, `themis-api`, `petasos-api`, HAPI HL7v2 (`2.3`), Jackson Databind, Slf4j.

### 2.2 `pylai-mllp-in` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:pylai-mllp-in:1.0.0-SNAPSHOT` (jar / war)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.mllpgateway.camel`
  - `net.fhirfactory.harmonia.mllpgateway.config`
  - `net.fhirfactory.harmonia.mllpgateway.hl7`
- **Key Classes & Route Builders**:
  - `IncomingAdtMessageMllpRouteBuilder`: Netty/Camel route listening on TCP port `2575` for HL7 ADT events (A01-A40).
  - `IncomingMfnMessageMllpRouteBuilder`: Route handling Master File Notifications (MFN).
  - `IncomingOrmMessageMllpRouteBuilder`: Route handling Pharmacy/Lab Orders (ORM).
  - `IncomingOruMessageMllpRouteBuilder`: Route handling Observation Results (ORU).
  - **Ingress Dual-Write Safety (REC-001)**: Processors guarantee downstream Petasos queue acceptance and Mneme cache registration before generating synchronous `AA` (Application Accept) acknowledgments. If downstream publishing throws an exception, an `AE` (Application Error) NACK is emitted.
- **Runtime Dependencies**: `pylai-mllp-base`, `petasos-artemis`, Apache Camel Netty (`4.4.2`), Spring Boot Starter Web (`3.2.5`) / WildFly 31.

### 2.3 `pylai-mllp-out` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:pylai-mllp-out:1.0.0-SNAPSHOT` (jar / war)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.mllpout.camel`
  - `net.fhirfactory.harmonia.mllpout.config`
  - `net.fhirfactory.harmonia.mllpout.consumer`
  - `net.fhirfactory.harmonia.mllpout.lifecycle`
  - `net.fhirfactory.harmonia.mllpout.rest`
- **Key Classes & Dispatchers**:
  - `OutboundTaskQueueConsumer`: Consumes dispatch tasks from dedicated per-destination Petasos queues (`petasos.queue.mllp.outbound.<endpoint-id>`).
  - `OutboundMllpRouteBuilder` & `OutboundMllpProcessor`: Formats MLLP wire frames and transmits messages to external hospital sockets.
  - `Hl7AckProcessor`: Synchronously inspects remote acknowledgment (`AA`, `AE`, `AR`), updating the parent `Task.output` with destination delivery checkpoints (REC-002).
  - `OutboundStateLifecycleManager`: Manages graceful socket shutdown and connection reconnect backoff.
- **Runtime Dependencies**: `pylai-mllp-base`, `petasos-artemis`, Apache Camel Netty (`4.4.2`), Spring Boot Starter Web (`3.2.5`).

### 2.4 `pylai-fhir-registry` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:pylai-fhir-registry:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.pylai.fhir.controller`
  - `net.fhirfactory.harmonia.pylai.fhir.provider`
  - `net.fhirfactory.harmonia.pylai.fhir.security`
  - `net.fhirfactory.harmonia.pylai.fhir.service`
- **Key Classes & Endpoints**:
  - `FhirRestGatewayController`: REST gateway exposing standard FHIR R5 endpoints on port 8080 (`/fhir/r5/*`).
  - `FhirSecurityInterceptor`: Evaluates incoming bearer tokens and TLS certificates against Themis policies (`ProviderRegistryReadPolicy`, `ProviderRegistrySubmitPolicy`).
  - `ChangeRequestSubmissionService`: Translates mutation requests (`POST`/`PUT`/`DELETE`) into asynchronous `ProviderRegistryChangePragma` envelopes for governed workflow approval.
  - `CapabilityStatementProvider`: Emits the platform `CapabilityStatement` metadata resource.
- **Runtime Dependencies**: `calliope`, `themis-core`, Spring Boot Starter Web (`3.2.5`), HAPI FHIR Structures R5 (`7.2.0`).

### 2.5 `pylai-mllp-cli` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:pylai-mllp-cli:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.mllpgatewaycli`
  - `net.fhirfactory.harmonia.mllpgatewaycli.client`
  - `net.fhirfactory.harmonia.mllpgatewaycli.template`
- **Key Classes**:
  - `MllpGatewayCliMain`: Command-line tool entry point.
  - `MllpClient`: Low-level TCP client implementing MLLP byte framing (`\x0b..\x1c\x0d`).
  - `AdtMessageBuilder` & `AdtTemplateRegistry`: Generates parameterized HL7 v2 ADT templates (`A01` Admission, `A03` Discharge, `A08` Update).
- **Runtime Dependencies**: Java 21, Picocli, Slf4j.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Pylai Owns
- Inbound TCP socket listening and MLLP framing over port `2575` (`pylai-mllp-in`).
- HL7 v2 trigger event parsing and conversion into FHIR `Communication` and `Task`/`Pragma` models.
- Upstream MLLP ACK/NACK generation (`AA`, `AE`, `AR`) enforcing persistence before ACK (REC-001).
- Outbound MLLP transmission and synchronous remote ACK validation (`pylai-mllp-out`).
- Dedicated per-destination Petasos egress queue consumption (`petasos.queue.mllp.outbound.<endpoint-id>`).
- Direct FHIR REST API gateway routing (`pylai-fhir-registry`).

### What Pylai Explicitly Does NOT Own (Anti-Responsibilities)
- Authoritative relational database schemas or direct JPA tables (owned by Mnemosyne).
- Complex multi-step workflow sequence orchestration (owned by Energeia).
- Security policy definitions or RBAC/ABAC authority mappings (delegated to Themis).

---

## 4. Configuration Parameters `[CONFIGURED]`

| Property / Env Var | Target Module | Default Value | Purpose |
| :--- | :--- | :--- | :--- |
| `MLLP_INBOUND_PORT` | `pylai-mllp-in` | `2575` | Inbound MLLP TCP listener port |
| `MLLP_INBOUND_MAX_FRAME_LENGTH` | `pylai-mllp-in` | `10485760` (10 MB) | Max MLLP frame payload size |
| `PETASOS_INBOUND_QUEUE` | `pylai-mllp-in` | `petasos.queue.task.inbound` | Inbound Petasos queue |
| `MLLP_OUTBOUND_TIMEOUT_MS` | `pylai-mllp-out` | `10000` (10s) | Remote MLLP ACK timeout |
| `DESTINATION_REGISTRY_CONFIG` | `pylai-mllp-out` | `classpath:destinations.json`| Outbound endpoint registry config |

---

## 5. Verification & Testing `[IMPLEMENTED]`

- **Execute All Pylai Tests**:
  ```bash
  mvn test -pl pylai/pylai-mllp-base,pylai/pylai-mllp-in,pylai/pylai-mllp-out,pylai/pylai-fhir-registry,pylai/pylai-mllp-cli -am
  ```
- **Key Test Classes**: `IncomingAdtMessageProcessorTest`, `OutboundResourceBuildersTest`, `MllpDestinationRegistryTest`.
