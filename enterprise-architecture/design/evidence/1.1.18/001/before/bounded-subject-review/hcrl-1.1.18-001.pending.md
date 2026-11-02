<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Bound the oversized export review | Not recorded | Interpretation gate; see scope question below |
| [DR-2](#dr-2) | G14–G18 Subject correspondence and hierarchy | Not recorded | Architectural reconciliation remains pending DR-1 |
| [DR-3](#dr-3) | UseCase and UserStory modelling | Not recorded | Neither specialisation exists in the current catalogue |
| [DR-4](#dr-4) | Candidate identities and source contributions | Not recorded | No new canonical IDs allocated |
| [DR-5](#dr-5) | Historical Capability targets | Not recorded | Preserve history; no automatic retargeting |
| [DR-6](#dr-6) | Capability and Feature coverage recommendations | Not recorded | Advisory assessment of all 784 rows |
| [DR-7](#dr-7) | Primary and secondary Subject classification | Not recorded | Classification is distinct from coverage |
| [DR-8](#dr-8) | Release scope and source provenance limits | Not recorded | No release transition proposed or performed |
| [DR-9](#dr-9) | Bounded Minutiae and verification limits | Not recorded | Temporal provenance and Java test execution unresolved |

# HCRL 1.1.18 / 001 — Subject and OpenSpec incorporation preparation

**Pending review preparation; no acceptance or application authority.**
Prepared by Codex on 2026-11-01 UTC (2026-11-02 Australia/Sydney).
The user assigns evidence boundary `1.1.18/001`. Root release authority currently
reads `1.1.18`, previous `1.1.17`; this pending record does not allocate a
transition, establish acceptance of either state, or select the eventual release.
The exact current projection passes the maintained baseline check. Source HEAD:
`763f97586685d5c4b9744509b067d95b9694d0db`, plus the pinned workspace inputs.

The formal [Task](../../evidence/1.1.18/001/before/ai-agent-tasks/unresolved-openspec-subject-capability-reconciliation.md)
preserves the operative request and locator follow-up. The
[report index](../../evidence/1.1.18/001/after/report-index.md) identifies the formal
Report. Detailed outputs are under
[`after/reconciliation/`](../../evidence/1.1.18/001/after/reconciliation/summary.json).
The [specialised checkpoint](change-review.md) is incomplete at the input-scope
gate; it is not a finished Archi reconciliation review.

Applicable authority: [Architecture Governance](../../../governance/architecture-governance.md),
[Review Praxis](../../../governance/architecture-review-praxis.md),
[Provenance](../../../governance/architecture-change-provenance.md),
[Round-Trip Praxis](../../../governance/archimate-round-trip-engineering-praxis.md),
[representation](../../../governance/continuum-representation.md), and
[incorporation Praxis](../../../governance/continuum-incorporation-praxis.md).
AX-04, AX-08, AX-14, AX-15, AX-17 and AX-18 materially apply: tooling does not
decide architecture; meaningful source/decision evidence is retained; classification,
coverage, identity, currentness and acceptance remain distinct; uncertainty and
domain-authority limits are preserved. No implementation conflict is resolved here.

<a id="dr-1"></a>
## DR-1 — Bound the oversized export review

The explicitly supplied export 004 is 29,121,058 bytes, SHA-256
`4ddfaa9045d8d0196f1003c9344d85eb2f121398a79a579fbb6c18545470c0de`.
The exact current published projection is 27,091,699 bytes, SHA-256
`7df92528a09f9a765ddebec105643bae08ac92583cad4e8901b3135d49db2c2e`.
Maintained `review --normalised` emits a 3,824,520-byte
[delta](../../evidence/1.1.18/001/after/architectural-delta.json) and exits 2 with
`REVIEW_PAYLOAD_LIMIT`. The Praxis says a delta exceeding 128,000 encoded bytes
stops dependent processing; it requires bounded scope before AI interpretation.
No workaround, threshold increase, or whole-model architectural interpretation
has been applied. Raw validation/inventory and independent OpenSpec work continued.

**Requested disposition:** authorise interpretation confined to G14–G18 and their
directly incident relationships, with all other export changes retained as
unadjudicated inventory; or supply another bounded scope. This question was
submitted in the interaction. The later mapping-locator message supplies no
disposition on that question. Silence is not approval.

There are mechanically 120 added and 23 changed elements; 2 added and 222 changed
relationships; 69 added and 1 changed Views. No records are removed in those
three categories. These are raw observations, not CR counts or architectural
conclusions. Existing navigation/View differences are retained, not accepted as
transport effects. No exact no-edit transport control has been established.

<a id="dr-2"></a>
## DR-2 — G14–G18 Subject correspondence and hierarchy

The canonical hierarchy has 82 source-bound groups/subgroups in G01–G13.
The mapping proposes 32 additional entries: five groups and 27 subgroups.
No G14–G18 source-code binding currently exists in the governed Subject resources.
This is not proof that every proposal needs a new architectural identity.
The incoming raw Grouping inventory and mapping-versus-governed source-code
comparison are retained. Incoming identities, specialisations, rename/duplicate
correspondence, containment and relationship standing are **not reconciled**:
architectural interpretation is held at DR-1. No Subject was created or renamed.

After DR-1, establish each actual incoming counterpart using explicit identity and
source evidence; do not equate differently labelled groups by name or import
folder position. Preserve existing IDs, parent relationships, history and Views.
Determine whether extensions use the governed native Grouping treatment or propose
a new specialisation. Group/subgroup/statement Composition is subject classification
only under the representation contract, not Home, implementation ownership or
Capability decomposition. Do not treat any submitted edge as current/accepted
without its own standing assessment.

### DR-2.01 — G14 Patient Identity and Demographics

Six proposed subgroups, G14.01–G14.06. Review identity evidence, person correlation,
maintenance/status, demographics, disclosure and oversight separately. General
carer/contact relationships do not automatically belong under identity assurance.
Subject identity/containment remains unresolved pending bounded export review.

### DR-2.02 — G15 Provider and Workforce Management

Five proposed subgroups, G15.01–G15.05. Practitioner identity, qualifications,
affiliations/roles/locations, profile and lifecycle differ. The group title does
not establish HR/workforce or privilege-granting authority. Incoming correspondence
and any overlap with existing subjects remain unadjudicated.

### DR-2.03 — G16 Consent and Information Sharing

Six proposed subgroups, G16.01–G16.06. Capture/maintenance, scope, evaluation,
representatives, communication/history and emergency exceptions need distinct
classification. Representative contact information does not establish consent
authority or clinical/legal capacity. Incoming correspondence is unresolved.

### DR-2.04 — G17 Terminology and Reference Data

Five proposed subgroups, G17.01–G17.05. Preserve governed terminology meaning,
mapping, lifecycle, mutable runtime reference content, and stewardship as distinct
concerns. Existing G02 Information Management classification is not superseded by
this proposal. Incoming correspondence is unresolved.

### DR-2.05 — G18 Provider Directory

Five proposed subgroups, G18.01–G18.05. Discovery, publication/visibility,
maintenance, verification and synchronisation do not establish practitioner
registration, clinical qualification, or healthcare-organisation authority.
Incoming identity/containment remains unresolved.

<a id="dr-3"></a>
## DR-3 — UseCase and UserStory modelling

The current type catalogue has neither UseCase nor UserStory specialisation.
Source heading/identifier prefixes cannot establish native type. All 363 UseCase
and 421 UserStory candidates are therefore on hold for incorporation.
Decide whether these are independently bounded Requirement specialisations,
other native constructs, or source contributions to already represented meaning.
Any metamodel extension requires explicit definitions, native bases, template,
storage/naming/projection treatment and governed validation before creation.
Do not silently substitute FunctionalRequirement or allocate two UUIDs merely
because a UC source is expressed as both a title and a story.

<a id="dr-4"></a>
## DR-4 — Candidate identities and source contributions

All 784 rows map to 421 existing OpenSpec files and 421 manifest rows: 363 UC
sources each supply two candidates; 58 SR sources supply UserStory candidates only.
Source IDs, title/story wording and supplied actor fields compare exactly. The
27 absent primary-actor fields remain absent. Scoped source-plus-kind keys and
source-ID-plus-kind keys have no duplicates. Eighteen identical-wording groups
affect 38 rows; these are review leads, not duplicate-identity determinations.
No canonical source binding to these OpenSpec files, nor exact candidate title/text
lead in the scanned Motivation/Grouping resources, was found. This does not
establish semantic novelty or authorise creation.

Preserve original source IDs, source path/revision/hash, exact selector, full
UserStory field and SR/UC provenance separately. The irregular source identifier
`UC-1 of DoH_Requirements_Specification_PD v0.3.docx.` remains verbatim.
Decide semantic overlap and paired UC/story instance treatment before UUIDv4
allocation. No canonical IDs, aliases, relationships or tombstones were created.
See [duplicate review](../../evidence/1.1.18/001/after/reconciliation/duplicate-review.json)
and [all candidates](../../evidence/1.1.18/001/after/reconciliation/candidates.json).

<a id="dr-5"></a>
## DR-5 — Historical Capability targets

All 18 proposed target Exchange identifiers resolve to existing canonical
Capabilities/Features. Four refer to superseded records, affecting 208 rows.
Healthcare Organisation Profile Administration (142 rows) has explicit historical
`e485424b799c42aca7399d341ca02d0b` → current
`f2de9e9394b849f2943355c7a2882a15` correspondence. Governed Reference Dataset
Administration (16 rows) has explicit historical
`15d972931ed6447797c30a5ddb71737e` → current
`682b8684ad9e49d5a5c348a148081607` correspondence.

Provider Relationship (18 rows, `3bb8a3d5b62a4b779442290fe8d5106c`) and Provider
Engagement (32 rows, `a0777dbd9b0c4528817e99a7f34aa444`) are superseded but supply
no direct successor identity field. Do not manufacture a successor binding.
Current Features may instead be independently proposed on their definitions.
Retain old IDs and relationships for history/Views. Even explicit correspondence
does not establish that the mapping's original association is semantically valid.
See [target resolution](../../evidence/1.1.18/001/after/reconciliation/mapping-targets.json).

<a id="dr-6"></a>
## DR-6 — Capability and Feature coverage recommendations

The [784-row assessment](../../evidence/1.1.18/001/after/reconciliation/candidate-assessment.csv)
and [421-source rationale](../../evidence/1.1.18/001/after/reconciliation/source-alignment-review.json)
propose existing targets with explicit canonical IDs, definitions, source locators
and independent standing. There are 244 Feature fits for review, 248 partial
Feature fits with residual scope, and 292 CT3-only/gap/unresolved-scope rows.
Gaps/residuals affect 528 rows from 289 sources; 226 rows identify more than one
potentially relevant Capability/Feature. These categories describe review
triage, not established coverage, satisfaction, implementation, or ownership.
No new Capability or Feature is proposed as an allocated element.

For any future edge, decide native type, direction, endpoints, meaning, source
qualification and standing explicitly. No Realization or implementation
responsibility follows from these suggestions. Feature CT3 ancestry is not
inferred from a name or manufactured as Composition.

### DR-6.01 — Consent, representatives and emergency access

Patient Health Information Restriction Management explicitly respects the consent
owner; it is not that owner. Prefer consideration of Client Privacy Guardrail
Management for directive lifecycle and Consent Directive Evaluation only for
evaluating a requested use. Relationship validity, legal capacity, emergency
override, audit capture and appropriateness assessment remain distinct.
Health Information Sharing Programme Management is Candidate with unresolved
definition/tier/parent history; it is not defensible sole coverage for consent
history/alerts. Capture, renewal, withdrawal, emergency and notification Feature
gaps remain explicit. No patient access or emergency policy is invented.

### DR-6.02 — Person identity and related persons

Health Information Data Correlation explicitly excludes establishing person
identity. Consider Cross-Authority Identifier Correlation for issuing-namespace
identifier association. Distinguish Identity Assurance from contextual Identity
Qualification Control; neither covers all profile, guardian or relationship
activities. Consider Client Relationship Administration, Next-of-Kin & Guardian
Association and Relationship Validity Tracking for nominated relationships.
Externally decided corrections and source lineage may fit existing identity
Features; golden-record authority, probabilistic matching, local merge decisions
and interpreter-service fulfilment remain unestablished.

### DR-6.03 — Practitioner, organisation and directory information

Healthcare Organisation Profile Administration describes organisations, not every
practitioner record. Practitioner Profile Governance, National Practitioner
Verification, Practitioner-Organisation Affiliation, Role & Specialty Modeling,
Service-Location-Provider Binding and Electronic Endpoint Binding are specific
existing candidates for their respective actions. National Organisation
Verification remains separately relevant for HPI-O/organisation sources.
Identity Qualification Control is not clinical qualification or generic directory
data quality. Provider Directory Discovery & Resolution expressly does not decide
privilege or availability. Visibility, quality, duplicate resolution, approval,
interface monitoring and integration contracts retain gaps/residual scope.

### DR-6.04 — Terminology and mutable reference content

Consider Bidirectional Terminology Mapping for map/translation actions, keeping
mapping quality and source authority explicit. Terminology Governance covers
lifecycle/standing; Enterprise Reference Data Stewardship covers definitions and
governance; Governed Reference Dataset Administration and Governed Dataset Content
Revision address mutable approved runtime content. Preserve Calliope Models/DataSets
and Mneme/Mnemosyne boundaries. Do not claim a full release/version-validation or
API/query Feature exists from these broad fits.

### DR-6.05 — Operational and cross-cutting concerns

Audit, interface monitoring, failure reconciliation, source-update rules,
notifications and API delivery are not automatically covered by a business-functional
directory/identity Capability. Source-specific gaps are retained. Rhapsody-named
UC/SR records require an explicit portability/implementation-constraint decision;
an engine name supplies no Harmonia semantic authority. Unresolved coverage is
preferable to an invented Capability.

<a id="dr-7"></a>
## DR-7 — Primary and secondary Subject classification

Of 784 primary assignments, 780 name G14–G18 proposals and four name existing
G04.04. Every secondary Subject code resolves in the governed G01–G13 hierarchy.
Existence supplies neither semantic fit nor an edge. Advisory source-level review
flags 426 rows for a changed/unresolved primary classification; this is not an
accepted correction count. Original assignments remain intact in the ledger.

Examples: Record Consent/Withdrawal/Renewal is a capture/lifecycle concern rather
than scope alone; Verify Consent is evaluation; Maintain Data Ownership Information
is reference-data stewardship rather than directory verification; receiving
Tasmanian provider details is not intrinsically audit accountability; interface
monitoring is operational. General related-person/contact relationships need a
Subject decision: the supplied G14 proposal has no general client-relationship
subgroup, and G16.04 is specifically delegated consent. No additional subgroup is
created. Decide whether secondary classification uses an ordinary Association or
another explicit governed treatment; do not multiply primary containment by inference.

<a id="dr-8"></a>
## DR-8 — Release scope and source provenance limits

The available mapping describes extraction from `openspec.zip` and uses working
export 003 solely for names/IDs. Neither is canonical authority. The repository
OpenSpec project identifies `HIE - Target State - Use Cases - v0.5.docx` as its
upstream source. The original uploaded ZIP and upstream Word sources were not
located in the bounded repository search and were not independently verified.
This review proves correspondence to the current repository OpenSpec edition,
not fidelity to unavailable originals. Preserve that limit for acceptance.

Target-state SHALL wording does not assign R1/R2 delivery scope or expand current
domain authority. Preserve source scope and unresolved current/future allocation
for every candidate. In particular, Person Identity Administration retains its
history of upstream PAS/MPI reliance for R1/R2; no autonomous master-patient
reconciliation authority follows. Set no release, implementation owner or
satisfaction status without disposition. Root release, published XML, canonical
sources and existing local publication edits remain unchanged.

<a id="dr-9"></a>
## DR-9 — Bounded Minutiae and verification limits

Minutiae-Accepted: No

Comment: No Solution Architect acceptance recorded; this is an unaccepted group.

The formal Task retains the unavailable instruction-entry time and filename prefix
as UNRESOLVED. The formal Report uses the maintained completed-emission event
capture. Original source/working inputs are pinned; unique uncommitted mapping
bytes are retained in `before/`. No untracked input is claimed recoverable from HEAD.

Current canonical schema, provenance, navigation, containment, Exchange references,
offline XSD and exact baseline checks pass. Incoming Exchange XSD/identifier/
reference checks report no issues. Technical validation is not architectural
acceptance. The full delta remains UNRESOLVED at the 128 KB gate. The required
Java architecture test command stopped in Maven dependency resolution because
`~/.m2/.../resolver-status.properties` is read-only; suite outcome is UNRESOLVED,
not an executed test failure. No unchanged retry or production-code correction
was attempted. Actual Archi transport/inspection and publication were not performed.
Detailed [verification](../../evidence/1.1.18/001/after/reconciliation/verification.json)
records source/wording/identity preservation and evidence consistency checks.

No commit, push, incorporation, accepted release increment, governance closure or
Solution Architect confirmation occurred. DR-1 must be dispositioned before
dependent export interpretation; all application decisions remain separate.
