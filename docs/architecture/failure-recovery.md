# Harmonia Failure Recovery and Resiliency Architecture

This document defines the fault tolerance models, error handling semantics, idempotency guarantees, and recovery procedures across the Harmonia Health Integration Environment.

---

## 1. Subsystem Failure Matrix

| Failure Mode | Impacted Subsystem | Detection Mechanism | Immediate Behaviour | Automated Recovery Mechanism | Residual Impact & RPO/RTO |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Primary Artemis Broker Crash (`artemis-primary-a`)** | Petasos Messaging Tier | Passive Backup (`artemis-backup-a`) loses heartbeat | Backup detects disconnect and activates live journal; clients reconnect via failover URL. | Artemis HA Journal Replay; client connection retries with exponential backoff. | **RPO = 0** (Synchronously replicated journal); **RTO < 5s** (Automatic failover). |
| **Infinispan Cache Node Crash (`infinispan-1`)** | Mneme Distributed Cache | JGroups FD_SOCK / TCP heartbeat failure | Cluster views updated; Hot Rod clients route read/write traffic to `infinispan-2`. | StatefulSet restarts pod; joins JGroups cluster and triggers state transfer. | **RPO = 0** (Replicated cache mode); **RTO < 1s** (Client failover). |
| **PostgreSQL Node Crash (`postgres-1`)** | Mnemosyne Persistence | Spring Boot HikariCP connection test query failure | Connection pool invalidates broken sockets; retry loop attempts reconnect. | StatefulSet restarts pod; re-mounts persistent hostpath volume; HikariCP reconnects. | **RPO = 0** (WAL flush on commit); **RTO < 15s** (Pod restart & DB recovery). |
| **Inbound Gateway Publish Error (REC-001)** | `pylai-mllp-in` | `sendTaskEvent()` throws exception | Inbound gateway catches exception, aborts ACK, and returns HL7 `AE` (Application Error) NACK. | Upstream clinical system (EMR/PAS) receives `AE` and schedules retransmission. | **RPO = 0** (No unpersisted messages acknowledged). |
| **Downstream Destination Unreachable** | `pylai-mllp-out` | Netty TCP connection timeout / MLLP NACK | Outbound message redelivery scheduled in Petasos queue; Task sub-status marked `FAILED_RETRYING`. | Artemis DLQ / redelivery policy executes max delivery attempts before moving to DLQ. | Other fan-out destinations unaffected (isolated egress queues - REC-002). |
| **Ponos Worker Crash Mid-Activity** | `energeia-ponos` | Artemis unacknowledged consumer session timeout | Unacked JMS message returned to Artemis queue head; available for peer Ponos instances. | Surviving Ponos workers consume message; deduplication prevents duplicate processing. | **At-least-once delivery guaranteed**; idempotent tasks prevent duplicate writes. |
| **Single-Node Ubuntu Host Crash** | All Workloads | Physical hardware / OS monitor | All pods terminate simultaneously; host reboot required. | MicroK8s restarts on host boot; PersistentVolumes re-bound; journals replayed. | Workload isolation and process resilience restored; single-node host remains physical single failure point. |

---

## 2. 4-Tier Idempotency Model

To guarantee safe at-least-once message processing without data duplication, Harmonia enforces idempotency across four distinct tiers:

```
[ Inbound Message / Event ]
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│ Tier 1: Artemis Broker Sliding ID Cache                     │
│ Checks `_AMQ_DUPL_ID` header against sliding cache (20k IDs)│
└──────────────────────────┬──────────────────────────────────┘
                           ▼
┌─────────────────────────────────────────────────────────────┐
│ Tier 2: Petasos Client-Side In-Memory Cache                 │
│ `DuplicateDetector` verifies UUID window on consumer        │
└──────────────────────────┬──────────────────────────────────┘
                           ▼
┌─────────────────────────────────────────────────────────────┐
│ Tier 3: Mneme In-Memory Data Grid Verification              │
│ Checks for existing FHIR Resource / Task ID in cache        │
└──────────────────────────┬──────────────────────────────────┘
                           ▼
┌─────────────────────────────────────────────────────────────┐
│ Tier 4: Mnemosyne Relational Unique Constraints             │
│ PostgreSQL schema enforces unique business identifiers      │
└─────────────────────────────────────────────────────────────┘
```

1. **Tier 1 (Artemis `_AMQ_DUPL_ID`)**: Artemis tracks the unique identifier of messages published within a sliding window. Duplicate sends from gateways are dropped before writing to the journal.
2. **Tier 2 (Petasos `DuplicateDetector`)**: Client consumers evaluate message IDs within local sliding windows to discard redundant transport redeliveries.
3. **Tier 3 (Mneme Task State)**: `Ponos` and `Erga` query the active task state in Infinispan before starting execution; completed tasks are skipped.
4. **Tier 4 (Mnemosyne Relational Constraints)**: PostgreSQL enforces uniqueness on `hie_fhir_resources` (`res_type`, `res_id`, `res_version`) and `hie_operations_resources` (`resource_type`, `resource_id`).

---

## 3. MLLP ACK/NACK Semantics & Ingress Dual-Write Safety (REC-001)

Harmonia strictly implements healthcare ACK semantics over HL7 MLLP:

- **`AA` (Application Accept)**: Sent ONLY when the inbound message has been successfully validated, parsed into a canonical `Communication`/`Task` entity, stored in Mneme, and **successfully published to the Petasos durable queue**.
- **`AE` (Application Error)**: Returned when internal downstream processing fails (e.g., Artemis queue publish throws an exception). Upstream systems interpret `AE` as a temporary failure and retry transmission.
- **`AR` (Application Reject)**: Returned when the HL7 message structure is invalid, unparseable, or fails Themis security policy authorization.

---

## 4. Dead Letter Queue (DLQ) & Redelivery Architecture

- **Address Settings**: Every Petasos queue (`petasos.queue.#`) is configured with a dedicated Dead Letter Address: `petasos.queue.dlq`.
- **Max Delivery Attempts**: Set to `5` by default (`max-delivery-attempts=5`).
- **Redelivery Delay**: Exponential backoff starting at 1,000ms with multiplier `2.0` (1s, 2s, 4s, 8s, 16s).
- **Poison Message Isolation**: Messages exhausting max attempts are routed to `petasos.queue.dlq` without blocking active queue traffic.
- **DLQ Telemetry**: Iris Console monitors DLQ depth and exposes manual redelivery and inspection capabilities.
