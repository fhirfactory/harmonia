<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Migration boundary, source inventory and release basis | Not recorded | — |
| [DR-2](#dr-2) | Continuum classification and textual architecture destinations | Not recorded | — |
| [DR-3](#dr-3) | Mixed governed captures, projections and identity material | Not recorded | — |
| [DR-4](#dr-4) | Governance sources and specialised contracts | Not recorded | — |
| [DR-5](#dr-5) | Existing architectural review and decision records | Not recorded | — |
| [DR-6](#dr-6) | Candidate Information Systems Architecture and scratchpad material | Not recorded | — |
| [DR-7](#dr-7) | ArchiMate workspace, machinery and processing preservation | Not recorded | — |
| [DR-8](#dr-8) | LaTeX publication packages and their mixed supporting material | Not recorded | — |
| [DR-9](#dr-9) | Legacy, implementation and other documentation boundaries | Not recorded | — |
| [DR-10](#dr-10) | Reference continuity, application prerequisites and verification | Not recorded | — |
| [DR-11](#dr-11) | Bounded Minutiae for candidate preparation | Not recorded | — |

# HCRL — Bounded migration of existing Enterprise Architecture content

**Standing.** First pending iteration **001**, prepared by Codex on 2026-10-21
for the proposed **RAD-1.1.3 → RAD-1.1.4** transition. This is a migration
candidate for Architecture Review. No Solution Architect disposition on this
candidate, migration application or resulting-state confirmation is recorded.
All destinations below are proposals, subject to the qualifications in their DR.
An unresolved destination means retain the source in place until adjudicated;
it is neither a rejection of the content nor permission to invent a destination.

**Originating state.** Root [release authority][release-json] records
`version=1.1.3`, `previous_version=1.1.2`. Repository HEAD at inspection is
`41047e1dc5c7d1ca4769dc7479bb50cae60989a7`, branch
`ArchitecturalGovernance`; the working tree was clean. That commit supplies the
retrievable source boundary for this inventory. The user attached
[pending HCRL 1.1.3 iteration 005][supplied-reference]; its pre-confirmation
narrative is retained history. The [completed HCRL 1.1.3 iteration 005][baseline]
and maintained common contracts establish the accepted baseline, including the
Solution Architect's confirmation, “result confirmed. Complete!” The supplied
pending record is not used to reopen that completed transition.

**Governing basis.** [Architecture Governance][governance], particularly
“Repository structure and migration boundary” and “Governance and architectural
knowledge”, the [Review Praxis][praxis], [Architecture Change Provenance][provenance],
[root instructions][agents] §1.2 and the [release policy][release-policy] govern.
The target structure and migration limits were accepted in the
[reviewed RAD-1.1.2 HCRL][structure-review], DR-3/DR-5/DR-7, and confirmed in its
[completed transition][structure-complete]. This candidate proposes application
of that structure; it does not reconsider it or install a second taxonomy.
The [modelling strategy][modelling], [mapping profile][mapping],
[mapping catalogue][mapping-catalogue], [identity/naming contract][naming] and
[Round-Trip Praxis][roundtrip] constrain classification and continuity.

**Axiom consistency.** [AX-04][axioms] keeps architectural meaning independent
of tools and folders. AX-08/AX-09 distinguish architecture and selected evidence
from processing history; no processing cleanup or recursive evidence capture is
proposed. AX-14 preserves governance/architecture, conceptual/logical/physical,
canonical/representation identity, candidate/approved, review/execution and
release/publication distinctions. AX-15 preserves unknown application effects;
AX-17 requires adjudication of unestablished classifications and destinations.
AX-03 supports retaining native representations rather than converting content
for folder convenience. No runtime responsibility, domain authority, axiom,
element, specialisation, semantic relationship or canonical name is changed by
this proposal. Specific unresolved compliance questions are in DR-2–DR-10.

<a id="dr-1"></a>
## DR-1 — Migration boundary, source inventory and release basis

**Operative review-preparation instruction.** The user supplied the following
Goal, Expected Outcome, Context, Constraints, Authorised Activity / Scope and
Report, with the pending RAD-1.1.3 reference identified above:

> Goal
>
> Prepare the RAD-1.1.4 HCRL candidate for migration of existing Harmonia Enterprise Architecture content into the governed repository structure established by RAD-1.1.2/1.1.3.
>
> Expected Outcome
>
> A pending HCRL identifying the bounded source-to-destination migration mapping, including any content whose architectural standing or destination requires Solution Architect adjudication.
>
> Context
>
> RAD-1.1.3 is the current governed baseline. The target Enterprise Architecture repository structure and migration constraints are already governed. This change is intended to apply the previously deferred content migration.
>
> Constraints
>
> Do not migrate, rename, delete, rewrite or otherwise modify existing architecture content while preparing the candidate. Preserve existing architectural standing, canonical identity, correspondence and specialised semantics. Do not infer destinations merely for structural completeness or symmetry. Do not reinterpret content during migration analysis. Where destination or standing is uncertain, surface it for Architecture Review.
>
> Authorised Activity / Scope
>
> Inspect the current Enterprise Architecture/documentation repository content and the governed target structure. Derive the proposed source-to-destination mapping necessary for the migration. Identify content that is architecture knowledge, governance, design/review/register/evidence material, publication material, tooling/machinery, legacy/superseded material, or otherwise requires explicit treatment. Prepare the first pending HCRL for RAD-1.1.4. Do not apply the migration.
>
> Report
>
> Report the candidate HCRL path, the migration scope discovered, unresolved classification/mapping matters requiring Solution Architect adjudication, and any material issue that prevents a faithful bounded migration.

**Inventory boundary and notation.** Counts are tracked files from the exact
originating commit, before adding this candidate. A directory row means the
finite set returned by `git ls-tree -r --name-only` for that commit and prefix;
it does not include later additions, ignored build products, local exports or
untracked files. There were no untracked files at entry. No archive was expanded
to create a second inventory. Each source is assigned its more specific DR where
a broader subtree contains it. “Same path” is an explicit no-move proposal.
Destination paths are repository-relative. A preserved suffix retains its exact
spelling, release/run identity and internal directory structure. Counts describe
files, not architectural constructs, approvals or migration operations.

| Inspected source set | Files | Proposed treatment / applicable DR |
| --- | ---: | --- |
| `docs/markdown/01-motivation/` | 14 | Native-concept candidates and unclassified orientation, DR-2; one review, DR-5 |
| `docs/markdown/02-strategy/` | 19 | Native-concept candidates and mixed methodology, DR-2; three reviews, DR-5 |
| `docs/markdown/03-business-architecture/` | 19 | Native-concept/mixed architecture, DR-2; three reference-model files, DR-3 |
| `docs/markdown/04-information-architecture/` | 29 | Conceptual architecture and cross-concept rules, DR-2; four reviews, DR-5 |
| `docs/markdown/05-information-systems-architecture/` | 36 | Intact candidate/review unit, DR-6 |
| `docs/markdown/governance/` | 55 | Twenty-six baseline and eleven identity files, DR-3; eighteen other files, DR-4 |
| `docs/markdown/architecture-completion-plan.md` | 1 | Governance programme plan, DR-4 |
| `docs/markdown/scratchpad/` | 6 | Working notes and five candidate LaTeX files, DR-6 |
| **RADS subtotal** | **179** | No wholesale `docs/markdown → continuum` mapping |
| `docs/archimate/` | 1,830 | Specialised workspace mapping, DR-7 |
| `docs/latex-rad/` | 134 | Correlated publication packages, DR-8 |
| `docs/latex-legacy/`; `docs/libreoffice/` | 101; 21 | Legacy publication material, DR-9 |
| Other tracked `docs/` files | 153 | Instructions, implementation sequence, navigation and legacy technical content, DR-9 |
| **All `docs/`** | **2,418** | Exhaustive tracked path partition above; no assertion of semantic approval |
| Existing `enterprise-architecture/` | 21 | Same path: five governance files, eight pending HCRLs, two completed HCRLs, six execution records |
| `openspec/`; `.junie/` | 424; 411 | Separate specification and working/historical material, DR-9 |
| `scripts/` | 16 | Shared machinery and tracked cache files stay in place; consumers, DR-10 |

**Proposed transition boundary.** Migrate only exact approved mappings from this
source set after adjudication and a separate Apply instruction. Preserve all
explicitly excluded/deferred sources in place. Acceptance may authorise a bounded
subset; that must not be reported as completion of the entire corpus migration.
No empty native-concept folders, missing architecture domains, new master index
or inferred symmetric counterparts are proposed. Existing common governance,
HCRLs and RAD-1.1.3 Tasks/Reports are already correctly located and require no
migration or timestamp reconstruction.

**Release and evidence.** Preparation leaves `architecture-release.json`
unchanged. Pending iterations consume no release. An accepted application must
recheck 1.1.3 as the immediate governed predecessor and apply the single normal
patch increment to 1.1.4 with the accepted change set. Baseline drift requires
review of applicability, not merely a filename change. This is Architecture
Review provenance, retained here; no formal execution Task/Report is created
for this investigation. Actual future application requires its own formal
timestamped Task/Report records under `design/evidence/1.1.4/`, with selected
additional evidence and application facts in the applicable DRs. No future
execution Report, release, publication or completed HCRL is claimed to exist.

**Decision sought.** Confirm or narrow the inventory/application boundary and
the explicit no-move treatments. Resolve the dependent DRs before application;
this DR alone does not approve every proposed destination.

<a id="dr-2"></a>
## DR-2 — Continuum classification and textual architecture destinations

**Established rule.** The continuum follows native ArchiMate concepts and
accepted Harmonia specialisations. A document can contain current definitions,
proposals, rejected options, review decisions and unresolved assertions together.
The mapping profile expressly makes standing assertion-specific; its “Direct”
classification establishes type compatibility, not approval or completeness.
The numbered documentation domains and their subfolders are not an accepted
continuum taxonomy. No new native type, specialisation, Home, Composition,
ownership, hierarchy or identity follows from the destinations proposed here.

**Concrete native-concept candidates.** These exact paths use native concept
names as proposed directory spellings. They preserve whole source files and
their existing anchors; they do not split, paraphrase or reconstruct definitions.
The type basis is the cited mapping catalogue and source definitions, rather
than the filename. Accepted folder spelling and intact-file suitability remain
Solution Architect decisions. Embedded uncertainty/history remains unchanged.

| Source relative to `docs/markdown/` | Proposed exact destination relative to `enterprise-architecture/` | Classification basis / retained limits |
| --- | --- | --- |
| `01-motivation/drivers-assessments/drivers.md` | `continuum/Driver/drivers.md` | Seven Drivers |
| `01-motivation/drivers-assessments/assessments.md` | `continuum/Assessment/assessments.md` | Five Assessments |
| `01-motivation/goals-outcomes/strategic-goals.md` | `continuum/Goal/strategic-goals.md` | Six Goals |
| `01-motivation/goals-outcomes/business-outcomes.md` | `continuum/Outcome/business-outcomes.md` | Three Outcomes |
| `01-motivation/stakeholders/enterprise-stakeholders.md` | `continuum/Stakeholder/enterprise-stakeholders.md` | Established stakeholder groups |
| `01-motivation/requirements-constraints/foundational-requirements.md` | `continuum/Requirement/foundational-requirements.md` | REQ-FND-001–005; retain historical references and approved reconciliation qualifications |
| `01-motivation/requirements-constraints/external-constraints.md` | `continuum/Constraint/external-constraints.md` | Three generalised Constraints; jurisdictional examples remain examples |
| `02-strategy/capabilities/enterprise-capabilities.md` | `continuum/Capability/enterprise-capabilities.md` | EC-01–14; preserve qualified responsibilities and established IDs |
| `02-strategy/courses-of-action/strategic-courses-of-action.md` | `continuum/CourseOfAction/strategic-courses-of-action.md` | COA-01–06 |
| `02-strategy/resources/strategic-resources.md` | `continuum/Resource/strategic-resources.md` | SR-01–03; rejected/relegated resource candidates must remain distinguishable |
| `02-strategy/strategic-views/strategic-value-streams.md` | `continuum/ValueStream/strategic-value-streams.md` | VS-01–04; stage descriptions do not establish additional objects |
| `03-business-architecture/actors-roles/actors.md` | `continuum/BusinessActor/actors.md` | Actor definitions; seven categories are not seven automatically instantiated Actors |
| `03-business-architecture/actors-roles/roles.md` | `continuum/BusinessRole/roles.md` | Roles; families/job titles remain contextual classification |
| `03-business-architecture/collaborations-interactions/collaborations.md` | `continuum/BusinessCollaboration/collaborations.md` | Governed Collaborations, subject to source boundaries |
| `03-business-architecture/collaborations-interactions/interactions.md` | `continuum/BusinessInteraction/interactions.md` | Interactions retain distinct collaboration/participation semantics |
| `03-business-architecture/processes/business-processes.md` | `continuum/BusinessProcess/business-processes.md` | Principal Processes; no stage/function equivalence inferred |

**Mixed and unestablished placement.** The following bounded sets have no
executable destination yet. “Retain pending classification” preserves their
existing architectural standing; it does not demote established meaning to a
proposal. Candidate conceptual homes are explanatory options only where stated.

| Exact source / bounded set relative to `docs/markdown/` | Classification / proposed treatment requiring adjudication |
| --- | --- |
| `01-motivation/stakeholders/external-authorities.md` | External governing-body and operational facets must not be conflated. Mapping catalogue makes Stakeholder/BusinessActor conditional; retain pending exact classification. |
| `01-motivation/README.md`; `01-motivation/orientation-view.md` | Cross-concept orientation and motivation threads. No native semantic element follows from a documentation View. Decide intact navigation location without inventing a continuum “orientation” type. |
| `01-motivation/principles/architectural-axioms.md`; `01-motivation/principles/reclassified-principles.md` | Explicitly non-authoritative compatibility navigation. Preserve that standing and historical anchors; no duplicate Principle register. Coordinate with DR-4's single authoritative axiom source. |
| `01-motivation/requirements-constraints/master-requirements-catalogue.md` | Navigation plus downstream Candidate requirements and prospective Domain13 claims, already qualified by RAD-1.1.2. No promotion to an approved Requirement catalogue or EA master index. Decide its navigation/review treatment. |
| `02-strategy/capabilities/business-capabilities.md`; `02-strategy/capabilities/business-enabling-capabilities.md` | Capability, with Feature retaining its governed Capability specialisation; the sources also retain candidate concerns, Proposed hierarchy and unestablished ancestry. Candidate paths are `continuum/Capability/business-capabilities.md` and `continuum/Capability/business-enabling-capabilities.md`, conditional on adjudication of intact mixed-standing catalogue placement. No CT/FT inference or flattening. |
| `02-strategy/capabilities/ict-foundation-lenses.md`; `02-strategy/capabilities/index.md`; `02-strategy/capability-maps/capability-tier-model.md` | Lenses and derivation stages are not Capabilities. Mixed explanatory and modelling rules: determine governance/context treatment without converting them into native elements. |
| `02-strategy/capability-maps/health-service-assurance-derivation.md`; `02-strategy/strategic-views/logical-component-responsibilities.md` | Cross-concept architectural derivation/responsibility knowledge. Strategic logical responsibility is not sufficient evidence of ApplicationComponent identity. Decide intact placement or explicitly bounded section mapping; no allocation inferred. |
| `02-strategy/README.md`; `02-strategy/capability-maps/index.md`; `02-strategy/courses-of-action/index.md`; `02-strategy/resources/index.md`; `02-strategy/strategic-views/index.md` | Orientation/index and some metamodel guidance. Do not discard apparently duplicative indexes: some contain substantive rules. Decide navigation/governance treatment. |
| `03-business-architecture/behaviours/` — seven files | Capability-scoped Functions, Services, Processes, Features, allocations and assurance boundaries. The source explicitly rejects disconnected global catalogues. Neither wholesale placement under BusinessFunction nor automatic splitting by headings is faithful. Decide preservation of the scoped unit versus exact authorised section mappings. |
| `03-business-architecture/dependencies/cross-capability-dependencies.md`; `03-business-architecture/information-responsibility/information-responsibility.md` | Relationship/responsibility knowledge spanning native types. No generic “Dependency” or “InformationResponsibility” specialisation is established by this migration. Retain pending placement. |
| `03-business-architecture/metamodel/business-architecture-metamodel.md`; `03-business-architecture/README.md` | Mixed architectural semantics, native compatibility rules and domain orientation. Determine governance versus architecture/context placement; do not move merely because “metamodel” sounds like tooling. Preserve §8 identity and approved CT3/Feature semantics. |
| `04-information-architecture/information-families/` — twelve files | Eleven family descriptions plus README, including task/work and partially reviewed Package2 content. Conceptual meanings can map to BusinessObject where established, but a family/heading is not automatically one BusinessObject. The selected projection does not classify or approve every assertion in a whole family file. Decide intact-file placement/standing or exact section boundaries. |
| `04-information-architecture/metamodel/information-architecture-metamodel.md` | Rules plus the established InformationUnit concept, selected as BusinessObject in the governed projection. Whole-file governance relocation would misclassify that concept; whole-file BusinessObject placement also needs treatment of rules. Retain pending decision. |
| `04-information-architecture/governance/` — two files | Runtime information authority/custody/provenance and lifecycle semantics: architectural knowledge, not automatically architecture-review governance. Preserve AX-05/06/08/09 distinctions; determine native/multi-concept placement. |
| `04-information-architecture/guardrails/` — two files | Modelling rules and substantive observability/domain-meaning boundaries. Separate classification needs adjudication; the folder name supplies none. |
| `04-information-architecture/patterns/` — five files | Qualified containment, definition/accountability, information relationships, Metabolē and search results. Some selected concepts have BusinessObject representation; patterns and their cross-level realisations are not thereby one native type. Preserve conceptual/logical/physical independence. |
| `04-information-architecture/assemblies-views/assemblies-and-views.md`; `04-information-architecture/traceability/domain03-traceability.md`; `04-information-architecture/README.md` | Assemblies/information Views, traceability and completion/orientation context. An information View is not an ArchiMate presentation View. No artificial Grouping, View or native “Pattern” type; retain pending classification. |

The source partition above plus DR-5's eight reviews, DR-3's three Domain03
reference-model files and DR-6's Domain05 unit accounts for all 117 numbered-domain
files. No whole-domain move is proposed. Selected native-object correspondence
in DR-3 is evidence for bounded classification, not authority to retype every
neighbouring statement.

**Decisions sought.** Confirm/correct the sixteen concrete native-concept paths;
adjudicate mixed-standing intact catalogue placement under the common review/
register rule. For each retained set, establish an exact destination or explicit
deferral. If section extraction is required, approve exact source boundaries,
correspondence and standing first; this candidate supplies no rewrite authority.
Do not create invented “Pattern”, “InformationFamily”, “LogicalComponent” or
documentation-domain types to obtain an apparently complete continuum.

<a id="dr-3"></a>
## DR-3 — Mixed governed captures, projections and identity material

**Observed architecture, not folder-based authority.** The
[synchronised baseline README][textual-baseline] defines a governed structured
capture shared by textual and ArchiMate projections. It preserves independent
currentness, semantic standing, placement, history, aliases and unresolved
dimensions. `architecture.json` has 946 element records plus 214 historical
reference elements, 1,096 relationship assertions and resource-owned metadata.
The earlier Domain03 capture remains an explicitly Candidate representation.
Neither capture is a new identity register; prose and structured records have
the qualified correspondence described by the baseline contract.

The initial Information Architecture capture selects 54 resources: 50 native
BusinessObjects and four native DataObjects. Its evidence points to seven
Domain04 family files, the metamodel, Metabolē, search results, the G2 review and
three Domain05 documents. Selected G2 meanings and bounded Domain05 meanings
retain their distinct approval/candidate standing. This is a concrete reason
not to infer that a review folder is devoid of architecture or that the whole
capture belongs beneath one native concept. Subsequent accepted-ingestion
captures must remain correlated with their existing resources; no reallocation
or retyping is proposed.

| Exact source set | Proposed destination / decision needed |
| --- | --- |
| `docs/markdown/governance/baseline/` — all 26 files | Mixed architecture, structured source, projections, review findings and historical correspondence; no blanket move to governance or register. Decide whether to preserve the correlated structured-source unit in an expressly accepted continuum location, or authorise exact subdivision. No generic native “Baseline” type is proposed. Retain in place meanwhile. |
| Baseline `business-capabilities.md`, `business-enabling-capabilities.md`, `enterprise-capabilities.md`, `features.md`, `functions.md`, `services.md`, `processes.md`, `products.md` | Native-compatible destination candidates respectively: `enterprise-architecture/continuum/Capability/{business-capabilities,business-enabling-capabilities,enterprise-capabilities}.md`, `continuum/Capability/Feature/features.md`, `continuum/BusinessFunction/functions.md`, `continuum/BusinessService/services.md`, `continuum/BusinessProcess/processes.md`, `continuum/Product/products.md` (all continuum paths beneath `enterprise-architecture/`). These are conditional on intact mixed-standing projection treatment and coordination with DR-2; they are not independent architecture instances. |
| Baseline `architecture.json`, `accepted-ingestion-1.0.8.json`, `accepted-ingestion-1.0.10.json`, `information-architecture.json`, `motivation.json` | Resource-bearing/current-and-historical multi-type sources; no faithful single-type destination established. Keep records, IDs, names, keys and source correspondence intact. Do not split or regenerate as a classification shortcut. |
| Baseline `README.md`, `motivation.md`, `information-architecture.md`, `relationships.md`, `coverage.md`, `discrepancies.json` | Mixed navigation, projections and findings. Current architecture is not disposable generated output; unresolved/history entries are not approved architecture. Exact placement requires treatment of those roles. |
| Baseline `business-homes.json`, `home-derived-metadata.json` | Reviewed one-time Home result and resource naming amendments, not prospective inference or fresh identity registers. Keep correlated with captures; decide governance/architecture-support placement without changing Home/Composition/currentness. |
| Baseline `correspondence.json`, `documentation-classification.json`, `source-presentation-overlays.json`, `source-history/business-architecture-metamodel-321cdfa5.md.gz`, `release-1.1.0.json` | Correspondence, classification, frozen source and original release decision. Preserve exact original claims/pins. No reclassification as newly accepted RAD-1.1.4 architecture, no refresh of historical hashes and no investigation/reconciliation of the excluded 1.0.10 → 1.1.0 pilot. Decide carrier location, retaining recovery dependencies. |
| `docs/markdown/03-business-architecture/reference-model/` — README, JSON capture and schema | Preserve the three-file Candidate representation and schema together pending decision. Candidate location option: `enterprise-architecture/design/review/1.1.4/reference-model/` with suffixes unchanged; 1.1.4 would identify this migration's carrier, not the capture's original architectural acceptance. Confirm that treatment and relation to the synchronised capture; do not promote it to the current master. |
| `docs/markdown/governance/architecture-identity/model.md`; `registry.schema.json` | Proposed exact `enterprise-architecture/governance/architecture-identity/model.md` and `registry.schema.json`. Preserve later identity/naming precedence over older recommendations; relocating the contract does not reinstate superseded identity rules. |
| Identity `registry.json` | Frozen 14-EC alias/binding and allocation history, not the current owner of all canonical IDs. Proposed exact `enterprise-architecture/governance/architecture-identity/registry.json` as governance support, subject to preserving that historical standing and machinery correspondence. |
| Remaining eight identity files: `adoption-1.0.3.md`, `adoption-baseline-1.0.2.json`, `adoption-validation.json`, `bindings-validation.json`, `inventory.json`, `inventory.md`, `release-report.md`, `verification.json` | Historical decision, inventories and verification evidence. Determine register/evidence or preserved governance-support treatment and original release correspondence. Do not treat an inventory as current architecture, manufacture historical HCRLs, or place everything in the register because an adoption succeeded. Retain in place pending treatment. |

Baseline subrows partition its 26 files; identity subrows partition its eleven.
The compact braces in the projection row enumerate literal listed filenames,
not a pattern permitting additional files. No current/historical selection,
semantic source precedence, identity field, allocation rule or unknown
crosswalk is changed. Canonical UUID32, structural identifiers, capture keys,
aliases, XML element/relationship IDs and visual IDs retain their separate roles.
Relocation is one-to-one path correspondence unless an explicitly adjudicated
section mapping says otherwise; similar names do not establish identity.

**Three additional target collisions.** The proposed baseline projection paths
for `business-capabilities.md`, `business-enabling-capabilities.md` and
`enterprise-capabilities.md` coincide with DR-2's proposed paths for the older
Strategy source catalogues. These are different files with qualified source/
projection correspondence, not interchangeable copies. The projection paths are
therefore unresolved alternatives, not executable mappings alongside DR-2.
The Solution Architect must select distinct exact paths or a separately bounded
representation-incorporation treatment preserving both originals and their
source precedence. No overwrite, silent merge, source substitution or deletion
is proposed. Other projection paths still depend on the mixed-standing decision.

**Decisions sought.** Establish the destination of the correlated multi-type
source unit, decide whether the type-specific projection paths can coexist
faithfully with it, and classify the historical identity/support records. This
is a material prerequisite to migrating the complete architecture corpus.
An unresolved combined capture must not be moved into a made-up continuum
category merely to empty `docs/markdown/governance/`.

<a id="dr-4"></a>
## DR-4 — Governance sources and specialised contracts

**Concrete governance mapping.** For the following sixteen exact current files,
propose `docs/markdown/governance/<suffix>` →
`enterprise-architecture/governance/<suffix>`, preserving filenames and the
specialised meaning of each contract:

| Exact suffix | Preserved role / qualification |
| --- | --- |
| `archi-navigation-and-view-convention.md` | Derived navigation/View rules, qualified by current Round-Trip Praxis |
| `archimate-ingestion-change-review.md` | Specialised CR decisions, instructions and distinct outcomes; feeds HCRL |
| `archimate-ingestion-publication-semantics.md` | Native interpretation and publication rules |
| `archimate-round-trip-engineering-praxis.md` | Two triggers, exact inputs, containment, identity and View-preservation checkpoints |
| `architectural-axioms.md` | Single authoritative cross-domain register; no duplicate continuum register manufactured |
| `architecture-authoring-praxis.md` | Serial baseline/authoring/submission/publication lifecycle |
| `architecture-modelling-strategy.md` | Native compatibility; explicit Home distinct from Composition and other relations |
| `architecture-release-policy.md` | Existing allocation rule; root JSON stays at repository root |
| `canonical-identity-and-naming.md` | Resource-owned canonical identity and names, current containment precedence |
| `initial-information-architecture-projection.md` | Bounded projection selection and unresolved limits; no whole-family acceptance |
| `rad-archimate-coverage-assessment.md` | Qualified coverage/findings; relocation accepts no unresolved mapping |
| `rad-archimate-mapping-catalogue.md` | Representation compatibility and explicit adjudication cases |
| `rad-archimate-model-mapping.md` | Publication profile, source/identity/standing distinctions |
| `rad-archimate-publication-plan.md` | Incremental publication gates; no authorisation of later slices |
| `rad-archimate-source-inventory.json` | Scoped source inventory/navigation evidence, not an EA master catalogue or current source-count authority |
| `templates/archimate-ingestion-change-review-template.md` | Specialised template; no replacement of common HCRL acceptance |

Also propose exact
`docs/markdown/architecture-completion-plan.md` →
`enterprise-architecture/governance/architecture-completion-plan.md`.
Its programme context, completion standing, fresh-context guardrail and
deferrals remain unchanged. This is not programme completion work.

The two remaining top-level governance sources need explicit treatment:

| Source | Proposed treatment / decision needed |
| --- | --- |
| `docs/markdown/governance/README.md` | Governance index, but `enterprise-architecture/governance/README.md` already exists and has its own maintained content. No overwrite, silent merge or duplicate authority. Decide exact distinct destination or separately bounded incorporation/preservation of the index. Retain in place meanwhile. |
| `docs/markdown/governance/architecture-change-provenance.md` | Entire pre-adoption contract is preserved with supersession boundaries; the maintained common contract already exists at the same target basename. Same path pending decision on a historical carrier and navigation. Never overwrite the current contract or reinterpret old package duties as current requirements. |

The eighteen files here plus DR-3's 37 files account for the 55 existing
governance files. The already installed five common-governance files remain
at the same paths. Axioms' native Principle representation in the older mapping
catalogue is distinct from the authoritative register's governance location;
moving that register supplies no requirement to create another continuum copy
or new ArchiMate elements. Confirm this treatment if a different distinction is
intended. Domain-specific runtime information governance is separately retained
for classification in DR-2, rather than swept into this mapping.

**Decisions sought.** Review the seventeen exact governance/plan relocations and
two collision treatments, coordinated with DR-3's three identity-support proposals.
Any later mechanical link/path amendments require DR-10's bounded application
treatment. Preserve specialised checkpoints, dispositions, source history and
unresolved content throughout.

<a id="dr-5"></a>
## DR-5 — Existing architectural review and decision records

**Observed standing and proposed carrier options.** These eight records remain
at their source paths during preparation. Established decisions remain
established; historical uncertainty is not reopened or resolved by migration.
An approved record with explicit accepted deferrals can differ from a partially
approved record. The record's own decision history determines that distinction.

| Source relative to `docs/markdown/` | Recorded standing / destination decision |
| --- | --- |
| `01-motivation/reviews/independent-assurance-reconciliation.md` | APPROVED / CLOSED, human approval 2026-10-08. Register candidate, preserving original approval and refreeze boundary. |
| `02-strategy/reviews/ax05-state-responsibility-reconciliation.md` | APPROVED / CLOSED, human approval 2026-10-08. Register candidate; no new AX-05 meaning. |
| `02-strategy/reviews/business-capability-five-region-reconciliation.md` | Historical decision retained; later catalogue/metamodel qualify its current authority. Register/history carrier candidate; no restoration of superseded hierarchy. |
| `02-strategy/reviews/fixed-capability-decomposition-decision.md` | Approved human decision 2026-10-13; normative meaning also in the metamodel. Register candidate; preserve distinction between decision record and current semantics. |
| `04-information-architecture/reviews/package2-g1-review.md` | G1 CLOSED; approved K1–K13 and reconciliation/history/retained uncertainties. Register candidate only with the documented whole-record closure and explicit deferrals preserved. |
| `04-information-architecture/reviews/package2-g2-block1-review.md` | G2 Candidate; D01–D04 approved, Q01 resolved, remaining propositions await review; G2 not CLOSED. Review candidate only, including the approved meanings selected by the IA projection. No whole-record promotion. |
| `04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md` | Name/conceptual boundaries approved for capture; complete architectural placement not established in that record. Review candidate; preserve original finding and later linked adjudications without rewriting the historical finding. |
| `04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md` | Historical investigation/recommendations, explicitly qualified by later REQ-FND-005 and downstream derivation. Investigation evidence, not an approved design simply because the investigation completed. Carrier requires adjudication. |

**Exact candidate path rule for review.** If the Solution Architect approves
1.1.4 as the *migration-carrier* version, register candidates map to
`enterprise-architecture/design/register/1.1.4/migrated-decisions/<source-relative-to-docs/markdown>`;
review candidates map to
`enterprise-architecture/design/review/1.1.4/migrated-reviews/<same-relative-path>`.
Each listed row thereby has a deterministic candidate filename. These are
proposed grouping names, not architectural types, new HCRLs or assertions that
the original decisions were made in RAD-1.1.4. For the investigation, a possible
evidence carrier is
`enterprise-architecture/design/evidence/1.1.4/before/migrated-review-provenance/04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md`;
confirm the purpose/role or leave it in place. No source date is converted into
an inferred original RAD release.

**Decisions sought.** Confirm standing-based treatment and the migration-carrier
convention, choose a different evidenced resulting-version correspondence where
appropriate, or explicitly defer each affected row. No existing completed HCRL
is changed. No historical HCRL is reconstructed and no approved fragment is
extracted into the register through an agent judgement about the whole file.

<a id="dr-6"></a>
## DR-6 — Candidate Information Systems Architecture and scratchpad material

**Observed scope.** Domain05's 36 files comprise fourteen top-level Markdown
files, eighteen DesignNotes, one B1 review and three traceability files. Its
README records Step A accepted
as sufficiently faithful for Step B, A4 PASS and bounded B2.1 PASS; B2.2 is not
commenced. It expressly distinguishes this from final Solution Architecture,
upstream coverage, MVP determination and implementation authority. Approved
individual adjudications coexist with working conversational notes, historical
positions, explicit deferrals and the Candidate Architecture. “Architectural
Design Note” alone is not evidence of complete approval. The accepted Agora/
Omilia, Calliope, Mneme/Mnemosyne, Themis, Kleio, Pylai and execution boundaries
must survive unchanged, including their source correspondences.

| Exact source set | Proposed exact prefix substitution / conditions |
| --- | --- |
| `docs/markdown/05-information-systems-architecture/` — 36 files | Candidate intact-unit destination: `enterprise-architecture/design/review/1.1.4/information-systems-candidate/`, preserving every suffix. Requires confirmation that resulting migration-version placement preserves the accepted candidate-baseline role, individual approved decisions and working-note distinctions. |
| `docs/markdown/scratchpad/architecture-design-notes.md` | `enterprise-architecture/design/review/1.1.4/scratchpad/architecture-design-notes.md`; preserve decision-time working reasoning, not authority over reconciled canonical catalogues. |
| `docs/markdown/scratchpad/capability-reference-candidate/` — five `.tex` files, v0.1–v0.5 | `enterprise-architecture/design/review/1.1.4/scratchpad/capability-reference-candidate/`, preserving filenames. These are candidate drafts, not the governed LaTeX published set. |

**Adjudication needed.** Confirm intact candidate-unit migration rather than
forcing component/service/data meanings into final continuum locations.
Alternatively specify exact independently approved artefacts and destinations
without promoting partially approved files to the register. In particular:

- `sources.md`, C/F/D/M/CDG identifiers and gaps are review/source navigation,
  not a new canonical registry. Preserve U01–U21 and their actual authority.
- The two expressly conversational notes for Mneme publish/subscribe and
  Pathways/Journeys remain working input, despite later adjudicated meanings
  appearing in canonical candidate documents.
- Pylai's working DesignNote retains further unadjudicated nuances; the
  Pragma Outputs DesignNote's explicit adjudication does not accept all Domain05.
- Both Kleio DesignNotes, ClassAndPackage and other overlapping/historical
  notes remain distinct; apparent succession is not permission to merge/delete.
- B1 review/worked example/method and B2.1 approved relationships retain their
  different outcomes. Candidate baseline acceptance does not establish B1 PASS
  or B2.2 commencement.

The 1.1.4 review path would identify this migration's review carrier, not a new
architecture baseline or a reissued original approval. Confirm whether any
already authoritative candidate semantics also require a separately mapped
continuum representation. No duplicate source, extraction, re-authoring or new
ApplicationComponent/DataObject classification is authorised by this candidate.
DR-3's selected IA records retain their existing source correspondence.

<a id="dr-7"></a>
## DR-7 — ArchiMate workspace, machinery and processing preservation

**Established boundary and proposed mapping.** Common governance places the
existing specialised ArchiMate strategy beneath `design/archimate/` and forbids
redesigning it through restructuring. Propose the exact prefix substitution
`docs/archimate/` → `enterprise-architecture/design/archimate/` for its 1,830
tracked files, preserving all internal suffixes, subject to DR-10. This table
classifies that single bounded mapping; it is not permission to move only the
executables and leave their live dependencies behind.

| Exact source relative to `docs/archimate/` | Files | Preserved role within the proposed destination |
| --- | ---: | --- |
| `README.md` | 1 | Practical entry/navigation with preserved superseded sections |
| `framework/` | 89 | Reusable tools, schemas, tests, fixtures, framework baselines/configuration and historical location rules; no architectural authority from execution |
| `exchange/publish/` | 8 | Published exchange XMLs and package index; publication release names/standing remain unchanged |
| `exchange/ingest/` | 7 | Incoming exports/index; submission is not acceptance |
| `processing/publishing/` | 543 | Existing publication working state/evidence; no automatic promotion to permanent evidence or current releases |
| `processing/ingestion/` | 238 | Existing intake/reconciliation/review state; exact baseline/export/review applicability retained |
| `processing/machinery-validation/` | 904 | Run identities and frozen specimens; not RAD releases |
| `processing/README.md` | 1 | Workspace role/navigation, including preserved historical guidance |
| `governance-tooling/` | 39 | Classified non-RAD 1.0.14 evidence and prior migration audit; keep outside architecture-release namespaces |

**Specialised semantics that the move must preserve.** Native types and
specialisations, Home versus type-selected Composition, current/historical
relationship selection, canonical versus Exchange identity, CR disposition versus
Outcome/Reason, exact input pins, authoring/submission/publication lineage and
all existing authored View identities/membership/layout/style/navigation remain
as governed. No model regeneration, new identity allocation, relationship
normalisation, naming cleanup, changed View or new processing run is proposed.
Historical `change-review.md` and nested source snapshots keep their original
text, timestamps, pins and applicability. They are not reissued as completed HCRLs.

**Decision sought.** Confirm the workspace substitution and whether specialised
run-local reviews/evidence should remain in the intact workspace with common
HCRL correspondence, rather than being indiscriminately pulled into general
review/register/evidence folders. Identify any specifically selected durable
transition evidence requiring a separate mapped carrier. Until then, preserve
all processing material, including recovery, unique-content and migration
dependencies. No processing disposal, decompression/repackaging, recursive
archiving, fixture redesign or historical pilot adjudication is included.
Moving a dependency is not proof of current-operation independence from it.

<a id="dr-8"></a>
## DR-8 — LaTeX publication packages and their mixed supporting material

**Observed material.** `docs/latex-rad/` contains one shared `.gitignore`, a
56-file v0.1-draft bundle and a 77-file v0.2-draft bundle, all beneath
`Harmonia-EA-Capability-Map/`. Their publication labels are not RAD releases.
The v0.2 README explicitly retains reviewed Proposed hierarchy and does not
amend RADS or declare a domain complete. The packages contain `.tex`/PDF
publication, authored catalogue explanations, frozen source/coverage and identity
manifests, model JSON, validation/reconciliation records, templates, machinery
and some tracked build/log output. They are not 134 interchangeable approved
architecture files. Current baseline/recovery machinery still refers to these
frozen packages; ordinary refresh from current architecture is expressly excluded
by the synchronised baseline contract.

| Exact source set | Proposed destination / treatment |
| --- | --- |
| `docs/latex-rad/Harmonia-EA-Capability-Map/v0.1-draft/` | Intact-bundle candidate: `enterprise-architecture/continuum/documentation/published/Harmonia-EA-Capability-Map/v0.1-draft/`, suffixes unchanged; conditional on the publication/support classification below. |
| `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/` | Intact-bundle candidate: `enterprise-architecture/continuum/documentation/published/Harmonia-EA-Capability-Map/v0.2-draft/`, suffixes unchanged; preserves Proposed/Candidate/historical labels, not final architecture acceptance. |
| `docs/latex-rad/Harmonia-EA-Capability-Map/.gitignore` | Corresponding shared parent under the proposed publication prefix, only if the intact correlated package mapping is approved. No clean/delete command follows from it. |

**Decisions sought.** Confirm that “published” may hold these labelled historical
draft publications with inseparable reproduction/support material, or require
an explicit per-file separation between publication, design/review, evidence and
machinery. A complete bundle move is a candidate option to preserve correlation,
not an inference that its model JSON and tools are continuum architecture.
If separation is required, establish exact paths, sibling dependencies and
frozen-byte treatment before application; no model/transcriber refresh or
deletion of incidental build products is authorised. The v0.2 loader reads the
frozen v0.1 transcriber and adapts its root lookup in memory: moving one bundle
alone would break that existing dependency. Legacy LaTeX, LibreOffice and
scratchpad drafts have distinct treatment in DR-9/DR-6 and are not silently
included in this publication mapping.

<a id="dr-9"></a>
## DR-9 — Legacy, implementation and other documentation boundaries

**Proposed no-move boundary.** Inspecting/discovering a document is not
reconciliation of its architectural meaning. Root instructions require
`legacy-doco → architectural review/adjudication → RADS`; useful implementation
detail, source-era claims and superseded sections must not become current
architecture through relocation. No legacy section is rewritten or newly wrapped
in this preparation. Existing supersession wrappers and original enclosed text
remain unchanged, even when contrary to current RADS.

| Exact source set | Proposed treatment / retained role |
| --- | --- |
| `docs/AGENTS.md` | Same path; scoped repository instructions, not legacy architecture or a continuum element. Update its live navigation only in an authorised application. |
| `docs/implementation/harmonia-convergence-runtime-integration-plan.md` | Same path; assigned implementation order/scope authority remains in its defined role. It does not override architecture. Migration does not commence a later runtime milestone. |
| `docs/README.md` | Same path; existing documentation navigation. A future bounded navigation amendment must preserve legacy/current distinctions. |
| Other 150 `docs/` files outside the five large sets in DR-1 and the three exact rows above | Same path. Includes legacy architecture/concepts/design, configuration/deployment/operations, security, provider registry, modules, simulation, backlog/deferred registers, mindmap and the Strategy `.docx`. No source is incorporated into the continuum merely because its topic matches a native concept. |
| `docs/latex-legacy/` — 101 files | Same path; legacy source/publication/build artefacts, including original chapters, diagrams and PDF. Its “publication-ready” description is not current acceptance. No regeneration or promotion. |
| `docs/libreoffice/` — 21 files | Same path; legacy ODT representation, assets, generator and templates. Preserve LaTeX dependency; do not present it as a newly governed EA publication. |
| `openspec/` — 424 files | Same path; separate specification material/manifest/instructions outside current RADS. No automatic Requirement/Service classification or current acceptance. Any incorporation requires separately bounded reconciliation. |
| `.junie/` — 117 plans and 294 reports/support files | Same path; working/historical execution and navigation aids, including existing linked completion/recovery evidence. Do not bulk-migrate them into post-1.1.3 formal Task/Report evidence or infer architecture from them. Preserve live dependencies; prospective retention is not retrospective reconstruction. |
| Root `AGENTS.md`, `AGENTS-old.md`, `README.md` | Same path; current instructions, old instructions and repository navigation retain their distinct standing. No root authority file is moved into the continuum. |
| `deployment/README.md`; `energeia/README.md`; two `hestia/mneme-cluster/docs/` Markdown files; `hestia/mnemosyne-operations-cli/README.md`; `paradeigma/README.md` and fourteen `paradeigma/docs/` Markdown files; `petasos/README.md`, `petasos/deployment/README.md` and three `petasos/docs/` Markdown files; `pylai/pylai-mllp-cli/README.md` | Same path; all 26 other tracked Markdown documentation files outside the already inventoried trees. Module/runtime/simulation meaning remains legacy or implementation material unless specifically reconciled. No production/module/build relocation is proposed. |

The legacy material is discovered and bounded, not semantically certified. No
claim is made that its contents have all been reconciled, are all superseded,
or are dispensable. No new archive/historical repository taxonomy is introduced.

**Decision sought.** Confirm this explicit exclusion from the first content
migration, or identify exact additional sections for separate review before
mapping/incorporation. Preserve linked existing execution/completion evidence
without manufacturing historical release or timestamp correspondence. General
legacy reconciliation, implementation planning, historical cleanup and disposal
would be additional scope.

<a id="dr-10"></a>
## DR-10 — Reference continuity, application prerequisites and verification

**Material issues preventing an immediate faithful whole-corpus move.** The
unresolved native/multi-concept destinations and standing treatments in DR-2–DR-6,
the three source/projection collisions in DR-3, two governance basename
collisions in DR-4 and mixed publication/support treatment in DR-8 prevent an
unqualified complete mapping. Additional concrete dependencies
mean that even straightforward path proposals cannot be applied as bare renames:

| Observed maintained consumer / source | Consequence for bounded application |
| --- | --- |
| `docs/archimate/framework/tools/workspace_paths.py`, `governed_metadata.py`, `recover_v02.py` | Root discovery uses existing `parents[2]` depth. The additional target `design/` level changes that result. Preserve repository-root and historical-location resolution through a reviewed mechanical adaptation. |
| `scripts/architecture_identity.py` | Hard-coded framework/identity locations and validation accepting only `docs/markdown/` provenance paths. A move otherwise makes governed sources appear invalid. Preserve original source pins and old locator support; do not weaken identity validation. |
| Framework `synchronise_baseline.py`, `information_population.py`, `Makefile` and `documentation-map.json` | Textual baseline paths, source references, expected RADS containment, hashes and output destinations encode the old layout. Live-source path adaptation and frozen historical provenance need different treatment. |
| Framework historical locations, governance transitions, correspondence, source overlays, fixtures and recovery manifests | Recorded path/hash claims must remain frozen. Resolve old locators through explicit correspondence; do not run a global replacement through archives, locks, reviews or old publications. Existing resolution is exact, not a general directory-prefix redirect. |
| `scripts/architecture_identity.py`, `scripts/rads_business_architecture.py`, `scripts/architecture_release.py` and associated tests | Shared repository machinery remains at `scripts/`. Adapt only proven migration consumers under separate application authority; no gratuitous relocation or cache cleanup. Root release JSON remains the same authority. |
| Publication `v0.2-draft/tools/baseline.py` and frozen v0.1 transcriber | Sibling bundle and root-depth assumptions require faithful relocation support without editing the frozen transcriber or inventing a new source snapshot. |
| Root/docs instructions, maintained governance entry/contracts/templates, domain indexes and active tool commands | Live relative links/anchors and command paths need exact source-to-destination updates. Existing common-governance files stay in place but references to relocated sources would change. |
| Completed HCRLs, preserved pending decisions, historical Reports, XML publication properties and embedded superseded text | Preserve original content and original path statements; provide retrievable mapping/current navigation or source-control locators. No completed-HCRL edit or invented erratum procedure. Decide any compatibility carrier explicitly. |

These observations identify path consumers; they do not prove a complete static
or runtime dependency audit. A file inventory is exhaustive for its tracked set;
semantic classification and future application validation remain qualified.
Some existing navigation already reflects older publication/source states.
Migration is not permission to “correct” those historical statements. Baseline
1.1.3 does not imply an XML/LaTeX publication for 1.1.3 or 1.1.4.

**Proposed bounded application requirements for adjudication.** Once exact
destinations and scope are authorised, retain the source-state locator and an
old/new path account sufficient to recover every mapped item, and distinguish
mechanical live-reference amendments from preserved historical text. Recheck
target collisions, untracked/ignored material, baseline drift and source changes
before mutation. New files are not silently swept into the fixed inventory.
Use current governed sources/configuration and declared fixtures; preserve any
unresolved dependence on historical processing explicitly. Removing such a
dependency requires separate authorisation if it changes machinery beyond
relocation support. No symlink/stub service, archive format, new identity system
or broad historical rewrite is authorised by this submission.

For an actual application, verify the exact accepted path set, content and
section correspondence, no unmapped loss/overwrites, retained supersession
boundaries, all canonical/representation/visual identities, names, currentness,
standing and relationship semantics. Check live links and old locator retrieval
separately. Run required architecture tests, applicable identity/source-validation
and workspace/publication tests with bounded execution; stage any necessary
generation outside immutable historical packages. Test path discovery and output
destinations against the new structure rather than accepting unchanged outputs
as proof that new sources were consumed. No regeneration of old publications or
actual Archi transport is claimed by a path-only check. If unchanged models are
part of the accepted application, preserve their bytes/semantic/View signatures
as applicable. Report any unmet specialised gate or uncertain result explicitly.

**Preparation facts and validation.** Only this pending candidate is added; no
existing tracked source is modified, renamed or deleted. The root release,
common contracts, reviewed inputs, completed HCRLs, models, publications and
processing material are unchanged. No migration, formal execution records,
historical reconciliation, publication generation or disposal was performed.

The required architecture suite ran with a 240-second timeout and five-second
termination grace:

```bash
timeout --signal=TERM --kill-after=5s 240s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS:** 90 tests in eleven classes, zero failures/errors/skips, Maven BUILD
SUCCESS, 17.133 seconds, exit 0. `python3 scripts/architecture_release.py check`
**PASS:** 1.1.3, previous 1.1.2. Preparation checks **PASS:** eleven matching DR
table rows/sections/anchors, thirteen consistently formed tables, seventeen
existing local reference definitions, complete tracked inventory arithmetic,
selected native-type counts, and sixteen existing native-concept source files
with distinct proposed destinations that do not already exist. The separately
disclosed source/projection and governance collisions remain unresolved.
Source preservation **PASS:** zero tracked differences from the originating
commit; this pending HCRL is the sole untracked addition, and root release values
are unchanged. Candidate whitespace validation **PASS**. No command timed out or
stalled. These checks establish
preparation conformance, not migrated-tool operation, complete native
classification, architectural acceptance or final migration readiness.

**Decision sought.** Authorise only the necessary mechanical consumer/reference
changes as part of a later exact mapping application, preserving original
historical claims and all specialised semantics. Establish explicit treatment
for unresolved effects before dependent migration. Do not infer application
authority from this preparation's successful tests.

<a id="dr-11"></a>
## DR-11 — Bounded Minutiae for candidate preparation

**Disclosed group.** Limited to preservation of the review-preparation wording,
source-state navigation, inventory presentation, Markdown/table/anchor formatting
and factual preparation-check reporting in this one pending HCRL. This creates
no independent architecture catalogue, evidence archive, agent interaction log,
formal review Task/Report convention or application Report. The material scope,
classifications, candidate mappings, unresolved decisions and application
prerequisites remain in DR-1–DR-10; none is demoted to Minutiae. No other existing
document is changed through this group.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

The table and control refer to this same bounded group. Silence, an agent's
classification and successful checks supply no acceptance. No Solution Architect
disposition or incorporated-result confirmation has been generated by Codex.

[release-json]: ../../../../architecture-release.json
[agents]: ../../../../AGENTS.md
[axioms]: ../../../../docs/markdown/governance/architectural-axioms.md
[release-policy]: ../../../../docs/markdown/governance/architecture-release-policy.md
[governance]: ../../../governance/architecture-governance.md
[praxis]: ../../../governance/architecture-review-praxis.md
[provenance]: ../../../governance/architecture-change-provenance.md
[supplied-reference]: ../1.1.3/hcrl-1.1.3-005.pending.md
[baseline]: ../../register/1.1.3/hcrl-1.1.3-005.complete.md
[structure-review]: ../1.1.2/hcrl-1.1.2-002.pending.md
[structure-complete]: ../../register/1.1.2/hcrl-1.1.2-003.complete.md
[modelling]: ../../../../docs/markdown/governance/architecture-modelling-strategy.md
[mapping]: ../../../../docs/markdown/governance/rad-archimate-model-mapping.md
[mapping-catalogue]: ../../../../docs/markdown/governance/rad-archimate-mapping-catalogue.md
[naming]: ../../../../docs/markdown/governance/canonical-identity-and-naming.md
[roundtrip]: ../../../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
[textual-baseline]: ../../../../docs/markdown/governance/baseline/README.md
