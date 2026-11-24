---
{
  "schemaVersion": 1,
  "canonicalId": "d6c40bcfc84a4b758530edcaabfbd0ca",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT2] Urgent & Emergency Response",
  "friendlyName": "ClinicalCareAndResponse.UrgentAndEmergencyResponse",
  "canonicalName": "CT1-ClinicalCareAndResponse.CT2-UrgentAndEmergencyResponse",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B03.04",
    "allocation": "UUIDv4 allocated once by explicit create",
    "predecessorCanonicalIds": [
      "aa088f0647ad4b78966fe28fa80af674",
      "e5edbb54258c490aaba0816d980dc624"
    ]
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
        "key": "B03.04",
        "type": "capability",
        "name": "Urgent & Emergency Response",
        "tier": "CT2",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B03.04",
    "parent": "B03",
    "tier": "CT2",
    "sourceName": "Urgent & Emergency Response",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B03.04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/65",
      "role": "Exact source contributions, original wording/criteria/status and accepted adjudication correspondence; historical assurance evidence",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json"
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
      "selector": "B03.04"
    }
  ],
  "relationships": [
    {
      "identifier": "id-d73e205a599756d89b3455d88f29795e",
      "archimateType": "Composition",
      "source": "id-eb15d4e8cba45be8b05f1b45d7ca21cd",
      "target": "id-24e5ab2fae265452bb5d6ce68938c4a4",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
    },
    {
      "identifier": "id-59d0561c3a0a51ad87f238826a64b40b",
      "archimateType": "Composition",
      "source": "id-eb15d4e8cba45be8b05f1b45d7ca21cd",
      "target": "id-5fd1af75d9cb520da6f651c82d5ce500",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
    },
    {
      "identifier": "id-cb9467c4de94566d8763fe0a6cd7dbb8",
      "archimateType": "Composition",
      "source": "id-eb15d4e8cba45be8b05f1b45d7ca21cd",
      "target": "id-a305b44b218b54c4aac15a0225148a97",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
  ],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-eb15d4e8cba45be8b05f1b45d7ca21cd",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B03.04",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "CT2",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "CT2",
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

# [CT2] Urgent & Emergency Response

## Architectural content

Provide timely action when health is seriously threatened.

### Incorporation standing

Catalogue identity `B03.04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
