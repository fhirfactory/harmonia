# Hestia Subproject Reference: Data & Persistence Services `[IMPLEMENTED]`

Hestia is Harmonia's foundational persistence and caching subsystem, managing the dual-state persistence boundary between high-throughput in-memory caching (**Mneme**) and durable relational persistence (**Mnemosyne**) across clinical and operational domains.

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
hestia/
├── mneme-cluster/          # Clustered Infinispan 15.0.3 data grid configuration
├── mneme-persistence/      # NonBlockingStore SPI implementation for write-behind caching
├── mnemosyne-clinical/     # Spring Boot HAPI FHIR R5 JPA persistence server
├── mnemosyne-operations/   # Spring Boot Spring Data JPA operations persistence server
└── hie-operations-cli/     # Command-line administrative tool for operational inspection
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `hestia`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `mneme-cluster` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:mneme-cluster:1.0.0-SNAPSHOT` (jar)
- **Primary Configuration**: `src/main/resources/infinispan.xml`
- **Key Capabilities**:
  - Clustered Infinispan 15.0.3.Final server configuration.
  - Exposes the Hot Rod binary RPC protocol on TCP port `11222` (node 1) and `11223` (node 2).
  - JGroups symmetric cluster discovery and replication on TCP port `7800` (node 1) and `7801` (node 2).
  - Manages three primary cache definitions:
    - `task-cache`: Synchronously replicated cache holding in-flight `Pragma` task execution states with configurable TTL.
    - `tasksequence-cache`: Synchronously replicated cache storing validated `Praxis` workflow blueprints.
    - `dedup-cache`: Distributed sliding-window LRU cache for message deduplication.
- **Runtime Dependencies**: Infinispan Server 15.0.3.Final, JGroups 5.3.x.

### 2.2 `mneme-persistence` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:mneme-persistence:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.persistence.client`
  - `net.fhirfactory.harmonia.persistence.config`
  - `net.fhirfactory.harmonia.persistence.store`
- **Key Classes & Interfaces**:
  - `FhirRestCacheStore`: Infinispan `NonBlockingStore` SPI implementation that asynchronously writes cached FHIR resources to the downstream Mnemosyne Clinical server.
  - `OperationsRestCacheStore`: Infinispan `NonBlockingStore` SPI implementation that asynchronously commits operational telemetry to Mnemosyne Operations.
  - `HapiFhirRestClient` / `OperationsRestClient`: Non-blocking HTTP REST client adapters used by the store SPI.
  - `FhirStoreConfiguration` / `OperationsStoreConfiguration`: Programmatic configuration builders for cache persistence.
- **Runtime Dependencies**: Infinispan Core 15.0.3.Final, Jackson Databind, Slf4j.

### 2.3 `mnemosyne-clinical` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:mnemosyne-clinical:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.hapifhir`
  - `net.fhirfactory.harmonia.hapifhir.config`
  - `net.fhirfactory.harmonia.hapifhir.model`
  - `net.fhirfactory.harmonia.hapifhir.provider`
  - `net.fhirfactory.harmonia.hapifhir.repository`
  - `net.fhirfactory.harmonia.hapifhir.service`
- **Key Classes & Providers**:
  - `HapiFhirJpaApplication`: Spring Boot 3.2.5 microservice application entry point.
  - `FhirServerConfig` & `JpaRestfulServer`: HAPI FHIR 7.2.0 RESTful servlet configuration exposing standard endpoints at `/fhir/r5/*`.
  - `FhirResourceEntity`: Relational JPA entity mapped to table `hie_fhir_resources` storing serialized FHIR JSON, resource type, version, and search indices.
  - `FhirResourceRepository`: Spring Data JPA repository for entity lookups.
  - `FhirStorageService`: High-performance persistence layer executing transactional CRUD operations.
  - `ProviderRegistryReferenceValidator`: Enforces referential integrity across master entities (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `Endpoint`).
  - **FHIR R5 Resource Providers**:
    - `PractitionerResourceProvider` (`/fhir/r5/Practitioner`)
    - `PractitionerRoleResourceProvider` (`/fhir/r5/PractitionerRole`)
    - `OrganizationResourceProvider` (`/fhir/r5/Organization`)
    - `LocationResourceProvider` (`/fhir/r5/Location`)
    - `HealthcareServiceResourceProvider` (`/fhir/r5/HealthcareService`)
    - `EndpointResourceProvider` (`/fhir/r5/Endpoint`)
    - `GroupResourceProvider` (`/fhir/r5/Group`)
    - `PersonResourceProvider` / `RelatedPersonResourceProvider`
    - `CommunicationResourceProvider` (`/fhir/r5/Communication`)
    - `TaskResourceProvider` (`/fhir/r5/Task`)
    - `ConsentResourceProvider` (`/fhir/r5/Consent`)
    - `DocumentReferenceResourceProvider` (`/fhir/r5/DocumentReference`)
    - `ProvenanceResourceProvider` (`/fhir/r5/Provenance`)
    - `AuditEventResourceProvider` (`/fhir/r5/AuditEvent`)
- **Runtime Dependencies**: Spring Boot Starter Web, Spring Boot Starter Data JPA, HAPI FHIR JPA Server 7.2.0, PostgreSQL Driver (`42.7.3`). Backed by PostgreSQL database `fhir_node_*`.

### 2.4 `mnemosyne-operations` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:mnemosyne-operations:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.operations`
  - `net.fhirfactory.harmonia.operations.controller`
  - `net.fhirfactory.harmonia.operations.model`
  - `net.fhirfactory.harmonia.operations.repository`
  - `net.fhirfactory.harmonia.operations.service`
- **Key Classes & Endpoints**:
  - `HieOperationsJpaApplication`: Spring Boot 3.2.5 application entry point.
  - `OperationResourceEntity`: JPA entity mapped to table `hie_operations_resources` storing operational states, task sequence metadata, and module metrics.
  - `OperationResourceRepository`: Spring Data JPA repository.
  - `OperationResourceController`: REST controller exposing `/api/operations/resource/*` for querying task states.
  - `OperationStatusController`: REST controller exposing `/api/operations/status/*` for module health checks.
  - `OperationStorageService`: Transactional service managing operational records.
- **Runtime Dependencies**: Spring Boot Starter Web, Spring Boot Starter Data JPA, PostgreSQL Driver (`42.7.3`). Backed by PostgreSQL database `ops_node_*`.

### 2.5 `hie-operations-cli` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:hie-operations-cli:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.operationscli`
  - `net.fhirfactory.harmonia.operationscli.client`
  - `net.fhirfactory.harmonia.operationscli.command`
  - `net.fhirfactory.harmonia.operationscli.formatter`
  - `net.fhirfactory.harmonia.operationscli.model`
- **Key Classes**:
  - `OperationsCliMain`: CLI tool entry point powered by Picocli.
  - `OperationsHttpClient`: HTTP client communicating with `mnemosyne-operations` REST endpoints.
  - `OperationsCliCommand`: CLI commands for querying active task sequences, inspecting node status, and dumping audit checkpoints.
  - `OutputFormatter`: Formats operational outputs as JSON, YAML, or colored ASCII tables.
- **Runtime Dependencies**: Java 21, Picocli, Jackson Databind.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Hestia Owns
- Low-latency in-memory cache clustering and Hot Rod server configuration (`mneme-cluster`).
- Asynchronous write-behind cache store implementation (`mneme-persistence`).
- Authoritative FHIR R5 relational persistence and JPA schema (`mnemosyne-clinical`).
- Non-FHIR operational state, TaskSequence metadata, and module status persistence (`mnemosyne-operations`).
- Relational schema definitions (`hie_fhir_resources`, `hie_operations_resources`).
- Operational inspection CLI tool (`hie-operations-cli`).

### What Hestia Explicitly Does NOT Own (Anti-Responsibilities)
- Direct MLLP network socket connections (owned by Pylai).
- JMS message queue consumption or worker thread pools (owned by Energeia/Petasos).
- User interface rendering or browser session management (owned by Iris).
- Security policy definitions (delegated to Themis).

---

## 4. Configuration Parameters `[CONFIGURED]`

| Property / Env Var | Target Module | Default Value | Purpose |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `mnemosyne-clinical` | `jdbc:postgresql://postgres-1:5432/fhir_node_1` | Clinical JDBC connection string |
| `SPRING_DATASOURCE_USERNAME` | `mnemosyne-clinical` | `fhir_user` | Clinical database user |
| `SPRING_DATASOURCE_PASSWORD` | `mnemosyne-clinical` | `fhir_password` | Clinical database password |
| `SPRING_DATASOURCE_URL` | `mnemosyne-operations` | `jdbc:postgresql://postgres-ops-1:5432/ops_node_1` | Operations JDBC connection string |
| `HARMONIA_CACHE_SERVERS` | `mneme-cluster` | `infinispan-1:11222;infinispan-2:11222` | Hot Rod cluster endpoints |
| `HAPI_FHIR_VERSION` | `mnemosyne-clinical` | `R5` | FHIR specification version |

---

## 5. Verification & Testing `[IMPLEMENTED]`

- **Execute All Hestia Tests**:
  ```bash
  mvn test -pl hestia/mneme-persistence,hestia/mnemosyne-clinical,hestia/mnemosyne-operations,hestia/hie-operations-cli -am
  ```
- **Key Test Classes**: `HapiFhirJpaTest`, `OperationsPersistenceTest`, `FhirRestCacheStoreTest`, `ProviderRegistryReferenceValidatorTest`.
