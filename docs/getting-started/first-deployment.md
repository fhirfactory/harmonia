# Getting Started: First Deployment & Verification Runbook

This guide walks you through deploying a complete Harmonia cluster locally using Docker Compose, verifying subsystem health endpoints, and validating end-to-end clinical message processing through an HL7 ADT^A01 admission event.

---

## 1. Local Environment Deployment `[CONFIGURED]`

Harmonia provides a multi-container Docker Compose topology in `docker-compose.yml` that provisions all five tiers:
- **Relational Databases (PostgreSQL 16)**: `postgres-1` (port 5432), `postgres-2` (port 5433), `postgres-ops-1` (port 5434), `postgres-ops-2` (port 5435).
- **Relational Persistence Servers (HAPI FHIR R5 JPA & Operations)**: `hapi-fhir-1` (port 8081), `hapi-fhir-2` (port 8082), `hie-operations-1` (port 8085), `hie-operations-2` (port 8086).
- **In-Memory Cache Grid (Infinispan 15.0.3)**: `infinispan-1` (ports 11222, 7800), `infinispan-2` (ports 11223, 7801).
- **Workflow Task Processor & Artemis Broker (Energeia / Petasos)**: `task-processor` (ports 8083, 61616).
- **Perimeter Gateways (Pylai)**: `mllp-gateway` (ports 2575, 8084), `mllp-outbound-his` (port 8087), `mllp-outbound-lis` (port 8088).
- **Presentation Tier (Iris)**: `befe` (ports 8080, 8090, 9990), `iris-clinical` (port 3000), `iris-console` (port 3001).

### Step 1: Start the Cluster
```bash
docker compose up -d
```

### Step 2: Monitor Startup Sequence
Harmonia enforces strict dependency healthchecks. Wait until all containers report healthy:
```bash
docker compose ps
```

---

## 2. Health & Endpoint Verification `[IMPLEMENTED]`

Verify that core subsystem listeners are operational:

| Subsystem | Target URL / Port | Expected Response | Description | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Inbound MLLP Gateway** | `localhost:2575` (TCP) | Connected / Open Socket | Pylai Netty MLLP receiver | `[IMPLEMENTED]` |
| **Mnemosyne FHIR API** | `http://localhost:8081/fhir/metadata` | HTTP 200 (CapabilityStatement JSON) | HAPI FHIR R5 JPA server | `[IMPLEMENTED]` |
| **Mnemosyne Operations** | `http://localhost:8085/actuator/health` | HTTP 200 (`{"status":"UP"}`) | Operations JPA service | `[IMPLEMENTED]` |
| **Iris BEFE (Clinical)** | `http://localhost:8080/api/fhir/Patient` | HTTP 200 (FHIR Bundle JSON) | BEFE clinical REST gateway | `[IMPLEMENTED]` |
| **Iris BEFE (Operations)**| `http://localhost:8090/api/operations/system/status` | HTTP 200 (Topology JSON) | BEFE operational REST gateway | `[IMPLEMENTED]` |
| **Iris Clinical UI** | `http://localhost:3000/` | HTTP 200 (HTML Web App) | Vue 3 Clinical Explorer | `[IMPLEMENTED]` |
| **Iris Console UI** | `http://localhost:3001/` | HTTP 200 (HTML Web App) | Vue 3 Operations Dashboard | `[IMPLEMENTED]` |
| **Mneme Cache Grid** | `http://localhost:11222/rest/v2/caches` | HTTP 200 (Infinispan REST) | Distributed cache grid | `[IMPLEMENTED]` |
| **Petasos Artemis Broker**| `tcp://localhost:61616` | Active JMS OpenWire / CORE | Artemis message broker | `[IMPLEMENTED]` |

---

## 3. End-to-End Clinical Event Walkthrough `[EXAMPLE/REFERENCE]`

To verify that clinical events flow through the entire system without loss, submit a synthetic HL7 v2.4 ADT^A01 (Inpatient Admission) message.

### Step 1: Construct the ADT^A01 Payload
Create a file named `sample-admission.hl7` containing:

```hl7
MSH|^~\&|PAS_HOSPITAL|NORTH_FACILITY|HARMONIA_HIE|ENTERPRISE|20260917120000||ADT^A01|MSG202609170001|P|2.4
EVN|A01|20260917120000
PID|1||PAT987654321^^^MRN||DOE^JOHN^A||19800512|M|||123 HEALING WAY^^BRISBANE^QLD^4000
PV1|1|I|WARD-4B^BED-02|E|||DOC001^SMITH^EMILY^^^DR|||||||||ADM100987|||||||||||||||||||||||||20260917114500
```

### Step 2: Send Message via MLLP (Port 2575)
Using `pylai-mllp-cli` or a standard MLLP wrapper script:

```bash
# Using netcat with MLLP byte framing (\x0b at start, \x1c\x0d at end):
printf "\x0b$(cat sample-admission.hl7)\x1c\x0d" | nc -w 3 localhost 2575
```

### Step 3: Verify the Synchronous MLLP Acknowledgment
You will receive an MLLP-framed HL7 `ACK^A01` with acknowledgment code `AA` (Application Accept):

```hl7
MSH|^~\&|HARMONIA_HIE|ENTERPRISE|PAS_HOSPITAL|NORTH_FACILITY|20260917120001||ACK^A01|ACK202609170001|P|2.4
MSA|AA|MSG202609170001
```

> **Ingress Invariant (REC-001)**: The `AA` acknowledgment confirms that the message was accepted, validated, registered in the `task-cache` in Mneme, and published to `petasos.queue.task.inbound`.

---

## 4. Verifying Downstream Processing `[IMPLEMENTED]`

1. **Verify Task Processing in Ponos**:
   Inspect the container logs for `hie-task-processor`:
   ```bash
   docker logs hie-task-processor | grep -i "AdtDistributionErgon"
   ```
   You will observe `AdtDistributionErgon` executing the task, transforming the HL7 payload into a FHIR R5 `Patient` and `Encounter`, and updating the `Pragma` checkpoint.

2. **Verify FHIR Clinical Record in Mnemosyne**:
   Query the HAPI FHIR JPA server for the created patient:
   ```bash
   curl -s http://localhost:8081/fhir/Patient?identifier=PAT987654321 | jq .
   ```
   The response contains a FHIR R5 `Bundle` with the ingested patient demographics.

3. **Verify Egress Fan-Out Dispatches**:
   Check outbound gateway logs (`hie-mllp-outbound-his` and `hie-mllp-outbound-lis`):
   ```bash
   docker logs hie-mllp-outbound-his | grep -i "Dispatching"
   ```
   The outbound gateways consume from their respective queues (`petasos.queue.mllp.outbound.HIS_NORTH`) and track delivery status in `Task.output` (REC-002).

---

## 5. Teardown `[CONFIGURED]`

To shut down all containers and clean up ephemeral volumes:
```bash
docker compose down -v
```
