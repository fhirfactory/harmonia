<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Governed Architecture Identity — Release 1.0.2

Prepared for review: **1.0.1 → 1.0.2**, incremented
with `scripts/architecture_release.py bump --expect 1.0.1`.
The [root authority](../../../../architecture-release.json) remains the sole current
release value. Maven is unchanged. No commit or new GUI verification is claimed.
The owner confirmed the committed Release 1.0.1 publication was visually verified
and the 14 exact EC Documentation fields display correctly in Archi.

## Delivered model and inventory

The [identity model](model.md) defines lifetime identity, structural/source/
navigation/semantic/visual/implementation distinctions, lifecycle, provenance and
migration gates across all domains. It recommends opaque UUIDv4 URNs for future
allocations, while retaining genuine existing native IDs exactly. Domain03's
typed structural grammar and capture field meanings remain intact. CM-R05 is
preserved independently of ID uniqueness. The RAD → ArchiMate mapping is updated.

The [inventory and collision report](inventory.md) provides coverage by construct
and all six requested classifications; [inventory.json](inventory.json) includes
per-record evidence, reasons, source fingerprints and observed ID token occurrences.
There are **53 native governed definition
IDs**, **14 safe exact EC adoption candidates**,
and **214 unresolved baseline capability
crosswalks**. Populations overlap; counts are not a total architecture census.

The empty common [registry](registry.json), [schema](registry.schema.json), validator
and synthetic regression fixtures establish enforceable identity/history rules.
**0 identities allocated, 0 migrated; 0 semantic identity changes.** The namespace
constant is not a construct allocation. No additional RAD domain or View is published.

## Collisions, uncertainty and retained findings

F46 remains an unresolved navigation-key collision between Omilia operations/client
experience and Shared Security and Assurance Nomenclature. Health Information
Exchange remains a CM-R05 name conflict between separate BC/BEC responsibilities.
Neither is repaired, merged or renamed. All 705 full structural Canonical IDs remain
unallocated; the 137 local Feature tokens retain their spelling and missing ancestry.
C/F/D/M/CDG keys remain navigation. The 22 unresolved captured assertions, all 189
baseline Composition crosswalks, Domain05 candidate standing and deferrals, and
**2,467 unresolved RAD dimensions/relationships** remain unchanged.

Information family candidates need construct/type adjudication; numbered Motivation
definitions need allocation; candidate requirement navigation rows require owning-
domain review. Technology/Platform and Implementation/Migration identity counts are
unestablished in inspected RADS. Legacy HCM/LaTeX identities and runtime/FHIR IDs
remain distinct evidence, not authority to assign RAD handles. Repeated reference
tokens are not counted as identity collisions.

## Validation

| Check | Result |
| --- | --- |
| Identity inventory and registry/schema/history checks | PASS — 13 identity regression tests included below; empty register, deterministic inventory, exact EC crosswalk and original semantic lock verified |
| Complete root script suite | PASS — 56 tests; includes 13 identity tests and existing release/Business validation tests |
| Complete publication suite | PASS — 40 tests; exact Documentation, original identities, Views, references, deterministic generation and portability |
| Required architecture suite | PASS — 90 tests; 0 failures, errors or skips |
| Archi 5.9 bundled Open Exchange XSD | PASS — Serialized publication XML valid against local Model/View/Diagram schemas; offline resolution |
| RAD Business capture validation | PASS — 0 invalid; exactly 2,467 unresolved dimensions/relationships retained |
| Identity and publication preservation | PASS — 0 allocations/migrations; XML bytes, 228 capabilities, 189 relationships, all Views and 14 Documentation fields unchanged from committed 1.0.1; baseline and legacy locks unchanged |
| Source finding preservation | PASS — All source assessment architectural assertions unchanged; Business capture differs only in the reviewed metamodel source fingerprint |
| Markdown local file targets | PASS — All added/changed document targets resolve |

The original 228 capabilities, 189 Composition relationships, four generated Views,
retained import diagnostic and 14 Documentation fields are byte-identical to the
committed publication. Its SHA-256 is `91af69c7d3f02ffadbe3b4f027421297b6f99e550b22582b72f71a4b3a56e43a`. Only release/
assessment evidence is refreshed. The 1.0.1 Documentation report and verification
snapshot remain historical; this report records the identity release's results.
No additional Archi import is required to establish byte preservation; no new
visual inspection is claimed. Later semantic changes still require Archi review.

## Files added or changed

- `architecture-release.json`
- `docs/archimate/capability-architecture/Makefile`
- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/tools/assess_rad.py`
- `docs/archimate/capability-architecture/validation.json`
- `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
- `docs/markdown/03-business-architecture/reference-model/business-architecture.json`
- `docs/markdown/governance/README.md`
- `docs/markdown/governance/architecture-identity/inventory.json`
- `docs/markdown/governance/architecture-identity/inventory.md`
- `docs/markdown/governance/architecture-identity/model.md`
- `docs/markdown/governance/architecture-identity/registry.json`
- `docs/markdown/governance/architecture-identity/registry.schema.json`
- `docs/markdown/governance/architecture-identity/release-report.md`
- `docs/markdown/governance/architecture-identity/verification.json`
- `docs/markdown/governance/rad-archimate-model-mapping.md`
- `docs/markdown/governance/rad-archimate-source-inventory.json`
- `scripts/architecture_identity.py`
- `scripts/tests/test_architecture_identity.py`

Source content outside the listed governance/metamodel clarification and tooling
changes remains intact. The immutable baseline fixture/semantic lock and legacy
publication locks are unchanged. [verification.json](verification.json) pins the
verified files and results. Commands are bounded; the validator reports missing
history/authority as failures rather than inventing identities.

## Recommended next slice and stop

Review this model and inventory first. If approved, adopt only EC-01–EC-14 into the
common register with their exact governed IDs, authoritative source fingerprints,
explicit adoption decisions and already locked semantic UUIDs. This would establish
registry continuity without allocating new capabilities or publishing another domain.
Then review the 214 unmapped baseline identities in separate bounded batches.
Do not bulk-allocate the Business capture or repair F46 as a generator operation.
This release stops before migration, allocation and additional-domain publication.
