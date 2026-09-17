# Paradeigma Simulation Framework Architecture `[IMPLEMENTED]`

> **Notice**: For the full architectural specification, component models, and sequence workflows, see **[Paradeigma Architecture & Component Specifications](architecture.md)** and **[Paradeigma Overview](overview.md)**.

---

## 1. Simulators and Scenario Engine

```
[ Scenario Engine (paradeigma-scenarios) ]
       │
       ├─► PAS Simulator (paradeigma-pas)   ──► Ingests ADT A01-A40 over MLLP :2575
       ├─► EMR Simulator (paradeigma-emr)   ──► Ingests ORM Orders over MLLP :2575 / REST
       ├─► LMS Simulator (paradeigma-lms)   ──► Ingests ORU Results over MLLP :2575
       └─► RIS Simulator (paradeigma-rispac)──► Ingests Imaging metadata over MLLP :2575
```

### Supported Clinical Workflows
1. **Inpatient Admission Workflow**:
   - `PAS` sends `ADT^A01` (Patient Admit) to `pylai-mllp-in`.
   - `Ponos` converts to FHIR `Patient`, `Encounter`, and `Location` resources.
   - `Pylai Outbound` fans out egress `ADT^A01` messages to external HIS and LIS destinations.
2. **Order & Result Fulfillment Workflow**:
   - `EMR` sends `ORM^O01` (Diagnostic Order) for blood pathology.
   - `LMS` simulator receives order notification and generates synthetic observation values.
   - `LMS` dispatches `ORU^R01` (Unsolicited Result) to Harmonia inbound gateway.
   - `Ponos` executes `Oru2FhirMapper` creating FHIR `DiagnosticReport` and `Observation` entries.
3. **Provider Registry Synchronization**:
   - `PAS`/`EMR` sends `MFN^M02` (Staff Master File) updates.
   - `Ponos` processes `Mfn2FhirBundle` updating `Practitioner` and `PractitionerRole` records.

---

## 2. Deterministic Persona Generation

`paradeigma-common` contains deterministic generators:
- Stable national identifiers (MRN, NPI, Tax ID) generated via seeded pseudo-random algorithms.
- Clinically valid demographics (gender, date of birth, postal codes).
- Structured clinical observation ranges for pathology and radiology.
