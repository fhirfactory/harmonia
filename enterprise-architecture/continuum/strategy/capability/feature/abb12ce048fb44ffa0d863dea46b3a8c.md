---
{
  "schemaVersion": 1,
  "canonicalId": "abb12ce048fb44ffa0d863dea46b3a8c",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] NonTwinWorkRecognised",
  "friendlyName": "DigitalWorkEventsAndTemporalCoordination.WorkflowAndWorkProgression.NonDigitalTwinWorkflowProgression.NonTwinWorkRecognised",
  "canonicalName": "CT1-EC-DigitalWorkEventsAndTemporalCoordination.CT2-EC-WorkflowAndWorkProgression.CT3-EC-NonDigitalTwinWorkflowProgression.FEAT-NonTwinWorkRecognised",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E06.02.04.F02",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "E06",
        "type": "capability",
        "name": "Digital Work, Events & Temporal Coordination",
        "tier": "CT1",
        "category": "enterprise"
      },
      {
        "key": "E06.02",
        "type": "capability",
        "name": "Workflow & Work Progression",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E06.02.04",
        "type": "capability",
        "name": "Non-Digital-Twin Workflow Progression",
        "tier": "CT3",
        "category": "enterprise"
      },
      {
        "key": "E06.02.04.F02",
        "type": "feature",
        "name": "NonTwinWorkRecognised",
        "tier": "FT",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E06.02.04.F02",
    "parent": "E06.02.04",
    "tier": "Feature",
    "sourceName": "NonTwinWorkRecognised",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E06.02.04.F02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/2303",
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
      "selector": "E06.02.04.F02"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-e6274a1c1e4e5979822fdb98d2df2384",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E06.02.04.F02",
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

# [Feature] NonTwinWorkRecognised

## Architectural content

Non-Twin workflow can be expressed without fake Twin identity.

### Incorporation standing

Catalogue identity `E06.02.04.F02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
