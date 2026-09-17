# Harmonia Authoritative Configuration Parameter Register

This register details all environment variables, Spring Boot properties, WildFly settings, and infrastructure parameters across the Harmonia platform.

---

## 1. Global Infrastructure Parameters

| Parameter / Variable Name | Component / Scope | Default Value | Description & Purpose | Classification |
| :--- | :--- | :--- | :--- | :--- |
| `HARMONIA_ENV` | Global | `microk8s` | Active environment profile (`dev`, `test`, `microk8s`, `prod`) | `CONFIGURED` |
| `HARMONIA_NAMESPACE` | Kubernetes | `harmonia` | Target Kubernetes namespace | `CONFIGURED` |
| `CONTAINER_REGISTRY` | Deployment | `localhost:32000` | Local MicroK8s or enterprise container registry | `CONFIGURED` |
| `HARMONIA_IMAGE_TAG` | Deployment | `1.0.0-SNAPSHOT` | Container image release tag | `CONFIGURED` |
| `SPRING_PROFILES_ACTIVE` | Spring Boot Services | `postgres` | Active Spring profile for persistence switching | `CONFIGURED` |

---

## 2. Messaging & Transport Configuration (Petasos & Artemis)

| Parameter / Variable Name | Target Module | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `PETASOS_BROKER_URL` | `petasos-artemis`, `pylai`, `ponos` | `(tcp://artemis-primary-a:61616,tcp://artemis-backup-a:61616,tcp://artemis-primary-b:61618,tcp://artemis-backup-b:61618)?ha=true&reconnectAttempts=-1` | Clustered failover transport URL |
| `PETASOS_SESSION_POOL_SIZE` | `petasos-artemis` | `20` | Maximum pooled JMS sessions per container |
| `PETASOS_DEDUP_CACHE_SIZE` | `petasos-core` | `20000` | In-memory sliding deduplication cache size |
| `PETASOS_MAX_REDELIVERY` | `petasos-artemis` | `5` | Maximum redelivery attempts before DLQ routing |
| `PETASOS_INBOUND_QUEUE` | `pylai-mllp-in`, `ponos` | `petasos.queue.task.inbound` | Ingress task event queue address |
| `ARTEMIS_USER` | `artemis-*` StatefulSets | `admin` | Artemis administrative username |
| `ARTEMIS_PASSWORD` | `artemis-*` StatefulSets | Secret `harmonia-artemis-secrets/admin-password` | Artemis administrative password |
| `CLUSTER_USER` | `artemis-*` StatefulSets | `artemisCluster` | Inter-broker cluster discovery username |
| `CLUSTER_PASSWORD` | `artemis-*` StatefulSets | Secret `harmonia-artemis-secrets/cluster-password` | Inter-broker cluster discovery password |
| `EXTRA_ARGS` | `artemis-*` StatefulSets | `--override-config /var/lib/artemis/etc-override/broker.xml` | Custom broker configuration override flag |

---

## 3. In-Memory Cache Grid Configuration (Mneme & Infinispan)

| Parameter / Variable Name | Target Module | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `HARMONIA_CACHE_SERVERS` | `iris-befe`, `ponos`, `pylai` | `infinispan-1:11222;infinispan-2:11222` | Hot Rod server connection endpoints |
| `INFINISPAN_HOST` | `pylai`, `ponos`, `iris-befe` | `infinispan-1` | Host address of Infinispan cache service |
| `INFINISPAN_PORT` | `pylai`, `ponos`, `iris-befe` | `11222` | Hot Rod binary client port |
| `INFINISPAN_USER` | `pylai`, `ponos`, `iris-befe` | `admin` | Hot Rod authentication username |
| `INFINISPAN_PASSWORD` | `pylai`, `ponos`, `iris-befe` | `admin` | Hot Rod authentication password |
| `INFINISPAN_CLUSTER_NAME` | `mneme-cluster` | `harmonia-mneme-cluster` | JGroups cluster name |
| `INFINISPAN_CACHE_MODE` | `mneme-cluster` | `REPL_SYNC` | Default cache synchronization mode |
| `JAVA_OPTIONS` | `mneme-cluster` | `-Dinfinispan.node.name=... -Dfhir.server.url=... -Dops.server.url=...` | Node identity & Mnemosyne REST targets |

---

## 4. Relational Persistence Configuration (Mnemosyne & PostgreSQL)

| Parameter / Variable Name | Target Module | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `mnemosyne-clinical` | `jdbc:postgresql://postgres-1:5432/fhir_node_1` | Clinical database JDBC connection URL |
| `SPRING_DATASOURCE_USERNAME` | `mnemosyne-clinical` | `fhir_user` | Clinical database username |
| `SPRING_DATASOURCE_PASSWORD` | `mnemosyne-clinical` | Secret `harmonia-db-secrets/fhir-password` | Clinical database password |
| `SPRING_DATASOURCE_OPS_URL` | `mnemosyne-operations` | `jdbc:postgresql://postgres-ops-1:5432/ops_node_1` | Operations database JDBC connection URL |
| `SPRING_DATASOURCE_OPS_USER`| `mnemosyne-operations` | `ops_user` | Operations database username |
| `SPRING_DATASOURCE_OPS_PASS`| `mnemosyne-operations` | Secret `harmonia-db-secrets/ops-password` | Operations database password |
| `POSTGRES_DB` | `postgres-*` StatefulSets | `fhir_node_1` / `fhir_node_2` / `ops_node_1` / `ops_node_2` | PostgreSQL database name |
| `POSTGRES_USER` | `postgres-*` StatefulSets | `fhir_user` / `ops_user` | PostgreSQL database master user |
| `POSTGRES_PASSWORD` | `postgres-*` StatefulSets | Secret `harmonia-db-secrets` | PostgreSQL database password |
| `HIKARI_MAX_POOL_SIZE` | `mnemosyne-*` | `20` | HikariCP maximum connection pool size |

---

## 5. Gateway & Worker Configuration (Pylai, Ponos, Iris)

| Parameter / Variable Name | Target Module | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `MLLP_HOST` | `pylai-mllp-in` | `0.0.0.0` | Inbound HL7 MLLP bind host |
| `MLLP_PORT` / `MLLP_INBOUND_PORT` | `pylai-mllp-in` | `2575` | Inbound HL7 MLLP TCP port |
| `MLLP_INBOUND_MAX_FRAME` | `pylai-mllp-in` | `10485760` (10 MB) | Maximum MLLP packet frame size |
| `TASK_BROKER_HOST` | `pylai-mllp-in` | `task-processor` | Target task broker host |
| `TASK_BROKER_PORT` | `pylai-mllp-in` | `61616` | Target task broker port |
| `TASK_PROCESSOR_URL` | `pylai-mllp-in` | `http://task-processor:8080/api/queue/event` | Task processor HTTP event URL |
| `MLLP_OUTBOUND_INSTANCE_ID` | `pylai-mllp-out` | `mllp-sender-his` / `mllp-sender-lis` | Outbound instance identifier |
| `MLLP_OUTBOUND_TARGET_ENDPOINT_ID` | `pylai-mllp-out` | `HIS_NORTH` / `LIS_MAIN` | Outbound target endpoint identifier |
| `MLLP_OUTBOUND_QUEUE_PREFIX` | `pylai-mllp-out` | `petasos.queue.mllp.outbound` | Destination queue prefix |
| `TASK_PROCESSOR_BROKER_URL` | `pylai-mllp-out` | `tcp://task-processor:61616` | Outbound gateway broker URL |
| `MLLP_OUTBOUND_TIMEOUT` | `pylai-mllp-out` | `10000` (10,000 ms) | Synchronous MLLP ACK response timeout |
| `FHIR_SERVER_URL` | `ponos` | `http://hapi-fhir-jpa-server-1:8080/fhir` | Clinical FHIR persistence endpoint |
| `OPS_SERVER_URL` | `ponos` | `http://hie-operations-jpa-server-1:8080/api/operations` | Operations JPA persistence endpoint |
| `BROKER_URL` | `ponos` | `tcp://task-processor:61616` | Ponos internal broker connection URL |
| `MANAGEMENT_PORT` | `ponos`, `pylai-*` | `9990` | Management & metrics port |
| `OPERATIONS_PORT` | `iris-befe` | `8090` | Iris BEFE operations REST port |

---

## 6. Collaboration Gateway & Matrix Synapse Configuration (Agora & Synapse)

| Parameter / Variable Name | Target Module | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `AGORA_SYNAPSE_BASE_URL` | `agora-service` | `http://synapse:8008` | URL of the Synapse homeserver for CS/Admin API calls |
| `AGORA_HOMESERVER_TOKEN` | `agora-service` | Secret `harmonia-agora-secrets/hs-token` | Secret token used by Synapse to authenticate to Agora (`hs_token`) |
| `AGORA_APPSERVICE_TOKEN` | `agora-service` | Secret `harmonia-agora-secrets/as-token` | Secret token used by Agora to authenticate to Synapse (`as_token`) |
| `AGORA_ADMIN_TOKEN` | `agora-service` | Secret `harmonia-agora-secrets/synapse-admin-token` | Synapse Admin access token for local user provisioning |
| `AGORA_APPSERVICE_ID` | `agora-service` | `harmonia-agora` | Application Service unique identifier in Synapse registration |
| `AGORA_SERVER_NAME` | `agora-service` | `harmonia.local` | Server name domain for Matrix user/room IDs |
| `AGORA_RECONCILIATION_CRON` | `agora-service` | `0 */15 * * * *` | Cron schedule for periodic membership reconciliation |
| `SYNAPSE_SERVER_NAME` | `synapse` StatefulSet | `harmonia.local` | Synapse server name domain |
| `SYNAPSE_REPORT_STATS` | `synapse` StatefulSet | `no` | Homeserver telemetry reporting flag (disabled for privacy) |
| `SYNAPSE_REGISTRATION_SHARED_SECRET` | `synapse` StatefulSet | Secret `harmonia-synapse-secrets/registration-shared-secret` | Shared secret for automated user registration |
| `SYNAPSE_MACAROON_SECRET_KEY` | `synapse` StatefulSet | Secret `harmonia-synapse-secrets/macaroon-secret-key` | Secret key for Macaroon token generation |
| `SYNAPSE_FORM_SECRET` | `synapse` StatefulSet | Secret `harmonia-synapse-secrets/form-secret` | Secret for web form CSRF protection |
| `SYNAPSE_DB_PASSWORD` | `synapse`, `postgres-synapse` | Secret `harmonia-synapse-secrets/db-password` | PostgreSQL password for `synapse_db` |
| `SYNAPSE_RETENTION_MIN_LIFETIME` | `synapse` ConfigMap | `1d` | Minimum retention period for room messages |
| `SYNAPSE_RETENTION_MAX_LIFETIME` | `synapse` ConfigMap | `90d` | Maximum retention period for room messages |
