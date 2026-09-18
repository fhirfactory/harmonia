# Calliope Canonical Models and Schemas Architecture

Calliope is Harmonia's foundation library for shared information models, canonical schemas, data type converters, and topic definitions.

---

## 1. Architectural Role & Boundary Constraints

Calliope sits at the foundation layer of Harmonia's module hierarchy:

```
[ All Subsystems (Themis, Hestia, Petasos, Energeia, Pylai, Iris) ]
                               │
                               ▼
            [ Calliope Canonical Library (calliope) ]
                               │
                               ▼
                  [ JDK 21 / HAPI FHIR Structures ]
```

### Invariant Constraints
- **Zero Inward Coupling**: Calliope must NEVER depend on higher-tier modules (Themis, Hestia, Petasos, Energeia, Pylai, Iris, or Paradeigma).
- **Format Agnostic**: Provides domain DTOs and canonical mappings between HL7 v2 and FHIR R5 structures without embedding network transport logic.

---

## 2. Core Information Models & Schemas

### 2.1 Event & Activity Envelopes
- **`ErgonEvent`**: Standardized event wrapper carrying event metadata, timestamps, trigger reason, and payload references.
- **`ErgonPayload`**: Strongly-typed payload container encapsulating raw HL7, parsed JSON, or FHIR resource bundles.
- **`ErgonReasonEnum`**: Standardized business reasons for activity execution (e.g., `PATIENT_ADMIT`, `PATIENT_DISCHARGE`, `PROVIDER_REGISTRATION`, `ORDER_PLACED`, `RESULT_AVAILABLE`).

### 2.2 Canonical Converters & Mappers
- **`Adt2FhirMapper`**: Maps HL7 v2.x ADT trigger events (A01, A02, A03, A04, A08, A40) to FHIR R5 `Patient`, `Encounter`, and `Location` resources.
- **`Mfn2FhirBundle`**: Maps Master File Notification (MFN) messages to FHIR R5 `Practitioner`, `PractitionerRole`, and `Organization` bundles.
- **`Orm2FhirMapper`**: Maps Order Management (ORM) messages to FHIR R5 `ServiceRequest`.
- **`Oru2FhirMapper`**: Maps Observation Result (ORU) messages to FHIR R5 `Observation` and `DiagnosticReport`.
