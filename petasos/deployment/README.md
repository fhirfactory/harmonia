# Petasos Local Deployment

This directory contains the Docker Compose environment and configuration files for the **Petasos Reference HA Topology**.

## Topology Architecture

The environment launches 4 Artemis broker instances:

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

* **Primary A & Primary B**: Simultaneously active nodes participating in the `petasos-cluster` with `ON_DEMAND` server-side message distribution and instant redistribution (`redistribution-delay = 0`).
* **Backup A**: Passive replicating replica protecting Primary A (`group-a`).
* **Backup B**: Passive replicating replica protecting Primary B (`group-b`).
* **Storage**: Each broker maintains its own persistent file journal, bindings, paging, and large message directories on dedicated Docker volumes.

## Port Allocations

| Broker Instance | Role | Cluster Group | Protocol Port (Host) | Web Console Port (Host) | Container Name |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **artemis-primary-a** | Live Primary | `group-a` | `61616` | `8161` | `petasos-artemis-primary-a` |
| **artemis-backup-a** | Backup Replica | `group-a` | `61617` | `8162` | `petasos-artemis-backup-a` |
| **artemis-primary-b** | Live Primary | `group-b` | `61618` | `8163` | `petasos-artemis-primary-b` |
| **artemis-backup-b** | Backup Replica | `group-b` | `61619` | `8164` | `petasos-artemis-backup-b` |

## Quickstart

Start the entire 4-broker cluster with a single command:

```bash
docker compose up -d
```

View broker container status and health:

```bash
docker compose ps
```

Stop the cluster:

```bash
docker compose down
```
