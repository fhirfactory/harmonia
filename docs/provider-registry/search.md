# Harmonia Provider Registry — Search Capabilities

### 1. Synchronous Search Architecture

Search queries are executed synchronously directly against the authoritative persistence store (`hie_fhir_resources`) via `FhirStorageService`. They do not route through Ponos asynchronous task queues, ensuring high performance (sub-50ms) and immediate consistency.

### 2. Supported Search Parameters by Resource

#### `Practitioner`
- `_id`: Logical resource ID (e.g. `GET /Practitioner?_id=pract-100`).
- `name`: Substring match across `family`, `given`, `text`, or `prefix` (e.g. `GET /Practitioner?name=Bowman`).
- `identifier`: Matches system, value, or composite `system|value` (e.g. `GET /Practitioner?identifier=http://ns.electronichealth.net.au/id/hi/hpii/1.0|8003610000000001`).
- `active`: Boolean active status (`true` | `false`).

#### `PractitionerRole`
- `_id`: Logical resource ID.
- `identifier`: Business identifier.
- `practitioner`: Practitioner reference or ID (e.g. `GET /PractitionerRole?practitioner=pract-dr-bowman-01`).
- `organization`: Organization reference or ID (e.g. `GET /PractitionerRole?organization=org-stvincents-01`).
- `location`: Location reference or ID (e.g. `GET /PractitionerRole?location=loc-cardiology-wing-01`).
- `service`: HealthcareService reference or ID (e.g. `GET /PractitionerRole?service=svc-cardiology-consult-01`).
- `active`: Boolean active status (`true` | `false`).

#### `Organization`
- `_id`: Logical resource ID.
- `name`: Substring match on organization name or alias.
- `identifier`: Business identifier (HPI-O, ABN).
- `active`: Boolean active status (`true` | `false`).

#### `Location`
- `_id`: Logical resource ID.
- `name`: Substring match on location name or alias.
- `identifier`: Business identifier.
- `organization`: Managing organization reference or ID.
- `status`: Operational status code (`active`, `suspended`, `inactive`).

#### `HealthcareService`
- `_id`: Logical resource ID.
- `name`: Healthcare service name.
- `identifier`: Business identifier.
- `organization`: Providing organization reference or ID.
- `location`: Delivery location reference or ID.
- `active`: Boolean active status (`true` | `false`).

#### `Endpoint`
- `_id`: Logical resource ID.
- `name`: Technical endpoint name.
- `identifier`: Business identifier.
- `organization`: Managing organization reference or ID.
- `status`: Status code (`active`, `suspended`, `error`, `off`, `entered-in-error`, `test`).
- `connection-type`: Technical standard code or display (e.g. `hl7-fhir-rest`, `secure-messaging`).

#### `Group`
- `_id`: Logical resource ID.
- `name`: Group descriptive name.
- `identifier`: Business identifier.
- `type`: Entity type (`person`, `practitioner`, `organization`, `location`, `healthcareservice`).
- `actual`: Membership type (`true`/`enumerated` | `false`/`definitional`).

### 3. Response Format

Searches return a standard FHIR Release 5 `Bundle` with `type=searchset`:

```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 1,
  "entry": [
    {
      "fullUrl": "http://localhost:8080/fhir/Practitioner/pract-dr-bowman-01",
      "resource": {
        "resourceType": "Practitioner",
        "id": "pract-dr-bowman-01",
        "meta": {
          "versionId": "1",
          "lastUpdated": "2026-09-16T09:30:00Z"
        },
        "active": true,
        "name": [
          {
            "family": "Bowman",
            "given": ["Frank"],
            "prefix": ["Dr."]
          }
        ]
      },
      "search": {
        "mode": "match"
      }
    }
  ]
}
```
