---
{
  "schemaVersion": 1,
  "canonicalId": "ade2153d1c7d42e2aec52bc0ce3aad19",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] PhysicalLocationIndependent",
  "friendlyName": "GovernedInformationCustody.GovernedBinaryAndLargeMediaContentManagement.BinaryCustodyAndSourceLocationManagement.PhysicalLocationIndependent",
  "canonicalName": "CT1-EC-GovernedInformationCustody.CT2-EC-GovernedBinaryAndLargeMediaContentManagement.CT3-EC-BinaryCustodyAndSourceLocationManagement.FEAT-PhysicalLocationIndependent",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E01.06.03.F03",
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
        "key": "E01.06",
        "type": "capability",
        "name": "Governed Binary & Large-Media Content Management",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E01.06.03",
        "type": "capability",
        "name": "Binary Custody & Source Location Management",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E01.06.03.F03",
        "type": "feature",
        "name": "PhysicalLocationIndependent",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E01.06.03.F03",
    "parent": "E01.06.03",
    "tier": "Feature",
    "sourceName": "PhysicalLocationIndependent",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E01.06.03.F03",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/1598",
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
      "selector": "E01.06.03.F03"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-695ba1da711f536d89d79e3e2dc2e00d",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E01.06.03.F03",
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

# [Feature] PhysicalLocationIndependent

## Architectural content

Logical references remain stable through authorised storage-provider or physical-location changes.

### Incorporation standing

Catalogue identity `E01.06.03.F03`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
