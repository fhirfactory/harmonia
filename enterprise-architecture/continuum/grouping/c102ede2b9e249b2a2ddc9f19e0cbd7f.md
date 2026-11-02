---
{
  "schemaVersion": 1,
  "canonicalId": "c102ede2b9e249b2a2ddc9f19e0cbd7f",
  "archimateType": "Grouping",
  "specialization": "SolutionFacet",
  "displayName": "Audit Integrity",
  "friendlyName": "AuditAndAccountability.AuditIntegrity",
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
    "mode": "Explicit Solution Architect-authored displayName and friendlyName; no new formal qualifier or naming ancestry",
    "authoredNativeName": "[SolutionFacet] AuditAndAccountability.AuditIntegrity",
    "reconciliationAuthority": "enterprise-architecture/design/review/1.1.19/hcrl-1.1.19-001.pending.md#dr-17"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
      "revision": "d5fdddb31e9bff39ae6c08b2e0f2c71d309ab5f5",
      "selector": "G04.02",
      "role": "Accepted Iteration 001 subject subgroup"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.19/001/before/Harmonia-Continuum-1.1.19-001.xml",
      "selector": "element[@identifier=\"id-ec71ca548f145eeaad3c575fcf93d474\"]",
      "role": "Solution Architect-authored SolutionFacet specialisation and explicit naming; existing identity preserved",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.19/001/before/Harmonia-Continuum-1.1.19-001.xml",
      "sha256": "217202bb3c303c0843b20b5fe6811ccd73cb2cd8c6ea3ed23c2de5951bcf52e1"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.19/001/before/ai-agent-tasks/unresolved-solutionfacet-dr17-clarification.md",
      "selector": "Authorised Scope; Bounded Interpretation; Completion Boundary",
      "role": "Operative DR-17 clarification; candidate application only, no result acceptance",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.19/001/before/ai-agent-tasks/unresolved-solutionfacet-dr17-clarification.md",
      "sha256": "891ab4068865f24320956a55cd2641fff57ae5038710243f5aa29c8bad62c22a"
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
    "canonicalName": "The inherited formal-name vocabulary qualification remains unresolved for this Grouping / SolutionFacet. This terminology refactoring authorises no formal qualifier or additional naming ancestry; see HCRL 1.1.19-001 DR-14/DR-17."
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

This existing element is a **SolutionFacet**, a specialisation of native
**Grouping**. The [governed definition](../../governance/continuum-representation.md#solutionfacet-classification--1119-candidate)
distinguishes accessible solution classification from Capability, responsibility
and ownership. Its canonical identity, G01–G18 context, existing Composition
and separately established Home assertions are preserved.

SolutionFacet subgroup **G04.02 — Audit Integrity** in the accepted Iteration 001 hierarchy.

Protection of audit records against unauthorised alteration, deletion or corruption.

This is SolutionFacet organisation of architectural statements. Explicit Composition denotes SolutionFacet containment within this register; it establishes no component ownership, implementation containment, logical Home or naming ancestry. Grouping itself supplies no additional relationship.
