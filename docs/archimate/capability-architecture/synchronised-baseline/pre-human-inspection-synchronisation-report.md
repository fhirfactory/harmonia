<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate / Markdown baseline synchronisation — 2026-10-17

Ready for Mark's final inspection of the corrected **1.0.6** candidate.
Mark accepted the initial baseline in substance after inspection in Archi 5.10;
this report records the bounded corrections. **No commit or new GUI acceptance is claimed.**
The [initial report](initial-synchronisation-report.md) and
[initial verification](initial-synchronisation-verification.json) preserve the prior audit stage.
Import [Harmonia-Architecture-Baseline.xml](Harmonia-Architecture-Baseline.xml) in Archi 5.10.0.
Use the [inspection guide](archi-inspection-guide.md) and
[canonical Markdown baseline](../../../markdown/governance/baseline/README.md).

## Starting state, authority and release

The initial synchronisation began on branch `Harmonia-GovernedAccess`,
commit `227895b66bc203446581a0483097bff1603e9966`, with a clean working tree at that earlier
task start. This bounded correction began from its **uncommitted 1.0.6 candidate**.
Before the initial synchronisation, Repository Architecture Release was **1.0.5**,
preceded by 1.0.4. The recovery task label 1.0.5A is not a release register.
The explicit patch helper prepared **1.0.5 → 1.0.6**
for this single change set. Generation reads the root release; it never increments it.
Acceptance and committing remain human actions. Historical recovery replay now reads
an explicit 1.0.5 fixture, so later current releases cannot rewrite its evidence.

The explicit task decision establishes the recovered/reconciled 1.0.5A architecture
and corresponding current RADS as this working baseline. It does not promote
Candidate, Proposed, pending, superseded or historical material. AX-04 separates
Harmonia meaning from representation machinery, AX-14 preserves distinctions and
identity roles, AX-15 preserves uncertain outcomes, and AX-17 prevents inferred
architecture or silent adjudication. No architecture-design conflict was resolved
by navigation, prose cleanup or name matching. No historical conversations were used.

## Inputs inspected and source boundaries

- `AGENTS.md`
- `docs/AGENTS.md`
- `docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml`
- `docs/archimate/capability-architecture/Makefile`
- `docs/archimate/capability-architecture/baseline/Harmonia-Capability-Architecture-v2.1a.xml`
- `docs/archimate/capability-architecture/baseline/navigation-baseline-1.0.3.json`
- `docs/archimate/capability-architecture/baseline/semantic-lock.json`
- `docs/archimate/capability-architecture/documentation-map.json`
- `docs/archimate/capability-architecture/recovery-v0.2/1.0.5A-recovery-report.md`
- `docs/archimate/capability-architecture/recovery-v0.2/Harmonia-Architecture-1.0.5A.xml`
- `docs/archimate/capability-architecture/recovery-v0.2/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/recovery-v0.2/architecture-release.json`
- `docs/archimate/capability-architecture/recovery-v0.2/recovery-findings.json`
- `docs/archimate/capability-architecture/recovery-v0.2/representation-crosswalk.json`
- `docs/archimate/capability-architecture/recovery-v0.2/source-manifest.json`
- `docs/archimate/capability-architecture/tools/generate_views.py`
- `docs/archimate/capability-architecture/tools/publish_documentation.py`
- `docs/archimate/capability-architecture/tools/publish_navigation.py`
- `docs/archimate/capability-architecture/tools/recover_v02.py`
- `docs/latex-legacy/chapters/01-motivation-strategy-old.tex`
- `docs/latex-legacy/chapters/01-motivation-strategy.tex`
- `docs/latex-legacy/chapters/appendix-requirements.tex`
- `docs/latex-legacy/diagrams/fig-motivation-map.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/Harmonia-EA-Capability-Map.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/Makefile`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/README.md`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/architecture-tests.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/bec-reconciliation-ledger.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/catalogue/proposals.md`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/catalogue/review-focus.md`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/change-ledger.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/corpus-register.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/derivation-provenance.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/identity-lock.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/model-validation.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/model.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/pre-review-identity-lock.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/pre-review-publication-evidence.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/pre-review-publication-labels.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/pre-review-v0.3-reconciliation.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/publication-preservation.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reconciliation-tests.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-identity-history.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/bec-review.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/business-capabilities.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/business-enabling-capabilities.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/catalogue-contexts.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/enterprise-capabilities.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/identifiers.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/preserved-responsibilities.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-assurance-strategy.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-assurance.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-axioms.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-b2-1.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-business.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-dependencies.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-detail.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-em.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-enabling.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-enterprise.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-hso.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-ise.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-k9.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-metamodel.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-processes.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-sa.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-sd.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-tier-model.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/rad-traceability.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/reading-guide.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/reconciliation.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/relationships.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/sections/sources.tex`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/semantic-baseline.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/source-coverage.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/standalone-validation.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/baseline.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/check.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/compose.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/navigation.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/probes.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/reconcile.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/render.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/report.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/reviewed_bec.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/test_reviewed_bec.py`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/v0.1-fingerprints.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/v0.3-reconciliation.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/validation.json`
- `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/validator-probes.json`
- `docs/markdown/01-motivation/drivers-assessments/assessments.md`
- `docs/markdown/01-motivation/drivers-assessments/drivers.md`
- `docs/markdown/01-motivation/goals-outcomes/business-outcomes.md`
- `docs/markdown/01-motivation/goals-outcomes/strategic-goals.md`
- `docs/markdown/01-motivation/principles/reclassified-principles.md`
- `docs/markdown/01-motivation/requirements-constraints/external-constraints.md`
- `docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md`
- `docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md`
- `docs/markdown/01-motivation/stakeholders/enterprise-stakeholders.md`
- `docs/markdown/02-strategy/capability-maps/capability-tier-model.md`
- `docs/markdown/02-strategy/reviews/fixed-capability-decomposition-decision.md`
- `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
- `docs/markdown/03-business-architecture/reference-model/README.md`
- `docs/markdown/03-business-architecture/reference-model/business-architecture.json`
- `docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md`
- `docs/markdown/05-information-systems-architecture/component-register.md`
- `docs/markdown/05-information-systems-architecture/data-and-persistence.md`
- `docs/markdown/05-information-systems-architecture/middleware.md`
- `docs/markdown/architecture-completion-plan.md`
- `docs/markdown/governance/archi-navigation-and-view-convention.md`
- `docs/markdown/governance/architectural-axioms.md`
- `docs/markdown/governance/architecture-identity/adoption-1.0.3.md`
- `docs/markdown/governance/architecture-identity/adoption-baseline-1.0.2.json`
- `docs/markdown/governance/architecture-identity/inventory.md`
- `docs/markdown/governance/architecture-identity/model.md`
- `docs/markdown/governance/architecture-identity/registry.json`
- `docs/markdown/governance/architecture-identity/registry.schema.json`
- `docs/markdown/governance/architecture-release-policy.md`
- `docs/markdown/governance/baseline/README.md`
- `docs/markdown/governance/baseline/documentation-classification.json`
- `docs/markdown/governance/rad-archimate-mapping-catalogue.md`
- `docs/markdown/governance/rad-archimate-model-mapping.md`
- `docs/markdown/governance/rad-archimate-publication-plan.md`
- `scripts/architecture_identity.py`
- `scripts/architecture_release.py`
- `scripts/rads_business_architecture.py`

The recovery manifest's 75 source/tooling/identity inputs were checked without
recomposition. The frozen model, all 30 included TeX sections, reconciliation and
identity locks supply the exact recovered correspondence. The source/capture
identity keys provide continuity; catalogue labels and headings do not.
The new current capture is byte-identical to the pinned 946-record structured
source. Current Motivation uses complete live RADS sections plus explicit typed
historical source assertions, checked separately against their own fingerprints.
The former 1.0.4 publication remains comparison evidence only.

Current inputs, immutable references, files inspected and verification fingerprints
are recorded in [verification.json](verification.json) and
[correspondence.json](../../../markdown/governance/baseline/correspondence.json).
Generation has no conversational-memory, PDF, network, date-clock or unrelated
live-source dependency. Existing source-era capture validation uses only the exact
hash-proved presentation insertions in `source-presentation-overlays.json`; it does
not replace old evidence with newer definitions.

## Quantitative preservation

| Object | Before | After | Preservation |
| --- | ---: | ---: | --- |
| Semantic elements | 1,165 | 1,299 | All 1,165 existing IDs, names and native types retained; zero removals |
| Relationships | 823 | 852 | All 823 existing IDs, endpoints, Documentation and Properties retained |
| Views | 984 | 1012 | All 984 retained byte-for-byte in content, including nodes, connections and geometry |
| Capability-typed elements | 841 | 841 | No new Capability or navigation copy |
| Adopted Architecture Identity bindings | 14 | 14 | All exact EC bindings retained; zero allocations |
| Source records | 946 | 946 | Full JSON records and existing version-qualified Properties preserved |
| Source relationships | 1,096 | 1,096 | 634 native mappings; 462 explicit metadata/findings |
| Source current / pending / superseded records | 845 / 69 / 32 | 845 / 69 / 32 | Independent semantic standing and placement retained |
| Source Canonical / Proposed / Candidate records | 716 / 227 / 3 | 716 / 227 / 3 | No promotion |

| Native element type | Before | After |
| --- | ---: | ---: |
| Assessment | 0 | 5 |
| BusinessFunction | 160 | 160 |
| BusinessProcess | 20 | 20 |
| BusinessService | 139 | 139 |
| Capability | 841 | 841 |
| Constraint | 0 | 3 |
| Driver | 0 | 10 |
| Goal | 0 | 9 |
| Outcome | 0 | 3 |
| Principle | 5 | 25 |
| Requirement | 0 | 74 |
| Stakeholder | 0 | 10 |

All original source Properties survive unchanged, including exact source JSON,
IDs and aliases, structural path/standing, provenance, record state, placement,
scope, name history, adjudication notes and deferred relationship assertions.
New `Baseline / …` Properties qualify the textual correspondence and current
Motivation/source standing separately from historical `v0.2 / …` metadata.
No source structural identifier, HCM reference, local Feature token, capture key,
semantic XML ID or visual ID was converted into a persistent Architecture Identity.
The source-based comparison retains all 20,943
existing structured property instances; the final model has
23,706. Elements with Documentation increase from
951 to 1085;
the same 214 unbound reference elements have no invented definition.

## Markdown synchronisation and discrepancies

The canonical baseline now contains full definitions and qualifications for
200 business-capabilities, 221 business-enabling-capabilities, 28 enterprise-capabilities, 178 features, 160 functions, 20 processes, 139 services.
Every record has an exact source key, snapshot reference, semantic ID, status,
currentness, placement, alias/unresolved metadata and a textual anchor. The
relationship catalogue accounts for all 1,096 assertions independently of headings
and folders. Motivation correspondence links maintained definitions and records
historical sources without asserting new present-day authority.

All **705 earlier candidate-capture keys** have exact continuity into the 946-record
baseline. **20 display-name differences**
were synchronised from explicit reviewed records, preserving their independent
standing and history. The reviewed BEC hierarchy and Proposed EC/BC placements are
represented as qualified assertions. No lexical crosswalk or automatic merge was used.
The old candidate capture remains byte-identical, with its original 2,467 unresolved
dimensions/relationships. Its historical uncertainty count is not substituted for
the reconciled baseline's independently recorded state.

BC/BEC catalogue entry notices and three specifically scoped source-era BEC assertion
wrappers direct readers to the reconciled records. Original wording is unchanged.
Earlier contextual views and unaffected definitions/boundaries remain useful.
Strategy/Business/reference-model navigation now leads to the current baseline.
No Archi navigation folder was reproduced as a semantic Markdown decomposition.

[discrepancies.json](../../../markdown/governance/baseline/discrepancies.json) retains
**69 explicit human-adjudication notes**, all
**17 definition gaps**, unresolved dimensions, historical
crosswalk limits and other review items. Preserved topics include technical boundary
communication versus patient communication; internal task progression versus an
external execution bridge; physical-access credentials versus clinical qualification;
subject assurance versus assurance-service review; specimen/materiel scope; general
security Feature framing; readiness determination versus dependency tracking; CM-R05
Exchange naming history; client consent/FEAT-EM-09 history; F46; and 214 unbound old
publication objects. A qualified display rename does not close an adjudication item.

No materially conflicting definition was selected through inference. Apparent
historical duplicates without proven correspondence were left as source evidence
and explicit unresolved mappings. Inclusion and machine validity do not resolve them.

## Documentation convention

All 946 recovered record Documentation fields now start with their source-backed
definition or an explicit definition-gap statement. Existing full EC definitions
remain first, including their qualifications. Verbatim explanatory material follows
in current Documentation where exact reconciled evidence establishes applicability.
Historical provenance does not imply historical meaning. Markdown emphasis labels
Intent, Boundary and equivalent sections. Genuinely historical or uncertain
reconciliation material follows `---` and `## History`.
Known TeX presentation macros are decoded deterministically; definitions are not
paraphrased. Metadata, raw provenance and source-record JSON remain in Properties,
not a historical-prose dump. Historical Motivation definitions are likewise
readable first, with lifecycle/currentness visible in Properties and View context.
The 214 old unbound objects lacked source-backed definitions before this task;
none was invented. Their reference status is now explicit in Properties.
There are 112 element Documentation fields with
the explicit current/history separator. Source-relative EC Markdown hyperlinks
were rebased for their new textual location without changing definition wording.

## Resource and View reorganisation

Archi's native resource categories are addressed by unnumbered `Motivation`,
`Strategy`, `Business` and `Application` Exchange category markers. Strategy uses
human-readable `Business Layer` and `Application Layer` groups with unnumbered
capability taxonomies. Business uses `Business Capabilities` and `Business Enabling
Capabilities` context trees, through the finest governed level including Feature.
Application's independent contexts use readable unnumbered labels. Relations
mirror their source's unnumbered actual home. No redundant global layer ordering
is reproduced beneath native resource roots.

Classified folders expose `[CT1]`, `[CT2]`, `[CT3]` and `[Feature]`; semantic Names
and types are unchanged. Capability/Feature elements exist once in Strategy.
Business resource and View folders provide the same architectural context without
creating semantic copies, retyping Features or adding relationships. Feature is
the first-class next level under CT3 wherever governed; the former intermediate
`Features (framing only)` folder is removed. Branches stop naturally. Existing
Business Function/Service/Process homes and diagram scopes are preserved.

Former View roots `Capabilities`, `Business`, `Traceability`, `Motivation` and
`Recovery Evidence` now sit under the ordered **0-Motivation**, **1-Strategy**,
**2-BusinessLayer**, **3-ApplicationLayer**, **4-TechnologyLayer**, **5-Implementation**.
All **984 Views moved** and **28 Views added**; none was
retired or replaced. Strategy retains the landscape, category overviews, progressive
context/immediate-child Views, Feature overviews and every leaf/anchor. Business
behaviour and Feature realisations now follow the appropriate BC/BEC context.
The new Motivation subjects sit directly at 0-Motivation; co-governance and multiple
historical/source-evidence Views have justified subfolders. Four label-only
Application coverage Views explain unallocated projections. Technology and
Implementation remain empty ordered top-level locations. See validation.json for
the complete added-View IDs and 3,460 checked folders.

| Resulting View domain | Views |
| --- | ---: |
| 0-Motivation | 25 |
| 1-Strategy | 769 |
| 2-BusinessLayer | 214 |
| 3-ApplicationLayer | 4 |
| 4-TechnologyLayer | 0 |
| 5-Implementation | 0 |

The old **Unreconciled 1.0.4 Reference** remains under Strategy and
`1-Strategy` Views. All 219 original Views, 228 original semantic elements (including
the 14 reused EC objects) and 189 original relationships survive. Source-based
relationship placement follows the actual retained source; no target duplicate is
created. The 214 unbound comparison elements never override the reconciled baseline.

## Motivation retained and added

The current Motivation projection contains the 18 maintained axioms, seven Drivers,
five Assessments, six Goals, three Outcomes, five foundational Requirements, three
Constraints and six Stakeholders. Five existing axiom IDs are reused. The historical
typed diagram contributes 14 source-qualified constructs and 11 explicit historical
relationships; five further explicitly typed Principle boxes and 67 explicitly
identified legacy Requirements retain their historical standing. In total,
**134 semantic representation elements** and
**29 relationships** were added;
zero persistent identities were adopted or allocated. Current native relationships
use exact governing-axiom/Requirement clauses and explicit Driver-to-Goal assertions.
No negative or positive influence strength is invented.

| Motivation type / currentness / standing (including five reused axioms) | Records |
| --- | ---: |
| Assessment / current / Agreed | 5 |
| Constraint / current / Accepted | 3 |
| Driver / current / Agreed | 7 |
| Driver / historical_reference / Historical — current applicability unresolved | 3 |
| Goal / current / Agreed | 6 |
| Goal / historical_reference / Historical — current applicability unresolved | 3 |
| Outcome / current / Agreed | 3 |
| Principle / current / Canonical | 18 |
| Principle / historical_reference / Historical / Reclassified | 3 |
| Principle / historical_reference / Historical / Reclassified — Domain06 | 1 |
| Principle / historical_reference / Historical — current applicability unresolved | 2 |
| Principle / historical_reference / Retired / Superseded | 1 |
| Requirement / current / APPROVED | 1 |
| Requirement / current / Accepted | 4 |
| Requirement / historical_reference / Historical / Reclassified — Domain04/06 | 1 |
| Requirement / historical_reference / Historical — current applicability unresolved | 1 |
| Requirement / historical_reference / Historical — current owning-domain standing not inferred | 67 |
| Stakeholder / current / Agreed | 6 |
| Stakeholder / historical_reference / Historical — current applicability unresolved | 4 |

Write-Behind remains explicitly Retired / Superseded; reclassified transport,
schema and logging principles retain their historical/reclassified state.
Legacy Goal timing, EMPI, schema and technology claims are historical evidence,
not current SLAs or authority. The complete original diagram, both legacy chapter
variants and requirements appendix remain inspectable in source-evidence Views.
Overlapping chapter Driver/Goal/Principle definitions and five edges touching
unbound historical Capability projections were not assigned new native identities
or merged by name. Their source assertions remain available for Mark's review.

## Deliberate exclusions and result semantics

No new Business/Application/Technology derivation, speculative Application
Capability, missing traceability, topology, identity adoption, naming adjudication,
historical issue resolution or conversation archaeology was undertaken. Domain04/05
RADS contains architecture even where this workbench lacks allocated native objects.
Four coverage Views and the canonical coverage page make that boundary explicit.
INVALID means a broken source/identity/reference/format contract. UNRESOLVED BY
ARCHITECTURAL DECISION and NOT YET REPRESENTED / FUTURE ARCHITECTURE EXPANSION remain
distinct, permitted and visible states.

## Validation and reproducibility

| Check | Result | Evidence | Log |
| --- | --- | --- | --- |
| publication | PASS | 95 publication tests PASS, including 24 baseline continuity/rejection tests, capability ancestry, four-projection Feature reachability, numbering separation and Documentation publication | [verification/projection/publication.log](verification/projection/publication.log) |
| scripts | PASS | 68 repository script/unit tests PASS | [verification/projection/scripts.log](verification/projection/scripts.log) |
| architecture | PASS | 90 architecture tests PASS, zero failures/errors/skips; Java 21 offline Maven reactor BUILD SUCCESS | [verification/projection/architecture.log](verification/projection/architecture.log) |
| rad | PASS | RAD structure, source-presentation overlays, exact source locators and retained unresolved findings PASS | [verification/projection/rad.log](verification/projection/rad.log) |
| identity | PASS | Architecture identity registry validation PASS; adopted bindings and unresolved identities retained | [verification/projection/identity.log](verification/projection/identity.log) |
| bindings | PASS | Representation crosswalk/binding validation PASS | [verification/projection/bindings.log](verification/projection/bindings.log) |
| release | PASS | Release governance check PASS; 1.0.6 unchanged | [verification/projection/release.log](verification/projection/release.log) |
| recovery_replay | PASS | Immutable 1.0.5A replay PASS, byte-identical frozen model and recovery outputs | [verification/projection/recovery_replay.log](verification/projection/recovery_replay.log) |
| snapshot_tests | PASS | 17 immutable source snapshot tests PASS | [verification/projection/snapshot_tests.log](verification/projection/snapshot_tests.log) |
| snapshot | PASS | Read-only frozen snapshot capability/ancestry, identity, aliases, reviewed reconciliation and source navigation contracts PASS | [verification/projection/snapshot.log](verification/projection/snapshot.log) |
| generation_xsd | PASS | Baseline generated twice identically; bundled Open Exchange XSD, ancestry, Feature reachability, navigation, Documentation and semantic preservation gates PASS | [verification/projection/generation_xsd.log](verification/projection/generation_xsd.log) |
| independent_regeneration | PASS | Independent subprocess reproduces all 12 model/validation/Markdown baseline outputs | [verification/projection/independent_regeneration.log](verification/projection/independent_regeneration.log) |
| regeneration_comparison | PASS | All 12 generated model/validation/Markdown files independently byte-identical | [verification/projection/regeneration_comparison.log](verification/projection/regeneration_comparison.log) |
| structural_audit | PASS | Exact-key BC/BEC reachability in four projections; all starting semantics, relationships, Views, Documentation, Properties, classification, lifecycle, namespace/schemaLocation and release preserved. All five installed Archi 5.10.0 XSDs byte-identical to bundled schemas | [verification/projection/structural_audit.log](verification/projection/structural_audit.log) |
| generation_check | PASS | Read-only baseline regeneration check PASS | [verification/projection/generation_check.log](verification/projection/generation_check.log) |
| preservation | PASS | Earlier Documentation correction proof links to exact starting candidate; semantic/Documentation/relationship/View/property/identity/recovery/release preservation PASS | [verification/projection/preservation.log](verification/projection/preservation.log) |
| report | PASS | Completion report/inspection guide read-only reproduction PASS; current evidence fingerprints and protected content revalidated | [verification/projection/report-check.log](verification/projection/report-check.log) |
| independent_report | PASS | Independent subprocess reproduction of completion report and inspection guide PASS | [verification/projection/report-independent.log](verification/projection/report-independent.log) |
| all_output_comparison | PASS | All 14 generated baseline outputs independently byte-identical, including model, validation, Markdown, report and guide | [verification/projection/all-output-comparison.log](verification/projection/all-output-comparison.log) |
| diff | PASS | Tracked working-tree whitespace/diff check PASS | [verification/projection/diff.log](verification/projection/diff.log) |

Commands were bounded using command-level timeouts; logs record final outcomes.
Independent generation reproduces all 12 model, validation and Markdown outputs byte-for-byte. Report and inspection guide are independently reproduced and checked separately; final comparison covers all 14 generator outputs.
Bundled offline Archi 5.9 Open Exchange Diagram/View/Model XSD validation: **PASS**.
Automated checks establish import structure and semantic/presentation integrity;
the agent has not exercised the Archi GUI. Mark's prior inspection accepted the
substance; visual acceptance of these corrections in Archi 5.10.0 remains pending.
The bundled Archi 5.9 schema remains valid; its Exchange namespace and schema files
are unchanged. No Open Exchange incompatibility with Archi 5.10.0 was demonstrated.
The optional installed-Archi import outcome is recorded separately below; XSD
validation does not establish GUI rendering or human acceptance.

The initial publication/script runs failed because a source-line locator refresh
changed the immutable 705-record capture fingerprint. That change was removed.
The capture is byte-identical to its starting state; exact source-presentation
insertions are now checked separately without semantic rewriting. The final suites
pass. No production code was changed to address a test-harness or fingerprint issue.
Two initial read-only snapshot diagnostic calls used incorrect helper API
assumptions (tuple unpacking and the include-reader module). The corrected
invocation passes; those invocation errors were not architecture failures.

The old recovery report's full live-checkout fingerprints remain historical.
They are not silently refreshed to claim current-source equality. The frozen
recovery model still reproduces byte-for-byte, with original SHA-256
`f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1`.

Commands executed:

- `timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v`
- `timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s scripts/tests -v`
- `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 timeout --signal=TERM --kill-after=5s 180s mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/rads_business_architecture.py validate`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/architecture_identity.py validate`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/architecture_identity.py bindings --check`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/architecture_release.py check`
- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/recover_v02.py --check`
- `timeout --signal=TERM --kill-after=5s 60s python3 -m unittest discover -s docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools -p 'test_*.py' -v`
- `timeout --signal=TERM --kill-after=5s 60s python3 /tmp/harmonia-106-projection-snapshot-check.py`
- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py --output-directory /tmp/harmonia-106-projection-reproduction`
- `timeout --signal=TERM --kill-after=5s 30s python3 /tmp/harmonia-106-projection-compare.py`
- `timeout --signal=TERM --kill-after=5s 90s python3 /tmp/harmonia-106-projection-structural-audit.py`
- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py --check`
- `report_baseline.validate_correction_preservation()`
- `timeout --signal=TERM --kill-after=5s 30s python3 docs/archimate/capability-architecture/tools/report_baseline.py --check`
- `timeout --signal=TERM --kill-after=5s 30s python3 docs/archimate/capability-architecture/tools/report_baseline.py --output-directory /tmp/harmonia-106-projection-reproduction`
- `timeout --signal=TERM --kill-after=5s 30s python3 /tmp/harmonia-106-projection-compare-all.py`
- `git diff --check`

## Canonical Documentation Assessment

Relevant architecture was found outside RADS in the reconciled v0.2 structured
source/TeX and the legacy Motivation diagram, chapters and requirements appendix.
The explicitly authorised task incorporated the recovered Strategy/Business
definitions, decomposition, statuses, identity history and unresolved assertions
into the canonical baseline, and recorded current/historical Motivation
correspondence. It preserved the frozen sources unchanged. Historical narrative
is retained as evidence; unsupported crosswalks and extra domain publication are
deferred. No domain is declared complete. Historical source protections preclude
rewriting the frozen recovery corpus merely to agree with current presentation.

## Earlier Documentation/order correction — historical record

The following records the preceding successful correction. Its resource numbering
is superseded by the final bounded projection correction below. Documentation
classification and semantic/visual preservation remain in force.

These corrections begin from the uncommitted synchronised baseline, not a recovery
restart. AX-04, AX-14 and AX-17 govern this presentation-only work. The exact source
capture, relationships, identity register, crosswalks, lifecycle states, deferred
assertions and immutable recovery remain unchanged. No architecture is rewritten,
expanded or adjudicated. No additional release increment or commit occurred.

Numeric prefixes now cover applicable resource roots and mirrored Relations paths,
as well as Views. Immediate sibling categories show Business before Business
Enabling; Strategy shows Business before Application. Independent Application
projections use the established Information / EC / Application / Middleware order.
Capability descendants are not recursively numbered. The current baseline has
BC/BEC category peers; no artificial Business Enabling Layer was introduced.
Numbering changes displayed folder labels only, retaining every folder ID and
semantic/visual identity key.

The [classification manifest](../../../markdown/governance/baseline/documentation-classification.json)
records exact capture keys, field hashes, dispositions and evidence. Existing
explanation is current when the unreplaced current record retains its established
meaning, when text is exactly reused as a reconciled current definition/overview,
or when a Feature's unchanged semantics have explicit responsibility-preserving
relocation evidence. No name-based matching or fresh historical recovery is used.

**396 Documentation entries**
have **675 fields**
reclassified from History into current explanation. Exact duplicate text already
present in the current section is shown once. All wording remains verbatim.
**212 fields in
98 entries** remain
historical: 141 lack proof after semantic reconciliation, 36 have unresolved scope
or definition gaps, and 35 belong to non-current records. Their complete exact-key
list and reasons are in `validation.json / documentation_classification /
retained_historical_material`. Examples include Person Identity Administration's
pre-review explanation, Client Administration's predecessor Intent/Boundary,
Health Service Operational Readiness and pending Health Information Communication.
This is deliberate AX-17 preservation, not a request to resolve these items here.

The [README lifecycle](../README.md#authority-and-collaboration-lifecycle) and
canonical baseline/navigation guidance now state: Markdown RAD authors and governs;
ArchiMate visualises, navigates, supports review and proposes change; LaTeX RAD
renders and publishes. Archi export is change input. Accepted presentation state
may remain subordinate governed projection metadata. Publication is strictly
`Markdown RAD -> LaTeX RAD -> publication output`, without a normal reverse-authoring
path. Historical LaTeX recovery remains bounded migration only.

The correction preservation gate compares against the captured starting candidate:
**1,299 semantic IDs, 852 relationships, 1,012 Views, every folder/reference ID,
all Properties and all non-Documentation semantic content are unchanged**.
Every View, node, connection, layout coordinate and bendpoint is unchanged.
The 946-record architecture capture and Motivation capture, adopted identities,
representation crosswalks, immutable recovery and release register are byte-identical.
The recovery still reproduces the original 1.0.5A XML hash. The release remains
**1.0.6**, with **1.0.5** as its preceding release.

The preceding correction reran its checks; final-pass results are listed above. Independent generation and
read-only checking reproduce every generated baseline output byte-for-byte.
Definition-only, definition plus current Overview, Intent, Boundary, genuine
History and unresolved historical examples are covered by rejection/continuity
tests and the updated [Archi guide](archi-inspection-guide.md). The remaining
human action is final navigation and Documentation rendering inspection in Archi
5.10.0; the earlier adjudication backlog remains outside this correction.

Files changed specifically by the correction:

- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/corrections-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-report.md`
- `docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/baseline-tests.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/documentation-samples.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation_check.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/preservation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/readme-lifecycle.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/report.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot_tests.log`
- `docs/archimate/capability-architecture/tests/test_baseline.py`
- `docs/archimate/capability-architecture/tools/report_baseline.py`
- `docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `docs/markdown/governance/archi-navigation-and-view-convention.md`
- `docs/markdown/governance/baseline/README.md`
- `docs/markdown/governance/baseline/business-capabilities.md`
- `docs/markdown/governance/baseline/business-enabling-capabilities.md`
- `docs/markdown/governance/baseline/correspondence.json`
- `docs/markdown/governance/baseline/documentation-classification.json`
- `docs/markdown/governance/baseline/enterprise-capabilities.md`
- `docs/markdown/governance/baseline/features.md`
- `docs/markdown/governance/baseline/functions.md`
- `docs/markdown/governance/baseline/processes.md`
- `docs/markdown/governance/baseline/services.md`

## Final bounded projection/navigation correction — same 1.0.6 candidate

This pass starts at the current uncommitted corrected candidate, model hash
`6152ac25113c24c4d95eb892b53c8fbcfb212ff5172630425fba789035a938a6`.
It is a navigation-only completion, with no new recovery, semantic architecture,
identity allocation, adjudication or release increment. AX-04 separates semantic
types from navigation, AX-14 preserves classifications/identity roles, and AX-17
keeps uncertainty explicit. Exact governed capture keys establish ancestry; names
and implementation structure supply none.

The generator separates unnumbered native resource categories from the retained
View sequence `0-Motivation`, `1-Strategy`, `2-BusinessLayer`, `3-ApplicationLayer`,
`4-TechnologyLayer`, `5-Implementation`. No numeric resource prefixes are retained;
there is no demonstrated ordering need among their siblings. Existing numbered
View category peers also remain. Root labels match Archi's native categories,
so `Strategy / 1-Strategy`, `Motivation / 0-Motivation` and
`Business / 2-BusinessLayer` are no longer generated.

| Current governed population | CT1 | CT2 | CT3 | Features | Established CT3 ancestry | Complete ancestry reachable in every applicable projection | Unresolved Feature ancestry |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| Business Capabilities | 18 | 40 | 142 | 0 | 0 | 0 | 0 |
| Business Enabling Capabilities | 7 | 76 | 95 | 157 | 157 | 157 | 0 |

The BC/BEC totals are **25 CT1, 116 CT2 and 237 CT3**. The independently retained
Enterprise catalogue adds **3 CT1, 15 CT2 and 10 CT3**, giving **28 / 131 / 247**
current Capability tier projections across all three catalogues. Non-current
source and historical reference populations remain separately qualified.
All **157 current governed Features** have complete recorded CT1 -> CT2 -> CT3
ancestry in this candidate. Their Proposed placement remains Proposed; established
here means recorded candidate ancestry, not newly approved architecture. Strategy
resources, Business resources, Strategy Views and Business Views each expose
that ancestry through a classified Feature folder. Independent BC and BEC coverage
gates validate each taxonomy. BC contains **zero** governed Features: the current
metamodel expressly frames Harmonia Features in BEC. A BC Feature-bearing inspection
example is unavailable without inventing architecture; none was manufactured.
There are **zero unresolved current Feature ancestries** in this reconciled
candidate. Earlier source-era uncertainty, pending and superseded records remain
visible; the generator and rejection tests retain an explicit `Unresolved Feature
Ancestry` path for incomplete current cases rather than guessing CT3.

The only retained leaf scaffolding is one classified context folder around the
existing semantic reference. Exchange navigation folders carry classification
without changing semantic Name. Its child reference therefore displays the same
unprefixed governed Name. Business/Views may contain empty classified context
folders where no associated Business element or diagram exists. No extra
`Features (framing only)` level remains. This presentation choice preserves the
single canonical semantic home and every existing View; the final human check must
confirm folder visibility and usability in Archi 5.10.0. Navigation creates no
semantic relationship and never retypes Feature as a Business Function or Service.

The new [starting-candidate proof](projection-start-preservation.json) and
preservation validator establish exact equality for **1,299 semantic elements
including Names, types, Documentation and Properties; 852 relationships; 1,012
Views including node/connection IDs and geometry; and 55 property definitions**.
All adopted persistent identities, representation crosswalks, lifecycle standing,
deferred assertions, historical reference population, source captures, schemas,
release state and every immutable recovery file retain their protected hashes.
Folder/reference navigation may change; semantic and existing View IDs do not.
Navigation folders change from 2,833
to 3,460:
2,681 folder IDs retained,
152 obsolete navigation folders removed,
779 context folders added. These are presentation changes only.
The earlier [Documentation correction proof](corrections-preservation.json) remains
unchanged and is linked to this pass's starting model hash. Documentation remains
**396 entries / 675 current explanatory fields; 212 historical fields across 98
entries**, verbatim. The classification manifest and authority/publication lifecycle
remain intact. The release register is byte-identical and remains **1.0.6**.

Archi **5.10.0** is the supported human architecture workbench. Current README,
navigation convention, generator validation descriptions, report and inspection
guide state that version; historical 5.9 provenance/reports remain accurate and
unchanged. The existing Exchange namespace/schemaLocation and bundled schema are
retained. All five schemas supplied by the installed Archi 5.10.0 Exchange plugin are byte-identical to the repository schemas. The optional native CLI import is UNRESOLVED: GTK could not initialise the available display (exit 13, before import), including the bounded retry using the discovered local X socket. This is a display/environment limitation, not demonstrated Exchange incompatibility. See verification/projection/archi-cli-no-display.log and archi-cli-display.log. No native import or GUI acceptance is claimed.
Automated checks establish structure, source correspondence and preservation;
Mark performs final import, folder/rendering and human acceptance in Archi 5.10.0.

Independent generation reproduces all 12 model, validation and Markdown outputs byte-for-byte. Report and inspection guide are independently reproduced and checked separately; final comparison covers all 14 generator outputs.
All final verification outcomes and exact commands are in the validation table and
`verification.json`; optional native application import diagnostics are recorded
without representing an environment limitation as a semantic test failure.
The immutable 1.0.5A recovery replay passes with original hash
`f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1`.

Files changed specifically by this final pass:

- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `docs/archimate/capability-architecture/tools/report_baseline.py`
- `docs/archimate/capability-architecture/tests/test_baseline.py`
- `docs/markdown/governance/archi-navigation-and-view-convention.md`
- `docs/markdown/governance/baseline/correspondence.json`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/projection-start-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/documentation-correction-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/ (final verification logs)`

Remaining human review: use every representative point in the
[Archi 5.10.0 inspection guide](archi-inspection-guide.md), including the natural BC
stopping points, BEC Feature branch, four projections, classification, retained View
ordering, unnumbered native resources, Documentation and visible uncertainty.
No commit has been made.

## Files changed in the complete candidate

- `architecture-release.json`
- `docs/archimate/capability-architecture/Makefile`
- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/recovery-v0.2/architecture-release.json`
- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/corrections-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/documentation-correction-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-report.md`
- `docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/projection-start-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/.gitignore`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/baseline_contract.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/baseline-tests.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/documentation-samples.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation_check.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/preservation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/readme-lifecycle.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/report.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot_tests.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/generation_xsd.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/ (final verification logs)`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot_tests.log`
- `docs/archimate/capability-architecture/tests/test_baseline.py`
- `docs/archimate/capability-architecture/tools/recover_v02.py`
- `docs/archimate/capability-architecture/tools/report_baseline.py`
- `docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `docs/markdown/02-strategy/capabilities/business-capabilities.md`
- `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`
- `docs/markdown/02-strategy/capabilities/index.md`
- `docs/markdown/03-business-architecture/README.md`
- `docs/markdown/03-business-architecture/reference-model/README.md`
- `docs/markdown/governance/archi-navigation-and-view-convention.md`
- `docs/markdown/governance/architecture-identity/bindings-validation.json`
- `docs/markdown/governance/baseline/README.md`
- `docs/markdown/governance/baseline/architecture.json`
- `docs/markdown/governance/baseline/business-capabilities.md`
- `docs/markdown/governance/baseline/business-enabling-capabilities.md`
- `docs/markdown/governance/baseline/correspondence.json`
- `docs/markdown/governance/baseline/coverage.md`
- `docs/markdown/governance/baseline/discrepancies.json`
- `docs/markdown/governance/baseline/documentation-classification.json`
- `docs/markdown/governance/baseline/enterprise-capabilities.md`
- `docs/markdown/governance/baseline/features.md`
- `docs/markdown/governance/baseline/functions.md`
- `docs/markdown/governance/baseline/motivation.json`
- `docs/markdown/governance/baseline/motivation.md`
- `docs/markdown/governance/baseline/processes.md`
- `docs/markdown/governance/baseline/relationships.md`
- `docs/markdown/governance/baseline/services.md`
- `docs/markdown/governance/baseline/source-presentation-overlays.json`
- `scripts/rads_business_architecture.py`
- `scripts/tests/test_rads_source_presentation.py`

## Files generated

- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/corrections-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/documentation-correction-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-report.md`
- `docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/projection-start-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/.gitignore`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/baseline_contract.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/baseline-tests.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/documentation-samples.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation_check.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/preservation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/readme-lifecycle.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/report.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot_tests.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/generation_xsd.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/all-output-comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/archi-cli-display.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/archi-cli-no-display.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/generation_check.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/generation_xsd.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/preservation.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/report-check.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/report-independent.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/snapshot_tests.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/projection/structural_audit.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot_tests.log`
- `docs/markdown/governance/architecture-identity/bindings-validation.json`
- `docs/markdown/governance/baseline/README.md`
- `docs/markdown/governance/baseline/business-capabilities.md`
- `docs/markdown/governance/baseline/business-enabling-capabilities.md`
- `docs/markdown/governance/baseline/discrepancies.json`
- `docs/markdown/governance/baseline/documentation-classification.json`
- `docs/markdown/governance/baseline/enterprise-capabilities.md`
- `docs/markdown/governance/baseline/features.md`
- `docs/markdown/governance/baseline/functions.md`
- `docs/markdown/governance/baseline/motivation.md`
- `docs/markdown/governance/baseline/processes.md`
- `docs/markdown/governance/baseline/relationships.md`
- `docs/markdown/governance/baseline/services.md`

## Mark's review before acceptance

1. Import the generated model in Archi 5.10.0 and inspect the guide's current/history,
   Candidate, pending, progressive/anchor and historical-reference examples.
2. Review the 69 pending records, explicit adjudication notes, 17 definition gaps,
   214 unproven old crosswalks and historical Motivation equivalence limits. This
   task preserves them and requests no architectural decision by implication.
3. Confirm that definition / current explanation / History / Properties are useful,
   that mirrored context folders are clearly navigation, and that coverage gaps
   do not imply absent RADS architecture.
4. Accept the 1.0.6 change set only after review; subsequent architecture expansion
   remains a separate task. No commit has been made.

Model SHA-256: `8dc2a377f080c7083811c0849bce66166b740f8c8321bb009eb3a8da3d78eb0b`.
