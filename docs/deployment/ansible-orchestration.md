# Harmonia Ansible Automation & Orchestration

Harmonia provides declarative Ansible automation under `deployment/ansible/` for host provisioning, cluster configuration, application lifecycle, and teardown.

---

## 1. Playbooks and Lifecycle Workflows

```
deployment/ansible/
├── prepare-microk8s.yml       # Host preparation, kernel tuning, and MicroK8s snap setup
├── deploy-harmonia.yml        # Namespace creation, secret injection, Kustomize deployment & verification
├── undeploy-harmonia.yml      # Workload removal (safe storage retention vs destructive purge)
└── decommission-microk8s.yml  # Complete snap removal and host cleanup
```

---

## 2. Playbook Execution Commands

### 2.1 Host Preparation
```bash
ansible-playbook -i inventory/development/hosts.ini prepare-microk8s.yml
```

### 2.2 Workload Deployment
```bash
ansible-playbook -i inventory/development/hosts.ini deploy-harmonia.yml \
  --ask-vault-pass
```

### 2.3 Safe Undeployment (Preserves Persistent Data)
Removes all Deployments, StatefulSets, Services, and Ingress resources while leaving PersistentVolumeClaims (PostgreSQL data and Artemis journals) intact:
```bash
ansible-playbook -i inventory/development/hosts.ini undeploy-harmonia.yml
```

### 2.4 Destructive Data Purge (Explicit Opt-In)
Permanently deletes all PVCs, database tables, and queues:
```bash
ansible-playbook -i inventory/development/hosts.ini undeploy-harmonia.yml \
  -e harmonia_purge_data=true
```

---

## 3. Secret Management with Ansible Vault

Production credentials must be stored in `deployment/ansible/inventory/group_vars/vault.yml` encrypted via `ansible-vault`:

```bash
# Encrypt secrets file
ansible-vault encrypt deployment/ansible/inventory/group_vars/vault.yml

# Edit encrypted secrets
ansible-vault edit deployment/ansible/inventory/group_vars/vault.yml
```

The `harmonia-deploy` role injects these secrets dynamically into Kubernetes `Secret` resources using `no_log: true` to prevent console leakage.
