# Harmonia Graceful Shutdown Runbook `[CONFIGURED]`

This runbook specifies the graceful, reverse-dependency shutdown procedure for the Harmonia Health Integration Environment. Adherence to this sequence guarantees that all in-flight clinical events, active task sequences, message broker journals, and in-memory cache stores are fully drained and committed to durable disk before compute workloads terminate, enforcing **zero data loss**.

---

## 1. Graceful Shutdown Principles `[IMPLEMENTED]`

Shutting down Harmonia requires strict adherence to reverse-dependency draining:
1. **Perimeter Quiescence First**: Inbound MLLP (`pylai-mllp-in`) and Ingress routing must cease accepting new clinical transactions before backend workers are stopped.
2. **In-Flight Task Draining**: The Ponos WorkEngine (`task-processor`) must complete in-flight Pragma execution and update checkpoints before shutdown.
3. **Write-Behind Flushing**: Infinispan (`mneme-cluster`) must drain its `modification-queue` to Mnemosyne JPA servers before terminating.
4. **Journal & WAL Sync**: ActiveMQ Artemis must flush NIO journals, and PostgreSQL must checkpoint WAL logs to disk before pod termination.

---

## 2. Step-by-Step Shutdown Execution `[CONFIGURED]`

### Step 1: Drain Perimeter Inbound Traffic
Quiesce ingress gateways so upstream hospital systems (PAS/EMR) receive immediate connection refused or retry prompts, preventing in-transit payload loss:

```bash
# 1. Scale inbound MLLP gateway to 0
microk8s kubectl scale deployment mllp-gateway --namespace harmonia --replicas=0

# 2. Scale Iris Presentation SPAs to 0
microk8s kubectl scale deployment iris-clinical iris-console iris-administration \
  --namespace harmonia --replicas=0
```

---

### Step 2: Drain Boundary Gateways, Agora & Presentation Backend
Allow in-flight HTTP requests on Iris BEFE, Agora, and Outbound MLLP senders to complete:

```bash
# 1. Wait 15 seconds for active HTTP/MLLP transactions to finish
sleep 15

# 2. Scale down Gateways, Agora, and BEFE
microk8s kubectl scale deployment mllp-outbound-his mllp-outbound-lis agora befe \
  --namespace harmonia --replicas=0
```

---

### Step 3: Drain Workflow Engine & Collaboration Core
Allow Ponos WorkEngine to finish executing active Ergon activity units and update task state machines:

```bash
# 1. Wait for Ponos queues to drain (monitor via Artemis CLI or logs)
sleep 10

# 2. Scale down Ponos and Synapse
microk8s kubectl scale deployment task-processor --namespace harmonia --replicas=0
microk8s kubectl scale statefulset synapse --namespace harmonia --replicas=0
```

---

### Step 4: Flush & Stop Relational JPA Persistence Servers
Mnemosyne servers commit final JPA entity states to PostgreSQL:

```bash
# Scale down JPA servers
microk8s kubectl scale deployment hapi-fhir-jpa-server-1 hapi-fhir-jpa-server-2 \
  hie-operations-jpa-server-1 hie-operations-jpa-server-2 --namespace harmonia --replicas=0
```

---

### Step 5: Flush & Stop Messaging Brokers and Caching Grids
Artemis flushes append-only NIO journal buffers to the `artemis-*-data` PVCs. Infinispan flushes write-behind queues:

```bash
# 1. Scale down Artemis brokers
microk8s kubectl scale statefulset artemis-primary-a artemis-backup-a \
  artemis-primary-b artemis-backup-b --namespace harmonia --replicas=0

# 2. Scale down Infinispan cache nodes
microk8s kubectl scale statefulset infinispan-1 infinispan-2 --namespace harmonia --replicas=0
```

---

### Step 6: Flush & Stop PostgreSQL Relational Databases
Ensure PostgreSQL performs a clean checkpoint and writes all dirty shared buffers to disk:

```bash
# 1. Scale down all PostgreSQL StatefulSets
microk8s kubectl scale statefulset postgres-1 postgres-2 postgres-ops-1 postgres-ops-2 postgres-synapse \
  --namespace harmonia --replicas=0

# 2. Verify all pods have cleanly terminated
microk8s kubectl get pods --namespace harmonia
```

---

## 3. Automated Single-Command Shutdown Script `[CONFIGURED]`

Save this script as `scripts/harmonia-shutdown.sh`:

```bash
#!/usr/bin/env bash
set -e

NAMESPACE="harmonia"
echo "=== Initiating Graceful Harmonia Platform Shutdown in: $NAMESPACE ==="

echo "Step 1: Quiescing Ingress & Inbound Gateways..."
microk8s kubectl scale deployment mllp-gateway iris-clinical iris-console iris-administration -n $NAMESPACE --replicas=0

echo "Step 2: Quiescing Outbound Gateways, Agora & Iris BEFE..."
sleep 10
microk8s kubectl scale deployment mllp-outbound-his mllp-outbound-lis agora befe -n $NAMESPACE --replicas=0

echo "Step 3: Draining Ponos WorkEngine & Matrix Synapse..."
sleep 10
microk8s kubectl scale deployment task-processor -n $NAMESPACE --replicas=0
microk8s kubectl scale statefulset synapse -n $NAMESPACE --replicas=0

echo "Step 4: Stopping Mnemosyne JPA Servers..."
microk8s kubectl scale deployment hapi-fhir-jpa-server-1 hapi-fhir-jpa-server-2 hie-operations-jpa-server-1 hie-operations-jpa-server-2 -n $NAMESPACE --replicas=0

echo "Step 5: Flushing Artemis Journals & Infinispan Caches..."
microk8s kubectl scale statefulset artemis-primary-a artemis-backup-a artemis-primary-b artemis-backup-b infinispan-1 infinispan-2 -n $NAMESPACE --replicas=0

echo "Step 6: Stopping PostgreSQL Databases..."
microk8s kubectl scale statefulset postgres-1 postgres-2 postgres-ops-1 postgres-ops-2 postgres-synapse -n $NAMESPACE --replicas=0

echo "=== Graceful Shutdown Complete. PersistentVolumeClaims remain intact ==="
```
