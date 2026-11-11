---
{
  "schemaVersion": 1,
  "canonicalId": "74ec609de1ae498fbf9d13e4ef8c6cbe",
  "archimateType": "DataObject",
  "specialization": "Class",
  "displayName": "[Class] InformationUnit",
  "friendlyName": "model.core.information.InformationUnit",
  "canonicalName": "PKG-Model.PKG-Core.PKG-Information.CLASS-InformationUnit",
  "standing": {
    "semantic": "Accepted bounded architecture — HCRL 1.1.15-001 DR-1",
    "currentness": "current"
  },
  "identity": {
    "allocation": "UUIDv4 allocated once under explicit HCRL 1.1.15-001 application",
    "authority": "enterprise-architecture/design/review/1.1.15/hcrl-1.1.15-001.pending.md#dr-1"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "mode": "Contextual derivation from Package Composition ancestry",
    "expected": {
      "displayName": "[Class] InformationUnit",
      "friendlyName": "model.core.information.InformationUnit",
      "canonicalName": "PKG-Model.PKG-Core.PKG-Information.CLASS-InformationUnit"
    }
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml",
      "selector": "element[@identifier=\"id-f5c345ab6c47433da11ee12f5d84e557\"]",
      "role": "Adjudicated Archi candidate under HCRL 1.1.15-001",
      "revision": "a31ed511c07e0a54c503383f6e52dfdad1e5905e642c05e7dc14fb7649af2eb9",
      "sha256": "a31ed511c07e0a54c503383f6e52dfdad1e5905e642c05e7dc14fb7649af2eb9"
    },
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
      "selector": "§§0.1,2–21,23.4 — existing logical InformationUnit",
      "role": "Accepted whole DRP; IU-01–IU-13 adjudications; ADD authorised by operative Task",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md",
      "sha256": "98abd39feea66f5f9d5c5a9540d2053b7a340864e68b22d3ac415028f4c42775"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml",
      "selector": "element[@identifier=\"id-f5c345ab6c47433da11ee12f5d84e557\"]"
    },
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.002.md",
      "selector": "§5 Class::InformationUnit"
    },
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
      "selector": "§§0.1,2–21,23.4 — existing logical InformationUnit"
    }
  ],
  "exchange": {
    "identifier": "id-f5c345ab6c47433da11ee12f5d84e557",
    "properties": []
  },
  "relationships": [
    {
      "identifier": "id-8aa8d0b08a3145b0bccda3307dd2fd35",
      "archimateType": "Realization",
      "source": "id-f5c345ab6c47433da11ee12f5d84e557",
      "target": "id-0525ac7ff42457dd9339e3ad95255296",
      "name": [
        {
          "text": "Logical representation of the same conceptual Information Unit",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Accepted IU DRP IU-01; §§0.1,3; incorporation is authorised by enterprise-architecture/design/evidence/1.1.22/001/before/ai-agent-tasks/20261111T025829Z-information-unit-framework-incorporation.md. Logical representation of the same conceptual Information Unit",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "standing": {
        "semantic": "Accepted Information Unit Framework semantics; resulting increment confirmation pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
        "selector": "IU-01; §§0.1,3",
        "role": "Accepted whole DRP; IU-01–IU-13 adjudications; ADD authorised by operative Task",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md",
        "sha256": "98abd39feea66f5f9d5c5a9540d2053b7a340864e68b22d3ac415028f4c42775"
      }
    },
    {
      "identifier": "id-eae38cd87c8b4d919c56a1db5f89d38c",
      "archimateType": "Composition",
      "source": "id-f5c345ab6c47433da11ee12f5d84e557",
      "target": "id-0cda04bcee235976b2b0b322ac01cfd4",
      "name": [
        {
          "text": "Contains exactly one governed InformationRepresentation [1..1]",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Accepted IU DRP IU-02; §§2,6; incorporation is authorised by enterprise-architecture/design/evidence/1.1.22/001/before/ai-agent-tasks/20261111T025829Z-information-unit-framework-incorporation.md. Contains exactly one governed InformationRepresentation [1..1]",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "standing": {
        "semantic": "Accepted Information Unit Framework semantics; resulting increment confirmation pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
        "selector": "IU-02; §§2,6",
        "role": "Accepted whole DRP; IU-01–IU-13 adjudications; ADD authorised by operative Task",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md",
        "sha256": "98abd39feea66f5f9d5c5a9540d2053b7a340864e68b22d3ac415028f4c42775"
      }
    },
    {
      "identifier": "id-855f9109f49842f79cecb538ded6f44c",
      "archimateType": "Composition",
      "source": "id-f5c345ab6c47433da11ee12f5d84e557",
      "target": "id-31a2323d561559e68b8715a0a9e9a95d",
      "name": [
        {
          "text": "Contains the IU Metadata concerns; no independently governed nested IU",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Accepted IU DRP IU-03; §§2,5; incorporation is authorised by enterprise-architecture/design/evidence/1.1.22/001/before/ai-agent-tasks/20261111T025829Z-information-unit-framework-incorporation.md. Contains the IU Metadata concerns; no independently governed nested IU",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "standing": {
        "semantic": "Accepted Information Unit Framework semantics; resulting increment confirmation pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
        "selector": "IU-03; §§2,5",
        "role": "Accepted whole DRP; IU-01–IU-13 adjudications; ADD authorised by operative Task",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md",
        "sha256": "98abd39feea66f5f9d5c5a9540d2053b7a340864e68b22d3ac415028f4c42775"
      }
    },
    {
      "identifier": "id-14ff6e983e1e45ac81f7f181b5339c8c",
      "archimateType": "Association",
      "source": "id-f5c345ab6c47433da11ee12f5d84e557",
      "target": "id-7b1ba31cbae65d08ab37f788cbbd4812",
      "name": [
        {
          "text": "IU UUID identifies the unit; its own Topic provides its canonical name",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Accepted IU DRP IU-06; §§3–4; incorporation is authorised by enterprise-architecture/design/evidence/1.1.22/001/before/ai-agent-tasks/20261111T025829Z-information-unit-framework-incorporation.md. IU UUID identifies the unit; its own Topic provides its canonical name",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "standing": {
        "semantic": "Accepted Information Unit Framework semantics; resulting increment confirmation pending",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
        "selector": "IU-06; §§3–4",
        "role": "Accepted whole DRP; IU-01–IU-13 adjudications; ADD authorised by operative Task",
        "retainedAt": "enterprise-architecture/design/evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md",
        "sha256": "98abd39feea66f5f9d5c5a9540d2053b7a340864e68b22d3ac415028f4c42775"
      },
      "attributes": {
        "isDirected": "true"
      }
    }
  ],
  "unresolvedProperties": {}
}
---

# [Class] InformationUnit

## Architectural content

### Accepted Information Unit Framework incorporation — 1.1.22

This Calliope logical Class and [[BusinessObject] Information Unit](../../../business/business-object/10284034c8534b639284386b064f2c54.md) represent the **same IU** at
logical and conceptual levels. Its existing canonicalId, Class classification,
Package Composition and names are preserved. No additional implementation Class
is inferred from the semantic decomposition below.

```text
InformationUnit
├── Identity: persistent UUID and its own Topic
│   └── harmonia.mneme.iu.<hex-string-of-compressed-uuid>
├── Metadata
│   ├── ManagementContext: operational Version; Active Generation
│   ├── InformationCoordinates: PersistenceGateway; GatewayEndpoint;
│   │                         InformationLocation; InformationOwner (TopicIDs)
│   ├── SecurityContext: IU-required Topics / TopicMasks
│   ├── LifecycleContext: Mneme–Mnemosyne operational handshaking
│   └── Relationships [0..*]: optional creator/updater-supplied references
└── InformationRepresentation [1..1]
    ├── ContainerMetadata
    ├── ContentMetadata: Content Structure Topic; Content Structure Version
    └── Content: native information; native instance version only if supported
```

[Information Unit Metadata](../c05b7e37278141488ecc90b9a9ef96eb.md) and [InformationRepresentation](../b9891b88150840ada3daefdec6f26c35.md)
define the two logical structures; their native Composition represents IU
constituency, not separate governed nested IUs or a physical schema. Their internal
concerns are documented in those structures rather than allocated a Class or Topic
per property. [Topic](../9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md) and [TopicMask](../bb489157a6bf48be926c5a601bb0a6c3.md) supply
the common Topic/Mask semantics; existing named Topic instances retain identity.

The IU is **atomic as governed information**: one identity, exactly one governed
representation and accepted-state consistency. Physical decomposition/references,
multiple active manifestations and provider-native persistence remain permitted.
A differently structured/formatted governed transformation result is a new IU;
the transforming consumer owns fidelity and any reverse transformation. The IU
does not manage alternative representations or relationship synchronisation.

Stable Feature-owned interfaces may expose characteristics without requiring clients
to traverse internal metadata. No universal operation catalogue, additional exposed
service, provider interface or mandatory attribute-population rule is introduced.
The historical phrase 'Governed Representation with its Management Context' denotes
this IU boundary; it is not a second independently governed context object.

The governed boundary associating a Governed Representation with its Management Context.
