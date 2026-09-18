# Concept: Paradeigma `[IMPLEMENTED]`

Paradeigma is Harmonia's isolated synthetic clinical simulation framework, providing high-fidelity digital twins of external hospital applications, deterministic patient persona generators, multi-system clinical scenarios, and automated architectural invariant verification.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Παράδειγμα* (Paradeigma)
- **Etymology**: Ancient Greek neuter noun derived from the verb *παραδείκνυμι* (paradeiknymi - to show side by side, to exhibit, to set forth as a model or example).
- **Philosophical Context**: In classical philosophy (notably in Plato's *Timaeus* and Aristotle's *Rhetoric*), a *paradeigma* is an archetype, exemplar, or structural pattern. Plato used the term to describe the eternal Forms or ideal patterns after which the physical world was modeled. Aristotle used it to describe inductive reasoning through illustrative examples—using a known model to prove or test a broader thesis.
- **Architectural Rationale**: Healthcare integration systems cannot be safely developed or tested using real patient data due to strict privacy regulations (HIPAA/GDPR) and the inherent danger to clinical safety. Furthermore, staging environments rarely have access to live hospital EMRs, lab systems, and PACS machines. Paradeigma provides the ideal "archetypal model"—a synthetic mirror of the hospital universe that exercises every corner of Harmonia without touching real patients or live clinical systems.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Paradeigma is organized across seven decoupled modules under `paradeigma/`:

```
+---------------------------------------------------------------------------------------+
|                                  PARADEIGMA SUBSYSTEM                                 |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                        paradeigma-common (Synthetic Data)                      |  |
|   |   - Deterministic Persona Generators (names, MRNs, addresses, demographics)    |  |
|   |   - Synthetic Clinical Generators (vitals, lab panels, diagnoses, medications) |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Consumed by Simulators                      |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                     EXTERNAL HOSPITAL SIMULATOR SERVICES                       |  |
|   |                                                                                |  |
|   |   +--------------------+  +--------------------+  +------------------------+   |  |
|   |   |   paradeigma-pas   |  |   paradeigma-emr   |  |    paradeigma-lms      |   |  |
|   |   | (Simulates Hospital|  | (Simulates Clinical|  | (Simulates Pathology  |   |  |
|   |   |  PAS: ADT A01-A40) |  |  EMR: Orders/Notes)|  |  Lab: ORU Observations)|   |  |
|   |   +--------------------+  +--------------------+  +------------------------+   |  |
|   |             |                       |                         |                |  |
|   |             +-----------------------+-------------------------+                |  |
|   |                                     |                                          |  |
|   |                           +--------------------+                               |  |
|   |                           |  paradeigma-rispac |                               |  |
|   |                           | (Simulates Imaging |                               |  |
|   |                           |  PACS: ORM / DICOM)|                               |  |
|   |                           +--------------------+                               |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Driven by                                   |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                      paradeigma-scenarios (Scenario Engine)                    |  |
|   |   - Multi-stage clinical trajectories (Emergency, Inpatient, Surgical)         |  |
|   |   - Chaos & failure injection hooks (network drop, broker crash, slow ACK)     |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                      paradeigma-test (ArchUnit Architecture Rules)             |  |
|   |   - Automated enforcement of compile-time and runtime isolation invariants     |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

---

## 3. Mandatory Architectural Invariant: Production Isolation `[IMPLEMENTED]`

> **Invariant 1: Production Code $\rightarrow$ Paradeigma is STRICTLY FORBIDDEN.**

Harmonia enforces a strict one-way isolation boundary:
1. **Allowed**: Paradeigma simulators may invoke public Harmonia interfaces over standard network protocols (MLLP on port `2575`, FHIR REST on port `8080`) just like real external hospital applications.
2. **Forbidden**: No production POM may declare a `<dependency>` on any `net.fhirfactory.harmonia:paradeigma*` artifact.
3. **Forbidden**: No production Java class may import `net.fhirfactory.harmonia.paradeigma.*`.
4. **Forbidden**: No production class may include simulation flags (e.g., `isSimulationMode`, `paradeigmaEnabled`).
5. **Continuous Verification**: Enforced on every build via `ParadeigmaIsolationArchitectureTest`.

---

## 4. Ownership Boundaries `[IMPLEMENTED]`

### What Paradeigma Owns
- Deterministic synthetic patient personas, practitioner profiles, and clinical records.
- Standalone simulator services mimicking external hospital applications (PAS, EMR, LMS, RIS-PACS).
- Orchestrated end-to-end clinical workflow scenarios (e.g., Emergency Admission $\rightarrow$ Pathology Order $\rightarrow$ Results Return $\rightarrow$ Discharge).
- Chaos engineering hooks and failure injection mechanisms for platform resilience testing.
- Automated ArchUnit architectural rule definitions verifying repository invariants.

### What Paradeigma Explicitly Does NOT Own (Anti-Responsibilities)
- Any production runtime code or production database manipulation.
- Production container packaging or production Kubernetes deployments.
- In-process interceptors inside production gateway pipelines.

---

## 5. Submodules & Simulator Roles `[IMPLEMENTED]`

| Module Name | Simulated Entity | Outbound Protocol | Target Harmonia Ingress | Status |
| :--- | :--- | :--- | :--- | :--- |
| `paradeigma-common` | Data Generators | N/A (Shared Library)| Consumed by simulators | `[IMPLEMENTED]` |
| `paradeigma-pas` | Hospital PAS | MLLP Client | `pylai-mllp-in:2575` (HL7 ADT) | `[IMPLEMENTED]` |
| `paradeigma-emr` | Clinical EMR | MLLP Client / REST | `pylai-mllp-in:2575`, `pylai-fhir-registry:8080` | `[IMPLEMENTED]` |
| `paradeigma-lms` | Pathology Lab | MLLP Client | `pylai-mllp-in:2575` (HL7 ORU) | `[IMPLEMENTED]` |
| `paradeigma-rispac`| Radiology / PACS | MLLP Client | `pylai-mllp-in:2575` (HL7 ORM) | `[IMPLEMENTED]` |
| `paradeigma-scenarios`| Multi-System Testbed| HTTP REST / Control | Scenario Orchestrator | `[IMPLEMENTED]` |
| `paradeigma-test` | Architecture Test | ArchUnit Engine | Compile-time rule validation | `[IMPLEMENTED]` |
