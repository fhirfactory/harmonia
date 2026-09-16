# Harmonia Paradeigma — Architecture & Design

### Architecture Principles
1. **Pylai receives and exposes interfaces**: All external ingress and egress traffic uses standard MLLP transport framing (`<VT>...<FS><CR>`) over TCP/IP with immediate HL7 ACK correlation (MSH-10/MSA).
2. **Petasos transports messages**: High-reliability message bus built on ActiveMQ Artemis and Infinispan, providing durable persistence, deduplication, and topic/queue delivery.
3. **Ponos performs the work**: Task execution engine routing tasks through `Praxis` workflows.
4. **Erga defines the work**: Modular, single-responsibility processing activities (`AdtDistributionErgon`, `OrmRoutingErgon`, `OruProcessingErgon`).
5. **Calliope defines shared canonical models**: Provides `Topic`, `ErgonPayload`, and `Pragma` domain structures.

---

### Sequence Diagram: Patient Journey Workflow

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
