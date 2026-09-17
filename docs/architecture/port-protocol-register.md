# Harmonia Port and Network Protocol Register

This register specifies all network communication ports, transport protocols, source/destination bindings, and security boundaries across the Harmonia platform.

---

## 1. Complete Port & Protocol Register

| Port Number | Protocol / Transport | Component / Workload | Direction & Flow | Purpose & Traffic Description | Security & Encryption |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`80` / `443`** | HTTP / HTTPS | Nginx Ingress Controller | External $\rightarrow$ Ingress | Public Web entrypoint for Iris SPAs (`iris-clinical`, `iris-console`, `iris-administration`) and REST APIs. | TLS 1.3 Termination / HTTPS |
| **`2575`** | HL7 MLLP over TCP | `pylai-mllp-in` | External EMR/PAS $\rightarrow$ Gateway | Inbound HL7 v2.4/v2.5 trigger event stream (ADT, MFN, ORM, ORU). | TCP / VPN / Dedicated VLAN |
| **`61616`** | Artemis Core / Netty | `artemis-*` (all 4 brokers) | Petasos Clients $\rightarrow$ Broker, Inter-broker HA | Active messaging traffic and inter-broker cluster journal replication (`CORE,AMQP,OPENWIRE`). | Artemis Netty Transport with Authentication |
| **`8161`** | HTTP | ActiveMQ Artemis Web | Admin $\rightarrow$ Artemis Console | Management and queue monitoring web console. | HTTP Basic Authentication (`admin`) |
| **`5432`** | PostgreSQL Native TCP | `postgres-1` | `mnemosyne-clinical` $\rightarrow$ DB | Primary clinical FHIR R5 database instance (`fhir_node_1`). | PostgreSQL Native Authentication |
| **`5433`** | PostgreSQL Native TCP | `postgres-2` | `mnemosyne-clinical` $\rightarrow$ DB | Secondary clinical FHIR R5 database instance (`fhir_node_2`). | PostgreSQL Native Authentication |
| **`5434`** | PostgreSQL Native TCP | `postgres-ops-1` | `mnemosyne-operations` $\rightarrow$ DB | Primary operations & task sequence database (`ops_node_1`). | PostgreSQL Native Authentication |
| **`5435`** | PostgreSQL Native TCP | `postgres-ops-2` | `mnemosyne-operations` $\rightarrow$ DB | Secondary operations & task sequence database (`ops_node_2`). | PostgreSQL Native Authentication |
| **`5436`** | PostgreSQL Native TCP | `postgres-synapse` | Synapse $\rightarrow$ DB | Dedicated relational storage for Synapse homeserver state (`synapse_db`). | PostgreSQL Native Authentication |
| **`8008`** | HTTP / REST | `synapse` | Agora $\rightarrow$ Synapse, Ingress $\rightarrow$ Synapse | Matrix Client-Server API, Synapse Admin API, and AS transaction egress. | Internal Cluster Network / TLS at Ingress |
| **`8092`** | HTTP / REST | `agora` | Synapse $\rightarrow$ Agora | Application Service transaction endpoint (`PUT /_matrix/app/v1/transactions/{txnId}`). | Authenticated via Bearer `hs_token` |
| **`9992`** | HTTP / Actuator | `agora` | K8s Probes $\rightarrow$ Agora | Spring Boot Actuator liveness, readiness, and metrics. | Internal Cluster Only |
| **`11222`** | Infinispan Hot Rod | `infinispan-1` / `mneme` | Iris BEFE / Ponos $\rightarrow$ Cache | Binary Hot Rod protocol for ephemeral caching and state synchronization. | Hot Rod Binary Protocol |
| **`11223`** | Infinispan Hot Rod | `infinispan-2` / `mneme` | Iris BEFE / Ponos $\rightarrow$ Cache | Node 2 Hot Rod endpoint for cache cluster access. | Hot Rod Binary Protocol |
| **`7800` / `7801`** | JGroups TCP | `infinispan-1` / `infinispan-2` | Node 1 $\leftrightarrow$ Node 2 | Cluster discovery and symmetric cache replication between peers. | JGroups TCP Transport |
| **`8080`** | HTTP REST | Spring Boot / WildFly | Inter-service REST | Default internal microservice REST API port across Spring Boot workloads. | Internal Cluster Network |
| **`8081` / `8082`** | HTTP REST | `mnemosyne-clinical` | Host/Docker $\rightarrow$ Clinical Node 1/2 | External mapped port for HAPI FHIR R5 JPA REST API in dev/docker topologies. | Internal Cluster Network |
| **`8083`** | HTTP Actuator / REST | `ponos` WorkEngine | Host/Docker $\rightarrow$ Ponos | Ponos WorkEngine telemetry, task status, and reload endpoints. | Internal Cluster Network |
| **`8084`** | HTTP REST | `pylai-mllp-in` | Host/Docker $\rightarrow$ MLLP Inbound | MLLP Inbound gateway health, status, and diagnostic endpoints. | Internal Cluster Network |
| **`8085` / `8086`** | HTTP REST | `mnemosyne-operations` | Host/Docker $\rightarrow$ Ops Node 1/2 | External mapped port for Operations JPA REST server in dev/docker topologies. | Internal Cluster Network |
| **`8087` / `8088`** | HTTP REST | `pylai-mllp-out` | Host/Docker $\rightarrow$ HIS / LIS Egress | Outbound MLLP dispatch and destination management endpoints. | Internal Cluster Network |
| **`8090`** | HTTP REST | `iris-befe` (Ops Port) | Iris Console $\rightarrow$ BEFE | Operations, telemetry, and queue monitoring endpoint on BEFE. | Internal Cluster Network |
| **`9990`** | HTTP Management | `iris-befe` (WildFly) | Admin $\rightarrow$ WildFly Console | WildFly 31 administrative management interface and deployment scanner. | Digest Auth / Admin Role |
| **`16443`** | HTTPS | MicroK8s Kube-API | `kubectl` / CI $\rightarrow$ MicroK8s | Kubernetes API Server endpoint for cluster management. | TLS / Certificate Auth |

---

## 2. Ingress & Routing Rules (Nginx)

In the MicroK8s Kubernetes deployment (`deployment/kubernetes/base/ingress.yaml`), the Ingress controller routes traffic across domain virtual hosts and path prefixes:

### 2.1 Virtual Host Routing (`deployment/kubernetes/base/ingress.yaml`)
```
http://clinical.harmonia.local/     ──► iris-clinical:80 (Clinical SPA)
http://console.harmonia.local/      ──► iris-console:80 (Operations Console SPA)
http://admin.harmonia.local/        ──► iris-administration:80 (Provider Registry Admin SPA)
http://api.harmonia.local/clinical  ──► befe:8080 (Clinical REST Gateway)
http://api.harmonia.local/operations──► befe:8090 (Operations REST Gateway)
http://gateway.harmonia.local/      ──► mllp-gateway:8080 (Inbound MLLP HTTP Gateway)
http://matrix.harmonia.local/       ──► synapse:8008 (Matrix Synapse Homeserver)
```

### 2.2 Path-Based Alternate Routing
```
https://<harmonia-host>/
├── /                   ──► iris-clinical:80 (Clinical SPA)
├── /console/           ──► iris-console:80 (Operations Console SPA)
├── /admin/             ──► iris-administration:80 (Provider Registry Admin SPA)
├── /api/fhir/          ──► iris-befe:8080 (Clinical REST Gateway)
├── /api/operations/    ──► iris-befe:8090 (Operations REST Gateway)
├── /_matrix/           ──► synapse:8008 (Matrix Client-Server / AS API)
├── /_synapse/          ──► synapse:8008 (Synapse Administration API)
└── /fhir/r5/           ──► pylai-fhir-registry:8080 (Direct FHIR REST API)
```
