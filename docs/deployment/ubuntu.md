# Harmonia Ubuntu Host Preparation Guide `[CONFIGURED]`

This guide provides the step-by-step operating system preparation procedure for setting up an Ubuntu Linux host to run the Harmonia Health Integration Environment under MicroK8s.

---

## 1. User Privileges & Group Configuration `[CONFIGURED]`

MicroK8s executes commands via the `microk8s` group. The non-root deploying user must be granted sudo privileges and added to the `microk8s` group:

```bash
# 1. Create deployment user (if not already present)
sudo adduser --gecos "" harmonia-admin
sudo usermod -aG sudo harmonia-admin

# 2. Add deploying user to microk8s group
sudo usermod -aG microk8s $USER

# 3. Create .kube directory and set appropriate ownership
mkdir -p ~/.kube
sudo chown -f -R $USER ~/.kube

# 4. Activate group changes in current shell session
newgrp microk8s
```

---

## 2. Kernel Parameters & Networking Setup `[CONFIGURED]`

Ensure network packet forwarding and bridged packet filtering are active:

```bash
# 1. Load required kernel modules
sudo modprobe overlay
sudo modprobe br_netfilter

# 2. Persist kernel module loading across reboots
sudo tee /etc/modules-load.d/harmonia-k8s.conf <<EOF
overlay
br_netfilter
EOF

# 3. Apply sysctl parameters
sudo tee /etc/sysctl.d/99-harmonia.conf <<EOF
net.bridge.bridge-nf-call-iptables = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward = 1
fs.file-max = 2097152
fs.inotify.max_user_watches = 524288
fs.inotify.max_user_instances = 8192
vm.max_map_count = 262144
EOF

sudo sysctl --system
```

*Note*: `vm.max_map_count = 262144` and `fs.file-max = 2097152` are critical for Infinispan (Hot Rod), Artemis NIO journal throughput, and high-concurrency Netty MLLP connections.

---

## 3. Host Firewall Configuration (UFW) `[CONFIGURED]`

If Uncomplicated Firewall (UFW) is active on the Ubuntu host, configure rules to allow external integration traffic and MicroK8s internal communications:

```bash
# 1. Allow external perimeter ingress traffic
sudo ufw allow 80/tcp comment 'Harmonia HTTP Ingress'
sudo ufw allow 443/tcp comment 'Harmonia HTTPS Ingress'
sudo ufw allow 2575/tcp comment 'Harmonia MLLP Inbound Gateway'

# 2. Allow MicroK8s cluster internal pod and CNI traffic
sudo ufw allow in on cni0
sudo ufw allow out on cni0
sudo ufw default allow routed

# 3. Allow Kubernetes API server from management network
sudo ufw allow 16443/tcp comment 'MicroK8s Kubernetes API'

# 4. Reload firewall
sudo ufw reload
```

---

## 4. Local Host DNS Resolution (`/etc/hosts`) `[CONFIGURED]`

In a normal local deployment, external DNS servers may not have records for `*.harmonia.local`. Configure local hostname resolution on the host or client workstations pointing to the host IP address (or `127.0.0.1` for single-node installations):

```bash
# Edit /etc/hosts to map Harmonia Ingress virtual hosts
sudo tee -a /etc/hosts <<EOF

# Harmonia HIE Virtual Host Mappings
127.0.0.1 clinical.harmonia.local
127.0.0.1 console.harmonia.local
127.0.0.1 admin.harmonia.local
127.0.0.1 api.harmonia.local
127.0.0.1 gateway.harmonia.local
127.0.0.1 matrix.harmonia.local
EOF
```

### Verification:
```bash
ping -c 1 clinical.harmonia.local
ping -c 1 matrix.harmonia.local
```
Both hostnames must resolve to `127.0.0.1` (or the cluster node IP).
