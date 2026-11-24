---
{
  "schemaVersion": 1,
  "canonicalId": "ead0ddee0e934ae2b051ec91cbbe9296",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Recovery Point Evidence",
  "friendlyName": "GovernedInformationCustody.InformationRetentionAndRecovery.CustodyRecovery.RecoveryPointEvidence",
  "canonicalName": "CT1-EC-GovernedInformationCustody.CT2-EC-InformationRetentionAndRecovery.CT3-EC-CustodyRecovery.FEAT-RecoveryPointEvidence",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E01.04.03.F04",
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
        "key": "E01.04",
        "type": "capability",
        "name": "Information Retention & Recovery",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E01.04.03",
        "type": "capability",
        "name": "Custody Recovery",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E01.04.03.F04",
        "type": "feature",
        "name": "Recovery Point Evidence",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E01.04.03.F04",
    "parent": "E01.04.03",
    "tier": "Feature",
    "sourceName": "Recovery Point Evidence",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E01.04.03.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/1549",
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
      "selector": "E01.04.03.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-056b8e2862f45b88ba94ac276d93e0df",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E01.04.03.F04",
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

# [Feature] Recovery Point Evidence

## Architectural content

Recovery point is evidenced, with accessible and accurate supporting evidence.

### Incorporation standing

Catalogue identity `E01.04.03.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
