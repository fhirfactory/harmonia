---
{
  "schemaVersion": 1,
  "canonicalId": "9e2ffa9b4a1d4422a33c2a4bbcfc86ef",
  "archimateType": "DataObject",
  "specialization": null,
  "displayName": "Topic",
  "friendlyName": "model.core.information.Topic",
  "canonicalName": null,
  "standing": {
    "semantic": "Accepted DRP semantics; authorised incorporation; resulting 1.1.22 increment awaiting Solution Architect confirmation",
    "currentness": "current"
  },
  "identity": {
    "authority": "enterprise-architecture/design/evidence/1.1.22/001/before/ai-agent-tasks/20261111T025829Z-information-unit-framework-incorporation.md",
    "allocation": "UUIDv4 allocated once by explicit create"
  },
  "naming": {
    "authority": "enterprise-architecture/governance/canonical-identity-and-naming.md",
    "mode": "Explicit logical information structure with recorded Composition context; no Class or named-Topic classification inferred",
    "expected": {
      "displayName": "Topic",
      "friendlyName": "model.core.information.Topic",
      "canonicalName": null
    }
  },
  "provenance": [
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
      "selector": "§§3–5,8,12,21 — common logical Topic model",
      "role": "Accepted whole DRP; IU-01–IU-13 adjudications; ADD authorised by operative Task",
      "retainedAt": "enterprise-architecture/design/evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md",
      "sha256": "98abd39feea66f5f9d5c5a9540d2053b7a340864e68b22d3ac415028f4c42775"
    }
  ],
  "sourceBindings": [
    {
      "source": "enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md",
      "selector": "§§3–5,8,12,21 — common logical Topic model"
    }
  ],
  "relationships": [],
  "unresolvedProperties": {
    "canonicalName": "No governed formal qualifier for an unspecialised logical DataObject; do not infer Class or Topic specialisation to populate a name. HCRL 1.1.22/001 DR-4. Identity, type and definition are established."
  },
  "exchange": {
    "identifier": "id-7b1ba31cbae65d08ab37f788cbbd4812",
    "properties": []
  }
}
---

# Topic

## Architectural content

The common logical Topic model describes Harmonia's governed, hierarchically named
identification/classification construct. A Topic has its own canonical identity
and a canonical name made of ordered dot-separated elements under its governed
naming/containment hierarchy. A TopicID identifies that Topic. It may name a gateway,
endpoint, location, owner, content structure, compartment, action or other concept;
it is neither its referent's implementation/configuration nor a broker destination.

This DataObject represents the **common logical model of Topics**, not a new named
Topic instance or an implementation Class. Existing specialised Topic elements,
their canonical IDs, classifications, names, containment and established scope
remain unchanged. No wildcard IU Topic instance or runtime UUID is allocated here.

An IU UUID identifies the IU; its own Topic provides the hierarchical canonical
name `harmonia.mneme.iu.<hex-string-of-compressed-uuid>`. Compression is deterministic
and reversible; exact encoding is deferred to the appropriate logical/implementation
contract. The namespace confers no IU ownership on Mneme. This IU designation is
distinct from coordinate, Content Structure and SecurityContext Topic references,
native Content/represented-entity identity, provider IDs, transport and correlation
identifiers. The earlier TopicID id example does not replace UUID-plus-Topic identity.
Referenced TopicIDs do not turn every metadata attribute into an independently
governed IU. Renaming a Topic can alter mask matching despite stable TopicID and
requires governance; name/identity/containment remain distinct semantic roles.

Calliope-Models defines shared semantics; runtime use belongs to consuming Features
and components. No registry service, registration mechanism, physical topology,
runtime model instance or new implementation dependency is prescribed.
