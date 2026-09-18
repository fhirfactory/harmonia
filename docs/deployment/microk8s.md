# Harmonia MicroK8s Provisioning Guide `[CONFIGURED]`

This guide details the installation, add-on enablement, dynamic storage class configuration, and local container registry integration for Canonical MicroK8s hosting the Harmonia Health Integration Environment.

---

## 1. MicroK8s Snap Installation `[CONFIGURED]`

Harmonia specifies the **1.28/stable** channel for production stability and API compatibility:

```bash
# 1. Install MicroK8s snap with classic confinement
sudo snap install microk8s --classic --channel=1.28/stable

# 2. Grant permissions to current user
sudo usermod -aG microk8s $USER
sudo chown -f -R $USER ~/.kube

# 3. Create convenient command alias for kubectl
echo "alias kubectl='microk8s kubectl'" >> ~/.bashrc
source ~/.bashrc

# 4. Wait for initial node readiness
microk8s status --wait-ready --timeout 180
```

---

## 2. Mandatory Add-on Activation `[CONFIGURED]`

Harmonia requires four foundational MicroK8s add-ons, plus an optional built-in container registry:

```bash
# Enable required add-ons
microk8s enable dns ingress hostpath-storage metrics-server
```

### Architectural Justification of Add-ons:
1. **`dns` (CoreDNS)**:
   - Provides cluster-internal service discovery, resolving headless services such as `infinispan-service`, `petasos-artemis-discovery`, and stateful DB endpoints (`postgres-1`, `postgres-ops-1`, `postgres-synapse`).
2. **`ingress` (Nginx Ingress Controller)**:
   - Evaluates HTTP host headers and path prefixes to route web traffic to Iris SPAs (`iris-clinical`, `iris-console`, `iris-administration`), WildFly BEFE (`befe`), Pylai MLLP Gateway (`mllp-gateway`), and Matrix Synapse (`synapse`).
3. **`hostpath-storage`**:
   - Dynamic persistent storage provisioner dynamically fulfilling PVCs with the `microk8s-hostpath` StorageClass. Guarantees durable disk binding for PostgreSQL databases and Artemis journals.
4. **`metrics-server`**:
   - Collects CPU and memory resource utilization metrics across all pods, supporting observability and auto-scaling evaluations.

---

## 3. StorageClass Configuration & Patching `[CONFIGURED]`

MicroK8s provides the `microk8s-hostpath` storage provisioner.

Verify the default storage class:
```bash
microk8s kubectl get storageclass
```
Expected output:
```
NAME                          PROVISIONER            RECLAIMPOLICY   VOLUMEBINDINGMODE   ALLOWVOLUMEEXPANSION   AGE
microk8s-hostpath (default)   microk8s.io/hostpath   Delete          Immediate           false                  2m
```

The Kustomize overlay (`deployment/kubernetes/environments/microk8s/patches/storage-class-patch.yaml`) automatically patches all StatefulSet volume claim templates to bind directly to `microk8s-hostpath`.

---

## 4. Local Container Registry Configuration `[CONFIGURED]`

For building and testing custom images locally without pushing to an external cloud registry, enable the built-in MicroK8s container registry:

```bash
# 1. Enable built-in Docker registry on port 32000
microk8s enable registry

# 2. Verify registry is active
microk8s kubectl get pods -n container-registry
```

### Image Tagging Convention:
Custom Harmonia images are tagged against `localhost:32000/harmonia/<image-name>:<tag>`:
```bash
# Example: Building and pushing Iris BEFE
docker build -t localhost:32000/harmonia/iris-befe:1.0.0-SNAPSHOT ./iris/iris-befe
docker push localhost:32000/harmonia/iris-befe:1.0.0-SNAPSHOT
```

---

## 5. Cluster Health & Readiness Verification `[CONFIGURED]`

Ensure the cluster has stabilized before deploying workloads:

```bash
# 1. Check MicroK8s daemon status
microk8s status

# 2. Verify node status
microk8s kubectl get nodes -o wide

# 3. Verify kube-system pods
microk8s kubectl get pods -n kube-system
```
All system pods (CoreDNS, Calico/CNI, HostPath provisioner, Ingress controller, Metrics Server) must report `Running`.
