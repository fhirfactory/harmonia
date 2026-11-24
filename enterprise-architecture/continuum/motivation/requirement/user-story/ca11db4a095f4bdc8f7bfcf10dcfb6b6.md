---
{
  "schemaVersion": 1,
  "canonicalId": "ca11db4a095f4bdc8f7bfcf10dcfb6b6",
  "archimateType": "Requirement",
  "specialization": "UserStory",
  "displayName": "[UserStory] Records can be linked across systems",
  "friendlyName": "PatientIdentityAndDemographcis.IdentityMatchingAndReconciliation.RecordsCanBeLinkedAcrossSystems",
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
    "mode": "Explicit local semantic name; independent of identity and presentation",
    "solutionFacetCanonicalId": "b91ec2110aaf4d44bd043bb787fbd910",
    "localName": "Records can be linked across systems"
  },
  "provenance": [
    {
      "source": "openspec/specs/patient-identity-management/uc-integration-identity-18-retrieve-patient-identifier-cross-references/spec.md",
      "selector": "Source Traceability / User Story",
      "role": "Qualified source contribution reconciled under the operative ADD authority",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "55d3d122831956ff6a6a84f1e9ce8628a266321e6a7db0c60bc01a2add37eb44"
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
      "source": "openspec/specs/patient-identity-management/uc-integration-identity-18-retrieve-patient-identifier-cross-references/spec.md",
      "selector": "UserStory"
    }
  ],
  "relationships": [
    {
      "identifier": "id-7d3416e308d54869a32d804ed3e15fd7",
      "archimateType": "Association",
      "source": "id-2fe36cc683ce51b3aecbf4fd3afaa316",
      "target": "id-193667f66ec7586b9c9822bcf3e795e3",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/patient-identity-management/uc-integration-identity-18-retrieve-patient-identifier-cross-references/spec.md",
            "sourceId": "UC-Integration-Identity-18",
            "assessmentRow": 308,
            "scopeQualification": "Partial scope contribution; no satisfaction, implementation, ownership or release-allocation claim"
          }
        ]
      },
      "documentation": [
        {
          "text": "Independent stakeholder need is relevant to this Journey; the Journey and associated interactions do not by themselves establish satisfaction.",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "attributes": {
        "isDirected": "true"
      }
    },
    {
      "identifier": "id-f29b1c9747114d878b69e7ef84773022",
      "archimateType": "Association",
      "source": "id-2fe36cc683ce51b3aecbf4fd3afaa316",
      "target": "id-5772a579c8d45c0eb0d665eeac646925",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/patient-identity-management/uc-integration-identity-18-retrieve-patient-identifier-cross-references/spec.md",
            "sourceId": "UC-Integration-Identity-18",
            "assessmentRow": 308,
            "scopeQualification": "Partial scope contribution; no satisfaction, implementation, ownership or release-allocation claim"
          }
        ]
      },
      "documentation": [
        {
          "text": "Partial architectural scope contribution, reconciled against the accepted capability definition; no complete coverage, Realization, ownership or implementation satisfaction is asserted.",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "attributes": {
        "isDirected": "true"
      }
    }
  ],
  "unresolvedProperties": {
    "canonicalName": "SolutionFacet formal naming vocabulary remains unestablished; no new qualifier inferred. See inherited HCRL DR-14 and incorporation DR-23."
  },
  "sourceContributions": [
    {
      "source": "openspec/specs/patient-identity-management/uc-integration-identity-18-retrieve-patient-identifier-cross-references/spec.md",
      "sourceId": "UC-Integration-Identity-18",
      "assessmentRow": 308,
      "originalText": "Retrieve mappings between local and enterprise patient identifiers so that records can be linked across systems.",
      "primaryActor": "Integration Service / Integration Platform"
    }
  ],
  "exchange": {
    "identifier": "id-2fe36cc683ce51b3aecbf4fd3afaa316",
    "properties": []
  }
}
---

# [UserStory] Records can be linked across systems

## Architectural content

Independent stakeholder need, expectation or desired outcome:

> Retrieve mappings between local and enterprise patient identifiers so that records can be linked across systems.

The need is independently identifiable. It may encompass interactions, governance, human and organisational activity beyond any associated UseCase. Successful execution of associated interactions does not establish satisfaction of this need.

### Reconciled source contributions

- `UC-Integration-Identity-18` — Retrieve Patient Identifier Cross References; Scope - Identity Query and Retrieval; [source](../../../../../openspec/specs/patient-identity-management/uc-integration-identity-18-retrieve-patient-identifier-cross-references/spec.md).

Source-specific access conditions, supporting actors, alternative outcomes, verification and data-authority rules remain distinct in their cited source contributions. Legacy claims that each statement is a business capability are not adopted: this instance has native Requirement semantics. Source target-state scope is not a software delivery commitment for 1.1.21. Historical assessment findings remain recoverable and do not establish current coverage.
