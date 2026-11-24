---
{
  "schemaVersion": 1,
  "canonicalId": "7276d7bffede4b3dbc8aadb003951ef6",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] TraceableBusinessReport",
  "friendlyName": "InformationInsightAndEvidenceUse.OperationalAndBusinessInformationReporting.BusinessInformationReporting.TraceableBusinessReport",
  "canonicalName": "CT1-EC-InformationInsightAndEvidenceUse.CT2-EC-OperationalAndBusinessInformationReporting.CT3-EC-BusinessInformationReporting.FEAT-TraceableBusinessReport",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E09.03.02.F01",
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
        "key": "E09.03",
        "type": "capability",
        "name": "Operational & Business Information Reporting",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E09.03.02",
        "type": "capability",
        "name": "Business Information Reporting",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E09.03.02.F01",
        "type": "feature",
        "name": "TraceableBusinessReport",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E09.03.02.F01",
    "parent": "E09.03.02",
    "tier": "Feature",
    "sourceName": "TraceableBusinessReport",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E09.03.02.F01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/2875",
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
      "selector": "E09.03.02.F01"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-80323dd6f2485c46ae9708268d1b98ae",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E09.03.02.F01",
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

# [Feature] TraceableBusinessReport

## Architectural content

Business reports can be traced to governed input versions.

### Incorporation standing

Catalogue identity `E09.03.02.F01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
