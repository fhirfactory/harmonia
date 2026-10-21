---
{
  "schemaVersion": 1,
  "canonicalId": "21eb20cdc600411cb71fbfe2abce4dbe",
  "archimateType": "Constraint",
  "specialization": null,
  "displayName": "Authoritative AuditEvents Are Immutable",
  "friendlyName": "AuthoritativeAuditEventsAreImmutable",
  "canonicalName": "MOT-AuthoritativeAuditEventsAreImmutable",
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
        "type": "Constraint",
        "name": "Authoritative AuditEvents Are Immutable",
        "key": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md:original lines 75-75"
      }
    ]
  },
  "provenance": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md",
      "selector": "original lines 75-75",
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
      "selector": "original lines 75-75"
    }
  ],
  "relationships": [
    {
      "identifier": "id-2d49cd8a38bb576aa511a3f2ff47efa3",
      "archimateType": "Association",
      "source": "id-64ec8c261f215ec5949c014a06c247ff",
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
        "selector": "original lines 75-75",
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
    "identifier": "id-64ec8c261f215ec5949c014a06c247ff",
    "properties": []
  }
}
---

# Authoritative AuditEvents Are Immutable

## Architectural content

> **An existing authoritative AuditEvent SHALL NOT be corrected, amended, replaced or overwritten.**

This assertion is normalised from the governed DesignNote under U07/U15 and the ARB release Praxis. Original candidate/release qualifications and separately deferred implementation details remain in force.

Architectural subject: [Kleio](../../application/application-component/fefda110e2274884ad58906733606efd.md).
