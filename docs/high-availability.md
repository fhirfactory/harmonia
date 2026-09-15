# Petasos High Availability & Clustering Specification

This document details the configuration, replication mechanics, and client connection management underpinning Petasos's High Availability (HA) messaging architecture.

---

### Reference HA Cluster Topology

The standard reference deployment topology consists of 4 Apache ActiveMQ Artemis nodes organized into 2 replication groups connected via server-side clustering:

```
                            +-----------------------------------+
                            |        Petasos Client API         |
                            +-----------------+-----------------+
                                              |
                    +-------------------------+-------------------------+
                    |                                                   |
                    v                                                   v
          +-----------------------+                           +-----------------------+
          |   Artemis Primary A   | <======= cluster =======> |   Artemis Primary B   |
          |   (Port 61616, 8161)  |         ON_DEMAND         |   (Port 61618, 8163)  |
          +-----------+-----------+                           +-----------+-----------+
                      |                                                   |
                 replication                                         replication
                      |                                                   |
                      v                                                   v
          +-----------------------+                           +-----------------------+
          |   Artemis Backup A    |                           |   Artemis Backup B    |
          |   (Port 61617, 8162)  |                           |   (Port 61619, 8164)  |
          +-----------------------+                           +-----------------------+
```

---

### Broker Roles & Grouping

| Instance | Role | Cluster Group | Protocol Port (Host) | Web Console (Host) | HA Policy |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `artemis-primary-a` | Live Primary | `group-a` | `61616` | `8161` | Replication Primary (`check-for-live-server: true`) |
| `artemis-backup-a` | Replicating Backup | `group-a` | `61617` | `8162` | Replication Backup (`allow-failback: true`) |
| `artemis-primary-b` | Live Primary | `group-b` | `61618` | `8163` | Replication Primary (`check-for-live-server: true`) |
| `artemis-backup-b` | Replicating Backup | `group-b` | `61619` | `8164` | Replication Backup (`allow-failback: true`) |

---

### Server-Side Clustering (`ON_DEMAND`)

Clustering between `Primary A` and `Primary B` provides server-side load distribution:

1. **Load Balancing Policy**: `ON_DEMAND`
   * Messages published to `Primary A` are routed to local consumers if present.
   * If consumers are connected to `Primary B` and not `Primary A`, messages are forwarded over the cluster connection to `Primary B`.
2. **Redistribution Delay**: `0`
   * When all consumers on a node disconnect, queued messages are immediately redistributed to alternative nodes with active consumers, avoiding message starvation.
3. **Duplicate Detection across Cluster**: `use-duplicate-detection = true`
   * Cluster bridges automatically attach duplicate detection headers to prevent duplicates during inter-broker routing.

---

### Journal Replication Mechanics

Replication operates at the NIO file journal level:

1. **Replication Stream**: The Primary asynchronously sends journal update packets over TCP to the passive Backup node.
2. **Synchronized State**: The Backup writes received journal records to its own independent file storage.
3. **Activation Trigger**: If the Primary process dies or loses network connectivity, the Backup detects the connection loss and initiates live server activation.
4. **Failback**: When the original Primary recovers, it connects to the active Backup, resynchronizes any changes occurred during the outage, and gracefully resumes the Primary role (`allow-failback = true`).

---

### Client Reconnection & Failover Configuration

The Petasos client connects using a multi-node HA bootstrap URL:

```
(tcp://artemis-primary-a:61616,tcp://artemis-backup-a:61617,tcp://artemis-primary-b:61618,tcp://artemis-backup-b:61619)?ha=true&reconnectAttempts=-1&retryInterval=500&maxRetryInterval=2000&retryIntervalMultiplier=1.5&connectionTTL=60000&clientFailureCheckPeriod=10000
```

* `ha=true`: Enables client-side HA topology discovery. The client receives live cluster updates from the broker.
* `reconnectAttempts=-1`: Indefinite reconnect attempts during transient network outages.
* `retryInterval=500` & `maxRetryInterval=2000`: Exponential backoff with jitter to protect brokers during cluster restarts.
* `connectionTTL=60000` & `clientFailureCheckPeriod=10000`: Heartbeat intervals detecting broker unreachability within 10 seconds.
* `failoverListener`: Petasos monitors failover events (`FAILURE_DETECTED`, `FAILOVER_COMPLETED`, `FAILOVER_FAILED`), updating `PetasosHealth` and incrementing reconnect metrics automatically.
