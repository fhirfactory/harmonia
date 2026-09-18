# Concept: Mnemosyne `[IMPLEMENTED]`

Mnemosyne is Harmonia's authoritative relational persistence subsystem within Hestia, responsible for permanent, ACID-compliant historical storage of clinical FHIR R5 resources and platform operational telemetry across dedicated PostgreSQL 16 databases.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Μνημοσύνη* (Mnemosyne)
- **Etymology**: Ancient Greek proper noun derived from *μνήμη* (memory) and *μνάομαι* (to be mindful of, to remember).
- **Mythological Context**: Mnemosyne is the Titaness of memory, daughter of Uranus (Sky) and Gaia (Earth). In Greek mythology, after the Titanomachy, Zeus lay with Mnemosyne for nine consecutive nights in Pieria; she gave birth to the nine Olympian Muses (including Calliope). While Mneme represented immediate working memory, Mnemosyne represented permanent, enduring, cultural, and historical remembrance. She knew everything that was, that is, and that will be.
- **Architectural Rationale**: Healthcare systems cannot rely on ephemeral caches for clinical records. A patient's medical history, diagnoses, lab results, and audit trails must be preserved faithfully across decades with strict relational integrity, ACID guarantees, and comprehensive auditability. Mnemosyne is the permanent, authoritative sanctuary for this clinical truth.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Mnemosyne enforces a strict separation between clinical and operational storage across two decoupled microservices:

```
+---------------------------------------------------------------------------------------+
|                                  MNEMOSYNE SUBSYSTEM                                  |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +---------------------------------------+   +------------------------------------+  |
|   |         MNEMOSYNE CLINICAL            |   |        MNEMOSYNE OPERATIONS        |  |
|   |        (mnemosyne-clinical)           |   |       (mnemosyne-operations)       |  |
|   |                                       |   |                                    |  |
|   | * Technology: Spring Boot 3.2.5       |   | * Technology: Spring Boot 3.2.5    |  |
|   | * Engine: HAPI FHIR R5 JPA 7.2.0      |   | * Engine: Spring Data JPA          |  |
|   | * REST Context: /fhir/r5/*            |   | * REST Context: /api/operations/*  |  |
|   | * Database: PostgreSQL 16             |   | * Database: PostgreSQL 16          |  |
|   |   (fhir_node_1, fhir_node_2)          |   |   (ops_node_1, ops_node_2)         |  |
|   |                                       |   |                                    |  |
|   | Domain Entities:                      |   | Domain Entities:                   |  |
|   |  - Patient, Encounter, Condition      |   |  - TaskSequenceEntity              |  |
|   |  - Observation, DiagnosticReport      |   |  - PragmaAuditEntity               |  |
|   |  - Practitioner, Organization         |   |  - ModuleHealthEntity              |  |
|   +---------------------------------------+   +------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

1. **Mnemosyne Clinical**: Provides a fully compliant FHIR R5 REST API powered by HAPI FHIR JPA. Exposes standard endpoints (`GET /fhir/r5/Patient/{id}`, `POST /fhir/r5/Observation`, etc.) backed by high-availability PostgreSQL clusters.
2. **Mnemosyne Operations**: Tracks the lifecycle, execution duration, and audit checkpoints of all platform `TaskSequence` instances, ensuring full compliance with healthcare regulatory audit mandates without contaminating clinical tables.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Mnemosyne Owns
- Authoritative relational schema management and JPA entities (`hie_fhir_resources`, `hie_operations_resources`).
- Relational database connections, HikariCP connection pooling, and transaction boundaries (`@Transactional`).
- HAPI FHIR R5 JPA DAO layer, indexing engines, and search parameter resolvers (`ca.uhn.fhir.jpa..`).
- Historical audit log retention and operational task lineage tables.
- Database migration and schema versioning.

### What Mnemosyne Explicitly Does NOT Own (Anti-Responsibilities)
- Ephemeral in-flight cache coordination (owned by Mneme).
- External network protocol adapters or MLLP socket connections (owned by Pylai).
- Direct browser UI rendering (owned by Iris).
- Background queue consumption and worker loops (owned by Energeia).

---

## 4. Architectural Invariants `[IMPLEMENTED]`

### Database Isolation Invariant `[IMPLEMENTED]`
- Operational telemetry queries must never execute against the clinical database (`fhir_node_*`), and clinical queries must never execute against the operations database (`ops_node_*`).
- Both microservices maintain independent database credentials, connection pools, and PostgreSQL instances.

### Presentation Decoupling Invariant `[IMPLEMENTED]`
- Modules in the `iris` presentation tier (such as `iris-befe` or Vue SPAs) are **strictly forbidden** from importing `jakarta.persistence..`, `org.hibernate..`, or `org.postgresql..`. All presentation queries route through REST APIs or Hot Rod cache interfaces.
- Continuously verified by `IrisDecouplingArchitectureTest`.

---

## 5. Key Classes & Configuration `[IMPLEMENTED]`

| Component | Module Name | Key Technology | Key Classes | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Clinical JPA Server** | `mnemosyne-clinical` | Spring Boot 3.2.5, HAPI FHIR JPA | `MnemosyneClinicalJpaApplication`, `FhirServerConfig` | `[IMPLEMENTED]` |
| **Operations JPA Server**| `mnemosyne-operations`| Spring Boot 3.2.5, Spring Data JPA | `MnemosyneOperationsApplication`, `TaskSequenceRepository` | `[IMPLEMENTED]` |

### Database Connection Configuration `[CONFIGURED]`
- `SPRING_DATASOURCE_URL` (Clinical): `jdbc:postgresql://postgres-1:5432/fhir_node_1`
- `SPRING_DATASOURCE_URL` (Operations): `jdbc:postgresql://postgres-ops-1:5432/ops_node_1`
- `HAPI_FHIR_VERSION`: `R5`
