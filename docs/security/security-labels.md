# Data Security Labels & FHIR Mapping

## 1. Concept

Data **Security Labels** describe the security characteristics and handling classification of data entities. They do **not** describe actor permissions directly; Themis policies evaluate the relationship between an actor's authorities and the data's security labels.

---

## 2. Controlled Security Labels (`HarmoniaSecurityLabelEnum`)

Code System URI: `http://harmonia.net/security-label`

| Label Code | Display | Description |
| :--- | :--- | :--- |
| `PROVIDER_REGISTRY` | Provider Registry Data | Healthcare provider, practitioner, and organization directory data |
| `INTERNAL` | Internal Harmonia Data | Internal system coordination, queue envelopes, and intermediate workflow state |
| `CLINICAL` | Clinical Data | Protected patient health information, diagnostics, and observations |
| `DIAGNOSTIC` | Diagnostic Data | Laboratory, pathology, and radiology imaging diagnostic reports |
| `ADMINISTRATIVE` | Administrative Data | Financial, scheduling, and billing records |
| `AUDIT` | Security Audit Record | Non-sensitive authorization logs, decision trails, and correlation events |
| `RESTRICTED` | Restricted Sensitivity | Highly sensitive data requiring explicit elevated authorizations |

---

## 3. Bi-Directional FHIR Mapping (`FhirSecurityTagManager`)

For FHIR R5 resources, Harmonia security labels are serialized to and from `Resource.meta.security`:

```json
{
  "resourceType": "Practitioner",
  "id": "PR-100",
  "meta": {
    "versionId": "1",
    "lastUpdated": "2026-09-16T11:30:00Z",
    "security": [
      {
        "system": "http://harmonia.net/security-label",
        "code": "PROVIDER_REGISTRY",
        "display": "Provider Registry Data"
      },
      {
        "system": "http://harmonia.net/security-label",
        "code": "INTERNAL",
        "display": "Internal Harmonia Data"
      }
    ]
  }
}
```
