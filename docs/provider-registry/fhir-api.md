# Harmonia Provider Registry — FHIR API Specification

### 1. Interaction Matrix

The Provider Registry exposes standard FHIR Release 5 REST endpoints:

| Endpoint | Method | FHIR Interaction | Processing Mode | Response Code | Headers / Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `/{resourceType}/{id}` | `GET` | `read` | Synchronous | `200 OK` | Returns full resource with `ETag: W/"{version}"`. `404 Not Found` if missing, `410 Gone` if retired/inactive (ADR-020). |
| `/{resourceType}` | `GET` | `search-type` | Synchronous | `200 OK` | Returns FHIR `Bundle` (`type=searchset`, `total=N`). Multi-parameter filtering supported. |
| `/{resourceType}` | `POST` | `create` | Asynchronous Governed | `202 Accepted` | `Location: /Task/{pragmaId}`, `X-Correlation-Id: {id}`, `Retry-After: 1`. Returns created `Task` payload. |
| `/{resourceType}/{id}` | `PUT` | `update` | Asynchronous Governed | `202 Accepted` | Supports `If-Match: W/"{version}"`. Returns `Location: /Task/{pragmaId}`. |
| `/Task/{id}` | `GET` | `read` | Synchronous | `200 OK` | Polls change execution state. Returns `Task` reflecting `PragmaStatus` and `OperationOutcome` on failure. |
| `/metadata` | `GET` | `capabilities` | Synchronous | `200 OK` | Returns FHIR R5 `CapabilityStatement` listing supported resources, search params, and security requirements. |

*Note: In accordance with ADR-020, physical DELETE endpoints are not exposed. Deactivation or retirement of directory entries is executed as an authoritative UPDATE via `PUT`, modifying the resource's lifecycle status (e.g., `active=false`).*

### 2. Supported Resources

The REST matrix applies across all seven core Provider Registry resource types:
- `/Practitioner`
- `/PractitionerRole`
- `/Organization`
- `/Location`
- `/HealthcareService`
- `/Endpoint`
- `/Group`

### 3. Asynchronous Response Model & Polling

POST and PUT requests do not mutate authoritative directory state synchronously. Upon successful request-level parsing and ingestion into Petasos messaging, the server responds immediately with `HTTP 202 Accepted`:

```http
HTTP/1.1 202 Accepted
Content-Type: application/fhir+json;charset=utf-8
Location: /Task/666c05a1-7786-4e59-a292-0b1a0305a4ec
X-Correlation-Id: corr-practitioner-create-001
Retry-After: 1

{
  "resourceType": "Task",
  "id": "666c05a1-7786-4e59-a292-0b1a0305a4ec",
  "status": "accepted",
  "intent": "order",
  "authoredOn": "2026-09-16T09:30:00Z"
}
```

Clients poll `GET /Task/{id}` until the task transitions to `completed`, `rejected`, or `failed`:
- **Success (`status = completed`)**: `Task.output` includes the resulting resource reference and version metadata.
- **Validation Rejection (`status = rejected`)**: `Task.output` embeds an `OperationOutcome` containing error codes (`PR-VAL-001` through `PR-VAL-010`) and diagnostic details.
- **Concurrency Conflict (`status = failed`)**: `Task.output` contains a concurrency conflict `OperationOutcome` indicating stale `If-Match` versioning.
