---
{
  "schemaVersion": 1,
  "canonicalId": "fb30eb9b8a3c41eda3a8b5edfee95ac1",
  "archimateType": "Constraint",
  "specialization": "Mandate",
  "displayName": "[Mandate] Controlled Capacity Admission",
  "friendlyName": "ControlledCapacityAdmission",
  "canonicalName": "MOT-ControlledCapacityAdmission",
  "standing": {
    "semantic": "Solution Architect-reviewed Iteration 001; authorised ADD; resulting increment review pending",
    "currentness": "current"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.17/001/before/ai-agent-tasks/20261031T104506Z-nfr-derived-elements-add.md; accepted Iteration 001 specification",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "mode": "Accepted source local lexical component; subject grouping supplies no naming ancestry",
    "inputs": [
      {
        "key": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md#NFR-111",
        "type": "Constraint",
        "name": "Controlled Capacity Admission"
      }
    ]
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
      "revision": "d5fdddb31e9bff39ae6c08b2e0f2c71d309ab5f5",
      "selector": "NFR-111",
      "role": "Accepted Iteration 001 architectural statement"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
      "selector": "NFR-111"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "registerEntries": [
    {
      "id": "NFR-111",
      "name": "Controlled Capacity Admission",
      "type": "Constraint",
      "specialization": "Mandate",
      "description": "Harmonia shall manage increasing workloads within its operational capacity and, where defined capacity thresholds are reached, shall apply controlled admission or rejection of incoming activity and report the associated capacity or performance conditions.",
      "domain": "G08.04"
    }
  ],
  "exchange": {
    "identifier": "id-45bfc1cb7f8c56b98a5ad3bf0c0f04e0",
    "properties": []
  }
}
---

# [Mandate] Controlled Capacity Admission

## Architectural content

Harmonia shall manage increasing workloads within its operational capacity and, where defined capacity thresholds are reached, shall apply controlled admission or rejection of incoming activity and report the associated capacity or performance conditions.

Source entry: **NFR-111 — Controlled Capacity Admission**. Primary subject: `G08.04`.

The source classification is Constraint «Mandate». The source identifier is a scoped register reference and does not determine architectural type. Applicability, release scope and unresolved thresholds remain as stated above.
