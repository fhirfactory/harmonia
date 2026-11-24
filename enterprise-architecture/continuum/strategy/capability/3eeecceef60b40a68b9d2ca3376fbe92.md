---
{
  "schemaVersion": 1,
  "canonicalId": "3eeecceef60b40a68b9d2ca3376fbe92",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT3] Digital Twin Referent Association",
  "friendlyName": "IdentityTrustAndInformationProtection.HealthEntityIdentityAndAssociation.DigitalTwinReferentAssociation",
  "canonicalName": "CT1-EC-IdentityTrustAndInformationProtection.CT2-EC-HealthEntityIdentityAndAssociation.CT3-EC-DigitalTwinReferentAssociation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E05.07.08",
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
        "key": "E05.07",
        "type": "capability",
        "name": "Health Entity Identity & Association",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E05.07.08",
        "type": "capability",
        "name": "Digital Twin Referent Association",
        "tier": "CT3",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E05.07.08",
    "parent": "E05.07",
    "tier": "CT3",
    "sourceName": "Digital Twin Referent Association",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E05.07.08",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/587",
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
      "selector": "E05.07.08"
    }
  ],
  "relationships": [
    {
      "identifier": "id-f1542290708f5c6d83720704af615fae",
      "archimateType": "Composition",
      "source": "id-dce67fff8b6f5a35acd799d6aeba5b78",
      "target": "id-7b7dec85cbd35ff0a434d568a4b88189",
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
      "identifier": "id-858f6f8906b2580e92e0d8923b26472e",
      "archimateType": "Composition",
      "source": "id-dce67fff8b6f5a35acd799d6aeba5b78",
      "target": "id-d70ef45b57de53c3bbb01b507c3272b4",
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
      "identifier": "id-a019f9340d5a5987bca50a3ebfee02ac",
      "archimateType": "Composition",
      "source": "id-dce67fff8b6f5a35acd799d6aeba5b78",
      "target": "id-888db1618d3e55de9660b483eb87d97f",
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
      "identifier": "id-3fa8bd7ea3d25a76a4008775ea57fb9d",
      "archimateType": "Composition",
      "source": "id-dce67fff8b6f5a35acd799d6aeba5b78",
      "target": "id-73c8d5fd5c3951b9adb1e801afee393e",
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
      "identifier": "id-e74deb970c755b26863d4f0c16f6fb6a",
      "archimateType": "Composition",
      "source": "id-dce67fff8b6f5a35acd799d6aeba5b78",
      "target": "id-48376fadb60d55ae82cc763b6a87d9d2",
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
    "identifier": "id-dce67fff8b6f5a35acd799d6aeba5b78",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E05.07.08",
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

# [CT3] Digital Twin Referent Association

## Architectural content

Preserve the association between a Digital Twin and the real-world entity it models without equating the Twin with that entity or its current fulfiller.

### Incorporation standing

Catalogue identity `E05.07.08`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
