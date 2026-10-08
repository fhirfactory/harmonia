# Domain01 Architectural Reconciliation — Independent Assurance

**Date:** 2026-10-08. **Scope:** authorised, bounded reopening of Motivation Architecture. **Status:** APPROVED / CLOSED — human architectural approval recorded on 2026-10-08. The normative wording and relationships are approved unchanged from the completed reconciliation. Domain01 is again CLOSED / FROZEN. This record documents the deliberate upstream correction, not a downstream override or a new ADR.

> **REQ-FND-005 — Independent Assurance of Governed Activity: APPROVED**
>
> **Independent Assurance reconciliation: APPROVED / CLOSED**
>
> **Domain01 Motivation Architecture: CLOSED / FROZEN**

## 1. Discovery, Review and Authority

Downstream Information Architecture review identified a need for independent governed assurance whose full upstream motivation was missing. The [R1 investigation — Motivation Traceability and AX-05 Investigation](../../04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md) concluded **R3 — Domain01 Architectural Gap**. Governed evaluation and evidence-based assessment were substantially motivated; distinct assurance activity/conclusions, inability to assure, accountability coverage and separation from operational management were only partially motivated. Performer-independent assurance was not established.

Human architectural review accepted that gap and expressly authorised this Domain01 reconciliation. Its existence is not re-investigated here. The change captures the real-world requirement rather than retrospectively promoting the downstream construct into Motivation. The R1 investigation and the [downstream finding](../../04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md) remain unchanged as discovery/review evidence.

Human architectural approval on 2026-10-08 accepts `REQ-FND-005` and its existing wording and relationships as the authorised correction of the Domain01 architectural gap identified by the Dokimasia R1 investigation. That decision closes the Independent Assurance reconciliation and refreezes Domain01. This approval/refreeze transaction changes status only; it does not refine, reinterpret or extend the approved architecture or commence Strategy reconciliation.

The [central Architectural Axioms](../../../architectural-axioms.md) and repository [AGENTS.md](../../../../AGENTS.md) govern this change. [AX-17](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty) requires an explicit review rather than silent upstream reinterpretation, truthful relationships rather than manufactured completeness, and preservation of unresolved downstream architecture. Authorisation to prepare this reconciliation did not itself constitute acceptance or refreezing of its final formulation; the human approval recorded above now establishes both.

## 2. Approved Real-World Motivation

Those accountable for activities, information and outcomes need credible assurance against applicable obligations, expectations or controls where assurance is required. A conclusion that the subject's performer or manager can solely determine, suppress, manufacture or retrospectively alter cannot meet the need for independent assurance. Operational review and evidence collection enable assurance but do not replace it.

The approved intent requires sufficient and trustworthy evidence, separation of assurance responsibility and authority from subject performance/control, and preservation of evidence insufficiency rather than false certainty. Findings or exceptions may identify the need for operational response without transferring management of that response to assurance. Assurance may manage its own activity.

This does not claim that every subject requires assurance or that privacy legislation universally mandates performer-independent assurance. Applicability remains to be established through subsequent governed derivation. Independence is architectural responsibility/authority separation, not a decision about physical topology, technology, organisation or an external assessor.

## 3. Minimum Motivation Change

Exactly one new Motivation element is assigned: **`REQ-FND-005 — Independent Assurance of Governed Activity`**, a Foundational Platform Requirement. The title follows the existing concise noun-phrase convention; the next available `REQ-FND-nnn` identifier follows the established sequential requirement namespace. Its scope explicitly includes activity, information and outcomes despite the concise title. No existing identifier is renumbered, aliased or superseded.

The normative source is [the foundational requirement](../requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity); the completion report quotes its full wording for review. Its status is **APPROVED**, with normative wording and relationships unchanged. `REQ-FND-001`–`004` and `CST-EXT-001`–`003` retain their existing wording and Accepted status; downstream Candidate entries retain their status.

No Stakeholder, Driver, Assessment, Goal, Outcome, Principle or Constraint is created or semantically rewritten. Existing concerns are sufficient to explain the real-world value; the approved requirement supplies the missing independence, responsibility and evidence-insufficiency obligations. A new Assessment merely repeating the accepted architectural gap or a new Goal merely restating the requirement would add no necessary meaning.

Only the foundational requirements, their master catalogue, Domain01 README and textual orientation branch are modified. This review record and the dated completion report are created. The seven-thread Mermaid graph and all existing Goal-to-Outcome handovers remain unchanged. The existing Domain01 catalogue owns requirement navigation, so no central cross-domain index change is needed.

## 4. Relationships Added and Their Limits

The canonical location for the reconciliation's approved relationships is the requirement's [Motivation Relationships table](../requirements-constraints/foundational-requirements.md#motivation-relationships). This is a branching graph, not a mandatory Stakeholder → Driver → Assessment → Goal → Outcome → Principle → Requirement pipeline.

| Relationship added | Why the existing semantics support it |
| :--- | :--- |
| Operator concern → requirement: motivates | Demonstrable compliance and forensic review supply a real-world need for credible review. The approved change adds independence; the old concern is not said to have already required it. |
| Engineer concern → requirement: motivates | Incident retrospectives and deterministic verification supply review/assessment value. Operational monitoring is not itself assurance. |
| Privacy Driver → requirement: supports | Applicable privacy obligations and controls supply one valid assessment basis without determining universal assurance applicability. |
| Requirement — O3: textual contribution to demonstrability/accountability | Preserves O3 as a Business Outcome/intrinsic property. No new Goal handover, Outcome → Principle chain or assurance-outcome equivalence is added. |
| CST-EXT-001 → requirement: bounds applicable privacy obligations | Governs assurance concerning privacy/protection where relevant. It does not establish a universal legal independence mandate or define assurance applicability. |
| AX-06 / AX-07 / AX-08 / AX-14 → requirement: parallel co-governance | Respectively constrain evidence authority/credibility, security governance, meaningful audit/provenance evidence and semantic distinctions. None is reinterpreted as already supplying the missing performer-independence obligation. |
| REQ-FND-002 — requirement: complementary operational-evidence relationship | Observable progression/outcome can supply facts, subject to evidence significance and sufficiency. It is not an independence parent or a conclusion. |
| REQ-FND-004 / AX-15 — requirement: complementary uncertainty relationship | Separates uncertainty about execution from insufficient evidence for assurance. Neither operational rule is rewritten. |
| AX-09 — requirement: evidence-selection/retention boundary | Transient mechanics and observations do not automatically become audit/provenance evidence; the requirement does not mandate retention of all operational state. |

AX-17 governs the reconciliation and the treatment of unestablished downstream derivation. It is not a motivational parent supplying the real-world assurance need. No relationship newly allocates downstream ownership or implementation.

### Relationships Considered but Rejected

| Candidate relationship | Reason for rejection |
| :--- | :--- |
| Operator → Privacy Driver as a missing intermediate edge | Not established by the existing stakeholder/driver model and unnecessary; the actual compliance/forensic-review concern motivates the requirement directly. |
| Perimeter Trust or PHI Logging Assessment → independent-assurance requirement as primary derivation | Those assessments diagnose security/logging hazards, not self-attestation or independent conclusion authority. Their existing Privacy → Assessment → AX-07 relationships remain intact; AX-07 governs assurance without making either assessment an independence parent. |
| New Driver → new Assessment → new Goal → new Outcome → new Principle | Would manufacture a visually complete chain. The single requirement adds the missing obligation coherently. |
| O3 → axiom → requirement, or axioms → O3 causal handover | Would change the existing intrinsic-property and direct Goal-to-Outcome relationship semantics. O3 contributes accountability value textually. |
| REQ-FND-002, AX-16 or operational completion → assurance conclusion/independence | Observable progression is possible evidence, not assurance. AX-16's central-register standing remains unresolved. |
| REQ-FND-004 / AX-15 → assurance-evidence sufficiency as an existing rule | Would conflate operational indeterminacy with a correctly executed evaluation that lacks enough subject evidence. The new requirement supplies the complementary rule. |
| AX-05 → performer independence or a separate assurance store/runtime | Active/durable state separation is a different responsibility boundary. It cannot supply independent conclusion authority or justify topology. |
| Vendor independence, external-regulator independence or AX-06 credibility independence → performer-independent assurance | These are different senses of independence. External assurance is not required. |
| Audit/provenance collection or its preservation responsibility → assurance evaluator/authority | Evidence collection/custody does not confer authority to determine assurance. Downstream ownership remains unresolved. |
| Specific external statutes, identifier/interoperability constraints or directory validation → universal assurance applicability | The task supplies no such applicability model. Applicable obligations can later be derived without manufacturing direct parents now. |
| Downstream finding or named solution → motivating requirement | Would reverse architectural derivation and justify a predetermined solution. Discovery evidence is not a normative solution mandate. |

## 5. Axiom Review and Existing Requirement Impact

**No new axiom is required or proposed.** Domain01 already represents enduring cross-cutting obligations as foundational requirements. The approved responsibility separation and evidence-sufficiency obligation fit that method; a new axiom repeating them would add no necessary architectural meaning. No axiom identifier is assigned or reserved and the central register is unchanged.

| Existing architecture | Consistency and impact |
| :--- | :--- |
| AX-06 | Evidence must be interpreted with explicit authority/credibility; technical custody or persistence does not establish either credibility or independent assurance. Wording and responsibility allocations unchanged. |
| AX-07 | Assurance is a governed activity subject to established security context and policy. No assessor is allocated to security policy evaluation. Unchanged. |
| AX-08 / AX-09 | Meaningful evidence supports conclusions; transient operational machinery is not automatically permanent evidence. No retention rule, evidence custody or persistence mechanism is added. Unchanged. |
| AX-14 | All ten [semantic distinctions](../requirements-constraints/foundational-requirements.md#semantic--uncertainty-boundaries) are retained as Motivation constraints without defining information types or an outcome taxonomy. Unchanged. |
| AX-15 / REQ-FND-004 | Operational state/effect/outcome uncertainty remains distinct from assurance-evidence insufficiency. Their exact existing semantics are preserved. |
| AX-17 | Explicit human-authorised reopening, completed formulation approval/refreezing and unresolved downstream relationships preserve architectural authority. No downstream construct is silently promoted upstream. Unchanged. |
| REQ-FND-001 / REQ-FND-002 / REQ-FND-003 | Acceptance, operational observability and referential integrity obligations remain unchanged; none is reclassified as independent assurance. |
| AX-05 / AX-16 / AX-12 | The separate Strategy contradiction, central-register standing and classification discrepancy remain untouched and unresolved. They are neither corrected nor used to invent assurance derivation. |

## 6. Operational Uncertainty and Assurance Insufficiency

| Condition | Existing or new governing obligation | Preserved meaning |
| :--- | :--- | :--- |
| The state, effect or outcome of a managed operation cannot be established with required certainty. | AX-15 / REQ-FND-004. | Uncertain execution outcome; requires explicit operational uncertainty and governed resolution. An assurance activity's own operation can have this condition. |
| Assurance activity executes correctly, but available evidence cannot establish a conclusion with required confidence. | REQ-FND-005. | Evidence insufficiency must remain explicit. It is not execution failure, uncertainty about whether evaluation occurred, assurance or non-assurance. |

These conditions may coexist but neither determines the other. Enough evidence of operational completion need not be enough evidence for assurance against applicable obligations or controls. A lack of evidence does not establish that the subject met or failed to meet them. No canonical outcome values or qualification taxonomy are created.

## 7. Solution Independence and Unresolved Derivation

The direction of derivation remains:

```text
Real-world need
→ Independent Assurance motivation/requirement
→ Strategy responsibility
→ Business assurance behaviour
→ Information Architecture
→ Application Architecture
→ eventual solution construct
```

Only the Motivation reconciliation is prepared here. The requirement names no downstream solution, component or execution model. It does not require physical separation, external audit, a separate process, workload, cluster, database or persistence technology. The explanatory QA-service/Job-Foreman shorthand constrains responsibility; it specifies no technical service boundary.

Still unresolved are assurance applicability and coverage criteria; applicable assurance obligations/expectations/controls and confidence rules; Strategy responsibility and authority allocation; Business behaviour and its relationship to operational response; Information semantics/representation; and Application/solution mechanisms. The approved downstream finding's execution/representation questions remain deferred. No new obligation to assure every subject is invented to complete coverage.

The existing AX-05 Strategy contradiction requires separate authorised review before disputed state-responsibility statements are used downstream. AX-16 central standing and AX-12 classification remain open. Domain02–04, G2, the downstream finding and all implementation/deployment artefacts are unchanged from the incoming worktree, including existing work already present there.

Human review of the requirement, truthful relationships and semantic limits is complete: the requirement is APPROVED, the reconciliation is APPROVED / CLOSED, and Domain01 is CLOSED / FROZEN. Subsequent Strategy reconciliation requires its own instruction; no downstream step is commenced here. The AX-05 / Strategy contradiction remains explicitly unresolved for that separate architectural task.

## 8. Validation and Completion Evidence

The [dated completion report](../../../../.junie/reports/2026-10-08-domain01-independent-assurance-reconciliation.md) records exact files, full normative wording, checks actually executed, material results and unresolved matters. Automated architecture tests verify repository conformance; they do not approve the Motivation reconciliation or establish downstream derivation.

The [approval/refreeze completion report](../../../../.junie/reports/2026-10-08-domain01-independent-assurance-approval-refreeze.md) records this status transaction and its architecture-test, link, semantic-preservation and whitespace verification. The original completion report remains unchanged as historical execution evidence.
