# Harmonia Paradeigma — HL7 Event Specifications

### Supported Trigger Events

#### 1. ADT Events (PAS Simulator)
- **`ADT^A04` (Register Patient)**: Initializes synthetic patient demographics (PID-3, PID-5, PID-7, PID-8, PID-11, PID-13) and preliminary encounter details (PV1-2, PV1-3, PV1-19).
- **`ADT^A01` (Admit / Visit Notification)**: Formally assigns inpatient location (PV1-3 Point of Care, Room, Bed, Facility), admit timestamp (PV1-44), and attending clinician (PV1-7).
- **`ADT^A02` (Transfer Patient)**: Updates current patient location (PV1-3) while maintaining prior location history in PV1-6 and updating transfer timestamps.
- **`ADT^A08` (Update Patient Information)**: Modifies patient demographic elements (such as updated phone numbers or addresses) without changing admission status.
- **`ADT^A03` (Discharge / End Visit)**: Sets encounter discharge timestamp (PV1-45) and discharge disposition (PV1-36).
- **`ADT^A11` / `A12` / `A13`**: Handles cancellation of admit, transfer, and discharge operations.

#### 2. ORM Events (EMR Simulator)
- **`ORM^O01` (General Clinical Order)**:
  - **Common Order Segment (`ORC`)**: Control code (ORC-1 `NW`), Placer Order Number (ORC-2), Order Status (ORC-5 `IP`), Priority (ORC-7.6 `R`/`S`), Order DateTime (ORC-9), Ordering Doctor (ORC-12).
  - **Observation Request Segment (`OBR`)**: Placer Order Number (OBR-2), Universal Service Identifier (OBR-4.1 Code, OBR-4.2 Description, OBR-4.3 System), Observation DateTime (OBR-7).
  - **Deterministic Routing Classification**:
    - **Laboratory**: `CBC`, `ELEC`, `LFT`, `GLU`, `CRP`, `COAG`, `TROP`, `LIPID` -> Routed to LMS (:2204).
    - **Diagnostic Imaging**: `XR_CHEST`, `CT_HEAD`, `CT_ABDOMEN`, `MRI_BRAIN`, `US_ABDOMEN`, `XR_KNEE` -> Routed to RIS-PAC (:2205).

#### 3. ORU Events (LMS & RIS-PAC Simulators)
- **`ORU^R01` (Unsolicited Observation Result)**:
  - **`OBR`**: Correlated Placer Order Number (OBR-2), Filler Order Number (OBR-3), Universal Service Identifier (OBR-4), Result Status (OBR-25 `F`).
  - **`OBX` (Observation Segment)**: Set ID (OBX-1), Value Type (OBX-2 `NM`/`TX`), Observation ID & Name (OBX-3), Observation Value (OBX-5), Units (OBX-6), Reference Range (OBX-7), Abnormality Flags (OBX-8 `N`/`H`/`L`), Status (OBX-11 `F`).
