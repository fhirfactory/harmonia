---
{
  "schemaVersion": 1,
  "canonicalId": "ddaa9bff2b684af7a6ad18fe4f2da8ad",
  "archimateType": "Requirement",
  "specialization": null,
  "displayName": "Audit Corrections Create New Linked Events",
  "friendlyName": "AuditCorrectionsCreateNewLinkedEvents",
  "canonicalName": "MOT-AuditCorrectionsCreateNewLinkedEvents",
  "standing": {
    "semantic": "Adjudicated Domain05 direction — U07/U15; holistic result acceptance pending",
    "currentness": "current"
  },
  "identity": {
    "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
    "normalisationAuthority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "docs/markdown/governance/canonical-identity-and-naming.md",
    "mode": "Local governed lexical component; no ancestry invented",
    "inputs": [
      {
        "type": "Requirement",
        "name": "Audit Corrections Create New Linked Events",
        "key": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md:original lines 77-77"
      }
    ]
  },
  "provenance": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md",
      "selector": "original lines 77-77",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Normative assertion reconciled under U07/U15"
    },
    {
      "source": "docs/markdown/05-information-systems-architecture/sources.md",
      "selector": "U07/U15",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Source adjudication and later-correction boundary"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md",
      "selector": "original lines 77-77"
    }
  ],
  "relationships": [
    {
      "identifier": "id-391cd9f684525481bc1ab01a8c5a3a08",
      "archimateType": "Association",
      "source": "id-ea95776395a4511d922c3ca1f15e65f4",
      "target": "id-d6e1b1c535985930a5d9805d0f3f0c53",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "Adjudicated Domain05 direction — U07/U15; holistic result acceptance pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md",
        "selector": "original lines 77-77",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation"
      },
      "name": [
        {
          "text": "applies to",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "The source explicitly bounds this assertion to its architectural subject; this is not a containment or ownership transfer.",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "properties": []
    }
  ],
  "unresolvedProperties": {},
  "exchange": {
    "identifier": "id-ea95776395a4511d922c3ca1f15e65f4",
    "properties": []
  }
}
---

# Audit Corrections Create New Linked Events

## Architectural content

Where an earlier AuditEvent is subsequently determined to be incorrect, incomplete, misleading, or otherwise requires qualification, Kleio SHALL create a **new AuditEvent** that refers back to the earlier AuditEvent and records the subsequent assertion.

This assertion is normalised from the governed DesignNote under U07/U15 and the ARB release Praxis. Original candidate/release qualifications and separately deferred implementation details remain in force.

Architectural subject: [Kleio](../../application/application-component/fefda110e2274884ad58906733606efd.md).
