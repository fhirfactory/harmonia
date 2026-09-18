# Health, Liveness & Readiness Probes Reference

This document specifies probe configurations, actuator metrics, and diagnostic endpoints across Harmonia workloads.

---

## 1. Kubernetes Probe Specifications

| Workload | Liveness Probe | Readiness Probe | Startup Probe |
| :--- | :--- | :--- | :--- |
| `artemis-primary-a/b` | TCP Socket :61616 | HTTP :8161 /console | TCP Socket :61616 (InitialDelay: 30s) |
| `infinispan-1/2` | HTTP :11222/rest/v2/cache-managers/default/health/status | HTTP :11222/rest/v2/cache-managers/default/health/status | HTTP :11222/rest/v2/... (InitialDelay: 20s) |
| `postgres-1/2` | `pg_isready -U fhir_user -d fhir_node_1` | `pg_isready -U fhir_user -d fhir_node_1` | `pg_isready` (InitialDelay: 10s) |
| `mnemosyne-clinical` | HTTP :8080/actuator/health/liveness | HTTP :8080/actuator/health/readiness | HTTP :8080/actuator/health (InitialDelay: 30s) |
| `mnemosyne-operations`| HTTP :8080/actuator/health/liveness | HTTP :8080/actuator/health/readiness | HTTP :8080/actuator/health (InitialDelay: 20s) |
| `pylai-mllp-in` | HTTP :8080/actuator/health/liveness | TCP Socket :2575 | HTTP :8080/actuator/health |
| `pylai-mllp-out` | HTTP :8080/actuator/health/liveness | HTTP :8080/actuator/health/readiness | HTTP :8080/actuator/health |
| `ponos` | HTTP :8080/actuator/health/liveness | HTTP :8080/actuator/health/readiness | HTTP :8080/actuator/health (InitialDelay: 30s) |
| `iris-befe` | HTTP :8080/api/operations/health | HTTP :8080/api/operations/health | HTTP :9990/management (InitialDelay: 30s) |
| `iris-clinical` | HTTP :80/healthz | HTTP :80/healthz | HTTP :80/healthz |
| `iris-console` | HTTP :80/healthz | HTTP :80/healthz | HTTP :80/healthz |
| `iris-administration`| HTTP :80/healthz | HTTP :80/healthz | HTTP :80/healthz |

---

## 2. Spring Boot Actuator & Metrics Endpoints

- **Health Check**: `GET http://<service-pod>:8080/actuator/health`
- **Prometheus Metrics**: `GET http://<service-pod>:8080/actuator/prometheus`
- **Info & Version**: `GET http://<service-pod>:8080/actuator/info`
