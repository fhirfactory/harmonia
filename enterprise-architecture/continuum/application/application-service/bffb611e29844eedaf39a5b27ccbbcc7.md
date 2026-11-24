---
{
  "schemaVersion": 1,
  "canonicalId": "bffb611e29844eedaf39a5b27ccbbcc7",
  "archimateType": "ApplicationService",
  "specialization": null,
  "displayName": "GovernedSummaryWriter",
  "friendlyName": "Mnemosyne.GovernedSummaryWriter",
  "canonicalName": null,
  "standing": {
    "semantic": "Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation",
    "currentness": "current"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md",
    "sourceAbsenceCheck": "6557-element canonical inventory and scoped bindings examined: no element separately represents this accepted service/cache responsibility or selected technology configuration.",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "mode": "Accepted source name; explicit logical home for application behaviour; selected technology configuration has local name; no formal type qualifier invented",
    "expected": {
      "friendlyName": "Mnemosyne.GovernedSummaryWriter",
      "canonicalName": null
    }
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md",
      "selector": "§§4.3.1,4.5,4.9–4.12; FC-01/04/05",
      "revision": "8204fd49a4d380c068a03acdf30c17bb06e5d953",
      "sha256": "9696e135a47fe5bd0d568a7f88624b64cb172d1c565c6529a60311b37431b3a6",
      "role": "Complete source approved and bounded ADD authorised by Mark Hunter in operative Task; original pre-approval wording is historical",
      "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md",
      "selector": "§§4.3.1,4.5,4.9–4.12; FC-01/04/05"
    }
  ],
  "relationships": [
    {
      "identifier": "id-c21977b99cac447487606e88b5600602",
      "archimateType": "Association",
      "source": "id-5b26c9db8c8f5308aea2c92a7812688d",
      "target": "id-829d7b5b57965765adf492ce3aaaa7d2",
      "name": [
        {
          "text": "Architectural Home in Mnemosyne",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Architectural Home in Mnemosyne. Accepted source §§2,4.4–4.5. Architectural contribution/allocation only; no implementation satisfaction, new authority, physical deployment multiplicity or stronger guarantee inferred."
        }
      ],
      "standing": {
        "semantic": "Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md",
        "selector": "§§2,4.4–4.5",
        "revision": "8204fd49a4d380c068a03acdf30c17bb06e5d953",
        "sha256": "9696e135a47fe5bd0d568a7f88624b64cb172d1c565c6529a60311b37431b3a6",
        "role": "Complete source approved and bounded ADD authorised by Mark Hunter in operative Task; original pre-approval wording is historical",
        "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md"
      },
      "specialization": "Home",
      "attributes": {
        "isDirected": "true"
      }
    },
    {
      "identifier": "id-be5525c1ea69466f972152164e1ca6c0",
      "archimateType": "Serving",
      "source": "id-5b26c9db8c8f5308aea2c92a7812688d",
      "target": "id-548e72ab6ead52c5b4897208d62320f2",
      "name": [
        {
          "text": "Provides authoritative Summary contract to Mneme Store",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Provides authoritative Summary contract to Mneme Store. Accepted source §§2,4.4–4.5. Architectural contribution/allocation only; no implementation satisfaction, new authority, physical deployment multiplicity or stronger guarantee inferred."
        }
      ],
      "standing": {
        "semantic": "Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md",
        "selector": "§§2,4.4–4.5",
        "revision": "8204fd49a4d380c068a03acdf30c17bb06e5d953",
        "sha256": "9696e135a47fe5bd0d568a7f88624b64cb172d1c565c6529a60311b37431b3a6",
        "role": "Complete source approved and bounded ADD authorised by Mark Hunter in operative Task; original pre-approval wording is historical",
        "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md"
      }
    },
    {
      "identifier": "id-4e8d7fa445484eb3b4a710bcebcd5a51",
      "archimateType": "Access",
      "source": "id-5b26c9db8c8f5308aea2c92a7812688d",
      "target": "id-f5c345ab6c47433da11ee12f5d84e557",
      "name": [
        {
          "text": "Uses same IU identity/version; Summary/full projection determined by this contract",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Uses same IU identity/version; Summary/full projection determined by this contract. Accepted source §§4.3–4.8. Architectural contribution/allocation only; no implementation satisfaction, new authority, physical deployment multiplicity or stronger guarantee inferred."
        }
      ],
      "standing": {
        "semantic": "Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md",
        "selector": "§§4.3–4.8",
        "revision": "8204fd49a4d380c068a03acdf30c17bb06e5d953",
        "sha256": "9696e135a47fe5bd0d568a7f88624b64cb172d1c565c6529a60311b37431b3a6",
        "role": "Complete source approved and bounded ADD authorised by Mark Hunter in operative Task; original pre-approval wording is historical",
        "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md"
      },
      "attributes": {
        "accessType": "ReadWrite"
      }
    },
    {
      "identifier": "id-769596f4cddc46769abd0d2f859f9c06",
      "archimateType": "Access",
      "source": "id-5b26c9db8c8f5308aea2c92a7812688d",
      "target": "id-572429fd8c905b8cb3a73cbc58351c70",
      "name": [
        {
          "text": "Reports factual operation Outcome without changing consumer disposition",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Reports factual operation Outcome without changing consumer disposition. Accepted source §4.9. Architectural contribution/allocation only; no implementation satisfaction, new authority, physical deployment multiplicity or stronger guarantee inferred."
        }
      ],
      "standing": {
        "semantic": "Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md",
        "selector": "§4.9",
        "revision": "8204fd49a4d380c068a03acdf30c17bb06e5d953",
        "sha256": "9696e135a47fe5bd0d568a7f88624b64cb172d1c565c6529a60311b37431b3a6",
        "role": "Complete source approved and bounded ADD authorised by Mark Hunter in operative Task; original pre-approval wording is historical",
        "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md"
      },
      "attributes": {
        "accessType": "Write"
      }
    },
    {
      "identifier": "id-a124dca3efa14025b956df16c15187a7",
      "archimateType": "Serving",
      "source": "id-5b26c9db8c8f5308aea2c92a7812688d",
      "target": "id-d0fdc0434d3d55379f64beb0d989bb5f",
      "name": [
        {
          "text": "Authoritative Summary operation supports Mneme-controlled reference-cache management",
          "attributes": {
            "{http://www.w3.org/XML/1998/namespace}lang": "en"
          }
        }
      ],
      "documentation": [
        {
          "text": "Authoritative Summary operation supports Mneme-controlled reference-cache management. Accepted source §§2,4.6; no cache population side effect implied. Architectural contribution/allocation only; no implementation satisfaction, new authority, physical deployment multiplicity or stronger guarantee inferred."
        }
      ],
      "standing": {
        "semantic": "Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation",
        "currentness": "current"
      },
      "provenance": {
        "source": "enterprise-architecture/design/offline-design/DRP-Mneme-Cache-Architecture.Final.001.md",
        "selector": "§§2,4.6; no cache population side effect implied",
        "revision": "8204fd49a4d380c068a03acdf30c17bb06e5d953",
        "sha256": "a95901d84c32c4568d5db01e170087097308a40b241b7b9254387e638c45d96c",
        "role": "Complete source approved and bounded ADD authorised by Mark Hunter in operative Task; original pre-approval wording is historical",
        "authority": "enterprise-architecture/design/evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md"
      }
    }
  ],
  "unresolvedProperties": {
    "canonicalName": "Governed formal qualifier for this native type/context remains unestablished; enterprise-architecture/design/review/1.1.24/hcrl-1.1.24-001.pending.md#dr-5. Do not infer a Class, Topic or naming token."
  },
  "exchange": {
    "identifier": "id-5b26c9db8c8f5308aea2c92a7812688d",
    "properties": []
  }
}
---

# GovernedSummaryWriter

## Architectural content

The initial contract supports **CREATE and RETIRE only**. CREATE durably registers an IU Summary with the supplied opaque ContentMetadata reference to Content represented as already managed externally. Mnemosyne does not retrieve, verify, copy, persist or rewrite that external Content. Local structural/metadata checks and security still apply. External custody, integrity, existence, currency, availability, durability and lifecycle are SEP under that service's own obligations. Success attests governed Summary/reference persistence only; later full-read failure does not invalidate registration.

RETIRE applies the governed Harmonia IU lifecycle while preserving required identity/history, immutable versions, retention holds, audit and provenance. It does not delete/retire/modify the external referent. Original Interaction DRP MI-07 remains the RETIRE/provider-DELETE mapping subject; any provider-specific physical disposition requires its own applicable authority.

There is **no UPDATE operation**. Full GovernedWriter updates its coherent IU Summary as part of the same full write, without calling SummaryWriter UPDATE. Version integrity may require a higher actual IU version, which is returned, while Mneme retains per-IU concurrency.

The IU Summary is the projection of the same accepted InformationUnit excluding **exactly InformationRepresentation.Content**. It retains Identity, applicable Metadata (ManagementContext, InformationCoordinates, SecurityContext, LifecycleContext and optional Relationships), ContainerMetadata and ContentMetadata. A Content reference belongs in ContentMetadata and is opaque to Mneme; Mnemosyne/PersistenceService resolve it with InformationCoordinates. ContentReference is illustrative vocabulary, not a prescribed field encoding. Summary projection does not promote transient ManagementContext data to mandatory durable metadata, create a second IU/InformationRepresentation, independent metadata authority or Summary version lineage. Logical separate Content addressability does not require separate physical databases or a particular native transaction.

All five contracts are synchronous governed request/response, with Calliope-owned factual Outcome (closed Success / Failure / Indeterminate and governed closed Reason vocabulary/extensions). Exact Reason members and stage codes remain deferred. Mnemosyne reports facts; Mneme determines consumer disposition without rewriting them. Transport/session timeout is not a factual operation Failure. Receipt, work acceptance, authoritative effect and end-to-end completion remain distinct.

Authentication and Themis policy apply: Readers require ACCESS + READ, SummarySearch invocation ACCESS + SEARCH and disclosed candidates ACCESS + READ, Writers CHANGE with the requested information operation. Protect identity, metadata, counts, pagination and other existence disclosures as well as Content. RequiredIU/SupportedIU capability matching grants no permission. Preserve source-near audit/provenance, Kleio mandatory capture authority/fail-closed obligations and Dokimasia/Provenance allocations; no blanket candidate event catalogue is promoted. Native database/query/provider values stay below the seam.

Every authoritative full or Summary mutation Success requires the applicable multi-site durable authoritative operation and actual resulting IU version, never cache success, local commit, replication queued or transport ACK. Immutable historical versions are preserved. Mneme retains individual-IU concurrency; eligible Mnemosyne instances must expose one coherent authoritative logical history or factual uncertainty. No extra seam transaction identifier or second version axis is introduced.

### Incorporation standing

Accepted complete Final.001 DRP semantics; authorised 1.1.24 ADD; actual incorporated candidate awaiting Solution Architect confirmation. Authority: operative Solution Architect Task and HCRL 1.1.24/001; input approval is distinct from result acceptance.
