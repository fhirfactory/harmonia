# Harmonia Paradeigma — Architecture & Design

### 1. Architectural Role & Isolation Principles

**Harmonia Paradeigma** acts strictly as a **simulation and test-support leaf module**. It exercises, tests, and observes public Harmonia production contracts without implementing production business logic or polluting production artifacts.

```mermaid
graph TD
    subgraph Paradeigma [Paradeigma Simulation & Test Layer - Leaf Module]
        Gen[Synthetic Data & Graph Generators]
        SecAct[Security Actor Fixtures]
        LogProbe[PhiLog Test Probes]
        ScenEng[Scenario Execution Engine]
    end

    subgraph ProductionHarmonia [Production Harmonia Contracts & Services]
        Pylai[Pylai Gateway & Security Interceptor]
        Themis[Themis Security Service & Evaluator]
        Erga[Ponos WorkEngine & Registry Ergons]
        Mneme[Mnemosyne Persistence & Storage]
        PhiLog[Harmonia PhiLogger & Dual-Gate]
    end

    Gen -->|Produces FHIR R5 Resources| Pylai
    SecAct -->|Provides ThemisSecurityContext| Pylai
    SecAct -->|Validates Against| Themis
    ScenEng -->|Invokes Change Pipeline| Pylai
    ScenEng -->|Executes Activity| Erga
    Erga -->|Persists Resources| Mneme
    Erga -->|Evaluates Execution Privileges| Themis
    Pylai -->|Emits Diagnostic Logs| PhiLog
    Erga -->|Emits Diagnostic Logs| PhiLog
    LogProbe -->|Observes & Asserts Events| PhiLog
```

#### Mandatory Architectural Invariants
1. **Allowed Dependency Direction**: `Paradeigma -> Production Harmonia` (Allowed). `Production Harmonia -> Paradeigma` (Forbidden).
2. **Zero Production Contamination**: No production Java class may import `net.fhirfactory.harmonia.paradeigma.*`.
3. **No Simulation Flags in Production**: Production business logic contains no `if (isSimulation)` or `if (paradeigmaMode)` branches.
4. **Failure Injection via Seams**: Negative and failure scenarios use explicit architectural seams (interfaces, test repository fakes, mock storage), never runtime flags.
5. **Build-Time Enforcement**: Enforced by ArchUnit (`ParadeigmaIsolationArchitectureTest`) and Maven POM dependency validation.

---

### 2. Governed Provider Registry Write Lifecycle

Every Provider Registry write (POST/PUT) is processed as an asynchronous **Request for Change** governed by Themis and Ponos:

```mermaid
sequenceDiagram
    autonumber
    participant Actor as Paradeigma (Provider Steward)
    participant Pylai as Pylai REST Gateway
    participant Themis as Themis Security
    participant Ponos as Ponos / Ergon
    participant Mneme as Mnemosyne Storage
    participant PhiLog as Harmonia PhiLogger

    Actor->>Pylai: POST /Practitioner (FHIR R5 JSON + Bearer Token)
    Pylai->>Themis: Authorize Request (ThemisSecurityContext, Action=CREATE)
    Themis-->>Pylai: ThemisDecision.ALLOW
    Pylai->>PhiLog: Diagnostic Ingress Trace (org.harmonia.phi + Marker PHI)
    Pylai-->>Actor: HTTP 202 Accepted (Location: /Task/{pragmaId}, X-Correlation-Id)

    Pylai->>Ponos: Dispatch Pragma (Status: RECEIVED -> VALIDATING)
    Ponos->>Themis: Authorize Async Execution (Submitter + Ergon Authorities)
    Themis-->>Ponos: ThemisDecision.ALLOW
    Ponos->>Ponos: Validate Identifiers (HPI-I) & References
    Ponos->>Ponos: Transition to APPROVED -> COMMITTING
    Ponos->>Mneme: Persist Practitioner & Increment Version (v1)
    Mneme-->>Ponos: Committed
    Ponos->>Ponos: Transition to COMPLETED (Store Result in Task.output)

    Actor->>Pylai: GET /Task/{pragmaId}
    Pylai-->>Actor: HTTP 200 OK (Task status=completed)
    Actor->>Pylai: GET /Practitioner/{id}
    Pylai-->>Actor: HTTP 200 OK (ETag: W/"1", Practitioner resource)
```

---

### 3. Patient Journey Clinical Workflow

```mermaid
sequenceDiagram
    autonumber
    participant PAS as PAS Simulator
    participant Harmonia as Harmonia HIE
    participant EMR as EMR Simulator
    participant LMS as LMS Simulator
    participant RIS as RIS-PAC Simulator

    Note over PAS,Harmonia: 1. Patient Registration & Admission
    PAS->>Harmonia: PD-01: ADT^A04 (Register)
    Harmonia-->>PAS: HL7 ACK (AA)
    Harmonia->>EMR: PD-05: ADT (Fan-out)
    Harmonia->>LMS: PD-06: ADT (Fan-out)
    Harmonia->>RIS: PD-07: ADT (Fan-out)

    PAS->>Harmonia: PD-01: ADT^A01 (Admit)
    Harmonia-->>PAS: HL7 ACK (AA)
    Harmonia->>EMR: PD-05: ADT (Fan-out)
    Harmonia->>LMS: PD-06: ADT (Fan-out)
    Harmonia->>RIS: PD-07: ADT (Fan-out)

    Note over EMR,LMS: 2. Pathology Laboratory Ordering & Results
    EMR->>Harmonia: PD-04: ORM^O01 (Lab: CBC)
    Harmonia-->>EMR: HL7 ACK (AA)
    Harmonia->>LMS: PD-08: ORM^O01 (Routed Lab Order)
    LMS-->>Harmonia: HL7 ACK (AA)
    LMS->>Harmonia: PD-02: ORU^R01 (Lab Result: Hb, WBC, Plt)
    Harmonia-->>LMS: HL7 ACK (AA)

    Note over EMR,RIS: 3. Diagnostic Imaging Ordering & Reporting
    EMR->>Harmonia: PD-04: ORM^O01 (Imaging: XR_CHEST)
    Harmonia-->>EMR: HL7 ACK (AA)
    Harmonia->>RIS: PD-09: ORM^O01 (Routed Imaging Order)
    RIS-->>Harmonia: HL7 ACK (AA)
    RIS->>Harmonia: PD-03: ORU^R01 (Imaging Report & Impression)
    Harmonia-->>RIS: HL7 ACK (AA)

    Note over PAS,Harmonia: 4. Transfer & Discharge
    PAS->>Harmonia: PD-01: ADT^A02 (Transfer)
    Harmonia-->>PAS: HL7 ACK (AA)
    Harmonia->>EMR: PD-05: ADT (Fan-out)
    Harmonia->>LMS: PD-06: ADT (Fan-out)
    Harmonia->>RIS: PD-07: ADT (Fan-out)

    PAS->>Harmonia: PD-01: ADT^A03 (Discharge)
    Harmonia-->>PAS: HL7 ACK (AA)
    Harmonia->>EMR: PD-05: ADT (Fan-out)
    Harmonia->>LMS: PD-06: ADT (Fan-out)
    Harmonia->>RIS: PD-07: ADT (Fan-out)
```
