# Health Information Exchange (HIE) 5-Tier FHIR Platform

A modular, scalable, enterprise-grade Health Information Exchange (HIE) platform targeting HL7 FHIR Release 5 (R5). The platform provides high-throughput in-memory cached access, asynchronous write-behind persistence, and a modern TypeScript/Vue single-page web interface.

---

## Architecture Overview

The system is organized into a 5-tier architecture across 5 coordinated submodules:

```mermaid
graph TD
  subgraph User Interface
    UI[1. UI: TypeScript / Vue 3 SPA - Port 3000]
  end

  subgraph Backend Gateway
    BEFE[2. BEFE: WildFly Jakarta EE 10 Service - Port 8080]
  end

  subgraph In-Memory Data Grid
    CacheCluster[3. Infinispan Cluster: Replicated Nodes - Ports 11222 / 11223]
    PersistenceSPI[4. Persistence Tier: FHIR REST CacheStore SPI]
    CacheCluster -->|Write-Behind / Load| PersistenceSPI
  end

  subgraph Relational Disk Persistence
    HapiCluster[5. Replicated HAPI FHIR JPA Server Nodes - Ports 8081 / 8082]
    DB[(PostgreSQL Database - Port 5432)]
    HapiCluster -->|JPA / JDBC| DB
  end

  UI -->|REST / JSON| BEFE
  BEFE -->|Hot Rod Protocol| CacheCluster
  PersistenceSPI -->|FHIR R5 REST API| HapiCluster
```

### Supported FHIR R5 Resources
The platform delivers full CRUD and search lifecycle support for 14 core FHIR R5 resources:
1. `Person`
2. `RelatedPerson`
3. `Practitioner`
4. `PractitionerRole`
5. `Organization`
6. `Location`
7. `HealthcareService`
8. `Group`
9. `Provenance`
10. `AuditEvent`
11. `Consent`
12. `Task`
13. `Communication`
14. `DocumentReference`

---

## Submodules

The project is structured into 4 domain-driven service groups containing 7 specialized modules:

- **`hie-common-models`**: Shared domain models, DTOs, event definitions (`TaskEvent`), and enumerations (`HieTaskReason`) with FHIR R5 `CodeableConcept` and `CodeableReference` mappings for cross-module reuse.
- **`presentation-services`**:
  - **`befe`**: WildFly Jakarta EE 10 Backend-For-Frontend providing dual-port separated RESTful interfaces for clinical FHIR resources on port 8080 (`/api/fhir/{resourceType}`) and operations/task-sequences on port 8090 (`/api/operations/{resourceType}`) connected via Infinispan Hot Rod client.
  - **`fhir-resource-ui`**: Single Page Application built with Vue 3, Vite, TypeScript, and Pinia dedicated to FHIR R5 resource browsing and CRUD exploration.
  - **`operations-ui`**: Single Page Application built with Vue 3, Vite, TypeScript, and Pinia dedicated to system topology monitoring, ActiveMQ Artemis messaging queues, distributed cache grid, and TaskSequence workflow management.
- **`data-services`**:
  - **`hapi-fhir-jpa-server`**: Spring Boot HAPI FHIR JPA server (FHIR R5) managing relational disk persistence via PostgreSQL/H2 with custom resource providers.
  - **`hie-operations-jpa-server`**: Spring Boot Operations JPA server managing relational disk persistence for non-FHIR operational data, task sequences, and workflow definitions via PostgreSQL/H2.
  - **`hie-operations-cli`**: Command-line tool for interacting with the Operations JPA Server, inspecting operational resources, and listing/managing all TaskSequence definitions.
  - **`infinispan-cluster`**: Clustered High-Availability Infinispan Data Grid with custom FHIR and Operations SPI Store configuration.
  - **`infinispan-persistence`**: Custom Infinispan `NonBlockingStore` SPI (`FhirRestCacheStore` and `OperationsRestCacheStore`) performing asynchronous write-behind queuing and read-through loading against HAPI FHIR and Operations JPA REST endpoints.
- **`interfacing-services`**:
  - **`mllp-gateway-base`**: Core integration library providing Infinispan cache services (Communication, Task, Provenance), ActiveMQ Artemis JMS event production, and FHIR resource management.
  - **`mllp-gateway-in`**: Jakarta EE 10 MLLP inbound interface with Apache Camel for HL7 v2.4 ADT/ORU/ORM trigger event ingestion, Topic data type resolution, transformation to FHIR resources, and task generation.
  - **`mllp-gateway-cli`**: Command-line tool for generating and sending HL7 v2.4 MLLP messages and ADT trigger events (A01-A40) to the MLLP Gateway with parameter customization (MRN, names, DOB, gender).
- **`workflow-services`**:
  - **`task-processors`**: Shared activity library and base Apache Camel Route abstractions (`TaskProcessingActivity`) for task execution.
  - **`task-sequence-processor`**: Jakarta EE 10 asynchronous workflow processing engine consuming tasks and task events via Artemis message queues.
  - **`hie-workflow-cli`**: Command-line interface for connecting to `task-sequence-processor` to trigger live reload and synchronization of message queues and task sequences, query runtime processor status, and validate configurations against the runtime environment.

---

## Prerequisites

Ensure the following tools are installed on your host system:
- **Java Development Kit (JDK)**: Version 21+
- **Apache Maven**: Version 3.9+
- **Node.js & npm**: Node v20+ / npm 10+ (managed automatically by `frontend-maven-plugin` during Maven builds)
- **Docker & Docker Compose**: Docker Engine 24+ and Docker Compose v2+

---

## Building the Project

Compile all submodules, run unit/integration test suites, and package the artifacts:

```bash
# Build and run all automated tests across all 5 submodules
mvn clean test

# Build and package all JARs, WARs, and frontend bundles without tests
mvn clean package -DskipTests
```

---

## Executing the Platform via Docker Compose

The fastest and most reliable way to run the entire 5-tier environment is using Docker Compose.

### 1. Launch All Services

```bash
docker compose up --build -d
```

### 2. Verify Service Status

```bash
docker compose ps
```

All 13 services (across 14 containers) should show `Up` (and `healthy` where applicable):
- `hie-postgres-1`
- `hie-postgres-2`
- `hie-postgres-ops-1`
- `hie-postgres-ops-2`
- `hie-hapi-fhir-1`
- `hie-hapi-fhir-2`
- `hie-operations-1`
- `hie-operations-2`
- `hie-infinispan-node1`
- `hie-infinispan-node2`
- `hie-befe`
- `hie-fhir-resource-ui`
- `hie-operations-ui`
- `hie-mllp-gateway`
- `hie-task-processor`

### 3. Service Endpoints and Port Mappings

| Service / Subproject | Host Port | Internal Port | Description & URL |
| :--- | :--- | :--- | :--- |
| **FHIR Resource UI** | `3000` | `80` | [http://localhost:3000](http://localhost:3000) (FHIR R5 Resource Explorer) |
| **Operations UI** | `3001` | `80` | [http://localhost:3001](http://localhost:3001) (Topology, Queues & Task Sequences) |
| **BEFE FHIR Gateway** | `8080` | `8080` | [http://localhost:8080/api/fhir](http://localhost:8080/api/fhir) (RESTful Clinical FHIR Endpoints) |
| **BEFE Operations Gateway** | `8090` | `8090` | [http://localhost:8090/api/operations](http://localhost:8090/api/operations) (Operations & Task Sequence Telemetry) |
| **WildFly Admin Console** | `9990` | `9990` | [http://localhost:9990](http://localhost:9990) |
| **HAPI FHIR JPA Node 1** | `8081` | `8080` | [http://localhost:8081/fhir](http://localhost:8081/fhir) (FHIR R5 REST Server) |
| **HAPI FHIR JPA Node 2** | `8082` | `8080` | [http://localhost:8082/fhir](http://localhost:8082/fhir) (Replicated JPA Node) |
| **Operations JPA Node 1** | `8085` | `8080` | [http://localhost:8085/api/operations](http://localhost:8085/api/operations) (Non-FHIR & TaskSequence Persistence) |
| **Operations JPA Node 2** | `8086` | `8080` | [http://localhost:8086/api/operations](http://localhost:8086/api/operations) (Replicated Operations Node) |
| **Infinispan Cluster Node 1** | `11222`, `7800` | `11222`, `7800` | Hot Rod & JGroups Discovery |
| **Infinispan Cluster Node 2** | `11223`, `7801` | `11222`, `7800` | Hot Rod & JGroups Discovery |
| **MLLP Gateway** | `2575`, `8084` | `2575`, `8080` | HL7 v2.4 MLLP Interface & REST endpoints |
| **Task Processor** | `8083`, `61616` | `8080`, `61616` | Workflow Task Processing Engine & Artemis Broker |
| **PostgreSQL FHIR Database Node 1** | `5432` | `5432` | `jdbc:postgresql://localhost:5432/fhir_node_1` (user: `fhir_user`, pass: `fhir_password`) |
| **PostgreSQL FHIR Database Node 2** | `5433` | `5432` | `jdbc:postgresql://localhost:5433/fhir_node_2` (user: `fhir_user`, pass: `fhir_password`) |
| **PostgreSQL Operations DB Node 1** | `5434` | `5432` | `jdbc:postgresql://localhost:5434/ops_node_1` (user: `ops_user`, pass: `ops_password`) |
| **PostgreSQL Operations DB Node 2** | `5435` | `5432` | `jdbc:postgresql://localhost:5435/ops_node_2` (user: `ops_user`, pass: `ops_password`) |

### 4. Stopping and Cleaning Up

```bash
# Stop all services
docker compose down

# Stop all services and remove persisted volume data
docker compose down -v
```

---

## End-to-End Workflow & Verification

You can verify the end-to-end data flow (UI/REST -> BEFE -> Infinispan -> Write-Behind SPI -> HAPI FHIR JPA -> PostgreSQL) using `curl`:

### 1. Create a FHIR Resource via BEFE
```bash
curl -i -X POST http://localhost:8080/api/fhir/Organization \
  -H "Content-Type: application/json" \
  -d '{
    "resourceType": "Organization",
    "name": "General Healthcare System",
    "active": true
  }'
```
*Expected response:* `HTTP/1.1 201 Created` with a `Location: /api/fhir/Organization/{id}` header and the JSON resource body containing the assigned ID.

### 2. Read Resource (Served from Infinispan Cache)
```bash
curl -i http://localhost:8080/api/fhir/Organization/{id}
```
*Expected response:* `HTTP/1.1 200 OK` with sub-10ms response latency.

### 3. Verify Asynchronous Persistence in HAPI FHIR JPA
The write-behind persistence SPI asynchronously flushes mutations to the HAPI FHIR JPA cluster:
```bash
curl -i http://localhost:8081/fhir/Organization/{id}
```
*Expected response:* `HTTP/1.1 200 OK` confirming data persistence on disk.

### 4. Interactive Web UI
Navigate to [http://localhost:3000](http://localhost:3000) to:
- Access the **Dashboard** displaying live resource metrics.
- Navigate to resource management pages (`Organizations`, `Locations`, `Practitioners`, `Roles`, `Services`, `Persons`, `Related Persons`, `Groups`, `Provenance`, `Audit`, `Consent`, `Tasks`, `Communication`, `Documents`).
- Create, filter, update, and delete records with automatic form validation.

---

## Running Submodules Locally (Development Mode)

If you prefer to run services individually on the host machine without Docker:

### 1. Database
Ensure PostgreSQL is running locally on port 5432 with database `fhir`, username `fhir_user`, and password `fhir_password` (or run `docker compose up -d postgres`).

### 2. HAPI FHIR JPA Server
```bash
cd hapi-fhir-jpa-server
SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run
```

### 3. Infinispan Cluster
```bash
cd infinispan-cluster
mvn clean package
# Start Infinispan server referencing the built infinispan-persistence provider and configuration
```

### 4. WildFly BEFE
```bash
cd befe
mvn clean package
# Deploy target/befe.war to your local WildFly 31+ application server
```

### 5. Vue 3 FHIR Resource UI (Vite Dev Server)
```bash
cd presentation-services/fhir-resource-ui
npm install
npm run dev
```
The Vite development server will start at `http://localhost:3000`.

### 6. Vue 3 Operations UI (Vite Dev Server)
```bash
cd presentation-services/operations-ui
npm install
npm run dev
```
The Vite development server will start at `http://localhost:3001`.

```xml
<plugin>
    <groupId>org.wildfly.plugins</groupId>
    <artifactId>wildfly-jar-maven-plugin</artifactId>
    <version>${wildfly-jar-maven-plugin.version}</version>
    <configuration>
        <feature-pack-location>wildfly@maven(org.jboss.universe:community-universe)#${wildfly.version}</feature-pack-location>
        <layers>
            <layer>jaxrs</layer>
            <layer>cdi</layer>
            <layer>jsonb</layer>
            <layer>management</layer>
        </layers>
        <context-root>/</context-root>
        <cloud/>
        <excluded-layers>
            <layer>deployment-scanner</layer>
        </excluded-layers>
        <plugin-options>
            <jboss-fork-embedded>true</jboss-fork-embedded>
        </plugin-options>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>package</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

---

## License

Copyright (C) 2026 Mark Hunter.

This project is licensed under the terms of the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.
