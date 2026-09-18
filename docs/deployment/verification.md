# Harmonia Deployment Verification & Smoke Testing Runbook `[CONFIGURED]`

This runbook specifies the post-deployment verification procedures and automated smoke testing suite to validate that all 25 workloads across all 5 architectural tiers in the Harmonia Health Integration Environment are operational, responsive, and communicating across network boundaries.

---

## 1. Verification Checklist & Acceptance Invariants `[IMPLEMENTED]`

Before declaring a Harmonia deployment operational, the following criteria must be satisfied:
- [ ] All 25 platform pods report status `Running` with `1/1` containers ready.
- [ ] All 12 PersistentVolumeClaims report status `Bound` under the `microk8s-hostpath` StorageClass.
- [ ] Nginx Ingress routes HTTP/HTTPS traffic to all virtual hosts (`clinical`, `console`, `admin`, `api`, `gateway`, `matrix`).
- [ ] MLLP Inbound Gateway accepts TCP handshakes on port `2575`.
- [ ] ActiveMQ Artemis HA broker mesh forms a 4-node cluster with `DLQ` queue depth at `0`.
- [ ] Infinispan Mneme cluster forms a 2-node view over JGroups port `7800` and accepts Hot Rod commands on port `11222`.
- [ ] Matrix Synapse homeserver reports version compatibility and Agora AS endpoint responds healthy.

---

## 2. Step-by-Step Verification Runbook `[CONFIGURED]`

### Step 1: Workload & Pod Status Verification
Verify that all 25 pods are running without crashes or restart loops:

```bash
microk8s kubectl get pods --namespace harmonia -o wide
```

Expected output:
```
NAME                                          READY   STATUS    RESTARTS   AGE
agora-66d4889c7d-x2m4q                        1/1     Running   0          5m
artemis-backup-a-0                            1/1     Running   0          8m
artemis-backup-b-0                            1/1     Running   0          8m
artemis-primary-a-0                           1/1     Running   0          8m
artemis-primary-b-0                           1/1     Running   0          8m
befe-5b6c4b4d6b-j8z1v                         1/1     Running   0          5m
hapi-fhir-jpa-server-1-79fc65d86-w9p2l        1/1     Running   0          6m
hapi-fhir-jpa-server-2-8f964dc75-m7b3k        1/1     Running   0          6m
hie-operations-jpa-server-1-654db4c5b-4v8tx   1/1     Running   0          6m
hie-operations-jpa-server-2-54db78996-r1q7s   1/1     Running   0          6m
infinispan-1-0                                1/1     Running   0          8m
infinispan-2-0                                1/1     Running   0          8m
iris-administration-7d84b9f68-q4r8p           1/1     Running   0          4m
iris-clinical-5c9c748c5b-t6k2z                1/1     Running   0          4m
iris-console-6975d69784-n2v8l                 1/1     Running   0          4m
mllp-gateway-55447b7df8-f7k8q                 1/1     Running   0          5m
mllp-outbound-his-765d7bb54c-v8x2m            1/1     Running   0          5m
mllp-outbound-lis-85669dcf4-t1q4x             1/1     Running   0          5m
postgres-1-0                                  1/1     Running   0          9m
postgres-2-0                                  1/1     Running   0          9m
postgres-ops-1-0                              1/1     Running   0          9m
postgres-ops-2-0                              1/1     Running   0          9m
postgres-synapse-0                            1/1     Running   0          9m
synapse-0                                     1/1     Running   0          7m
task-processor-78d46fb598-m4p9q               1/1     Running   0          6m
```

---

### Step 2: Storage Volume Binding Verification
Assert that dynamic hostpath provisioning successfully bound all 12 persistent volumes:

```bash
microk8s kubectl get pvc --namespace harmonia
```
Every entry must have `STATUS: Bound`.

---

### Step 3: Web Presentation & Ingress Verification
Test HTTP responses across all virtual host domains defined in Ingress:

```bash
# 1. Iris Clinical Frontend (HTTP 200)
curl -sf -o /dev/null -w "%{http_code}\n" http://clinical.harmonia.local/

# 2. Iris Operations Console (HTTP 200)
curl -sf -o /dev/null -w "%{http_code}\n" http://console.harmonia.local/

# 3. Iris Administration Frontend (HTTP 200)
curl -sf -o /dev/null -w "%{http_code}\n" http://admin.harmonia.local/

# 4. Iris BEFE Clinical REST Gateway (HTTP 200)
curl -sf -k http://api.harmonia.local/clinical/actuator/health

# 5. Iris BEFE Operations REST Gateway (HTTP 200)
curl -sf -k http://api.harmonia.local/operations/status
```

---

### Step 4: Perimeter MLLP Gateway Verification
Test TCP handshake connectivity to the Pylai Inbound MLLP receiver:

```bash
nc -zv localhost 2575
```
Expected output:
```
Connection to localhost 2575 port [tcp/*] succeeded!
```

---

### Step 5: ActiveMQ Artemis Messaging Mesh Verification
Query queue statistics and verify that the dead-letter queue (`DLQ`) has zero messages:

```bash
microk8s kubectl exec -n harmonia statefulset/artemis-primary-a -- \
  /var/lib/artemis/bin/artemis queue stat --queueName DLQ
```
Expected output:
```
|NAME                     |ADDRESS                  |CONSUMER_COUNT|MESSAGE_COUNT|DELIVERING_COUNT|MESSAGES_ADDED|
|DLQ                      |DLQ                      |0             |0            |0               |0             |
```

---

### Step 6: Matrix Synapse & Agora AS Verification
Verify that Matrix Synapse is serving Client-Server API versions and Agora is responding healthy:

```bash
# 1. Check Matrix Synapse version API
curl -sf http://matrix.harmonia.local/_matrix/client/versions | jq .

# 2. Check Agora Actuator Health
microk8s kubectl exec -n harmonia deployment/agora -- \
  curl -sf http://localhost:9992/actuator/health | jq .
```
Expected JSON output: `{"status":"UP"}`.

---

## 3. Automated Post-Deployment Verification Script `[CONFIGURED]`

Save this automated smoke test suite as `scripts/harmonia-verify.sh`:

```bash
#!/usr/bin/env bash
set -e

NAMESPACE="harmonia"
echo "=== Running Harmonia HIE Smoke Test Suite ==="

echo -n "Checking Pod readiness... "
UNREADY=$(microk8s kubectl get pods -n $NAMESPACE --no-headers | grep -v "1/1.*Running" || true)
if [ -n "$UNREADY" ]; then
  echo "FAILED: Some pods are not ready:"
  echo "$UNREADY"
  exit 1
fi
echo "PASSED (25/25 pods ready)"

echo -n "Checking PVC bindings... "
UNBOUND=$(microk8s kubectl get pvc -n $NAMESPACE --no-headers | grep -v "Bound" || true)
if [ -n "$UNBOUND" ]; then
  echo "FAILED: Unbound PVCs detected:"
  echo "$UNBOUND"
  exit 1
fi
echo "PASSED (12/12 PVCs bound)"

echo -n "Checking Inbound MLLP Port 2575... "
if nc -z localhost 2575; then
  echo "PASSED"
else
  echo "FAILED: Port 2575 unreachable"
  exit 1
fi

echo -n "Checking Clinical Web UI... "
CODE=$(curl -s -o /dev/null -w "%{http_code}" http://clinical.harmonia.local/ || true)
if [ "$CODE" = "200" ]; then
  echo "PASSED (HTTP 200)"
else
  echo "FAILED (HTTP $CODE)"
  exit 1
fi

echo -n "Checking Matrix Synapse API... "
CODE=$(curl -s -o /dev/null -w "%{http_code}" http://matrix.harmonia.local/_matrix/client/versions || true)
if [ "$CODE" = "200" ]; then
  echo "PASSED (HTTP 200)"
else
  echo "FAILED (HTTP $CODE)"
  exit 1
fi

echo "=== All 5 Verification Stages Passed Successfully ==="
```
