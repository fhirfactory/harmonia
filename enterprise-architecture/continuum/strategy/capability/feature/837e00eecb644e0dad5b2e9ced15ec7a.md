---
{
  "schemaVersion": 1,
  "canonicalId": "837e00eecb644e0dad5b2e9ced15ec7a",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Remote Referral and Evacuation",
  "friendlyName": "EnterpriseDirectionAndReadiness.HealthServiceReadinessAndMobilisation.DeployedOrRemoteCareReadiness.RemoteReferralAndEvacuation",
  "canonicalName": "CT1-EnterpriseDirectionAndReadiness.CT2-HealthServiceReadinessAndMobilisation.CT3-DeployedOrRemoteCareReadiness.FEAT-RemoteReferralAndEvacuation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B12.05.03.F04",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B12",
        "type": "capability",
        "name": "Enterprise Direction & Readiness",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B12.05",
        "type": "capability",
        "name": "Health Service Readiness & Mobilisation",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B12.05.03",
        "type": "capability",
        "name": "Deployed or Remote Care Readiness",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B12.05.03.F04",
        "type": "feature",
        "name": "Remote Referral and Evacuation",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B12.05.03.F04",
    "parent": "B12.05.03",
    "tier": "Feature",
    "sourceName": "Remote Referral and Evacuation",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B12.05.03.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/1185",
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
      "selector": "B12.05.03.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-29183993c7975a0ab8f2a9dc3f547a54",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B12.05.03.F04",
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

# [Feature] Remote Referral and Evacuation

## Architectural content

Remote referral and evacuation reaches and can be understood by the intended recipient.

### Incorporation standing

Catalogue identity `B12.05.03.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
