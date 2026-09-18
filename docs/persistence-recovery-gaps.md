# Harmonia Persistence & Recovery Implementation Gap Analysis

## 1. Overview

This document records the architectural and implementation gaps identified during the in-depth inspection of Harmonia's persistence, messaging, and recovery models. Each gap is catalogued with an ID, affected area, current behavior, risk assessment, recommended remediation, and priority.

---

## 2. Identified Gap Catalogue

### 2.1 REC-001: Inbound Gateway Dual-Write Failure Window without Transactional Outbox

* **ID**: `REC-001`
* **Area**: Inbound Gateway Persistence & Message Dispatch
* **Affected Component**: `pylai/pylai-mllp-in` (`IncomingAdtMessageProcessor`, `IncomingOrmMessageProcessor`, `IncomingOruMessageProcessor`)
* **Priority**: `HIGH`
* **Current Behavior**:
  The inbound processor writes the FHIR `Communication` and `Task` to the database/cache via `taskService.create(task)`, then immediately calls `taskEventProducerService.sendTaskEvent(event)` to publish to the Artemis queue. If the Artemis publish fails, the exception is caught and logged (`log.warn(...)`), but processing continues and an HL7 `AA` ACK is returned to the sending client.
* **Risk**:
  The sending system receives an `AA` ACK confirming receipt, but no `ErgonEvent` exists in the Petasos queue to drive downstream Ponos workflow execution. The task remains in `REQUESTED` status indefinitely in the database without being processed or dispatched to downstream systems.
* **Recommended Behavior**:
  1. *Immediate Option*: In the event of an Artemis publish failure, throw an exception that triggers the generation of an HL7 `AE` (Application Error) NACK back to the sender, forcing the source system to retry.
  2. *Architectural Option*: Implement a Transactional Outbox pattern where both the FHIR `Task` and an Outbox event record are committed in a single local database transaction. A background outbox poller or Change Data Capture (CDC) stream publishes events to Artemis.

---

### 2.2 REC-002: Destination-Level Fan-Out Completion State Not Persisted Individually

* **ID**: `REC-002`
* **Area**: Workflow Execution & Outbound Distribution State
* **Affected Component**: `energeia/erga` (`AdtDistributionErgon`), `pylai/pylai-mllp-out` (`OutboundStateLifecycleManager`)
* **Priority**: `MEDIUM`
* **Current Behavior**:
  When `AdtDistributionErgon` performs ADT fan-out, it publishes discrete messages to individual destination queues (`emr_adt`, `lms_adt`, `ris_adt`). However, the parent FHIR `Task` in database/cache tracks the macro workflow status rather than an explicit granular matrix of destination-level delivery states (e.g., `EMR: COMPLETED`, `LMS: COMPLETED`, `RIS: FAILED`).
* **Risk**:
  If an operator inspects the parent `Task` resource in Mnemosyne while RIS-PAC is down, the overall task status does not immediately convey which specific fan-out destinations succeeded and which failed without querying individual `Provenance` records.
* **Recommended Behavior**:
  Extend `Pragma` / FHIR `Task` with explicit destination-level checkpoint sub-states (e.g. `Task.output` or extensions recording `destinationId`, `status`, `deliveredTimestamp`, `ackCode`) updated by `OutboundStateLifecycleManager` upon each destination ACK.

---

### 2.3 REC-003: Ponos Worker Processing Ownership Lacks Distributed Lease / Heartbeat

* **ID**: `REC-003`
* **Area**: Distributed Workflow Coordination & Worker Failover
* **Affected Component**: `energeia/ponos`, `energeia/erga` (`ErgonBase`, `TaskProcessorRouteBuilder`)
* **Priority**: `MEDIUM`
* **Current Behavior**:
  Ponos relies solely on Artemis unacknowledged message consumer session timeouts for worker crash detection. If a worker node hangs or experiences a long GC pause while processing a `Pragma`, no distributed lease or heartbeat lock exists on the cached `Task` entity in Infinispan.
* **Risk**:
  If a worker pauses longer than the message acknowledgment timeout and another worker picks up the redistributed message, two workers could execute activities against the same task concurrently.
* **Recommended Behavior**:
  Implement a distributed lease or optimistic lock check with lease timeouts on active `Pragma` instances in the Infinispan cache grid, ensuring an abandoned or hanging worker is safely fenced off.

---

### 2.4 REC-004: Absence of Automated Startup Recovery Scan for Stalled In-Memory Tasks

* **ID**: `REC-004`
* **Area**: System Startup & Cold Restart Recovery
* **Affected Component**: `energeia/ponos` (`TaskSequenceLoader`, `TaskProcessorRouteBuilder`)
* **Priority**: `LOW-MEDIUM`
* **Current Behavior**:
  Upon a cold restart of Harmonia, Ponos initializes its routes and consumes pending messages from the persistent Artemis journal. However, it does not perform an active reconciliation scan against `hie_fhir_resources` or `hie_operations_resources` to discover any orphaned tasks stuck in `REQUESTED` or `IN_PROGRESS` (such as those caused by REC-001 dual-write windows).
* **Recommended Behavior**:
  Implement an optional startup recovery scan (`PonosStartupRecoveryScanner`) that queries for non-terminal `Task` resources older than a configurable threshold (e.g., 5 minutes) and re-publishes synthetic reconciliation `TaskEvent`s to the appropriate Petasos queue.

---

## 3. Gap Summary & Remediation Roadmap

| Gap ID | Area | Current State | Risk Level | Target Release | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **REC-001** | Dual-Write Ingress Window | Propagates exception, returns HL7 `AE` NACK on publish failure | Resolved | Phase 2 | `REMEDIATED` |
| **REC-002** | Destination Fan-Out Matrix | `Task.output` and `PragmaCheckpoint` track granular destination delivery status | Resolved | Phase 2 | `REMEDIATED` |
| **REC-003** | Distributed Worker Lease | Dependent solely on JMS consumer timeout | **MEDIUM** | Phase 3 | `DESIGNED/PLANNED` |
| **REC-004** | Startup Recovery Scan | Artemis persistent journal replay active; scheduled DB scanner roadmap | **LOW-MEDIUM** | Phase 3 | `DESIGNED/PLANNED` |
