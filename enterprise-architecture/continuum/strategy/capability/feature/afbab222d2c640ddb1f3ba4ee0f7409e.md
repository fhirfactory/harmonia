---
{
  "schemaVersion": 1,
  "canonicalId": "afbab222d2c640ddb1f3ba4ee0f7409e",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Record Authorship Clarity",
  "friendlyName": "HealthInformationStewardship.HealthRecordStewardship.ClinicallyMeaningfulRecordCompletion.RecordAuthorshipClarity",
  "canonicalName": "CT1-HealthInformationStewardship.CT2-HealthRecordStewardship.CT3-ClinicallyMeaningfulRecordCompletion.FEAT-RecordAuthorshipClarity",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B10.01.01.F04",
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
        "key": "B10.01",
        "type": "capability",
        "name": "Health Record Stewardship",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B10.01.01",
        "type": "capability",
        "name": "Clinically Meaningful Record Completion",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B10.01.01.F04",
        "type": "feature",
        "name": "Record Authorship Clarity",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B10.01.01.F04",
    "parent": "B10.01.01",
    "tier": "Feature",
    "sourceName": "Record Authorship Clarity",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B10.01.01.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/850",
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
      "selector": "B10.01.01.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-7d34c4ebd7d5505b8b9a75b8f3c0f519",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B10.01.01.F04",
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

# [Feature] Record Authorship Clarity

## Architectural content

Record authorship is clear, with accessible and accurate supporting evidence.

### Incorporation standing

Catalogue identity `B10.01.01.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
