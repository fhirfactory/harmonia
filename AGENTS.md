<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Harmonia Architectural Guardrails & Rules for AI Agents (AGENTS.md)

This document establishes the authoritative repository-wide architectural rules, boundaries, and development constraints for autonomous and co-executor agents working on the Harmonia Health Integration Environment (HIE) codebase.

---

## 1. System Taxonomy & Module Hierarchy

Harmonia enforces strict separation of concerns across its 9 core subprojects:

| Subproject | Domain / Responsibility | Allowed Dependencies | Invariants & Constraints |
| :--- | :--- | :--- | :--- |
| **Calliope** (`calliope`) | Canonical Schemas, Common Models, HL7/FHIR converters, Topic definitions | JDK, HAPI FHIR Structures | Pure domain models; zero dependencies on higher layers (Themis, Hestia, Petasos, Energeia, Pylai, Iris, Paradeigma, Agora). |
| **Themis** (`themis/`) | Policy evaluation, RBAC/ABAC authorization, non-PHI security auditing | JDK, Calliope, HAPI FHIR | Default-deny security engine; `themis-api` contains pure contracts without engine or storage dependencies. |
| **Hestia** (`hestia/`) | Mneme (Infinispan caching) and Mnemosyne (HAPI FHIR R5 / PostgreSQL JPA persistence) | Calliope, Themis, Infinispan, HAPI FHIR, PostgreSQL | Persistent authoritative storage; separate clinical (`fhir_node_*`) and operations (`ops_node_*`) databases. |
| **Petasos** (`petasos/`) | Resilient messaging abstraction and ActiveMQ Artemis broker adapters | JDK, Calliope, Themis API | `petasos-api` is strictly free of JMS or Artemis dependencies. Artemis client code is isolated to `petasos-artemis`. |
| **Energeia** (`energeia/`) | Task processing (Ponos), Ergon activity units (Erga), Workflow orchestration (Praxis) | Calliope, Themis, Hestia, Petasos | Activity execution handles `Pragma` envelopes and FHIR `Task` lifecycle. |
| **Pylai** (`pylai/`) | Inbound/Outbound protocol gateways (MLLP, FHIR REST Registry) | Calliope, Themis, Petasos, Hestia | Translates external wire protocols into Petasos events and Mnemosyne tasks. |
| **Iris** (`iris/`) | Presentation services (iris-befe WildFly gateway, iris-clinical, iris-console, iris-administration SPAs) | Calliope, Themis API, Hot Rod Client | Strictly decoupled from backend storage/JPA; communicates via REST and Hot Rod only. |
| **Agora** (`agora/`) | Matrix/Synapse collaboration projection, AS transaction ingestion, room/space lifecycle | Calliope, Themis API, Petasos API, Mnemosyne Operations | Zero Ponos dependencies; Matrix DTO encapsulation; Themis default-deny governance; zero-PHI metadata. |
| **Paradeigma** (`paradeigma/`) | Synthetic clinical simulation (EMR, LMS, PAS, RIS-PAC simulators, scenario engine) | Production APIs (MLLP, FHIR REST, Petasos API) | **Leaf / Simulation Only**. Production modules MUST NEVER depend on or import Paradeigma. |

---

## 2. Mandatory Architectural Invariants

### Invariant 1: Paradeigma Production Isolation
- **Rule**: `Production Code -> Paradeigma` is strictly **forbidden**.
- **Enforcement**:
  1. No production POM may declare a `<dependency>` on any `net.fhirfactory.harmonia:paradeigma*` artifact.
  2. No production Java class may import `net.fhirfactory.harmonia.paradeigma.*`.
  3. No production class may include simulation flags (e.g. `paradeigmaMode`, `simulationMode`, `syntheticRequest`, `isParadeigmaGenerated`).
  4. Verified continuously by `ParadeigmaIsolationArchitectureTest`.

### Invariant 2: Petasos API Abstraction & Encapsulation
- **Rule**: `petasos-api` contains pure Java abstractions (`Petasos`, `PetasosProducer`, `PetasosConsumer`, `PetasosMessage`, `PetasosDestination`).
- **Enforcement**:
  1. Zero `org.apache.activemq..`, `jakarta.jms..`, or `javax.jms..` classes may be exposed in or imported by `petasos-api`.
  2. Message payloads in Petasos are treated as opaque binary/text streams (`byte[]` / `String`). Petasos never parses HL7 or FHIR business content.
  3. Verified continuously by `PetasosApiIsolationArchitectureTest`.

### Invariant 3: Iris Presentation Decoupling
- **Rule**: The Iris presentation tier (`iris-befe` and Vue 3 SPAs) must remain presentation-only and decoupled from internal databases.
- **Enforcement**:
  1. Iris modules must not depend on or import JPA/Hibernate (`jakarta.persistence..`, `org.hibernate..`), PostgreSQL drivers (`org.postgresql..`), or server-side JPA (`ca.uhn.fhir.jpa..`).
  2. `iris-administration` is a Vue 3 SPA consuming FHIR REST endpoints (`/fhir/r5/Practitioner`, etc.). It must not implement Provider Registry database storage, authoritative validation state machines, or referential integrity rules.
  3. Server-side Provider Registry governance is owned exclusively by `mnemosyne-clinical`, `energeia-erga`, and `themis-core`.
  4. Verified continuously by `IrisDecouplingArchitectureTest` and `ProviderRegistryArchitectureTest`.

### Invariant 4: Ingress Dual-Write Safety (REC-001)
- **Rule**: Inbound gateways (`pylai-mllp-in`) must guarantee end-to-end downstream message acceptance before returning an `AA` (Application Accept) ACK to the upstream sender.
- **Enforcement**:
  1. If downstream Petasos queue publishing (`taskEventProducerService.sendTaskEvent(...)`) or cache write fails, the exception must NOT be swallowed.
  2. The failure MUST trigger an `AE` (Application Error) NACK response back over MLLP to prompt upstream sender retry.

### Invariant 5: Destination Fan-Out State Tracking (REC-002)
- **Rule**: Parent workflow tasks in Mnemosyne and Pragma envelopes must track granular sub-status per fan-out destination.
- **Enforcement**:
  1. `AdtDistributionErgon` records destination checkpoints (`FANOUT_DISPATCH_INITIATED`, `destinationQueue`, `status=QUEUED`) in the `Pragma`.
  2. `OutboundTaskResourceBuilder` updates `Task.output` with structured `http://example.org/hie/destination-delivery-status` extensions capturing `destinationId`, `status`, `ackCode`, and timestamps upon delivery.

### Invariant 6: Default-Deny Security Governance (Themis)
- **Rule**: All ingress endpoints, task processors, and storage mutators must evaluate authorization through Themis.
- **Enforcement**:
  1. Unauthenticated or unauthorized requests must default to `DENY` (`ThemisDecision.DENY`).
  2. Context propagation across pipelines must use immutable `PragmaSecurityContext` and FHIR security labels (`FhirSecurityTagManager`).
  3. Audit records must be dispatched to `ThemisAuditService` without logging unmasked PHI.

### Invariant 7: Zero-PHI Diagnostic Logging
- **Rule**: Protected Health Information (PHI) must never be emitted into non-clinical log streams.
- **Enforcement**:
  1. Log identifiers (MRN, control IDs) only; mask or omit patient names, addresses, and clinical observation values in standard log statements.
  2. Use `FhirSanitizer` or `PhiLogRouting` for diagnostic message logging.

### Invariant 8: Agora Collaboration & Matrix Isolation
- **Rule**: Agora makes Matrix a Harmonia collaboration capability; it does NOT make Matrix the Harmonia architecture.
- **Enforcement**:
  1. **Ponos Decoupling**: Direct dependency from Agora to Ponos (`net.fhirfactory.harmonia.energeia.ponos..`) is strictly forbidden. Agora coordinates with workflows exclusively via Petasos queues (`petasos.queue.agora.*`).
  2. **Matrix DTO Encapsulation**: Matrix protocol structures (Client-Server and Synapse Admin DTOs) must be encapsulated in `agora-matrix`. Raw Matrix types must never leak into other Harmonia modules.
  3. **Themis Default-Deny Authorization**: Every room creation, invite, join, or kick operation must be gated by Themis policy evaluation (`themisAuthorizer.evaluate(...)`).
  4. **Zero-PHI Room Metadata**: Room aliases, Space names, and topics must never include patient names, DOB, MRN, or clinical details (use opaque UUIDs).

---

## 3. Automated Architecture Test Suite

All agents making modifications to the Harmonia repository must verify changes against the ArchUnit architecture suite located in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`:

- `ParadeigmaIsolationArchitectureTest`: Asserts zero production dependencies, imports, or simulation flags for Paradeigma across all modules (including `agora`).
- `AgoraIsolationArchitectureTest`: Asserts Ponos decoupling, Matrix DTO encapsulation, and Paradeigma isolation for Agora.
- `PetasosApiIsolationArchitectureTest`: Asserts zero JMS or ActiveMQ Artemis API leakage into `petasos-api`.
- `IrisDecouplingArchitectureTest`: Asserts zero direct JPA, Hibernate, or PostgreSQL database dependencies in Iris.
- `ProviderRegistryArchitectureTest`: Asserts `iris-administration` separation from server-side Provider Registry governance.
- `PackageLayeringArchitectureTest`: Asserts strict unidirectional dependency layering across all subproject packages.
- `SecurityEnforcementArchitectureTest`: Asserts Themis policy contracts and security context structures.

---

## 4. Execution & Build Commands

- **Run Architecture Tests**:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
  ```

- **Run Agora Subsystem Tests**:
  ```bash
  mvn test -pl agora/agora-service -am
  ```

- **Run Inbound MLLP Gateway Tests (REC-001)**:
  ```bash
  mvn test -pl pylai/pylai-mllp-in -am
  ```

- **Run Ergon Activity & Outbound Gateway Tests (REC-002)**:
  ```bash
  mvn test -pl energeia/erga,pylai/pylai-mllp-out -am
  ```

- **Run Full Repository Test Suite**:
  ```bash
  mvn test
  ```
