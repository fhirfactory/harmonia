# Harmonia Undeployment & Teardown Runbook `[CONFIGURED]`

This runbook specifies the procedures for decommissioning Harmonia workloads from a MicroK8s cluster. It strictly separates **Safe Undeployment** (which removes compute pods while retaining persistent databases and message queues) from **Guarded Destructive Purge** (which permanently deletes all clinical data and volumes).

---

## 1. Data Retention Policy & Invariants `[IMPLEMENTED]`

By default, Harmonia enforces **Data Retention on Undeploy**:
- **Stateless Workloads, Ingress & Services**: Safely deleted to free host CPU and RAM.
- **StatefulSet Compute Pods**: Terminated cleanly.
- **PersistentVolumeClaims (PVCs)**: **Strictly Retained**. All 12 PVCs (`postgres-data-1/2`, `postgres-ops-data-1/2`, `postgres-synapse-data`, `artemis-*-data`, `mneme-data-1/2`, `synapse-data`) remain bound in the cluster, preserving clinical history and message broker journals across maintenance cycles.
- **Secrets & ConfigMaps**: Retained by default to enable immediate re-deployment.

---

## 2. Safe Undeployment (Default: Retain Data) `[CONFIGURED]`

### Method A: Manual `kubectl` Teardown
To remove compute workloads while retaining persistent data:

```bash
# 1. Delete Ingress routing
microk8s kubectl delete ingress harmonia-ingress --namespace harmonia --ignore-not-found

# 2. Delete all stateless Deployments
microk8s kubectl delete deployments --all --namespace harmonia

# 3. Delete StatefulSets (Compute only, preserving volume claims)
microk8s kubectl delete statefulsets --all --namespace harmonia

# 4. Delete Kubernetes Services
microk8s kubectl delete services --all --namespace harmonia

# 5. Delete NetworkPolicies
microk8s kubectl delete networkpolicies --all --namespace harmonia

# 6. Verify that PersistentVolumeClaims remain intact and Bound
microk8s kubectl get pvc --namespace harmonia
```
Expected output:
```
NAME                       STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS        AGE
artemis-backup-a-data      Bound    pvc-8199b218-a62d-426b-800c-b26a5789f894   2Gi        RWO            microk8s-hostpath   1h
artemis-backup-b-data      Bound    pvc-35abf9bc-2321-4fce-bc86-53d3ad118228   2Gi        RWO            microk8s-hostpath   1h
artemis-primary-a-data     Bound    pvc-72a3e5c7-e6db-4e9b-b5cb-ff153b477b94   2Gi        RWO            microk8s-hostpath   1h
artemis-primary-b-data     Bound    pvc-a059c25f-2c71-46da-b391-450f7f185c78   2Gi        RWO            microk8s-hostpath   1h
mneme-data-1               Bound    pvc-91f1a0ba-06b4-4b51-9bf6-b4dc80292fc0   1Gi        RWO            microk8s-hostpath   1h
mneme-data-2               Bound    pvc-99bce866-9964-42ea-a4e9-11ba103ceef1   1Gi        RWO            microk8s-hostpath   1h
postgres-data-1            Bound    pvc-fc230302-3c2c-47b2-8411-137b0b8c634c   2Gi        RWO            microk8s-hostpath   1h
postgres-data-2            Bound    pvc-84df6d50-fe8a-4cba-a10c-3be06efb4cc1   2Gi        RWO            microk8s-hostpath   1h
postgres-ops-data-1        Bound    pvc-2b2f6f4e-2895-4670-af96-188dfec5c9b7   2Gi        RWO            microk8s-hostpath   1h
postgres-ops-data-2        Bound    pvc-4929cf05-4f76-4d05-b3e3-535359a99723   2Gi        RWO            microk8s-hostpath   1h
postgres-synapse-data      Bound    pvc-34cf204c-1e24-4f81-ba54-c9431e217277   2Gi        RWO            microk8s-hostpath   1h
synapse-data               Bound    pvc-d84382bf-73c1-4bcf-85d7-709b1f73752e   5Gi        RWO            microk8s-hostpath   1h
```

### Method B: Automated Ansible Safe Undeployment
```bash
cd deployment/ansible
ansible-playbook -i inventory/hosts.ini undeploy-harmonia.yml
```

---

## 3. Guarded Destructive Purge (Permanent Data Deletion) `[CONFIGURED]`

> **WARNING: IRREVERSIBLE OPERATION**  
> Purging data permanently destroys all PostgreSQL clinical tables, audit logs, Matrix room history, and message broker journals. Execute only in ephemeral testbeds or when explicitly decommissioning a cluster.

### Method A: Manual Purge Commands
```bash
# 1. Complete safe compute undeployment first
microk8s kubectl delete deployments,statefulsets,services,ingress --all -n harmonia

# 2. Delete all PersistentVolumeClaims (Destroys underlying hostpath storage)
microk8s kubectl delete pvc --all --namespace harmonia

# 3. Delete ConfigMaps and Secrets
microk8s kubectl delete configmaps,secrets --all --namespace harmonia

# 4. Delete the Harmonia namespace
microk8s kubectl delete namespace harmonia
```

### Method B: Automated Ansible Purge
Pass `-e harmonia_purge_data=true` and confirm the interactive prompt:
```bash
cd deployment/ansible
ansible-playbook -i inventory/hosts.ini undeploy-harmonia.yml -e harmonia_purge_data=true
```

---

## 4. Host Decommissioning (Optional) `[CONFIGURED]`

To completely remove MicroK8s and reset host network settings:

```bash
# Decommission MicroK8s snap
ansible-playbook -i deployment/ansible/inventory/hosts.ini deployment/ansible/decommission-microk8s.yml
```
Or manually:
```bash
sudo snap remove microk8s --purge
sudo rm -rf ~/.kube
```
