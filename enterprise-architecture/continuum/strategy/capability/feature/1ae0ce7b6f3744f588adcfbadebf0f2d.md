---
{
  "schemaVersion": 1,
  "canonicalId": "1ae0ce7b6f3744f588adcfbadebf0f2d",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] LongFollowUpIdentifiable",
  "friendlyName": "CareCoordinationAndContinuity.FollowUpAndOngoingCareArrangement.DeferredClinicalFollowUpAccountability.LongFollowUpIdentifiable",
  "canonicalName": "CT1-CareCoordinationAndContinuity.CT2-FollowUpAndOngoingCareArrangement.CT3-DeferredClinicalFollowUpAccountability.FEAT-LongFollowUpIdentifiable",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B05.05.04.F01",
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
        "key": "B05.05",
        "type": "capability",
        "name": "Follow-Up & Ongoing Care Arrangement",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B05.05.04",
        "type": "capability",
        "name": "Deferred Clinical Follow-Up Accountability",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B05.05.04.F01",
        "type": "feature",
        "name": "LongFollowUpIdentifiable",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B05.05.04.F01",
    "parent": "B05.05.04",
    "tier": "Feature",
    "sourceName": "LongFollowUpIdentifiable",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B05.05.04.F01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/435",
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
      "selector": "B05.05.04.F01"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-832b1d8eae86547daccb40e2da7f7aa7",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B05.05.04.F01",
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

# [Feature] LongFollowUpIdentifiable

## Architectural content

A follow-up obligation remains identifiable over multi-day or multi-week periods.

### Incorporation standing

Catalogue identity `B05.05.04.F01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
