---
{
  "schemaVersion": 1,
  "canonicalId": "87e8ef7fffbb42b6b1e8bc9bdb9f26b8",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Recovery Observation Coverage",
  "friendlyName": "ClinicalCareAndResponse.ProceduralAndPeriProceduralCare.PostProceduralRecoverySupport.RecoveryObservationCoverage",
  "canonicalName": "CT1-ClinicalCareAndResponse.CT2-ProceduralAndPeriProceduralCare.CT3-PostProceduralRecoverySupport.FEAT-RecoveryObservationCoverage",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B03.05.03.F02",
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
        "key": "B03.05",
        "type": "capability",
        "name": "Procedural & Peri-Procedural Care",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B03.05.03",
        "type": "capability",
        "name": "Post-Procedural Recovery Support",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B03.05.03.F02",
        "type": "feature",
        "name": "Recovery Observation Coverage",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B03.05.03.F02",
    "parent": "B03.05.03",
    "tier": "Feature",
    "sourceName": "Recovery Observation Coverage",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B03.05.03.F02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/263",
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
      "selector": "B03.05.03.F02"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-c600fd79c86b57478a8aae445d57ddf4",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B03.05.03.F02",
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

# [Feature] Recovery Observation Coverage

## Architectural content

Recovery observation is covered through relevant changes or disruptions, with recovery where required.

### Incorporation standing

Catalogue identity `B03.05.03.F02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
