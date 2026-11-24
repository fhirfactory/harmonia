---
{
  "schemaVersion": 1,
  "canonicalId": "4ad5ed4ee9ad4965bbd4d68a2c3fda7d",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT3] Governed Audit Event Availability",
  "friendlyName": "OperationalObservabilityAssuranceAndResilience.AuditAndProvenanceReporting.GovernedAuditEventAvailability",
  "canonicalName": "CT1-EC-OperationalObservabilityAssuranceAndResilience.CT2-EC-AuditAndProvenanceReporting.CT3-EC-GovernedAuditEventAvailability",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E08.03.01",
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
        "key": "E08.03",
        "type": "capability",
        "name": "Audit & Provenance Reporting",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E08.03.01",
        "type": "capability",
        "name": "Governed Audit Event Availability",
        "tier": "CT3",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E08.03.01",
    "parent": "E08.03",
    "tier": "CT3",
    "sourceName": "Governed Audit Event Availability",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E08.03.01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/700",
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
      "selector": "E08.03.01"
    }
  ],
  "relationships": [
    {
      "identifier": "id-48cb2aa963ac5492b2e73d4f9ff81d6b",
      "archimateType": "Composition",
      "source": "id-5cfceb3252735adab2b0ab1eb5fb4b86",
      "target": "id-5c4fda7270765b029833ebcad11e9a3b",
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
      "identifier": "id-af104f8596355f698e84ef532fb66380",
      "archimateType": "Composition",
      "source": "id-5cfceb3252735adab2b0ab1eb5fb4b86",
      "target": "id-1dca6edcdfab509f8e2879fa8a74aded",
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
      "identifier": "id-810997f09bc45717a4829781b90a7124",
      "archimateType": "Composition",
      "source": "id-5cfceb3252735adab2b0ab1eb5fb4b86",
      "target": "id-a85b9185720d5cd79cf9282e988bbb56",
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
      "identifier": "id-0b29e7cc860f55c9ae382dd3bee57d1d",
      "archimateType": "Composition",
      "source": "id-5cfceb3252735adab2b0ab1eb5fb4b86",
      "target": "id-b0a0f3f9b926575a98629821e13f8366",
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
      "identifier": "id-4485b930bdad5348961253331b565ff0",
      "archimateType": "Composition",
      "source": "id-5cfceb3252735adab2b0ab1eb5fb4b86",
      "target": "id-09feebb362705355a9e7039876437268",
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
    "identifier": "id-5cfceb3252735adab2b0ab1eb5fb4b86",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E08.03.01",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "CT3",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "CT3",
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

# [CT3] Governed Audit Event Availability

## Architectural content

Required significant activity can be inspected as structured audit evidence.

### Incorporation standing

Catalogue identity `E08.03.01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
