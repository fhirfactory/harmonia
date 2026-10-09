# Domain04 Information Architecture Reconciliation — Step 2

**Date:** 2026-10-09. **Scope:** established responsibility and trace corrections only. **Status:** COMPLETE within the authorised Step 2 boundary. **Domain04 semantic sufficiency: NO.**

## Task Goal

Propagate verified upstream architecture into Domain04 without deciding missing Information Architecture. Step 1 is assessment evidence and the authorised finding inventory, not architectural authority. Domain03 remains complete for its agreed R1.x/R2.x scope; this is programme stage 3, Domain04 Step 2, not convergence/runtime implementation.

## Authority and Context Loaded

Fresh context used repository-held sources: [root instructions](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md), [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md), the [canonical axioms](../../docs/markdown/governance/architectural-axioms.md), and the [Step 1 assessment](2026-10-09-domain04-information-architecture-reconciliation-step1.md). No narrower applicable instruction file was found.

The following keys identify canonical sources actually retrieved before editing:

| Key | Upstream authority |
| :--- | :--- |
| AX | [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), especially AX-05/06/14/15/16/17/18. AX-17 preserves absent relationships; AX-18 excludes authority inferred from integration/coordination. |
| M-FND | [Foundational Requirements](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md): REQ-FND-003 non-MPI boundary and REQ-FND-005 independent assurance and evidence insufficiency. |
| S-CAP | [Business Enabling Capabilities](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md): R1.x/R2.x identity scope and Entity Management catalogue 1.1–1.3. |
| S-LC | [Logical Component Responsibilities](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md): active/durable state, Digital Twin definition and entity-specific coordination boundary. |
| S-ASS | [Health Service Assurance Derivation](../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md): approved Motivation/Strategy derivation, without component placement. |
| B-EM | [Entity Management](../../docs/markdown/03-business-architecture/behaviours/01-entity-management.md), §§2.1–2.3: source-established corrections and canonical Healthcare Subject / Client Relationship owners. |
| B-IR | [Business Information Responsibility](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md), §§1–4: meaning-based responsibility, non-transfer, Device/LHR/service mapping, capacity and contextual assurance boundaries. |
| B-DEP | [Cross-Capability Dependencies](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md), §§2–3: retained Service Provision Resolution and withdrawn Practitioner Role Resolution consumption. |
| B-HSO | [Health Service Operations](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md), capacity boundary and §2.15: FEAT-HSO-29 confirmed-exit dispatch. |
| B-PROC | [Business Processes](../../docs/markdown/03-business-architecture/processes/business-processes.md), §4.13: preparation/publication/authorisation/exit distinction and retained applicability uncertainty. |
| B-MM | [Business Metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), §§3.5–3.7: privilege and clinical-work integration boundaries. |
| B-ROLE | [Business Roles](../../docs/markdown/03-business-architecture/actors-roles/roles.md): Service Assurance Modeller / Service Guardian and their authority limits. |
| B-ASS | [Health Service Assurance](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md), §§1–2: approved capability/Function/Role/Process/Service/Interaction derivation and retained information/placement gaps. |

## Step 1 Findings Considered

This is the complete IA01–IA37 inventory, including preserved findings and partial corrections. “Partial” permits only the settled boundary; it does not adjudicate the remaining model. The user's Step 2 instruction expressly authorises the established identity boundary beyond Step 1's narrower R1 recommendation.

| Finding ID | Proposed Edit | Upstream Authority | Deterministic? | Disposition |
| :--- | :--- | :--- | :--- | :--- |
| IA01 | Preserve semantic distinctions and scoped lifecycles. | AX-14; B-IR | Already correct | Retain; no lifecycle design. |
| IA02 | Preserve/apply non-transfer. | B-IR §§1–3; AX-06/18 | Yes | Supporting invariant for bounded edits; no replacement model. |
| IA03 | Preserve relationship roles and containment/membership distinctions. | B-MM; B-IR | Already correct | Retain. |
| IA04 | Preserve adopted n-ary service binding. | B-IR Health Service Administration row | Already correct | Retain; no duplicate binding. |
| IA05 | Preserve established entity distinctions. | M-FND; B-EM; B-IR | Already correct | Retain. |
| IA06 | Preserve G1 closure and G2-D01–D04 approvals/residuals. | Explicit Domain04 decisions; AX-17 | Already bounded | Retain; G2 remains candidate/open. |
| IA07 | Exclude Harmonia-originating matching/master/merge authority; attribute corrections to source decisions. | REQ-FND-003; S-CAP identity scope; B-EM §2.1; B-IR Person Identity row | Partial | Apply settled authority boundary only; exact linkage/evidence/correction model deferred. |
| IA08 | Correct Healthcare Subject and Client Relationship owner labels. | S-CAP 1.2–1.3; B-EM §§2.2–2.3; B-IR | Yes | Apply; preserve information-context/support concepts. |
| IA09 | Remove Service Delivery/LHR universal Observation ownership. | B-IR §§1.2/2; AX-06/18 | Yes | Apply; no universal replacement owner. |
| IA10 | Correct capability-as-authority labels in assembly illustration. | B-IR Person Identity / Client Privacy rows; AX-06 | Partial | Relabel two responsibility examples only; credibility/attribution vocabulary deferred. |
| IA11 | No category/stage taxonomy change. | AX-14/17 | No | Defer adjudication. |
| IA12 | No context/Endpoint/ledger equivalences. | AX-14/17 | No | Defer vocabulary/reuse decisions. |
| IA13 | Constrain generic work illustration by established clinical-work boundary. | AX-18; B-MM §3.7; B-IR §§1.2/1.5 | Partial | Add boundary only; generic Task/FulfillmentTask/TaskOutcome interpretation remains unresolved. |
| IA14 | No full managed-state model; inspect for direct active/durable contradictions. | AX-05; S-LC; B-IR §3 | Settled boundary; missing model | No contradictory Domain04 state allocation established; defer missing model. |
| IA15 | No event/lifecycle/Praxis equivalence or universal progression. | AX-14/15/16/18; B-MM/B-IR | No for full concern | Defer; retain distinct progression and uncertainty. |
| IA16 | No Digital Twin information model. | S-LC Twin definition; AX-16/18 | Settled definition; missing model | No direct Twin-equivalence contradiction established; defer information relationship. |
| IA17 | No general standards-independence rewrite. | AX-01–04 | Yes for direction | Defer to focused standards/metamodel clarification; no responsibility/trace repair requires it. |
| IA18 | No relocation/rewrite of downstream representation examples. | AX-02–04/17 | Yes for example standing | Defer separate example cleanup; add no mappings. |
| IA19 | Separate graph direction from assertion authority. | B-IR §§1–2; AX-06/14/18 | Yes | Apply in Relationships, Containment and Guardrail 10. |
| IA20 | No new as-of/snapshot/history guarantees. | AX-05/09/14; B-IR §3 | No for full scope | Defer; preserve historical operational truth without expanding guarantees. |
| IA21 | No provenance/evidence permanence model. | AX-08/09/14; B-IR §§3–4 | No | Defer evidence/retention scope adjudication. |
| IA22 | No assurance information model. | REQ-FND-005; S-ASS; B-IR §4; B-ROLE | No for model | Preserve established contextual evidence and independence; current navigation corrected under IA24 only. |
| IA23 | No AssuredHealthcareService / governed-assurance equivalence. | S-ASS; B-IR §4 | No | Defer accountability relation and scope. |
| IA24 | Add current disposition to superseded review claims; preserve historical bodies. | REQ-FND-005; AX-05/12/16/18; S-LC/S-ASS; B-IR §4 | Yes | Apply current-status/navigation notes; no historical rewrite. |
| IA25 | No query/result information model. | AX; B-IR access row | No | Defer retrieval contract semantics. |
| IA26 | No search-result atomicity/member decision. | AX-14/17; B-IR | No | Explicitly unresolved. |
| IA27 | No security/privilege content model. | AX-07/12/13; B-MM §3.5 | Settled constraints; missing model | Retain; no deterministic harmful content/publication allocation identified. |
| IA28 | No capacity Information Concept. | B-HSO capacity boundary; B-IR §1.5; AX-18 | Settled ownership; missing model | Retain six contextual owners; no contradictory central owner identified. |
| IA29 | No new material service/clearance/handover model. | B-MM/B-IR | No | Defer minimum operational information derivation. |
| IA30 | No new clinical kinds, Observation/Finding derivation or FHIR mapping. | B-IR; AX-14/17/18 | No | Defer selected conceptual adjudication. |
| IA31 | No residual G2 question adjudication. | G2-Q02–Q07; AX-17 | No | Preserve Episode responsibility, compositions, evidence and change-effect uncertainty. |
| IA32 | Correct withdrawn Order dependency and settled discharge timing portion. | B-DEP §3; B-HSO §2.15; B-PROC §4.13 | Yes, bounded | Apply; no replacement edge, universal closure or state mapping. |
| IA33 | No universal service tuple/attestation decision. | B-IR; AX-14/18 | No | Defer contextual applicability. |
| IA34 | Distinguish service binding from referenced Organisation/Location entities. | B-IR Health Service Administration row | Yes | Apply text/diagram reference distinction; preserve entity owners. |
| IA35 | No Domain04 navigation restructure or coverage expansion. | Completion Plan §9; AX-17 | Mechanical direction settled | Minimum programme status only; domain sufficiency remains open. |
| IA36 | No Information Object/Data Object/Pragma/Praxis equivalence. | AX-14/17; Completion Plan §§5–7 | No | Defer external canonical dependency disposition. |
| IA37 | Preserve historical evidence and explicit approval standing. | AX-17; AGENTS §6 | Yes | Historical review bodies retained; current dispositions labelled under IA24/32. |

## Changes Made

The table below is also the complete changed Domain04 document inventory. Each correction has a retrieved canonical basis; references to reports or review history do not supply that basis. No Information Concept, Business relationship, hierarchy, cardinality, FHIR mapping or component is introduced.

| Changed canonical-location document | Finding / previous semantic problem | Verified upstream basis | Resulting correction | Unresolved semantics deliberately preserved |
| :--- | :--- | :--- | :--- | :--- |
| [Person / Healthcare Subject](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), §§2, 3.5–3.6, 6 and diagram | IA07/08: stale owners and unattributed probabilistic matching / merge correction | M-FND REQ-FND-003; S-CAP identity scope/1.1–1.3; B-EM §§2.1–2.3; B-IR Person Identity row | Use Healthcare Subject / Client Relationship owners. Attribute association evidence and corrections/merge outcomes to established sources; exclude Harmonia matching/master/merge origination. | Exact linkage/evidence/correction model, category vocabulary and historical reconstruction guarantees. |
| [Device](../../docs/markdown/04-information-architecture/information-families/device.md), §5 | IA09: observation streams “belong to Service Delivery and the LHR” | B-IR §§1.2/2 Device/LHR rows; AX-06/18 | Retain Device association-ledger boundary; distinguish consuming/processing/view inclusion from originating observation responsibility. | Universal Observation originating owner remains unestablished; Observation/Finding derivation untouched. |
| [Order](../../docs/markdown/04-information-architecture/information-families/order.md), OR-IR6 | IA32: withdrawn Practitioner Role Resolution consumption still asserted | B-DEP §§2–3; B-MM §3.5 | Remove that consumption assertion; retain valid Service Provision Resolution and explicit absence of privilege-related Service relationship. | Order privilege/effect eligibility, G2-Q07 and candidate approval. |
| [Episode / Encounter](../../docs/markdown/04-information-architecture/information-families/episode-encounter.md), §§6/8 | IA32: all discharge publication timing described as unresolved | B-HSO §2.15 FEAT-HSO-29; B-PROC §4.13; B-DEP §3 | State earlier preparation/publication versus confirmed-exit dispatch distinction; narrow residual U9. | Episode responsibility; Encounter lifecycle/closure, ON_LEAVE, applicability, signing/finalisation, authorisation and detailed timing. |
| [Information Relationships](../../docs/markdown/04-information-architecture/patterns/information-relationships.md), §§1.1/3 | IA19: source position / forward direction could imply asserting authority | B-IR §§1–2; AX-06/14/18 | Define source as relationship position; authority/provenance follow established source/governing responsibility independently of direction. | Assertion credibility vocabulary and each relationship's actual authority/evidence details. |
| [Containment and Collections](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md), §1 and diagram | IA19: container automatically “authoritatively asserts” containment | B-IR non-transfer; AX-06/18 | Retain forward containment; remove authority inferred from container position and diagram arrow. | Context-specific qualified authority; no reversed or replacement relationship model. |
| [Modelling Guardrails](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md), Guardrail 10 | IA19: forward direction said to establish clear authority | B-IR; AX-06/18 | Clarify semantic direction versus assertion authority without renaming the guardrail or changing inverse-query rules. | Standards-independence wording, category vocabulary and other guardrails unchanged. |
| [Assemblies and Views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md), §1 illustration | IA10 partial: Client Admin / Client Privacy labelled as originating authority | B-IR Person Identity / Client Privacy rows; AX-06 | Label two capabilities as responsibility; use current Person Identity owner and preserve constituent-source authority. | Full authority/credibility vocabulary, clinical source allocation, compositions and snapshot guarantees. |
| [Definition to Accountability](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md), §3 | IA13 partial: generic work/result example could imply clinical-work authority | B-MM §3.7; B-IR §§1.2/1.5; AX-18 | Add controlling clinical-work boundary and explicit uncertainty about generic Task/work/result interpretation. | Existing generic Concept definitions/classifications, execution/result meaning, stage/category and assurance relation remain unadjudicated. |
| [Domain03 Traceability](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md), §2.1 | IA34: HSA “realised as” mapped Organisation/Location entities could imply ownership | B-IR HSA row and entity-owner rows | Qualify those existing text/diagram references as mapped participants whose responsibility is retained. | No universal tuple/attestation, replacement binding, new provider relationship or ownership matrix. |
| [G1 Review](../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md), current disposition / §16.4 framing | IA24/32/37: closure-time U9/U13 and AX-12 statements appeared current | AX-12/16; B-PROC §4.13 | Add dated current disposition and label register as closure-time evidence. | Historical rows/quotations/decisions preserved; unrelated task ordering and residual discharge questions remain open. |
| [G2 Review](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), §§3/8 | IA24/32: stale discharge and axiom-gap current summaries | AX-12/16; B-PROC §4.13 | Update current residual table and navigation; identify old immutability statement as decision-persistence history. | G2-D01–D04 and all residual Q02–Q07 questions preserved; candidate/open standing unchanged. |
| [Dokimasia Motivation / AX-05 Investigation](../../docs/markdown/04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md), opening framing | IA24/37: independent-assurance absence, AX-05 conflict and axiom discrepancies presented as current | M-FND REQ-FND-005; S-ASS; S-LC Mneme/Mnemosyne; B-IR §4; AX-05/12/16 | Add current canonical disposition; label original recommendation/conclusion historical without rewriting investigation. | Dokimasia placement, detailed assurance/evidence and managed-state models. |
| [Dokimasia Finding](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md), current disposition | IA24/22 partial/37: capture-time Motivation/Strategy/Business gaps and proposed sequence appeared current | M-FND REQ-FND-005; S-ASS; B-ASS; B-IR §4; B-ROLE | Point to established upstream assurance responsibilities and contextual evidence/assessment/adjudication boundaries. | Approved capture preserved; information decomposition, AssuredHealthcareService relation, approval/lifecycle/retention and execution placement unresolved. |

Outside Domain04, only the [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md), §4, receives a programme-status update: Step 1 assessment and bounded Step 2 complete, sufficiency NO, further Step 3 separately authorised. Its axiom-migration paragraph is framed at that historical boundary. Sequence, gates and Domain03 completion are unchanged. This new report is execution evidence, not architectural authority.

## Person / Healthcare Subject Corrections

Matching/merge authority ambiguity was corrected in the family diagram, responsibility table, correlation characteristics, correction definition and audit-continuity wording. Harmonia may maintain authoritative identifiers, aliases, relationships, established correlation/federation and managed consequences of source-established corrections/merge outcomes. It may preserve the historical truth of what it received, knew, asserted, managed, decided, communicated or did; preservation does not originate the source identity decision.

Harmonia has no established R1.x/R2.x mandate for probabilistic patient/person matching, golden records, master-person/master-patient selection, authoritative MPI/EMPI reconciliation or independently deciding that source identities should merge. A correlation does not itself authorise a merge. Legitimate federation/correlation and non-destructive source correction history remain.

**IA07 disposition discrepancy:** Step 1 marked the compound concern as requiring human adjudication. Re-reading REQ-FND-003, S-CAP, B-EM and B-IR confirms that the exclusion/source-decision boundary itself requires no new decision. The user's express Step 2 authorisation permits this deterministic portion. The full correlation/evidence/correction interpretation and vocabulary remain deferred; the edit neither approves a probabilistic-evidence model nor designs MPI/EMPI. IA08's capability names are settled; the valid Healthcare Subject Context and support-network information meanings are retained.

## Device / Observation Corrections

Removed the blanket claim that physiological observations/telemetry “belong to Service Delivery and the Longitudinal Health Record.” The association ledger still records device association with subject/context/location without owning the produced streams. Source attribution and established governing responsibility remain independent of Device association, Service Delivery consumption/processing/correlation, LHR view inclusion, presentation and persistence.

**Universal originating Observation responsibility remains unestablished.** No Diagnostic Administration, Service Delivery, LHR, Device or other capability is substituted as a universal owner. The broader Observation/Finding model is deliberately untouched.

## Order Trace Correction

OR-IR6 no longer asserts Order Administration consumes Practitioner Role Resolution. B-DEP §3 expressly withdraws that edge because Role/capacity resolution establishes neither Clinical Privilege nor Operational Privilege. The existing, valid Service Provision Resolution consumption is retained. Requester/privilege context remains meaningful, but its precise consuming Service relationship is explicitly unestablished.

**No replacement relationship, Function, Process, Service or Interaction was inferred.** OR-IR1–5, Order semantics, requested-versus-effective-change distinction and candidate status remain intact; Domain03 Order is not reopened.

## Encounter / Discharge Correction

The family and current G2 uncertainty summary now reflect B-HSO §2.15 / B-PROC §4.13: preparation, information availability/publication, applicable authorisation and physical departure are distinct. Applicable information may be prepared/communicated earlier. FEAT-HSO-29 requires departure notifications, updated bed-state communication and finalised-summary dispatch upon confirmed physical exit; earlier preparation does not discharge that responsibility.

The old wholly unresolved before/upon-departure question is not carried forward. Remaining questions concern applicability and timing beyond that distinction, signing versus finalisation, authorisation workflows, ON_LEAVE correspondence, Encounter classification/tracking and closure. No complete Encounter lifecycle, universal discharge transition, FHIR Encounter status mapping, atomic departure-to-information linkage or replacement publication Service/event is established. G1 closure-time U9 remains historical evidence with a current disposition note.

## Responsibility / Authority Corrections

B-IR's rule controls all changed statements: information responsibility follows the capability/behaviour establishing or governing meaning. Handling, access, caching, persistence, presentation, preservation, workflow use and evidence use do not transfer originating responsibility. Existing valid non-transfer statements remain; no comprehensive ownership matrix is added.

AX-18 is applied concretely to Person decision attribution, Device/Observation non-ownership, relationship-direction authority and the generic work illustration. The work note prevents coordination/representation from conferring clinical-work determination, allocation, handover, clinical decision, worklist or task ownership. It preserves Harmonia-assigned information management, operational/workflow coordination, communication, provenance, control and activity execution. IA13's exact generic work/result Concept interpretation remains unresolved; the note is a constraint, not its semantic adjudication.

IA10's two capability labels now identify responsibility rather than source authority. No new information-credibility taxonomy is derived. IA34 distinguishes HSA-owned mapping associations from independently governed referenced entities; the existing diagram edges are qualified, not supplemented with speculative relationships.

## Other Deterministic Corrections

**IA19:** Forward relationship/containment direction remains canonical; authority to assert the relationship is separately attributable and may lie with another participant or external source. No reverse ownership, redundant association or new authority provider is chosen.

**IA24/37:** G1 and both Dokimasia reviews retain their historical investigation/analysis/decision bodies. Dated framing identifies superseded motivation, state and axiom-gap claims and links to current canonical authority. The historical recommendation/conclusion words are labelled historical, not erased. G2's current summary is corrected; its approved decision text remains unchanged.

**IA14/16/27/28 inspections:** Step 1 identifies missing managed-state, Twin-information, security/content and contextual-capacity semantics rather than a demonstrated direct Domain04 allocation of durable authority to Mneme, identity of Twin with Resource/Actor/record, harmful public security metadata or central capacity ownership. No model was added. The historical AX-05 note carries the settled active-generation/durable-version and failed-convergence boundary without deriving a new lifecycle. Capacity remains within Clinic & Practice, Ward, Theatre, Emergency Department, Outpatient and Mobile Service contexts; resource availability alone is not service capacity, and higher-order consumption does not transfer originating responsibility.

**IA22/24:** Current assurance navigation preserves source information → contextual evidence use → assessment → adjudicated finding/conclusion, non-transfer and insufficient evidence. It does not instantiate that chain as a universal entity model. Independent outcome responsibility remains established by REQ-FND-005; subject performance/management cannot solely manufacture, suppress or retrospectively alter the independent outcome. No Evidence entity, flag, state machine, Service or Interaction is added.

**IA35:** Programme status alone is refreshed. No Domain04 restructure, new navigation hierarchy, exhaustive Feature coverage or semantic completion claim is made. Deterministic standards/example clarifications IA17/18 remain for a separate focused package; they are not needed to perform these responsibility/trace corrections.

## Findings Deliberately NOT Resolved

| Remaining concern | Finding IDs | Why deferred / boundary preserved |
| :--- | :--- | :--- |
| Search-result atomicity, whether returned resources become individually managed, result lifecycle and minimum retrieval contract | IA25/26 | Upstream establishes access/projection responsibility, not result/member management semantics. Bundle, caching and implementation cannot decide it. |
| Managed-state, active lifecycle, history/snapshot/evidence and event/Praxis relations | IA14/15/20/21/27 | AX-05's distinct active generation/durable version and failed-convergence boundary are settled; the sufficient Domain04 model and retention/effect guarantees are not. No universal lifecycle is created. |
| Episode responsibility and remaining candidate clinical-family questions | IA31 | G2-D01 approves Episode meaning, not an owner. Q02–Q07 residual classification, evidence, composition, authority and change-effect questions remain open. |
| Assurance/evidence model and AssuredHealthcareService relation | IA22/23 | Upstream assessment/adjudication, contextual evidence and independent responsibility are established, but formal Concept decomposition, accountability equivalence and approval/lifecycle/retention are not. Insufficient evidence remains explicit and is neither assurance nor non-assurance. |
| Context-specific capacity information model | IA28/29/33 | Six contextual owners are established. Resource states contribute but do not define capacity; no universal Capacity Concept, provider contract or identical context model follows. |
| Observation/Finding derivation and other clinical information kinds | IA09/30 | Removing a false owner does not derive clinical concepts, source rules or a universal Observation authority. No new clinical family or mapping is approved. |
| Candidate-family approval | IA06/31 | Trace repairs do not approve G2. Existing G2-D01–D04 remain bounded; Order/Encounter/other residual candidate propositions remain candidate. |
| Authority/category vocabulary and generic work/result meaning | IA07/10–13/15 | Known responsibility exclusions are now explicit, while credibility/category/stage relationships, source evidence semantics and work/result representation still require architectural judgement. |
| Digital Twin information relationship | IA16 | Upstream Twin definition remains an active coordination construct associated with a real-world entity. Domain04 has no demonstrated contradictory identity to correct; representation alone cannot derive Twin state/schema. |
| External canonical terminology/dependencies | IA36 | Information Object/Data Object, Pragma/Praxis and Concept equivalences/consolidation remain unestablished; implementation-heavy material is not silently incorporated. |
| Universal service tuple/attestation and material operational information | IA29/30/33 | Context-specific applicability, source/finality/acceptance and adequate minimum kind boundaries are not supplied by corrected traces. |

## Validation

### Semantic and traceability review

**PASS within Step 2 scope:** every changed statement is accounted for in the source/change table above. No unresolved Step 1 question is treated as a new architectural answer. IA07, IA10 and IA13 receive only their deterministic responsibility boundary/label portion; remaining model semantics are explicit. No withdrawn Business edge receives an inferred replacement. No capability/Function rename beyond the established Healthcare Subject / Client Relationship / Person Identity owner labels is required. Missing relationships remain missing.

G1 K1–K13, G2-D01–D04 and G2-Q01 standing are preserved. G2 remains candidate/open; G3 and Domain05 have not commenced. Historical G1 and Dokimasia bodies were compared with HEAD after removing only the inserted framing/labels: **PASS**. Existing approved G2 decision sections are also preserved. No standards-derived answer, cardinality, schema or wire representation is introduced.

### Domain04 links and whitespace

`python3 /tmp/domain04-step2-links.py`: **PASS, exit 0** — **25 Markdown documents, 546 local link occurrences, 85 code fences, zero missing-file/anchor errors, zero structural errors**. The Step 1 parser was refreshed into a separate Step 2 script/output: markdown-it-py CommonMark plus tables, inline/image/reference links, explicit HTML anchors and GFM-style heading IDs. Link targets outside Domain04 are checked too. Evidence: `/tmp/domain04-step2-links.json`. This is syntax/target validation, not a full documentation renderer or semantic proof.

`git diff --check`: **PASS, exit 0**, no whitespace diagnostics. The untracked report receives a direct whitespace/newline scan. `python3 /tmp/domain04-step2-report-plan-links.py`: **PASS, exit 0** — this report and the changed Completion Plan, **50 local links, five closed fences, zero link/structural errors**. Structural/disposition checks confirm **37 sequential IA findings, all 15 required main report sections and no pending markers**. Final link/whitespace/scope checks were refreshed after report completion without rerunning the already-passing architecture suite.

### Required architecture suite

Executed once, with native command launch at a 10-second yield, 600-second command timeout and 15-second termination grace:

```bash
timeout --signal=TERM --kill-after=15s 600s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS / BUILD SUCCESS, exit 0:** **90 tests across 11 architecture suites; 0 failures, 0 errors, 0 skipped**. Maven duration **17.403 seconds**, finished `2026-10-09T20:30:38+11:00`. This matches the previous 90/0/0/0 baseline. Progress was observed through suite completion; no stall/timeout/termination or unchanged rerun occurred. A no-progress investigation threshold of approximately two minutes applied. The final session completion wait used a 30-second yield and returned immediately because the process had finished.

| Suite | Tests | Failures | Errors | Skipped |
| :--- | ---: | ---: | ---: | ---: |
| AgoraIsolationArchitectureTest | 7 | 0 | 0 | 0 |
| GovernedWriteCompositionArchitectureTest | 7 | 0 | 0 | 0 |
| GovernedWriteContractArchitectureTest | 12 | 0 | 0 | 0 |
| IrisDecouplingArchitectureTest | 5 | 0 | 0 | 0 |
| MnemosyneAuthoritativePersistenceArchitectureTest | 6 | 0 | 0 | 0 |
| PackageLayeringArchitectureTest | 4 | 0 | 0 | 0 |
| ParadeigmaIsolationArchitectureTest | 4 | 0 | 0 | 0 |
| PetasosApiIsolationArchitectureTest | 3 | 0 | 0 | 0 |
| ProviderRegistryArchitectureTest | 2 | 0 | 0 | 0 |
| PylaiPublicationBoundaryArchitectureTest | 8 | 0 | 0 | 0 |
| SecurityEnforcementArchitectureTest | 32 | 0 | 0 | 0 |

Evidence: `/tmp/domain04-step2-architecture-tests.log`, `/tmp/domain04-step2-test-summary.json` and the Surefire `TEST-*ArchitectureTest.xml` reports. XML totals were checked against the Maven log; all required named suites ran.

**Environment warnings, separate from failures:** four Maven warnings concerned unwritable `com/sun/mail/jakarta.mail/resolver-status.properties` and Central/OSS snapshot metadata `.part.lock` files under read-only `.m2/repository`; cached dependencies sufficed. ArchUnit detected **Java 21.0.12.1**; no Java 25 / ArchUnit compatibility warning was emitted in this run. No environment, implementation or test modification was made. This selected architecture reactor is not a full-repository test, deployment verification or documentary semantic conformance proof.

### Scope / immutability and changed-file inventory

Initial worktree contained only the incoming untracked Step 1 report. Before edits, `/tmp/domain04-step2-baseline.json` captured **2,289 tracked files plus that report**. Final hash comparison: **PASS** — **15 existing files changed, all allowed**; **2,275 other baseline files byte-identical**, no missing files and tracked-file set unchanged. Evidence: `/tmp/domain04-step2-preservation.json`.

**Implementation source, tests, Domain01/02/03 semantics, canonical axioms, Domain05+ architecture and the incoming Step 1 report are unchanged.** No upstream defect needing repair was established by these corrections. There is no runtime/deployment prerequisite for the documentation patch; outstanding information decisions remain material to future completion.

The complete task inventory is **16 files**: the **14 Domain04 documents** linked in Changes Made, the **Architecture Completion Plan** status update, and **this new Step 2 report**. The untracked incoming Step 1 report is an input, not a task change. Maven writes only generated build/test output; no implementation or test source is edited.

### Canonical Documentation Assessment

The Step 1 report identifies accepted external ADR obligations, the Deferred Document Register's Information Object/Data Object terminology, Pragma/Praxis material and historical LaTeX. They are navigation/evidence for later IA36 and state/history packages, not authority for these Step 2 edits. No external architectural content is migrated or assigned new canonical meaning here. The two canonical-location Dokimasia reviews retain historical analysis with current upstream navigation; detailed assurance information and application placement remain outstanding. Overall corpus consolidation and Domain04 completion are not declared.

## Remaining Human Decisions

| Genuine remaining decision | Related findings |
| :--- | :--- |
| What exact source evidence qualifies correlation/correction, and how should that information be represented within the now-explicit non-MPI boundary? | IA07 |
| How do authority credibility, source attribution, category/stage and event/lifecycle vocabulary relate without inferred equivalences? | IA10–12/15 |
| What do the generic work/result Concepts denote, and how do they relate to represented Work Orders / To Dos / synthetic Tasks and externally governed clinical work? | IA13 |
| Which history/snapshot/evidence guarantees and entity-information/Twin coordination relationships are materially required? | IA16/20/21 |
| Which external Information Object/Data Object and Pragma/Praxis semantic obligations belong in canonical Information Architecture? | IA36 |
| How are residual G2 source/authority/evidence/composition/change-effect questions resolved, including Episode responsibility? | IA31 |
| What are minimum retrieval/result semantics, result atomicity and individual member-management rules? | IA25/26 |
| What minimum contextual evidence/assessment/adjudication information decomposition and AssuredHealthcareService relationship are established, with whose approval/lifecycle/retention mandate? | IA22/23 |
| What minimum contextual capacity and clinical/operational information packages are required, with which applicability, kind, source and attestation boundaries? | IA28–30/33 |

The canonical owner names, non-MPI/merge-origination exclusions, non-transfer rules, withdrawn Order consumption and preparation/confirmed-exit distinction are resolved upstream and are not presented as open decisions. AX-05 concurrency domains, current AX-12/16 standing, the Digital Twin definition and independent-assurance responsibility also remain established; their missing downstream information models do not reopen them.

## Semantic Sufficiency

> Is Domain04 now semantically sufficient to support Domain05 completion without Domain05 inventing missing Information Architecture?

**NO.** The false ownership and stale trace/status statements are corrected, but generic work/result meaning, authority vocabulary, sufficient managed-state/history, retrieval/result management, contextual assurance/capacity and residual G2 semantics remain material gaps. Application completion would still have to invent those meanings. Improved correctness does not establish completeness or justify YES WITH BOUNDED RECONCILIATION. Valid existing foundations and bounded G2 approvals remain useful; no one-Concept-per-Feature or complete ownership matrix is required.

## Recommended Step 3

**Separately authorise the smallest R2a work-information slice: IA13 generic work/result meaning, with only the IA12/15 vocabulary needed to interpret it.** Adjudicate whether ActionableTask, FulfillmentTask, TaskOutcome and ReportedTask denote governed information about assigned operational activity or the underlying work/result, and their relationship to the established Work Order / To Do / synthetic Task distinction. Preserve external clinical-work authority, Harmonia-assigned execution, existing service-stage distinctions and the absence of component/FHIR allocation.

This is the next P1 semantic prerequisite to adequate managed-state/activity/history distinctions (IA14/15/20/21). Do not combine it with a complete taxonomy, state machine, assurance or search model. The narrower remaining IA07 source-evidence question may be separately adjudicated without reopening matching/merge authority. Subsequent state/history work and result/evidence/capacity packages should follow their actual dependencies; no later work is started by this recommendation.

**Step 2 stops here:** deterministic/partial corrections, validation, this report and minimum programme status. No Step 3 adjudication, clinical-family approval or Domain05 work commenced.
