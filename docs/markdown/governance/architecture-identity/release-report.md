<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# First governed identity adoption — Release 1.0.3

Prepared for review: **1.0.2 → 1.0.3**, incremented
with `scripts/architecture_release.py bump --expect 1.0.2`.
The [root authority](../../../../architecture-release.json) remains the sole current
release value. Maven is unchanged. No commit or new GUI verification is claimed.
The owner confirmed the committed Release 1.0.1 publication was visually verified
and the 14 exact EC Documentation fields display correctly in Archi.

## Adopted identity chain and release counts

The [adoption decision](adoption-1.0.3.md) records the owner's bounded authorisation.
The [register](registry.json) adopts only EC-01–EC-14, retaining their governed
native IDs as persistent Architecture Identities, exact authoritative definition
provenance, qualified source locators, active lifecycle status and existing locked
semantic UUIDs. No historical aliases were needed. No IDs are rederived from EC
strings, and the XML gains no properties. The [1.0.2 identity model](model.md),
schema and immutable publication lock retain their meaning.

[adoption-validation.json](adoption-validation.json) proves each chain:
governed RAD definition → retained Architecture Identity → common register →
locked deterministic semantic binding → existing published element, with its
unchanged governed Documentation and View references. The
[frozen 1.0.2 snapshot](adoption-baseline-1.0.2.json) retains pre-adoption evidence
from commit `a3bc5aa7f38beacf66bb161f7164273cad91b8c4`; validation does not rely on a moving HEAD
to establish publication continuity after commit.

| Measure | Expected | Observed |
| --- | ---: | ---: |
| Identities adopted | 14 | 14 |
| New identities allocated | 0 | 0 |
| Semantic identity changes | 0 | 0 |
| Visual identity changes | 0 | 0 |
| Capability semantic changes | 0 | 0 |
| Capability hierarchy changes | 0 | 0 |
| Relationship changes / endpoint changes | 0 / 0 | 0 / 0 |
| Documentation changes | 0 | 0 |
| Unresolved Capability identities remaining | 214 | 214 |

The [inventory](inventory.md) still identifies 53 native governed definition IDs;
only the 14 authorised EC bindings are registered. Populations overlap and are
not a total architecture census.

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
| Identity register schema, history and bounded adoption | PASS — Exactly 14 retained EC identities; one governed definition and one existing semantic element each; no identity/alias collisions; 21 identity regression tests included in the root suite |
| Complete root script suite | PASS — 64 tests; 0 failures or errors, including identity, release governance and Business validation |
| Complete publication suite and regeneration | PASS — 41 tests; 0 failures or errors; deterministic generation, original IDs, Views, exact Documentation and portability |
| Required architecture suite | PASS — 90 tests; 0 failures, errors or skips |
| Archi 5.9 bundled Open Exchange XSD | PASS — Serialized XML valid against local Model/View/Diagram schemas; offline resolution |
| RAD Business capture validation | PASS — 0 invalid; exactly 2,467 unresolved dimensions/relationships retained |
| Full identity chain and publication continuity | PASS — 14 adoptions, 0 allocations; semantic IDs, visual IDs, capability content/hierarchy, relationships/endpoints and Documentation all unchanged; byte-identical XML; repeated deterministic generation |
| Frozen pre-adoption evidence | PASS — All preservation fingerprints and the empty prior register independently match committed Release 1.0.2 at a3bc5aa7f38beacf66bb161f7164273cad91b8c4 |
| Source provenance and unresolved finding preservation | PASS — Current definition/decision hashes verified independently of accepted history; RAD assertion and collision digests unchanged; F46, 214 capability crosswalks, incomplete IDs, C navigation keys, unresolved assertions and Domain05 deferrals retained |
| Release governance | PASS — Explicit 1.0.2 to 1.0.3 patch increment; generation does not increment it; no Maven POM/version changes |
| Markdown local file targets and diff whitespace | PASS — All changed/added Markdown file targets resolve; git diff --check passes |

The original 228 capabilities, 189 Composition relationships, four generated Views,
retained import diagnostic and 14 Documentation fields are byte-identical to the
committed Release 1.0.2 publication. Its SHA-256 is `91af69c7d3f02ffadbe3b4f027421297b6f99e550b22582b72f71a4b3a56e43a`.
The generated artefact is
[Harmonia-Capability-Architecture.xml](../../../archimate/capability-architecture/Harmonia-Capability-Architecture.xml).
The 1.0.1 Documentation report and verification
snapshot remain historical; this report records the identity release's results.
No additional Archi import is required to establish byte preservation; no new
visual inspection is claimed. Later semantic changes still require Archi review.

## Files added or changed

- `architecture-release.json`
- `docs/archimate/capability-architecture/Makefile`
- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/tests/test_publication_metadata.py`
- `docs/archimate/capability-architecture/validation.json`
- `docs/markdown/governance/README.md`
- `docs/markdown/governance/architecture-identity/adoption-1.0.3.md`
- `docs/markdown/governance/architecture-identity/adoption-baseline-1.0.2.json`
- `docs/markdown/governance/architecture-identity/adoption-validation.json`
- `docs/markdown/governance/architecture-identity/inventory.json`
- `docs/markdown/governance/architecture-identity/inventory.md`
- `docs/markdown/governance/architecture-identity/model.md`
- `docs/markdown/governance/architecture-identity/registry.json`
- `docs/markdown/governance/architecture-identity/release-report.md`
- `docs/markdown/governance/architecture-identity/verification.json`
- `docs/markdown/governance/rad-archimate-model-mapping.md`
- `docs/markdown/governance/rad-archimate-source-inventory.json`
- `scripts/architecture_identity.py`
- `scripts/tests/test_architecture_identity.py`

Authoritative Capability definitions and all source architectural assertions
remain intact. The immutable baseline fixture/semantic lock and legacy
publication locks are unchanged. [verification.json](verification.json) pins the
verified files and results. Commands are bounded; the validator reports missing
history/authority as failures rather than inventing identities.

## Unexpected findings and review stop

No unexpected architectural or publication finding was established. All pre-existing
findings above remain explicit. This release stops after the 14 adoptions,
regeneration and validation for human review. The remaining 214 Capabilities,
F46 adjudication, Domain03 identity migration, opaque allocation, further domains
and additional semantic constructs require separate authorisation.

## Canonical Documentation Assessment

The existing Exchange baseline, semantic lock and Documentation manifest outside
RADS supply the already reviewed publication-binding evidence referenced by the
governed mapping and identity model. No new architectural knowledge was imported
from legacy documentation. The authoritative Strategy EC definitions stay in
RADS; this task records only their authorised identity adoption and continuity.
No legacy reconciliation or domain completion is claimed.
