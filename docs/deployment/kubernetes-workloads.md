# Harmonia Kubernetes Workload Specification

This document details the declarative Kubernetes manifests, Kustomize overlays, resource limits, and network policies across the Harmonia platform.

---

## 1. Workload Structure & Kustomize Hierarchy

```
deployment/kubernetes/
├── base/
│   ├── kustomization.yaml
│   ├── namespace.yaml
│   ├── ingress.yaml
│   ├── network-policy.yaml
│   ├── energeia/ (ponos.yaml)
│   ├── hestia/   (mneme-cluster.yaml, mnemosyne-clinical.yaml, mnemosyne-operations.yaml, postgres.yaml)
│   ├── iris/     (iris-befe.yaml, iris-clinical.yaml, iris-console.yaml, iris-administration.yaml)
│   ├── petasos/  (artemis-primary-a.yaml, artemis-backup-a.yaml, artemis-primary-b.yaml, artemis-backup-b.yaml, etc.)
│   └── pylai/    (pylai-mllp-in.yaml, pylai-mllp-out.yaml)
└── environments/
    └── microk8s/
        ├── kustomization.yaml
        └── patches/
            └── storage-class-patch.yaml
```

---

## 2. Resource Requests and Limits Matrix

The following values are copied from the Kubernetes base manifests under `deployment/kubernetes/base/`. The MicroK8s overlay adds `storageClassName: microk8s-hostpath` to the StatefulSet claim templates; that overlay does not change the sizes below.

| Workload | Kind | Replicas | CPU Request / Limit | Memory Request / Limit | Persistent Storage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a` | StatefulSet | 1 | 200m / 1000m | 512Mi / 1Gi | 2Gi claim `artemis-primary-a-data` |
| `artemis-backup-a` | StatefulSet | 1 | 200m / 1000m | 512Mi / 1Gi | 2Gi claim `artemis-backup-a-data` |
| `artemis-primary-b` | StatefulSet | 1 | 200m / 1000m | 512Mi / 1Gi | 2Gi claim `artemis-primary-b-data` |
| `artemis-backup-b` | StatefulSet | 1 | 200m / 1000m | 512Mi / 1Gi | 2Gi claim `artemis-backup-b-data` |
| `infinispan-1` | StatefulSet | 1 | 250m / 1000m | 512Mi / 1536Mi | 1Gi claim `mneme-data-1` |
| `infinispan-2` | StatefulSet | 1 | 250m / 1000m | 512Mi / 1536Mi | 1Gi claim `mneme-data-2` |
| `postgres-1` | StatefulSet | 1 | 100m / 1000m | 256Mi / 1Gi | 2Gi claim `postgres-data-1` |
| `postgres-2` | StatefulSet | 1 | 100m / 1000m | 256Mi / 1Gi | 2Gi claim `postgres-data-2` |
| `postgres-ops-1` | StatefulSet | 1 | 100m / 1000m | 256Mi / 1Gi | 2Gi claim `postgres-ops-data-1` |
| `postgres-ops-2` | StatefulSet | 1 | 100m / 1000m | 256Mi / 1Gi | 2Gi claim `postgres-ops-data-2` |
| `hapi-fhir-jpa-server-1` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A (connects to `postgres-1`) |
| `hapi-fhir-jpa-server-2` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A (connects to `postgres-2`) |
| `hie-operations-jpa-server-1` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A (connects to `postgres-ops-1`) |
| `hie-operations-jpa-server-2` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A (connects to `postgres-ops-2`) |
| `task-processor` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A |
| `mllp-gateway` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A |
| `mllp-outbound-his` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A |
| `mllp-outbound-lis` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A |
| `befe` | Deployment | 1 | 250m / 1000m | 512Mi / 1536Mi | N/A |
| `iris-clinical` | Deployment | 1 | 50m / 500m | 64Mi / 256Mi | N/A (Static SPA) |
| `iris-console` | Deployment | 1 | 50m / 500m | 64Mi / 256Mi | N/A (Static SPA) |
| `iris-administration` | Deployment | 1 | 50m / 500m | 64Mi / 256Mi | N/A (Static SPA) |

---

## 3. StorageClass Patching

In MicroK8s, the persistent volume claims dynamically bind to `microk8s-hostpath`. The overlay `deployment/kubernetes/environments/microk8s/patches/storage-class-patch.yaml` overrides default storage class specifications seamlessly without modifying the root base descriptors.
