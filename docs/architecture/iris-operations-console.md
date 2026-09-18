<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Iris Operations Console Architecture & Operational Specification

## 1. Overview & System Purpose

The **Iris Operations Console** (`iris-console`, formerly known in legacy documents as `iris-monitor`) is Harmonia's dedicated systems administration, reliability engineering, and operational diagnostic portal. Operating on network port 3001, it provides Site Reliability Engineers (SREs), system administrators, and integration specialists with real-time platform observability, queue monitoring, workflow tracing, and incident remediation capabilities.

The operations console is architected strictly around five canonical operational perspectives:

1. **SUBSYSTEMS** — *Is Harmonia healthy?*
   - Platform health summary, runtime inventory of all 9 core subsystems (Pylai, Petasos, Energeia [Ponos, Praxis], Mneme, Mnemosyne, Calliope, Themis, Agora, Iris), runtime instance lifecycle details (Kubernetes Pods / local Docker containers), service health ratios, and downstream dependency round-trip latencies.
2. **QUEUES** — *Is work moving?*
   - Apache ActiveMQ Artemis message broker topology, queue depths, enqueue/dequeue rates, consumer/producer ratios, message age, redeliveries, and Dead-Letter Queue (DLQ) accumulation surveillance.
3. **WORKFLOWS** — *What work is Harmonia currently performing?*
   - Energeia/Praxis workflow execution sequences, active/queued/completed/failed task counts, processing throughput rates, P95 execution durations, and drill-down into individual Pragma execution envelopes and Ergon checkpoints.
4. **EVENTS** — *What happened to a particular interaction?*
   - Cross-subsystem end-to-end diagnostic timeline tracing messages across gateways, brokers, workflow processors, and persistence using technical Correlation IDs, Causation IDs, Message IDs, and Pragma IDs.
5. **ALERTS** — *What currently requires operator attention?*
   - Actionable operational anomalies classified by severity (Critical, Warning, Information) and lifecycle state (Active, Acknowledged, Resolved) with concrete remediation guidance and audit-governed acknowledgment.

---

## 2. Decoupled Presentation Architecture

The Operations Console adheres strictly to **Invariant 3 (Iris Presentation Decoupling)** and **Invariant 7 (Zero-PHI Diagnostic Logging)**.

```
+-------------------------------------------------------------------------+
|                  Browser: iris-console (Vue 3 / Vite SPA)               |
|  [Global Operations Header: Cluster, Health, Alerts, Refresh Indicator] |
|  [Persistent Top Navigation: Subsystems | Queues | Workflows | Events | Alerts] |
+------------------------------------+------------------------------------+
                                     | HTTP/REST (Port 8090)
                                     v
+-------------------------------------------------------------------------+
|                Operations Backend: iris-befe (:8090)                    |
|  - OperationsServerManager (JAX-RS / Undertow HTTP)                     |
|  - Themis Authorization Enforcement (Default-Deny)                      |
|  - OperationsAggregatorService                                          |
|    * KubernetesInstanceProvider (Pod Discovery & Probes)                |
|    * SubsystemHealthProvider SPI (Pylai, Petasos, Energeia, Mneme, etc.)|
+---------------------+-------------------+-------------------+-----------+
                      |                   |                   |
                      v                   v                   v
             Infinispan Grid      Artemis Broker      Subsystem Probes
           (modulestatus-cache,   (Management API,     (:8083, :8085,
            task-cache, etc.)     Jolokia Metrics)     :8092 Actuators)
```

- **Zero Database Leakage**: `iris-console` and `iris-befe` contain zero JPA/Hibernate entities, zero PostgreSQL driver imports, and zero direct database connections.
- **Zero-PHI Presentation Boundary**: Diagnostic timelines and queue details display strictly technical tracking identifiers (`correlationId`, `messageId`, `pragmaId`, counts, durations, and error reason codes). Message payloads, HL7 segments, and FHIR resource bodies are completely excluded.
- **Default-Deny Security (Themis)**: All operations REST endpoints require authenticated callers holding valid operations authorities (`operations.viewer`, `operations.admin`, `system.admin`, or `SYS_ADM`). Unauthorized requests are rejected immediately with HTTP 403 Forbidden.

---

## 3. Normalized Operational REST API Catalog (Port 8090)

The Operations Backend (`iris-befe`) exposes normalized telemetry under `/api/operations/*`:

| Endpoint | Method | Query Parameters | Description |
| :--- | :--- | :--- | :--- |
| `/api/operations/summary` | `GET` | — | Platform health, environment, cluster identifier, subsystem counts, active alert counts. |
| `/api/operations/subsystems` | `GET` | — | Full hierarchical inventory of operational subsystems and health states. |
| `/api/operations/subsystems/{id}` | `GET` | — | Overview of a single subsystem by identifier. |
| `/api/operations/subsystems/{id}/instances` | `GET` | — | Runtime instances (role, state, uptime, CPU %, memory MB, pod metadata, errors). |
| `/api/operations/subsystems/{id}/health` | `GET` | — | Availability %, failed operations, restart counts, P95 latency, and dependency latencies. |
| `/api/operations/subsystems/{id}/statistics` | `GET` | `window` (`15m`, `1h`, `6h`, `24h`) | Time-series metrics (events in/out, throughput, latency percentiles). |
| `/api/operations/queues` | `GET` | — | Petasos broker topology, message depth, consumers, producers, rates, DLQ counts. |
| `/api/operations/queues/{id}` | `GET` | — | Queue details and historical depth trend. |
| `/api/operations/workflows` | `GET` | — | Praxis sequence execution counts (active, queued, completed, failed) and processing rates. |
| `/api/operations/workflows/{id}` | `GET` | — | Single workflow sequence summary. |
| `/api/operations/pragmas/{id}` | `GET` | — | Pragma lifecycle details, retry count, and Ergon execution checkpoints. |
| `/api/operations/events` | `GET` | `correlationId`, `causationId`, `messageId`, `pragmaId`, `subsystem`, `eventType`, `status`, `from`, `to` | Filtered cross-subsystem interaction diagnostic events. |
| `/api/operations/events/{id}` | `GET` | — | Single event details with correlated flow metadata. |
| `/api/operations/alerts` | `GET` | `severity` (`CRITICAL`, `WARNING`, `INFORMATION`), `status` (`ACTIVE`, `ACKNOWLEDGED`, `RESOLVED`), `subsystem` | Filtered active and historical operational alerts. |
| `/api/operations/alerts/{id}/acknowledge` | `POST` | Payload: `{"operator": "name"}` | Transitions active alert status to `ACKNOWLEDGED`. |

---

## 4. Operational Information Model Register

| Concept | UI Information | Operations API Resource | Authoritative Source | Sampling / Retention |
| :--- | :--- | :--- | :--- | :--- |
| **Platform Summary** | Environment, cluster state, total & degraded subsystems, active alert counts | `GET /api/operations/summary` | Aggregated from Subsystem Providers & K8s | Real-time / 10s polled |
| **Subsystem Overview** | Subsystem name, state, instance count, version | `GET /api/operations/subsystems` | Infinispan `modulestatus-cache` + REST Actuators | Clustered cache / Real-time |
| **Runtime Instance** | Instance name, role, state, uptime, restarts, CPU %, Memory MB | `GET /api/operations/subsystems/{id}/instances` | Kubernetes Pod API (`harmonia` namespace) | Polled / 10s |
| **Instance Detail** | Pod, node, IP, image, version, probe status, errors | `GET /api/operations/subsystems/{id}/instances/{name}` | Kubernetes Pod Spec & Status | On-demand / Polled |
| **Operational Health** | Service state, failed ops, availability %, P95 latency | `GET /api/operations/subsystems/{id}/health` | Subsystem Health Probes & Metrics | Derived / Windowed |
| **Dependency Health** | Downstream dependency name, status, latency ms | `GET /api/operations/subsystems/{id}/health` | Direct ping / Hot Rod / HTTP HEAD | Sampled / 15s |
| **Operational Statistics** | Events In/Out, Tasks Processed/Queued, Processing Rate | `GET /api/operations/subsystems/{id}/statistics` | Infinispan `task-cache` & Actuators | Time windows (15m, 1h, 6h, 24h) |
| **Queue Depth & Rates** | Queue name, address, depth, consumer count, enqueue/dequeue rate, DLQ | `GET /api/operations/queues` | ActiveMQ Artemis Management / Jolokia | Sampled / 5s |
| **Workflow (Praxis)** | Workflow name, active executions, queued/completed/failed work | `GET /api/operations/workflows` | Infinispan `tasksequence-cache` & Ponos | Clustered cache / Real-time |
| **Pragma Execution** | Pragma ID, state, duration, Ergon checkpoints, retry count | `GET /api/operations/pragmas/{id}` | Infinispan `task-cache` & Mnemosyne Ops | Checkpoint event stream |
| **Operational Event** | Correlation ID, causation ID, hop timeline, status, duration | `GET /api/operations/events` | Infinispan `auditevent-cache` & Mnemosyne | Indexed event metadata |
| **Operational Alert** | Severity, condition, component, duration, guidance | `GET /api/operations/alerts` | Health Provider Alert Evaluators | Evaluated on telemetry ingest |

---

## 5. Telemetry Gaps Register (`MONITOR-GAP`)

### `MONITOR-GAP-001`: Granular Container CPU/Memory Metrics Outside Kubernetes
- **Desired Information**: Precise per-instance container CPU and memory utilization percentages in local Docker Compose deployments.
- **Subsystem**: All runtime instances.
- **Current Source**: Kubernetes Metrics API is available in MicroK8s, but local Docker environments lack a uniform cgroup metrics REST endpoint without exposing raw docker socket mounts.
- **Resolution**: Display JVM runtime memory (`Runtime.getRuntime()`) and system load average reported by subsystem health endpoints when running outside Kubernetes; mark CPU percent as `N/A` if unavailable rather than fabricating data.

### `MONITOR-GAP-002`: Durable Alert Acknowledgement Persistence
- **Desired Information**: Persistent recording of operator alert acknowledgements across browser sessions and cluster restarts.
- **Subsystem**: Operations Alert Management.
- **Current Source**: Alert lifecycle is currently evaluated dynamically from live telemetry conditions.
- **Resolution**: Provide in-memory session acknowledgement in the Operations backend with an Infinispan `alert-status-cache` projection; display alerts read-only if persistence is unconfigured.

---

## 6. Architecture Decision Register (`MONITOR-ADR`)

### `MONITOR-ADR-001`: Hybrid Infinispan Cache and REST Endpoint Telemetry Aggregation
- **Status**: Approved.
- **Context**: The Operations backend needs low-latency cluster status without overloading platform subsystems on every browser poll.
- **Decision**: Read cluster state primarily from Infinispan replicated caches (`modulestatus-cache`, `task-cache`, `messagequeue-cache`), and use asynchronous background probes for subsystem HTTP actuator health.
- **Consequences**: Telemetry requests respond within < 50ms without cascading load into downstream services.

### `MONITOR-ADR-002`: Native SVG Time-Series Charting in Vue 3
- **Status**: Approved.
- **Context**: Iris Console requires sparklines and time-series visualisations without introducing heavyweight external dashboard dependencies.
- **Decision**: Implement custom reactive SVG components (`SvgTimeSeriesChart.vue`) directly in Vue 3, integrating natively with Tailwind CSS and the dark theme.
- **Consequences**: Zero external charting bundle weight, 60fps rendering, high contrast accessibility, and total control over layout.

### `MONITOR-ADR-003`: Strict Presentation Zero-PHI Boundary
- **Status**: Approved.
- **Context**: Iris Console provides operational and diagnostic visibility across integration events.
- **Decision**: Operations APIs and console views strictly filter and omit message payloads, FHIR resource JSON, and clinical identifiers, exposing only technical correlation IDs, timestamps, and reason codes.
- **Consequences**: Operations engineers can inspect and diagnose platform issues in production environments without needing clinical data access permissions or risking PHI exposure.

---

## 7. Paradeigma Simulation Scenarios (`IrisOperationsScenario`)

Paradeigma provides automated deterministic operational scenarios under `net.fhirfactory.harmonia.paradeigma.scenarios.operations`:

- `IrisOperationsScenarioProfile.ALL_HEALTHY`: Validates nominal baseline where all 9 subsystems, queues, and workflows operate cleanly.
- `IrisOperationsScenarioProfile.PETASOS_DEGRADED`: Simulates Petasos broker degradation and elevated latency, asserting platform degradation indicators and warning alerts.
- `IrisOperationsScenarioProfile.DLQ_GROWTH`: Simulates dead-letter queue accumulation on inbound queues, verifying queue health warnings.
- `IrisOperationsScenarioProfile.FAILED_PRAGMA`: Simulates a failed Pragma with Ergon checkpoint error (`FhirValidationErgon`), verifying diagnostic drill-down.
- `IrisOperationsScenarioProfile.CRITICAL_ALERT`: Simulates an active critical condition (broker heap exhaustion) and validates the operator remediation guidance and acknowledgment transition.
- **Themis Default-Deny Verification**: Exercises unauthenticated calls against the operations REST API, asserting immediate rejection with HTTP 403 / `ThemisDecision.DENY`.
