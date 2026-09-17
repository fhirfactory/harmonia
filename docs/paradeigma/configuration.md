# Paradeigma Configuration Reference `[CONFIGURED]`

This document provides the authoritative configuration property and environment variable reference for all Paradeigma simulator workloads, fault injectors, and scenario execution engines.

---

## 1. Dual-Dimension Configuration Discipline `[CONFIGURED]`

Like the rest of Harmonia, Paradeigma enforces strict separation between:
1. **Infrastructure Configuration**: Container ports, host network bindings, CPU/memory resource allocations, and health probes.
2. **Application Configuration**: Clinical execution profiles, timer frequencies, seed random parameters, and failure injection thresholds.

---

## 2. Workload Configuration Properties `[CONFIGURED]`

### 2.1 Patient Administration System Simulator (`paradeigma-pas`)

| Property / Environment Variable | Default Value | Valid Options | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `8091` | `1024-65535` | Spring Boot HTTP REST management port |
| `HARMONIA_HOST` | `mllp-gateway` | Hostname / IP | Target Harmonia Inbound MLLP gateway hostname |
| `HARMONIA_PAS_ADT_PORT` | `2101` | Port number (e.g. `2101`, `2575`) | Target MLLP port on Harmonia for ADT ingress (PD-01) |
| `PAS_TIMER_ENABLED` | `false` | `true`, `false` | Enables autonomous background generation of ADT events |
| `PAS_TIMER_MODE` | `FIXED` | `FIXED`, `RANDOM_RANGE` | Timer pacing mode |
| `PAS_FIXED_INTERVAL_MS` | `5000` | Positive Integer | Delay in milliseconds between events when in `FIXED` mode |
| `PAS_RANDOM_MIN_DELAY_MS` | `2000` | Positive Integer | Minimum interval when in `RANDOM_RANGE` mode |
| `PAS_RANDOM_MAX_DELAY_MS` | `10000` | Positive Integer | Maximum interval when in `RANDOM_RANGE` mode |
| `PAS_EXECUTION_PROFILE` | `DEMO` | `TEST`, `DEMO`, `LOAD` | Pacing and concurrency execution profile |
| `PAS_RANDOM_SEED` | `42` | Long Integer | Pseudorandom seed for deterministic patient demographics |

### 2.2 Electronic Medical Record Simulator (`paradeigma-emr`)

| Property / Environment Variable | Default Value | Valid Options | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `8092` | `1024-65535` | Spring Boot HTTP REST management port |
| `EMR_ADT_INBOUND_PORT` | `2201` | Port number | MLLP listener port for receiving ADT demographic broadcasts (PD-05) |
| `HARMONIA_HOST` | `mllp-gateway` | Hostname / IP | Target Harmonia Inbound MLLP gateway hostname |
| `HARMONIA_EMR_ORM_PORT` | `2104` | Port number | Target MLLP port on Harmonia for ORM order ingress (PD-04) |
| `EMR_TIMER_ENABLED` | `false` | `true`, `false` | Enables autonomous background generation of lab/imaging orders |
| `EMR_TIMER_MODE` | `FIXED` | `FIXED`, `RANDOM_RANGE` | Order generation timer mode |
| `EMR_FIXED_INTERVAL_MS` | `7000` | Positive Integer | Interval in milliseconds between autonomous orders |
| `EMR_EXECUTION_PROFILE` | `DEMO` | `TEST`, `DEMO`, `LOAD` | Execution profile |

### 2.3 Laboratory Management System Simulator (`paradeigma-lms`)

| Property / Environment Variable | Default Value | Valid Options | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `8093` | `1024-65535` | Spring Boot HTTP REST management port |
| `LMS_ADT_INBOUND_PORT` | `2202` | Port number | MLLP listener port for receiving ADT demographic broadcasts (PD-06) |
| `LMS_ORM_INBOUND_PORT` | `2204` | Port number | MLLP listener port for receiving routed pathology orders (PD-08) |
| `HARMONIA_HOST` | `mllp-gateway` | Hostname / IP | Target Harmonia Inbound MLLP gateway hostname |
| `HARMONIA_LMS_ORU_PORT` | `2102` | Port number | Target MLLP port on Harmonia for ORU result ingress (PD-02) |
| `LMS_AUTO_PRODUCE_RESULTS` | `true` | `true`, `false` | Automatically generates and emits ORU results upon receiving an ORM order |
| `LMS_RESULT_DELAY_MS` | `2000` | Positive Integer | Simulated analyzer processing delay before result emission |
| `LMS_EXECUTION_PROFILE` | `DEMO` | `TEST`, `DEMO`, `LOAD` | Execution profile |

### 2.4 Radiology & PACS Simulator (`paradeigma-rispac`)

| Property / Environment Variable | Default Value | Valid Options | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `8094` | `1024-65535` | Spring Boot HTTP REST management port |
| `RISPAC_ADT_INBOUND_PORT` | `2203` | Port number | MLLP listener port for receiving ADT demographic broadcasts (PD-07) |
| `RISPAC_ORM_INBOUND_PORT` | `2205` | Port number | MLLP listener port for receiving routed imaging orders (PD-09) |
| `HARMONIA_HOST` | `mllp-gateway` | Hostname / IP | Target Harmonia Inbound MLLP gateway hostname |
| `HARMONIA_RISPAC_ORU_PORT` | `2103` | Port number | Target MLLP port on Harmonia for ORU report ingress (PD-03) |
| `RISPAC_AUTO_PRODUCE_REPORTS`| `true` | `true`, `false` | Automatically generates and emits radiology reports upon order receipt |
| `RISPAC_REPORTING_DELAY_MS` | `3000` | Positive Integer | Simulated radiologist interpretation delay before report emission |
| `RISPAC_EXECUTION_PROFILE` | `DEMO` | `TEST`, `DEMO`, `LOAD` | Execution profile |

### 2.5 Scenario Orchestration Engine (`paradeigma-scenarios`)

| Property / Environment Variable | Default Value | Valid Options | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `8090` | `1024-65535` | Spring Boot HTTP REST management port |
| `PAS_BASE_URL` | `http://paradeigma-pas:8091` | HTTP URL | PAS simulator management base URL |
| `EMR_BASE_URL` | `http://paradeigma-emr:8092` | HTTP URL | EMR simulator management base URL |
| `LMS_BASE_URL` | `http://paradeigma-lms:8093` | HTTP URL | LMS simulator management base URL |
| `RISPAC_BASE_URL` | `http://paradeigma-rispac:8094`| HTTP URL | RIS-PAC simulator management base URL |
| `SCENARIO_PROFILE` | `DEMO` | `TEST`, `DEMO`, `LOAD` | Default execution profile for scenario runs |

---

## 3. Fault Injection Parameters (`FaultInjectionConfig`) `[IMPLEMENTED]`

Each simulator exposes configurable fault injection parameters that can be updated at runtime via `POST /api/{subsystem}/config`:

| Parameter | Type | Default Value | Range | Simulated Failure Effect |
| :--- | :--- | :--- | :--- | :--- |
| `dropConnectionProbability` | `double` | `0.0` | `0.0 – 1.0` | Abruptly terminates the TCP socket during transmission without sending an ACK |
| `noAckProbability` | `double` | `0.0` | `0.0 – 1.0` | Keeps socket open but fails to return an ACK, forcing upstream MLLP timeout |
| `ackDelayMs` | `long` | `0` | `0 – 60000` | Injects artificial latency in milliseconds prior to returning synchronous ACK |
| `applicationErrorProbability` | `double` | `0.0` | `0.0 – 1.0` | Returns HL7 `AE` (Application Error) NACK response |
| `applicationRejectProbability`| `double` | `0.0` | `0.0 – 1.0` | Returns HL7 `AR` (Application Reject) NACK response |
| `malformedSegmentProbability` | `double` | `0.0` | `0.0 – 1.0` | Corrupts HL7 segment delimiters or field separators |
| `duplicateMsh10Probability` | `double` | `0.0` | `0.0 – 1.0` | Retransmits previously sent `MSH-10` message control ID for idempotency testing |
