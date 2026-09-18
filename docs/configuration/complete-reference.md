# Harmonia Authoritative Dual-Dimension Configuration Reference `[CONFIGURED]`

This document provides the authoritative, exhaustive dual-dimension configuration reference for the Harmonia Health Integration Environment (HIE). It strictly bifurcates platform configuration into:
1. **Dimension 1: Infrastructure Configuration** — Declarative Kubernetes resource allocations, workload topologies, replica counts, persistent storage volume claims (PVCs), network policies, health probes, and Kubernetes Secrets/ConfigMaps.
2. **Dimension 2: Application Configuration** — JVM runtime options, Spring Boot parameters, Jakarta EE / WildFly configurations, Camel route timeouts, ActiveMQ Artemis broker parameters, Infinispan cache configurations, and Themis security policies.

---

## 1. Architectural Philosophy: Dual-Dimension Configuration Discipline `[IMPLEMENTED]`

Harmonia enforces a strict separation between *how the platform is hosted* (Infrastructure) and *how clinical workflows execute* (Application):
- **Infrastructure Parameters** are externalized into declarative Kubernetes manifests (`deployment/kubernetes/base/`), Kustomize environment overlays (`deployment/kubernetes/environments/microk8s/`), or Ansible playbooks (`deployment/ansible/`). Changes to infrastructure parameters (e.g., scaling replicas, expanding disk allocations, tweaking CPU throttling) never alter application business logic.
- **Application Parameters** are externalized via environment variables, Spring Boot YAML configurations, ConfigMaps, or mounted configuration files (`broker.xml`, `infinispan.xml`, `homeserver.yaml`). Services consume these parameters at bootstrap to configure internal thread pools, cache eviction, database connections, and protocol gateways.

---

## 2. Dimension 1: Infrastructure Configuration Reference `[CONFIGURED]`

### 2.1 Kubernetes Workload Topology & Compute Allocation Matrix

Every container in Harmonia operates under strictly bounded CPU and memory requests and limits to guarantee deterministic execution, prevent noisy-neighbor starvation, and eliminate Out-Of-Memory (OOM) cascading failures.

The table below lists the authoritative compute allocations defined in `deployment/kubernetes/base/`:

| Workload Name | Workload Kind | Architectural Tier | Default Replicas | CPU Request | CPU Limit | Memory Request | Memory Limit | Persistent Volume Claim |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a` | StatefulSet | Tier 4 (Messaging) | 1 | `200m` | `1000m` | `512Mi` | `1024Mi` | `2Gi` (`artemis-primary-a-data`) |
| `artemis-backup-a` | StatefulSet | Tier 4 (Messaging) | 1 | `200m` | `1000m` | `512Mi` | `1024Mi` | `2Gi` (`artemis-backup-a-data`) |
| `artemis-primary-b` | StatefulSet | Tier 4 (Messaging) | 1 | `200m` | `1000m` | `512Mi` | `1024Mi` | `2Gi` (`artemis-primary-b-data`) |
| `artemis-backup-b` | StatefulSet | Tier 4 (Messaging) | 1 | `200m` | `1000m` | `512Mi` | `1024Mi` | `2Gi` (`artemis-backup-b-data`) |
| `infinispan-1` | StatefulSet | Tier 4 (Cache Grid) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | `1Gi` (`mneme-data-1`) |
| `infinispan-2` | StatefulSet | Tier 4 (Cache Grid) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | `1Gi` (`mneme-data-2`) |
| `postgres-1` | StatefulSet | Tier 5 (Clinical DB 1) | 1 | `100m` | `1000m` | `256Mi` | `1024Mi` | `2Gi` (`postgres-data-1`) |
| `postgres-2` | StatefulSet | Tier 5 (Clinical DB 2) | 1 | `100m` | `1000m` | `256Mi` | `1024Mi` | `2Gi` (`postgres-data-2`) |
| `postgres-ops-1` | StatefulSet | Tier 5 (Ops DB 1) | 1 | `100m` | `1000m` | `256Mi` | `1024Mi` | `2Gi` (`postgres-ops-data-1`) |
| `postgres-ops-2` | StatefulSet | Tier 5 (Ops DB 2) | 1 | `100m` | `1000m` | `256Mi` | `1024Mi` | `2Gi` (`postgres-ops-data-2`) |
| `postgres-synapse` | StatefulSet | Tier 5 (Synapse DB) | 1 | `100m` | `1000m` | `256Mi` | `1024Mi` | `2Gi` (`postgres-synapse-data`) |
| `synapse` | StatefulSet | Tier 3 (Collaboration) | 1 | `250m` | `1000m` | `512Mi` | `2048Mi` | `5Gi` (`synapse-data`) |
| `hapi-fhir-jpa-server-1` | Deployment | Tier 5 (FHIR JPA Node 1) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `hapi-fhir-jpa-server-2` | Deployment | Tier 5 (FHIR JPA Node 2) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `hie-operations-jpa-server-1` | Deployment | Tier 5 (Ops JPA Node 1) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `hie-operations-jpa-server-2` | Deployment | Tier 5 (Ops JPA Node 2) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `task-processor` (Ponos) | Deployment | Tier 3 (WorkEngine) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `mllp-gateway` (Pylai In) | Deployment | Tier 2 (Ingress Gateway) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `mllp-outbound-his` | Deployment | Tier 2 (Egress Gateway) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `mllp-outbound-lis` | Deployment | Tier 2 (Egress Gateway) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `befe` (Iris BEFE) | Deployment | Tier 2 (Presentation API) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |
| `iris-clinical` | Deployment | Tier 1 (Clinical SPA) | 1 | `50m` | `500m` | `64Mi` | `256Mi` | *None (Stateless)* |
| `iris-console` | Deployment | Tier 1 (Console SPA) | 1 | `50m` | `500m` | `64Mi` | `256Mi` | *None (Stateless)* |
| `iris-administration` | Deployment | Tier 1 (Admin SPA) | 1 | `50m` | `500m` | `64Mi` | `256Mi` | *None (Stateless)* |
| `agora` | Deployment | Tier 2 (Collaboration AS) | 1 | `250m` | `1000m` | `512Mi` | `1536Mi` | *None (Stateless)* |

**Cluster Aggregate Sizing Summary**:
- **Total Pods**: 25 pods (12 StatefulSets, 13 Deployments)
- **Minimum CPU Request**: 4.05 cores (Base reservation)
- **Maximum CPU Limit**: 23.5 cores (Burst capacity)
- **Minimum Memory Request**: 9.47 GB RAM (Base reservation)
- **Maximum Memory Limit**: 28.5 GB RAM (Burst capacity)
- **Total Persistent Storage Claims**: 25 GB host-backed storage across 12 PVCs

---

### 2.2 Storage Volume Claims & StorageClass Configuration `[CONFIGURED]`

In MicroK8s, dynamic persistent volume provisioning is managed by the `hostpath-storage` add-on via the `microk8s-hostpath` StorageClass.

```yaml
# Kustomize Patch: deployment/kubernetes/environments/microk8s/patches/storage-class-patch.yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: storage-class-patch
spec:
  volumeClaimTemplates:
    - metadata:
        name: data
      spec:
        storageClassName: microk8s-hostpath
        accessModes:
          - ReadWriteOnce
```

**Authoritative Persistent Storage Register**:

| Volume Claim Name | Bound Workload | Storage Size | Mount Path | Storage Class | Retention Policy | Purpose |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a-data` | `artemis-primary-a` | `2Gi` | `/var/lib/artemis/data` | `microk8s-hostpath` | Retain on undeploy | Group A primary message journal, bindings, paging |
| `artemis-backup-a-data` | `artemis-backup-a` | `2Gi` | `/var/lib/artemis/data` | `microk8s-hostpath` | Retain on undeploy | Group A backup replicated message journal |
| `artemis-primary-b-data` | `artemis-primary-b` | `2Gi` | `/var/lib/artemis/data` | `microk8s-hostpath` | Retain on undeploy | Group B primary message journal, bindings, paging |
| `artemis-backup-b-data` | `artemis-backup-b` | `2Gi` | `/var/lib/artemis/data` | `microk8s-hostpath` | Retain on undeploy | Group B backup replicated message journal |
| `mneme-data-1` | `infinispan-1` | `1Gi` | `/opt/infinispan/server/data` | `microk8s-hostpath` | Retain on undeploy | Infinispan node 1 cluster state & internal cache data |
| `mneme-data-2` | `infinispan-2` | `1Gi` | `/opt/infinispan/server/data` | `microk8s-hostpath` | Retain on undeploy | Infinispan node 2 cluster state & internal cache data |
| `postgres-data-1` | `postgres-1` | `2Gi` | `/var/lib/postgresql/data` | `microk8s-hostpath` | Retain on undeploy | Clinical database node 1 relational tables (`fhir_node_1`) |
| `postgres-data-2` | `postgres-2` | `2Gi` | `/var/lib/postgresql/data` | `microk8s-hostpath` | Retain on undeploy | Clinical database node 2 relational tables (`fhir_node_2`) |
| `postgres-ops-data-1` | `postgres-ops-1` | `2Gi` | `/var/lib/postgresql/data` | `microk8s-hostpath` | Retain on undeploy | Operations database node 1 relational tables (`ops_node_1`) |
| `postgres-ops-data-2` | `postgres-ops-2` | `2Gi` | `/var/lib/postgresql/data` | `microk8s-hostpath` | Retain on undeploy | Operations database node 2 relational tables (`ops_node_2`) |
| `postgres-synapse-data` | `postgres-synapse` | `2Gi` | `/var/lib/postgresql/data` | `microk8s-hostpath` | Retain on undeploy | Matrix Synapse homeserver state database (`synapse_db`) |
| `synapse-data` | `synapse` | `5Gi` | `/data` | `microk8s-hostpath` | Retain on undeploy | Matrix media store, signing keys, and runtime artifacts |

---

### 2.3 Container Health Probes Register `[CONFIGURED]`

Harmonia relies on fine-grained Kubernetes Liveness and Readiness probes to guarantee that traffic is routed only to fully initialized pods and that hung instances are automatically restarted.

| Workload Name | Probe Type | Probe Mechanism | Target Path / Port | Initial Delay | Period | Timeout | Failure Threshold |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-*` (all 4) | Liveness | `tcpSocket` | Port `61616` | 20s | 10s | 5s | 3 |
| `artemis-*` (all 4) | Readiness | `tcpSocket` | Port `61616` | 15s | 5s | 3s | 3 |
| `infinispan-*` (both) | Liveness | `tcpSocket` | Port `11222` | 30s | 15s | 5s | 3 |
| `infinispan-*` (both) | Readiness | `tcpSocket` | Port `11222` | 20s | 10s | 5s | 3 |
| `postgres-*` (all 5) | Liveness | `exec` | `pg_isready -U <user> -d <db>` | 15s | 10s | 5s | 3 |
| `postgres-*` (all 5) | Readiness | `exec` | `pg_isready -U <user> -d <db>` | 5s | 5s | 3s | 3 |
| `synapse` | Liveness | `httpGet` | `/_matrix/client/versions` (8008) | 45s | 15s | 5s | 3 |
| `synapse` | Readiness | `httpGet` | `/health` (8008) | 30s | 10s | 5s | 3 |
| `hapi-fhir-jpa-server-*` | Liveness | `httpGet` | `/actuator/health` (8080) | 30s | 15s | 5s | 3 |
| `hapi-fhir-jpa-server-*` | Readiness | `httpGet` | `/actuator/health` (8080) | 20s | 10s | 5s | 3 |
| `hie-operations-jpa-server-*` | Liveness | `httpGet` | `/actuator/health` (8080) | 30s | 15s | 5s | 3 |
| `hie-operations-jpa-server-*` | Readiness | `httpGet` | `/actuator/health` (8080) | 20s | 10s | 5s | 3 |
| `task-processor` (Ponos) | Liveness | `tcpSocket` | Port `8080` | 30s | 15s | 5s | 3 |
| `task-processor` (Ponos) | Readiness | `tcpSocket` | Port `8080` | 20s | 10s | 5s | 3 |
| `mllp-gateway` (Pylai In) | Liveness | `tcpSocket` | Port `8080` | 30s | 15s | 5s | 3 |
| `mllp-gateway` (Pylai In) | Readiness | `tcpSocket` | Port `8080` | 20s | 10s | 5s | 3 |
| `mllp-outbound-*` (both) | Liveness | `tcpSocket` | Port `8080` | 30s | 15s | 5s | 3 |
| `mllp-outbound-*` (both) | Readiness | `tcpSocket` | Port `8080` | 20s | 10s | 5s | 3 |
| `befe` (Iris BEFE) | Liveness | `tcpSocket` | Port `8080` | 30s | 15s | 5s | 3 |
| `befe` (Iris BEFE) | Readiness | `tcpSocket` | Port `8080` | 20s | 10s | 5s | 3 |
| `iris-clinical` | Liveness | `httpGet` | `/` (80) | 10s | 15s | 3s | 3 |
| `iris-clinical` | Readiness | `httpGet` | `/` (80) | 5s | 10s | 3s | 3 |
| `iris-console` | Liveness | `httpGet` | `/` (80) | 10s | 15s | 3s | 3 |
| `iris-console` | Readiness | `httpGet` | `/` (80) | 5s | 10s | 3s | 3 |
| `iris-administration` | Liveness | `httpGet` | `/` (80) | 10s | 15s | 3s | 3 |
| `iris-administration` | Readiness | `httpGet` | `/` (80) | 5s | 10s | 3s | 3 |
| `agora` | Liveness | `httpGet` | `/actuator/health/liveness` (9992) | 30s | 15s | 5s | 3 |
| `agora` | Readiness | `httpGet` | `/actuator/health/readiness` (9992) | 20s | 10s | 5s | 3 |

---

### 2.4 Kubernetes Secrets & ConfigMaps Inventory `[CONFIGURED]`

Harmonia externalizes sensitive credentials via Kubernetes Secrets and declarative configurations via ConfigMaps:

#### Secrets Register
1. **`harmonia-db-secrets`**:
   - `fhir-password`: PostgreSQL password for `fhir_user` accessing `fhir_node_1` and `fhir_node_2`.
   - `ops-password`: PostgreSQL password for `ops_user` accessing `ops_node_1` and `ops_node_2`.
2. **`harmonia-artemis-secrets`**:
   - `admin-password`: Administrator password for ActiveMQ Artemis management console and core operations.
   - `cluster-password`: Symmetric password for inter-broker cluster discovery (`artemisClusterPassword`).
3. **`harmonia-infinispan-secrets`**:
   - `admin-password`: Administrative credentials for Hot Rod authentication and management.
4. **`harmonia-synapse-secrets`**:
   - `registration-shared-secret`: Shared secret for automated user registration API.
   - `macaroon-secret-key`: Cryptographic HMAC key for generating Macaroon access tokens.
   - `form-secret`: CSRF protection token for Synapse web forms.
   - `db-password`: PostgreSQL password for `synapse_user` accessing `synapse_db`.
5. **`harmonia-agora-secrets`**:
   - `hs-token`: Homeserver token used by Synapse to authenticate inbound transactions to Agora (`hs_token`).
   - `as-token`: Application Service token used by Agora to authenticate API calls to Synapse (`as_token`).
   - `synapse-admin-token`: Synapse administrator access token used for administrative user provisioning.

#### ConfigMaps Register
1. **`agora-config`**:
   - `AGORA_SYNAPSE_BASE_URL`: Homeserver endpoint (`http://synapse:8008`).
   - `AGORA_SERVER_NAME`: Domain namespace (`harmonia.local`).
   - `AGORA_APPSERVICE_ID`: Registered AS identifier (`harmonia-agora`).
   - `AGORA_RECONCILIATION_CRON`: Cron expression for practitioner room sync (`0 */15 * * * *`).
2. **`synapse-config`**:
   - `homeserver.yaml`: Master Synapse server configuration (non-federated, retention rules, listener ports).
3. **`synapse-appservice-registration`**:
   - `appservice-agora.yaml`: Declarative AS descriptor registering Agora regex namespaces (`@_harmonia_.*`, `#_harmonia_.*`).
4. **`artemis-primary-a-config`**, **`artemis-backup-a-config`**, **`artemis-primary-b-config`**, **`artemis-backup-b-config`**:
   - `broker.xml`: Master broker configuration defining journal directories, connectors, cluster-connections, and `#` address-settings.

---

## 3. Dimension 2: Application Configuration Reference `[CONFIGURED]`

### 3.1 Global & Common Environment Variables

| Variable Name | Target Workloads | Default Value | Description & Code Binding |
| :--- | :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Spring Boot Services (`mnemosyne-*`, `agora`) | `postgres` | Selects active database profile (`h2` for unit tests, `postgres` for production). |
| `SERVER_PORT` | Spring Boot Services | `8080` (or `8092` for Agora) | HTTP server port for embedded Tomcat/Jetty. |
| `MANAGEMENT_SERVER_PORT` | Spring Boot Services | `9992` (Agora) | Dedicated port for Spring Boot Actuator endpoints. |
| `JAVA_OPTIONS` / `JAVA_TOOL_OPTIONS` | All Java containers | `-Xms256m -Xmx1024m` | JVM heap parameters and system properties. |

---

### 3.2 Messaging Subsystem Configuration (Petasos & Artemis)

| Parameter / Variable Name | Target Module | Default Value | Description & Repository Grounding |
| :--- | :--- | :--- | :--- |
| `PETASOS_BROKER_URL` | `petasos-artemis`, `pylai`, `ponos`, `agora` | `(tcp://artemis-primary-a:61616,tcp://artemis-backup-a:61616,tcp://artemis-primary-b:61616,tcp://artemis-backup-b:61616)?ha=true&reconnectAttempts=-1` | Clustered HA failover transport URL. Binds to `PetasosArtemisConnectionFactory`. |
| `PETASOS_SESSION_POOL_SIZE` | `petasos-artemis` | `20` | Pooled JMS session count per container in `ArtemisSessionPool`. |
| `PETASOS_DEDUP_CACHE_SIZE` | `petasos-core` | `10000` | In-memory sliding-window duplicate detection capacity (`DuplicateDetector`). |
| `PETASOS_MAX_REDELIVERY` | ActiveMQ Artemis (`broker.xml`) | `3` | Maximum delivery attempts before routing to `DLQ`. |
| `PETASOS_REDELIVERY_DELAY` | ActiveMQ Artemis (`broker.xml`) | `1000` | Initial redelivery delay in milliseconds. |
| `PETASOS_REDELIVERY_MULTIPLIER`| ActiveMQ Artemis (`broker.xml`) | `1.5` | Exponential backoff factor for retries. |
| `PETASOS_PAGE_MAX_SIZE` | ActiveMQ Artemis (`broker.xml`) | `104857600` (100 MB) | Disk paging threshold per address. |
| `PETASOS_PAGE_SIZE` | ActiveMQ Artemis (`broker.xml`) | `10485760` (10 MB) | Paging file segment size. |
| `PETASOS_DLQ_ADDRESS` | ActiveMQ Artemis (`broker.xml`) | `DLQ` | Target address for dead-lettered payloads. |
| `PETASOS_EXPIRY_ADDRESS` | ActiveMQ Artemis (`broker.xml`) | `ExpiryQueue` | Target address for expired messages. |

---

### 3.3 In-Memory Cache Grid Configuration (Mneme & Infinispan)

| Parameter / Variable Name | Target Module | Default Value | Description & Repository Grounding |
| :--- | :--- | :--- | :--- |
| `INFINISPAN_HOST` | `pylai`, `ponos`, `iris-befe` | `infinispan-1` | Hostname of primary Infinispan cache instance. |
| `INFINISPAN_PORT` | `pylai`, `ponos`, `iris-befe` | `11222` | Hot Rod binary client port. |
| `INFINISPAN_USER` | `pylai`, `ponos`, `iris-befe` | `admin` | Hot Rod authentication username. |
| `INFINISPAN_PASSWORD` | `pylai`, `ponos`, `iris-befe` | `admin` | Hot Rod authentication password. |
| `HARMONIA_CACHE_SERVERS` | `iris-befe`, `ponos` | `infinispan-1:11222;infinispan-2:11222` | Multi-node Hot Rod bootstrap connection string. |
| `-Dinfinispan.node.name` | `mneme-cluster` | `node1` / `node2` | JGroups unique cluster node identity. |
| `-Dfhir.server.url` | `mneme-cluster` | `http://hapi-fhir-jpa-server-1:8080/fhir` | Target clinical REST persistence endpoint for write-behind store. |
| `-Dops.server.url` | `mneme-cluster` | `http://hie-operations-jpa-server-1:8080/api/operations` | Target operations REST persistence endpoint for write-behind store. |

**Infinispan Cache Architecture Settings (`infinispan.xml`)**:
- **Cache Mode**: `mode="SYNC"` (All 17 caches operate in Synchronous Replication mode; distributed mode is not used).
- **Encoding**: `<encoding media-type="text/plain"/>` for keys and values.
- **Write-Behind Persistence**: Asynchronous write-behind store with `modification-queue-size="1024"`.
- **TTL / Expiration**: Immortal by default (no lifespan/max-idle defined in config; evictions managed explicitly by business logic).

---

### 3.4 Relational Persistence Configuration (Mnemosyne & PostgreSQL)

| Parameter / Variable Name | Target Module | Default Value | Description & Code Binding |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `mnemosyne-clinical` | `jdbc:postgresql://postgres-1:5432/fhir_node_1` | JDBC URL for clinical primary node 1. |
| `SPRING_DATASOURCE_USERNAME` | `mnemosyne-clinical` | `fhir_user` | Database user for clinical schemas. |
| `SPRING_DATASOURCE_PASSWORD` | `mnemosyne-clinical` | Secret `harmonia-db-secrets/fhir-password` | Database password for clinical schemas. |
| `SPRING_DATASOURCE_URL` | `mnemosyne-operations` | `jdbc:postgresql://postgres-ops-1:5432/ops_node_1` | JDBC URL for operations primary node 1. |
| `SPRING_DATASOURCE_USERNAME` | `mnemosyne-operations` | `ops_user` | Database user for operational telemetry schemas. |
| `SPRING_DATASOURCE_PASSWORD` | `mnemosyne-operations` | Secret `harmonia-db-secrets/ops-password` | Database password for operational schemas. |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `mnemosyne-*` | `update` | Hibernate schema management strategy. |
| `SPRING_DATASOURCE_HIKARI_MAX_POOL_SIZE` | `mnemosyne-*`, `agora` | `20` | Maximum HikariCP connection pool size. |
| `SPRING_DATASOURCE_HIKARI_MIN_IDLE` | `mnemosyne-*`, `agora` | `5` | Minimum idle database connections. |
| `SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT`| `mnemosyne-*` | `30000` (30s) | Maximum wait time for database connection from pool. |

---

### 3.5 Protocol Gateways & Workflow Processing Configuration

| Parameter / Variable Name | Target Module | Default Value | Description & Code Binding |
| :--- | :--- | :--- | :--- |
| `MLLP_HOST` | `pylai-mllp-in` | `0.0.0.0` | Inbound HL7 MLLP network bind host. |
| `MLLP_PORT` | `pylai-mllp-in` | `2575` | Inbound HL7 MLLP TCP listener port. |
| `TASK_BROKER_HOST` | `pylai-mllp-in`, `ponos` | `task-processor` (or `0.0.0.0`) | Host address of task broker. |
| `TASK_BROKER_PORT` | `pylai-mllp-in`, `ponos` | `61616` | Port of task broker. |
| `TASK_PROCESSOR_URL` | `pylai-mllp-in` | `http://task-processor:8080/api/queue/event` | HTTP notification endpoint for inbound events. |
| `MLLP_OUTBOUND_INSTANCE_ID` | `pylai-mllp-out` | `mllp-sender-his` / `mllp-sender-lis` | Unique gateway instance identifier. |
| `MLLP_OUTBOUND_TARGET_ENDPOINT_ID` | `pylai-mllp-out` | `HIS_NORTH` / `LIS_MAIN` | Target external downstream destination identifier. |
| `MLLP_OUTBOUND_QUEUE_PREFIX` | `pylai-mllp-out` | `petasos.queue.mllp.outbound` | Prefix for dedicated outbound Petasos queues. |
| `MLLP_OUTBOUND_PORT` | `pylai-mllp-out` | `8080` (or `8087`/`8088` in Compose) | Internal HTTP status port for outbound sender. |
| `TASK_PROCESSOR_BROKER_URL` | `pylai-mllp-out` | `tcp://task-processor:61616` | Broker URL for outbound Petasos queue consumption. |
| `OPERATIONS_PORT` | `iris-befe` | `8090` | Port for BEFE operations, metrics, and queue inspection. |
| `MANAGEMENT_PORT` | `iris-befe`, `ponos`, `pylai` | `9990` | WildFly management interface port. |

---

### 3.6 Agora Collaboration & Matrix Synapse Configuration

| Parameter / Variable Name | Target Module | Default Value | Description & Code Binding |
| :--- | :--- | :--- | :--- |
| `AGORA_SYNAPSE_BASE_URL` | `agora-service` | `http://synapse:8008` | URL of Matrix Synapse homeserver for CS/Admin REST APIs. |
| `AGORA_SERVER_NAME` | `agora-service`, `synapse` | `harmonia.local` | Server name domain for Matrix room aliases and user IDs. |
| `AGORA_APPSERVICE_ID` | `agora-service` | `harmonia-agora` | Application Service unique ID matching `appservice-agora.yaml`. |
| `AGORA_RECONCILIATION_CRON` | `agora-service` | `0 */15 * * * *` | Spring scheduled cron for practitioner membership reconciliation. |
| `AGORA_HOMESERVER_TOKEN` | `agora-service` | Secret `harmonia-agora-secrets/hs-token` | Inbound authentication token passed by Synapse in HTTP header. |
| `AGORA_APPSERVICE_TOKEN` | `agora-service` | Secret `harmonia-agora-secrets/as-token` | Outbound authentication token used by Agora for Synapse requests. |
| `AGORA_ADMIN_TOKEN` | `agora-service` | Secret `harmonia-agora-secrets/synapse-admin-token` | Synapse Admin API access token for user provisioning. |
| `SYNAPSE_SERVER_NAME` | `synapse` | `harmonia.local` | Matrix server domain. |
| `SYNAPSE_REPORT_STATS` | `synapse` | `no` | Telemetry reporting flag (strictly disabled for PHI safety). |
| `SYNAPSE_CONFIG_PATH` | `synapse` | `/data/homeserver.yaml` | Path to master homeserver configuration. |
| `SYNAPSE_RETENTION_MIN_LIFETIME` | `synapse` (`homeserver.yaml`) | `1d` | Minimum retention period for clinical discussion rooms. |
| `SYNAPSE_RETENTION_MAX_LIFETIME` | `synapse` (`homeserver.yaml`) | `90d` | Maximum retention period for clinical discussion rooms. |

---

## 4. Verification & Consistency Audit `[IMPLEMENTED]`

To ensure zero drift between documentation and runtime configuration:
1. **Manifest Alignment**: Every compute request, limit, volume claim, and probe parameter matches `deployment/kubernetes/base/` byte-for-byte.
2. **Artemis Grounding**: Redelivery parameters (attempts=3, delay=1000ms, multiplier=1.5, queues `DLQ` and `ExpiryQueue`) align strictly with `petasos/deployment/artemis/*/broker.xml`.
3. **Infinispan Grounding**: Cache mode (`SYNC`), encoding (`text/plain`), and store bindings reflect `hestia/mneme-cluster/src/main/resources/infinispan.xml`.
4. **Decoupling Invariants**: Iris BEFE and SPAs contain zero direct database credentials or JPA connection strings, enforcing Invariant 3.
