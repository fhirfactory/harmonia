# Harmonia Deployment Component Inventory `[CONFIGURED]`

This document provides the definitive, exhaustive component inventory for the normal (reference/production) Harmonia Health Integration Environment. It accounts for all 25 workloads across all 5 architectural tiers, detailing container images, Kubernetes workload types, replicas, compute allocations, storage claims, services, and health probes.

---

## 1. Architectural Tier Decomposition `[IMPLEMENTED]`

Harmonia decomposes its runtime into five functional tiers:
- **Tier 1: Presentation Tier (Iris SPAs)** — Decoupled Vue 3 Single-Page Applications served via Nginx.
- **Tier 2: Boundary Protocol Gateways & Presentation API (Pylai, Iris BEFE, Agora)** — MLLP Inbound/Outbound, WildFly JAX-RS BEFE, and Matrix Application Service gateway.
- **Tier 3: Task Execution & Collaboration Subsystems (Energeia Ponos, Matrix Synapse)** — WildFly Jakarta EE WorkEngine daemons and Matrix homeserver.
- **Tier 4: Resilient Messaging & Distributed Caching (Petasos Artemis, Hestia Mneme)** — ActiveMQ Artemis replicated HA broker mesh and Infinispan cache grid.
- **Tier 5: Durable Relational Persistence & JPA Servers (Hestia Mnemosyne & PostgreSQL)** — HAPI FHIR R5 JPA servers, Operations JPA servers, and isolated PostgreSQL databases.

---

## 2. Comprehensive 25-Workload Inventory Register `[CONFIGURED]`

### 2.1 Tier 1: Presentation Tier (Vue 3 SPAs)

| Workload Name | Kind | Container Image | Replicas | CPU Req/Lim | Mem Req/Lim | Storage | Service / Ports | Probes (Liveness / Readiness) | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `iris-clinical` | Deployment | `localhost:32000/harmonia/iris-clinical:1.0.0-SNAPSHOT` | 1 | `50m` / `500m` | `64Mi` / `256Mi` | None | `iris-clinical:80` | HTTP `/` (80) [Delay: 10s/5s] | `[IMPLEMENTED]` |
| `iris-console` | Deployment | `localhost:32000/harmonia/iris-console:1.0.0-SNAPSHOT` | 1 | `50m` / `500m` | `64Mi` / `256Mi` | None | `iris-console:80` | HTTP `/` (80) [Delay: 10s/5s] | `[IMPLEMENTED]` |
| `iris-administration`| Deployment | `localhost:32000/harmonia/iris-administration:1.0.0-SNAPSHOT` | 1 | `50m` / `500m` | `64Mi` / `256Mi` | None | `iris-administration:80` | HTTP `/` (80) [Delay: 10s/5s] | `[IMPLEMENTED]` |

---

### 2.2 Tier 2: Boundary Protocol Gateways & Presentation Backend

| Workload Name | Kind | Container Image | Replicas | CPU Req/Lim | Mem Req/Lim | Storage | Service / Ports | Probes (Liveness / Readiness) | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `befe` (Iris BEFE) | Deployment | `localhost:32000/harmonia/iris-befe:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `befe:8080` (Clinical), `8090` (Ops), `9990` (Mgmt) | TCP `8080` [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `mllp-gateway` (Pylai In) | Deployment | `localhost:32000/harmonia/pylai-mllp-in:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `mllp-gateway:2575` (MLLP), `8080` (HTTP), `9990` (Mgmt)| TCP `8080` [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `mllp-outbound-his` | Deployment | `localhost:32000/harmonia/pylai-mllp-out:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `mllp-outbound-his:8080` (HTTP), `9990` (Mgmt) | TCP `8080` [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `mllp-outbound-lis` | Deployment | `localhost:32000/harmonia/pylai-mllp-out:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `mllp-outbound-lis:8080` (HTTP), `9990` (Mgmt) | TCP `8080` [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `agora` | Deployment | `localhost:32000/harmonia/agora:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `agora:8092` (AS HTTP), `9992` (Actuator) | HTTP `/actuator/health/*` (9992) [Delay: 30s/20s] | `[IMPLEMENTED]` |

---

### 2.3 Tier 3: Workflow Processing & Collaboration Core

| Workload Name | Kind | Container Image | Replicas | CPU Req/Lim | Mem Req/Lim | Storage | Service / Ports | Probes (Liveness / Readiness) | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `task-processor` (Ponos) | Deployment | `localhost:32000/harmonia/ponos-task-processor:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `task-processor:8080` (HTTP), `61616` (Broker), `9990` (Mgmt)| TCP `8080` [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `synapse` | StatefulSet | `matrixdotorg/synapse:v1.120.0` | 1 | `250m` / `1000m` | `512Mi` / `2048Mi` | `5Gi` (`synapse-data`) | `synapse:8008` (Matrix HTTP) | HTTP `/_matrix/client/versions` (8008) [Delay: 45s/30s] | `[CONFIGURED]` |

---

### 2.4 Tier 4: Resilient Messaging & Distributed Cache Grid

| Workload Name | Kind | Container Image | Replicas | CPU Req/Lim | Mem Req/Lim | Storage | Service / Ports | Probes (Liveness / Readiness) | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a` | StatefulSet | `apache/activemq-artemis:2.33.0` | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `2Gi` (`artemis-primary-a-data`) | `artemis-primary-a:61616, 8161` | TCP `61616` [Delay: 20s/15s] | `[CONFIGURED]` |
| `artemis-backup-a` | StatefulSet | `apache/activemq-artemis:2.33.0` | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `2Gi` (`artemis-backup-a-data`) | `artemis-backup-a:61616, 8161` | TCP `61616` [Delay: 20s/15s] | `[CONFIGURED]` |
| `artemis-primary-b` | StatefulSet | `apache/activemq-artemis:2.33.0` | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `2Gi` (`artemis-primary-b-data`) | `artemis-primary-b:61616, 8161` | TCP `61616` [Delay: 20s/15s] | `[CONFIGURED]` |
| `artemis-backup-b` | StatefulSet | `apache/activemq-artemis:2.33.0` | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `2Gi` (`artemis-backup-b-data`) | `artemis-backup-b:61616, 8161` | TCP `61616` [Delay: 20s/15s] | `[CONFIGURED]` |
| `infinispan-1` | StatefulSet | `localhost:32000/harmonia/mneme-cluster:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | `1Gi` (`mneme-data-1`) | `infinispan-1:11222, 7800` | TCP `11222` [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `infinispan-2` | StatefulSet | `localhost:32000/harmonia/mneme-cluster:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | `1Gi` (`mneme-data-2`) | `infinispan-2:11222, 7800` | TCP `11222` [Delay: 30s/20s] | `[IMPLEMENTED]` |

---

### 2.5 Tier 5: Relational Persistence & JPA Microservices

| Workload Name | Kind | Container Image | Replicas | CPU Req/Lim | Mem Req/Lim | Storage | Service / Ports | Probes (Liveness / Readiness) | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `postgres-1` | StatefulSet | `postgres:16-alpine` | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `2Gi` (`postgres-data-1`) | `postgres-1:5432` | Exec `pg_isready` [Delay: 15s/5s] | `[CONFIGURED]` |
| `postgres-2` | StatefulSet | `postgres:16-alpine` | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `2Gi` (`postgres-data-2`) | `postgres-2:5432` | Exec `pg_isready` [Delay: 15s/5s] | `[CONFIGURED]` |
| `postgres-ops-1` | StatefulSet | `postgres:16-alpine` | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `2Gi` (`postgres-ops-data-1`) | `postgres-ops-1:5432` | Exec `pg_isready` [Delay: 15s/5s] | `[CONFIGURED]` |
| `postgres-ops-2` | StatefulSet | `postgres:16-alpine` | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `2Gi` (`postgres-ops-data-2`) | `postgres-ops-2:5432` | Exec `pg_isready` [Delay: 15s/5s] | `[CONFIGURED]` |
| `postgres-synapse` | StatefulSet | `postgres:16-alpine` | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `2Gi` (`postgres-synapse-data`) | `postgres-synapse:5432` | Exec `pg_isready` [Delay: 15s/5s] | `[CONFIGURED]` |
| `hapi-fhir-jpa-server-1` | Deployment | `localhost:32000/harmonia/mnemosyne-clinical:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `hapi-fhir-jpa-server-1:8080` | HTTP `/actuator/health` (8080) [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `hapi-fhir-jpa-server-2` | Deployment | `localhost:32000/harmonia/mnemosyne-clinical:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `hapi-fhir-jpa-server-2:8080` | HTTP `/actuator/health` (8080) [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `hie-operations-jpa-server-1`| Deployment| `localhost:32000/harmonia/mnemosyne-operations:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `hie-operations-jpa-server-1:8080` | HTTP `/actuator/health` (8080) [Delay: 30s/20s] | `[IMPLEMENTED]` |
| `hie-operations-jpa-server-2`| Deployment| `localhost:32000/harmonia/mnemosyne-operations:1.0.0-SNAPSHOT` | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None | `hie-operations-jpa-server-2:8080` | HTTP `/actuator/health` (8080) [Delay: 30s/20s] | `[IMPLEMENTED]` |

---

## 3. Platform Supporting Manifests Register `[CONFIGURED]`

### 3.1 Headless & Cluster Discovery Services
1. **`infinispan-service`**: Headless Kubernetes Service (`clusterIP: None`) selecting `app.kubernetes.io/name: mneme-cluster` on ports `11222` and `7800` for JGroups peer discovery.
2. **`petasos-artemis-discovery`**: Headless Kubernetes Service selecting `app.kubernetes.io/component: messaging` on ports `61616` and `8161` for cluster-wide broker topology discovery.

### 3.2 Ingress & Network Policies
1. **`harmonia-ingress`**: Master Ingress resource (`networking.k8s.io/v1`) managing virtual hosts `clinical.harmonia.local`, `console.harmonia.local`, `admin.harmonia.local`, `api.harmonia.local`, `gateway.harmonia.local`, and `matrix.harmonia.local`.
2. **`harmonia-default-network-policy`**: Default network policy isolating the `harmonia` namespace, permitting intra-namespace pod communication, Ingress ingress traffic, and DNS egress.
