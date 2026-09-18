# Agora Subproject Reference: Matrix & Synapse Collaboration Gateway `[IMPLEMENTED]`

Agora is Harmonia's collaboration integration framework, establishing an authoritative, governed architectural bridge between Harmonia's clinical event stream and the Matrix Synapse collaboration ecosystem.

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
agora/
├── agora-api/       # Pure Java domain models, events, and topic constants
├── agora-matrix/    # Encapsulated Matrix Client-Server & Synapse Admin REST adapters
├── agora-core/      # Collaboration lifecycle, identity resolution, reconciliation & persistence
└── agora-service/   # Spring Boot 3.2.5 microservice runtime & AS transaction endpoint
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `agora`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `agora-api` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:agora-api:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.agora.api.model`
  - `net.fhirfactory.harmonia.agora.api.topic`
- **Key Classes & Interfaces**:
  - `AgoraCollaborationEvent`: Standard event envelope representing a collaboration trigger (room message, notification, or membership change).
  - `AgoraMembershipRequest`: Model for requesting practitioner join, invite, or kick operations.
  - `AgoraMembershipAction`: Enum representing `INVITE`, `JOIN`, `LEAVE`, `KICK`, `BAN`.
  - `AgoraPatientSpaceResponse`: Response model returning IDs for the created Patient Space and its four child rooms.
  - `AgoraReconciliationResult`: Encapsulates outcomes from care team membership audit runs.
  - `AgoraRoomRequest` / `AgoraSpaceRequest`: DTOs defining creation parameters for rooms and spaces.
  - `AgoraRoomType`: Enum defining `STATISTICS`, `TASKS`, `DISCUSSION`, `DIAGNOSTICS`, `GENERAL`.
  - `AgoraTopics`: Constant definitions for Petasos queue and topic addresses (`petasos.queue.agora.inbound`, etc.).
- **Runtime Dependencies**: Pure Java 21 library. Depends on Jackson Core/Databind, Slf4j, and HAPI FHIR Structures R5. Strictly zero Spring Framework or Matrix SDK dependencies.

### 2.2 `agora-matrix` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:agora-matrix:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.agora.matrix.admin`
  - `net.fhirfactory.harmonia.agora.matrix.appservice`
  - `net.fhirfactory.harmonia.agora.matrix.client`
- **Key Classes & Interfaces**:
  - `SynapseAdministrationGateway`: HTTP REST client for the Synapse Admin API (`/_synapse/admin/v2/users`, etc.) used to provision local Matrix users and reset passwords.
  - `MatrixClientAdapter`: HTTP REST client interacting with Matrix Client-Server API (`/_matrix/client/v3/rooms/*`) for room creation, state events, power levels, and invites.
  - `AppServiceRegistrationGenerator`: Generates the YAML registration descriptor (`appservice-registration-harmonia.yaml`) required by Matrix Synapse.
  - `AppServiceTransactionDto`: Data transfer object representing inbound event transactions pushed by Synapse to the Application Service endpoint.
  - `MatrixPowerLevelsDto`: Enforces room permissions, setting moderator levels and soft-archiving rooms.
- **Runtime Dependencies**: `agora-api`, Spring Web (`RestClient` / `RestTemplate`), Jackson. Encapsulates all raw Matrix protocol JSON structures so they never leak into downstream Harmonia components.

### 2.3 `agora-core` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:agora-core:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.agora.core.identity`
  - `net.fhirfactory.harmonia.agora.core.lifecycle`
  - `net.fhirfactory.harmonia.agora.core.messaging`
  - `net.fhirfactory.harmonia.agora.core.persistence`
  - `net.fhirfactory.harmonia.agora.core.reconciliation`
  - `net.fhirfactory.harmonia.agora.core.security`
- **Key Classes & Interfaces**:
  - `AgoraCollaborationLifecycleService`: Orchestrates the creation of hierarchical **Patient Spaces** with four standard child rooms (`Statistics`, `Tasks`, `Discussion`, `Diagnostics`).
  - `AgoraIdentityService`: Manages provisioning and lookup of local Matrix users (`@_harmonia_p_<uuid>:synapse`) for practitioners and patients.
  - `AgoraMembershipReconciliationService`: Scheduled engine comparing live Matrix room members against authoritative care teams in Mnemosyne/Themis, executing corrective invites or kicks.
  - `AgoraPetasosEventProducer` / `AgoraPetasosEventConsumer`: Handles asynchronous queue interaction via Petasos without direct coupling to Ponos.
  - `AgoraMappingRepository` & `AgoraMappingEntity`: JPA persistence tracking mappings between Harmonia entity UUIDs and Matrix room IDs (`agora_resource_mappings`).
  - `AgoraTransactionRepository` & `AgoraTransactionEntity`: JPA persistence ensuring deduplication and idempotency for inbound AS transactions (`agora_as_transactions`).
  - `AgoraCollaborationPolicy`: Themis security policy asserting authorization for all collaboration operations.
- **Runtime Dependencies**: `agora-api`, `agora-matrix`, `calliope`, `themis-api`, `petasos-api`, Spring Data JPA.

### 2.4 `agora-service` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:agora-service:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.agora.service`
  - `net.fhirfactory.harmonia.agora.service.config`
  - `net.fhirfactory.harmonia.agora.service.health`
  - `net.fhirfactory.harmonia.agora.service.rest`
- **Key Classes & Endpoints**:
  - `AgoraApplication`: Spring Boot 3.2.5 application entry point.
  - `ApplicationServiceTransactionEndpoint`: Matrix Application Service REST transaction receiver implementing `PUT /_matrix/app/v1/transactions/{txnId}` on service port `8092`.
  - `MatrixHealthIndicator`: Spring Boot Actuator health indicator verifying connectivity to the Matrix Synapse homeserver on management port `9992` (`/actuator/health`).
  - `AgoraProperties`: Type-safe configuration binding for Synapse URLs, AS registration tokens, and retention policies.
- **Runtime Dependencies**: `agora-core`, Spring Boot Starter Web, Spring Boot Starter Actuator, Spring Boot Starter Data JPA, PostgreSQL JDBC Driver (`org.postgresql:postgresql:42.7.3`).

---

## 3. Relational Database Persistence Schemas `[IMPLEMENTED]`

Agora manages two dedicated relational tables within Mnemosyne PostgreSQL storage:

### 3.1 `agora_resource_mappings` (Resource Mapping Entity)
Persists durable bindings between Harmonia clinical UUIDs and live Matrix room/space identifiers (AGORA-ADR-003):

| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `UUID` | `PK`, `NOT NULL` | Surrogate primary key |
| `harmonia_resource_type` | `VARCHAR(64)` | `NOT NULL` | Entity type (`PATIENT`, `PRACTITIONER`, `PRACTITIONER_ROLE`, `GROUP`) |
| `harmonia_resource_id` | `VARCHAR(128)` | `NOT NULL` | Canonical Harmonia resource identifier or UUID |
| `matrix_entity_type` | `VARCHAR(64)` | `NOT NULL` | Matrix entity classification (`SPACE`, `ROOM`, `USER`, `ROOM_STATISTICS`, `ROOM_TASKS`, `ROOM_DISCUSSION`, `ROOM_DIAGNOSTICS`) |
| `matrix_entity_id` | `VARCHAR(256)` | `NOT NULL` | Matrix room ID (`!room:synapse`) or user ID (`@user:synapse`) |
| `status` | `VARCHAR(32)` | `NOT NULL` | Lifecycle state (`ACTIVE`, `ARCHIVED`) |
| `created_at` | `TIMESTAMP` | `NOT NULL` | Initial provisioning timestamp |
| `updated_at` | `TIMESTAMP` | `NOT NULL` | Last update or reconciliation timestamp |

- **Unique Constraint**: `uk_agora_res_mapping` on `(harmonia_resource_type, harmonia_resource_id, matrix_entity_type)`
- **Indexes**: `idx_agora_res_mapping_lookup` on `(harmonia_resource_type, harmonia_resource_id)`, `idx_agora_matrix_entity_id` on `matrix_entity_id`, `idx_agora_mapping_status` on `status`.

### 3.2 `agora_as_transactions` (Application Service Transaction Deduplication)
Guarantees idempotent processing of incoming homeserver push transactions (AGORA-ADR-007):

| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `transaction_id` | `VARCHAR(128)` | `PK`, `NOT NULL` | Matrix homeserver transaction identifier (`txnId`) |
| `received_at` | `TIMESTAMP` | `NOT NULL` | Transaction receipt timestamp |
| `processed_at` | `TIMESTAMP` | `NOT NULL` | Processing completion timestamp |
| `event_count` | `INTEGER` | `NOT NULL` | Total events contained in transaction payload |
| `status` | `VARCHAR(32)` | `NOT NULL` | Outcome state (`RECEIVED`, `PROCESSED`, `FAILED`, `IGNORED`) |

- **Indexes**: `idx_agora_txn_received` on `received_at`, `idx_agora_txn_status` on `status`.

---

## 4. Mandatory Architectural Invariants `[IMPLEMENTED]`

1. **Ponos Decoupling**: Direct dependency from Agora to Ponos (`net.fhirfactory.harmonia.energeia.ponos..`) is strictly prohibited. Agora coordinates with workflows exclusively via Petasos queues (`petasos.queue.agora.*`).
2. **Matrix DTO Encapsulation**: Matrix protocol structures (Client-Server and Synapse Admin DTOs) must be encapsulated in `agora-matrix`. Raw Matrix types must never leak into other Harmonia modules.
3. **Themis Default-Deny Authorization**: Every room creation, invite, join, or kick operation must be gated by Themis policy evaluation (`themisAuthorizer.evaluate(...)`).
4. **Zero-PHI Room Metadata**: Room aliases, Space names, and topics must never include patient names, DOB, MRN, or clinical details (use opaque UUIDs).
5. **Non-Authoritative Projection**: Matrix metadata is strictly subordinate to Harmonia/Mnemosyne; clinical data must be saved to FHIR storage.
6. **Verified by ArchUnit**: Continuously validated by `AgoraIsolationArchitectureTest`.

---

## 5. Configuration Parameters `[CONFIGURED]`

| Property / Env Var | Target Module | Default Value | Purpose |
| :--- | :--- | :--- | :--- |
| `AGORA_SYNAPSE_BASE_URL` | `agora-service` | `http://synapse:8008` | Matrix Synapse Client-Server REST URL |
| `AGORA_SYNAPSE_ADMIN_URL` | `agora-service` | `http://synapse:8008` | Synapse Admin REST API endpoint |
| `AGORA_AS_TOKEN` | `agora-service` | `<as_token>` | Application service token for Synapse |
| `AGORA_HS_TOKEN` | `agora-service` | `<hs_token>` | Homeserver token for transaction verification |
| `AGORA_SERVER_NAME` | `agora-service` | `synapse` | Matrix homeserver domain name |
| `AGORA_RECONCILIATION_INTERVAL_MS`| `agora-core` | `300000` (5m) | Care team membership reconciliation frequency |
| `SERVER_PORT` | `agora-service` | `8092` | AS transaction endpoint HTTP port |
| `MANAGEMENT_SERVER_PORT` | `agora-service` | `9992` | Spring Boot Actuator management port |

---

## 6. Verification & Testing `[IMPLEMENTED]`

- **Run Agora Subsystem Tests**:
  ```bash
  mvn test -pl agora/agora-service -am
  ```
- **Run Agora Isolation Architecture Tests**:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="AgoraIsolationArchitectureTest"
  ```
- **Key Test Classes**:
  - `SynapseAdministrationGatewayTest`: Validates local user provisioning and password resets.
  - `MatrixClientAdapterTest`: Asserts room creation, power levels, state events, and member management.
  - `AgoraCollaborationLifecycleServiceTest`: Asserts Patient Space hierarchy provisioning and soft archival.
  - `AgoraMembershipReconciliationServiceTest`: Asserts care team reconciliation against live Matrix rooms.
  - `AgoraPetasosEventProducerTest` & `AgoraPetasosEventConsumerTest`: Validates Petasos message serialization and Themis gating.
  - `AgoraIsolationArchitectureTest`: Enforces Ponos decoupling, Matrix DTO encapsulation, and Paradeigma isolation.
