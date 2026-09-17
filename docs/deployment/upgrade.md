# Harmonia Platform Upgrade & Rollback Runbook `[CONFIGURED]`

This runbook specifies the operational procedures for performing rolling updates, image tag upgrades, schema migrations, and emergency rollbacks across the Harmonia Health Integration Environment.

---

## 1. Upgrade Categorization & Strategy `[IMPLEMENTED]`

Harmonia distinguishes between two upgrade patterns based on statefulness:
1. **Stateless Deployments (Rolling Update)**:
   - Target workloads: `iris-clinical`, `iris-console`, `iris-administration`, `befe`, `mllp-gateway`, `mllp-outbound-*`, `task-processor`, `hapi-fhir-jpa-server-*`, `hie-operations-jpa-server-*`, `agora`.
   - Strategy: `RollingUpdate` with `maxUnavailable: 0` and `maxSurge: 1`. New pods initialize and pass Readiness probes before old pods terminate.
2. **StatefulSets (Ordered In-Place Upgrade)**:
   - Target workloads: `postgres-1/2`, `postgres-ops-1/2`, `postgres-synapse`, `artemis-primary-a/b`, `artemis-backup-a/b`, `infinispan-1/2`, `synapse`.
   - Strategy: `OnDelete` or sequential `RollingUpdate`. Backups are updated before primaries to preserve HA quorum.

---

## 2. Pre-Upgrade Health Assertion `[CONFIGURED]`

Never initiate an upgrade while the cluster has unready pods, failing health probes, or unresolved DLQ messages:

```bash
# 1. Assert all pods in harmonia namespace are 1/1 Running
microk8s kubectl get pods -n harmonia

# 2. Check Artemis Dead Letter Queue (DLQ) depth
microk8s kubectl exec -n harmonia statefulset/artemis-primary-a -- \
  /var/lib/artemis/bin/artemis queue stat --queueName DLQ

# 3. Verify PostgreSQL replication/readiness
microk8s kubectl exec -n harmonia statefulset/postgres-1 -- pg_isready -U fhir_user -d fhir_node_1
```

---

## 3. Image Tag Upgrade Procedure `[CONFIGURED]`

### Method A: Declarative Kustomize Upgrade (Recommended)
Update the container image tag in `deployment/ansible/inventory/group_vars/all.yml` or Kustomize overlay:

```bash
# 1. Edit image tag in overlay or run sed replacement
cd deployment/kubernetes/environments/microk8s
microk8s kubectl kustomize . | \
  sed 's|:1.0.0-SNAPSHOT|:1.0.1|g' | \
  microk8s kubectl apply -f -

# 2. Track rollout status of upgraded components
microk8s kubectl rollout status deployment/befe -n harmonia
microk8s kubectl rollout status deployment/task-processor -n harmonia
```

### Method B: Imperative Workload Patching
For single-component hotfixes:

```bash
# Update Iris BEFE image tag
microk8s kubectl set image deployment/befe befe=localhost:32000/harmonia/iris-befe:1.0.1 -n harmonia

# Monitor rollout
microk8s kubectl rollout status deployment/befe -n harmonia --timeout 120s
```

---

## 4. Stateful Subsystem Upgrade Sequences `[CONFIGURED]`

### 4.1 Artemis Messaging Brokers
Always upgrade backup brokers before primary brokers:
1. Upgrade `artemis-backup-a` and `artemis-backup-b`.
2. Wait for backups to synchronize journals with primaries.
3. Upgrade `artemis-primary-a` and `artemis-primary-b`. Backups take over live traffic during primary restart, preventing client disconnects.

### 4.2 Infinispan Cache Grid
Infinispan operates with 2 symmetric sync nodes (`infinispan-1`, `infinispan-2`):
1. Upgrade `infinispan-2`. Wait for node 2 to rejoin JGroups cluster on port `7800` and sync replicated caches.
2. Upgrade `infinispan-1`. Wait for cluster view to report 2 nodes.

### 4.3 PostgreSQL Relational Schemas
Harmonia persistence schemas (`hie_fhir_resources`, `hie_operations_resources`) utilize versioned rows. For minor version updates:
- Spring Boot services (`mnemosyne-*`) apply non-destructive DDL updates automatically via Hibernate (`hibernate.ddl-auto: update`).
- For major structural migrations, execute SQL scripts directly against PostgreSQL before deploying upgraded JPA servers.

---

## 5. Rollback Procedures `[CONFIGURED]`

If an upgraded workload fails health probes or emits errors, immediately revert to the previous revision:

```bash
# 1. Inspect rollout revision history
microk8s kubectl rollout history deployment/befe -n harmonia

# 2. Roll back to immediate previous revision
microk8s kubectl rollout undo deployment/befe -n harmonia

# 3. Or roll back to a specific revision number
microk8s kubectl rollout undo deployment/befe --to-revision=1 -n harmonia

# 4. Verify rollback readiness
microk8s kubectl rollout status deployment/befe -n harmonia
```
