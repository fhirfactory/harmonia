---
{
  "schemaVersion": 1,
  "canonicalId": "ef476f07cd9e4ba6bdac99dc6e940baa",
  "archimateType": "Grouping",
  "specialization": null,
  "displayName": "Configuration Management",
  "friendlyName": "ConfigurationManagement",
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
      "selector": "G10.03",
      "role": "Accepted Iteration 001 subject subgroup"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
      "selector": "G10.03"
    }
  ],
  "relationships": [
    {
      "identifier": "id-8d5d7904b7ad44e093f6dd702863a967",
      "archimateType": "Composition",
      "source": "id-6868d76816f853a3876146eb6a80d6fd",
      "target": "id-b9ea2eed3f5c5f8e90fad3d44f514653",
      "standing": {
        "semantic": "Solution Architect-reviewed Iteration 001; authorised ADD; resulting increment review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md",
        "revision": "d5fdddb31e9bff39ae6c08b2e0f2c71d309ab5f5",
        "selector": "NFR-130 / Domain",
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
          "text": "Source NFR-130 / Domain. Primary subject classification containment. No component ownership, implementation containment, Home, dependency or realisation is implied."
        }
      ]
    },
    {
      "identifier": "id-3ad53e03beaa46908ae9fcfec6f022cc",
      "source": "id-6868d76816f853a3876146eb6a80d6fd",
      "target": "id-9bdc892bfc1b564c97f85b758ed6a275",
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
        "selector": "Consolidated scope / Composition containment and distinct authoritative Home; Existing Subject G10.03",
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
          "text": "Existing Subject G10.03. Composition defines containment; the separate directed child-to-parent Home Association identifies authoritative architectural location. No new statement allocation or component responsibility is asserted."
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
      "identifier": "id-94af484d1885402383558d62bf2e238e",
      "source": "id-6868d76816f853a3876146eb6a80d6fd",
      "target": "id-02fb026d5d5d4a2aaaf22f3c6db5781f",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect authorised Subject hierarchy and Home completion; result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "selector": "Consolidated scope / Composition containment and distinct authoritative Home; Authored Grouping id-02fb026d5d5d4a2aaaf22f3c6db5781f",
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
          "text": "Authored Grouping id-02fb026d5d5d4a2aaaf22f3c6db5781f. Composition defines containment; the separate directed child-to-parent Home Association identifies authoritative architectural location. No new statement allocation or component responsibility is asserted."
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
    },
    {
      "identifier": "id-c304d915b248493bb2c7c9d57f9bc4e9",
      "source": "id-6868d76816f853a3876146eb6a80d6fd",
      "target": "id-0336eab69f074587b7d11c5919c1d137",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect authorised Subject hierarchy and Home completion; result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md",
        "selector": "Consolidated scope / Composition containment and distinct authoritative Home; Authored Grouping id-0336eab69f074587b7d11c5919c1d137",
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
          "text": "Authored Grouping id-0336eab69f074587b7d11c5919c1d137. Composition defines containment; the separate directed child-to-parent Home Association identifies authoritative architectural location. No new statement allocation or component responsibility is asserted."
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
    "sourceId": "G10.03",
    "role": "subject subgroup"
  },
  "exchange": {
    "identifier": "id-6868d76816f853a3876146eb6a80d6fd",
    "properties": []
  }
}
---

# Configuration Management

## Architectural content

Subject subgroup **G10.03 — Configuration Management** in the accepted Iteration 001 hierarchy.

Management of environment-specific configuration and deployment parameters.

This is subject organisation of architectural statements. Explicit Composition denotes subject containment within this register; it establishes no component ownership, implementation containment, logical Home or naming ancestry. Grouping itself supplies no additional relationship.
