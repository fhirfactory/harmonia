---
{
  "schemaVersion": 1,
  "canonicalId": "ba9a24cc692f4a9abcba8bf4279fadbf",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT3] Deferred Task Lifecycle",
  "friendlyName": "DigitalWorkEventsAndTemporalCoordination.AsynchronousDigitalTwinEntityWorkCoordination.DeferredTaskLifecycle",
  "canonicalName": "CT1-EC-DigitalWorkEventsAndTemporalCoordination.CT2-EC-AsynchronousDigitalTwinEntityWorkCoordination.CT3-EC-DeferredTaskLifecycle",
  "standing": {
    "semantic": "Accepted replacement model — Pass3/014; implementation unverified",
    "currentness": "current",
    "incorporationReview": "Accepted by Solution Architect — 1.1.20 closure disposition"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md",
    "sourceIdentity": "E06.09.02",
    "allocation": "UUIDv4 allocated once by explicit create",
    "predecessorCanonicalIds": [
      "b25bb04a8aa94dcfac105a646878ccfc",
      "95985d244a234571892a9e9b4024f594"
    ]
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
        "key": "E06.09",
        "type": "capability",
        "name": "Asynchronous Digital Twin–Entity Work Coordination",
        "tier": "CT2",
        "category": "enterprise"
      },
      {
        "key": "E06.09.02",
        "type": "capability",
        "name": "Deferred Task Lifecycle",
        "tier": "CT3",
        "category": "enterprise"
      }
    ],
    "categoryInterpretation": "Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split."
  },
  "catalogue": {
    "id": "E06.09.02",
    "parent": "E06.09",
    "tier": "CT3",
    "sourceName": "Deferred Task Lifecycle",
    "hierarchy": "Enterprise"
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "selector": "E06.09.02",
      "role": "Accepted Pass3/014 source definition, now incorporated into this canonical resource",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md",
      "sha256": "83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4"
    },
    {
      "source": "enterprise-architecture/design/evidence/1.1.20/001/after/pass3-fr-reconciliation/capability-reconciliation.json",
      "selector": "/635",
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
      "selector": "E06.09.02"
    }
  ],
  "relationships": [
    {
      "identifier": "id-7f8ebe550b9952f88508255113cf75ec",
      "archimateType": "Composition",
      "source": "id-698ebbb4ebc15396812d2b314f884180",
      "target": "id-e61f9a8b460653f58b2b47276dbc8b1a",
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
      "identifier": "id-1897fa32fa6f5e41967e5a64683f5d17",
      "archimateType": "Composition",
      "source": "id-698ebbb4ebc15396812d2b314f884180",
      "target": "id-6cbceb2d93705ac4ae1fb0f7e61b203a",
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
      "identifier": "id-ce44e31251e05b72b868042a1a2b3dc4",
      "archimateType": "Composition",
      "source": "id-698ebbb4ebc15396812d2b314f884180",
      "target": "id-dada599966e45b84aaa85dec17656b8c",
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
      "identifier": "id-c9f62734e21f57d5991ed993e046b56b",
      "archimateType": "Composition",
      "source": "id-698ebbb4ebc15396812d2b314f884180",
      "target": "id-4ca54e5a6f845744a73e3d5ce7847a23",
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
      "identifier": "id-b2989a5d567e50d08f233f28d5a86478",
      "archimateType": "Composition",
      "source": "id-698ebbb4ebc15396812d2b314f884180",
      "target": "id-1b6c9c15e25d51ba95f0d413a6a32ce8",
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
    "identifier": "id-698ebbb4ebc15396812d2b314f884180",
    "properties": [
      {
        "ref": "propid-25",
        "values": [
          {
            "text": "E06.09.02",
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

# [CT3] Deferred Task Lifecycle

## Architectural content

Maintain long-duration task obligations without keeping an Ergon thread, transaction or execution envelope continuously active.

### Incorporation standing

Catalogue identity `E06.09.02`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.
