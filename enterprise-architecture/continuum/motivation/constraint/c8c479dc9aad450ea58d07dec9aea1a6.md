---
{
  "schemaVersion": 1,
  "canonicalId": "c8c479dc9aad450ea58d07dec9aea1a6",
  "archimateType": "Constraint",
  "specialization": null,
  "displayName": "Dataset Access Has No Runtime Bootstrap Cycle",
  "friendlyName": "DatasetAccessHasNoRuntimeBootstrapCycle",
  "canonicalName": "MOT-DatasetAccessHasNoRuntimeBootstrapCycle",
  "standing": {
    "semantic": "Adjudicated Domain05 direction — U13/U15; holistic result acceptance pending",
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
        "name": "Dataset Access Has No Runtime Bootstrap Cycle",
        "key": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Calliope-Model-DataSet-and-Nomenclature-Management.md:original lines 389-394"
      }
    ]
  },
  "provenance": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Calliope-Model-DataSet-and-Nomenclature-Management.md",
      "selector": "original lines 389-394",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Normative assertion reconciled under U13/U15"
    },
    {
      "source": "docs/markdown/05-information-systems-architecture/sources.md",
      "selector": "U13/U15",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Source adjudication and later-correction boundary"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Calliope-Model-DataSet-and-Nomenclature-Management.md",
      "selector": "original lines 389-394"
    }
  ],
  "relationships": [
    {
      "identifier": "id-2ce0e9dae97f537ea7d836aa4d1e896c",
      "archimateType": "Association",
      "source": "id-e7b309a1369456c78b0f01b9dc960291",
      "target": "id-c870b3bd8d3c5a1686648ec78539bd50",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "Adjudicated Domain05 direction — U13/U15; holistic result acceptance pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Calliope-Model-DataSet-and-Nomenclature-Management.md",
        "selector": "original lines 389-394",
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
    "identifier": "id-e7b309a1369456c78b0f01b9dc960291",
    "properties": []
  }
}
---

# Dataset Access Has No Runtime Bootstrap Cycle

## Architectural content

> **The structural contracts necessary to access and manage
> Calliope-DataSets SHALL be established through Calliope-Models and the
> software build/deployment lifecycle. Runtime Calliope-DataSets SHALL
> provide the authoritative mutable content governed by those contracts
> and SHALL NOT be required to establish the structural contracts
> necessary for its own access.**

This assertion is normalised from the governed DesignNote under U13/U15 and the ARB release Praxis. Original candidate/release qualifications and separately deferred implementation details remain in force.

Architectural subject: [Calliope](../../application/application-component/466ad8f75a954c0581b5bba090443849.md).
