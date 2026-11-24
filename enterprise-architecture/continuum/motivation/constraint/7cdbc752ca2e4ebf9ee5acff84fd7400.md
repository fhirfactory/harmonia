---
{
  "schemaVersion": 1,
  "canonicalId": "7cdbc752ca2e4ebf9ee5acff84fd7400",
  "archimateType": "Constraint",
  "specialization": null,
  "displayName": "Configuration and Governance Has Coherent Authority",
  "friendlyName": "ConfigurationAndGovernanceHasCoherentAuthority",
  "canonicalName": "MOT-ConfigurationAndGovernanceHasCoherentAuthority",
  "standing": {
    "semantic": "Adjudicated Domain05 direction — U11; holistic result acceptance pending",
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
        "name": "Configuration and Governance Has Coherent Authority",
        "key": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mnemosyne-Persistence-Management-and-Authoritative-Persistence-Oversight.md:original lines 138-138"
      }
    ]
  },
  "provenance": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mnemosyne-Persistence-Management-and-Authoritative-Persistence-Oversight.md",
      "selector": "original lines 138-138",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Normative assertion reconciled under U11"
    },
    {
      "source": "docs/markdown/05-information-systems-architecture/sources.md",
      "selector": "U11",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Source adjudication and later-correction boundary"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mnemosyne-Persistence-Management-and-Authoritative-Persistence-Oversight.md",
      "selector": "original lines 138-138"
    }
  ],
  "relationships": [
    {
      "identifier": "id-72afd379311252e6a21b4327d93231a1",
      "archimateType": "Association",
      "source": "id-60d81b8a64e65a8fa1108622c20fe6f9",
      "target": "id-829d7b5b57965765adf492ce3aaaa7d2",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "Adjudicated Domain05 direction — U11; holistic result acceptance pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mnemosyne-Persistence-Management-and-Authoritative-Persistence-Oversight.md",
        "selector": "original lines 138-138",
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
    "identifier": "id-60d81b8a64e65a8fa1108622c20fe6f9",
    "properties": []
  }
}
---

# Configuration and Governance Has Coherent Authority

## Architectural content

**Harmonia Configuration & Governance (C&G)** SHALL remain a single coherent governed information space and SHALL NOT be partitioned in a manner that creates independent or divergent governance authority.

This assertion is normalised from the governed DesignNote under U11 and the ARB release Praxis. Original candidate/release qualifications and separately deferred implementation details remain in force.

Architectural subject: [Mnemosyne](../../application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md).
