# Harmonia Authoritative System Inventory

This register represents the definitive, codebase-verified inventory of all 9 subprojects and 45 Maven modules across the Harmonia Health Integration Environment.

---

## 1. Subproject & Module Inventory Matrix

| Subproject | Maven Module Path | Maven Artifact ID | Classification | Packaging | Primary Technology / Framework | Runtime Workload / Container | Default Port(s) | Persistence / Middleware Dependency |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Calliope** | `calliope` | `calliope` | `IMPLEMENTED` | `jar` | Java 21, HAPI FHIR, Jackson | Library / Embedded | N/A | None (In-memory canonical schemas) |
| **Themis** | `themis/themis-api` | `themis-api` | `IMPLEMENTED` | `jar` | Java 21, Jakarta Security Contracts | Library / Embedded | N/A | None (Pure security interfaces) |
| **Themis** | `themis/themis-core` | `themis-core` | `IMPLEMENTED` | `jar` | Java 21, Deterministic Policy Engine | Library / Service | N/A | In-Memory / Policy Repository |
| **Themis** | `themis/themis-audit` | `themis-audit` | `IMPLEMENTED` | `jar` | Java 21, Non-PHI Audit Logger | Library / Service | N/A | Non-PHI Audit Event Stream |
| **Hestia** | `hestia/mneme-cluster` | `mneme-cluster` | `CONFIGURED` | `jar` | Infinispan 15.0.3, JGroups TCP | StatefulSet (`infinispan-1`, `infinispan-2`) / `localhost:32000/harmonia/mneme-cluster:1.0.0-SNAPSHOT` | `11222`, `7800` | In-Memory Distributed Cache Grid |
| **Hestia** | `hestia/mneme-persistence`| `mneme-persistence` | `IMPLEMENTED` | `jar` | Infinispan NonBlockingStore SPI | Library / Infinispan Plugin | N/A | REST CacheStore SPI $\rightarrow$ Mnemosyne |
| **Hestia** | `hestia/mnemosyne-clinical`| `mnemosyne-clinical` | `IMPLEMENTED` | `jar` | Spring Boot 3.2.5, HAPI FHIR R5 JPA | Deployments (`hapi-fhir-jpa-server-1`, `hapi-fhir-jpa-server-2`) / `localhost:32000/harmonia/mnemosyne-clinical:1.0.0-SNAPSHOT` | `8080` per Deployment | PostgreSQL 16 (`fhir_node_1`, `fhir_node_2`) |
| **Hestia** | `hestia/mnemosyne-operations`| `mnemosyne-operations` | `IMPLEMENTED` | `jar` | Spring Boot 3.2.5, Spring Data JPA | Deployments (`operations-1`, `operations-2`) / `localhost:32000/harmonia/mnemosyne-operations:1.0.0-SNAPSHOT` | `8080` per Deployment | PostgreSQL 16 (`ops_node_1`, `ops_node_2`) |
| **Hestia** | `hestia/mnemosyne-operations-cli` | `mnemosyne-operations-cli` | `IMPLEMENTED` | `jar` | Java 21 CLI, Picocli / HTTP REST | CLI Utility | N/A | Mnemosyne Operations REST API |
| **Petasos** | `petasos/petasos-api` | `petasos-api` | `IMPLEMENTED` | `jar` | Java 21 Messaging Abstractions | Library / Embedded | N/A | None (Pure transport contracts) |
| **Petasos** | `petasos/petasos-core` | `petasos-core` | `IMPLEMENTED` | `jar` | Java 21 Envelope & Dedup Cache | Library / Embedded | N/A | In-memory sliding dedup cache |
| **Petasos** | `petasos/petasos-artemis` | `petasos-artemis` | `IMPLEMENTED` | `jar` | Artemis Core Client, Jakarta JMS 3.1 | Library / Driver | N/A | Apache ActiveMQ Artemis 2.33.0 |
| **Petasos** | `petasos/petasos-test` | `petasos-test` | `IMPLEMENTED` | `jar` | Embedded Artemis Harness & Tests | Test Library | N/A | Embedded Artemis Broker |
| **Energeia** | `energeia/erga` | `erga` | `IMPLEMENTED` | `jar` | Apache Camel 4.4, HL7/FHIR Mappers | Library / Embedded | N/A | Calliope, Themis, Mneme Cache |
| **Energeia** | `energeia/praxis` | `praxis` | `IMPLEMENTED` | `jar` | TaskSequence Orchestration & Seeding | Library / Embedded | N/A | Mneme Cache, Mnemosyne Operations |
| **Energeia** | `energeia/ponos` | `ponos` | `IMPLEMENTED` | `jar` | Spring Boot 3.2.5 / Camel Worker | Deployment (`task-processor`) / `localhost:32000/harmonia/ponos-task-processor:1.0.0-SNAPSHOT` | `8080`, `61616`, `9990` | Petasos Artemis, Mneme, Mnemosyne |
| **Energeia** | `energeia/ponos-cli` | `ponos-cli` | `IMPLEMENTED` | `jar` | Java 21 CLI, HTTP REST / JMS Control | CLI Utility | N/A | Ponos Management Endpoints |
| **Pylai** | `pylai/pylai-mllp-base` | `pylai-mllp-base` | `IMPLEMENTED` | `jar` | Netty / Camel MLLP Base & Destination Registry | Library / Embedded | N/A | Petasos, Mneme Cache, Themis |
| **Pylai** | `pylai/pylai-mllp-in` | `pylai-mllp-in` | `IMPLEMENTED` | `jar` | Spring Boot / Netty / Camel MLLP | Deployment (`mllp-gateway`) / `localhost:32000/harmonia/pylai-mllp-in:1.0.0-SNAPSHOT` | `2575` (MLLP), `8080`, `9990` | Petasos Queue Producer, Themis, Mneme |
| **Pylai** | `pylai/pylai-mllp-out` | `pylai-mllp-out` | `IMPLEMENTED` | `jar` | Spring Boot / Netty / Camel MLLP | Deployments (`mllp-outbound-his`, `mllp-outbound-lis`) / `localhost:32000/harmonia/pylai-mllp-out:1.0.0-SNAPSHOT` | `8080`, `9990` per Deployment | Petasos Queue Consumer, Remote MLLP |
| **Pylai** | `pylai/pylai-fhir-registry` | `pylai-fhir-registry`| `IMPLEMENTED` | `jar` | Spring Boot 3.2.5 REST Server | No Kubernetes workload in `deployment/kubernetes/base` | `8080` (application default; no base workload binding) | Mnemosyne Clinical, Themis Core |
| **Pylai** | `pylai/pylai-mllp-cli` | `pylai-mllp-cli` | `IMPLEMENTED` | `jar` | Java 21 MLLP Synthetic Test CLI | CLI Utility | N/A | Pylai Inbound MLLP Gateway |
| **Iris** | `iris/iris-befe` | `iris-befe` | `IMPLEMENTED` | `war` | WildFly 31.0.1, Jakarta EE 10, JAX-RS | Deployment (`befe`) / `localhost:32000/harmonia/iris-befe:1.0.0-SNAPSHOT` | `8080` (FHIR), `8090` (Ops), `9990` | Mneme Hot Rod Client, Themis API |
| **Iris** | `iris/iris-clinical` | `iris-clinical` | `IMPLEMENTED` | `jar` / Static | Vue 3, Vite, TypeScript, Pinia | Deployment (`iris-clinical`) / `localhost:32000/harmonia/iris-clinical:1.0.0-SNAPSHOT` (Nginx) | `80` | Iris BEFE REST API (`/api/fhir`) |
| **Iris** | `iris/iris-console` | `iris-console` | `IMPLEMENTED` | `jar` / Static | Vue 3, Vite, TypeScript, Pinia | Deployment (`iris-console`) / `localhost:32000/harmonia/iris-console:1.0.0-SNAPSHOT` (Nginx) | `80` | Iris BEFE REST API (`/api/operations`) |
| **Iris** | `iris/iris-administration` | `iris-administration`| `IMPLEMENTED` | `jar` / Static | Vue 3, Vite, TypeScript, Pinia | Deployment (`iris-administration`) / `localhost:32000/harmonia/iris-administration:1.0.0-SNAPSHOT` (Nginx) | `80` | Pylai FHIR Registry REST API |
| **Agora** | `agora/agora-api` | `agora-api` | `IMPLEMENTED` | `jar` | Java 21 Domain Contracts, Jackson | Library / Embedded | N/A | None (Pure API contracts) |
| **Agora** | `agora/agora-matrix` | `agora-matrix` | `IMPLEMENTED` | `jar` | Spring WebClient, Matrix v1.11 / Synapse Admin DTOs | Library / Embedded | N/A | Matrix Synapse 1.120.0 REST APIs |
| **Agora** | `agora/agora-core` | `agora-core` | `IMPLEMENTED` | `jar` | Java 21, Spring Data JPA, Petasos Client | Library / Embedded | N/A | Petasos, Mnemosyne Operations, Themis |
| **Agora** | `agora/agora-service` | `agora-service` | `IMPLEMENTED` | `jar` | Spring Boot 3.2.5, REST Controller | Deployment (`agora`) / `localhost:32000/harmonia/agora:1.0.0-SNAPSHOT` | `8092` (AS/REST), `9992` (Actuator) | Matrix Synapse, PostgreSQL, Petasos |
| **Paradeigma** | `paradeigma/paradeigma-common` | `paradeigma-common` | `IMPLEMENTED` | `jar` | Synthetic Personas, Generators & DTOs | Library / Embedded | N/A | Production APIs (Leaf Simulator) |
| **Paradeigma** | `paradeigma/paradeigma-emr` | `paradeigma-emr` | `IMPLEMENTED` | `jar` | Synthetic EMR Simulator (ORM/ADT) | Container (`paradeigma-emr`) | `8080` | Inbound MLLP (`2575`), FHIR REST |
| **Paradeigma** | `paradeigma/paradeigma-lms` | `paradeigma-lms` | `IMPLEMENTED` | `jar` | Synthetic LMS Simulator (ORU) | Container (`paradeigma-lms`) | `8080` | Inbound MLLP (`2575`), FHIR REST |
| **Paradeigma** | `paradeigma/paradeigma-pas` | `paradeigma-pas` | `IMPLEMENTED` | `jar` | Synthetic PAS Simulator (ADT A01-A40) | Container (`paradeigma-pas`) | `8080` | Inbound MLLP (`2575`), FHIR REST |
| **Paradeigma** | `paradeigma/paradeigma-rispac`| `paradeigma-rispac`| `IMPLEMENTED` | `jar` | Synthetic RIS-PAC Simulator | Container (`paradeigma-rispac`) | `8080` | Inbound MLLP (`2575`), FHIR REST |
| **Paradeigma** | `paradeigma/paradeigma-scenarios`| `paradeigma-scenarios`| `IMPLEMENTED` | `jar` | Orchestrated Multi-System Scenarios | Container (`paradeigma-scenarios`) | `8080` | Target MLLP & FHIR REST Gateways |
| **Paradeigma** | `paradeigma/paradeigma-test` | `paradeigma-test` | `IMPLEMENTED` | `jar` | ArchUnit Rules & Platform Acceptance | Test Harness | N/A | ArchUnit Java 21, Surefire |

---

## 2. Infrastructure & Middleware Workloads

| Workload Name | Component Type | Container Image | K8s Workload Kind | Cluster / Replicas | Persistent Storage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a` | ActiveMQ Artemis 2.33.0 Broker | `apache/activemq-artemis:2.33.0` | StatefulSet | 1 (Group A Live) | PVC template `artemis-primary-a-data`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `artemis-backup-a` | ActiveMQ Artemis 2.33.0 Broker | `apache/activemq-artemis:2.33.0` | StatefulSet | 1 (Group A Replica) | PVC template `artemis-backup-a-data`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `artemis-primary-b` | ActiveMQ Artemis 2.33.0 Broker | `apache/activemq-artemis:2.33.0` | StatefulSet | 1 (Group B Live) | PVC template `artemis-primary-b-data`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `artemis-backup-b` | ActiveMQ Artemis 2.33.0 Broker | `apache/activemq-artemis:2.33.0` | StatefulSet | 1 (Group B Replica) | PVC template `artemis-backup-b-data`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `infinispan-1` | Infinispan 15.0.3 Server | `localhost:32000/harmonia/mneme-cluster:1.0.0-SNAPSHOT` | StatefulSet | 1 (Node 1) | PVC template `mneme-data-1`, 1Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `infinispan-2` | Infinispan 15.0.3 Server | `localhost:32000/harmonia/mneme-cluster:1.0.0-SNAPSHOT` | StatefulSet | 1 (Node 2) | PVC template `mneme-data-2`, 1Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `postgres-1` | PostgreSQL 16 Server | `postgres:16-alpine` | StatefulSet | 1 (Clinical DB 1) | PVC template `postgres-data-1`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `postgres-2` | PostgreSQL 16 Server | `postgres:16-alpine` | StatefulSet | 1 (Clinical DB 2) | PVC template `postgres-data-2`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `postgres-ops-1` | PostgreSQL 16 Server | `postgres:16-alpine` | StatefulSet | 1 (Operations DB 1) | PVC template `postgres-ops-data-1`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `postgres-ops-2` | PostgreSQL 16 Server | `postgres:16-alpine` | StatefulSet | 1 (Operations DB 2) | PVC template `postgres-ops-data-2`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `postgres-synapse` | PostgreSQL 16 Server | `postgres:16-alpine` | StatefulSet | 1 (Synapse DB) | PVC template `postgres-synapse-data`, 2Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `synapse` | Matrix Synapse 1.120.0 Homeserver | `matrixdotorg/synapse:v1.120.0` | StatefulSet | 1 (Matrix Homeserver) | PVC template `synapse-data`, 5Gi; MicroK8s overlay adds `microk8s-hostpath` |
| `iris-ingress` | Nginx Ingress routing | MicroK8s Ingress Add-on plus repository `Ingress` resource | External controller / `Ingress` resource | L7 Routing | No platform PVC |

---

## 3. End-to-End Architectural Traceability Matrix

This matrix establishes complete end-to-end traceability across all 11 architectural dimensions for every core capability in the Harmonia platform:

| Concept | Capability | Module | Maven Artifact | Container Image | Kubernetes Workload | Service Name | Port / Protocol | Configuration Properties | Middleware | Persistence |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Calliope** | Canonical Schemas & FHIR DTOs | `calliope` | `calliope` | Embedded JAR | Embedded Library | N/A | In-Memory Local JVM | Jackson / HAPI FHIR Context | HAPI FHIR R5 Structures | Ephemeral In-Memory |
| **Themis** | Default-Deny Policy Evaluation | `themis/themis-core` | `themis-core` | Embedded JAR | Embedded Service | N/A | Local Security Context | Declarative ABAC Rules | Themis Policy Engine | Policy Repository (Memory) |
| **Themis** | Non-PHI Security Auditing | `themis/themis-audit` | `themis-audit` | Embedded JAR | Embedded Service | N/A | SLF4J / stdout | Sanitized Logback Filters | SLF4J / Logback | Container stdout / Logs |
| **Hestia** | Ephemeral Distributed Caching | `hestia/mneme-cluster` | `mneme-cluster` | `.../mneme-cluster:1.0.0-SNAPSHOT` | StatefulSet (`infinispan-1`, `infinispan-2`) | `infinispan-service`, `infinispan-1`, `infinispan-2` | `11222` (Hot Rod), `7800` (JGroups TCP) | `JAVA_OPTIONS`, `infinispan.xml` (`DIST_SYNC`, `REPL_SYNC`) | Infinispan 15.0.3, JGroups | In-Memory Grid + HostPath PVC |
| **Hestia** | Write-Behind Persistence SPI | `hestia/mneme-persistence` | `mneme-persistence` | Packaged in `mneme-cluster` | StatefulSet (`infinispan-1`, `infinispan-2`) | N/A | `8080` (HTTP REST to Mnemosyne) | `fhir.server.url`, `ops.server.url` | Infinispan NonBlockingStore SPI | Forwarded to Mnemosyne |
| **Hestia** | Clinical FHIR R5 Persistence | `hestia/mnemosyne-clinical` | `mnemosyne-clinical` | `.../mnemosyne-clinical:1.0.0-SNAPSHOT` | Deployment (`hapi-fhir-jpa-server-1`, `2`) | `hapi-fhir-jpa-server-1`, `hapi-fhir-jpa-server-2` | `8080` (FHIR REST), `5432` (JDBC) | `SPRING_PROFILES_ACTIVE=postgres`, `SPRING_DATASOURCE_URL` | Spring Boot 3.2.5, HAPI JPA | PostgreSQL 16 (`fhir_node_1`, `2`) |
| **Hestia** | Operations & Task Persistence | `hestia/mnemosyne-operations` | `mnemosyne-operations` | `.../mnemosyne-operations:1.0.0-SNAPSHOT` | Deployment (`hie-operations-jpa-server-1`, `2`) | `hie-operations-jpa-server-1`, `hie-operations-jpa-server-2` | `8080` (Ops REST), `5432` (JDBC) | `SPRING_PROFILES_ACTIVE=postgres`, `SPRING_DATASOURCE_URL` | Spring Boot 3.2.5, Spring JPA | PostgreSQL 16 (`ops_node_1`, `2`) |
| **Hestia** | Relational Database Storage | N/A | `postgres:16-alpine` | `postgres:16-alpine` | StatefulSet (`postgres-1`, `2`, `postgres-ops-1`, `2`) | `postgres-1`, `postgres-2`, `postgres-ops-1`, `postgres-ops-2` | `5432` (PostgreSQL Native TCP) | `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | PostgreSQL 16 | HostPath PVC (2Gi per DB instance) |
| **Petasos** | Resilient Clustered Messaging | `petasos/petasos-artemis` | `petasos-artemis` | `apache/activemq-artemis:2.33.0` | StatefulSet (`artemis-primary-a`, `backup-a`, `primary-b`, `backup-b`) | `petasos-artemis-discovery`, `artemis-primary-a/b`, `artemis-backup-a/b` | `61616` (Core/Netty), `8161` (Console) | `broker.xml`, `CLUSTER_USER`, `CLUSTER_PASSWORD`, `EXTRA_ARGS` | ActiveMQ Artemis 2.33.0 (replicated journal, ON_DEMAND) | HostPath PVC (2Gi per broker) |
| **Energeia** | Workflow & Task Execution | `energeia/ponos`, `erga`, `praxis` | `ponos` | `.../ponos-task-processor:1.0.0-SNAPSHOT` | Deployment (`task-processor`) | `task-processor` | `8080` (HTTP), `61616` (JMS Core), `9990` (Mgmt) | `BROKER_URL`, `INFINISPAN_HOST`, `FHIR_SERVER_URL`, `OPS_SERVER_URL` | Spring Boot 3.2.5, Camel 4.4, Artemis Client, Hot Rod Client | Persisted via Mnemosyne & Mneme |
| **Pylai** | Inbound HL7 MLLP Gateway | `pylai/pylai-mllp-in`, `pylai-mllp-base` | `pylai-mllp-in` | `.../pylai-mllp-in:1.0.0-SNAPSHOT` | Deployment (`mllp-gateway`) | `mllp-gateway` | `2575` (MLLP), `8080` (HTTP), `9990` (Mgmt) | `MLLP_HOST=0.0.0.0`, `MLLP_PORT=2575`, `TASK_BROKER_HOST/PORT` | Spring Boot 3.2.5, Netty, Camel 4.4, Petasos Producer | Task events to Petasos; cache in Mneme |
| **Pylai** | Outbound HL7 MLLP Gateway | `pylai/pylai-mllp-out`, `pylai-mllp-base` | `pylai-mllp-out` | `.../pylai-mllp-out:1.0.0-SNAPSHOT` | Deployment (`mllp-outbound-his`, `lis`) | `mllp-outbound-his`, `mllp-outbound-lis` | `8080` (HTTP), `9990` (Mgmt), Remote MLLP | `MLLP_OUTBOUND_INSTANCE_ID`, `TASK_PROCESSOR_BROKER_URL` | Spring Boot 3.2.5, Netty Client, Camel MLLP, Petasos Consumer | Delivery checkpoints to Mnemosyne |
| **Iris** | Presentation Gateway (BEFE) | `iris/iris-befe` | `iris-befe` | `.../iris-befe:1.0.0-SNAPSHOT` | Deployment (`befe`) | `befe` | `8080` (Clinical REST), `8090` (Ops REST), `9990` (Admin) | `INFINISPAN_HOST`, `INFINISPAN_PORT`, `OPERATIONS_PORT=8090` | WildFly 31.0.1, Jakarta EE 10 (JAX-RS 3.1, CDI 4.0), Hot Rod | Decoupled; proxy to Mneme & Mnemosyne |
| **Iris** | Clinical Presentation SPA | `iris/iris-clinical` | `iris-clinical` | `.../iris-clinical:1.0.0-SNAPSHOT` | Deployment (`iris-clinical`) | `iris-clinical` | `80` (HTTP Nginx) | Nginx static config, Ingress `clinical.harmonia.local` | Vue 3, Vite, TypeScript, Pinia, Nginx 1.25 Alpine | Presentation consuming `/api/fhir/*` |
| **Iris** | Operations Console SPA | `iris/iris-console` | `iris-console` | `.../iris-console:1.0.0-SNAPSHOT` | Deployment (`iris-console`) | `iris-console` | `80` (HTTP Nginx) | Nginx static config, Ingress `console.harmonia.local` | Vue 3, Vite, TypeScript, Pinia, Nginx 1.25 Alpine | Presentation consuming `/api/operations/*` |
| **Iris** | Provider Registry Admin SPA | `iris/iris-administration` | `iris-administration` | `.../iris-administration:1.0.0-SNAPSHOT` | Deployment (`iris-administration`) | `iris-administration` | `80` (HTTP Nginx) | Nginx static config, Ingress `admin.harmonia.local` | Vue 3, Vite, TypeScript, Pinia, Nginx 1.25 Alpine | Presentation consuming FHIR REST |
| **Agora** | Matrix Collaboration Gateway | `agora/agora-service`, `core`, `matrix`, `api` | `agora-service` | `.../agora:1.0.0-SNAPSHOT` | Deployment (`agora`) | `agora` | `8092` (AS/REST), `9992` (Mgmt) | `AGORA_SYNAPSE_BASE_URL`, `AGORA_SERVER_NAME`, `AGORA_APPSERVICE_ID` | Matrix Synapse 1.120.0, Petasos Artemis | PostgreSQL 16 (`agora_resource_mappings`, `agora_as_transactions`) |
| **Agora** | Matrix Synapse Homeserver | N/A | N/A | `matrixdotorg/synapse:v1.120.0` | StatefulSet (`synapse`) | `synapse` | `8008` (HTTP CS/Admin API) | `homeserver.yaml`, `appservice-agora.yaml`, `SYNAPSE_REPORT_STATS=no` | Matrix Synapse v1.120.0 | PostgreSQL 16 (`synapse_db` on `postgres-synapse:5436`) |
| **Agora** | Synapse Database Storage | N/A | `postgres:16-alpine` | `postgres:16-alpine` | StatefulSet (`postgres-synapse`) | `postgres-synapse` | `5436` (Service TCP), `5432` (Native) | `POSTGRES_DB=synapse_db`, `POSTGRES_USER=synapse_user` | PostgreSQL 16 | HostPath PVC (2Gi `postgres-synapse-data`) |
