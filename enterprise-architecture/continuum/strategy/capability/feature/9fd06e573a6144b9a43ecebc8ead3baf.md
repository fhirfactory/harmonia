---
{
  "schemaVersion": 1,
  "canonicalId": "9fd06e573a6144b9a43ecebc8ead3baf",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Attendance State Accuracy",
  "friendlyName": "HealthServiceProvision.ServiceAccessAdministration.AppointmentAndAttendanceAdministration.AttendanceStateAccuracy",
  "canonicalName": "CT1-HealthServiceProvision.CT2-ServiceAccessAdministration.CT3-AppointmentAndAttendanceAdministration.FEAT-AttendanceStateAccuracy",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B07.05.02.F02",
    "allocation": "UUIDv4 allocated once by explicit create",
    "predecessorCanonicalIds": [
      "03e185a6e0d145c3975b3f4715c94207",
      "5852c7d4c9ac4bbf99e7df008d0ed770"
    ]
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B07",
        "type": "capability",
        "name": "Health Service Provision",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B07.05",
        "type": "capability",
        "name": "Service Access Administration",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B07.05.02",
        "type": "capability",
        "name": "Appointment and Attendance Administration",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B07.05.02.F02",
        "type": "feature",
        "name": "Attendance State Accuracy",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B07.05.02.F02",
    "parent": "B07.05.02",
    "tier": "Feature",
    "sourceName": "Attendance State Accuracy",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B07.05.02.F02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/627",
      "role": "Exact source contributions, original wording/criteria/status and accepted adjudication correspondence; historical assurance evidence",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
      "selector": "Authorised Activity / Scope / B",
      "role": "Explicit canonical incorporation and UUID allocation authority",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B07.05.02.F02"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-085e0b5cee5456b5ac5d3e66fac4a623",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B07.05.02.F02",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "Feature",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "FT",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-7",
        "values": [
          {
            "text": "current",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-6",
        "values": [
          {
            "text": "Accepted",
            "attributes": {}
          }
        ]
      }
    ]
  }
}
---

# [Feature] Attendance State Accuracy

## Architectural content

Attendance state is accurate.

### Incorporation standing

Catalogue identity `B07.05.02.F02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
