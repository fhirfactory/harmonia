# Harmonia MicroK8s Reference Deployment Guide

This document defines the operational architecture, host prerequisites, deployment automation, and administrative lifecycle for the Harmonia Health Integration Platform on single-node MicroK8s on Ubuntu Linux.

---

## 1. Architectural Principles & Separation of Concerns

The Harmonia platform maintains a strict separation between application runtime logic and deployment platform mechanics:

```
Harmonia Application Logic (Java 21 / Spring Boot 3 / WildFly 31 / Vue 3)
       │
       ▼
OCI Container Image (Built via Maven Profiles -Pcontainer-build / -Pcontainer-publish)
       │
       ▼
Authoritative Kubernetes Manifests (Kustomize Base in deployment/kubernetes/base)
       │
       ▼
Environment Overlays (MicroK8s in deployment/kubernetes/environments/microk8s)
       │
       ▼
Ansible Automation (Orchestration in deployment/ansible)
```

Application code contains no environment-specific conditional branching (`if (microk8s)` or `if (kubernetes)`). All infrastructure variations reside in declarative Kustomize overlays and Ansible group variables.

---

## 2. Ubuntu Host Prerequisites & Sizing

### Supported Operating Systems
- Ubuntu Linux 22.04 LTS (Jammy Jellyfish) - x86_64 / arm64
- Ubuntu Linux 24.04 LTS (Noble Numbat) - x86_64 / arm64

### Hardware Sizing Guidelines
| Tier | CPU Cores | RAM | Storage (SSD recommended) | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **Minimum Reference** | 4 vCPU | 8 GB | 40 GB | Single-node functional verification & CI/CD |
| **Recommended Dev/Test** | 8 vCPU | 16 GB | 100 GB | Multi-module scenario testing & simulation |
| **Production Target** | 16+ vCPU | 32+ GB | 250+ GB (Fast NVMe) | High-throughput HIE integration traffic |

### Host OS Prerequisites
The `prepare-microk8s.yml` playbook validates and configures:
1. `snapd` daemon active and enabled.
2. Kernel networking parameters:
   - `net.bridge.bridge-nf-call-iptables = 1`
   - `net.bridge.bridge-nf-call-ip6tables = 1`
   - `net.ipv4.ip_forward = 1`
3. Firewall / security group ingress permissions for ports:
   - `80/tcp`, `443/tcp`: Ingress controller (Clinical, Console, Admin, API)
   - `2575/tcp`: Inbound MLLP Gateway
   - `16443/tcp`: MicroK8s Kubernetes API server

---

## 3. MicroK8s Add-on Architecture

Harmonia requires four authoritative MicroK8s add-ons:

| Add-on | Purpose & Justification |
| :--- | :--- |
| **`dns`** (CoreDNS) | Provides cluster-internal service discovery (e.g., `infinispan-1.harmonia.svc`, `postgres-1.harmonia.svc`, `task-processor.harmonia.svc`). |
| **`ingress`** (Nginx) | Provides unified L7 routing and SSL/TLS termination for Vue SPAs and REST APIs. |
| **`hostpath-storage`** | Dynamic local storage provisioner satisfying PersistentVolumeClaims for PostgreSQL databases and Artemis HA journals. |
| **`metrics-server`** | Collects resource utilization metrics for CPU and memory sizing validation. |

---

## 4. Stateful Infrastructure Topologies

### 4.1 Petasos Messaging (Apache ActiveMQ Artemis 2.33.0 HA)
Petasos utilizes an active/backup paired replication topology:
- **Primary A (`artemis-primary-a`)** & **Backup A (`artemis-backup-a`)**: Replicated journal cluster for `group-a`.
- **Primary B (`artemis-primary-b`)** & **Backup B (`artemis-backup-b`)**: Replicated journal cluster for `group-b`.
- **Clustering**: Symmetric server-side clustering (`petasos-cluster`) with `ON_DEMAND` load balancing and instant message redistribution (`redistribution-delay: 0`).
- **Data Durability**: Each broker instance attaches to a dedicated `PersistentVolumeClaim` backed by local hostpath storage.

### 4.2 Mnemosyne Persistence (PostgreSQL 16)
- Deployed as dedicated StatefulSets (`postgres-1`, `postgres-2`, `postgres-ops-1`, `postgres-ops-2`).
- Each node mounts an isolated persistent volume at `/var/lib/postgresql/data`.
- Default credentials and connection strings are managed via Kubernetes Secrets.

### 4.3 Mneme Cache (Infinispan 15)
- Clustered StatefulSets (`infinispan-1`, `infinispan-2`) with JGroups TCP discovery for distributed caching and operational state.

---

## 5. Security, Secrets & PHI Privacy

### Secret Hygiene
1. No passwords, tokens, API keys, or private keys are stored in Git, Dockerfiles, or unencrypted manifests.
2. Production credentials must be stored in `deployment/ansible/inventory/group_vars/vault.yml` encrypted via `ansible-vault`:
   ```bash
   ansible-vault encrypt deployment/ansible/inventory/group_vars/vault.yml
   ```
3. Secret injection in Ansible executes with `no_log: true` to prevent credential exposure in build logs.

### PHI-Sanitized Logging
All Kubernetes container logs adhere to Harmonia's PHI-sanitized logging invariants:
- Message payloads, patient identifiers, and authentication tokens are omitted from cluster log aggregations.
- Health checks and diagnostic readiness probes report status codes without dumping message bodies.

---

## 6. Safe Undeployment vs. Destructive Data Purge

### Default Undeployment (Safe)
Executing `undeploy-harmonia.yml` removes compute workloads while **retaining all persistent storage**:
- Deployments, StatefulSets, Services, and Ingress resources are removed.
- **PersistentVolumeClaims (PVCs) remain bound and intact.**
- MicroK8s remains running and ready for redeployment.

```bash
ansible-playbook -i inventory/hosts.ini undeploy-harmonia.yml
```

### Destructive Data Purge (Explicit Opt-In)
To permanently purge all database volumes and queues:
```bash
ansible-playbook -i inventory/hosts.ini undeploy-harmonia.yml \
  -e harmonia_purge_data=true
```

---

## 7. Multi-Node Kubernetes Evolution

The manifests defined under `deployment/kubernetes/base` are cloud- and distribution-agnostic:
1. **StorageClass Abstraction**: Replacing `microk8s-hostpath` with standard CSI provisioners (e.g., `ebs.csi.aws.com`, `ceph-rbd`, `nfs-client`) migrates stateful tiers to network-attached SAN/NAS without manifest redesign.
2. **Multi-Node Pod Anti-Affinity**: Overlays for multi-node clusters can inject `podAntiAffinity` rules ensuring `artemis-primary-a` and `artemis-backup-a` schedule onto distinct physical failure domains.
3. **Ingress Controllers**: Standard Ingress definitions seamlessly integrate with AWS ALB Ingress Controller, Traefik, or Contour.
