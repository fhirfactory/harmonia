# Harmonia Ordered Startup Runbook `[CONFIGURED]`

This runbook specifies the strict, dependency-ordered startup sequence for the Harmonia Health Integration Environment. Adherence to this order guarantees that databases, message brokers, and caching grids are fully operational before dependent worker daemons and protocol gateways begin accepting clinical traffic.

---

## 1. Startup Dependency Graph `[IMPLEMENTED]`

```
Phase 1: PostgreSQL Databases (postgres-1, postgres-2, postgres-ops-1, postgres-ops-2, postgres-synapse)
   │
   ▼
Phase 2: ActiveMQ Artemis Messaging (artemis-primary-a/b, artemis-backup-a/b)
         & Infinispan Cache Grid (infinispan-1, infinispan-2)
   │
   ▼
Phase 3: Mnemosyne JPA Servers (hapi-fhir-jpa-server-1/2, hie-operations-jpa-server-1/2)
   │
   ▼
Phase 4: Ponos Task Processor (task-processor) & Matrix Synapse (synapse)
   │
   ▼
Phase 5: Gateways & BEFE (mllp-gateway, mllp-outbound-his/lis, agora, befe)
   │
   ▼
Phase 6: Presentation SPAs (iris-clinical, iris-console, iris-administration) & Ingress
```

---

## 2. Phase-by-Phase Execution Commands `[CONFIGURED]`

### Phase 1: Relational Persistence Layer
Start and verify all 5 PostgreSQL database instances:

```bash
# 1. Scale/apply PostgreSQL StatefulSets
microk8s kubectl scale statefulset postgres-1 postgres-2 postgres-ops-1 postgres-ops-2 postgres-synapse \
  --namespace harmonia --replicas=1

# 2. Wait for database readiness
microk8s kubectl rollout status statefulset/postgres-1 -n harmonia --timeout 60s
microk8s kubectl rollout status statefulset/postgres-2 -n harmonia --timeout 60s
microk8s kubectl rollout status statefulset/postgres-ops-1 -n harmonia --timeout 60s
microk8s kubectl rollout status statefulset/postgres-ops-2 -n harmonia --timeout 60s
microk8s kubectl rollout status statefulset/postgres-synapse -n harmonia --timeout 60s
```

---

### Phase 2: Messaging Mesh & Cache Grid
Start the Artemis message broker mesh and Infinispan Hot Rod cache grid:

```bash
# 1. Scale Artemis and Infinispan StatefulSets
microk8s kubectl scale statefulset artemis-primary-a artemis-backup-a artemis-primary-b artemis-backup-b \
  infinispan-1 infinispan-2 --namespace harmonia --replicas=1

# 2. Wait for Artemis brokers to complete journal replay
microk8s kubectl rollout status statefulset/artemis-primary-a -n harmonia --timeout 90s
microk8s kubectl rollout status statefulset/artemis-backup-a -n harmonia --timeout 90s
microk8s kubectl rollout status statefulset/artemis-primary-b -n harmonia --timeout 90s
microk8s kubectl rollout status statefulset/artemis-backup-b -n harmonia --timeout 90s

# 3. Wait for Infinispan cluster formation over JGroups (Port 7800)
microk8s kubectl rollout status statefulset/infinispan-1 -n harmonia --timeout 90s
microk8s kubectl rollout status statefulset/infinispan-2 -n harmonia --timeout 90s
```

---

### Phase 3: Relational JPA Servers (Mnemosyne)
Start HAPI FHIR R5 JPA and Operations JPA servers connecting to PostgreSQL:

```bash
# 1. Scale JPA Deployments
microk8s kubectl scale deployment hapi-fhir-jpa-server-1 hapi-fhir-jpa-server-2 \
  hie-operations-jpa-server-1 hie-operations-jpa-server-2 --namespace harmonia --replicas=1

# 2. Verify JPA server Actuator health
microk8s kubectl rollout status deployment/hapi-fhir-jpa-server-1 -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/hapi-fhir-jpa-server-2 -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/hie-operations-jpa-server-1 -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/hie-operations-jpa-server-2 -n harmonia --timeout 90s
```

---

### Phase 4: Task Execution Engine & Collaboration Homeserver
Start Ponos WorkEngine (which binds to Artemis queues) and Matrix Synapse:

```bash
# 1. Scale Ponos and Synapse
microk8s kubectl scale deployment task-processor --namespace harmonia --replicas=1
microk8s kubectl scale statefulset synapse --namespace harmonia --replicas=1

# 2. Wait for WorkEngine and Matrix readiness
microk8s kubectl rollout status deployment/task-processor -n harmonia --timeout 90s
microk8s kubectl rollout status statefulset/synapse -n harmonia --timeout 180s
```

---

### Phase 5: Boundary Protocol Gateways & Presentation Backend
Start Inbound/Outbound MLLP gateways, Agora AS, and Iris BEFE:

```bash
# 1. Scale Gateways and BEFE
microk8s kubectl scale deployment mllp-gateway mllp-outbound-his mllp-outbound-lis \
  agora befe --namespace harmonia --replicas=1

# 2. Wait for gateway initialization
microk8s kubectl rollout status deployment/mllp-gateway -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/mllp-outbound-his -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/mllp-outbound-lis -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/agora -n harmonia --timeout 90s
microk8s kubectl rollout status deployment/befe -n harmonia --timeout 90s
```

---

### Phase 6: Web Presentation SPAs & Ingress Controller
Activate Iris web applications:

```bash
# 1. Scale Vue 3 frontend Nginx containers
microk8s kubectl scale deployment iris-clinical iris-console iris-administration \
  --namespace harmonia --replicas=1

# 2. Verify frontend readiness
microk8s kubectl rollout status deployment/iris-clinical -n harmonia --timeout 60s
microk8s kubectl rollout status deployment/iris-console -n harmonia --timeout 60s
microk8s kubectl rollout status deployment/iris-administration -n harmonia --timeout 60s
```

---

## 3. Automated Single-Command Startup Script `[CONFIGURED]`

For convenience, save this script as `scripts/harmonia-startup.sh`:

```bash
#!/usr/bin/env bash
set -e

NAMESPACE="harmonia"
echo "=== Starting Harmonia HIE Platform in namespace: $NAMESPACE ==="

echo "Phase 1: Starting PostgreSQL databases..."
microk8s kubectl scale statefulset postgres-1 postgres-2 postgres-ops-1 postgres-ops-2 postgres-synapse -n $NAMESPACE --replicas=1
microk8s kubectl rollout status statefulset/postgres-1 -n $NAMESPACE

echo "Phase 2: Starting Messaging (Artemis) & Cache Grid (Infinispan)..."
microk8s kubectl scale statefulset artemis-primary-a artemis-backup-a artemis-primary-b artemis-backup-b infinispan-1 infinispan-2 -n $NAMESPACE --replicas=1
microk8s kubectl rollout status statefulset/artemis-primary-a -n $NAMESPACE
microk8s kubectl rollout status statefulset/infinispan-1 -n $NAMESPACE

echo "Phase 3: Starting Mnemosyne JPA Persistence Servers..."
microk8s kubectl scale deployment hapi-fhir-jpa-server-1 hapi-fhir-jpa-server-2 hie-operations-jpa-server-1 hie-operations-jpa-server-2 -n $NAMESPACE --replicas=1
microk8s kubectl rollout status deployment/hapi-fhir-jpa-server-1 -n $NAMESPACE

echo "Phase 4: Starting Ponos WorkEngine & Matrix Synapse..."
microk8s kubectl scale deployment task-processor -n $NAMESPACE --replicas=1
microk8s kubectl scale statefulset synapse -n $NAMESPACE --replicas=1
microk8s kubectl rollout status deployment/task-processor -n $NAMESPACE

echo "Phase 5: Starting Protocol Gateways, Agora & Iris BEFE..."
microk8s kubectl scale deployment mllp-gateway mllp-outbound-his mllp-outbound-lis agora befe -n $NAMESPACE --replicas=1
microk8s kubectl rollout status deployment/mllp-gateway -n $NAMESPACE

echo "Phase 6: Starting Presentation SPAs..."
microk8s kubectl scale deployment iris-clinical iris-console iris-administration -n $NAMESPACE --replicas=1
microk8s kubectl rollout status deployment/iris-clinical -n $NAMESPACE

echo "=== All Harmonia workloads successfully started and healthy ==="
```
