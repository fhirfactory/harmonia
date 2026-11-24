---
{
  "schemaVersion": 1,
  "canonicalId": "e453905e7aef4db2aba07de5efc932bc",
  "archimateType": "ValueStream",
  "specialization": "Journey",
  "displayName": "Establish collaborative clinical context",
  "friendlyName": "EstablishCollaborativeClinicalContext",
  "canonicalName": null,
  "standing": {
    "semantic": "Authorised 1.1.21 incorporation candidate; exact incorporated result pending Solution Architect review",
    "currentness": "current"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/stakeholder-journey-requirements-profile.md",
    "mode": "Explicit local semantic name; independent of identity and presentation"
  },
  "provenance": [
    {
      "source": "/home/hunterm/Development/Code/harmonia/enterprise-architecture/continuum/strategy/value-stream/3ba98c0c1170458f8c02fa76694fee67.md",
      "selector": "Value Transformation Stages / S4",
      "role": "Reconciled architectural progression/outcome evidence",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "3903027b42ddcb0265a379b0f44d1599fb6c8bf45a2f24beecce3ce959c33607"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
      "selector": "Authorised Activity / Scope",
      "role": "Operative ADD authority and accepted semantics",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
      "selector": "journey/collaboration-context"
    }
  ],
  "relationships": [
    {
      "identifier": "id-e3c4e988f6004325a0bf5ec9f9565064",
      "archimateType": "Triggering",
      "source": "id-50d837d39e965ed9a48ff03c572c7f81",
      "target": "id-7e14054026c55514a33d6be267345bc0",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          [
            [
              "/home/hunterm/Development/Code/harmonia/enterprise-architecture/continuum/strategy/value-stream/3ba98c0c1170458f8c02fa76694fee67.md",
              "## Architectural content"
            ]
          ]
        ]
      },
      "documentation": [
        {
          "text": "Meaningful progression on the primary path where the target prerequisites hold. Conditional, adverse or incomplete outcomes may prevent progression; no execution orchestration is selected.",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ]
    }
  ],
  "unresolvedProperties": {
    "canonicalName": "No formal Journey qualifier has been authorised; local friendlyName does not establish a formal naming context. HCRL DR-23."
  },
  "journeyDefinition": {
    "intendedOutcome": "Relevant longitudinal facts, active clinical context and diagnostic findings are available to the collaboration.",
    "startCondition": "The relevant input or preceding condition in Distributed Clinical Knowledge → Informed Clinical Collaboration is available.",
    "alternativeOutcomes": [
      "Missing prerequisites or unresolved governance conditions prevent a claimed successful outcome."
    ]
  },
  "exchange": {
    "identifier": "id-50d837d39e965ed9a48ff03c572c7f81",
    "properties": []
  }
}
---

# Establish collaborative clinical context

## Architectural content

The relevant input or preceding condition in Distributed Clinical Knowledge → Informed Clinical Collaboration is available. Progresses towards: Relevant longitudinal facts, active clinical context and diagnostic findings are available to the collaboration.

### Alternative outcomes and conditions

- Missing prerequisites or unresolved governance conditions prevent a claimed successful outcome.

The source stage describes the intended transformation. This definition retains that context and its authority boundaries; constituent naming does not substitute internal execution mechanics for progression.

This is a reusable Journey definition. Actual progress, decisions and outcomes belong to particular occurrences. It selects no workflow engine, orchestration, runtime state machine or execution owner.
