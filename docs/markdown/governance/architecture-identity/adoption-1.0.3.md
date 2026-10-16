<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Release 1.0.3 — First governed identity adoption

## Adoption decision

The architecture owner's explicit Release 1.0.3 task authorises adoption of only
the 14 exact Enterprise Capability bindings proven in Release 1.0.2. This records
that bounded decision in RADS under the [identity model](model.md); it authorises
no new construct identity allocation or additional publication slice.

Retain **EC-01–EC-14**, verbatim, as both their existing governed/native definition
IDs and their persistent `canonical_architecture_id` values. Adopt them with
`origin: retained`, `construct_type: Capability`, lifecycle `status: active`, and
no successors. Active describes these continuing identities; it does not change
the architectural standing, responsibility or allocation of their definitions.
The [Enterprise catalogue](../../02-strategy/capabilities/enterprise-capabilities.md)
remains their authoritative definition, including all exclusions and the EC-14
non-allocation boundary. No Capability Tier or component allocation is inferred.

The [register](registry.json) retains each exact definition heading and full source
fingerprint as provenance, and this decision as adoption authority, at Release
1.0.3. Its qualified `source/capture` identifier locates the native definition ID
within that source document. The definition ID is retained as Architecture
Identity by this explicit decision, not promoted by a source-key convention.
There are no historical identifier aliases to add: `aliases: []` is deliberate.
No C/F/D/M/CDG navigation key, Domain03 structural ID or implementation ID is
adopted or fabricated. Source locations and native definition IDs remain
distinguishable roles even where the accepted ID spelling is identical.

## Existing semantic bindings

Use `archimate.mode: locked` and the existing semantic IDs in the reviewed
[Documentation manifest](../../../archimate/capability-architecture/documentation-map.json).
These bindings have precedence over UUIDv5 derivation from a canonical handle.
Do not derive replacement UUIDs from EC strings. The table records the existing
bindings; it creates no new ArchiMate element.

| Architecture Identity / native definition ID | Existing ArchiMate semantic ID |
| --- | --- |
| EC-01 | `id-d53727d908365757a293b0896b2c769b` |
| EC-02 | `id-06e038737c5355edbc0591e08c594ab0` |
| EC-03 | `id-5891936941f65d20afad86b0697879c7` |
| EC-04 | `id-9e79bc474aed516c8c6752ab81b63d6b` |
| EC-05 | `id-e11f02764fe55ff29c0782db21198040` |
| EC-06 | `id-a6f6d30d8fce5ac78f60f7aefa71b28e` |
| EC-07 | `id-d71457435a085e749ffb1e1a3b551228` |
| EC-08 | `id-dbd66e69788450649e184966aa724797` |
| EC-09 | `id-770ebb3e3bdc5f2bbe77b2bfd55bd74d` |
| EC-10 | `id-49d4f69ebeeb5ea782a52172fb3e8a7b` |
| EC-11 | `id-82cd1f6a3e2352639e22c542f5cb3a9d` |
| EC-12 | `id-9bbd142f818e50d39a3d0429f74b72b4` |
| EC-13 | `id-7d6f635ffb6f5c6181bde1d07b859662` |
| EC-14 | `id-4dbf578f52bd5b3b9301b9807ce60f30` |

The common register's bindings are validated against the manifest, authoritative
headings, exact Reference-ID properties and published semantic objects. Visual
IDs remain the existing deterministic View/node/connection IDs and reference the
same semantic objects. The publication gains no properties. Existing Documentation
continues to copy the same complete governed sections onto the same 14 elements.

## Preservation and stop boundary

AX-04 preserves Harmonia meaning independently of the publication machinery;
AX-14 preserves identity-role and semantic distinctions; AX-17 preserves authority
and unresolved findings. This adoption changes no names, definitions, hierarchy,
relationships, Views or existing IDs and conflicts with none of those axioms.

The [1.0.2 preservation snapshot](adoption-baseline-1.0.2.json) records the accepted
commit, empty prior register, publication/source/lock fingerprints, semantic and
visual content hashes, and source assertion/collision digests. It is release
verification evidence, not another architectural identity or allocation authority.
Do not refresh that snapshot to make changed content pass.

Keep the 214 unresolved Capability crosswalks, F46 collision, CM-R05 name conflict,
incomplete structural Canonical IDs, C keys' navigation meaning, unresolved
relationship assertions, Domain05 deferrals, and 2,467 unresolved RAD dimensions
and relationships explicit. Stop after adoption, deterministic regeneration and
validation for human review. No remaining Capability adoption, F46 repair,
Domain03 migration, opaque ID allocation, new semantic construct or further RAD
domain publication is authorised.
