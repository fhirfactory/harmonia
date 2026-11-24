---
{
  "schemaVersion": 1,
  "canonicalId": "9fff7bd6dabc4c88abda324dfb324f56",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Authorisation Snapshot Fidelity",
  "friendlyName": "IdentityTrustAndInformationProtection.SecurityContextAndPassportPropagation.CredentialFreeContextPropagation.AuthorisationSnapshotFidelity",
  "canonicalName": "CT1-EC-IdentityTrustAndInformationProtection.CT2-EC-SecurityContextAndPassportPropagation.CT3-EC-CredentialFreeContextPropagation.FEAT-AuthorisationSnapshotFidelity",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E05.04.02.F03",
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
        "key": "E05.04",
        "type": "capability",
        "name": "Security Context & Passport Propagation",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E05.04.02",
        "type": "capability",
        "name": "Credential-Free Context Propagation",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E05.04.02.F03",
        "type": "feature",
        "name": "Authorisation Snapshot Fidelity",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E05.04.02.F03",
    "parent": "E05.04.02",
    "tier": "Feature",
    "sourceName": "Authorisation Snapshot Fidelity",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E05.04.02.F03",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/2169",
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
      "selector": "E05.04.02.F03"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-465f6aa5c6be5289b04029a4a00c57ad",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E05.04.02.F03",
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

# [Feature] Authorisation Snapshot Fidelity

## Architectural content

Authorisation snapshot is faithfully preserved, distinguishing permitted and nonconforming conditions.

### Incorporation standing

Catalogue identity `E05.04.02.F03`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
