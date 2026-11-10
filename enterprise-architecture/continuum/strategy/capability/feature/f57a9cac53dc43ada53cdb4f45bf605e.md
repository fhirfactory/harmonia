---
{
  "schemaVersion": 1,
  "canonicalId": "f57a9cac53dc43ada53cdb4f45bf605e",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Stock Arrival Confirmation",
  "friendlyName": "ClinicalFacilitiesAndSupply.ConsumablesAndClinicalLogistics.ClinicalStockReplenishment.StockArrivalConfirmation",
  "canonicalName": "CT1-ClinicalFacilitiesAndSupply.CT2-ConsumablesAndClinicalLogistics.CT3-ClinicalStockReplenishment.FEAT-StockArrivalConfirmation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B09.04.01.F04",
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
        "key": "B09.04",
        "type": "capability",
        "name": "Consumables & Clinical Logistics",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B09.04.01",
        "type": "capability",
        "name": "Clinical Stock Replenishment",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B09.04.01.F04",
        "type": "feature",
        "name": "Stock Arrival Confirmation",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B09.04.01.F04",
    "parent": "B09.04.01",
    "tier": "Feature",
    "sourceName": "Stock Arrival Confirmation",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B09.04.01.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/805",
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
      "selector": "B09.04.01.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-0e74fe70e8ed5b71903e4c5f856f267c",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B09.04.01.F04",
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

# [Feature] Stock Arrival Confirmation

## Architectural content

Stock arrival confirmation reaches and can be understood by the intended recipient.

### Incorporation standing

Catalogue identity `B09.04.01.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
