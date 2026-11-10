---
{
  "schemaVersion": 1,
  "canonicalId": "bf0a6df94e224e3cb4398f0fdecbad76",
  "archimateType": "Requirement",
  "specialization": "UserStory",
  "displayName": "[UserStory] I only disclose information as authorised",
  "friendlyName": "ConsentAndInformationSharing.ConsentScopeAndPreferences.IOnlyDiscloseInformationAsAuthorised",
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
    "solutionFacetCanonicalId": "e48346674b0740c19f4a13cc50a7ae66",
    "localName": "I only disclose information as authorised"
  },
  "provenance": [
    {
      "source": "openspec/specs/consent-and-information-sharing/uc-externalclinician-consent-11-view-participant-sharing-preferences/spec.md",
      "selector": "Source Traceability / User Story",
      "role": "Qualified source contribution reconciled under the operative ADD authority",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "ca476ce65f0f43ffdb8d122382123e14dc6ac2ca933ff5224f900f4375140b54"
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
      "source": "openspec/specs/consent-and-information-sharing/uc-externalclinician-consent-11-view-participant-sharing-preferences/spec.md",
      "selector": "UserStory"
    }
  ],
  "relationships": [
    {
      "identifier": "id-c8cf2a77bf064f35878fbcc36699526e",
      "archimateType": "Association",
      "source": "id-919f5a556448558b963a8d5a122ce392",
      "target": "id-37c2ba58273f596e9c82f751b363ee01",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/consent-and-information-sharing/uc-externalclinician-consent-11-view-participant-sharing-preferences/spec.md",
            "sourceId": "UC-ExternalClinician-Consent-11",
            "assessmentRow": 116,
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
      "identifier": "id-e80f3e63335e492d880613fbadf82324",
      "archimateType": "Association",
      "source": "id-919f5a556448558b963a8d5a122ce392",
      "target": "id-8d5101a06efb5c9c960c89bdd30d457f",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/consent-and-information-sharing/uc-externalclinician-consent-11-view-participant-sharing-preferences/spec.md",
            "sourceId": "UC-ExternalClinician-Consent-11",
            "assessmentRow": 116,
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
      "source": "openspec/specs/consent-and-information-sharing/uc-externalclinician-consent-11-view-participant-sharing-preferences/spec.md",
      "sourceId": "UC-ExternalClinician-Consent-11",
      "assessmentRow": 116,
      "originalText": "Understand the participant's information sharing preferences so that I only disclose information as authorised.",
      "primaryActor": "External ClinicianProvider (Physiotherapist)"
    }
  ],
  "exchange": {
    "identifier": "id-919f5a556448558b963a8d5a122ce392",
    "properties": []
  }
}
---

# [UserStory] I only disclose information as authorised

## Architectural content

Independent stakeholder need, expectation or desired outcome:

> Understand the participant's information sharing preferences so that I only disclose information as authorised.

The need is independently identifiable. It may encompass interactions, governance, human and organisational activity beyond any associated UseCase. Successful execution of associated interactions does not establish satisfaction of this need.

### Reconciled source contributions

- `UC-ExternalClinician-Consent-11` — View Participant Sharing Preferences; Scope - Information Sharing; [source](../../../../../openspec/specs/consent-and-information-sharing/uc-externalclinician-consent-11-view-participant-sharing-preferences/spec.md).

Source-specific access conditions, supporting actors, alternative outcomes, verification and data-authority rules remain distinct in their cited source contributions. Legacy claims that each statement is a business capability are not adopted: this instance has native Requirement semantics. Source target-state scope is not a software delivery commitment for 1.1.21. Historical assessment findings remain recoverable and do not establish current coverage.
