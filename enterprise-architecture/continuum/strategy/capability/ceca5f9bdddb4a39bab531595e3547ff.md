---
{
  "schemaVersion": 1,
  "canonicalId": "ceca5f9bdddb4a39bab531595e3547ff",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT2] Service Provider & Network Participation",
  "friendlyName": "HealthServiceProvision.ServiceProviderAndNetworkParticipation",
  "canonicalName": "CT1-HealthServiceProvision.CT2-ServiceProviderAndNetworkParticipation",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "B07.02",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "inputs": [
      {
        "key": "B07",
        "type": "capability",
        "name": "Health Service Provision",
        "tier": "CT1",
        "category": "business"
      },
      {
        "key": "B07.02",
        "type": "capability",
        "name": "Service Provider & Network Participation",
        "tier": "CT2",
        "category": "business"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "B07.02",
    "parent": "B07",
    "tier": "CT2",
    "sourceName": "Service Provider & Network Participation",
    "hierarchy": "Business/Business Enabling"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "B07.02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/150",
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
      "selector": "B07.02"
    }
  ],
  "relationships": [
    {
      "identifier": "id-33656aae879e5e8f92162482a68cd580",
      "archimateType": "Serving",
      "source": "id-6523e6416a115a99a75b0dd50b0b1b51",
      "target": "id-0255d2fa68b650d5a03193a30aebdc37",
      "standing": {
        "semantic": "Accepted semantic migration — Solution Architect 1.1.20 closure disposition",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
        "selector": "C",
        "hcrl": "enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5",
        "predecessorRelationshipIdentifier": "id-30ac30dbbea75996800109a4d15345ad",
        "coverage": "Candidate migration; partial scope correspondence; no implementation satisfaction or transferred domain authority"
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
        },
        {
          "ref": "propid-40",
          "values": [
            {
              "text": "current",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-47",
          "values": [
            {
              "text": "Proposed",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-58",
          "values": [
            {
              "text": "enabled_by",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-59",
          "values": [
            {
              "text": "{\"from\": \"business-capability:healthcare-organisation--service-structure-management\", \"provenance\": {\"authority\": \"Proposed only; cited source standing is not inherited\", \"derivation\": \"Semantic responsibility analysis; no authority transfer\", \"evidence\": [{\"line\": 10, \"path\": \"docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md\", \"standing\": \"Current corpus evidence; does not promote the derived assertion\", \"text\": \"### 1.2 Non-Transfer of Ownership Guardrail\"}], \"method\": \"AI architectural synthesis under the explicitly authorised Step 2 task\"}, \"rationale\": \"Represented entity identities and service mappings support structure decisions; mapped entity owners retain authority.\", \"responsibility_boundary\": \"Enablement only; no decomposition, implementation equivalence, inherited descendant mapping, source-ownership transfer or domain-authority transfer.\", \"sources\": [], \"status\": \"Proposed\", \"supporting_responsibility_elements\": [\"function:maintain-healthcare-service-definition\", \"service:healthcare-service-directory-query\", \"function:maintain-service--location--provider-map\", \"service:service-provision-resolution\", \"function:maintain-service-availability\", \"service:service-availability-query\"], \"to\": \"capability:health-service-administration\", \"type\": \"enabled_by\"}",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-57",
          "values": [
            {
              "text": "/relationships/711",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ],
      "attributes": {},
      "name": [
        {
          "text": "enabled_by",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Native treatment authorised by HCRL 1.1.5-002. Original qualification: {\"from\": \"business-capability:healthcare-organisation--service-structure-management\", \"provenance\": {\"authority\": \"Proposed only; cited source standing is not inherited\", \"derivation\": \"Semantic responsibility analysis; no authority transfer\", \"evidence\": [{\"line\": 10, \"path\": \"docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md\", \"standing\": \"Current corpus evidence; does not promote the derived assertion\", \"text\": \"### 1.2 Non-Transfer of Ownership Guardrail\"}], \"method\": \"AI architectural synthesis under the explicitly authorised Step 2 task\"}, \"rationale\": \"Represented entity identities and service mappings support structure decisions; mapped entity owners retain authority.\", \"responsibility_boundary\": \"Enablement only; no decomposition, implementation equivalence, inherited descendant mapping, source-ownership transfer or domain-authority transfer.\", \"sources\": [], \"status\": \"Proposed\", \"supporting_responsibility_elements\": [\"function:maintain-healthcare-service-definition\", \"service:healthcare-service-directory-query\", \"function:maintain-service--location--provider-map\", \"service:service-provision-resolution\", \"function:maintain-service-availability\", \"service:service-availability-query\"], \"to\": \"capability:health-service-administration\", \"type\": \"enabled_by\"}\n\nCandidate migration; partial scope correspondence; no implementation satisfaction or transferred domain authority. Original relationship id-30ac30dbbea75996800109a4d15345ad remains preserved as history.",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ]
    },
    {
      "identifier": "id-f0830f51c0ed5dc9a4b78d9c0b6bc943",
      "archimateType": "Composition",
      "source": "id-6523e6416a115a99a75b0dd50b0b1b51",
      "target": "id-771c1f0e4a025e608c59c7d1b66fb207",
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
      "identifier": "id-88844441122150229f46671e02b30e71",
      "archimateType": "Composition",
      "source": "id-6523e6416a115a99a75b0dd50b0b1b51",
      "target": "id-391973495f1d5896b0cb257256c769c1",
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
      "identifier": "id-c9bfa110400c54be8d484eae9b281bbf",
      "archimateType": "Composition",
      "source": "id-6523e6416a115a99a75b0dd50b0b1b51",
      "target": "id-189d778a8d185f2ea5097bff0ec55e00",
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
      "identifier": "id-fea79aae1ede542eb3190dbd814d2434",
      "archimateType": "Composition",
      "source": "id-6523e6416a115a99a75b0dd50b0b1b51",
      "target": "id-4aa91c2a3e865e20a92d2b889ff06e02",
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
      "identifier": "id-fabf4fe231a05345b0a9660dab704741",
      "archimateType": "Composition",
      "source": "id-6523e6416a115a99a75b0dd50b0b1b51",
      "target": "id-905a15252a7b59b48f732031769d0653",
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
    "identifier": "id-6523e6416a115a99a75b0dd50b0b1b51",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "B07.02",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "CT2",
            "attributes": {}
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "CT2",
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

# [CT2] Service Provider & Network Participation

## Architectural content

Establish appropriate provider relationships and service contributions.

### Incorporation standing

Catalogue identity `B07.02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
