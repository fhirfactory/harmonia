---
{
  "schemaVersion": 1,
  "canonicalId": "9ac82bbbf21c4feba916e1e46d5fd2de",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] ContentAvailability",
  "friendlyName": "GovernedInformationCustody.GovernedInformationPersistence.GovernedContentCustody.ContentAvailability",
  "canonicalName": "CT1-EC-GovernedInformationCustody.CT2-EC-GovernedInformationPersistence.CT3-EC-GovernedContentCustody.FEAT-ContentAvailability",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E01.01.03.F01",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "E01",
        "type": "capability",
        "name": "Governed Information Custody",
        "tier": "CT1",
        "category": "enterprise"
      },
      {
        "key": "E01.01",
        "type": "capability",
        "name": "Governed Information Persistence",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E01.01.03",
        "type": "capability",
        "name": "Governed Content Custody",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E01.01.03.F01",
        "type": "feature",
        "name": "ContentAvailability",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E01.01.03.F01",
    "parent": "E01.01.03",
    "tier": "Feature",
    "sourceName": "ContentAvailability",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E01.01.03.F01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/1476",
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
      "selector": "E01.01.03.F01"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-04415031b2ba5d7084f80fc69cc274f2",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E01.01.03.F01",
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

# [Feature] ContentAvailability

## Architectural content

Required information content is returned within the agreed availability conditions.

### Incorporation standing

Catalogue identity `E01.01.03.F01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
