# Dokimasia R1 — Motivation Traceability and AX-05 Investigation

**Date:** 2026-10-08. **Status:** evidence investigation complete; assessment and recommendations for architectural review, not architectural reconciliation.

**Current disposition — 2026-10-09:** This is a historical investigation of the then-inspected baseline. Its motivation-gap conclusion is superseded by approved [REQ-FND-005](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) and the established [Strategy assurance derivation](../../02-strategy/capability-maps/health-service-assurance-derivation.md) / [Business information responsibilities](../../03-business-architecture/information-responsibility/information-responsibility.md#4-assurance-related-business-information-responsibilities). The recorded Strategy/AX-05 contradiction is superseded by [current Mneme/Mnemosyne responsibilities](../../02-strategy/strategic-views/logical-component-responsibilities.md#component-2-mnemosyne-durable-preservation--recovery) and [canonical AX-05](../../governance/architectural-axioms.md#ax-05): active generation and authoritative durable version remain distinct, and failed/degraded convergence establishes no valid active generation. [AX-12](../../governance/architectural-axioms.md#ax-12) and [AX-16](../../governance/architectural-axioms.md#ax-16) are established in the single canonical register; their old register discrepancies are historical. The findings, quotations and recommendations below remain decision-time evidence, not current absence/conflict claims. Dokimasia placement and the detailed Domain04 assurance/evidence and managed-state models remain unresolved.

**Historical recommendation: R3 — Domain01 Architectural Gap.** Domain01 establishes reasons to demonstrate compliance, account for significant events, verify operational facts and preserve uncertainty. It does not establish that assurance must be independent of the activity being assured. The approved Dokimasia finding therefore cannot be represented as fully derived from existing Motivation Architecture. This is an upstream motivation gap, not a rejection of the approved construct.

**Historical AX-05 conclusion:** a real Strategy contradiction denies Mnemosyne the authoritative state/version progression and durable-truth responsibilities that AX-05 explicitly assigns to it. Excluding operational workflow execution from Mnemosyne is compatible with AX-05. The contradiction needs a separately scoped architectural review; it does not prevent this motivation assessment or supply the missing independence motivation.

## 1. Scope, Authority and Subject of the Test

The [central Architectural Axioms](../../governance/architectural-axioms.md), repository [AGENTS.md](../../../../AGENTS.md), applicable accepted ADRs and documented Domain01 relationships govern this investigation. [AX-17](../../governance/architectural-axioms.md#ax-17) requires missing relationships and conflicting statements to remain explicit. [Domain04's upstream immutability boundary](../README.md#upstream-authority--immutability-context) remains intact.

The [approved Dokimasia finding](dokimasia-assurance-architectural-finding.md#2-approved-conceptual-definition-and-boundaries) supplies the requirement being tested, not retrospective evidence that Domain01 already derives it. This document's location records the review's origin; it does not place Dokimasia in Domain04. The Junie completion report is execution evidence, not architectural authority.

The exact approved definition is:

> **Dokimasia is Harmonia's independent assurance framework responsible for coordinating governed assurance activity over a subject of assurance.**

The exact approved operational boundary is:

> **Dokimasia SHALL coordinate independent assurance activity concerning a subject of assurance. It SHALL NOT assume responsibility for the operational management, assignment, delegation, reassignment, escalation or remediation of the activity being assured. Assurance findings and outcomes MAY initiate or inform operational activity, but responsibility for that activity remains with the appropriate operational construct.**

> **Dokimasia is the QA service, not the Job Foreman.**

The approved finding also establishes assurance as governed workflow and operational independence: the assured activity cannot determine, suppress, manufacture or retrospectively alter its own assurance outcome. Assurance Praxis may manage its own assurance work without managing its subject's operational work. Those boundaries are preserved and are not extended here.

```text
Provenance ≠ Assurance
Audit information ≠ Assurance
Operational progression ≠ Assurance
Workflow completion ≠ Assurance
Business Outcome ≠ Assurance Outcome
```

Evidence availability is not an assurance conclusion. Accountability is not workflow execution. Architectural usefulness is not existing architectural derivation.

No Domain01–04 architecture, axiom, approved finding, G2 decision, Pragma/Praxis definition or implementation is changed. G2-D04 remains authoritative and G2-Q02–Q07 are not continued. This investigation creates no motivational element, Capability allocation, assurance Function/Service/Process, Information Concept, execution engine or implementation model.

## 2. Evidence Examined and Interpretation Method

### 2.1 Source inventory

All 13 Markdown documents in the Domain01 tree were inspected. Source labels below support the traceability tables; line locations refer to the unchanged repository snapshot inspected on this date.

| Label | Authoritative or contextual source | Use in this investigation |
| :--- | :--- | :--- |
| M01 | [Domain01 README](../../01-motivation/README.md), especially §4, lines 52–64; [Orientation View](../../01-motivation/orientation-view.md), Detailed Thread Walkthrough and Architectural Visual Semantics | Motivation relationship pattern and seven selective threads. |
| M02 | [Enterprise Stakeholders](../../01-motivation/stakeholders/enterprise-stakeholders.md); [External Authorities](../../01-motivation/stakeholders/external-authorities.md) | Operator compliance/forensic-review concern; engineers' incident review and deterministic verification; authority versus stakeholder distinction. |
| M03 | [Drivers](../../01-motivation/drivers-assessments/drivers.md); [Assessments](../../01-motivation/drivers-assessments/assessments.md) | Seven agreed drivers, five assessments and their documented associations. |
| M04 | [Strategic Goals](../../01-motivation/goals-outcomes/strategic-goals.md); [Business Outcomes](../../01-motivation/goals-outcomes/business-outcomes.md) | Goal handovers, O1/O2 and demonstrable protection/accountable handling in O3. |
| M05 | [Domain01 Architectural Axioms](../../01-motivation/principles/architectural-axioms.md); [Reclassified Principles](../../01-motivation/principles/reclassified-principles.md) | Local principles, AX-05 duplicate, AX-16 local definition and AX-12 reclassification record. |
| M06 | [Foundational Requirements](../../01-motivation/requirements-constraints/foundational-requirements.md); [External Constraints](../../01-motivation/requirements-constraints/external-constraints.md); [Master Requirements Catalogue](../../01-motivation/requirements-constraints/master-requirements-catalogue.md) | Four accepted foundational requirements, three accepted constraint categories, downstream Candidate status and derivation relationships. |
| A01 | [Central Architectural Axioms](../../../architectural-axioms.md) | Highest architectural authority; normative wording and actual limits of relevant axioms. |
| A02 | [Architecture Decision Register](../../../architecture-decisions.md), particularly ADR-018 and ADR-019 | Corroboration of authoritative durable state and final persisted version responsibilities; no inspected accepted override of AX-05. |
| S01 | [Strategy logical-component responsibilities](../../02-strategy/strategic-views/logical-component-responsibilities.md), Component 1, Component 2, summary and Seams 1–3 | Exact contradictory and compatible statements. |
| S02 | [Strategic Courses of Action](../../02-strategy/courses-of-action/strategic-courses-of-action.md), COA-02 and traceability matrix; [course index](../../02-strategy/courses-of-action/index.md) | Ambiguous exclusion of state progression and compatible summary references. |
| S03 | [Enterprise Capabilities](../../02-strategy/capabilities/enterprise-capabilities.md), EC-04; [Strategic Value Streams](../../02-strategy/strategic-views/strategic-value-streams.md), VS-02 | AX-05 reference checks; these do not override the axiom. |
| C01 | [Approved Dokimasia finding](dokimasia-assurance-architectural-finding.md); [G1 AX-16 investigation](package2-g1-review.md#163-ax-16-investigation-and-disposition) and [G1 consistency validation](package2-g1-review.md#165-cross-architecture-consistency-and-architecture-control-validation) | Subject and previously recorded discrepancies; not substitutes for reading the axioms. |
| C02 | [Governed Write and Concurrency Contract](../../../design/governed-write-concurrency-contract.md), §§2.1–2.3; [convergence/runtime plan](../../../implementation/harmonia-convergence-runtime-integration-plan.md), §2.1 | Downstream corroboration of AX-05 only. No runtime work is planned or commenced. |

The investigation searched motivation concerning assurance, independence, accountability, governance, oversight, verification, evidence, trust, auditability, provenance, traceability, conformance, compliance, quality, safety, operational correctness, performance/review separation and insufficient evidence. Relevant passages and their documented relationships were read in context. Repository history was used only to distinguish recorded change from evidence of architectural approval.

The negative finding is bounded to the inspected architectural material. It is not a claim about every deployment's external legal obligations. No external regulator's requirements, historical plan or implementation is promoted into a missing Harmonia motivation relationship.

### 2.2 Relationship discipline

The requested checklist is:

`Stakeholder → Driver → Assessment → Goal → Outcome → Principle → Requirement / Constraint`

M01 explicitly rejects that sequence as a rigid pipeline. The existing model is a branching graph: Goals hand over directly to Outcomes; Principles co-govern requirements; Constraints enter at their actual point of effect. The checklist is used to inspect coverage, not to invent successive causal edges.

Thread 6 deliberately contains no manufactured Goal. AX-07 and AX-08 have no causal arrows to Outcomes. M04 nevertheless documents O3 as an intrinsic property realised through those principles. That textual realisation relationship is retained without creating an `Outcome → Principle` or `Axiom → Outcome` causal edge. The orientation view is selective; an omitted graphical stage alone does not prove missing motivation.

M06 marks REQ-FND-001–004 and CST-EXT-001–003 **Accepted**. CORE-001, CORE-003, SEC-001/002, SEC-010, PROV-001 and DIR-001..006 remain downstream **Candidate** requirements. Their documented navigation links are evidence of proposed handoffs, not accepted additional Domain01 foundational obligations.

## 3. Motivation Evidence and Reconstructed Relationships

### 3.1 Evidence traceability matrix

| Evidence and exact source location | What Domain01 establishes | What it does not establish | Relevant test facets |
| :--- | :--- | :--- | :--- |
| M02, Regional Health Network Operator, line 25: audit trails that “prove compliance with regulatory directives and enable forensic review of clinical incidents” | Need to demonstrate compliance and enable substantive retrospective review. The concern is broader than storing diagnostics. | A separate Harmonia assurance workflow or assessor independent of the performer. No documented Operator-to-Privacy-Driver edge is manufactured. | Need, evidence, accountability; partial activity. |
| M02, Operations & Integration Engineers, lines 105–113: incident retrospectives, deterministic progression verification, observable multi-stage state | Verification/investigation is an intended use of operational information. | Verification performed by an independent assurance responsibility or a distinct assurance result. | Activity, evidence, accountability, bounded uncertainty. |
| M04, O3, lines 46–60: demonstrable protection, statutory compliance/legal assurance, tamper-evident provenance enabling incident retrospectives | Need to establish that handling meets obligations; evidence must support demonstration. | Audit/provenance as assurance itself, an independent assessor, or an Assurance Outcome. “Patients are assured” is a desired effect, not a workflow specification. | Need, evidence, accountability. |
| M03, Privacy Driver and Perimeter Trust/PHI Logging Assessments; M06, CST-EXT-001, lines 13–29 | Applicable obligations, platform-enforced controls, attributable handling and audit/provenance for demonstrating authorised handling. | A universal statutory requirement for Harmonia performer-independent assurance. OAIC's external independence is not that requirement. | Need, governed activity, evidence. |
| M06, REQ-FND-002, lines 35–49 | “Harmonia shall maintain sufficient explicit and observable state to determine the progression and outcome of managed multi-stage operational activities.” Its rationale enables monitoring, audit and reconciliation. | Progression or completion as an assurance conclusion; governance-population coverage; separate Assurance Praxis. | Operational evidence, accountability, review activity. |
| M06, REQ-FND-004, lines 77–95 | “Where Harmonia cannot establish the outcome of a managed operation with sufficient certainty, the outcome shall be represented explicitly as indeterminate and shall not be interpreted as either success or failure without subsequent reconciliation or another authoritative determination.” | An assurance-specific evidence sufficiency test, qualification taxonomy or equation of operational INDETERMINATE with an Assurance Outcome. | Uncertainty and evidence; partial inability-to-assure support. |
| M03/M04/M06, Identity Driver, Identity Goal, CST-EXT-002 and REQ-FND-003 | Required identifier validation, explicit ambiguity and preserved identifier authority/provenance. | Validation as independent assurance or a new directory-assurance requirement. | Bounded correctness/criteria and evidence. |
| M05/A01, AX-06 | Information credibility/authority is independent of technical custody; standing and policy response are distinct. | Independence between the activity performer and assurance evaluator. | Evidence interpretation; partial result/response separation. |
| M05/A01, AX-07–09 | Governed operations have security context; meaningful evidence explains significant events/decisions; transient mechanics are not automatically evidence. | Audit collection as assurance evaluation or mandatory retention of every operational observation. | Governance, evidence, accountability. |

These passages support limited evaluation of compliance and operational facts. None supplies the missing performer-independent assurance requirement.

### 3.2 Established thread routes and missing relationships

The following routes use M01's documented walkthrough together with M03–M06. `S`, `D`, `A`, `G`, `O`, `P`, `R` and `C` denote Stakeholder, Driver, Assessment, Goal, Outcome, Principle, Requirement and Constraint. A governing branch is stated separately from the Goal-to-Outcome route. Missing stages are reported, not filled.

| Thread / source | Established route and governing branch | Deliberately absent or unestablished relationship | Assurance relevance and limit |
| :--- | :--- | :--- | :--- |
| [T1 — Standards interoperability](../../01-motivation/orientation-view.md#thread-1-standards-interoperability--boundary-sovereignty) | S: Network Operator/HDO → D: Heterogeneous Standards → G: Longitudinal Coherence → O2. C: CST-EXT-003 bounds D. P: AX-02/03 govern; R: CORE-003 is a Candidate handoff. | No Assessment in this thread. No assurance-evaluator relationship. O2 does not cause AX-02/03. | Applicable boundary contracts/semantic conformance can supply expectations. Conformance alone does not establish independent assurance. |
| [T2 — Information independence](../../01-motivation/orientation-view.md#thread-2-clinical-information-independence--longitudinal-coherence) | S: Clinicians/HDO → D: Durable Clinical Information Independence → G: Longitudinal Coherence → O1. G links to P: AX-01/06; AX-06 → R: CORE-001 Candidate. | No Assessment in this thread; no assessor-independence requirement. | Credible information and vendor-neutral longevity. Vendor independence is not assurance independence. |
| [T3 — Acceptance/failure safety](../../01-motivation/orientation-view.md#thread-3-durable-ingress-acceptance--failure-safety) | S: HDO/Engineers → D: Availability → A: False Acceptance → G: Durable Acceptance/Preservation → O2. P: AX-05/10/15 co-govern REQ-FND-001; AX-15 governs REQ-FND-004. M06 records the Assessment/Driver motivation for both requirements. | No independent verification responsibility or separate assurance-result handoff. No sequential AX-05 → AX-10 → AX-15 edge. | Establishes acceptance certainty, responsibility and operational indeterminacy. Durable acceptance is not assurance. |
| [T4 — Responsive access](../../01-motivation/orientation-view.md#thread-4-concurrent-availability--responsive-information-access) | S: Clinicians/Engineers → D: Availability → A: Synchronous Persistence Contention → G: Responsive Access → O2. P: AX-05/09/11 co-govern. | No dedicated foundational Requirement assigned to this thread; no assessor-independence link. | Keeps access/state responsibilities distinct. State separation does not require independent assurance execution. |
| [T5 — Identity integrity](../../01-motivation/orientation-view.md#thread-5-subject-identity--referential-integrity) | S: Patients/Clinicians → D: Identity Safety → G: Reliable Identity → O1. C: CST-EXT-002 bounds D. P: AX-06/14 govern REQ-FND-003. | No Assessment in this thread; no independent review of validation is assigned. | Explicit criteria and provenance for subject association. Operational validation is not an Assurance Praxis. |
| [T6 — Security/accountable provenance](../../01-motivation/orientation-view.md#thread-6-intrinsic-security-privacy--accountable-provenance) | S: Patients/Engineers → D: Privacy, bounded by CST-EXT-001 → A: Perimeter Trust / PHI Logging → P: AX-07. The diagram records AX-07 → AX-08. AX-07 governs Candidate SEC-001/002 and SEC-010; AX-08 governs Candidate PROV-001. O3 is the textually related intrinsic platform property. | **No Goal is manufactured. No causal axiom-to-O3 edge.** No accepted independent-assurance requirement follows from the Candidate catalogue. | Strongest compliance/evidence motivation. Meaningful accountable evidence enables review; it does not conduct or independently decide assurance. |
| [T7 — Coordinated activity/state](../../01-motivation/orientation-view.md#thread-7-coordinated-operational-activity--entity-state) | S: HDO/Engineers → D: Coordinated Activity/Entity State → A: Fan-Out Divergence → G: Coordinated Progression → O2. Local P: AX-16 governs accepted REQ-FND-002. | No independent assurance route/result. AX-16's central-register standing remains unresolved (§4). | Establishes sufficient progression/outcome information for operational determination. Operational accountability is not independent assurance coverage. |
| Directory branch — M02/M03/M06 | S: Directory Stewards/Clinicians → D: Directory/Endpoint Governance; documented G: Reliable Identity and P: AX-14. External HI Service/AHPRA references provide context. DIR-001..006 are downstream Candidates. | No directory-specific Assessment, foundational Requirement or distinct assurance Outcome is established. The associated Identity Goal's O1 link is not a separately documented directory-assurance handover. | Credentials/endpoints must be verified. REQ-FND-003 is not substituted for an independent directory-assurance obligation. |
| Operator concern / O3 — M02/M04 | Operator auditability/compliance concern; O3's demonstration/forensic-review value; AX-07/08 textual realisation relationship. Each is established evidence. | A complete Operator → Privacy Driver → new Goal → O3 → independent assurance Requirement chain is **not** documented. | Supports a valid bounded motive to evaluate evidence against obligations. Does not supply performer independence or governance-population coverage. |

### 3.3 Per-chain support for the requested A–F questions

In this table **Yes** means documented support for the stated, bounded facet; **Partial** means relevant motivation exists but does not establish the Dokimasia-specific meaning; **No** means the thread supplies no such relationship. These are evidence coverage observations, not the A–E classification codes in §5.

| Route | A: Need to determine satisfaction | B: Assurance activity rather than passive records | C: Performer independence | D: Information as evidence | E: Insufficient evidence/indeterminacy | F: Accountability distinct from execution |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| T1 | Partial: conformance/coherence | No | No | Partial: preserved semantics | No | Partial: boundary obligations |
| T2 | Partial: credibility/coherence | No | No | Yes: authority/credibility | No | Partial: information standing |
| T3 | Yes: establish durable acceptance | Partial: reconciliation/authoritative determination | No | Yes: facts establishing acceptance/effect | Yes: operational indeterminacy; assurance-specific test absent | Yes: distinguish acceptance responsibility from volatile execution |
| T4 | No assurance-specific need | No | No | Partial: separate observation/state domains | No assurance-specific rule | Partial: distinct state/access responsibilities |
| T5 | Yes: identifier obligations/validation | Partial: validation activity | No | Yes: identifier authority/provenance | Partial: ambiguous references remain explicit | Partial: validation is not separate oversight |
| T6 / O3 | Yes: demonstrate compliance | Partial: evidence enables forensic review | No | Yes: meaningful audit/provenance | No assurance-specific rule | Yes: accountable handling/demonstrability |
| T7 | Yes: determine progression/outcome | Partial: monitoring/audit/reconciliation | No | Yes: observable progression/outcome | Partial: destination indeterminacy; no assurance conclusion | Yes: observable accounting for performed activity |
| Directory branch | Yes: credentials/endpoint validity | Partial: verification/maintenance | No | Partial: authoritative registers | No assurance-specific rule | Partial: steward governance |
| Operator concern | Yes: prove compliance | Partial: forensic review | No | Yes: audit evidence | No assurance-specific rule | Yes: compliance demonstration |

“Yes” for operational indeterminacy in T3 does not turn REQ-FND-004 into an assurance qualification model. No reconstructed route establishes all six facets of independent governed assurance.

## 4. Axiom Assessment and Authority Limits

The central normative sections were inspected, including their rationale and consequences. An axiom that constrains an activity once established does not necessarily motivate the introduction of that activity.

| Axiom / authoritative source | Actual established meaning relevant to this investigation | Limit on Dokimasia derivation |
| :--- | :--- | :--- |
| AX-01–04, A01 | Health-information purpose, standards boundaries/native models and Harmonia-owned semantics versus engine machinery. | Context and derivation discipline; no independent assurance responsibility. An engine's usefulness does not establish placement. |
| AX-05, A01 lines 205–255; M05 lines 86–101 | Active use and authoritative durable establishment/version progression are distinct responsibilities (§6). | Neither performance/verification separation nor assurance decision authority follows from state separation. |
| AX-06, A01 lines 257–290; M05 §AX-06 | Authority/credibility is independent of technical state. Consequences state: “Information authority and assurance describe the standing of competing assertions.” “Policy determines what Harmonia does about them.” | Supports assessment meaning and standing/response distinction. It does not prescribe an independent assessor, a governed assurance workflow, criteria, lifecycle or distinct assurance result model. |
| AX-07, A01 lines 295–325; M05 §AX-07 | Every governed operation executes within an established security context and is subject to security policy; framework/platform enforcement cannot rely solely on voluntary caller behaviour. Themis owns security policy evaluation. | Any established assurance operation must be governed. Platform-enforced security is not performer-independent assurance, and Themis is not thereby its assessor. |
| AX-08, A01 lines 329–356; M05 §AX-08 | Meaningful provenance/audit describes significant events, assertions and decisions; a durable repository explains what happened and why. Kleio preserves durable evidence. | Explicit evidence motive, not assurance evaluation. Evidence preservation does not confer assessment or assurance authority. |
| AX-09, A01 lines 362–387; M05 §AX-09 | Operational state is ephemeral by default; meaningful information contributing to an evidentiary fact/decision is promoted to appropriate durable provenance/audit. | Not every observation/checkpoint becomes evidence. No assurance schema or wholesale workflow-state retention is derived. |
| AX-10/11, A01 | Failure-aware distributed operation and responsive managed-information access without weakening authority/security/governance guarantees. | Later assurance operations must preserve these guarantees; neither axiom establishes performer independence or deployment separation. |
| AX-12, central register and M05 reclassification record | Central “Hide Plumbing, Not Information” remains present; Domain01 marks it reclassified/transferred to Domain05. | Existing classification discrepancy, also recorded by G1. It does not establish independent assurance and is not repaired or used as a conflicting direction for an assurance requirement. |
| AX-13, A01 | Harmonia management has an explicit boundary; retained provenance/audit does not extend operational control beyond egress. | External evidence does not imply operational supervision. No external assurance-control arrangement is derived. |
| AX-14, A01 | Documented semantic distinctions must be preserved; technical convenience does not collapse them. | Preserves an established assurance/evidence distinction. It does not create the missing motivation for independent assurance or assign separate components. |
| AX-15, A01 lines 600–628; M05 §AX-15 | Uncertain state/outcome/effect of an operation remains explicit until governed evidence establishes it. Governing contract determines required certainty; technical failure alone does not determine semantic outcome. | Directly constrains uncertainty in an Assurance Praxis's own execution if it is a managed operation. It does not establish what subject evidence suffices to assure, or make an uncertain subject fact automatically an assurance-specific outcome. |
| AX-16, M05 lines 257–269 | Local documented principle: operational activity/entity state progress through explicit coordinated observable transitions. REQ-FND-002 has accepted catalogue status independently of this register question. | Central register has no AX-16 entry. Its central standing is unresolved; no independent assurance motivation or Dokimasia execution allocation follows from it. |
| AX-17, A01 lines 633–656; M05 links to it | Documented authority cannot be silently contradicted; absence cannot be filled by convention, lexical similarity or inference; candidates remain non-authoritative until accepted. | Requires this truthful gap/conflict report. Does not itself create the missing assurance obligation. |

The [existing G1 disposition](package2-g1-review.md#163-ax-16-investigation-and-disposition) records AX-16's unresolved central standing and insufficient approval evidence. Its documented Domain01 presence is not treated as central adoption. Neither AX-16 absence nor the AX-12 classification discrepancy invalidates the independently stated REQ-FND-002 or supplies performer independence. These matters remain separate from the R3 gap and the AX-05 contradiction.

## 5. Dokimasia Requirement Classifications

Classification tests motivation for each facet, not complete placement of the framework:

- **A — Explicitly Established:** Domain01 directly establishes the tested motivation.
- **B — Established by Valid Derivation:** explicit elements/relationships establish it without new meaning.
- **C — Partially Established:** relevant motivation exists but an important semantic requirement is missing.
- **D — Not Established:** no authoritative motivation for the requirement was found.
- **E — Conflicted:** Domain01 gives contradictory direction for that requirement.

| Requirement | Class | Established support / bounded valid derivation | Missing meaning or relationship |
| :--- | :--- | :--- | :--- |
| 1. Governed assurance | **B** | O3 and the Operator concern require demonstration of compliance, not only possession of records. Demonstrating compliance using meaningful evidence requires determining whether known obligations were met. AX-07 governs any operations undertaking that determination. This derives the **bounded need for governed evaluation** of compliance/operational facts. | Does not derive Dokimasia as a named subject-centric framework, independence, or universal assurance over every governable subject. Those are assessed separately. |
| 2. Assurance as activity | **C** | Forensic review, deterministic verification and governed reconciliation are actual activities enabled by evidence (M02, O3, REQ-FND-002/004). | No documented requirement/relationship establishes assurance itself as a separate governed workflow concerning its subject. Review activity is not a full Assurance Praxis derivation. |
| 3. Assurance independence | **D** | No inspected provision requires separation of performance/control from determination of assurance. | The performer must not determine, suppress, manufacture or retrospectively alter its own assurance outcome. Neither auditability, regulatory oversight, vendor independence, AX-05 nor AX-07 supplies that obligation. |
| 4. Evidence-based assessment | **B** | Operator compliance demonstration/forensic review and O3 explicitly use meaningful attributable evidence; REQ-FND-002 provides facts for determination, and AX-08/09 delimit evidence. Using those facts to establish compliance or what occurred is a valid bounded derivation. | Does not establish a complete Assurance Basis/Evidence/Assessment concept model, criteria-selection rule or assurance evidence schema. Evidence remains distinct from the assessment. |
| 5. Assurance outcome | **C** | Compliance demonstrability, operational determinations and AX-06 assertion standing provide related motivations. | A separately governed result of assurance evaluation, distinct from Operational/Business Outcome, is not established by a Domain01 chain. O3 and operational outcomes cannot be relabelled Assurance Outcomes. |
| 6. Inability to assure / insufficient evidence | **C** | REQ-FND-004 and AX-15 explicitly prohibit invented certainty about managed operations and allow governed evidential reconciliation. | No assurance-specific governing contract establishes evidence sufficiency or inability to conclude about the subject. Operational INDETERMINATE is not an approved assurance qualification. |
| 7. Accountability/coverage | **C** | Accountable handling and compliance demonstration are explicit in O3; REQ-FND-002 covers progression/outcome of managed multi-stage operational activities. | An obligation to assure the complete subject population within governance scope, including missing/insufficient evidence, is not established as an independent assurance responsibility in Domain01. Referral-specific G2 evidence coverage is not an upstream motivation link. |
| 8. Separation of assurance from operational management | **C** | AX-06 distinguishes assertion standing from policy response; accountability evidence has a purpose distinct from operational execution. | Domain01 does not exclude an assurance construct from assigning/delegating/escalating/remediating its subject's work, or distinguish assurance-internal workflow management from subject-work management. The approved QA/Foreman boundary is additional, not already derived. |

The B classifications establish motivation for bounded evaluation and use of evidence, not the whole approved Dokimasia construct. The C classifications are not hidden passes. The strongest direct A-level **subfacets** are accountable handling, observable operational facts and explicit operational uncertainty; the broader compound requirements retain their displayed classifications. No E classification is justified: the AX-05 inconsistency is between Strategy and the axiom, not contradictory Domain01 motivation for independent assurance.

For requirement 6, two different uncertainties must remain distinguishable. An assurance operation may have an uncertain execution effect, which AX-15 already constrains. An evaluation may complete successfully yet lack enough subject evidence to reach a conclusion. Domain01 has not established a contract for the latter. This explanatory distinction creates no new state, value set or outcome type.

Lexical counterexamples checked include the vendor/technology independence in Drivers/Goals, credibility independent of technical state in AX-06, and the OAIC's description as an independent statutory agency in External Authorities. None means operational independence of Harmonia assurance from its performer.

## 6. AX-05: Exact Definition, Copies and References

### 6.1 Canonical identity and normative wording

**Identifier: AX-05. Canonical title: Active State and Authoritative Durable State Are Distinct.**

The highest-authority definition is [central AX-05](../../../architectural-axioms.md#ax-05-----active-state-and-authoritative-durable-state-are-distinct), heading at line 205, Axiom block at lines 209–218. Its complete Axiom block, with source line wrapping normalised, is:

> Mneme owns Harmonia's application-facing access to managed information and the distributed active-state representation, observation and coordination required to use that information safely.
>
> Mnemosyne owns Harmonia's authoritative durable representation of managed information. It atomically establishes authoritative state and authoritative version progression and persists the durable management metadata required to interpret that state.
>
> Mneme manages active use; Mnemosyne establishes durable truth.

Its normative consequences further establish:

- Mneme MAY reject or coordinate a proposed state progression before persistence.
- Only Mnemosyne can establish a new authoritative durable state.
- Following authoritative commit, Mneme SHALL converge its active representation toward authoritative state.
- Loss of either boundary SHALL NOT promote active/process-local/cached state into authoritative durable state; active state is reconstructable where applicable.
- Mneme active-state generation and Mnemosyne authoritative version SHALL remain distinct concurrency domains. Failed/degraded convergence does not establish a valid active-state generation.

These distinguish proposing/coordinating a transition, establishing authoritative durable state/version, and executing operational workflow. They do not make persistence an application-facing interface or require a database operation for every active-state operation.

### 6.2 Duplicate formulation and aliases

[Domain01 AX-05](../../01-motivation/principles/architectural-axioms.md#ax-05-active-state-and-authoritative-durable-state-are-distinct), lines 86–101, uses the same identifier/title. Its complete Principle statement is:

> **Active operational state and authoritative durable state are distinct architectural responsibilities. Active operational state encompasses application-facing information access, distributed in-memory coordination, active observation, and transient workflow coordination. Authoritative durable state encompasses the permanent, durable record of truth, authoritative version progression, and persistent governance metadata. Active operational state manages active use; authoritative durable state establishes durable truth.**

This component-neutral formulation is consistent with central AX-05. Its implications reserve committed-version establishment to authoritative persistence; its Current Harmonia Realisation expressly assigns authoritative durable state/version progression to Mnemosyne alone. Different wording is not a competing decision. The central register governs where scope/detail differs.

Reference labels **State Separation**, **Active vs Durable State**, and **Active Coordination ≠ Authoritative State** occur in M01/M03/M06 and Strategy. They are shorthand references, not renamed canonical axioms or independent definitions. No inconsistent AX-05 definition was found in the inspected central/Domain01 axiom copies. The inconsistent material is the Strategy responsibility exclusion in §7.

Domain01 references include Availability Driver, False Acceptance and Persistence Contention Assessments, T3/T4, REQ-FND-001, the master catalogue and its retired write-behind entry. Strategy references include COA-02, EC-04, the course index/matrix and VS-02. General references to separation are compatible; their presence does not make every associated statement conformant.

[ADR-018](../../../architecture-decisions.md#adr-018-----mnemosyne-defines-the-authoritative-durable-state-boundary), Decision at lines 203–224, identifies Mnemosyne as the authoritative committed application-state boundary and distinguishes durable Petasos **work acceptance**. [ADR-019](../../../architecture-decisions.md#adr-019--mneme-owns-distributed-resource-access-and-coordination), lines 494–507, reserves final persisted version to Mnemosyne and states that active state is non-authoritative. C02 agrees with central AX-05. These sources corroborate the boundary without deriving assurance persistence. No inspected accepted decision authorises the contradictory Strategy exclusion.

## 7. Strategy / AX-05 Conflict Reconstructed from Source

### 7.1 Exact Strategy statements

**S1 — Component 2 clarification**, [S01, Component 2](../../02-strategy/strategic-views/logical-component-responsibilities.md#component-2-mnemosyne-durable-preservation--recovery), line 102:

> **Architectural Clarification**: Mnemosyne does **not** own authoritative state progression, authoritative version progression, "durable truth", or the progression of operational state. Ponos progresses operational activity; Mneme governs runtime information management. Mnemosyne preserves what has been committed and provides the recovery baseline.

**S2 — Seam 1 rule**, [S01, Seam 1](../../02-strategy/strategic-views/logical-component-responsibilities.md#seam-1-mneme--mnemosyne), line 348, relevant complete sentences:

> State managed through Mneme may represent durable business concepts. Mnemosyne does not own state progression or durable truth; Ponos progresses activity, Mneme manages runtime state, and Mnemosyne preserves it. Application tiers must never bypass Mneme to access Mnemosyne persistence stores directly.

**S3 — COA-02 Component Boundary Impact**, [S02, COA-02](../../02-strategy/courses-of-action/strategic-courses-of-action.md#coa-02-distinct-management-and-durable-preservation-of-information-and-state), line 61:

> **Component Boundary Impact**: Explicitly establishes the seam between **Mneme** (runtime management of information, context, and state) and **Mnemosyne** (durable preservation and recovery). Neither component subsumes the other; Mnemosyne does not own state progression or application-facing query interfaces.

### 7.2 Semantic comparison and classification

| Candidate | AX-05 statement and location | Strategy statement and location | Interpretation and conflict classification |
| :--- | :--- | :--- | :--- |
| S1a — authoritative state progression | A01 lines 213–218 and 233: Mnemosyne establishes authoritative state; only it establishes new authoritative durable state. M05 line 101 also reserves state/version progression to it. | S01 line 102: Mnemosyne “does **not** own authoritative state progression”. | **Direct contradiction.** AX-05 concerns establishing committed state, not running the business workflow. The explicit “authoritative” exclusion cannot be cured by interpreting all progression as workflow. |
| S1b — authoritative version progression | A01 lines 213–216: Mnemosyne atomically establishes “authoritative version progression”. | S01 line 102 denies “authoritative version progression”. | **Direct contradiction** concerning the same qualified responsibility. Version archiving alone is not authoritative version establishment. |
| S1c — durable truth | A01 line 218: “Mnemosyne establishes durable truth.” | S01 line 102 denies ownership of “durable truth”. | **Direct contradiction.** Preserving an already committed record does not erase responsibility for establishing authoritative durable truth. |
| S1d — operational progression | A01 assigns authoritative commit/version establishment and permits Mneme coordination; it does not assign operational workflow execution to Mnemosyne. | S01 line 102 excludes “progression of operational state”; line 106 excludes workflow progression/operational task execution and gives it to Ponos. | **Apparent but not real**, when this is workflow/task progression. The workflow exclusion is compatible. It does not justify S1a–c. |
| S2a — durable truth | A01 line 218, same explicit responsibility as above. | S01 line 348: “Mnemosyne does not own state progression or durable truth”. | **Direct contradiction** for durable truth. This is an additional occurrence, not merely the finding's summary. |
| S2b — unqualified state progression | A01 lines 230–235 distinguish proposed progression from authoritative establishment. | S01 line 348 denies unqualified “state progression”. | **Unresolved scope mismatch** in this phrase: compatible for operational proposal/workflow progression, contradictory for authoritative commit/version establishment. S2a independently establishes a real contradiction in the same sentence. |
| S3 — unqualified state progression | A01 lines 230–235 and M05 lines 93/101, same distinction. | S02 line 61: “Mnemosyne does not own state progression or application-facing query interfaces”. | **Unresolved scope mismatch** for progression. No qualifier limits it to operational workflow. Query-interface exclusion is compatible with AX-05 and remains so. Do not silently narrow the progression wording. |

This is not solely a terminology mismatch: S1's authoritative qualifiers and S1/S2's durable-truth denial are explicit. Whether the underlying cause is stale documentation, a derivation error, an unrecorded intended change or another cause remains **unresolved**. Repository history establishes wording changes, not an accepted architectural override. The existing higher authority remains controlling; no repair is performed here.

### 7.3 Compatible statements and limits of the finding

| Statement / location | Assessment against AX-05 |
| :--- | :--- |
| S01 lines 96–101: durable preservation, historical versions, immutability and recovery | Compatible positive responsibilities. Incomplete about authoritative establishment when read alone; not inherently a contradiction. |
| S01 line 74: Mneme manages information representing enduring business concepts | Compatible. Business longevity does not make active representation authoritative durable state. |
| S01 line 85: Mneme is authoritative for runtime access contracts/active-state presentation | Compatible within that stated scope. Runtime presentation authority is not durable commit authority. |
| S01 lines 106 and 309: Mnemosyne does not own operational workflow/task progression | Compatible operational responsibility exclusion. |
| S01 Seam 2, line 355: Ponos requests governed state transitions; Mneme reflects resulting state | Compatible as operational proposal/coordination. Does not replace authoritative commit responsibility. |
| S01 Seam 3, line 362: Ponos executes; Digital Twin coordinates for an entity | No direct AX-05 contradiction. Does not determine Dokimasia execution placement or independence. |
| S02 COA-02 summary/matrix and course index; S03 EC-04 line 130 and VS-02 line 143 AX-05 “State Separation” reference | Compatible general separation references; not complete authoritative-state responsibility definitions. Other shorthand labels in VS-02 are outside this investigation's decision scope. |

## 8. AX-05 Relevance to Dokimasia

| Concern | Established effect of AX-05 / conflict | What remains unestablished |
| :--- | :--- | :--- |
| Independent assurance | AX-05 separates active use from authoritative durable establishment. It neither supplies nor contradicts the approved performer-independent assurance rule. | Performance/verification separation needs its own truthful motivation and derivation. No independence is inferred from having two state responsibilities. |
| Assurance authority | Durable commit authority determines authoritative durable state/version. AX-06 separately governs the credibility/standing of the assertion being stored. | The responsibility permitted to determine an assurance conclusion is not assigned by AX-05. A committed assertion is not thereby a valid assurance conclusion. |
| Evidence | Later use of Harmonia-managed evidence/state must respect durable authority and reconstruction. AX-08's Kleio evidence-preservation responsibility remains intact. | This does not allocate all evidence or assurance information to a store, replace Kleio, or specify evidence sufficiency. |
| Workflow execution / Ponos | Requesting/effecting operational activity and authoritatively committing resulting state are distinct. Mnemosyne's commit responsibility does not make it a workflow engine. | AX-05 does not make Ponos the assurance engine. The execution relationship among Dokimasia, Assurance Praxis and Ponos remains unresolved. |
| Digital Twin | Entity-centred coordination can request managed-state progression without establishing durable truth itself. | State coordination similarity does not make Dokimasia a Twin/subtype. |
| Pragma/Praxis | Their later use must not equate observed/active progression with authoritative durable establishment or assurance. | AX-05 neither defines an Assurance Definition nor establishes Pragma equivalence or Assurance Praxis mechanics. No definitions change. |
| Operational/assurance separation | The Strategy contradiction would misdirect later derivation if copied into assurance state responsibilities. | AX-05 does not separate assessor and performer or forbid operational supervision on its own. The QA/Foreman boundary remains approved separately. |
| Downstream execution independence | Durable-state integrity is relevant to preventing retrospective alteration; it is not sufficient to prevent a performer determining/suppressing/manufacturing its own conclusion. | No separate database, process, cluster, executor, topology or technology is required by this investigation. Independent control must later be derived under the approved finding. |

**Distinction:** the contradiction is an existing state-authority defect discovered while investigating Dokimasia. It is relevant to future assurance-state derivation, but is not a conflict in independent-assurance motivation and is not the cause of its absence. Dokimasia is not responsible for solving that defect. AX-05 can constrain later design without providing the missing reason for independent assurance.

## 9. Recommendation and Next Architectural Decisions

**R3 — Domain01 Architectural Gap** is recommended for human architectural review. The gap is materially semantic: existing requirements do not establish independence between performing activity and determining its assurance, or the distinct assurance-workflow/operational-management boundary. Adding a diagram edge cannot truthfully derive those obligations from vendor independence, passive auditability or durable-state separation.

| Alternative | Assessment |
| :--- | :--- |
| R1 — No Domain01 Change Required | Not supported. Existing bounded verification/accountability motives do not establish the full approved responsibility. |
| R2 — Domain01 Clarification Required | Insufficient for the independence gap on current evidence. Some evidence/uncertainty relationships may need clarification, but the central missing obligation introduces materially new motivation rather than merely clarifying an existing edge. |
| **R3 — Domain01 Architectural Gap** | **Recommended.** Explicitly review/reopen Domain01 before attempting Strategy derivation of independent assurance. The approved finding remains valid as a captured boundary while its upstream motivation is reviewed. |
| R4 — Existing Conflict Must Be Resolved First | Not the appropriate motivation conclusion. Domain01 and central AX-05 agree on the relevant state boundary; a downstream contradiction does not prevent identifying the motivation gap. Resolve the Strategy conflict before relying on its disputed state-responsibility statements. |

Recommended sequence, requiring subsequent architectural authorisation rather than action in this task:

1. Review whether Domain01 should explicitly establish the newly approved independent governed assurance need and its accountability/operational-management limits. Determine the proper motivational elements and relationships without assuming a new Driver, axiom or particular requirement is necessary. No such element is drafted here.
2. Conduct a separately scoped Strategy/AX-05 consistency review of S1–S3 against the central register, Domain01 and accepted ADRs. Determine the approved correction or other architectural disposition. This can be reviewed alongside the Domain01 gap; disputed statements must not become a downstream derivation basis.
3. After the required upstream decisions, derive Strategy assurance responsibility from approved motivation. Do not preassign an Enterprise Capability or make evidence preservation equivalent to assurance coordination.
4. Subsequently derive Business assurance activity and information responsibilities, then Domain04 semantics. Keep subject evidence, governed evaluation, assurance result and operational response distinct. Preserve G2-D04 and defer assurance-specific workflow representation/execution questions until supported.

The next decision is whether to authorise an explicit Domain01 architectural review of the gap. A separate AX-05 consistency review is also warranted. Neither review, reopening nor reconciliation is executed by this investigation.

## 10. Unresolved Matters and Deferred Work

| Matter | Status / next evidence or decision needed |
| :--- | :--- |
| Proper Domain01 expression of independent assurance | **Gap established; remedy unresolved.** Human review must decide the motivational requirement and relationships. No new element or normative wording is proposed here. |
| Any existing deployment-specific independent-review obligation | **Unestablished by inspected Domain01 material.** A specific applicable obligation and an approved traceability relationship would be needed; external-regulator independence is insufficient. |
| Assurance as its own governed workflow and QA/operational-response separation | **Approved finding; upstream derivation incomplete.** Scope/form of motivational and subsequent Business derivation requires review. |
| Assurance conclusion/evidence sufficiency/qualification and governance-population coverage | **Partially motivated; not formalised.** Operational determinations, O3 and REQ-FND-004 do not settle these assurance meanings. No taxonomy or concept model is created. |
| Cause and approved resolution of S1/S2 contradiction; intended scope of S2/S3 progression exclusions | **Unresolved.** Need explicit architectural disposition; no inspected accepted override was found. |
| AX-16 central-register standing and AX-12 classification discrepancy | **Existing unresolved consistency matters.** Retained without repair; neither supplies independence or blocks this bounded assessment. |
| Dokimasia execution relationship with Ponos; Pragma/Praxis formalisation | **Unresolved and deferred.** No engine allocation or equivalence is inferred from AX-05 or operational Strategy. |
| Assurance information representation, custody and downstream control/deployment | **Downstream/deferred.** Subject of Assurance, Assurance Basis/Criteria, Evidence, Assessment, Finding, Exception, Outcome, qualification and assurance provenance remain working candidate vocabulary from the approved finding, not newly promoted concepts. |

## 11. Verification and Preservation

The [completion report](../../../../.junie/reports/2026-10-08-dokimasia-r1-motivation-traceability-ax05.md#6-verification-results) records reproducible verification details. Verification establishes preservation and conformance of the documentation change; architecture tests do not prove motivation sufficiency or approve reconciliation.

- **PASS — required architecture suite:** bounded Maven architecture invocation completed with BUILD SUCCESS in 18.228 seconds; 90 tests in 11 suites, zero failures, errors or skipped tests. No additional tests or implementation were introduced.
- **PASS — preservation:** all 2,257 pre-existing tracked/untracked files retain their baseline SHA-256 content; the incoming git index is unchanged. The approved finding, central axioms, Domain01–04 architecture and incoming G2 work are untouched.
- **PASS — scope:** exactly two new artefacts: this review and the dated completion report. No authoritative architecture is modified.
- **PASS — documentation validation:** task-local file/anchor links, source quotations, requirement classifications, relationship boundaries, whitespace and internal-link regression checked. No new broken link or broken task link was introduced; seven existing historical Junie link defects remain unchanged.
