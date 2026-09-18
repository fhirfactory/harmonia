# PostgreSQL 16 Relational Persistence Topology `[IMPLEMENTED]`

PostgreSQL 16 serves as Harmonia's authoritative relational persistence engine across both clinical data (**Mnemosyne Clinical**) and operational telemetry (**Mnemosyne Operations**).

---

## 1. Multi-Instance Database Topology `[CONFIGURED]`

Harmonia partitions relational storage across independent PostgreSQL instances to isolate high-throughput operational logging from clinical ACID transactions:

| Database Instance | Service Name | Service Port | Native Port | Database Name | Default User | Persistent Storage | Domain Scope |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `postgres-1` | `postgres-clinical-1` | `5432` | `5432` | `fhir_node_1` | `fhir_user` | StatefulSet PVC (10Gi) | Clinical Primary (FHIR R5 / Provider Registry) |
| `postgres-2` | `postgres-clinical-2` | `5433` | `5432` | `fhir_node_2` | `fhir_user` | StatefulSet PVC (10Gi) | Clinical Replica / Backup Node |
| `postgres-ops-1` | `postgres-ops-1` | `5434` | `5432` | `ops_node_1` | `ops_user` | StatefulSet PVC (10Gi) | Operations Primary (Lineage & Telemetry) |
| `postgres-ops-2` | `postgres-ops-2` | `5435` | `5432` | `ops_node_2` | `ops_user` | StatefulSet PVC (10Gi) | Operations Replica / Backup Node |
| `postgres-synapse`| `postgres-synapse` | `5436` | `5432` | `synapse_db` | `synapse_user`| StatefulSet PVC (5Gi) | Matrix Collaboration Storage |

---

## 2. Relational Schemas & Indexing Strategy `[IMPLEMENTED]`

Harmonia avoids unstructured blob storage in favor of strongly indexed, versioned relational schemas:

### 2.1 Clinical FHIR R5 Table (`hie_fhir_resources`)
```sql
CREATE TABLE hie_fhir_resources (
    res_type VARCHAR(64) NOT NULL,
    res_id VARCHAR(128) NOT NULL,
    res_version INT NOT NULL,
    res_status VARCHAR(32) NOT NULL,
    res_text TEXT NOT NULL,
    res_updated TIMESTAMP WITH TIME ZONE NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    security_labels VARCHAR(256),
    PRIMARY KEY (res_type, res_id, res_version)
);

CREATE INDEX idx_fhir_res_lookup ON hie_fhir_resources (res_type, res_id);
CREATE INDEX idx_fhir_res_updated ON hie_fhir_resources (res_updated);
CREATE INDEX idx_fhir_res_status ON hie_fhir_resources (res_type, res_status);
```

### 2.2 Operations Table (`hie_operations_resources`)
```sql
CREATE TABLE hie_operations_resources (
    resource_type VARCHAR(64) NOT NULL,
    resource_id VARCHAR(128) NOT NULL,
    resource_version INT NOT NULL,
    resource_payload TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (resource_type, resource_id, resource_version)
);

CREATE INDEX idx_ops_res_lookup ON hie_operations_resources (resource_type, resource_id);
CREATE INDEX idx_ops_res_created ON hie_operations_resources (created_at);
```

---

## 3. Connection Pooling & Resiliency (HikariCP) `[CONFIGURED]`

Relational connections are pooled using HikariCP integrated into Spring Boot / Jakarta persistence layers:

```properties
# HikariCP Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-test-query=SELECT 1
spring.datasource.hikari.leak-detection-threshold=60000
```
- **Connection Leak Detection**: Automatically logs stack traces if a thread holds a connection longer than 60 seconds without closing it.
- **Failover Connection Timeout**: 30-second timeout allows graceful recovery during Kubernetes pod rescheduling.

---

## 4. Deep Capability Exploitation: Used vs. Avoided `[IMPLEMENTED]`

| Capability Dimension | Used / Relied Upon in Harmonia | Avoided / Excluded in Harmonia | Architectural Rationale |
| :--- | :--- | :--- | :--- |
| **Database Isolation** | Partitioned instances (`fhir_node_*`, `ops_node_*`, `synapse_db`) | Unified monolithic database schema | Prevents telemetry queries from consuming shared lock managers or IOPS. |
| **Persistence Schema** | Immutable versioned composite keys `(res_type, res_id, res_version)` | In-place row updates without versioning | Guarantees complete historical auditability and temporal reproducibility. |
| **Indexing Strategy** | Targeted composite B-Tree indexes on lookup/time columns | Full-database unconstrained indexing | Balances fast sub-millisecond retrieval with high write throughput. |
| **Logic Placement** | Standard ANSI SQL DDL, constraints, and JPA entities | Database triggers, stored procedures, DB rules | Business logic remains in testable Java services (Themis, Energeia, Mnemosyne). |
| **Replication Strategy** | Independent node instances partitioned by domain | Native streaming replication / Patroni clustering | Simplifies base container orchestration; HA provided by application-tier redundancy. |

---

## 5. Operational Verification `[IMPLEMENTED]`

```bash
# 1. Connect to clinical database and count active resources
kubectl exec -it postgres-clinical-1-0 -n harmonia -- \
  psql -U fhir_user -d fhir_node_1 -c "SELECT res_type, count(*) FROM hie_fhir_resources GROUP BY res_type;"

# 2. Check active database connections and pool state
kubectl exec -it postgres-clinical-1-0 -n harmonia -- \
  psql -U fhir_user -d fhir_node_1 -c "SELECT count(*), state FROM pg_stat_activity GROUP BY state;"

# 3. Verify vacuum and table bloat metrics
kubectl exec -it postgres-clinical-1-0 -n harmonia -- \
  psql -U fhir_user -d fhir_node_1 -c "SELECT relname, n_dead_tup, last_vacuum, last_autovacuum FROM pg_stat_user_tables;"
```
