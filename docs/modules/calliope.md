# Calliope Subproject Reference: Canonical Schemas & Models `[IMPLEMENTED]`

Calliope is Harmonia's foundational canonical data modeling, schema definition, and PHI-safe logging library. It establishes the shared vocabulary, type systems, and event structures across all Harmonia subsystems.

---

## 1. Subproject Architecture & Overview `[IMPLEMENTED]`

Calliope is packaged as a single foundation library that sits at the base of the Harmonia dependency hierarchy.

### Subproject Maven Coordinates `[CONFIGURED]`
- **GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `calliope`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `jar`

---

## 2. Leaf Module Deep-Dive: `calliope` `[IMPLEMENTED]`

- **Maven Coordinates**: `net.fhirfactory.harmonia:calliope:1.0.0-SNAPSHOT` (jar)
- **Primary Package Hierarchy**:
  - `net.fhirfactory.harmonia.logging`
  - `net.fhirfactory.harmonia.model.ergon`
  - `net.fhirfactory.harmonia.model.petasos`
  - `net.fhirfactory.harmonia.model.pragma`
  - `net.fhirfactory.harmonia.model.praxis`
  - `net.fhirfactory.harmonia.model.registry`
  - `net.fhirfactory.harmonia.model.security`
  - `net.fhirfactory.harmonia.model.status`
  - `net.fhirfactory.harmonia.model.topic`
- **Key Classes & Interfaces**:
  - **PHI-Safe Logging Primitives**:
    - `PhiLogger`: Interface for gated diagnostic clinical logging, preventing PHI from escaping into standard log streams.
    - `DefaultPhiLogger` / `PhiLoggerFactory`: Factory producing slf4j-compatible PHI loggers with masked fallback behavior.
    - `PhiLoggingConfig`: Runtime configuration controlling whether debug PHI streams are active.
  - **Canonical Event Envelopes**:
    - `ErgonEvent`: Standard envelope carrying event metadata, origin headers, reason, and payload references.
    - `ErgonPayload`: Container for clinical message payloads (HL7 v2 text or FHIR JSON).
    - `ErgonReasonEnum`: Enumeration of business triggers (e.g., `ADMISSION`, `DISCHARGE`, `TRANSFER`, `LAB_RESULT`, `ORDER_PLACED`).
  - **Pragma State & Checkpoints**:
    - `Pragma`: Canonical task execution state machine model.
    - `PragmaCheckpoint`: Immutable milestone record capturing step outcome, execution duration, and timestamp.
    - `PragmaFhirConverter`: Converts between internal `Pragma` models and standard FHIR R5 `Task` resources.
    - `ProviderRegistryChangePragma`: Specialized Pragma model governing asynchronous Provider Registry updates.
  - **Security Models & Enumerations**:
    - `HarmoniaRoleEnum`: Mnemonic role definitions (e.g., `CLINICAL_SYSTEM`, `ADMINISTRATOR`, `INTEGRATION_GATEWAY`).
    - `HarmoniaAuthorityEnum`: Granular machine authorities (`PRV_READ`, `PRV_SUBMIT`, `PRV_PROCESS`, `PRV_WRITE`, `SYS_ADMIN`).
    - `HarmoniaSecurityLabelEnum`: Classification levels (`NORMAL`, `RESTRICTED`, `VERY_RESTRICTED`).
    - `FhirSecurityTagManager`: Utility for reading and injecting FHIR R5 `.meta.security` coding tags.
  - **Addressing & Topics**:
    - `PetasosQueueDefinition`: Declarative definition of Petasos queue characteristics (DLQ, TTL, retry limits).
    - `Topic` / `TopicSubscription`: Publish/subscribe channel addressing models.
- **Runtime Dependencies**:
  - Pure Java 21 library.
  - HAPI FHIR Structures R5 (`ca.uhn.hapi.fhir:hapi-fhir-structures-r5:7.2.0`).
  - Jackson Core / Databind (`com.fasterxml.jackson.core:jackson-databind:2.17.0`).
  - Slf4j API (`org.slf4j:slf4j-api:2.0.13`).
  - Zero dependencies on higher layers (Themis, Hestia, Petasos, Energeia, Pylai, Iris, Agora, Paradeigma).

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Calliope Owns
- Canonical event envelope definitions (`ErgonEvent`, `ErgonPayload`, `ErgonReasonEnum`).
- Pragma execution context contracts and checkpoint representations.
- Global role, authority, and security tag enumerations.
- PHI-safe logger contracts and factory implementations (`PhiLogger`).
- Topic and queue addressing models.

### What Calliope Explicitly Does NOT Own (Anti-Responsibilities)
- Network sockets, MLLP framing, or HTTP endpoints (owned by Pylai).
- Security policy evaluation algorithms or decision logic (owned by Themis).
- Cache grid storage or database access (owned by Hestia).
- Task sequence worker execution or Camel routes (owned by Energeia).
- Broker connection pooling or JMS sessions (owned by Petasos).

---

## 4. Verification & Testing `[IMPLEMENTED]`

- **Execute Calliope Unit Tests**:
  ```bash
  mvn test -pl calliope
  ```
- **Key Test Classes**: `ErgonEventSerializationTest`, `PragmaFhirConverterTest`, `PhiLoggerTest`.
