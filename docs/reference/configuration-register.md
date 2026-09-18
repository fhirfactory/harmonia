# Harmonia Configuration Register `[CONFIGURED]`

This register provides the formal, comprehensive configuration property matrix across the Harmonia Health Integration Environment. It details parameter names, categories, bound modules/workloads, default values, configuration files/locations, and authoritative status classifications.

---

## 1. Register Classification Standard `[IMPLEMENTED]`

All parameters are tagged with explicit four-tier status markers:
- `[IMPLEMENTED]`: Backed by active Java/TypeScript code, Spring `@Value`/`@ConfigurationProperties`, or Jakarta CDI bindings.
- `[CONFIGURED]`: Deployed and parameterized via Kubernetes YAML, Helm/Kustomize, or Docker Compose manifests.
- `[DESIGNED/PLANNED]`: Architectural targets reserved for future enterprise extensions.
- `[EXAMPLE/REFERENCE]`: Sample fixtures or development placeholders.

---

## 2. Infrastructure Configuration Parameters `[CONFIGURED]`

### 2.1 Kubernetes Workload Topology & Compute Bounds

| Workload Name | Kind | Replicas | CPU Request / Limit | Memory Request / Limit | PVC Name / Mount | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a` | StatefulSet | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `artemis-primary-a-data` (`2Gi`) | `[CONFIGURED]` |
| `artemis-backup-a` | StatefulSet | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `artemis-backup-a-data` (`2Gi`) | `[CONFIGURED]` |
| `artemis-primary-b` | StatefulSet | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `artemis-primary-b-data` (`2Gi`) | `[CONFIGURED]` |
| `artemis-backup-b` | StatefulSet | 1 | `200m` / `1000m` | `512Mi` / `1024Mi` | `artemis-backup-b-data` (`2Gi`) | `[CONFIGURED]` |
| `infinispan-1` | StatefulSet | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | `mneme-data-1` (`1Gi`) | `[CONFIGURED]` |
| `infinispan-2` | StatefulSet | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | `mneme-data-2` (`1Gi`) | `[CONFIGURED]` |
| `postgres-1` | StatefulSet | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `postgres-data-1` (`2Gi`) | `[CONFIGURED]` |
| `postgres-2` | StatefulSet | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `postgres-data-2` (`2Gi`) | `[CONFIGURED]` |
| `postgres-ops-1` | StatefulSet | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `postgres-ops-data-1` (`2Gi`) | `[CONFIGURED]` |
| `postgres-ops-2` | StatefulSet | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `postgres-ops-data-2` (`2Gi`) | `[CONFIGURED]` |
| `postgres-synapse` | StatefulSet | 1 | `100m` / `1000m` | `256Mi` / `1024Mi` | `postgres-synapse-data` (`2Gi`) | `[CONFIGURED]` |
| `synapse` | StatefulSet | 1 | `250m` / `1000m` | `512Mi` / `2048Mi` | `synapse-data` (`5Gi`) | `[CONFIGURED]` |
| `hapi-fhir-jpa-server-1` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `hapi-fhir-jpa-server-2` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `hie-operations-jpa-server-1` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `hie-operations-jpa-server-2` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `task-processor` (Ponos) | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `mllp-gateway` (Pylai In) | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `mllp-outbound-his` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `mllp-outbound-lis` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `befe` (Iris BEFE) | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |
| `iris-clinical` | Deployment | 1 | `50m` / `500m` | `64Mi` / `256Mi` | None (Stateless) | `[CONFIGURED]` |
| `iris-console` | Deployment | 1 | `50m` / `500m` | `64Mi` / `256Mi` | None (Stateless) | `[CONFIGURED]` |
| `iris-administration` | Deployment | 1 | `50m` / `500m` | `64Mi` / `256Mi` | None (Stateless) | `[CONFIGURED]` |
| `agora` | Deployment | 1 | `250m` / `1000m` | `512Mi` / `1536Mi` | None (Stateless) | `[CONFIGURED]` |

---

### 2.2 Kubernetes Health Probes Configuration

| Workload Name | Probe | Type | Endpoint / Port | Delay | Period | Timeout | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-*` (all 4) | Liveness / Readiness | `tcpSocket` | Port `61616` | 20s / 15s | 10s / 5s | 5s / 3s | `[CONFIGURED]` |
| `infinispan-*` (both) | Liveness / Readiness | `tcpSocket` | Port `11222` | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `postgres-*` (all 5) | Liveness / Readiness | `exec` | `pg_isready -U <user> -d <db>` | 15s / 5s | 10s / 5s | 5s / 3s | `[CONFIGURED]` |
| `synapse` | Liveness | `httpGet` | `/_matrix/client/versions` (8008) | 45s | 15s | 5s | `[CONFIGURED]` |
| `synapse` | Readiness | `httpGet` | `/health` (8008) | 30s | 10s | 5s | `[CONFIGURED]` |
| `hapi-fhir-jpa-server-*` | Liveness / Readiness | `httpGet` | `/actuator/health` (8080) | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `hie-operations-jpa-server-*`| Liveness / Readiness | `httpGet` | `/actuator/health` (8080) | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `task-processor` (Ponos) | Liveness / Readiness | `tcpSocket` | Port `8080` | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `mllp-gateway` (Pylai In) | Liveness / Readiness | `tcpSocket` | Port `8080` | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `mllp-outbound-*` (both) | Liveness / Readiness | `tcpSocket` | Port `8080` | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `befe` (Iris BEFE) | Liveness / Readiness | `tcpSocket` | Port `8080` | 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |
| `iris-*` (all 3 SPAs) | Liveness / Readiness | `httpGet` | `/` (80) | 10s / 5s | 15s / 10s | 3s / 3s | `[CONFIGURED]` |
| `agora` | Liveness / Readiness | `httpGet` | `/actuator/health/{liveness,readiness}` (9992)| 30s / 20s | 15s / 10s | 5s / 5s | `[CONFIGURED]` |

---

### 2.3 Kubernetes Secrets and ConfigMaps

| Resource Name | Kind | Key / Property | Default / Value Reference | Target Component | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `harmonia-db-secrets` | Secret | `fhir-password` | Secret string (`fhir_password`) | `mnemosyne-clinical`, `postgres-1/2` | `[CONFIGURED]` |
| `harmonia-db-secrets` | Secret | `ops-password` | Secret string (`ops_password`) | `mnemosyne-operations`, `postgres-ops-1/2`, `agora` | `[CONFIGURED]` |
| `harmonia-artemis-secrets` | Secret | `admin-password` | Secret string (`admin`) | `artemis-*` StatefulSets | `[CONFIGURED]` |
| `harmonia-artemis-secrets` | Secret | `cluster-password` | Secret string (`artemisClusterPassword`)| `artemis-*` StatefulSets | `[CONFIGURED]` |
| `harmonia-infinispan-secrets`| Secret | `admin-password` | Secret string (`admin`) | `infinispan-*` StatefulSets | `[CONFIGURED]` |
| `harmonia-synapse-secrets` | Secret | `registration-shared-secret` | Cryptographic hex token | `synapse` StatefulSet | `[CONFIGURED]` |
| `harmonia-synapse-secrets` | Secret | `macaroon-secret-key` | Cryptographic HMAC token | `synapse` StatefulSet | `[CONFIGURED]` |
| `harmonia-synapse-secrets` | Secret | `form-secret` | CSRF secret string | `synapse` StatefulSet | `[CONFIGURED]` |
| `harmonia-synapse-secrets` | Secret | `db-password` | PostgreSQL password (`synapse_db_password`)| `synapse`, `postgres-synapse` | `[CONFIGURED]` |
| `harmonia-agora-secrets` | Secret | `hs-token` | Inbound bearer token | `agora`, `synapse` | `[CONFIGURED]` |
| `harmonia-agora-secrets` | Secret | `as-token` | Outbound bearer token | `agora`, `synapse` | `[CONFIGURED]` |
| `harmonia-agora-secrets` | Secret | `synapse-admin-token` | Matrix Admin API token | `agora` | `[CONFIGURED]` |
| `agora-config` | ConfigMap | `AGORA_SYNAPSE_BASE_URL` | `http://synapse:8008` | `agora` | `[CONFIGURED]` |
| `agora-config` | ConfigMap | `AGORA_SERVER_NAME` | `harmonia.local` | `agora` | `[CONFIGURED]` |
| `agora-config` | ConfigMap | `AGORA_APPSERVICE_ID` | `harmonia-agora` | `agora` | `[CONFIGURED]` |
| `agora-config` | ConfigMap | `AGORA_RECONCILIATION_CRON` | `0 */15 * * * *` | `agora` | `[CONFIGURED]` |
| `synapse-config` | ConfigMap | `homeserver.yaml` | YAML configuration file | `synapse` | `[CONFIGURED]` |
| `synapse-appservice-registration`| ConfigMap| `appservice-agora.yaml` | AS descriptor file | `synapse` | `[CONFIGURED]` |
| `artemis-*-config` | ConfigMap | `broker.xml` | XML broker configuration | `artemis-*` StatefulSets | `[CONFIGURED]` |

---

## 3. Application Configuration Parameters `[CONFIGURED]`

### 3.1 Messaging Subsystem (Petasos & ActiveMQ Artemis)

| Parameter / Variable | Scope / File | Default Value | Purpose | Status |
| :--- | :--- | :--- | :--- | :--- |
| `PETASOS_BROKER_URL` | Environment Variable | `(tcp://artemis-primary-a:61616,...)?ha=true` | Clustered HA connection URL | `[IMPLEMENTED]` |
| `PETASOS_SESSION_POOL_SIZE` | Java Config (`petasos-artemis`) | `20` | Pooled JMS sessions per container | `[IMPLEMENTED]` |
| `PETASOS_DEDUP_CACHE_SIZE` | Java Config (`petasos-core`) | `10000` | In-memory message duplicate cache size | `[IMPLEMENTED]` |
| `max-delivery-attempts` | `broker.xml` (`#` setting) | `3` | Attempts before routing message to DLQ | `[CONFIGURED]` |
| `redelivery-delay` | `broker.xml` (`#` setting) | `1000` (ms) | Initial redelivery backoff delay | `[CONFIGURED]` |
| `redelivery-delay-multiplier`| `broker.xml` (`#` setting) | `1.5` | Exponential retry factor | `[CONFIGURED]` |
| `dead-letter-address` | `broker.xml` (`#` setting) | `DLQ` | Target address for undeliverable messages| `[CONFIGURED]` |
| `expiry-address` | `broker.xml` (`#` setting) | `ExpiryQueue` | Target address for expired messages | `[CONFIGURED]` |
| `max-size-bytes` | `broker.xml` (`#` setting) | `104857600` (100 MB) | Disk paging memory threshold | `[CONFIGURED]` |
| `page-size-bytes` | `broker.xml` (`#` setting) | `10485760` (10 MB) | Journal page block size | `[CONFIGURED]` |
| `address-full-policy` | `broker.xml` (`#` setting) | `PAGE` | Flow control strategy when memory full | `[CONFIGURED]` |
| `protocols` | `broker.xml` (`acceptors`) | `CORE,AMQP,OPENWIRE` | Enabled wire protocols on port 61616 | `[CONFIGURED]` |

---

### 3.2 In-Memory Caching (Mneme & Infinispan)

| Parameter / Variable | Scope / File | Default Value | Purpose | Status |
| :--- | :--- | :--- | :--- | :--- |
| `INFINISPAN_HOST` | Environment Variable | `infinispan-1` | Hostname of primary cache node | `[CONFIGURED]` |
| `INFINISPAN_PORT` | Environment Variable | `11222` | Hot Rod binary client RPC port | `[CONFIGURED]` |
| `HARMONIA_CACHE_SERVERS` | Environment Variable | `infinispan-1:11222;infinispan-2:11222` | Clustered Hot Rod endpoint list | `[CONFIGURED]` |
| `cache-container` mode | `infinispan.xml` | `SYNC` | Synchronous replication across cluster | `[CONFIGURED]` |
| `encoding media-type` | `infinispan.xml` | `text/plain` | Serialized value encoding format | `[CONFIGURED]` |
| `modification-queue-size` | `infinispan.xml` | `1024` | Write-behind store queue capacity | `[CONFIGURED]` |
| `infinispan.node.name` | JVM System Property | `node1` / `node2` | Node identifier in JGroups cluster | `[CONFIGURED]` |
| `fhir.server.url` | JVM System Property | `http://hapi-fhir-jpa-server-1:8080/fhir`| Write-behind clinical REST store target | `[CONFIGURED]` |
| `ops.server.url` | JVM System Property | `http://hie-operations-jpa-server-1:8080/...`| Write-behind operations REST store target| `[CONFIGURED]` |

---

### 3.3 Relational Persistence (Mnemosyne & PostgreSQL)

| Parameter / Variable | Scope / File | Default Value | Purpose | Status |
| :--- | :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | Environment Variable | `jdbc:postgresql://postgres-1:5432/fhir_node_1` | Clinical database JDBC connection string | `[CONFIGURED]` |
| `SPRING_DATASOURCE_USERNAME` | Environment Variable | `fhir_user` / `ops_user` | Database user identity | `[CONFIGURED]` |
| `SPRING_DATASOURCE_PASSWORD` | Environment Variable | Secret reference | Database user credentials | `[CONFIGURED]` |
| `hikari.maximum-pool-size` | Spring Boot YAML | `20` | Max pooled connections per container | `[IMPLEMENTED]` |
| `hikari.minimum-idle` | Spring Boot YAML | `5` | Minimum standby connections in pool | `[IMPLEMENTED]` |
| `hikari.connection-timeout`| Spring Boot YAML | `30000` (30s) | Max wait time before connection timeout | `[IMPLEMENTED]` |
| `hikari.leak-detection-threshold`| Spring Boot YAML | `60000` (60s) | Warn on unclosed connection leak | `[IMPLEMENTED]` |
| `hibernate.ddl-auto` | Spring Boot YAML | `update` | Schema lifecycle strategy | `[CONFIGURED]` |

---

### 3.4 Protocol Gateways & Runtime Execution (Pylai, Ponos, Iris)

| Parameter / Variable | Scope / File | Default Value | Purpose | Status |
| :--- | :--- | :--- | :--- | :--- |
| `MLLP_HOST` | Environment Variable | `0.0.0.0` | Inbound HL7 MLLP bind address | `[CONFIGURED]` |
| `MLLP_PORT` | Environment Variable | `2575` | Inbound HL7 MLLP TCP listener port | `[CONFIGURED]` |
| `TASK_BROKER_HOST` | Environment Variable | `task-processor` (or `0.0.0.0`) | Task event broker hostname | `[CONFIGURED]` |
| `TASK_BROKER_PORT` | Environment Variable | `61616` | Task event broker port | `[CONFIGURED]` |
| `TASK_PROCESSOR_URL` | Environment Variable | `http://task-processor:8080/api/queue/event` | Task processor event notification URL | `[CONFIGURED]` |
| `MLLP_OUTBOUND_INSTANCE_ID`| Environment Variable | `mllp-sender-his` / `mllp-sender-lis` | Outbound gateway instance identity | `[CONFIGURED]` |
| `MLLP_OUTBOUND_TARGET_ENDPOINT_ID`| Environment Variable | `HIS_NORTH` / `LIS_MAIN` | Target external system endpoint ID | `[CONFIGURED]` |
| `MLLP_OUTBOUND_QUEUE_PREFIX`| Environment Variable | `petasos.queue.mllp.outbound` | Destination outbound queue prefix | `[CONFIGURED]` |
| `OPERATIONS_PORT` | Environment Variable | `8090` | Iris BEFE operational telemetry port | `[CONFIGURED]` |
| `MANAGEMENT_PORT` | Environment Variable | `9990` | WildFly management interface port | `[CONFIGURED]` |

---

### 3.5 Collaboration Subsystem (Agora & Matrix Synapse)

| Parameter / Variable | Scope / File | Default Value | Purpose | Status |
| :--- | :--- | :--- | :--- | :--- |
| `AGORA_SYNAPSE_BASE_URL` | ConfigMap / Env | `http://synapse:8008` | Base URL of Matrix Synapse homeserver | `[CONFIGURED]` |
| `AGORA_SERVER_NAME` | ConfigMap / Env | `harmonia.local` | Matrix server domain | `[CONFIGURED]` |
| `AGORA_APPSERVICE_ID` | ConfigMap / Env | `harmonia-agora` | Application Service unique identifier | `[CONFIGURED]` |
| `AGORA_RECONCILIATION_CRON`| ConfigMap / Env | `0 */15 * * * *` | Scheduled cron for room reconciliation | `[IMPLEMENTED]` |
| `SYNAPSE_REPORT_STATS` | ConfigMap / Env | `no` | Homeserver telemetry reporting (disabled)| `[CONFIGURED]` |
| `SYNAPSE_RETENTION_MIN_LIFETIME`| `homeserver.yaml` | `1d` | Minimum message retention period | `[CONFIGURED]` |
| `SYNAPSE_RETENTION_MAX_LIFETIME`| `homeserver.yaml` | `90d` | Maximum message retention period | `[CONFIGURED]` |
| `federation.enabled` | `homeserver.yaml` | `false` | Disable external Matrix federation | `[CONFIGURED]` |
