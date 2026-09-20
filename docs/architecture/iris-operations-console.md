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

The operations console is architected around six canonical operational perspectives structured across a persistent, light-themed application shell:

1. **OVERVIEW** (`/overview`) — *Is Harmonia healthy? Is work moving? Are messages backing up?*
   - Concise, high-density platform health strip, active alert ribbons with direct subsystem links, active work execution rates, and area-by-area operational status summary. Giant dark KPI cards are eliminated in favor of restrained, desktop-optimized summary strips.
2. **SUBSYSTEMS** (`/subsystems`) — *What is the authoritative architectural status?*
   - Proving ground Split-Tree Master-Detail layout: Left panel presents Harmonia's 6 authoritative architectural areas (Integration & Transport, Execution & Processing, Information & State, Security, Collaboration, Presentation) and child subsystems with accessible status badges. Right master-detail panel provides `IrisSubsystemIdentity` (Greek mythological name + mandatory English subtitle), `IrisToolbar` actions, and tabbed inspection of Runtime Instances, Operational Health & Dependencies, and Telemetry Statistics. Middleware runtimes (Artemis, Infinispan, PostgreSQL, Synapse) are subordinated under their consuming capabilities (Petasos, Mneme, Mnemosyne, Agora).
3. **INTERFACES** (`/interfaces`) — *Are protocol gateways listening and communicating?*
   - Compact operational table of Pylai inbound and outbound gateways (MLLP Inbound, MLLP Outbound HIS/LIS, FHIR Provider Registry Gateway) with ports, protocols, live status, and compliance tracking for Invariant 4 (Dual-Write AA ACK safety) and Invariant 5 (Destination Fan-Out).
4. **WORK** (`/work`, formerly `/workflows`) — *What tasks and workflows are currently executing?*
   - Energeia Ponos task worker rates, Praxis workflow sequence definitions, and deep diagnostic drill-down into concrete Pragma execution envelopes and Ergon checkpoint timelines.
5. **MESSAGES** (`/messages`, formerly `/queues`) — *How are messaging transport and queues performing?*
   - Petasos-centric messaging view subordinating Apache ActiveMQ Artemis cluster node health, queue depth, consumers, producers, enqueue/dequeue throughput rates, and Dead-Letter Queue (DLQ) accumulation surveillance.
6. **HEALTH & ALERTS** (`/health`, `/alerts`) — *What requires triage or operator remediation?*
   - Cross-subsystem operational health matrix, dependency latencies, actionable operational alerts classified by severity (Critical, Warning, Information) and status (Active, Acknowledged, Resolved) with structured remediation guidance ribbons.
7. **EVENTS** (`/events`) — *What happened to a particular interaction?*
   - Cross-subsystem diagnostic timeline tracing messages across gateways, brokers, workflow processors, and persistence using technical Correlation IDs, Causation IDs, Message IDs, and Pragma IDs with dual Flowchart and Table views.

---

## 2. Decoupled Presentation Architecture

The Operations Console adheres strictly to **Invariant 3 (Iris Presentation Decoupling)** and **Invariant 7 (Zero-PHI Diagnostic Logging)**.

```
+-------------------------------------------------------------------------+
|                  Browser: iris-console (Vue 3 / Vite SPA)               |
|  [IrisApplicationShell: Cluster, Health, Active Alerts, Clock, Refresh] |
|  [IrisPrimaryNavigation: Overview | Subsystems | Interfaces | Work      |
|   | Messages | Health]                                                  |
|  [Shared Iris Design System Foundation: @harmonia/iris-befe / PrimeVue4]|
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

## 5. Telemetry Gaps Register (`IRIS-API-GAP` / `MONITOR-GAP`)

### `IRIS-API-GAP-001`: Live Interface Throughput and Error Metrics
- **Desired Information**: Real-time message ingress/egress rates (`events_in`, `events_out`) and error counters per Pylai gateway endpoint (`pylai-mllp-in`, `pylai-mllp-out-his`, `pylai-mllp-out-lis`, `pylai-fhir-registry`).
- **Subsystem**: Pylai (Interface Gateways).
- **Architectural Source**: Pylai Camel metrics (`camel-management` / JMX) or Infinispan `modulestatus-cache`.
- **Current Limitation**: `PylaiHealthProvider` returns empty time-series for `events_in` and `events_out` unless populated by live Camel MBean hooks.
- **UI Handling**: Display gateway connectivity, listening ports (`2575`, `8084`, `8087`, `8088`, `8089`), and health status derived from live Pylai subsystem state; display honest placeholder "— (Telemetry initializing)" for throughput micro-charts and "—" for error rates until live Camel telemetry is connected.

### `IRIS-API-GAP-002`: Granular Synapse Matrix Collaboration Metrics
- **Desired Information**: Active Matrix room counts, room creation lifecycle rates, and AS transaction rates.
- **Subsystem**: Agora (Matrix Homeserver & Application Service Bridge).
- **Architectural Source**: Agora AS transaction bridge (`agora-service`) and Synapse Admin API.
- **Current Limitation**: `AgoraHealthProvider` reports status based on Synapse connectivity probe without exposing historical room event counters.
- **UI Handling**: Display Synapse connection status (`HEALTHY` / `UNAVAILABLE`) and AS bridge port (`8095`), noting event telemetry as pending live probe.

### `IRIS-API-GAP-003`: Granular Interface Restart History and Wire Diagnostics
- **Desired Information**: Per-interface restart timeline, NACK reason breakdown (e.g. MLLP AE vs AR response codes), and wire connection latency.
- **Subsystem**: Pylai (Interface Gateways).
- **Architectural Source**: Pylai Netty/Mina MLLP server audit log and Themis policy rejection telemetry.
- **Current Limitation**: `iris-befe` aggregates restart counts at the subsystem level rather than per-listener port/socket.
- **UI Handling**: Display overall Pylai subsystem restart count and provide link to Events perspective filtered by `subsystem=pylai` for diagnostic tracing.

### `IRIS-API-GAP-004` (formerly `MONITOR-GAP-001`): Granular Container CPU/Memory Metrics Outside Kubernetes
- **Desired Information**: Precise per-instance container CPU and memory utilization percentages in local Docker Compose deployments.
- **Subsystem**: All runtime instances.
- **Current Source**: Kubernetes Metrics API is available in MicroK8s, but local Docker environments lack a uniform cgroup metrics REST endpoint without exposing raw docker socket mounts.
- **Resolution**: Display JVM runtime memory (`Runtime.getRuntime()`) and system load average reported by subsystem health endpoints when running outside Kubernetes; mark CPU percent as `N/A` if unavailable rather than fabricating data.

### `IRIS-API-GAP-005` (formerly `MONITOR-GAP-002`): Durable Alert Acknowledgement Persistence
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

### `MONITOR-ADR-002`: Native SVG Time-Series Charting in Vue 3 with Iris Design Tokens
- **Status**: Approved.
- **Context**: Iris Console requires sparklines and time-series visualisations without introducing heavyweight external dashboard dependencies.
- **Decision**: Implement custom reactive SVG components (`SvgTimeSeriesChart.vue`) directly in Vue 3, integrating natively with Iris Design System design tokens and the Aura Light theme.
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
