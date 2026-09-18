# Harmonia Paradeigma — Simulation, Synthetic Data & Test Framework

**Harmonia Paradeigma** is the leaf simulation, synthetic data, and scenario framework for the Harmonia Health Integration Environment (HIE). It generates representative synthetic data, executes deterministic multi-system journeys, exercises public Harmonia contracts, and validates observable runtime behavior across HL7 v2 clinical messaging, FHIR R5 Provider Registry, Themis Security, and Dual-Gate PHI Logging.

---

## Core Simulation Capabilities

1. **FHIR R5 Provider Registry Simulation**
   - Deterministic resource generation (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) with Australian Healthcare Identifiers (HPI-I, HPI-O).
   - Interconnected topology graph builders and negative broken-reference graph generators.
   - Governed change request write pipeline lifecycle (`RECEIVED` -> `VALIDATING` -> `APPROVED` -> `COMMITTING` -> `COMPLETED`) with ETag concurrency, duplicate detection, and OperationOutcome capture.

2. **Themis Security Simulation & RBAC/ABAC Validation**
   - Pre-configured security actors (`Provider Steward`, `Clinician`, `System Administrator`, `Integration Service`, `Read-Only User`, `Unauthorized User`, `Unauthenticated Principal`).
   - Granular permission mapping against `HarmoniaRoleEnum` and `HarmoniaAuthorityEnum`.
   - Simulation test seams for expired contexts, missing roles, and defense-in-depth async execution governance.

3. **Dual-Gate PHI-Aware Logging Probes & Secret Protection**
   - In-memory `PhiLogTestProbe` for inspecting operational namespaces and dedicated `org.harmonia.phi` diagnostic events with `PHI` marker.
   - Matrix assertions covering all 5 logging states (`harmonia.logging.phi-enabled` + INFO/DEBUG/TRACE).
   - `SecretLeakageAssertion` ensuring authentication tokens, passwords, API keys, and JWTs never leak into any log stream.

4. **HL7 v2 Clinical Workflow Simulators**
   - **PAS** (`paradeigma-pas` :8091): ADT registration, admission, transfer, and discharge lifecycle.
   - **EMR** (`paradeigma-emr` :8092): Ingests ADT fan-out and places laboratory/imaging `ORM^O01` orders.
   - **LMS** (`paradeigma-lms` :8093): Ingests ADT/ORM and emits synthetic `ORU^R01` pathology results.
   - **RIS-PAC** (`paradeigma-rispac` :8094): Ingests ADT/ORM and emits synthetic `ORU^R01` diagnostic imaging reports.
   - **Scenario Conductor Engine** (`paradeigma-scenarios` :8090): Orchestrates automated clinical journeys and registry scenarios.

---

## Architectural Principle: Strict Leaf Module Topology

Paradeigma is strictly a **test-support and simulation leaf module**:
- **Allowed Dependency Direction**: `Paradeigma -> Production Harmonia`
- **Forbidden Dependency Direction**: `Production Harmonia -X-> Paradeigma`
- **Zero Production Contamination**: No production class imports `net.fhirfactory.harmonia.paradeigma.*`, no production POM declares Paradeigma dependencies, and no production code contains simulation flags (`if (simulationMode)`).
- **Automated Enforcement**: Enforced at build-time via ArchUnit (`ParadeigmaIsolationArchitectureTest`) and Maven POM dependency checks.

---

## Documentation Index

### Provider Registry, Security & Logging
- [Provider Registry Simulation Guide](docs/provider-registry-simulation.md)
- [Themis Security Simulation Guide](docs/security-simulation.md)
- [Dual-Gate PHI Logging Simulation Guide](docs/logging-simulation.md)

### Architecture & Clinical Workflows
- [Architecture & Design](docs/architecture.md)
- [Interface Catalogue (PD-01 to PD-09)](docs/interfaces.md)
- [HL7 Event Specifications](docs/hl7-events.md)
- [PAS Simulator Guide](docs/pas-simulator.md)
- [EMR Simulator Guide](docs/emr-simulator.md)
- [LMS Simulator Guide](docs/lms-simulator.md)
- [RIS-PAC Simulator Guide](docs/rispac-simulator.md)
- [Scenario Engine & Patient Journey](docs/scenarios.md)
- [Failure Simulation & Resilience Testing](docs/failure-testing.md)
- [Running Paradeigma Locally & Docker Deployment](docs/running-paradeigma.md)
- [Troubleshooting Guide](docs/troubleshooting.md)
