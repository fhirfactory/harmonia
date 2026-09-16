# Harmonia Message Lifecycle & Transaction Boundaries

## 1. Overview

This document provides end-to-end tracing, sequence diagrams, transaction boundary definitions, and dual-write failure analyses for the three primary clinical integration flows within Harmonia:
1. **PAS ADT Distribution Flow**: Inbound HL7 ADT fan-out to EMR, LMS, and RIS-PAC.
2. **EMR ORM Routing Flow**: Inbound HL7 ORM order routing to LMS (Lab) or RIS-PAC (Radiology) based on OBR-4 Universal Service Identifier.
3. **LMS / RIS-PAC ORU Processing Flow**: Inbound HL7 ORU clinical observation result ingestion, FHIR transformation, and durable persistence.

---

## 2. End-to-End Clinical Message Flows

### 2.1 ADT Multi-Destination Fan-Out Flow (PAS -> EMR / LMS / RIS-PAC)

```mermaid
sequenceDiagram
    autonumber
    actor PAS as Patient Admin System (PAS)
    participant PylaiIn as Pylai Inbound MLLP Gateway
    participant Mneme as Mneme Cache / Mnemosyne DB
    participant PetasosIn as Petasos Inbound Queue (task-sequence-processor)
    participant Ponos as Ponos Pipeline Engine
    participant Erga as AdtDistributionErgon
    participant PetasosOut as Petasos Outbound Queues (emr_adt, lms_adt, ris_adt)
    participant PylaiOut as Pylai Outbound MLLP Gateway
    actor EMR as Electronic Medical Record (EMR)
    actor LMS as Lab Management System (LMS)
    actor RIS as Radiology System (RIS-PAC)

    PAS->>PylaiIn: HL7 v2.4 ADT^A01 (MLLP Frame)
    activate PylaiIn
    Note over PylaiIn: 1. Parse HL7 & Extract MSH-10, PID, PV1
    PylaiIn->>Mneme: Persist FHIR Communication & Task (REQUESTED)
    PylaiIn->>PetasosIn: Publish ErgonEvent (Action: PROCESS, Queue: task-sequence-processor)
    Note over PylaiIn: [Point of No Data Loss]
    PylaiIn-->>PAS: HL7 ACK^A01 (AA Accept)
    deactivate PylaiIn

    PetasosIn->>Ponos: Consume ErgonEvent
    activate Ponos
    Ponos->>Mneme: Update Task status to IN_PROGRESS (Add Checkpoint)
    Ponos->>Erga: Execute Activity (AdtDistributionErgon)
    activate Erga
    Note over Erga: 2. Fan-out to 3 target destinations
    Erga->>PetasosOut: Enqueue to petasos.queue.mllp.outbound.emr_adt
    Erga->>PetasosOut: Enqueue to petasos.queue.mllp.outbound.lms_adt
    Erga->>PetasosOut: Enqueue to petasos.queue.mllp.outbound.ris_adt
    deactivate Erga
    Ponos->>PetasosIn: ACK ErgonEvent (Artemis Journal commit)
    deactivate Ponos

    par Outbound Delivery to EMR
        PetasosOut->>PylaiOut: Consume from emr_adt
        PylaiOut->>EMR: MLLP HL7 ADT^A01
        EMR-->>PylaiOut: HL7 ACK (AA)
        PylaiOut->>Mneme: Record Provenance & Update Task
    and Outbound Delivery to LMS
        PetasosOut->>PylaiOut: Consume from lms_adt
        PylaiOut->>LMS: MLLP HL7 ADT^A01
        LMS-->>PylaiOut: HL7 ACK (AA)
        PylaiOut->>Mneme: Record Provenance & Update Task
    and Outbound Delivery to RIS-PAC
        PetasosOut->>PylaiOut: Consume from ris_adt
        PylaiOut->>RIS: MLLP HL7 ADT^A01
        RIS-->>PylaiOut: HL7 ACK (AA)
        PylaiOut->>Mneme: Record Provenance & Update Task (COMPLETED)
    end
```

---

### 2.2 ORM Deterministic Order Routing Flow (EMR -> LMS / RIS-PAC)

```mermaid
sequenceDiagram
    autonumber
    actor EMR as EMR Clinical System
    participant PylaiIn as Pylai Inbound MLLP Gateway
    participant Mneme as Mneme Cache / Mnemosyne DB
    participant PetasosIn as Petasos Inbound Queue
    participant Ponos as Ponos Pipeline Engine
    participant Erga as OrmRoutingErgon
    participant PetasosOut as Petasos Outbound Queue (lms_orm or ris_orm)
    participant PylaiOut as Pylai Outbound MLLP Gateway
    actor Dest as LMS or RIS-PAC

    EMR->>PylaiIn: HL7 v2.4 ORM^O01 (Placer Order)
    activate PylaiIn
    PylaiIn->>Mneme: Persist FHIR Communication & Task
    PylaiIn->>PetasosIn: Publish ErgonEvent (Action: PROCESS)
    PylaiIn-->>EMR: HL7 ACK^O01 (AA Accept)
    deactivate PylaiIn

    PetasosIn->>Ponos: Consume ErgonEvent
    activate Ponos
    Ponos->>Erga: Execute OrmRoutingErgon
    activate Erga
    Note over Erga: Inspect OBR-4 Universal Service Identifier:<br/>- LAB / CBC / LFT -> Queue: lms_orm<br/>- RAD / CHEST / XR / CT -> Queue: ris_orm
    Erga->>PetasosOut: Enqueue routed ORM message
    deactivate Erga
    Ponos->>PetasosIn: ACK ErgonEvent
    deactivate Ponos

    PetasosOut->>PylaiOut: Consume routed ORM message
    PylaiOut->>Dest: MLLP HL7 ORM^O01
    Dest-->>PylaiOut: HL7 ACK (AA)
    PylaiOut->>Mneme: Record Provenance & Complete Task
```

---

### 2.3 ORU Observation Result Ingestion Flow (LMS / RIS-PAC -> Harmonia Store)

```mermaid
sequenceDiagram
    autonumber
    actor Source as LMS / RIS-PAC System
    participant PylaiIn as Pylai Inbound MLLP Gateway
    participant Mneme as Mneme Cache / Mnemosyne DB
    participant PetasosIn as Petasos Inbound Queue
    participant Ponos as Ponos Pipeline Engine
    participant Erga as OruProcessingErgon
    participant DB as PostgreSQL (hie_fhir_resources)

    Source->>PylaiIn: HL7 v2.4 ORU^R01 (Filler Result + OBX Segments)
    activate PylaiIn
    PylaiIn->>Mneme: Persist FHIR Communication & Task (COMPLETED)
    PylaiIn->>PetasosIn: Publish ErgonEvent
    PylaiIn-->>Source: HL7 ACK^R01 (AA Accept)
    deactivate PylaiIn

    PetasosIn->>Ponos: Consume ErgonEvent
    activate Ponos
    Ponos->>Erga: Execute OruProcessingErgon
    activate Erga
    Note over Erga: Extract OBR/OBX segments<br/>Transform to FHIR DiagnosticReport & Observation
    Erga->>DB: INSERT / UPDATE DiagnosticReport & Observations
    deactivate Erga
    Ponos->>PetasosIn: ACK ErgonEvent
    deactivate Ponos
```

---

## 3. Persistence Timeline & Point of No Data Loss

```mermaid
sequenceDiagram
    autonumber
    participant Source as MLLP Client (PAS)
    participant Pylai as Pylai Inbound
    participant CacheDB as Cache & DB Tier
    participant Artemis as Petasos Artemis Journal

    Source->>Pylai: 1. Send HL7 message over TCP socket
    Note over Pylai: [Vulnerable Window 1]<br/>Process crash here = TCP disconnect,<br/>Source will reconnect and retry. No data loss.
    
    Pylai->>CacheDB: 2. Save Communication & Task
    Note over Pylai,CacheDB: [Local ACID / Cache Write]<br/>Resource version 1 created.
    
    Pylai->>Artemis: 3. sendTaskEvent(ErgonEvent)
    activate Artemis
    Note over Artemis: [Fsync to persistent journal]<br/>Message assigned durable sequence.
    Artemis-->>Pylai: 4. Producer confirmation
    deactivate Artemis
    
    Note over Pylai: ★ POINT OF NO DATA LOSS REACHED ★<br/>Message is committed to both storage & journal.
    
    Pylai-->>Source: 5. Return HL7 AA ACK frame
    Note over Source: Source marks message as acknowledged.
```

---

## 4. Transaction Boundaries

| Stage | Operation | Component | Transaction Type | Commit / ACK Point | Rollback Action |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Inbound Stage 1** | HL7 Message Parse & Validation | `IncomingAdtMessageProcessor` | None (In-memory) | N/A | Return HL7 `AR` (Reject) |
| **Inbound Stage 2** | Resource Creation | `FhirStorageService` / `TaskCacheService` | Spring `@Transactional` (PostgreSQL) / HotRod Put | DB Commit upon method exit | Return HL7 `AE` (Error) |
| **Inbound Stage 3** | ErgonEvent Enqueue | `TaskEventProducerService` | Artemis JMS Producer Session | Broker Journal Write | Log Warning (See Dual-Write Analysis) |
| **Inbound Stage 4** | Protocol Acceptance | `IncomingAdtMessageRouteBuilder` | MLLP Socket Protocol | Send HL7 `AA` ACK Frame | Client TCP Retries |
| **Execution Stage 1** | Consume TaskEvent | `TaskProcessorRouteBuilder` | Camel / JMS Client ACK Mode | Received into memory | Redelivered after unacked timeout |
| **Execution Stage 2** | Task Mutation & Routing | `ErgonBase` (`AdtDistributionErgon`) | Cache Mutation & JMS Outbound Produces | Multiple JMS queue puts | Artemis redelivery if worker crashes |
| **Execution Stage 3** | Completion ACK | `PetasosQueueToExchangeConduit` | JMS Message ACK | Artemis Journal records consumption | Unacked message redelivered |
| **Outbound Stage 1** | Outbound Dispatch | `OutboundMllpProcessor` | MLLP Socket Transaction | Await Remote HL7 `AA` ACK | Camel retry policy (backoff + DLQ) |
| **Outbound Stage 2** | Provenance Record | `OutboundStateLifecycleManager` | JPA Transaction | DB Commit | Log warning |

---

## 5. Dual-Write Failure Analysis

In the inbound gateway (`IncomingAdtMessageProcessor`, `IncomingOrmMessageProcessor`, `IncomingOruMessageProcessor`), two distinct external systems are updated sequentially without a distributed two-phase commit (2PC / XA):

```
       Step 1: Write DB / Cache        Step 2: Publish Artemis Queue
      ┌─────────────────────────┐     ┌─────────────────────────────┐
      │   Task / Communication  │     │       ErgonEvent to         │
      │   saved in PostgreSQL   │────►│   task-sequence-processor   │
      └─────────────────────────┘     └─────────────────────────────┘
```

### 5.1 Failure Window 1: DB Commit Succeeds, Artemis Publish Fails
* **Scenario**: The FHIR `Task` and `Communication` are successfully committed to PostgreSQL / Infinispan, but the network connection to Apache ActiveMQ Artemis times out or throws an exception during `taskEventProducerService.sendTaskEvent(event)`.
* **Observed Implementation Behavior**: The exception is caught and logged (`log.warn("Could not dispatch TaskEvent...")`), and an HL7 `AA` ACK is returned to the sending system.
* **Risk**: The clinical source believes Harmonia has accepted the message, but no event exists in the Artemis queue to drive Ponos workflow execution. The task remains in `REQUESTED` status indefinitely unless recovered.
* **Remediation / Gap Reference**: Documented as **REC-001 (Dual-Write Outbox Gap)** in `docs/persistence-recovery-gaps.md`. Requires implementing a transactional outbox table or failing the inbound HL7 transaction with an `AE` NACK so the sender retries.

### 5.2 Failure Window 2: Outbound MLLP Delivery Succeeds, Completion Event Fails
* **Scenario**: `OutboundMllpProcessor` successfully sends an ADT message to EMR and receives an `AA` ACK. In `OutboundStateLifecycleManager`, recording `Provenance` or dispatching the completion event to Petasos throws an exception.
* **Observed Implementation Behavior**: The exception is caught and logged; the remote system has received the clinical data, but the local Task execution log may not reflect the completed state.
* **Mitigation**: The consumer queue message is acknowledged after dispatch, preventing duplicate message transmission to the destination.
