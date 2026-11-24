---
{
  "schemaVersion": 1,
  "canonicalId": "b86da6ceb9b84719b3cbe8daa1ba3ddc",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] IUVersionRefsOnly",
  "friendlyName": "InformationInsightAndEvidenceUse.InformationSetAndResultOrganisation.StableVersionedInformationSet.IuVersionRefsOnly",
  "canonicalName": "CT1-EC-InformationInsightAndEvidenceUse.CT2-EC-InformationSetAndResultOrganisation.CT3-EC-StableVersionedInformationSet.FEAT-IuVersionRefsOnly",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E09.01.04.F01",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "E09",
        "type": "capability",
        "name": "Information Insight & Evidence Use",
        "tier": "CT1",
        "category": "enterprise"
      },
      {
        "key": "E09.01",
        "type": "capability",
        "name": "Information Set & Result Organisation",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E09.01.04",
        "type": "capability",
        "name": "Stable Versioned Information Set",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E09.01.04.F01",
        "type": "feature",
        "name": "IUVersionRefsOnly",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E09.01.04.F01",
    "parent": "E09.01.04",
    "tier": "Feature",
    "sourceName": "IUVersionRefsOnly",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E09.01.04.F01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/2845",
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
      "selector": "E09.01.04.F01"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-814af33d09b45df2beda0aea3da09a00",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E09.01.04.F01",
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

# [Feature] IUVersionRefsOnly

## Architectural content

Search result sets retain governed IU version references rather than uncontrolled payload copies.

### Incorporation standing

Catalogue identity `E09.01.04.F01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
