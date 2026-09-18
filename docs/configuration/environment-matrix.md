# Harmonia Environment Configuration Matrix

This matrix compares configuration overlays across Harmonia deployment environments.

---

## 1. Environment Comparison Matrix

| Configuration Aspect | Local Development (Docker Compose) | CI / Unit Testing (Maven) | Reference Deployment (MicroK8s) | Enterprise Production (Multi-Node K8s) | Paradeigma Simulation Environment |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Artemis Messaging** | 4 Docker containers (Primary A/B, Backup A/B) | Embedded in-memory Artemis cluster | 4 StatefulSet pods (`artemis-*`) with HostPath PVCs | Multi-node StatefulSets with SAN/CSI storage | Production cluster + Paradeigma synthetic event stream |
| **Mneme Cache** | 2 Infinispan containers (`REPL_SYNC`) | Local in-memory cache | 2 StatefulSet pods (`infinispan-*`) | Clustered StatefulSets with Pod Anti-Affinity | Standard Mneme cluster |
| **Mnemosyne Persistence** | 4 PostgreSQL containers (`fhir_node_1/2`, `ops_node_1/2`) | Embedded H2 in-memory DB | 4 PostgreSQL StatefulSets (`postgres-*`) | Dedicated PostgreSQL HA clusters / Cloud SQL | Dedicated test DB instances / test schemas |
| **Iris Presentation** | Vite dev server / Docker Nginx | Unit tests / JSDOM | Nginx containers behind MicroK8s Ingress | HA Ingress with CDN caching & TLS cert-manager | Clinical, Console & Admin SPAs active |
| **Paradeigma Simulators**| Optional (`docker-compose --profile simulation`) | ArchUnit & Unit tests only | Excluded from normal deployment | Excluded from normal production | Active synthetic workload containers (EMR, LMS, PAS, RIS-PAC) |
| **Security Enforcement** | Themis Default-Deny (Local Keys) | Mocked Themis Service | Themis Default-Deny with Kubernetes Secrets | Themis with Vault / KMS secret rotation | Synthetic personas with designated test roles |
