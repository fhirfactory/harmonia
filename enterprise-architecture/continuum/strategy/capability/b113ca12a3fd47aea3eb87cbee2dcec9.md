---
{
  "schemaVersion": 1,
  "canonicalId": "b113ca12a3fd47aea3eb87cbee2dcec9",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT3] Clinical Context Presentation",
  "friendlyName": "InformationPresentationAndHumanInteraction.ContextualInformationPresentation.ClinicalContextPresentation",
  "canonicalName": "CT1-EC-InformationPresentationAndHumanInteraction.CT2-EC-ContextualInformationPresentation.CT3-EC-ClinicalContextPresentation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E07.01.01",
    "allocation": "UUIDv4 allocated once by explicit create",
    "predecessorCanonicalIds": [
      "44049d029a41437685272dfe94db4a2a",
      "c0dfa2ef22354340a4f1488790359bbd",
      "c25d66fd6aff4299857eadc7c0243544"
    ]
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "E07",
        "type": "capability",
        "name": "Information Presentation & Human Interaction",
        "tier": "CT1",
        "category": "enterprise"
      },
      {
        "key": "E07.01",
        "type": "capability",
        "name": "Contextual Information Presentation",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E07.01.01",
        "type": "capability",
        "name": "Clinical Context Presentation",
        "tier": "CT3",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E07.01.01",
    "parent": "E07.01",
    "tier": "CT3",
    "sourceName": "Clinical Context Presentation",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E07.01.01",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/643",
      "role": "Exact source contributions, original wording/criteria/status and accepted adjudication correspondence; historical assurance evidence",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json"
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
      "selector": "E07.01.01"
    }
  ],
  "relationships": [
    {
      "identifier": "id-2907deb4719357e5a6e24d79ead15840",
      "archimateType": "Composition",
      "source": "id-7f948913a9965d5b84cde8b16ef41faf",
      "target": "id-6e1ad8c8649a50918160c5baffbf6d7d",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
    },
    {
      "identifier": "id-1b2324497d3b5041ac3c30019835ad46",
      "archimateType": "Composition",
      "source": "id-7f948913a9965d5b84cde8b16ef41faf",
      "target": "id-2e24f86611d85d89837990c39ae44301",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
    },
    {
      "identifier": "id-63ab7758af2a5801bef565de0a5f588d",
      "archimateType": "Composition",
      "source": "id-7f948913a9965d5b84cde8b16ef41faf",
      "target": "id-40794a84fafe50eaaf49a059060fc510",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
    },
    {
      "identifier": "id-6f84ce27936e5bfcb5b92a84dd1bdd36",
      "archimateType": "Composition",
      "source": "id-7f948913a9965d5b84cde8b16ef41faf",
      "target": "id-4bf8c2d66c425554b40f9b480eb58310",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
    },
    {
      "identifier": "id-516bbadcefdf54c69f3a4ef12b9810ba",
      "archimateType": "Composition",
      "source": "id-7f948913a9965d5b84cde8b16ef41faf",
      "target": "id-57cb24481bd05defb1a6e2a07a16292f",
      "standing": {
        "semantic": "Accepted Pass3/014 containment",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "B",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5"
      },
      "properties": [
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
  ],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-7f948913a9965d5b84cde8b16ef41faf",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E07.01.01",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "CT3",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "CT3",
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

# [CT3] Clinical Context Presentation

## Architectural content

Clinical consumers can view governed information relevant to a clinical question.

### Incorporation standing

Catalogue identity `E07.01.01`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
