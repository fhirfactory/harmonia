---
{
  "schemaVersion": 1,
  "canonicalId": "fd9d0fa7117a4dc3b45cfed0e7be614b",
  "archimateType": "Grouping",
  "specialization": "SolutionFacet",
  "displayName": "Provider and Workforce Management",
  "friendlyName": "ProviderAndWorkforceManagement",
  "canonicalName": null,
  "standing": {
    "semantic": "Solution Architect accepted CR-008; incorporated-result review pending",
    "currentness": "current"
  },
  "identity": {
    "authority": "enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-2",
    "acceptanceTask": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
    "changeReview": "enterprise-architecture/design/review/1.1.18/change-review.md#cr-008",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "mode": "Explicit Solution Architect-authored displayName and friendlyName; no new formal qualifier or naming ancestry",
    "expected": {
      "displayName": "Provider and Workforce Management",
      "friendlyName": "ProviderAndWorkforceManagement",
      "canonicalName": null
    },
    "authoredNativeName": "[SolutionFacet] ProviderAndWorkforceManagement",
    "reconciliationAuthority": "enterprise-architecture/design/review/1.1.19/hcrl-1.1.19-001.pending.md#dr-17"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
      "selector": "DR-2 / CR-008",
      "role": "Operative Solution Architect acceptance and incorporation authority; not HCRL closure or release transition",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
      "sha256": "4cdc2217cc0bb8e8d2296537a1228cf7c7423ee423103d6d56857f87c57584dc"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md",
      "selector": "Proposed Subject extensions / G15",
      "role": "Accepted Subject name and concern through the operative Task; mapping alone is not architectural authority",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md",
      "sha256": "b6f59ce36ad4b7719fd0e954e4c4b0e86a874fe5cab2459c1804019e43944094"
    },
    {
      "source": "enterprise-architecture/design/review/1.1.18/change-review.md",
      "selector": "#cr-008",
      "role": "Accepted bounded Subject instance proposal; original review preserved in before/subject-incorporation",
      "maintained": true
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.19/001/before/Harmonia-Continuum-1.1.19-001.xml",
      "selector": "element[@identifier=\"id-a5fe9dd5dcfd502dbdfea4ab86d0e26d\"]",
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
      "source": "enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md",
      "selector": "G15"
    },
    {
      "source": "enterprise-architecture/design/review/1.1.18/change-review.md",
      "selector": "#cr-008"
    }
  ],
  "relationships": [
    {
      "identifier": "id-3373898f24364cdbb160877ef3736585",
      "source": "id-a5fe9dd5dcfd502dbdfea4ab86d0e26d",
      "target": "id-895be9de41a9574e8866fc73571a0cf2",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect accepted CR-039; incorporated-result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "sha256": "4cdc2217cc0bb8e8d2296537a1228cf7c7423ee423103d6d56857f87c57584dc",
        "selector": "DR-2 / CR-039",
        "meaning": "Accepted Subject subgroup classification containment; no Motivation allocation or Capability relationship",
        "review": "enterprise-architecture/design/review/1.1.18/change-review.md#cr-039"
      },
      "name": [
        {
          "text": "Subject subgroup containment"
        }
      ],
      "documentation": [
        {
          "text": "Accepted CR-039: G15 → G15.01. Subject classification only; no Capability decomposition, Component ownership, logical Home, implementation responsibility or Motivation allocation."
        }
      ],
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
          "ref": "propid-54",
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
      "identifier": "id-3af26f94665b4e2aac09061aa7a39e83",
      "source": "id-a5fe9dd5dcfd502dbdfea4ab86d0e26d",
      "target": "id-c76e998defa35740bdf92bea51aaf54d",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect accepted CR-040; incorporated-result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "sha256": "4cdc2217cc0bb8e8d2296537a1228cf7c7423ee423103d6d56857f87c57584dc",
        "selector": "DR-2 / CR-040",
        "meaning": "Accepted Subject subgroup classification containment; no Motivation allocation or Capability relationship",
        "review": "enterprise-architecture/design/review/1.1.18/change-review.md#cr-040"
      },
      "name": [
        {
          "text": "Subject subgroup containment"
        }
      ],
      "documentation": [
        {
          "text": "Accepted CR-040: G15 → G15.02. Subject classification only; no Capability decomposition, Component ownership, logical Home, implementation responsibility or Motivation allocation."
        }
      ],
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
          "ref": "propid-54",
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
      "identifier": "id-44c480f471ac4911bcff69d5e47f0448",
      "source": "id-a5fe9dd5dcfd502dbdfea4ab86d0e26d",
      "target": "id-3ee9a01480ae5f8bb48fa6ad7148859f",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect accepted CR-041; incorporated-result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "sha256": "4cdc2217cc0bb8e8d2296537a1228cf7c7423ee423103d6d56857f87c57584dc",
        "selector": "DR-2 / CR-041",
        "meaning": "Accepted Subject subgroup classification containment; no Motivation allocation or Capability relationship",
        "review": "enterprise-architecture/design/review/1.1.18/change-review.md#cr-041"
      },
      "name": [
        {
          "text": "Subject subgroup containment"
        }
      ],
      "documentation": [
        {
          "text": "Accepted CR-041: G15 → G15.03. Subject classification only; no Capability decomposition, Component ownership, logical Home, implementation responsibility or Motivation allocation."
        }
      ],
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
          "ref": "propid-54",
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
      "identifier": "id-85b62f6ca72645ed95c855839b2cbcb9",
      "source": "id-a5fe9dd5dcfd502dbdfea4ab86d0e26d",
      "target": "id-bb0b8df89b025d5081ea84e83bce6d83",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect accepted CR-042; incorporated-result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "sha256": "4cdc2217cc0bb8e8d2296537a1228cf7c7423ee423103d6d56857f87c57584dc",
        "selector": "DR-2 / CR-042",
        "meaning": "Accepted Subject subgroup classification containment; no Motivation allocation or Capability relationship",
        "review": "enterprise-architecture/design/review/1.1.18/change-review.md#cr-042"
      },
      "name": [
        {
          "text": "Subject subgroup containment"
        }
      ],
      "documentation": [
        {
          "text": "Accepted CR-042: G15 → G15.04. Subject classification only; no Capability decomposition, Component ownership, logical Home, implementation responsibility or Motivation allocation."
        }
      ],
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
          "ref": "propid-54",
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
      "identifier": "id-e10c2ecfa9134d048f731e071d6df484",
      "source": "id-a5fe9dd5dcfd502dbdfea4ab86d0e26d",
      "target": "id-0dfc3ee08d0557d5bb5212b6eb8a5c52",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "semantic": "Solution Architect accepted CR-043; incorporated-result review pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md",
        "sha256": "4cdc2217cc0bb8e8d2296537a1228cf7c7423ee423103d6d56857f87c57584dc",
        "selector": "DR-2 / CR-043",
        "meaning": "Accepted Subject subgroup classification containment; no Motivation allocation or Capability relationship",
        "review": "enterprise-architecture/design/review/1.1.18/change-review.md#cr-043"
      },
      "name": [
        {
          "text": "Subject subgroup containment"
        }
      ],
      "documentation": [
        {
          "text": "Accepted CR-043: G15 → G15.05. Subject classification only; no Capability decomposition, Component ownership, logical Home, implementation responsibility or Motivation allocation."
        }
      ],
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
          "ref": "propid-54",
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
  "unresolvedProperties": {
    "canonicalName": "The inherited formal-name vocabulary qualification remains unresolved for this Grouping / SolutionFacet. This terminology refactoring authorises no formal qualifier or additional naming ancestry; see HCRL 1.1.19-001 DR-14/DR-17."
  },
  "subjectGroup": {
    "sourceId": "G15",
    "role": "subject group"
  },
  "exchange": {
    "identifier": "id-a5fe9dd5dcfd502dbdfea4ab86d0e26d",
    "properties": []
  }
}
---

# Provider and Workforce Management

## Architectural content

This existing element is a **SolutionFacet**, a specialisation of native
**Grouping**. The [governed definition](../../governance/continuum-representation.md#solutionfacet-classification--1119-candidate)
distinguishes accessible solution classification from Capability, responsibility
and ownership. Its canonical identity, G01–G18 context, existing Composition
and separately established Home assertions are preserved.

SolutionFacet group **G15 — Provider and Workforce Management** in the Solution Architect-accepted G14–G18 extension.

Business-functional motivation grouping for provider and workforce management.

This is classification of architectural statements. Explicit Composition denotes the accepted parent/subgroup classification only; it establishes no Capability decomposition, Component ownership, logical Home, implementation responsibility, Motivation allocation or naming ancestry. The classification code is a source-qualified reference, not canonical identity.

Acceptance and incorporation are bounded by enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-2, CR-008 and the operative retained Task. Existing G01–G13, the 784 Motivation candidates and their separate unresolved review matters remain unchanged. No clinical, workforce, source-domain or Capability authority follows from this classification.
