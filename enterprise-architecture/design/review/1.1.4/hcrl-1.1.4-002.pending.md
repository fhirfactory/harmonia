<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Complete inventory, preferred mapping and review boundary | Not recorded | — |
| [DR-2](#dr-2) | Corpus-derived classification and migration guidelines | Not recorded | — |
| [DR-3](#dr-3) | Textual architecture, composite documents and conceptual information | Not recorded | — |
| [DR-4](#dr-4) | Governance, modelling rules, identity and historical controls | Not recorded | — |
| [DR-5](#dr-5) | Review/register placement, partial candidates and working material | Not recorded | — |
| [DR-6](#dr-6) | Governed captures and distinct synchronised projections | Not recorded | — |
| [DR-7](#dr-7) | Specialised ArchiMate workspace and maintained-input dependencies | Not recorded | — |
| [DR-8](#dr-8) | Draft publication packages and distribution standing | Not recorded | — |
| [DR-9](#dr-9) | Retained engineering, accepted ADRs, legacy, OpenSpec and agent history | Not recorded | — |
| [DR-10](#dr-10) | Collision resolutions, reference continuity and existing validation failure | Not recorded | — |
| [DR-11](#dr-11) | Faithful application boundary, missing evidence and acceptance gates | Not recorded | — |
| [DR-12](#dr-12) | Bounded Minutiae: candidate preparation and verification | Not recorded | — |

# HCRL — Comprehensive repository migration candidate

**Standing:** pending iteration **002**, prepared by Codex on 2026-10-21 for
**RAD-1.1.3 → RAD-1.1.4**. This is analysed review material. No Solution Architect
disposition, migration, source-content amendment or release increment is recorded
by this iteration. Every destination and guideline below is a proposal.

**Review set:** this HCRL, the [per-source mapping ledger][ledger], the
[reference impact list][references] and the [inventory/check reconciliation][checks].
The CSVs (the reference list is gzip-compressed) are finite supporting review
material for these DRs, not another decision
log, approval scheme, architecture master catalogue or architectural identity
register. The HCRL contains the semantic proposals and alternatives; approval of a
CSV cell cannot be inferred from its presence. Paths in the ledger are exact,
repository-relative paths, with original spelling and case.

**Revision:** [iteration 001][previous] is preserved unchanged. This iteration
replaces its unresolved placement proposals with preferred treatments and exact
paths. It also corrects an overbroad possible reading of its legacy boundary:
accepted ADRs, operative engineering contracts and assigned implementation
sequencing retain their existing roles outside RADS. It does not supersede any
human disposition: none is recorded against iteration 001.

**Authority:** [Architecture Governance][governance], [Review Praxis][praxis],
[Architecture Change Provenance][provenance], [root instructions][agents] and the
[release policy][release-policy] govern. The [completed RAD-1.1.3 HCRL][completed]
is the current transition baseline. [Modelling strategy][modelling],
[mapping profile][mapping], [mapping catalogue][mapping-catalogue],
[identity/naming][naming] and [Round-Trip Praxis][roundtrip] constrain the specialised
semantics. Their actual current sources were used; earlier summaries and agent
reports supply navigation/evidence, not substitute architectural authority.

**Axiom consistency:** [AX-03/04][axioms] preserve native representations and
Harmonia semantics independently of repository machinery. AX-08/09 support useful
decision evidence without promoting processing history to architecture or
authorising disposal. AX-14 preserves architectural classification versus carrier
classification; conceptual versus logical/physical; canonical versus
representation identity; semantic standing versus currentness/placement; review
versus execution; release versus publication. AX-15/17 preserve unestablished
meanings and effects. No type, specialisation, ownership, Home, Composition,
allocation, canonical name, identifier or architectural relationship is created
by moving its carrier. DR-2 expressly requests adjudication of the carrier
guideline needed to make a faithful multi-concept migration possible.

<a id="dr-1"></a>
## DR-1 — Complete inventory, preferred mapping and review boundary

**Operative review-preparation instruction.** The user directs deeper interrogation
of the complete iteration-001 corpus, with the purpose of establishing a stable,
reproducible and shareable baseline before substantial further Information,
Application, Middleware and Technology architecture. The instruction is retained
here as review provenance:

> Goal
>
> Deepen the RAD-1.1.4 repository migration analysis and produce a comprehensive candidate mapping for the existing Enterprise Architecture/documentation corpus.
>
> Expected Outcome
>
> Revise the pending RAD-1.1.4 candidate so that every in-scope source artefact has a proposed disposition and, where migration is proposed, an explicit destination. The candidate must be sufficiently complete for the Solution Architect and ARB to derive and adjudicate the repository classification/migration guidelines and then authorise a coherent migration.
>
> Context
>
> The purpose of RAD-1.1.4 is to establish a stable, reproducible and shareable Enterprise Architecture/documentation baseline before substantial further Information, Application, Middleware and Technology architecture is added. The first candidate successfully identified many mixed, ambiguous and historically qualified artefacts, but frequently stopped at “requires adjudication” without proposing the most defensible treatment. That is insufficient for the purpose of this transition.
>
> Constraints
>
> Do not migrate, rename, delete, rewrite or otherwise modify source content. Do not invent architectural meaning, standing, identity or relationships. Preserve candidate/approved/historical/superseded distinctions and specialised semantics. However, uncertainty is not a reason to stop classification analysis: inspect the artefact itself, its surrounding documentation, references, governed contracts and repository usage deeply enough to propose the most defensible treatment. Where more than one treatment is genuinely plausible, identify a preferred treatment and alternatives with reasons. Explicitly distinguish architectural classification from repository/document-management classification.
>
> Authorised Activity / Scope
>
> Perform a deeper interrogation of the complete in-scope corpus identified in iteration 001. For every source artefact or genuinely homogeneous bounded set:
>
> - determine its actual role and standing from its content and governed context;
> - propose Move → exact destination, Remain → exact current location and reason, Incorporate → exact target and preservation treatment, or another explicitly defined disposition where those are insufficient;
> - identify collisions, mixed-content artefacts and live/historical dependencies and propose a preferred resolution rather than merely reporting the problem;
> - identify recurring classification problems from which repository migration guidelines can be derived;
> - distinguish cases requiring an architectural decision from cases that are merely mechanical/document-management choices;
> - provide sufficient source → destination/disposition coverage that no in-scope artefact disappears into an unanalysed “other”, “mixed” or “pending classification” bucket.
>
> Where a file contains genuinely different classes of material, determine whether preserving it intact is semantically preferable or whether bounded extraction/incorporation is required. If extraction is proposed, identify the exact boundaries and treatment; do not perform it.
>
> Revise the RAD-1.1.4 pending HCRL as the next candidate iteration. Do not apply any migration.
>
> Report
>
> Report:
> 1. the revised HCRL path;
> 2. complete disposition/mapping coverage and reconciliation to the inspected inventory;
> 3. proposed repository classification/migration guidelines derived from the corpus;
> 4. the material decisions that genuinely require Solution Architect adjudication;
> 5. collisions/dependencies and Codex's preferred resolution for each;
> 6. anything for which Codex still cannot propose a defensible treatment, including exactly what evidence is missing.
>
> The objective is not to minimise the number of decisions presented to the Solution Architect. It is to ensure those decisions are informed choices between analysed candidate treatments, rather than requests for the Solution Architect to perform the repository archaeology.

**Originating state and finite inventory.** HEAD is
`41047e1dc5c7d1ca4769dc7479bb50cae60989a7`, branch `ArchitecturalGovernance`.
Root `architecture-release.json` is `1.1.3`, predecessor `1.1.2`. The tracked
iteration-001 corpus contains **3,320 files**. The only additional source at entry
to this revision was untracked iteration 001; it is a separately identified input,
giving **3,321 ledger records**. New iteration-002 review artefacts are outputs,
not silently added migration inputs. Tracked scope is the exact paths from that
commit, including tracked caches/build products; ignored/untracked local exports,
external software installations and archive members are not additional sources.

| Inspected source partition | Baseline files | Preferred Move | Preferred Remain |
| --- | ---: | ---: | ---: |
| `docs/markdown/` | 179 | 179 | 0 |
| `docs/archimate/` | 1,830 | 1,830 | 0 |
| `docs/latex-rad/` | 134 | 134 | 0 |
| `docs/latex-legacy/` | 101 | 0 | 101 |
| `docs/libreoffice/` | 21 | 0 | 21 |
| Remaining individually listed `docs/` files | 153 | 0 | 153 |
| Existing `enterprise-architecture/` | 21 | 0 | 21 |
| `openspec/` | 424 | 0 | 424 |
| `.junie/` | 411 | 0 | 411 |
| `scripts/` | 16 | 0 | 16 |
| Root/module/deployment Markdown | 29 | 0 | 29 |
| Root `architecture-release.json` | 1 | 0 | 1 |
| **Tracked total** | **3,320** | **2,143** | **1,177** |
| Prior pending candidate, additional review input | **1** | **0** | **1** |

Of the proposed moves, 208 primary destinations are under `continuum/`, 48 under
`governance/`, and 1,887 under `design/`. These are carrier counts, not counts of
accepted architecture. Three rows additionally propose purpose-selected fixture
copies (DR-7); they do not inflate source counts. Each of the 179 RADS files has
an explicit mapping: 117 numbered-domain files, 55 governance files, the programme
plan and six scratchpad files. No whole `docs/markdown → continuum` move is implied.

**Ledger semantics.** `Move` proposes relocating the entire source to the exact
primary destination; `Remain` proposes the exact unchanged current path and
explains its continuing role. `CopyFixture` is an additional, byte-for-byte copy
of the entire named source into the exact additional destination; the primary
Move/Remain still applies. It confers fixture standing, not architectural
incorporation. There is **no proposed architectural extraction, merge or new
incorporation** in this transition. DR-3/5/8/9 explain why intact preservation is
preferable for each recurring mixed case. If review instead chooses extraction,
that alternative needs a revised bounded section mapping before application.

Each row records architectural classification separately from repository role,
standing basis, exact paths, source SHA-256/size, content-derived title/evidence,
inspection method, governed context, reason and applicable DR. The 3,320 source
hashes match entry; iteration 001 also matches its entry hash. No missing,
duplicate or unmapped source or colliding proposed primary/additional destination
was found. A Remain row is an analysed disposition, not an exclusion from coverage.

**Decision sought:** confirm this finite boundary and the proposal semantics.
Adjudicate dependent DRs before authorising application. The complete ledger is
the preferred treatment, not authority to execute it. Preparation leaves the
release unchanged and creates no formal execution Task/Report pair.

<a id="dr-2"></a>
## DR-2 — Corpus-derived classification and migration guidelines

These proposed guidelines arise from specific corpus problems; they are not
claimed as already adopted additions to the common contract.

| Recurring problem and evidence | Preferred guideline | Alternative and consequence | Decision character |
| --- | --- | --- | --- |
| A Capability catalogue contains approved responsibilities, Proposed placement and historical explanation; the synchronised baseline records those dimensions independently | Classify architectural assertions by established native concept/specialisation; classify the **document carrier** separately. Migration preserves the assertion's standing and does not accept every sentence. A maintained catalogue is distinguishable from a submitted, partially approved review artefact. | Place every mixed-standing carrier in review. This is conservative but removes existing maintained canonical architecture from the continuum and makes almost all current catalogues look like one pending submission. | **Material SA clarification** of current-carrier versus partial-review rule |
| Six capability-scoped behaviour documents deliberately keep Features, Functions, Services, Processes and information together; InformationUnit's metamodel contains both concept and rules | Preserve genuinely multi-concept architecture as **individually mapped files directly under `continuum/`**, with native subjects stated in this review. Root placement is a document-management accommodation, not a new architectural category, type, specialisation, hierarchy or master index. Continue native-concept navigation to their existing assertions. | Pick one primary native directory where dominance is defensible; use it for the single-subject cases in DR-3. For irreducibly multi-concept cases, splitting by heading loses context; duplicating whole files creates competing carriers. If root carriers are rejected, retain the exact sources in place pending a reviewed extraction mapping. | **Material SA placement decision**; no invented Pattern/InformationFamily/LogicalComponent type |
| Indexes contain both navigation and actual modelling qualification rules | Put predominantly normative modelling governance in `governance/`; put architecture orientation/navigation at `continuum/documentation/`. Preserve substantive explanation intact and link to the original authority rather than creating duplicate architecture or a new global index. | Keep every index alongside one native type: sometimes misstates its scope. Extract every explanatory paragraph: adds unnecessary reconciliation and new authoritative carriers. | Placement principle material; exact filenames/rebased links mechanical after acceptance |
| Domain04 `governance/` describes runtime assertion authority, custody and lifecycle; Domain03 metamodel describes modelling rules | Classify by subject and operative purpose, not current folder/filename: runtime information governance is architecture; architecture modelling/review governance belongs under repository governance. | Move all files named governance/metamodel to repository governance: loses the InformationUnit concept and confuses runtime authority with architectural review. | **Architectural classification**, corpus evidence in DR-3/4 |
| G2 families and the Domain05 candidate contain some approved meanings but lack whole-artefact acceptance | Keep the whole partially adjudicated unit under review, preserving approved assertions in force. Use register only for concluded decision records. Migration date/version does not rewrite historical decision date or establish whole-candidate acceptance. | Extract approved clauses into continuum/register. Requires exact clause boundaries, reconciliation of dependencies and a separate decision on the remaining source; no such extraction is necessary for a faithful migration. | **Material standing/placement decision** |
| Three original/projection basename collisions; IDs, historical aliases, reference keys, headings and LaTeX labels play different roles | Preserve both carriers with descriptive representation qualifiers; filenames may change solely to disambiguate carriers. Never use similarity, position, folder or name to create canonical identity or architectural correspondence. | Merge the colliding files: loses source explanation or qualified projection. Drop one as redundant: unsupported. | Filename mechanical; source/projection precedence remains architectural |
| Current code reads old pinned captures and run history | Separate maintained current inputs, declared fixtures and historical replay. Preserve frozen bytes/hashes/link bases; adapt live lookup through an explicit locator map. A path repair does not legitimise a historical dependency in the current operation. | Blanket search/replace or hash refresh: destroys historical claims. Preserve old aliases indefinitely as current inputs: retains the prohibited operational dependency. | Dependency strategy material; exact path edits mechanical within approved scope |
| Draft LaTeX packages include editable source, Proposed model, review inputs and generators | Treat `documentation/published/` as distribution/publication role, with draft standing explicit. Keep correlated package support and whole editable publication together; source definitions do not gain acceptance because published. | Put complete packages in review (DR-8 alternative), or split source/model/evidence/tooling across trees. Splitting weakens standalone use and existing identity/verification dependencies. | **Material SA interpretation** of published versus approved |
| Accepted ADRs and operative engineering material live outside RADS, alongside genuine legacy descriptions | Remain in their assigned role/location; preserve accepted decisions and bounded supersession. Incorporation into architectural sources requires separate reconciliation, not a migration shortcut. | Promote or demote all material by directory: both contradict recorded source roles. | Existing authority preserved; wholesale incorporation would be a new architectural decision |
| A historical file lacks proof of current adoption, but its physical treatment is clear | Propose preservation at its exact current path with recorded role and limits. Missing adoption evidence does not prevent a defensible document-management disposition or authorise invented architectural meaning. | Ask SA to classify every technical guide from scratch: unnecessary; only a requested change of its architectural authority needs new adjudication. | Management choice unless meaning/authority changes |

**Scope of requested decision:** approve/correct these principles for this finite
mapping. They introduce no new mandatory metadata system or ongoing knowledge
taxonomy. If a principle is rejected, the associated ledger destinations require
a revised candidate; the underlying architectural standing remains unchanged.

<a id="dr-3"></a>
## DR-3 — Textual architecture, composite documents and conceptual information

**Native-subject placements.** The ledger maps the Driver, Assessment, Goal,
Outcome, Stakeholder, Requirement, Constraint, Capability, CourseOfAction,
Resource, ValueStream, BusinessActor, BusinessRole, BusinessCollaboration,
BusinessInteraction and BusinessProcess sources to their exact native directories.
The Capability trio now has explicit destinations despite qualified catalogue
standing. Feature retains its governed Capability specialisation; no tier,
parent, structural Canonical ID or Home is completed from folder position.

Seven maintained information-family files go under `continuum/BusinessObject/`
with filenames `information-family-<original-name>.md`: device,
healthcare-location, healthcare-service, organisation, person-healthcare-subject,
practitioner and task-work. This classifies their primary conceptual information
subject, **not each family as one BusinessObject**. The projection's selected
native compatibility is not approval of every family assertion, nor permission
to equate BusinessObject, DataObject, FHIR Resource, Java class and table.
The four expressly G2-candidate families instead follow DR-5.

**Difficult cases now given preferred placements.** Paths in this table are
relative to `enterprise-architecture/`; full source and destination pairs are
in the ledger.

| Inspected source and actual content | Preferred intact destination and classification | Why this preserves meaning; analysed alternative |
| --- | --- | --- |
| Motivation `external-authorities.md`: explicitly says these are **not enterprise stakeholders**; describes external regulatory actors and constraint sources, including a statutory service and SDO group | `continuum/external-authorities.md`; cross-concept context for Constraint sources and external regulatory actors | Do not assign the entire file to Stakeholder or instantiate every listed body as a BusinessActor. BusinessActor is plausible for evidenced actor facets, but would overtype the mixed authority/service/source list without adjudication. No native object extraction is needed to preserve the context. |
| Strategy `health-service-assurance-derivation.md`: bounded approved Requirement→Capability derivation, EC contributions and explicit downstream deferrals | `continuum/health-service-assurance-derivation.md`; multi-concept relationship knowledge | Preserve approved matrix and exclusions together. Capability-only placement is plausible but obscures Motivation/relationship scope; no new contribution or allocation follows. |
| Strategy `logical-component-responsibilities.md`: responsibility tests, Mneme/Mnemosyne boundaries and strategic candidates | `continuum/strategic-logical-component-responsibilities.md`; cross-concept responsibility knowledge | Strategic responsibility is not proof of native ApplicationComponent identity. Preserve candidate and current distinctions; do not reify every logical label. |
| Business `behaviours/`: six content documents plus a rule/navigation index | Six `continuum/business-behaviour-<original-stem>.md` files; BusinessFunction/BusinessService/BusinessProcess in Capability/Feature and information context. The index's modelling rules go to `governance/capability-scoped-business-behaviour-rules.md`. | The source expressly prohibits disconnected global behaviour catalogues. Keep each content document intact, including Health Service Assurance's bounded approved services/consumers. Native-type extraction would need to carry owning context and exact relationships into every resulting unit, increasing semantic risk without migration benefit. |
| Business dependency matrix and information-responsibility document | `continuum/business-cross-capability-dependencies.md`; `continuum/business-information-responsibility.md` | Service consumption, capability responsibility and role/interaction boundaries remain assertions, not new Dependency/Ownership types. Keep approved and unestablished associations together and qualified. |
| Information metamodel, including InformationUnit and multidimensional semantic categories | `continuum/information-architecture-metamodel.md`; cross-concept architecture and BusinessObject-compatible conceptual meaning | Keep §§1–end together: independence rules explain the concepts, so moving the whole file to governance would hide architecture; extracting §2 or InformationUnit alone would sever its qualifications. The alternative is a separately reviewed concept/rule extraction, not automatic migration. |
| Information `authority-custody-provenance.md` and `information-lifecycle.md` | `continuum/information-authority-custody-provenance.md`; `continuum/information-lifecycle.md` | Runtime information authority, assertion granularity, provenance and non-universal lifecycles are architectural meaning. Repository governance placement would misclassify their subject. Preserve examples as examples. |
| Information `observable-information-and-domain-meaning.md` | `continuum/observable-information-and-domain-meaning.md` | Preserve observable information versus encapsulated meaning, governed Ergo execution and optional runtime AI distinctions. It is a substantive architecture boundary, despite its old `guardrails/` location. |
| Five Information patterns: containment/collections, definition/accountability, relationships, Metabolē and search results | Individually named root files `continuum/information-<original-name>.md` as enumerated in ledger | Preserve containment qualifications and conceptual/logical/physical mappings intact. No native Pattern, universal Composition, new Grouping or implementation mechanism is inferred. The exact Metabolē meaning is not reduced to one folder's type. |
| Information assemblies/views and Domain03 traceability | `continuum/information-assemblies-and-views.md`; `continuum/business-information-traceability.md` | Preserve conceptual assemblies, purpose-specific information views, candidate examples and responsibility traceability. Information View is not an ArchiMate presentation View. Existing approved explanation plus illustrative candidate §2 is a maintained architecture carrier, unlike an expressly pending G2 submission. |

**Orientation and rules.** Four domain READMEs, Motivation orientation,
requirements navigation, capability-map/strategic-view navigation and information
family navigation receive explicit `continuum/documentation/` filenames. This
retains substantive source context; it does not install the old numbered-domain
taxonomy as architecture. Predominant modelling rules go to governance (DR-4).

**Decision sought:** adjudicate the composite-carrier and current-catalogue
guidelines, the native classifications and these exact placements. The preferred
treatment requires no semantic rewriting or section extraction. Rejection of a
carrier location must not be interpreted as rejection of its established
architectural assertions. Questions about unresolved native identity inside a
carrier remain with their existing review; the migration does not resolve them.

<a id="dr-4"></a>
## DR-4 — Governance, modelling rules, identity and historical controls

**Preferred treatment.** Move the specialised governance contracts, Praxes,
mapping/inventory/coverage material, template and programme plan to their exact
ledger paths under `enterprise-architecture/governance/`. Retain their different
contract, plan, assessment, navigation and template roles. Move the **single
authoritative axiom source** to `governance/architectural-axioms.md`; keep the
repository/legacy compatibility entry in place and rebase live navigation only
under subsequent application authority. No duplicate axiom definitions or
ArchiMate Principle register is proposed.

Strategy's ICT foundation lenses, framework/derivation progression, Course of
Action qualification and Resource qualification, Business metamodel and
capability-scoped behaviour rules, and Information modelling guardrails are
predominantly modelling governance. Their exact renamed carrier paths are in the
ledger. Examples remain intact and do not become new Capabilities/Resources merely
because a rule mentions them. The Information metamodel and runtime information
governance take the distinct DR-3 treatment.

**The eleven-file identity bundle has been differentiated:**

| Files under existing `architecture-identity/` | Actual role and preferred treatment |
| --- | --- |
| `model.md` | Earlier identity model, with its recorded supersession/limits preserved; move intact, do not treat it as replacement for resource-owned Canonical IDs |
| `registry.json`, `registry.schema.json` | Established limited EC register/representation contract; preserve frozen scope and IDs, do not expand to a new all-architecture registry |
| `adoption-baseline-1.0.2.json`, `adoption-1.0.3.md` | Original adoption baseline and bounded adoption record; retain historical versions and assertions |
| `inventory.json`, `inventory.md` | Inventory/correspondence support, not proof that every possible match is canonical |
| `adoption-validation.json`, `bindings-validation.json`, `verification.json`, `release-report.md` | Original validation/release evidence with recorded scope; relocation does not rerun or refresh historical claims |

All eleven move to `governance/architecture-identity/<same filename>`. Keeping the
bundle intact is preferable to fabricating retrospective review/register/evidence
releases from its old dates. [Canonical identity/naming][naming] remains the
operative specialised authority; the ledger records per-file evidence rather
than inferring current authority from `registry` or `model` in a filename.

**Two governance collisions:** existing common `governance/README.md` and
`architecture-change-provenance.md` remain. Old governance README instead moves
to `governance/specialised-contracts.md`; the old, expressly superseded provenance
source moves to `governance/history/architecture-change-provenance-2026-10-20.md`.
That date identifies the source-era contract, not a guessed adoption/acceptance
event. Preserve its wrapper and original body. Merging either file into the
maintained common source would obscure scope and reviewed history.

**Decision sought:** confirm governance classification and retention of the
historical identity bundle. Renaming carriers and rebasing live links are
mechanical once the mapping is accepted; changing a contract's operative wording,
axiom meaning, identity regime or source precedence is outside migration authority.

<a id="dr-5"></a>
## DR-5 — Review/register placement, partial candidates and working material

**Preferred imported-record rule.** `migration-inputs/` is a carrier grouping in
this transition's lifecycle directories, not a new semantic class. The resulting
version `1.1.4` records relocation. Original decision dates, approval scope,
unknown historical release attribution and evidence roles remain in the sources.
No historical HCRL or formal Task timestamp is reconstructed.

| Exact source group | Preferred destination under `enterprise-architecture/design/` and reason |
| --- | --- |
| Motivation independent-assurance reconciliation; Strategy AX-05 reconciliation, five-region reconciliation and fixed-capability-decomposition decision; Information G1 review | Five exact ledger paths under `register/1.1.4/migration-inputs/<original domain>/reviews/`. Their bounded decisions/reconciliations are concluded/approved, including G1 CLOSED with explicit preserved uncertainty. A decision that closes its bounded scope may retain deferred consequences; that is different from an unapproved remainder of the submitted artefact. |
| Information Dokimasia finding, R1 investigation, G2 Block1 review | Three exact paths under `review/1.1.4/migration-inputs/04-information-architecture/reviews/`. The finding approves capture of name/boundaries, not complete placement; the investigation is recommendations/evidence, with later supersession notices; G2 is partially adjudicated. Do not promote them wholesale to register. |
| G2 `appointment-scheduling.md`, `episode-encounter.md`, `order.md`, `referral.md` | Four exact paths under `review/1.1.4/migration-inputs/information-architecture/information-families/`. Preserve G2-D01–D04 meaning and each file's remaining propositions. Semantic derivation grades A–D are not approval states. |
| Three Business reference-model files | `review/1.1.4/migration-inputs/business-reference-model/{README.md,business-architecture.json,business-architecture.schema.json}`. `capture_status: candidate` remains distinct from an element's `status: established`; the schema is representation machinery, not acceptance. |
| All 36 Domain05 files | `review/1.1.4/migration-inputs/information-systems-architecture/<unchanged relative suffix>`. Keep the correlated Candidate unit: 14 top-level files, 18 DesignNotes, one B1 review and three traceability files. A4 PASS accepts the **Candidate baseline for Step B**; B2.1 PASS settles one L1 and six L2 relationships; B2.2 is not commenced. This is not final whole-candidate approval. |
| Six scratchpad files | `review/1.1.4/migration-inputs/working/architecture-design-notes.md` and `working/capability-reference-candidate/<five original versioned TeX names>`. Preserve all working/review input intact. No notebook paragraph is incorporated into approved architecture through this mapping. |

**Domain05 interrogation and preferred intact treatment.** The ledger gives each
file its own content-derived evidence and exact destination. The following
distinctions matter for review; they must survive a whole-directory relocation:

- README, system map and component/feature registers document an accepted
  **candidate** baseline. `gaps.md` retains real deferrals; its word register does
  not confer approval. Data/persistence, middleware, context/Pragma, pathway/handoff,
  Pylai, Agora/Omilia, Calliope and Sylloge views carry bounded candidate
  adjudications, with packaging, deployment and detailed interfaces deferred.
- Agora, Identity, Calliope, Pylai, Energeia/Praxis, DigitalTwin/Dokimasia and
  Pragma-boundary DesignNotes retain their own working/adjudication/input status.
  The Class/Package note expressly records candidate direction for later
  classification. No Package or Class native type is invented by migration.
- Mneme, Mnemosyne, Petasos, Themis and Kleio notes describe Candidate Architecture
  responsibilities and design inputs. Both Kleio notes are retained independently:
  the longer “Resilient Capture” filename does not alone prove full supersession
  of the shorter note. Existing reconciled source references govern applicability.
- The Pathways/Journeys note expressly identifies conversational design input;
  the publication/subscription note is working discussion. Pragma Outputs and
  Digital Twin Behaviour notes record bounded U18/U19 incorporation. Their
  historic statements about A4 do not override later README/B1 standing.
- `sources.md` distinguishes current constraints, legacy inputs and U02–U19
  reconciliation. The B1 method/worked example, B2.1 relationship record and B1
  reconciliation are preserved with the candidate they explain. Moving their
  carrier under review does not revoke the approved method or B2.1 decisions.

**Extraction alternative.** Separately moving approved B2.1, method, U decisions
or G2 clauses into the register/continuum is plausible only with a clause-level
acceptance and correspondence decision. Preferred: preserve each entire source
and cross-references, retaining the existing bounded authority. This avoids
duplicating canonical statements or making candidate acceptance broader than
recorded. The 7,404-line working notebook is similarly a correlated working unit;
no requirement for a clean continuum justifies mining its conversations into
new architectural definitions.

**Decision sought:** confirm whole-record standing, five concluded-record
register placements, and intact candidate/working placements. The genuine
architectural decisions are placement/standing boundaries, not re-adjudication
of existing G1, A4, B1, B2.1 or U decisions.

<a id="dr-6"></a>
## DR-6 — Governed captures and distinct synchronised projections

The 26 baseline files are **not a homogeneous governance folder**. The maintained
README explains source/projection precedence, resource-owned identity,
record-specific standing and historical evidence. It records 946 recovered
records (716 Canonical, 227 Proposed, three Candidate), with 845 current,
69 pending-adjudication and 32 superseded, and 1,096 original relationship
assertions. The separate 214 historical reference objects are not current RAD
identity matches. The Information capture has 54 initial resources, 50
BusinessObject and four DataObject; this does not approve every neighbouring
concept or assertion. The accepted-ingestion captures own their persisted IDs;
no IDs are regenerated.

| Baseline sources, individually enumerated in ledger | Preferred destination relative to `enterprise-architecture/` | Treatment |
| --- | --- | --- |
| Original synchronised Business, Business Enabling and Enterprise Capability Markdown projections | `continuum/Capability/synchronised-business-capabilities.md`, `synchronised-business-enabling-capabilities.md`, `synchronised-enterprise-capabilities.md` | Keep distinct from original explanatory catalogues at unsuffixed filenames; preserve qualified current definitions, history and canonical identity |
| `features.md`, `functions.md`, `services.md`, `processes.md`, `products.md` | `continuum/Capability/synchronised-features.md`; `BusinessFunction/synchronised-functions.md`; `BusinessService/synchronised-services.md`; `BusinessProcess/synchronised-processes.md`; `Product/synchronised-products.md` | Projection placement reflects native subject; no new free-standing behaviour ownership or hierarchy |
| `architecture.json`, `information-architecture.json`, `information-architecture.md`, `motivation.json`, `motivation.md`, `relationships.md`, `business-homes.json`, `accepted-ingestion-1.0.8.json`, `accepted-ingestion-1.0.10.json`, `home-derived-metadata.json` | Ten exact same-basename files directly under `continuum/` | Intact multi-concept architecture/relationship/resource-metadata carriers, under DR-2; preserve Home versus Composition, accepted/held ingestion scope, resource-owned IDs/names and record-specific fields. Accepted-ingestion files carry actual elements/relationships/Views, not just a report of processing; home-derived metadata carries 212 resources' governed names. |
| `README.md`, `correspondence.json`, `coverage.md`, `discrepancies.json`, `documentation-classification.json`, `release-1.1.0.json`, `source-presentation-overlays.json`, compressed source-history metamodel | `governance/baseline/<unchanged suffix>` | Keep control/correspondence/presentation/history roles distinct. The release-1.1.0 historical control remains unchanged; it is not investigated or reconstructed as a new HCRL. |

**Why preserve the captures intact:** splitting `architecture.json` by native
type would change its representation contract and cross-key semantics, introduce
multiple current carrier authorities and complicate fixed-identity validation.
Historical references, current resource metadata and provenance overlays already
have distinct fields/roles. Keep those distinctions rather than stripping history
out for visual tidiness. Likewise do not remove source-era assertions from an
explanatory catalogue merely because the synchronised projection qualifies them.

**Preferred dependency resolution:** replace machinery's assumption that one
`RAD` directory holds every capture/projection with an explicit source/output
path table from this reviewed mapping. Current live lookups use the new table;
frozen source locators resolve through the historical locator mechanism (DR-10).
Never reassign key/ID, regenerate names, change status/currentness or flatten
specialisations to accommodate paths. An alternative is keeping the entire
baseline bundle together; this is operationally simpler but leaves current
architectural captures under governance or creates an independent baseline
taxonomy in the continuum. The proposed split is preferable if its carrier
guideline is accepted and consumers are adapted as one bounded change.

**Decision sought:** approve/correct the ten composite carriers, eight native
projections and eight control/history placements, and preserve the distinct
explanatory/projection roles. Exact path-table implementation is mechanical;
changing the semantic capture is outside migration authority.

<a id="dr-7"></a>
## DR-7 — Specialised ArchiMate workspace and maintained-input dependencies

**Preferred mapping:** all 1,830 tracked sources under `docs/archimate/` move to
`enterprise-architecture/design/archimate/<unchanged suffix>`. This preserves the
established strategy, not just XML exports:

| Source component | Files | Preserved role |
| --- | ---: | --- |
| Workspace README | 1 | Orientation and specialised boundaries |
| `framework/` | 89 | Maintained machinery/configuration, schemas, baselines and declared tests/fixtures; recovery helpers retain specialised roles |
| `exchange/publish/`; `exchange/ingest/` | 8; 7 | Exact governed release/interchange versus raw incoming exports; direction/case/version preserved |
| `processing/publishing/`; `processing/ingestion/`; `processing/machinery-validation/` | 543; 238; 904 | Purpose-preserved processing/history, candidate runs, diagnostics, inputs/outputs and audit/recovery dependencies; no blanket evidence/approval |
| Processing README | 1 | Processing retention/role contract |
| `governance-tooling/` | 39 | Explicit non-RAD 1.0.14 evidence and namespace-migration history, retained outside release publication namespaces |

**Boundary evidence:** `framework/tools/workspace_paths.py` supplies workspace
paths; `historical_path()` resolves **exact registered** file/directory entries,
not arbitrary recursive aliases. `framework/historical-locations.json` preserves
older capability-workspace and non-RAD relocations. `governance_history.follows()`
validates registered transition manifests and compressed before-snapshot hashes.
These are locator/hash chains, not architectural identity correspondence.

**Preferred current-versus-historical dependency treatments:**

| Dependency actually present | Preferred resolution and exact boundary | Alternative / limit |
| --- | --- | --- |
| `workspace_paths.py` uses `ROOT = FRAMEWORK.parents[2]`; framework Makefile assumes repository `../../..`; scripts contain old Markdown/ArchiMate paths | Adapt maintained root discovery and explicit path configuration for the additional `enterprise-architecture/design/` depth. Replace live capture/output lookups using the DR-6 map, and adapt schema, documentation-map, publication and validation consumers. | Blind prefix replacement misses parent-depth assumptions and split capture paths. Keeping symlinks would conceal broken configuration. |
| Round-trip fixture manifest reads two raw `.junie` exports | Add complete-byte copies: `.junie/reports/2026-10-18-archimate-roundtrip-increment-1/archi-exports/release-1.0.6.xml` → `enterprise-architecture/design/archimate/framework/tests/fixtures/roundtrip/raw-release-1.0.6.xml`; sibling `future-contract.xml` → `…/roundtrip/raw-future-contract.xml`. Retain originals in place; update the **maintained** fixture manifest to reference declared copies, preserve source provenance and verify hashes. | Keep tests reading `.junie`: shareable clone works while history remains, but current tests continue depending on an agent workspace. Copying all `.junie` history is unnecessary. |
| Information fixture control is a compressed no-edit export in processing | Complete-byte copy of `docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-no-edit-export.xml.gz` → `enterprise-architecture/design/archimate/framework/tests/fixtures/roundtrip/information-control-no-edit-export.xml.gz`. Primary source moves with workspace. Preserve compressed SHA-256 `1e2ecfb5585448c84eb009d22c9d1f7794d8e452f6d841865e08d675526aca8d` and uncompressed XML SHA-256 `ac7fad3d30b230e894fa12e64e74e77939fc6574dd3336a8ddd7569f70c22b65`; update maintained fixture pointer only. | Decompressing/re-exporting would change pinned transport bytes. This is a fixture copy, not historical pilot reconciliation. |
| Historical fixture baselines reference old capability-workspace paths; Information fixture references exchange release/incoming 1.0.10 | Resolve to the exact moved governed release/input, preserve the original path/hash provenance and retain release/raw-input distinction. The whole governed release is a valid declared input; do not regenerate it. | Replacing the baseline with newest publication would change the test's meaning. |
| Current baseline/identity validation loads synchronisation/establishment and recovery evidence through `workspace_paths` constants and `governed_metadata`; frozen governance audit also runs a processing `verify-execution.py` | Separate current validation from historical replay: current resource/identity checks should consume maintained captures, explicit current authority and declared fixtures; historical snapshot reconstruction/hash-chain and namespace-reclassification replay remain an explicitly historical check using preserved inputs. Keep every existing source/executable. The **preferred authorisation boundary** is a separately reviewed maintained-validator change, not moving frozen executables into current framework or weakening hashes. | A pure relocation can preserve the historical replay dependency through locators, but cannot claim the provenance contract's required operational end state. Exact replacement algorithms/fixtures for this larger validator separation are not established here; DR-11 makes that limitation explicit. |
| `information_workspace_run.py` freezes a `.junie` report, all run-001 files and release 1.0.10; run-local algorithms require that old release | Retain as historical/machinery-run reproduction. Do not execute it as current 1.1.4 generation or relax its release guard. Its historical inputs follow explicit locators, with original fingerprints unchanged. | Making it a current publisher would reinterpret a proving run; that requires separate design. |
| `verify_governance_evidence.py` validates the old namespace migration's paths/hashes and protected-before claims | Preserve original manifests/claims; make maintained historical lookup resolve original paths/link bases through the new locator layer. Continue rejection of governance-tooling identifier 1.0.14 as a RAD release. | Editing historical manifest paths to look current destroys the evidence it verifies. |

The three `CopyFixture` rows select **entire files**; there are no partial XML
node extractions or new architecture assertions. A new fixture provenance
manifest may describe source locator/hash and purpose, but must not overwrite the
original manifest or masquerade as an accepted architecture release.

**Decision sought:** approve intact specialised relocation and the three declared
fixture copies; decide whether validator dependency separation is a prerequisite
to the user-intended stable baseline or a separately governed follow-on.
**Codex prefers it as a prerequisite to claiming that baseline achieved.**
The mapping itself remains concrete; no current-generation success is claimed.
The historical 1.0.10→1.1.0 pilot is not investigated or reopened. Reading path
dependencies supplies preservation evidence only, not retrospective acceptance.

<a id="dr-8"></a>
## DR-8 — Draft publication packages and distribution standing

**Preferred mapping:** all 134 tracked `docs/latex-rad/` files →
`enterprise-architecture/continuum/documentation/published/<unchanged suffix>`.
This includes the package-level ignore file, 56 v0.1-draft files and 77 v0.2-draft
files. The ledger distinguishes editable TeX/style/editor configuration, rendered
PDF, authored catalogue, model/identity locks, reviewed inputs, build/generation
tools and validation evidence. These roles are not architectural approval states.

**Evidence and mixed standing:** v0.2's maintained README explicitly says the
human-reviewed BEC candidate hierarchy remains Proposed and does not amend RADS.
It preserves 862 prior identities, 719 v0.1 elements, reference IDs/LaTeX labels,
432 historical aliases, current/superseded/pending records and explicit
adjudication tensions. The 1,531-page PDF, 946-record model and authored catalogue
are correlated publication representations; published prose does not resolve
their pending matters. `pre-review-*`, identity locks, source-coverage and
reconciliation ledgers preserve their actual historical scope. Do not replace
them with fresh PASS reports after relocation.

**Why intact is preferred:** v0.2 has first-class editable master/section sources
and conventional standalone compilation; its optional regeneration/validation
also depends on v0.1 transcriber, reviewed inputs, canonical sources and historical
fingerprints. Keeping the full package preserves both use modes and the
source/model/review correspondence. Do not split §§/catalogues into architecture
as a migration operation, or treat optional regeneration as authority to overwrite
editable source. No rendered output is regenerated in candidate preparation.

**Analysed alternatives:** placing the entire two packages at
`enterprise-architecture/design/review/1.1.4/migration-inputs/publications/Harmonia-EA-Capability-Map/<version>/`
would strictly foreground their draft status and is a defensible alternative if
SA construes `published/` as requiring whole-content approval. It is less useful
for stable distribution of the already produced editable publication. Splitting
TeX/PDF into published and JSON/review/tooling into design would require a new
multi-root package contract and adaptation of relative imports, v0.1 fingerprints
and standalone checks, with no semantic gain. Preferred: adopt the distinction
**publication role ≠ whole architectural acceptance**, preserve existing draft
names/notices, and keep package-local support visibly package-local.

**Decision sought:** adjudicate that published-role interpretation and the intact
package mapping. Existing substantive publication tensions (including assurance,
credentials, communication, specimen/materiel wording and unresolved framing)
retain their existing review standing; this transition does not adjudicate them.
Legacy LaTeX/ODT packages remain in place under DR-9, not mixed into this target.

<a id="dr-9"></a>
## DR-9 — Retained engineering, accepted ADRs, legacy, OpenSpec and agent history

**Preferred treatment:** every source in this DR has **Remain → its exact
current path** in the ledger, with per-file content evidence and repository role.
This creates a bounded EA migration while preserving the wider technical
documentation corpus and its live use. It is not an assertion that every retained
document is obsolete or unapproved architecture.

| Analysed content | Preferred classification/treatment and reason | Alternative and why not preferred |
| --- | --- | --- |
| `docs/architecture-decisions.md`, ADR-001–020 | Preserve complete decision anthology. Domain05 `sources.md` L13 explicitly preserves accepted ADR-018–020's state/lifecycle governance role and says no decision is rescinded; ADR-013–017 descriptions have bounded supersession. The ledger preserves actual accepted/superseded distinctions. | Move whole anthology to governance or split accepted ADRs into register: possible later, but would mix old/partial/reconciled meanings with changed carrier authority. No need to split or demote accepted decisions for this migration. |
| `docs/design/governed-write-concurrency-contract.md` | Preserve the engineering contract intact at its exact path. Domain05 L14 records partial incorporation: §§10.2/14.2 superseded; exact Java signatures, CAS/reconciliation algorithms and other engineering detail retain their role. | Incorporate the entire file into continuum: would promote engineering detail and superseded formulations. Extracting the already superseded clauses duplicates reconciled meaning; keep existing wrappers unchanged instead. |
| `docs/implementation/harmonia-convergence-runtime-integration-plan.md` | Assigned implementation sequence remains operative within its role, subordinate to architecture. Migration does not initiate runtime implementation or alter milestones. | Move into architecture governance merely because it has programme significance: unnecessary and risks conflating sequencing with architecture authority. |
| `docs/deferred-document-register.md` | Preserve recovered/deferred/historical/resolved entries intact; recovery of names/metamodel knowledge does not itself confer new architecture. | Mine “recovered decisions” into canonical sources: requires reconciliation/adjudication, beyond migration. |
| `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx` | ZIP/XML content inspection identifies 178 paragraphs, strategic position/baseline self-description dated 24 September 2026, and Local Directory's broader Harmonia context. Remain as preserved source manuscript, not an inferred current RADS baseline. | Incorporate into Strategy: plausible only with attributable current adoption/reconciliation evidence, absent here. Remaining in place is defensible without resolving adoption. |
| `docs/mindmaps/Harmonia-Capability-Architecture-v0.2.mm` | 676-node Capability/Feature/Function/Service/Process **working view**. Preserve source/labels/geometry in place; identity cannot be inferred from mindmap position. | Treat it as an accepted architectural model or automatically reconcile by names: unsupported. |
| Remaining technical guides and module/deployment docs | Each has a content-derived title/evidence and a purpose role: architecture/convergence descriptions, concepts, configuration, deployment, getting-started, integration, middleware, modules, operations, Paradeigma, Provider Registry, security, backlog, reference registers or module engineering. Remain in original context; preserve all bounded supersession wrappers and unchanged original text. | Bulk move to continuum would launder legacy descriptions into current authority. Bulk move to a history directory would demote useful/current operational detail. Section reconciliation belongs to a separate semantic change. |
| 101 `docs/latex-legacy/` files; 21 `docs/libreoffice/` files | Preserve legacy technical publication sources, diagrams, image assets, compiled/build products and generators intact. ODT XML describes a September-2026 technical architecture publication; LaTeX→ODT generation is a live packaging dependency, not proof of current architectural acceptance. | Move outputs alone to published: breaks source/tool relationship and suggests current EA standing. Split old chapter copies by filename: no safe supersession inference. |
| 424 OpenSpec files | Keep instructions/project/manifest and **421 discrete use-case specs**. Manifest paths, source-use-case references and titles agree with all specs; inspected structure contains 1,657 Requirements and 4,928 Scenarios. Normativity remains within assigned implementation scope. Preserve intentional cross-use-case duplication and open questions. | Treat each spec as a native Capability/Requirement in EA: changes semantics/identity. Merge duplications: explicitly requires architecture decision under OpenSpec project rules. |
| 411 `.junie/` files | 117 plans, 294 reports/support. Preserve historical/working plans, reports, raw exports, validation material and run-local executables in place. Statements remain attributable source claims; location/report completion gives no acceptance. Three exact fixture-copy proposals are in DR-7 (two from this set). | Bulk import into formal evidence would fabricate retrospective compliance; bulk disposal would lose live dependencies and unique history. Formal Task/Report convention is prospective from RAD-1.1.3. |

**Repository instructions/navigation:** active `AGENTS.md`, scoped instructions,
root README/release authority and compatibility entries remain at their current
paths; `AGENTS-old.md` remains historical, not active instruction authority.
The five existing common governance files, eight earlier pending HCRLs, two
completed HCRLs and six formal execution records also remain exactly where they
are. Completed HCRLs remain immutable. None is migrated just to make the tree
look symmetric.

**Decision sought:** confirm the explicit retained scope and authority boundaries,
especially accepted ADRs and engineering contracts. Ordinary Remain choices are
document-management decisions; promoting/incorporating legacy architectural
meaning would be a new architectural decision requiring different authority.

<a id="dr-10"></a>
## DR-10 — Collision resolutions, reference continuity and existing validation failure

**All five known path collisions have preferred resolutions:**

| Collision | Preferred resolution | Alternative rejected for this candidate |
| --- | --- | --- |
| Original versus synchronised Business Capability catalogue | Original → `continuum/Capability/business-capabilities.md`; projection → `continuum/Capability/synchronised-business-capabilities.md` | Merging, deduplicating or choosing by newest timestamp loses qualified source/projection roles |
| Original versus synchronised Business Enabling Capability catalogue | Original → `continuum/Capability/business-enabling-capabilities.md`; projection → `continuum/Capability/synchronised-business-enabling-capabilities.md` | Same; neither carrier is globally replaceable by the other |
| Original versus synchronised Enterprise Capability catalogue | Original → `continuum/Capability/enterprise-capabilities.md`; projection → `continuum/Capability/synchronised-enterprise-capabilities.md` | Same; canonical ID continuity is not filename identity |
| Old versus maintained governance README | Old → `governance/specialised-contracts.md`; maintained README remains | Replacing maintained common governance navigation would obscure adoption scope |
| Superseded versus maintained provenance contract | Old → `governance/history/architecture-change-provenance-2026-10-20.md`; maintained source remains | Merging/replacing would erase explicit supersession and reviewed meaning |

All paths in this table are relative to `enterprise-architecture/`. The complete
ledger has unique primary destinations and three distinct non-colliding fixture
destinations. Architectural name tensions inside the publication/capture remain
explicit architectural review matters; a carrier rename cannot solve them.

**Reference continuity proposal.** The reference CSV aggregates repeated exact
source-path mentions and Markdown links by consumer/target/kind, recording first
source line, count, current and proposed paths, and preferred live/historical
treatment. It is lexical impact evidence, **not proof of executable reachability,
architectural correspondence or exhaustive dynamic path coverage**. Generated
catalogues account for many repeated occurrences. Relative links, JSON paths,
source fingerprints, templates, Makefiles, scripts, tests and source-derived
metadata need different treatment:

- Maintained navigation/code/configuration: rebase operational paths against the
  accepted mapping, including code-generated links, all split capture/output
  paths, schema references and repository-root depth. Retained root scripts
  `architecture_identity.py`, `architecture_release.py`,
  `rads_business_architecture.py` and their tests stay at their current paths;
  only their affected maintained consumers/configuration need adaptation.
- Repository authority declarations: update `AGENTS.md` §1.2 and current
  governance/Praxis navigation to describe the **accepted finite migration**
  instead of saying that all RADS remains under `/docs/markdown` awaiting it.
  Existing governed meaning follows its mapped carrier, including bounded
  adjudicated Candidate Architecture now under review. Do not replace that
  declaration with a claim that every continuum sentence is approved or that
  every file outside it loses existing assigned authority. This is a material
  source-boundary clarification for SA approval, not an automatic text replacement.
- Frozen review records, completed HCRLs, formal Task/Reports, captured source
  line locators, original hash keys and historical publication locks: preserve
  exact bytes and original link-base meaning. Add explicit old-path→new-path
  lookup from the accepted ledger in maintained machinery; compose the older
  exact historical mappings to the new destinations without guessing a recursive
  rule. Keep collision cases one-to-one by original source path. A relocation
  manifest must distinguish before/after hashes and real link edits from semantic
  edits. The current HCRL/ledger supplies review correspondence, not a replacement
  for governed source identity.
- Publication regeneration: adjust live repository/root/source consumers while
  preserving frozen v0.1 fingerprints and historical source assertions. Recheck
  ordinary editable-source/standalone compilation separately from optional
  regeneration. Legacy LaTeX→ODT paths stay valid because both bundles remain.
- Historical Markdown hyperlinks cannot all be made clickable by changing current
  code alone. Preserve originals, expose a small migration locator/navigation
  aid generated from the approved path map, and have maintained report/render
  tooling resolve original file plus original link base. Such an aid is path
  navigation, not a new architectural master index. Alternative: per-file stubs
  at old paths; less preferable because they keep the old tree as competing
  navigation and cannot safely substitute for pinned source bytes.

**Material existing failure.** A bounded, read-only check of the **unmodified
current source** ran:

```text
python3 -B docs/archimate/framework/tools/governed_metadata.py validate
exit 1: INVALID: Current baseline authority drift; reconcile explicitly: AGENTS.md
```

`baseline/correspondence.json`'s `current_authority_sha256` includes the earlier
`AGENTS.md` fingerprint `df693d4b012f8b94e595641f0e04494a683e6382e771b5398d730ecaee685411`.
The present instructions/common governance are at the accepted 1.1.3 baseline.
This failure was observed before source changes; it is not evidence of migration
damage. The validator stops at its first failure, so later checks are not claimed
to pass or to have no further drift.

**Preferred resolution:** expressly review the live-authority fingerprint chain
against the accepted 1.1.2/1.1.3 contracts/completed HCRLs, identify changed live
authorities and their bounded effects, preserve old snapshots/pins, then establish
new **current** authority metadata through a reviewed change. Carry forward
historical assertions using their validated original hash chains and locators.
Do not turn off validation, blanket-refresh hashes, weaken historical comparisons
or falsely certify the old pin as matching. Path relocation alone cannot fix a
content fingerprint mismatch. This candidate proposes the treatment, not the
substantive reconciliation or new metadata values.

**Decision sought:** confirm the five concrete collision resolutions, historical
lookup versus live-link policy and bounded scope of current-authority
reconciliation. The strategy is material; exact accepted-map application and
non-semantic navigation repairs are mechanical. Any newly discovered semantic
drift returns to its DR before dependent implementation.

<a id="dr-11"></a>
## DR-11 — Faithful application boundary, missing evidence and acceptance gates

**No source lacks a defensible proposed disposition.** Unestablished architecture
is preserved with its recorded standing. The remaining missing evidence limits
stronger claims or a later implementation plan; it does not leave an unclassified
source bucket.

| Evidence/authority not established | Preferred treatment now | What specifically remains necessary |
| --- | --- | --- |
| A generally adopted rule for intact multi-native carriers and current catalogues with assertion-specific standing | Exact DR-2/3/6 placements are preferred candidates, with alternatives analysed | SA adjudication of the carrier/partial-approval distinction; cannot infer it from folders or successful generation |
| Whole acceptance of G2 families, Domain05 candidate, working notes or publication proposals | Keep partial candidates in review and draft publication standing explicit | An attributable future whole-scope decision would be needed to promote them; migration does not need or request that promotion |
| Current adoption/reconciliation of the strategic DOCX and arbitrary legacy architectural statements | Remain exact paths as preserved source/technical material | The relevant reviewed adoption/reconciliation record and clause-level correspondence, if incorporation is later requested; source-era self-description alone is insufficient |
| Original `HIE - Target State - Use Cases - v0.5.docx` used by OpenSpec | All 421 specs and manifest remain with their assigned role | Source DOCX is not present in this tracked corpus. Translation fidelity to that original cannot be independently established here; the tracked manifest/spec agreement is verified. No request to reconstruct missing source wording. |
| Complete replacement design for current baseline validation's recovery/processing dependency | Preserve existing whole workspace; perform the three precisely bounded fixture copies only after approval; treat remaining dependency detachment as a material prerequisite to the intended operational end state | A reviewed separation of maintained-current validation from historical replay, declaring exact replacement inputs/fixtures and invariant checks. Static path archaeology establishes the dependency, but not semantic equivalence of an unimplemented replacement algorithm. This is the one engineering boundary this candidate deliberately does not pretend is implementation-ready. |
| All downstream failures after the first live-authority mismatch | Preserve failure and propose reviewed current-authority reconciliation | After authorised correction, run the full maintained validation chain and investigate any further failures. A first-failure run cannot establish that only one pin is stale. |
| New-location standalone publication, regeneration and human Archi transport | No source moves/build/regeneration performed in this preparation | Post-application checks in an isolated clean clone/package, using declared dependencies; specialised actual import/export/inspection checkpoints remain separate from unit tests. |

**Recommended coherent authorisation boundary:** accept/correct the classification
guidelines and exact mapping first. Authorise migration only with the live-path,
historical-locator and current-authority treatments explicit. Preserve every
original source and its semantic payload; only specifically authorised mechanical
navigation/configuration edits accompany relocation. Keep fixture copies
purpose-bounded. Do not include legacy incorporation, semantic cleanup,
reclassification of pending relationships, deletion of processing/cache/build
material or retrospective pilot completion.

For the stable, reproducible, shareable **operational** baseline requested by the
user, Codex recommends resolving current-authority drift and the maintained
validator's historical-processing dependency before claiming that objective
achieved. A pure relocation may be faithfully bounded and reviewable while those
dependencies are retained, but must be reported as such. SA may authorise a staged
transition; an explicit deferral does not make the operational end-state claim
true. No decision is requested to resolve all existing architecture gaps merely
to relocate their carriers.

**Future application checks, after a separate Apply instruction:** recheck HEAD,
all source hashes and 1.1.3 immediate predecessor; apply only the actual SA
dispositions; preserve review history/conditions; retain formal timestamped
application Task/Report provenance; allocate the single 1.1.4 patch increment
within the accepted change set. Verify full map/collision/reference reconciliation,
semantic/ID/status/relationship preservation, historical snapshot/hash-chain replay,
maintained current-source validation, architecture tests, specialised machinery
tests and editable/standalone publication modes proportionately. Establish
reproducibility from clean declared inputs, not local `/tmp`, agent workspace or
undocumented cached state. No completed HCRL is created until SA confirms both
authorised meaning and actual incorporated result.

**Decision sought:** decide the operational prerequisite/staging boundary and
the listed material carrier/standing choices. The candidate is complete as a
source-disposition proposal; the validator separation is explicitly a further
bounded design task, not unanalysed repository classification delegated to SA.

<a id="dr-12"></a>
## DR-12 — Bounded Minutiae: candidate preparation and verification

**Disclosed group:** creation of this iteration and its three supporting review
files; source enumeration/hashing/content/structure inspection; read-only
dependency investigation; CSV/JSON reconciliation and link/DR checks; required
bounded architecture test execution. These support DR-1–DR-11 and do not approve
their substantive proposals.

**Inspection limits:** individual text/JSON/XML sources were inspected for
content/structure and source-specific evidence, supplemented by deeper governed
context for the mixed cases discussed above. Gzip payloads and DOCX/ODT XML were
decoded read-only. Binary figures, PDF outputs and Python caches were examined by
container/package role, fingerprint and associated source; this does not claim
visual review of every PDF page or independent validation of every historical
log/outcome. The ledger records these inspection distinctions. The source SHA-256
covers original bytes, including compressed files. Archived members are not
double-counted as source files.

Ten empty or whitespace-only decoded artefacts, including one agent-report
placeholder and nine logs, have explicit empty-payload treatment. They remain
counted and preserved; an empty log supplies no PASS or architectural decision.

**Verification facts:**

- Exactly **3,320 tracked inputs + one prior candidate = 3,321 rows**; all have
  explicit nonempty dispositions/destinations, separate role/classification,
  source fingerprint and DR reference. The source partition reconciles to
  iteration 001; zero missing/duplicate sources or destination collisions.
- All 3,320 original tracked source hashes and the prior-candidate hash are
  unchanged. Release authority remains 1.1.3; no migration, source rename,
  architectural extraction, rewrite or deletion was performed.
- OpenSpec manifest reconciles exactly to all 421 spec paths, titles and source
  references. Requirement/scenario counts are structure inspection, not proof
  that all scenarios have been implemented.
- The repository-required architecture suite passed: **90 tests in 11 classes,
  zero failures/errors/skips**, Maven BUILD SUCCESS, 17.478 seconds, with a
  240-second command bound. This verifies existing implementation guardrails;
  it cannot verify proposed moved paths.
- The read-only maintained metadata validation failed on existing authority
  drift as recorded in DR-10. No current-generation or migrated-state PASS is
  asserted. Generating `make all`, historical run reproduction and publication
  regeneration were not used as investigation steps because they write source
  outputs or rely on a different historical baseline.

The [check reconciliation][checks] records the commands/results and exact
inventory counts. Temporary local logs are not required future inputs or formal
Task Reports. Detailed candidate preparation does not create a new formal
review Task/Report convention; material findings are in the substantive DRs.

**Minutiae-Accepted:** Not recorded (Solution Architect to set Yes or No)

**Comment:** —

[previous]: hcrl-1.1.4-001.pending.md
[ledger]: ../../evidence/1.1.4/002/after/hcrl-1.1.4-002.mapping.csv
[references]: ../../evidence/1.1.4/002/after/hcrl-1.1.4-002.references.csv.gz
[checks]: ../../evidence/1.1.4/002/after/hcrl-1.1.4-002.checks.json
[governance]: ../../../governance/architecture-governance.md
[praxis]: ../../../governance/architecture-review-praxis.md
[provenance]: ../../../governance/architecture-change-provenance.md
[completed]: ../../register/1.1.3/hcrl-1.1.3-005.complete.md
[agents]: ../../../../AGENTS.md
[axioms]: ../../../../docs/markdown/governance/architectural-axioms.md
[release-policy]: ../../../../docs/markdown/governance/architecture-release-policy.md
[modelling]: ../../../../docs/markdown/governance/architecture-modelling-strategy.md
[mapping]: ../../../../docs/markdown/governance/rad-archimate-model-mapping.md
[mapping-catalogue]: ../../../../docs/markdown/governance/rad-archimate-mapping-catalogue.md
[naming]: ../../../../docs/markdown/governance/canonical-identity-and-naming.md
[roundtrip]: ../../../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
