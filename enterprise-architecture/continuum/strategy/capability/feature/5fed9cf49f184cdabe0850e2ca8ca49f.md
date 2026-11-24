---
{
  "schemaVersion": 1,
  "canonicalId": "5fed9cf49f184cdabe0850e2ca8ca49f",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Cross-Episode Information Availability",
  "friendlyName": "CareCoordinationAndContinuity.LongitudinalCareCoordination.CrossEpisodeCareContext.CrossEpisodeInformationAvailability",
  "canonicalName": "CT1-CareCoordinationAndContinuity.CT2-LongitudinalCareCoordination.CT3-CrossEpisodeCareContext.FEAT-CrossEpisodeInformationAvailability",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B05.04.02.F03",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B05",
        "type": "capability",
        "name": "Care Coordination & Continuity",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B05.04",
        "type": "capability",
        "name": "Longitudinal Care Coordination",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B05.04.02",
        "type": "capability",
        "name": "Cross-Episode Care Context",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B05.04.02.F03",
        "type": "feature",
        "name": "Cross-Episode Information Availability",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B05.04.02.F03",
    "parent": "B05.04.02",
    "tier": "Feature",
    "sourceName": "Cross-Episode Information Availability",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B05.04.02.F03",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/407",
      "role": "Exact source contributions, original wording/criteria/status and accepted adjudication correspondence; historical assurance evidence",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
      "selector": "Authorised Activity / Scope / B",
      "role": "Explicit canonical incorporation and UUID allocation authority",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B05.04.02.F03"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-39fe1b53a2a2536a8f89986d9042a955",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B05.04.02.F03",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "Feature",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "FT",
            "attributes": {}
          }
        ]
      },
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
        "ref": "propid-6",
        "values": [
          {
            "text": "Accepted",
            "attributes": {}
          }
        ]
      }
    ]
  }
}
---

# [Feature] Cross-Episode Information Availability

## Architectural content

Cross-episode information is available.

### Incorporation standing

Catalogue identity `B05.04.02.F03`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
