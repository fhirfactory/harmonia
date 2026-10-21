<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Whole rejection of 001 and revised application authority | Rejected 001 as a whole; establish and apply the revised strategy | Operative Task, 2026-10-21T09:27:25.083Z; resulting architecture awaits holistic SA/ARB review. |
| [DR-2](#dr-2) | Element representation, identity, standing and incorporation Praxis | Establishment and application authorised by Task | Applied representation and checks below; holistic acceptance pending. |
| [DR-3](#dr-3) | RAD ingestion, reconciliation and source retirement | Application authorised by Task | Preserve authority and qualified assertions; defer only affected matters requiring new architectural decisions. |
| [DR-4](#dr-4) | ArchiMate, publication and reproducible machinery | Bounded extensions authorised by Task | Preserve projection/workbench roles and actual verification limits. |
| [DR-5](#dr-5) | Unresolved architectural questions and result acceptance | ARB review in progress; substantial residuals adjudicated below | Migration uncertainty is preserved, but the Solution Architect/ARB now supplies authorised semantic reconciliation rules. |
| [DR-6](#dr-6) | Bounded Minutiae and formal activity evidence | Not recorded | Supporting edits and formal evidence disclosed below. |

# HCRL — RAD-1.1.5 element-oriented Enterprise Architecture Continuum

**Standing:** authorised implementation applied; submitted for holistic review, iteration **1.1.5-002**, transition
**RAD-1.1.4 → RAD-1.1.5**. Establishment and application are explicitly authorised;
holistic review of the incorporated result remains with Mark Hunter, Solution
Architect, and the ARB. Generation and validation confer no architectural approval.

The [operative Task](../../evidence/1.1.5/002/before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md)
retains the full issued instruction and its attributable event time. Entry HEAD
is `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`, branch `ArchitecturalGovernance`,
entry root release 1.1.4 / previous 1.1.3; resulting root release 1.1.5 / previous 1.1.4. Prior iteration 001 and its evidence were
untracked inputs at entry and are preserved explicitly.

<a id="dr-1"></a>
## DR-1 — Whole rejection and revised authority

The Solution Architect rejects [1.1.5-001](hcrl-1.1.5-001.pending.md) **as a whole**.
Its investigation is not rejected; its premise that historical RAD documents
should generally remain architectural carriers is rejected. The former DR-1
acceptance and unanswered DR-2–DR-13 remain decision history, not operative
migration directives. No individual adjudication of those proposals is performed.

The new unit of continuum representation is the individual architectural element,
classified by the governed native ArchiMate metamodel and Harmonia specialisations.
The Task authorises establishing a deterministic representation and machinery,
then applying it to real RAD content, preserving identity, standing, meaningful
provenance and explicit uncertainty. It authorises retirement of successfully
ingested source carriers and bounded supporting governance/machinery changes.

AX-03/AX-04 retain native semantics and architecture's authority over machinery.
AX-08/AX-09 distinguish durable transformation evidence from transient processing.
AX-14 preserves identity roles, assertion standing, source history and projection
versus approval. AX-15 preserves unestablished outcomes; AX-17 limits deferral to
affected items requiring missing architectural authority. The change supplies no
new runtime boundary or metamodel concept. Existing naming and containment rules
control; paths and labels confer neither identity nor semantic relationships.

<a id="dr-2"></a>
## DR-2 — Canonical element representation and Praxis

The following initial migration figures are retained as the reviewed entry
snapshot. The authorised ARB follow-up and current population are recorded below.

**Applied under the operative Task; result acceptance pending.** The continuum is
one resource-owned UUID32 and one Markdown file per architectural element. Its
native classification selects a deterministic directory from the governed
ArchiMate type catalogue; Feature specialises Capability and Topic explicitly
specialises DataObject. Home retains its directed Association meaning and is
represented as an assertion, not an invented element. Grouping and Location are
native types, not document-aggregation categories.

The [representation contract](../../../governance/continuum-representation.md),
[incorporation Praxis](../../../governance/continuum-incorporation-praxis.md),
[base template and type catalogue](../../../governance/continuum/), and JSON schema
implement identification → classification → representation → identity → content →
provenance/standing → placement → validation. The catalogue covers **60 native
types**, of which **26** occur in the migrated population. One common Markdown
representation with native/specialisation constraints avoids a parallel taxonomy.

There are **1,559 canonical elements**, **1,137 native relationships**, and **462
qualified source predicates** retained without coercion to native edges. All
1,559 elements have provenance. **1,394 canonical identities are preserved**;
**165 are newly allocated** through bounded authorised ingestion. Three erroneous
unaccepted Role-attribute allocations were withdrawn and are retained as non-reuse
evidence. They are not fabricated historical architectural elements. The 50
accepted evolved source records retain the same canonical identities.

Existing naming/identity rules control. The governed native Name = displayName
rule corrects **841** differing prior native Names, with prior values retained.
Naming does not infer identity or acceptance. **164 canonicalName values remain
explicitly unresolved** for missing formal vocabulary, and **223 source records
have no established original definition**; historical absence is not filled by
inventing meaning. Currentness is independent of semantic standing: 1,158 current,
69 pending adjudication, 300 historical-reference and 32 superseded records.
[Detailed validation and exact type/standing counts](../../evidence/1.1.5/002/after/continuum-validation.json)
retain the source's full qualified standing text.

### Applied ARB follow-up — continuity and assertion representation

The Solution Architect's [Apply Task](../../evidence/1.1.5/002/before/ai-agent-tasks/20261021T114345Z-apply-arb-semantic-reconciliation.md)
authorises the recorded ARB rules within this same pending release transition.
Entry HEAD is `798344f80eb7fd3998e17f4f040cf7dabe260388`. AX-03/AX-04 require native
semantics and architecture-led machinery; AX-14 separates identity, lineage,
classification, representation and standing; AX-08/AX-09 govern selected evidence;
AX-15/AX-17 preserve conditional assertions and genuine uncertainty.

All **214 historical Capability references** now have explicit correspondence:
**209 direct, five evolved, zero related-only and zero no-counterpart**. Historical
UUIDs/representation IDs remain reserved in superseded resources, with reciprocal
successor/predecessor lineage. The current counterpart retains its independent
standing. The HIE Enablement predecessor is distinguished from the separate
Connected Health Services HIE capability; Care Intent Appropriateness evolves to
Care Appropriateness Review. The **223 missing original definitions** remain absent.

The result contains **1,602 elements**, **1,651 native relationships** and all
**462 original qualified predicates**. The **43 new element identities** represent
33 Constraints, nine Requirements and one **ImplementationGuideline**, the
authorised CourseOfAction specialisation. Its Keycloak/PostgreSQL direction is
bounded to development/test/POC, with an external replaceable IdP and no
product-specific architectural coupling. No qualifier is invented for its formal
canonicalName: **165 canonicalNames remain unresolved**. All 26 previously
populated native types remain supported; currentness is **1,201 current, 69 pending
adjudication, 86 historical-reference and 246 superseded**. This does not promote
source-level Candidate or Proposed semantics to approved architecture.

The representation/schema/type catalogue and incorporation Praxis record the
ImplementationGuideline direction, source-qualified native predicates, reserved
lineage and assertion-level DesignNote processing. Package→Grouping remains the
note's agreed metamodel direction without manufacturing concrete Package
instances; Class native mapping and the four named historical DataObject
classifications remain individually deferred.

<a id="dr-3"></a>
## DR-3 — Ingestion, reconciliation and source retirement

**Applied to the actual RAD corpus.** Exact canonicalId continuity reconciles the
maintained captures and accepted ingestion records; document position, name
similarity and co-location supply no instance correspondence. Additional bounded
native definitions include strategic constructs, Business Roles/Interactions/
Collaborations, application responsibilities, services/interfaces and logical
information. U18/U19 and other adjudicated responsibility/semantic contracts are
incorporated as qualified contributions to their explicitly identified subjects.
F/D/M rows remain source assertions, not automatically new Features or objects.

The migration accounts for **179 original tracked RAD files**. **855 selected
source ranges across 57 files** retire from the active architectural role;
**20 whole carriers** retire, including 11 historical generated baseline carriers.
The [source succession](../../evidence/1.1.5/002/after/source-succession.json) records
exact replacements and source-edition offsets; [preservation validation](../../evidence/1.1.5/002/after/source-preservation-validation.json)
checks unchanged original text. Other passages retain their actual governance,
review, navigation, evidence or unresolved-source roles. No old source document
is relocated into the continuum as a substitute for decomposition.

The [corpus accounting](../../evidence/1.1.5/002/after/corpus-accounting.json),
[definition reconciliation](../../evidence/1.1.5/002/after/definition-ingestion.json),
[section correspondence](../../evidence/1.1.5/002/after/section-correspondence.json)
and [exact source ranges](../../evidence/1.1.5/002/after/source-range-correspondence.json)
identify that boundary. The last source-completion pass adds 509 exact contributions
through 452 source ranges to existing resources without allocation. Accepted
retypings retain earlier conceptual contributions as provenance; no speculative
BusinessObject counterpart is manufactured. Source conflicts/qualifications
remain visible. The runtime-AI optional-adjunct contribution is assigned to the
explicit Ergon subject, not the separately bounded AI Assurance Ergon.

This is a real migrated continuum with an explicit residual boundary, not a claim
that every unretired source assertion has been reconciled. DR-5 identifies the
remaining matters requiring architectural authority. No blanket migration of
legacy-doco or unrelated historical processing disposal is performed.

<a id="dr-4"></a>
## DR-4 — Projection, publication and reproducibility

The original control and manifest-follow-up results below retain their actual
populations and verification events. The new ARB result is separately verified at
the end of this DR.

**Applied and exercised.** The [review entry point](../../../README.md) exposes the
full-content HTML browser, canonical index, Exchange model and derived PDF catalogue.
The [Archi workbench](../../archimate/README.md) retains the human modelling and
review role. Its presentation template carries 1,209 existing Views, property
vocabulary and navigation, with empty semantic collections. Canonical Markdown
supplies elements and outgoing native relationships. Publication allocates no IDs.

The current pipeline passes schema, reference, placement, identity, provenance,
qualified-predicate, containment and independent information-name checks. All 83
information elements with established naming inputs reproduce their names; 11
unclassified DataObjects remain explicitly outside Topic derivation. Eleven
mutation/workbench/publication tests pass, including exact-baseline and stale-review
rejection, prospective validation, identity retry safety and zero-allocation checks.

**Actual Archi 5.10 import and complete-model export PASS** for the retained control:
all 1,559 canonical values/owners, native types, names and documentation; all 1,137
relationships; and all 1,209 Views survive. The canonical no-edit review detects
zero changes. [Transport evidence](../../evidence/1.1.5/002/after/archimate-validation.json)
preserves raw differences: language attributes and organization/navigation
transformations mean whole-model byte fidelity is **not** established. Existing
presentation is not replaced with the return. Full interactive visual review and
architectural acceptance remain distinct.

[Isolated regeneration PASS](../../evidence/1.1.5/002/after/reproducibility.json):
XML, HTML, Markdown index and TeX are byte-identical from maintained inputs without
historical captures, processing workspaces, copied LaTeX architecture or local
interaction logs. The generated 255-page PDF catalogue compiles and repeats byte
for byte in the fixed recorded environment; it is not a second narrative source.
The browser passes a DOM-contract smoke test; live-browser visual review is not
claimed. Rendered local links pass. Retained source succession is repeatable from
its exact entry Git edition; initial UUID allocation is not rerun as publication.

The required Maven architecture suite passes. The historical framework suite
already had 10 authority-pin errors at entry; two historical fixture classes in
the focused shared suite encounter the same AGENTS.md pin. These suites are
reported **FAIL**, separately from 45 executed shared semantic tests and the
passing current pipeline. Historical pins are not rewritten to manufacture a
PASS. [Validation evidence](../../evidence/1.1.5/002/after/validation-summary.json)
records commands, outcomes and limits.

### Authorised follow-up — root continuum review manifest

The Solution Architect's [2026-10-21T10:16:02.745Z instruction](../../evidence/1.1.5/002/before/ai-agent-tasks/20261021T101602Z-generate-continuum-review-manifest.md)
authorises the generated [root manifest](../../../continuum/MANIFEST.md) and its
deterministic generation/validation. It is a navigation/review projection, with no
independent architectural authority. All **1,559 elements** appear exactly once,
organised by the existing catalogue's native layer/aspect and type, then by
displayName. Each row exposes native type, specialisation, independent semantic
standing/currentness, canonicalName or explicit unresolved status, canonicalId,
linked canonical path and source qualifications. The **164 unresolved names** and
all historical/qualified populations remain visible. The manifest also exposes
zero counts for supported but unpopulated types.

The exact `continuum/MANIFEST.md` path joins the existing publication subtree as
a derived-output exclusion from element loading. Arbitrary other Markdown still
fails element validation. Generation validates canonical inputs; `--check` detects
missing/stale output without writing. The current Makefile and isolated generation
check include the manifest. No classification, identity, standing or relationship
is inferred or altered for presentation.

[Follow-up verification](../../evidence/1.1.5/002/after/manifest-validation.json)
passes: 16 continuum tests (including five manifest checks), all 1,595 rendered
local links, current generation/staleness checks and 90 Maven architecture tests.
[Isolated regeneration](../../evidence/1.1.5/002/after/manifest-reproducibility.json)
reproduces the manifest and existing generated publication bytes. All 1,559
canonical files and six existing publication artifacts are byte-identical to
this follow-up's entry state. This adds review navigation without changing the
migrated architecture, original verification limits or pending holistic acceptance.

### Applied ARB follow-up — native validation and current publication

Native endpoint applicability is now checked independently of XML XSD validity,
using the retained ArchiMate 3.2 endpoint matrix and source hashes. Reciprocal
historical lineage, source standing/provenance, reverse Serving and justified
endpoint-invalid fallback are validated. The initial projection exposed a second
Composition parent for the same responsibility assertion; application was
corrected to **reuse the original Composition**, retaining the qualified source
contribution. No duplicate structural parent remains.

The current pipeline passes **20 continuum tests** and generation/staleness,
schema, references, containment, identity/provenance, native endpoint and naming
checks. All **90 required Maven architecture tests** pass. The existing historical
authority-pin failures above remain historical results; those fixtures were not
rewritten. The [transition check](../../evidence/1.1.5/002/after/arb-transition-verification.json)
verifies all 1,559 prior canonical/representation IDs, all 1,137 original native
relationships, the unchanged original predicates, 18 entry-state DesignNotes and
the presentation template. It records **691 modified existing elements**, **43
new elements** and **514 additional native relationships**. Reapplying all three
bounded plans allocates zero IDs and changes zero canonical files.

[Current isolated regeneration](../../evidence/1.1.5/002/after/arb-reproducibility.json)
reproduces the manifest, Exchange XML, full-content HTML, index, TeX and publication
metadata from maintained inputs. [Rendered local-link validation](../../evidence/1.1.5/002/after/arb-link-validation.json)
passes after correcting nested-label and source-local-fragment link rebasing in
DesignNote contributions. Canonical rendering removes source-table trailing
padding while exact original text remains in the source/disposition evidence.
The XML serializer uses numeric references for retained line-end whitespace,
preserving parsed documentation exactly; the projection test checks every
element's complete documentation against its canonical body. The derived **261-page PDF** compiles and repeats
byte-for-byte in the recorded fixed XeLaTeX environment; full visual inspection
is not asserted. [Actual Archi 5.10 import/export](../../evidence/1.1.5/002/after/arb-transport/validation.json)
passes for **1,602 elements, 1,651 relationships and all 1,209 Views**. Canonical
properties/owners, native types, names and documentation survive, and the no-edit
workbench review detects zero changes. The raw comparator still reports language,
navigation and other representation differences; the retained returned XML and
comparison counts do **not** establish whole-model byte fidelity. The authored
presentation is not replaced by the return. Initial sandbox GTK display access
failed; authorised local display execution succeeded, with no network dependency.

<a id="dr-5"></a>
## DR-5 — Unresolved questions and holistic review

The original question table is retained as reviewed decision history. The ARB
dispositions and subsequent application facts below govern its current treatment;
the former absence of application authority is not an ongoing blanket blocker.

**Solution Architect/ARB disposition requested on the resulting architecture as a
whole.** No agent architectural closure is asserted. These affected matters remain
explicit; they have not halted independently supported ingestion:

| Question | Preserved boundary / decision needed |
| --- | --- |
| Q-01 | Formal canonical-name vocabulary and contextual inputs for newly represented native types; 164 unresolved canonicalNames. Do not invent qualifiers or root completeness. |
| Q-02 | 214 historical Capability references have no accepted current RAD crosswalk. The broader historical population retains source standing; 223 original definitions remain absent rather than inferred. |
| Q-03 | External governing-body interest versus operational Actor facets: the source expressly distinguishes these from enterprise Stakeholders. Classification/identity requires that distinction to be adjudicated. |
| Q-04 | Candidate CORE/SEC/PROV/INT/APP/DIR catalogue handoffs versus existing historical coded resources, including a DIR range and a layering-pattern entry: explicit owner/instance correspondence is required; equal codes/titles do not alone approve identity continuity or standing. |
| Q-05 | Remaining G2 candidates, assemblies, reified Relationship and later accepted authored Topics: retain exact known correspondences and accepted retypings; decide any additional same-instance/cross-level mapping. |
| Q-06 | Strategic construct facets versus ApplicationComponents, notably Digital Twin: no identity merge follows from shared terminology. |
| Q-07 | Ergo pattern, Erga collection, Pathway/Segment/Traversal/Route/Horos/Terma and composite control/support bundles: resolve the expressly open native classification/identity questions before materialising additional elements. |
| Q-08 | Unallocated F/D/M proposals and unresolved carrier/control facets: mapped assertions are preserved on existing subjects; a table row does not itself establish another architectural element. |
| Q-09 | Class/Package working DesignNote explicitly defers native classification. Older working notes retain their U01–U19 source/adjudication roles. |
| Q-10 | Remaining ownership/Service-consumption and cross-level assertion matrices require explicit instance/endpoint correspondence. Generic modelling rules remain governing. Labels cannot supply a crosswalk. |
| Q-11 | 462 qualified predicates have no accepted native relationship mapping. Preserve their source owners, target identities, standing and original predicate meanings until adjudicated. |
| Q-12 | Holistic acceptance, residual-source treatment and any future retyping/lineage or automatic relationship/View application scope. The bounded workbench adapter defers unsupported automatic application explicitly. |

[Corpus accounting](../../evidence/1.1.5/002/after/corpus-accounting.json) names affected
sources; [continuum validation](../../evidence/1.1.5/002/after/continuum-validation.json)
names affected canonical IDs/properties. Source-level Proposed, Candidate,
historical, superseded and unresolved assertions remain available. Native graph
validity and tooling PASS are not architectural approval. The maintained result is
mechanically reproducible and reviewable; full semantic reconciliation/stability
is qualified by these questions and final Solution Architect confirmation.

<a id="arb-semantic-reconciliation"></a>
## ARB semantic reconciliation — continuum migration residuals

**ARB adjudication recorded after migration review.** The migration correctly
preserved uncertainty rather than manufacturing identity or native ArchiMate
relationships. That conservative migration boundary is not, however, an
architectural prohibition on subsequent adjudication. The Solution Architect and
ARB may establish semantic continuity, identity correspondence and relationship
typing from the architectural evidence. A migration residual therefore does not
remain unresolved merely because the migration machinery declined to infer it.

The review applies a semantic-continuity test rather than a string-identity test.
An architectural assertion is represented when its architectural meaning survives
in the continuum, including where its name, decomposition, native representation
or carrier has changed. Exact labels remain useful evidence but are neither
necessary nor sufficient by themselves. Where an earlier and current element have
the same meaning, tier, parentage and governed canonical context, the ARB may
establish identity/correspondence notwithstanding different migration UUIDs.
Where the concept has evolved, the ARB records the evolution rather than creating
a false duplicate. Only a source assertion for which no represented,
superseded/historical, deliberately excluded or explicitly unresolved disposition
can be established is an omission.

### Capability reconciliation

The **214 historical Capability references in Q-02 are not to be treated as 214
architecture gaps**. Review has established that a substantial population has
direct current RAD counterparts, including examples with the same display name,
capability tier, hierarchy and canonicalName but a different migration identity.
Those are strong correspondence/duplicate candidates rather than orphaned
architecture.

The ARB authorises capability reconciliation using the following evidence order:
semantic responsibility and architectural meaning; capability tier and governed
parentage; canonical hierarchy/name; source provenance and accepted evolution; and
display-name correspondence. Exact name/path agreement is strong evidence but does
not override contradictory semantics. Reconciliation SHALL distinguish:

1. **direct correspondence** — same architectural capability represented by a
   current canonical capability;
2. **evolved correspondence** — the architectural concern survives through an
   accepted rename, refinement or decomposition;
3. **related only** — same architectural vicinity but not sufficient evidence of
   same identity; and
4. **no counterpart** — genuinely historical/unmapped and retained as such.

Where direct correspondence is established, the historical/current pair SHALL NOT
remain represented as two independent current architectural capabilities merely
because migration UUIDs differ. Identity/lineage mechanics must preserve the
historical provenance and prevent UUID reuse while presenting one coherent
architectural concept.

The **223 records lacking an established original definition** remain a separate
evidence-quality fact. Missing historical definition text SHALL NOT be invented;
it does not prevent correspondence where other authoritative architectural
evidence is sufficient.

### Qualified predicate reconciliation

The **462 qualified source predicates in Q-11 are preserved architectural
relationship assertions, not 462 missing architectural decisions**. Their
population is:

| Predicate | Count | ARB native-treatment rule |
| --- | ---: | --- |
| governed_process | 16 | Use the strongest endpoint-valid native relationship established by source semantics; otherwise Association. |
| uses_function | 6 | Prefer the endpoint-valid Serving direction where the Function provides behaviour used by the consumer; otherwise Association pending stronger semantics. |
| realises_feature | 11 | Realization where the source assertion and endpoint types preserve that meaning. |
| participating_function | 8 | Use the strongest endpoint-valid participation/behaviour relationship established by the source; otherwise Association. |
| contributes_feature | 1 | Use the strongest justified contribution relationship; otherwise Association. |
| exposes_process | 1 | Prefer Serving where the Process exposes behaviour to the consumer; otherwise Association. |
| uses_process | 1 | Prefer the endpoint-valid reverse Serving direction where the Process serves the user; otherwise Association. |
| associated_feature | 120 | Association unless a stronger relationship is established by the source assertion. |
| enabled_by | 62 | **Reverse Serving:** if A is enabled_by B, represent B —Serving→ A, subject to native endpoint validity. |
| enterprise_enabled_by | 194 | **Reverse Serving:** if A is enterprise_enabled_by B, represent B —Serving→ A, subject to native endpoint validity. |
| enterprise_contribution | 38 | Preserve contribution semantics using the strongest justified native relationship; otherwise Association. Do not invent Service-consumption semantics. |
| purpose_context | 3 | Association unless the source establishes a stronger architectural relationship; retain the predicate qualification where it carries context not expressible by the edge. |
| responsibility_parent | 1 | Apply the native structural relationship justified by the governed responsibility model; otherwise Association pending endpoint adjudication. |

Thus **256 predicates (55.4%)** in the two enabled-by families have an ARB-approved
default mapping to reverse Serving, and **11** explicit realises-feature predicates
have a direct Realization rule where endpoint validity holds. Association is the
governed fallback when the existence of the architectural relationship is known
but a stronger ArchiMate semantic cannot be demonstrated.

The governing principle is:

> **Represent the known relationship using the strongest justified native
> ArchiMate relationship. Use Association as the safe native fallback; strengthen
> it only when the source semantics justify the stronger relationship.**

The original qualified predicate and source provenance SHALL remain available
where they convey semantics not fully expressed by the native edge. Native
relationship materialisation SHALL NOT erase the evidence from which the
adjudication was made. Conversely, the existence of a qualified predicate is no
longer sufficient reason to leave a known architectural relationship outside the
native graph.

### DesignNote capture and release Praxis

The retained Domain05 **DesignNotes are governed architectural capture artefacts**.
They are not required to correspond one-for-one with canonical ArchiMate elements,
and they are not historical junk to be discarded after ingestion. Their purpose is
to permit architectural thinking to be captured before every contained assertion
has been normalised into the continuum.

A DesignNote may contain zero, one or many architectural assertions. During the
release Praxis, each substantive assertion SHALL be analysed and given an explicit
disposition. The normal classifications include:

- **Constraint** — invariant, prohibition, responsibility boundary, semantic
  distinction or other restriction on the architecture;
- **Requirement** — behaviour, capability, service or outcome the architecture is
  required to provide or support;
- **ImplementationGuideline** — Harmonia specialisation of CourseOfAction,
  recording an authorised architectural/implementation direction for satisfying
  requirements without incorrectly turning that choice into an enduring
  requirement;
- an appropriate existing/new canonical architectural element or relationship
  where the assertion defines such a thing;
- represented by an existing canonical assertion;
- superseded/rejected/deliberately excluded; or
- explicitly unresolved.

Rationale, examples, explanatory prose and diagrams may remain provenance without
being manufactured into canonical elements. Explicitly unresolved implementation
choices SHALL remain unresolved and SHALL NOT be converted into Requirements merely
because they occur in a DesignNote.

A DesignNote is considered **fully processed for a release only when every
normative architectural assertion has an explicit disposition**. Processing does
not delete the DesignNote: it remains attributable provenance/evidence for the
canonical architecture and its decisions. New DesignNotes may continue to be
captured in the governed DesignNote source directory and are inputs to this release
Praxis.

Review of the current 18 pre-migration Domain05 DesignNotes confirms that this
classification is materially applicable. They contain substantial normative
Constraints, Requirements and implementation directions alongside rationale and
explicitly deferred matters. Examples include Mneme Context/Pragma boundaries,
Petasos durable handoff semantics, Themis distributed policy enforcement,
Mnemosyne persistence constraints, Kleio authoritative audit capture, Pylai
interoperability boundaries, Energeia/Praxis responsibility, Digital Twin
execution semantics and Identity/Authentication/Authorisation implementation
direction. Their architectural content SHALL therefore be reconciled assertion by
assertion rather than treated as an indivisible DesignNote element.

### Effect on DR-5 questions

This ARB review changes the interpretation of the original migration residuals:

- **Q-02 is substantially resolved in principle.** Capability correspondence is
  authorised under the semantic-continuity rules above. The remaining activity is
  exhaustive application of those rules to the 214 records and retention of any
  genuine no-counterpart cases.
- **Q-09 is resolved as a carrier question.** DesignNote is a governed capture and
  provenance artefact; its assertions are classified/disposed during the release
  Praxis. The Class/Package assertions may remain explicitly unresolved where the
  note itself defers their native classification.
- **Q-11 is substantially resolved in principle.** Qualified predicates are known
  architectural relationship assertions. Native mapping follows the rules above,
  with reverse Serving for enabled-by families and Association as the safe fallback
  where no stronger semantics are established. Remaining work is endpoint-valid
  application and validation, not rediscovery of whether a relationship exists.
- **Q-10 is narrowed accordingly.** Service-consumption and cross-level matrices
  still require correct endpoint correspondence, but known relationships need not
  remain non-native solely because the migration lacked permission to adjudicate
  them.
- **Q-12 no longer reserves these matters to an unspecified future authority.**
  The present Solution Architect/ARB review is that authority. Holistic acceptance
  still requires completion of the semantic audit and truthful recording of any
  genuine residuals.

### Further ARB adjudication of DR-5 residuals

The remaining DR-5 questions have been reviewed against the maintained RAD sources.
The following dispositions replace a blanket interpretation of them as equally
unresolved migration blockers.

#### Q-01 — canonical-name vocabulary: retained, but narrowed

Q-01 remains a **real naming-governance question**, not an architectural-content
gap. The maintained naming governance explicitly requires formal qualifiers to be
authorised and explicitly prohibits manufacturing missing vocabulary or ancestry.
Existing governed vocabularies already cover Motivation local naming, CT1/CT2/CT3,
BEC/EC where applicable, FEAT/FN/SV/PR, and the information abstractions BO/TOPIC/
ARTIFACT. The review has not found an authorised general qualifier vocabulary for
all newly represented native types responsible for the remaining 164 unresolved
canonicalNames.

The ARB therefore preserves those canonicalNames as unresolved until the naming
vocabulary is deliberately completed. Their identity, native classification,
displayName, friendlyName where established, architectural content and standing
remain independently valid. **No element is an architectural omission merely
because canonicalName is unresolved.**

#### Q-03 — external authorities: resolved

The Motivation architecture explicitly distinguishes external authorities and
regulatory bodies from Harmonia's six enterprise Stakeholders. The Business
Architecture independently establishes **Government / Regulatory Body** as one of
the seven canonical Business Actor categories.

The ARB therefore establishes:

- an external authority/regulatory organisation is **not** an enterprise
  Stakeholder merely because it influences Harmonia;
- where the authority itself is represented as a participating identifiable
  enterprise-ecosystem entity, its appropriate Business Architecture
  classification is **BusinessActor — Government / Regulatory Body**;
- the legal, statutory, identifier, privacy, interoperability or standards effect
  imposed by that authority is represented through the applicable Motivation
  Driver/Constraint/Requirement/Influence architecture rather than by converting
  the authority into a Stakeholder; and
- a Motivation-level reference to the source of a constraint and a BusinessActor
  representation of the external organisation are architectural facets, not
  automatically the same element identity.

Q-03 is therefore closed as a classification question. Individual authority
instances need only be materialised where the architecture actually requires an
independently represented Actor.

#### Q-04 — coded catalogue handoffs: identity rule resolved; instance work remains

CORE/SEC/PROV/INT/APP/DIR catalogue codes, titles and handoff labels are evidence,
not identity. The ARB retains the migration's refusal to merge instances from code
or title equality alone, but removes any implication that such records require a
special identity doctrine.

The ordinary semantic-continuity rule established above applies: where source
responsibility, meaning, scope and provenance establish the same architectural
thing, reconcile it to the existing canonical element and retain the catalogue
code as alias/provenance. Where the catalogue entry is an evolved predecessor,
record lineage/evolution. Where it represents a distinct architectural thing,
retain a distinct identity. A layering-pattern or catalogue row does not become an
element merely by appearing in a catalogue.

Q-04 is therefore **resolved in principle**. Remaining work is bounded
record-by-record correspondence, not an unresolved architectural rule.

#### Q-05 — G2 information candidates and reified relationships: standing governs

Q-05 is narrowed rather than globally closed. The maintained G2 review already
contains explicit architectural standing and decisions and SHALL be honoured as
such. In particular, approved decisions such as G2-D01–D04, accepted Topic
retypings/correspondences and explicitly approved relationships may be reconciled
into the continuum under semantic continuity. Rejected/superseded candidates,
including the former independent Referral Disposition candidate, SHALL NOT be
resurrected by migration. Category-D suggestions remain non-authoritative.
Residual G2 B/C questions remain genuinely candidate/unresolved only to the extent
recorded by that review.

Assemblies, Views, Collections, Relationships and Topics SHALL retain their
different semantics. A reified information Relationship is not collapsed into an
ArchiMate edge merely because both describe a relationship; nor does an ArchiMate
edge manufacture an information Concept. Known same-instance retypings may be
reconciled; cross-level correspondence requires the semantic assertion actually
established by the G2 decision.

Thus Q-05 is no longer a single unresolved migration bucket. Its disposition is
the **per-assertion standing already established by G2**, with only the explicitly
retained G2 questions remaining unresolved.

#### Q-06 — strategic versus application facets: resolved

Shared terminology does not establish identity across architectural layers. A
strategic construct such as Digital Twin expresses strategic capability/resource/
course meaning at its governed abstraction, while an ApplicationComponent named
DigitalTwin represents an application-structure realisation. They SHALL remain
separate architectural elements unless explicit architectural evidence establishes
a relationship between them.

Where the architecture establishes implementation/realisation, represent the
appropriate native relationship rather than merging identities. Q-06 is closed.

#### Q-07 — Ergo/Erga, Pathway and interoperability constructs: classify by meaning, not bundle

Q-07 is substantially narrowed by the maintained Domain05 adjudications. The
architecture already distinguishes the meanings that the migration grouped into
one question:

- **Ergon/Ergo** is the discrete governed unit of executable business behaviour;
  Ponos executes it under the established execution boundaries.
- **Erga** denotes a meaningful collection/population of Ergo and is not thereby
  another executable Ergo.
- **Praxis** defines fulfilment semantics and the applicable Pathway structure; it
  is not an orchestration engine.
- **Pathway** defines conceptual sequencing of Ergo. A Pathway/segment/traversal
  description is not automatically an ApplicationComponent.
- **Horos** is the logical Praxis boundary construct established by the Pylai
  adjudication; it replaces the ambiguous logical Praxis "Endpoint" meaning only,
  without renaming genuine FHIR/network/Camel/Ergon endpoints.
- **Terma** is the logical external interface responsible for external
  communication mechanics and is explicitly **not Ergo**.
- **Diadromí** is transient interaction state and is neither Task/Pragma nor Ergon.

The ARB therefore rejects materialising composite control/support **bundles** as
elements merely to satisfy migration structure. Each independently meaningful
construct is classified at the abstraction its accepted semantics establish;
otherwise the assertion remains a relationship, Constraint, Requirement,
ImplementationGuideline or unresolved DesignNote assertion under the release
Praxis.

Q-07 is **resolved as a classification method and semantic separation**. Any
construct whose accepted source still explicitly defers its precise native
ArchiMate type remains individually unresolved; that local deferral does not make
the entire family unresolved.

#### Q-08 — F/D/M rows and carrier/control facets: resolved as assertion-versus-element rule

The ARB confirms the migration's conservative rule: an F/D/M table row or
carrier/control facet does not establish a new architectural element merely
because it has a key and description. The release Praxis SHALL analyse what the
row asserts.

Where it identifies an already established Feature, Function, information object,
component, service or relationship, bind the assertion to that element. Where its
normative content is a Constraint, Requirement or ImplementationGuideline, model
that assertion accordingly. Allocate a new element only where the source actually
establishes an independently bounded architectural thing. Otherwise retain the
row as traceability/provenance or as an explicitly unresolved assertion.

Q-08 is therefore closed as a general representation question; only individually
unresolved source assertions remain open.

#### Q-10 and Q-12 — narrowed release-application boundary

Q-10 no longer requires labels to prove identity, but neither does it prohibit ARB
semantic adjudication. Ownership, Service-consumption and cross-level matrices
SHALL use the semantic-continuity and relationship rules established in this
review. A known service-consumption relationship should be represented natively
when its endpoints are established; Association is available where relationship
existence is known but stronger native semantics are not. Information ownership
remains with the responsible Capability/Function/Process architecture and is not
transferred by search, transport, caching, indexing, persistence or presentation.

Q-12 is narrowed to **completion evidence and genuinely residual architecture**.
This review is the authorised Solution Architect/ARB adjudication; no further
unspecified authority is required for Q-02/Q-03/Q-04/Q-06/Q-07/Q-08/Q-09/Q-11 or
the general Q-10 mapping method. Release completion still requires mechanical
application of authorised reconciliations, assertion-by-assertion DesignNote
processing, validation of the resulting native graph and a final residual register
showing anything that genuinely remains unresolved.

These adjudications do **not** assert that the 214 capability records and 462
predicate records have already been mechanically rewritten. They authorise and
define the deterministic reconciliation that the release machinery must apply and
verify. Mechanical application evidence must report exact mapped, evolved,
related-only and residual counts rather than converting this architectural
decision into an unsupported PASS.

### Applied reconciliation and final residual boundary

The Apply instruction now supplies the mechanical application event; the preceding
ARB text remains unchanged decision authority/history. [Capability and predicate
application](../../evidence/1.1.5/002/after/arb-semantic-reconciliation.json) records
all 214 correspondences and 462 predicate dispositions. **440 predicates map to
native relationships:** 264 Serving, 175 Association and one reused Composition,
so **439 new predicate edges** are added. All 256 enabled-by assertions use reverse
Serving with legal endpoints. **22 predicates remain excluded:** 11
realises_feature and 11 associated_feature assertions whose original sources
expressly say that the Feature association is not established. The ARB Realization
rule is conditional on established meaning/existence; neither it nor fallback
Association authorises manufacturing those relationships. The original predicates,
record state and source qualifications remain unchanged.

[DesignNote dispositions](../../evidence/1.1.5/002/after/arb-designnote-assertion-dispositions.json)
account for **2,198 source blocks across 18 notes**, using original-edition line
selectors and complete paragraph/list-entry text. These are evidence selectors,
not 2,198 independently normative elements. **1,527** blocks contribute exact
source-qualified assertions to existing canonical subjects; **43** become the
bounded new Constraint/Requirement/ImplementationGuideline elements in DR-2;
**281** remain rationale/example/provenance; **23** record later evolution; **one**
is superseded by U15; **six** retain metamodel direction without asserting an
instance; and **317** retain explicit source-level design/classification deferrals.
All notes remain byte-identical to this follow-up's entry state, including the
already installed Digital Twin supersession wrappers. Original wording is not
rewritten to agree with later decisions.

The assertion audit preserves U09's **Observe → Report**, U15's policy-governed
intrinsic Kleio capture authority (rather than a blanket authorisation exemption),
U06/U19's discrete Energeia and peer Ponos, U14's limited logical Horos rename,
U18's continuation-role representation deferral and U19's serial Twin processing,
fresh per-Behaviour Context, stable per-input sequence, failure dispositions and
authorised administrative resume. Mixed statements retain the established rule
alongside the expressly deferred detail. Inventory coverage and exact contribution
checks are evidence for review, not a claim that automated counting proves
semantic completeness or architectural acceptance.

[Remaining source application](../../evidence/1.1.5/002/after/arb-remaining-source-dispositions.json)
records **nine catalogue-row dispositions**, **37 information-responsibility
contributions** and **32 additional native relationships**. Seven catalogue
requirements correspond to existing historical concerns (including INT-TRANS-001
to historical INT-002); APP-SEP-001 binds to the related reclassified layering
pattern without becoming a Requirement. The DIR range remains navigation, with
six distinct historical requirements and no six-way workflow-Pragma equivalence
invented. Candidate owning-domain standing is preserved.

All **18 active Service-consumption rows** have known endpoints and become directed
Associations: native BusinessService→Capability Serving is invalid. The separately
approved assurance Role-level use produces **11 legal Serving relationships**,
with status limited to System Steward and no mandatory outcome broadcast or
consuming-Capability allocation. Three approved G2 contextual Associations now
represent Episode→Encounter, Appointment→Encounter and Encounter→ServiceDelivery,
retaining only authorised multiplicities and qualifications. No withdrawn
Service-consumption row is reinstated and no descriptive ownership-ledger label
is manufactured into an information element. Reified information Relationships
retain their separate semantics and identities.

The final residual register is bounded as follows:

| Question | Current application / genuinely retained boundary |
| --- | --- |
| Q-01 | 165 formal canonicalNames remain unresolved for unauthorised qualifier vocabulary; their elements/content are represented. |
| Q-02 | Applied: 209 direct and five evolved correspondences; UUID reservation and reciprocal lineage verified. Missing historical definitions remain absent. |
| Q-03 | Classification resolved. External authority references do not require Actor instances; participating identifiable organisations use the Government / Regulatory Body BusinessActor facet when needed. |
| Q-04 | Record-by-record correspondence performed; catalogue Candidate standing and the DIR range's absent individual workflow definitions remain explicit. |
| Q-05 | G2-D01–D04 meanings and accepted Topic correspondences retained; three native contextual associations applied. Problems/Conditions lacks separately established family endpoints, so Episode's [1..*] meaning remains on Episode. G2-Q02–Q07 retain their precise recorded classification, qualification, authority, association, composition and change questions. Rejected Referral Disposition and Category-D Responsibility Assumption Assertion are not resurrected. |
| Q-06 | Separate strategic/application facets retained; no label-based merge or unsupported realisation allocated. |
| Q-07/Q-08 | Independent meanings and assertion-versus-element rule applied; only individually deferred classification/physical representation remains. No control/support bundles or automatic keyed-row elements. |
| Q-09 | Capture-carrier question resolved; assertion dispositions retained. Class mapping and specific historical DataObject classifications remain deferred; Package's agreed Grouping direction does not resolve Class or instantiate a Package. |
| Q-10 | Active matrix endpoint work performed as above. Definition-to-Accountability remains reusable guidance, not mandatory five-stage instances. Its established HealthcareService/Task concepts already correspond to retained family elements; ReportedTask accountability, universal Delivery/Outcome ownership and explicitly unestablished provider contracts remain unresolved. |
| Q-11 | Applied: 440 native mappings, 22 expressly unestablished exclusions; original assertions and qualifications preserved. |
| Q-12 | Application and verification evidence supplied. Holistic acceptance remains with the Solution Architect/ARB; no unspecified future authority is substituted for the present adjudications. |


<a id="dr-6"></a>
## DR-6 — Bounded Minutiae and formal evidence

Minutiae-Accepted: No

Comment: No resulting-group disposition has been recorded. The operative Task
already authorises the work. Supporting navigation points agents to the same
individual axioms, canonical continuum and current pipeline. Existing governance
contracts retain their roles; historical documentation gets bounded succession
not retrospective rewriting. The root release is incremented once to 1.1.5 with
previous_version 1.1.4. Maven versions and runtime code remain unchanged.

Formal Tasks preserve the original instruction plus the holistic-review,
continuation and transient-retry follow-ups at their actual instruction-entry
timestamps. The final Report records factual outcomes and correspondence under
this iteration's after/ai-agent-reports location. Purpose-selected migration,
validation and actual Archi-return evidence support this HCRL. New source changes
remain in the working tree for review; no commit, push or architectural closure
is claimed. Completed historical HCRLs and the rejected 001 input remain preserved.

Formal Report: [applied migration and verification](../../evidence/1.1.5/002/after/ai-agent-reports/20261021T100913Z-element-continuum-applied-migration.md). Report production event: `2026-10-21T10:09:13.320Z`.

Initial migration evidence-layout validation: PASS (four Tasks, one Report). The post-emission timestamp-field formatting correction is retained as subsequent Report provenance; original event times and filename prefixes are unchanged. The later manifest Task/Report records the separate authorised follow-up without rewriting that original Report.

Manifest follow-up Report: [generated review manifest](../../evidence/1.1.5/002/after/ai-agent-reports/20261021T102230Z-continuum-review-manifest.md). Report production event: `2026-10-21T10:22:30.214Z`. The follow-up adds the exact root projection exclusion and its checks under DR-4; it does not amend the original migration Report or allocate another release.

Follow-up iteration-evidence validation: PASS (five Tasks, two Reports; no errors
or unresolved event-time/prefix correspondence). This mechanical check supplies
no architectural acceptance or attribution-authenticity adjudication.

ARB application Report: [applied semantic reconciliation and verification](../../evidence/1.1.5/002/after/ai-agent-reports/20261021T121642Z-arb-semantic-reconciliation-applied.md).
Report production event: `2026-10-21T12:16:42.780Z`; operative Task issuance event:
`2026-10-21T11:43:45.839Z`. Entry HEAD for this follow-up remains
`798344f80eb7fd3998e17f4f040cf7dabe260388`. The [validation summary](../../evidence/1.1.5/002/after/arb-validation-summary.json)
and exact plans/checks support the material application facts in DR-2/DR-4/DR-5.
Generic transport evidence now retains a trackable text log and attributes the
actual supplied environment rather than asserting an earlier run's recovery.
Task-generated Python bytecode is excluded from the resulting change.

Current iteration evidence-layout validation: **PASS — six Tasks, three Reports**;
no errors or unresolved recorded timestamp/prefix correspondence. This is a
mechanical check, not architectural acceptance or authenticity adjudication.
The original Minutiae disposition and human decision history are unchanged.
The authorised application is reviewable in the working tree; holistic Solution
Architect/ARB acceptance remains pending. No additional release, commit, push or
architectural closure is claimed.
