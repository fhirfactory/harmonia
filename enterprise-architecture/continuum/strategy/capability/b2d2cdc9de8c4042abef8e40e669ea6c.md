---
{
  "schemaVersion": 1,
  "canonicalId": "b2d2cdc9de8c4042abef8e40e669ea6c",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT2] Operational Fault Management",
  "friendlyName": "OperationalObservabilityAssuranceAndResilience.OperationalFaultManagement",
  "canonicalName": "CT1-EC-OperationalObservabilityAssuranceAndResilience.CT2-EC-OperationalFaultManagement",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E08.05",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "E08",
        "type": "capability",
        "name": "Operational Observability, Assurance & Resilience",
        "tier": "CT1",
        "category": "enterprise"
      },
      {
        "key": "E08.05",
        "type": "capability",
        "name": "Operational Fault Management",
        "tier": "CT2",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E08.05",
    "parent": "E08",
    "tier": "CT2",
    "sourceName": "Operational Fault Management",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E08.05",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/708",
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
      "selector": "E08.05"
    }
  ],
  "relationships": [
    {
      "identifier": "id-1fe821e0fe175be8bd8ac515a82a64bc",
      "archimateType": "Composition",
      "source": "id-cb39e7c06f5f5b3e83c12435c3334f66",
      "target": "id-b8ed13b4f9455ad08abacd68ff1f65e1",
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
      "identifier": "id-8badf63436da55f99ec9b587345953c5",
      "archimateType": "Composition",
      "source": "id-cb39e7c06f5f5b3e83c12435c3334f66",
      "target": "id-bb061700b015522b907e1670ae5d18ca",
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
      "identifier": "id-9fd64b8ee2055ddb92f1d1cf1ab9eb5b",
      "archimateType": "Composition",
      "source": "id-cb39e7c06f5f5b3e83c12435c3334f66",
      "target": "id-75b0aac9325b557192624f1026855b5e",
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
    "identifier": "id-cb39e7c06f5f5b3e83c12435c3334f66",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E08.05",
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

# [CT2] Operational Fault Management

## Architectural content

Detect, diagnose, contain and support remedy of faults.

### Incorporation standing

Catalogue identity `E08.05`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
