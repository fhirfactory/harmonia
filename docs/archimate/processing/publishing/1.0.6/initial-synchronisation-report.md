<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate / Markdown baseline synchronisation — 2026-10-17

Ready for Mark's architectural inspection. **No commit or GUI acceptance is claimed.**
Import [Harmonia-Architecture-Baseline.xml](Harmonia-Architecture-Baseline.xml) in Archi 5.9.
Use the [inspection guide](archi-inspection-guide.md) and
[canonical Markdown baseline](../../../markdown/governance/baseline/README.md).

## Starting state, authority and release

Branch `Harmonia-GovernedAccess`, starting commit `227895b66bc203446581a0483097bff1603e9966`;
clean working tree at task start. Repository Architecture Release was **1.0.5**,
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
remain first, including their qualifications. Predecessor explanatory narrative
follows two newlines, 60 hyphens, two newlines and `Historical narrative`.
Known TeX presentation macros are decoded deterministically; definitions are not
paraphrased. Metadata, raw provenance and source-record JSON remain in Properties,
not a historical-prose dump. Historical Motivation definitions are likewise
readable first, with lifecycle/currentness visible in Properties and View context.
The 214 old unbound objects lacked source-backed definitions before this task;
none was invented. Their reference status is now explicit in Properties.
There are 466 element Documentation fields with
the explicit current/history separator. Source-relative EC Markdown hyperlinks
were rebased for their new textual location without changing definition wording.

## Resource and View reorganisation

Strategy now separates `Business Layer / Business Capabilities`, `Business Layer /
Business Enabling Capabilities` and `Application Layer / Enterprise Capabilities`.
`Application Capabilities` remains empty. Existing deeper decomposition, pending,
superseded and Feature-framing branches remain available. Capability duplication
removed: **zero**; none existed solely for navigation, and none was introduced.

`BusinessLayer / BC-Grouped-Elements` and `BEC-Grouped-Elements` mirror Capability
contexts as folders; existing Business Functions/Services/Processes have their exact
responsibility homes there. Capability elements themselves remain only in Strategy.
`ApplicationLayer` contains distinct `InformationMap`, `EC-Grouped-Elements`,
`ApplicationMap` and `MiddlewareMap` contexts. EC folders mirror established
qualified EC hierarchy without inferred allocation. No software/information object
was forced into a common decomposition. Relations follow their source's actual
resource home. Source identity/projection roles stabilise semantic context folder
keys; folder/reference IDs are reprojected presentation identities, not semantic IDs.

Former View roots `Capabilities`, `Business`, `Traceability`, `Motivation` and
`Recovery Evidence` now sit under the ordered **0-Motivation**, **1-Strategy**,
**2-BusinessLayer**, **3-ApplicationLayer**, **4-TechnologyLayer**, **5-Implementation**.
All **984 Views moved** and **28 Views added**; none was
retired or replaced. Strategy retains the landscape, category overviews, progressive
context/immediate-child Views, Feature framing and every leaf/anchor. Business
behaviour and Feature realisations now follow the appropriate BC/BEC context.
The new Motivation subjects sit directly at 0-Motivation; co-governance and multiple
historical/source-evidence Views have justified subfolders. Four label-only
Application coverage Views explain unallocated projections. Technology and
Implementation remain empty ordered top-level locations. See validation.json for
the complete added-View IDs and 2,833 checked folders.

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
| publication | PASS | 86 publication tests; zero failures/errors; 47.616 s | [verification/publication.log](verification/publication.log) |
| baseline_contract | PASS | 15 focused continuity/rejection tests pass after final documentation/link/source-boundary changes | [verification/baseline_contract.log](verification/baseline_contract.log) |
| scripts | PASS | 68 repository script tests; zero failures/errors; 29.858 s | [verification/scripts.log](verification/scripts.log) |
| architecture | PASS | 90 architecture tests; zero failures/errors/skips; Java 21 offline Maven; 28.995 s | [verification/architecture.log](verification/architecture.log) |
| snapshot_tests | PASS | 17 frozen reviewed-BEC tests; zero failures/errors; 1.176 s | [verification/snapshot_tests.log](verification/snapshot_tests.log) |
| snapshot | PASS | 946 frozen model records; structural-ID/direct-alias/identity/review/navigation checks pass | [verification/snapshot.log](verification/snapshot.log) |
| rad | PASS | INVALID=0; 2,467 source-era unresolved dimensions/relationships preserved | [verification/rad.log](verification/rad.log) |
| identity | PASS | 14 registered identities valid; unchanged registry; no allocations | [verification/identity.log](verification/identity.log) |
| bindings | PASS | 14 exact retained bindings; no semantic drift; generated binding verification current at 1.0.6 | [verification/bindings.log](verification/bindings.log) |
| release | PASS | 1.0.5 → 1.0.6; explicit single patch increment | [verification/release.log](verification/release.log) |
| recovery_replay | PASS | Pinned 1.0.5A recovery byte-identical with original SHA-256 and frozen release; separate from live baseline | [verification/recovery_replay.log](verification/recovery_replay.log) |
| generation_xsd | PASS | Current baseline generated twice identically; bundled offline Archi 5.9 XSD, navigation, endpoint, Properties and Markdown correspondence gates pass | [verification/generation_xsd.log](verification/generation_xsd.log) |
| independent_regeneration | PASS | Independent output-directory generation passes | [verification/independent_regeneration.log](verification/independent_regeneration.log) |
| regeneration_comparison | PASS | All 12 generated XML/validation/Markdown/ledger outputs byte-identical to independent reproduction | [verification/regeneration_comparison.log](verification/regeneration_comparison.log) |
| diff | PASS | No whitespace errors | [verification/diff.log](verification/diff.log) |

Commands were bounded using command-level timeouts; logs record final outcomes.
PASS: generation independently builds twice per invocation; a separate /tmp output tree reproduces all 12 XML, validation, Markdown and ledger files byte-for-byte. The live release is read and remains unchanged.
Bundled offline Archi 5.9 Open Exchange Diagram/View/Model XSD validation: **PASS**.
Automated checks establish import structure and semantic/presentation integrity;
the Archi GUI has not been exercised and human visual acceptance remains pending.

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

- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 90s python3 -m unittest discover -s docs/archimate/capability-architecture/tests -p test_baseline.py -v`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s scripts/tests -v`
- `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 timeout --signal=TERM --kill-after=10s 180s mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 60s python3 -m unittest discover -s docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools -p 'test_*.py' -v`
- `v0.2 check.validate_model(model); check.validate_review(model, elements); navigation.validate_source(elements, check.included_tex(master))`
- `PYTHONDONTWRITEBYTECODE=1 python3 scripts/rads_business_architecture.py validate`
- `PYTHONDONTWRITEBYTECODE=1 python3 scripts/architecture_identity.py validate`
- `PYTHONDONTWRITEBYTECODE=1 python3 scripts/architecture_identity.py bindings --check`
- `PYTHONDONTWRITEBYTECODE=1 python3 scripts/architecture_release.py check`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/recover_v02.py --check`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py --output-directory /tmp/harmonia-baseline-reproduction`
- `Compare every generated output against /tmp/harmonia-baseline-reproduction using read_bytes() equality`
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

## Files changed

- `architecture-release.json`
- `docs/archimate/capability-architecture/Makefile`
- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/recovery-v0.2/architecture-release.json`
- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/.gitignore`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/baseline_contract.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/generation_xsd.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/independent_regeneration.log`
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
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/.gitignore`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/architecture.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/baseline_contract.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/bindings.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/diff.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/generation_xsd.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/identity.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/independent_regeneration.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/publication.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/rad.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/recovery_replay.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/regeneration_comparison.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/release.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/scripts.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot.log`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot_tests.log`
- `docs/markdown/governance/architecture-identity/bindings-validation.json`
- `docs/markdown/governance/baseline/business-capabilities.md`
- `docs/markdown/governance/baseline/business-enabling-capabilities.md`
- `docs/markdown/governance/baseline/discrepancies.json`
- `docs/markdown/governance/baseline/enterprise-capabilities.md`
- `docs/markdown/governance/baseline/features.md`
- `docs/markdown/governance/baseline/functions.md`
- `docs/markdown/governance/baseline/motivation.md`
- `docs/markdown/governance/baseline/processes.md`
- `docs/markdown/governance/baseline/relationships.md`
- `docs/markdown/governance/baseline/services.md`

## Mark's review before acceptance

1. Import the generated model in Archi 5.9 and inspect the guide's current/history,
   Candidate, pending, progressive/anchor and historical-reference examples.
2. Review the 69 pending records, explicit adjudication notes, 17 definition gaps,
   214 unproven old crosswalks and historical Motivation equivalence limits. This
   task preserves them and requests no architectural decision by implication.
3. Confirm that current Definition / historical narrative / Properties are useful,
   that mirrored context folders are clearly navigation, and that coverage gaps
   do not imply absent RADS architecture.
4. Accept the 1.0.6 change set only after review; subsequent architecture expansion
   remains a separate task. No commit has been made.

Model SHA-256: `7b5f8b8e8a08b755b6d7800b6d6ae032e51f95676f2c272cc707c98374f33bd1`.
