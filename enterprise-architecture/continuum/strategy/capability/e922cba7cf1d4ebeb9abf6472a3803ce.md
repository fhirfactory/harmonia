---
{
  "schemaVersion": 1,
  "canonicalId": "e922cba7cf1d4ebeb9abf6472a3803ce",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT2] Information Protection & Accountability Enablement",
  "friendlyName": "SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement",
  "canonicalName": "CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement",
  "standing": {
    "semantic": "Proposed",
    "currentness": "superseded",
    "placement_status": "Proposed",
    "structural_id_status": "Proposed",
    "name_status": "Proposed",
    "ancestry_status": "proposed"
  },
  "identity": {
    "allocation": "Preserved existing resource-owned UUID32; no allocation in migration",
    "authority": "docs/markdown/governance/baseline/release-1.1.0.json",
    "migrationAuthority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#dr-2"
  },
  "naming": {
    "authority": "docs/markdown/governance/canonical-identity-and-naming.md",
    "mode": "Preserved governed names; source-qualified context and unresolved ancestry retained",
    "expected": {
      "displayName": "[CT2] Information Protection & Accountability Enablement",
      "friendlyName": "SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement",
      "canonicalName": "CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement"
    }
  },
  "provenance": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/762",
      "role": "governed source resource",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "0f7dc7b43da74dddffcb41b169031f0bd771c51509002851bb07c1f1a7eeee0b",
      "sourceKey": "proposal:bec-information-protection-accountability-enablement"
    },
    {
      "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
      "selector": "element[@identifier=\"id-c72a309296ec5da68e55dc8ba1f8fcb7\"]",
      "role": "accepted representation binding and presentation; not an additional architecture source",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/762"
    }
  ],
  "exchange": {
    "identifier": "id-c72a309296ec5da68e55dc8ba1f8fcb7",
    "previousNativeName": "Information Protection & Accountability Enablement",
    "properties": [
      {
        "ref": "propid-1",
        "values": [
          {
            "text": "proposal:bec-information-protection-accountability-enablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-2",
        "values": [
          {
            "text": "HCM-BEC-information-protection-accountability-enablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-3",
        "values": [
          {
            "text": "cap:bec-information-protection-accountability-enablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-4",
        "values": [
          {
            "text": "capability",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-5",
        "values": [
          {
            "text": "CT2",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-6",
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
        "ref": "propid-7",
        "values": [
          {
            "text": "superseded",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-8",
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
        "ref": "propid-9",
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
        "ref": "propid-10",
        "values": [
          {
            "text": "CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-11",
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
        "ref": "propid-12",
        "values": [
          {
            "text": "Unresolved — representation ID only",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-13",
        "values": [
          {
            "text": "{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Inferred from bounded responsibilities and their semantic decomposition\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"},{\"line\":10,\"path\":\"docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"### 1.2 Non-Transfer of Ownership Guardrail\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"}",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-14",
        "values": [
          {
            "text": "{\"ancestry_status\":\"proposed\",\"architectural_status\":\"Proposed\",\"baseline_version\":null,\"boundary\":\"Patient directives remain with Client Privacy; runtime authorisation does not become external authentication or clinical authority.\",\"canonical_id\":\"CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT\",\"canonical_name\":null,\"category\":\"business_enabling\",\"confidence\":\"High\",\"definition\":\"Apply information-use policy and preserve the accountability context needed for governed health-information activity.\",\"id_aliases\":[],\"intent\":\"Make information use both governed and explainable through policy, attributable rules and significant evidence.\",\"key\":\"proposal:bec-information-protection-accountability-enablement\",\"latex_label\":\"cap:bec-information-protection-accountability-enablement\",\"local_id\":\"BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT\",\"name\":\"Information Protection & Accountability Enablement\",\"name_aliases\":[],\"name_status\":\"Proposed\",\"overview\":\"Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.\",\"owner_identifier\":null,\"owner_key\":null,\"parent_identifier\":\"HCM-BEC-governed-health-information-enablement\",\"parent_key\":\"proposal:bec-governed-health-information-enablement\",\"placement_rationale\":\"Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.\",\"placement_status\":\"Proposed\",\"pre_review_record\":{\"ancestry_status\":\"proposed\",\"architectural_status\":\"Proposed\",\"baseline_version\":null,\"boundary\":\"Patient directives remain with Client Privacy; runtime authorisation does not become external authentication or clinical authority.\",\"canonical_id\":\"CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT\",\"canonical_name\":null,\"category\":\"business_enabling\",\"confidence\":\"High\",\"definition\":\"Apply information-use policy and preserve the accountability context needed for governed health-information activity.\",\"id_aliases\":[],\"intent\":\"Make information use both governed and explainable through policy, attributable rules and significant evidence.\",\"key\":\"proposal:bec-information-protection-accountability-enablement\",\"latex_label\":\"cap:bec-information-protection-accountability-enablement\",\"local_id\":\"BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT\",\"name\":\"Information Protection & Accountability Enablement\",\"name_aliases\":[],\"name_status\":\"Proposed\",\"overview\":\"Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.\",\"owner_identifier\":null,\"owner_key\":null,\"parent_identifier\":\"HCM-BEC-governed-health-information-enablement\",\"parent_key\":\"proposal:bec-governed-health-information-enablement\",\"placement_rationale\":\"Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.\",\"placement_status\":\"Proposed\",\"provenance\":{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Inferred from bounded responsibilities and their semantic decomposition\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"},{\"line\":10,\"path\":\"docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"### 1.2 Non-Transfer of Ownership Guardrail\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"},\"reference_id\":\"HCM-BEC-information-protection-accountability-enablement\",\"relevance\":\"Proposed materiality; no inherited Core classification\",\"sources\":[],\"structural_id_status\":\"Proposed\",\"tier\":\"CT2\",\"type\":\"capability\",\"unresolved\":[],\"views\":[]},\"provenance\":{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Inferred from bounded responsibilities and their semantic decomposition\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"},{\"line\":10,\"path\":\"docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"### 1.2 Non-Transfer of Ownership Guardrail\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"},\"reconciliation_targets\":[\"capability:health-information-control\",\"reviewed:bec-information-content-maintainer-management\"],\"record_state\":\"superseded\",\"reference_id\":\"HCM-BEC-information-protection-accountability-enablement\",\"relevance\":\"Proposed materiality; no inherited Core classification\",\"sources\":[],\"structural_id_status\":\"Proposed\",\"tier\":\"CT2\",\"type\":\"capability\",\"unresolved\":[],\"views\":[]}",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-15",
        "values": [
          {
            "text": "HCM-BEC-governed-health-information-enablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-17",
        "values": [
          {
            "text": "docs/markdown/governance/baseline/business-enabling-capabilities.md#hcm-bec-information-protection-accountability-enablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-18",
        "values": [
          {
            "text": "e922cba7cf1d4ebeb9abf6472a3803ce",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-19",
        "values": [
          {
            "text": "[CT2] Information Protection & Accountability Enablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-20",
        "values": [
          {
            "text": "SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-21",
        "values": [
          {
            "text": "CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-22",
        "values": [
          {
            "text": "Capability",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      }
    ]
  },
  "relationships": [
    {
      "identifier": "id-5b9aa203f31654b19ef6d3f33912f6da",
      "source": "id-c72a309296ec5da68e55dc8ba1f8fcb7",
      "target": "id-24a125b569e45888bf313cd39d435fba",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "v0.2 / Assertion Status": "Proposed",
        "v0.2 / Record State": "preserved historical assertion",
        "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-5b9aa203f31654b19ef6d3f33912f6da\"]"
      },
      "properties": [
        {
          "ref": "propid-54",
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
          "ref": "propid-7",
          "values": [
            {
              "text": "preserved historical assertion",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-55",
          "values": [
            {
              "text": "capability_decomposition",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-56",
          "values": [
            {
              "text": "{\"from\":\"proposal:bec-information-protection-accountability-enablement\",\"provenance\":{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Semantic responsibility analysis; no authority transfer\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"},\"rationale\":\"Mandatory evidence capture has its own policy-governed platform authority and must remain possible when the audited identity is denied an action. The captured fact is distinguished from debug output and the identity whose activity is recorded.\",\"sources\":[],\"status\":\"Proposed\",\"to\":\"proposal:bec-significant-activity-audit-capture\",\"type\":\"capability_decomposition\"}",
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
              "text": "/relationships/638",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-12",
          "values": [
            {
              "text": "Unresolved — representation ID only",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ],
      "name": [],
      "documentation": [
        {
          "text": "Snapshot predicate: capability_decomposition; assertion status: Proposed; currentness: preserved historical assertion.\n\n{\n  \"type\": \"capability_decomposition\",\n  \"from\": \"proposal:bec-information-protection-accountability-enablement\",\n  \"to\": \"proposal:bec-significant-activity-audit-capture\",\n  \"status\": \"Proposed\",\n  \"sources\": [],\n  \"rationale\": \"Mandatory evidence capture has its own policy-governed platform authority and must remain possible when the audited identity is denied an action. The captured fact is distinguished from debug output and the identity whose activity is recorded.\",\n  \"provenance\": {\n    \"derivation\": \"Semantic responsibility analysis; no authority transfer\",\n    \"evidence\": [\n      {\n        \"path\": \"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\n        \"line\": 49,\n        \"text\": \"Harmonia SHALL use the fixed three-tier Capability decomposition:\",\n        \"standing\": \"Current corpus evidence; does not promote the derived assertion\"\n      }\n    ],\n    \"method\": \"AI architectural synthesis under the explicitly authorised Step 2 task\",\n    \"authority\": \"Proposed only; cited source standing is not inherited\"\n  }\n}",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ]
    },
    {
      "identifier": "id-50f772edf6cd5584b51d7c0a4fc3dd98",
      "source": "id-c72a309296ec5da68e55dc8ba1f8fcb7",
      "target": "id-72018038e84c518c8bb24cb76c3b418e",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "v0.2 / Assertion Status": "Proposed",
        "v0.2 / Record State": "preserved historical assertion",
        "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-50f772edf6cd5584b51d7c0a4fc3dd98\"]"
      },
      "properties": [
        {
          "ref": "propid-54",
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
          "ref": "propid-7",
          "values": [
            {
              "text": "preserved historical assertion",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-55",
          "values": [
            {
              "text": "capability_decomposition",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-56",
          "values": [
            {
              "text": "{\"from\":\"proposal:bec-information-protection-accountability-enablement\",\"provenance\":{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Semantic responsibility analysis; no authority transfer\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"},\"rationale\":\"Recipients need to know which assertions were received, transformed or superseded and why. This ability preserves those meaningful relationships without making every retry or active-state reconstruction permanent provenance.\",\"sources\":[],\"status\":\"Proposed\",\"to\":\"proposal:bec-information-lineage-stewardship\",\"type\":\"capability_decomposition\"}",
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
              "text": "/relationships/639",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-12",
          "values": [
            {
              "text": "Unresolved — representation ID only",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ],
      "name": [],
      "documentation": [
        {
          "text": "Snapshot predicate: capability_decomposition; assertion status: Proposed; currentness: preserved historical assertion.\n\n{\n  \"type\": \"capability_decomposition\",\n  \"from\": \"proposal:bec-information-protection-accountability-enablement\",\n  \"to\": \"proposal:bec-information-lineage-stewardship\",\n  \"status\": \"Proposed\",\n  \"sources\": [],\n  \"rationale\": \"Recipients need to know which assertions were received, transformed or superseded and why. This ability preserves those meaningful relationships without making every retry or active-state reconstruction permanent provenance.\",\n  \"provenance\": {\n    \"derivation\": \"Semantic responsibility analysis; no authority transfer\",\n    \"evidence\": [\n      {\n        \"path\": \"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\n        \"line\": 49,\n        \"text\": \"Harmonia SHALL use the fixed three-tier Capability decomposition:\",\n        \"standing\": \"Current corpus evidence; does not promote the derived assertion\"\n      }\n    ],\n    \"method\": \"AI architectural synthesis under the explicitly authorised Step 2 task\",\n    \"authority\": \"Proposed only; cited source standing is not inherited\"\n  }\n}",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ]
    },
    {
      "identifier": "id-c38af2d256de513b9b2724ac4f22d532",
      "source": "id-c72a309296ec5da68e55dc8ba1f8fcb7",
      "target": "id-b9c636efc8a35c18bd0dce7db89caa88",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "v0.2 / Assertion Status": "Proposed",
        "v0.2 / Record State": "preserved historical assertion",
        "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-c38af2d256de513b9b2724ac4f22d532\"]"
      },
      "properties": [
        {
          "ref": "propid-54",
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
          "ref": "propid-7",
          "values": [
            {
              "text": "preserved historical assertion",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-55",
          "values": [
            {
              "text": "capability_decomposition",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-56",
          "values": [
            {
              "text": "{\"from\":\"proposal:bec-information-protection-accountability-enablement\",\"provenance\":{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Semantic responsibility analysis; no authority transfer\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"},\"rationale\":\"The responsibility concerns apply information-use policy and preserve the accountability context needed for governed health-information activity. It is the finest capability frame required for the established behaviour; the older contextual heading is not another capability tier.\",\"sources\":[],\"status\":\"Proposed\",\"to\":\"capability:health-information-control\",\"type\":\"capability_decomposition\"}",
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
              "text": "/relationships/567",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-12",
          "values": [
            {
              "text": "Unresolved — representation ID only",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ],
      "name": [],
      "documentation": [
        {
          "text": "Snapshot predicate: capability_decomposition; assertion status: Proposed; currentness: preserved historical assertion.\n\n{\n  \"type\": \"capability_decomposition\",\n  \"from\": \"proposal:bec-information-protection-accountability-enablement\",\n  \"to\": \"capability:health-information-control\",\n  \"status\": \"Proposed\",\n  \"sources\": [],\n  \"rationale\": \"The responsibility concerns apply information-use policy and preserve the accountability context needed for governed health-information activity. It is the finest capability frame required for the established behaviour; the older contextual heading is not another capability tier.\",\n  \"provenance\": {\n    \"derivation\": \"Semantic responsibility analysis; no authority transfer\",\n    \"evidence\": [\n      {\n        \"path\": \"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\n        \"line\": 49,\n        \"text\": \"Harmonia SHALL use the fixed three-tier Capability decomposition:\",\n        \"standing\": \"Current corpus evidence; does not promote the derived assertion\"\n      }\n    ],\n    \"method\": \"AI architectural synthesis under the explicitly authorised Step 2 task\",\n    \"authority\": \"Proposed only; cited source standing is not inherited\"\n  }\n}",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ]
    },
    {
      "identifier": "id-3157ca8e9bf75da08e48f7d8c69851c5",
      "source": "id-c72a309296ec5da68e55dc8ba1f8fcb7",
      "target": "id-43399be21121568aa6b26f5b0c8414c4",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "v0.2 / Assertion Status": "Proposed",
        "v0.2 / Record State": "preserved historical assertion",
        "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-3157ca8e9bf75da08e48f7d8c69851c5\"]"
      },
      "properties": [
        {
          "ref": "propid-54",
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
          "ref": "propid-7",
          "values": [
            {
              "text": "preserved historical assertion",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-55",
          "values": [
            {
              "text": "capability_decomposition",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-56",
          "values": [
            {
              "text": "{\"from\":\"proposal:bec-information-protection-accountability-enablement\",\"provenance\":{\"authority\":\"Proposed only; cited source standing is not inherited\",\"derivation\":\"Semantic responsibility analysis; no authority transfer\",\"evidence\":[{\"line\":49,\"path\":\"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\"standing\":\"Current corpus evidence; does not promote the derived assertion\",\"text\":\"Harmonia SHALL use the fixed three-tier Capability decomposition:\"}],\"method\":\"AI architectural synthesis under the explicitly authorised Step 2 task\"},\"rationale\":\"Consumers need an identifiable statement of which rule applies and who established it. This proposal fills BC-17 information support without giving the platform authority to make legislation, approve governance requirements or reinterpret their force.\",\"sources\":[],\"status\":\"Proposed\",\"to\":\"proposal:bec-governance-requirement-publication-support\",\"type\":\"capability_decomposition\"}",
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
              "text": "/relationships/643",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        },
        {
          "ref": "propid-12",
          "values": [
            {
              "text": "Unresolved — representation ID only",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ],
      "name": [],
      "documentation": [
        {
          "text": "Snapshot predicate: capability_decomposition; assertion status: Proposed; currentness: preserved historical assertion.\n\n{\n  \"type\": \"capability_decomposition\",\n  \"from\": \"proposal:bec-information-protection-accountability-enablement\",\n  \"to\": \"proposal:bec-governance-requirement-publication-support\",\n  \"status\": \"Proposed\",\n  \"sources\": [],\n  \"rationale\": \"Consumers need an identifiable statement of which rule applies and who established it. This proposal fills BC-17 information support without giving the platform authority to make legislation, approve governance requirements or reinterpret their force.\",\n  \"provenance\": {\n    \"derivation\": \"Semantic responsibility analysis; no authority transfer\",\n    \"evidence\": [\n      {\n        \"path\": \"docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md\",\n        \"line\": 49,\n        \"text\": \"Harmonia SHALL use the fixed three-tier Capability decomposition:\",\n        \"standing\": \"Current corpus evidence; does not promote the derived assertion\"\n      }\n    ],\n    \"method\": \"AI architectural synthesis under the explicitly authorised Step 2 task\",\n    \"authority\": \"Proposed only; cited source standing is not inherited\"\n  }\n}",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ]
    }
  ],
  "unresolvedProperties": {},
  "representation": {
    "sourceLinksRebased": true,
    "authority": "HCRL 1.1.5-002 DR-3; source meaning unchanged"
  }
}
---

# [CT2] Information Protection & Accountability Enablement

## Architectural content

Apply information-use policy and preserve the accountability context needed for governed health-information activity.

**Overview**

Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.

**Intent**

Make information use both governed and explainable through policy, attributable rules and significant evidence.

**Boundary**

Patient directives remain with Client Privacy; runtime authorisation does not become external authentication or clinical authority.
