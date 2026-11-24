---
{
  "schemaVersion": 1,
  "canonicalId": "180ce38abebc4c8aa3e882efda217d8c",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Timely Critical-Result Acknowledgement and Escalation",
  "friendlyName": "DiagnosticInvestigation.DiagnosticFindingsReporting.CriticalResultReceiptAndFollowUpConfirmation.TimelyCriticalResultAcknowledgementAndEscalation",
  "canonicalName": "CT1-DiagnosticInvestigation.CT2-DiagnosticFindingsReporting.CT3-CriticalResultReceiptAndFollowUpConfirmation.FEAT-TimelyCriticalResultAcknowledgementAndEscalation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B04.03.04.F04",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B04",
        "type": "capability",
        "name": "Diagnostic Investigation",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B04.03",
        "type": "capability",
        "name": "Diagnostic Findings Reporting",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B04.03.04",
        "type": "capability",
        "name": "Critical Result Receipt & Follow-Up Confirmation",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B04.03.04.F04",
        "type": "feature",
        "name": "Timely Critical-Result Acknowledgement and Escalation",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B04.03.04.F04",
    "parent": "B04.03.04",
    "tier": "Feature",
    "sourceName": "Timely Critical-Result Acknowledgement and Escalation",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B04.03.04.F04",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/325",
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
      "selector": "B04.03.04.F04"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-8f3e64c99414530bb3f308b530278ae3",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B04.03.04.F04",
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

# [Feature] Timely Critical-Result Acknowledgement and Escalation

## Architectural content

Critical-result acknowledgement is required within the applicable interval. Missing or late acknowledgement triggers the specified escalation pathway; notification or acknowledgement alone does not establish clinical response completion.

### Incorporation standing

Catalogue identity `B04.03.04.F04`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
