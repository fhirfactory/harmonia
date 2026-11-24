---
{
  "schemaVersion": 1,
  "canonicalId": "bb94ab95abcf48d7bedd15aad6d3fe81",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Maintenance Compliance",
  "friendlyName": "ClinicalFacilitiesAndSupply.ClinicalEquipmentReadiness.EquipmentSafetyAndMaintenance.MaintenanceCompliance",
  "canonicalName": "CT1-ClinicalFacilitiesAndSupply.CT2-ClinicalEquipmentReadiness.CT3-EquipmentSafetyAndMaintenance.FEAT-MaintenanceCompliance",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B09.02.02.F02",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B09",
        "type": "capability",
        "name": "Clinical Facilities & Supply",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B09.02",
        "type": "capability",
        "name": "Clinical Equipment Readiness",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B09.02.02",
        "type": "capability",
        "name": "Equipment Safety and Maintenance",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B09.02.02.F02",
        "type": "feature",
        "name": "Maintenance Compliance",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B09.02.02.F02",
    "parent": "B09.02.02",
    "tier": "Feature",
    "sourceName": "Maintenance Compliance",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B09.02.02.F02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/778",
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
      "selector": "B09.02.02.F02"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-c0f97aadbff45db08177317105bc0a1f",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B09.02.02.F02",
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

# [Feature] Maintenance Compliance

## Architectural content

Maintenance complies with the applicable requirements, distinguishing permitted and nonconforming conditions.

### Incorporation standing

Catalogue identity `B09.02.02.F02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
