---
{
  "schemaVersion": 1,
  "canonicalId": "b611a1fcfadb4b30af9ba74a4c993d8a",
  "archimateType": "Capability",
  "specialization": null,
  "displayName": "[CT1] Care Administration Enablement",
  "friendlyName": "CareAdministrationEnablement",
  "canonicalName": "CT1-BEC-CareAdministrationEnablement",
  "standing": {
    "semantic": "Superseded historical identity — direct correspondence",
    "currentness": "superseded"
  },
  "identity": {
    "allocation": "Preserved existing resource-owned UUID32; no allocation in migration",
    "authority": "docs/markdown/governance/baseline/release-1.1.0.json",
    "migrationAuthority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#dr-2",
    "correspondence": {
      "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
      "disposition": "direct correspondence",
      "successorCanonicalId": "28df27d55c514a37995145ab46945f78",
      "previousStanding": {
        "semantic": "Historical reference — RAD correspondence unresolved",
        "currentness": "historical_reference"
      },
      "historicalIdentityReserved": true,
      "reason": "Same bounded capability at the same tier, BC/BEC context and complete recorded canonical hierarchy; current responsibility/provenance supplies the represented meaning. No contrary historical definition is present. Correspondence is adjudicated under the ARB semantic-continuity rule, not inferred by the application adapter."
    }
  },
  "naming": {
    "authority": "docs/markdown/governance/canonical-identity-and-naming.md",
    "mode": "Preserved governed names; source-qualified context and unresolved ancestry retained",
    "expected": {
      "displayName": "[CT1] Care Administration Enablement",
      "friendlyName": "CareAdministrationEnablement",
      "canonicalName": "CT1-BEC-CareAdministrationEnablement"
    }
  },
  "provenance": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/historical_reference_elements/78",
      "role": "governed source resource",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "0f7dc7b43da74dddffcb41b169031f0bd771c51509002851bb07c1f1a7eeee0b",
      "sourceKey": "historical-reference:id-47a8d188ce4c516f9865b6523723e1f3"
    },
    {
      "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
      "selector": "element[@identifier=\"id-47a8d188ce4c516f9865b6523723e1f3\"]",
      "role": "accepted representation binding and presentation; not an additional architecture source",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/historical_reference_elements/78"
    }
  ],
  "exchange": {
    "identifier": "id-47a8d188ce4c516f9865b6523723e1f3",
    "previousNativeName": "Care Administration Enablement",
    "properties": [
      {
        "ref": "propid-23",
        "values": [
          {
            "text": "Business Enabling Capability",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-24",
        "values": [
          {
            "text": "CT1",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-26",
        "values": [
          {
            "text": "Human-adjudicated candidate",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-42",
        "values": [
          {
            "text": "R1.x / R2.x architecture",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-40",
        "values": [
          {
            "text": "historical_reference",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-41",
        "values": [
          {
            "text": "Unreconciled — no proven baseline crosswalk",
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
            "text": "b611a1fcfadb4b30af9ba74a4c993d8a",
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
            "text": "[CT1] Care Administration Enablement",
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
            "text": "CareAdministrationEnablement",
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
            "text": "CT1-BEC-CareAdministrationEnablement",
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
      "identifier": "id-4c438b06b04353078fcf98092deb2cb8",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-24f0d184ab385eb9a2fa8d8bc39ec06e",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-4c438b06b04353078fcf98092deb2cb8\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-07a798fb1ecd50bb8b4de3b12c34839d",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-87aa79f1e50459ef8826a46449a73600",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-07a798fb1ecd50bb8b4de3b12c34839d\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-ae4a716eea78515793b3f2b52d6e605c",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-48014b9acfa152b38e5b100233156e19",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-ae4a716eea78515793b3f2b52d6e605c\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-b16cddc08dc7581bafc1c99aef7e2126",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-0f0ebf04da8e5f5784bea0f593591996",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-b16cddc08dc7581bafc1c99aef7e2126\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-f30345ab17565cae978704203ce4d0af",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-baa0e64c54175bba86f21e766140414b",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-f30345ab17565cae978704203ce4d0af\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-b0bda80c28365d25928cf5fd2fdd91c3",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-a7f5492d52235705a4354970d8dabf62",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-b0bda80c28365d25928cf5fd2fdd91c3\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-170f12d6bd2c58e7967f6b3f587f08d7",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-740d5118859d5f269f7dbdfed7de3447",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-170f12d6bd2c58e7967f6b3f587f08d7\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-7408aa6816535c1e9263e223a07f1f8e",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-f84d4877352f5247adf6d6539b19019d",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-7408aa6816535c1e9263e223a07f1f8e\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-448baeeda52850eda24423cbefb97672",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-3caf2ebccbbe5caf9096e29855953186",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-448baeeda52850eda24423cbefb97672\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-a1f48f3216305a3888d3327fe134763a",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-2dda57f0b2de58bdb1d54a9e3b08747c",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-a1f48f3216305a3888d3327fe134763a\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-57fb5315f1ed5feab93000682620e615",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-653effd194c05bec9ce8e248f5272ff8",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-57fb5315f1ed5feab93000682620e615\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-bb59c2fa84115033be95c8e392b82f03",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-14ca90e0382a539ea11b61e33dc44336",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-bb59c2fa84115033be95c8e392b82f03\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-2295f92d2856570fb21af54e50ca14e3",
      "source": "id-47a8d188ce4c516f9865b6523723e1f3",
      "target": "id-36dc392f62815be1a97a7bb14799df15",
      "archimateType": "Composition",
      "attributes": {},
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-2295f92d2856570fb21af54e50ca14e3\"]"
      },
      "properties": [],
      "name": [],
      "documentation": []
    }
  ],
  "unresolvedProperties": {
    "definition": "Absent in the exact governed source and accepted representation."
  },
  "representation": {
    "sourceLinksRebased": true,
    "authority": "HCRL 1.1.5-002 DR-3; source meaning unchanged"
  }
}
---

# [CT1] Care Administration Enablement

## Architectural content

The governed source has no established definition. This absence is retained for Solution Architect review; no definition is inferred from the name.

## ARB correspondence

Same bounded capability at the same tier, BC/BEC context and complete recorded canonical hierarchy; current responsibility/provenance supplies the represented meaning. No contrary historical definition is present. Correspondence is adjudicated under the ARB semantic-continuity rule, not inferred by the application adapter.

Current representation: [[CT1] Care Administration Enablement](28df27d55c514a37995145ab46945f78.md). This resource reserves the historical identity and representation for provenance and existing Views. Its missing original definition remains missing.
