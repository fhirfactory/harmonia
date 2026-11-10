---
{
  "schemaVersion": 1,
  "canonicalId": "24c784ddaadc4ddaaa8f86878facf79d",
  "archimateType": "Requirement",
  "specialization": "UserStory",
  "displayName": "[UserStory] Downstream systems maintain consistent patient information",
  "friendlyName": "PatientIdentityAndDemographcis.IdentityRegistrationAndVerification.DownstreamSystemsMaintainConsistentPatientInformation",
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
    "solutionFacetCanonicalId": "5fcada54e6df40a8983335a90e4a9f02",
    "localName": "Downstream systems maintain consistent patient information"
  },
  "provenance": [
    {
      "source": "openspec/specs/patient-identity-management/uc-integration-identity-9-receive-golden-patient-identity/spec.md",
      "selector": "Source Traceability / User Story",
      "role": "Qualified source contribution reconciled under the operative ADD authority",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "976b1a34a397002961e56fe2a9375f1896e1f326e7cdabc29f10743bd87916d0"
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
      "source": "openspec/specs/patient-identity-management/uc-integration-identity-9-receive-golden-patient-identity/spec.md",
      "selector": "UserStory"
    }
  ],
  "relationships": [
    {
      "identifier": "id-a217cb6aae3d4a0c8af857477fa4c260",
      "archimateType": "Association",
      "source": "id-58912546dab75d198294624a9a825770",
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
            "source": "openspec/specs/patient-identity-management/uc-integration-identity-9-receive-golden-patient-identity/spec.md",
            "sourceId": "UC-Integration-Identity-9",
            "assessmentRow": 334,
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
      "identifier": "id-09240f0d26ad45a4be039d55e23517ff",
      "archimateType": "Association",
      "source": "id-58912546dab75d198294624a9a825770",
      "target": "id-d1be1f39d0ef5ff3863043e2dc489e03",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/patient-identity-management/uc-integration-identity-9-receive-golden-patient-identity/spec.md",
            "sourceId": "UC-Integration-Identity-9",
            "assessmentRow": 334,
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
      "source": "openspec/specs/patient-identity-management/uc-integration-identity-9-receive-golden-patient-identity/spec.md",
      "sourceId": "UC-Integration-Identity-9",
      "assessmentRow": 334,
      "originalText": "Receive the authoritative patient identity record so that downstream systems maintain consistent patient information.",
      "primaryActor": "Downstream Consumer System System"
    }
  ],
  "exchange": {
    "identifier": "id-58912546dab75d198294624a9a825770",
    "properties": []
  }
}
---

# [UserStory] Downstream systems maintain consistent patient information

## Architectural content

Independent stakeholder need, expectation or desired outcome:

> Receive the authoritative patient identity record so that downstream systems maintain consistent patient information.

The need is independently identifiable. It may encompass interactions, governance, human and organisational activity beyond any associated UseCase. Successful execution of associated interactions does not establish satisfaction of this need.

### Reconciled source contributions

- `UC-Integration-Identity-9` — Receive Golden Patient Identity; Scope - Identity Matching and Resolution; [source](../../../../../openspec/specs/patient-identity-management/uc-integration-identity-9-receive-golden-patient-identity/spec.md).

Source-specific access conditions, supporting actors, alternative outcomes, verification and data-authority rules remain distinct in their cited source contributions. Legacy claims that each statement is a business capability are not adopted: this instance has native Requirement semantics. Source target-state scope is not a software delivery commitment for 1.1.21. Historical assessment findings remain recoverable and do not establish current coverage.
