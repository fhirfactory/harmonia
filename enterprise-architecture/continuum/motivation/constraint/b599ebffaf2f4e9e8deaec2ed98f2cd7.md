---
{
  "schemaVersion": 1,
  "canonicalId": "b599ebffaf2f4e9e8deaec2ed98f2cd7",
  "archimateType": "Constraint",
  "specialization": null,
  "displayName": "PetasosParcel Is Transported by Value",
  "friendlyName": "PetasosParcelIsTransportedByValue",
  "canonicalName": "MOT-PetasosParcelIsTransportedByValue",
  "standing": {
    "semantic": "Adjudicated Domain05 direction — U09; holistic result acceptance pending",
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
        "name": "PetasosParcel Is Transported by Value",
        "key": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Petasos-Durable-Message-Event-Queue-Management.md:original lines 136-136"
      }
    ]
  },
  "provenance": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Petasos-Durable-Message-Event-Queue-Management.md",
      "selector": "original lines 136-136",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Normative assertion reconciled under U09"
    },
    {
      "source": "docs/markdown/05-information-systems-architecture/sources.md",
      "selector": "U09",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "role": "Source adjudication and later-correction boundary"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Petasos-Durable-Message-Event-Queue-Management.md",
      "selector": "original lines 136-136"
    }
  ],
  "relationships": [
    {
      "identifier": "id-39d0893cc3325fd9ac01b9b6a242362a",
      "archimateType": "Association",
      "source": "id-7db0ef4b055456de9b8b76686020dae3",
      "target": "id-9223f665940e57189c3fa1f11f1cfe3f",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "Adjudicated Domain05 direction — U09; holistic result acceptance pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Petasos-Durable-Message-Event-Queue-Management.md",
        "selector": "original lines 136-136",
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
    "identifier": "id-7db0ef4b055456de9b8b76686020dae3",
    "properties": []
  }
}
---

# PetasosParcel Is Transported by Value

## Architectural content

Petasos SHALL NOT be described as transporting only references. It transports PetasosParcel instances by value.

This assertion is normalised from the governed DesignNote under U09 and the ARB release Praxis. Original candidate/release qualifications and separately deferred implementation details remain in force.

Architectural subject: [Petasos](../../application/application-component/195e5f10db8e419391354197b5d7a8cc.md).
