# Iris Subproject Reference: Presentation Tier & User Interfaces `[IMPLEMENTED]`

Iris is Harmonia's presentation tier, comprising the WildFly 31 Jakarta EE 10 Backend-For-Frontend (BEFE) REST gateway and three decoupled TypeScript / Vue 3 Single Page Applications (SPAs).

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
iris/
├── iris-befe/             # WildFly 31.0.1 Jakarta EE 10 Backend-For-Frontend REST gateway
├── iris-clinical/         # Vue 3 / Vite SPA for clinical FHIR R5 resource browsing and exploration
├── iris-console/          # Vue 3 / Vite SPA for system topology, Artemis queues & TaskSequence telemetry
└── iris-administration/   # Vue 3 / Vite SPA for administrative Provider Registry management
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `iris`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `iris-befe` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:iris-befe:1.0.0-SNAPSHOT` (war)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.befe.config`
  - `net.fhirfactory.harmonia.befe.rest`
  - `net.fhirfactory.harmonia.befe.server`
  - `net.fhirfactory.harmonia.befe.service`
- **Key Classes & Resources**:
  - `HotRodClientProducer`: CDI producer creating `RemoteCacheManager` connections to the Mneme cluster (port 11222).
  - `FhirCacheService`: Reads and queries clinical FHIR resources from the `task-cache` in Mneme.
  - `TaskSequenceCacheService`: Queries active workflow states and execution checkpoints from Mneme.
  - `OperationsServerManager`: Spawns and manages the secondary embedded HTTP server for operational telemetry on port `8090`.
  - **Clinical REST Resources (Port 8080: `/api/fhir/*`)**:
    - `PractitionerResource`, `PractitionerRoleResource`, `OrganizationResource`, `LocationResource`
    - `TaskResource`, `CommunicationResource`, `DocumentReferenceResource`, `ConsentResource`
    - `AuditEventResource`, `ProvenanceResource`, `GroupResource`, `HealthcareServiceResource`
  - **Operational REST Resources (Port 8090: `/api/operations/*`)**:
    - `SystemStatusResource`: Reports platform subsystem status and cluster node topology.
    - `TaskSequenceResource`: Exposes live workflow sequences and checkpoint execution metrics.
- **Runtime Dependencies**: WildFly 31.0.1.Final, Jakarta RESTful Web Services 3.1, Infinispan Hot Rod Client (`15.0.3.Final`), Jackson Databind, Slf4j. Zero direct JPA or JDBC dependencies!

### 2.2 `iris-clinical` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:iris-clinical:1.0.0-SNAPSHOT` (pom / npm)
- **Technology Stack**: Vue 3 (Composition API), Vite, TypeScript, Pinia, Vue Router, TailwindCSS.
- **Key Views & Capabilities**:
  - `PatientTimelineView`: Chronological display of admissions, encounters, lab results, and medication orders.
  - `ClinicalResourceExplorer`: Raw and formatted inspection of FHIR R5 JSON resources.
  - Interfaces with `iris-befe` on port 8080 (`/api/fhir/*`).
- **Container Deployment**: Packaged into an Nginx container listening on container port `80` (mapped to host port `3000`).

### 2.3 `iris-console` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:iris-console:1.0.0-SNAPSHOT` (pom / npm)
- **Technology Stack**: Vue 3, Vite, TypeScript, Pinia, Vue Router, Chart.js.
- **Key Views & Capabilities**:
  - `BrokerTopologyView`: Live visualization of ActiveMQ Artemis symmetric cluster nodes, message counts, and consumer allocations.
  - `QueueTelemetryView`: Real-time queue depths, message ingress/egress rates, and Dead-Letter Queue (DLQ) alerts.
  - `TaskSequenceMonitorView`: Visual tracking of active workflow execution DAGs, checkpoint milestones, and processing latencies.
  - Interfaces with `iris-befe` on port 8090 (`/api/operations/*`).
- **Container Deployment**: Packaged into an Nginx container listening on container port `80` (mapped to host port `3001`).
- **Note on Terminology**: Canonically named **`iris-console`**. Legacy references to `iris-monitor` are deprecated.

### 2.4 `iris-administration` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:iris-administration:1.0.0-SNAPSHOT` (pom / npm)
- **Technology Stack**: Vue 3, Vite, TypeScript, Pinia, Vue Router.
- **Key Views & Capabilities**:
  - `AdminDashboardView`: Summary of registered master entities, data quality scores, and pending change requests.
  - `PractitionerAdminView` & `PractitionerRoleAdminView`: Search, view, and initiate changes for clinicians.
  - `OrganizationAdminView` & `LocationAdminView`: Healthcare facilities, wards, clinics, and room registries.
  - `EndpointAdminView`: Technical MLLP and FHIR communication endpoint routing configurations.
  - `WorkQueueView`: Review and audit queue for asynchronous registry change requests.
  - `ThemisSecurityView`: Inspection of Themis security policies, roles, and machine authorities.
  - Interfaces directly with `pylai-fhir-registry` on port 8080 (`/fhir/r5/*`).
- **Container Deployment**: Packaged into an Nginx container listening on container port `80` (mapped to host port `3002`).

---

## 3. Mandatory Architectural Invariants `[IMPLEMENTED]`

### Invariant 3: Presentation Tier Decoupling `[IMPLEMENTED]`
- Iris modules must **never** declare dependencies on or import `jakarta.persistence..`, `org.hibernate..`, or `org.postgresql..`.
- All clinical data is retrieved via Hot Rod binary RPC to Mneme or upstream REST APIs.
- Verified continuously by `IrisDecouplingArchitectureTest`.

### Provider Registry Decoupling `[IMPLEMENTED]`
- `iris-administration` is strictly a presentation-tier client.
- It does **not** implement server-side validation rules, referential integrity logic, or database transactions.
- All change requests are submitted to Pylai/Themis for server-side governance.
- Verified continuously by `ProviderRegistryArchitectureTest`.

---

## 4. Configuration & Ports `[CONFIGURED]`

| Component | Technology | Default Port | Primary URL / Context | Backend Connection |
| :--- | :--- | :--- | :--- | :--- |
| `iris-befe` | WildFly 31.0.1 WAR | `8080`, `8090`, `9990` | `/api/fhir`, `/api/operations` | Hot Rod (`infinispan:11222`), Themis API |
| `iris-clinical` | Vue 3 / Nginx | `3000` (container: `80`) | `/` | Iris BEFE (`/api/fhir`) |
| `iris-console` | Vue 3 / Nginx | `3001` (container: `80`) | `/console/` | Iris BEFE (`/api/operations`) |
| `iris-administration` | Vue 3 / Nginx | `3002` (container: `80`) | `/admin/` | Pylai FHIR Registry (`/fhir/r5`) |

---

## 5. Verification & Testing `[IMPLEMENTED]`

- **Execute All Iris Tests**:
  ```bash
  mvn test -pl iris/iris-befe,iris/iris-clinical,iris/iris-console,iris/iris-administration -am
  ```
- **Run Iris Architecture Tests**:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="*Iris*ArchitectureTest"
  ```
- **Key Test Classes**: `IrisDecouplingArchitectureTest`, `ProviderRegistryArchitectureTest`, `IrisBefeRestTest`.
