# Petasos Failure Scenarios & Recovery Procedures

This document details failure behaviors, verification steps, and recovery expectations across the Petasos messaging architecture.

---

### Failure Matrix

| Scenario | Trigger | Expected Subsystem Behavior | Recovery Verification |
| :--- | :--- | :--- | :--- |
| **1. Primary Broker Failure** | Primary A is terminated (`kill -9` / process abort) | Backup A becomes active in <2s; clients fail over; queued messages preserved. | Producers resume sending; consumers receive in-flight messages without loss. |
| **2. Primary Broker Restart** | Primary A restarts after failover | Primary A connects to Backup A, synchronizes journal changes, and initiates failback. | Primary A resumes live ownership; Backup A returns to passive replication mode. |
| **3. Consumer Failure** | Consumer disconnects with unacknowledged messages | Broker redelivers messages to other consumers on the same node or forwards to other cluster nodes (`redistribution-delay = 0`). | Remaining consumers receive and process queued messages. |
| **4. Client Reconnect** | Temporary network partition | Client buffers requests up to call timeout and retries indefinitely (`reconnectAttempts = -1`). | Connection restored automatically; `reconnectCount` metric incremented. |
| **5. Complete Broker-Pair Failure** | Both Primary A and Backup A fail simultaneously | Group A messages remain queued in disk volumes until at least one node is restored; clients route new requests to Group B. | When Primary A or Backup A is restored, journal replay recovers all durable messages. |
| **6. Poison Pill / Processing Failure** | Message processing throws repeated exceptions | Message is retried up to `max-delivery-attempts` (3), then routed to `DLQ`. | DLQ consumer inspects corrupt payload; main queue resumes unobstructed. |
| **7. Expired Message** | Message TTL expires before consumption | Artemis expiry scanner removes message from main queue and routes to `ExpiryQueue`. | `ExpiryQueue` contains dead event; normal queue contains 0 expired messages. |

---

### Step-by-Step Failure & Recovery Procedures

#### Scenario 1: Primary Broker Failure & Failover

1. **Active State**: Primary A and Backup A are running. Messages are continuously published by Petasos producers and processed by consumers.
2. **Failure Injection**: Terminate `artemis-primary-a`:
   ```bash
   docker stop petasos-artemis-primary-a
   ```
3. **Behavior Verification**:
   * Artemis logs on Backup A report `AMQ221037: Backup server is now active`.
   * Petasos client detects failover event:
     ```
     [main] INFO n.f.h.p.a.c.ArtemisConnectionManager - Petasos HA failover event received: FAILURE_DETECTED
     [main] INFO n.f.h.p.a.c.ArtemisConnectionManager - Petasos client successfully failed over and reconnected to backup broker. Total reconnects: 1
     ```
   * Message consumption and production continue seamlessly.
   * Total acknowledged messages remain 100% accounted for.

#### Scenario 2: Primary Restart & Rejoining Topology

1. **Action**: Restart Primary A:
   ```bash
   docker start petasos-artemis-primary-a
   ```
2. **Behavior Verification**:
   * Primary A contacts active Backup A and resynchronizes journal records.
   * Primary A resumes live operation (`allow-failback = true`).
   * Backup A transitions back to passive replica state.
   * No split-brain or duplicate active ownership occurs.

#### Scenario 3: Consumer Failure & Message Redistribution

1. **Action**: Stop a consumer on Primary A while 20 messages are queued.
2. **Behavior Verification**:
   * Artemis detects zero active consumers on Primary A for the address.
   * Because `redistribution-delay = 0` is configured in `address-settings`, the cluster bridge immediately forwards messages to active consumers on Primary B.
   * Consumers on Primary B process all remaining messages.

#### Scenario 4: Poison Pill / Dead-Letter Routing

1. **Action**: Produce a message that throws processing errors on consumer handling.
2. **Behavior Verification**:
   * After 3 delivery attempts, message is routed to `DLQ`.
   * Redelivery metrics incremented.
   * Subsequent valid messages on the main queue are not blocked.
