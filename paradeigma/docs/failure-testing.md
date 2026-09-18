# Harmonia Paradeigma — Failure Simulation & Resilience Testing

### Overview
Paradeigma simulators include an integrated `FailureSimulator` to inject deterministic, reproducible transport and application-level faults to validate Harmonia's recovery, retry, and auditing behaviors.

---

### Configurable Failure Modes

| Fault Mode | Parameter | Default | Simulated Clinical/Network Event |
|---|---|---|---|
| **Dropped TCP Connection** | `dropConnectionProbability` | `0.0` | Abrupt network disconnect during transmission |
| **No ACK / Timeout** | `noAckProbability` | `0.0` | Network partition or recipient unresponsive |
| **ACK Delay** | `ackDelayMs` | `0` | Latency / high system load on downstream application |
| **Application Error** | `applicationErrorProbability` | `0.0` | Validation failure on receiving application (`AE` ACK) |
| **Application Reject** | `applicationRejectProbability` | `0.0` | Protocol or schema rejection (`AR` ACK) |
| **Malformed Segment** | `malformedSegmentProbability` | `0.0` | Corrupted HL7 payload / syntax violation |
| **Duplicate Message** | `duplicateMsh10Probability` | `0.0` | Duplicate message control ID transmission |

---

### Distinction: Transport Retries vs Application Errors
- **Transport Failures (Network timeouts, connection resets)**:
  Harmonia outbound MLLP pipelines retry transmission using exponential backoff according to configured retry policies.
- **Application Rejections (`AE` / `AR` ACKs)**:
  Application rejections indicate business/semantic rejection by the destination EHR. Harmonia records the non-retryable rejection into audit logs and moves the task into an error/review state rather than blindly retrying.

---

### Duplicate Message Idempotency
When the same `MSH-10` message control ID is retransmitted, Harmonia detects duplicate ingestion via Petasos deduplication filters and returns an idempotent ACK without duplicate task execution.
