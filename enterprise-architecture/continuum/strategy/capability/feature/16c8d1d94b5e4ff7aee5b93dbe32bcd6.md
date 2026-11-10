---
{
  "schemaVersion": 1,
  "canonicalId": "16c8d1d94b5e4ff7aee5b93dbe32bcd6",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Invalid Evidence Recognition",
  "friendlyName": "DiagnosticInvestigation.DiagnosticEvidenceProduction.EvidenceQualityExceptionHandling.InvalidEvidenceRecognition",
  "canonicalName": "CT1-DiagnosticInvestigation.CT2-DiagnosticEvidenceProduction.CT3-EvidenceQualityExceptionHandling.FEAT-InvalidEvidenceRecognition",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B04.02.03.F02",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B04",
        "type": "capability",
        "name": "Diagnostic Investigation",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B04.02",
        "type": "capability",
        "name": "Diagnostic Evidence Production",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B04.02.03",
        "type": "capability",
        "name": "Evidence Quality Exception Handling",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B04.02.03.F02",
        "type": "feature",
        "name": "Invalid Evidence Recognition",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B04.02.03.F02",
    "parent": "B04.02.03",
    "tier": "Feature",
    "sourceName": "Invalid Evidence Recognition",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B04.02.03.F02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/308",
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
      "selector": "B04.02.03.F02"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-a88c1415a2e45b65838e0da78e96cb46",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B04.02.03.F02",
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

# [Feature] Invalid Evidence Recognition

## Architectural content

Invalid evidence is recognised.

### Incorporation standing

Catalogue identity `B04.02.03.F02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
