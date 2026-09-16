# Mnemonic Roles & Granular Authorities

## 1. Role vs. Authority Decoupling

Roles in Harmonia are mnemonic groupings of permissions assigned to principals; they are **not** evaluated directly by atomic policy logic. Themis policies evaluate **granular authorities**.

```
ROLE (e.g. PRV_RDR)
  ├── Authority: provider.read
  └── Authority: provider.search
```

---

## 2. Controlled Mnemonic Roles (`HarmoniaRoleEnum`)

| Role Code | Display Name | Assigned Granular Authorities |
| :--- | :--- | :--- |
| `PRV_RDR` | Provider Registry Reader | `provider.read`, `provider.search` |
| `PRV_SUB` | Provider Registry Submitter | `provider.change.submit` |
| `PRV_PROC` | Provider Registry Processor | `provider.change.process`, `provider.resource.create`, `provider.resource.update`, `provider.read` |
| `PRV_APR` | Provider Registry Approver | `provider.change.approve`, `provider.read`, `provider.search` |
| `PRV_ADM` | Provider Registry Administrator | `provider.admin`, `provider.read`, `provider.search`, `provider.change.submit`, `provider.change.process`, `provider.change.approve`, `provider.resource.create`, `provider.resource.update`, `provider.resource.delete` |
| `AUD_RDR` | Audit Reader | `audit.read` |
| `SYS_INT` | System Integration Service | `system.integration` |
| `SYS_ADM` | Platform System Administrator | `system.admin`, `audit.read`, `system.integration` |

---

## 3. Controlled Authority Vocabulary (`HarmoniaAuthorityEnum`)

```
provider.read              # Read Practitioner, Organization, Location, HealthcareService, Endpoint
provider.search            # Multi-parameter search on Provider Registry resources
provider.change.submit     # Submit asynchronous change requests (POST/PUT) at Pylai
provider.change.process    # Dispatch and execute Ergon change tasks in Ponos
provider.change.approve    # Approve pending governance tasks
provider.resource.create   # Direct / persistence creation in storage
provider.resource.update   # Direct / persistence update in storage
provider.resource.delete   # Soft-delete resource in storage
provider.admin             # Administrative override for Provider Registry domain
audit.read                 # Read Themis security decision audit records
system.integration         # Inter-service integration messaging authority
system.admin               # Platform infrastructure administration
```
