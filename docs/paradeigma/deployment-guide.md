# Paradeigma Deployment & Execution Guide `[CONFIGURED]`

> **Notice**: For the full deployment guide and container inventory, see **[Paradeigma Deployment & Operations Guide](deployment.md)** and the **[Normal vs Paradeigma Deployment Register](normal-vs-paradeigma.md)**.

---

## 1. Running Simulation via Docker Compose

The simplest way to execute synthetic clinical simulations is using Docker Compose profiles:

```bash
# Launch core Harmonia services plus Paradeigma simulator containers
docker compose --profile simulation up -d --build

# Verify all services and simulators are active
docker compose ps
```

Active simulator containers:
- `hie-paradeigma-pas`
- `hie-paradeigma-emr`
- `hie-paradeigma-lms`
- `hie-paradeigma-rispac`
- `hie-paradeigma-scenarios`

---

## 2. Triggering Synthetic Clinical Scenarios

You can trigger synthetic scenarios via HTTP REST or CLI:

### 2.1 Standard Inpatient Scenario (Emergency Admission $\rightarrow$ Lab Order $\rightarrow$ Discharge)
```bash
curl -X POST http://localhost:8080/scenarios/inpatient-admission \
  -H "Content-Type: application/json" \
  -d '{"patientCount": 10, "includeLabOrders": true}'
```

### 2.2 Provider Registry Master File Sync
```bash
curl -X POST http://localhost:8080/scenarios/provider-sync \
  -H "Content-Type: application/json" \
  -d '{"practitionerCount": 25, "updateFrequencySec": 5}'
```

---

## 3. Teardown Simulation
```bash
docker compose --profile simulation down
```
