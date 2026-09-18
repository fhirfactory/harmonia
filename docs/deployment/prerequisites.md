# Harmonia Host & Environment Prerequisites `[CONFIGURED]`

This document specifies the technical prerequisites, hardware sizing, Linux kernel tuning, and package dependencies required to host the Harmonia Health Integration Environment on an authoritative Ubuntu Linux MicroK8s cluster.

---

## 1. Supported Operating Systems `[IMPLEMENTED]`

Harmonia is officially verified on 64-bit Debian-family Linux operating systems:
- **Ubuntu Linux 22.04 LTS (Jammy Jellyfish)** — `x86_64` (amd64) and `arm64` (aarch64)
- **Ubuntu Linux 24.04 LTS (Noble Numbat)** — `x86_64` (amd64) and `arm64` (aarch64)

The deployment automation (`deployment/ansible/roles/microk8s/tasks/prerequisites.yml`) asserts that `ansible_os_family == "Debian"`. Other Linux distributions (RHEL, Fedora, Arch) are unsupported for the reference MicroK8s topology.

---

## 2. Hardware Sizing & Capacity Planning `[CONFIGURED]`

The platform compute bounds require adequate physical or virtualized capacity to run all 25 containers concurrently without CPU throttling or memory swapping:

| Deployment Tier | Minimum vCPUs | Minimum RAM | Storage Allocation | Target Workload & Scenario |
| :--- | :--- | :--- | :--- | :--- |
| **Minimum** | 4 vCPU | 8 GB RAM | 40 GB SSD | Development, CI/CD pipeline, and single-scenario functional testing. |
| **Recommended** | 8 vCPU | 16 GB RAM | 100 GB SSD | Multi-scenario integration testing, synthetic simulation, and QA validation. |
| **Target / Production** | 16+ vCPU | 32+ GB RAM | 250+ GB NVMe | Continuous clinical HIE operations, high-throughput MLLP traffic, 90-day retention. |

### Memory Swap Invariant
Kubernetes requires memory swap to be either disabled or controlled. On Ubuntu, ensure swap is disabled to guarantee predictable memory residency and prevent JVM latency spikes:
```bash
sudo swapoff -a
sudo sed -i '/swap/d' /etc/fstab
```

---

## 3. Host Linux Kernel & Network Tuning `[CONFIGURED]`

MicroK8s relies on bridge netfilter and IP forwarding to handle pod-to-pod and pod-to-external packet routing:

### Required Sysctl Parameters:
- `net.bridge.bridge-nf-call-iptables = 1`: Enables iptables packet filtering for bridged IPv4 traffic.
- `net.bridge.bridge-nf-call-ip6tables = 1`: Enables ip6tables packet filtering for bridged IPv6 traffic.
- `net.ipv4.ip_forward = 1`: Enables kernel packet forwarding between network interfaces.

### Persistent Kernel Configuration:
Apply these parameters permanently by writing to `/etc/sysctl.d/99-kubernetes-cri.conf`:
```bash
sudo tee /etc/sysctl.d/99-kubernetes-cri.conf <<EOF
net.bridge.bridge-nf-call-iptables = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward = 1
EOF

sudo sysctl --system
```

---

## 4. Host OS Packages & Dependencies `[CONFIGURED]`

The following base utilities must be installed prior to provisioning MicroK8s:

```bash
sudo apt-get update && sudo apt-get install -y \
  snapd \
  iptables \
  curl \
  conntrack \
  socat \
  ca-certificates \
  jq \
  netcat-openbsd
```

### Dependency Rationale:
- `snapd`: Required to install and manage the MicroK8s snap container.
- `iptables`: Required for Kubernetes kube-proxy and network policy enforcement.
- `curl` & `netcat-openbsd`: Used for post-deployment verification and MLLP handshake testing.
- `conntrack` & `socat`: Required by Kubernetes networking primitives for port-forwarding and connection tracking.
- `ca-certificates`: Required for TLS trust validation against external registries.

Ensure the `snapd` systemd service is active:
```bash
sudo systemctl enable --now snapd
```
