<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia RAD / Architecture Release 1.0.1

**Prepared and validated: 1.0.0 → 1.0.1.** The single
current-release authority is [architecture-release.json](../../../architecture-release.json).
The [governed release policy](../../markdown/governance/architecture-release-policy.md)
records the owner's decision, patch increment procedure and the JSON/Python convention
choice. Publication, inventory and this report read the shared authority. Regeneration
never increments it. Maven remains `1.0.0-SNAPSHOT`. No commit or Archi GUI review is claimed.

The mapping specification, catalogue, coverage assessment and incremental plan are
installed in governed RADS. Proposed mappings and missing architectural decisions
remain findings; installation does not authorise their semantic generation.

## Files added or changed in this bundle

Paths describe this bounded bundle relative to the working tree before installation;
the previously prepared `docs/archimate/` publication is already untracked. Unrelated
LaTeX publication changes and the existing Junie report are outside this bundle.

| File | Change |
| --- | --- |
| [AGENTS.md](../../../AGENTS.md) | Discoverable repository-wide release rule |
| [architecture-release.json](../../../architecture-release.json) | Single machine-readable release authority |
| [scripts/architecture_release.py](../../../scripts/architecture_release.py) | Shared loader, checks and explicit guarded patch increment |
| [scripts/tests/test_architecture_release.py](../../../scripts/tests/test_architecture_release.py) | Release validation and increment tests |
| [docs/markdown/governance/README.md](../../markdown/governance/README.md) | Governance navigation |
| [docs/markdown/governance/architecture-release-policy.md](../../markdown/governance/architecture-release-policy.md) | Owner-authorised release policy and mechanism choice |
| [docs/markdown/governance/rad-archimate-model-mapping.md](../../markdown/governance/rad-archimate-model-mapping.md) | Common-model mapping, authority, identity and publication rules |
| [docs/markdown/governance/rad-archimate-mapping-catalogue.md](../../markdown/governance/rad-archimate-mapping-catalogue.md) | Construct mappings and instance dispositions; unresolved decisions retained |
| [docs/markdown/governance/rad-archimate-coverage-assessment.md](../../markdown/governance/rad-archimate-coverage-assessment.md) | Coverage, source conflicts and readiness findings |
| [docs/markdown/governance/rad-archimate-publication-plan.md](../../markdown/governance/rad-archimate-publication-plan.md) | Small gated slices; later slices remain unauthorised |
| [docs/markdown/governance/rad-archimate-source-inventory.json](../../markdown/governance/rad-archimate-source-inventory.json) | Reproducible source assertions and assessment inventory |
| [docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml) | 14 exact-ID Documentation additions only |
| [docs/archimate/capability-architecture/Makefile](Makefile) | Bounded generation, assessment, tests, checking and reporting |
| [docs/archimate/capability-architecture/README.md](README.md) | Portable inputs, release authority and reproduction instructions |
| [docs/archimate/capability-architecture/documentation-map.json](documentation-map.json) | Explicit 14 EC UUID/Reference-ID/source crosswalk |
| [docs/archimate/capability-architecture/documentation-publication-report.md](documentation-publication-report.md) | Generated release change and validation report |
| [docs/archimate/capability-architecture/tests/test_documentation.py](tests/test_documentation.py) | Exact-ID, preservation, drift and portable release-input checks |
| [docs/archimate/capability-architecture/tests/test_publication_metadata.py](tests/test_publication_metadata.py) | Assessment findings and release-evidence consistency checks |
| [docs/archimate/capability-architecture/tools/assess_rad.py](tools/assess_rad.py) | Assessment inventory; no XML semantic generation |
| [docs/archimate/capability-architecture/tools/publish_documentation.py](tools/publish_documentation.py) | Constrained Documentation overlay and release-aware validation report |
| [docs/archimate/capability-architecture/tools/report_publication.py](tools/report_publication.py) | Report renderer reading authoritative release and pinned verification evidence |
| [docs/archimate/capability-architecture/validation.json](validation.json) | XML, identity, reference, deterministic-output and source checks |
| [docs/archimate/capability-architecture/release-verification.json](release-verification.json) | Bounded suite results and verified-file fingerprints |

## Identity and Documentation

Semantic identity changes: **0**. All **228 capabilities**,
**189 Composition relationships**, labels, properties,
hierarchy, model identity, existing Views and visual IDs remain intact.
Documentation enrichment: **14 exact matches**,
EC-01–EC-14; the remaining **214 identities**
remain unresolved and receive no inferred Documentation. No semantic objects or
relationships, new Views or visual identities are allocated in this release.

The unchanged View publisher is wrapped by an explicit exact-ID overlay. The manifest
checks the existing UUID, unique Reference ID, layer, source heading, source fingerprint
and full-description fingerprint. Names check drift; they confer no mapping authority.
Complete governed EC section bodies are copied verbatim, including EC-12/14 separation,
anti-responsibilities and EC-14 non-allocation/conceptual-sequence qualifications.

The baseline fixture and original semantic lock remain immutable. Validation removes
only the verified allowed Documentation additions in memory and checks the original
lock again. Existing UUIDv5 visual namespaces/keys and layout are unchanged: a tiered
layout stacks siblings vertically; unconnected capabilities use wrapped grids.
No missing BEC natural-group memberships or Enterprise hierarchy are inferred.

| Preserved generated View | Nodes | Connections |
| --- | ---: | ---: |
| Harmonia Capability Landscape | 39 | 0 |
| Business Capability Model | 52 | 34 |
| Business Enabling Capability Model | 162 | 155 |
| Enterprise Capability Model | 14 | 0 |

The baseline's original import diagnostic is also retained. Output:
[Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml).
SHA-256: `91af69c7d3f02ffadbe3b4f027421297b6f99e550b22582b72f71a4b3a56e43a`.

## Coverage and unresolved findings

The source inventory fingerprints **125 RADS files**
(the original discovery snapshot covered 124; the release policy is new).
It preserves 705 Business records, 462 relationship assertions, 73 contexts,
110 participant definitions, 57 Information Family section candidates and 146 C/F/D/M
rows. Heading counts are navigation evidence, not approved architecture element counts.
The assessment tool never feeds XML semantic generation.

- **214 unresolved baseline capability identities** remain unmapped. The 228-object
  Exchange baseline and current 280 established RAD capabilities remain unreconciled
  outside the 14 exact EC matches. Names, including exact label coincidences, confer
  no identity authority.
- **F46 identity collision** remains: Omilia operations/client experience and Shared
  Security and Assurance Nomenclature. No renumbering or merge is performed.
- **Component-register C keys** (and F/D/M keys) remain documentary navigation keys,
  not Canonical IDs. All 705 captured full Canonical IDs remain unallocated;
  137 local Feature IDs and incomplete BEC/Feature ancestry remain as governed.
- **Unresolved relationship assertions** remain: 22 captured assertions, including
  11 realises_feature and 11 associated_feature. Native predicate/profile decisions,
  capability ownership and information Requires/Consumes/Produces distinctions are
  not normalised into convenient graph edges.
- **Domain05 deferrals** remain: accepted candidate standing, B2.1 PASS only, later
  application allocation/derivation gates and the final Solution Architecture remain
  incomplete. C rows are not automatically 49 ApplicationComponents.
- **2,467 unresolved RAD dimensions/relationships** remain unchanged. The 53 absent
  capture summaries are source-review leads, not proof of absent governed definitions.
- Health Information Exchange category/name conflict, clinical-handover wording,
  mixed information/application types and unestablished accepted technology/migration
  catalogues remain findings. Missing cross-layer traceability is not invented.

See the [coverage assessment](../../markdown/governance/rad-archimate-coverage-assessment.md)
and [catalogue](../../markdown/governance/rad-archimate-mapping-catalogue.md) for evidence.

## Validation

| Check | Result |
| --- | --- |
| Publication regression/rejection suite | PASS — 40 tests; 28 View, 10 Documentation and 2 metadata tests |
| Root script tests (release and Business capture) | PASS — 43 tests; includes 3 release-governance tests |
| Required repository architecture suite | PASS — 90 tests; 0 failures, errors or skips |
| RAD Business capture validation | PASS — 0 invalid; 2,467 unresolved dimensions/relationships retained |
| Deterministic XML/report and source-inventory regeneration | PASS — Read-only make check; current release and source fingerprints agree |
| Baseline, lock, source semantics and unrelated working tree preservation | PASS — Fixture/lock/Maven unchanged; 123 original architectural source files unchanged; governance README navigation only; all assessment records unchanged; unrelated dirty paths untouched |
| Local Markdown file links | PASS — All local file targets resolve |
| Archi 5.9 bundled Model/View/Diagram XSDs, offline imports | PASS — serialized XML |
| Original semantics/IDs and all View/reference checks | PASS — original lock after only authorised Documentation is removed |
| Repeat generation, idempotence and configurable input portability | PASS |
| Archi GUI import and visual inspection | NOT PERFORMED — review before the next architecture slice |

[validation.json](validation.json) records detailed XML/identity/reference checks.
[release-verification.json](release-verification.json) records bounded command results
and file fingerprints. The report renderer refuses stale release or file evidence.
Commands below run from the repository root; publication commands inside Make are bounded.

```bash
make -C docs/archimate/capability-architecture/ generate inventory test check
timeout --signal=TERM --kill-after=5s 60s python3 -m unittest discover -s scripts/tests -v
timeout --signal=TERM --kill-after=10s 300s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
timeout --signal=TERM --kill-after=5s 60s python3 scripts/rads_business_architecture.py validate
```

## Presentation and stop point

Verbatim Markdown and relative source links require rendering review in Archi. The
BEC View remains tall (12,540 layout units); Enterprise remains flat because the
baseline supplies no hierarchy. No natural-group membership is inferred from names.
No GUI visual result is claimed by XSD validation.

This release stops at the installed mapping deliverables and 14 exact Documentation
additions, with four generated capability Views preserved. No next RAD publication
slice, Features, Functions or other architecture visualisation has begun.
