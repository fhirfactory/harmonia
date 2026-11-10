---
{
  "schemaVersion": 1,
  "canonicalId": "31a6bdcabcc84bfa812df8bc976b6b99",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] EffectiveRecallDisposition",
  "friendlyName": "ClinicalFacilitiesAndSupply.MedicinesAndTherapeuticGoodsSupply.TherapeuticRecallResponse.EffectiveRecallDisposition",
  "canonicalName": "CT1-ClinicalFacilitiesAndSupply.CT2-MedicinesAndTherapeuticGoodsSupply.CT3-TherapeuticRecallResponse.FEAT-EffectiveRecallDisposition",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B09.03.03.F01",
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
        "key": "B09.03",
        "type": "capability",
        "name": "Medicines & Therapeutic Goods Supply",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B09.03.03",
        "type": "capability",
        "name": "Therapeutic Recall Response",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B09.03.03.F01",
        "type": "feature",
        "name": "EffectiveRecallDisposition",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B09.03.03.F01",
    "parent": "B09.03.03",
    "tier": "Feature",
    "sourceName": "EffectiveRecallDisposition",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B09.03.03.F01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/797",
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
      "selector": "B09.03.03.F01"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-2c5f879e84b25186b00b15aa5ba0610f",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B09.03.03.F01",
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

# [Feature] EffectiveRecallDisposition

## Architectural content

Recalled items receive timely, attributable action.

### Incorporation standing

Catalogue identity `B09.03.03.F01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
