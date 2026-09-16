# Harmonia Recovery Guarantees & Objectives

## 1. Formal Delivery & Recovery Semantics

Harmonia defines explicit delivery semantics across each subsystem boundary. Due to the distributed nature of TCP MLLP transport and multi-broker messaging, delivery guarantees must be analyzed per interface boundary rather than as a monolithic claim.

```
                  DELIVERY SEMANTICS ACROSS BOUNDARIES
                  
  PAS / External ──► [Inbound Gateway] ──► [Artemis Queue] ──► [Ponos Worker] ──► [Outbound MLLP]
      (MLLP)              (Pylai)             (Petasos)            (Erga)              (Pylai)
         │                   │                    │                   │                   │
         ▼                   ▼                    ▼                   ▼                   ▼
     At-Least-Once       At-Least-Once       At-Least-Once       Effectively-Once    At-Least-Once
    (with MSH-10 ACK)    (DB Commit)        (Journal Fsync)     (Task Idempotency)   (Destination ACK)
```

---

## 2. Guarantee Scope per Processing Phase

| Processing Boundary | Formal Guarantee | Implementation Mechanism | Justification & Edge Conditions |
| :--- | :--- | :--- | :--- |
| **Inbound MLLP Ingress** (Source -> Pylai) | **At-Least-Once** | TCP socket framing + HL7 `AA`/`AE`/`AR` acknowledgment | If the connection drops before the sender receives `AA`, the sender retries the same message (`MSH-10`). |
| **Petasos Message Transport** (Pylai -> Artemis -> Ponos) | **At-Least-Once** | Persistent disk journal (`./data/journal`) + explicit consumer client acknowledgment | Messages survive broker restarts and are redelivered if a consumer crashes before sending ACK. |
| **Application Entity Persistence** (Mnemosyne / PostgreSQL) | **Effectively-Once** | Database unique constraint `uk_resource_type_fhir_id` + `version_id` optimistic locking | Duplicate inserts are rejected; idempotent updates overwrite with incremented version. |
| **Ponos Workflow Execution** (Ponos -> Erga Activity) | **Effectively-Once** | Task state checking (`PragmaStatus`) + Artemis deduplication (`_AMQ_DUPL_ID`) | Re-delivered events evaluate current task state in cache before re-executing business logic. |
| **Outbound MLLP Egress** (Pylai -> External Destination) | **At-Least-Once** | Camel MLLP producer retries + isolated destination queues | If network drops after destination receives message but before Harmonia receives `AA`, a duplicate may be sent upon retry. External systems must support `MSH-10` deduplication. |

---

## 3. Recovery Point Objective (RPO) & Recovery Time Objective (RTO)

### 3.1 Recovery Point Objective (RPO)
RPO defines the maximum acceptable data loss measured in time across different disaster scenarios.

| Subsystem / Phase | Recovery Point Objective (RPO) | Description |
| :--- | :--- | :--- |
| **Durable Ingress (Post-ACK)** | **RPO = 0** | Once Harmonia returns an HL7 `AA` ACK, the message is durably recorded in PostgreSQL and committed to the Artemis journal. No data loss occurs during broker or database crash. |
| **In-Flight Queue Processing** | **RPO = 0** | Unacknowledged messages in transit remain in the Artemis journal and are fully recovered and redelivered upon restart. |
| **Pre-Ingress Network Transit** | **N/A (Managed by Source)** | If power or network fails before Harmonia returns `AA`, the source system retains the message and retries delivery upon reconnection. |

### 3.2 Recovery Time Objective (RTO)
RTO defines the duration required to restore full message processing capacity following an outage.

| Disaster Scenario | Target RTO | Automated vs. Manual Recovery |
| :--- | :--- | :--- |
| **Ponos Worker Node Failure** | **< 1 second** | **Fully Automated**: Artemis immediately redistributes messages to other active workers (`redistribution-delay=0`). |
| **Artemis Primary Broker Failure (HA)** | **< 2 seconds** | **Fully Automated**: Backup broker takes over master replication lock and begins servicing clients. |
| **Artemis Single Broker Restart** | **< 15 seconds** | **Automated on service start**: Reads and verifies journal index, opens acceptor ports. |
| **PostgreSQL Database Restart** | **< 30 seconds** | **Automated on service start**: Replays WAL logs to consistent checkpoint. |
| **Complete Cold Platform Restart** | **< 60 seconds** | **Automated orchestration**: Compose / Kubernetes dependency startup sequence (DB -> Cache -> Artemis -> Ponos -> Pylai). |

---

## 4. Operational Invariants

To guarantee data consistency across recoveries, the following invariants are strictly maintained:
1. **No Phantom ACKs**: An HL7 `AA` ACK is NEVER returned to a clinical source system before the message payload has been parsed, validated, and stored in persistent storage.
2. **Deterministic Fan-Out Isolation**: Outbound message queues for fan-out destinations (EMR, LMS, RIS-PAC) are isolated; failure or outage of one destination queue NEVER delays or causes duplicate delivery to other healthy destination queues.
3. **No Unbounded Poison Pill Retries**: Failed messages are attempted exactly 3 times before being segregated into the `DLQ`, ensuring active production queues never experience pipeline deadlock.
