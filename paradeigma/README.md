# Harmonia Paradeigma — HL7 v2 Reference Solution

**Harmonia Paradeigma** is an end-to-end runnable exemplar demonstrating representative HL7 v2.4 clinical workflows across simulated healthcare systems interacting with the Harmonia Health Integration Environment (HIE).

---

## Simulated Healthcare Applications

1. **PAS (Patient Administration System)** (`paradeigma-pas` :8091)
   - Generates stateful ADT lifecycle events: `A04` (Register) -> `A01` (Admit) -> `A02` (Transfer) -> `A08` (Update) -> `A03` (Discharge).
   - Ingests into Harmonia on MLLP port `2101` (PD-01).

2. **EMR (Electronic Medical Record)** (`paradeigma-emr` :8092)
   - Ingests fan-out ADT messages on MLLP port `2201` (PD-05).
   - Places Laboratory and Diagnostic Imaging `ORM^O01` orders to Harmonia on MLLP port `2104` (PD-04).

3. **LMS (Laboratory Management System)** (`paradeigma-lms` :8093)
   - Ingests fan-out ADT messages on MLLP port `2202` (PD-06) and routed Lab ORM orders on MLLP port `2204` (PD-08).
   - Produces synthetic `ORU^R01` pathology results (Haemoglobin, WBC, Potassium, Sodium, etc.) to Harmonia on MLLP port `2102` (PD-02).

4. **RIS-PAC (Diagnostic Imaging & PACS)** (`paradeigma-rispac` :8094)
   - Ingests fan-out ADT messages on MLLP port `2203` (PD-07) and routed Diagnostic Imaging ORM orders on MLLP port `2205` (PD-09).
   - Produces synthetic `ORU^R01` radiology reports (Chest X-Ray, CT Head, MRI Brain) to Harmonia on MLLP port `2103` (PD-03).

5. **Scenario Conductor Engine** (`paradeigma-scenarios` :8090)
   - Orchestrates automated multi-system patient journeys via REST management APIs across `DEMO`, `TEST`, and `LOAD` execution profiles.

---

## Architecture Overview

```mermaid
graph TD
    subgraph Simulators ["Paradeigma Simulated Systems"]
        PAS[PAS Simulator :8091]
        EMR[EMR Simulator :8092 / :2201]
        LMS[LMS Simulator :8093 / :2202 / :2204]
        RIS[RIS-PAC Simulator :8094 / :2203 / :2205]
        SE[Scenario Engine :8090]
        SE -.->|REST Control| PAS
        SE -.->|REST Control| EMR
        SE -.->|REST Control| LMS
        SE -.->|REST Control| RIS
    end

    subgraph Harmonia ["Harmonia Platform (HIE)"]
        subgraph PylaiIn ["Pylai Inbound Gateways"]
            P_ADT_IN[PAS ADT Inbound :2101]
            P_ORM_IN[EMR ORM Inbound :2104]
            P_LMS_IN[LMS ORU Inbound :2102]
            P_RIS_IN[RIS ORU Inbound :2103]
        end

        subgraph PonosEngine ["Ponos WorkEngine & Erga Pipelines"]
            ADT_SEQ[AdtDistributionErgon]
            ORM_SEQ[OrmRoutingErgon]
            ORU_SEQ[OruProcessingErgon]
        end

        subgraph PetasosBus ["Petasos Messaging Queues"]
            Q_EMR_ADT[(petasos.queue.mllp.outbound.emr_adt)]
            Q_LMS_ADT[(petasos.queue.mllp.outbound.lms_adt)]
            Q_RIS_ADT[(petasos.queue.mllp.outbound.ris_adt)]
            Q_LMS_ORM[(petasos.queue.mllp.outbound.lms_orm)]
            Q_RIS_ORM[(petasos.queue.mllp.outbound.ris_orm)]
        end
    end

    PAS -->|PD-01: ADT| P_ADT_IN
    EMR -->|PD-04: ORM| P_ORM_IN
    LMS -->|PD-02: ORU| P_LMS_IN
    RIS -->|PD-03: ORU| P_RIS_IN

    P_ADT_IN --> ADT_SEQ
    P_ORM_IN --> ORM_SEQ
    P_LMS_IN --> ORU_SEQ
    P_RIS_IN --> ORU_SEQ

    ADT_SEQ --> Q_EMR_ADT
    ADT_SEQ --> Q_LMS_ADT
    ADT_SEQ --> Q_RIS_ADT

    ORM_SEQ -->|OBR-4 Lab| Q_LMS_ORM
    ORM_SEQ -->|OBR-4 Rad| Q_RIS_ORM

    Q_EMR_ADT -->|PD-05: ADT| EMR
    Q_LMS_ADT -->|PD-06: ADT| LMS
    Q_RIS_ADT -->|PD-07: ADT| RIS
    Q_LMS_ORM -->|PD-08: ORM| LMS
    Q_RIS_ORM -->|PD-09: ORM| RIS
```

---

## Documentation Index

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
