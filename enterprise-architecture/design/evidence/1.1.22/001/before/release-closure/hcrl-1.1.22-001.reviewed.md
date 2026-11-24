<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Accepted Information Unit semantics and bounded incorporation | Whole DRP accepted; ADD/reconciliation authorised by operative Task | Incorporated-result confirmation remains pending. |
| [DR-2](#dr-2) | Existing hierarchy, Motivation and cross-component reconciliation | Accepted IU-10–IU-12 method and boundaries; ADD authorised | Actual canonical mapping is submitted for result review. |
| [DR-3](#dr-3) | Validation, transport, structural review and release | Incorporation, candidate verification and follow-up investigation authorised; transport failure not dispositioned | Inherited serialization patterns established; no IU graph/text defect found. Strict transport remains FAIL; human/result review pending. |
| [DR-4](#dr-4) | Naming qualifications and contract-specific residual detail | Not newly dispositioned | Preserve bounded unestablished detail; no invented Class or naming token. |
| [DR-5](#dr-5) | Bounded supporting Minutiae | Not recorded | Supporting provenance only. |

# HCRL 1.1.22 / 001 — Information Unit Framework incorporation

**PENDING — authorised incorporation; actual result requires Solution Architect confirmation.**
Accepted predecessor: **1.1.21**, at `cb21ca0c5291f726f4babc7e59dabeba1a6c302c`
on `Mneme-and-Mnemosyne-Build`. Intended resulting development release: **1.1.22**.
This new transition preserves the accepted 1.1.20/1.1.21 hierarchy and each
inherited qualification; it neither reopens nor resolves unrelated residuals.
The root development release is now **1.1.22**, allocated once by the normal
expected-current patch operation. This supplies no architectural acceptance.

**Attributable authority:** Mark Hunter's [operative issued Task](../../evidence/1.1.22/001/before/ai-agent-tasks/20261111T025829Z-information-unit-framework-incorporation.md),
instruction-entry **2026-11-11T02:58:29.162Z**, accepts the whole
[Final.001 DRP](../../offline-design/DRP-Information-Unit-Framework.Final.001.md)
and authorises bounded governed ADD/reconciliation. Its exact bytes are retained
in [before evidence](../../evidence/1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md)
because the input was untracked at entry. The document's prior review-stage
statements remain unchanged historical wording; this later issued acceptance is
explicit. The Task is not acceptance of an as-yet-unreviewed incorporated result.

The [accepted predecessor HCRL](../../register/1.1.21/hcrl-1.1.21-001.complete.md)
and [immutable Exchange](../../archimate/published/1.1.21/Harmonia-Architecture.xml)
establish **6,542 elements / 9,264 relationships / 1,304 Views**, Exchange SHA-256
`6471411187a5ff72b005c9b9855c48beb3d86b7ae6c7c556a4eb58338011a739`.
[Baseline evidence](../../evidence/1.1.22/001/before/baseline.json) identifies exact
Git state, entry changes and current-source pins. No existing publication is
replaced. No commit/push is authorised by this incorporation Task.

[Formal execution Report](../../evidence/1.1.22/001/after/ai-agent-reports/20261111T031941Z-information-unit-framework-incorporation.md),
produced **2026-11-11T03:19:41.608001Z**, records the actual incorporation,
verification limits and uncommitted resulting Git state. The
[review guide](../../evidence/1.1.22/001/after/information-unit-incorporation/review-guide.md)
identifies the exact current candidate and inspectable review products.

<a id="dr-1"></a>
## DR-1 — Accepted Information Unit semantics and bounded incorporation

The issued whole-DRP acceptance governs IU-01–IU-13. Preserve the existing
BusinessObject `10284034c8534b639284386b064f2c54` and logical Calliope Class
`74ec609de1ae498fbf9d13e4ef8c6cbe` as conceptual/logical representations of the
same IU. The accepted delta intentionally replaces the earlier non-indivisibility
wording and Identity/Metadata/Content/Context partition. Preserve that original
text within bounded supersession; do not silently overwrite prior architecture.
Established generation success/failure/previous-generation-preservation rules
remain applicable, including AX-05's failed/degraded-convergence restriction.

The incorporated architecture must preserve atomic governed identity and state
consistency, **exactly one InformationRepresentation**, distinct Unit/Container/
Content metadata, four IU metadata contexts, four coordinate Topic references
independent of security, UUID-plus-IU-Topic naming, shared element-wise regex
TopicMask semantics, optional supplied relationships with local/remote validation
boundaries, and distinct operational Version/generation/structure/native versions.
Physical persistence freedom is compatible with governed atomicity. A separately
governed transformation result receives another IU; no representation registry,
automatic synchronisation or universal lifecycle is authorised.

Representation judgement: use logical native DataObjects for the independently
bounded IU Metadata and InformationRepresentation structures and shared Topic and
TopicMask models. Internal contexts/fields remain semantic structure, not inferred
implementation Classes, Functions, interfaces or new named Topic instances.
Retain the existing IU Class/Package identity and ancestry. Native Composition
expresses constituency; a native Realization links the logical and conceptual IU.
Cardinality and non-implementation qualifications are explicit in canonical bodies.

Relevant canonical axioms: AX-01–04 preserve information purpose, native Content
and architecture-led reuse; AX-05 preserves active/durable authority; AX-06/18
preserve originating information/domain authority; AX-07 preserves Themis policy
and intrinsic security; AX-08/09 preserve meaningful evidence versus transient
workspace; AX-10/11 preserve distributed operation and governed access; AX-12/13
preserve access encapsulation and management boundaries; AX-14/15/17 preserve
semantic distinctions, uncertainty and accepted versus unestablished architecture.
The approach preserves these through explicit component boundaries, metadata homes,
provider freedom and retained contract-specific gaps. No application implementation
is used as architectural authority or changed by this increment.

**Actual incorporation:** the candidate contains **6,546 elements / 9,314
relationships / 1,304 preserved authored Views**. All 6,542 predecessor identities,
9,264 predecessor relationships, existing classifications, names, standing and
source bindings are preserved. Forty-three existing canonical element files
receive bounded definitions, source contributions or relationships; 6,499 retain
their exact predecessor bytes. No existing element is deleted or retyped.

Four independently bounded logical DataObjects were allocated through explicit
`create`, with their accepted-source correspondence retained:

| Construct | Canonical identity |
| --- | --- |
| InformationRepresentation | `b9891b88150840ada3daefdec6f26c35` |
| Information Unit Metadata | `c05b7e37278141488ecc90b9a9ef96eb` |
| Common Topic model | `9e2ffa9b4a1d4422a33c2a4bbcfc86ef` |
| TopicMask | `bb489157a6bf48be926c5a601bb0a6c3` |

The 50 new native relationships comprise **1 Realization, 4 Composition,
43 Association and 2 Access**. They connect the reused conceptual/logical IU,
logical structure, package context, shared Topic semantics, existing Features,
Motivation and owning components. Cardinality and semantic decomposition remain
explicit in the canonical definitions; there are no new implementation Classes,
Functions, Services or interfaces. The old non-atomic definition/partition is
preserved inside a section-bounded supersession wrapper; established generation
and failure guardrails remain outside it unchanged.

The [adjudication coverage](../../evidence/1.1.22/001/after/information-unit-incorporation/adjudication-coverage.json)
and [source succession](../../evidence/1.1.22/001/after/information-unit-incorporation/source-succession.md)
trace IU-01–IU-13 and the unchanged accepted input to the canonical result.
The [change inventory](../../evidence/1.1.22/001/after/information-unit-incorporation/change-inventory.json)
identifies every added/modified canonical file. Incorporated-result acceptance
remains pending.

<a id="dr-2"></a>
## DR-2 — Existing hierarchy, Motivation and cross-component reconciliation

Reuse actual accepted CT1→CT2→CT3→Feature identities and Composition, with scoped
applicability Associations rather than claiming that a passive information object
implements a Capability. The seven historical Feature names, proposed Information
Content and State Management Capability, and two historical RequirementGroups do
not acquire identities by appearance in the DRP. Map semantic coverage across the
accepted hierarchy, preserving partial/multiple coverage and independent Motivation
types/standing. Existing NFRs can be native Axioms, Mandates or Requirements;
historical requirements do not become current by being cited here.

Mneme retains active information/access and operational workspace; Mnemosyne
retains authoritative durable state/version and coordinate resolution with faithful
IU reconstruction. GovernedReader/Writer/Search and persistence-framework/provider
labels do not create new exposed services or APIs. Themis retains policy and
distributed PEP semantics; IU-side criteria are separate from Identity permissions.
Kleio retains AuditEvent capture/management. Existing Dokimasia, Provenance,
Provenance Client Library and their native relationships supply provenance/assurance
allocation. No new evidence subsystem, responsibility or authority is inferred.

Related drafts supply overlap/provenance only. Any alternate representations,
undifferentiated Context/TemporalContext, TopicID-as-IU-identity, wrapper masks,
provider vocabulary or lifecycle proposals remain subordinate to the accepted IU
DRP and existing owning-component contracts. This activity performs no other DRP
consolidation or application implementation.

**Actual reconciliation:** [the retained mapping](../../evidence/1.1.22/001/after/information-unit-incorporation/reconciliation.json)
maps the seven historical Feature proposals to **23 existing accepted Features**
with their exact CT1→CT2→CT3→Feature ancestry. **45 existing Capability Composition
relationships** are reused unchanged, together with five existing IU/package and
provenance-allocation relationships. The historical Information Content and State
Management Capability is covered across existing Enterprise Capabilities rather
than allocated as a duplicate. The proposed GovernedInformationState and
InformationPersistence RequirementGroups receive no new identity, reparenting or
asserted identity-equivalence.

Ten historical requirement assertions from draft.003 §1.2 are individually
reconciled against Final.001 and existing canonical semantics. Ten existing
Motivation elements receive scoped applicability relationships while retaining
their native Axiom, Mandate, Requirement or AssuranceRequirement classifications
and standing; other reused Motivation definitions remain unchanged. The existing
Information Management RequirementGroup supplies context without new containment.
Neither a passive IU nor an applicability Association claims implementation
satisfaction or ownership of a Capability. Existing Dokimasia/Provenance/Client
Library allocation is reused; Kleio audit authority and Mneme/Mnemosyne state
authority remain distinct.

<a id="dr-3"></a>
## DR-3 — Validation, transport, structural review and release

Entry canonical validation **PASS** for 6,542 elements and 9,264 relationships.
Apply the established validator, offline XSD/reference, identity/standing/naming/
containment checks, meaningful mutation tests, deterministic candidate generation,
no-edit workbench review and View preservation checks. Reader and narrative
candidates use explicit evidence-local output destinations. The Makefile
`continuum` target and `project --publish` must not publish the unconfirmed result.

Prepare the one normal patch allocation through the expected-current mechanism
for this authorised substantive transition. Generation consumes no increment;
iteration 001 is not advanced automatically. Accepted 1.1.21 publications remain
immutable. The final candidate requires actual supported-Archi import/export and
applicable Solution Architect tree/reader structural review under Review Praxis §5,
then whole-HCRL confirmation under §6 before complete registration or accepted
publication. Prior 1.1.21 transport qualifications waive no new gate.

**Actual verification and candidate:** [candidate-v2](../../evidence/1.1.22/001/after/information-unit-incorporation/candidate-v2/reader/Harmonia-Architecture.xml)
is the current review candidate, SHA-256
`8ef02598667b643850efca6bd3a8f3f8438f08dce1c764c190018de3a410e312`.
The original candidate, SHA-256
`938928dab4b6ae66597c3d7a84702ab130ba8fecc12764511099ce217c62fc97`,
and its verification remain preserved separately. The second candidate adds
explicit Content-aware/Content-neutral participation and appropriate Provenance
for transformed governed IUs to InformationRepresentation; graph/type/counts
are unchanged. No reviewed or published candidate bytes were overwritten.

Canonical validation, provenance/identity/native endpoints, containment, navigation,
information naming with disclosed unresolved properties, offline XSD/references,
canonical links, exact no-edit workbench review, preservation and independent
generation **PASS**. The no-edit review reports zero element/relationship/View
changes or unresolved deltas. All 18 candidate artifacts and the root Manifest
reproduce from maintained inputs with no legacy docs/scripts/Git and no identity
allocation. Eight existing mutation/projection tests and four existing navigation
tests **PASS**. The required Maven architecture suite **PASS**: 90 tests,
zero failures/errors/skips, build time 21.950 seconds. The reader DOM smoke check
**PASS** establishes population,
search/filter/selection contracts; it is not visual or human structural review.

Supported **Archi 5.10 actual import → native save → export PASS** for the exact
v2 candidate. **Strict transport verification FAILS: Relationship/View transport
drift.** [Transport evidence](../../evidence/1.1.22/001/after/information-unit-incorporation/transport-summary.json)
preserves the exact source/return hashes and comparator findings. All 6,546 element
identities/types/specialisations/names/documentation and the four governed
identity/name property values are preserved. No element/relationship/View ID is
lost or added. Nevertheless **6,456 of 9,314 relationship records** and **15 of
1,304 View records** differ. Raw classifications include 4,693 equivalent
normalisations, 18,397 information-loss findings, 111,908 semantic-difference
findings and 16,351 unexplained differences. Those are raw comparator labels,
not an accepted interpretation or a waiver; successful import and population
equality do not establish lossless transport. The returned model does not replace
canonical Markdown or the preserved authored View template.

**Authorised follow-up investigation:** the Solution Architect's
[transport investigation Task](../../evidence/1.1.22/001/before/ai-agent-tasks/20261111T033823Z-archi-transport-failure-investigation.md),
issued **2026-11-11T03:38:23.096Z**, authorises determining the causes within this
same iteration, with no architecture alteration or acceptance exception. The
[investigation Report](../../evidence/1.1.22/001/after/ai-agent-reports/20261111T034947Z-archi-transport-failure-investigation.md),
produced **2026-11-11T03:49:47.028012Z**, records the actual controls,
interpretation and limits. The
[exhaustive diagnostic evidence](../../evidence/1.1.22/001/after/transport-investigation/candidate-v2-analysis/analysis.json)
accounts for **all 151,349 raw findings**, with zero unexplained transformation
patterns. The raw classification totals above remain unchanged historical facts;
they do not count independent architectural errors.

Seven mechanisms explain the findings: 69 exact-key Property-definition rekeys;
4,624 keyed unordered-collection reorderings; 111,908 absent-to-`en` language
annotations; 15,860 omitted redundant catalogue labels; 2,537 omitted derived
organisation-item IDs; 238 effective-default connection styles made explicit;
and changed global connection sequences in 15 Views. Label removal also generates
15,860 child-list findings, and style addition another 238; those are repeated
observations of the same operations, not additional losses or proven reorderings.

Independent per-ID comparison preserves **every element and relationship type,
endpoint, text, property value/multiplicity and other non-language attribute**.
All 111,908 language deltas are precisely absent → `en`; no existing language
annotation or text is removed/changed, and the input contains no non-English
language tags. This is text/value-preserving export annotation, not authority to
normalise arbitrary language differences. The 6,456 changed relationship records
therefore establish no changed relationship meaning in this model.

All 15,860 omitted labels reconstruct exactly from retained referenced Names
(6,546 elements and 1,553 relationships) or the retained ID of an unnamed
relationship (7,761). All 2,537 removed folder IDs belong to generated non-View
catalogues; no canonical or authored View-folder identity is lost. After those
separately inventoried differences, the entire ordered navigation tree,
references, folder metadata and View hierarchy match. The derived IDs are real
representation information loss, though not lost canonical architecture.

All **4,599 visual nodes**, **2,042 connections**, membership, references,
nesting/node order, geometry, bendpoints, metadata and existing explicit styles
are preserved. The only View changes are **238 previously implicit styles**
materialised as black lines/text and Sans 9, and **15 global connection-sequence
changes**, all accounted for by native source-connection containment traversal.
Within-source connection order is retained. Installed exporter bytecode confirms
both default selection and traversal. Original global sequence and unspecified
style state are not preserved in the return: cross-platform/default-preference
and crossing-line stacking effects are not proven harmless. No pixel-equivalence
or human review is claimed; strict preservation continues to fail.

The equivalent actual **1.1.21 control also FAILS strict transport**, with the
**same 6,456 relationship IDs, same 15 View IDs and identical existing
relationship/View before-and-after records**. It has the same 238 default-style
materialisations and same sequence transformations. The v2 increase is exactly
40 generated scalar language annotations, 54 catalogue-reference labels and
their 54 child-list observations, and 39 derived source-folder IDs. No new IU
transport-failure mechanism is introduced. A second actual import/save/export
of the returned v2 model produces **byte-identical Exchange XML**, SHA-256
`d79a958a14639288c23bcf2de9653543712a23c6333c71cb1ec862ff904ead62`.
[Cross-release evidence](../../evidence/1.1.22/001/after/transport-investigation/cross-release-checks.json)
and exact control inputs/outputs are retained in this iteration.

**Investigation conclusion:** no incorporation or comparison-algorithm defect
requiring correction was demonstrated. Raw fidelity checks are correctly
conservative; interpreting their attribute/subtree labels as independent semantic
changes would be a reporting error. No production comparator, verifier, canonical
source, candidate, original return/native model or original raw evidence changed.
Twenty-six focused diagnostic/current-EA/review tests and all 90 required Maven
architecture tests **PASS**. Replayed strict checks **FAIL** for both releases;
no gate is weakened or waived. Any remediation of connection ordering/default
style state or acceptance treatment requires separate explicit Solution Architect
disposition here. The present Task supplies no such disposition; the IU
architecture remains intact and finalisation remains stopped.

**Required Solution Architect treatment:** disposition the exact v2 transport
failure and its consequences before any dependent finalisation; review the native
workbench tree and generated reader structure where applicable under §5, using
the [review guide](../../evidence/1.1.22/001/after/information-unit-incorporation/review-guide.md);
then confirm this exact incorporated result under §6. These human checkpoints
are **NOT PERFORMED**. No normalization, new transport waiver or inherited
qualification is inferred. Finalisation is stopped under fail-closed governance.

The normal patch allocation is **1.1.21 → 1.1.22**, already prepared once;
further review/completion within this change set consumes no extra increment.
Reader, Exchange and narrative generation used explicit evidence-local outputs.
All 259 predecessor accepted publication/register files are byte-unchanged.
No accepted-release publication, completed registration, iteration advance,
commit or push occurred. Optional candidate PDFs were not compiled. Detailed
verification, required architecture-suite outcome and resulting Git state are
retained in the formal execution Report.

<a id="dr-4"></a>
## DR-4 — Naming qualifications and contract-specific residual detail

Native DataObject is an established type. The current formal naming vocabulary
does not supply a qualifier for unspecialised logical DataObjects. Preserve the
affected canonicalName as explicitly unresolved, with actual Composition-context
friendlyName; do not invent a qualifier or misclassify the common model as a Class
or named Topic to satisfy naming. This is a bounded dependent naming qualification
under the Incorporation Praxis, not missing authority for the accepted semantics.

The four new DataObjects listed in DR-1 retain `canonicalName: null` with explicit
`unresolvedProperties` and Composition-context friendly names. This increment
therefore has **four new formal-name qualifications**; the validator reports
16 unresolved information names in total, including 12 inherited qualifications.
The new Topic object is the common logical Topic model, not an instance of the
governed Topic specialisation. No new qualifier or implicit implementation Class
has been invented. This representation and its bounded naming treatment are
submitted for Solution Architect attention.

The accepted specification intentionally leaves UUID encoding details, exact Java/
wire/physical structures, TopicMask regex dialect/anchoring/depth rules, coordinate
presence/population, local-reference constraints, provider acknowledgement/recovery
vocabulary and concurrency/lifecycle machinery to their owning contracts. This
incorporation preserves those limits and AX-05 authoritative version/generation
semantics; it supplies no new contract decisions. Existing cross-service provenance
detail and broader authority/evidence/retention questions remain with their current
owning HCRLs and accepted qualifications. No new semantic conflict is assumed
resolved by machinery success.

<a id="dr-5"></a>
## DR-5 — Bounded supporting Minutiae

The disclosed group comprises the retained exact Task/input, baseline/source pins,
purpose-selected reconciliation and validation evidence, formal execution Report,
generated Manifest/candidate reader/narrative correspondence, and non-semantic
links within this iteration. The existing population/provenance test expectation
is mechanically updated from 6,542/9,264/462 to 6,546/9,314/462 to reflect the
authorised elements/relationships; no production tooling behaviour is changed.
Substantive IU semantics and mapping remain DR-1/DR-2;
validation/acceptance limits remain DR-3/DR-4.

The follow-up investigation adds its issued Task/Report, exact read-only baseline
and second-pass control results, exhaustive diagnostic comparisons, installed
exporter/importer bytecode excerpts and focused check evidence within 1.1.22/001.
These are supporting evidence; the interpretation and residual presentation
questions remain in DR-3. No new release/iteration or comparator policy is created.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```
