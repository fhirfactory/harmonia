---
{
  "schemaVersion": 1,
  "canonicalId": "decf3c2e6c31424cbeedf8f5d0cdcd13",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Care Activity Handover",
  "friendlyName": "ClinicalCareAndResponse.NursingAndDirectCare.ContinuityOfDirectCareDelivery.CareActivityHandover",
  "canonicalName": "CT1-ClinicalCareAndResponse.CT2-NursingAndDirectCare.CT3-ContinuityOfDirectCareDelivery.FEAT-CareActivityHandover",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B03.02.01.F04",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B03",
        "type": "capability",
        "name": "Clinical Care & Response",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B03.02",
        "type": "capability",
        "name": "Nursing & Direct Care",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B03.02.01",
        "type": "capability",
        "name": "Continuity of Direct Care Delivery",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B03.02.01.F04",
        "type": "feature",
        "name": "Care Activity Handover",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B03.02.01.F04",
    "parent": "B03.02.01",
    "tier": "Feature",
    "sourceName": "Care Activity Handover",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B03.02.01.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/209",
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
      "selector": "B03.02.01.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-4d051f368a5d508981a6b742cd37f16b",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B03.02.01.F04",
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

# [Feature] Care Activity Handover

## Architectural content

Care activity can be handed over through relevant changes or disruptions, with recovery where required.

### Incorporation standing

Catalogue identity `B03.02.01.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
