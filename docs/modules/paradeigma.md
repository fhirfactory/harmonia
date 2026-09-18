# Paradeigma Subproject Reference: Synthetic Clinical Simulation `[IMPLEMENTED]`

Paradeigma is Harmonia's isolated synthetic clinical simulation framework, delivering high-fidelity digital twins of external hospital applications, deterministic patient persona generators, multi-system clinical scenarios, and automated architectural invariant verification.

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
paradeigma/
├── paradeigma-common/      # Synthetic personas, identifiers, clinical data generators & DTOs
├── paradeigma-emr/         # Electronic Medical Record simulator (ORM / ADT / Clinical notes)
├── paradeigma-lms/         # Laboratory Management System simulator (ORU observations & lab results)
├── paradeigma-pas/         # Patient Administration System simulator (ADT A01-A40 trigger events)
├── paradeigma-rispac/      # Radiology Information System / PACS simulator (DICOM metadata & imaging orders)
├── paradeigma-scenarios/   # Orchestrated multi-system clinical scenarios and load injectors
└── paradeigma-test/        # ArchUnit architectural rule enforcement and acceptance test suite
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `paradeigma`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `paradeigma-common` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-common:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.common.failure`
  - `net.fhirfactory.harmonia.paradeigma.common.generator`
  - `net.fhirfactory.harmonia.paradeigma.common.hl7`
  - `net.fhirfactory.harmonia.paradeigma.common.logging`
  - `net.fhirfactory.harmonia.paradeigma.common.mllp`
  - `net.fhirfactory.harmonia.paradeigma.common.model`
  - `net.fhirfactory.harmonia.paradeigma.common.rest`
  - `net.fhirfactory.harmonia.paradeigma.common.security`
- **Key Classes & Utilities**:
  - `SyntheticPatientGenerator`: Generates realistic, deterministic patient demographics (names, MRNs, addresses, Medicare IDs).
  - `SyntheticProviderRegistryGenerator`: Generates practitioner rosters, specialties, and facility hierarchies with referential integrity.
  - `SyntheticOrderGenerator` & `SyntheticResultGenerator`: Generates simulated medication orders and pathology results.
  - `FailureSimulator` & `FaultInjectionConfig`: Programmable chaos testing hooks (network socket drops, slow ACKs, corrupted MLLP frames).
  - `MllpClient` & `MllpServer`: Standalone Netty MLLP client and server engines for simulating external endpoints.
  - `PhiLogTestProbe` & `SecretLeakageAssertion`: Automated test fixtures asserting zero PHI or unmasked credentials in logs.
- **Runtime Dependencies**: Java 21, Netty, HAPI HL7v2 (`2.3`), HAPI FHIR Structures R5 (`7.2.0`), Slf4j.

### 2.2 `paradeigma-pas` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-pas:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.pas`
  - `net.fhirfactory.harmonia.paradeigma.pas.config`
  - `net.fhirfactory.harmonia.paradeigma.pas.mllp`
  - `net.fhirfactory.harmonia.paradeigma.pas.rest`
- **Key Classes & Capabilities**:
  - `PasApplication`: Spring Boot 3.2.5 simulator service.
  - Simulates a hospital Patient Administration System emitting HL7 v2 ADT trigger events (A01 Admission, A02 Transfer, A03 Discharge, A08 Update) over MLLP to `pylai-mllp-in` (port `2575`).
  - REST control endpoints for triggering manual admission batches (`POST /api/simulator/pas/admission`).
- **Runtime Dependencies**: `paradeigma-common`, Spring Boot Starter Web (`3.2.5`).

### 2.3 `paradeigma-emr` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-emr:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.emr`
  - `net.fhirfactory.harmonia.paradeigma.emr.config`
  - `net.fhirfactory.harmonia.paradeigma.emr.mllp`
  - `net.fhirfactory.harmonia.paradeigma.emr.rest`
  - `net.fhirfactory.harmonia.paradeigma.emr.service`
- **Key Classes & Capabilities**:
  - `EmrApplication`: Spring Boot 3.2.5 simulator service.
  - Simulates an Electronic Medical Record system generating physician medication orders (HL7 ORM) and consuming downstream patient updates.
  - Contains embedded MLLP listener to receive outbound messages from `pylai-mllp-out`.
- **Runtime Dependencies**: `paradeigma-common`, Spring Boot Starter Web (`3.2.5`).

### 2.4 `paradeigma-lms` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-lms:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.lms`
  - `net.fhirfactory.harmonia.paradeigma.lms.config`
  - `net.fhirfactory.harmonia.paradeigma.lms.mllp`
  - `net.fhirfactory.harmonia.paradeigma.lms.rest`
- **Key Classes & Capabilities**:
  - `LmsApplication`: Spring Boot 3.2.5 simulator service.
  - Simulates a hospital Pathology Laboratory Management System consuming lab orders and emitting structured HL7 v2 ORU^R01 observation results (biochemistry, hematology, microbiology).
- **Runtime Dependencies**: `paradeigma-common`, Spring Boot Starter Web (`3.2.5`).

### 2.5 `paradeigma-rispac` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-rispac:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.rispac`
  - `net.fhirfactory.harmonia.paradeigma.rispac.config`
  - `net.fhirfactory.harmonia.paradeigma.rispac.mllp`
  - `net.fhirfactory.harmonia.paradeigma.rispac.rest`
- **Key Classes & Capabilities**:
  - `RispacApplication`: Spring Boot 3.2.5 simulator service.
  - Simulates a Radiology Information System / Picture Archiving and Communication System (PACS) generating imaging orders (X-ray, CT, MRI) and report metadata.
- **Runtime Dependencies**: `paradeigma-common`, Spring Boot Starter Web (`3.2.5`).

### 2.6 `paradeigma-scenarios` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-scenarios:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.scenarios`
  - `net.fhirfactory.harmonia.paradeigma.scenarios.agora`
  - `net.fhirfactory.harmonia.paradeigma.scenarios.client`
  - `net.fhirfactory.harmonia.paradeigma.scenarios.engine`
  - `net.fhirfactory.harmonia.paradeigma.scenarios.journey`
  - `net.fhirfactory.harmonia.paradeigma.scenarios.registry`
  - `net.fhirfactory.harmonia.paradeigma.scenarios.rest`
- **Key Classes & Scenarios**:
  - `ParadeigmaScenarioEngine`: Central orchestrator driving multi-step synthetic clinical trajectories.
  - `PatientJourneyScenario`: Executes an end-to-end journey: Emergency Presentation $\rightarrow$ Inpatient Admission (PAS) $\rightarrow$ Blood Test Order (EMR) $\rightarrow$ Specimen Processing & Result Return (LMS) $\rightarrow$ Care Team Collaboration (Agora) $\rightarrow$ Patient Discharge.
  - `ProviderRegistryJourneyScenario`: Exercises master practitioner onboarding, role assignments, and referential verification.
  - `AgoraCollaborationScenario`: Drives synthetic room chats and task resolution inside Matrix Synapse.
  - `ScenariosRestController`: REST control plane on port `8080` exposing `/api/scenarios/*`.
- **Runtime Dependencies**: `paradeigma-common`, Spring Boot Starter Web (`3.2.5`).

### 2.7 `paradeigma-test` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:paradeigma-test:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.paradeigma.test`
  - `net.fhirfactory.harmonia.paradeigma.test.arch`
  - `net.fhirfactory.harmonia.paradeigma.test.generator`
  - `net.fhirfactory.harmonia.paradeigma.test.logging`
  - `net.fhirfactory.harmonia.paradeigma.test.registry`
  - `net.fhirfactory.harmonia.paradeigma.test.security`
- **Key Architecture & Acceptance Tests**:
  - **ArchUnit Architectural Rules**:
    - `ParadeigmaIsolationArchitectureTest`: Asserts production code contains zero dependencies, imports, or simulation flags for Paradeigma.
    - `AgoraIsolationArchitectureTest`: Asserts Ponos decoupling and Matrix DTO encapsulation for Agora.
    - `IrisDecouplingArchitectureTest`: Asserts Iris contains zero JPA, Hibernate, or PostgreSQL imports.
    - `PetasosApiIsolationArchitectureTest`: Asserts `petasos-api` contains zero JMS or ActiveMQ imports.
    - `ProviderRegistryArchitectureTest`: Asserts Iris presentation decoupling from backend registry governance.
    - `PackageLayeringArchitectureTest`: Asserts unidirectional dependency layering across all subprojects.
    - `SecurityEnforcementArchitectureTest`: Asserts Themis policy contracts and security context propagation.
  - **Acceptance & Resilience Tests**:
    - `EndToEndPatientJourneyTest`: Validates end-to-end processing across all tiers.
    - `FailureRecoveryTest`: Validates broker crash recovery and MLLP error responses (REC-001).
    - `AdtFanOutIntegrationTest`: Validates destination-specific fan-out tracking (REC-002).
- **Runtime Dependencies**: ArchUnit (`1.3.0`), JUnit Jupiter (`5.10.2`), AssertJ (`3.25.3`), Mockito (`5.11.0`).

---

## 3. Mandatory Isolation Invariants `[IMPLEMENTED]`

### Invariant 1: Paradeigma Production Isolation `[IMPLEMENTED]`
- **Rule**: `Production Code -> Paradeigma` is strictly **forbidden**.
- **Enforcement**:
  1. No production POM may declare a `<dependency>` on any `net.fhirfactory.harmonia:paradeigma*` artifact.
  2. No production Java class may import `net.fhirfactory.harmonia.paradeigma.*`.
  3. No production class may include simulation flags (`isSimulation`, `paradeigmaMode`, `syntheticFlag`).
  4. Paradeigma classes are never packaged into production container images.
  5. Continuously verified by `ParadeigmaIsolationArchitectureTest`.

---

## 4. Verification & Testing `[IMPLEMENTED]`

- **Execute Architecture Isolation Tests**:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
  ```
- **Execute Full Simulation Test Suite**:
  ```bash
  mvn test -pl paradeigma/paradeigma-common,paradeigma/paradeigma-emr,paradeigma/paradeigma-lms,paradeigma/paradeigma-pas,paradeigma/paradeigma-rispac,paradeigma/paradeigma-scenarios,paradeigma/paradeigma-test -am
  ```
