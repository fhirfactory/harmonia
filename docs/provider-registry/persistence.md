# Harmonia Provider Registry — Persistence & Database Mapping

### 1. Hybrid Relational-Document Architecture

Authoritative Provider Registry persistence is provided by `Mnemosyne` (`hestia/mnemosyne-clinical`) using PostgreSQL (production) or H2 (testing/embedded).

The model utilizes a **hybrid relational-document pattern**:
- Relational metadata columns are indexed for fast lookup, filtering, and optimistic concurrency checks.
- Complete, lossless FHIR Release 5 JSON structures are stored in the `resource_json` LOB column.

### 2. Database Schema: `hie_fhir_resources`

```sql
CREATE TABLE hie_fhir_resources (
    id BIGSERIAL PRIMARY KEY,
    resource_type VARCHAR(64) NOT NULL,
    fhir_id VARCHAR(128) NOT NULL,
    version_id BIGINT NOT NULL DEFAULT 1,
    resource_json TEXT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- Indexes & Constraints
CREATE UNIQUE INDEX uk_resource_type_fhir_id ON hie_fhir_resources (resource_type, fhir_id);
CREATE INDEX idx_resource_type_fhir_id ON hie_fhir_resources (resource_type, fhir_id);
CREATE INDEX idx_resource_type_deleted ON hie_fhir_resources (resource_type, is_deleted);
```

### 3. Entity Column Mapping

| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | `BIGSERIAL` | Internal relational surrogate primary key. |
| `resource_type` | `VARCHAR(64)` | FHIR resource name (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`, `Task`). |
| `fhir_id` | `VARCHAR(128)` | Business/logical FHIR identifier (e.g. `pract-dr-bowman-01`, `org-stvincents-01`). |
| `version_id` | `BIGINT` | Monotonically increasing version counter for optimistic concurrency and history. |
| `resource_json` | `TEXT` | Complete FHIR R5 JSON representation (including meta, names, identifiers, extensions, and security tags). |
| `is_deleted` | `BOOLEAN` | Relational retirement flag enabling FHIR `410 Gone` lifecycle semantics and audit retention (ADR-020). |
| `last_updated` | `TIMESTAMP` | Timestamp of latest mutation. Synchronized with FHIR `Meta.lastUpdated`. |

### 4. Versioning & Optimistic Concurrency Control

- **Version Incrementing**: Every update operation increments `version_id` by 1 and updates `last_updated`. The resulting FHIR resource carries `Meta.versionId` and `Meta.lastUpdated` reflecting the database record.
- **ETag & If-Match**:
  - Direct read (`GET /{resourceType}/{id}`) returns `ETag: W/"{version_id}"`.
  - Update requests (`PUT /{resourceType}/{id}`) accept `If-Match: W/"{version_id}"`.
  - If the requested baseline version does not match the stored `version_id`, an optimistic lock conflict is thrown (`PreconditionFailedException`), causing the change Pragma to transition to `FAILED` with `PR-VAL-006`.
- **Lifecycle Transitions (ADR-020)**: Physical deletion is not supported. Deactivation or retirement of provider directory records is executed as an authoritative `UPDATE` modifying status/active fields, preserving audit history and referential relationships.
