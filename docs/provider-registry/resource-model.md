# Harmonia Provider Registry — Resource Model

### 1. Scope of Managed Resources

The Harmonia Provider Registry implements the HL7 FHIR Release 5 (R5) standard baseline, managing seven core provider directory resources:

| Resource Type | Description / Healthcare Role | Key Identifiers / Profiles |
| :--- | :--- | :--- |
| `Practitioner` | Individual healthcare professional providing clinical care (e.g., Doctor, Nurse, Specialist). | Australian HPI-I (`http://ns.electronichealth.net.au/id/hi/hpii/1.0`), AHPRA, NPI. |
| `PractitionerRole` | The distinct role, location, duties, and specialty performed by a Practitioner for an Organization. | Organization link, Practitioner link, HealthcareService link, Endpoint link. |
| `Organization` | Formal healthcare organization, hospital trust, clinic, or administrative legal entity. | Australian HPI-O (`http://ns.electronichealth.net.au/id/hi/hpio/1.0`), ABN. |
| `Location` | Physical or logical healthcare delivery location (ward, consultation room, hospital building). | Managing Organization reference, operational status, coordinates. |
| `HealthcareService` | Specific clinical service offered through an organization and/or location (e.g., Cardiology, Pathology). | Providing Organization reference, Location references, Endpoint references. |
| `Endpoint` | First-class electronic service destination for interoperability (FHIR REST, secure messaging, HL7 MLLP). | Connection type, URL/address standard, payload types, Managing Organization. |
| `Group` | Logical grouping of providers, practitioners, or directory entities for specialized panels or teams. | Entity type, definitional/enumerated membership, managing entity. |

### 2. Relationship Model & Graph Topology

The Provider Registry maintains relational links between core directory entities strictly according to FHIR R5 reference conventions:

```
                    Practitioner
                         │
                         ▼
                  PractitionerRole
                   /      │       \
                  /       │        \
                 ▼        ▼         ▼
          Organization  Location  HealthcareService
                │          │             │
                └──────────┼─────────────┘
                           │
                           ▼
                        Endpoint


                          Group
                            │
                            ▼
                     Registry Entities
```

### 3. First-Class Resource Status for Endpoint and Group

- **`Endpoint` as an Independent Resource**: Electronic service addresses are persisted and managed as distinct FHIR `Endpoint` resources rather than flattened strings. Resources such as `Organization`, `Location`, `HealthcareService`, and `PractitionerRole` reference `Endpoint` instances to express secure electronic communication channels.
- **`Group` as an Independent Entity**: `Group` represents clinical or operational groupings of directory resources. It is independent of system IAM or security group constructs, supporting both enumerated rosters (`Group.member.entity`) and definitional criteria.
