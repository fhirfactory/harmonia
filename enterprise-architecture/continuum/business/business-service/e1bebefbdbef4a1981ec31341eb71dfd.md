---
{
  "schemaVersion": 1,
  "canonicalId": "e1bebefbdbef4a1981ec31341eb71dfd",
  "archimateType": "BusinessService",
  "specialization": null,
  "displayName": "Consent Decision Service",
  "friendlyName": "EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConsentDirectiveEvaluation.ConsentDecisionService",
  "canonicalName": "CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConsentDirectiveEvaluation.SV-ConsentDecisionService",
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
      "displayName": "Consent Decision Service",
      "friendlyName": "EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConsentDirectiveEvaluation.ConsentDecisionService",
      "canonicalName": "CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConsentDirectiveEvaluation.SV-ConsentDecisionService"
    }
  },
  "provenance": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/422",
      "role": "governed source resource",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "0f7dc7b43da74dddffcb41b169031f0bd771c51509002851bb07c1f1a7eeee0b",
      "sourceKey": "service:consent-decision-service"
    },
    {
      "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
      "selector": "element[@identifier=\"id-f004e0762488533d95b1b0a829c9c89e\"]",
      "role": "accepted representation binding and presentation; not an additional architecture source",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1"
    },
    {
      "source": "docs/markdown/03-business-architecture/behaviours/01-entity-management.md",
      "selector": "24-client-privacy",
      "role": "original source locator; recorded edition",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "captureSource": "docs/markdown/governance/baseline/architecture.json",
      "recordedSourceSha256": "15903e877addfbbc5fdc256377f48d52b7d7ac18c7b4f040baa8e8361666c69e"
    },
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "source-qualified relationship assertions whose from-key binds to canonicalId e1bebefbdbef4a1981ec31341eb71dfd",
      "role": "Preserved non-native predicates, not new graph relationships",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0"
    },
    {
      "source": "docs/markdown/03-business-architecture/behaviours/01-entity-management.md",
      "selector": "entry lines 74-74",
      "role": "Exact source-locator contribution; original qualifications retained",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/422"
    }
  ],
  "exchange": {
    "identifier": "id-f004e0762488533d95b1b0a829c9c89e",
    "previousNativeName": "Consent Decision Service",
    "properties": [
      {
        "ref": "propid-1",
        "values": [
          {
            "text": "service:consent-decision-service",
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
            "text": "HCM-SV-consent-decision-service",
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
            "text": "service:consent-decision-service",
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
            "text": "CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.SV-CONSENT-DECISION-SERVICE",
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
            "text": "Consent Decision Service",
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
            "text": "[{\"anchor\":\"24-client-privacy\",\"line\":74,\"source\":\"em\",\"text\":\" - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.\"}]",
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
            "text": "{\"ancestry_status\":\"proposed\",\"architectural_status\":\"Canonical\",\"baseline_description\":{},\"baseline_placement\":{\"canonical_id\":null,\"owner_key\":\"capability:client-privacy\",\"parent_key\":null,\"tier\":null},\"baseline_unresolved\":[\"ancestry\",\"canonical_id\",\"code_allocation\"],\"baseline_version\":\"0.1\",\"canonical_id\":\"CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.SV-CONSENT-DECISION-SERVICE\",\"canonical_name\":\"Consent Decision Service\",\"category\":null,\"governed_definition_latex\":\"\\nProvides real-time consent evaluation decisions to Health Information Control.\\n\\n\",\"id_aliases\":[],\"key\":\"service:consent-decision-service\",\"latex_label\":\"service:consent-decision-service\",\"local_id\":\"CONSENT-DECISION-SERVICE\",\"name\":\"Consent Decision Service\",\"name_aliases\":[],\"name_status\":\"Canonical\",\"owner_identifier\":\"HCM-BEC-client-privacy\",\"owner_key\":\"capability:client-privacy\",\"parent_identifier\":null,\"parent_key\":null,\"placement_status\":\"Established\",\"record_state\":\"current\",\"reference_id\":\"HCM-SV-consent-decision-service\",\"responsibility_owner_key\":\"capability:client-privacy\",\"source_rad_documents\":[\"docs/markdown/03-business-architecture/behaviours/01-entity-management.md\"],\"sources\":[{\"anchor\":\"24-client-privacy\",\"line\":74,\"source\":\"em\",\"text\":\" - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.\"}],\"structural_id_status\":\"Proposed\",\"tier\":null,\"type\":\"service\",\"unresolved\":[],\"views\":[\"view:entity-management\"]}",
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
            "text": "HCM-BEC-client-privacy",
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
            "text": "[{\"disposition\":\"metadata_and_finding\",\"reason\":\"Feature association does not establish a particular native edge or whole-Feature realisation.\",\"record_state\":\"current\",\"snapshot_pointer\":\"/relationships/22\",\"source_assertion\":{\"from\":\"service:consent-decision-service\",\"sources\":[{\"anchor\":\"24-client-privacy\",\"line\":74,\"source\":\"em\",\"text\":\" - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.\"}],\"status\":\"established\",\"to\":\"feature:consent-directive-evaluation\",\"type\":\"associated_feature\"},\"source_index\":22}]",
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
            "text": "docs/markdown/governance/baseline/services.md#hcm-sv-consent-decision-service",
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
            "text": "e1bebefbdbef4a1981ec31341eb71dfd",
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
            "text": "Consent Decision Service",
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
            "text": "EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConsentDirectiveEvaluation.ConsentDecisionService",
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
            "text": "CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConsentDirectiveEvaluation.SV-ConsentDecisionService",
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
      "identifier": "id-e4bf2a4218b55d3b9f9eb26951aa5d37",
      "source": "id-f004e0762488533d95b1b0a829c9c89e",
      "target": "id-59c5db76c2d25e2d894cbaf05f986efb",
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
        "selector": "relationship[@identifier=\"id-e4bf2a4218b55d3b9f9eb26951aa5d37\"]"
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
      "identifier": "id-f9ff674beb6551c0ad7e041d0acbac1b",
      "archimateType": "Association",
      "source": "id-f004e0762488533d95b1b0a829c9c89e",
      "target": "id-59c5db76c2d25e2d894cbaf05f986efb",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "established",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.5/002/before/arb-reconciliation-plan.json",
        "selector": "/relationships/22",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
        "qualifiedSourceCanonicalId": "e1bebefbdbef4a1981ec31341eb71dfd",
        "sourceAssertion": {
          "from": "service:consent-decision-service",
          "sources": [
            {
              "anchor": "24-client-privacy",
              "line": 74,
              "source": "em",
              "text": " - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control."
            }
          ],
          "status": "established",
          "to": "feature:consent-directive-evaluation",
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
          "text": "Native treatment authorised by HCRL 1.1.5-002. Original qualification: {\"from\": \"service:consent-decision-service\", \"sources\": [{\"anchor\": \"24-client-privacy\", \"line\": 74, \"source\": \"em\", \"text\": \" - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.\"}], \"status\": \"established\", \"to\": \"feature:consent-directive-evaluation\", \"type\": \"associated_feature\"}",
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
              "text": "{\"from\": \"service:consent-decision-service\", \"sources\": [{\"anchor\": \"24-client-privacy\", \"line\": 74, \"source\": \"em\", \"text\": \" - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.\"}], \"status\": \"established\", \"to\": \"feature:consent-directive-evaluation\", \"type\": \"associated_feature\"}",
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
              "text": "/relationships/22",
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
      "sourcePointer": "/relationships/22",
      "sourceCanonicalId": "e1bebefbdbef4a1981ec31341eb71dfd",
      "targetCanonicalId": "baf6dc6ef38a4256a9a67a65fd3f31ba",
      "disposition": "metadata_and_finding",
      "reason": "Feature association does not establish a particular native edge or whole-Feature realisation.",
      "record_state": "current",
      "snapshot_pointer": "/relationships/22",
      "source_assertion": {
        "from": "service:consent-decision-service",
        "sources": [
          {
            "anchor": "24-client-privacy",
            "line": 74,
            "source": "em",
            "text": " - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control."
          }
        ],
        "status": "established",
        "to": "feature:consent-directive-evaluation",
        "type": "associated_feature"
      },
      "source_index": 22,
      "nativeMapping": {
        "disposition": "native relationship",
        "relationshipType": "Association",
        "reverse": false,
        "reason": "ARB predicate rule; native endpoint applicability verified.",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
        "relationshipIdentifier": "id-f9ff674beb6551c0ad7e041d0acbac1b",
        "sourceCanonicalId": "e1bebefbdbef4a1981ec31341eb71dfd",
        "targetCanonicalId": "baf6dc6ef38a4256a9a67a65fd3f31ba"
      }
    }
  ]
}
---

# Consent Decision Service

## Architectural content

Provides real-time consent evaluation decisions to Health Information Control.

## Qualified relationship assertions

Original source predicates, evidence and qualifications remain in the metadata. The ARB has now adjudicated their native treatment; explicitly unestablished relationship existence remains excluded. Native representation does not promote source standing.

| Source predicate | Target element | Source standing / currentness | ARB native treatment |
| --- | --- | --- | --- |
| associated_feature | [[Feature] Consent Directive Evaluation](../../strategy/capability/feature/baf6dc6ef38a4256a9a67a65fd3f31ba.md) | established / current | Association — `id-f9ff674beb6551c0ad7e041d0acbac1b` |

## Source-qualified contribution

Source: `docs/markdown/03-business-architecture/behaviours/01-entity-management.md` — entry lines 74-74. Standing: exact originating assertion; current resource standing unchanged.

- *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.
