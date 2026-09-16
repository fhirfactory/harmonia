# Harmonia Provider Registry — Validation & Business Rules

### 1. Separation of Concerns

Harmonia enforces a strict separation between interface-level gateway validation and domain-level business validation:

```
                  REQUEST SUBMISSION
                          │
                          ▼
             [ Interface Validation (Pylai) ]
             - HTTP structure, size limits, headers
             - HAPI FHIR JSON parsing & type check
             - Supported resource check (PR-VAL-009)
             - Security & RBAC evaluation
                          │
                          ▼
            [ Domain Validation (Ponos / Erga) ]
             - Structural mandatory field rules
             - Business identifier uniqueness & duplicate checks
             - Referential integrity across directory graph
             - Concurrency & ETag conflict detection
```

### 2. Validation Codes & Error Taxonomy

| Code | Issue Description | Typical Trigger / Rule |
| :--- | :--- | :--- |
| `PR-VAL-001` | Structural validation error | Payload is malformed or not a valid FHIR R5 resource representation. |
| `PR-VAL-002` | Missing mandatory field | Missing required attribute (e.g. Practitioner without name/identifier, Group without type). |
| `PR-VAL-003` | Duplicate business identifier | Attempting to assign an existing HPI-I/HPI-O identifier to a different practitioner/organization. |
| `PR-VAL-004` | Referenced target does not exist | PractitionerRole, Location, HealthcareService, or Group referencing a non-existent entity. |
| `PR-VAL-005` | Referenced target is inactive / deleted | Reference points to a soft-deleted or inactive directory entity. |
| `PR-VAL-006` | Optimistic concurrency conflict | Version mismatch on `PUT` with `If-Match` against current `version_id`. |
| `PR-VAL-007` | Invalid endpoint configuration | Endpoint missing required `connectionType` or valid URI `address`. |
| `PR-VAL-008` | Invalid group definition | Group missing required `type` or membership definition. |
| `PR-VAL-009` | Unsupported resource type | Submitting an unmanaged resource type (e.g. `Patient`) to Provider Registry API. |
| `PR-VAL-010` | General business rule failure | Persistence exception or unhandled business validation constraint failure. |

### 3. Referential Integrity Rules by Resource

- **`PractitionerRole`**:
  - `practitioner` reference must exist in `hie_fhir_resources` with `is_deleted = false`.
  - `organization` reference must exist and be active.
  - `location`, `healthcareService`, and `endpoint` references must exist and be active if specified.
- **`Location`**:
  - `managingOrganization` reference must exist and be active.
  - `partOf` parent Location reference must exist and be active.
  - `endpoint` references must exist and be active.
- **`HealthcareService`**:
  - `providedBy` organization reference must exist and be active.
  - `location` and `endpoint` references must exist and be active.
- **`Endpoint`**:
  - `managingOrganization` reference must exist and be active if specified.
- **`Group`**:
  - `managingEntity` reference must exist if specified.
  - `member.entity` references must exist and be active.
