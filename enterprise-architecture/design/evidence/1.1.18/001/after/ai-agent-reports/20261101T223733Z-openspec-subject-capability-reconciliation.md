Report-Produced-At: 2026-11-01T22:37:33.055483Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

# OpenSpec Subject and Capability reconciliation preparation

Prepared by Codex, 2026-11-02 Australia/Sydney (2026-11-01 UTC).
Boundary: 1.1.18 / 001. **Review preparation only; no incorporation or acceptance.**

The 784 OpenSpec mapping rows have a complete source/identity check and advisory
Capability assessment. **G14–G18 architectural reconciliation is not complete:**
the full incoming delta triggered the governed 128 KB interpretation limit.
The required bounded-scope disposition remains pending. This report records the
completed independent work and the precise remaining gate; it does not claim that
all requested reconciliation outcomes have been achieved.

## Inputs, versions and authority

Root `architecture-release.json` reads 1.1.18, previous 1.1.17. No release value
was changed or new transition allocated. Source HEAD is
`763f97586685d5c4b9744509b067d95b9694d0db`, with retained/pinned uncommitted inputs.
Markdown Continuum is authority; export 004 and the mapping are proposed inputs.

| Input | Size | SHA-256 |
| --- | ---: | --- |
| Current published 1.1.18 baseline | 27,091,699 | `7df92528a09f9a765ddebec105643bae08ac92583cad4e8901b3135d49db2c2e` |
| Incoming `Harmonia-Continuum-1.1.18-001-004.xml` | 29,121,058 | `4ddfaa9045d8d0196f1003c9344d85eb2f121398a79a579fbb6c18545470c0de` |
| Supplied expanded mapping | 414,779 | `b6f59ce36ad4b7719fd0e954e4c4b0e86a874fe5cab2459c1804019e43944094` |

The mapping was located at the user-confirmed
`enterprise-architecture/design/archimate/working/1.1.18/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md`.
Its exact bytes are retained in [before evidence](../../before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md).
Its claim of extraction from an uploaded `openspec.zip` and its use of working
export 003 for Capability names/IDs remain source provenance, not authority.
The ZIP and original Word sources were not located in the bounded search.
The available OpenSpec project cites `HIE - Target State - Use Cases - v0.5.docx`.
This assessment verifies the repository's OpenSpec edition, not unavailable originals.
The 421 files and manifest are recoverable from the pinned source state; untracked
working inputs are not claimed recoverable from that commit.

The governing Review/Provenance, Round-Trip, representation and incorporation
Praxes were loaded. AX-04 and AX-08 constrain tool authority and meaningful evidence;
AX-14 separates identity, classification, coverage, currentness and acceptance;
AX-15/17 preserve uncertainty; AX-18 preserves external decision authority.
The approach preserves these axioms by writing only review/evidence artefacts,
retaining existing architecture and exposing missing decisions. No implementation
authority or architectural completeness is inferred from source SHALL statements.

## Subject reconciliation status

The Continuum contains 82 governed source-bound G01–G13 groups/subgroups.
Their native type is Grouping with no specialisation. Existing identity, standing,
relationships and history were retained. The mapping proposes:

| Group | Proposed name | Subgroups |
| --- | --- | ---: |
| G14 | Patient Identity and Demographics | 6 |
| G15 | Provider and Workforce Management | 5 |
| G16 | Consent and Information Sharing | 6 |
| G17 | Terminology and Reference Data | 5 |
| G18 | Provider Directory | 5 |

These 32 mapping entries have no existing governed G14–G18 source-code binding.
This does not establish that they are all new architectural instances.
Raw incoming Groupings and source-code comparisons are retained in
[incoming inventory](../reconciliation/incoming-grouping-inventory.json),
[governed Subjects](../reconciliation/governed-subjects.json) and
[label observations](../reconciliation/subject-label-observations.json).
Label observations are navigation leads only; they establish no identity.

The maintained normalised review validates the exact current baseline, then
emits a 3,824,520-byte delta and exits 2 with `REVIEW_PAYLOAD_LIMIT`.
The Round-Trip Praxis requires bounded scope before interpreting such a delta.
The proposed disposition is to interpret only G14–G18 and directly incident
relationships, leaving other export changes as unadjudicated inventory.
No answer is recorded. The mapping-locator follow-up does not supply that decision.
Incoming Subject names, specialisations, identities, containment and relationship
semantics therefore remain unreconciled. No new Subject was created and no
existing one was merged, retyped, renamed or retired.

Mechanically, the export has 120 added/23 changed elements, 2 added/222 changed
relationships and 69 added/1 changed Views, with zero removed records in those
categories. All 120 new elements lack canonical IDs. No canonical representation
rebindings or representation anomalies are reported. These counts are not CRs;
the origin and meaning of remaining View/navigation differences are unadjudicated.
No exact no-edit control or actual Archi inspection is established for this input.

## Motivation reconciliation

| Source domain | Sources | UseCase candidates | UserStory candidates |
| --- | ---: | ---: | ---: |
| Consent and information sharing | 69 | 69 | 69 |
| Cross-domain services | 8 | 8 | 8 |
| Governance, quality and audit | 7 | 7 | 7 |
| Patient identity management | 127 | 127 | 127 |
| Provider directory integration | 109 | 51 | 109 |
| Provider management | 66 | 66 | 66 |
| Terminology and reference data | 35 | 35 | 35 |
| **Total** | **421** | **363** | **421** |

Every mapping row resolves to its repository source. All source IDs and exact
title/UserStory fields match; supplied actor fields also match. Twenty-seven
sources omit primary actor, and those omissions remain explicit. No source files
are omitted or added relative to the mapping/421-row manifest.

No existing Continuum binding to these exact OpenSpec source paths, or exact
candidate title/story-text lead in the scanned Motivation/Grouping resources,
was found. All 784 remain **proposed instances with semantic novelty unresolved**;
zero were classified as established existing instances, and zero were incorporated.
This is a bounded identity search, not proof that no existing architectural
requirement expresses overlapping meaning. The current catalogue defines neither
UseCase nor UserStory specialisation. Native type, independent-instance treatment
and any metamodel extension require Solution Architect adjudication.

There are zero duplicate scoped-source/type or source-ID/type keys. Eighteen
identical-wording groups affect 38 rows, including stories reused across distinct
SRs and repeated actor-specific UC titles. These are not automatic duplicates.
The 363 UC title/story pairs require explicit treatment as separate elements or
source contributions before any UUID allocation. The irregular source identifier
`UC-1 of DoH_Requirements_Specification_PD v0.3.docx.` is preserved exactly.
See [duplicate findings](../reconciliation/duplicate-review.json).

Of the original primary Subject assignments, 780 reference proposed G14–G18
subgroups and four reference governed G04.04. All secondary codes exist in G01–G13;
this proves reference existence, not classification fitness or a relationship.
The advisory source review flags 426 rows for changed/unresolved primary placement.
It retains the original mapping alongside each recommendation. Examples include
consent capture versus scope/evaluation, reference-data ownership placed under
directory verification, participant searches placed under provider concerns, and
operational interface monitoring. General client relationships lack a clear
subgroup in the supplied proposal; delegated consent must not absorb all carers,
contacts, guardians and related-person information. No subgroup is invented.

## Capability and Feature alignment

All 18 supplied target Exchange IDs resolve to canonical Capability records.
Their separate canonical IDs and standing are retained in
[target resolution](../reconciliation/mapping-targets.json).
Four targets are superseded, affecting **208 rows**:

| Historical target | Rows | Treatment |
| --- | ---: | --- |
| Healthcare Organisation Profile Administration | 142 | Explicit historical-to-current correspondence retained; applicability still needs review |
| Governed Reference Dataset Administration | 16 | Explicit historical-to-current correspondence retained; no silent retargeting |
| Provider Relationship | 18 | Superseded; no direct successor field establishes continuity |
| Provider Engagement | 32 | Superseded; no direct successor field establishes continuity |

Historical resources and existing relationships remain intact. Choosing an existing
Feature by its documented responsibility is an advisory association proposal,
not a declaration that it succeeds an old Capability identity.

| Advisory assessment | Candidate rows |
| --- | ---: |
| Existing Feature fit for review | 244 |
| Partial existing Feature fit with residual scope | 248 |
| CT3-only fit, Feature gap or unresolved coverage | 292 |
| **Total** | **784** |

Across these categories, 528 rows from 289 sources have explicit gaps/residuals,
and 226 rows identify multiple relevant existing targets. A CT3 recommendation
does not imply the entire source contract is covered. No new Capability/Feature,
containment edge, satisfaction claim, implementation allocation or Realization
was created. Every suggested record is current, while its Canonical/Proposed/
Candidate and structural standing remains independently visible.

The principal architectural discrepancies are:

- Person identity correlation differs from health-information correlation, whose
  definition explicitly excludes establishing person identity. Cross-Authority
  Identifier Correlation is a more specific existing candidate for identifier links.
- Identity Qualification Control evaluates contextual identity sufficiency; it is
  neither clinical qualification nor generic provider/reference data quality.
- Practitioner information differs from healthcare-organisation profiles.
  Practitioner Profile Governance, National Practitioner Verification, affiliation,
  role, service-location and endpoint Features offer narrower candidates.
- Person assurance/model governance does not cover all guardian/contact/related-person
  administration. Client relationship and effective-scope Features are relevant;
  relationship data does not establish legal authority.
- Consent capture/withdrawal/renewal differs from evaluation of a requested use.
  Client Privacy Guardrail Management retains directive responsibility; restriction
  management respects it. Sharing Programme Management remains Candidate with
  definition/placement history unresolved, and is weak sole coverage for consent
  history or alerts. Emergency override policy remains unestablished.
- Terminology governance, semantic mapping/binding, reference stewardship and mutable
  runtime dataset content are distinct. Governed Dataset Content Revision is an
  existing narrow candidate for authorised content revisions.
- Directory discovery does not determine privilege or availability. Publication,
  integrity/duplicate review, approvals, interface recovery, API contracts, audit
  versus history presentation, and interpreter fulfilment retain explicit gaps.

The [784-row CSV](../reconciliation/candidate-assessment.csv)
provides original text, identity outcome, original/proposed Subject, original target,
recommended existing IDs, rationale and unresolved scope. The
[421-source review](../reconciliation/source-alignment-review.json)
includes the exact original stories and target definitions/standing. The
[complete candidate ledger](../reconciliation/candidates.json)
adds source hashes, secondary Subject resolutions and all hold reasons.
This is an assessment for adjudication, not a completed assertion-by-assertion
incorporation or a finding that any source requirement is satisfied.

## Scope, provenance and recommendations

No current/future delivery release was assigned. Target-state wording is preserved
without being promoted into R1/R2 commitments. Existing person identity history
retains upstream PAS/MPI reliance and excludes an autonomous master-patient
reconciliation engine for R1/R2. Golden identity, local merge/correction authority,
clinical consent/legal capacity and privilege decisions require explicit source
authority. Rhapsody-named requirements retain their original wording; portability
or implementation-constraint treatment requires review.

The pending [HCRL](../../../../../review/1.1.18/hcrl-1.1.18-001.pending.md)
requests separate dispositions on bounded export scope; each G14–G18 correspondence;
native modelling/instance semantics; duplicate/source contributions; historical
target treatment; domain-specific Capability/Feature recommendations; Subject
classification; source/release scope; and provenance/verification limits.
The [specialised checkpoint](../../../../../review/1.1.18/change-review.md)
is explicitly incomplete at the scope gate and allocates no architectural CRs.
Recommendations are not executable reconciliation instructions.

## Verification and resulting state

- **PASS:** current canonical element schema, provenance, identity, navigation,
  containment, offline XSD and Exchange references; baseline equals the maintained
  current projection byte-for-byte (1,842 elements, 1,882 relationships).
- **PASS:** incoming secure XML/XSD/identifier/reference checks report no issues;
  all 784 candidate/source/wording checks; exact mapping snapshot; all recommendation
  IDs resolve to current existing records. Technical validity supplies no acceptance.
- **UNRESOLVED:** the full review delta exceeds 128 KB; Subject interpretation,
  exact submitted-to-Subject correspondence and CR decomposition remain pending.
- **UNRESOLVED:** the required Java architecture suite command failed in dependency
  resolution after approximately two seconds because Maven attempted to write
  read-only `~/.m2/.../resolver-status.properties`. No architecture tests executed;
  this is an environment limitation, not an executed test failure. The
  [log](../reconciliation/architecture-tests.log)
  is retained. The command was not retried unchanged.
- **UNRESOLVED:** the interaction exposes no attributable instruction-entry time;
  the Task event and filename prefix remain explicitly unresolved for Solution
  Architect treatment before closure. A later clock reading was not substituted.
- **NOT PERFORMED:** canonical incorporation, actual Archi transport/visual
  inspection, publication, release transition, commit, push or governance closure.

The initial system Python lacked `jsonschema`; the existing
`/tmp/harmonia-tooling-venv` supplied the maintained dependencies for successful
technical checks. No repository tooling correction or dependency installation
was needed. Source/evidence consistency, protected input hashes and links are
verified in [verification evidence](../reconciliation/verification.json).
The pre-existing local `motivation.tex` edit, publication by-products, Archi working
files, baseline, export, OpenSpec sources and Continuum were preserved. Required
evidence lives under `enterprise-architecture/design/evidence/1.1.18/001/`;
the `.junie` report is a reading copy of the formal governed Report.

**Next required action:** Solution Architect disposition on DR-1's bounded scope.
Then complete Subject/relationship interpretation and stable CR preparation before
any incorporation directive. All modelling, coverage and release decisions remain
at their separately recorded review boundaries.

Corresponding formal [Task](../../before/ai-agent-tasks/unresolved-openspec-subject-capability-reconciliation.md) preserves operative wording, locator follow-up, constraints and unresolved instruction-entry provenance.
