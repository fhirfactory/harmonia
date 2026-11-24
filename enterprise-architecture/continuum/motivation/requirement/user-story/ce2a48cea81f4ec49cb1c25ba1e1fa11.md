---
{
  "schemaVersion": 1,
  "canonicalId": "ce2a48cea81f4ec49cb1c25ba1e1fa11",
  "archimateType": "Requirement",
  "specialization": "UserStory",
  "displayName": "[UserStory] Unapproved changes are prevented",
  "friendlyName": "TerminologyAndReferenceData.ReferenceDataManagement.UnapprovedChangesArePrevented",
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
    "solutionFacetCanonicalId": "ba0e7f67e2e7496d9df8f6617ee39ec3",
    "localName": "Unapproved changes are prevented"
  },
  "provenance": [
    {
      "source": "openspec/specs/provider-directory-integration/sr-55-restrict-changes-to-reference-data-sets/spec.md",
      "selector": "Source Traceability / User Story",
      "role": "Qualified source contribution reconciled under the operative ADD authority",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "e456abe1132dbca30219155dc13a261856e0f8580033a82d00ed3c82fc9cc393"
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
      "source": "openspec/specs/provider-directory-integration/sr-55-restrict-changes-to-reference-data-sets/spec.md",
      "selector": "UserStory"
    }
  ],
  "relationships": [
    {
      "identifier": "id-88332a54a3f54342be373141f020cd3c",
      "archimateType": "Association",
      "source": "id-08c53dd5419b54a18ecb2eefacf9ebb8",
      "target": "id-3262759ee57755b4b8d5f1e6620d9f22",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/provider-directory-integration/sr-55-restrict-changes-to-reference-data-sets/spec.md",
            "sourceId": "SR-55",
            "assessmentRow": 543,
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
      "identifier": "id-72e8688ba18f4dacb671fd9702a75652",
      "archimateType": "Association",
      "source": "id-08c53dd5419b54a18ecb2eefacf9ebb8",
      "target": "id-50a97c4a659359afb4fb068f2defe408",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/provider-directory-integration/sr-55-restrict-changes-to-reference-data-sets/spec.md",
            "sourceId": "SR-55",
            "assessmentRow": 543,
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
      "source": "openspec/specs/provider-directory-integration/sr-55-restrict-changes-to-reference-data-sets/spec.md",
      "sourceId": "SR-55",
      "assessmentRow": 543,
      "originalText": "As a Provider Directory Administrator, I want to limit changes to reference data sets to specific roles only, so that unapproved changes are prevented.",
      "primaryActor": "Provider Directory Administrator"
    }
  ],
  "exchange": {
    "identifier": "id-08c53dd5419b54a18ecb2eefacf9ebb8",
    "properties": []
  }
}
---

# [UserStory] Unapproved changes are prevented

## Architectural content

Independent stakeholder need, expectation or desired outcome:

> As a Provider Directory Administrator, I want to limit changes to reference data sets to specific roles only, so that unapproved changes are prevented.

The need is independently identifiable. It may encompass interactions, governance, human and organisational activity beyond any associated UseCase. Successful execution of associated interactions does not establish satisfaction of this need.

### Reconciled source contributions

- `SR-55` — Restrict Changes to Reference Data Sets; Additional Source Requirements – SR-26 to SR-58; [source](../../../../../openspec/specs/provider-directory-integration/sr-55-restrict-changes-to-reference-data-sets/spec.md).

Source-specific access conditions, supporting actors, alternative outcomes, verification and data-authority rules remain distinct in their cited source contributions. Legacy claims that each statement is a business capability are not adopted: this instance has native Requirement semantics. Source target-state scope is not a software delivery commitment for 1.1.21. Historical assessment findings remain recoverable and do not establish current coverage.
