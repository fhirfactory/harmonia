# Paradeigma — LMS Simulator (`paradeigma-lms`)

### Overview
The LMS Simulator models a Laboratory Information System that receives patient ADT events on port `2202` (PD-06) and routed pathology orders on port `2204` (PD-08), producing synthetic `ORU^R01` laboratory results to Harmonia on port `2102` (PD-02).

---

### Interfaces
1. **PD-06 (LMS-ADT-OUT)**: Inbound MLLP on `2202` receiving distributed ADT demographics.
2. **PD-08 (LMS-ORM-OUT)**: Inbound MLLP on `2204` receiving routed `ORM^O01` pathology orders.
3. **PD-02 (LMS-ORU-IN)**: Outbound MLLP transmitting `ORU^R01` results to Harmonia (`2102`).

---

### Representative Laboratory Panels & LOINC Codes
- **Full Blood Count (`CBC`)**: Haemoglobin (`718-7`), White Blood Cell Count (`6690-2`), Platelets (`777-3`), Haematocrit (`4544-3`).
- **Electrolytes & Renal Panel (`ELEC`)**: Sodium (`2951-2`), Potassium (`2823-3`), Creatinine (`2160-0`), Urea (`3094-0`), eGFR (`33914-3`).
- **Liver Function Panel (`LFT`)**: ALT (`1742-6`), AST (`1920-8`), Total Bilirubin (`1975-2`), ALP (`6768-6`).
- **Blood Glucose (`GLU`)**: Fasting Glucose (`2345-7`).

---

### REST Management Endpoints
- `GET /api/lms/status`: Returns received orders count and transmitted results count.
- `GET /api/lms/config` / `POST /api/lms/config`: Configures result generation delay.
- `POST /api/lms/results/laboratory`: Manually generates and sends a lab result for an order.
- `GET /api/lms/orders`: Returns all currently received lab orders.
