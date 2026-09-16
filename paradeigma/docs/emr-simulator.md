# Paradeigma — EMR Simulator (`paradeigma-emr`)

### Overview
The EMR Simulator models an Electronic Medical Record system that consumes distributed ADT messages from Harmonia on MLLP port `2201` (PD-05) and generates laboratory and diagnostic imaging `ORM^O01` clinical orders to Harmonia on MLLP port `2104` (PD-04).

---

### Inbound Interface (PD-05)
- **Port**: `2201`
- **Protocol**: MLLP / TCP
- **Payload**: `ADT^A01`, `A02`, `A03`, `A04`, `A08`
- **Behavior**: Ingests patient demographics, updates local patient registry, and returns `ACK^AA`.

---

### Outbound Interface (PD-04)
- **Destination**: Harmonia Pylai Inbound (`2104`)
- **Payload**: `ORM^O01`
- **Supported Order Types**:
  - Pathology: `CBC` (Full Blood Count), `ELEC` (Electrolytes), `LFT` (Liver Function), `GLU` (Glucose).
  - Radiology: `XR_CHEST` (Chest X-Ray), `CT_HEAD` (CT Brain), `MRI_BRAIN` (MRI Brain), `US_ABDOMEN` (Ultrasound Abdomen).

---

### REST Management Endpoints
- `GET /api/emr/status`: Returns current message counts and connection status.
- `GET /api/emr/config` / `POST /api/emr/config`: Updates autonomous background ordering timer.
- `POST /api/emr/orders/laboratory`: Manually triggers a pathology order.
- `POST /api/emr/orders/imaging`: Manually triggers a diagnostic imaging order.
- `GET /api/emr/patients`: Returns all patients currently known to the EMR.
