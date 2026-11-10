---
{
  "schemaVersion": 1,
  "canonicalId": "69bb9ce6ae5d4cc8bbd3b8e5c7def62c",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] AttributableBusinessInformation",
  "friendlyName": "HealthInformationStewardship.InformationCompletenessAndProvenanceAccountability.ClinicalInformationSourceAccountability.AttributableBusinessInformation",
  "canonicalName": "CT1-HealthInformationStewardship.CT2-InformationCompletenessAndProvenanceAccountability.CT3-ClinicalInformationSourceAccountability.FEAT-AttributableBusinessInformation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B10.02.02.F01",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B10",
        "type": "capability",
        "name": "Health Information Stewardship",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B10.02",
        "type": "capability",
        "name": "Information Completeness & Provenance Accountability",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B10.02.02",
        "type": "capability",
        "name": "Clinical Information Source Accountability",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B10.02.02.F01",
        "type": "feature",
        "name": "AttributableBusinessInformation",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B10.02.02.F01",
    "parent": "B10.02.02",
    "tier": "Feature",
    "sourceName": "AttributableBusinessInformation",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B10.02.02.F01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/877",
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
      "selector": "B10.02.02.F01"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-a3c3f244b6265f728c4d6b02778ceaee",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B10.02.02.F01",
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

# [Feature] AttributableBusinessInformation

## Architectural content

Business information retains retrievable required origin context.

### Incorporation standing

Catalogue identity `B10.02.02.F01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
