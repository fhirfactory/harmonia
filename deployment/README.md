# Harmonia Kubernetes & MicroK8s Deployment Framework

This directory contains the authoritative Kubernetes manifests, Kustomize overlays, and Ansible automation playbooks for deploying and operating the Harmonia Health Integration Platform.

Single-node **MicroK8s on Ubuntu Linux** serves as the initial reference deployment environment.

---

## Architecture Overview

```
Source Code
   │
   ▼
Maven Lifecycle
   │ ├── Compile & Unit Tests (mvn clean verify)
   │ └── Container Build Profile (mvn clean verify -Pcontainer-build)
   ▼
Container Registry (e.g., localhost:32000 / private registry)
   │
   ▼
Ansible Automation & Kustomize Orchestration
   │ ├── prepare-microk8s.yml (Ubuntu host & add-on provisioning)
   │ ├── deploy-harmonia.yml  (Declarative K8s deployment & rollout verification)
   │ └── undeploy-harmonia.yml (Safe compute workload removal; retains PVCs)
   ▼
MicroK8s Kubernetes Cluster (Namespace: harmonia)
   ├── Presentation Layer: iris-clinical, iris-console, iris-administration
   ├── API & Gateways:     iris-befe, pylai-mllp-in, pylai-mllp-out
   ├── Workflow Compute:   ponos-task-processor
   └── Stateful Tier:      PostgreSQL (16), ActiveMQ Artemis HA (2.33.0), Infinispan (15)
```

---

## Deployable Harmonia Modules & Container Images

| Module Path | Container Image Name | Technology Stack | Function |
| :--- | :--- | :--- | :--- |
| `hestia/mnemosyne-clinical` | `mnemosyne-clinical` | Spring Boot 3 / HAPI FHIR R5 | Clinical JPA FHIR Persistence Server |
| `hestia/mnemosyne-operations` | `mnemosyne-operations` | Spring Boot 3 | HIE Operations & Metrics JPA Server |
| `hestia/mneme-cluster` | `mneme-cluster` | Infinispan 15 / HotRod | Distributed Operational Cache & Discovery |
| `energeia/ponos` | `ponos-task-processor` | WildFly 31 / Artemis | Task Sequence Workflow Execution Engine |
| `pylai/pylai-mllp-in` | `pylai-mllp-in` | WildFly 31 / Apache Camel | Inbound HL7 v2 / MLLP Gateway |
| `pylai/pylai-mllp-out` | `pylai-mllp-out` | WildFly 31 / Apache Camel | Outbound HL7 v2 / MLLP Gateways |
| `iris/iris-befe` | `iris-befe` | WildFly 31 / JAX-RS | Backend-For-Frontend Presentation API |
| `iris/iris-clinical` | `iris-clinical` | Vue 3 / Vite / Nginx | Clinical Portal Web Application |
| `iris/iris-console` | `iris-console` | Vue 3 / Vite / Nginx | System Monitor & Queue Console SPA |
| `iris/iris-administration` | `iris-administration` | Vue 3 / Vite / Nginx | Identity & RBAC Administration SPA |

---

## Developer Quickstart

### 1. Build and Test Application (Standard Build)
The standard build runs unit and integration tests without interacting with the Docker daemon or remote registries:
```bash
mvn clean verify
```

### 2. Build Container Images
Build local OCI container images using module Dockerfiles:
```bash
mvn clean verify -Pcontainer-build \
  -Dharmonia.container.registry=localhost:32000 \
  -Dharmonia.image.tag=1.0.0-SNAPSHOT
```

### 3. Build and Publish Container Images
Build and push images to your target container registry:
```bash
mvn clean verify -Pcontainer-publish \
  -Dharmonia.container.registry=registry.example.org \
  -Dharmonia.container.namespace=harmonia \
  -Dharmonia.image.tag=1.0.0
```

---

## Ansible Playbook Operations

All Ansible operations are executed from `deployment/ansible/`:

```bash
cd deployment/ansible
```

### 1. Prepare Ubuntu / MicroK8s Host
Installs MicroK8s via snap with pinned channel `1.28/stable`, validates CPU/RAM/disk prerequisites, sets up user permissions, and enables required add-ons (`dns`, `ingress`, `hostpath-storage`, `metrics-server`):
```bash
ansible-playbook -i inventory/hosts.ini prepare-microk8s.yml
```

### 2. Deploy Harmonia Platform
Applies Kustomize manifests, provisions secrets from Ansible Vault, and verifies pod rollouts:
```bash
# Deploy default version (1.0.0-SNAPSHOT)
ansible-playbook -i inventory/hosts.ini deploy-harmonia.yml

# Deploy specific release version
ansible-playbook -i inventory/hosts.ini deploy-harmonia.yml \
  -e harmonia_image_tag=1.2.0 \
  -e harmonia_image_registry=registry.example.org
```

### 3. Safe Undeployment (Preserves Data)
Removes all stateless deployments, stateful compute workloads, services, and ingress routes, **strictly preserving all PersistentVolumeClaims (PVCs) and database data**:
```bash
ansible-playbook -i inventory/hosts.ini undeploy-harmonia.yml
```

### 4. Destructive Undeployment (Data Purge)
To intentionally purge all persistent database storage, queues, and the namespace, pass `-e harmonia_purge_data=true`:
```bash
ansible-playbook -i inventory/hosts.ini undeploy-harmonia.yml \
  -e harmonia_purge_data=true
```

### 5. Decommission MicroK8s Host
Completely uninstalls MicroK8s from the host (destructive cleanup):
```bash
ansible-playbook -i inventory/hosts.ini decommission-microk8s.yml \
  -e microk8s_confirm_decommission=true
```

---

## Directory Structure

```
deployment/
├── README.md                                    # This guide
├── kubernetes/
│   ├── base/                                    # Authoritative Kubernetes resource definitions
│   │   ├── kustomization.yaml                   # Base resource aggregation
│   │   ├── namespace.yaml                       # Dedicated harmonia namespace
│   │   ├── network-policy.yaml                  # Intra-namespace traffic isolation
│   │   ├── ingress.yaml                         # External HTTP routing rules
│   │   ├── hestia/                              # PostgreSQL (16), Mnemosyne, Mneme (Infinispan)
│   │   ├── energeia/                            # Ponos Task Sequence Processor
│   │   ├── pylai/                               # Inbound and Outbound MLLP Gateways
│   │   ├── iris/                                # BEFE API, Clinical, Console, Administration SPAs
│   │   └── petasos/                             # ActiveMQ Artemis HA StatefulSets & ConfigMaps
│   └── environments/
│       └── microk8s/                            # Reference MicroK8s overlay
│           ├── kustomization.yaml               # HostPath storage class and image transformers
│           └── patches/                         # Environment patches
└── ansible/
    ├── ansible.cfg                              # Ansible execution settings
    ├── prepare-microk8s.yml                     # Host preparation playbook
    ├── deploy-harmonia.yml                      # Platform deployment playbook
    ├── undeploy-harmonia.yml                    # Safe teardown playbook
    ├── decommission-microk8s.yml                # Host cleanup playbook
    ├── inventory/
    │   ├── hosts.ini                            # Reference host inventory
    │   ├── development/hosts.ini                # Dev environment inventory
    │   ├── test/hosts.ini                       # Test environment inventory
    │   ├── production/hosts.ini                 # Production environment inventory
    │   └── group_vars/
    │       ├── all.yml                          # Centralized configuration variables
    │       └── vault.yml                        # Encrypted secret templates
    └── roles/
        ├── microk8s/                            # Prerequisite verification & snap installation
        ├── harmonia-deploy/                     # Kustomize rollout & readiness verification
        └── harmonia-undeploy/                   # PVC-safe resource teardown
```

---

## Single-Node Availability Semantics

> **CRITICAL ARCHITECTURAL DISTINCTION**:
> Single-node MicroK8s provides **process and container level resilience** (automatic pod restarts, rolling deployments, health monitoring, service discovery).
> 
> **It does NOT provide host-level High Availability.**
> If the underlying physical or virtual Ubuntu host experiences a hardware failure, power interruption, or kernel panic, all Harmonia services on that host become unavailable. Multiple pods or paired Artemis brokers running on a single host do not protect against host loss.
