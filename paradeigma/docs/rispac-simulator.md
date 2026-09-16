# Paradeigma — RIS-PAC Simulator (`paradeigma-rispac`)

### Overview
The RIS-PAC Simulator models a Radiology Information System and PACS archive that receives ADT messages on port `2203` (PD-07) and routed diagnostic imaging orders on port `2205` (PD-09), producing structured diagnostic imaging reports (`ORU^R01`) to Harmonia on port `2103` (PD-03).

---

### Interfaces
1. **PD-07 (RISPAC-ADT-OUT)**: Inbound MLLP on `2203` receiving distributed ADT demographics.
2. **PD-09 (RISPAC-ORM-OUT)**: Inbound MLLP on `2205` receiving routed `ORM^O01` radiology orders.
3. **PD-03 (RISPAC-ORU-IN)**: Outbound MLLP transmitting `ORU^R01` imaging reports to Harmonia (`2103`).

---

### Supported Imaging Studies & Reports
- **Chest Radiograph (`XR_CHEST`)**: Standard PA and Lateral views with clinical indications, findings, and impressions.
- **Brain CT (`CT_HEAD`)**: Non-contrast computed tomography brain study.
- **Abdomen CT (`CT_ABDOMEN`)**: Contrast-enhanced CT examination of abdomen and pelvis.
- **Brain MRI (`MRI_BRAIN`)**: Multi-sequence MR study of intracranial structures.
- **Abdomen Ultrasound (`US_ABDOMEN`)**: Complete real-time abdominal sonography.

---

### REST Management Endpoints
- `GET /api/rispac/status`: Returns metrics on received radiology orders and completed reports.
- `GET /api/rispac/config` / `POST /api/rispac/config`: Configures report generation delay.
- `POST /api/rispac/results/imaging`: Manually produces and sends an imaging report.
- `GET /api/rispac/orders`: Returns all currently received imaging orders.
