# Harmonia Port & Protocol Register `[CONFIGURED]`

This register provides the formal, definitive network listener reference for the Harmonia Health Integration Environment. It enumerates all 18 network listeners, transport protocols, service bindings, traffic directions, payload descriptions, and TLS/security termination points.

---

## 1. Authoritative 18 Network Listeners Register `[CONFIGURED]`

| Port Number | Transport Protocol | Interface / Component | Service Binding | Direction & Traffic Flow | Description & Payload | Security & TLS Termination | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`2575`** | TCP (MLLP) | `pylai-mllp-in` | `mllp-gateway` | Ingress: External PAS/EMR $\rightarrow$ Harmonia | HL7 v2.4/v2.5 trigger events (ADT, ORM, ORU) framed by `\x0B` and `\x1C\x0D` | Plaintext TCP or IPSec/VPN tunnel | `[IMPLEMENTED]` |
| **`80`** | TCP (HTTP) | `iris-clinical`, `iris-console`, `iris-administration` | `iris-clinical`, `iris-console`, `iris-administration` | Ingress: Ingress $\rightarrow$ Nginx SPAs | Static HTML5/JavaScript assets for Vue 3 frontend user interfaces | Plaintext HTTP (TLS terminated at Ingress) | `[IMPLEMENTED]` |
| **`443`** | TCP (HTTPS) | MicroK8s Nginx Ingress | `harmonia-ingress` | Ingress: External Browser $\rightarrow$ Platform | External TLS gateway for web presentation and FHIR REST endpoints | TLS 1.2 / 1.3 Termination | `[CONFIGURED]` |
| **`8080`** | TCP (HTTP) | `iris-befe` (WildFly 31) | `befe` | Ingress & Internal: Ingress $\rightarrow$ BEFE | Clinical FHIR R5 REST API (`/clinical/r5/*`) and internal microservice REST | Internal cluster network | `[IMPLEMENTED]` |
| **`8090`** | TCP (HTTP) | `iris-befe` (WildFly 31) | `befe` | Ingress & Internal: Ingress $\rightarrow$ BEFE | Operational telemetry, queue inspection, and system metrics (`/operations/*`) | Internal cluster network | `[IMPLEMENTED]` |
| **`9990`** | TCP (HTTP) | `iris-befe`, `ponos`, `pylai` | Pod internal | Management: Operator $\rightarrow$ WildFly CLI | WildFly Jakarta EE 10 management console and runtime metrics | Digest Authentication / Admin role | `[CONFIGURED]` |
| **`61616`** | TCP (Netty) | `artemis-primary-a/b`, `artemis-backup-a/b` | `petasos-artemis-discovery`, `artemis-*` | Intra-cluster: Workloads $\rightarrow$ Artemis | Petasos message transport (`CORE,AMQP,OPENWIRE`) and HA journal replication | Artemis internal authentication (`admin`) | `[IMPLEMENTED]` |
| **`8161`** | TCP (HTTP) | `artemis-*` (all 4) | `artemis-*` | Management: Operator $\rightarrow$ Artemis Console | ActiveMQ Artemis Web Management Console and Jolokia REST metrics | HTTP Basic Authentication (`admin`) | `[CONFIGURED]` |
| **`11222`** | TCP (Hot Rod) | `infinispan-1`, `infinispan-2` | `infinispan-service`, `infinispan-*` | Intra-cluster: BEFE / Ponos $\rightarrow$ Cache Grid | Hot Rod binary RPC protocol and REST API for 17 replicated Mneme caches | Hot Rod binary protocol with auth | `[IMPLEMENTED]` |
| **`7800`** | TCP (JGroups) | `infinispan-1`, `infinispan-2` | `infinispan-service` | Intra-cluster: Node 1 $\leftrightarrow$ Node 2 | JGroups TCP peer discovery and synchronous cache replication | Internal cluster network | `[CONFIGURED]` |
| **`5432`** | TCP (PostgreSQL) | `postgres-1` | `postgres-1` | Intra-cluster: `mnemosyne-clinical` $\rightarrow$ DB | Primary relational store for clinical FHIR resources (`fhir_node_1`) | PostgreSQL SCRAM-SHA-256 Auth | `[CONFIGURED]` |
| **`5433` (mapped)** | TCP (PostgreSQL) | `postgres-2` | `postgres-2` (5432 internal) | Intra-cluster: `mnemosyne-clinical` $\rightarrow$ DB | Secondary relational store for clinical FHIR resources (`fhir_node_2`) | PostgreSQL SCRAM-SHA-256 Auth | `[CONFIGURED]` |
| **`5434` (mapped)** | TCP (PostgreSQL) | `postgres-ops-1` | `postgres-ops-1` (5432 internal) | Intra-cluster: `mnemosyne-operations` $\rightarrow$ DB | Primary relational store for operational states (`ops_node_1`) | PostgreSQL SCRAM-SHA-256 Auth | `[CONFIGURED]` |
| **`5435` (mapped)** | TCP (PostgreSQL) | `postgres-ops-2` | `postgres-ops-2` (5432 internal) | Intra-cluster: `mnemosyne-operations` $\rightarrow$ DB | Secondary relational store for operational states (`ops_node_2`) | PostgreSQL SCRAM-SHA-256 Auth | `[CONFIGURED]` |
| **`5436` (mapped)** | TCP (PostgreSQL) | `postgres-synapse` | `postgres-synapse` (5432 internal) | Intra-cluster: `synapse` $\rightarrow$ DB | Relational store for Matrix Synapse homeserver state (`synapse_db`) | PostgreSQL SCRAM-SHA-256 Auth | `[CONFIGURED]` |
| **`8008`** | TCP (HTTP) | `synapse` | `synapse` | Ingress & Intra-cluster: Agora $\rightarrow$ Synapse | Matrix Client-Server API, Synapse Admin API, and AS transaction egress | Internal HTTP / TLS at Ingress | `[CONFIGURED]` |
| **`8092`** | TCP (HTTP) | `agora` | `agora` | Intra-cluster: Synapse $\rightarrow$ Agora | Matrix Application Service transaction endpoint (`PUT /_matrix/app/v1/transactions/{txnId}`) | Bearer `hs_token` Authentication | `[IMPLEMENTED]` |
| **`9992`** | TCP (HTTP) | `agora` | `agora` | Intra-cluster: K8s Probes $\rightarrow$ Agora | Spring Boot Actuator liveness, readiness, and metrics endpoints | Internal cluster network | `[CONFIGURED]` |

---

## 2. Ingress & External Routing Topology `[CONFIGURED]`

```
https://*.harmonia.local (TLS 443 via Ingress Controller)
  │
  ├── clinical.harmonia.local/      ──► iris-clinical:80 (Clinical SPA)
  ├── console.harmonia.local/       ──► iris-console:80 (Console SPA)
  ├── admin.harmonia.local/         ──► iris-administration:80 (Admin SPA)
  ├── api.harmonia.local/clinical   ──► befe:8080 (Iris BEFE Clinical REST Gateway)
  ├── api.harmonia.local/operations ──► befe:8090 (Iris BEFE Operations REST Gateway)
  ├── gateway.harmonia.local/       ──► mllp-gateway:8080 (Pylai Inbound HTTP Gateway)
  └── matrix.harmonia.local/        ──► synapse:8008 (Matrix Synapse Homeserver)
```

---

## 3. Protocol Invariants & Guardrails `[IMPLEMENTED]`

1. **Dedicated Transport Isolation**:
   - ActiveMQ Artemis clients communicate exclusively via Core Netty on port `61616`. No separate replication ports exist; inter-broker HA clustering and journal replication share the `61616` Netty acceptor.
2. **Infinispan Separation**:
   - Client access occurs strictly over Hot Rod port `11222`. JGroups cluster management is isolated to port `7800`.
3. **Database Port Decoupling**:
   - Within the Kubernetes overlay, all PostgreSQL instances bind to port `5432` behind dedicated Service endpoints (`postgres-1`, `postgres-2`, `postgres-ops-1`, `postgres-ops-2`, `postgres-synapse`). Host mappings (`5432-5436`) apply to Docker Compose and host debugging environments.
