# Paradeigma — PAS Simulator (`paradeigma-pas`)

### Overview
The PAS Simulator models a hospital Patient Administration System generating synthetic, stateful ADT lifecycle messages transmitted to Harmonia Pylai Inbound over MLLP on port `2101` (PD-01).

---

### Key Capabilities
1. **Stateful Clinical Sequences**: Maintains patient states (Registered -> Admitted -> Transferred -> Updated -> Discharged).
2. **Deterministic Data Generation**: Uses seed-controlled pseudo-random generators to produce consistent names, dates of birth, MRNs, and encounter numbers.
3. **Autonomous Timer Modes**:
   - `FIXED`: Sends messages at constant intervals (e.g., every 5 seconds).
   - `RANDOM_RANGE`: Sends messages at variable intervals between configurable minimum and maximum delays (e.g., 2–10 seconds).
4. **REST Control API**: Exposes endpoints for inspecting status, updating configuration, and triggering manual lifecycle transitions.

---

### REST Management Endpoints
- `GET /api/pas/status`: Returns runtime metrics (messages sent, ACK statuses, active port).
- `GET /api/pas/config` / `POST /api/pas/config`: Inspects or updates timer settings and failure injection parameters.
- `POST /api/pas/patients/register`: Triggers `ADT^A04` registration.
- `POST /api/pas/patients/{id}/admit`: Triggers `ADT^A01` admission.
- `POST /api/pas/patients/{id}/transfer`: Triggers `ADT^A02` location transfer.
- `POST /api/pas/patients/{id}/update`: Triggers `ADT^A08` demographic update.
- `POST /api/pas/patients/{id}/discharge`: Triggers `ADT^A03` discharge.
- `POST /api/pas/trigger-next`: Automatically selects and triggers the next logical lifecycle event.
