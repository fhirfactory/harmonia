---
{
  "schemaVersion": 1,
  "canonicalId": "4e648bacd2484ba7bacafcfa12c87af2",
  "archimateType": "Requirement",
  "specialization": "UseCase",
  "displayName": "[UseCase] Request Consent Renewal",
  "friendlyName": "ConsentAndInformationSharing.ConsentCaptureAndMaintenance.RequestConsentRenewal",
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
    "solutionFacetCanonicalId": "884f06591f6c4dcf8e7f94e4b31269c1",
    "localName": "Request Consent Renewal"
  },
  "provenance": [
    {
      "source": "openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md",
      "selector": "Purpose; Requirements / Request Consent Renewal",
      "role": "Qualified source contribution reconciled under the operative ADD authority",
      "revision": "c08a02a5569ead5805a46048da486db5365adc7c",
      "sha256": "fb9536844060e27d4f6fb66dd077348635e3b957f693dcc3f8c4c238b4eb105b"
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
      "source": "openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md",
      "selector": "UseCase"
    }
  ],
  "relationships": [
    {
      "identifier": "id-48fe92643e7e4834aeb3265faf626647",
      "archimateType": "Association",
      "source": "id-8fa20b10b58251dd8f54919123a00865",
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
            "source": "openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md",
            "sourceId": "UC-Clinician-Consent-13",
            "assessmentRow": 79,
            "scopeQualification": "Partial scope contribution; no satisfaction, implementation, ownership or release-allocation claim"
          }
        ]
      },
      "documentation": [
        {
          "text": "Interaction contributes to this Journey in its stated source context; association is optional and does not imply executable sequencing.",
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
      "identifier": "id-152a323409a44ed29e6934ecb69f5c69",
      "archimateType": "Association",
      "source": "id-8fa20b10b58251dd8f54919123a00865",
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
            "source": "openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md",
            "sourceId": "UC-Clinician-Consent-13",
            "assessmentRow": 79,
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
    },
    {
      "identifier": "id-6d9ee80e34994f82ac9bcb274ea0b07b",
      "archimateType": "Association",
      "source": "id-8fa20b10b58251dd8f54919123a00865",
      "target": "id-41ed4c2b5b7f516caa260ae81b578308",
      "standing": {
        "semantic": "Authorised 1.1.21 incorporation candidate; exact assertions pending Solution Architect result review",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md",
        "selector": "Accepted semantics; Authorised Activity / Scope",
        "contributions": [
          {
            "source": "openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md",
            "sourceId": "UC-Clinician-Consent-13",
            "assessmentRow": 79,
            "scopeQualification": "Partial scope contribution; no satisfaction, implementation, ownership or release-allocation claim"
          }
        ]
      },
      "documentation": [
        {
          "text": "This interaction contributes to the independently expressed need evidenced by its source. Its successful execution is insufficient to prove that the broader need is satisfied.",
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
      "source": "openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md",
      "sourceId": "UC-Clinician-Consent-13",
      "assessmentRow": 79,
      "originalText": "Request Consent Renewal",
      "primaryActor": "Clinician (Treating Clinician)"
    }
  ],
  "exchange": {
    "identifier": "id-8fa20b10b58251dd8f54919123a00865",
    "properties": []
  }
}
---

# [UseCase] Request Consent Renewal

## Architectural content

Recognisable interaction: **Request Consent Renewal**.

The interaction is directed towards the source purpose: ongoing care remains appropriately authorised

Source primary actor: Clinician (Treating Clinician)

The interaction retains its source context and conditions. It is not a Journey, Capability or claim of implementation satisfaction.

### Reconciled source contributions

- `UC-Clinician-Consent-13` — Request Consent Renewal; Scope - Consent – Internal Clinicians; [source](../../../../../openspec/specs/consent-and-information-sharing/uc-clinician-consent-13-request-consent-renewal/spec.md).

Source-specific access conditions, supporting actors, alternative outcomes, verification and data-authority rules remain distinct in their cited source contributions. Legacy claims that each statement is a business capability are not adopted: this instance has native Requirement semantics. Source target-state scope is not a software delivery commitment for 1.1.21. Historical assessment findings remain recoverable and do not establish current coverage.
