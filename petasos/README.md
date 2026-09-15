# Harmonia :: Petasos

**Petasos** (πέτασος) is the high-availability messaging and event distribution framework for the **Harmonia** Health Integration Environment (HIE), powered by **Apache ActiveMQ Artemis**.

## Features

* **Abstraction Layer**: Clean, framework-independent Java messaging API (`Petasos`, `PetasosProducer`, `PetasosConsumer`, `PetasosMessage`, `PetasosDestination`).
* **High Availability**: Multi-broker ActiveMQ Artemis clustering with real-time journal replication (`Primary A` <-> `Backup A`, `Primary B` <-> `Backup B`).
* **Server-Side Load Balancing**: Clustered `ON_DEMAND` distribution across active nodes with dynamic consumer message redistribution (`redistribution-delay = 0`).
* **Healthcare Envelope**: Strongly-typed metadata envelope tracking `messageId`, `correlationId`, `causationId`, `messageType`, schemas, and timestamps.
* **Deduplication**: Multi-layer deduplication via Artemis broker `_AMQ_DUPL_ID` caching and client-side sliding window detectors.
* **Resilience**: Automatic client failover and reconnect policies (`reconnectAttempts = -1`), dead-letter queue (`DLQ`), and message expiry routing (`ExpiryQueue`).
* **Observability & Health**: Live health checks (`PetasosHealth`), cluster topology inspection (`PetasosBrokerTopology`), and operational metrics (`PetasosMetrics`).
* **PHI Privacy**: Payload contents are never written to logs by default.

## Module Structure

* **`petasos-api`**: High-level domain interfaces and message envelopes decoupled from Artemis/JMS dependencies.
* **`petasos-core`**: Serialization, metrics collection, deduplication sliding cache, and configuration resolution.
* **`petasos-artemis`**: Artemis adapter implementing connection lifecycle, failover detection, and message conversion.
* **`petasos-test`**: Automated integration test harness (`EmbeddedArtemisCluster`) and HA test suites.
* **`deployment`**: Docker Compose 4-node reference cluster and Artemis configuration profiles.
* **`docs`**: In-depth architectural, HA, and failure scenario documentation.

## Quick Usage

```java
// 1. Initialize Petasos client (from environment or bootstrap URL)
Petasos petasos = ArtemisPetasos.create("tcp://127.0.0.1:61616");

// 2. Publish durable message
PetasosDestination queue = PetasosDestination.queue("clinical.tasks.inbound");
PetasosMessage message = PetasosMessage.builder()
        .messageType("PatientAdmitEvent")
        .source("pylai-mllp-gateway")
        .destination(queue)
        .contentType("application/fhir+json")
        .payload("{\"resourceType\":\"Patient\",\"id\":\"pat-001\"}")
        .durable(true)
        .build();

petasos.send(queue, message);

// 3. Asynchronously consume messages
PetasosSubscription subscription = petasos.receive(queue, (msg, context) -> {
    System.out.println("Received: " + msg.getMessageId() + " type: " + msg.getMessageType());
    context.acknowledge();
});

// 4. Query health and topology
PetasosHealth health = petasos.health();
System.out.println("Petasos Health: " + health.getStatus() + " (" + health.getConnectionState() + ")");
```

## Documentation

* [Petasos Architecture](docs/architecture.md)
* [High Availability & Clustering](docs/high-availability.md)
* [Failure Scenarios & Recovery](docs/failure-scenarios.md)
* [Local Deployment Guide](deployment/README.md)
