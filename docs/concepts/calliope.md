# Concept: Calliope `[IMPLEMENTED]`

Calliope is Harmonia's foundational canonical data modeling, schema definition, and conversion library, responsible for establishing a uniform vocabulary and type-safe translations across all clinical integration domains.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Καλλιόπη* (Calliope)
- **Etymology**: Derived from *κάλλος* (kallos - beauty) and *ὄψ* (ops - voice), translating literally to "she of the beautiful voice."
- **Mythological Context**: In Greek mythology, Calliope is the eldest and most distinguished of the nine Olympian Muses (daughters of Zeus and Mnemosyne). She presided over eloquence, rhetorical beauty, and epic poetry (inspiring Homer's *Iliad* and *Odyssey*). In Hesiod's *Theogony*, she holds the highest rank among the Muses because she attends upon kings, granting them the gift of fair, persuasive, and authoritative speech that settles fierce disputes with gentle, clear words.
- **Architectural Rationale**: Healthcare integration is plagued by a "Tower of Babel" problem: PAS speaks HL7 v2.3, LMS speaks HL7 v2.5.1, modern EMRs speak FHIR R5 JSON, and legacy billing engines speak custom delimited files. Calliope is the "eloquent voice" that brings structural beauty, clarity, and canonical consensus to this disparate discourse, translating heterogeneous dialects into an authoritative, elegant common tongue.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Calliope serves as the foundational data contract library at the base of the Harmonia dependency hierarchy:

```
+---------------------------------------------------------------------------------------+
|                                  CALLIOPE FOUNDATION                                  |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +---------------------------------------+   +------------------------------------+  |
|   |         CANONICAL ENVELOPES           |   |       HL7 v2 <-> FHIR CONVERTERS   |  |
|   |                                       |   |                                    |  |
|   | * ErgonEvent                          |   | * Adt2FhirMapper (ADT -> Patient)  |  |
|   | * ErgonPayload                        |   | * Mfn2FhirBundle (MFN -> Master)   |  |
|   | * ErgonReasonEnum                     |   | * Orm2FhirMapper (ORM -> Order)    |  |
|   | * HarmoniaTopics                      |   | * Oru2FhirMapper (ORU -> Results)  |  |
|   +---------------------------------------+   +------------------------------------+  |
|                        |                                         |                    |
|                        +====================+====================+                    |
|                                             |                                         |
|                                             v                                         |
|                   +---------------------------------------------------+               |
|                   |           PURE JAVA 21 DOMAIN CONTRACTS           |               |
|                   |   - Zero dependencies on higher Harmonia layers   |               |
|                   |   - Consumed by Pylai, Energeia, Themis, Hestia   |               |
|                   +---------------------------------------------------+               |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Calliope Owns
- Canonical event envelopes and metadata structures (`ErgonEvent`, `ErgonPayload`, `ErgonReasonEnum`).
- Centralized Petasos topic and queue naming constants (`HarmoniaTopics`).
- Bi-directional conversion utilities between HL7 v2.x messages and FHIR R5 resources (`Adt2FhirMapper`, `Mfn2FhirBundle`, `Orm2FhirMapper`, `Oru2FhirMapper`).
- Shared domain models and DTOs utilized across multiple subprojects.

### What Calliope Explicitly Does NOT Own (Anti-Responsibilities)
- Network sockets, MLLP framing, or HTTP endpoints (owned by Pylai).
- Security policy evaluation or identity assertions (owned by Themis).
- Cache grid clustering or database connections (owned by Hestia).
- Task sequence orchestration or worker loops (owned by Energeia).
- Message broker connections (owned by Petasos).

---

## 4. Key Architectural Invariant: Pure Model Isolation `[IMPLEMENTED]`

Calliope sits at the absolute foundation of the codebase. It must remain pure and free from higher-level architectural dependencies:
- **Zero Inbound Harmonia Dependencies**: Calliope depends strictly on JDK 21, HAPI FHIR Structures R5 (`hapi-fhir-structures-r5`), Jackson Core/Databind, and Slf4j.
- It must **never** declare a dependency on Themis, Hestia, Petasos, Energeia, Pylai, Iris, Agora, or Paradeigma.

---

## 5. Key Classes & Converters `[IMPLEMENTED]`

| Class Name | Target Standard | Transformation Mapping | Status |
| :--- | :--- | :--- | :--- |
| `Adt2FhirMapper` | HL7 v2 ADT $\leftrightarrow$ FHIR R5 | Maps PID/PV1 segments into `Patient` and `Encounter` resources | `[IMPLEMENTED]` |
| `Mfn2FhirBundle` | HL7 v2 MFN $\leftrightarrow$ FHIR R5 | Maps Master File Notifications into `Practitioner` / `Organization` | `[IMPLEMENTED]` |
| `Orm2FhirMapper` | HL7 v2 ORM $\leftrightarrow$ FHIR R5 | Maps pharmacy/lab orders into `ServiceRequest` resources | `[IMPLEMENTED]` |
| `Oru2FhirMapper` | HL7 v2 ORU $\leftrightarrow$ FHIR R5 | Maps pathology/lab results into `DiagnosticReport` / `Observation` | `[IMPLEMENTED]` |
| `HarmoniaTopics` | Petasos Addressing | Constant definitions for platform queues and topics | `[IMPLEMENTED]` |
| `ErgonEvent` | Canonical Envelope | Standardized event container carrying payload, reason, and sender info | `[IMPLEMENTED]` |
