# Harmonia MicroK8s Reference Deployment Guide

This guide specifies the step-by-step procedure for deploying the Harmonia Health Integration Environment onto a single-node Ubuntu MicroK8s cluster.

---

## 1. Host Sizing & Prerequisites

### Supported Operating Systems
- Ubuntu Linux 22.04 LTS (Jammy Jellyfish) - x86_64 / arm64
- Ubuntu Linux 24.04 LTS (Noble Numbat) - x86_64 / arm64

### Hardware Sizing
| Tier | vCPU | RAM | Storage | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **Minimum** | 4 vCPU | 8 GB | 40 GB SSD | Single-node functional validation & CI |
| **Recommended** | 8 vCPU | 16 GB | 100 GB SSD | Full scenario testing & simulation |
| **Target** | 16+ vCPU | 32+ GB | 250+ GB NVMe | High-throughput HIE integration traffic |

### Host OS Kernel & Network Preparation
```bash
# Enable required kernel bridge parameters
sudo sysctl -w net.bridge.bridge-nf-call-iptables=1
sudo sysctl -w net.bridge.bridge-nf-call-ip6tables=1
sudo sysctl -w net.ipv4.ip_forward=1

# Ensure snapd is installed and running
sudo systemctl enable --now snapd
```

---

## 2. MicroK8s Installation & Add-on Configuration

```bash
# 1. Install MicroK8s snap
sudo snap install microk8s --classic --channel=1.28/stable

# 2. Add local user to microk8s group
sudo usermod -a -G microk8s $USER
sudo chown -f -R $USER ~/.kube

# 3. Enable essential Harmonia add-ons
microk8s enable dns ingress hostpath-storage metrics-server

# 4. Wait for MicroK8s readiness
microk8s status --wait-ready
```

---

## 3. Workload Deployment via Kustomize

```bash
# 1. Create target namespace
microk8s kubectl create namespace harmonia --dry-run=client -o yaml | microk8s kubectl apply -f -

# 2. Apply Harmonia MicroK8s Kustomize Overlay
microk8s kubectl apply -k deployment/kubernetes/environments/microk8s

# 3. Monitor Pod Deployment Progress
microk8s kubectl get pods -n harmonia -w
```

---

## 4. Verification & Smoke Testing

1. **Verify Pod Status**:
   All 17 core pods (`artemis-*`, `infinispan-*`, `postgres-*`, `mnemosyne-*`, `pylai-*`, `ponos`, `iris-*`) should report `Running` or `Completed`.
   ```bash
   microk8s kubectl get pods -n harmonia
   ```

2. **Verify Ingress Routing**:
   ```bash
   curl -k http://localhost/api/fhir/Patient
   curl -k http://localhost/api/operations/status
   ```

3. **Verify MLLP Inbound Port**:
   ```bash
   nc -zv localhost 2575
   ```
