---
{
  "schemaVersion": 1,
  "canonicalId": "c102ede2b9e249b2a2ddc9f19e0cbd7f",
  "archimateType": "Grouping",
  "specialization": null,
  "displayName": "Audit Integrity",
  "friendlyName": "AuditIntegrity",
  "canonicalName": null,
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
    "mode": "Accepted source local lexical component; subject grouping supplies no naming ancestry"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
      "revision": "d5fdddb31e9bff39ae6c08b2e0f2c71d309ab5f5",
      "selector": "G04.02",
      "role": "Accepted Iteration 001 subject subgroup"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
      "selector": "G04.02"
    }
  ],
  "relationships": [
    {
      "identifier": "id-e4a1e38b28b04d778240d14071147a35",
      "archimateType": "Composition",
      "source": "id-ec71ca548f145eeaad3c575fcf93d474",
      "target": "id-18438fd3db435f88992cd982b27f5757",
      "standing": {
        "semantic": "Solution Architect-reviewed Iteration 001; authorised ADD; resulting increment review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
        "revision": "d5fdddb31e9bff39ae6c08b2e0f2c71d309ab5f5",
        "selector": "NFR-044 / Domain",
        "meaning": "Primary subject classification containment"
      },
      "properties": [],
      "name": [
        {
          "text": "Primary subject classification containment"
        }
      ],
      "documentation": [
        {
          "text": "Source NFR-044 / Domain. Primary subject classification containment. No component ownership, implementation containment, Home, dependency or realisation is implied."
        }
      ]
    },
    {
      "identifier": "id-3a04df9805cf46199a2ac8970b011719",
      "source": "id-ec71ca548f145eeaad3c575fcf93d474",
      "target": "id-ff3a2d67c241526199031d3dc1b50e0a",
      "archimateType": "Association",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "Solution Architect authorised Subject hierarchy and Home completion; result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "selector": "Consolidated scope / Composition containment and distinct authoritative Home; Existing Subject G04.02",
        "sha256": "3ba6a88a66c3c4956e80e6676a9b60ceb04454b59d510643ace7ba1b7dd8fc5d",
        "authority": "enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-12",
        "meaning": "Authoritative architectural location"
      },
      "name": [
        {
          "text": "Home"
        }
      ],
      "documentation": [
        {
          "text": "Existing Subject G04.02. Composition defines containment; the separate directed child-to-parent Home Association identifies authoritative architectural location. No new statement allocation or component responsibility is asserted."
        }
      ],
      "properties": [
        {
          "ref": "propid-40",
          "values": [
            {
              "text": "current",
              "attributes": {}
            }
          ]
        },
        {
          "ref": "specialization",
          "values": [
            {
              "text": "Home",
              "attributes": {}
            }
          ]
        }
      ],
      "specialization": "Home"
    },
    {
      "identifier": "id-50dfe9f759e24f25b5cc2ccc37b80a1e",
      "source": "id-ec71ca548f145eeaad3c575fcf93d474",
      "target": "id-bf875446915c42ccb0bff4a538b4f41a",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect authorised Subject hierarchy and Home completion; result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "selector": "Consolidated scope / Composition containment and distinct authoritative Home; Authored Grouping id-bf875446915c42ccb0bff4a538b4f41a",
        "sha256": "3ba6a88a66c3c4956e80e6676a9b60ceb04454b59d510643ace7ba1b7dd8fc5d",
        "authority": "enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-12",
        "meaning": "Subject Grouping containment"
      },
      "name": [
        {
          "text": "Subject Grouping containment"
        }
      ],
      "documentation": [
        {
          "text": "Authored Grouping id-bf875446915c42ccb0bff4a538b4f41a. Composition defines containment; the separate directed child-to-parent Home Association identifies authoritative architectural location. No new statement allocation or component responsibility is asserted."
        }
      ],
      "properties": [
        {
          "ref": "propid-40",
          "values": [
            {
              "text": "current",
              "attributes": {}
            }
          ]
        }
      ]
    }
  ],
  "unresolvedProperties": {
    "canonicalName": "Existing formal-name qualifier remains unestablished for Grouping; no new token or subject-derived ancestry is invented. HCRL 1.1.17-001 DR-3; existing Q-01 naming boundary."
  },
  "subjectGroup": {
    "sourceId": "G04.02",
    "role": "subject subgroup"
  },
  "exchange": {
    "identifier": "id-ec71ca548f145eeaad3c575fcf93d474",
    "properties": []
  }
}
---

# Audit Integrity

## Architectural content

Subject subgroup **G04.02 — Audit Integrity** in the accepted Iteration 001 hierarchy.

Protection of audit records against unauthorised alteration, deletion or corruption.

This is subject organisation of architectural statements. Explicit Composition denotes subject containment within this register; it establishes no component ownership, implementation containment, logical Home or naming ancestry. Grouping itself supplies no additional relationship.
