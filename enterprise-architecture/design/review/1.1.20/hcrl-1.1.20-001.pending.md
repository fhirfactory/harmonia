<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Bounded export review and remaining incoming semantic changes | CLOSED — historical incoming export reconciliation | Finalisation Task disposition; Historical evidence retained; incorporated accepted changes remain authoritative; no acceptance of unincorporated residual proposals. |
| [DR-2](#dr-2) | G14–G18 accepted Subject hierarchy | ACCEPTED — inherited G14–G18 Subject hierarchy | Finalisation Task disposition; Inherited baseline accepted; no repeat incorporation or change to independent naming qualifications. |
| [DR-3](#dr-3) | UseCase and UserStory modelling | ACCEPTED — Requirement specialisations; creation authorised | Finalisation Task disposition; UseCase/UserStory use SolutionFacet-based naming; no instances or metamodel/naming implementation incorporated in this transition. |
| [DR-4](#dr-4) | Candidate identity and source contributions | ACCEPTED — independent identities and semantic equivalence | Finalisation Task disposition; Retain all source contributions/provenance; no candidate merge, equivalence finding or identity allocation performed. |
| [DR-5](#dr-5) | Historical Capability targets | REVIEWED WITH QUALIFICATIONS — Capability Model adjudication deferred | Finalisation Task disposition; Conditional carry-forward to 1.1.20 authorised; no historical Capability/Feature adjudication, retargeting or successor relationship created. |
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
