---
{
  "schemaVersion": 1,
  "canonicalId": "f33f98aa3cf74f3db6af2b6f9bf60bbd",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Compromised Boundary Detection",
  "friendlyName": "IdentityTrustAndInformationProtection.LocalAccessPolicyEnforcement.BoundaryEnforcedAccess.CompromisedBoundaryDetection",
  "canonicalName": "CT1-EC-IdentityTrustAndInformationProtection.CT2-EC-LocalAccessPolicyEnforcement.CT3-EC-BoundaryEnforcedAccess.FEAT-CompromisedBoundaryDetection",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E05.03.01.F05",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "E05",
        "type": "capability",
        "name": "Identity, Trust & Information Protection",
        "tier": "CT1",
        "category": "enterprise"
      },
      {
        "key": "E05.03",
        "type": "capability",
        "name": "Local Access Policy Enforcement",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E05.03.01",
        "type": "capability",
        "name": "Boundary-Enforced Access",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E05.03.01.F05",
        "type": "feature",
        "name": "Compromised Boundary Detection",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E05.03.01.F05",
    "parent": "E05.03.01",
    "tier": "Feature",
    "sourceName": "Compromised Boundary Detection",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E05.03.01.F05",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/2146",
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
      "selector": "E05.03.01.F05"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-9a8274b988e25920b23971e96abe26c0",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E05.03.01.F05",
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

# [Feature] Compromised Boundary Detection

## Architectural content

Compromised boundary is detected.

### Incorporation standing

Catalogue identity `E05.03.01.F05`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
