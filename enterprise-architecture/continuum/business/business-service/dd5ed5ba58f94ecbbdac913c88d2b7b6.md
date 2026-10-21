---
{
  "schemaVersion": 1,
  "canonicalId": "dd5ed5ba58f94ecbbdac913c88d2b7b6",
  "archimateType": "BusinessService",
  "specialization": null,
  "displayName": "Person Identity Correction",
  "friendlyName": "EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.GovernedIdentityCorrection.PersonIdentityCorrection",
  "canonicalName": "CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-GovernedIdentityCorrection.SV-PersonIdentityCorrection",
  "standing": {
    "semantic": "Canonical",
    "currentness": "current",
    "placement_status": "Established",
    "structural_id_status": "Proposed",
    "name_status": "Canonical",
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
      "displayName": "Person Identity Correction",
      "friendlyName": "EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.GovernedIdentityCorrection.PersonIdentityCorrection",
      "canonicalName": "CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-GovernedIdentityCorrection.SV-PersonIdentityCorrection"
    }
  },
  "provenance": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/412",
      "role": "governed source resource",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "0f7dc7b43da74dddffcb41b169031f0bd771c51509002851bb07c1f1a7eeee0b",
      "sourceKey": "service:person-identity-correction"
    },
    {
      "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
      "selector": "element[@identifier=\"id-a236d718153153808c072f1d8cae8c1f\"]",
      "role": "accepted representation binding and presentation; not an additional architecture source",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1"
    },
    {
      "source": "docs/markdown/03-business-architecture/behaviours/01-entity-management.md",
      "selector": "21-person-identity",
      "role": "original source locator; recorded edition",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "captureSource": "docs/markdown/governance/baseline/architecture.json",
      "recordedSourceSha256": "15903e877addfbbc5fdc256377f48d52b7d7ac18c7b4f040baa8e8361666c69e"
    },
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "source-qualified relationship assertions whose from-key binds to canonicalId dd5ed5ba58f94ecbbdac913c88d2b7b6",
      "role": "Preserved non-native predicates, not new graph relationships",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0"
    },
    {
      "source": "docs/markdown/03-business-architecture/behaviours/01-entity-management.md",
      "selector": "entry lines 34-34",
      "role": "Exact source-locator contribution; original qualifications retained",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/412"
    }
  ],
  "exchange": {
    "identifier": "id-a236d718153153808c072f1d8cae8c1f",
    "previousNativeName": "Person Identity Correction",
    "properties": [
      {
        "ref": "propid-1",
        "values": [
          {
            "text": "service:person-identity-correction",
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
            "text": "HCM-SV-person-identity-correction",
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
            "text": "service:person-identity-correction",
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
            "text": "service",
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
            "text": "Canonical",
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
            "text": "current",
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
            "text": "Established",
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
            "text": "CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.SV-PERSON-IDENTITY-CORRECTION",
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
            "text": "Canonical",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-28",
        "values": [
          {
            "text": "Person Identity Correction",
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
        "ref": "propid-30",
        "values": [
          {
            "text": "[{\"anchor\":\"21-person-identity\",\"line\":34,\"source\":\"em\",\"text\":\" - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.\"}]",
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
            "text": "{\"ancestry_status\":\"proposed\",\"architectural_status\":\"Canonical\",\"baseline_description\":{},\"baseline_placement\":{\"canonical_id\":null,\"owner_key\":\"capability:person-identity\",\"parent_key\":null,\"tier\":null},\"baseline_unresolved\":[\"ancestry\",\"canonical_id\",\"code_allocation\"],\"baseline_version\":\"0.1\",\"canonical_id\":\"CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.SV-PERSON-IDENTITY-CORRECTION\",\"canonical_name\":\"Person Identity Correction\",\"category\":null,\"governed_definition_latex\":\"\\nExposed to authorized HIM/\\\\allowbreak{}Medical Records administrators.\\n\\n\",\"id_aliases\":[],\"key\":\"service:person-identity-correction\",\"latex_label\":\"service:person-identity-correction\",\"local_id\":\"PERSON-IDENTITY-CORRECTION\",\"name\":\"Person Identity Correction\",\"name_aliases\":[],\"name_status\":\"Canonical\",\"owner_identifier\":\"HCM-BEC-person-identity\",\"owner_key\":\"capability:person-identity\",\"parent_identifier\":null,\"parent_key\":null,\"placement_status\":\"Established\",\"record_state\":\"current\",\"reference_id\":\"HCM-SV-person-identity-correction\",\"responsibility_owner_key\":\"capability:person-identity\",\"source_rad_documents\":[\"docs/markdown/03-business-architecture/behaviours/01-entity-management.md\"],\"sources\":[{\"anchor\":\"21-person-identity\",\"line\":34,\"source\":\"em\",\"text\":\" - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.\"}],\"structural_id_status\":\"Proposed\",\"tier\":null,\"type\":\"service\",\"unresolved\":[],\"views\":[\"view:entity-management\"]}",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-33",
        "values": [
          {
            "text": "HCM-BEC-person-identity",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-16",
        "values": [
          {
            "text": "[{\"disposition\":\"metadata_and_finding\",\"reason\":\"Feature association does not establish a particular native edge or whole-Feature realisation.\",\"record_state\":\"current\",\"snapshot_pointer\":\"/relationships/12\",\"source_assertion\":{\"from\":\"service:person-identity-correction\",\"sources\":[{\"anchor\":\"21-person-identity\",\"line\":34,\"source\":\"em\",\"text\":\" - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.\"}],\"status\":\"established\",\"to\":\"feature:governed-identity-correction\",\"type\":\"associated_feature\"},\"source_index\":12}]",
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
            "text": "docs/markdown/governance/baseline/services.md#hcm-sv-person-identity-correction",
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
            "text": "dd5ed5ba58f94ecbbdac913c88d2b7b6",
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
            "text": "Person Identity Correction",
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
            "text": "EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.GovernedIdentityCorrection.PersonIdentityCorrection",
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
            "text": "CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-GovernedIdentityCorrection.SV-PersonIdentityCorrection",
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
            "text": "BusinessService",
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
      "identifier": "id-522e3c0de84f53509fc53e401c579c8e",
      "source": "id-a236d718153153808c072f1d8cae8c1f",
      "target": "id-6f0bfe5e814c5734a7c530f2b7515666",
      "archimateType": "Association",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "source": "Retained accepted model assertion; original qualifications apply"
      },
      "provenance": {
        "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
        "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
        "selector": "relationship[@identifier=\"id-522e3c0de84f53509fc53e401c579c8e\"]"
      },
      "properties": [
        {
          "ref": "specialization",
          "values": [
            {
              "text": "Home",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ],
      "specialization": "Home",
      "name": [],
      "documentation": []
    },
    {
      "identifier": "id-ce3f2c3bce4a55bea7b58ccf7560ed6e",
      "archimateType": "Association",
      "source": "id-a236d718153153808c072f1d8cae8c1f",
      "target": "id-6f0bfe5e814c5734a7c530f2b7515666",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "established",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.5/002/before/arb-reconciliation-plan.json",
        "selector": "/relationships/12",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
        "qualifiedSourceCanonicalId": "dd5ed5ba58f94ecbbdac913c88d2b7b6",
        "sourceAssertion": {
          "from": "service:person-identity-correction",
          "sources": [
            {
              "anchor": "21-person-identity",
              "line": 34,
              "source": "em",
              "text": " - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators."
            }
          ],
          "status": "established",
          "to": "feature:governed-identity-correction",
          "type": "associated_feature"
        }
      },
      "name": [
        {
          "text": "associated_feature",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Native treatment authorised by HCRL 1.1.5-002. Original qualification: {\"from\": \"service:person-identity-correction\", \"sources\": [{\"anchor\": \"21-person-identity\", \"line\": 34, \"source\": \"em\", \"text\": \" - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.\"}], \"status\": \"established\", \"to\": \"feature:governed-identity-correction\", \"type\": \"associated_feature\"}",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "properties": [
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
              "text": "established",
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
              "text": "associated_feature",
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
              "text": "{\"from\": \"service:person-identity-correction\", \"sources\": [{\"anchor\": \"21-person-identity\", \"line\": 34, \"source\": \"em\", \"text\": \" - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.\"}], \"status\": \"established\", \"to\": \"feature:governed-identity-correction\", \"type\": \"associated_feature\"}",
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
              "text": "/relationships/12",
              "attributes": {
                "{http://www.w3.org/XML/1998/namespace}lang": "en"
              }
            }
          ]
        }
      ]
    }
  ],
  "unresolvedProperties": {},
  "representation": {
    "sourceLinksRebased": true,
    "authority": "HCRL 1.1.5-002 DR-3; source meaning unchanged"
  },
  "qualifiedAssertions": [
    {
      "sourcePointer": "/relationships/12",
      "sourceCanonicalId": "dd5ed5ba58f94ecbbdac913c88d2b7b6",
      "targetCanonicalId": "f7ab7a24e8234129b2ab02e9abd4fba2",
      "disposition": "metadata_and_finding",
      "reason": "Feature association does not establish a particular native edge or whole-Feature realisation.",
      "record_state": "current",
      "snapshot_pointer": "/relationships/12",
      "source_assertion": {
        "from": "service:person-identity-correction",
        "sources": [
          {
            "anchor": "21-person-identity",
            "line": 34,
            "source": "em",
            "text": " - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators."
          }
        ],
        "status": "established",
        "to": "feature:governed-identity-correction",
        "type": "associated_feature"
      },
      "source_index": 12,
      "nativeMapping": {
        "disposition": "native relationship",
        "relationshipType": "Association",
        "reverse": false,
        "reason": "ARB predicate rule; native endpoint applicability verified.",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
        "relationshipIdentifier": "id-ce3f2c3bce4a55bea7b58ccf7560ed6e",
        "sourceCanonicalId": "dd5ed5ba58f94ecbbdac913c88d2b7b6",
        "targetCanonicalId": "f7ab7a24e8234129b2ab02e9abd4fba2"
      }
    }
  ]
}
---

# Person Identity Correction

## Architectural content

Exposed to authorized HIM/Medical Records administrators.

## Qualified relationship assertions

Original source predicates, evidence and qualifications remain in the metadata. The ARB has now adjudicated their native treatment; explicitly unestablished relationship existence remains excluded. Native representation does not promote source standing.

| Source predicate | Target element | Source standing / currentness | ARB native treatment |
| --- | --- | --- | --- |
| associated_feature | [[Feature] Governed Identity Correction](../../strategy/capability/feature/f7ab7a24e8234129b2ab02e9abd4fba2.md) | established / current | Association — `id-ce3f2c3bce4a55bea7b58ccf7560ed6e` |

## Source-qualified contribution

Source: `docs/markdown/03-business-architecture/behaviours/01-entity-management.md` — entry lines 34-34. Standing: exact originating assertion; current resource standing unchanged.

- *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.
