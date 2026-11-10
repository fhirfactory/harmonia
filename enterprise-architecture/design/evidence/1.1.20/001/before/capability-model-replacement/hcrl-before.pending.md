<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Bounded export review and remaining incoming semantic changes | CLOSED — historical incoming export reconciliation | Finalisation Task disposition; Historical evidence retained; incorporated accepted changes remain authoritative; no acceptance of unincorporated residual proposals. |
| [DR-2](#dr-2) | G14–G18 accepted Subject hierarchy | ACCEPTED — inherited G14–G18 Subject hierarchy | Finalisation Task disposition; Inherited baseline accepted; no repeat incorporation or change to independent naming qualifications. |
| [DR-3](#dr-3) | UseCase and UserStory modelling | ACCEPTED — Requirement specialisations; creation authorised | Finalisation Task disposition; UseCase/UserStory use SolutionFacet-based naming; no instances or metamodel/naming implementation incorporated in this transition. |
| [DR-4](#dr-4) | Candidate identity and source contributions | ACCEPTED — independent identities and semantic equivalence | Finalisation Task disposition; Retain all source contributions/provenance; no candidate merge, equivalence finding or identity allocation performed. |
| [DR-5](#dr-5) | Historical Capability targets | REVIEWED WITH QUALIFICATIONS — Capability Model adjudication deferred | Finalisation Task disposition; Conditional carry-forward to 1.1.20 authorised; no historical Capability/Feature adjudication, retargeting or successor relationship created. Subsequent bounded disposition: Pass3/014 FR-001–FR-009 offline incorporation ACCEPTED subject to retained evidence/qualifications; HCRL remains pending. |
| [DR-6](#dr-6) | Capability and Feature coverage, including DR-6.01–DR-6.05 | ACCEPTED — multi-Feature/multi-Capability partial coverage semantics | Finalisation Task disposition; Coverage does not establish implementation satisfaction; actual coverage reconciliation/gap adjudication and five subordinate matters deferred. |
| [DR-7](#dr-7) | Primary and secondary Subject classification | Unresolved — carried forward | Preserved originating disposition/provenance: Transfer authorised; substantive disposition unresolved. Deferred unresolved; supplied assignments retained |
| [DR-8](#dr-8) | Source provenance, delivery scope and domain authority | Unresolved — carried forward | Preserved originating disposition/provenance: Transfer authorised; substantive disposition unresolved. Deferred unresolved; original source limits retained |
| [DR-9](#dr-9) | Historical Task times, supporting evidence and assurance limitations | UNRESOLVED — bounded temporal exception authorised | Preserved originating disposition/provenance: Final closure Task disposition 2; original transfer/history preserved. Three inherited and eight current Task events/prefixes may remain UNRESOLVED; assurance limitations and future requirements preserved |
| [DR-10](#dr-10) | G18.03 spelling | Unresolved — carried forward | Preserved originating disposition/provenance: Inherited accepted baseline; no new action. Completed and accepted; inherited baseline only |
| [DR-11](#dr-11) | Previously blocked presentation reconciliation | Unresolved — carried forward | Preserved originating disposition/provenance: Inherited accepted baseline; no new action. Superseded blocking state; completed by DR-12/DR-13, history preserved |
| [DR-12](#dr-12) | 115 Groupings and distinct Composition/Home completion | Unresolved — carried forward | Preserved originating disposition/provenance: Inherited accepted baseline; no new action. Completed and accepted with preserved naming qualifications |
| [DR-13](#dr-13) | Five approved elements and complete authored Views | Unresolved — carried forward | Preserved originating disposition/provenance: Inherited accepted baseline; no new action. Completed; exact candidate visually accepted by the Solution Architect |
| [DR-14](#dr-14) | Inherited canonical naming, definition and standing qualifications | Unresolved — carried forward | Preserved originating disposition/provenance: Transfer authorised; substantive disposition unresolved. Deferred unresolved; no acceptance inferred from publication |
| [DR-15](#dr-15) | Generated-reader structural review | ACCEPTED — actual Solution Architect review | Preserved originating disposition/provenance: Final closure Task disposition 3; prior deferral history preserved. Retained three scopes and applicable DR-18/DR-19 additions reviewed and accepted; no exhaustive page/element inspection asserted |
| [DR-16](#dr-16) | Actual no-edit Archi transport qualifications | ACCEPTED WITH QUALIFICATION — remediation deferred | Preserved originating disposition/provenance: Final closure Task disposition 1; actual strict FAIL preserved. Exactly 575 relationship identities / 1,741 xml:lang differences; evidence retained; no content-loss acceptance or general exemption |
| [DR-17](#dr-17) | SolutionFacet refactoring and final naming dispositions | ACCEPTED — visual/model acceptance and bounded Minutiae | Preserved originating disposition/provenance: Earlier exact-candidate acceptance plus final closure Task disposition 4. Minutiae-Accepted: Yes for the disclosed supporting group only; all prior decisions and qualifications retained |
| [DR-18](#dr-18) | Provenance Management | ACCEPTED — Solution Architect architectural content acceptance | Preserved originating disposition/provenance: Prior formal acceptance reaffirmed by final closure Task; history retained. Six definitions, ten relationships and Mneme policy accepted unchanged; detailed design unknowns preserved; conditional whole-HCRL closure authorised |
| [DR-19](#dr-19) | Provenance and Assurance | ACCEPTED — Solution Architect architectural content acceptance | Preserved originating disposition/provenance: Prior formal acceptance reaffirmed by final closure Task; history retained. Assurance/Audit/Provenance boundaries accepted; final whole-HCRL confirmation recorded; release conflict in DR-20 awaits adjudication |
| [DR-20](#dr-20) | Existing 1.1.19 publication and accepted-candidate release conflict | DISPOSITION APPLIED — retained immutable baseline; candidate changes carried | Current explicit Solution Architect DR-20 disposition; accepted 1.1.19 preserved, full difference inventory transferred as qualified candidate work to 1.1.20. |

# HCRL 1.1.20 / 001 — Active architectural development baseline

**ACTIVE — PENDING; 1.1.20 is a development identity, not an accepted reference release.**

Accepted predecessor: **1.1.19**, existing immutable XML SHA-256
`9d4b6e27d69bf8999144ca474acc0e6264f0a2de9d42a8ee41f042cdf6095d09`
(1,994 elements / 2,235 relationships / 1,285 Views). Original release acceptance
remains HCRL 1.1.18/001. [Predecessor review closure](../../register/1.1.19/hcrl-1.1.19-002.complete.md).

The canonical working tree retains the complete carried candidate (1,996 elements /
2,246 relationships / 1,288 Views). It is an explicit overlay on that predecessor,
with original identities, provenance, source standing and scoped acceptance preserved.
No whole-candidate/release acceptance is inferred. The original exact candidate
and its source-recovery overlay remain in originating 1.1.19/001 evidence.

[Deterministic assessment](../../evidence/1.1.19/002/after/dr20-disposition/difference-assessment.md) · [Complete identity/provenance inventory](../../evidence/1.1.19/002/after/dr20-disposition/difference-assessment.json).
All 2 added and 251 modified elements, 11 added relationships and 3 added/12 modified
Views are carried. No removed records exist; no change is discarded. New 1.1.20
candidate projection changes only declared release metadata; it supplies no new acceptance.

The opening table retains every actual originating disposition. Closed and accepted
baseline facts are references, not reopened work. Outstanding implementation, naming,
coverage, source-authority and assurance questions remain explicit. Original evidence
stays at its originating iteration; the new baseline records correspondence rather
than duplicating historical packages. No consolidation programme has commenced.


<a id="dr-1"></a>
## DR-1 — Bounded export review and remaining incoming semantic changes

**Preserved Solution Architect disposition:** CLOSED — historical incoming export reconciliation.

**Origin/history:** [1.1.19/002 DR-1](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-1).

Historical incoming export reconciliation remains closed. Preserve its evidence and accepted incorporated meaning; do not resume historical export processing.

<a id="dr-2"></a>
## DR-2 — G14–G18 accepted Subject hierarchy

**Preserved Solution Architect disposition:** ACCEPTED — inherited G14–G18 Subject hierarchy.

**Origin/history:** [1.1.19/002 DR-2](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-2).

G14–G18 remains accepted inherited baseline; no repeat incorporation or identity change.

<a id="dr-3"></a>
## DR-3 — UseCase and UserStory modelling

**Preserved Solution Architect disposition:** ACCEPTED — Requirement specialisations; creation authorised.

**Origin/history:** [1.1.19/002 DR-3](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-3).

Realisation of UseCase/UserStory Requirement specialisations and SolutionFacet naming is authorised but unexecuted. Current candidate has zero such instances. Preserve the 784-candidate assessment; reconciliation follows the adjudicated Capability Model.

<a id="dr-4"></a>
## DR-4 — Candidate identity and source contributions

**Preserved Solution Architect disposition:** ACCEPTED — independent identities and semantic equivalence.

**Origin/history:** [1.1.19/002 DR-4](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-4).

Independent canonical identities and semantic-equivalence principles are accepted. Candidate-level equivalence, merges and identity allocation remain unperformed; retain every source contribution/provenance.

<a id="dr-5"></a>
## DR-5 — Historical Capability targets

**Preserved Solution Architect disposition:** REVIEWED WITH QUALIFICATIONS — Capability Model adjudication deferred.

**Origin/history:** [1.1.19/002 DR-5](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-5).

Historical Capability/Feature adjudication and gap analysis remain explicitly deferred to the three-pass programme below. No Superseded successor or coverage conclusion is invented.

**Intended programme — not commenced:**

1. **Pass 1 — Historical Capability/Feature Adjudication:** Codex prepares the reconciliation table. The Solution Architect and ChatGPT review proposals for acceptance, rejection or expunction; final architectural disposition remains with the Solution Architect. Completion requires no historical Capability or Feature pending adjudication.
2. **Pass 2 — Capability Model Gap Analysis:** ChatGPT examines the adjudicated model and proposes missing Capabilities/Features for Solution Architect review; Codex incorporates accepted additions.
3. **Pass 3 — Solution Architect Refinement:** Independently identified Solution Architect corrections/refinements are reviewed and incorporated through the governed process.

The programme also preserves reconsideration of relevant Superseded elements, definition/placement/standing/relationship reconciliation, coverage gaps, subsequent UseCase/UserStory reconciliation and the full 784-candidate/source assessment. Its execution is outside the closure/initialisation task.

### Pass 1 preparation — inventory submitted for adjudication

The separately issued Solution Architect [Pass 1 Task](../../evidence/1.1.20/001/before/ai-agent-tasks/20261103T065659Z-pass1-historical-capability-feature-reconciliation.md),
issued at **2026-11-03T06:56:59.964Z**, authorises evidence-based reconciliation
and recommendations only. This begins preparation after the opening baseline's
“not commenced” account above; it supplies no model-incorporation authority.

The [reconciliation report and review guide](../../evidence/1.1.20/001/after/pass1-historical-reconciliation/README.md)
and [complete adjudication inventory](../../evidence/1.1.20/001/after/pass1-historical-reconciliation/adjudication-inventory.md)
account for **842 existing Capability/Feature identities**. The inventory includes
**436 identities: 388 Capabilities and 48 Features** — **32 Pending Adjudication,
246 Superseded and 158 existing current Proposed/Candidate carry-forward records**.
The additional current records remain separately identified because currentness
does not accept their semantic standing. They are existing proposals, not new
Pass 2 gap discoveries. The remaining 406 current Canonical/accepted records
supply comparison context. The 784 OpenSpec assessments are a different population;
no UseCase/UserStory reconciliation is performed.

**Codex recommendations, not Solution Architect dispositions:** 189 ACCEPT,
246 RETIRE and one REJECT. ACCEPT concerns the bounded architectural concept,
not automatic retention of an overlapping historical representation. RETIRE
preserves identities/provenance and, for already Superseded resources, normally
confirms their existing inactive treatment without a new state transition. The
sole REJECT concerns incorporation of the underdefined sharing-programme addition;
its Candidate record and proposed elaboration remain preserved.

The inventory preserves the **214 already governed correspondences** (209 direct,
five evolved) without reopening them or promoting successor standing. Other
comparisons distinguish overlap, different responsibility and unresolved meaning;
no new equivalence or successor is asserted. Coordinated SA judgement remains
necessary for internal/external work, subject/service assurance, physical/clinical
credentials, protocol/patient communication, general security Feature framing,
audit/provenance/source authority, requirement publication, collective readiness
and the device-maintenance membership conflict. The current external-work CT2's
retained internal-registry source contribution is exposed as a scope tension,
not resolved by widening its definition. Detailed consequences and independently
traceable dependencies belong to the linked inventory.

**Verification:** canonical validation PASS (1,996 elements / 2,246 relationships);
the [accounting and preservation checks](../../evidence/1.1.20/001/after/pass1-historical-reconciliation/verification.json)
record exact population/row/source correspondence and preservation results.
No Capability Model, canonical identity, definition, classification, standing,
relationship, navigation, candidate or accepted 1.1.19 publication is changed.
DR-4, DR-6 including its five subordinate matters, DR-14 and all inherited
qualifications retain their recorded scope. The pending HCRL is not advanced,
completed or accepted; no release is allocated.

[Formal preparation Report](../../evidence/1.1.20/001/after/ai-agent-reports/20261103T071256Z-pass1-historical-capability-feature-reconciliation.md) preserves
the actual outcome, limitations, Task correspondence and production event.

**Handover:** every inventory SA decision is **PENDING** and every incorporation
status **NOT AUTHORISED**. Preparation is complete to the evidence-producing
boundary; **Pass 1 is not complete**. The next activity is Solution Architect and
ChatGPT adjudication of the individual rows under this DR.

### Pass 3 XD incorporation — required offline baseline unavailable

The Solution Architect's current Task authorises offline incorporation of accepted
XD-001–XD-028 Revision 2 and catalogue-wide AR-001/AR-002, preserving accepted SR
and identity history, with no Continuum publication or release mutation. This is
subsequent programme activity; earlier preparation/history above remains unchanged.
Baseline establishment is **UNRESOLVED**: neither the named Pass3/011 catalogue nor
its adjudication workbook was located. Available Candidate 013 (503 CT3s / 2,529
Features) and Pass3/002 (587 / 2,948) do not establish the specified 586 / 2,941
baseline. No substitution, incorporation, semantic normalisation or final validation
is performed. Exact source recovery or explicit bounded Solution Architect recovery
disposition is required under AX-17 before dependent work. Accepted XD intent is
not reopened. Task issuance temporal provenance remains unresolved for treatment
before closure. [Task](../../evidence/1.1.20/001/before/ai-agent-tasks/unresolved-pass3-xd-incorporation-baseline.md) ·
[baseline blocker Report](../../evidence/1.1.20/001/after/ai-agent-reports/20261109T092021Z-pass3-xd-incorporation-baseline-blocker.md).

### Pass 3 XD incorporation — recovered baseline and offline review result

The subsequently supplied Pass3/011 catalogue and retained workbook resolve the
source-availability blocker above; baseline population is verified at **586 CT3 /
2,941 Features**. The Solution Architect's accepted complete Revision 2 governs
XD-001–XD-028 and AR-001/AR-002. In this activity the Solution Architect explicitly
selected **“Authorise this refinement of E06.02.03.F03”** for the concrete additive
selective Stimulus contextualisation wording retained in the Task. Twin event
context and its original criterion remain, with no new Feature or reassignment
of recovery, custody or input allocation.

All 28 XD dispositions are incorporated into the **offline Pass3/012** catalogue.
Active identity sets and containment remain unchanged (24 CT1 / 145 CT2 / 586 CT3 /
2,941 Features); existing retirements and transfers are preserved. Canonical
Feature semantics are separated from measurements and source status across the
catalogue: 2,030 definitions refined beyond status cleanup, 17 CT3 descriptions
refined, original source criteria/status retained by ID. Policy-governed selective
or all-or-nothing disclosure, critical-result recipient/acknowledgement/follow-up
coverage, comprehensive model versus information-instance verification, enterprise
continuity versus pre-processing preservation, longitudinal source associations,
selective Twin information contextualisation and reference versus payload/access
boundaries remain explicit. This is not implementation verification or acceptance.

**Verification and qualifications:** 36 automated structural, source-preservation,
bounded semantic and workbook OOXML/data checks PASS. Spreadsheet application
load/export remains UNRESOLVED after bounded launcher failures; no parse defect
or successful load is inferred. Pre-existing accepted-SR correspondence discrepancies
(SR-001 consolidation not reflected in the baseline; SR-004 rename not reflected;
SR-015/SR-016 semantic correspondence; SR-017 reversed client/server narrative)
remain explicit for Solution Architect treatment. Accepted SR decisions/history
are preserved unchanged; unrelated SR follow-up and FR-001–FR-009 are not applied
or reopened. Task issuance timestamps/prefix remain UNRESOLVED before closure.
No unqualified whole-programme reconciliation or architectural closure is asserted.

[Detailed qualified review result](../../evidence/1.1.20/001/after/pass3-xd-consolidation/reconciliation-report.md) ·
[Formal execution Report](../../evidence/1.1.20/001/after/ai-agent-reports/20261109T094506Z-pass3-xd-incorporation-and-semantic-normalisation.md) ·
[operative Task/disposition](../../evidence/1.1.20/001/before/ai-agent-tasks/unresolved-pass3-xd-incorporation-baseline.md).
The candidate remains offline; no Continuum/accepted-publication/release mutation,
iteration advance, completion, commit or push is performed.

### Pass 3 SR reconciliation — confirmed decisions incorporated offline

The Solution Architect's separately issued [SR reconciliation directions](../../evidence/1.1.20/001/before/pass3-sr-reconciliation-issued-directions.md)
confirm and authorise **SR-001, SR-004, SR-015, SR-016 and SR-017** against the exact
completed Pass3/012 package. The [formal Task](../../evidence/1.1.20/001/before/ai-agent-tasks/unresolved-pass3-sr-reconciliation.md)
retains that scope; instruction-entry time/prefix remain explicitly UNRESOLVED.
This resolves the five baseline correspondence issues recorded above without
reopening their accepted decisions or erasing the earlier qualified history.

The resulting **offline Pass3/013** catalogue incorporates all five decisions:

- **SR-001:** retained B05.03.01 and its six Feature identities; retired B05.03.04
  and F01–F05 after mapping every unique obligation and original criterion to
  existing survivors. Accountable identity-assured clinical acceptance is distinct
  from receipt; context, outstanding ownership, timing/delay, rejection/escalation
  and traceable evidence remain explicit. E06.08.05 operational transfer and
  B07.04.05 human operational handover are unchanged. Existing qualified
  contribution references/findings are carried unchanged to the clinical survivor.
- **SR-004:** E06.03.01 is Governed Parcel Handoff & Accountability; E06.03.03 is
  Routing Decision Implementation. Selection, delivery attempt and accepted handoff
  remain distinct, with accountability, correlation and evidence. No Feature moves;
  XD-012 decision and actual-movement evidence definitions remain unchanged.
- **SR-015:** E06.08.07 is Ergon Output Processing & Continuation; E06.09.07 is
  Long-Duration Asynchronous Activity Management. Ergon/activity/Task/authoritative
  commitment outcomes remain distinct; no automatic extra Task or mechanism
  dependency. All inherited recovery, subscription and lineage obligations remain.
- **SR-016:** B13.04.02 is Research Information De-identification & Minimisation;
  B14.06.01 is Participant-Contextual Information Protection. Research utility,
  re-identification risk, contextual protection, consent/authorisation/disclosure
  and external domain authority remain distinct. All inherited safeguarding,
  wellbeing, withdrawal, risk and response obligations remain.
- **SR-017:** current workbook narrative now correctly identifies E03.06.04 as
  server to external clients and E03.06.05 as client to external servers. Both CT3
  definitions and their ten Features remain unchanged; original narrative is history.

Population reconciles from **24 CT1 / 145 CT2 / 586 CT3 / 2,941 Features** to
**24 / 145 / 585 / 2,936** solely through SR-001. Seven CT3 and 33 Feature definitions
are refined; no surviving Feature names, identifiers or containment change. All
2,941 original source criteria/status records remain traceable, including the five
consolidated sources; original criteria do not alone assure every refined scope.

**Verification:** 62 automated catalogue/OOXML/data checks PASS; repeated builds
produce identical candidate/workbook bytes. Both workbooks load with available
standard-library OOXML tooling. All 28 XD decision records and their 69 active
referenced definitions are unchanged; historical referenced identities stay retired.
AR-001/AR-002 and original input bytes are preserved. No unresolved architectural
conflict or deferred incorporation remains within these five SR decisions.
**Qualifications:** external office application load/export/visual inspection and
original Task event times/prefixes remain UNRESOLVED; no implementation or
whole-candidate acceptance is asserted. FR and other excluded work retain their
standing. Continuum, accepted publications, release authority and completed
register are unchanged; no HCRL close/advance, publication, commit or push.

[Detailed SR result](../../evidence/1.1.20/001/after/pass3-sr-reconciliation/reconciliation-report.md) ·
[Formal execution Report](../../evidence/1.1.20/001/after/ai-agent-reports/20261109T101507Z-pass3-sr-reconciliation.md) ·
[Source-result manifest](../../evidence/1.1.20/001/after/pass3-sr-reconciliation/source-result-manifest.json).
The candidate and 29-sheet workbook remain offline for Solution Architect review.

### Pass 3 FR reconciliation — nine accepted decisions incorporated offline

The Solution Architect's [accepted FR-001–FR-009 package](../../evidence/1.1.20/001/before/Harmonia_1.1.20_FR001-FR009_Accepted_Adjudication_Package.md)
supplies the semantic dispositions. Before mutation the package's canonical-target
assumption was raised against the actual offline Pass3/013 candidate. The Solution
Architect explicitly dispositioned: **“Authorise offline Pass3/014 reconciliation;
preserve the Continuum”**. The [formal Task](../../evidence/1.1.20/001/before/ai-agent-tasks/unresolved-pass3-fr-reconciliation.md)
preserves the question, answer and correspondence. This resolves the target scope;
canonical incorporation/identity allocation remains separately unauthorised.
Earlier reviewed records and application facts above remain unchanged.

All nine accepted decisions are incorporated in a distinct **offline Pass3/014**:
native conformance versus content fidelity (FR-001); applicable obligation
identification/mapping consolidation (FR-002); programmatic versus human intent
confirmation with their distinct authority and refined E07.02.02 parent (FR-003);
appropriate versus timely reassessment (FR-004); checkpoint integrity versus valid
workflow continuation position (FR-005); authorised working context versus
interaction position (FR-006); comparable investment evidence versus comparable
options (FR-007); workforce readiness determination versus assessment currency
(FR-008); and exceptional interrupted-context recovery with lost/uncertain state
explicit (FR-009). FR-006 and FR-009 share exactly one final E07.04.01.F01 definition.
Participant-facing context is distinct from execution checkpoints; confirmation
is distinct from transport acknowledgement and completed execution. No reserved
domain/AI authority, new runtime mechanism, protocol or AR-003 is approved.

Sixteen surviving Feature definitions and only E07.02.02's CT3 description are
refined. **Only B11.05.01.F02 is retired into B11.05.01.F01**, with its exact source
record, original criterion/status and active survivor mapping retained through the
existing evidence-local retirement convention. Active population changes from
**24 CT1 / 145 CT2 / 585 CT3 / 2,936 Features** to **24 / 145 / 585 / 2,935**.
The offline retirement register changes from 14 to 15 Features, retaining two
retired CT3s; these are candidate-register counts, not a whole-repository census.
No identity is added, renumbered, reassigned or moved. All neighbouring definitions,
XD/SR outcomes, prior retirements, source files and containment remain unchanged.
All 2,941 original source-criterion records remain traceable, now with six
consolidated source identities mapped to active survivors. AR-001/AR-002 remain
intact; original criteria/status are historical assessment evidence and do not
override accepted definitions or alone assure all refined scope. FR-001 does not
make conformance a prerequisite for retaining received native representations.

**Verification:** 50 bounded identity/containment, exact accepted wording,
source/criteria preservation, retirement, adjacent-boundary and OOXML data checks
PASS. Repeated generation preserves exact catalogue/workbook/register bytes.
The 32-sheet workbook preserves all unaffected sheet bytes and exact historical
screening proposals, with nine current accepted FR dispositions clearly recorded.
The repository architecture suite passes **90 tests, 0 failures/errors/skips**;
unchanged canonical Continuum validation passes (1,996 elements / 2,246
relationships). Release authority remains 1.1.20 with predecessor 1.1.19.

**Qualifications:** external office application load/export/visual inspection and
full OOXML XSD validation are not performed; inherited office assurance remains
UNRESOLVED. Original FR instruction and scope-disposition entry times/Task prefix
remain UNRESOLVED for Solution Architect treatment before closure; no historical
exception is extended. Iteration evidence checking reports UNRESOLVED temporal
provenance with no layout errors. No unresolved architectural incorporation
decision remains within these nine FR decisions; the resulting candidate remains
subject to Solution Architect review, independently of implementation assurance.

[Formal FR Report](../../evidence/1.1.20/001/after/ai-agent-reports/20261110T052641Z-pass3-fr-reconciliation.md) ·
[Detailed FR result](../../evidence/1.1.20/001/after/pass3-fr-reconciliation/reconciliation-report.md) ·
[Nine-decision application trace](../../evidence/1.1.20/001/after/pass3-fr-reconciliation/fr-incorporation.json) ·
[Source/result manifest](../../evidence/1.1.20/001/after/pass3-fr-reconciliation/source-result-manifest.json).
The Continuum, accepted publications, completed register and release authority are
unchanged. This HCRL remains pending at 001; no iteration advance/closure,
publication, release allocation, commit or push occurred.

### Solution Architect acceptance — Pass3/014 FR reconciliation

**Solution Architect disposition: ACCEPTED — reported FR-001–FR-009 incorporation
into the offline Pass3/014 candidate, subject to existing evidence and qualifications.**

**Attribution:** Mark Hunter, Solution Architect, through the explicit user
disposition below. Its original instruction-entry timestamp is unavailable in
this interaction; no current clock or inferred timestamp substitutes for that
event. Existing unresolved temporal provenance remains unresolved and unwaived.

**Issued disposition — preserved verbatim:**

> Solution Architect disposition — Pass3/014 FR reconciliation
>
> Accept the reported incorporation of FR-001–FR-009 into the offline Pass3/014 candidate, subject to the existing evidence and qualifications.
>
> The accepted scope remains limited to offline candidate reconciliation. The Continuum, accepted publications and release authority are unchanged.
>
> Retain all recorded qualifications concerning external office verification and unresolved temporal provenance. Neither is waived by this acceptance.
>
> Record this disposition against HCRL 1.1.20/001, DR-5. Preserve the candidate, source/result evidence, validation results and retirement correspondence.
>
> Do not advance or close the HCRL, publish or ingest into the canonical Continuum, allocate new architectural identities, commit or push without separate authorisation.

**Accepted-result correspondence:** the preceding FR application record and
[formal Report](../../evidence/1.1.20/001/after/ai-agent-reports/20261110T052641Z-pass3-fr-reconciliation.md),
with the exact offline catalogue/workbook identified in the retained
[source/result manifest](../../evidence/1.1.20/001/after/pass3-fr-reconciliation/source-result-manifest.json).
Catalogue SHA-256: `83aa0a696e057a4d1cb63177269d6c613554fea86189b9c5a8578472f09272a4`;
workbook SHA-256: `d99dd8d506badfcc5c8d88be28481a8c47706e0d9a43a7e060f1368edaaca8f7`.
The 50-check validation record, source criteria, source/result evidence and
B11.05.01.F02-to-F01 retirement correspondence remain preserved unchanged.
External office verification and temporal-provenance qualifications retain their
recorded standing; acceptance supplies no waiver or implementation assurance.

Earlier candidate/report statements that result review was pending remain the
historical state at emission. This subsequent entry records the bounded offline
incorporation acceptance. HCRL 1.1.20/001 remains pending at the same iteration;
the Continuum, accepted publications, release authority and architectural
identities are unchanged. Recording this disposition is provenance maintenance
within the existing boundary, with no new Task, Report, iteration or release.
No advancement, closure, publication, canonical ingestion, identity allocation,
commit or push is performed.

<a id="dr-6"></a>
## DR-6 — Capability and Feature coverage, including DR-6.01–DR-6.05

**Preserved Solution Architect disposition:** ACCEPTED — multi-Feature/multi-Capability partial coverage semantics.

**Origin/history:** [1.1.19/002 DR-6](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-6).

Multiple Features/Capabilities and partial coverage remain legitimate; implementation satisfaction is separate. Actual coverage reconciliation and DR-6.01–DR-6.05 remain unresolved within their recorded scope.

### DR-6.01 — Consent and emergency

Original subordinate identity and unresolved scope retained from [originating DR-6](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-6); no coverage or relationship adjudication is performed.

### DR-6.02 — Person and related persons

Original subordinate identity and unresolved scope retained from [originating DR-6](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-6); no coverage or relationship adjudication is performed.

### DR-6.03 — Practitioner, organisation and directory

Original subordinate identity and unresolved scope retained from [originating DR-6](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-6); no coverage or relationship adjudication is performed.

### DR-6.04 — Terminology and reference content

Original subordinate identity and unresolved scope retained from [originating DR-6](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-6); no coverage or relationship adjudication is performed.

### DR-6.05 — Operational and cross-cutting

Original subordinate identity and unresolved scope retained from [originating DR-6](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-6); no coverage or relationship adjudication is performed.

<a id="dr-7"></a>
## DR-7 — Primary and secondary Subject classification

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-7](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-7).

Primary/secondary classification and native relationship semantics remain unresolved; no inferred multiple primary containment/Home assignment.

<a id="dr-8"></a>
## DR-8 — Source provenance, delivery scope and domain authority

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-8](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-8).

Source verification, scope and domain authority remain unresolved. Original source limits and evidence remain binding.

<a id="dr-9"></a>
## DR-9 — Historical Task times, supporting evidence and assurance limitations

**Preserved Solution Architect disposition:** UNRESOLVED — bounded temporal exception authorised.

**Origin/history:** [1.1.19/002 DR-9](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-9).

Preserve exactly eight originating-001 and three inherited Task temporal exceptions and the separate JDK assurance limitation. New Task/Report records require established timestamps; no future exemption.

<a id="dr-10"></a>
## DR-10 — G18.03 spelling

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-10](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-10).

Accepted spelling/identity baseline reference; no architectural rename or UUID change.

<a id="dr-11"></a>
## DR-11 — Previously blocked presentation reconciliation

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-11](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-11).

Superseded prior blocking state remains history; do not reopen the completed reference repair.

<a id="dr-12"></a>
## DR-12 — 115 Groupings and distinct Composition/Home completion

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-12](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-12).

Accepted baseline and distinct Composition/Home facts remain preserved. Later SolutionFacet changes are carried candidate work under DR-17; no repeat incorporation.

<a id="dr-13"></a>
## DR-13 — Five approved elements and complete authored Views

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-13](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-13).

Accepted original elements and authored Views remain baseline facts. Additional/modified later Views are carried separately; no new visual inspection or acceptance is invented.

<a id="dr-14"></a>
## DR-14 — Inherited canonical naming, definition and standing qualifications

**Preserved Solution Architect disposition:** Unresolved — carried forward.

**Origin/history:** [1.1.19/002 DR-14](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-14).

Preserve canonical naming, definitions and independent standing qualifications. The current candidate has 687 property qualifications (673 inherited plus 14 on DR-18 additions); do not manufacture formal names.

<a id="dr-15"></a>
## DR-15 — Generated-reader structural review

**Preserved Solution Architect disposition:** ACCEPTED — actual Solution Architect review.

**Origin/history:** [1.1.19/002 DR-15](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-15).

Retain the actual originating Solution Architect reader structural acceptance within its recorded scope. It is not acceptance of unrelated future publication changes.

<a id="dr-16"></a>
## DR-16 — Actual no-edit Archi transport qualifications

**Preserved Solution Architect disposition:** ACCEPTED WITH QUALIFICATION — remediation deferred.

**Origin/history:** [1.1.19/002 DR-16](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-16).

Strict transport remains FAIL (575 relationships / 1,741 language attributes), with original bounded qualification and deferred remediation. Existing actual Archi execution evidence remains linked to its exact original candidate; no fresh execution is claimed.

<a id="dr-17"></a>
## DR-17 — SolutionFacet refactoring and final naming dispositions

**Preserved Solution Architect disposition:** ACCEPTED — visual/model acceptance and bounded Minutiae.

**Origin/history:** [1.1.19/002 DR-17](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-17).

Carry the full SolutionFacet candidate changes with original scoped acceptance, Minutiae decision, identities and naming qualifications. Inclusion in this candidate register is not whole-release acceptance.

<a id="dr-18"></a>
## DR-18 — Provenance Management

**Preserved Solution Architect disposition:** ACCEPTED — Solution Architect architectural content acceptance.

**Origin/history:** [1.1.19/002 DR-18](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-18).

Carry Provenance Management definitions, relationships and Mneme cache-availability policy with original accepted intent and detailed-design unknowns. No unapproved API, lifecycle or policy-determination choice.

<a id="dr-19"></a>
## DR-19 — Provenance and Assurance

**Preserved Solution Architect disposition:** ACCEPTED — Solution Architect architectural content acceptance.

**Origin/history:** [1.1.19/002 DR-19](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-19).

Carry independent Assurance/Audit/Provenance meanings and original qualifications. Preserve the six detailed-design questions and actual application history.

<a id="dr-20"></a>
## DR-20 — Existing 1.1.19 publication and accepted-candidate release conflict

**Preserved Solution Architect disposition:** DISPOSITION APPLIED — retained immutable baseline; candidate changes carried.

**Origin/history:** [1.1.19/002 DR-20](../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-20).

The previous release conflict is dispositioned: existing 1.1.19 stays immutable. All later delta records remain governed candidate changes for 1.1.20; original review qualifications remain attached. No candidate change is silently accepted or discarded.

**Initialisation evidence:** [Accepted predecessor and carried overlay](../../evidence/1.1.20/001/before/initial-baseline.json), [current candidate](../../evidence/1.1.20/001/after/initial-candidate/Harmonia-Architecture-candidate.xml), [initialisation verification](../../evidence/1.1.20/001/after/initialisation.json), and [originating Task/Report](../../evidence/1.1.19/002/after/ai-agent-reports/20261103T063308Z-dr20-baseline-preserving-finalisation.md). No programme execution is authorised by initialisation.
