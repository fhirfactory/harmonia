# Hestia Data Services: Mneme & Mnemosyne Architecture

Hestia is Harmonia's foundational data management subsystem, uniting low-latency in-memory caching (**Mneme**) and durable relational persistence (**Mnemosyne**).

---

## 1. Subsystem Architecture

```
                       [ Harmonia Services (BEFE / Ponos / Gateways) ]
                                              │
                                              ▼  Hot Rod Protocol (Binary RPC :11222)
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ Mneme In-Memory Data Grid (Infinispan 15.0.3 Clustered StatefulSets)                        │
│                                                                                             │
│  ├── fhir-resource-cache    (FHIR R5 Resources: Patient, Practitioner, Communication, etc.) │
│  ├── task-sequence-cache    (Praxis TaskSequence definitions)                               │
│  ├── operations-cache       (Operational metrics, telemetry & module statuses)              │
│  └── pragma-cache           (Active Task/Pragma state and execution checkpoints)            │
└─────────────────────────────────────────────┬───────────────────────────────────────────────┘
                                              │
                                              ▼  NonBlockingStore SPI (mneme-persistence)
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ Mnemosyne Relational Disk Persistence (Spring Boot 3.2.5 JPA REST Servers)                  │
│                                                                                             │
│  ├── mnemosyne-clinical    (HAPI FHIR R5 JPA Server :8081/8082)                             │
│  └── mnemosyne-operations  (Operations JPA Server :8085/8086)                               │
└─────────────────────────────────────────────┬───────────────────────────────────────────────┘
                                              │
                                              ▼  PostgreSQL Native Wire Protocol (:5432-5435)
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ PostgreSQL 16 Relational Databases                                                          │
│                                                                                             │
│  ├── fhir_node_1 / fhir_node_2  (hie_fhir_resources: Clinical FHIR R5 entities)             │
│  └── ops_node_1 / ops_node_2    (hie_operations_resources: Task histories & Audit)         │
└─────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Mneme Cache Grid Topology (Infinispan 15.0.3)

- **Cache Modes**:
  - `REPL_SYNC` (Synchronous Replication) for high-criticality shared state (`task-sequence-cache`, `active-pragmas`).
  - `DIST_SYNC` (Distributed Clustered) with 2 owners for horizontal scaling of large resource sets.
- **Cluster Discovery**: JGroups TCP discovery over port `7800` using the configured `infinispan-service` Kubernetes Service in the `harmonia` namespace.
- **Hot Rod Clients**: Low-latency binary RPC protocol (`11222`) with near-caching and intelligent client routing.

---

## 3. Mnemosyne Relational Persistence (PostgreSQL 16)

### 3.1 Clinical Persistence (`mnemosyne-clinical`)
- **Framework**: Spring Boot 3.2.5 with HAPI FHIR R5 JPA structures (`ca.uhn.fhir.jpa`).
- **Supported Resources**: Complete CRUD and search for 14 core FHIR R5 resources (`Person`, `RelatedPerson`, `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Group`, `Provenance`, `AuditEvent`, `Consent`, `Task`, `Communication`, `DocumentReference`).
- **Database**: PostgreSQL 16 (`fhir_node_1`, `fhir_node_2`).

### 3.2 Operations Persistence (`mnemosyne-operations`)
- **Framework**: Spring Boot 3.2.5 with Spring Data JPA.
- **Scope**: Non-FHIR operational entities, TaskSequence blueprints, module registration telemetry, and historical execution records.
- **Database**: PostgreSQL 16 (`ops_node_1`, `ops_node_2`).
