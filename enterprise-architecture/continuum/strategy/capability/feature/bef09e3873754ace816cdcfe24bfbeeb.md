---
{
  "schemaVersion": 1,
  "canonicalId": "bef09e3873754ace816cdcfe24bfbeeb",
  "archimateType": "Capability",
  "specialization": "Feature",
  "displayName": "[Feature] Specimen Provenance Availability",
  "friendlyName": "DiagnosticInvestigation.DiagnosticEvidenceProduction.SpecimenAndSubjectIdentification.SpecimenProvenanceAvailability",
  "canonicalName": "CT1-DiagnosticInvestigation.CT2-DiagnosticEvidenceProduction.CT3-SpecimenAndSubjectIdentification.FEAT-SpecimenProvenanceAvailability",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B04.02.01.F05",
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
        "key": "B04.02",
        "type": "capability",
        "name": "Diagnostic Evidence Production",
        "tier": "CT2",
        "category": "business"
      },
      {
        "key": "B04.02.01",
        "type": "capability",
        "name": "Specimen and Subject Identification",
        "tier": "CT3",
        "category": "business"
      },
      {
        "key": "B04.02.01.F05",
        "type": "feature",
        "name": "Specimen Provenance Availability",
        "tier": "FT",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B04.02.01.F05",
    "parent": "B04.02.01",
    "tier": "Feature",
    "sourceName": "Specimen Provenance Availability",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B04.02.01.F05",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/feature-reconciliation.json",
      "selector": "/301",
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
      "selector": "B04.02.01.F05"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-d4370ead56d156d794ebcb921d763391",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B04.02.01.F05",
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

# [Feature] Specimen Provenance Availability

## Architectural content

Specimen provenance is available, with accessible and accurate supporting evidence.

### Incorporation standing

Catalogue identity `B04.02.01.F05`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
