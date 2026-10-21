---
{
  "schemaVersion": 1,
  "canonicalId": "b151ad42b9194d9e84b1dd9beecdf6dd",
  "archimateType": "BusinessService",
  "specialization": null,
  "displayName": "Diagnostic Report Ingress",
  "friendlyName": "CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportIngestionAndBinding.DiagnosticReportIngress",
  "canonicalName": "CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportIngestionAndBinding.SV-DiagnosticReportIngress",
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
      "displayName": "Diagnostic Report Ingress",
      "friendlyName": "CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportIngestionAndBinding.DiagnosticReportIngress",
      "canonicalName": "CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportIngestionAndBinding.SV-DiagnosticReportIngress"
    }
  },
  "provenance": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/500",
      "role": "governed source resource",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "0f7dc7b43da74dddffcb41b169031f0bd771c51509002851bb07c1f1a7eeee0b",
      "sourceKey": "service:diagnostic-report-ingress"
    },
    {
      "source": "docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml",
      "selector": "element[@identifier=\"id-77ff0fef85f05d44b38dda24fc719a2b\"]",
      "role": "accepted representation binding and presentation; not an additional architecture source",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1"
    },
    {
      "source": "docs/markdown/03-business-architecture/behaviours/02-service-administration.md",
      "selector": "7-diagnostic-administration",
      "role": "original source locator; recorded edition",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "captureSource": "docs/markdown/governance/baseline/architecture.json",
      "recordedSourceSha256": "7a26c3ad1d0581f6b5321b6089b99299e891256d5a739d5c074de306d9321878"
    },
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "source-qualified relationship assertions whose from-key binds to canonicalId b151ad42b9194d9e84b1dd9beecdf6dd",
      "role": "Preserved non-native predicates, not new graph relationships",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0"
    },
    {
      "source": "docs/markdown/03-business-architecture/behaviours/02-service-administration.md",
      "selector": "entry lines 120-120",
      "role": "Exact source-locator contribution; original qualifications retained",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/governance/baseline/architecture.json",
      "selector": "/elements/500"
    }
  ],
  "exchange": {
    "identifier": "id-77ff0fef85f05d44b38dda24fc719a2b",
    "previousNativeName": "Diagnostic Report Ingress",
    "properties": [
      {
        "ref": "propid-1",
        "values": [
          {
            "text": "service:diagnostic-report-ingress",
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
            "text": "HCM-SV-diagnostic-report-ingress",
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
            "text": "service:diagnostic-report-ingress",
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
            "text": "CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-REPORT-INGRESS",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-27",
        "values": [
          {
            "text": "[\"CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-REPORT-INGRESS\"]",
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
            "text": "Diagnostic Report Ingress",
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
            "text": "[{\"anchor\":\"7-diagnostic-administration\",\"line\":120,\"source\":\"sa\",\"text\":\" - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.\"}]",
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
            "text": "{\"ancestry_status\":\"proposed\",\"architectural_status\":\"Canonical\",\"baseline_description\":{},\"baseline_placement\":{\"canonical_id\":null,\"owner_key\":\"capability:diagnostic-administration\",\"parent_key\":null,\"tier\":null},\"baseline_unresolved\":[\"ancestry\",\"canonical_id\",\"code_allocation\"],\"baseline_version\":\"0.1\",\"canonical_id\":\"CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-REPORT-INGRESS\",\"canonical_name\":\"Diagnostic Report Ingress\",\"category\":null,\"governed_definition_latex\":\"\\nIngests results from external LIS/\\\\allowbreak{}RIS systems.\\n\\n\",\"id_aliases\":[\"CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-REPORT-INGRESS\"],\"key\":\"service:diagnostic-report-ingress\",\"latex_label\":\"service:diagnostic-report-ingress\",\"local_id\":\"DIAGNOSTIC-REPORT-INGRESS\",\"name\":\"Diagnostic Report Ingress\",\"name_aliases\":[],\"name_status\":\"Canonical\",\"owner_identifier\":\"HCM-BEC-diagnostic-administration\",\"owner_key\":\"capability:diagnostic-administration\",\"parent_identifier\":null,\"parent_key\":null,\"placement_status\":\"Established\",\"record_state\":\"current\",\"reference_id\":\"HCM-SV-diagnostic-report-ingress\",\"responsibility_owner_key\":\"capability:diagnostic-administration\",\"source_rad_documents\":[\"docs/markdown/03-business-architecture/behaviours/02-service-administration.md\"],\"sources\":[{\"anchor\":\"7-diagnostic-administration\",\"line\":120,\"source\":\"sa\",\"text\":\" - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.\"}],\"structural_id_status\":\"Proposed\",\"tier\":null,\"type\":\"service\",\"unresolved\":[],\"views\":[\"view:service-administration\"]}",
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
            "text": "HCM-BEC-diagnostic-administration",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-37",
        "values": [
          {
            "text": "Not Addressed in R1.x / R2.x",
            "attributes": {
              "{http://www.w3.org/XML/1998/namespace}lang": "en"
            }
          }
        ]
      },
      {
        "ref": "propid-38",
        "values": [
          {
            "text": "HCM-BEC-diagnostic-administration",
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
            "text": "[{\"disposition\":\"metadata_and_finding\",\"reason\":\"Feature association does not establish a particular native edge or whole-Feature realisation.\",\"record_state\":\"current\",\"snapshot_pointer\":\"/relationships/98\",\"source_assertion\":{\"from\":\"service:diagnostic-report-ingress\",\"sources\":[{\"anchor\":\"7-diagnostic-administration\",\"line\":120,\"source\":\"sa\",\"text\":\" - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.\"}],\"status\":\"established\",\"to\":\"feature:diagnostic-report-ingestion--binding\",\"type\":\"associated_feature\"},\"source_index\":98}]",
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
            "text": "docs/markdown/governance/baseline/services.md#hcm-sv-diagnostic-report-ingress",
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
            "text": "b151ad42b9194d9e84b1dd9beecdf6dd",
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
            "text": "Diagnostic Report Ingress",
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
            "text": "CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportIngestionAndBinding.DiagnosticReportIngress",
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
            "text": "CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportIngestionAndBinding.SV-DiagnosticReportIngress",
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
      "identifier": "id-3e5075abed5a599a874e5faa42b69a01",
      "source": "id-77ff0fef85f05d44b38dda24fc719a2b",
      "target": "id-edd2707ca7e458b190c0e857d51bc695",
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
        "selector": "relationship[@identifier=\"id-3e5075abed5a599a874e5faa42b69a01\"]"
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
      "identifier": "id-3299f4b6a38558188642ba9d76298a1b",
      "archimateType": "Association",
      "source": "id-77ff0fef85f05d44b38dda24fc719a2b",
      "target": "id-edd2707ca7e458b190c0e857d51bc695",
      "attributes": {
        "isDirected": "true"
      },
      "standing": {
        "semantic": "established",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/evidence/1.1.5/002/before/arb-reconciliation-plan.json",
        "selector": "/relationships/98",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
        "qualifiedSourceCanonicalId": "b151ad42b9194d9e84b1dd9beecdf6dd",
        "sourceAssertion": {
          "from": "service:diagnostic-report-ingress",
          "sources": [
            {
              "anchor": "7-diagnostic-administration",
              "line": 120,
              "source": "sa",
              "text": " - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems."
            }
          ],
          "status": "established",
          "to": "feature:diagnostic-report-ingestion--binding",
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
          "text": "Native treatment authorised by HCRL 1.1.5-002. Original qualification: {\"from\": \"service:diagnostic-report-ingress\", \"sources\": [{\"anchor\": \"7-diagnostic-administration\", \"line\": 120, \"source\": \"sa\", \"text\": \" - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.\"}], \"status\": \"established\", \"to\": \"feature:diagnostic-report-ingestion--binding\", \"type\": \"associated_feature\"}",
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
              "text": "{\"from\": \"service:diagnostic-report-ingress\", \"sources\": [{\"anchor\": \"7-diagnostic-administration\", \"line\": 120, \"source\": \"sa\", \"text\": \" - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.\"}], \"status\": \"established\", \"to\": \"feature:diagnostic-report-ingestion--binding\", \"type\": \"associated_feature\"}",
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
              "text": "/relationships/98",
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
      "sourcePointer": "/relationships/98",
      "sourceCanonicalId": "b151ad42b9194d9e84b1dd9beecdf6dd",
      "targetCanonicalId": "a00823445a8a44c1a00f3ec93a7e0e6f",
      "disposition": "metadata_and_finding",
      "reason": "Feature association does not establish a particular native edge or whole-Feature realisation.",
      "record_state": "current",
      "snapshot_pointer": "/relationships/98",
      "source_assertion": {
        "from": "service:diagnostic-report-ingress",
        "sources": [
          {
            "anchor": "7-diagnostic-administration",
            "line": 120,
            "source": "sa",
            "text": " - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems."
          }
        ],
        "status": "established",
        "to": "feature:diagnostic-report-ingestion--binding",
        "type": "associated_feature"
      },
      "source_index": 98,
      "nativeMapping": {
        "disposition": "native relationship",
        "relationshipType": "Association",
        "reverse": false,
        "reason": "ARB predicate rule; native endpoint applicability verified.",
        "authority": "enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation",
        "relationshipIdentifier": "id-3299f4b6a38558188642ba9d76298a1b",
        "sourceCanonicalId": "b151ad42b9194d9e84b1dd9beecdf6dd",
        "targetCanonicalId": "a00823445a8a44c1a00f3ec93a7e0e6f"
      }
    }
  ]
}
---

# Diagnostic Report Ingress

## Architectural content

Ingests results from external LIS/RIS systems.

## Qualified relationship assertions

Original source predicates, evidence and qualifications remain in the metadata. The ARB has now adjudicated their native treatment; explicitly unestablished relationship existence remains excluded. Native representation does not promote source standing.

| Source predicate | Target element | Source standing / currentness | ARB native treatment |
| --- | --- | --- | --- |
| associated_feature | [[Feature] Diagnostic Report Ingestion & Binding](../../strategy/capability/feature/a00823445a8a44c1a00f3ec93a7e0e6f.md) | established / current | Association — `id-3299f4b6a38558188642ba9d76298a1b` |

## Source-qualified contribution

Source: `docs/markdown/03-business-architecture/behaviours/02-service-administration.md` — entry lines 120-120. Standing: exact originating assertion; current resource standing unchanged.

- *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.
