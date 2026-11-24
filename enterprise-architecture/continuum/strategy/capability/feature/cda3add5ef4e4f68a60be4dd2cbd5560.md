---
{
  "schemaVersion": 1,
  "canonicalId": "cda3add5ef4e4f68a60be4dd2cbd5560",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Authorised Change Attribution",
  "friendlyName": "GovernedInformationCustody.GovernedInformationPersistence.ControlledInformationChange.AuthorisedChangeAttribution",
  "canonicalName": "CT1-EC-GovernedInformationCustody.CT2-EC-GovernedInformationPersistence.CT3-EC-ControlledInformationChange.FEAT-AuthorisedChangeAttribution",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E01.01.02.F04",
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
        "key": "E01.01",
        "type": "capability",
        "name": "Governed Information Persistence",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E01.01.02",
        "type": "capability",
        "name": "Controlled Information Change",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E01.01.02.F04",
        "type": "feature",
        "name": "Authorised Change Attribution",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E01.01.02.F04",
    "parent": "E01.01.02",
    "tier": "Feature",
    "sourceName": "Authorised Change Attribution",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E01.01.02.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/1473",
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
      "selector": "E01.01.02.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-929accda3a9a5b0cbfabb3e8e10c3f9f",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E01.01.02.F04",
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

# [Feature] Authorised Change Attribution

## Architectural content

Authorised change is attributable, distinguishing permitted and nonconforming conditions.

### Incorporation standing

Catalogue identity `E01.01.02.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
