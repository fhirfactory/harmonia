---
{
  "schemaVersion": 1,
  "canonicalId": "fcdfdb5dedd2410f8d0301a34cfce1d6",
  "archimateType": "Requirement",
  "specialization": "UserStory",
  "displayName": "[UserStory] Current information is available in the Provider Directory",
  "friendlyName": "ProviderDirectory.DirectoryPublicationAndVisibility.CurrentInformationIsAvailableInTheProviderDirectory",
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
    "solutionFacetCanonicalId": "b6a20afe29624f73906e5e852d6f2fbf",
    "localName": "Current information is available in the Provider Directory"
  },
  "provenance": [
    {
      "source": "openspec/specs/cross-domain-services/uc-clinician-directory-20-publish-my-profile/spec.md",
      "selector": "Source Traceability / User Story",
      "role": "Qualified source contribution reconciled under the operative ADD authority",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "21ec58f540ac3b7bec4ead1d279a0a8b570d98b2f17f9087892e4142a45c1dde"
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
      "source": "openspec/specs/cross-domain-services/uc-clinician-directory-20-publish-my-profile/spec.md",
      "selector": "UserStory"
    }
  ],
  "relationships": [
    {
      "identifier": "id-fc78386f5f9d4ab88b101506352d0107",
      "archimateType": "Association",
      "source": "id-e1df631b5c7b5b739b3c3365aaa1aa48",
      "target": "id-577fbdb4938c570b8f107bddd69bc036",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/cross-domain-services/uc-clinician-directory-20-publish-my-profile/spec.md",
            "sourceId": "UC-Clinician-Directory-20",
            "assessmentRow": 216,
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
      "identifier": "id-edfda15817994d478a1a8636ed6c66f3",
      "archimateType": "Association",
      "source": "id-e1df631b5c7b5b739b3c3365aaa1aa48",
      "target": "id-f35bb78cd8835e119d11ac65e1b11501",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/cross-domain-services/uc-clinician-directory-20-publish-my-profile/spec.md",
            "sourceId": "UC-Clinician-Directory-20",
            "assessmentRow": 216,
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
      "source": "openspec/specs/cross-domain-services/uc-clinician-directory-20-publish-my-profile/spec.md",
      "sourceId": "UC-Clinician-Directory-20",
      "assessmentRow": 216,
      "originalText": "Publish approved changes to my profile so that current information is available in the Provider Directory.",
      "primaryActor": null
    }
  ],
  "exchange": {
    "identifier": "id-e1df631b5c7b5b739b3c3365aaa1aa48",
    "properties": []
  }
}
---

# [UserStory] Current information is available in the Provider Directory

## Architectural content

Independent stakeholder need, expectation or desired outcome:

> Publish approved changes to my profile so that current information is available in the Provider Directory.

The need is independently identifiable. It may encompass interactions, governance, human and organisational activity beyond any associated UseCase. Successful execution of associated interactions does not establish satisfaction of this need.

### Reconciled source contributions

- `UC-Clinician-Directory-20` — Publish My Profile; Scope – Clinician Availability and Visibility; [source](../../../../../openspec/specs/cross-domain-services/uc-clinician-directory-20-publish-my-profile/spec.md).
  Primary actor is not established by this source.

Source-specific access conditions, supporting actors, alternative outcomes, verification and data-authority rules remain distinct in their cited source contributions. Legacy claims that each statement is a business capability are not adopted: this instance has native Requirement semantics. Source target-state scope is not a software delivery commitment for 1.1.21. Historical assessment findings remain recoverable and do not establish current coverage.
